import { createRouter, createWebHistory } from "vue-router"
import WorkoutList from "../components/WorkoutList.vue"
import LoginView from "../views/LoginView.vue"

const routes = [
  { path: "/", component: LoginView },
  { path: "/workouts", component: WorkoutList, 
    meta: {
      requiresAuth: true
    }}
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {

  const token =
    localStorage.getItem("token")

  if (
    to.meta.requiresAuth &&
    !token
  ) {
    next("/")
    return
  } 

  if (
    to.path === "/" &&
    token
  ) {
    next("/workouts")
    return
  }

  next()
})

export default router