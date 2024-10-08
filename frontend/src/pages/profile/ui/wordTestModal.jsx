/* eslint-disable react/prop-types */
import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import cross from "shared/images/cross.png";
import { RiCheckboxMultipleFill } from "react-icons/ri";
import { FaFrown, FaSmile, FaMeh } from "react-icons/fa";
import baseAxios from "shared/utils/baseAxios";

const WordTestModal = ({ isOpen, onClose, wordList, userName, onWordsRemoved }) => {
  const [currentQuestion, setCurrentQuestion] = useState(0);
  const [userAnswer, setUserAnswer] = useState("");
  const [correctCount, setCorrectCount] = useState(0);
  const [incorrectCount, setIncorrectCount] = useState(0);
  const [showResult, setShowResult] = useState(false);
  const [shuffledWordList, setShuffledWordList] = useState([]);
  const [correctWords, setCorrectWords] = useState([]); // 맞힌 단어 리스트
  const [removeCorrectWords, setRemoveCorrectWords] = useState(false); // 체크박스 상태

  const nav = useNavigate();

  // 단어 리스트를 랜덤으로 섞기 위한 함수
  const shuffleArray = (array) => {
    return array
      .map((item) => ({ ...item, sort: Math.random() }))
      .sort((a, b) => a.sort - b.sort)
      .map(({ sort, ...item }) => item);
  };

  // 모달이 열릴 때마다 초기화
  useEffect(() => {
    if (isOpen && wordList.length > 0) {
      const shuffledList = shuffleArray(wordList).slice(0, 15); // 최대 15개의 단어만 추출
      setShuffledWordList(shuffledList);
      setCurrentQuestion(0);
      setUserAnswer("");
      setCorrectCount(0);
      setIncorrectCount(0);
      setShowResult(false);
      setCorrectWords([]);
      setRemoveCorrectWords(false);

      // 스크롤을 맨 위로 이동
      window.scrollTo(0, 0);
    }
  }, [isOpen, wordList]);

  if (!isOpen) return null;

  const currentWord = shuffledWordList[currentQuestion];

  // 점수 계산 함수
  const calculateScore = () => {
    const score = (correctCount / wordList.length) * 100;
    return Math.round(score); // 소수점 첫째 자리에서 반올림
  };

  // wordList가 비어있을 때 보여줄 안내창
  if (wordList.length === 0) {
    return (
      <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
        <div className="w-[420px] h-[180px] rounded-[24px] bg-white border-2 border-gray-300 shadow-lg flex flex-col items-center justify-center">
          <h2 className="text-xl font-bold mb-4 text-[#5E007E]">
            등록된 단어가 없습니다.
          </h2>
          <button
            className="mt-4 w-[100px] py-2 bg-gradient-to-r from-purple-500 to-pink-500 text-white font-bold rounded-full shadow-lg hover:bg-gradient-to-r hover:from-pink-500 hover:to-purple-500 transition duration-300"
            onClick={onClose}
          >
            확인
          </button>
        </div>
      </div>
    );
  }

  const handleSubmit = () => {
    if (userAnswer.trim() === currentWord.name) {
      alert("정답입니다! 다음 문제로 넘어갑니다.");
      setCorrectCount((prev) => prev + 1);
      setCorrectWords((prev) => [...prev, currentWord.name]); // 맞힌 단어 리스트에 추가
    } else {
      alert(
        `틀렸습니다. 정답은 "${currentWord.name}"입니다. 다음 문제로 넘어갑니다.`
      );
      setIncorrectCount((prev) => prev + 1);
    }

    if (currentQuestion < shuffledWordList.length - 1) {
      setCurrentQuestion((prev) => prev + 1);
    } else {
      setShowResult(true);
    }

    setUserAnswer("");
  };

  // 맞힌 단어들을 서버에서 삭제하는 함수
  const removeWordsFromList = async () => {
    try {
      // correctWords 배열을 콤마로 구분된 문자열로 변환
      const wordsToRemove = correctWords.join(","); 

      // 직접 쿼리 문자열을 URL에 추가하여 요청
      await baseAxios().delete(`/word?wordList=${encodeURIComponent(wordsToRemove)}`);

      alert("맞힌 단어들이 성공적으로 삭제되었습니다.");

    } catch (error) {
      console.error("단어 삭제에 실패했습니다.", error);
      alert("단어 삭제에 실패했습니다.");
    }
  };

  // 테스트 종료 함수
  const handleTestEnd = () => {
    if (removeCorrectWords && correctWords.length > 0) {
      removeWordsFromList(); // 체크박스가 선택되었고 맞힌 단어가 있을 경우 삭제 요청
      // 프론트엔드에서 맞힌 단어들을 제거하도록 Words 컴포넌트에 전달
      onWordsRemoved(correctWords);
    }
    onClose(); // 모달 닫기
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      {showResult ? (
        <div className="w-[480px] h-[560px] rounded-[24px] bg-white border-2 border-gray-300 shadow-lg">
          <header className="flex justify-between items-center px-6 py-2 bg-[#EBBDFE] rounded-t-[22px]">
            <div className="flex items-center">
              <RiCheckboxMultipleFill className="text-green-500 w-9 h-9" />
              <p className="ml-4 font-['Poppins'] text-[24px] font-semibold tracking-[-0.04em] text-[#BF2EF0]">
                Words Test
              </p>
            </div>
            <button
              className="w-[30px] h-[30px] flex items-center justify-center rounded-full"
              onClick={onClose}
            >
              <img src={cross} className="w-full h-full object-cover" />
            </button>
          </header>
          <div className="flex flex-col items-center justify-center h-[508px]">
            {/* 결과 화면 */}
            <h2 className="text-3xl font-bold mb-8 text-[#5E007E]">
              {userName} 님의 결과는?
            </h2>

            {/* 점수에 따른 아이콘 및 점수 표시 */}
            <div className="flex items-center text-[48px] font-bold text-blue-800">
              {calculateScore() <= 50 ? (
                <>
                  <FaFrown className="mr-4 text-yellow-500" />{" "}
                  {/* 50점 이하 😔 */}
                  {calculateScore()}점...
                </>
              ) : calculateScore() < 80 ? (
                <>
                  <FaSmile className="mr-4 text-green-500" />{" "}
                  {/* 50점 초과 80점 미만 🙂 */}
                  {calculateScore()}점!
                </>
              ) : (
                <>
                  <p>
                    <icon className="mr-5">🎊</icon>
                    {calculateScore()}점!!
                    <icon className="ml-5">🎊</icon>
                  </p>
                </>
              )}
            </div>

            <p className="text-xl font-semibold mt-8 mb-4">
              총 문제: {wordList.length}
            </p>
            <p className="text-xl font-semibold text-green-600 mb-2">
              맞춘 문제: {correctCount}
            </p>
            <p className="text-xl font-semibold text-red-600 mb-5">
              틀린 문제: {incorrectCount}
            </p>
            <label className="flex items-center justify-between w-[55%] font-['Poppins'] text-[15px] font-semibold leading-[28px] tracking-[-0.04em] text-[#4B5563] bg-[#F5F5F5] py-3 px-6 rounded-[20px] shadow-md border border-gray-300">
              <span className="flex flex-col text-left">
                맞힌 단어들을 단어장에서
                <br />
                제외하시겠습니까?
              </span>
              <input
                className="w-5 h-5 text-purple-600 border-gray-300 rounded focus:ring-purple-500 transition duration-300"
                type="checkbox"
                checked={removeCorrectWords}
                onChange={(e) => setRemoveCorrectWords(e.target.checked)}
              />
            </label>
            <button
              className="mt-5 w-[150px] py-2 bg-gradient-to-r from-purple-500 to-pink-500 text-white font-bold rounded-full shadow-lg hover:bg-gradient-to-r hover:from-pink-500 hover:to-purple-500 transition duration-300"
              onClick={handleTestEnd}
            >
              테스트 종료
            </button>
          </div>
        </div>
      ) : (
        <div className="w-[650px] h-[670px] rounded-[24px] bg-white border-2 border-gray-300 shadow-lg flex flex-col">
          {/* 테스트 화면 */}
          <header className="flex justify-between items-center px-6 py-2 bg-[#EBBDFE] rounded-t-[22px]">
            <div className="flex items-center">
              <RiCheckboxMultipleFill className="text-green-500 w-9 h-9" />
              <p className="ml-4 font-['Poppins'] text-[24px] font-semibold tracking-[-0.04em] text-[#BF2EF0]">
                Words Test
              </p>
            </div>
            <button
              className="w-[30px] h-[30px] flex items-center justify-center rounded-full"
              onClick={onClose}
            >
              <img src={cross} className="w-full h-full object-cover" />
            </button>
          </header>

          {/* 컨텐츠 영역 */}
          <div className="flex flex-col flex-grow items-center justify-center">
            {/* 안내문 */}
            <div className="font-['Poppins'] text-[18px] font-semibold leading-[28px] tracking-[-0.04em] text-[#91929F] bg-[#EEEEEE] py-3 px-10 rounded-[20px] text-center">
              단어장에 등록된 단어들에서 랜덤으로 문제가 출제됩니다.
              <br />
              제출 버튼을 클릭하면 정답을 확인할 수 있어요!
            </div>

            {/* 문제 번호 */}
            <div className="my-6 flex justify-center">
              <span className="font-['Open_Sans'] text-[32px] font-semibold leading-[36px] text-center tracking-[-0.04em] text-[#BF2EF0]">
                {currentQuestion + 1} / {shuffledWordList.length}
              </span>
            </div>

            {/* 현재 단어의 뜻 */}
            <div className="mt-4 mx-auto w-[85%] px-4 py-8 border-2 border-[#BF2EF0] text-start rounded-[20px]">
              <p className="text-lg font-semibold font-['Poppins']">
                {currentWord && currentWord.mean
                  ? currentWord.mean.map((meaning, index) => (
                      <span key={index}>
                        {meaning}
                        <br />
                      </span>
                    ))
                  : "단어 정보가 없습니다."}
              </p>
            </div>

            {/* 단어 입력 및 제출 버튼 */}
            <div className="mt-8 flex flex-col items-center">
              <input
                type="text"
                className="font-['Poppins'] text-center text-[28px] font-semibold border-none outline-none border-b-2 border-[#BF2EF0] focus:border-[#8B008B] transition duration-300 bg-[#EEEEEE] text-[#3D3D3D] rounded-[20px] py-3 px-5"
                placeholder="단어를 입력하세요"
                value={userAnswer}
                onChange={(e) => setUserAnswer(e.target.value)}
                onKeyDown={(e) => e.key === "Enter" && handleSubmit()}
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
      )}
    </div>
  );
};

export default WordTestModal;
