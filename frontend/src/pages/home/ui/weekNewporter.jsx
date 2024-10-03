import { FaRegHeart } from "react-icons/fa6";

const dummyData = [
  {
    newporterid: 1,
    nickname: "장하권",
    like_cnt: 31,
    profile_img: "https://i.pravatar.cc/75?random=1",
  },
  {
    newporterid: 2,
    nickname: "이영원",
    like_cnt: 28,
    profile_img: "https://i.pravatar.cc/75?random=2",
  },
  {
    newporterid: 3,
    nickname: "이다율",
    like_cnt: 15,
    profile_img: "https://i.pravatar.cc/75?random=3",
  },
];

export const WeekNewpoter = () => {
  return (
    <div>
      <div className="panel flex flex-col justify-start items-start gap-4 px-4 pb-5 bg-gray-200">
        <div className="group-36 flex flex-row justify-start items-center gap-1 ">
          <div className="text-[20px] font-semibold text-gray-800">
            9월 둘째주의 뉴포터
          </div>
          <svg id="41:008477" className="information-line w-5 h-5"></svg>
        </div>

        <div className="user-list flex flex-row justify-start items-start gap-5 w-full h-[129px] px-1.5">
          {dummyData.map((user) => (
            <div
              key={user.newporterid}
              className="flex flex-col justify-start items-center gap-2 w-[125.67px] h-[129px]"
            >
              <div className="avatar relative w-[75px] h-[75px]">
                <img
                  src={user.profile_img}
                  alt={`${user.nickname} Avatar`}
                  className="absolute top-0 left-0 w-[75px] h-[75px] rounded-full object-cover shadow-md"
                />
              </div>
              <div className="text-[14px] font-semibold text-center w-full truncate">
                {user.nickname}
              </div>
              <div className="flex items-center justify-center gap-1 text-[16px] text-gray-600">
                <FaRegHeart />
                <p>{user.like_cnt}</p>
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};
