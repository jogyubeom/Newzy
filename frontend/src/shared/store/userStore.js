// userStore.js (zustand 스토어)
import { create } from "zustand";
import { persist } from "zustand/middleware";

const useAuthStore = create(
  persist(
    (set, get) => ({
      token: null, // 토큰 상태
      setToken: (newToken) => set({ token: newToken }), // 토큰 저장 함수
      clearToken: () => set({ token: null }), // 토큰 삭제 함수
      isLoggedIn: () => !!get().token, // 로그인 상태 확인 함수
    }),
    {
      name: "authStorage", // 로컬 스토리지에 저장될 키 이름
      getStorage: () => localStorage, // 기본적으로 localStorage에 저장
    }
  )
);

export default useAuthStore;
