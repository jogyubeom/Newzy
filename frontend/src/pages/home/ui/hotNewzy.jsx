import useHomeStore from "../store/useHomeStore";
import { formatKoreanDate } from "shared/utils/dateUtils";

export const HotNewzy = () => {
  const { hotNewzy } = useHomeStore();

  return (
    <div className="flex flex-col">
      <h2 className="text-lg font-semibold mb-4">많이 본 뉴지</h2>
      <div className="grid sm:grid-cols-1 md:grid-cols-2 lg:grid-cols-1 xl:grid-cols-2 gap-4">
        {hotNewzy &&
          hotNewzy.map((item) => (
            <div
              key={item.newzyId}
              className="w-[312px] h-[180px] shadow-lg rounded-lg flex flex-row justify-start items-center gap-4 p-4 overflow-hidden bg-[#F7F4F4]"
            >
              {/* Text 컨텐츠 */}
              <div className="flex h-full flex-col items-start gap-2">
                <div className="flex flex-col gap-1">
                  {/* Title */}
                  <div className="w-full text-[#26262C] font-sans text-md leading-5 font-semibold">
                    {item.title}
                  </div>
                  {/* 날짜, 조회수 */}
                  <div className="flex items-center w-full text-xs text-gray-500 gap-1">
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
                <div className="flex-grow flex items-center w-full text-[#747483] text-sm leading-4 font-normal overflow-hidden text-ellipsis whitespace-nowrap">
                  {item.content}
                </div>
              </div>
              {/* Thumbnail 이미지 */}
              {item.thumbnail && ( //썸네일이 있다면...
                <img
                  src={item.thumbnail || "https://via.placeholder.com/76"} // 썸네일 없을 때 기본 이미지
                  className="flex-shrink-0 w-[76px] h-[76px] rounded-lg object-cover"
                  alt={item.title}
                />
              )}
            </div>
          ))}
      </div>
    </div>
  );
};
