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
    const input = document.createElement("input");
    input.setAttribute("type", "file");
    input.setAttribute("accept", "image/*");
    input.click();

    input.onchange = async () => {
      const file = input.files ? input.files[0] : null;
      if (!file) return;

      const formData = new FormData();
      formData.append("image", file);

      let quillObj = quillRef.current?.getEditor();
      if (!quillObj) return;

      const range = quillObj.getSelection();
      if (!range) return;

      try {
        const res = await baseAxios().post("/newzy/upload-image", formData, {
          headers: {
            "Content-Type": "multipart/form-data",
          },
        });

        const imgUrl = res.data.url;
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
        ['image'],
      ],
      handlers: { image: imageHandler },
    },
    clipboard: {
      matchVisual: false,
    },
    ImageResize: {
      parchment: Quill.import('parchment'),
    },
  }), []);

  return (
    <div style={{ width: '100%', maxWidth: '800px', overflowY: 'auto' }}>
      <ReactQuill
        ref={quillRef}
        style={{ minHeight: '400px', maxHeight: '400px', overflowY: 'auto' }} // 최소 및 최대 높이 설정
        modules={modules}
        value={content}
        onChange={setContent}
      />
    </div>
  );
};

export default ContentEditor;