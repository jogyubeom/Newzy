import React from 'react';

const ReplyInput = ({ replyText, setReplyText, commentId, handleReplySubmit }) => {
  const handleKeyPress = (e) => {
    if (e.key === 'Enter') {
      handleReplySubmit(replyText[commentId], commentId);
    }
  };

  // 글자수 제한 500자
  const handleInputChange = (e) => {
    const { value } = e.target;

    // 500자를 초과하는 경우 경고 표시 및 잘라내기
    if (value.length > 500) {
      setNewCommentText(value.slice(0, 500)); // 500자까지만 유지
      alert('댓글은 최대 500자까지 입력할 수 있습니다.');
    } else {
      setNewCommentText(value);
    }
  };

  return (
    <div className="mt-2">
      <input
        type="text"
        value={replyText[commentId] || ''}
        onChange={handleInputChange}
        onKeyPress={handleKeyPress}
        placeholder="대댓글을 입력하세요..."
        className="border border-gray-300 rounded-md p-1 mr-2"
      />
      <button
        onClick={() => handleReplySubmit(replyText[commentId], commentId)}
        className="bg-purple-600 text-white rounded-md px-2 hover:bg-purple-700 transition"
      >
        추가
      </button>
      <div className="text-sm text-gray-500">{(replyText[commentId] || '').length}/500</div>
    </div>
  );
};

export default ReplyInput;