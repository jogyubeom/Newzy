import React from 'react';

const Pagination = ({ currentPage, totalPages, onPageChange }) => {
  const getPaginationButtons = () => {
    const buttons = [];
    const maxButtons = 5;

    let startPage = Math.max(currentPage - Math.floor(maxButtons / 2), 1);
    let endPage = Math.min(startPage + maxButtons - 1, totalPages);

    if (endPage - startPage < maxButtons - 1) {
      startPage = Math.max(endPage - (maxButtons - 1), 1);
    }

    for (let i = startPage; i <= endPage; i++) {
      buttons.push(
        <button
          key={i}
          className={`mx-1 px-3 py-1 min-w-[45px] rounded-md ${currentPage === i ? 'bg-purple-500 text-white' : 'bg-gray-200 text-gray-700'}`}
          onClick={() => onPageChange(i)}
        >
          {i}
        </button>
      );
    }
    return buttons;
  };

  const handlePrevious = () => {
    const newPage = Math.max(currentPage - 5, 1);
    onPageChange(newPage);
  };

  const handleNext = () => {
    const newPage = Math.min(currentPage + 5, totalPages);
    onPageChange(newPage);
  };

  return (
    <div className="flex justify-center py-4">
      <button
        className={`mx-1 px-3 py-1 rounded-md ${currentPage === 1 ? 'bg-gray-300 text-gray-500' : 'bg-gray-200 text-gray-700'}`}
        onClick={handlePrevious}
        disabled={currentPage === 1}
      >
        이전
      </button>

      {getPaginationButtons()}

      <button
        className={`mx-1 px-3 py-1 rounded-md ${currentPage === totalPages ? 'bg-gray-300 text-gray-500' : 'bg-gray-200 text-gray-700'}`}
        onClick={handleNext}
        disabled={currentPage === totalPages}
      >
        다음
      </button>
    </div>
  );
};

export default Pagination;
