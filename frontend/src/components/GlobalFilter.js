import React from 'react';

function GlobalFilter({ filter, setFilter }) {
  return (
    <div className="global-filter">
      <span>Search: </span>
      <input
        value={filter || ''}
        onChange={(e) => setFilter(e.target.value)}
        placeholder="Type to search"
      />
    </div>
  );
}

export default GlobalFilter;
