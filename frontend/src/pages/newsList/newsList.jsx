import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import NewsListHeader from "./ui/newsListHeader";
import PostList from "../../shared/postList/postList";
import Pagination from "../../shared/postList/pagination";
import SearchBar from "../../shared/postList/searchBar";

export const NewsList = () => {
  const navigate = useNavigate();
  const [state, setState] = useState({
    posts: [],
    totalPages: 0,
    currentPage: 1,
    sort: {
      selectedCategory: "전체",
      selectedRange: 0,
      searchTerm: "",
    },
  });

  const fetchPosts = async () => {
    const { currentPage, sort: { selectedCategory,selectedRange, searchTerm } } = state;

    const categoryMap = {
      전체: "",
      경제: 0,
      사회: 1,
      세계: 2,
    };

    const categoryParam = categoryMap[selectedCategory] !== undefined ? categoryMap[selectedCategory] : "";
    const apiUrl = `https://j11b305.p.ssafy.io/api/news?page=${currentPage}&category=${categoryParam}&sort=${selectedRange}`;

    try {
      const response = await axios.get(apiUrl);
      const { totalPage, newsList } = response.data;
      setState((prevState) => ({
        ...prevState,
        posts: newsList,
        totalPages: totalPage,
      }));
    } catch (error) {
      console.error("게시글을 가져오는 중 오류 발생:", error);
    }
  };

  useEffect(() => {
    window.scrollTo(0, 0);
    fetchPosts();
  }, [state.currentPage, state.sort.selectedCategory, state.sort.selectedRange]);

  const handlePostClick = (id) => navigate(`/news/${id}`);

  const handleCategoryOrRangeClick = (key, value) => {
    setState((prevState) => ({
      ...prevState,
      sort: { ...prevState.sort, [key]: value, searchTerm: "",},
      currentPage: 1, // 카테고리나 범위 클릭 시 첫 페이지로 초기화
    }));
  };

  const handleSearchChange = (e) =>
    setState((prevState) => ({
      ...prevState,
      sort: { ...prevState.sort, searchTerm: e.target.value },
    }));

  const handleKeyPress = (e) => {
    if (e.key === "Enter") setState((prevState) => ({ ...prevState, currentPage: 1 }));
  };

  const clearSearch = () =>
    setState((prevState) => ({
      ...prevState,
      sort: { ...prevState.sort, searchTerm: "" },
    }));

  useEffect(() => {
    window.scrollTo(0, 0);
  }, [state.currentPage]);

  return (
    <div>
      <NewsListHeader
        selectedCategory={state.sort.selectedCategory}
        onCategoryClick={(category) =>
          handleCategoryOrRangeClick("selectedCategory", category)
        }
      />

      <div className="bg-white">
        <div className="flex justify-between items-center mb-4 px-4 py-2">
          <SearchBar
            selectedRange={state.sort.selectedRange}
            onRangeClick={(range) =>
              handleCategoryOrRangeClick("selectedRange", range)
            }
            searchTerm={state.sort.searchTerm}
            onSearchChange={handleSearchChange}
            onClearSearch={clearSearch}
            onKeyPress={handleKeyPress}
          />
        </div>

        <PostList posts={state.posts} onPostClick={handlePostClick} type='news'/>

        <Pagination
          currentPage={state.currentPage}
          totalPages={state.totalPages}
          onPageChange={(page) => setState((prevState) => ({ ...prevState, currentPage: page }))}
        />
      </div>
    </div>
  );
};