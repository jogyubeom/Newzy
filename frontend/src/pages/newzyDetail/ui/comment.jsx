import React, { useState, useEffect } from 'react';
import baseAxios from 'shared/utils/baseAxios';
import useAuthStore from 'shared/store/userStore';

const CommentContent = ({ newzyId }) => {
  const { token, userInfo } = useAuthStore(state => ({
    token: state.token,
    userInfo: state.userInfo,
  }));

  const [comments, setComments] = useState([]);
  const [newCommentText, setNewCommentText] = useState('');
  const [replyText, setReplyText] = useState({});
  const [showReplyInput, setShowReplyInput] = useState({});

  const fetchComments = async () => {
    try {
      const res = await baseAxios().get(`/newzy/${newzyId}/comments`);
      console.log('가져온 댓글:', res.data);
      setComments(res.data);
    } catch (error) {
      console.error(error);
    }
  };

  useEffect(() => {
    fetchComments();
  }, [newzyId]);

  // 댓글 작성 및 대댓글 작성을 위한 공통 함수
  const handleCommentSubmit = async (commentText, parentCommentId = null) => {
    if (!token) {
      alert('로그인이 필요합니다.');
      return;
    }

    if (commentText.trim()) {
      const newComment = {
        newzyComment: commentText,
        newzyParentCommentId: parentCommentId,
        newzyId,
      };

      try {
        const res = await baseAxios().post(`/newzy/${newzyId}/comments`, newComment);
        // 댓글을 새로 작성할 때마다 목록을 새로 가져옴
        await fetchComments(); // 새로운 댓글 작성 후 댓글 목록 재요청
        // 입력 필드 초기화
        if (parentCommentId === null) {
          setNewCommentText(''); // 댓글 입력 필드 초기화
        }
        setReplyText((prev) => ({ ...prev, [parentCommentId]: '' })); // 대댓글 입력 필드 초기화
      } catch (error) {
        console.error(error);
      }
    }
  };

  // 댓글 삭제 함수
  const handleCommentDelete = async (commentId) => {
    try {
      await baseAxios().delete(`/newzy/${newzyId}/comments/${commentId}`);
      await fetchComments(); // 댓글 삭제 후 목록 재요청
    } catch (error) {
      console.error('댓글 삭제 오류:', error);
    }
  };

  // 댓글 수정 함수
  const handleCommentEdit = async (commentId, updatedText) => {
    try {
      await baseAxios().put(`/newzy/${newzyId}/comments/${commentId}`, { newzyComment: updatedText });
      await fetchComments(); // 댓글 수정 후 목록 재요청
    } catch (error) {
      console.error('댓글 수정 오류:', error);
    }
  };

  // 엔터키로 새 댓글 추가
  const handleKeyPress = (e) => {
    if (e.key === 'Enter') {
      handleCommentSubmit(newCommentText); // 새 댓글 추가
    }
  };

  // 각 댓글에 대한 대댓글 엔터키 처리
  const handleReplyKeyPress = (e, commentId) => {
    if (e.key === 'Enter') {
      handleCommentSubmit(replyText[commentId], commentId); // 대댓글 추가
    }
  };

  // 댓글과 대댓글을 그룹화
  const groupComments = (comments) => {
    const grouped = comments.reduce((acc, comment) => {
      if (comment.parentCommentId === null) {
        // 최상위 댓글인 경우
        acc.push({ ...comment, replies: [] });
      }
      return acc;
    }, []);

    // 대댓글 추가
    grouped.forEach((parentComment) => {
      comments.forEach((comment) => {
        if (comment.parentCommentId === parentComment.newzyCommentId) {
          parentComment.replies.push(comment);
        }
      });
    });

    return grouped;
  };

  const groupedComments = groupComments(comments);

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
          onClick={() => handleCommentSubmit(newCommentText)} // 클릭 시 댓글 추가
          className="bg-purple-600 text-white rounded-md px-2 hover:bg-purple-700 transition"
        >
          추가
        </button>
      </div>

      {/* 댓글 목록 */}
      <div>
        {groupedComments.length > 0 ? (
          groupedComments.map((comment) => (
            <div key={comment.newzyCommentId} className="bg-white p-2 mb-2 rounded-md shadow-sm">
              <div className="flex items-center">
                {/* 프로필 이미지 존재 여부에 따라 다르게 표시 */}
                {comment.profile ? (
                  <img
                    src={comment.profile}
                    alt={`${comment.nickname}의 프로필`}
                    className="w-8 h-8 rounded-full mr-2 border border-gray-300"
                  />
                ) : (
                  <div
                    className="w-8 h-8 rounded-full mr-2"
                    style={{
                      backgroundColor: '#4A90E2', // 원하는 색상으로 변경 가능
                    }}
                  ></div>
                )}
                <span>{comment.nickname}</span>
                {/* 수정 및 삭제 버튼 추가 */}
                {userInfo && userInfo.nickname === comment.nickname && ( // userInfo가 null이 아닌 경우에만 체크
                  <>
                    <button
                      onClick={() => handleCommentEdit(comment.newzyCommentId, prompt("댓글을 수정하세요", comment.newzyComment))} // 수정 버튼 클릭 시 수정 함수 호출
                      className="text-blue-600 ml-2"
                    >
                      수정
                    </button>
                    <button
                      onClick={() => handleCommentDelete(comment.newzyCommentId)} // 삭제 버튼 클릭 시 삭제 함수 호출
                      className="text-red-600 ml-2"
                    >
                      삭제
                    </button>
                  </>
                )}
              </div>
              <div>{comment.newzyComment}</div>
              <div className="text-gray-500 text-sm">
                {new Date(comment.createdAt).toLocaleString('ko-KR', {
                  year: 'numeric',
                  month: '2-digit',
                  day: '2-digit',
                  hour: '2-digit',
                  minute: '2-digit',
                })}
              </div>

              {/* 대댓글 입력 버튼 */}
              <button
                onClick={() => setShowReplyInput((prev) => ({ ...prev, [comment.newzyCommentId]: !prev[comment.newzyCommentId] }))}
                className="text-purple-600 underline mt-2"
              >
                {showReplyInput[comment.newzyCommentId] ? '대댓글 숨기기' : '대댓글 달기'}
              </button>

              {/* 대댓글 입력창 */}
              {showReplyInput[comment.newzyCommentId] && (
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
                    onClick={() => handleCommentSubmit(replyText[comment.newzyCommentId], comment.newzyCommentId)} // 클릭 시 대댓글 추가
                    className="bg-purple-600 text-white rounded-md px-2 hover:bg-purple-700 transition"
                  >
                    추가
                  </button>
                </div>
              )}

              {/* 대댓글 목록 */}
              {comment.replies?.length > 0 && (
                <div className="ml-4 mt-2">
                  {comment.replies.map((reply) => (
                    <div key={reply.newzyCommentId} className="bg-gray-200 p-2 rounded-md shadow-sm mb-1">
                      <div className="flex items-center">
                        {/* 대댓글 작성자 프로필 이미지 존재 여부에 따라 다르게 표시 */}
                        {reply.profile ? (
                          <img
                            src={reply.profile}
                            alt={`${reply.nickname}의 프로필`}
                            className="w-8 h-8 rounded-full mr-2 border border-gray-300"
                          />
                        ) : (
                          <div
                            className="w-8 h-8 rounded-full mr-2"
                            style={{
                              backgroundColor: '#4A90E2', // 원하는 색상으로 변경 가능
                            }}
                          ></div>
                        )}
                        <span>{reply.nickname}</span>
                        {/* 수정 및 삭제 버튼 추가 */}
                        {userInfo && userInfo.nickname === reply.nickname && ( // userInfo가 null이 아닌 경우에만 체크
                          <>
                            <button
                              onClick={() => handleCommentEdit(reply.newzyCommentId, prompt("대댓글을 수정하세요", reply.newzyComment))} // 수정 버튼 클릭 시 수정 함수 호출
                              className="text-blue-600 ml-2"
                            >
                              수정
                            </button>
                            <button
                              onClick={() => handleCommentDelete(reply.newzyCommentId)} // 삭제 버튼 클릭 시 삭제 함수 호출
                              className="text-red-600 ml-2"
                            >
                              삭제
                            </button>
                          </>
                        )}
                      </div>
                      <div>{reply.newzyComment}</div>
                      <div className="text-gray-500 text-xs">
                        {new Date(reply.createdAt).toLocaleString('ko-KR', {
                          year: 'numeric',
                          month: '2-digit',
                          day: '2-digit',
                          hour: '2-digit',
                          minute: '2-digit',
                        })}
                      </div>
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