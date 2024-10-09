import React, { useState, useEffect } from 'react';
import baseAxios from 'shared/utils/baseAxios';
import useAuthStore from 'shared/store/userStore';
import CommentInput from './commentInput';
import CommentList from './commentList';

const CommentContent = ({ newzyId }) => {
  const { token, userInfo } = useAuthStore(state => ({
    token: state.token,
    userInfo: state.userInfo,
  }));

  const [comments, setComments] = useState([]);
  const [newCommentText, setNewCommentText] = useState('');
  const [replyText, setReplyText] = useState({});

  const fetchComments = async () => {
    try {
      const res = await baseAxios().get(`/newzy/${newzyId}/comments`);
      setComments(res.data);
    } catch (error) {
      console.error(error);
    }
  };

  useEffect(() => {
    fetchComments();
  }, [newzyId]);

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
        await baseAxios().post(`/newzy/${newzyId}/comments`, newComment);
        await fetchComments();
        if (parentCommentId === null) setNewCommentText('');
        setReplyText((prev) => ({ ...prev, [parentCommentId]: '' }));
      } catch (error) {
        console.error(error);
      }
    }
  };

  const handleCommentDelete = async (commentId) => {
    try {
      await baseAxios().delete(`/newzy/${newzyId}/comments/${commentId}`);
      await fetchComments();
    } catch (error) {
      console.error('댓글 삭제 오류:', error);
    }
  };

  const handleCommentEdit = async (commentId, updatedText) => {
    try {
      await baseAxios().put(`/newzy/${newzyId}/comments/${commentId}`, { newzyComment: updatedText });
      await fetchComments();
    } catch (error) {
      console.error('댓글 수정 오류:', error);
    }
  };

  const groupComments = (comments) => {
    const grouped = comments.reduce((acc, comment) => {
      if (comment.parentCommentId === null) {
        acc.push({ ...comment, replies: [] });
      }
      return acc;
    }, []);

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
      <CommentInput
        newCommentText={newCommentText}
        setNewCommentText={setNewCommentText}
        handleCommentSubmit={handleCommentSubmit}
      />
      <CommentList
        groupedComments={groupedComments}
        userInfo={userInfo}
        handleCommentEdit={handleCommentEdit}
        handleCommentDelete={handleCommentDelete}
        replyText={replyText}
        setReplyText={setReplyText}
        handleReplySubmit={handleCommentSubmit}
      />
    </div>
  );
};

export default CommentContent;