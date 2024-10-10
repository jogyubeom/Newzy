import { useNavigate } from "react-router-dom";
import useAuthStore from "shared/store/userStore";
import useHomeStore from "../store/useHomeStore";
import { KoreanWeek } from "shared/utils/dateUtils";
import { FaRegHeart } from "react-icons/fa6";

export const WeekNewpoter = () => {
  const { userInfo } = useAuthStore();
  const { weekNewporter } = useHomeStore();
  const navigate = useNavigate();

  const handleNewporter = (nickname) =>
    nickname !== userInfo.nickname
      ? navigate(`profile/${nickname}`)
      : navigate("profile");

  const now = new Date();

  return (
    <div>
      <div className="panel flex flex-col justify-center items-center gap-4 px-4 pb-5 rounded-lg bg-gray-200">
        <div className="group-36 flex flex-row justify-start items-center gap-1 ">
          <div className="text-[20px] font-semibold text-gray-800">
            {KoreanWeek(now)} 뉴포터
          </div>
          <svg id="41:008477" className="information-line w-5 h-5"></svg>
        </div>

        <div className="user-list flex flex-row justify-start items-start gap-5 w-full h-[129px] px-1.5">
          {weekNewporter?.map((newporter, index) => (
            <div
              key={index}
              className="flex flex-col justify-start items-center gap-2 w-[125.67px] h-[129px]"
            >
              <button onClick={() => handleNewporter(newporter.nickname)}>
                <div className="avatar relative w-[75px] h-[75px]">
                  <img
                    src={newporter.profile}
                    alt={`${newporter.nickname} Avatar`}
                    className="absolute top-0 left-0 w-[75px] h-[75px] rounded-full object-cover shadow-md"
                  />
                </div>
                <div className="text-[14px] font-semibold text-center w-full truncate">
                  {newporter.nickname}
                </div>
              </button>
              <div className="flex items-center justify-center gap-1 text-[16px] text-gray-600">
                <FaRegHeart />
                <p>{newporter.count}</p>
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};
