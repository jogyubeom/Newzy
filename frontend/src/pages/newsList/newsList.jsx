import React, { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom"; // useParams 추가
import axios from "axios";
import NewsListHeader from "./ui/newsListHeader";
import PostList from "../../shared/postList/postList";
import Pagination from "../../shared/postList/pagination";
import SearchBar from "../../shared/postList/searchBar";

export const NewsList = () => {
  const navigate = useNavigate();
  const { category } = useParams(); // useParams로 URL에서 카테고리 값을 받아옴
  const [state, setState] = useState({
    posts: [],
    totalPages: 0,
    currentPage: 1,
    sort: {
      selectedCategory: category || "전체", // URL 파라미터에 따른 초기 카테고리 설정
      selectedRange: 0, // 구독이나 인기 상태
      searchTerm: "",
    },
  });

  // 게시글 불러오는 함수
  const fetchPosts = async () => {
    const {
      currentPage,
      sort: { selectedCategory, selectedRange, searchTerm },
    } = state;

    const categoryMap = {
      전체: "",
      경제: 0,
      사회: 1,
      세계: 2,
    };

    const categoryParam =
      categoryMap[selectedCategory] !== undefined
        ? categoryMap[selectedCategory]
        : "";
    const keyword = searchTerm ? `&keyword=${searchTerm}` : ""; // 키워드가 있을 때만 추가
    const apiUrl = `https://j11b305.p.ssafy.io/api/news?page=${currentPage}&category=${categoryParam}&sort=${selectedRange}${keyword}`;

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
    fetchPosts(); // 컴포넌트가 마운트될 때 및 URL 파라미터가 바뀔 때 API 호출
  }, [
    state.currentPage,
    state.sort.selectedCategory,
    state.sort.selectedRange,
    category, // URL 파라미터 카테고리 변경 시 다시 호출
  ]);

  const handlePostClick = (id) => navigate(`/news/detail/${id}`);

  // 카테고리나 구독/인기 상태 변경 핸들러
  const handleCategoryOrRangeClick = (key, value) => {
    setState((prevState) => ({
      ...prevState,
      sort: { ...prevState.sort, [key]: value, searchTerm: "" },
      currentPage: 1, // 카테고리나 범위 변경 시 첫 페이지로 초기화
    }));

    // 카테고리 변경 시에만 URL 업데이트
    if (key === "selectedCategory") {
      const newPath = value === "전체" ? "/news" : `/news/${value}`;
      navigate(newPath);
    }
  };

  const handleSearchChange = (e) =>
    setState((prevState) => ({
      ...prevState,
      sort: { ...prevState.sort, searchTerm: e.target.value },
    }));

  const handleKeyPress = (e) => {
    if (e.key === "Enter") {
      setState((prevState) => ({
        ...prevState,
        currentPage: 1,
      }));
      fetchPosts();
    }
  };

  const clearSearch = () =>
    setState((prevState) => ({
      ...prevState,
      sort: { ...prevState.sort, searchTerm: "" },
    }));

  return (
    <div className='min-w-[1150px]'>
      <NewsListHeader
        selectedCategory={state.sort.selectedCategory}
        onCategoryClick={(category) =>
          handleCategoryOrRangeClick("selectedCategory", category)
        }
      />

      <div className="bg-white">
        <div className="flex justify-between items-center mb-4 px-4 py-2">
          <SearchBar
            type="news"
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

        <PostList
          posts={state.posts}
          onPostClick={handlePostClick}
          type="news"
        />

        <Pagination
          currentPage={state.currentPage}
          totalPages={state.totalPages}
          onPageChange={(page) =>
            setState((prevState) => ({ ...prevState, currentPage: page }))
          }
        />
      </div>
    </div>
  );
};