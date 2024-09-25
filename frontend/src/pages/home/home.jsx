import { Header } from "./ui/header";
import { RecommendNews } from "./ui/recommendNews";
import { Top3News } from "./ui/top3News";
import { WordCloud } from "./ui/wordCloud";
import { WeekCardWinner } from "./ui/weekCardWinner";
import { WeekNewpoter } from "./ui/weekNewporter";
import { HotNewzy } from "./ui/hotNewzy";

export const Home = () => {
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

        <div className="ml:col-span-12 lg:col-span-6">
          <WordCloud />
        </div>

        <div className="ml:col-span-12 lg:col-span-6">
          <WeekCardWinner />
        </div>

        <div className="ml:col-span-12 lg:col-span-6">
          <HotNewzy />
        </div>

        <div className="ml:col-span-12 lg:col-span-6">
          <WeekNewpoter />
        </div>
      </div>
    </div>
  );
};
