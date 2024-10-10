import React from 'react';
import NewsItem from '../../pages/newsList/ui/newsItem';
import NewzyItem from '../../pages/newzyList/ui/newzyItem';

const PostList = ({ posts, onPostClick, type }) => {
  const ItemComponent = type === 'news' ? NewsItem : type === 'newzy' ? NewzyItem : null;

  // posts가 빈 배열일 때 출력할 문구
  const emptyMessage = type === 'news' 
    ? '표시할 뉴스가 없습니다.' 
    : type === 'newzy' 
    ? '표시할 뉴지가 없습니다.' 
    : '표시할 게시물이 없습니다.';

  return (
    <>
      {posts.length === 0 ? ( // posts 배열이 비어 있을 때
        <p className="text-center text-gray-500 mt-4">{emptyMessage}</p>
      ) : (
        posts.map((post) => {
          const key = type === 'news' ? post.newsId : type === 'newzy' ? post.newzyId : post.id;
          return <ItemComponent key={key} post={post} onPostClick={onPostClick} />;
        })
      )}
    </>
  );
};

export default PostList;