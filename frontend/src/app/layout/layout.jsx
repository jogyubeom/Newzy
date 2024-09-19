import { Sidebar } from "widgets/sidebar";
import { Header } from "widgets/header";
import { Footer } from "widgets/footer";
import { Outlet } from "react-router-dom";

export const Layout = () => {
  return (
    <div className="wrapper">
      <div className="sidebar">
        <Sidebar />
      </div>
      <div className="main-content">
        <div className="header">
          <Header />
        </div>
        <div className="content">
          <Outlet />
        </div>
        <div className="footer">
          <Footer />
        </div>
      </div>
    </div>
  );
};
