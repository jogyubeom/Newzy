import { IoIosLock as Lock } from "react-icons/io";

export const Header = () => {
  return (
    <div className="relative h-[471px] bg-gray-800">
      <div className="absolute top-0 left-0 w-full h-[225px]">
        <div className="flex items-center justify-center text-white text-center w-full h-full text-[196px] leading-[24px] font-semibold font-poppins">
          Newzy
        </div>
        <div className="absolute top-[216px] left-[520px] flex items-center text-white text-[36px] leading-[20px] font-semibold font-open-sans">
          2024.09.05.THU
        </div>
      </div>

      <div className="absolute top-[225px] left-[348px] flex gap-[4px]">
        {/* Number content */}
      </div>

      <div className="absolute top-0 left-[906px] w-[324px] h-[471px] shadow-lg flex flex-col justify-end items-center gap-[16px] p-[24px]">
        <div className="w-[300px] h-[228px] flex flex-col items-center gap-[18px]">
          <div className="w-full h-[64px] bg-purple-200 flex justify-center items-center rounded-l-full">
            <svg id="41:92858" className="w-[94px] h-[94px]"></svg>
            <div className="text-white text-center text-[32px] font-semibold leading-[20px]">
              경제
            </div>
          </div>
          <div className="w-full h-[64px] bg-teal-200 flex justify-center items-center rounded-l-full">
            <svg id="41:92845" className="w-[94px] h-[94px]"></svg>
            <div className="text-white text-center text-[32px] font-semibold leading-[20px]">
              사회
            </div>
          </div>
          <div className="w-full h-[64px] bg-yellow-200 flex justify-center items-center rounded-l-full">
            <svg id="41:92849" className="w-[94px] h-[94px]"></svg>
            <div className="text-white text-center text-[32px] font-semibold leading-[20px]">
              세계
            </div>
          </div>
        </div>
      </div>

      <div className="absolute top-[256px] left-[453px] w-[453px] h-[215px] bg-yellow-200 flex flex-col justify-between items-center rounded-t-lg p-[16px]">
        <div className="w-full flex justify-center items-center">
          <div className="text-white text-[36px] font-semibold leading-[20px]">
            TODAY QUIZ
          </div>
        </div>
        <svg id="41:93898" className="w-[103px] h-[103px]"></svg>
        <div className="text-center text-yellow-600 opacity-80 text-[12px]">
          로그인하고 오늘의 퀴즈를 풀어보세요! 퀴즈는 Daily News의 내용을
          바탕으로 만들어집니다.
        </div>
      </div>

      <div className="absolute top-[256px] left-0 w-[453px] h-[215px] bg-teal-200 shadow-lg flex flex-col justify-between items-center rounded-t-lg p-[16px]">
        <div className="w-full flex justify-center items-center">
          <div className="text-white text-[36px] font-semibold leading-[20px]">
            TODAY NEWS
          </div>
        </div>
        <svg id="41:93920" className="w-[103px] h-[103px]"></svg>
        <div className="text-center text-teal-500 opacity-80 text-[12px]">
          로그인하고 오늘의 추천 뉴스를 확인해 보세요! 추천 뉴스는 나의 문해력,
          관심도에 따라 제공됩니다.
        </div>
      </div>
    </div>
  );
};
