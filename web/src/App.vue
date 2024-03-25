<template>
  <div id="app">
    <router-view />
    <Lock v-if="isLock == 'true'" />
  </div>
</template>

<script>
import lockMixin from "@/views/mixins/lock.vue"
export default {
  name: 'App'
}
</script>
