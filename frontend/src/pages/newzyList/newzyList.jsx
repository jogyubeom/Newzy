import React, { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom"; // useParams 추가
import NewzyListHeader from "./ui/newzyListHeader";
import PostList from "../../shared/postList/postList";
import Pagination from "../../shared/postList/pagination";
import SearchBar from "../../shared/postList/searchBar";
import baseAxios from "shared/utils/baseAxios";
import useAuthStore from "shared/store/userStore";

export const NewzyList = () => {
  const navigate = useNavigate();
  const { category } = useParams(); // useParams를 통해 URL 파라미터에서 카테고리를 받아옴
  const { token } = useAuthStore();
  const [state, setState] = useState({
    posts: [],
    totalPages: 0,
    currentPage: 1,
    sort: {
      selectedCategory: category || "전체", // URL에 따라 초기 카테고리 설정
      selectedRange: 0, // default: 0 (기본 API)
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
      시사: 0,
      문화: 1,
      자유: 2,
    };

    const categoryParam = categoryMap[selectedCategory] !== undefined ? categoryMap[selectedCategory] : "";

    const keyword = searchTerm ? `&keyword=${searchTerm}` : ""; // 키워드가 있을 때만 추가

    // selectedRange가 0일 때는 기존 API, 1일 때는 구독 API 호출
    const apiUrl =
      selectedRange === 0
        ? `/newzy?page=${currentPage}&category=${categoryParam}${keyword}` // 기본 API에 키워드 추가
        : `/user/followings-newzy-list?page=${currentPage}&category=${categoryParam}${keyword}`; // 구독 API에 키워드 추가

    try {
      const response = await baseAxios().get(apiUrl);
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
  }, [state.currentPage, state.sort.selectedCategory, state.sort.selectedRange, category]); // URL의 카테고리 변화도 감지

  const handlePostClick = (id) => navigate(`/newzy/${id}`);

  const handleCategoryOrRangeClick = (key, value) => {
    setState((prevState) => ({
      ...prevState,
      sort: { ...prevState.sort, [key]: value, searchTerm: "" },
      currentPage: 1, // 카테고리나 범위 클릭 시 첫 페이지로 초기화
    }));

    // 카테고리 변경 시에만 URL 업데이트
    if (key === "selectedCategory") {
      const newPath = value === "전체" ? "/newzy" : `/newzy/${value}`;
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
        onCategoryClick={(category) => handleCategoryOrRangeClick("selectedCategory", category)}
      />

      <div className="bg-white">
        <div className="flex justify-between items-center mb-4 px-4 py-2">
          <SearchBar
            type="newzy"
            selectedRange={state.sort.selectedRange}
            onRangeClick={(range) => handleCategoryOrRangeClick("selectedRange", range)}
            searchTerm={state.sort.searchTerm}
            onSearchChange={handleSearchChange}
            onClearSearch={clearSearch}
            onKeyPress={handleKeyPress}
          />
          <button className="ml-2 px-4 py-2 bg-red-500 text-white rounded" onClick={handleNewzyWriteClick}>
            뉴지 작성
          </button>
        </div>

        <PostList posts={state.posts} onPostClick={handlePostClick} type="newzy" />

        <Pagination
          currentPage={state.currentPage}
          totalPages={state.totalPages}
          onPageChange={(page) => setState((prevState) => ({ ...prevState, currentPage: page }))}
        />
      </div>
    </div>
  );
};