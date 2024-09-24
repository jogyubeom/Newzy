/* eslint-disable react/prop-types */
import { FaUserCircle } from "react-icons/fa";
import { MdCancel } from "react-icons/md";
import { useState } from "react";
import FollowListItem from "./followListItem";

const FollowIndexModal = ({ isOpen, onClose }) => {

  // 임시 유저 더미데이터
  const user = {
    name: "정지훈",
    grade: 3,
    introduce: "안녕하세요. 잘 부탁드립니다! 하하",
    img: null,
    followers: 42,
    newzy: 7,
    followings: 25,
  };

  // 임시 팔로우 더미데이터
  const followList = [
    { name: "차은우", follow: false, img: "https://i.namu.wiki/i/ZnBMAAGJaiFKqDmASXCt-977Xuq6gLA-G8AsD4K1BKCVBEzrjISoW9QyfcSKPnacwuBpCGSSyBtCJv8E-UocNQ.webp"},
    { name: "손시우", follow: true, img: "https://image.xportsnews.com/contents/images/upload/article/2023/0628/mb_1687924647620553.jpg"},
    { name: "장하권", follow: false, img: "https://news.nateimg.co.kr/orgImg/ck/2022/04/21/kuk202204210051.680x.0.jpg"},
    { name: "김수환", follow: true, img: "https://i.namu.wiki/i/aUXvF4edykEEenR2kKMPLCVJD7Bq22Sl_ogTMQRrcL0qFTuvgisB-hmrxhbXviq7fZVEwMvLhY9tfKcysbK0qA.webp"},
    { name: "허수", follow: false, img: "https://i.namu.news/26/26fc4836ce73833bfcca8d7f09f248e97a1ab7901df987da03b591b16b4be9bf.jpg"},
    { name: "김건부", follow: true, img: "https://t1.daumcdn.net/news/202405/15/SPORTSSEOUL/20240515150316509ihrp.jpg"},
    { name: "김기인", follow: true, img: "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTeE7h4b1EiwLtg5LfzOhDzX_62gM74LQVNog&s"},
    { name: "차은우", follow: false, img: "https://i.namu.wiki/i/ZnBMAAGJaiFKqDmASXCt-977Xuq6gLA-G8AsD4K1BKCVBEzrjISoW9QyfcSKPnacwuBpCGSSyBtCJv8E-UocNQ.webp"},
    { name: "손시우", follow: true, img: "https://image.xportsnews.com/contents/images/upload/article/2023/0628/mb_1687924647620553.jpg"},
    { name: "장하권", follow: false, img: "https://news.nateimg.co.kr/orgImg/ck/2022/04/21/kuk202204210051.680x.0.jpg"},
    { name: "김수환", follow: true, img: "https://i.namu.wiki/i/aUXvF4edykEEenR2kKMPLCVJD7Bq22Sl_ogTMQRrcL0qFTuvgisB-hmrxhbXviq7fZVEwMvLhY9tfKcysbK0qA.webp"},
    { name: "허수", follow: false, img: "https://i.namu.news/26/26fc4836ce73833bfcca8d7f09f248e97a1ab7901df987da03b591b16b4be9bf.jpg"},
    { name: "김건부", follow: true, img: "https://t1.daumcdn.net/news/202405/15/SPORTSSEOUL/20240515150316509ihrp.jpg"},
    { name: "김기인", follow: true, img: "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTeE7h4b1EiwLtg5LfzOhDzX_62gM74LQVNog&s"},
  ]

  // 선택된 메뉴를 추적하는 상태
  const [selectedMenu, setSelectedMenu] = useState(0);
  const menus=[`팔로워 ${user.followers}명`, `팔로잉 ${user.followings}명`]

  // 선택된 메뉴에 따라 다른 컴포넌트 렌더링
  const renderContent = () => {
    switch (selectedMenu) {
      case 0:
        return (
          followList.map((item, index) => (
          <div 
            key={index}
            className="relative py-1 px-5 flex flex-col"
          >
            <FollowListItem name={item.name} img={item.img} follow={item.follow}/>
          </div>
          ))
        )
      case 1:
        return (
          followList.map((item, index) => (
          <div 
            key={index}
            className="relative py-1 px-5 flex flex-col"
          >
            <FollowListItem name={item.name} img={item.img} follow={item.follow}/>
          </div>
          ))
        )
      default:
        return null;
    }
  };

  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div className="w-[480px] h-[650px] transform rotate-[0.5deg] rounded-[24px] bg-white border-[3px] border-gray-400 px-6">
        <header className="w-full flex justify-between items-center mt-4">
          <div className="flex-shrink-0 text-[#26262C] whitespace-nowrap text-ellipsis font-[Open Sans] text-[36px] leading-[24px] font-semibold flex items-center justify-between text-justify">
            <button className="w-[40px] h-[40px] bg-gray-200 rounded-full">
              <FaUserCircle className="w-full h-full object-cover rounded-full text-blue-400" />
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
