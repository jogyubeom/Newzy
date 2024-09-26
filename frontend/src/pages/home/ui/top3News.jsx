export const Top3News = () => {
  // 더미 데이터
  const news = [
    {
      newsId: 1,
      title: "증권사 2분기 순이익, 전 분기比 30% 감소… 실적 양극화 진행 중",
    },
    {
      newsId: 2,
      title: "추석 열차에 빈자리 텅텅…알고보니 20만표가 안타요, 안타",
    },
    {
      newsId: 3,
      title: "유창한 영어로 美보안회사 원격 근무... 그는 北에 있는 스파이였다",
    },
  ];

  return (
    <div className="shadow-md rounded bg-gray-200 p-4 flex flex-col gap-4">
      <div className="w-full text-center text-xl font-semibold text-gray-800">
        Newzy TOP3 News
      </div>

      {news.map((item, index) => (
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
