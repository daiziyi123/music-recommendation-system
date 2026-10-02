import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:9171',
  timeout: 10000
});

// 请求拦截器
api.interceptors.request.use(config => {
  // 可以在这里添加token等
  return config;
}, error => {
  return Promise.reject(error);
});

// 响应拦截器
api.interceptors.response.use(response => {
  return response.data;
}, error => {
  return Promise.reject(error);
});

export default api;
