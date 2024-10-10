/* eslint-disable react/prop-types */
import React, { useState } from 'react';
import axios from 'axios';
import naver from 'shared/images/naver_logo.png';
import { FcGoogle } from "react-icons/fc";
import { RiKakaoTalkFill } from "react-icons/ri";

export const SocialLoginModal = ({ isOpen, onClose }) => {

  const [forceLogin, setForceLogin] = useState(false); // "다른 계정으로 로그인하기" 상태를 관리하는 state

  if (!isOpen) return null;

  const handleSocialLogin = async (platform) => {
    try {
      let response;
      
      if (platform === 'Kakao') {
        console.log(`${platform} 로그인 시도`);
        // axios를 사용해 백엔드에서 로그인 URL 요청
        response = await axios.get('https://j11b305.p.ssafy.io/api/oauth2/kakao/authorize');

      } else if (platform === 'Google') {
        console.log(`${platform} 로그인 시도`);
        response = await axios.get('https://j11b305.p.ssafy.io/api/oauth2/google/authorize');

      } else if (platform === 'Naver') {
        console.log(`${platform} 로그인 시도`);
        alert("네이버 로그인은 향후 추가될 예정입니다.");
        return;
      }
      
      let url = response.data;
      // forceLogin이 true일 경우, URL에 &prompt=login을 추가
      if (forceLogin) {
        url += '&prompt=login';
      }

      // 서버에서 받은 로그인 URL로 리다이렉트
      window.location.href = url
      
    } catch (error) {
      console.error(`${platform} 로그인 오류:`, error);
    }
  };

  return (
    <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50 z-50">
      <div className="bg-white p-8 rounded-lg w-[400px] shadow-lg flex flex-col items-center justify-center">
        <h2 className="text-center text-2xl font-semibold mb-10 text-black">로그인하기</h2>
        
        <div className="flex flex-col w-full space-y-4">

          {/* Kakao 로그인 버튼 */}
          <button
            className="flex items-center justify-center w-full px-4 py-2 bg-[#FEE500] border border-[#FEE500] rounded hover:bg-yellow-400 transition"
            onClick={() => handleSocialLogin('Kakao')}
          >
            <RiKakaoTalkFill className="w-8 h-8 mr-3 text-black" />
            <span className="text-black font-semibold">Kakao 로그인</span>
          </button>

          {/* Google 로그인 버튼 */}
          <button
            className="flex items-center justify-center w-full px-4 py-2 bg-white border-2 rounded hover:bg-gray-100 transition"
            onClick={() => handleSocialLogin('Google')}
          >
            <FcGoogle className="w-8 h-8 mr-3" />
            <span className="text-gray-700 font-semibold">Google 로그인</span>
          </button>

          {/* Naver 로그인 버튼 */}
          <button
            className="flex items-center justify-center w-full px-4 py-2 bg-[#03C75A] border border-[#03C75A] rounded hover:bg-green-600 transition"
            onClick={() => handleSocialLogin('Naver')}
          >
            <img src={naver} alt="Naver Login" className="w-8 h-8 mr-3" />
            <span className="text-white font-semibold">Naver 로그인</span>
          </button>

        </div>

        {/* "다른 계정으로 로그인하기" 토글 버튼 */}
        <div className="flex items-center justify-center w-full mt-6">
          <span className="text-blue-700 font-semibold mr-3">다른 계정으로 로그인하기</span>
          <label className="relative inline-flex items-center cursor-pointer">
            <input
              type="checkbox"
              className="sr-only peer" // peer 클래스 추가
              checked={forceLogin}
              onChange={() => setForceLogin(!forceLogin)}
            />
            {/* 배경 색상 */}
            <div className="w-11 h-6 bg-gray-200 rounded-full peer-checked:bg-blue-500 transition-colors"></div>
            {/* 토글 버튼 */}
            <div className="absolute right-0.5 left-0.5 top-0.5 w-5 h-5 bg-white border border-gray-300 rounded-full shadow transition-transform transform peer-checked:translate-x-5"></div>
          </label>
        </div>

        {/* 닫기 버튼 */}
        <button
          className="mt-14 w-full py-2 border border-red-500 text-red-500 font-semibold rounded hover:bg-red-500 hover:text-white transition"
          onClick={onClose}
        >
          닫기
        </button>
      </div>
    </div>
  );
};
