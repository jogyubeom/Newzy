import {
  createBrowserRouter,
  createRoutesFromElements,
  RouterProvider,
  Route,
  Navigate,
} from "react-router-dom";

import { Layout } from "app/layout/layout";
import { Home } from "pages/home";
import { NewsList } from "pages/newsList";
import { NewzyList } from "pages/newzyList";
import { Profile } from "pages/profile";
import { NewsDetail } from "../../pages/newsDetail/newsDetail";
import { NewzyEdit } from "../../pages/newzyEdit/newzyEdit";
import { NewzyDetail } from "../../pages/newzyDetail/newzyDetail";
import { UserTest } from "pages/userTest";

export const AppRouter = () => {
  const routers = createRoutesFromElements(
    <Route path="/" element={<Layout />}>
      <Route index element={<Home />} />
      <Route path="news" element={<NewsList />} />
      <Route path="newzy">
        <Route index element={<NewzyList />} />
        <Route path="edit" element={<NewzyEdit />} />
        <Route path=":id" element={<NewzyDetail />} />
      </Route>
      <Route path="profile">
        <Route index element={<Navigate to="myNewzy" replace />} /> 
        <Route path="myNewzy" element={<Profile />}/>
        <Route path="bookMark" element={<UserTest />}/>
        <Route path="words" element={<Profile />}/>
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
