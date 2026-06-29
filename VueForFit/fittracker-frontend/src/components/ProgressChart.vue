<template>
  <div class="d-flex justify-content-center mb-3">

    <!-- <label class="form-label mt-2 me-3">
        Chart Type
    </label> -->

    <div class="mb-3">

      <div class="text-center mb-3">

          <span class="text-secondary fw-semibold">
              Analysis
          </span>

      </div>

      <ul class="nav nav-tabs justify-content-center">

        <li class="nav-item me-2">

          <button
            class="nav-link"
            :class="{ active: chartType === 'weight' }"
            @click="chartType = 'weight'">

            Weight

          </button>

        </li>

        <li class="nav-item me-2">

          <button
            class="nav-link"
            :class="{ active: chartType === 'oneRm' }"
            @click="chartType = 'oneRm'">

            Estimated 1RM

          </button>

        </li>

        <li class="nav-item">

          <button
            class="nav-link"
            :class="{ active: chartType === 'volume' }"
            @click="chartType = 'volume'">

            Volume

          </button>

        </li>

      </ul>

    </div>

    


  </div>


  <div class="card shadow-lg mt-4 mb-4" style="height: 400px;">
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

const chartConfig = {

    weight: {

        label: "Weight (kg)",

        yAxis: "Weight (kg)"

    },

    oneRm: {

        label: "Estimated 1RM (kg)",

        yAxis: "Estimated 1RM (kg)"

    },

    volume: {

        label: "Volume (kg)",

        yAxis: "Volume (kg)"

    }

}


const chartValues = computed(() => {

    if (chartType.value === "weight") {

        return props.data.map(
            item => item.weight
        )

    }

    else if (chartType.value === "volume") {
      return props.data.map(
        item => item.volume
      )
    }

    return props.data.map(
        item => item.estimatedOneRm
    )

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
          label: chartConfig[chartType.value].label,
          
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

                  if (chartType.value === "volume") {

                      return `Volume: ${data.volume} kg`

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
            text: chartConfig[chartType.value].yAxis
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