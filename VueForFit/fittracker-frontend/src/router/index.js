import { createRouter, createWebHistory } from "vue-router"
import WorkoutList from "../components/WorkoutList.vue"
import LoginView from "../views/LoginView.vue"
import { getRole } from "../utils/auth"
import CreateExerciseView from "../views/CreateExerciseView.vue"
import ExerciseManagementView from "../views/ExerciseManagementView.vue"
import { parseJwt } from "../utils/auth"

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
      path: "/",
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
  ]
})

router.beforeEach((to) => {

  const token =
    localStorage.getItem("token")

  const role = getRole()

  if (
    to.meta.requiresAuth &&
    !token
  ) {
    return "/"
    
  } 

  if (
    to.path === "/" &&
    token
  ) {
    return "/workouts"
    
  }

  if (
    to.meta.requiresAdmin &&
    role !== "ADMIN"
  ) {
    return "/workouts"
    
  } 

  if (
    to.meta.requiresAuth &&
    isTokenExpired()
  ) {

    logout()

    return "/"

  }

  return true
})

export function isTokenExpired() {

  const token =
    localStorage.getItem("token")

  if (!token)
    return true

  const payload =
    parseJwt(token)

  return (
    payload.exp * 1000 <
    Date.now()
  )

}

export default router