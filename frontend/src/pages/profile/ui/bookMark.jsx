import React, { useState } from 'react';
import BookmarkNews from './bookMarkNews';
import BookmarkNewzy from './bookMarkNewzy';

const BookMark = () => {
  const [showNews, setShowNews] = useState(true);

  const toggleComponent = () => {
    setShowNews((prev) => !prev);
  };

  return (
    <div className="mx-20">
      <h1 className="text-2xl font-bold mb-4">Bookmark Page</h1>
      <button 
        onClick={toggleComponent} 
        className="mb-4 px-4 py-2 bg-blue-500 text-white rounded">
        {showNews ? "Newzy" : "News"}
      </button>
      {showNews ? <BookmarkNews /> : <BookmarkNewzy />}
    </div>
  );
};

export default BookMark;