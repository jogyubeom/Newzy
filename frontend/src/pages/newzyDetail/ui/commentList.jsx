import React from 'react';
import Comment from './comment';

const CommentList = ({
  groupedComments,
  userInfo,
  handleCommentEdit,
  handleCommentDelete,
  replyText,
  setReplyText,
  handleReplySubmit,
}) => {
  return (
    <div>
      {groupedComments.length > 0 ? (
        groupedComments.map((comment) => (
          <div key={comment.newzyCommentId}>
            <Comment
              comment={comment}
              userInfo={userInfo}
              handleCommentEdit={handleCommentEdit}
              handleCommentDelete={handleCommentDelete}
              replyText={replyText}
              setReplyText={setReplyText}
              handleReplySubmit={handleReplySubmit}
            />
            {comment.replies?.length > 0 && (
              <div className="ml-4 mt-2">
                {comment.replies.map((reply) => (
                  <Comment
                    key={reply.newzyCommentId}
                    comment={reply}
                    userInfo={userInfo}
                    handleCommentEdit={handleCommentEdit}
                    handleCommentDelete={handleCommentDelete}
                    replyText={replyText}
                    setReplyText={setReplyText}
                    handleReplySubmit={handleReplySubmit}
                  />
                ))}
              </div>
            )}
          </div>
        ))
      ) : (
        <p className="text-gray-500">댓글이 없습니다.</p>
      )}
    </div>
  );
};

export default CommentList;