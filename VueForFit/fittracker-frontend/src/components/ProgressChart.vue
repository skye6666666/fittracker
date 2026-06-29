<template>
  <div class="d-flex justify-content-center mb-3">

    <label class="form-label mt-2 me-3">
        Chart Type
    </label>

    <select
        class="form-select"
        style="max-width:250px"
        v-model="chartType">

        <option value="weight">
            Weight
        </option>

        <option value="oneRm">
            Estimated 1RM
        </option>

    </select>

  </div>

  <div style="height: 400px;">
    <canvas ref="chartCanvas"></canvas>
  </div>
</template>

<script setup>
import { ref, watch, nextTick, onMounted, computed  } from 'vue'
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

const chartType = ref("weight")

const chartLabel = computed(() => {

    return chartType.value === "weight"

        ? "Weight (kg)"

        : "Estimated 1RM (kg)"

})

const chartValues = computed(() => {

    if (chartType.value === "weight") {

        return props.data.map(
            item => item.weight
        )

    }

    return props.data.map(
        item => item.estimatedOneRm
    )

})

const yAxisTitle = computed(() => {

    return chartType.value === "weight"

        ? "Weight (kg)"

        : "Estimated 1RM (kg)"

})

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
          label: chartLabel.value,
          
          data:
          chartValues.value
          ,

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

        // title: {
        //   display: true,
        //   text: 'Exercise Progress'
        // },

        tooltip: {
            callbacks: {
                label: function(context) {

                  const data = props.data[context.dataIndex]

                  if (chartType.value === "weight") {

                      return `Weight: ${data.weight} kg`

                  }

                  return `Estimated 1RM: ${data.estimatedOneRm.toFixed(1)} kg`
                }
            }
        }
      },

      scales: {

        y: {

          beginAtZero: false,

          title: {
            display: true,
            text: yAxisTitle.value
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

watch(chartType, () => {

    renderChart()

})
</script>