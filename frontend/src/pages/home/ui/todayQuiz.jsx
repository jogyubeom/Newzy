import { useState } from "react";
import useHomeStore from "../store/useHomeStore";

export const TodayQuizModal = ({ isOpen, onClose }) => {
  const { todayNews } = useHomeStore();

  const [selectedOption, setSelectedOption] = useState(null);
  const [disabledOptions, setDisabledOptions] = useState([]);
  const [isAnswered, setIsAnswered] = useState(false);
  const [feedback, setFeedback] = useState("");

  const handleOptionClick = (optionIndex) => {
    setSelectedOption(optionIndex);

    if (optionIndex === parseInt(todayNews.answer)) {
      setFeedback("정답입니다!");
      setIsAnswered(true); // 정답을 맞추면 모든 버튼 비활성화
    } else {
      setFeedback("오답입니다. 다시 시도해보세요.");
      setDisabledOptions([...disabledOptions, optionIndex]); // 틀린 버튼만 비활성화
    }
  };

  if (!isOpen || !todayNews) return null;

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div className="bg-white w-[500px] h-[400px] rounded-lg p-6 shadow-lg relative">
        <h2 className="text-xl font-semibold text-center mb-4">
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
              className={`w-full p-3 rounded-lg text-white font-semibold transition-colors ${
                isAnswered
                  ? "bg-gray-300"
                  : disabledOptions.includes(index)
                  ? "bg-gray-500 cursor-not-allowed"
                  : "bg-blue-500 hover:bg-blue-600"
              }`}
              onClick={() => handleOptionClick(index)}
              disabled={isAnswered || disabledOptions.includes(index)} // 정답을 맞추면 모든 버튼 비활성화, 틀린 버튼은 계속 비활성화
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
