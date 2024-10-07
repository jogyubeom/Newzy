import React from 'react';
import { useNavigate } from 'react-router-dom';
import CategoryBadge from '../../../shared/categoryBadge';
import useAuthStore from 'shared/store/userStore'; // Zustand store import
import baseAxios from '../../../shared/utils/baseAxios';

const NewzyInfo = ({ category, title, date, author, newzyId, isFollowed, onFollowChange }) => {
  const navigate = useNavigate(); // useNavigate 사용
  const { token, userInfo } = useAuthStore(state => ({
    token: state.token,
    userInfo: state.userInfo,
  }));
  const userNickname = userInfo ? userInfo.nickname : null; // user가 null일 경우 에러 방지

  const handleSubscribe = async () => {
    if (!token) {
      alert("로그인이 필요합니다.");
      return; // 동작 중단
    }

    try {
      await baseAxios(token).post(`/user/${author}/follower`);
      alert(`${author}님을 구독했습니다.`);
      onFollowChange(true); // 구독 후 상태 업데이트
    } catch (error) {
      if (error.response && error.response.status === 409) {
        alert("이미 구독한 사용자입니다.");
      } else {
        console.error("Error subscribing to author:", error);
      }
    }
  };

  const handleUnsubscribe = async () => {
    if (!token) {
      alert("로그인이 필요합니다.");
      return;
    }

    try {
      await baseAxios(token).delete(`/user/${author}/follower`);
      alert(`${author}님의 구독을 취소했습니다.`);
      onFollowChange(false); // 구독 취소 후 상태 업데이트
    } catch (error) {
      console.error("Error unsubscribing from author:", error);
    }
  };

  const handleDelete = async () => {
    if (!token) {
      alert("로그인이 필요합니다.");
      return;
    }

    const confirmed = window.confirm("정말 삭제하시겠습니까?");
    if (!confirmed) return;

    try {
      await baseAxios(token).delete(`/newzy/${newzyId}`);
      navigate('/newzy'); // 성공 시 newzyList 페이지로 이동
    } catch (error) {
      console.error("Error deleting newzy:", error);
    }
  };

  // 새로운 핸들러를 추가하여 수정 페이지로 이동
  const handleEdit = () => {
    navigate(`/newzy/edit/${newzyId}`);
  };

  return (
    <div className="mb-6">
      <CategoryBadge category={category} />
      <h1 className="text-[36px] font-bold mt-2 mb-2">{title}</h1>
      <div className="text-[#A0A0A0] text-sm mb-2">작성일: {date}</div>
      <div className="flex items-center mb-6">
        <span className="text-[#3D3D3D] text-lg mr-4">{author}</span>

        {userNickname === author ? (
          // 작성자와 동일한 경우 수정/삭제 버튼을 보여줌
          <>
            <button 
              onClick={handleEdit} // 수정 버튼 클릭 시 handleEdit 호출
              className="px-4 py-2 text-[#333] bg-[#F0F0F0] rounded-md text-sm"
            >
              수정
            </button>
            <button
              onClick={handleDelete} // 삭제 버튼 클릭 시 handleDelete 호출
              className="ml-2 px-4 py-2 text-[#333] bg-[#F0F0F0] rounded-md text-sm"
            >
              삭제
            </button>
          </>
        ) : (
          // 작성자가 아닐 경우 구독 또는 구독 취소 버튼을 보여줌
          isFollowed ? (
            <button
              onClick={handleUnsubscribe} // 구독 취소 버튼 클릭 시 handleUnsubscribe 호출
              className="px-4 py-2 text-[#333] bg-[#F0F0F0] rounded-md text-sm"
            >
              구독 취소
            </button>
          ) : (
            <button
              onClick={handleSubscribe} // 구독 버튼 클릭 시 handleSubscribe 호출
              className="px-4 py-2 text-[#333] bg-[#F0F0F0] rounded-md text-sm"
            >
              구독 +
            </button>
          )
        )}
      </div>
    </div>
  );
};

export default NewzyInfo;