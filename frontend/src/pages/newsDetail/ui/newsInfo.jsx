import React from 'react';
import CategoryBadge from '../../../shared/categoryBadge';

const NewsInfo = ({ category, title, date, publisher }) => {
  return (
    <div className="mb-6">
      <CategoryBadge category={category} />
      <h1 className="text-[36px] font-bold mt-2 mb-2">{title}</h1>
      <div className="text-[#A0A0A0] text-sm mb-2">{publisher}, 작성일: {date}</div>
    </div>
  );
};

export default NewsInfo;