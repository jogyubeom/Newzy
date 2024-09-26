import React, { useState } from 'react';

const SearchContent = () => {
  const [searchTerm, setSearchTerm] = useState('');
  const [results, setResults] = useState([]);

  const handleSearch = () => {
    // 검색 로직을 여기에 추가 (예: API 요청 등)
    // 현재는 더미 데이터를 사용하여 결과를 시뮬레이션합니다.
    const dummyResults = [
      '검색 결과 1',
      '검색 결과 2',
      '검색 결과 3',
      '검색 결과 4',
    ];

    // 검색어가 포함된 결과만 필터링
    const filteredResults = dummyResults.filter(result =>
      result.toLowerCase().includes(searchTerm.toLowerCase())
    );

    setResults(filteredResults);
  };

  const handleKeyPress = (e) => {
    if (e.key === 'Enter') {
      handleSearch();
    }
  };

  return (
    <div className="p-4">
      {/* <h3 className="text-lg font-semibold mb-2">검색</h3> */}
      <div className="mb-4 flex">
        <input
          type="text"
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          onKeyPress={handleKeyPress} // 엔터키 이벤트 추가
          placeholder="검색어를 입력하세요..."
          className="border border-gray-300 rounded-md p-2 flex-1 mr-2"
        />
        <button
          onClick={handleSearch}
          className="bg-blue-500 text-white rounded-md px-4 hover:bg-blue-600 transition"
        >
          검색
        </button>
      </div>
      <div>
        {results.length > 0 ? (
          results.map((result, index) => (
            <div key={index} className="bg-gray-100 p-2 rounded-md mb-1">
              {result}
            </div>
          ))
        ) : (
          <p className="text-gray-500">검색 결과가 없습니다.</p>
        )}
      </div>
    </div>
  );
};

export default SearchContent;
