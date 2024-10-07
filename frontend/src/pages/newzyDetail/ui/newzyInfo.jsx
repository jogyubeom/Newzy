import React from 'react';
import { useNavigate } from 'react-router-dom';
import CategoryBadge from '../../../shared/categoryBadge';
import useAuthStore from 'shared/store/userStore'; // Zustand store import
import baseAxios from '../../../shared/utils/baseAxios';

const NewzyInfo = ({ category, title, date, author, newzyId }) => {
  const navigate = useNavigate(); // useNavigate 사용
  const { token, userInfo } = useAuthStore(state => ({
    token: state.token,
    userInfo: state.userInfo,
  }));
  const userNickname = userInfo ? userInfo.nickname : null; // user가 null일 경우 에러 방지

  const handleSubscribe = async () => {
    if (!token) {
      // 토큰이 없으면 로그인 요청
      alert("로그인이 필요합니다.");
      return; // 동작 중단
    }

    try {
      // POST 요청으로 구독하기
      await baseAxios(token).post(`/user/${author}/follower`);
      console.log(`${author}님을 구독했습니다.`);
    } catch (error) {
      if (error.response && error.response.status === 409) {
        // 409 에러인 경우 이미 구독 중이라는 메시지 표시
        alert("이미 구독한 사용자입니다.");
      } else {
        console.error("Error subscribing to author:", error);
      }
    }
  };

  const handleDelete = async () => {
    if (!token) {
      alert("로그인이 필요합니다.");
      return;
    }

    // 삭제 확인을 위한 확인창을 띄움
    const confirmed = window.confirm("정말 삭제하시겠습니까?");
    if (!confirmed) {
      return; // 사용자가 '취소'를 누른 경우 동작 중단
    }

    try {
      // 삭제 요청 전송
      await baseAxios(token).delete(`/newzy/${newzyId}`);
      navigate('/newzy'); // 성공 시 newzyList 페이지로 이동
    } catch (error) {
      console.error("Error deleting newzy:", error);
    }
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
            <button className="px-4 py-2 text-[#333] bg-[#F0F0F0] rounded-md text-sm">
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
          // 작성자가 아닐 경우 구독 버튼을 보여줌
          <button 
            onClick={handleSubscribe} // 구독 버튼 클릭 시 handleSubscribe 호출
            className="px-4 py-2 text-[#333] bg-[#F0F0F0] rounded-md text-sm"
          >
            구독 +
          </button>
        )}
      </div>
    </div>
  );
};

export default NewzyInfo;