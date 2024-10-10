/* eslint-disable react/prop-types */
import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import axios from 'axios';
import PostList from "../../../shared/postList/postList";
import Pagination from "../../../shared/postList/pagination";

export const Newzy = ( { nickname } ) => {
  const navigate = useNavigate();
  
  const [state, setState] = useState({
    posts: [], // 초기 상태를 빈 배열로 설정
    totalPages: 0,
    currentPage: 1,
    loading: true, // 로딩 상태를 추적
    error: null, // 오류 상태를 추적
  });

  const fetchPosts = async () => {
    const { currentPage } = state;

    try {
      console.log('nickname : ' + nickname); 
      const response = await axios.get(`https://j11b305.p.ssafy.io/api/user/newzy-list/${nickname}`, {
        params: {
          page: currentPage
        }
    });
      console.log(response.data);
      const { totalPage, newzyList } = response.data;

      // 가져온 게시글과 총 페이지 수로 상태 업데이트
      setState((prevState) => ({
        ...prevState,
        posts: newzyList, // 수정된 부분: myNewzyList -> newzyList
        totalPages: totalPage,
        loading: false, // 데이터 가져온 후 로딩 상태 해제
      }));
    } catch (error) {
      console.error("게시글을 가져오는 중 오류 발생:", error);
      setState((prevState) => ({
        ...prevState,
        loading: false, // 오류 발생 시 로딩 상태 해제
        error: "게시글을 불러오는 데 실패했습니다. 나중에 다시 시도해 주세요.", // 오류 메시지 설정
      }));
    }
  };

  useEffect(() => {
    window.scrollTo(0, 0);
    fetchPosts(); // 컴포넌트가 마운트될 때 API 호출
  }, [state.currentPage, nickname]); // 페이지 변경 시 호출

  const handlePostClick = (id) => navigate(`/newzy/detail/${id}`);

  return (
    <div className="bg-white px-10">
      {state.loading ? (
        <div className="text-center py-4">로딩 중...</div> // 로딩 인디케이터
      ) : state.error ? (
        <div className="text-red-500 text-center py-4">{state.error}</div> // 오류 메시지
      ) : (
        <>
          <PostList posts={state.posts} onPostClick={handlePostClick} type='newzy' />
          <Pagination
            currentPage={state.currentPage}
            totalPages={state.totalPages}
            onPageChange={(page) => setState((prevState) => ({ ...prevState, currentPage: page }))} 
          />
        </>
      )}
    </div>
  );
};

export default Newzy;