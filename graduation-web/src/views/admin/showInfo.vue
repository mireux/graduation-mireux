<template>
  <div class="showInfo">
    <el-row>
      <MixChart height="600px" width="100%"></MixChart>
    </el-row>
  </div>
</template>

<script>
import MixChart from '@/components/charts/MixChart'
import LineMarker from '@/components/charts/LineMarker'
import Keyboard from '@/components/charts/Keyboard'

export default {
  name: 'showInfo.vue',
  components: { MixChart, LineMarker, Keyboard },
  data() {
    return {}
  },

  computed: {},

  watch: {},

  methods: {},

  created() {
  },

  mounted() {
  },

  updated() {
  },

  destroyed() {
  }
}
</script>

<style scoped>
</style>
