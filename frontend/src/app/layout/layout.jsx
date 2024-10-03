import { Sidebar } from "widgets/sidebar";
import { Header } from "widgets/header";
import { Footer } from "widgets/footer";
import { Outlet } from "react-router-dom";

export const Layout = () => {
  return (
    <div className="flex min-h-screen bg-gray-300">
      {/* Sidebar 영역 */}
      <div className="w-36 h-screen fixed">
        <Sidebar />
      </div>

      {/* 메인 콘텐츠 영역 */}
      <div className="flex flex-col flex-grow ml-36 mr-12 mb-8 ">
        {/* Header를 fixed로 설정하되, 부모 요소의 너비를 기준으로 설정 */}
        <div className="sticky top-0 left-36 right-32 z-20 bg-gray-300">
          <div className="w-full mt-8 rounded-t-3xl bg-gray-100 border-b-2">
            <Header />
          </div>
        </div>

        {/* Outlet과 Footer 영역 */}
        <div className="rounded-b-3xl bg-gray-100">
          <Outlet />
          <Footer />
        </div>
      </div>
    </div>
  );
};
