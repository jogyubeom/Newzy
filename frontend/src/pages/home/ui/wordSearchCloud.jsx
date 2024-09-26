import { useState } from "react";
import WordCloud from "react-wordcloud";

const words = [
  { text: "감언이설", value: 50, category: 0 },
  { text: "액트지오", value: 40, category: 1 },
  { text: "상충", value: 30, category: 2 },
  { text: "국정감사", value: 25, category: 3 },
  { text: "시추", value: 20, category: 2 },
  { text: "유망 구조", value: 28, category: 2 },
  { text: "석유공사", value: 18, category: 1 },
  { text: "가스전", value: 22, category: 2 },
  { text: "탐사", value: 17, category: 1 },
  { text: "유전", value: 12, category: 1 },
];

const options = {
  rotations: 1,
  rotationAngles: [0],
  fontSizes: [10, 60],
  padding: 1,
};

const categories = [
  { id: 0, label: "전체" },
  { id: 1, label: "경제" },
  { id: 2, label: "사회" },
  { id: 3, label: "세계" },
];

export function WordSearchCloud() {
  const [category, setCategory] = useState(0);

  const searchWords = words.filter(
    (word) => category === 0 || word.category === category
  );

  return (
    <div
      style={{
        width: "100%",
        minHeight: "400px",
        display: "flex",
        flexDirection: "column",
      }}
    >
      <div
        style={{
          marginBottom: "20px",
          display: "flex",
          justifyContent: "center",
          gap: "10px",
        }}
      >
        {categories.map((cat) => (
          <button
            key={cat.id}
            className={`px-4 py-2 rounded-lg transition duration-300 
            ${
              category === cat.id
                ? "bg-blue-600 text-white"
                : "bg-gray-200 text-gray-800 hover:bg-blue-500 hover:text-white"
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
