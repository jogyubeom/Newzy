/* eslint-disable react/prop-types */
import { useEffect, useState } from "react";
import baseAxios from "shared/utils/baseAxios";

const FollowListItem = ({ name, isFollowing: initialFollowing }) => {
  const [profile, setProfile] = useState(null); // 프로필 정보
  const [isFollowing, setIsFollowing] = useState(initialFollowing); // 팔로우 상태

  // 유저 프로필 조회
  const fetchUserProfile = async () => {
    try {
      const { data } = await baseAxios().get(`/user/profile/${name}`);
      setProfile(data);
    } catch (error) {
      console.error("유저 프로필 조회 중 에러 발생:", error);
    }
  };

  useEffect(() => {
    fetchUserProfile();
  }, [name]);

  // 프로필이 없을 때 로딩 중 표시
  if (!profile) {
    return (
      <div className="flex items-center justify-center h-16">
        <p>로딩 중...</p>
      </div>
    );
  }

  // 팔로우/언팔로우 버튼 클릭
  const toggleFollow = async () => {
    try {
      if (isFollowing) {
        await baseAxios().delete(`/user/${name}/follower`);
      } else {
        await baseAxios().post(`/user/${name}/follower`);
      }
      // 팔로우 상태를 로컬에서 토글
      setIsFollowing(!isFollowing);
    } catch (error) {
      console.error("팔로우/언팔로우 처리 중 에러 발생:", error);
    }
  };

  return (
    <>
      <div className="flex items-center justify-between mb-1">
        <div className="flex items-center justify-around gap-5">
          {/* 프로필 이미지 */}
          <div className="rounded-full overflow-hidden w-16 h-16">
            <img
              src={profile.profile || "/shared/images/user.png"}
              className="w-full h-full object-cover"
            />
          </div>
          {/* 이름 */}
          <div className="text-lg font-semibold">{name}</div>
        </div>
        {/* 팔로우/언팔로우 버튼 */}
        <button
          onClick={toggleFollow}
          className={`w-32 h-12 px-4 py-2 border-2 rounded-[15px] font-semibold transition-colors duration-300
            ${isFollowing ? 'border-red-500 text-red-500 hover:bg-red-500 hover:text-white' : 'border-blue-500 text-blue-500 hover:bg-blue-500 hover:text-white'}`}
        >
          {isFollowing ? 'Unfollow' : 'Follow'}
        </button>
      </div>
      <div className="border-t border-gray-300"></div>
    </>

  );
};

export default FollowListItem;
