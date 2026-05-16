-- V1__create_submissions_table.sql
CREATE TABLE IF NOT EXISTS submissions (
    id                          VARCHAR(36)   PRIMARY KEY,
    candidate_id                VARCHAR(255)  NOT NULL,
    booking_id                  VARCHAR(36)   NOT NULL,
    type                        VARCHAR(20)   NOT NULL,
    s3_key                      TEXT,
    github_url                  TEXT,
    github_analysis             TEXT,
    status                      VARCHAR(20)   NOT NULL DEFAULT 'PENDING',
    evaluation_report_s3_key    TEXT,
    evaluator_feedback          TEXT,
    score                       INTEGER,
    created_at                  TIMESTAMPTZ   NOT NULL DEFAULT NOW(),
    updated_at                  TIMESTAMPTZ   NOT NULL DEFAULT NOW()
);
CREATE INDEX idx_submissions_candidate ON submissions(candidate_id);
CREATE INDEX idx_submissions_booking   ON submissions(booking_id);
CREATE INDEX idx_submissions_status    ON submissions(status);
