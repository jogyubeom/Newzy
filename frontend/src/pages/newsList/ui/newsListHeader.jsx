import React from "react";
import CategoryButtons from "./categoryButtons";

const NewsListHeader = ({ selectedCategory, onCategoryClick }) => {
  return (
    <div className="bg-emerald-900 text-white flex flex-col items-center py-8">
      <div className="text-9xl font-bold mb-12 relative">News</div>

      <CategoryButtons
        selectedCategory={selectedCategory}
        onCategoryClick={onCategoryClick}
      />
    </div>
  );
};

export default NewsListHeader;
