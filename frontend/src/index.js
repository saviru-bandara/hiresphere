import React from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { Authenticator } from '@aws-amplify/ui-react';
import '@aws-amplify/ui-react/styles.css';
import './amplifyConfig';

import SearchPage     from './pages/SearchPage';
import BookingPage    from './pages/BookingPage';
import ProfilePage    from './pages/ProfilePage';
import SubmissionPage from './pages/SubmissionPage';
import MessagingPage  from './pages/MessagingPage';
import SessionPage    from './pages/SessionPage';
import DashboardPage  from './pages/DashboardPage';

export default function Index() {
  return (
    // Authenticator wraps entire app — handles Cognito sign-up/sign-in
    <Authenticator>
      {({ signOut, user }) => (
        <BrowserRouter>
          <Routes>
            <Route path="/"             element={<DashboardPage  user={user} signOut={signOut} />} />
            <Route path="/search"       element={<SearchPage     user={user} />} />
            <Route path="/book/:userId" element={<BookingPage    user={user} />} />
            <Route path="/profile"      element={<ProfilePage    user={user} />} />
            <Route path="/submissions"  element={<SubmissionPage user={user} />} />
            <Route path="/messages"     element={<MessagingPage  user={user} />} />
            <Route path="/session/:bookingId" element={<SessionPage user={user} />} />
            <Route path="*"             element={<Navigate to="/" replace />} />
          </Routes>
        </BrowserRouter>
      )}
    </Authenticator>
  );
}
