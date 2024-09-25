import React, { useEffect } from 'react';
import { useParams } from 'react-router-dom';
import NewzyInfo from './ui/newzyInfo';
import Content from './ui/content';
import UtilityButtons from './ui/utilityButtons';

export const NewzyDetail = () => {
  useEffect(() => {
    window.scrollTo(0, 0);
  }, []);

  const { id } = useParams();

  const htmlContent = `<p>여기에 본문 내용이 들어갑니다.</p>`;

  return (
    <div className="relative flex h-screen bg-white">
      <div className="w-[15%]"></div>

      <div className="flex-1 p-6">
        <NewzyInfo 
          category="시사" 
          title="제목이 들어갑니다" 
          date="2024.09.19. 오후 3:58" 
          author="김싸피 뉴포터" 
        />
        <Content htmlContent={htmlContent} />
      </div>

      <div className="w-[15%]"></div>

      <UtilityButtons />
    </div>
  );
};