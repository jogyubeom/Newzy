import {
  createBrowserRouter,
  createRoutesFromElements,
  RouterProvider,
  Route,
} from "react-router-dom";

import { Layout } from "app/layout/layout";
import { Home } from "pages/home";
import { NewsList } from "pages/newsList";
import { NewzyList } from "pages/newzyList";
import { Profile } from "pages/profile";

// const router = createBrowserRouter([
//   {
//     path: "/",
//     element: <Home />,
//   },
// ]);

// export const AppRouter = () => {
//   return <RouterProvider router={router} />;
// };

export const AppRouter = () => {
  const routers = createRoutesFromElements(
    <Route path="/" element={<Layout />}>
      <Route path="/" element={<Home />} />
      <Route path="news" element={<NewsList />} />
      <Route path="newzy" element={<NewzyList />} />
      <Route path="profile" element={<Profile />} />
    </Route>
  );

  const router = createBrowserRouter(routers, {});
  return (
    <div>
      <RouterProvider router={router} />
    </div>
  );
};
