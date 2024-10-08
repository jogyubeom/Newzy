// src/shared/store/followStore.js
import { create } from "zustand";
import baseAxios from "shared/utils/baseAxios";

export const useFollowStore = create((set) => ({
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

  // 팔로우 상태 업데이트
  updateFollowStatus: (nickname, isFollowing) => {
    set((state) => {
      if (isFollowing) {
        return {
          followings: [...state.followings, { toUserNickname: nickname }],
        };
      } else {
        return {
          followings: state.followings.filter(
            (user) => user.toUserNickname !== nickname
          ),
        };
      }
    });
  },
}));
