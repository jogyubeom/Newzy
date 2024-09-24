import { Sidebar } from "widgets/sidebar";
import { Header } from "widgets/header";
import { Footer } from "widgets/footer";
import { Outlet } from "react-router-dom";

export const Layout = () => {
  return (
    <div className="flex min-h-screen bg-purple-200">
      <div className="w-36 h-screen fixed">
        <Sidebar />
      </div>
      <div className="flex flex-col flex-grow ml-36 mr-32 mb-8 ">
        <div className="fixed w-[calc(100%-272px)] z-40 bg-purple-200">
          <div className="mt-12 flex-grow rounded-t-3xl bg-gray-100 ">
            <Header />
          </div>
        </div>
        <div className="mt-32 rounded-b-3xl bg-gray-100">
          <Outlet />
          <Footer />
        </div>
      </div>
    </div>
  );
};
