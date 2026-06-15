<template>
    <div
        v-if="successMessage"
        class="alert alert-success"
        >
        {{ successMessage }}
    </div>

  <div class="container mt-4">

    <div
      class="d-flex justify-content-between align-items-center mb-4"
    >

      <h2>Exercise Management</h2>

      <div class="mb-3">
        <input
            v-model="search"
            class="form-control"
            placeholder="Search exercise..."
        />
      </div>

      <div class="d-flex gap-2">
                
        <button
            class="btn btn-success"
            @click="goToCreateExercise"
        >
            Create Exercise
        </button>

        <button
            class="btn btn-secondary"
            @click="backToWorkoutList"
        >
            Back
        </button>

      </div>
      

    </div>

    <table class="table table-hover">

      <thead>
        <tr>
          <th>Description</th>
          <th>Name</th>
          <th>Muscle Group</th>
          <th>Action</th>
        </tr>
      </thead>

      <tbody>

        <tr
          v-for="exercise in paginatedExercises"
          :key="exercise.id"
        >

          <td v-if="editingId !== exercise.id">
            {{ exercise.description }}
          </td>
          <td v-else>
            <input class="form-control" v-model="editForm.description" />
          </td>

          <td v-if="editingId !== exercise.id">
            {{ exercise.name }}
          </td>
          <td v-else>
            <input class="form-control" v-model="editForm.name" />
          </td>

          <td v-if="editingId !== exercise.id">
            {{ exercise.muscleGroup }}
          </td>
          <td v-else>
            <input class="form-control" v-model="editForm.muscleGroup" />
          </td>

          <td class="text-center">
            <div class="d-flex justify-content-center gap-1">
                <template v-if="editingId !== exercise.id">
                    <button class="btn btn-warning" @click="startEdit(exercise)">
                        Edit
                    </button>

                    <button class="btn btn-danger" @click="deleteExercise(exercise.id)">
                        Delete
                    </button>
                </template>

                <template v-else>
                    <button class="btn btn-success" @click="saveEdit(exercise.id)">
                        Save
                    </button>

                    <button class="btn btn-secondary" @click="cancelEdit">
                        Cancel
                    </button>
                </template>
            </div>
          </td>

        </tr>

        <tr v-if="paginatedExercises.length === 0">
            <td colspan="4">No exercises found</td>
        </tr>

      </tbody>

    </table>

    <div class="d-flex gap-1 mt-3">
        <button
            class="btn btn-outline-primary"
            v-for="p in totalPages"
            :key="p"
            @click="page = p"
            :class="{ active: page === p }"
        >
            {{ p }}
        </button>
    </div>

  </div>

</template>

<script setup>
import { ref, onMounted, computed, watch } from "vue"
import { useRouter } from "vue-router"
import http from "../api/http"

const router = useRouter()
const exercises = ref([])
const editingId = ref(null)
const editForm = ref({
  description: null,
  name: null,
  muscleGroup: null
})
const successMessage = ref("")

//add search & pagination
const search = ref("")
const page = ref(1)
const pageSize = 5





const loadExercises = async () => {
    try {
        const response = await http.get("/exercises")

        exercises.value = response.data

    } catch (error) {
        console.error(error)

        alert("讀取失敗")

    }
}

const startEdit = (exercise) => {

  editingId.value = exercise.id

  editForm.value = {
    description: exercise.description,
    name: exercise.name,
    muscleGroup: exercise.muscleGroup
  }

}

const deleteExercise = async (id) => {
    const confirmed = confirm("確定刪除嗎？")

    if (!confirmed) {
        return
    }

    try {

        await http.delete(
            `/exercises/${id}`
        )

        //alert("刪除成功")
        successMessage.value =
        "Exercise deleted successfully"

        setTimeout(() => {
        successMessage.value = ""
        }, 3000)

        await loadExercises()

    } catch(error) {

        console.error(error)

        alert("刪除失敗")

    }

}

const saveEdit = async (id) => {
    if (
        !editForm.value.description.trim() ||
        !editForm.value.name.trim() ||
        !editForm.value.muscleGroup.trim()
    ) {
        alert("所有欄位皆為必填")
        return
    }


  try {

    await http.put(
      `/exercises/${id}`,
      editForm.value
    )

    alert("修改成功")

    editingId.value = null

    editForm.value = {
        description: null,
        name: null,
        muscleGroup: null
    }

    await loadExercises()

  } catch(error) {

    console.error(error)

    alert("修改失敗")

  }
}

const cancelEdit = () => {
  editingId.value = null

  editForm.value = {
    description: null,
    name: null,
    muscleGroup: null
  }
}

const goToCreateExercise = () => {
    router.push(
    "/admin/exercises/create"
  )
}


const backToWorkoutList = () => {

    router.push(
    "/workouts"
    )

}

const filteredExercises = computed(() => {
  return exercises.value.filter((ex) => {
    const keyword = search.value.toLowerCase()

    return (
      ex.name.toLowerCase().includes(keyword) ||
      ex.description.toLowerCase().includes(keyword) ||
      ex.muscleGroup.toLowerCase().includes(keyword)
    )
  })
})

const paginatedExercises = computed(() => {
  const start = (page.value - 1) * pageSize
  const end = start + pageSize

  return filteredExercises.value.slice(start, end)
})

const totalPages = computed(() => {
  return Math.ceil(filteredExercises.value.length / pageSize)
})

watch(search, () => {
  page.value = 1
})

watch(totalPages, () => {
  if (page.value > totalPages.value) {
    page.value = 1
  }
})

onMounted(() => {
    loadExercises();
});


</script>