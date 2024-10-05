// entities/card/store/cardStore.js

import { create } from "zustand";

export const useCardStore = create((set) => ({
  summaryText: "",
  setSummaryText: (text) => set(() => ({ summaryText: text })),
}));
