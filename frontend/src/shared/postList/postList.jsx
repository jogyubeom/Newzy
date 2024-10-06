import React from 'react';
import NewsItem from '../../pages/newsList/ui/newsItem';
import NewzyItem from '../../pages/newzyList/ui/newzyItem';

const PostList = ({ posts, onPostClick, type }) => {
  const ItemComponent = type === 'news' ? NewsItem : type === 'newzy' ? NewzyItem : null;

  return (
    <>
      {posts.map((post) => {
        const key = type === 'news' ? post.newsId : type === 'newzy' ? post.newzyId : post.id;
        return <ItemComponent key={key} post={post} onPostClick={onPostClick} />;
      })}
    </>
  );
};

export default PostList;