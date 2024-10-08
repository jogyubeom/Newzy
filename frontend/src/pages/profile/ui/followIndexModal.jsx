/* eslint-disable react/prop-types */
import { FaUserCircle } from "react-icons/fa";
import { MdCancel } from "react-icons/md";
import { useState, useEffect } from "react";
import FollowListItem from "./followListItem";
import baseAxios from "shared/utils/baseAxios";

const FollowIndexModal = ({ isOpen, onClose, userInfo }) => {

  const [selectedMenu, setSelectedMenu] = useState(0);  // 메뉴 상태
  const [followers, setFollowers] = useState([]);       // 팔로워 목록
  const [followings, setFollowings] = useState([]);     // 팔로잉 목록
  const menus = [`팔로워 ${userInfo.followerCnt || 0}명`, `팔로잉 ${userInfo.followingCnt || 0}명`];  // 메뉴 항목에 팔로워, 팔로잉 수 반영


  const fetchFollowers = async () => {
    try {
      const { data } = await baseAxios().get(`/user/followers-list/${userInfo.nickname}`);
      setFollowers(data.followingList);
    } catch (error) {
      console.error("팔로워 목록을 불러오는 중 에러 발생:", error);
    }
  };

  const fetchFollowings = async () => {
    try {
      const { data } = await baseAxios().get(`/user/followings-list/${userInfo.nickname}`);
      setFollowings(data.followingList);
    } catch (error) {
      console.error("팔로잉 목록을 불러오는 중 에러 발생:", error);
    }
  };

  useEffect(() => {
    if (isOpen) {
      if (selectedMenu === 0) {
        fetchFollowers(); // 팔로워 목록 불러오기
      } else {
        fetchFollowings(); // 팔로잉 목록 불러오기
      }
    }
  }, [isOpen, selectedMenu]);

  // 팔로워/팔로잉 여부 체크
  const isFollowingUser = (username) => {
    return followings.some(following => following.toUserNickname === username);
  };

  const renderContent = () => {
    if (selectedMenu === 0) {
      // 팔로워 목록 렌더링 (팔로우 여부 확인)
      return followers.map((item) => (
        <div key={item.fromUserNickname} className="relative py-1 px-5 flex flex-col">
          <FollowListItem 
            name={item.fromUserNickname} 
            isFollowing={isFollowingUser(item.fromUserNickname)}  // 팔로우 여부 체크
          />
        </div>
      ));
    } else {
      // 팔로잉 목록 렌더링 (팔로우 여부 true로 고정)
      return followings.map((item) => (
        <div key={item.toUserNickname} className="relative py-1 px-5 flex flex-col">
          <FollowListItem 
            name={item.toUserNickname} 
            isFollowing={true}  // 항상 true로 설정
          />
        </div>
      ));
    }
  };

  if (!isOpen) return null;

  // userInfo가 없을 경우 로딩 중을 표시
  if (!userInfo) {
    return (
      <div className="flex items-center justify-center h-64">
        <p>로딩 중...</p>
      </div>
    );
  }

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div className="w-[480px] h-[650px] transform rotate-[0.5deg] rounded-[24px] bg-white border-[3px] border-gray-400 px-6">
        <header className="w-full flex justify-between items-center mt-4">
          <div className="flex-shrink-0 text-[#26262C] whitespace-nowrap text-ellipsis font-[Open Sans] text-[36px] leading-[24px] font-semibold flex items-center justify-between text-justify">
            <button className="w-[40px] h-[40px] bg-gray-200 rounded-full">
              <img src={userInfo.profile || "/shared/images/user.png"} className="w-full h-full object-cover rounded-full" />
            </button>
          </div>
          <div className="flex items-center">
            <button className="flex-shrink-0 w-[37px] h-[37px] flex items-center justify-center rounded-full hover:bg-white bg-opacity-30">
              <MdCancel className="w-10 h-10 text-red-600" onClick={onClose}/>
            </button>
          </div>
        </header>

        {/* MenuBar에 선택된 메뉴 상태와 상태 변경 함수 전달 */}
        <div className="flex items-center justify-center border-b-2 border-gray-300 font-semibold text-[18px]">
        {menus.map((menu, index) => (
          <div 
            key={index}
            className="relative inline-block cursor-pointer py-2 px-10"
            onClick={() => setSelectedMenu(index)} // 메뉴 클릭 시 선택 상태 변경
          >
            <span className={`${selectedMenu === index ? 'text-blue-500' : 'text-gray-400'} transition-all duration-300`}>
              {menu}
            </span>
            {/* 선택된 메뉴의 하단에 파란색 라인 */}
            <div className={`absolute bottom-[-3px] left-0 right-0 h-1 
              ${selectedMenu === index ? 'bg-blue-500' : 'bg-transparent'} 
              transition-all duration-300`}></div>
          </div>
          ))}
        </div>

      <div className="h-[520px] overflow-y-auto">
        {/* 선택된 메뉴에 따라 다른 컴포넌트 렌더링 */}
        {renderContent()}
      </div>
      </div>
    </div>
  );
};

export default FollowIndexModal;
