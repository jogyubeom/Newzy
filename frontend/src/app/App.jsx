import Profile from "../pages/profile/Profile";
import { AppRouter } from "app/routers/appRouter";

function App() {
  return (
    <div className="App">
      {/* <header className="App-header">
        <p>사이드바입니다.</p>
      </header> */}
      <Profile />
      <header className="App-header">
        <AppRouter />
      </header>
    </div>
  );
}

export default App;
