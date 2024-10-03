import { Header } from "./ui/header";
import { RecommendNews } from "./ui/recommendNews";
import { Top3News } from "./ui/top3News";
import { WordSearchCloud } from "./ui/wordSearchCloud";
import { WeekCardWinner } from "./ui/weekCardWinner";
import { WeekNewpoter } from "./ui/weekNewporter";
import { HotNewzy } from "./ui/hotNewzy";
import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import useAuthStore from 'shared/store/userStore';

export const Home = () => {
  const setToken = useAuthStore((state) => state.setToken);
  const token = useAuthStore((state) => state.token); // 이미 저장된 토큰 확인
  const navigate = useNavigate();

  useEffect(() => {
    // 토큰이 이미 저장되어 있는 경우에는 쿼리 파라미터 확인을 건너뜀
    if (token) return;

    // URL에서 쿼리 파라미터 추출
    const queryParams = new URLSearchParams(window.location.search);
    const queryToken = queryParams.get('token');

    // 쿼리 파라미터에 토큰이 있으면 상태에 저장
    if (queryToken) {
      setToken(queryToken);

      // 쿼리 파라미터에서 토큰을 제거하고 홈 화면으로 리다이렉트
      navigate('/', { replace: true });
    }
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

        <div className="flex col-span-12 lg:col-span-12">
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
