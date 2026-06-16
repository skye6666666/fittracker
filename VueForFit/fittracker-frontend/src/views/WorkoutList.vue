<template>
  <div class="container mt-4">
    <!--<h1 class="text-center mb-4">My Workouts</h1>-->
    <div
     class="d-flex justify-content-between align-items-center mb-4"
     >

        <div>
            <h1 class="mb-0">My Workouts</h1>

            <small class="text-muted">
            Welcome, {{ userEmail }} ({{ role }})
            </small>
        </div>

        <div class="d-flex gap-2">
            <button 
                class="btn btn-outline-primary"
                v-if="isAdmin" @click="goToExerciseManagement"
            >
                Exercise Management
            </button>

            <button
                class="btn btn-outline-danger"
                @click="logout"
            >
                Logout
            </button>
        </div>

    </div>

    <button class="btn btn-primary me-1 mb-2" @click="loadWorkouts">
      Refresh
    </button>

    <input type="date" v-model="selectedDate" />

    <!--今日kpi暫定區------START------>
    <div class="row g-3 mb-4">

        <div class="col-md-3">
            <div class="card shadow-sm">
            <div class="card-body text-center">
                <div class="text-muted">Workouts</div>
                <h3>{{ workouts.length }}</h3>
            </div>
            </div>
        </div>

        <div class="col-md-3">
            <div class="card shadow-sm">
            <div class="card-body text-center">
                <div class="text-muted">Sets</div>
                <h3>{{ totalSets }}</h3>
            </div>
            </div>
        </div>

        <div class="col-md-3">
            <div class="card shadow-sm">
            <div class="card-body text-center">
                <div class="text-muted">Reps</div>
                <h3>{{ totalReps }}</h3>
            </div>
            </div>
        </div>

        <div class="col-md-3">
            <div class="card shadow-sm">
            <div class="card-body text-center">
                <div class="text-muted">Volume</div>
                <h3>{{ totalVolume }} kg</h3>
            </div>
            </div>
        </div>

    </div>

    <!--今日kpi暫定區------END-------->

    <!--舊按鈕-->
    <!-- <div style="margin-bottom: 10px;">
        <button class="btn btn-primary" @click="selectedSummary = 'today'">
            Today Summary
        </button>
        
        <button class="btn btn-primary" @click="selectedSummary = 'muscle'">
            Muscle Group
        </button>
    </div> -->

    <div class="btn-group mb-3">
        <button
            class="btn"
            :class="selectedSummary === 'today' ? 'btn-primary' : 'btn-outline-primary'"
            @click="selectedSummary = 'today'"
        >
            Today Summary
        </button>

        <button
            class="btn"
            :class="selectedSummary === 'muscle' ? 'btn-primary' : 'btn-outline-primary'"
            @click="selectedSummary = 'muscle'"
        >
            Muscle Group
        </button>

        <button
            class="btn"
            :class="selectedSummary === 'progress' ? 'btn-primary' : 'btn-outline-primary'"
            @click="selectedSummary = 'progress'"
        >
            Exercise Progress
        </button>
    </div>

    
    <div class="card shadow-sm mb-4" v-if="selectedSummary === 'today'">
        <div class="card-header bg-info text-dark">
            Insight Panel
        </div>

        <div class="card-body">

            <h5>🔥 Muscle Ranking</h5>

            <div v-for="[group, data] in muscleGroupSummary" :key="group">
            <div class="d-flex justify-content-between">
                <span>{{ group }}</span>
                <strong>{{ data.volume }} kg</strong>
            </div>
            </div>

            <hr>

            <h5>📊 Quick Insight</h5>

            <p>
            Most trained muscle:
            <strong>{{ topMuscle }}</strong>
            </p>

        </div>
    </div>

    
    <div v-if="selectedSummary === 'muscle'">
        <h3>Muscle Group Summary</h3>
        
        <MuscleChart
            :labels="chartLabels"
            :data="chartData"
        />
        <!-- <div
            v-for="[group, data] in muscleGroupSummary"
            :key="group"
        >
        <h4>{{ group }}</h4>
        <p>
            總訓練量：{{ data.volume }}kg
        </p>
        <p>
            動作數：{{ data.exerciseCount }}
        </p>
        <hr>
        </div> -->
    </div>  

    <div v-if="selectedSummary === 'progress'">

        <h3>Exercise Progress</h3>

        <select
        class="form-select mb-3"
        v-model="selectedExerciseId"
        >
            <option :value="null">
                Select Exercise
            </option>

            <option
                v-for="exercise in exercises"
                :key="exercise.id"
                :value="exercise.id"
            >
                {{ exercise.description }}
            </option>

        </select>

        <ProgressChart
        :data="progressData"
        />
    </div>

    
    
    <!--add-workout卡片化之前的版本-->
    <!-- <div class="add-workout">
        <h3>Add Workout</h3>

        <div class="row mb-3">
            <div class="col-md-4">
                <select class="form-select" v-model="selectedMuscleGroup">
                    <option value="">請選擇肌群</option>

                    <option
                        v-for="group in muscleGroups"
                        :key="group"
                        :value="group"
                    >
                        {{ group }}
                    </option>
                </select>
            </div>
            <div class="col-md-4">
                <select class="form-select" v-model.number="form.exerciseId">
                    <option value="null"> 
                        {{
                            selectedMuscleGroup
                                ? "請選擇動作"
                                : "請先選擇肌群"
                        }}
                    </option>

                    <option
                        v-for="exercise in filteredExercises"
                        :key="exercise.id"
                        :value="exercise.id"
                    >
                        {{ exercise.description }}
                    </option>
                </select>
            </div>

            <div class="col-md-4 d-flex align-items-center">
                <span
                    v-if="selectedExercise"
                    class="badge bg-secondary fs-6"
                >
                    <strong>英文名稱：</strong>
                    {{ selectedExercise.name }}
                </span>
            </div>
        </div>

        <div class="row mb-3">
            <div class="col-md-4">
                <input class="form-control" v-model.number="form.weight" placeholder="Weight(kg)" />
            </div>
            <div class="col-md-4">
                <input class="form-control" v-model.number="form.reps" placeholder="Reps" />
            </div>
            <div class="col-md-4">
                <input class="form-control" v-model.number="form.sets" placeholder="Sets" />
            </div>
        </div>
        <button class="btn btn-success" @click="createWorkout">Add</button>
    </div> -->


    <div class="container mt-4">
        <div class="card shadow-sm mb-4">
            <!-- Header -->
            <div class="card-header bg-secondary text-white">
                Add Workout
            </div>
            <div class="text-muted mb-2">
                Record your training session
            </div>

            <div>
                <label>Workout Date</label>

                <input
                    type="date"
                    v-model="form.workoutDate"
                    class="form-control"
                />
            </div>

            <div class="card-body">

                <!-- Muscle Group + Exercise + Badge -->
                <div class="row mb-3 g-3">

                    <!-- Muscle Group -->
                    <div class="col-md-4">
                        <label class="form-label">Muscle Group</label>

                        <select class="form-select" v-model="selectedMuscleGroup">
                            <option value="">請選擇肌群</option>

                            <option
                                v-for="group in muscleGroups"
                                :key="group"
                                :value="group"
                                >
                                {{ group }}
                            </option>
                        </select>
                    </div>

                    <!-- Exercise -->
                    <div class="col-md-4">
                        <label class="form-label">Exercise</label>

                        <select class="form-select" v-model.number="form.exerciseId">
                            <option value="null">
                                {{
                                    selectedMuscleGroup
                                    ? "請選擇動作"
                                    : "請先選擇肌群"
                                }}
                            </option>

                            <option
                                v-for="exercise in filteredExercises"
                                :key="exercise.id"
                                :value="exercise.id"
                                >
                                {{ exercise.description }}
                            </option>
                        </select>
                    </div>

                    <!-- Selected Exercise Badge -->
                    <div class="col-md-4 d-flex align-items-end">
                        <span
                            v-if="selectedExercise"
                            class="badge bg-secondary fs-6 p-2"
                        >
                            <strong>英文名稱：</strong>
                            {{ selectedExercise.name }}
                        </span>
                    </div>

                </div>

                <!-- Input Row -->
                <div class="row mb-3 g-3">
                    <div class="col-md-4">
                        <label class="form-label">Weight (kg)</label>
                        <input
                            class="form-control"
                            v-model.number="form.weight"
                            type="number"
                            placeholder="Enter weight"
                        />
                    </div>

                    <div class="col-md-4">
                        <label class="form-label">Reps</label>
                        <input
                            class="form-control"
                            v-model.number="form.reps"
                            type="number"
                            placeholder="Enter reps"
                        />
                    </div>

                    <div class="col-md-4">
                        <label class="form-label">Sets</label>
                        <input
                            class="form-control"
                            v-model.number="form.sets"
                            type="number"
                            placeholder="Enter sets"
                        />
                    </div>
                </div>

                <!-- Button -->
                <div class="d-grid">
                    <button
                    class="btn btn-success btn-lg"
                    @click="createWorkout"
                    >
                    Add
                    </button>
                </div>

            </div>
        </div>
    </div>

    
    <div class="card shadow-sm">
        <div class="card-body p-0">

            <table class="table table-hover align-middle mb-0">
            <thead>
            <tr>
                <th>Exercise</th>
                <th>Muscle Group</th>
                <th>Weight(kg)</th>
                <th>Reps</th>
                <th>Sets</th>
                <th>Date&Time</th>
                <th>Action</th>
            </tr>
            </thead>

            <tbody>
            <tr
                v-for="workout in workouts"
                :key="workout.id"
            >
                <td>{{ workout.exerciseName }}</td>
                <td>
                    <span class="badge bg-primary">
                        {{ workout.muscleGroup }}
                    </span>
                </td>
                                                                    
                <td v-if="editingId !== workout.id">
                    {{ workout.weight }}
                </td>
                <td v-else>
                    <input v-model.number="editForm.weight" class="form-control form-control-sm" style="width: 60px;"/>
                </td>

                <td v-if="editingId !== workout.id" >
                    {{ workout.reps }}
                </td>
                <td v-else>
                    <input v-model.number="editForm.reps" class="form-control form-control-sm" style="width: 60px;"/>
                </td>

                <td v-if="editingId !== workout.id">
                    {{ workout.sets }}
                </td>
                <td v-else>
                    <input v-model.number="editForm.sets" class="form-control form-control-sm" style="width: 60px;"/>
                </td>
                
                <td>
                    {{ workout.workoutDate }}
                </td>


                <td>
                    <div class="d-flex gap-1">
                        <template v-if="editingId !== workout.id">
                            <button class="btn btn-warning" @click="startEdit(workout)">
                                Edit
                            </button>

                            <button class="btn btn-danger" @click="deleteWorkout(workout.id)">
                                Delete
                            </button>
                        </template>

                        <template v-else>
                            <button class="btn btn-success" @click="saveEdit(workout.id)">
                                Save
                            </button>

                            <button class="btn btn-secondary" @click="cancelEdit">
                                Cancel
                            </button>
                        </template>
                    </div>
                </td>
            </tr>
            </tbody>
            </table>
        </div>
    </div>

  </div>
</template>

<script setup>
import { ref, onMounted, computed,  watch} from "vue";
import http from "../api/http.js";
import MuscleChart from '../components/MuscleChart.vue'
import { useRouter } from "vue-router"
import ProgressChart from '../components/ProgressChart.vue'
import {getRole, logout} from "../utils/auth.js"

const router = useRouter()
const workouts = ref([]);
const exercises = ref([]);
const selectedMuscleGroup = ref("")
const selectedDate = ref("")
const editingId = ref(null)
const selectedSummary = ref('today') 
// 'today' | 'muscle'

const selectedExerciseId = ref(null)
const progressData = ref([])

const userEmail = ref(
  localStorage.getItem("email")
)

// const role = ref(
//   localStorage.getItem("role")
// )
const role = ref(getRole())
const isAdmin = computed(() => role.value === "ADMIN")


const form = ref({
  exerciseId: null,
  weight: null,
  reps: null,
  sets: null,
  workoutDate: null
});

const editForm = ref({
  weight: null,
  reps: null,
  sets: null
})


const loadExercises = async () => {

  const response =
      await http.get("/exercises")

  exercises.value = response.data
  console.log(response.data);

}

const muscleGroups = computed(() => {

  return [
    ...new Set(
      exercises.value.map(
        e => e.muscleGroup
      )
    )
  ]

})

const filteredExercises = computed(() => {

  return exercises.value.filter(
      e =>
          e.muscleGroup ===
          selectedMuscleGroup.value
  )

})

const selectedExercise = computed(() => {
  return exercises.value.find(
    e => e.id === form.value.exerciseId
  )
})


const loadWorkouts = async () => {

  try {

    const response = await http.get("/workouts", {
      params: {
        date: selectedDate.value || null
      }
    });

    workouts.value = response.data;

    console.log(response.data);

  } catch (error) {

    console.error(error);

    //alert("讀取失敗");

  }
};

const createWorkout = async () => {
console.log(form.value)

  try {
    await http.post("/workouts", form.value);

    alert("新增成功");

    await loadWorkouts(); // refresh list

    // 清空表單
    form.value = {
      exerciseId: null,
      weight: null,
      reps: null,
      sets: null,
      workoutDate: selectedDate
    };

    //將肌群一並清空
    selectedMuscleGroup.value = "";

  } catch (error) {
    console.error(error);
    alert("新增失敗");
  }
};

const deleteWorkout = async (id) => {

  const confirmed =
      confirm("確定刪除嗎？")

  if (!confirmed) {
    return
  }

  try {

    await http.delete(
        `/workouts/${id}`
    )

    alert("刪除成功")

    await loadWorkouts()

  } catch(error) {

    console.error(error)

    alert("刪除失敗")

  }
}

const startEdit = (workout) => {

  editingId.value = workout.id

  editForm.value = {
    weight: workout.weight,
    reps: workout.reps,
    sets: workout.sets
  }

}

const saveEdit = async (id) => {

  try {

    await http.put(
      `/workouts/${id}`,
      editForm.value
    )

    alert("修改成功")

    editingId.value = null

    editForm.value = {
         weight: null,
        reps: null,
        sets: null
    }

    await loadWorkouts()

  } catch(error) {

    console.error(error)

    alert("修改失敗")

  }

}

const cancelEdit = () => {
  editingId.value = null

  editForm.value = {
    weight: null,
    reps: null,
    sets: null
  }
}

const totalVolume = computed(() => {

  return workouts.value.reduce(
    (sum, workout) =>
      sum +
      workout.weight *
      workout.reps *
      workout.sets,
    0
  )

})

const totalSets = computed(() => {

  return workouts.value.reduce(
    (sum, workout) =>
      sum + workout.sets,
    0
  )

})

const totalReps = computed(() => {

  return workouts.value.reduce(
    (sum, workout) =>
      sum + workout.reps * workout.sets,
    0
  )

})

const exerciseCount = computed(() => {

  return workouts.value.length

})

const muscleGroupSummary = computed(() => {

  const summary = {}

  workouts.value.forEach(workout => {

    const group = workout.muscleGroup

    if (!summary[group]) {

      summary[group] = {
        volume: 0,
        exerciseCount: 0
      }

    }

    summary[group].volume +=
      workout.weight *
      workout.reps *
      workout.sets

    summary[group].exerciseCount += 1

  })

    return Object.entries(summary)
    .sort((a, b) =>
      b[1].volume - a[1].volume
    )

})

const chartLabels = computed(() =>
  muscleGroupSummary.value.map(i => i[0])
)

const chartData = computed(() =>
  muscleGroupSummary.value.map(i => ({
    group: i[0],
    volume: i[1].volume,
    exerciseCount: i[1].exerciseCount
  }))
)

const topMuscle = computed(() => {
  if (!muscleGroupSummary.value.length) return "-"

  return muscleGroupSummary.value[0][0]
})

const loadProgress = async () => {

  if (!selectedExerciseId.value)
    return

  const response =
    await http.get(
      "/workouts/progress",
      {
        params: {
          exerciseId:
            selectedExerciseId.value
        }
      }
    )

  progressData.value = response.data
}

// const createExercise = () => {

//   router.push(
//     "/admin/create-exercise"
//   )

// }

const goToExerciseManagement = () => {

  router.push(
    "/admin/exercises"
  )

}

watch(
  selectedExerciseId,
  loadProgress
)



onMounted(() => {
  selectedDate.value = new Date().toISOString().slice(0, 10)
  loadWorkouts();
  loadExercises(); 
});

watch(selectedDate, () => {
  loadWorkouts();
});

// const logout = () => {

//   localStorage.removeItem("token")
//   localStorage.removeItem("email")
//   localStorage.removeItem("role")

//   router.push("/")
// }



</script>

<!-- <style>
.add-workout select,
.add-workout input,
.add-workout button {
    margin-right: 8px;
}

.fade {
  transition: all 0.2s ease;
}
</style> -->