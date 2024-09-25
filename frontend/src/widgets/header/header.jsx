import { BiBell } from "react-icons/bi";
import { FaUserCircle } from "react-icons/fa";

export const Header = () => {

  return (
    <header className="w-full h-[77px] flex justify-between items-center px-[48px]">
      <div className="flex-shrink-0 h-[72px] text-[#26262C] whitespace-nowrap text-ellipsis font-[Open Sans] text-[36px] leading-[24px] font-semibold flex items-center justify-between text-justify">
        Newzy
      </div>
      <div className="flex items-center space-x-4">
        <button className="flex-shrink-0 w-[76px] h-[37px] flex items-center justify-center rounded-full hover:bg-gray-100">
          <BiBell className="w-6 h-6 text-gray-800" />
        </button>

        <button className="w-[40px] h-[40px] bg-gray-200 rounded-full">
          <FaUserCircle className="w-full h-full object-cover rounded-full text-blue-400" />
        </button>
      </div>
    </header>
  );
};

