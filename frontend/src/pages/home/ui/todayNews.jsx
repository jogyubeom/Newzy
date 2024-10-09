import { useState } from "react";
import { useNavigate } from "react-router-dom";
import useHomeStore from "../store/useHomeStore";
import useAuthStore from "shared/store/userStore";
import { SocialLoginModal } from "widgets/login/socialLoginModal";
import { TodayQuizModal } from "./todayQuiz";
import { IoIosLock as Lock } from "react-icons/io?react";

export const TodayNews = () => {
  const navigate = useNavigate();
  const isLoggedIn = useAuthStore((state) => state.isLoggedIn());
  const { userInfo } = useAuthStore();
  const { todayNews } = useHomeStore();
  const [isQuizModalOpen, setIsQuizModalOpen] = useState(false); // 퀴즈 모달
  const [isLoginModalOpen, setIsLoginModalOpen] = useState(false); // 로그인 모달

  const handleTodayNews = (id) => navigate(`/news/${id}`);

  const openQuizModal = () => {
    setIsQuizModalOpen(true);
  };

  const closeQuizModal = () => {
    setIsQuizModalOpen(false);
  };

  const openLoginModal = () => {
    setIsLoginModalOpen(true);
  };

  const closeLoginModal = () => {
    setIsLoginModalOpen(false);
  };

  return (
    <div className="flex flex-col lg:flex-row justify-between items-center px-6 py-8 space-y-6 lg:space-y-0 lg:space-x-6">
      {isLoggedIn ? (
        <div className="absolute flex flex-col bottom-0 left-0 xl:flex-row">
          <button
            className="w-[453px] h-[215px] bg-purple-600 flex flex-col items-center rounded-t-lg p-4 gap-2"
            onClick={openLoginModal}
          >
            <div className="flex justify-center items-center text-center">
              <div className="text-white text-6xl font-semibold">
                TODAY NEWS
              </div>
              <div className="p-4 m-2 bg-purple-300 rounded-full">
                <Lock className="text-purple-800 text-[80px]" />
              </div>
            </div>
            <div className="text-center text-purple-200 opacity-80 text-[12px]">
              <p>로그인하고 오늘의 추천 뉴스를 확인해 보세요!</p>
              <p>추천 뉴스는 나의 문해력, 관심도에 따라 제공됩니다.</p>
            </div>
          </button>

          <button
            className="w-[453px] h-[215px] bg-yellow-400 flex flex-col items-center rounded-t-lg p-4 gap-2"
            onClick={openLoginModal}
          >
            <div className="flex justify-center items-center text-right gap-4">
              <div className="text-white text-7xl font-semibold">
                TODAY QUIZ
              </div>
              <div className="p-4 m-2 bg-yellow-600 rounded-full">
                <Lock className="text-yellow-100 text-[80px]" />
              </div>
            </div>
            <div className="text-center text-yellow-700 opacity-80 text-[12px]">
              <p>로그인하고 오늘의 퀴즈를 풀어보세요!</p>
              <p>퀴즈는 Daily News의 내용을 바탕으로 만들어집니다.</p>
            </div>
          </button>
        </div>
      ) : (
        <div className="absolute flex bottom-0 left-0 xl:flex-row w-full p-6">
          {/* 추천 뉴스 섹션 */}
          <button
            className="w-[453px] h-[215px] bg-purple-600 flex flex-col items-center rounded-t-lg p-4 gap-2"
            onClick={() => handleTodayNews(todayNews?.newsId)}
          >
            <div className="flex justify-center items-center text-center">
              <div className="text-white text-6xl font-semibold">
                TODAY NEWS
              </div>
              <div className="p-4 m-2 bg-purple-300 rounded-full">
                <Lock className="text-purple-800 text-[80px]" />
              </div>
            </div>
            <div className="text-center text-purple-200 opacity-80 text-[12px]">
              <p>{userInfo?.nickname}님을 위한 추천 뉴스 제발 읽어주세요</p>
            </div>
          </button>

          {/* 퀴즈 섹션 */}
          <div className="flex w-full lg:w-1/2">
            <div className="w-full bg-white shadow-lg rounded-lg p-4">
              <h2 className="text-lg font-semibold text-gray-900">
                오늘의 퀴즈!
              </h2>
              <p className="text-base font-normal text-gray-700 mt-2">
                {todayNews?.question}
              </p>
              <button
                className="mt-4 w-full h-12 bg-purple-600 hover:bg-purple-700 text-white rounded-lg"
                onClick={openQuizModal}
              >
                퀴즈 풀기
              </button>
            </div>
          </div>
        </div>
      )}

      {/* 퀴즈 모달 */}
      <TodayQuizModal isOpen={isQuizModalOpen} onClose={closeQuizModal} />

      {/* 소셜 로그인 모달 */}
      <SocialLoginModal isOpen={isLoginModalOpen} onClose={closeLoginModal} />
    </div>
  );
};
