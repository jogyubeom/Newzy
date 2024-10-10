// store/useHomeStore.js
import { create } from "zustand";
import { devtools } from "zustand/middleware";
import baseAxios from "shared/utils/baseAxios";

const useHomeStore = create(
  devtools((set) => ({
    todayNews: null,
    recommendNews: null,
    top3News: null,
    wordSearchCloud: [],
    weekCardWinner: null,
    weekNewporter: null,
    hotNewzy: null,

    fetchHomeData: async () => {
      try {
        const [
          todayNewsResponese,
          recommendNewsResponse,
          top3NewsResponse,
          wordSearchResponse,
          weekCardWinnerResponse,
          weekNewporterResponse,
          hotNewzyResponse,
        ] = await Promise.allSettled([
          baseAxios().get("/news/daily"),
          baseAxios().get("/news/recommend"),
          baseAxios().get("/news/hot"),
          baseAxios().get("/word/wordcloud"),
          baseAxios().get("/user/ranking/card-collector"),
          baseAxios().get("/user/ranking/newporter"),
          baseAxios().get("/newzy/hot"),
        ]);

        // 성공한 요청만 상태에 저장
        set({
          todayNews:
            todayNewsResponese.status === "fulfilled"
              ? todayNewsResponese.value.data
              : null,
          recommendNews:
            recommendNewsResponse.status === "fulfilled"
              ? recommendNewsResponse.value.data
              : null,
          top3News:
            top3NewsResponse.status === "fulfilled"
              ? top3NewsResponse.value.data
              : null,
          wordSearchCloud:
            wordSearchResponse.status === "fulfilled"
              ? wordSearchResponse.value.data
              : [],
          weekCardWinner:
            weekCardWinnerResponse.status === "fulfilled"
              ? weekCardWinnerResponse.value.data
              : null,
          weekNewporter:
            weekNewporterResponse.status === "fulfilled"
              ? weekNewporterResponse.value.data
              : null,
          hotNewzy:
            hotNewzyResponse.status === "fulfilled"
              ? hotNewzyResponse.value.data
              : null,
        });
      } catch (error) {
        console.error("Error fetching home data:", error);
      }
    },

    // 퀴즈 정답 제출 요청 함수
    postAnswer: async () => {
      try {
        const response = await baseAxios().post("/news/daily");
        console.log("정답 제출 성공", response.data);
        return response.data; // 성공한 데이터를 반환할 수 있음
      } catch (error) {
        console.error("정답 제출 실패", error);
        throw error; // 실패 시 오류를 던져서 호출한 곳에서 처리할 수 있도록
      }
    },
  }))
);

export default useHomeStore;
