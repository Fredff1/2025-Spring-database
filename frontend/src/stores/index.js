import { createPinia } from 'pinia'
import { useAuthStore } from './auth'
import { useSettingsStore } from './settings'
import { useAppStore } from './app'
import { usePermissionStore } from './permission'

const pinia = createPinia()

export {
  useAuthStore,
  useSettingsStore,
  useAppStore,
  usePermissionStore
}

export default pinia 