// SocialLoginModal.js
import React from 'react';
import axios from 'axios';
import naver from 'shared/images/naver_logo.png';
import { FcGoogle } from "react-icons/fc";
import { RiKakaoTalkFill } from "react-icons/ri";

export const SocialLoginModal = ({ isOpen, onClose }) => {
  if (!isOpen) return null;

  const handleSocialLogin = async (platform) => {
    try {
      if (platform === 'Kakao') {
        console.log(`${platform} 로그인 시도`);
        const response = await axios.get('https://j11b305.p.ssafy.io/api/oauth2/authorization/kakao');
        console.log('카카오 로그인 결과:', response.data); // 결과 확인
      } else if (platform === 'Google') {
        console.log(`${platform} 로그인 시도`);
        const response = await axios.get('https://j11b305.p.ssafy.io/api/oauth2/authorization/google');
        console.log('구글 로그인 결과:', response.data); // 결과 확인
      } else if (platform === 'Naver') {
        console.log(`${platform} 로그인 시도`);
        const response = await axios.post('https://j11b305.p.ssafy.io/api/oauth2/authorization/kakao');
        console.log('네이버 로그인 결과:', response.data); // 결과 확인
      }
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
