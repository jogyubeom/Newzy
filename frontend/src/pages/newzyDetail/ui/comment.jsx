import React, { useState } from 'react';
import ReplyInput from './replyInput';
import baseAxios from 'shared/utils/baseAxios';

const Comment = ({
  comment,
  userInfo,
  handleCommentEdit,
  handleCommentDelete,
  replyText,
  setReplyText,
  handleReplySubmit,
}) => {
  const [isEditing, setIsEditing] = useState(false); // 수정 모드 상태
  const [editedComment, setEditedComment] = useState(comment.newzyComment); // 수정할 댓글 내용 상태
  const [showReplyInput, setShowReplyInput] = useState(false);

  // 댓글 수정 저장 함수
  const handleSaveEdit = async () => {
    if (editedComment.trim() === '') {
      alert('댓글 내용을 입력해주세요.');
      return;
    }

    try {
      await baseAxios().patch(`/newzy/${comment.newzyId}/comments/${comment.newzyCommentId}`, {
        newzyComment: editedComment,
      });
      setIsEditing(false); // 수정 모드 종료
      handleCommentEdit(comment.newzyCommentId, editedComment); // 변경된 댓글 반영
    } catch (error) {
      console.error('댓글 수정 오류:', error);
    }
  };

  return (
    <div className="bg-white p-2 mb-2 rounded-md shadow-sm">
      <div className="flex items-center">
        {comment.profile ? (
          <img
            src={comment.profile}
            alt={`${comment.nickname}의 프로필`}
            className="w-8 h-8 rounded-full mr-2 border border-gray-300"
          />
        ) : (
          <div className="w-8 h-8 rounded-full mr-2" style={{ backgroundColor: '#4A90E2' }}></div>
        )}
        <span>{comment.nickname}</span>
        {userInfo && userInfo.nickname === comment.nickname && (
          <>
            {isEditing ? (
              <>
                <button
                  onClick={handleSaveEdit} // 수정 저장
                  className="text-green-600 ml-2"
                >
                  저장
                </button>
                <button
                  onClick={() => setIsEditing(false)} // 수정 취소
                  className="text-red-600 ml-2"
                >
                  취소
                </button>
              </>
            ) : (
              <>
                <button
                  onClick={() => setIsEditing(true)} // 수정 모드로 전환
                  className="text-blue-600 ml-2"
                >
                  수정
                </button>
                <button
                  onClick={() => handleCommentDelete(comment.newzyCommentId)} // 댓글 삭제
                  className="text-red-600 ml-2"
                >
                  삭제
                </button>
              </>
            )}
          </>
        )}
      </div>
      <div>
        {isEditing ? (
          // 수정 모드일 때는 input으로 댓글 내용 표시
          <input
            type="text"
            value={editedComment}
            onChange={(e) => setEditedComment(e.target.value)}
            className="border border-gray-300 rounded-md p-1 w-full"
          />
        ) : (
          // 수정 모드가 아닐 때는 일반 텍스트로 댓글 내용 표시
          <p>{comment.newzyComment}</p>
        )}
      </div>
      <div className="text-gray-500 text-sm">
        {new Date(comment.createdAt).toLocaleString('ko-KR', {
          year: 'numeric',
          month: '2-digit',
          day: '2-digit',
          hour: '2-digit',
          minute: '2-digit',
        })}
      </div>
      <button
        onClick={() => setShowReplyInput(!showReplyInput)}
        className="text-purple-600 underline mt-2"
      >
        {showReplyInput ? '대댓글 숨기기' : '대댓글 달기'}
      </button>

      {showReplyInput && (
        <ReplyInput
          replyText={replyText}
          setReplyText={setReplyText}
          commentId={comment.newzyCommentId}
          handleReplySubmit={handleReplySubmit}
        />
      )}
    </div>
  );
};

export default Comment;