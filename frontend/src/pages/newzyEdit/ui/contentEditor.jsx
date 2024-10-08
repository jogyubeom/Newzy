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

  // const imageHandler = () => {
  //   const input = document.createElement('input');
  //   input.setAttribute('type', 'file');
  //   input.setAttribute('accept', 'image/*');
  //   input.click();

  //   input.addEventListener('change', async () => {
  //     const file = input.files[0];
  //     const formData = new FormData();
  //     formData.append("image", file); // 'image'라는 key로 파일 전송

  //     try {
  //       // 이미지 업로드를 위한 POST 요청
  //       const res = await baseAxios().post("/newzy/upload-image", formData, {
  //         headers: {
  //           "Content-Type": "multipart/form-data",  // 파일 전송을 위한 헤더 설정
  //         },
  //       });

  //       const imgUrl = res.data.imgUrl; // 서버에서 받은 이미지 URL
  //       const editor = quillRef.current.getEditor();
  //       const range = editor.getSelection(); // 현재 커서 위치 가져오기
  //       editor.insertEmbed(range.index, 'image', imgUrl); // 이미지 삽입
  //       editor.setSelection(range.index + 1); // 커서를 이미지 다음으로 이동
  //     } catch (error) {
  //       console.log(error);
  //     }
  //   });
  // };

  const imageHandler = () => {
    const input = document.createElement('input');
    input.setAttribute('type', 'file');
    input.setAttribute('accept', 'image/*');
    input.click();
  
    input.addEventListener('change', async () => {
      const file = input.files[0];
      if (!file) return; // Ensure a file is selected
      const formData = new FormData();
      formData.append("image", file); // Append the file
  
      try {
        // Post the image to the server
        const res = await baseAxios().post("/newzy/upload-image", formData, {
          headers: {
            "Content-Type": "multipart/form-data",  // Set the correct header
          },
        });
  
        const imgUrl = res.data.imgUrl; // Get the image URL from response
        const editor = quillRef.current.getEditor();
        const range = editor.getSelection(); // Get the current selection
  
        if (range) {
          editor.insertEmbed(range.index, 'image', imgUrl); // Insert the image at the cursor position
          editor.setSelection(range.index + 1); // Move cursor to the right of the image
        }
      } catch (error) {
        console.error('Error uploading image:', error);
      }
    });
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
      clipboard: {
        matchVisual: false,
      },
      ImageResize: {
        parchment: Quill.import('parchment'),
      },
    },
  }), []);

  return (
    <ReactQuill
      ref={quillRef} // ref 연결
      style={{ width: '100%', height: '60%' }}
      modules={modules}
      value={content}
      onChange={setContent}
    />
  );
};

export default ContentEditor;