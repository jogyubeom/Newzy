// utils/stringUtils.js

// UTF-8 바이트 계산 함수
export const getByteLengthUTF8 = (str) => {
  return new TextEncoder().encode(str).length;
};

// 글자 수 제한 및 초과 여부 확인
export const checkByteLength = (value, maxBytes) => {
  const trimmedValue = value.replace(/\s/g, ""); // 공백 제거한 문자열
  const byteLength = getByteLengthUTF8(trimmedValue);

  // 글자 수 초과 여부 체크
  const isExceeded = byteLength > maxBytes;

  return { byteLength, isExceeded };
};
