import React from 'react';
import CategoryBadge from '../../../shared/categoryBadge';

const categories = [
  { id: 0, label: '시사' },
  { id: 1, label: '문화' },
  { id: 2, label: '자유' }
];

const CategorySelector = ({ category, onCategoryChange }) => {
  return (
    <div className="flex items-center gap-4">
      <span className="text-[#3D3D3D] text-[24px] leading-[20px] font-bold">카테고리 :</span>
      {categories.map(({ id, label }) => (
        <label key={id} className="flex items-center">
          <CategoryBadge category={label} />
          <input
            type="radio"
            name="category"
            value={id}
            checked={category === id}
            onChange={() => onCategoryChange(id)}
            className="ml-2"
          />
        </label>
      ))}
    </div>
  );
};

export default CategorySelector;