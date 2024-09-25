import { Header } from "./ui/header";
import { RecommendNews } from "./ui/recommendNews";
import { Top3News } from "./ui/top3News";
import { WordSearchCloud } from "./ui/wordSearchCloud";
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
