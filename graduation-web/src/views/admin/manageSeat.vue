<template>
  <div class="manageSeat">

  </div>
</template>

<script>
import { getSeatByRoomId } from '@/api/seat'

export default {
  name: 'manageSeat',
  data() {
    return {}
  },

  //监听属性 类似于data概念",
  computed: {},

  //监控data中的数据变化",
  watch: {},

  methods: {
    getSeatListByRoomId() {
      return new Promise((resolve, reject) => {
        getSeatByRoomId(13).then((resp) => {
          const { data } = resp
          console.log(data)
        })
      })
    }
  },

  //生命周期 - 创建完成（可以访问当前this实例）",数据模型已加载，方法已加载,html模板已加载,html模板未渲染
  created() {
  },

  //生命周期 - 挂载完成（可以访问DOM元素）",html模板已渲染
  mounted() {
    this.getSeatListByRoomId()
  },

  //生命周期 - 更新之后",数据模型已更新,html模板已更新
  updated() {
  },

  //生命周期 - 销毁完成"
  destroyed() {
  }
}
</script>

<style scoped>
</style>
