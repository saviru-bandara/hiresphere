import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { searchApi } from '../services/api';

export default function SearchPage() {
  const navigate = useNavigate();
  const [interviewers, setInterviewers] = useState([]);
  const [loading, setLoading]           = useState(false);
  const [filters, setFilters]           = useState({
    domain: '', interviewType: '', minExperience: '', minRating: '', maxPrice: ''
  });

  const search = async () => {
    setLoading(true);
    try {
      const params = Object.fromEntries(
        Object.entries(filters).filter(([, v]) => v !== '')
      );
      const { data } = await searchApi.searchInterviewers(params);
      setInterviewers(data);
    } catch (err) {
      console.error('Search failed:', err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { search(); }, []); // Load all on mount

  const handleFilter = (e) => {
    setFilters(prev => ({ ...prev, [e.target.name]: e.target.value }));
  };

  return (
    <div style={{ padding: 24, maxWidth: 900, margin: '0 auto' }}>
      <h1>Find an Interviewer</h1>

      {/* Filter bar */}
      <div style={{ display: 'flex', gap: 12, flexWrap: 'wrap', marginBottom: 24 }}>
        <select name="domain" onChange={handleFilter} value={filters.domain}>
          <option value="">All domains</option>
          <option value="Backend">Backend</option>
          <option value="Frontend">Frontend</option>
          <option value="DevOps">DevOps</option>
          <option value="System Design">System Design</option>
        </select>
        <select name="interviewType" onChange={handleFilter} value={filters.interviewType}>
          <option value="">All interview types</option>
          <option value="DATA_STRUCTURES">Data Structures</option>
          <option value="SYSTEM_DESIGN">System Design</option>
          <option value="BEHAVIORAL">Behavioral</option>
          <option value="FULLSTACK">Fullstack</option>
        </select>
        <input name="minExperience" type="number" placeholder="Min years exp"
               onChange={handleFilter} value={filters.minExperience} style={{ width: 130 }} />
        <input name="minRating" type="number" step="0.1" max="5" placeholder="Min rating"
               onChange={handleFilter} value={filters.minRating} style={{ width: 100 }} />
        <input name="maxPrice" type="number" placeholder="Max price/hr"
               onChange={handleFilter} value={filters.maxPrice} style={{ width: 120 }} />
        <button onClick={search} disabled={loading}>
          {loading ? 'Searching...' : 'Search'}
        </button>
      </div>

      {/* Results */}
      <div style={{ display: 'grid', gap: 16 }}>
        {interviewers.map(iv => (
          <div key={iv.userId} style={{ border: '1px solid #ddd', borderRadius: 8, padding: 16 }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
              <div>
                <h3 style={{ margin: 0 }}>{iv.name}</h3>
                <p style={{ margin: '4px 0', color: '#666' }}>{iv.bio}</p>
                <div style={{ fontSize: 13, color: '#888' }}>
                  {iv.domains?.join(' · ')} · {iv.yearsExperience} yrs · ⭐ {iv.rating?.toFixed(1)} ({iv.totalReviews} reviews)
                </div>
              </div>
              <div style={{ textAlign: 'right' }}>
                <div style={{ fontSize: 18, fontWeight: 'bold' }}>${iv.pricePerHour}/hr</div>
                <button onClick={() => navigate(`/book/${iv.userId}`)}
                        style={{ marginTop: 8, background: '#2E75B6', color: '#fff', border: 'none',
                                 borderRadius: 6, padding: '8px 16px', cursor: 'pointer' }}>
                  Book Interview
                </button>
              </div>
            </div>
          </div>
        ))}
        {!loading && interviewers.length === 0 && (
          <p style={{ color: '#888' }}>No interviewers found. Try adjusting your filters.</p>
        )}
      </div>
    </div>
  );
}
