// entities/card/store/cardStore.js

import { create } from "zustand";
import { devtools } from "zustand/middleware";
import baseAxios from "shared/utils/baseAxios";

export const useCardStore = create(
  devtools((set) => ({
    summaryText: "",
    inputLength: 0,
    trimmedLength: 0,
    setSummaryText: (text) => set(() => ({ summaryText: text })),
    setInputLength: (length) => set(() => ({ inputLength: length })),
    setTrimmedLength: (length) => set(() => ({ trimmedLength: length })),
  }))
);

export const useNewsCardStore = create(
  devtools((set) => ({
    newsCards: [],
    totalPage: 1,
    newsCard: {},

    fetchNewsCardList: async (page) => {
      try {
        const newsCardListResponse = await baseAxios().get(
          "/news/news-card-list",
          {
            params: { page: page },
          }
        );
        // console.log("응답하니", newsCardResponse.data);
        set({
          newsCards: newsCardListResponse.data.newsCardList,
          totalPage: newsCardListResponse.data.totalPage,
        });
      } catch (error) {
        console.error("카드 리스트 받아오기 에러 당첨", error);
      }
    },

    fetchNewsCard: async (newsId) => {
      try {
        const newsCardResponse = await baseAxios().get(
          `/news/news-card-list/${newsId}`
        );

        set({ newsCard: newsCardResponse.data });
        // console.log("뉴스 카드 조회 잘 됌");
      } catch (error) {
        console.error("카드 조회 실 패 !", error);
      }
    },

    postNewsCard: async (category, newsId, userDifficulty, summaryText) => {
      try {
        const response = await baseAxios().post(
          `/news/${newsId}/collect-news-card`,
          {
            category: category,
            newsId: newsId,
            score: userDifficulty,
            summary: summaryText,
          }
        );
        console.log(response.data);
      } catch (error) {
        if (error.response) {
          console.error("Error response:", error.response.data);
        } else if (error.request) {
          console.error("No response received:", error.request);
        } else {
          console.error("Error setting up request:", error.message);
        }
      }
    },
  }))
);
