import { FaMinusCircle } from "react-icons/fa";
import { useState, useEffect } from "react";
import WordTestModal from "./wordTestModal";
import baseAxios from "shared/utils/baseAxios";
import useAuthStore from "shared/store/userStore";
import Pagination from "../../../shared/postList/pagination";

const Words = () => {

  const [wordList, setWordList] = useState([]); // 서버로부터 가져온 단어 리스트 상태 관리
  const [isModalOpen, setModalOpen] = useState(false); // 모달 상태 관리
  const [page, setPage] = useState(0); // 현재 페이지 상태
  const [sort, setSort] = useState(0); // 정렬 기준 (0: 최신순, 1: 오래된 순)
  const [totalPages, setTotalPages] = useState(0); // 전체 페이지 수
  const [loading, setLoading] = useState(false); // 로딩 상태
  const user = useAuthStore.getState().userInfo;

  // 단어 리스트를 서버에서 가져오는 함수
  const fetchWordList = async () => {
    setLoading(true);

    try {
      const response = await baseAxios().get(`https://j11b305.p.ssafy.io/api/word`, {
        params: { page, sort }, 
      });

      const { totalPage, vocaList } = response.data; // 응답 데이터에서 필요한 값 추출
      const fetchedWords = vocaList.map((wordData) => ({
        name: wordData.word,
        mean: [wordData.definition], // 단어 의미 리스트
      }));

      setWordList(fetchedWords); // 단어 리스트 업데이트
      setTotalPages(totalPage); // 전체 페이지 수 설정
    } catch (error) {
      console.error("단어 목록을 불러오는 데 실패했습니다.", error);
    } finally {
      setLoading(false);
    }
  };

  // 정렬 방식 변경 핸들러
  const handleSortChange = (e) => {
    setSort(parseInt(e.target.value));
    setPage(0); // 정렬 변경 시 페이지를 1로 초기화
  };

  // 페이지가 변경될 때마다 단어 리스트 다시 요청
  useEffect(() => {
    fetchWordList();
  }, [page, sort]);

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

  // 모달 열기 및 닫기 함수
  const openModal = () => setModalOpen(true);
  const closeModal = () => setModalOpen(false);

  return (
    <>
      <div className="flex justify-end gap-10 items-center py-5 px-10">
        <select
          className="w-[120px] h-10 font-['Open_Sans'] text-[18px] font-semibold leading-[24px] tracking-[-0.04em] text-[#91929F] flex items-center border-none focus:outline-none text-center rounded-[10px] bg-[#F4F4F4] hover:bg-[#EAEAEA] shadow-md transition-colors duration-300"
          value={sort}
          onChange={handleSortChange}
        >
          <option className="text-center" value={0}>
            최신 순
          </option>
          <option className="text-center" value={1}>
            오래된 순
          </option>
        </select>
        <button
          onClick={openModal}
          className="w-[220px] h-[50px] rounded-[10px] font-['Open_Sans'] bg-[#BF2EF0] hover:bg-[#A229CC] opacity-100 text-white text-[24px] font-semibold transition-colors duration-300 shadow-md"
        >
          단어 테스트
        </button>
      </div>
      <div className="py-3 px-10 mx-auto font-sans mb-10">
        {loading ? (
            <div className="text-center">로딩 중...</div>
          ) : wordList.length === 0 ? (
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

        {/* 페이지네이션 컴포넌트 */}
        <Pagination
          currentPage={page + 1}
          totalPages={totalPages}
          onPageChange={(newPage) => setPage(newPage)} // 페이지 변경 함수
        />

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
