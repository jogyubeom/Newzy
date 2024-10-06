import React from 'react';

const CategoryButtons = ({ selectedCategory, onCategoryClick }) => {
  const categories = ['전체', '경제', '사회', '세계'];

  return (
    <div className="flex space-x-12 pt-4">
      {categories.map((category) => (
        <button
          key={category}
          onClick={() => onCategoryClick(category)}
          className={`text-lg hover:text-gray-200 ${
            selectedCategory === category ? 'font-bold underline underline-offset-8' : ''
          }`}
        >
          {category}
        </button>
      ))}
    </div>
  );
};

export default CategoryButtons;