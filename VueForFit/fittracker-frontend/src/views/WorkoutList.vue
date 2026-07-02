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
                class="btn btn-outline-success"
                 @click="goToProfile"
            >
                Profile
            </button>

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


    <div class="btn-group mb-4">

        <button
            class="btn"
            :class="selectedTab === 'overview'
            ? 'btn-primary'
            : 'btn-outline-primary'"
            @click="selectedTab = 'overview'"
        >
            Overview
        </button>

        <button
            class="btn"
            :class="selectedTab === 'weekly'
            ? 'btn-primary'
            : 'btn-outline-primary'"
            @click="selectedTab = 'weekly'"
        >
            Weekly
        </button>

        <button
            class="btn"
            :class="selectedTab === 'progress'
            ? 'btn-primary'
            : 'btn-outline-primary'"
            @click="selectedTab = 'progress'"
        >
            Progress
        </button>

    </div>

    
    <div v-if="selectedTab === 'overview'">
     
        <!-- <button class="btn btn-primary me-1 mb-2" @click="loadWorkouts">
        Refresh
        </button> -->
        <div class="mb-3">
            <input type="date" v-model="selectedDate" />
        </div>
        
        <!--今日kpi------START------>
        <div class="row ms-1 me-1 g-3 mb-4">

            <div class="col-md-3">
                <div class="card shadow-sm">
                <div class="card-body text-center">
                    <div class="text-muted">Exercises</div>
                    <h3>{{ exerciseCount }}</h3>
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
                    <h3>{{ totalVolume.toLocaleString() }} kg</h3>
                </div>
                </div>
            </div>

        </div>
        <!--今日kpi------END------>

        <!-- <div class="card shadow-sm mb-4">
            <div class="card-header bg-primary text-white">
                    Today Summary
            </div>

            <div class="card-body" ms-1 me-1>

                <h5>🔥 Muscle Ranking</h5>

                <div v-for="[group, data] in muscleGroupSummary" :key="group">
                <div class="d-flex justify-content-between">
                    <span>{{ group }}</span>
                    <strong>{{ data.volume.toFixed(1) }} kg</strong>
                </div>
                </div>

                <hr>

                <div>
                    <h5>📊 Quick Insight</h5>
                    <p>
                        Most trained muscle:
                        <strong>{{ topMuscle }}</strong>
                    </p>
                </div>
                
            </div>
        </div> -->


        <div class="card shadow-sm mb-4">     
            <!-- <div class="card-header bg-primary text-white">
                Muscle Chart
            </div> -->
            <h4 class="mt-4">
                Muscle Chart
            </h4>
            <div>
                <MuscleChart
                    :labels="chartLabels"
                    :data="chartData"
                />
            </div>
        </div>

        <div class="container mt-4">
            <div class="card shadow-sm mb-4">
                <!-- Header -->
                <div class="card-header bg-secondary text-white">
                    Add Workout
                </div>
                <!-- <div class="text-muted mb-2">
                    Record your training session
                </div> -->

                <div>
                    
                    <div class="d-flex justify-content-center">
                        <label class="mt-2 me-3">Workout Date</label>
                        <input
                        type="date"
                        v-model="form.workoutDate"
                        class="form-control w-25 mt-1"
                        />

                    </div>
                    
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
                <div
                    v-if="errorMessage"
                    class="alert alert-danger"
                    >
                    {{ errorMessage }}
                </div>
                <div
                    v-if="successMessage"
                    class="alert alert-success"
                    >
                    {{ successMessage }}
                </div>
            </div>
        </div>

        <div
            v-if="errorMessage2"
            class="alert alert-danger"
            >
            {{ errorMessage2 }}
        </div>
        <div
            v-if="successMessage2"
            class="alert alert-success"
            >
            {{ successMessage2 }}
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
                            <th>Date</th>
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

    
    <div v-if="selectedTab === 'weekly'">
        
        <!-- weeklyworkout kpi-->
        <div class="row g-3 mb-4">
            <!-- <h4 class="mb-3">
                Weekly Dashboard
            </h4> -->

            <div class="col-md-3">
                <div class="card shadow-sm">
                <div class="card-body text-center">
                    <div class="text-muted">Volume</div>
                    <h3>{{ weeklyVolume.toLocaleString() }} kg</h3>
                </div>
                </div>
            </div>

            <div class="col-md-3">
                <div class="card shadow-sm">
                <div class="card-body text-center">
                    <div class="text-muted">Sets</div>
                    <h3>{{ weeklySets }}</h3>
                </div>
                </div>
            </div>

            <div class="col-md-3">
                <div class="card shadow-sm">
                <div class="card-body text-center">
                    <div class="text-muted">Exercises</div>
                    <h3>{{ weeklyExerciseCount }}</h3>
                </div>
                </div>
            </div>

            <div class="col-md-3">
                <div class="card shadow-sm">
                <div class="card-body text-center">
                    <div class="text-muted">Days</div>
                    <h3>{{ trainingDays }}</h3>
                </div>
                </div>
            </div>

        </div>



        <div class="card shadow-lg mb-4">  
            <h4 class="mt-4">
                Weekly Volume Trend
            </h4>

            <WeeklyVolumeChart
                :labels="weeklyChartLabels"
                :data="weeklyChartData"
            />
        </div>

        <div class="card shadow-lg mt-4">

            <div class="card-header bg-primary text-white">
                Weekly Muscle Summary
            </div>

            <div class="card-body">

                <table class="table table-hover">

                <thead>

                <tr>
                    <th>Muscle Group</th>
                    <th>Exercises</th>
                    <th>Sets</th>
                    <th>Volume</th>
                    <th>Share</th>
                </tr>

                </thead>

                <tbody>

                <tr
                    v-for="item in weeklyMuscleSummary"
                    :key="item[0]"
                >

                    <td>
                    {{ item[0] }}
                    </td>

                    <td>
                    {{ item[1].exerciseCount }}
                    </td>

                    <td>
                    {{ item[1].sets }}
                    </td>

                    <td>
                    {{ item[1].volume.toLocaleString() }} kg
                    </td>

                    <td style="min-width:200px">

                        <div class="d-flex align-items-center">

                            <div
                            class="progress flex-grow-1 me-2"
                            style="height:20px"
                            >

                            <div
                                class="progress-bar bg-success"
                                :style="{
                                width:
                                    getVolumeShare(item[1].volume)
                                    + '%'
                                }"
                            >
                            </div>

                        </div>

                        <small>

                        {{ getVolumeShare(item[1].volume) }}%

                        </small>

                        </div>

                    </td>

                </tr>

                </tbody>

                </table>

            </div>

        </div>
        
    </div>  

    <div v-if="selectedTab === 'progress'">
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

        <!-- <div v-if="summary">

            <h4>Progress Summary</h4>

            <p>
                Current Weight:
                {{ summary.currentWeight }} kg
            </p>

            <p>
                Best PR:
                {{ summary.bestWeight }} kg
            </p>

            <p>
                Current 1RM:
                {{ summary.currentOneRm.toFixed(1) }} kg
            </p>

            <p>
                Best 1RM:
                {{ summary.bestOneRm.toFixed(1) }} kg
            </p>

        </div> -->

        <div class="row g-3 mb-4" v-if="summary">
            <!-- <h4 class="mb-3">
                Summary
            </h4> -->

            <div class="col-md-3">
                <div class="card shadow-sm">
                <div class="card-body text-center">
                    <div class="text-muted">Latest Lift</div>
                    <h3>{{  summary.currentWeight }} kg</h3>
                    {{ summary.currentWorkoutDate }}
                </div>
                </div>
            </div>

            <div class="col-md-3">
                <div class="card shadow-sm">
                <div class="card-body text-center">
                    <div class="text-muted">Best PR</div>
                    <h3>{{ summary.bestWeight }} kg</h3>
                    {{ summary.bestWeightDate }}
                </div>
                </div>
            </div>

            <div class="col-md-3">
                <div class="card shadow-sm">
                <div class="card-body text-center">
                    <div class="text-muted">Latest Estimated 1RM</div>
                    <h3>{{ summary.currentOneRm.toFixed(1) }} kg</h3>
                </div>
                </div>
            </div>

            <div class="col-md-3">
                <div class="card shadow-sm">
                <div class="card-body text-center">
                    <div class="text-muted">Best Estimated 1RM</div>
                    <h3>{{ summary.bestOneRm.toFixed(1) }} kg</h3>
                </div>
                </div>
            </div>

        </div>

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




  </div>
</template>

<script setup>
import { ref, onMounted, computed,  watch} from "vue";
import http from "../api/http.js";
import MuscleChart from '../components/MuscleChart.vue'
import { useRouter } from "vue-router"
import ProgressChart from '../components/ProgressChart.vue'
import {getRole, logout} from "../utils/auth.js"
import WeeklyVolumeChart from '../components/WeeklyVolumeChart.vue'

const router = useRouter()
const workouts = ref([]);
const exercises = ref([]);
const selectedMuscleGroup = ref("")
const selectedDate = ref("")
const editingId = ref(null)
const errorMessage = ref("")
const successMessage = ref("")
const errorMessage2 = ref("")
const successMessage2 = ref("")
const summary = ref(null)

// 'today' | 'muscle' | 'progress'
// 已被selectTab取代
//const selectedSummary = ref('today') 

//將selectedSummary改新版tab(today/weekly/progress)
const selectedTab = ref('overview')

const selectedExerciseId = ref(null)
const progressData = ref([])

const userEmail = ref(
  localStorage.getItem("email")
)

const role = ref(getRole())
const isAdmin = computed(() => role.value === "ADMIN")

const weeklyWorkouts = ref([])


const form = ref({
  exerciseId: "",
  weight: "",
  reps: "",
  sets: "",
  workoutDate: selectedDate
});

const editForm = ref({
  weight: "",
  reps: "",
  sets: ""
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
    if (
        !form.value.exerciseId ||
        !form.value.weight ||
        !form.value.reps ||
        !form.value.sets ||
        !form.value.workoutDate
    ) {
        errorMessage.value = "所有欄位皆為必填"
        return
    }

  try {

    await http.post("/workouts", form.value);

    //alert("新增成功");
    successMessage.value =
        "Workout added successfully"

    errorMessage.value = ""

    setTimeout(() => {
    successMessage.value = ""
    }, 3000)

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

    // alert("刪除成功")
    successMessage.value =
        "Workout deleted successfully"

    errorMessage.value = ""

    setTimeout(() => {
    successMessage.value = ""
    }, 3000)

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

    if (
        !editForm.value.weight ||
        !editForm.value.reps ||
        !editForm.value.sets
    ) {
        errorMessage2.value = "所有欄位皆為必填"
        return
    }

  try {

    await http.put(
      `/workouts/${id}`,
      editForm.value
    )

    //alert("修改成功")
    successMessage2.value =
        "Workout saved successfully"

    errorMessage2.value = ""

    setTimeout(() => {
    successMessage2.value = ""
    }, 3000)


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

// const exerciseCount = computed(() => {

//   return workouts.value.length

// })

const muscleGroupSummary = computed(() => {

  const summary = {}

  workouts.value.forEach(workout => {

    const group = workout.muscleGroup
    const exerciseName = workout.exerciseName

    if (!summary[group]) {
      summary[group] = {
        volume: 0,
        set: 0,
        exerciseSet: new Set()
      }
    }

    summary[group].volume += workout.weight * workout.reps * workout.sets
    summary[group].set += workout.sets

    //summary[group].exerciseCount += 1
    summary[group].exerciseSet.add(exerciseName)

  })

  //console.log(summary);

  const result = Object.entries(summary).map(([group,data]) => {
    return [
        group,
        {
            volume: data.volume,
            set: data.set,
            exerciseCount: data.exerciseSet.size
        }
    ]
  })

    return result.sort((a, b) =>
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
    set: i[1].set,
    exerciseCount: i[1].exerciseCount
  }))
)

// const topMuscle = computed(() => {
//   if (!muscleGroupSummary.value.length) return "-"

//   return muscleGroupSummary.value[0][0]
// })

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

const goToExerciseManagement = () => {

  router.push(
    "/admin/exercises"
  )

}

const loadWeeklyWorkouts = async () => {

    const response = await http.get("/workouts/week", {
        params: {
        date: selectedDate.value
        }
    })

    weeklyWorkouts.value = response.data
}

const weeklyVolume = computed(() => {

  return weeklyWorkouts.value.reduce(
    (sum, workout) =>
      sum +
      workout.weight *
      workout.reps *
      workout.sets,
    0
  )

})

const weeklySets = computed(() => {

  return weeklyWorkouts.value.reduce(
    (sum, workout) =>
      sum + workout.sets,
    0
  )

})

const weeklyExerciseCount = computed(() => {
  const exerciseNames = weeklyWorkouts.value.map(w => w.exerciseName)

  return new Set(exerciseNames).size

})

const trainingDays = computed(() => {

  return new Set(
    weeklyWorkouts.value.map(
      workout =>
        workout.workoutDate
    )
  ).size

})


const weeklyVolumeByDay = computed(() => {

  const result = {
    Mon: 0,
    Tue: 0,
    Wed: 0,
    Thu: 0,
    Fri: 0,
    Sat: 0,
    Sun: 0
  }

  weeklyWorkouts.value.forEach(workout => {

    const date = new Date(
        workout.workoutDate + "T00:00:00"
    )

    const weekday =
      ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"]
      [date.getDay()]

    result[weekday] +=
      workout.weight *
      workout.reps *
      workout.sets

  })

  return result

})

const weeklyChartLabels = computed(() => [
  "Mon",
  "Tue",
  "Wed",
  "Thu",
  "Fri",
  "Sat",
  "Sun"
])

const weeklyChartData = computed(() => [

  weeklyVolumeByDay.value.Mon,
  weeklyVolumeByDay.value.Tue,
  weeklyVolumeByDay.value.Wed,
  weeklyVolumeByDay.value.Thu,
  weeklyVolumeByDay.value.Fri,
  weeklyVolumeByDay.value.Sat,
  weeklyVolumeByDay.value.Sun

])


const weeklyMuscleSummary = computed(() => {

  const summary = {}

  weeklyWorkouts.value.forEach(workout => {

    const group = workout.muscleGroup
    const exerciseName = workout.exerciseName

    if (!summary[group]) {
      summary[group] = {
        volume: 0,
        sets: 0,
        exerciseSet: new Set()
      }
    }

    summary[group].volume += workout.weight * workout.reps * workout.sets
    summary[group].sets += workout.sets
    summary[group].exerciseSet.add(exerciseName)

  })

  const result = Object.entries(summary).map(([group,data]) => {
    return [
        group,
        {
            volume: data.volume,
            sets: data.sets,
            exerciseCount: data.exerciseSet.size
        }
    ]
  })

    return result.sort((a, b) =>
      b[1].volume - a[1].volume
    )

})

// watch(weeklyMuscleSummary, () => {
//   console.log(weeklyMuscleSummary.value)
// })

// const highestVolumeDay = computed(() => {

//   return Object.entries(
//     weeklyVolumeByDay.value
//   ).reduce(
//     (max, current) =>
//       current[1] > max[1]
//         ? current
//         : max
//   )[0]

// })

//計算不重複動作數
const exerciseCount = computed(() => {
  const exerciseNames = workouts.value.map(w => w.exerciseName)

  return new Set(exerciseNames).size

})

// watch(weeklyVolume, (newVal) => {
//   console.log("weeklyVolume updated:", newVal)
// })



const getVolumeShare = (volume) => {

  if (!weeklyVolume.value) {
    return 0
  }

  return (
    volume /
    weeklyVolume.value *
    100
  ).toFixed(1)

}

const goToProfile = () => {

  router.push(
    "/profile"
  )

}

const loadSummary = async () => {
  const response =
    await http.get(`/workouts/summary/${selectedExerciseId.value}`)

  summary.value = response.data
}


// watch(
//   selectedExerciseId,
//   loadProgress,
// )

watch(selectedExerciseId, async () => {
  await loadProgress()
  await loadSummary()
  
})

watch(selectedTab, async () => {
  await Promise.all([loadWeeklyWorkouts(), loadProgress()])
})

onMounted(() => {
  selectedDate.value = new Date().toISOString().slice(0, 10)
  loadWorkouts();
  loadExercises(); 
  loadWeeklyWorkouts()
});

watch(selectedDate, async () => {
  await loadWorkouts()
  await loadWeeklyWorkouts()
})



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