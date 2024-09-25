import React from 'react';

const CategoryBadge = ({ category }) => {
  let backgroundColor;

  switch (category) {
    case '시사':
      backgroundColor = '#A898FF';
      break;
    case '문화':
      backgroundColor = '#FAB7BB';
      break;
    case '자유':
      backgroundColor = '#FAF4B7';
      break;
    default:
      backgroundColor = '#E0E0E0'; // 기본 배경 색상
  }

  return (
    <div
      className="w-[80px] h-[34px] rounded-full flex items-center justify-center"
      style={{ backgroundColor }}
    >
      <span className="text-[#3D3D3D] text-[20px] leading-[24px] font-bold">
        {category}
      </span>
    </div>
  );
};

export default CategoryBadge;
