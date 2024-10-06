import React from 'react';
import CategoryBadge from '../../../shared/categoryBadge'

const NewzyItem = ({ post, onPostClick }) => {

  const getCategoryName = (category) => {
    switch (category) {
      case 0:
        return "시사";
      case 1:
        return "문화";
      case 2:
        return "자유";
      default:
        return "";
    }
  };

  return (
    <div className="flex items-center border-b py-4 px-4 h-40">
      <div className="w-[100px] flex flex-col justify-center items-start pr-6 border-r">
        <div className="text-sm text-gray-500 mb-1">{post.createdAt.slice(0, 10)}</div>

        <div className="flex items-center text-sm text-gray-500 mb-1">
          <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 16 16" fill="currentColor" className="w-4 h-4 mr-1">
            <path d="M8 9.5a1.5 1.5 0 1 0 0-3 1.5 1.5 0 0 0 0 3Z" />
            <path fillRule="evenodd" d="M1.38 8.28a.87.87 0 0 1 0-.566 7.003 7.003 0 0 1 13.238.006.87.87 0 0 1 0 .566A7.003 7.003 0 0 1 1.379 8.28ZM11 8a3 3 0 1 1-6 0 3 3 0 0 1 6 0Z" clipRule="evenodd" />
          </svg>
          {post.hit}
        </div>

        <div className="flex items-center text-sm text-gray-500">
          <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 16 16" fill="currentColor" className="w-4 h-4 mr-1">
            <path d="M2 6.342a3.375 3.375 0 0 1 6-2.088 3.375 3.375 0 0 1 5.997 2.26c-.063 2.134-1.618 3.76-2.955 4.784a14.437 14.437 0 0 1-2.676 1.61c-.02.01-.038.017-.05.022l-.014.006-.004.002h-.002a.75.75 0 0 1-.592.001h-.002l-.004-.003-.015-.006a5.528 5.528 0 0 1-.232-.107 14.395 14.395 0 0 1-2.535-1.557C3.564 10.22 1.999 8.558 1.999 6.38L2 6.342Z" />
          </svg>
          {post.likeCnt}
        </div>
      </div>

      <div
        className="flex-1 flex items-center justify-between pl-4 cursor-pointer"
        onClick={() => onPostClick(post.newzyId)}
      >
        <div className="pr-4">
          <div className="flex items-center mb-2">
            <CategoryBadge category={getCategoryName(post.category)} />
            <h2 className="text-xl font-semibold ml-2">{post.title}</h2>
          </div>
          <p className="text-sm text-gray-700">
            {post.contentText.length > 60 
              ? `${post.contentText.slice(0, 60)}...` 
              : post.contentText}
          </p>
        </div>
        <div>
          {post.thumbnail && (
            <img
              src={post.thumbnail}
              alt="썸네일"
              className="w-40 h-32 object-cover rounded-md shadow-md"
              style={{ minWidth: '10rem' }}
              onError={() => setIsError(true)} // 이미지 로딩 실패 시 상태 업데이트
            />
          )}
        </div>
      </div>
    </div>
  );
};

export default NewzyItem;
