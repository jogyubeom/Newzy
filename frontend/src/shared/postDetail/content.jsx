import React from 'react';

const Content = ({ htmlContent }) => {
  const formatHtmlContent = (html) => {
    const parser = new DOMParser();
    const doc = parser.parseFromString(html, 'text/html');

    // 모든 img 태그를 찾아서 Tailwind CSS 클래스를 추가
    const images = doc.querySelectorAll('img');
    images.forEach(img => {
      img.classList.add('max-w-full', 'h-auto'); // Tailwind CSS 클래스 추가
    });

    // 모든 요소의 스타일을 제거하고 img 제외
    const allElements = doc.body.querySelectorAll('*');
    allElements.forEach(element => {
      if (element.tagName.toLowerCase() !== 'img') {
        element.removeAttribute('style'); // 스타일 속성 제거
      }
    });

    return doc.body.innerHTML; // 수정된 HTML 반환
  };

  return (
    <div dangerouslySetInnerHTML={{ __html: formatHtmlContent(htmlContent) }} />
  );
};

export default Content;
