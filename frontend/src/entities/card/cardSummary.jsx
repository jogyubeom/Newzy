import { useState } from "react";
import { useCardStore, useNewsCardStore } from "./store/cardStore";

import {
  BsEmojiDizzyFill as Hard,
  BsEmojiSmileFill as Normal,
  BsEmojiSunglassesFill as Easy,
} from "react-icons/bs";

const difficultyItems = [
  { label: "어려워요", icon: <Hard />, value: 0 },
  { label: "보통이예요", icon: <Normal />, value: 1 },
  { label: "쉬워요", icon: <Easy />, value: 2 },
];

export const CardSummary = ({ onAcquire, onClose, news }) => {
  const {
    summaryText,
    setSummaryText,
    inputLength,
    setInputLength,
    trimmedLength,
    setTrimmedLength,
  } = useCardStore();
  const { postNewsCard } = useNewsCardStore();
  const [modalMessage, setModalMessage] = useState("");
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [userDifficulty, setUserDifficulty] = useState(null);
  const [isAnimating, setIsAnimating] = useState(false);

  const { newsId, category } = news;

  const minLength = 10; // 공백 제외 최소 글자 수
  const maxLength = 150; // 최대 글자 수

  // 텍스트 인풋 핸들러
  const handleInputChange = (e) => {
    let value = e.target.value;
    const currentLength = value.length;

    // 글자 수가 maxLength를 초과하면 자르기
    if (currentLength > maxLength) {
      value = value.substring(0, maxLength); // 최대 글자 수를 넘지 않도록 자름
      setModalMessage(`${maxLength}자 이하로 입력해 주세요.`);
      setIsModalVisible(true);
      setTimeout(() => setIsModalVisible(false), 1500);
    }

    // 상태 업데이트
    setSummaryText(value);
    setInputLength(value.length); // 입력된 글자 수 (공백 포함)
    setTrimmedLength(value.replace(/\s/g, "").length); // 공백 제거한 글자 수
  };

  const handleDifficultySelect = (value) => {
    setUserDifficulty(value);
  };

  const handleSubmit = async () => {
    if (trimmedLength < minLength) {
      setModalMessage(`공백을 제외하고 최소 ${minLength}자 이상 입력해 주세요`);
      setIsModalVisible(true);
      setTimeout(() => setIsModalVisible(false), 1500);
    } else if (userDifficulty === null) {
      setModalMessage(`난이도를 선택해 주세요.`);
      setIsModalVisible(true);
      setTimeout(() => setIsModalVisible(false), 1500);
      return;
    } else {
      // 애니메이션 발동
      setIsAnimating(true);

      // console.log(newsId, userDifficulty, summaryText);
      // 카드 post 요청
      postNewsCard(category, newsId, userDifficulty, summaryText);

      setTimeout(() => {
        onAcquire(summaryText, userDifficulty); // 업데이트된 값을 전달
        setIsAnimating(false);
      }, 1500);
    }
  };

  return (
    <div
      className={`w-[360px] h-[480px] rounded-2xl flex flex-col items-center justify-start gap-3 p-6 bg-[#9E79BC] ${
        isAnimating ? "animate-spin-y" : ""
      }`}
    >
      <div className="text-white text-3xl font-card text-center">Newzy</div>
      <div className="flex flex-col items-center">
        <div className="text-white text-lg font-semibold">
          기사를 요약하고 카드를 획득하세요.
        </div>
        <div className="flex flex-col w-full border border-[#F9E7FF] rounded-md p-2 mt-2">
          <textarea
            placeholder="기사의 핵심 내용을 간단히 요약해 보세요. 이 요약은 나의 페이지의 카드 뒷면에서 확인할 수 있습니다."
            className="text-white w-full h-36 text-sm mt-2 bg-transparent resize-none focus:outline-none placeholder-[#F9E7FF]"
            value={summaryText}
            onChange={handleInputChange}
          ></textarea>
          <div className="text-[#F9E7FF] text-xs text-end mt-1">
            {inputLength}/{maxLength}
          </div>
        </div>

        <div className="flex flex-col w-full mt-4 gap-2 justify-center ">
          <div className="text-white text-sm font-semibold text-center">
            이 기사는 읽기에 어땠나요?
          </div>
          <div className="flex items-center justify-center gap-6">
            {difficultyItems.map((item, index) => (
              <div
                key={index}
                className="flex flex-col items-center w-[50px] gap-1 "
              >
                <button
                  className={`rounded-full ${
                    userDifficulty === item.value
                      ? "bg-gray-700 text-yellow-400"
                      : "bg-white text-gray-300"
                  } hover:text-yellow-300 text-3xl`}
                  onClick={() => handleDifficultySelect(item.value)}
                >
                  {item.icon}
                </button>
                <span className="mt-1 text-[10px] text-white">
                  {item.label}
                </span>
              </div>
            ))}
          </div>
        </div>

        <div className="absolute px-8 w-full bottom-6 flex items-center">
          <button
            className="w-full h-12 rounded-md 
             bg-[#5E007E] text-white text-lg font-semibold"
            onClick={handleSubmit}
          >
            카드 획득
          </button>
        </div>
      </div>

      {/* 모달 컴포넌트 */}
      {isModalVisible && (
        <div className="absolute">
          <div className="bg-white p-4 rounded-md shadow-md">
            <p className="text-black text-sm text-header">{modalMessage}</p>
          </div>
        </div>
      )}
    </div>
  );
};
