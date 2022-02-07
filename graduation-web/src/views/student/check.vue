<template>
  <div>
    <el-table
      v-loading="listLoading"
      :data="bookOrderList"
      element-loading-text="Loading"
      border
      fit
      size="mini"
      highlight-current-ro
      style="margin-top: 50px"
    >
      <el-table-column align="center" label="编号" width="110">
        <template slot-scope="scope">
          {{ scope.row.id }}
        </template>
      </el-table-column>
      <el-table-column label="教室" align="center">
        <template slot-scope="scope">
          {{ scope.row.room }}
        </template>
      </el-table-column>
      <el-table-column label="座位" align="center" width="100">
        <template slot-scope="scope">
          {{ scope.row.seat }}
        </template>
      </el-table-column>
      <el-table-column label="预定时间" width="180" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.createTime }}</span>
        </template>
      </el-table-column>
      <el-table-column label="开始时间" width="180" align="center">
        <template slot-scope="scope">
          {{ scope.row.bookStartTime }}
        </template>
      </el-table-column>
      <el-table-column label="结束时间" width="180" align="center">
        <template slot-scope="scope">
          {{ scope.row.bookEndTime }}
        </template>
      </el-table-column>
      <el-table-column class-name="status-col" label="状态" width="120" align="center">
        <template slot-scope="scope">
          <el-tag type="success" v-if="scope.row.isFinished === 1">已完成</el-tag>
          <el-tag type="danger" v-if="scope.row.isFinished === 0">未完成</el-tag>
          <el-tag type="info" v-if="scope.row.isFinished === 3">已取消</el-tag>

        </template>
      </el-table-column>
      <el-table-column align="center" prop="created_at" label="操作" width="300">
        <template slot-scope="scope">
          <el-button size="small" type="primary" icon="el-icon-edit" @click="showTheEdit(scope.row)" v-if="scope.row.isFinished === 0">取消预订</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import { cancelOrder, getOrderByUser } from '@/api/order'

export default {
  name: 'check',
  data() {
    return {
      bookOrderList: [],
      loading: false,
      listLoading: true
    }
  },

  //监听属性 类似于data概念",
  computed: {},

  //监控data中的数据变化",
  watch: {},

  methods: {
    showTheEdit(row) {
      return new Promise((resolve, reject) => {
        cancelOrder(row.id).then(resp => {
          console.log(resp)
          this.listLoading = true
          this.getAllOrder()
          resolve(resp)
        }).then(error => {
          reject(error)
        })
      })
    },
    getAllOrder() {
      return new Promise((resolve, reject) => {
        getOrderByUser().then((resp) => {
          const { data } = resp
          console.log(data)
          this.bookOrderList = data
          this.listLoading = false
          resolve(resp)
        }).catch(error => {
          reject(error)
        })
      })
    }
  },

  //生命周期 - 创建完成（可以访问当前this实例）",数据模型已加载，方法已加载,html模板已加载,html模板未渲染
  created() {
    this.getAllOrder()
  },

  //生命周期 - 挂载完成（可以访问DOM元素）",html模板已渲染
  mounted() {
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
