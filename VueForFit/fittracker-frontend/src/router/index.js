import { createRouter, createWebHistory } from "vue-router"
import WorkoutList from "../views/WorkoutList.vue"
import LoginView from "../views/LoginView.vue"
import { getRole } from "../utils/auth"
import CreateExerciseView from "../views/CreateExerciseView.vue"
import ExerciseManagementView from "../views/ExerciseManagementView.vue"
import { parseJwt, logout } from "../utils/auth"
import Register from "../components/Register.vue"

const routes = [
  { path: "/", component: LoginView },
  { path: "/workouts", component: WorkoutList, 
    meta: {
      requiresAuth: true
    }}
]

// const router = createRouter({
//   history: createWebHistory(),
//   routes
// })

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: "/login",
      component: LoginView
    },

    {
      path: "/workouts",
      component: WorkoutList,
      meta: {
        requiresAuth: true
      }
    }
    ,

    {
      path: "/admin/exercises/create",
      component: CreateExerciseView,
      meta: {
        requiresAuth: true,
        requiresAdmin: true
      }
    }
    ,
    {
      path: "/admin/exercises",
      component: ExerciseManagementView,
      meta: {
        requiresAuth: true,
        requiresAdmin: true
      }
    }
    ,
    {
      path: '/register',
      component: Register
    }
  ]
})

router.beforeEach((to) => {

  const token = localStorage.getItem("token")

  const role = getRole()

  if (to.path === "/login") {
    return true
  }

  //沒token
  if (to.meta.requiresAuth && !token) {
    return "/login"
    
  } 

  //token過期
  if (to.meta.requiresAuth && isTokenExpired()) {
    alert("登入已過期，請重新登入");
    //logout()
    return "/login"
    

  }

  //admin權限
  if (to.meta.requiresAdmin && role !== "ADMIN") {
    return "/workouts"
    
  } 


  //已登入卻去login
  if (
    to.path === "/login" &&
    token
  ) {
    return "/workouts"
    
  }

  if (
    to.path === "/"
  ){
    return "/login"
  }

  
  return true
})

export function isTokenExpired() {

  const token =
    localStorage.getItem("token")

  if (!token)
    return true

  try {
    const payload = parseJwt(token)
    if (!payload?.exp) return true

    return payload.exp * 1000 < Date.now()

  } catch (e) {
    return true
  }

}

export default router