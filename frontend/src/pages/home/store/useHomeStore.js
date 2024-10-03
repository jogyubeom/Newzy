// store/useHomeStore.js
import { create } from "zustand";
import { devtools } from "zustand/middleware"; // devtools 미들웨어 추가
import baseAxios from "shared/utils/baseAxios";

const useHomeStore = create(
  devtools((set) => ({
    recommendNews: null,
    top3News: null,
    wordSearchCloud: null,
    weekCardWinner: null,
    weekNewporter: null,
    hotNewzy: null,

    fetchHomeData: async () => {
      try {
        // API 호출
        // const recommendNewsResponse = await baseAxios().get("/news/recommend");
        const top3NewsResponse = await baseAxios().get("/news/hot");
        const wordSearchResponse = await baseAxios().get("/word/wordcloud");
        // const weekCardWinnerResponse = await baseAxios().get(
        //   "/api/weekly-card-winner"
        // );
        // const weekNewporterResponse = await baseAxios().get("/api/weekly-newporter");
        const hotNewzyResponse = await baseAxios().get("/newzy/hot");

        set({
          //   recommendNews: recommendNewsResponse.data,
          top3News: top3NewsResponse.data,
          wordSearchCloud: wordSearchResponse.data,
          //   weekCardWinner: weekCardWinnerResponse.data,
          //   weekNewporter: weekNewporterResponse.data,
          hotNewzy: hotNewzyResponse.data,
        });
      } catch (error) {
        console.error("Error fetching home data:", error);
      }
    },
  }))
);

export default useHomeStore;
