import React, { useState } from "react";
import { getGrade } from "shared/getGrade";
import { MdAdd, MdDelete } from "react-icons/md";
import MenuBar from "widgets/profilePage/menuBar";
import MyNewzy from "widgets/profilePage/myNewzy";
import BookMark from "widgets/profilePage/bookMark";
import Words from "widgets/profilePage/words";
import FollowIndexModal from "widgets/profilePage/followIndexModal";
import CardListModal from "widgets/profilePage/cardListModal";

import userProfile from "shared/images/user.png";
import cards from "shared/images/cards.svg";

// 임시 유저 더미데이터
const user = {
  name: "정지훈",
  grade: 1,
  introduce: `안녕하세요\n잘 부탁드립니다!!`,
  img: null,
  followers: 42,
  newzy: 7,
  followings: 25,
  birth: "2001-03-05",
};

export const Profile = () => {
  const [selectedMenu, setSelectedMenu] = useState(0);
  const [isModalOpen, setModalOpen] = useState(false); // 모달 상태 관리
  const [isCardListModalOpen, setIsCardListModalOpen] = useState(false); // CardListModa

  // 편집 모드 관리
  const [isEditing, setIsEditing] = useState(false);
  const [profileData, setProfileData] = useState({
    name: user.name,
    introduce: user.introduce,
    img: user.img,
    birth: user.birth,
  });

  // 글자수 제한
  const maxIntroduceLength = 30;

  // 모달 열기 및 닫기 함수
  const openModal = () => setModalOpen(true);
  const closeModal = () => setModalOpen(false);

  // CardListModal 열기 및 닫기 함수
  const openCardListModal = () => setIsCardListModalOpen(true);
  const closeCardListModal = () => setIsCardListModalOpen(false);

  // 저장 버튼 클릭 시 변경 사항 저장
  const handleSave = () => {
    console.log("저장된 데이터:", profileData);
    setIsEditing(false);
  };

  // 이미지 변경 핸들러
  const handleImageChange = (e) => {
    const file = e.target.files[0];
    if (file) {
      setProfileData({ ...profileData, img: URL.createObjectURL(file) });
    }
  };

  // 이미지 제거 핸들러
  const handleImageRemove = () => {
    setProfileData({ ...profileData, img: null });
  };

  // 선택된 메뉴에 따라 다른 컴포넌트 렌더링
  const renderContent = () => {
    switch (selectedMenu) {
      case 0:
        return <MyNewzy />;
      case 1:
        return <BookMark />;
      case 2:
        return <Words />;
      default:
        return null;
    }
  };

  return (
    <div className="overflow-x-auto bg-[#FFFFFF]">
      <div className="h-[409px] bg-[#132956] relative flex px-8 mb-12">
        <div className="relative">
          <div className={"absolute top-[70px] left-[0px] w-[270px] h-[270px] rounded-full border-[24px] border-yellow-500 flex items-center justify-center"}>
            <img
              src={profileData.img || userProfile}
              className={`w-full h-full object-cover rounded-full ${isEditing ? 'opacity-60' : ''}`}
              alt="프로필 이미지"
            />
            {isEditing && (
              <div className="absolute top-[105px] right-111 flex gap-12">
                <label htmlFor="profile-upload" className="cursor-pointer">
                  <MdAdd className="text-white bg-blue-500 rounded-full w-7 h-7" />
                  <input
                    id="profile-upload"
                    type="file"
                    accept="image/*"
                    className="hidden"
                    onChange={handleImageChange}
                  />
                </label>
                <MdDelete
                  className="text-white bg-red-500 rounded-full w-7 h-7 cursor-pointer"
                  onClick={handleImageRemove}
                />
              </div>
            )}
          </div>

          <div className="absolute top-[263px] left-[196px] w-[100px] h-[100px] bg-white bg-opacity-20 rounded-full flex items-center justify-center">
            <img
              src={getGrade(user.grade)}
              className="w-[60px] h-[60px] object-cover"
            />
          </div>

          {isEditing && (
            <input
              type="date"
              className="absolute top-[360px] left-[10px] w-[140px] px-2 py-1 rounded-md bg-gray-800 text-white"
              value={profileData.birth}
              onChange={(e) => setProfileData({ ...profileData, birth: e.target.value })}
            />
          )}
        </div>

        <div className="ml-[320px]">
          <div className="h-[100px] mt-[80px] text-white font-[Open Sans] text-[46px] leading-[24px] font-semibold flex items-center">
            {isEditing ? (
              <input
                type="text"
                value={profileData.name}
                onChange={(e) => setProfileData({ ...profileData, name: e.target.value })}
                className="outline-none w-full p-2 mr-5 text-white bg-gray-800 opacity-100"
              />
            ) : (
              <p className="p-2">{profileData.name}</p>
            )}
          </div>

          <div className="w-[250px] h-[200px] text-white font-[Open Sans] text-[24px] leading-[36px] font-semibold flex items-center text-left break-words whitespace-pre-wrap">
            {isEditing ? (
              <textarea
                value={profileData.introduce}
                onChange={(e) => {
                  const lines = e.target.value.split('\n');
                  // 최대 5줄까지만 입력 가능하도록 제한
                  if (lines.length <= 5) {
                    setProfileData({ ...profileData, introduce: e.target.value });
                  }
                }}
                rows={5} // 기본 5줄
                maxLength={maxIntroduceLength}
                className="outline-none w-full h-[200px] p-2 text-white bg-gray-800 opacity-100"
                placeholder={`자기소개 문구를\n작성해주세요!`}
              />
            ) : (
              <p className="p-2">{profileData.introduce}</p>
            )}
          </div>
          {isEditing && (
            <div className="font-semibold text-gray-200 text-left">
              {profileData.introduce.length}/{maxIntroduceLength}
            </div>
          )}
        </div>

        {/* Followers, Followings, Newzy 요소 */}
        <div className="flex flex-col items-center ml-auto mt-auto">
          <div className="flex gap-16">
            <div className="flex flex-col items-center">
              <div className="w-[123px] h-[103px] text-white font-[Poppins] text-[36px] leading-[24px] font-semibold flex items-center">
                Newzy
              </div>
              <div className="w-[100px] h-[60px] text-white font-[Poppins] text-[36px] leading-[24px] font-semibold flex items-center justify-center text-center">
                {user.newzy}
              </div>
            </div>
            <div className="flex flex-col items-center cursor-pointer" onClick={openModal}>
              <div className="w-[166px] h-[103px] text-white font-[Poppins] text-[36px] font-semibold flex items-center">
                Followers
              </div>
              <div className="w-[100px] h-[60px] text-white font-[Poppins] text-[36px] leading-[24px] font-semibold flex items-center justify-center text-center">
                {user.followers}
              </div>
            </div>
            <div className="flex flex-col items-center cursor-pointer" onClick={openModal}>
              <div className="w-[188px] h-[103px] text-white font-[Poppins] text-[36px] leading-[24px] font-semibold flex items-center">
                Followings
              </div>
              <div className="w-[100px] h-[60px] text-white font-[Poppins] text-[36px] leading-[24px] font-semibold flex items-center justify-center text-center">
                {user.followings}
              </div>
            </div>
          </div>

          {/* 프로필 편집 및 저장 버튼 */}
          {isEditing ? (
            <div className="w-[293px] h-[103px] relative mt-10 mb-7">
              <button
                className="w-full h-[73px] rounded-[10px] font-[Open Sans] bg-[#3578FF] hover:bg-[#2a61cc] flex items-center justify-center text-white text-[32px] font-semibold transition-colors duration-300"
                onClick={handleSave}
              >
                저장하기
              </button>
            </div>
          ) : (
            <div className="w-[293px] h-[103px] relative mt-10 mb-7">
              <button
                className="w-full h-[73px] rounded-[10px] font-[Open Sans] bg-[#3578FF] hover:bg-[#2a61cc] flex items-center justify-center text-white text-[32px] font-semibold transition-colors duration-300"
                onClick={() => setIsEditing(true)}
              >
                프로필 편집
              </button>
            </div>
          )}
        </div>
      </div>

      <MenuBar
        selectedMenu={selectedMenu}
        setSelectedMenu={setSelectedMenu}
        menus={["My Newzy", "BookMark", "Words"]}
      />

      {renderContent()}

      <FollowIndexModal isOpen={isModalOpen} onClose={closeModal} />

      {/* 하단에 고정된 버튼 추가 */}
      <button
        className="fixed bottom-5 left-0.5 bg-transparent flex items-center justify-center cursor-pointer"
        style={{ zIndex: 1000 }}
        onClick={openCardListModal} // 버튼 클릭 시 CardListModal 열기
      >
        <img src={cards} alt="카드 버튼" className="w-full h-full object-cover" />
      </button>
      {/* CardListModal 렌더링 */}
      {isCardListModalOpen && <CardListModal onClose={closeCardListModal} cardNum={11}/>}
    </div>
  );
};
