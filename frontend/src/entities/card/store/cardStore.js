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

// NewsDetailStore에 devtools 적용
export const useNewsDetailStore = create(
  devtools((set) => ({
    newsData: null,
    setNewsData: (data) => set(() => ({ newsData: data })),
  }))
);

export const useNewsCardStore = create(
  devtools((set) => ({
    newsCards: [],
    totalPage: 1,

    fetchNewsCard: async (page = 1) => {
      try {
        const newsCardResponse = await baseAxios().get("/news/news-card-list", {
          params: { page: page },
        });
        // console.log("응답하니", newsCardResponse.data);
        set({
          newsCards: newsCardResponse.data.newsCardList,
          totalPage: newsCardResponse.data.totalPage,
        });
      } catch (error) {
        console.error("카드 리스트 받아오기 에러 당첨", error);
      }
    },
  }))
);
