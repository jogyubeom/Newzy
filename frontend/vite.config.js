import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";
import svgr from "vite-plugin-svgr";

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    react(),
    svgr({
      include: "**/*.svg?react",
      // svg파일을 React 컴포넌트로 사용할 때 ?react를 붙임
      // 예시) import { ReactComponent as Icon } from './icon.svg?react';

      // svg파일을 이미지 URL로 사용할 때
      // 예시) import iconUrl from './icon.svg';
    }),
  ],
  resolve: {
    alias: {
      app: "/src/app",
      entities: "/src/entities",
      features: "/src/features",
      pages: "/src/pages",
      shared: "/src/shared",
      widgets: "/src/widgets",
    },
  },
});
