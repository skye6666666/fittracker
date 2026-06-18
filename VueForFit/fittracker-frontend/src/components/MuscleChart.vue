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

// const props = defineProps({
//   labels: Array,
//   data: Array
// })
const props = defineProps({
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
      labels: props.data.map(d => d.group),
      datasets: [
        {
          label: 'Training Volume (kg)',
          data: props.data.map(d => d.volume),
          backgroundColor: 'rgba(255, 115, 21, 0.5)',
          hoverBackgroundColor: 'rgba(255, 115, 21, 1)',
          borderRadius: 8
        }
      ]
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,

      plugins: {
        title: {
          display: true,
          text: '訓練部位與訓練量'
        },
        tooltip: {
            callbacks: {
                label: function(context) {
                    const data = props.data[context.dataIndex]
                    
                    return [
                        `Volume: ${data.volume.toLocaleString()} kg`,
                        `Exercises: ${data.exerciseCount}`,
                        `Sets: ${data.set}`
                    ]
                }
            }
        }
      },
      scales: {
            y: {
                ticks: {
                    callback: function(value) {
                        if (value >= 1000) {
                            return (value / 1000) + "k"
                        }
                        return value
                    }
                }
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
