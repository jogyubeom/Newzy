import { Link, useLocation } from "react-router-dom";
import { RiHomeLine as Home, RiHomeFill as HomeChecked } from "react-icons/ri";
import {
  RiFileCopy2Line as News,
  RiFileCopy2Fill as NewsChecked,
} from "react-icons/ri";
import {
  BiEditAlt as Newzy,
  BiSolidEditAlt as NewzyChecked,
} from "react-icons/bi";
import {
  BsPerson as Profile,
  BsPersonFill as ProfileChecked,
} from "react-icons/bs";

const menuItems = [
  { label: "Home", icon: <Home />, checkedIcon: <HomeChecked />, path: "/" },
  {
    label: "News",
    icon: <News />,
    checkedIcon: <NewsChecked />,
    path: "/news",
  },
  {
    label: "Newzy",
    icon: <Newzy />,
    checkedIcon: <NewzyChecked />,
    path: "/newzy",
  },
  {
    label: "Profile",
    icon: <Profile />,
    checkedIcon: <ProfileChecked />,
    path: "/profile",
  },
];

export const Sidebar = () => {
  const location = useLocation(); // 현재 위치

  return (
    <div className="flex flex-col h-full text-white justify-center items-center">
      {menuItems.map((item, index) => {
        const isActive = location.pathname === item.path;
        return (
          <Link
            key={index}
            to={item.path}
            className={"flex items-center justify-center mb-4 rounded-full"}
          >
            <div
              className={`flex items-center justify-center w-12 h-12 rounded-full cursor-pointer hover:bg-purple-600 ${
                isActive ? "bg-purple-600" : "bg-purple-400"
              } shadow-lg`}
            >
              {isActive ? item.checkedIcon : item.icon}
            </div>
          </Link>
        );
      })}
    </div>
  );
};
