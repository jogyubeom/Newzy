import { useState } from "react";
import { FaAngleLeft } from "react-icons/fa6";
import { FaAngleRight } from "react-icons/fa6";

export const RecommendNews = () => {
  const [currentIndex, setCurrentIndex] = useState(0);

  // 더미 뉴스 카드 데이터
  const newsCards = [
    {
      newsId: 1,
      summary: "This is the summary of News 1",
      thumbnail: "https://picsum.photos/300?random=1",
    },
    {
      newsId: 2,
      summary: "This is the summary of News 2",
      thumbnail: "https://picsum.photos/300?random=2",
    },
    {
      newsId: 3,
      summary: "This is the summary of News 3",
      thumbnail: "https://picsum.photos/300?random=3",
    },
    {
      newsId: 4,
      summary: "This is the summary of News 4",
      thumbnail: "https://picsum.photos/300?random=4",
    },
    {
      newsId: 5,
      summary: "This is the summary of News 5",
      thumbnail: "https://picsum.photos/300?random=5",
    },
    {
      newsId: 6,
      summary: "This is the summary of News 6",
      thumbnail: "https://picsum.photos/300?random=6",
    },
    {
      newsId: 7,
      summary: "This is the summary of News 7",
      thumbnail: "https://picsum.photos/300?random=7",
    },
    {
      newsId: 8,
      summary: "This is the summary of News 8",
      thumbnail: "https://picsum.photos/300?random=8",
    },
  ];

  // 슬라이드 이동 핸들러
  const prevSlide = () => {
    setCurrentIndex((prev) => (prev === 0 ? newsCards.length - 4 : prev - 1));
  };

  const nextSlide = () => {
    setCurrentIndex((prev) => (prev === newsCards.length - 4 ? 0 : prev + 1));
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
            transform: `translateX(-${currentIndex * 25}%)`, // 4개 뉴스 카드 보여주기 -> 25%
          }}
        >
          {newsCards.map((news) => (
            <div
              key={news.newsId}
              className="min-w-[25%] p-4 hover:duration-150"
            >
              {/* 뉴스 카드 디자인 */}
              <div className="rounded-lg shadow-lg overflow-hidden">
                <img
                  src={news.thumbnail}
                  alt={`Thumbnail for news ${news.newsId}`}
                  className="w-full h-full object-cover"
                />
                <div className="p-4">
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
