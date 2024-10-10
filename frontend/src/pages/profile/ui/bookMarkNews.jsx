import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import baseAxios from "../../../shared/utils/baseAxios";
import PostList from "../../../shared/postList/postList";
import Pagination from "../../../shared/postList/pagination";

export const BookmarkNews = () => {
  const navigate = useNavigate();
  const [state, setState] = useState({
    posts: [],
    totalPages: 0,
    currentPage: 1,
  });

  const fetchPosts = async () => {
    const { currentPage } = state;

    const apiUrl = `/user/news-bookmark?page=${currentPage}`; // API URL

    try {
      const response = await baseAxios().get(apiUrl); // baseAxios 사용
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
    fetchPosts(); // 컴포넌트가 마운트될 때 API 호출
  }, [state.currentPage]); // 페이지 변경 시 호출

  const handlePostClick = (id) => navigate(`/news/${id}`);

  return (
    <div>
      <div className="bg-white">
        <PostList posts={state.posts} onPostClick={handlePostClick} type='news' />

        <Pagination
          currentPage={state.currentPage}
          totalPages={state.totalPages}
          onPageChange={(page) => setState((prevState) => ({ ...prevState, currentPage: page }))}
        />
      </div>
    </div>
  );
};

export default BookmarkNews;