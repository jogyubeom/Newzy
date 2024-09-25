import { FaMinusCircle } from "react-icons/fa";
import { useState } from "react";
import WordTestModal from "widgets/profilePage/wordTestModal";

const Words = () => {
  const wordList = [
    {
      name: "공천",
      mean: [
        "1. 명사 여러 사람이 합의하여 추천함.",
        "2. 명사 공정하고 정당하게 추천함.",
        "3. 명사 행정 공인된 정당에서 선거에 출마한 당원을 공식적으로 추천하는 일.",
      ],
    },
    {
      name: "등용",
      mean: ["1. 명사 인재를 뽑아서 씀."],
    },
    {
      name: "합의",
      mean: [
        "1. 명사 서로 의견이 일치함. 또는 그 의견.",
        "2. 명사 법률 둘 이상의 당사자의 의사가 일치함. 또는 그런 일.",
      ],
    },
    {
      name: "공천",
      mean: [
        "1. 명사 여러 사람이 합의하여 추천함.",
        "2. 명사 공정하고 정당하게 추천함.",
        "3. 명사 행정 공인된 정당에서 선거에 출마한 당원을 공식적으로 추천하는 일.",
      ],
    },
    {
      name: "등용",
      mean: ["1. 명사 인재를 뽑아서 씀."],
    },
    {
      name: "합의",
      mean: [
        "1. 명사 서로 의견이 일치함. 또는 그 의견.",
        "2. 명사 법률 둘 이상의 당사자의 의사가 일치함. 또는 그런 일.",
      ],
    },
    {
      name: "공천",
      mean: [
        "1. 명사 여러 사람이 합의하여 추천함.",
        "2. 명사 공정하고 정당하게 추천함.",
        "3. 명사 행정 공인된 정당에서 선거에 출마한 당원을 공식적으로 추천하는 일.",
      ],
    },
    {
      name: "등용",
      mean: ["1. 명사 인재를 뽑아서 씀."],
    },
    {
      name: "합의",
      mean: [
        "1. 명사 서로 의견이 일치함. 또는 그 의견.",
        "2. 명사 법률 둘 이상의 당사자의 의사가 일치함. 또는 그런 일.",
      ],
    },
  ];

  const [isModalOpen, setModalOpen] = useState(false); // 모달 상태 관리

  // 모달 열기 및 닫기 함수
  const openModal = () => setModalOpen(true);
  const closeModal = () => setModalOpen(false);

  return (
    <>
      <div className="flex justify-end gap-10 items-center py-5 px-10">
        <select className="w-[125px] h-[36px] font-['Open_Sans'] text-[24px] font-semibold leading-[24px] tracking-[-0.04em] text-[#91929F] flex items-center shadow-sm border-none focus:outline-none text-end">
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
                <button className="w-[40px] h-[40px] rounded-full flex justify-center items-center mr-3">
                  <FaMinusCircle className="w-7 h-7 text-red-500" />
                </button>
                <h2 className="text-blue-600 font-semibold mb-2 text-[24px]">
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
          userName="정지훈"
        />
      </div>
    </>
  );
};

export default Words;
