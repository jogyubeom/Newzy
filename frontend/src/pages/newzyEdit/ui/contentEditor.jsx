import React from 'react';
import ReactQuill from 'react-quill';
import 'react-quill/dist/quill.snow.css';

const ContentEditor = ({ content, setContent }) => {
  const modules = {
    toolbar: {
      container: [
        [{ header: [1, 2, 3, 4, 5, false] }],
        ['bold', 'italic', 'underline'],
        [{ align: ['justify', 'center', 'right'] }],
        ['image'],
      ],
    },
  };

  return (
    <ReactQuill
      style={{ width: '100%', height: '400px' }}
      modules={modules}
      value={content}
      onChange={setContent}
    />
  );
};

export default ContentEditor;