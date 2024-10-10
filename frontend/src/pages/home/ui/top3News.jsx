import { Navigate, useNavigate } from "react-router-dom";
import useHomeStore from "../store/useHomeStore";

export const Top3News = () => {
  const navigate = useNavigate();
  const { top3News } = useHomeStore();
  const handleTop3News = (id) => navigate(`/news/detail/${id}`);
  // console.log(top3News);
  return (
    <div className="shadow-md rounded bg-gray-200 p-3 flex flex-col gap-2">
      <div className="w-full text-center text-2xl font-semibold text-gray-800 mb-4">
        TOP3 News
      </div>

      {top3News?.map((item, index) => (
        <div key={item.newsId} className="flex items-start gap-3">
          <div
            className={`font-semibold text-xl ${
              index === 0
                ? "text-red-500"
                : index === 1
                ? "text-orange-500"
                : "text-yellow-500"
            }`}
          >
            {index + 1}
          </div>
          <div className="flex-1">
            <button
              className="text-gray-800 font-semibold text-left text-lg whitespace-normal"
              onClick={() => handleTop3News(item.newsId)}
            >
              {item.title}
            </button>
          </div>
        </div>
      ))}
    </div>
  );
};
