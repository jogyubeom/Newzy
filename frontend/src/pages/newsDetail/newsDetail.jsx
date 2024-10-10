import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import baseAxios from "../../shared/utils/baseAxios";
import NewsInfo from "./ui/newsInfo";
import Content from "../../shared/postDetail/content";
import UtilityButtons from "./ui/utilityButtons";
import Sidebar from "../../shared/postDetail/sidebar";
import { CardGauge } from "entities/card/cardGauge";
import { useCardStore } from "entities/card/store/cardStore";

export const NewsDetail = () => {
  const { setSummaryText, setInputLength, setTrimmedLength } = useCardStore();
  const [activeSidebar, setActiveSidebar] = useState(null);
  const [news, setNews] = useState(null);
  const { id } = useParams();

  const handleSidebarToggle = (type) => {
    setActiveSidebar((prev) => (prev === type ? null : type));
  };

  useEffect(() => {
    setSummaryText("");
    setInputLength(0);
    setTrimmedLength(0);
  }, []);

  useEffect(() => {
    window.scrollTo(0, 0);
    fetchNews();
  }, []);

  useEffect(() => {
    if (activeSidebar) {
      window.scrollTo(0, 0);
    }
  }, [activeSidebar]);

  const fetchNews = async () => {
    try {
      const response = await baseAxios().get(`/news/${id}`);
      setNews(response.data);
      // setNewsData(response.data);
      // console.log(response.data);
    } catch (error) {
      console.error("Error fetching news details:", error);
    }
  };

  const htmlContent = news ? news.content : "";

  return (
    <div className="relative flex min-h-screen bg-white">
      <div className="w-[17%]"></div>

      <div className="flex-1 p-6">
        {news && (
          <NewsInfo
            category={getCategoryName(news.category)}
            title={news.title}
            date={new Date(news.createdAt).toLocaleString("ko-KR")}
            publisher={news.publisher}
          />
        )}
        <Content htmlContent={htmlContent} />
      </div>

      <div className="w-[17%]"></div>

      {/* 안전하게 news가 존재할 때만 isLiked와 isBookmarked를 전달 */}
      {news && (
        <UtilityButtons
          onActiveSidebar={handleSidebarToggle}
          activeSidebar={activeSidebar}
          isLiked={news.isLiked}
          isBookmarked={news.isBookmarked}
          newsId={news.newsId}
        />
      )}
      {news && ( // news가 존재하는 경우에만 Sidebar를 렌더링
        <Sidebar
          activeSidebar={activeSidebar}
          onActiveSidebar={handleSidebarToggle}
          category={news.category}
        />
      )}

      <CardGauge news={news} />
    </div>
  );
};

const getCategoryName = (category) => {
  switch (category) {
    case 0:
      return "경제";
    case 1:
      return "사회";
    case 2:
      return "세계";
    default:
      return "";
  }
};
