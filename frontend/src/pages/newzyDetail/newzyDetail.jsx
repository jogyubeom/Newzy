import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import NewzyInfo from "./ui/newzyInfo";
import Content from "./ui/content";
import UtilityButtons from "./ui/utilityButtons";
import Sidebar from "./ui/sidebar";
import { CardGauge } from "entities/card/cardGauge";

export const NewzyDetail = () => {
  const [activeSidebar, setActiveSidebar] = useState(null);

  const handleSidebarToggle = (type) => {
    setActiveSidebar((prev) => (prev === type ? null : type));
  };

  useEffect(() => {
    window.scrollTo(0, 0);
  }, []);

  const { id } = useParams();

  const htmlContent = `<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.</p>`;

  return (
    <div className="relative flex h-screen bg-white">
      <CardGauge />
      <div className="w-[17%]"></div>

      <div className="flex-1 p-6">
        <NewzyInfo
          category="시사"
          title="제목이 들어갑니다"
          date="2024.09.19. 오후 3:58"
          author="김싸피 뉴포터"
        />
        <Content htmlContent={htmlContent} />
      </div>

      <div className="w-[17%]"></div>

      <UtilityButtons
        onActiveSidebar={handleSidebarToggle}
        activeSidebar={activeSidebar}
      />

      <Sidebar
        activeSidebar={activeSidebar}
        onActiveSidebar={handleSidebarToggle}
      />
    </div>
  );
};
