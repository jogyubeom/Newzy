import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import NewzyListHeader from "./ui/newzyListHeader";
import PostList from "../../shared/postList/postList";
import Pagination from "../../shared/postList/pagination";
import SearchBar from "../../shared/postList/searchBar";
import useAuthStore from "shared/store/userStore"; // zustand 스토어 import

export const NewzyList = () => {
  const navigate = useNavigate();
  const { token } = useAuthStore(); // 토큰 가져오기
  const [state, setState] = useState({
    posts: [],
    totalPages: 0,
    currentPage: 1,
    sort: {
      selectedCategory: "전체",
      selectedRange: "전체",
      searchTerm: "",
    },
  });

  const fetchPosts = async () => {
    const { currentPage, sort: { selectedCategory } } = state;

    const categoryMap = {
      전체: "",
      시사: 0,
      문화: 1,
      자유: 2,
    };

    const categoryParam = categoryMap[selectedCategory] !== undefined ? categoryMap[selectedCategory] : "";
    const apiUrl = `https://j11b305.p.ssafy.io/api/newzy?page=${currentPage}`;

    try {
      const response = await axios.get(apiUrl);
      const { totalPage, newzyList } = response.data;
      setState((prevState) => ({
        ...prevState,
        posts: newzyList,
        totalPages: totalPage,
      }));
    } catch (error) {
      console.error("게시글을 가져오는 중 오류 발생:", error);
    }
  };

  useEffect(() => {
    window.scrollTo(0, 0);
    fetchPosts(); // 컴포넌트가 마운트될 때 API 호출
  }, [state.currentPage, state.sort.selectedCategory]); // 페이지와 카테고리 변경 시 호출

  const handlePostClick = (id) => navigate(`/newzy/${id}`);

  const handleCategoryOrRangeClick = (key, value) => {
    setState((prevState) => ({
      ...prevState,
      sort: { ...prevState.sort, [key]: value },
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

  // 뉴지 작성 버튼 클릭 핸들러
  const handleNewzyWriteClick = () => {
    if (!token) {
      alert("로그인 후 뉴지를 작성할 수 있습니다."); // 토큰이 없을 때 알림
    } else {
      navigate("/newzy/edit");
    }
  };

  return (
    <div>
      <NewzyListHeader
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
          <button
            className="ml-2 px-4 py-2 bg-red-500 text-white rounded"
            onClick={handleNewzyWriteClick} // 클릭 핸들러 변경
          >
            뉴지 작성
          </button>
        </div>

        <PostList posts={state.posts} onPostClick={handlePostClick} type='newzy' />

        <Pagination
          currentPage={state.currentPage}
          totalPages={state.totalPages}
          onPageChange={(page) => setState((prevState) => ({ ...prevState, currentPage: page }))}
        />
      </div>
    </div>
  );
};
