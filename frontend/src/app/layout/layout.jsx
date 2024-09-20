import { Sidebar } from "widgets/sidebar";
import { Header } from "widgets/header";
import { Footer } from "widgets/footer";
import { Outlet } from "react-router-dom";

export const Layout = () => {
  return (
    <div className="flex min-h-screen">
      <div className="w-36 h-screen fixed">
        <Sidebar />
      </div>
      <div className="flex flex-col flex-grow py-8">
        <div className="bg-gray-200 rounded-3xl  ml-36 mr-32 shadow-lg">
          <div className="flex">
            <Header />
          </div>
          <div className="flex-grow overflow-auto">
            <Outlet />
            <Footer />
          </div>
        </div>
        {/* <div
          className="absolute bg-gray-100 ml-36 mr-32 w-full h-full
         rounded-3xl rotate-2"
        ></div> */}
      </div>
    </div>
  );
};
