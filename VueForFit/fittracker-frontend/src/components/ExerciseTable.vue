<template>
  <table class="table table-hover">
      <thead>
        <tr>
          <th>Description</th>
          <th>Name</th>
          <th>Muscle Group</th>
          <th>Action</th>
        </tr>
      </thead>

      <tbody>

        <tr
          v-for="exercise in exercises"
          :key="exercise.id"
        >

          <td v-if="editingId !== exercise.id">
            {{ exercise.description }}
          </td>
          <td v-else>
            <input class="form-control" v-model="editForm.description" />
          </td>

          <td v-if="editingId !== exercise.id">
            {{ exercise.name }}
          </td>
          <td v-else>
            <input class="form-control" v-model="editForm.name" />
          </td>

          <td v-if="editingId !== exercise.id">
            {{ exercise.muscleGroup }}
          </td>
          <td v-else>
            <input class="form-control" v-model="editForm.muscleGroup" />
          </td>

          <td class="text-center">
            <div class="d-flex justify-content-center gap-1">
                <template v-if="editingId !== exercise.id">
                    <button class="btn btn-warning" @click="emit('start-edit', exercise)">
                        Edit
                    </button>

                    <button class="btn btn-danger" @click="emit('delete-exercise', exercise.id)">
                        Delete
                    </button>
                </template>

                <template v-else>
                    <button class="btn btn-success" @click="emit('save-edit', exercise.id)">
                        Save
                    </button>

                    <button class="btn btn-secondary" @click="emit('cancel-edit')">
                        Cancel
                    </button>
                </template>
            </div>
          </td>

        </tr>

        <tr v-if="exercises.length === 0">
            <td colspan="4">No exercises found</td>
        </tr>

      </tbody>

    </table>
</template>

<script setup>
const props = defineProps({
  exercises: Array,
  editingId: Number,
  editForm: Object
})

const emit = defineEmits([
  'start-edit',
  'save-edit',
  'cancel-edit',
  'delete-exercise'
])
</script>