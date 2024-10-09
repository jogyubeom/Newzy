/* eslint-disable react/prop-types */

import { useNewsCardStore } from "entities/card/store/cardStore";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import cross from "shared/images/cross.png";
import file from "shared/images/file.png";
import card from "shared/images/card.svg";
import arrowBack from "shared/images/arrowBack.png";
import arrowFront from "shared/images/arrowForward.png";
import { CardBack } from "entities/card/cardBack";
import "./cardListModal.css";

const CardListModal = ({ onClose }) => {
  const { newsCards, fetchNewsCard, totalPage } = useNewsCardStore();
  const [currentPage, setCurrentPage] = useState(1); // 현재 페이지 상태 관리
  const [animationDirection, setAnimationDirection] = useState(""); // 애니메이션 방향 관리

  const navigate = useNavigate();

  // 이전 페이지로 이동
  const handlePrevPage = () => {
    if (currentPage > 1) {
      setCurrentPage(currentPage - 1);
      setAnimationDirection("fade-out");
      setTimeout(() => {
        fetchNewsCard(currentPage);
        setAnimationDirection("fade-in");
      }, 300);
    }
  };

  // 다음 페이지로 이동
  const handleNextPage = () => {
    if (currentPage < totalPage) {
      setCurrentPage(currentPage + 1);
      setAnimationDirection("fade-out");
      setTimeout(() => {
        fetchNewsCard(currentPage);
        setAnimationDirection("fade-in");
      }, 300);
    }
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div className="w-[650px] h-[670px] rounded-[24px] bg-white border-2 border-gray-300 shadow-lg flex flex-col">
        {/* 헤더 */}
        <header className="flex justify-between items-center px-6 py-2 bg-[#3D3D3D] rounded-t-[22px]">
          <div className="flex items-center">
            <img src={file} className="w-9 h-9 object-cover" />
            <p className="ml-10 font-['Poppins'] text-[24px] font-semibold tracking-[-0.04em] text-[#FFFFFF]">
              Cards List
            </p>
          </div>
          <button
            className="w-[30px] h-[30px] flex items-center justify-center rounded-full"
            onClick={onClose}
          >
            <img src={cross} className="w-full h-full object-cover" />
          </button>
        </header>

        {/* 카드 리스트 */}
        <div className="flex-1 flex flex-col items-center justify-center overflow-hidden">
          <div
            className={`grid grid-cols-4 gap-4 p-4 transition-opacity duration-300 ${animationDirection}`}
          >
            {newsCards.map((newsCard) => (
              <button
                key={newsCard.cardId}
                className="bg-transparent flex items-center justify-center"
                onClick={() => navigate(`/newzy/${newsCard.newsId}`)}
              >
                <div className="w-full relative">
                  <div
                    className="absolute bottom-0 w-full p-2 rounded-lg text-white font-bold text-sm text-start"
                    style={{
                      boxShadow: "inset 0 0 100px rgba(0, 0, 0, 0.25)",
                    }}
                  >
                    {newsCard.newsTitle}
                  </div>
                  {newsCard?.thumbnailUrl ? (
                    <img
                      src={newsCard.thumbnailUrl}
                      alt={newsCard.newsTitle}
                      className="w-full h-[172px] object-cover rounded-lg"
                    />
                  ) : (
                    <div
                      className={`w-full h-[172px] rounded-lg ${
                        newsCard.category === 0
                          ? "bg-red-600"
                          : newsCard.category === 1
                          ? "bg-green-600"
                          : "bg-blue-600"
                      }  flex justify-center pt-4`}
                    >
                      <span className="text-white font-card text-2xl">
                        Newzy
                      </span>
                    </div>
                  )}
                </div>
              </button>
            ))}
          </div>
        </div>

        {/* 페이지 네비게이션 */}
        {totalPage > 1 && (
          <footer className="flex justify-between items-center px-4 py-2 mb-10">
            <button
              className={`w-10 h-10 flex items-center justify-center ${
                currentPage === 1 ? "opacity-50 cursor-not-allowed" : ""
              }`}
              onClick={handlePrevPage}
              disabled={currentPage === 1}
            >
              <img src={arrowBack} className="w-full h-full object-cover" />
            </button>

            <span className="text-gray-700 font-semibold text-xl">
              {currentPage} / {totalPage}
            </span>

            <button
              className={`w-10 h-10 flex items-center justify-center ${
                currentPage === totalPage ? "opacity-50 cursor-not-allowed" : ""
              }`}
              onClick={handleNextPage}
              disabled={currentPage === totalPage}
            >
              <img src={arrowFront} className="w-full h-full object-cover" />
            </button>
          </footer>
        )}
      </div>
      <CardBack />
    </div>
  );
};

export default CardListModal;
