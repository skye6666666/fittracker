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
          v-for="exercise in exercises"
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

          <td>
            <div class="d-flex gap-1">
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

      </tbody>

    </table>

  </div>

</template>

<script setup>
import { ref, onMounted } from "vue"
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

    // form.value = {
    //   name: "",
    //   description: "",
    //   muscleGroup: ""
    // }

    router.push(
    "/workouts"
    )


}

onMounted(() => {
    loadExercises();
});


</script>