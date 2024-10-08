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

  const imageHandler = () => {
    const input = document.createElement('input');
    input.setAttribute('type', 'file');
    input.setAttribute('accept', 'image/*');
    input.click();

    input.addEventListener('change', async () => {
      const file = input.files[0];
      const formData = new FormData();
      formData.append("img", file);

      try {
        const res = await baseAxios().post("https://j11b305.p.ssafy.io/api/newzy/upload-image", formData, {
          headers: {
            "Content-Type": "multipart/form-data",
          },
        });
        const imgUrl = res.data.imgUrl;
        const editor = quillRef.current.getEditor();
        const range = editor.getSelection();
        editor.insertEmbed(range.index, 'image', imgUrl);
        editor.setSelection(range.index + 1);
      } catch (error) {
        console.log(error);
      }
    });
  };

  const modules = useMemo(() => ({
    toolbar: {
      container: [
        [{ header: [1, 2, 3, 4, 5, false] }],
        ['bold', 'italic', 'underline'],
        [{ align: ['justify', 'center', 'right'] }],
        ['image'],
      ],
      handlers: { image: imageHandler },
      clipboard: {
        matchVisual: false,
      },
      ImageResize: {
        parchment: Quill.import('parchment')
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