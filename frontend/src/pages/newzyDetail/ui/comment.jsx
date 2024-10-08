import React, { useState, useEffect } from 'react';
import baseAxios from 'shared/utils/baseAxios'; // baseAxios 임포트
import useAuthStore from 'shared/store/userStore'; // 토큰 가져오기 위한 스토어 임포트

const CommentContent = ({ newzyId }) => {
  const { token } = useAuthStore(); // 스토어에서 토큰 가져오기
  const [comments, setComments] = useState([]); // 댓글 리스트 상태
  const [newCommentText, setNewCommentText] = useState(''); // 새 댓글 입력값 상태
  const [replyText, setReplyText] = useState({}); // 대댓글 입력값 상태

  // 댓글 데이터 로드
  useEffect(() => {
    const fetchComments = async () => {
      try {
        const res = await baseAxios().get(`/newzy/${newzyId}/comments`);
        setComments(res.data); // 서버에서 가져온 댓글 데이터를 상태로 설정
      } catch (error) {
        console.error(error);
      }
    };
    fetchComments();
  }, [newzyId]);

  // 새 댓글 작성
  const handleCommentSubmit = async () => {
    if (!token) {
      alert('로그인이 필요합니다.');
      return;
    }
    
    if (newCommentText.trim()) {
      const newComment = {
        newzyComment: newCommentText,
        newzyId,
      };
      
      try {
        const res = await baseAxios().post(`/newzy/${newzyId}/comments`, newComment);
        setComments([...comments, res.data]); // 새 댓글을 기존 댓글 리스트에 추가
        setNewCommentText(''); // 입력 필드 초기화
      } catch (error) {
        console.error(error);
      }
    }
  };

  // 대댓글 작성
  const handleReplySubmit = async (parentCommentId) => {
    if (!token) {
      alert('로그인이 필요합니다.');
      return;
    }

    if (replyText[parentCommentId]?.trim()) {
      const newReply = {
        newzyComment: replyText[parentCommentId],
        newzyParentCommentId: parentCommentId,
        newzyId,
      };

      try {
        const res = await baseAxios().post(`/newzy/${newzyId}/comments`, newReply);
        setComments((prevComments) =>
          prevComments.map((comment) =>
            comment.newzyCommentId === parentCommentId
              ? {
                  ...comment,
                  replies: [...comment.replies, res.data], // 대댓글 추가
                }
              : comment
          )
        );
        setReplyText((prev) => ({ ...prev, [parentCommentId]: '' })); // 대댓글 입력 필드 초기화
      } catch (error) {
        console.error(error);
      }
    }
  };

  // 엔터키로 새 댓글 추가
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
      {/* 댓글 입력창 */}
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

      {/* 댓글 목록 */}
      <div>
        {comments.length > 0 ? (
          comments.map((comment) => (
            <div key={comment.newzyCommentId} className="bg-white p-2 mb-2 rounded-md shadow-sm">
              <div>{comment.nickname}</div>
              <div>{comment.newzyComment}</div>
              <div>{new Date(comment.createdAt).toLocaleDateString()}</div>

              {/* 대댓글 입력창 */}
              <div className="mt-2">
                <input
                  type="text"
                  value={replyText[comment.newzyCommentId] || ''}
                  onChange={(e) => setReplyText({ ...replyText, [comment.newzyCommentId]: e.target.value })}
                  onKeyPress={(e) => handleReplyKeyPress(e, comment.newzyCommentId)} // 엔터키 이벤트 추가
                  placeholder="대댓글을 입력하세요..."
                  className="border border-gray-300 rounded-md p-1 mr-2"
                />
                <button
                  onClick={() => handleReplySubmit(comment.newzyCommentId)}
                  className="bg-purple-600 text-white rounded-md px-2 hover:bg-purple-700 transition"
                >
                  추가
                </button>
              </div>

              {/* 대댓글 목록 */}
              {comment.replies?.length > 0 && (
                <div className="ml-4 mt-2">
                  {comment.replies.map((reply, index) => (
                    <div key={index} className="bg-gray-200 p-2 rounded-md shadow-sm mb-1">
                      {reply.newzyComment}
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