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

<script setup>
import { ref } from "vue";
import http from "../api/http";
import { useRouter } from "vue-router"
import { parseJwt } from "../utils/auth.js"
import Register from "../components/Register.vue";

const router = useRouter()

const email = ref("");
const password = ref("");
const loading = ref(false)
const role = ref("");


const login = async () => {
  try {
    loading.value = true;

    const response = await http.post("/users/login", {
      email: email.value,
      password: password.value,
    });

    localStorage.setItem(
      "token",
      response.data.token
    );

    localStorage.setItem(
    "email",
    email.value
    );

    // const user = parseJwt(response.data.token)
    // role.value = user?.role || "UNKNOWN"

    // localStorage.setItem(
    //   "role",
    //   role.value
    // );

    //alert("登入成功");

    console.log(response.data);
    //alert("準備跳轉")
    //router.push("/workouts")
    if (response.data.token) {
        router.push("/workouts")
    }
    //alert("已執行 router.push")

  } catch (error) {

    console.error(error);

    alert(error.response?.data?.message || "登入失敗");
  }
};


const goToRegister = () => {

  router.push(
    "/register"
  )

}
</script>

