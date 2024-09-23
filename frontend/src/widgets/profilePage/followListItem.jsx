/* eslint-disable react/prop-types */
const FollowListItem = ({ name, img, follow }) => {
  return (
    <>
      <div className="flex items-center justify-between mb-1">
        <div className="flex items-center justify-around gap-5">
          {/* 프로필 이미지 */}
          <div className="rounded-full overflow-hidden w-16 h-16">
            <img
              src={img}
              alt={`${name}의 프로필`}
              className="w-full h-full object-cover"
            />
          </div>
          {/* 이름 */}
          <div className="text-lg font-semibold">{name}</div>
        </div>
        {/* 팔로우/언팔로우 버튼 */}
        <button
          className={`w-32 h-12 px-4 py-2 border-2 rounded-[15px] font-semibold transition-colors duration-300
            ${follow ? 'border-red-500 text-red-500 hover:bg-red-500 hover:text-white' 
                     : 'border-blue-500 text-blue-500 hover:bg-blue-500 hover:text-white'}`}
        >
          {follow ? 'Unfollow' : 'Follow'}
        </button>
      </div>
      <div className="border-t border-gray-300"></div>
    </>

  );
};

export default FollowListItem;
