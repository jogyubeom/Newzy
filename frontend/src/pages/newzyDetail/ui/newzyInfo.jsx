import React from 'react';
import CategoryBadge from '../../../shared/categoryBadge';
import useAuthStore from 'shared/store/userStore'; // Zustand store import

const NewzyInfo = ({ category, title, date, author }) => {
  // Zustand store에서 user 정보를 가져옴
  const user = useAuthStore(state => state.userInfo);
  const userNickname = user ? user.nickname : null; // user가 null일 경우 에러 방지

  return (
    <div className="mb-6">
      <CategoryBadge category={category} />
      <h1 className="text-[36px] font-bold mt-2 mb-2">{title}</h1>
      <div className="text-[#A0A0A0] text-sm mb-2">작성일: {date}</div>
      <div className="flex items-center mb-6">
        <span className="text-[#3D3D3D] text-lg mr-4">{author}</span>
        
        {userNickname === author ? (
          // 작성자와 동일한 경우 저장/삭제 버튼을 보여줌
          <>
            <button className="px-4 py-2 text-[#333] bg-[#F0F0F0] rounded-md text-sm">
              수정
            </button>
            <button className="ml-2 px-4 py-2 text-[#333] bg-[#F0F0F0] rounded-md text-sm">
              삭제
            </button>
          </>
        ) : (
          // 작성자가 아닐 경우 구독 버튼을 보여줌
          <button className="px-4 py-2 text-[#333] bg-[#F0F0F0] rounded-md text-sm">
            구독 +
          </button>
        )}
      </div>
    </div>
  );
};

export default NewzyInfo;