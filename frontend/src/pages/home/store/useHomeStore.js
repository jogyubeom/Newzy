// store/useHomeStore.js
import { create } from "zustand";
import { devtools } from "zustand/middleware";
import baseAxios from "shared/utils/baseAxios";

const useHomeStore = create(
  devtools((set) => ({
    todayNews: null,
    recommendNews: null,
    top3News: null,
    wordSearchCloud: null,
    weekCardWinner: null,
    weekNewporter: null,
    hotNewzy: null,

    fetchHomeData: async () => {
      try {
        // API 호출
        const todayNewsResponese = await baseAxios().get("/news/daily");
        const recommendNewsResponse = await baseAxios().get("/news/recommend");
        const top3NewsResponse = await baseAxios().get("/news/hot");
        const wordSearchResponse = await baseAxios().get("/word/wordcloud");
        // const weekCardWinnerResponse = await baseAxios().get(
        //   ""
        // );
        // const weekNewporterResponse = await baseAxios().get("");
        // const hotNewzyResponse = await baseAxios().get("/newzy/hot");

        set({
          todayNews: todayNewsResponese.data,
          recommendNews: recommendNewsResponse.data,
          top3News: top3NewsResponse.data,
          wordSearchCloud: wordSearchResponse.data,
          //   weekCardWinner: weekCardWinnerResponse.data,
          //   weekNewporter: weekNewporterResponse.data,
          // hotNewzy: hotNewzyResponse.data,
        });
      } catch (error) {
        console.error("Error fetching home data:", error);
      }
    },
  }))
);

export default useHomeStore;
