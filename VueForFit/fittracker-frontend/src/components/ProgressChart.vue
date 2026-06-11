<template>
  <div style="height: 400px;">
    <canvas ref="chartCanvas"></canvas>
  </div>
</template>

<script setup>
import { ref, watch, nextTick, onMounted } from 'vue'
import {
  Chart,
  LineController,
  LineElement,
  PointElement,
  CategoryScale,
  LinearScale,
  Title,
  Tooltip,
  Legend
} from 'chart.js'

Chart.register(
  LineController,
  LineElement,
  PointElement,
  CategoryScale,
  LinearScale,
  Title,
  Tooltip,
  Legend
)

const props = defineProps({
  data: {
    type: Array,
    default: () => []
  }
})

const chartCanvas = ref(null)

let chartInstance = null

const renderChart = async () => {

  await nextTick()

  if (!chartCanvas.value) {
    return
  }

  if (chartInstance) {
    chartInstance.destroy()
  }

  chartInstance = new Chart(chartCanvas.value, {

    type: 'line',

    data: {

      labels: props.data.map(item =>
        new Date(item.date)
          .toLocaleDateString()
      ),

      datasets: [
        {
          label: 'Weight (kg)',

          data: props.data.map(
            item => item.weight
          ),

          borderColor: '#198754',
          pointBackgroundColor: '#198754',
          pointBorderColor: '#ffffff',
          pointBorderWidth: 2,

          pointRadius: 6,
          pointHoverRadius: 8,

          borderWidth: 5,

          tension: 0.3,

          fill: false
        }
      ]
    },

    options: {

      responsive: true,

      maintainAspectRatio: false,

      plugins: {

        title: {
          display: true,
          text: 'Exercise Progress'
        },

        tooltip: {
            callbacks: {
                label: function(context) {

                const data =
                    props.data[
                    context.dataIndex
                    ]

                return `Weight: ${data.weight} kg`
                }
            }
        }
      },

      scales: {

        y: {

          beginAtZero: false,

          title: {
            display: true,
            text: 'Weight (kg)'
          }
        },

        x: {

          title: {
            display: true,
            text: 'Date'
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
  () => props.data,
  async () => {
    await renderChart()
  },
  { deep: true }
)
</script>