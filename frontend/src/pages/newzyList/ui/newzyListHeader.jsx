import React from 'react';
import CategoryButtons from './categoryButtons';

const NewzyListHeader = ({ selectedCategory, onCategoryClick }) => {
  return (
    <div className="bg-purple-900 text-white flex flex-col items-center py-8">
        <div className="text-9xl font-bold mb-12 relative">
          Newzy
          <div className="text-2xl font-bold absolute bottom-[-30px] right-[80px]">
            by Newporter
          </div>
        </div>

        <CategoryButtons
         selectedCategory={selectedCategory}
         onCategoryClick={onCategoryClick}
       />
    </div>
  );
};

export default NewzyListHeader;