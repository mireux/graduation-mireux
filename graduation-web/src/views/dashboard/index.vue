<template>
  <div class="dashboard-container">
    <div class="dashboard-text">欢迎{{name}}图书馆自习室预约系统</div>
    <div class="dashboard-text">您的身份是：{{role === 'Admin' ? '管理员': '学生'}}</div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'

export default {
  name: 'Dashboard',
  computed: {
    ...mapGetters([
      'name',
      'role'
    ])
  }
}
</script>

<style lang="scss" scoped>
.dashboard {
  &-container {
    margin: 30px;
  }
  &-text {
    font-size: 30px;
    line-height: 46px;
    text-align: center;
  }
}
</style>
