/* eslint-disable react/prop-types */
import { useState } from "react";
import cross from "shared/images/cross.png";
import file from "shared/images/file.png";
import card from "shared/images/card.svg";
import arrowBack from "shared/images/arrowBack.png";
import arrowFront from "shared/images/arrowForward.png";

import "./cardListModal.css"

const CardListModal = ({ onClose, cardNum }) => {
  const cardsPerPage = 8; // 한 페이지당 보여줄 카드 수
  const [currentPage, setCurrentPage] = useState(0); // 현재 페이지 상태 관리
  const [animationDirection, setAnimationDirection] = useState(""); // 애니메이션 방향 관리

  // 총 페이지 수 계산
  const totalPages = Math.ceil(cardNum / cardsPerPage);

  // 현재 페이지에 보여줄 카드 목록
  const currentCards = Array.from({ length: cardNum })
    .slice(currentPage * cardsPerPage, (currentPage + 1) * cardsPerPage);

  // 이전 페이지로 이동
  const handlePrevPage = () => {
    if (currentPage > 0) {
      setAnimationDirection("fade-out");
      setTimeout(() => {
        setCurrentPage(currentPage - 1);
        setAnimationDirection("fade-in");
      }, 300);
    }
  };

  // 다음 페이지로 이동
  const handleNextPage = () => {
    if (currentPage < totalPages - 1) {
      setAnimationDirection("fade-out");
      setTimeout(() => {
        setCurrentPage(currentPage + 1);
        setAnimationDirection("fade-in");
      }, 300);
    }
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div className="w-[650px] h-[670px] rounded-[24px] bg-white border-2 border-gray-300 shadow-lg flex flex-col">
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
            {currentCards.map((_, index) => (
              <button key={index} className="w-50 h-50 bg-transparent flex items-center justify-center">
                <img src={card} alt={`Card ${index}`} className="w-full h-full object-cover" />
              </button>
            ))}
          </div>
        </div>

        {/* 페이지 네비게이션 */}
        {cardNum > cardsPerPage && (
          <footer className="flex justify-between items-center px-4 py-2 mb-10">
            <button
              className={`w-10 h-10 flex items-center justify-center ${currentPage === 0 ? "opacity-50 cursor-not-allowed" : ""}`}
              onClick={handlePrevPage}
              disabled={currentPage === 0}
            >
              <img src={arrowBack} className="w-full h-full object-cover" />
            </button>

            <span className="text-gray-700 font-semibold text-xl">
              {currentPage + 1} / {totalPages}
            </span>

            <button
              className={`w-10 h-10 flex items-center justify-center ${currentPage === totalPages - 1 ? "opacity-50 cursor-not-allowed" : ""}`}
              onClick={handleNextPage}
              disabled={currentPage === totalPages - 1}
            >
              <img src={arrowFront} className="w-full h-full object-cover" />
            </button>
          </footer>
        )}
      </div>
    </div>
  );
};

export default CardListModal;
