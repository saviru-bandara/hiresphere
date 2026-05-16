'use strict';

/**
 * HireSphere — Email Notification Lambda
 *
 * Trigger : AWS SQS (hiresphere-notification-delivery + hiresphere-booking-events)
 * Purpose : Sends transactional emails via AWS SES for:
 *             - BOOKING_CONFIRMED : confirmation to candidate + interviewer
 *             - NEW_MESSAGE       : email nudge to offline recipient
 *
 * Environment variables:
 *   SES_FROM_ADDRESS   — verified SES sender email
 *   AWS_REGION         — AWS region
 */

const { SESClient, SendEmailCommand } = require('@aws-sdk/client-ses');

const ses    = new SESClient({ region: process.env.AWS_REGION ?? 'us-east-1' });
const FROM   = process.env.SES_FROM_ADDRESS ?? 'noreply@hiresphere.io';

exports.handler = async (event) => {
  const failures = [];

  for (const record of event.Records) {
    try {
      const payload = JSON.parse(record.body);
      await routeEvent(payload);
    } catch (err) {
      console.error(`Failed to process messageId=${record.messageId}:`, err);
      failures.push({ itemIdentifier: record.messageId });
    }
  }

  return { batchItemFailures: failures };
};

async function routeEvent(payload) {
  switch (payload.eventType) {
    case 'BOOKING_CONFIRMED':
      await sendBookingConfirmation(payload);
      break;
    case 'NEW_MESSAGE':
      await sendMessageNotification(payload);
      break;
    default:
      console.warn(`Unknown event type: ${payload.eventType}`);
  }
}

async function sendBookingConfirmation({ bookingId, candidateId, interviewerId, startTime, amount }) {
  console.log(`Sending booking confirmation for bookingId=${bookingId}`);

  // In production: look up emails from Profile Service or pass them in the event
  const candidateEmail   = `${candidateId}@placeholder.com`;
  const interviewerEmail = `${interviewerId}@placeholder.com`;

  const startFormatted = new Date(startTime).toLocaleString('en-US', {
    weekday: 'long', year: 'numeric', month: 'long', day: 'numeric',
    hour: '2-digit', minute: '2-digit', timeZone: 'UTC',
  });

  await sendEmail(candidateEmail, 'Your HireSphere Interview is Confirmed!',
    `Your mock interview has been confirmed.\n\nBooking ID : ${bookingId}\nDate/Time  : ${startFormatted} UTC\nAmount paid: $${amount}\n\nGood luck!\n— HireSphere`
  );

  await sendEmail(interviewerEmail, 'New Interview Booking',
    `You have a new interview booking.\n\nBooking ID : ${bookingId}\nDate/Time  : ${startFormatted} UTC\n\nPlease be available at the scheduled time.\n— HireSphere`
  );
}

async function sendMessageNotification({ messageId, senderId, recipientId, preview }) {
  console.log(`Sending message notification for messageId=${messageId}`);
  const recipientEmail = `${recipientId}@placeholder.com`;

  await sendEmail(recipientEmail, `New message from ${senderId} on HireSphere`,
    `You have a new message from ${senderId}:\n\n"${preview}..."\n\nLog in to HireSphere to reply.\n— HireSphere`
  );
}

async function sendEmail(to, subject, body) {
  await ses.send(new SendEmailCommand({
    Source: FROM,
    Destination: { ToAddresses: [to] },
    Message: {
      Subject: { Data: subject, Charset: 'UTF-8' },
      Body:    { Text: { Data: body, Charset: 'UTF-8' } },
    },
  }));
  console.log(`Email sent to ${to}: ${subject}`);
}
