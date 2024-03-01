import { defineStore } from 'pinia'
import { ref } from 'vue'

// 定义 Store
export const useMemberStore = defineStore(
  'member',
  () => {
    // 登录用户信息
    const userInfo = ref<any>()

    // 保存登录用户信息
    const setUserInfo = (val: any) => {
      userInfo.value = val
    }

    // 清除登录用户信息
    const clearUserInfo = () => {
      userInfo.value = undefined
    }

    // 记得 return
    return {
      userInfo,
      setUserInfo,
      clearUserInfo,
    }
  },
  // TODO: 持久化
  {
    persist: true,
  },
)
