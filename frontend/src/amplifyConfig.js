import { Amplify } from 'aws-amplify';

Amplify.configure({
  Auth: {
    Cognito: {
      userPoolId:       process.env.REACT_APP_COGNITO_POOL_ID,
      userPoolClientId: process.env.REACT_APP_COGNITO_CLIENT_ID,
      region:           process.env.REACT_APP_COGNITO_REGION ?? 'us-east-1',
      loginWith: {
        email: true,
      },
    },
  },
  Storage: {
    S3: {
      bucket: process.env.REACT_APP_S3_BUCKET,
      region: process.env.REACT_APP_COGNITO_REGION ?? 'us-east-1',
    },
  },
});
