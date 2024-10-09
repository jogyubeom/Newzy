// userStore.js (zustand 스토어)
import { create } from "zustand";
import { persist } from "zustand/middleware";

const useAuthStore = create(
  persist(
    (set, get) => ({
      token: null, // 토큰 상태
      userInfo: null, // 유저 정보 상태 추가
      followers: [],
      followings: [],

      setToken: (newToken) => set({ token: newToken }), // 토큰 저장 함수
      clearToken: () => set({ token: null }), // 토큰 삭제 함수
      setUserInfo: (newUserInfo) => set({ userInfo: newUserInfo }), // 유저 정보 저장 함수 추가
      clearUserInfo: () => set({ userInfo: null }), // 유저 정보 삭제 함수 추가
      isLoggedIn: () => !!get().token, // 로그인 상태 확인 함수

      // 팔로워 목록 가져오기
      fetchFollowers: async (nickname) => {
        set({ loading: true });
        try {
          const { data } = await baseAxios().get(
            `/user/followers-list/${nickname}`
          );
          set({ followers: data.followingList, loading: false });
        } catch (error) {
          console.error("팔로워 목록 가져오기 실패:", error);
          set({ loading: false });
        }
      },

      // 팔로잉 목록 가져오기
      fetchFollowings: async (nickname) => {
        set({ loading: true });
        try {
          const { data } = await baseAxios().get(
            `/user/followings-list/${nickname}`
          );
          set({ followings: data.followingList, loading: false });
        } catch (error) {
          console.error("팔로잉 목록 가져오기 실패:", error);
          set({ loading: false });
        }
      },

      // 팔로워/팔로잉 상태를 업데이트하는 함수
      updateFollowStatus: (name, isFollowing) => {
        set((state) => {
          // 팔로우 취소 (언팔로우)
          if (!isFollowing) {
            return {
              followings: state.followings.filter(
                (following) => following.toUserNickname !== name
              ),
              followers: state.followers.filter(
                (follower) => follower.fromUserNickname !== name
              ),
            };
          }
          // 팔로우 추가
          return {
            followings: [...state.followings, { toUserNickname: name }],
            followers: [...state.followers, { fromUserNickname: name }],
          };
        });
      },

      // 팔로우 여부 확인 함수
      isFollowing: (nickname) => {
        return get().followings.some(
          (following) => following.toUserNickname === nickname
        );
      },
    }),
    {
      name: "authStorage", // 로컬 스토리지에 저장될 키 이름
      getStorage: () => localStorage, // 기본적으로 localStorage에 저장
    }
  )
);

export default useAuthStore;
