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

    <div class="d-flex justify-content-center mb-3">
        <input class="form-control w-25"
        type="password"
        v-model="confirmPassword"
        placeholder="Confirm Password"
        />
    </div>

    <button class="btn btn-success me-2" @click="register">
      Register
    </button>
    <button class="btn btn-warning" @click="cancelRegister">
      Back
    </button>
  </div>
</template>

<script setup>
import { ref } from "vue";
import http from "../api/http.js";
import { useRouter } from "vue-router"


const username = ref('')
const email = ref('')
const password = ref('')
const confirmPassword = ref('')
const router = useRouter()

const register = async () => {

    try {

        if (password.value !== confirmPassword.value) {
        alert('兩次密碼不一致')
        return
        }

        await http.post(
            "/users/register",
            {
            username: username.value,
            email: email.value,
            password: password.value
            }
        )
        

        alert('註冊成功')

        router.push('/login')
    }catch(error){
        alert(error.response?.data || '註冊失敗')
        
    }
}

const cancelRegister = () => {

    router.push(
    "/login"
    )
}
</script>