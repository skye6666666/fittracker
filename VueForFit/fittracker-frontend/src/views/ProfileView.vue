<template>
    <div class="card p-4">
        <h3>My Profile</h3>

        <p><strong>Username:</strong> {{profile.username}} </p>

        <p><strong>Email:</strong> {{profile.email}} </p>

        <p><strong>Role:</strong> {{profile.role}} </p>

        <p><strong>Member Since:</strong> {{profile.createdAt}} </p>

        <div>
            <button class="btn btn-success me-3" @click="changePassword">
                Change Password
            </button>

            <button class="btn btn-secondary" @click="backToWorkout">
                Back
            </button>

        </div>
    </div>

    
</template>

<script setup>
import { ref, onMounted } from 'vue';
import http from "../api/http.js";
import { useRouter } from "vue-router"

const router = useRouter()
const profile = ref([])


onMounted(async () => {

  const response = await http.get(
    "/users/me",
    {
      headers: {
        Authorization:
          `Bearer ${localStorage.getItem("token")}`
      }
    }
  )

  profile.value = response.data

})

const backToWorkout = () => {

    router.push(
    "/workouts"
    )
}

const changePassword = () => {
    router.push(
        "/changePassword"
    )
}


</script>
