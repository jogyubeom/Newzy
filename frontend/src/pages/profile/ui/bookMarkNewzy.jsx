import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import baseAxios from "../../../shared/utils/baseAxios";
import PostList from "../../../shared/postList/postList";
import Pagination from "../../../shared/postList/pagination";
import useAuthStore from "../../../shared/store/userStore";

export const BookmarkNewzy = () => {
  const navigate = useNavigate();
  const [state, setState] = useState({
    posts: [],
    totalPages: 0,
    currentPage: 1,
  });
  const { token } = useAuthStore();

  const fetchPosts = async () => {
    const { currentPage } = state;

    const apiUrl = `/user/newzy-bookmark?page=${currentPage}`;
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
    fetchPosts();
  }, [state.currentPage]);

  const handlePostClick = (id) => navigate(`/newzy/detail/${id}`);

  return (
    <div>
      <div className="bg-white">
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

export default BookmarkNewzy;