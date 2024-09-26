import React from 'react';
import CategoryBadge from '../../../shared/categoryBadge';

const categories = ['시사', '문화', '자유'];

const CategorySelector = ({ category, onCategoryChange }) => {
  return (
    <div className="flex items-center gap-4">
      <span className="text-[#3D3D3D] text-[24px] leading-[20px] font-bold">카테고리 :</span>
      {categories.map((cat) => (
        <label key={cat} className="flex items-center">
          <CategoryBadge category={cat} />
          <input
            type="radio"
            name="category"
            value={cat}
            checked={category === cat}
            onChange={() => onCategoryChange(cat)}
            className="ml-2"
          />
        </label>
      ))}
    </div>
  );
};

export default CategorySelector;