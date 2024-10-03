// src/shared/utils/baseAxios.js
import axios from "axios";

const BASE_SERVER_URL = "https://j11b305.p.ssafy.io/api";

function baseAxios() {
  const instance = axios.create({
    baseURL: BASE_SERVER_URL,
    headers: {
      "Content-Type": "application/json",
    },
    withCredentials: true, // 필요에 따라 쿠키를 포함할지 결정
  });

  return instance;
}

export default baseAxios;
