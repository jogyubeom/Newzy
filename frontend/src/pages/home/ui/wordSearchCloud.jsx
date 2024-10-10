import { useState } from "react";
import WordCloud from "react-wordcloud";
import useHomeStore from "../store/useHomeStore";

const options = {
  rotations: 1,
  rotationAngles: [0],
  fontSizes: [10, 60],
  padding: 1,
  fontFamily: "title",
};

const categories = [
  { id: 0, label: "전체", apiCategory: null },
  { id: 1, label: "경제", apiCategory: 0 },
  { id: 2, label: "사회", apiCategory: 1 },
  { id: 3, label: "세계", apiCategory: 2 },
];

export function WordSearchCloud() {
  const { wordSearchCloud } = useHomeStore();
  const [category, setCategory] = useState(0);

  // 선택된 카테고리와 매칭되는 단어 필터링
  const searchWords = wordSearchCloud?.filter(
    (word) =>
      categories[category].apiCategory === null ||
      word.category === categories[category].apiCategory
  );

  return (
    <div className="w-full min-h-[400px] flex flex-col">
      <div className="mb-2 flex justify-center gap-2.5">
        {categories.map((cat) => (
          <button
            key={cat.id}
            className={`px-4 py-2 rounded-lg transition duration-300 
            ${
              category === cat.id
                ? "bg-gray-800 text-white"
                : " text-gray-800 hover:text-white"
            }`}
            onClick={() => setCategory(cat.id)}
          >
            {cat.label}
          </button>
        ))}
      </div>
      <div className="text-sm text-gray-500 flex justify-center">
        지금 사람들이 모르는 단어들을 살펴보세요.
      </div>
      {/* 필터링된 단어 구름 */}
      <div style={{ height: "100%", width: "100%" }}>
        <WordCloud words={searchWords} options={options} spiral="archimedean" />
      </div>
    </div>
  );
}
