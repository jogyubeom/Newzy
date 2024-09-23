import React, { useState } from "react";
import { getGrade } from "../../shared/getGrade";
import MenuBar from "../../widgets/profilePage/Menubar";
import Mynewzy from "../../widgets/profilePage/Mynewzy";
import Bookmark from "../../widgets/profilePage/Bookmark";
import Words from "../../widgets/profilePage/Words";

// 임시 유저 더미데이터
const user = {
  name: "정지훈",
  grade: 3,
  introduce: "안녕하세요. 잘 부탁드립니다!",
  img: null,
  followers: 42,
  newzy: 7,
  followings: 25,
};

const Profile = () => {
  // 선택된 메뉴를 추적하는 상태
  const [selectedMenu, setSelectedMenu] = useState(0);

  // 선택된 메뉴에 따라 다른 컴포넌트 렌더링
  const renderContent = () => {
    switch (selectedMenu) {
      case 0:
        return <Mynewzy />;
      case 1:
        return <Bookmark />;
      case 2:
        return <Words />;
      default:
        return null;
    }
  };

  return (
    <div className="overflow-x-auto">
      <div className="min-w-[1200px] h-[409px] bg-[#132956] relative flex items-center px-8 mb-12">
        <div>
          <div className="absolute top-[70px] left-[53px] w-[270px] h-[270px] rounded-full border-[24px] border-yellow-500 flex items-center justify-center">
            <img
              src="https://cgeimage.commutil.kr/phpwas/restmb_allidxmake.php?pp=002&idx=3&simg=20240122131247036757d8bc5f1a81839820248.jpg&nmt=27"
              className="w-full h-full object-cover rounded-full"
            />
          </div>
          <div className="absolute top-[263px] left-[249px] w-[100px] h-[100px] bg-white bg-opacity-20 rounded-full flex items-center justify-center">
            <img
              src={getGrade(user.grade)}
              className="w-[60px] h-[60px] object-cover"
            />
          </div>
        </div>

        <div className="ml-[350px]">
          <div className="h-[103px] text-white font-[Open Sans] text-[46px] leading-[24px] font-semibold flex items-center">
            {user.name}
          </div>
          <div className="w-[200px] text-white font-[Open Sans] text-[24px] leading-[36px] font-semibold flex items-center text-left break-words whitespace-normal pe-5">
            {user.introduce}
          </div>
        </div>

        <div className="flex flex-col items-center ml-auto mt-auto">
          <div className="flex gap-20">
            <div className="flex flex-col items-center">
              <div className="w-[166px] h-[103px] text-white font-[Poppins] text-[36px] font-semibold flex items-center">
                Followers
              </div>
              <div className="w-[100px] h-[60px] text-white font-[Poppins] text-[36px] leading-[24px] font-semibold flex items-center justify-center text-center">
                {user.followers}
              </div>
            </div>
            <div className="flex flex-col items-center">
              <div className="w-[123px] h-[103px] text-white font-[Poppins] text-[36px] leading-[24px] font-semibold flex items-center">
                Newzy
              </div>
              <div className="w-[100px] h-[60px] text-white font-[Poppins] text-[36px] leading-[24px] font-semibold flex items-center justify-center text-center">
                {user.newzy}
              </div>
            </div>
            <div className="flex flex-col items-center">
              <div className="w-[188px] h-[103px] text-white font-[Poppins] text-[36px] leading-[24px] font-semibold flex items-center">
                Followings
              </div>
              <div className="w-[100px] h-[60px] text-white font-[Poppins] text-[36px] leading-[24px] font-semibold flex items-center justify-center text-center">
                {user.followings}
              </div>
            </div>
          </div>
          <div className="w-[293px] h-[103px] relative mt-10 mb-7">
            <button className="w-full h-[73px] rounded-[45px] font-[Open Sans] bg-[#3578FF] hover:bg-[#2a61cc] flex items-center justify-center text-white text-[32px] font-semibold transition-colors duration-300">
              프로필 편집
            </button>
          </div>
        </div>
      </div>

      {/* MenuBar에 선택된 메뉴 상태와 상태 변경 함수 전달 */}
      <MenuBar
        selectedMenu={selectedMenu}
        setSelectedMenu={setSelectedMenu}
        menus={["My Newzy", "BookMark", "Words"]}
      />

      {/* 선택된 메뉴에 따라 다른 컴포넌트 렌더링 */}
      {renderContent()}
      <div className="h-[500px] mx-20 my-20 text-[32px]">Content</div>
    </div>
  );
};

export default Profile;  // Profile을 기본으로 export
