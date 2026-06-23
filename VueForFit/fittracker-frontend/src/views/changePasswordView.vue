<template>
      <div
        v-if="successMessage"
        class="alert alert-success"
        >
        {{ successMessage }}
    </div>

  <div class="container mt-4">

    <h2>Change Password</h2>

    <div class="card shadow-sm">

      <div class="card-body">

        <div class="mb-3">
          <label class="form-label">
            Current Password
          </label>

          <input
            class="form-control" type="password" v-model="currentPassword"/>
        </div>

        <div class="mb-3">
          <label class="form-label">
            New Password
          </label>

          <input
            class="form-control" type="password" v-model="newPassword"/>
        </div>

        <div class="mb-3">
          <label class="form-label">
            Confirm Password
          </label>

          <input
            class="form-control" type="password" v-model="confirmPassword"/>
        </div>

        <div>

            <button class="btn btn-success me-3" @click="SaveChangePassword">
                Change Password Confirm
            </button>

            <button class="btn btn-secondary" @click="cancelChange">
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
import { logout } from "../utils/auth"

const router = useRouter()
const errorMessage = ref("")
const successMessage = ref("")

const currentPassword = ref('')
const newPassword = ref('')
const confirmPassword = ref('')


const SaveChangePassword = async () => {

    if (
        !currentPassword.value.trim() ||
        !newPassword.value.trim() ||
        !confirmPassword.value.trim()
    ) {
        errorMessage.value = "所有欄位皆為必填"
        return
    }

    

    try {

        if (currentPassword.value == newPassword.value) {
        errorMessage.value = '新密碼不能與舊密碼相同'
        return
        }

        if (newPassword.value !== confirmPassword.value) {
        errorMessage.value = '兩次密碼不一致'
        return
        }

        

        await http.put(
            "/users/password",
            {
                currentPassword: currentPassword.value,
                newPassword: newPassword.value
            }
        )

        successMessage.value =
        "Password changed successfully"
        errorMessage.value = ""

        setTimeout(() => {
            successMessage.value = ""
            logout()
            router.push(
            "/login"
            )
        }, 3000)

        
 
    } catch (error) {

        console.error(error)

        alert(
        error.response?.data?.message
        || "更改密碼失敗"
        )

    }

}

const cancelChange = () => {

    successMessage.value = ""
    errorMessage.value = ""

    router.push(
    "/profile"
    )

}

</script>