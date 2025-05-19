<template>
  <svg :class="svgClass" aria-hidden="true">
    <use :xlink:href="`#icon-${name}`" />
  </svg>
</template>

<script setup>
import { computed } from 'vue';
const props = defineProps({ name: { type: String, required: true }, size: { type: [String, Number], default: 20 } });
const svgClass = computed(() => `svg-icon svg-${props.name}`);
</script>

<style scoped>
svg {
  width: v-bind(size + 'px');
  height: v-bind(size + 'px');
  fill: currentColor;
  vertical-align: middle;
}
</style>