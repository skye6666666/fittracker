<template>
  <canvas ref="chartCanvas"></canvas>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'

import {
  Chart,
  BarElement,
  CategoryScale,
  LinearScale,
  Tooltip,
  Legend
} from 'chart.js'

Chart.register(
  BarElement,
  CategoryScale,
  LinearScale,
  Tooltip,
  Legend
)

const props = defineProps({
  labels: Array,
  data: Array
})

const chartCanvas = ref(null)

let chartInstance = null

const renderChart = () => {

  if (chartInstance) {
    chartInstance.destroy()
  }

  chartInstance = new Chart(
    chartCanvas.value,
    {
      type: 'bar',

      data: {
        labels: props.labels,

        datasets: [
          {
            label: 'Weekly Volume',
            data: props.data,
            backgroundColor: 'rgba(255, 218, 34, 0.6)',
            hoverBackgroundColor: 'rgba(255, 218, 34, 1)',
            borderRadius: 8,
            maxBarThickness: 50
          }
        ]
      },

      options: {
        responsive: true,

        plugins: {
          legend: {
            display: true
          }
        }
      }
    }
  )
}

onMounted(renderChart)

watch(
  () => props.data,
  renderChart,
  { deep: true }
)
</script>