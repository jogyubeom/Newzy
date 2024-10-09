import React from 'react';

const CommentInput = ({ newCommentText, setNewCommentText, handleCommentSubmit }) => {
  const handleKeyPress = (e) => {
    if (e.key === 'Enter') {
      handleCommentSubmit(newCommentText);
    }
  };

  return (
    <div className="mb-4">
      <input
        type="text"
        value={newCommentText}
        onChange={(e) => setNewCommentText(e.target.value)}
        onKeyPress={handleKeyPress}
        placeholder="새 댓글을 입력하세요..."
        className="border border-gray-300 rounded-md p-1 mr-2"
      />
      <button
        onClick={() => handleCommentSubmit(newCommentText)}
        className="bg-purple-600 text-white rounded-md px-2 hover:bg-purple-700 transition"
      >
        추가
      </button>
    </div>
  );
};

export default CommentInput;