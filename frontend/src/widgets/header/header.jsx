import { BiBell } from "react-icons/bi";
import { FaUserCircle } from "react-icons/fa";
import { useNavigate } from "react-router-dom";
import { useState, useEffect } from "react";
import { SocialLoginModal } from "widgets/login/socialLoginModal";
import useAuthStore from "shared/store/userStore";
import baseAxios from "shared/utils/baseAxios";

export const Header = () => {
  const nav = useNavigate();
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [isHovering, setIsHovering] = useState(false); // hover 상태 관리
  const isLoggedIn = useAuthStore((state) => state.isLoggedIn()); // 로그인 상태 확인

  const user = useAuthStore((state) => state.userInfo);

  useEffect(() => {
    console.log("헤더의 userInfo 변경 감지:", user);  // 유저 정보가 변경되었을 때 제대로 반영되는지 확인
  }, [user]);

  const openModal = () => {
    setIsModalOpen(true);
  };

  const closeModal = () => {
    setIsModalOpen(false);
  };

  const handleLogout = async () => {
    try {
      const token = useAuthStore.getState().token; // Zustand 스토어에서 토큰 가져오기
      console.log("현재 토큰:", token);

      if (!token) {
        console.error("토큰이 없습니다. 로그아웃을 처리할 수 없습니다.");
        return;
      }

      // 서버에 로그아웃 요청 (GET 요청)
      await baseAxios().get("/user/logout");

    } catch (error) {
      console.error("로그아웃 오류:", error);
    }

    // 프론트의 토큰 및 유저 정보 삭제
    useAuthStore.getState().clearToken(); // 토큰 삭제
    useAuthStore.getState().clearUserInfo(); // 유저 정보 삭제

    // 로그아웃 후 메인 화면으로 리다이렉트
    nav("/"); // useNavigate를 컴포넌트 내에서 사용
  };

  // 종 모양 아이콘 클릭 시 토큰 삭제 함수(디버그용 임시)
  // const handleBellClick = () => {
  //   const confirmed = window.confirm(
  //     "임시로 만들어둔 강제 로그아웃 버튼,\n확인 시 프론트의 토큰이 삭제됨!\n(토큰이 만료되어 로그아웃이 안될 때만 사용할 것)"
  //   );
  //   if (confirmed) {
  //     useAuthStore.getState().clearToken(); // 토큰 삭제
  //     console.log("토큰이 삭제되었습니다.");
  //   } else {
  //     console.log("토큰 삭제가 취소되었습니다.");
  //   }
  // };

  return (
    <>
      <header className="w-full h-14 flex justify-between items-center px-7">
        <div className="flex-shrink-0 h-[72px] text-[#26262C] whitespace-nowrap text-ellipsis font-[Open Sans] text-2xl font-semibold font-header tracking-[-0.04em] flex items-center justify-between text-justify">
          Newzy
        </div>
        <div className="flex items-center space-x-4">
          {/* <button
            className="flex-shrink-0 w-[76px] h-[37px] flex items-center justify-center rounded-full hover:bg-gray-100"
            // onClick={handleBellClick}
          >
            <BiBell className="w-6 h-6 text-gray-800" />
          </button> */}

          {/* 로그인 상태에 따라 FaUserCircle 또는 로그인 버튼 표시 */}
          {user && isLoggedIn ? (
            <div
              className="relative flex items-center"
              onMouseEnter={() => setIsHovering(true)} // hover 시작
              onMouseLeave={() => setIsHovering(false)} // hover 종료
            >
              <button className="w-[45px] h-[45px] bg-gray-200 rounded-full">
              {user.profile ? ( // user가 null이 아닌지 먼저 확인
                  <img src={user.profile} className="w-full h-full object-cover rounded-full" />
                ) : (
                  <FaUserCircle className="w-full h-full object-cover rounded-full text-blue-400" />
                )}
              </button>

              {/* hover 시 로그아웃 버튼 표시 */}
              {isHovering && (
                <button
                  className="absolute top-[45px] left-[-40px] w-[120px] bg-gradient-to-r font-semibold from-red-400 to-red-600 text-white text-sm py-2 rounded-full shadow-md hover:shadow-lg hover:from-red-500 hover:to-red-700 transition-all duration-300 ease-in-out"
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
