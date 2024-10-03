/** @type {import('tailwindcss').Config} */
export default {
  content: ["./index.html", "./src/**/*.{js,ts,jsx,tsx}"],
  theme: {
    extend: {
      fontFamily: {
        header: ["Poppins"],
        title: ["title"],
        text: ["text"],
        card: ["card"],
      },
    },
    animation: {
      "spin-y": "spinY 1s linear infinite", // 5초에 한 번 회전
    },
    keyframes: {
      spinY: {
        "0%": { transform: "rotateY(0deg)" },
        "100%": { transform: "rotateY(720deg)" },
      },
    },
  },
  plugins: [],
};
