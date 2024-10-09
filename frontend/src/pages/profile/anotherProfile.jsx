import React, { useEffect, useState } from "react";
import { useNavigate, useLocation, useParams } from "react-router-dom";
import { getGrade } from "shared/getGrade";
import MenuBar from "pages/profile/ui/menuBar";
import Newzy from "./ui/newzy";
import FollowIndexModal from "pages/profile/ui/followIndexModal";

import userProfile from "shared/images/user.png";
import baseAxios from "shared/utils/baseAxios";
import { useFollowStore } from "./store/useFollowStore";
import useAuthStore from "../../shared/store/userStore";

const getZoomLevel = () => {
  return window.devicePixelRatio * 100;
};

const gradeDescriptions = {
  1: (
    <>
      <p className="font-semibold">Level 1 초보 뉴포터</p>
      시작하는 단계입니다!
    </>
  ),
  2: (
    <>
      <p className="font-semibold">Level 2 중급 뉴포터</p>
      꽤 실력이 있어요!
    </>
  ),
  3: (
    <>
      <p className="font-semibold">Level 3 고급 뉴포터</p>
      실력이 뛰어나요!
    </>
  ),
  4: (
    <>
      <p className="font-semibold">Level 4 마스터 뉴포터</p>
      당신은 프로입니다!
    </>
  ),
};

export const AnotherProfile = () => {
  const navigate = useNavigate(); // useNavigate 훅 사용
  const location = useLocation(); // 현재 경로를 가져오기 위해 useLocation 훅 사용
  const { nickname } = useParams(); // 경로에서 닉네임을 가져옴

  const [selectedMenu, setSelectedMenu] = useState(0);
  const [isModalOpen, setModalOpen] = useState(false); // 모달 상태 관리
  const [isTooltipVisible, setIsTooltipVisible] = useState(false); // 말풍선 상태 관리

  const [user, setUser] = useState(null); // 유저 데이터 상태
  const { followers, followings, fetchFollowers, fetchFollowings, isFollowing, updateFollowStatus } = useFollowStore();
  const [isUserFollowing, setIsUserFollowing] = useState(false);

  const { userInfo: loggedInUser, fetchFollowers: loggedInUserFollowers, fetchFollowings: loggedInUserFollowings, followings: loggedInUserFollowingsIndex } = useAuthStore();  // ✅ 로그인된 사용자 정보

  const [profileData, setProfileData] = useState({
    name: '',
    introduce: '',
    img: null,
    birth: '',
  });

  const [paddingX, setPaddingX] = useState(32); // 기본 패딩

  // 패딩 업데이트 로직을 추가합니다.
  useEffect(() => {
    const updatePaddingBasedOnZoom = () => {
      const zoomLevel = getZoomLevel();
  
      if (zoomLevel <= 90) {
        setPaddingX(300); // 줌 레벨이 80% 이하일 때 80px
      } else if (zoomLevel <= 100) {
        setPaddingX(150); // 줌 레벨이 80% ~ 90%일 때 56px
      } else if (zoomLevel <= 120) {
        setPaddingX(100); // 줌 레벨이 90% ~ 100%일 때 32px
      } else {
        setPaddingX(32); // 줌 레벨이 100% 이상일 때 16px
      }
    };

    // 초기 실행
    updatePaddingBasedOnZoom();

    // 매 500ms마다 줌 레벨을 확인하여 패딩을 업데이트
    const intervalId = setInterval(updatePaddingBasedOnZoom, 500);

    // cleanup 함수로 interval 제거
    return () => {
      clearInterval(intervalId);
    };
  }, []);

  // 다른 유저 정보 불러오기
  useEffect(() => {
    const fetchUserData = async () => {
      try {
        const response = await baseAxios().get(`/user/profile/${nickname}`); // 닉네임을 URL에 포함해 요청

        const userData = response.data;

        // 유저 데이터가 로드된 후 profileData 초기화
        setUser(userData);
        setProfileData({
          name: userData.nickname,
          introduce: userData.info || "자기소개가 없습니다.",
          img: userData.profile,
          birth: userData.birth || "",
        });

      } catch (error) {
        console.error("유저 정보를 불러오는 중 오류 발생:", error);
      }
    };

    fetchUserData(); 
  }, [nickname]); // 닉네임이 변경될 때마다 다시 유저 정보를 불러옴


  // 팔로워/팔로잉 목록 가져오기 및 팔로우 여부 확인
  useEffect(() => {
    const checkIfFollowing = async () => {
      await fetchFollowings(nickname); // 타 유저 닉네임으로 팔로잉 목록을 가져옴
      setIsUserFollowing(isFollowing(nickname)); // 해당 유저를 팔로우하는지 여부 확인
    };

    checkIfFollowing();
    fetchFollowers(nickname);  // 팔로워 목록 불러오기
  }, [nickname, fetchFollowings, isFollowing]);

  // 팔로우/언팔로우 버튼 클릭 시
  const toggleFollow = async () => {
    try {
      if (isUserFollowing) {
        await baseAxios().delete(`/user/${nickname}/follower`);
      } else {
        await baseAxios().post(`/user/${nickname}/follower`);
      }
      updateFollowStatus(nickname, !isUserFollowing);
      setIsUserFollowing(!isUserFollowing); // 로컬 상태 업데이트
    } catch (error) {
      console.error("팔로우/언팔로우 처리 중 에러 발생:", error);
    }
  };


  // 현재 경로에 따라 메뉴를 선택 상태로 설정
  useEffect(() => {
    switch (location.pathname) {
      case "/profile/myNewzy":
        setSelectedMenu(0);
        break;
      case "/profile/bookMark":
        setSelectedMenu(1);
        break;
      case "/profile/words":
        setSelectedMenu(2);
        break;
      default:
        setSelectedMenu(0);
        break;
    }
  }, [location]);

  // exp 값을 기준으로 grade를 구하는 함수
  const getGradeByExp = (exp) => {
    if (exp >= 10000) return 4;
    if (exp >= 5000) return 3;
    if (exp >= 1000) return 2;
    return 1;
  };

  // 유저 정보가 로드되지 않았을 때 로딩 메시지 표시
  if (!user) {
    return <div>해당 유저가 존재하지 않습니다.</div>;
  }

  // 각 grade의 최대 경험치값
  const maxExpByGrade = {
    1: 1000,    // 0~999: Level 1
    2: 5000,   // 1000~4999: Level 2
    3: 10000, // 5000~9999: Level 3
    4: user.exp, // 10000 이상: Level 4
  };

  // 유저의 grade 및 비중 계산
  const userGrade = getGradeByExp(user.exp);
  const maxExp = maxExpByGrade[userGrade];
  const expRatio = (user.exp / maxExp) * 100; // 현재 경험치 비중 계산 (퍼센트 값)

  // SVG에서 사용할 경험치바 계산
  const radius = 135;
  const circumference = 2 * Math.PI * radius;
  const progress = (expRatio / 100) * circumference; // 현재 경험치에 해당하는 원형 길이

  // 모달 열기 및 닫기 함수
  const openModal = () => async () => {
    if (nicknameData) {
      // ✅ 로그인한 현재 사용자의 팔로워/팔로잉 목록도 다시 불러오기
      await loggedInUserFollowers(loggedInUser.nickname);  // 팔로워 목록 다시 불러오기
      await loggedInUserFollowings(loggedInUser.nickname); // 팔로잉 목록 다시 불러오기
    }
    setModalOpen(true);
  };
  const closeModal = () => setModalOpen(false);

  // 메뉴 클릭 시 경로를 변경
  const handleMenuChange = (menuIndex) => {
    setSelectedMenu(menuIndex);
    switch (menuIndex) {
      case 0:
        navigate(`/profile/:${nickname}`); 
        break;
      default:
        break;
    }
  };

  // 선택된 메뉴에 따라 다른 컴포넌트 렌더링
  const renderContent = () => {
    switch (selectedMenu) {
      case 0:
        return <Newzy nickname={nickname} />;
      default:
        return null;
    }
  };

  return (
    <div className="overflow-x-auto bg-[#FFFFFF]">
      <div id="target" className="h-[409px] bg-[#132956] relative flex mb-12" style={{ paddingLeft: `${paddingX}px`, paddingRight: `${paddingX}px` }}>
        <div className="relative">
          {/* 경험치량 표시 */}
          <div
            className="absolute top-[10px] left-[150px] w-full flex justify-center items-center text-yellow-500 text-base font-extrabold tracking-wide whitespace-nowrap"
            style={{ textShadow: "2px 2px 4px rgba(0, 0, 0, 0.6)" }}
            >
            {user.exp > 10000 ? '10000 / 10000' : `${user.exp} / ${maxExp}`}
          </div>
          {/* SVG로 경험치 바 추가 */}
          <svg width="310" height="310" className="absolute top-[35px] left-[0px]" style={{ transform: "rotate(-90deg)" }}>
            <circle
              cx="150"
              cy="150"
              r={radius}
              stroke="gray"
              strokeWidth="24"
              fill="none"
              className="opacity-20"
            />
            <circle
              cx="150"
              cy="150"
              r={radius}
              stroke="gold"
              strokeWidth="24"
              fill="none"
              strokeDasharray={circumference}
              strokeDashoffset={circumference - progress}
              strokeLinecap="round"
              className="transition-all duration-500"
            />
          </svg>

          {/* 프로필 이미지 */}
          <div className={"absolute top-[70px] left-[25px] w-[250px] h-[250px] rounded-full flex items-center justify-center"}>
            <img
              src={profileData.img || userProfile}
              className={"w-full h-full object-cover rounded-full"}
              alt="프로필 이미지"
            />
          </div>

          <div className="absolute top-[285px] left-[218px] w-[100px] h-[100px] bg-white bg-opacity-20 rounded-full flex items-center justify-center">
          <img
              src={getGrade(userGrade)}
              className="w-[60px] h-[60px] object-cover cursor-pointer"
              onMouseEnter={() => setIsTooltipVisible(true)}
              onMouseLeave={() => setIsTooltipVisible(false)}
            />
            {isTooltipVisible && (
              <div
                className="absolute top-[110%] transform -translate-x-1/2 bg-gray-700 text-white text-base rounded-lg p-2 opacity-0 animate-tooltip shadow-md"
                style={{ whiteSpace: "nowrap" }}
              >
                {gradeDescriptions[userGrade]}
                <span className="absolute top-[-5px] left-1/2 transform -translate-x-1/2 w-0 h-0 border-l-8 border-r-8 border-b-8 border-transparent border-b-gray-700"></span>
              </div>
            )}
          </div>
        </div>

        <div className="ml-[380px]">
          <div className="h-[100px] mt-[80px] text-white font-[Open Sans] text-[46px] leading-[24px] font-semibold flex items-center">
            <p className="p-2">{profileData.name}</p>
          </div>

          <div className="w-[250px] h-[200px] text-white font-[Open Sans] text-[24px] leading-[36px] font-semibold flex items-center text-left break-words whitespace-pre-wrap">
            <p className="p-2">{profileData.introduce}</p>
          </div>
        </div>

        {/* Followers, Followings, Newzy 요소 */}
        <div className="flex flex-col items-center ml-auto mt-auto">
          <div className="flex gap-9">
            <div className="flex flex-col items-center">
              <div className="w-[123px] h-[103px] text-white font-[Poppins] text-[36px] leading-[24px] font-semibold flex items-center">
                Newzy
              </div>
              <div className="w-[100px] h-[60px] text-white font-[Poppins] text-[36px] leading-[24px] font-semibold flex items-center justify-center text-center">
                {user.newzyCnt}
              </div>
            </div>
            <div className="flex flex-col items-center cursor-pointer" onClick={openModal}>
              <div className="w-[166px] h-[103px] text-white font-[Poppins] text-[36px] font-semibold flex items-center">
                Followers
              </div>
              <div className="w-[100px] h-[60px] text-white font-[Poppins] text-[36px] leading-[24px] font-semibold flex items-center justify-center text-center">
                {followers.length}
              </div>
            </div>
            <div className="flex flex-col items-center cursor-pointer" onClick={openModal}>
              <div className="w-[188px] h-[103px] text-white font-[Poppins] text-[36px] leading-[24px] font-semibold flex items-center">
                Followings
              </div>
              <div className="w-[100px] h-[60px] text-white font-[Poppins] text-[36px] leading-[24px] font-semibold flex items-center justify-center text-center">
              {followings.length}
              </div>
            </div>
          </div>
          <div className="w-[293px] h-[103px] relative mt-10 mb-7">
            <button
              onClick={toggleFollow}
              className={`w-full h-[73px] px-4 py-2 rounded-[10px] flex items-center justify-center text-white font-[Open Sans] text-[32px] font-semibold transition-colors duration-300
                ${isUserFollowing ? 'bg-red-600 hover:bg-red-700' : 'bg-[#3578FF] hover:bg-[#2a61cc]'}`}
            >
              {isFollowing ? 'Unfollow' : 'Follow'}
            </button>
          </div>
        </div>
      </div>

      <MenuBar
        selectedMenu={selectedMenu}
        setSelectedMenu={handleMenuChange}
        menus={["Newzy"]}
      />

      {renderContent()}

      <FollowIndexModal isOpen={isModalOpen} onClose={closeModal} userInfo={user} />
    </div>
  );
};
