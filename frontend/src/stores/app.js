import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useAppStore = defineStore('app', () => {
  const sidebar = ref({
    opened: localStorage.getItem('sidebarStatus') ? !!+localStorage.getItem('sidebarStatus') : true,
    withoutAnimation: false
  })
  const device = ref('desktop')
  const size = ref(localStorage.getItem('size') || 'default')
  const theme = ref(localStorage.getItem('theme') || 'light')

  const toggleSidebar = () => {
    sidebar.value.opened = !sidebar.value.opened
    sidebar.value.withoutAnimation = false
    if (sidebar.value.opened) {
      localStorage.setItem('sidebarStatus', 1)
    } else {
      localStorage.setItem('sidebarStatus', 0)
    }
  }

  const closeSidebar = ({ withoutAnimation }) => {
    localStorage.setItem('sidebarStatus', 0)
    sidebar.value.opened = false
    sidebar.value.withoutAnimation = withoutAnimation
  }

  const toggleDevice = (device) => {
    device.value = device
  }

  const setSize = (size) => {
    size.value = size
    localStorage.setItem('size', size)
  }

  const setTheme = (theme) => {
    theme.value = theme
    localStorage.setItem('theme', theme)
  }

  return {
    sidebar,
    device,
    size,
    theme,

    toggleSidebar,
    closeSidebar,
    toggleDevice,
    setSize,
    setTheme
  }
}) 