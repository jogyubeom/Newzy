const dummyData = [
  {
    newzyid: 1,
    title: "Content 1",
    content: "Quam id leo in vitae turpis massa.",
    thumbnail: "https://picsum.photos/300?random=1",
  },
  {
    newzyid: 2,
    title: "Content 2",
    content: "Quam id leo in vitae turpis massa.",
    thumbnail: "https://picsum.photos/300?random=2",
  },
  {
    newzyid: 3,
    title: "Content 3",
    content: "Quam id leo in vitae turpis massa.",
    thumbnail: "https://picsum.photos/300?random=3",
  },
  {
    newzyid: 4,
    title: "Content 4",
    content: "Quam id leo in vitae turpis massa.",
    thumbnail: "https://picsum.photos/300?random=4",
  },
];

export const HotNewzy = () => {
  return (
    <div>
      <h2 className="text-lg font-semibold mb-4">많이 본 뉴지</h2>
      <div className="grid grid-cols-2 gap-4">
        {dummyData.map((item) => (
          <div
            key={item.newzyid}
            className="relative w-[311px] h-[138px] shadow-lg rounded-lg flex flex-row justify-start items-center gap-4 p-4 overflow-hidden bg-[#F7F4F4]"
          >
            <div className="flex-grow w-[187px] h-[76px] flex flex-col justify-start items-start gap-2">
              <div className="flex-shrink-0 w-full h-5 text-[#26262C] font-sans text-sm leading-5 font-semibold flex items-center">
                {item.title}
              </div>
              <div className="flex-shrink-0 w-full h-12 text-[#747483] overflow-hidden whitespace-nowrap text-ellipsis font-sans text-xs leading-4 font-normal flex items-center">
                {item.content}
              </div>
            </div>
            <img
              src={item.thumbnail}
              className="flex-shrink-0 w-[76px] h-[76px] rounded-lg object-cover"
              alt={item.title}
            />
          </div>
        ))}
      </div>
    </div>
  );
};
