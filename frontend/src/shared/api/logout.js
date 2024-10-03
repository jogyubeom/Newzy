import useAuthStore from "../store/userStore";
import baseAxios from "../utils/baseAxios";
import { useNavigate } from "react-router-dom";

const logout = async () => {
  const clearToken = useAuthStore.getState().clearToken; // 토큰 삭제 함수 가져오기
  const nav = useNavigate();

  try {
    // 서버에 로그아웃 요청 (GET 요청)
    await baseAxios().get("/user/logout");

    // 로그아웃 성공 시 토큰 삭제
    clearToken();
    console.log("로그아웃 성공: 토큰이 삭제되었습니다.");

    // 필요시 로그아웃 후 리다이렉트 추가
    nav("/");
  } catch (error) {
    console.error("로그아웃 요청 중 오류 발생:", error);
  }
};

export default logout;
