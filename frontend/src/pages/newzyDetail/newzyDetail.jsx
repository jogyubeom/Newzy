import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import axios from 'axios';
import NewzyInfo from './ui/newzyInfo';
import Content from '../../shared/postDetail/content';
import UtilityButtons from './ui/utilityButtons';
import Sidebar from '../../shared/postDetail/sidebar';
import useAuthStore from 'shared/store/userStore'; // Zustand store import

export const NewzyDetail = () => {
  const [activeSidebar, setActiveSidebar] = useState(null);
  const [newzy, setNewzy] = useState(null);

  const { id } = useParams();
  const token = useAuthStore(state => state.token); // 토큰 가져오기

  const handleSidebarToggle = (type) => {
    setActiveSidebar((prev) => (prev === type ? null : type));
  };

  useEffect(() => {
    window.scrollTo(0, 0);
    fetchNewzy();
  }, []);

  useEffect(() => {
    if (activeSidebar) {
      window.scrollTo(0, 0);
    }
  }, [activeSidebar]);

  const fetchNewzy = async () => {
    try {
      const response = await axios.get(`https://j11b305.p.ssafy.io/api/newzy/${id}`);
      setNewzy(response.data);
    } catch (error) {
      console.error("Error fetching newzy details:", error);
    }
  };

  const htmlContent = newzy ? newzy.content : "";

  return (
    <div className="relative flex h-screen bg-white">
      <div className="w-[17%]"></div>

      <div className="flex-1 p-6">
        {newzy && (
          <NewzyInfo 
            category={getCategoryName(newzy.category)}
            title={newzy.title} 
            date={new Date(newzy.createdAt).toLocaleString('ko-KR')}
            author={newzy.nickname}
          />
        )}
        <Content htmlContent={htmlContent} />
      </div>

      <div className="w-[17%]"></div>

      {newzy && ( // newzy가 존재할 때만 UtilityButtons 렌더링
        <UtilityButtons 
          onActiveSidebar={handleSidebarToggle} 
          activeSidebar={activeSidebar} 
          isLiked={newzy.isLiked}
          isBookmarked={newzy.isBookmarked}
          newzyId={newzy.newzyId} // newzy ID를 전달
        />
      )}
      {newzy && ( // newzy가 존재하는 경우에만 Sidebar를 렌더링
        <Sidebar 
          activeSidebar={activeSidebar} 
          onActiveSidebar={handleSidebarToggle} 
          category={3}
        />
      )}
    </div>
  );
};

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
