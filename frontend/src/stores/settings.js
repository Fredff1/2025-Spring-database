import { defineStore } from 'pinia'
import { ref } from 'vue'
import { admin } from '@/api'

export const useSettingsStore = defineStore('settings', () => {

  const basicSettings = ref({})
  const repairSettings = ref({})
  const inventorySettings = ref({})
  const notificationSettings = ref({})

  const fetchSettings = async () => {
    try {
      const [basic, repair, inventory, notification] = await Promise.all([
        admin.getBasicSettings(),
        admin.getRepairSettings(),
        admin.getInventorySettings(),
        admin.getNotificationSettings()
      ])
      basicSettings.value = basic
      repairSettings.value = repair
      inventorySettings.value = inventory
      notificationSettings.value = notification
    } catch (error) {
      console.error('Fetch settings failed:', error)
      throw error
    }
  }

  const updateBasicSettings = async (settings) => {
    try {
      const res = await admin.updateBasicSettings(settings)
      basicSettings.value = res
      return res
    } catch (error) {
      console.error('Update basic settings failed:', error)
      throw error
    }
  }

  const updateRepairSettings = async (settings) => {
    try {
      const res = await admin.updateRepairSettings(settings)
      repairSettings.value = res
      return res
    } catch (error) {
      console.error('Update repair settings failed:', error)
      throw error
    }
  }

  const updateInventorySettings = async (settings) => {
    try {
      const res = await admin.updateInventorySettings(settings)
      inventorySettings.value = res
      return res
    } catch (error) {
      console.error('Update inventory settings failed:', error)
      throw error
    }
  }

  const updateNotificationSettings = async (settings) => {
    try {
      const res = await admin.updateNotificationSettings(settings)
      notificationSettings.value = res
      return res
    } catch (error) {
      console.error('Update notification settings failed:', error)
      throw error
    }
  }

  return {

    basicSettings,
    repairSettings,
    inventorySettings,
    notificationSettings,

    fetchSettings,
    updateBasicSettings,
    updateRepairSettings,
    updateInventorySettings,
    updateNotificationSettings
  }
}) 