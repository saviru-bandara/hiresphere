import axios from 'axios';
import { fetchAuthSession } from 'aws-amplify/auth';

const api = axios.create({
  baseURL: process.env.REACT_APP_API_URL ?? 'http://localhost:8080/api',
  timeout: 15000,
});

// Attach Cognito JWT to every request automatically
api.interceptors.request.use(async (config) => {
  try {
    const session = await fetchAuthSession();
    const token   = session.tokens?.accessToken?.toString();
    if (token) config.headers.Authorization = `Bearer ${token}`;
  } catch {
    // Not authenticated — let the request go through (gateway will 401)
  }
  return config;
});

// ── Service-specific clients ───────────────────────────────────────────────
export const profileApi = {
  getMe:            ()       => api.get('/profiles/me'),
  getProfile:       (id)     => api.get(`/profiles/${id}`),
  createProfile:    (data)   => api.post('/profiles', data),
  updateProfile:    (data)   => api.put('/profiles/me', data),
};

export const searchApi = {
  searchInterviewers: (params) => api.get('/search/interviewers', { params }),
  getInterviewer:     (id)     => api.get(`/search/interviewers/${id}`),
};

export const bookingApi = {
  createPaymentIntent: (data)      => api.post('/bookings/payment-intent', data),
  confirmBooking:      (data)      => api.post('/bookings/confirm', data),
  getMyBookings:       ()          => api.get('/bookings/my'),
  getBooking:          (id)        => api.get(`/bookings/${id}`),
  cancelBooking:       (id)        => api.delete(`/bookings/${id}`),
};

export const submissionApi = {
  getUploadUrl:       (data) => api.post('/submissions/upload-url', data),
  submitGithubUrl:    (data) => api.post('/submissions/github', data),
  getMySubmissions:   ()     => api.get('/submissions/my'),
  getSubmission:      (id)   => api.get(`/submissions/${id}`),
};

export const messagingApi = {
  sendMessage:        (data)          => api.post('/messages/send', data),
  getConversation:    (otherUserId)   => api.get(`/messages/conversation/${otherUserId}`),
};

export default api;
