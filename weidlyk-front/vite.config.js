import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [vue()],
  server: {
    // 你的前端服务端口（根据你的错误日志，是8082）
    port: 8082,
    proxy: {
      // 关键配置：将所有 /api 开头的请求代理到后端服务
      '/api': {
        // 你的后端服务地址（根据你的配置文件，是8089）
        target: 'http://localhost:8089',

        // 必须开启，允许跨域
        changeOrigin: true,
      }
    }
  }
})