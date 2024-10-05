import useHomeStore from "../store/useHomeStore";

export const Top3News = () => {
  const { top3News } = useHomeStore();
  console.log(top3News);
  return (
    <div className="shadow-md rounded bg-gray-200 p-4 flex flex-col gap-4">
      <div className="w-full text-center text-xl font-semibold text-gray-800">
        TOP3 News
      </div>

      {top3News?.map((item, index) => (
        <div key={item.newsId} className="flex gap-3">
          <div
            className={`font-semibold ${
              index === 0
                ? "text-red-500"
                : index === 1
                ? "text-orange-500"
                : "text-yellow-500"
            }`}
          >
            {index + 1}
          </div>
          <div className="w-full">
            <div className="text-gray-800 font-semibold text-sm truncate">
              {item.title}
            </div>
          </div>
        </div>
      ))}
    </div>
  );
};
