import React from 'react';
import useAuthStore from 'shared/store/userStore'; // zustand 스토어에서 토큰 가져오기

const SearchBar = ({ type, selectedRange, onRangeClick, searchTerm, onSearchChange, onClearSearch, onKeyPress }) => {
  const { token } = useAuthStore(); // zustand에서 토큰 가져오기
  
  // type에 따라 버튼 텍스트를 조건부로 설정
  const buttonLabels = type === 'newzy' ? ['전체', '구독'] : ['최신', '인기'];

  // 구독 버튼 클릭 시 토큰 유무 확인 함수
  const handleRangeClick = (range) => {
    if (type === 'newzy' && range === 1 && !token) {
      alert('로그인 후 구독을 이용할 수 있습니다.');
    } else {
      onRangeClick(range);
    }
  };

  return (
    <div className="relative flex items-center">
      <button
        className={`mr-2 px-4 py-2 rounded ${selectedRange === 0 ? 'bg-blue-500 text-white' : 'bg-gray-200 text-gray-700'}`}
        onClick={() => onRangeClick(0)}
      >
        {buttonLabels[0]} {/* 첫 번째 버튼 텍스트 */}
      </button>
      <button
        className={`mr-2 px-4 py-2 rounded ${selectedRange === 1 ? 'bg-green-500 text-white' : 'bg-gray-200 text-gray-700'}`}
        onClick={() => handleRangeClick(1)} // 구독 버튼 클릭 시 토큰 확인
      >
        {buttonLabels[1]} {/* 두 번째 버튼 텍스트 */}
      </button>
      <input
        type="text"
        value={searchTerm}
        onChange={onSearchChange}
        onKeyPress={onKeyPress}
        placeholder="검색..."
        className="border p-2 rounded pr-10"
      />
      {searchTerm && (
        <button
          onClick={onClearSearch}
          className="absolute right-2 top-1/2 transform -translate-y-1/2 text-gray-500"
        >
          &times;
        </button>
      )}
    </div>
  );
};

export default SearchBar;