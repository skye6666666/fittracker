<template>
  <div>
    <h1>My Workouts</h1>

    <button @click="loadWorkouts">
      Refresh
    </button>

    <input type="date" v-model="selectedDate" />


    <div>
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
            <option value="">請選擇動作</option>

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

    <table border="1">
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

const workouts = ref([]);
const exercises = ref([]);
const selectedMuscleGroup = ref("")
const selectedDate = ref("")
const editingId = ref(null)

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

onMounted(() => {
  selectedDate.value = new Date().toISOString().slice(0, 10)
  loadWorkouts();
  loadExercises();
});

watch(selectedDate, () => {
  loadWorkouts();
});
</script>