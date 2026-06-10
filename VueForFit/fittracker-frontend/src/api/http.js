import axios from "axios";

const http = axios.create({
  baseURL: "http://localhost:8080/api",
});

http.interceptors.request.use((config) => {

  const token = localStorage.getItem("token");

  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }

  return config;
});

// 回應攔截器：統一處理錯誤
http.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response) {
      if (error.response.status === 401) {
        alert("登入已過期，請重新登入");
        localStorage.removeItem("token");
        // 這裡可以加上 router.push("/login") 讓使用者回登入頁
        router.push("../views/LoginView.vue")
      } else if (error.response.status >= 500) {
        alert("伺服器錯誤，請稍後再試");
      }
    } else {
      alert("網路連線失敗");
    }
    return Promise.reject(error);
  }
);

export default http;