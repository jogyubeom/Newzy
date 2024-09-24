/* eslint-disable react/prop-types */
import { useState, useEffect } from "react";
import { MdCancel } from "react-icons/md";
import { RiCheckboxMultipleFill } from "react-icons/ri";

const WordTestModal = ({ isOpen, onClose, wordList }) => {
  const [currentQuestion, setCurrentQuestion] = useState(0);
  const [userAnswer, setUserAnswer] = useState("");
  const [showAnswer, setShowAnswer] = useState(false);

  // 모달이 열릴 때마다 초기화
  useEffect(() => {
    if (isOpen) {
      setCurrentQuestion(0);
      setUserAnswer("");
      setShowAnswer(false);
    }
  }, [isOpen]);

  if (!isOpen) return null;

  const currentWord = wordList[currentQuestion];

  const handleSubmit = () => {
    if (userAnswer.trim() === currentWord.name) {
      alert("정답입니다! 다음 문제로 넘어갑니다.");
      if (currentQuestion < wordList.length - 1) {
        setCurrentQuestion((prev) => prev + 1);
      } else {
        alert("모든 문제를 완료했습니다!");
        onClose();
      }
      setUserAnswer("");
      setShowAnswer(false);
    } else {
      alert("오답입니다. 다시 시도해보세요.");
    }
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div className="w-[650px] h-[670px] rounded-[24px] bg-white border-2 border-gray-300 shadow-lg">
        <header className="flex justify-between items-center px-6 py-4 bg-[#EBBDFE] rounded-t-[22px]">
          <div className="flex items-center">
            <RiCheckboxMultipleFill className="text-green-500 w-9 h-9" />
            <p className="ml-4 font-['Poppins'] text-[24px] font-semibold tracking-[-0.04em] text-[#BF2EF0]">
              Words Test
            </p>
          </div>
          <button
            className="w-9 h-9 flex items-center justify-center rounded-full"
            onClick={onClose}
          >
            <MdCancel className="w-10 h-10 text-red-500" />
          </button>
        </header>

        {/* 안내문 */}
        <div className="mt-5 flex items-center justify-center">
          <div className="font-['Poppins'] text-[18px] font-semibold leading-[28px] tracking-[-0.04em] text-[#91929F] bg-[#EEEEEE] py-3 px-10 rounded-[45px]">
            단어장을 바탕으로 랜덤으로 문제가 출제됩니다.
            <br />
            스티커를 클릭하면 정답을 확인할 수 있어요!
          </div>
        </div>

        {/* 문제 번호 */}
        <div className="my-6 flex justify-center">
          <span className="font-['Open_Sans'] text-[32px] font-semibold leading-[36px] text-center tracking-[-0.04em] text-[#BF2EF0]">
            {currentQuestion + 1} / {wordList.length}
          </span>
        </div>

        {/* 현재 단어의 뜻 */}
        <div className="mt-4 mx-auto w-[85%] px-4 py-8 border-2 border-[#BF2EF0] text-start rounded-[30px]">
          <p className="text-lg font-semibold font-['Poppins']">
            {currentWord.mean.map((meaning, index) => (
              <span key={index}>
                {meaning}
                <br />
              </span>
            ))}
          </p>
        </div>

        {/* 단어 입력 및 제출 버튼 */}
        <div className="mt-8 flex flex-col items-center">
          <input
            type="text"
            className="w-[80%] text-center text-xl border-none outline-none border-b-2 border-[#BF2EF0] focus:border-[#8B008B] transition duration-300"
            placeholder="정답을 입력하세요"
            value={userAnswer}
            onChange={(e) => setUserAnswer(e.target.value)}
            onKeyDown={(e) => e.key === 'Enter' && handleSubmit()}
          />
          <button
            className="mt-6 w-[150px] py-2 bg-gradient-to-r from-purple-500 to-pink-500 text-white font-bold rounded-full shadow-lg hover:bg-gradient-to-r hover:from-pink-500 hover:to-purple-500 transition duration-300"
            onClick={handleSubmit}
          >
            제출
          </button>
        </div>
      </div>
    </div>
  );
};

export default WordTestModal;
