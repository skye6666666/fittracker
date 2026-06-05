import { createRouter, createWebHistory } from "vue-router"
import WorkoutList from "../components/WorkoutList.vue"
import LoginView from "../views/LoginView.vue"

const routes = [
  { path: "/", component: LoginView },
  { path: "/workouts", component: WorkoutList }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})


export default router