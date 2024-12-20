import { useNavigate } from "react-router-dom";
import useHomeStore from "../store/useHomeStore";
import { formatKoreanDate } from "shared/utils/dateUtils";

export const HotNewzy = () => {
  const navigate = useNavigate();
  const { hotNewzy } = useHomeStore();
  const handleHotNewzy = (id) => navigate(`/newzy/detail/${id}`);

  return (
    <div className="flex flex-col">
      <h2 className="text-lg font-semibold mb-4">많이 본 뉴지</h2>
      <div className="grid sm:grid-cols-1 md:grid-cols-2 lg:grid-cols-1 xl:grid-cols-2 gap-4">
        {hotNewzy &&
          hotNewzy.map((item) => (
            <button
              key={item.newzyId}
              className="w-[312px] h-[180px] shadow-lg rounded-lg flex flex-row justify-start items-center gap-4 p-4 overflow-hidden bg-[#F7F4F4] hover:shadow-xl transition-shadow"
              onClick={() => handleHotNewzy(item.newzyId)}
            >
              {/* Text 컨텐츠 */}
              <div className="flex h-full flex-col items-start gap-2">
                <div className="flex flex-col gap-1">
                  {/* Title */}
                  <div className="w-full text-[#26262C] text-start font-sans text-md leading-5 font-semibold">
                    {item.title}
                  </div>
                  {/* 날짜, 조회수 */}
                  <div className="flex items-center w-full text-xs text-gray-500 gap-1">
                    <span>{item.nickname}</span>·
                    <span>{formatKoreanDate(item.createdAt)}</span>·
                    <span className="flex">
                      <svg
                        xmlns="http://www.w3.org/2000/svg"
                        viewBox="0 0 16 16"
                        fill="currentColor"
                        className="w-4 h-4 mr-1"
                      >
                        <path d="M8 9.5a1.5 1.5 0 1 0 0-3 1.5 1.5 0 0 0 0 3Z" />
                        <path
                          fillRule="evenodd"
                          d="M1.38 8.28a.87.87 0 0 1 0-.566 7.003 7.003 0 0 1 13.238.006.87.87 0 0 1 0 .566A7.003 7.003 0 0 1 1.379 8.28ZM11 8a3 3 0 1 1-6 0 3 3 0 0 1 6 0Z"
                          clipRule="evenodd"
                        />
                      </svg>
                      {item.hit}
                    </span>
                  </div>
                </div>

                {/* Content (ellipsis 처리) */}
                <div
                  className="flex-grow flex items-center w-full text-[#747483] text-start text-sm font-normal overflow-hidden whitespace-normal"
                  style={{
                    display: "-webkit-box",
                    WebkitLineClamp: 3,
                    WebkitBoxOrient: "vertical",
                  }}
                >
                  {item.contentText}
                </div>
              </div>
              {/* Thumbnail 이미지 */}
              {item.thumbnail && ( //썸네일이 있다면...
                <img
                  src={item.thumbnail}
                  className="flex-shrink-0 w-[76px] h-full rounded-lg object-cover"
                  alt={item.title}
                />
              )}
            </button>
          ))}
      </div>
    </div>
  );
};
