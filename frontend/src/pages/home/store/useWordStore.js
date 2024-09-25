import create from "zustand";

export const useWordStore = create((set) => ({
  words: [
    { text: "React", count: 50 },
    { text: "JavaScript", count: 30 },
    { text: "Tailwind", count: 20 },
    { text: "Mustand", count: 40 },
    { text: "SSAFY", count: 10 },
  ],
  addWord: (word, count) =>
    set((state) => ({
      words: [...state.words, { text: word, count }],
    })),
}));
