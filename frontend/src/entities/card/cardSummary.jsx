// entities/card/cradSummary.jsx

import { useState } from "react";
import { getByteLengthUTF8, checkByteLength } from "shared/utils/stringUtils";

export const CardSummary = ({ onAcquire, onClose }) => {
  const [inputText, setInputText] = useState("");
  const [byteLength, setByteLength] = useState(0);
  const [summaryText, setSummaryText] = useState("");
  const [modalMessage, setModalMessage] = useState(""); // 모달 메시지
  const [isModalVisible, setIsModalVisible] = useState(false); // 모달 상태
  const [selectedDifficulty, setSelectedDifficulty] = useState(null);
  const [isAnimating, setIsAnimating] = useState(false);

  const minBytes = 10;
  const maxBytes = 300;

  const handleInputChange = (e) => {
    const value = e.target.value;
    const { byteLength, isExceeded } = checkByteLength(value, maxBytes);

    if (isExceeded) {
      setModalMessage("200자 이하로 입력해주세요.");
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
      const finalSummaryText = inputText; // textarea의 현재 값을 사용

      // 애니메이션 발동
      setIsAnimating(true);
      setTimeout(() => {
        setSummaryText(finalSummaryText);
        onAcquire(finalSummaryText); // 업데이트된 값을 전달

        // console.log(finalSummaryText, "카드 요약");
        setIsAnimating(false);
      }, 1500); // 애니메이션 효과가 끝난 후 상태 리셋
    }
  };

  return (
    <div
      className={`w-[360px] h-[480px] rounded-2xl flex flex-col items-center justify-start gap-6 p-6 bg-[#9E79BC] ${
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
            className="text-white w-full h-40 text-sm mt-2 text-center bg-transparent"
            value={inputText}
            onChange={handleInputChange}
          ></textarea>
          <div className="text-[#F9E7FF] text-xs text-end mt-1">
            {byteLength}/{maxBytes}
          </div>
        </div>
        <div className="flex justify-between w-full mt-4">
          <div className="text-white text-lg font-semibold">이 기사는</div>
          <div className="flex items-center justify-center gap-3">
            {/* 쉬워요 버튼 */}
            <button
              className={`px-4 py-2 rounded-md text-xs ${
                selectedDifficulty === "easy"
                  ? "bg-[#3578FF] text-white"
                  : "border border-[#3578FF] text-[#3578FF]"
              }`}
              onClick={() => handleDifficultySelect("easy")}
            >
              쉬워요
            </button>

            {/* 보통이예요 버튼 */}
            <button
              className={`px-4 py-2 rounded-md text-xs ${
                selectedDifficulty === "normal"
                  ? "bg-[#2BDB65] text-white"
                  : "border border-[#2BDB65] text-[#2BDB65]"
              }`}
              onClick={() => handleDifficultySelect("normal")}
            >
              보통이예요
            </button>

            {/* 어려워요 버튼 */}
            <button
              className={`px-4 py-2 rounded-md text-xs ${
                selectedDifficulty === "hard"
                  ? "bg-[#FF3535] text-white"
                  : "border border-[#FF3535] text-[#FF3535]"
              }`}
              onClick={() => handleDifficultySelect("hard")}
            >
              어려워요
            </button>
          </div>
        </div>

        <div className="flex justify-center w-full mt-4">
          <button
            className={
              "w-full h-11 rounded-md flex items-center justify-center bg-[#5E007E] text-white text-lg font-semibold "
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
