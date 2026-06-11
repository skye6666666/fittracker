import { createRouter, createWebHistory } from "vue-router"
import WorkoutList from "../components/WorkoutList.vue"
import LoginView from "../views/LoginView.vue"
import { getRole } from "../utils/auth"
import CreateExerciseView from "../views/CreateExerciseView.vue"
//import ExerciseManagementView from "../views/ExerciseManagementView.vue"

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
      path: "/admin/create-exercise",
      component: CreateExerciseView,
      meta: {
        requiresAdmin: true
      }
    }
    // ,
    // {
    //   path: "/admin/exercises",
    //   component: ExerciseManagementView,
    //   meta: {
    //     requiresAdmin: true
    //   }
    // }
  ]
})

router.beforeEach((to, from, next) => {

  const token =
    localStorage.getItem("token")

  const role = getRole()

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

  if (
    to.meta.requiresAdmin &&
    role !== "ADMIN"
  ) {
    next("/workouts")
  } else {
    next()
  }

  next()
})

export default router