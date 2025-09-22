import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vite.dev/config/
//
export default defineConfig({
  plugins: [vue()],
//配置文件
  server: {
    host: '0.0.0.0',
    port: 8082,
    open: false,
  },
})
