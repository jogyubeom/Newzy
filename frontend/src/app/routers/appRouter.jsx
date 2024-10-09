import {
  createBrowserRouter,
  createRoutesFromElements,
  RouterProvider,
  Route,
  Navigate,
  useNavigate,
  Outlet,
} from "react-router-dom";
import { useState, useEffect } from "react";
import useAuthStore from "shared/store/userStore";
import { SocialLoginModal } from "widgets/login/socialLoginModal";

import { Layout } from "app/layout/layout";
import { Home } from "pages/home";
import { NewsList } from "pages/newsList";
import { NewzyList } from "pages/newzyList";
import { Profile } from "pages/profile";
import { AnotherProfile } from "pages/profile";
import { NewsDetail } from "pages/newsDetail/newsDetail";
import { NewzyEdit } from "pages/newzyEdit/newzyEdit";
import { NewzyDetail } from "pages/newzyDetail/newzyDetail";
import { UserTest } from "pages/userTest";

// PrivateRoute 컴포넌트
const PrivateRoute = () => {
  const navigate = useNavigate();
  const isLoggedIn = useAuthStore((state) => state.isLoggedIn());
  const [isLoginModalOpen, setIsLoginModalOpen] = useState(false);

  const openLoginModal = () => {
    setIsLoginModalOpen(true);
  };

  const closeLoginModal = () => {
    setIsLoginModalOpen(false);
  };

  useEffect(() => {
    if (!isLoggedIn) {
      // 로그인되어 있지 않으면 홈으로 보내고 로그인 모달을 띄움
      openLoginModal();
    }
  }, [isLoggedIn]);

  // 로그인 상태가 아니라면 아무것도 렌더링하지 않음
  if (!isLoggedIn) {
    return (
      <>
        <SocialLoginModal isOpen={isLoginModalOpen} onClose={closeLoginModal} />
      </>
    );
  }

  // 로그인된 상태면 자식 라우트를 렌더링
  return <Outlet />;
};

export const AppRouter = () => {
  const routers = createRoutesFromElements(
    <Route path="/" element={<Layout />}>
      <Route index element={<Home />} />
      <Route path="news">
        <Route index element={<NewsList />} />
        <Route path=":id" element={<NewsDetail />} />
        <Route path="economy" element={<NewsList category="economy" />} />
        <Route path="social" element={<NewsList category="social" />} />
        <Route path="global" element={<NewsList category="global" />} />
      </Route>
      <Route path="newzy">
        <Route index element={<NewzyList />} />
        {/* 새 글 작성 라우트 */}
        <Route path="edit" element={<NewzyEdit />} />
        {/* 수정 라우트 */}
        <Route path="edit/:newzyId" element={<NewzyEdit />} />
        <Route path=":id" element={<NewzyDetail />} />
      </Route>
      {/* PrivateRoute로 보호된 경로들 */}
      <Route element={<PrivateRoute />}>
        <Route path="profile">
          <Route index element={<Navigate to="myNewzy" replace />} />
          <Route path="myNewzy" element={<Profile />} />
          <Route path="bookMark" element={<Profile />} />
          <Route path="words" element={<Profile />} />
        </Route>
        <Route path="profile/:nickname" element={<AnotherProfile />} />
      </Route>
      <Route path="usertest" element={<UserTest />} />
    </Route>
  );

  const router = createBrowserRouter(routers, {});
  return (
    <div>
      <RouterProvider router={router} />
    </div>
  );
};
