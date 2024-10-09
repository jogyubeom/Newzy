import React from 'react';

const ReplyInput = ({ replyText, setReplyText, commentId, handleReplySubmit }) => {
  const handleKeyPress = (e) => {
    if (e.key === 'Enter') {
      handleReplySubmit(replyText, commentId);
    }
  };

  return (
    <div className="mt-2">
      <input
        type="text"
        value={replyText[commentId] || ''}
        onChange={(e) => setReplyText({ ...replyText, [commentId]: e.target.value })}
        onKeyPress={(e) => handleKeyPress(e)}
        placeholder="대댓글을 입력하세요..."
        className="border border-gray-300 rounded-md p-1 mr-2"
      />
      <button
        onClick={() => handleReplySubmit(replyText[commentId], commentId)}
        className="bg-purple-600 text-white rounded-md px-2 hover:bg-purple-700 transition"
      >
        추가
      </button>
    </div>
  );
};

export default ReplyInput;