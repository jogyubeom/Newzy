import React, { useMemo, useRef } from 'react';
import ReactQuill from 'react-quill-new';
import baseAxios from "shared/utils/baseAxios";
import Quill from 'quill';
import ImageResize from 'quill-image-resize';
import 'react-quill-new/dist/quill.snow.css';

// ImageResize 모듈을 Quill에 등록
Quill.register('modules/ImageResize', ImageResize);

const ContentEditor = ({ content, setContent }) => {
  const quillRef = useRef(null); // ref 생성

  // image를 서버로 전달하는 과정
  const imageHandler = () => {
    // input type=file DOM을 만든다.
    const input = document.createElement("input");
    input.setAttribute("type", "file");
    input.setAttribute("accept", "image/*");
    input.click(); // toolbar 이미지를 누르게 되면 이 부분이 실행된다.
    
    // 이미지를 선택하게 될 시
    input.onchange = async () => {
      // 이미지 선택에 따른 조건을 다시 한번 하게 된다.
      const file = input.files ? input.files[0] : null;
      if (!file) return; // 선택을 안하면 취소버튼처럼 수행하게 된다.

      // 서버에서 FormData 형식으로 받기 때문에 이에 맞는 데이터 형식으로 만들어준다.
      const formData = new FormData();
      formData.append("image", file); // 서버의 key 값에 맞게 변경해야 합니다.

      // 에디터 정보를 가져온다.
      let quillObj = quillRef.current?.getEditor();
      if (!quillObj) return; // 에디터가 없는 경우 함수 종료

      // 에디터 커서 위치를 가져온다.
      const range = quillObj.getSelection();
      if (!range) return; // 커서가 없다면 종료

      try {
        // 서버에 데이터를 보내고 URL을 imgUrl로 받는다.
        const res = await baseAxios().post("/newzy/upload-image", formData, {
          headers: {
            "Content-Type": "multipart/form-data",
          },
        });

        const imgUrl = res.data.url; // 서버로부터 받은 이미지 URL

        console.log(res.data);
        console.log(imgUrl);

        // 에디터의 커서 위치에 이미지 요소를 삽입한다.
        quillObj.insertEmbed(range.index, "image", `${imgUrl}`);
      } catch (error) {
        console.error('Error uploading image:', error);
      }
    };
  };

  const modules = useMemo(() => ({
    toolbar: {
      container: [
        [{ header: [1, 2, 3, 4, 5, false] }],
        ['bold', 'italic', 'underline'],
        [{ align: ['justify', 'center', 'right'] }],
        ['image'], // 이미지 삽입 버튼
      ],
      handlers: { image: imageHandler }, // 이미지 핸들러 등록
    },
    clipboard: {
      matchVisual: false,
    },
    ImageResize: {
      parchment: Quill.import('parchment'),
    },
  }), []);

  return (
    <div style={{ width: '100%', maxWidth: '800px', overflowY: 'auto', maxHeight: '400px' }}>
      <ReactQuill
        ref={quillRef} // ref 연결
        style={{ width: '100%', height: '100%' }} // width와 height를 100%로 설정
        modules={modules}
        value={content}
        onChange={setContent}
      />
    </div>
  );
};

export default ContentEditor;