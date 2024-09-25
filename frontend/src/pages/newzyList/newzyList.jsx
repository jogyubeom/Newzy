import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { posts } from './data';
import NewzyListHeader from './ui/newzyListHeader';
import PostList from './ui/postList';
import Pagination from './ui/pagination';
import SearchBar from './ui/searchBar';

const postsPerPage = 10;

export const NewzyList = () => {
  const [currentPage, setCurrentPage] = useState(1);
  const navigate = useNavigate();
  const [sort, setSort] = useState({
    selectedCategory: '전체',
    selectedRange: '전체',
    searchTerm: ''
  });

  const handlePostClick = (id) => navigate(`/newzy/${id}`);
  const totalPages = Math.ceil(posts.length / postsPerPage);
  const currentPosts = posts.slice((currentPage - 1) * postsPerPage, currentPage * postsPerPage);

  const handleCategoryOrRangeClick = (key, value) => {
    setSort((prevSort) => ({ ...prevSort, [key]: value }));
    setCurrentPage(1);
  };

  const handleSearchChange = (e) => setSort((prevSort) => ({ ...prevSort, searchTerm: e.target.value }));

  const handleKeyPress = (e) => {
    if (e.key === 'Enter') setCurrentPage(1);
  };

  const clearSearch = () => setSort((prevSort) => ({ ...prevSort, searchTerm: '' }));

  useEffect(() => {
    window.scrollTo(0, 0);
  }, [currentPage]);

  return (
    <div>
      <NewzyListHeader
        selectedCategory={sort.selectedCategory}
        onCategoryClick={(category) => handleCategoryOrRangeClick('selectedCategory', category)}
      />

      <div className="bg-white">
        <div className="flex justify-between items-center mb-4 px-4 py-2">
          <SearchBar
            selectedRange={sort.selectedRange}
            onRangeClick={(range) => handleCategoryOrRangeClick('selectedRange', range)}
            searchTerm={sort.searchTerm}
            onSearchChange={handleSearchChange}
            onClearSearch={clearSearch}
            onKeyPress={handleKeyPress}
          />
          <button className="ml-2 px-4 py-2 bg-red-500 text-white rounded" onClick={() => navigate('/edit')}>
            뉴지 작성
          </button>
        </div>

        <PostList posts={currentPosts} onPostClick={handlePostClick} />

        <Pagination
          currentPage={currentPage}
          totalPages={totalPages}
          onPageChange={setCurrentPage}
        />
      </div>
    </div>
  );
};
