<template>
  <div>
    <h2>Register</h2>

    <div class="d-flex justify-content-center mb-3">
        <input class="form-control w-25"
        v-model="username"
        placeholder="Username"
        />
    </div>

    <div class="d-flex justify-content-center mb-3">
        <input class="form-control w-25"
        v-model="email"
        placeholder="Email"
        />
    </div>

    <div class="d-flex justify-content-center mb-3">
        <input class="form-control w-25"
        type="password"
        v-model="password"
        placeholder="Password"
        />
    </div>

    <button @click="register">
      Register
    </button>
  </div>
</template>

<script setup>
import { ref, onMounted, computed,  watch} from "vue";
import axios from "axios";
import LoginView from "../views/LoginView.vue";
import { useRouter } from "vue-router"


const username = ref('')
const email = ref('')
const password = ref('')
const router = useRouter()

const register = async () => {

  await axios.post(
    'http://localhost:8080/api/users/register',
    {
      username: username.value,
      email: email.value,
      password: password.value
    }
  )

  alert('註冊成功')

  router.push('/login')
}
</script>