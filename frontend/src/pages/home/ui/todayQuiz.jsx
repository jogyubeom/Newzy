import { useState, useEffect } from "react";
import useHomeStore from "../store/useHomeStore";

export const TodayQuizModal = ({ isOpen, onClose }) => {
  const { todayNews, postAnswer } = useHomeStore();
  const [selectedOption, setSelectedOption] = useState(null);
  const [disabledOptions, setDisabledOptions] = useState([]);
  const [isAnswered, setIsAnswered] = useState(false);
  const [feedback, setFeedback] = useState("");

  // 새로고침 시 isSolved에 따른 처리
  useEffect(() => {
    if (todayNews?.isSolved) {
      setSelectedOption(parseInt(todayNews.answer) - 1);
      setIsAnswered(true);
      setFeedback("오늘의 문제를 풀었어요. 내일 또 풀어봐요!");
    }
  }, [todayNews]);

  const handleOptionClick = (optionIndex) => {
    setSelectedOption(optionIndex);

    if (optionIndex === parseInt(todayNews.answer) - 1) {
      setFeedback("정답입니다!");
      setIsAnswered(true);
      postAnswer();
    } else {
      setFeedback("오답입니다. 다시 시도해보세요.");
      setDisabledOptions([...disabledOptions, optionIndex]);
    }
  };

  if (!isOpen || !todayNews) return null;

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div className="bg-white w-[500px] h-auto rounded-lg p-6 shadow-lg relative">
        <h2 className="text-2xl font-semibold text-center mb-6">
          {todayNews.question}
        </h2>
        <div className="grid grid-cols-2 gap-4">
          {[
            todayNews.option1,
            todayNews.option2,
            todayNews.option3,
            todayNews.option4,
          ].map((option, index) => (
            <button
              key={index}
              className={`w-full p-3 rounded-lg text-white font-semibold transition-colors
                ${
                  selectedOption === index
                    ? "bg-purple-700" // 정답 선택된 버튼은 보라색
                    : disabledOptions.includes(index) || isAnswered
                    ? "bg-gray-300 cursor-not-allowed" // 틀린 답이나 답변이 완료된 버튼은 회색
                    : "bg-purple-500 hover:bg-purple-400" // 선택 가능한 버튼은 파란색
                }`}
              onClick={() => handleOptionClick(index)}
              disabled={isAnswered || disabledOptions.includes(index)}
            >
              {option}
            </button>
          ))}
        </div>

        {feedback && (
          <div className="mt-6 text-center text-lg font-bold">{feedback}</div>
        )}

        <button
          onClick={onClose}
          className="absolute top-2 right-2 text-gray-500 text-xl"
        >
          X
        </button>
      </div>
    </div>
  );
};
