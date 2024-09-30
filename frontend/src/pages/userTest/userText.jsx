import React, { useState } from "react";
import Modal from "./ui/Modal";
import { useNavigate } from "react-router-dom";

const vocabularyData = {
  economy: [
    "경제학", "인플레이션", "디플레이션", "주식", "채권",
    "환율", "금리", "재무제표", "GDP", "경기순환",
    "거시경제", "미시경제", "투자", "이자율", "무역",
    "외환", "세금", "예산", "자본주의", "사회적책임"
  ],
  society: [
    "사회학", "문화", "인류학", "교육", "빈곤",
    "사회복지", "시민권", "평등", "인권", "정의",
    "복지국가", "청년실업", "다문화", "고령화", "민주주의",
    "사회의식", "보건", "주거문제", "환경운동", "시민단체"
  ],
  world: [
    "유엔", "세계화", "국제분쟁", "무역협정", "난민",
    "국제기구", "기후변화", "외교", "테러", "다국적기업",
    "세계은행", "유럽연합", "신흥국", "북극해", "이슬람문화",
    "원조", "국제법", "해양문제", "국제정치", "세계경제"
  ]
};

const categories = ["economy", "society", "world"];

export const UserTest = () => {
  const [currentCategoryIndex, setCurrentCategoryIndex] = useState(0);
  const [selectedWords, setSelectedWords] = useState({
    economy: [],
    society: [],
    world: []
  });
  const [isModalOpen, setIsModalOpen] = useState(true); // 모달 상태 관리

  const currentCategory = categories[currentCategoryIndex];

  const handleWordClick = (word) => {
    setSelectedWords((prevSelectedWords) => {
      const alreadySelected = prevSelectedWords[currentCategory].includes(word);
      const updatedWords = alreadySelected
        ? prevSelectedWords[currentCategory].filter((w) => w !== word)
        : [...prevSelectedWords[currentCategory], word];

      return {
        ...prevSelectedWords,
        [currentCategory]: updatedWords
      };
    });
  };

  const handleNext = () => {
    if (currentCategoryIndex < categories.length - 1) {
      setCurrentCategoryIndex(currentCategoryIndex + 1);
    } else {
      alert("테스트가 완료되었습니다!");
      console.log("아는 경제 단어 개수:", selectedWords.economy.length);
      console.log("아는 사회 단어 개수:", selectedWords.society.length);
      console.log("아는 세계 단어 개수:", selectedWords.world.length);
      nav('/')
    }
  };

  const nav = useNavigate()

  return (
    <div className="mx-auto py-10 px-28 bg-white rounded-lg shadow-lg">
      <Modal isOpen={isModalOpen} onClose={() => setIsModalOpen(false)}>
        <h2 className="text-xl font-bold mb-2">Newzy 어휘 테스트 안내</h2>
        <div className="mt-10">
          <p className="text-gray-600 font-semibold mt-3">당신의 어휘력을 뽐낼 시간입니다!</p>
          <p className="text-gray-600 font-semibold mt-3">하지만 너무 욕심내지 마세요 😉</p>
          <p className="text-gray-600 font-semibold mt-3">솔직하게 아는 단어만 골라주시면</p>
          <p className="text-gray-600 font-semibold mt-3">더 정확한 맞춤 콘텐츠를 추천드릴 수 있으니까요!</p>
          <p className="text-gray-600 font-semibold mt-3">🎯 이제 시작해볼까요?</p>
        </div>
      </Modal>

      <h2 className="text-2xl font-bold mb-4 text-center">
        {currentCategory === "economy" && "어휘 테스트 [경제]"}
        {currentCategory === "society" && "어휘 테스트 [사회]"}
        {currentCategory === "world" && "어휘 테스트 [세계]"}
      </h2>

      {/* 단어 목록 그리드 */}
      <div className="grid grid-cols-4 gap-7 py-4 px-10 bg-gray-100 rounded-lg shadow-inner">
        {vocabularyData[currentCategory].map((word, index) => (
          <button
            key={index}
            onClick={() => handleWordClick(word)}
            className={`p-2 border rounded text-center transition-transform duration-300 transform ${
              selectedWords[currentCategory].includes(word)
                ? "bg-blue-500 text-white font-semibold"
                : "bg-white text-gray-700 font-semibold"
            } hover:bg-blue-300 hover:text-white hover:scale-105`}
          >
            {word}
          </button>
        ))}
      </div>

      {/* 네비게이션 버튼 */}
      <div className="flex justify-between mt-6">
        <button
          onClick={() => setCurrentCategoryIndex(currentCategoryIndex - 1)}
          disabled={currentCategoryIndex === 0}
          className="px-4 py-2 bg-gray-300 rounded hover:bg-gray-400 disabled:opacity-50 disabled:cursor-not-allowed"
        >
          이전
        </button>
        <button
          onClick={handleNext}
          className="px-4 py-2 bg-green-500 text-white rounded hover:bg-green-600"
        >
          {currentCategoryIndex === categories.length - 1 ? "완료" : "다음"}
        </button>
      </div>
    </div>
  );
};
