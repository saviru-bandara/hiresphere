'use strict';

/**
 * HireSphere — GitHub Analysis Lambda
 *
 * Trigger : AWS SQS (hiresphere-github-analysis queue)
 * Purpose : Analyses a candidate's GitHub repository and posts
 *           results back to the Submission Service webhook.
 *
 * SQS Message schema:
 *   { submissionId, githubUrl, candidateId }
 */

const { Octokit } = require('@octokit/rest');
const axios = require('axios');

const GITHUB_TOKEN         = process.env.GITHUB_TOKEN;
const SUBMISSION_WEBHOOK   = process.env.SUBMISSION_SERVICE_WEBHOOK_URL;
// e.g. http://submission-service.hiresphere.svc.cluster.local:8084/submissions/webhook/analysis-complete

exports.handler = async (event) => {
  console.log(`Processing ${event.Records.length} SQS record(s)`);

  const results = await Promise.allSettled(
    event.Records.map(record => processRecord(record))
  );

  // Report failures so SQS can retry or DLQ them
  const failures = results
    .map((r, i) => ({ result: r, messageId: event.Records[i].messageId }))
    .filter(({ result }) => result.status === 'rejected');

  if (failures.length > 0) {
    console.error('Failed records:', failures.map(f => f.messageId));
    return {
      batchItemFailures: failures.map(f => ({ itemIdentifier: f.messageId }))
    };
  }

  return { batchItemFailures: [] };
};

async function processRecord(record) {
  const { submissionId, githubUrl, candidateId } = JSON.parse(record.body);
  console.log(`Analysing repo for submissionId=${submissionId}, url=${githubUrl}`);

  const { owner, repo } = parseGithubUrl(githubUrl);
  const octokit = new Octokit({ auth: GITHUB_TOKEN });

  const [repoData, languages, commits, contributors] = await Promise.all([
    octokit.repos.get({ owner, repo }),
    octokit.repos.listLanguages({ owner, repo }),
    octokit.repos.listCommits({ owner, repo, per_page: 30 }),
    octokit.repos.listContributors({ owner, repo }),
  ]);

  const analysis = {
    repoName:         repoData.data.full_name,
    description:      repoData.data.description,
    stars:            repoData.data.stargazers_count,
    forks:            repoData.data.forks_count,
    openIssues:       repoData.data.open_issues_count,
    defaultBranch:    repoData.data.default_branch,
    languages:        languages.data,
    primaryLanguage:  getPrimaryLanguage(languages.data),
    totalCommits:     commits.data.length,    // approximate (last 30)
    contributors:     contributors.data.length,
    lastCommitDate:   commits.data[0]?.commit?.author?.date ?? null,
    commitFrequency:  analyseCommitFrequency(commits.data),
    hasReadme:        repoData.data.has_projects,
    topics:           repoData.data.topics ?? [],
    analysedAt:       new Date().toISOString(),
  };

  console.log('Analysis complete:', JSON.stringify(analysis, null, 2));

  // Post results back to Submission Service
  await axios.post(SUBMISSION_WEBHOOK, {
    submissionId,
    analysis: JSON.stringify(analysis),
  }, {
    timeout: 10_000,
    headers: { 'Content-Type': 'application/json' },
  });

  console.log(`Posted analysis results for submissionId=${submissionId}`);
}

function parseGithubUrl(url) {
  // Handles: https://github.com/owner/repo or https://github.com/owner/repo.git
  const match = url.match(/github\.com\/([^/]+)\/([^/]+?)(?:\.git)?(?:\/|$)/);
  if (!match) throw new Error(`Invalid GitHub URL: ${url}`);
  return { owner: match[1], repo: match[2] };
}

function getPrimaryLanguage(languages) {
  return Object.entries(languages)
    .sort(([, a], [, b]) => b - a)[0]?.[0] ?? 'Unknown';
}

function analyseCommitFrequency(commits) {
  if (commits.length < 2) return 'insufficient-data';
  const dates = commits.map(c => new Date(c.commit.author.date)).sort((a, b) => b - a);
  const daySpan = (dates[0] - dates[dates.length - 1]) / (1000 * 60 * 60 * 24);
  const perWeek = (commits.length / daySpan) * 7;
  if (perWeek >= 5)  return 'high';
  if (perWeek >= 1)  return 'medium';
  return 'low';
}
