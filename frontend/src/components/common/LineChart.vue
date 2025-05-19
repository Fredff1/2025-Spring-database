<template>
  <div ref="chartRef" class="chart" />
</template>

<script setup>
import { onMounted, ref } from 'vue';
import * as echarts from 'echarts';
const props = defineProps({ xData: Array, series: Array, height: { type: String, default: '300px' } });
const chartRef = ref();

onMounted(() => {
  const chart = echarts.init(chartRef.value);
  chart.setOption({
    tooltip: { trigger: 'axis' },
    xAxis: { type: 'category', data: props.xData },
    yAxis: { type: 'value' },
    series: [{ data: props.series, type: 'line', smooth: true }]
  });
});
</script>

<style scoped>
@import "@/styles/variables.scss";
.chart { width: 100%; height: v-bind(height); }
</style>
