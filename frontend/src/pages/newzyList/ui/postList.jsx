import React from 'react';
import PostItem from './postItem';

const PostList = ({ posts, onPostClick }) => {
  return (
    <>
      {posts.map((post) => (
        <PostItem key={post.id} post={post} onPostClick={onPostClick} />
      ))}
    </>
  );
};

export default PostList;
