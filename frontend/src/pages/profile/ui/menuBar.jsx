/* eslint-disable react/prop-types */

const MenuBar = ({ menus, selectedMenu, setSelectedMenu }) => {
  return (
    <div className="flex items-center border-b-2 border-gray-300 font-semibold text-[28px] mx-10">
      {menus.map((menu, index) => (
        <div 
          key={index}
          className="relative cursor-pointer py-2 px-20"
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
  );
};

export default MenuBar