import React, { useState, useEffect } from 'react';
import baseAxios from 'shared/utils/baseAxios'; // baseAxios import
import useAuthStore from 'shared/store/userStore'; // Zustand store import

const UtilityButtons = ({ onActiveSidebar, activeSidebar, isLiked, isBookmarked, newzyId }) => {
  const token = useAuthStore(state => state.token);
  const [activeButtons, setActiveButtons] = useState([isLiked, isBookmarked]);

  useEffect(() => {
    // 부모 컴포넌트에서 props로 전달된 상태가 변경되면 버튼 상태 업데이트
    setActiveButtons([isLiked, isBookmarked]);
  }, [isLiked, isBookmarked]);

  const handleButtonClick = async (index) => {
    if (index < 2) {
      // Handle like and bookmark actions
      if (!token) {
        alert("로그인 후 이용해주세요.");
        return;
      }

      try {
        let response;
        
        // 첫 번째 버튼 클릭 (좋아요)
        if (index === 0) {
          if (activeButtons[0]) {
            // 이미 좋아요가 되어 있다면 삭제 요청
            response = await baseAxios().delete(`/newzy/${newzyId}/like`);
          } else {
            // 좋아요가 되어 있지 않다면 추가 요청
            response = await baseAxios().post(`/newzy/${newzyId}/like`);
          }
        }
        
        // 두 번째 버튼 클릭 (북마크)
        else if (index === 1) {
          if (activeButtons[1]) {
            // 이미 북마크가 되어 있다면 삭제 요청
            response = await baseAxios().delete(`/newzy/${newzyId}/bookmark`);
          } else {
            // 북마크가 되어 있지 않다면 추가 요청
            response = await baseAxios().post(`/newzy/${newzyId}/bookmark`);
          }
        }

        // 버튼 상태 업데이트
        setActiveButtons((prev) => {
          const newActiveButtons = [...prev];
          newActiveButtons[index] = !newActiveButtons[index]; // 상태 반전
          return newActiveButtons;
        });
        
      } catch (error) {
        console.error("Error while handling button click:", error);
        alert("작업 중 오류가 발생했습니다. 다시 시도해 주세요.");
      }
    } else if (index === 2) {
      onActiveSidebar("댓글");
    } else if (index === 3) {
      onActiveSidebar("검색");
    }
  };

  return (
    <div className="fixed bottom-0 left-1/2 transform -translate-x-1/2 p-4" style={{ zIndex: 20 }}>
      <div className="flex items-center justify-center rounded-lg shadow-md bg-white p-2">
        {[
          <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" className="size-6">
            <path d="m11.645 20.91-.007-.003-.022-.012a15.247 15.247 0 0 1-.383-.218 25.18 25.18 0 0 1-4.244-3.17C4.688 15.36 2.25 12.174 2.25 8.25 2.25 5.322 4.714 3 7.688 3A5.5 5.5 0 0 1 12 5.052 5.5 5.5 0 0 1 16.313 3c2.973 0 5.437 2.322 5.437 5.25 0 3.925-2.438 7.111-4.739 9.256a25.175 25.175 0 0 1-4.244 3.17 15.247 15.247 0 0 1-.383.219l-.022.012-.007.004-.003.001a.752.752 0 0 1-.704 0l-.003-.001Z" />
          </svg>,
          <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" className="size-6">
            <path fillRule="evenodd" d="M6.32 2.577a49.255 49.255 0 0 1 11.36 0c1.497.174 2.57 1.46 2.57 2.93V21a.75.75 0 0 1-1.085.67L12 18.089l-7.165 3.583A.75.75 0 0 1 3.75 21V5.507c0-1.47 1.073-2.756 2.57-2.93Z" clipRule="evenodd" />
          </svg>,
          <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" className="size-6">
            <path fillRule="evenodd" d="M12 2.25c-2.429 0-4.817.178-7.152.521C2.87 3.061 1.5 4.795 1.5 6.741v6.018c0 1.946 1.37 3.68 3.348 3.97.877.129 1.761.234 2.652.316V21a.75.75 0 0 0 1.28.53l4.184-4.183a.39.39 0 0 1 .266-.112c2.006-.05 3.982-.22 5.922-.506 1.978-.29 3.348-2.023 3.348-3.97V6.741c0-1.947-1.37-3.68-3.348-3.97A49.145 49.145 0 0 0 12 2.25ZM8.25 8.625a1.125 1.125 0 1 0 0 2.25 1.125 1.125 0 0 0 0-2.25Zm2.625 1.125a1.125 1.125 0 1 1 2.25 0 1.125 1.125 0 0 1-2.25 0Zm4.875-1.125a1.125 1.125 0 1 0 0 2.25 1.125 1.125 0 0 0 0-2.25Z" clipRule="evenodd" />
          </svg>,
          <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" className="size-6">
            <path d="M8.25 10.875a2.625 2.625 0 1 1 5.25 0 2.625 2.625 0 0 1-5.25 0Z" />
            <path fillRule="evenodd" d="M12 2.25c-5.385 0-9.75 4.365-9.75 9.75s4.365 9.75 9.75 9.75 9.75-4.365 9.75-9.75S17.385 2.25 12 2.25Zm-1.125 4.5a4.125 4.125 0 1 0 2.338 7.524l2.007 2.006a.75.75 0 1 0 1.06-1.06l-2.006-2.007a4.125 4.125 0 0 0-3.399-6.463Z" clipRule="evenodd" />
          </svg>
        ].map((icon, index) => (
          <button
            key={index}
            onClick={() => handleButtonClick(index)}
            className={`flex items-center justify-center text-gray-500 focus:outline-none mx-2 ${
              (activeButtons[index] || (index === 2 && activeSidebar === "댓글") || (index === 3 && activeSidebar === "검색"))
              ? 'text-purple-500' : ''
            }`}
          >
            {icon}
          </button>
        ))}
      </div>
    </div>
  );
};

export default UtilityButtons;