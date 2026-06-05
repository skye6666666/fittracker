<template>
  <div>
    <h1>My Workouts</h1>

    <button @click="loadWorkouts">
      Refresh
    </button>

    <input type="date" v-model="selectedDate" />

    <div style="margin-bottom: 10px;">
        <button @click="selectedSummary = 'today'">
            Today Summary
        </button>
        
        <button @click="selectedSummary = 'muscle'">
            Muscle Group
        </button>
    </div>

    <div class="summary" v-if="selectedSummary === 'today'">
        <h3>Today Summary</h3>
        <p>
            總訓練量：{{ totalVolume.toFixed(1) }} kg
        </p>
        <p>
            總組數：{{ totalSets }}</p>
        <p>
            總次數：{{ totalReps }}
        </p>
        <p>
            動作數：{{ exerciseCount }}
        </p>
    </div>

    
    <div v-if="selectedSummary === 'muscle'">
        <h3>Muscle Group Summary</h3>
        
        <MuscleChart
            :labels="chartLabels"
            :data="chartData"
        />
        <div
            v-for="[group, data] in muscleGroupSummary"
            :key="group"
        >
        <h4>{{ group }}</h4>
        <p>
            總訓練量：{{ data.volume.toFixed(1) }}kg
        </p>
        <p>
            動作數：{{ data.exerciseCount }}
        </p>
        <hr>
        </div>
    </div>  
    

    <div class="add-workout">
        <h3>Add Workout</h3>
        <select v-model="selectedMuscleGroup">
            <option value="">請選擇肌群</option>

            <option
                v-for="group in muscleGroups"
                :key="group"
                :value="group"
            >
                {{ group }}
            </option>
        </select>
        <select v-model.number="form.exerciseId">
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

        <div v-if="selectedExercise">
            <p>
                英文名稱：
                {{ selectedExercise.name }}
            </p>
        </div>

        <input v-model.number="form.weight" placeholder="Weight(kg)" />
        <input v-model.number="form.reps" placeholder="Reps" />
        <input v-model.number="form.sets" placeholder="Sets" />
        <button @click="createWorkout">Add</button>
    </div>

    <table border="1" style="margin: 20px auto;">
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
        <td>{{ workout.muscleGroup }}</td>
                                                             
        <td v-if="editingId !== workout.id">
            {{ workout.weight }}
        </td>
        <td v-else>
            <input v-model.number="editForm.weight" />
        </td>

        <td v-if="editingId !== workout.id">
            {{ workout.reps }}
        </td>
        <td v-else>
            <input v-model.number="editForm.reps" />
        </td>

        <td v-if="editingId !== workout.id">
            {{ workout.sets }}
        </td>
        <td v-else>
            <input v-model.number="editForm.sets" />
        </td>
           
        <td>
             {{ workout.workoutDate
                ? workout.workoutDate.replace("T", " ")
                : "" }}
        </td>


        <td>
            <template v-if="editingId !== workout.id">
                <button @click="startEdit(workout)">
                    Edit
                </button>

                <button @click="deleteWorkout(workout.id)">
                    Delete
                </button>
            </template>

            <template v-else>
                <button @click="saveEdit(workout.id)">
                    Save
                </button>

                <button @click="cancelEdit">
                    Cancel
                </button>
            </template>
        </td>
      </tr>
      </tbody>
    </table>

  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import http from "../api/http";
import { computed } from "vue";
import { watch } from "vue";
import MuscleChart from './MuscleChart.vue'

const workouts = ref([]);
const exercises = ref([]);
const selectedMuscleGroup = ref("")
const selectedDate = ref("")
const editingId = ref(null)
const selectedSummary = ref('today') 
// 'today' | 'muscle'

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

    //const response = await http.get("/workouts");
    const response = await http.get("/workouts", {
      params: {
        date: selectedDate.value || null
      }
    });

    workouts.value = response.data;

    console.log(response.data);

  } catch (error) {

    console.error(error);

    alert("讀取失敗");

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
      workoutDate: null
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
  muscleGroupSummary.value.map(i => i[1].volume)
)



onMounted(() => {
  selectedDate.value = new Date().toISOString().slice(0, 10)
  loadWorkouts();
  loadExercises();
});

watch(selectedDate, () => {
  loadWorkouts();
});
</script>

<style>
.add-workout select,
.add-workout input,
.add-workout button {
    margin-right: 8px;
}

.fade {
  transition: all 0.2s ease;
}
</style>