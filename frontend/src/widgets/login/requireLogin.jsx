import useAuthStore from "shared/store/userStore";
import { useState } from "react";

export const RequireAuth = ({ children }) => {
  const isLoggedIn = useAuthStore((state) => state.isLoggedIn());
  const [showMessage, setShowMessage] = useState(false);

  if (!isLoggedIn) {
    // 로그아웃 상태일 때 경고 메시지 표시
    if (!showMessage) {
      setShowMessage(true);
    }
  } else {
    // 로그인 상태일 경우 메시지 숨김
    if (showMessage) {
      setShowMessage(false);
    }
  }

  return (
    <div>
      {showMessage && (
        <p style={{ color: "red", fontWeight: "bold" }}>로그인이 필요합니다.</p>
      )}
      {children}
    </div>
  );
};
