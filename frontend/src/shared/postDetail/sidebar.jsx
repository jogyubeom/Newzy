import React from 'react';
import CommentContent from '../../pages/newzyDetail/ui/comment';
import SearchContent from './search';

const Sidebar = ({ activeSidebar, onActiveSidebar, category }) => {
  const sidebarContent = {
    댓글: <CommentContent />, // 댓글 내용을 위한 컴포넌트
    검색: <SearchContent category={category}/>,   // 검색 내용을 위한 컴포넌트
  };

  const title = activeSidebar === '댓글' ? '댓글' : activeSidebar === '검색' ? '단어 검색' : '';

  return (
    <div
      className={`absolute top-0 right-0 h-full w-[400px] bg-white border-l border-gray-200 shadow-lg rounded-l-2xl transition-transform transform ${
        activeSidebar ? 'translate-x-0 opacity-100' : 'translate-x-full opacity-0'
      } ${!activeSidebar ? 'hidden' : ''}`}
      style={{ zIndex: 10 }}
    >
      <div className="flex justify-between items-center p-4 bg-white border-b border-gray-200 sticky top-0">
        <h2 className="text-xl font-bold m-0">{title}</h2>
        <button
          className="text-gray-500 hover:text-gray-700 focus:outline-none"
          onClick={() => onActiveSidebar(null)}
        >
          <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth="3.0" stroke="currentColor" className="w-6 h-6">
            <path strokeLinecap="round" strokeLinejoin="round" d="M6 18L18 6M6 6l12 12" />
          </svg>
        </button>
      </div>
      <div className="overflow-y-auto overflow-x-hidden h-[calc(100%-64px)]">
        {activeSidebar ? sidebarContent[activeSidebar] : null}
      </div>
    </div>
  );
};

export default Sidebar;
