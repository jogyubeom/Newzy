import React, { useState, useEffect } from "react";
import Modal from "./ui/Modal";
import { useNavigate } from "react-router-dom";
import baseAxios from "shared/utils/baseAxios";

const categories = ["economy", "society", "world", "signup"];

export const UserTest = () => {
  const [currentCategoryIndex, setCurrentCategoryIndex] = useState(0);
  const [selectedWords, setSelectedWords] = useState({
    economy: [],
    society: [],
    world: []
  });

  const [vocabularyData, setVocabularyData] = useState({
    economy: [],
    society: [],
    world: []
  });

  const [birthDate, setBirthDate] = useState("");
  const [introduction, setIntroduction] = useState("");
  const [isModalOpen, setIsModalOpen] = useState(true); // 모달 상태 관리

  const currentCategory = categories[currentCategoryIndex];
  const nav = useNavigate()


  // 서버에서 어휘 테스트 데이터를 받아오는 함수
  const fetchVocabularyData = async () => {
    try {
      const res = await baseAxios().get("/user/vocabulary-test");
      const fetchedData = res.data;

      // 서버에서 받은 데이터를 카테고리별로 분류
      const organizedData = {
        economy: fetchedData.filter((word) => word.category === 0).map((word) => word.word),
        society: fetchedData.filter((word) => word.category === 1).map((word) => word.word),
        world: fetchedData.filter((word) => word.category === 2).map((word) => word.word),
      };

      setVocabularyData(organizedData); // 상태에 저장
    } catch (error) {
      console.error("어휘 테스트 데이터를 불러오는 중 오류 발생:", error);
    }
  };

  useEffect(() => {
    fetchVocabularyData(); // 컴포넌트 마운트 시 어휘 테스트 데이터 요청
  }, []);

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


  const handleNext = async () => {
    if (currentCategoryIndex < categories.length - 1) {
      setCurrentCategoryIndex(currentCategoryIndex + 1);
    } else {
      if (!birthDate) {
        alert("생년월일을 입력해주세요.");
        return;
      }

      // 회원가입 정보와 어휘 테스트 결과 서버로 전송할 데이터 구성
    const Data = {
      categoryScores: {
        additionalProp1: selectedWords.economy.length,  // 경제 부문
        additionalProp2: selectedWords.society.length,  // 사회 부문
        additionalProp3: selectedWords.world.length     // 세계 부문
      },
      birth: birthDate,
      info: introduction
    };

      // 테스트용으로 콘솔에 출력
      console.log("유저 정보:", Data.birth, Data.info);
      console.log("아는 경제 단어 개수:", Data.additionalProp1);
      console.log("아는 사회 단어 개수:", Data.additionalProp2);
      console.log("아는 세계 단어 개수:", Data.additionalProp3);

      try {
        // 서버로 데이터 전송
        const response = await baseAxios().post("/user/vocabulary-test", Data);
        
        // 서버 응답 확인 (테스트용)
        console.log("서버 응답:", response.data);
  
        // 전송이 성공하면 알림을 띄우고 메인 페이지로 이동
        alert("어휘 테스트 및 회원가입이 완료되었습니다!");
        nav('/');
      } catch (error) {
        // 전송 실패 시 오류 처리
        console.error("서버 전송 오류:", error);
        alert("서버로 데이터를 전송하는 중 오류가 발생했습니다. 다시 시도해주세요.");
      }
    }
  };
  return (
    <div className="mx-auto py-10 px-24 bg-white rounded-lg shadow-lg">
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

      {currentCategory !== "signup" ? (
        <>
          <h2 className="text-2xl font-bold mb-4 text-center">
            {currentCategory === "economy" && "어휘 테스트 [경제]"}
            {currentCategory === "society" && "어휘 테스트 [사회]"}
            {currentCategory === "world" && "어휘 테스트 [세계]"}
          </h2>

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
        </>
      ) : (
        <div className="px-10">
          <h2 className="text-2xl font-bold mb-14 text-center">회원가입 정보 입력</h2>
          <div className="mb-10">
            <label className="block font-semibold mb-2">생년월일 (필수)</label>
            <input
              type="date"
              value={birthDate}
              onChange={(e) => setBirthDate(e.target.value)}
              className="w-full px-3 py-2 border rounded"
              required
            />
          </div>
          <div className="mb-8">
            <label className="block font-semibold mb-2">자기소개</label>
            <textarea
              value={introduction}
              onChange={(e) => setIntroduction(e.target.value)}
              className="w-full px-3 py-2 border rounded"
              rows="4"
              placeholder="자기소개를 입력해주세요 (선택사항)"
            />
          </div>
        </div>
      )}

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
          {currentCategoryIndex === categories.length - 1 ? "제출" : "다음"}
        </button>
      </div>
    </div>
  );
};