// shared/store/footerStore.js

import create from "zustand";

export const useFooterStore = create((set) => ({
  footerHeight: 0,
  setFooterHeight: (height) => set({ footerHeight: height }),
}));
