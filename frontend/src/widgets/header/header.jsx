import { BiBell } from "react-icons/bi";
import { FaUserCircle } from "react-icons/fa";
import { useNavigate } from "react-router-dom";
import { useState } from "react";
import { SocialLoginModal } from "widgets/login/socialLoginModal";
import useAuthStore from "shared/store/userStore";
import baseAxios from "shared/utils/baseAxios";

export const Header = () => {
  const nav = useNavigate();
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [isHovering, setIsHovering] = useState(false); // hover 상태 관리
  const isLoggedIn = useAuthStore((state) => state.isLoggedIn()); // 로그인 상태 확인

  const openModal = () => {
    setIsModalOpen(true);
  };

  const closeModal = () => {
    setIsModalOpen(false);
  };

  const handleLogout = async () => {
    try {
      // 서버에 로그아웃 요청 (GET 요청)
      await baseAxios().get("/user/logout");

      // 로그아웃 성공 시 토큰 삭제
      useAuthStore.getState().clearToken();

      // 로그아웃 후 메인 화면으로 리다이렉트
      nav("/"); // useNavigate를 컴포넌트 내에서 사용
    } catch (error) {
      console.error("로그아웃 오류:", error);
    }
  };

  // 종 모양 아이콘 클릭 시 토큰 삭제 함수(임시)
  const handleBellClick = () => {
    useAuthStore.getState().clearToken(); // 토큰 삭제
    console.log("토큰이 삭제되었습니다.");
  };


  return (
    <>
      <header className="w-full h-[77px] flex justify-between items-center px-[48px]">
        <div className="flex-shrink-0 h-[72px] text-[#26262C] whitespace-nowrap text-ellipsis font-[Open Sans] text-[36px] leading-[24px] font-semibold flex items-center justify-between text-justify">
          Newzy
        </div>
        <div className="flex items-center space-x-4">
          <button
            className="flex-shrink-0 w-[76px] h-[37px] flex items-center justify-center rounded-full hover:bg-gray-100"
            onClick={handleBellClick}
          >
            <BiBell className="w-6 h-6 text-gray-800" />
          </button>

          {/* 로그인 상태에 따라 FaUserCircle 또는 로그인 버튼 표시 */}
          {isLoggedIn ? (
            <div
              className="relative"
              onMouseEnter={() => setIsHovering(true)} // hover 시작
              onMouseLeave={() => setIsHovering(false)} // hover 종료
            >
              <button className="w-[40px] h-[40px] bg-gray-200 rounded-full">
                <FaUserCircle className="w-full h-full object-cover rounded-full text-blue-400" />
              </button>

              {/* hover 시 로그아웃 버튼 표시 */}
              {isHovering && (
                <button
                className="absolute top-[45px] left-0 w-[120px] bg-gradient-to-r font-semibold from-red-400 to-red-600 text-white text-sm py-2 rounded-full shadow-md hover:shadow-lg hover:from-red-500 hover:to-red-700 transition-all duration-300 ease-in-out"
                onClick={handleLogout}
              >
                로그아웃
              </button>
              )}
            </div>
          ) : (
            <button
            className="px-6 py-2 bg-gradient-to-r font-semibold from-blue-400 to-blue-600 text-white rounded-full shadow-lg hover:shadow-xl hover:bg-gradient-to-r hover:from-blue-500 hover:to-blue-700 transition-all duration-300 ease-in-out"
            onClick={openModal} // 로그인 모달 열기
            >
              로그인
            </button>
          )}
        </div>
      </header>
      <SocialLoginModal isOpen={isModalOpen} onClose={closeModal} />
    </>
  );
};
