import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { FiUser, FiMail, FiLock } from 'react-icons/fi';

function extractErrorMessage(err) {
  if (!err) return 'Registration failed. Please try again.';

  const debug = {
    hasResponse: !!err.response,
    status: err.response?.status,
    statusText: err.response?.statusText,
    data: err.response?.data,
    dataType: typeof err.response?.data,
    dataJSON: (() => { try { return JSON.stringify(err.response?.data); } catch(e) { return String(err.response?.data); } })(),
    message: err.message,
    code: err.code,
    name: err.name,
    isAxiosError: err.isAxiosError,
  };
  console.log('[Register] Debug info:', debug);
  window.__lastRegisterError = debug;

  if (err.response?.data?.message) return err.response.data.message;
  if (err.response?.data?.error) return err.response.data.error;
  if (typeof err.response?.data === 'string') return err.response.data;
  if (err.response?.data) {
    const map = err.response.data;
    const fieldErrors = Object.values(map).filter(v => typeof v === 'string');
    if (fieldErrors.length > 0) return fieldErrors.join('. ');
  }
  if (err.response?.status === 500 && !err.response?.data) return 'Backend server error (500) - check backend logs';
  if (err.response?.status === 400) return `Bad request (400) - ${JSON.stringify(err.response.data)}`;
  if (err.response?.status === 403) return 'Access denied (403) - CORS may be blocking';
  if (err.response?.status === 404) return 'API endpoint not found (404)';
  if (err.message === 'Network Error') return 'Network error - backend unreachable. Check: (1) minikube tunnel running? (2) backend pod ready? (3) nginx proxy correct?';
  if (err.code === 'ECONNABORTED') return 'Request timed out after 30s. Backend may be overloaded.';
  return `Error (${err.response?.status || 'unknown'}): ${err.message || 'No details available'}`;
}

export default function Register() {
  const { register } = useAuth();
  const navigate = useNavigate();
  const [form, setForm] = useState({ username: '', email: '', password: '' });
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSuccess('');
    setLoading(true);
    try {
      await register(form);
      setSuccess('Account created successfully! Redirecting to login...');
      setTimeout(() => navigate('/login'), 1500);
    } catch (err) {
      console.error('[Register] Error:', err);
      const msg = extractErrorMessage(err);
      setError(msg);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-surface-50 via-primary-50/30 to-surface-50 dark:from-surface-950 dark:via-primary-950/10 dark:to-surface-950 p-4">
      <div className="w-full max-w-md card p-8 animate-scale-in">
        <div className="text-center mb-8">
          <div className="mx-auto h-14 w-14 rounded-2xl bg-primary-600 flex items-center justify-center mb-4">
            <span className="text-2xl font-bold text-white">SP</span>
          </div>
          <h1 className="text-2xl font-bold text-surface-900 dark:text-white">Create account</h1>
          <p className="text-sm text-surface-500 dark:text-surface-400 mt-1">Get started with Smart Parking</p>
        </div>

        {error && (
          <div className="mb-4 p-3 rounded-lg bg-red-50 dark:bg-red-900/20 border border-red-200 dark:border-red-800">
            <p className="text-sm text-red-600 dark:text-red-400">{error}</p>
          </div>
        )}

        {success && (
          <div className="mb-4 p-3 rounded-lg bg-emerald-50 dark:bg-emerald-900/20 border border-emerald-200 dark:border-emerald-800">
            <p className="text-sm text-emerald-600 dark:text-emerald-400">{success}</p>
          </div>
        )}

        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="label">Username</label>
            <div className="relative">
              <FiUser className="absolute left-3 top-1/2 -translate-y-1/2 h-4 w-4 text-surface-400" />
              <input type="text" value={form.username} onChange={e => setForm({ ...form, username: e.target.value })} className="input-field pl-9" placeholder="Choose a username" required />
            </div>
          </div>

          <div>
            <label className="label">Email</label>
            <div className="relative">
              <FiMail className="absolute left-3 top-1/2 -translate-y-1/2 h-4 w-4 text-surface-400" />
              <input type="email" value={form.email} onChange={e => setForm({ ...form, email: e.target.value })} className="input-field pl-9" placeholder="Enter your email" required />
            </div>
          </div>

          <div>
            <label className="label">Password</label>
            <div className="relative">
              <FiLock className="absolute left-3 top-1/2 -translate-y-1/2 h-4 w-4 text-surface-400" />
              <input type="password" value={form.password} onChange={e => setForm({ ...form, password: e.target.value })} className="input-field pl-9" placeholder="Min. 6 characters" minLength={6} required />
            </div>
          </div>

          <button type="submit" className="btn-primary w-full" disabled={loading}>
            {loading ? (
              <span className="flex items-center gap-2">
                <div className="h-4 w-4 border-2 border-white border-t-transparent rounded-full animate-spin" />
                Creating account...
              </span>
            ) : (
              'Create Account'
            )}
          </button>
        </form>

        <p className="text-center text-sm text-surface-500 dark:text-surface-400 mt-6">
          Already have an account?{' '}
          <Link to="/login" className="text-primary-600 hover:text-primary-700 font-medium">Sign in</Link>
        </p>
      </div>
    </div>
  );
}
