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
  { id: 0, label: "전체" },
  { id: 1, label: "경제" },
  { id: 2, label: "사회" },
  { id: 3, label: "세계" },
];

export function WordSearchCloud() {
  const { wordSearchCloud } = useHomeStore();
  const [category, setCategory] = useState(0);

  const searchWords = wordSearchCloud?.filter(
    (word) => category === 0 || word.category === category
  );

  return (
    <div className="w-full min-h-[400px] flex flex-col">
      <div className="mb-5 flex justify-center gap-2.5">
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
      {/* 필터링된 단어 구름 */}
      <div style={{ height: "400px", width: "100%" }}>
        <WordCloud words={searchWords} options={options} spiral="archimedean" />
      </div>
    </div>
  );
}
