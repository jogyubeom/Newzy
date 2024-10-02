// entities/card/cardBack.jsx

export const CardBack = ({ summary, category }) => {
  return (
    <div className="w-[360px] h-[480px] rounded-2xl flex flex-col items-center gap-y-4 py-4 bg-red-600">
      <div className="flex flex-col items-center gap-y-[3px] px-5 w-full">
        <div className="flex flex-col items-center gap-y-[8px] w-full">
          <div className="text-white text-4xl font-card flex justify-center items-center h-16">
            Newzy
          </div>
          <div className="flex flex-col gap-y-2 w-full">
            <div className="w-full border border-gray-300"></div>
            <div className="w-full border border-gray-300"></div>
          </div>
        </div>

        <div className="px-2 pt-1 w-full">
          <div className="flex justify-end items-center gap-x-[6px] w-full ">
            <div className="text-white text-[14px] font-[ChosunilboNM]">
              획득날짜 :
            </div>
            <div className="text-white text-[14px] font-[ChosunilboNM]">
              2024년 9월 30일
            </div>
          </div>

          <div className="flex justify-end items-center gap-x-[6px] w-full">
            <div className="text-white text-[14px] font-[ChosunilboNM]">
              체감난이도 :
            </div>
            <div className="text-white text-[14px] font-[ChosunilboNM]">
              쉬어요
            </div>
          </div>
        </div>
      </div>

      <div className="px-8 h-auto flex justify-center items-center">
        <p className="text-white text-[16px] font-[ChosunilboNM] leading-[25px] tracking-wider">
          {summary}
        </p>
      </div>

      <div className="absolute px-8 w-full bottom-6 flex items-center">
        <button className="w-full h-12 bg-red-700 rounded-md">
          <p className="text-white text-4 font-semibold">기사 다시 보기</p>
        </button>
      </div>
    </div>
  );
};
