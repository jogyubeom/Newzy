// entities/card/cardGauge.jsx

import { useEffect, useState } from "react";
import { CardSummary } from "./cardSummary";
import { CardBack } from "./cardBack";

export const CardGauge = ({ news }) => {
  const [scrollPercent, setScrollPercent] = useState(0);
  const [isComplete, setIsComplete] = useState(false); // 카드 읽은 상태
  const [isCardAcquired, setIsCardAcquired] = useState(false); // 카드 획득 여부 상태
  const [summary, setSummary] = useState(""); // 요약 상태
  const [userDifficulty, setUserDifficulty] = useState(0); // 난이도 평가 (0: 쉬움, 1: 보통, 2: 어려움)
  const [modalStep, setModalStep] = useState(0); // 모달 상태 (0: 닫힘, 1: 요약 모달, 2: 뒷면 모달)

  useEffect(() => {
    const handleScroll = () => {
      const scrollTop =
        window.pageYOffset || document.documentElement.scrollTop;
      const scrollHeight =
        document.documentElement.scrollHeight - window.innerHeight;
      const scrollPercentage = (scrollTop / scrollHeight) * 100;

      // 스크롤이 100%에 도달했을 때 고정
      if (scrollPercentage >= 100) {
        setScrollPercent(100);
        setIsComplete(true);
      } else if (!isComplete) {
        // 스크롤이 완료되지 않았을 때만 업데이트
        setScrollPercent(scrollPercentage);
      }
    };

    window.addEventListener("scroll", handleScroll);
    return () => window.removeEventListener("scroll", handleScroll);
  }, [isComplete]); // isComplete가 바뀔 때만 다시 실행

  const pingAnimation = `
  @keyframes ping-border {
    0% {
      transform: scale(1);
      opacity: 1;
    }
    100% {
      transform: scale(1.2); /* 보더가 조금 커지면서 */
      opacity: 0; /* 점차 사라지게 */
    }
  }
`;

  const handleCardBack = (summaryText, userDifficulty) => {
    setSummary(summaryText); // 요약 내용 저장
    setUserDifficulty(userDifficulty);
    setIsCardAcquired(true);
    setModalStep(2); // 카드 뒷면 모달로 전환
    console.log(summaryText);
  };

  const closeModal = () => {
    setModalStep(0); // 모달 닫기
  };

  const handleCardClick = () => {
    if (isCardAcquired) {
      setModalStep(2); // 카드 뒷면 모달 열기
    } else if (isComplete) {
      setModalStep(1); // 요약 모달 열기
    }
  };
  return (
    <div className="fixed w-[120px] h-[172px] bottom-7 left-3 bg-gray-300 rounded-lg shadow-lg ">
      {isComplete && (
        <div
          className="absolute inset-0 rounded-lg border-2 border-white"
          style={{
            animation: "ping-border 1.5s cubic-bezier(0, 0, 0.2, 1) infinite",
          }}
        />
      )}

      {/* Ping 애니메이션을 위한 스타일 */}
      <style>{pingAnimation}</style>

      {/* 카드 획득 후 썸네일과 제목 표시 */}
      {isCardAcquired ? (
        <div className="relative" onClick={handleCardClick}>
          <div
            className="absolute bottom-0 w-full p-2 rounded-lg text-white font-bold text-sm"
            style={{
              boxShadow: "inset 0 0 100px rgba(0, 0, 0, 0.25)",
            }}
          >
            {news.title}
          </div>
          {news?.thumbnail ? (
            <img
              src={news.thumbnail}
              alt={news.title}
              className="w-full h-[172px] object-cover rounded-lg"
            />
          ) : (
            <div className=" w-full h-[172px] rounded-lg bg-purple-300 flex justify-center pt-4">
              <span className="text-white font-card text-2xl">Newzy</span>
            </div>
          )}
        </div>
      ) : (
        <>
          <div
            className={`absolute ${
              isComplete ? "rounded-lg" : "rounded-b-lg"
            } bg-purple-600`}
            style={{
              width: "100%",
              height: `${isComplete ? 100 : scrollPercent}%`, // isComplete가 true일 때 100%로 고정
              bottom: 0,
            }}
            onClick={handleCardClick} // 클릭 핸들러 추가
          />
          <span className="absolute bottom-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 text-white text-2xl transition-opacity font-card duration-300">
            Newzy
          </span>
        </>
      )}

      {/* 모달 */}
      {modalStep > 0 && (
        <div
          className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50 z-50"
          onClick={closeModal}
        >
          <div className="relative z-50" onClick={(e) => e.stopPropagation()}>
            {modalStep === 1 ? (
              <CardSummary
                onAcquire={handleCardBack}
                onClose={closeModal}
                news={news}
              />
            ) : (
              <CardBack
                summary={summary}
                userDifficulty={userDifficulty}
                news={news}
                onClose={closeModal}
              />
            )}
          </div>
        </div>
      )}
    </div>
  );
};
