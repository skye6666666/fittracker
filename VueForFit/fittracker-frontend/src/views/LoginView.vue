<template>
  <div>
    <h1>FitTracker</h1>

    <div class="d-flex justify-content-center mb-3">
        <input class="form-control w-25" 
        v-model="email" 
        placeholder="Email" />
    </div>
    <div class="d-flex justify-content-center mb-3">
        <input class="form-control w-25" 
        v-model="password" 
        type="password" 
        placeholder="Password" />
    </div>
    
    <button  class="btn btn-success me-2" @click="login" :disabled="loading">
        {{ loading ? "登入中..." : "Login" }}
    </button>

    <button class="btn btn-warning" @click="goToRegister">
        Register
    </button>

  </div>
</template>


<script setup lang="ts">
import { ref } from "vue";
import http from "../api/http";
import { useRouter } from "vue-router"
import type { LoginRequest, LoginResponse } from "../types/auth";
import axios from "axios"

const router = useRouter()

const email = ref<string>("");
const password = ref<string>("");
const loading = ref<boolean>(false)
const role = ref<string>("");

const login = async (): Promise<void> => {
  try {
    loading.value = true;

    const payload: LoginRequest = {
      email: email.value,
      password: password.value,
    }

    const response = await http.post<LoginResponse>("/users/login", payload);

    localStorage.setItem(
      "token",
      response.data.token
    );

    localStorage.setItem(
    "email",
    email.value
    );

    console.log(response.data);

    if (response.data.token) {
        router.push("/workouts")
    }


  } catch (error) {

    console.error(error);

    if (axios.isAxiosError(error)) {
      alert(error.response?.data?.message || "登入失敗")
    } else {
      alert("發生未知錯誤")
    }

  } finally{
    loading.value = false;
  }
};


const goToRegister = () => {

  router.push(
    "/register"
  )

}
</script>

