/* eslint-disable react/prop-types */
import React from "react";

const Modal = ({ isOpen, onClose, children }) => {
  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50 z-50">
      <div className="bg-white max-w-lg p-6 rounded-lg shadow-lg">
        <div className="mb-4">{children}</div>
        <button
          onClick={onClose}
          className="mt-4 px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600 font-semibold"
        >
          알겠습니다!
        </button>
      </div>
    </div>
  );
};

export default Modal;
