import { defineConfig } from 'vite'
import uni from '@dcloudio/vite-plugin-uni'

export default defineConfig({
  build: {
    sourcemap: process.env.NODE_ENV === 'development',
  },
  plugins: [uni()],
})
