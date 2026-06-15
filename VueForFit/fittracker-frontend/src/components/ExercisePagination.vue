<template>
  <div class="d-flex gap-1 mt-3">
    <button
      v-for="p in totalPages"
      :key="p"
      class="btn btn-outline-primary"
      :class="{ active: p === page }"
      @click="changePage(p)"
    >
      {{ p }}
    </button>
  </div>
</template>

<script setup>
const props = defineProps({
  page: Number,
  totalPages: Number
})

const emit = defineEmits(['change-page'])

const changePage = (p) => {
  emit('change-page', p)
}
</script>