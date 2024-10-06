import { FaMinusCircle } from "react-icons/fa";
import { useState, useEffect } from "react";
import WordTestModal from "./wordTestModal";
import baseAxios from "shared/utils/baseAxios";
import useAuthStore from "shared/store/userStore";

const Words = () => {

  const [wordList, setWordList] = useState([]); // 서버로부터 가져온 단어 리스트 상태 관리
  const [isModalOpen, setModalOpen] = useState(false); // 모달 상태 관리
  const user = useAuthStore.getState().userInfo

  // 단어 리스트를 서버에서 가져오는 함수
  const fetchWordList = async () => {
    try {
      const response = await baseAxios().get("/word"); // 서버로 GET 요청
      const transformedWordList = response.data.map((wordData) => ({
        name: wordData.word,
        mean: [wordData.definition], // 응답 구조에 맞게 데이터 변환
      }));
      setWordList(transformedWordList); // 서버에서 가져온 데이터로 wordList 업데이트
    } catch (error) {
      console.error("단어 목록을 불러오는 데 실패했습니다.", error);
    }
  };

  // 단어 삭제 요청 함수
  const handleDeleteWord = async (wordName) => {
    try {
      await baseAxios().delete("/word", {
        params: { wordList: wordName } // wordList 값을 쿼리 파라미터로 전달
      });

      // 삭제 후 성공적으로 처리되면, 프론트에서 해당 단어를 삭제
      setWordList((prevList) => prevList.filter((word) => word.name !== wordName));
    } catch (error) {
      console.error("단어 삭제에 실패했습니다.", error);
    }
  };

  // 컴포넌트가 마운트될 때 단어 리스트 가져오기
  useEffect(() => {
    fetchWordList();
  }, []);

  // 모달 열기 및 닫기 함수
  const openModal = () => setModalOpen(true);
  const closeModal = () => setModalOpen(false);

  return (
    <>
      <div className="flex justify-end gap-10 items-center py-5 px-10">
      <select className="w-[150px] h-[60px] font-['Open_Sans'] text-[20px] font-semibold leading-[24px] tracking-[-0.04em] text-[#91929F] flex items-center border-none focus:outline-none text-center rounded-[45px] bg-[#F4F4F4] hover:bg-[#EAEAEA] shadow-md transition-colors duration-300">
        <option className="text-center" value={"latest"}>최신 순</option>
        <option className="text-center" value={"oldest"}>오래된 순</option>
      </select>
        <button
          onClick={openModal}
          className="w-[234px] h-[60px] rounded-[45px] font-['Open_Sans'] bg-[#BF2EF0] hover:bg-[#A229CC] opacity-100 text-white text-[28px] font-semibold transition-colors duration-300 shadow-md"
        >
          단어 테스트
        </button>
      </div>
      <div className="py-3 px-10 mx-auto font-sans mb-10">
        {wordList.length === 0 ? (
          // 등록된 단어가 없을 경우 표시할 메시지
          <div className="flex flex-col items-center justify-center h-[210px] bg-gray-100 rounded-lg shadow-lg border-gray-300">
            <h2 className="text-3xl font-semibold text-[#BF2EF0] mb-4">
              등록된 단어가 없습니다.
            </h2>
            <p className="text-xl font-semibold text-gray-600">
              단어를 추가해주세요!
            </p>
          </div>
        ) : (
          // 등록된 단어가 있을 경우 단어 리스트 렌더링
          wordList.map((word, index) => (
            <div key={index} className="mb-6 pb-4 border-b border-gray-300">
              <div className="flex items-center mb-2">
                <button className="w-[40px] h-[40px] rounded-full flex justify-center items-center mr-3" onClick={() => handleDeleteWord(word.name)}>
                  <FaMinusCircle className="w-5 h-5 text-red-500" />
                </button>
                <h2 className="text-blue-600 font-semibold text-[24px]">
                  {word.name}
                </h2>
              </div>
              <ul className="pl-10 list-disc text-gray-700 font-semibold text-[22px]">
                {word.mean.map((meaning, idx) => (
                  <p key={idx} className="leading-relaxed">
                    {meaning}
                  </p>
                ))}
              </ul>
            </div>
          ))
        )}

         {/* 모달 컴포넌트 렌더링 */}
         <WordTestModal
          isOpen={isModalOpen}
          onClose={closeModal}
          wordList={wordList}
          userName={user && user.nickname ? user.nickname : "사용자"}
        />
      </div>
    </>
  );
};

export default Words;
