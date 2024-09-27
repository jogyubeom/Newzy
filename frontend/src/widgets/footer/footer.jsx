export const Footer = () => {
  return (
    <footer className="flex m-4 border-solid border-2 border-gray-500 p-4">
      <div className="container mx-auto flex flex-col md:flex-row justify-between space-y-8 md:space-y-0">
        {/* 회사 정보 */}
        <div>
          <h3 className="text-2xl font-bold">
            <a href="/" className="hover:text-gray-400">
              Newzy
            </a>
          </h3>
          <p>© 2024 Newzy. All rights reserved.</p>
          <p>삼성 청년 SW 아카데미</p>
          <p>대전광역시 유성구 덕명동 124, 삼성화재 유성연수원</p>
        </div>

        {/* 뉴스 빠른 링크 */}
        <div>
          <h3 className="text-lg font-bold">
            <a href="/news" className="hover:text-gray-400">
              News
            </a>
          </h3>
          <ul className="space-y-2">
            <li>
              <a href="/news" className="hover:text-gray-400">
                경제
              </a>
            </li>
            <li>
              <a href="/news" className="hover:text-gray-400">
                사회
              </a>
            </li>
            <li>
              <a href="/news" className="hover:text-gray-400">
                세계
              </a>
            </li>
          </ul>
        </div>

        {/* 뉴지 빠른 링크 */}
        <div>
          <h3 className="text-lg font-bold">
            <a href="/newzy" className="hover:text-gray-400">
              Newzy by Newporter
            </a>
          </h3>
          <ul className="space-y-2">
            <li>
              <a href="/newzy" className="hover:text-gray-400">
                구독 뉴지
              </a>
            </li>
            <li>
              <a href="/newzy" className="hover:text-gray-400">
                뉴지 작성하기
              </a>
            </li>
          </ul>
        </div>

        {/* 뉴스 빠른 링크 */}
        <div>
          <h3 className="text-lg font-bold">
            <a href="/profile" className="hover:text-gray-400">
              Profile
            </a>
          </h3>
          <ul className="space-y-2">
            <li>
              <a href="/profile" className="hover:text-gray-400">
                나의 뉴지
              </a>
            </li>
            <li>
              <a href="/profile" className="hover:text-gray-400">
                북마크 뉴스
              </a>
            </li>
            <li>
              <a href="/profile" className="hover:text-gray-400">
                단어장
              </a>
            </li>
          </ul>
        </div>
      </div>
    </footer>
  );
};
