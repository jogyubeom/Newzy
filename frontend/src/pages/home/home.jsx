// pages/home/home.jsx

import { useEffect } from "react";
import { useNavigate } from "react-router-dom";

import { Header } from "./ui/header";
import { RecommendNews } from "./ui/recommendNews";
import { Top3News } from "./ui/top3News";
import { WordSearchCloud } from "./ui/wordSearchCloud";
import { WeekCardWinner } from "./ui/weekCardWinner";
import { WeekNewpoter } from "./ui/weekNewporter";
import { HotNewzy } from "./ui/hotNewzy";
import useHomeStore from "./store/useHomeStore";
import useAuthStore from "shared/store/userStore";
import baseAxios from "shared/utils/baseAxios";

export const Home = () => {
  const {
    recommendNews,
    top3News,
    wordSearchData,
    hotNewzy,
    fetchHomeData, // API 호출 함수
  } = useHomeStore();

  // 컴포넌트가 마운트될 때 한 번만 API 요청
  useEffect(() => {
    fetchHomeData(); // API 호출
  }, [fetchHomeData]);

  // 데이터가 아직 로딩 중일 때 로딩 상태 표시
  // if (!recommendNews || !top3News || !wordSearchData || !hotNewzy) {
  //   return <div>Loading...</div>;
  // }

  const setToken = useAuthStore((state) => state.setToken);
  const token = useAuthStore((state) => state.token); // 이미 저장된 토큰 확인
  const navigate = useNavigate();

  useEffect(() => {
    // 토큰이 이미 저장되어 있는 경우에는 쿼리 파라미터 확인을 건너뜀
    if (token) return;

    const checkAndNavigate = async () => {
      // URL에서 쿼리 파라미터 추출
      const queryParams = new URLSearchParams(window.location.search);
      const queryToken = queryParams.get("token");

      if (queryToken) {
        // 쿼리 파라미터에 토큰이 있으면 상태에 저장
        setToken(queryToken);
  
        // 어휘력 테스트 여부 확인
        try {
          const res = await baseAxios().get("/user/first/login");
          if (res.data.firstLogin) {
            // firstLogin이 true이면 어휘력 테스트로 리다이렉트
            navigate("/usertest", { replace: true });
          } else {
            // firstLogin이 false이면 홈 화면으로 리다이렉트
            navigate("/", { replace: true });
          }
        } catch (error) {
          console.error("어휘력 테스트 체크 중 오류 발생:", error);
          navigate("/", { replace: true });
        }
      } else {
        // 토큰이 없으면 홈 화면으로 리다이렉트
        navigate("/", { replace: true });
      }
    };
  
    checkAndNavigate();
  }, [token, setToken, navigate]);

  return (
    <div className="relative">
      {/* Header */}
      <Header />

      {/* Main Grid */}
      <div className="grid grid-cols-12 gap-4 p-4">
        <div className="col-span-12 lg:col-span-9">
          <RecommendNews />
        </div>

        <div className="ml:col-span-12 lg:col-span-3">
          <Top3News />
        </div>

        <div className="flex flex-col lg:flex-row col-span-12 lg:col-span-12 gap-4">
          <div className="flex flex-col">
            <div className="mb-4">
              <WordSearchCloud />
            </div>

            <div>
              <WeekNewpoter />
            </div>
          </div>

          <div className="flex flex-col">
            <div className="mb-4">
              <WeekCardWinner />
            </div>
            <div>
              <HotNewzy />
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};
