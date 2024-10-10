/* eslint-disable react/prop-types */
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import baseAxios from "shared/utils/baseAxios";
import useAuthStore from "shared/store/userStore"; 
import { useFollowStore } from "../store/useFollowStore";

const FollowListItem = ({ PageOwner, name, isFollowing: initialFollowing }) => {
  const [profile, setProfile] = useState(null); // 프로필 정보
  const [isFollowing, setIsFollowing] = useState(initialFollowing); // 팔로우 상태
  const defaultProfileImage = "/shared/images/user.png";  // 기본 프로필 이미지
  const nav = useNavigate()
  
  const { followers, followings, updateFollowStatus, fetchFollowers, fetchFollowings } = useFollowStore();
  const { userInfo, fetchFollowers: loggedInUserFollowers, fetchFollowings: loggedInUserFollowings, followings: loggedInUserFollowingsIndex } = useAuthStore();  // ✅ 로그인된 사용자 정보

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

  useEffect(() => {
    setIsFollowing(initialFollowing);  // initialFollowing이 변경될 때마다 상태 업데이트
  }, [initialFollowing]);

  // 팔로우/언팔로우 버튼 클릭
  const toggleFollow = async () => {
    try {
      if (isFollowing) {
        await baseAxios().delete(`/user/${name}/follower`);
      } else {
        await baseAxios().post(`/user/${name}/follower`);
      }

    // ✅ 팔로워/팔로잉 수를 업데이트하기 위해 팔로워/팔로잉 목록 다시 불러오기
    await fetchFollowers(PageOwner.nickname);  // 팔로워 목록 다시 불러오기
    await fetchFollowings(PageOwner.nickname); // 팔로잉 목록 다시 불러오기

    // ✅ 로그인한 현재 사용자의 팔로워/팔로잉 목록도 다시 불러오기
    await loggedInUserFollowers(userInfo.nickname);  // 팔로워 목록 다시 불러오기
    await loggedInUserFollowings(userInfo.nickname); // 팔로잉 목록 다시 불러오기

    } catch (error) {
      console.error("팔로우/언팔로우 처리 중 에러 발생:", error);
    }
  };

  // 현재 사용자와 목록의 유저가 동일한지 확인
  const isCurrentUser = userInfo?.nickname === name;

  return (
    <>
      <div className="flex items-center justify-between mb-1">
        <div className="flex items-center justify-around gap-5">
          {/* 프로필 이미지 */}
          <div className="rounded-full overflow-hidden w-16 h-16" onClick={() => {nav(`/profile/${name}`)}}>
            <img
              src={profile?.profile || defaultProfileImage}
              className="w-full h-full object-cover"
            />
          </div>
          {/* 이름 */}
          <div className="text-lg font-semibold">{name}</div>
        </div>

        {/* 자기 자신이 아닐 때만 팔로우/언팔로우 버튼 보여주기, 공간은 유지 */}
        <button
          onClick={toggleFollow}
          className={`w-32 h-12 px-4 py-2 border-2 rounded-[15px] font-semibold transition-colors duration-300
            ${isFollowing ? 'border-red-500 text-red-500 hover:bg-red-500 hover:text-white' : 'border-blue-500 text-blue-500 hover:bg-blue-500 hover:text-white'}`}
          style={{ visibility: isCurrentUser ? 'hidden' : 'visible' }}  // 자기 자신이면 버튼은 숨기되, 공간은 유지
        >
          {isFollowing ? 'Unfollow' : 'Follow'}
        </button>
      </div>
      <div className="border-t border-gray-300"></div>
    </>

  );
};

export default FollowListItem;
