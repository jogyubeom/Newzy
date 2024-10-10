import { useNavigate } from "react-router-dom";
import useAuthStore from "shared/store/userStore";
import useHomeStore from "../store/useHomeStore";
import { KoreanWeek } from "shared/utils/dateUtils";

export const WeekCardWinner = () => {
  const { userInfo } = useAuthStore();
  const { weekCardWinner } = useHomeStore();
  const navigate = useNavigate();

  const handleCardWinner = (nickname) =>
    nickname !== userInfo.nickname
      ? navigate(`profile/${nickname}`)
      : navigate("profile");

  const now = new Date();

  return (
    <div className="w-full ">
      <div className="flex justify-between items-center w-[639px] h-[188px] p-6 bg-[#002F10] rounded-lg overflow-hidden">
        {/* Content Section */}
        <div className="flex flex-col items-center space-y-2 w-[296px]">
          <div className="flex justify-center items-center space-x-2 w-full">
            <span className="text-white text-xl font-semibold">
              {KoreanWeek(now)} 카드왕
            </span>
            <svg className="w-5 h-5"></svg>
          </div>
          <div className="text-[#CACACA] text-xs">획득한 카드 개수</div>
          <div className="w-[70px] border border-gray-300 transform -rotate-1"></div>
          <div className="flex justify-center items-center w-full">
            <span className="text-white text-lg font-semibold">
              {weekCardWinner?.count}개
            </span>
          </div>
        </div>

        {/* Avatar Section */}
        <button
          className="flex flex-col items-center space-y-2 w-[295px]"
          onClick={() => handleCardWinner(weekCardWinner?.nickname)}
        >
          <div className="relative w-[104px] h-[104px] rounded-full shadow-md">
            <img
              src={weekCardWinner?.profile}
              alt="Avatar"
              className="w-full h-full rounded-full object-cover shadow-[0px_0px_20px_5px_rgba(255,203,1,1)]"
            />
            <svg className="absolute bottom-[-8px] right-[-6.5px] w-[44px] h-[43px]"></svg>
          </div>
          <div className="text-white text-lg font-semibold text-center w-full text-shadow-md">
            {weekCardWinner?.nickname}
          </div>
        </button>
      </div>
    </div>
  );
};
