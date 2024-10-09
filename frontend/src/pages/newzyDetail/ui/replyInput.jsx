import React from 'react';

const ReplyInput = ({ replyText, setReplyText, commentId, handleReplySubmit }) => {
  // 대댓글 입력 처리
  const handleKeyPress = (e) => {
    if (e.key === 'Enter') {
      handleReplySubmit(replyText[commentId] || '', commentId);
      // 대댓글 제출 후 입력값 초기화
      setReplyText((prev) => ({ ...prev, [commentId]: '' }));
    }
  };

  // 글자수 제한 500자
  const handleInputChange = (e) => {
    const { value } = e.target;

    // 500자를 초과하는 경우 경고 표시 및 잘라내기
    if (value.length > 500) {
      alert('대댓글은 최대 500자까지 입력할 수 있습니다.');
      setReplyText((prev) => ({ ...prev, [commentId]: value.slice(0, 500) })); // 500자까지만 유지
    } else {
      setReplyText((prev) => ({ ...prev, [commentId]: value })); // 상태 업데이트
    }
  };

  return (
    <div className="mt-2">
      <input
        type="text"
        value={replyText[commentId] || ''} // 초기값 설정
        onChange={handleInputChange}
        onKeyDown={handleKeyPress} // onKeyPress에서 onKeyDown으로 변경
        placeholder="대댓글을 입력하세요..."
        className="border border-gray-300 rounded-md p-1 mr-2"
      />
      <button
        onClick={() => {
          handleReplySubmit(replyText[commentId] || '', commentId);
          setReplyText((prev) => ({ ...prev, [commentId]: '' })); // 제출 후 초기화
        }}
        className="bg-purple-600 text-white rounded-md px-2 hover:bg-purple-700 transition"
      >
        추가
      </button>
      <div className="text-sm text-gray-500">{(replyText[commentId] || '').length}/500</div>
    </div>
  );
};

export default ReplyInput;