import { useState } from "react";
import { FaAngleLeft } from "react-icons/fa6";
import { FaAngleRight } from "react-icons/fa6";
import useHomeStore from "../store/useHomeStore";
export const RecommendNews = () => {
  const { recommendNews } = useHomeStore();
  const [currentIndex, setCurrentIndex] = useState(0);

  // 슬라이드 이동 핸들러
  const prevSlide = () => {
    setCurrentIndex((prev) =>
      prev === 0 ? recommendNews.length - 3 : prev - 1
    );
  };

  const nextSlide = () => {
    setCurrentIndex((prev) =>
      prev === recommendNews.length - 3 ? 0 : prev + 1
    );
  };

  return (
    <div className="flex items-center justify-center max-w-[100%]">
      {/* 왼쪽 버튼 */}
      <button onClick={prevSlide} className="p-3">
        <FaAngleLeft />
      </button>

      {/* 뉴스 카드 리스트 */}
      <div className="overflow-hidden flex-1 mx-4">
        <div
          className="flex transition-transform duration-300 ease-out"
          style={{
            transform: `translateX(-${currentIndex * 33.33}%)`, // 3개 뉴스 카드 보여주기 -> 25%
          }}
        >
          {recommendNews?.map((news) => (
            <div
              key={news.newsId}
              className="min-w-[33.33%] p-4 hover:duration-150 flex flex-col"
            >
              {/* 뉴스 카드 디자인 */}
              <div className="rounded-lg shadow-lg overflow-hidden flex flex-col h-full">
                {news.thumbnail !== "null" ? (
                  <img
                    src={news.thumbnail}
                    alt={`Thumbnail for news ${news.newsId}`}
                    className="w-full h-48 object-cover"
                  />
                ) : (
                  <div className="w-full h-48 bg-purple-300 flex items-center justify-center">
                    <span className="text-white font-card">Newzy</span>
                  </div>
                )}
                <div className="p-4 flex-1">
                  <p className="text-sm text-gray-700">{news.summary}</p>
                </div>
              </div>
            </div>
          ))}
        </div>
      </div>

      {/* 오른쪽 버튼 */}
      <button
        onClick={nextSlide}
        className="p-2 rounded-full hover:bg-gray-300"
      >
        <FaAngleRight />
      </button>
    </div>
  );
};
