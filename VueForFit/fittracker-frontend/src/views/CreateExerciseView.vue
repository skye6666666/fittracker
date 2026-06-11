<template>
  <div class="container mt-4">

    <h2>Create Exercise</h2>

    <div class="card shadow-sm">

      <div class="card-body">

        <div class="mb-3">
          <label class="form-label">
            Exercise Name
          </label>

          <input
            class="form-control" v-model="form.name"/>
        </div>

        <div class="mb-3">
          <label class="form-label">
            Description
          </label>

          <input
            class="form-control" v-model="form.description"/>
        </div>

        <div class="mb-3">
          <label class="form-label">
            Muscle Group
          </label>

          <input
            class="form-control" v-model="form.muscleGroup"/>
        </div>

        <div>

            <button class="btn btn-success me-3" @click="createExercise">
                Create
            </button>

            <button class="btn btn-secondary" @click="cancelCreate">
                Back
            </button>

        </div>
        <br>

        <div
            v-if="errorMessage"
            class="alert alert-danger"
            >
            {{ errorMessage }}
        </div>

      </div>

    </div>

  </div>
</template>

<script setup>
import { ref } from "vue"
import http from "../api/http"
import { useRouter } from "vue-router"

const router = useRouter()
const errorMessage = ref("")

const form = ref({
  name: "",
  description: "",
  muscleGroup: ""
})

const createExercise = async () => {

    if (
        !form.value.name.trim() ||
        !form.value.description.trim() ||
        !form.value.muscleGroup.trim()
    ) {
        errorMessage.value = "所有欄位皆為必填"
        return
    }

    try {

    await http.post(
      "/exercises",
      form.value
    )

    alert("Exercise 建立成功")

    form.value = {
      name: "",
      description: "",
      muscleGroup: ""
    }

  } catch (error) {

    console.error(error)

    alert(
      error.response?.data?.message
      || "建立失敗"
    )

  }

}

const cancelCreate = () => {

    form.value = {
      name: "",
      description: "",
      muscleGroup: ""
    }

    router.push(
    "/workouts"
    )


}
</script>