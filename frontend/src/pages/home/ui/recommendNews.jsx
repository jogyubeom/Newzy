import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { FaAngleLeft } from "react-icons/fa6";
import { FaAngleRight } from "react-icons/fa6";
import useHomeStore from "../store/useHomeStore";

export const RecommendNews = () => {
  const navigate = useNavigate();
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

  const handleRecommendNews = (id) => navigate(`/news/${id}`);

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
              <button
                className="rounded-lg shadow-lg overflow-hidden flex flex-col h-full relative"
                onClick={() => handleRecommendNews(news.newsId)}
              >
                {/* 이미지 위에 요약 텍스트 */}
                <div
                  className="absolute bottom-0 left-0 w-full p-2 text-white font-bold text-lg"
                  style={{
                    boxShadow: "inset 0 0 200px rgba(0, 0, 0, 0.2)",
                  }}
                >
                  {news.summary}
                </div>
                {news.thumbnail !== "null" ? (
                  <img
                    src={news.thumbnail}
                    alt={`Thumbnail for news ${news.newsId}`}
                    className="w-full object-cover"
                    style={{
                      aspectRatio: "3 / 4",
                    }}
                  />
                ) : (
                  <div className="w-full h-full bg-purple-300 flex justify-center pt-4">
                    <span className="text-white font-card">Newzy</span>
                  </div>
                )}
              </button>
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
