import React, { useState } from 'react';

const CommentContent = () => {
  const initialComments = [
    { id: 1, text: '첫 번째 댓글입니다.', replies: [] },
    { id: 2, text: '두 번째 댓글입니다.', replies: [] },
    { id: 3, text: '세 번째 댓글입니다.', replies: [] },
  ];

  const [comments, setComments] = useState(initialComments);
  const [newCommentText, setNewCommentText] = useState('');
  const [replyText, setReplyText] = useState({});

  const handleCommentSubmit = () => {
    if (newCommentText.trim()) {
      const newComment = {
        id: comments.length + 1,
        text: newCommentText,
        replies: [],
      };
      setComments([...comments, newComment]);
      setNewCommentText('');
    }
  };

  const handleReplySubmit = (commentId) => {
    if (replyText[commentId]?.trim()) {
      setComments((prevComments) =>
        prevComments.map((comment) =>
          comment.id === commentId
            ? {
                ...comment,
                replies: [...comment.replies, replyText[commentId]],
              }
            : comment
        )
      );
      setReplyText((prev) => ({ ...prev, [commentId]: '' }));
    }
  };

  // 엔터키를 눌렀을 때 댓글 추가
  const handleKeyPress = (e) => {
    if (e.key === 'Enter') {
      handleCommentSubmit();
    }
  };

  // 각 댓글에 대한 대댓글 엔터키 처리
  const handleReplyKeyPress = (e, commentId) => {
    if (e.key === 'Enter') {
      handleReplySubmit(commentId);
    }
  };

  return (
    <div className="w-[400px] p-4">
      {/* <h3 className="text-lg font-semibold mb-2">댓글</h3> */}
      <div className="mb-4">
        <input
          type="text"
          value={newCommentText}
          onChange={(e) => setNewCommentText(e.target.value)}
          onKeyPress={handleKeyPress} // 엔터키 이벤트 추가
          placeholder="새 댓글을 입력하세요..."
          className="border border-gray-300 rounded-md p-1 mr-2"
        />
        <button
          onClick={handleCommentSubmit}
          className="bg-purple-600 text-white rounded-md px-2 hover:bg-text-purple-500 transition"
        >
          추가
        </button>
      </div>

      <div>
        {comments.length > 0 ? (
          comments.map((comment) => (
            <div key={comment.id} className="bg-white p-2 mb-2 rounded-md shadow-sm">
              <div>{comment.text}</div>
              <div className="mt-2">
                <input
                  type="text"
                  value={replyText[comment.id] || ''}
                  onChange={(e) => setReplyText({ ...replyText, [comment.id]: e.target.value })}
                  onKeyPress={(e) => handleReplyKeyPress(e, comment.id)} // 엔터키 이벤트 추가
                  placeholder="대댓글을 입력하세요..."
                  className="border border-gray-300 rounded-md p-1 mr-2"
                />
                <button
                  onClick={() => handleReplySubmit(comment.id)}
                  className="bg-purple-600 text-white rounded-md px-2 hover:bg-purple-700 transition"
                >
                  추가
                </button>
              </div>
              {comment.replies.length > 0 && (
                <div className="ml-4 mt-2">
                  {comment.replies.map((reply, index) => (
                    <div key={index} className="bg-gray-200 p-2 rounded-md shadow-sm mb-1">
                      {reply}
                    </div>
                  ))}
                </div>
              )}
            </div>
          ))
        ) : (
          <p className="text-gray-500">댓글이 없습니다.</p>
        )}
      </div>
    </div>
  );
};

export default CommentContent;
