// src/shared/store/followStore.js
import { create } from "zustand";
import baseAxios from "shared/utils/baseAxios";

export const useFollowStore = create((set, get) => ({
  followers: [],
  followings: [],
  loading: false,

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
          // followings: state.followings.filter(
          //   (following) => following.toUserNickname !== name
          // ),
          followers: state.followers.filter(
            (follower) => follower.fromUserNickname !== name
          ),
        };
      }
      // 팔로우 추가
      return {
        // followings: [...state.followings, { toUserNickname: name }],
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
}));
