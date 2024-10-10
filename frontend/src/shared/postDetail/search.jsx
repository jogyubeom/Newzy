import React, { useState } from 'react';
import axios from 'axios';
import useAuthStore from 'shared/store/userStore';
import baseAxios from 'shared/utils/baseAxios';

const SearchContent = ({ category }) => {
  const [searchTerm, setSearchTerm] = useState('');
  const [results, setResults] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const token = useAuthStore(state => state.token);

  const handleSearch = async () => {
    if (!searchTerm) return;

    setLoading(true);
    setError(null);

    try {
      const response = await axios.get(`https://j11b305.p.ssafy.io/api/word/search?word=${searchTerm}&category=${category}`);
      setResults(response.data);
    } catch (error) {
      if (error.response) {
        setError("검색 중 오류가 발생했습니다.");
      } else {
        setError("서버와 연결할 수 없습니다."); // 네트워크 에러 처리
      }
      console.error("Error searching word:", error);
    } finally {
      setLoading(false);
    }
  };

  const handleKeyPress = (e) => {
    if (e.key === 'Enter') {
      handleSearch();
    }
  };

  const handleAddWord = async (word, definition) => {
    if (!token) {
      alert("로그인 후 단어를 저장해주세요.");
      return;
    }

    try {
      await baseAxios().post('/word', {
        word: word,
        definition: definition,
      });
      alert("단어가 저장되었습니다.");
    } catch (error) {
      if (error.response && error.response.status === 409) {
        alert("이미 등록된 단어입니다."); // 409 에러일 때
      } else {
        alert("단어 저장 중 오류가 발생했습니다."); // 그 외 에러일 때
      }
      console.error("Error saving word:", error);
    }
  };

  return (
    <div className="p-4">
      <div className="mb-4 flex">
        <input
          type="text"
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          onKeyPress={handleKeyPress}
          placeholder="검색어를 입력하세요..."
          className="border border-gray-300 rounded-md p-2 flex-1 mr-2"
        />
        <button
          onClick={handleSearch}
          className="bg-purple-600 text-white rounded-md px-4 hover:bg-purple-700 transition"
        >
          검색
        </button>
      </div>
      {loading ? (
        <p className="text-gray-500">로딩 중...</p>
      ) : error ? (
        <p className="text-red-500">{error}</p>
      ) : results.length > 0 ? (
        results.map((result) => (
          <div key={result.id} className="bg-gray-100 p-2 rounded-md mb-1 flex justify-between items-center">
            <span>
              <strong>{result.word}</strong>: {result.definition}
            </span>
            <button
              onClick={() => handleAddWord(result.word, result.definition)}
              className="ml-2 bg-purple-500 text-white rounded-md px-2 hover:bg-purple-600 transition"
            >
              +
            </button>
          </div>
        ))
      ) : (
        <p className="text-gray-500">검색 결과가 없습니다.</p>
      )}
    </div>
  );
};

export default SearchContent;
