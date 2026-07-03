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

<script setup lang="ts">
import { ref } from "vue";
import http from "../api/http.js";
import { useRouter } from "vue-router"
import axios from "axios";
import type { RegisterRequest } from "../types/auth";


const username = ref<string>('')
const email = ref<string>('')
const password = ref<string>('')
const confirmPassword = ref<string>('')
const router = useRouter()

const register = async (): Promise<void> => {

    try {

        if (password.value !== confirmPassword.value) {
        alert('兩次密碼不一致')
        return
        }

        const payload: RegisterRequest = {
            username: username.value,
            email: email.value,
            password: password.value
        }

        await http.post<void>(
            "/users/register",payload
        )
        

        alert('註冊成功')

        router.push('/login')
    }catch(error){
        if(axios.isAxiosError(error)){
            alert(error.response?.data || '註冊失敗')
        } else{
            alert("發生未知錯誤")
        }
        
    }
}

const cancelRegister = (): void => {

    router.push(
    "/login"
    )
}
</script>