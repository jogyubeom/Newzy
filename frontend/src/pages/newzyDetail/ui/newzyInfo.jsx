import React from 'react';
import CategoryBadge from '../../../shared/categoryBadge';

const NewzyInfo = ({ category, title, date, author }) => {
  return (
    <div className="mb-6">
      <CategoryBadge category={category} />
      <h1 className="text-[36px] font-bold mt-2 mb-2">{title}</h1>
      <div className="text-[#A0A0A0] text-sm mb-2">작성일: {date}</div>
      <div className="flex items-center mb-6">
        <span className="text-[#3D3D3D] text-lg mr-4">{author}</span>
        <button className="px-4 py-2 text-[#333] bg-[#F0F0F0] rounded-md text-sm">
          구독 +
        </button>
      </div>
    </div>
  );
};

export default NewzyInfo;