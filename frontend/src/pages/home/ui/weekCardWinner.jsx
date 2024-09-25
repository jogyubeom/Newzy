import { KoreanWeek } from "shared/utils/dateFomatter";

export const WeekCardWinner = () => {
  return (
    <div className="w-full ">
      <div className="flex justify-between items-center w-[639px] h-[188px] p-6 bg-[#002F10] rounded-lg overflow-hidden">
        {/* Content Section */}
        <div className="flex flex-col items-center space-y-2 w-[296px]">
          <div className="flex justify-center items-center space-x-2 w-full">
            <span className="text-white text-xl font-semibold">
              9월 둘째주의 카드왕
            </span>
            <svg className="w-5 h-5"></svg>
          </div>
          <div className="text-[#CACACA] text-xs">획득한 카드 개수</div>
          <div className="w-[70px] border border-gray-300 transform -rotate-1"></div>
          <div className="flex justify-center items-center w-full">
            <span className="text-white text-lg font-semibold">38개</span>
          </div>
          ss
        </div>

        {/* Avatar Section */}
        <div className="flex flex-col items-center space-y-2 w-[295px]">
          <div className="relative w-[104px] h-[104px] rounded-full shadow-md">
            <img
              src="https://image-resource.creatie.ai/136922663561920/136922663561929/924ab9a4eef3f01bd05192b2c648cdb9.jpg"
              alt="Avatar"
              className="w-full h-full rounded-full object-cover shadow-[0px_0px_20px_5px_rgba(255,203,1,1)]"
            />
            <svg className="absolute bottom-[-8px] right-[-6.5px] w-[44px] h-[43px]"></svg>
          </div>
          <div className="text-white text-lg font-semibold text-center w-full text-shadow-md">
            John Doe
          </div>
        </div>
      </div>
    </div>
  );
};
