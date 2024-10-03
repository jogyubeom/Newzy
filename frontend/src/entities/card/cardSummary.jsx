// entities/card/cradSummary.jsx

import { useState } from "react";
import { getByteLengthUTF8, checkByteLength } from "shared/utils/stringUtils";
import {
  BsEmojiDizzyFill as Hard,
  BsEmojiSmileFill as Normal,
  BsEmojiSunglassesFill as Easy,
} from "react-icons/bs";

const difficultyItems = [
  { label: "쉬워요", icon: <Easy />, value: "easy" },
  { label: "보통이예요", icon: <Normal />, value: "normal" },
  { label: "어려워요", icon: <Hard />, value: "hard" },
];

export const CardSummary = ({ onAcquire, onClose }) => {
  const [inputText, setInputText] = useState("");
  const [byteLength, setByteLength] = useState(0);
  const [modalMessage, setModalMessage] = useState(""); // 모달 메시지
  const [isModalVisible, setIsModalVisible] = useState(false); // 모달 상태
  const [selectedDifficulty, setSelectedDifficulty] = useState(null);
  const [isAnimating, setIsAnimating] = useState(false);

  const minBytes = 10;
  const maxBytes = 450;

  const handleInputChange = (e) => {
    const value = e.target.value;
    const { byteLength, isExceeded } = checkByteLength(value, maxBytes);

    if (isExceeded) {
      setModalMessage(`${maxBytes}자 이하로 입력해주세요.`);
      setIsModalVisible(true);
      setTimeout(() => setIsModalVisible(false), 2000);
      return;
    }

    setInputText(value);
    setByteLength(getByteLengthUTF8(value)); // 공백 포함한 바이트 수 계산
  };

  const handleDifficultySelect = (value) => {
    setSelectedDifficulty(value);
  };

  const handleSubmit = (value) => {
    if (byteLength < minBytes) {
      setModalMessage(`최소 ${minBytes} 바이트 이상 입력해야 합니다.`);
      setIsModalVisible(true);
      setTimeout(() => setIsModalVisible(false), 1500);
    } else {
      // 애니메이션 발동
      setIsAnimating(true);
      setTimeout(() => {
        onAcquire(inputText); // 업데이트된 값을 전달
        // console.log(finalSummaryText, "카드 요약");
        setIsAnimating(false);
      }, 1500); // 애니메이션 효과가 끝난 후 상태 리셋
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
            placeholder="기사의 핵심 정보를 요약해 보세요. 요약 내용은 마이페이지에서 카드의 뒷면에서 확인할 수 있습니다."
            className="text-white w-full h-36 text-sm mt-2 text-center bg-transparent"
            value={inputText}
            onChange={handleInputChange}
          ></textarea>
          <div className="text-[#F9E7FF] text-xs text-end mt-1">
            {byteLength}/{maxBytes}
          </div>
        </div>

        <div className="flex flex-col w-full mt-4 gap-2 justify-center ">
          <div className="text-white text-sm font-semibold text-center">
            이 기사는 읽기에 어땠나요?
          </div>
          <div className="flex items-center justify-center gap-3">
            {difficultyItems.map((item, index) => (
              <div
                key={index}
                className="flex flex-col items-center gap-1 w-full"
              >
                <button
                  className={`rounded-full ${
                    selectedDifficulty === item.value
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
            className={
              "w-full h-12 rounded-md bg-[#5E007E] text-white text-lg font-semibold "
            }
            onClick={handleSubmit}
          >
            카드 획득
          </button>
        </div>
      </div>

      {/* 모달 컴포넌트 */}
      {isModalVisible && (
        <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50">
          <div className="bg-white p-4 rounded-md shadow-md">
            <p className="text-black">{modalMessage}</p>
          </div>
        </div>
      )}
    </div>
  );
};
