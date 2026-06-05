<template>
  <div>
    <canvas ref="chartCanvas"></canvas>
  </div>
</template>

<script setup>
import { ref, watch, nextTick, onMounted } from 'vue'
import {
  Chart,
  BarController,
  BarElement,
  CategoryScale,
  LinearScale,
  Title,
  Tooltip,
  Legend
} from 'chart.js'

Chart.register(BarController, BarElement, CategoryScale, LinearScale, Title, Tooltip, Legend)

const props = defineProps({
  labels: Array,
  data: Array
})

const chartCanvas = ref(null)
let chartInstance = null

const renderChart = async () => {
  await nextTick()
  if (!chartCanvas.value) return

  if (chartInstance) chartInstance.destroy()

  chartInstance = new Chart(chartCanvas.value, {
    type: 'bar',
    data: {
      labels: props.labels || [],
      datasets: [
        {
          label: 'Training Volume (kg)',
          data: props.data || [],
          backgroundColor: 'rgba(75, 192, 192, 0.5)'
        }
      ]
    },
    options: {
      responsive: true,
      plugins: {
        title: {
          display: true,
          text: '訓練部位與訓練量'
        }
      }
    }
  })
}

onMounted(() => {
  renderChart()
})

watch(
  () => [props.labels, props.data],
  async () => {
    await renderChart()
  },
  { deep: true }
)
</script>

<style scoped>
canvas {
  width: 600px;
  height: 400px;
}
</style>
