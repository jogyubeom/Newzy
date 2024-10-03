import { useState } from "react";
import { IoIosLock as Lock } from "react-icons/io?react";
import useAuthStore from "shared/store/userStore";
import { SocialLoginModal } from "widgets/login/socialLoginModal";

export const Header = () => {
  const isLoggedIn = useAuthStore((state) => state.isLoggedIn());
  const [isModalOpen, setIsModalOpen] = useState(false);

  const now = new Date();
  const days = ["SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"];
  const today = `${now.getFullYear()}. ${
    now.getMonth() + 1
  }. ${now.getDate()}. ${days[now.getDay()]}`;

  const openModal = () => {
    setIsModalOpen(true);
  };

  const closeModal = () => {
    setIsModalOpen(false);
  };
  return (
    <div className="relative h-[440px] bg-gray-800">
      <div className="absolute top-0 left-0 w-full h-[200px]">
        <div className="relative flex items-center justify-center text-white text-center w-full h-full text-[140px] font-semibold font-header tracking-[-0.05em]">
          Newzy
        </div>
        <div className="absolute bottom-[7px] left-1/3 flex items-center text-white text-2xl font-semibold font-header">
          {today}
        </div>
      </div>

      <div className="absolute bottom-0 right-0 flex flex-col justify-end items-center gap-3 pb-4">
        <div className="w-[300px] h-[228px] flex flex-col items-center gap-[18px]">
          <button className="w-full h-[64px] bg-red-800 flex justify-center items-center rounded-l-full">
            <svg id="41:92858" className="w-[94px] h-[94px]"></svg>
            <div className="text-white text-center text-[32px] font-semibold leading-[20px]">
              경제
            </div>
          </button>
          <button className="w-full h-[64px] bg-green-800 flex justify-center items-center rounded-l-full">
            <svg id="41:92845" className="w-[94px] h-[94px]"></svg>
            <div className="text-white text-center text-[32px] font-semibold leading-[20px]">
              사회
            </div>
          </button>
          <button className="w-full h-[64px] bg-blue-800 flex justify-center items-center rounded-l-full">
            <svg id="41:92849" className="w-[94px] h-[94px]"></svg>
            <div className="text-white text-center text-[32px] font-semibold leading-[20px]">
              세계
            </div>
          </button>
        </div>
      </div>

      {isLoggedIn ? (
        <div className="absolute flex bottom-0 left-0 ">
          <button className="w-[453px] h-[215px] bg-purple-600 flex flex-col items-center rounded-t-lg p-4 gap-2">
            <div className="flex justify-center items-center text-center">
              <div className="text-white text-7xl font-semibold">
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

          <button className="w-[453px] h-[215px] bg-orange-600 flex flex-col items-center rounded-t-lg p-4 gap-2">
            <div className="flex justify-center items-center text-right gap-4">
              <div className="text-white text-7xl font-semibold">
                TODAY QUIZ
              </div>
              <div className="p-4 m-2 bg-orange-300 rounded-full">
                <Lock className="text-orange-800 text-[80px]" />
              </div>
            </div>
            <div className="text-center text-orange-200 opacity-80 text-[12px]">
              <p>로그인하고 오늘의 퀴즈를 풀어보세요!</p>
              <p>퀴즈는 Daily News의 내용을 바탕으로 만들어집니다.</p>
            </div>
          </button>
        </div>
      ) : (
        <div className="absolute flex bottom-0 left-0 ">
          <button
            className="w-[453px] h-[215px] bg-purple-600 flex flex-col items-center rounded-t-lg p-4 gap-2"
            onClick={openModal}
          >
            <div className="flex justify-center items-center text-center">
              <div className="text-white text-7xl font-semibold">
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
            className="w-[453px] h-[215px] bg-orange-600 flex flex-col items-center rounded-t-lg p-4 gap-2"
            onClick={openModal}
          >
            <div className="flex justify-center items-center text-right gap-4">
              <div className="text-white text-7xl font-semibold">
                TODAY QUIZ
              </div>
              <div className="p-4 m-2 bg-orange-300 rounded-full">
                <Lock className="text-orange-800 text-[80px]" />
              </div>
            </div>
            <div className="text-center text-orange-200 opacity-80 text-[12px]">
              <p>로그인하고 오늘의 퀴즈를 풀어보세요!</p>
              <p>퀴즈는 Daily News의 내용을 바탕으로 만들어집니다.</p>
            </div>
          </button>
        </div>
      )}
      <SocialLoginModal isOpen={isModalOpen} onClose={closeModal} />
    </div>
  );
};
