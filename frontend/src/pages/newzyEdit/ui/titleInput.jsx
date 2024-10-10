import React from 'react';

const TitleInput = ({ title, onChange }) => {
  return (
    <input
      type="text"
      name="title"
      placeholder="제목을 작성해주세요..."
      value={title}
      onChange={onChange}
      className="w-full h-[100px] text-black text-[40px] leading-[100px] font-bold bg-transparent border-none outline-none placeholder:text-gray-400"
    />
  );
};

export default TitleInput;