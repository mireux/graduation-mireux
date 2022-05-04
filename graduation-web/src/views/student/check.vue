<template xmlns="http://www.w3.org/1999/html">
  <div>
    您的总共学习时间为：{{studyReportList.totalStudyTime}} 分钟
    <br/> 您的上月学习时间为：{{studyReportList.lastMonthStudyTime}} 分钟
    <br/>您的总共预约次数为：{{studyReportList.bookTotalTimes}} 次
    <br/>您上周得预约次数为：{{studyReportList.bookWeekTimes}} 次
    <el-table
      v-loading="listLoading"
      :data="bookOrderList"
      border
      element-loading-text="Loading"
      fit
      highlight-current-ro
      size="mini"
      style="margin-top: 50px"
    >
      <el-table-column align="center" label="编号" width="110">
        <template slot-scope="scope">
          {{ scope.row.id }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="教室">
        <template slot-scope="scope">
          {{ scope.row.room }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="座位" width="100">
        <template slot-scope="scope">
          {{ scope.row.seat }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="预定时间" width="180">
        <template slot-scope="scope">
          <span>{{ scope.row.createTime }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="开始时间" width="180">
        <template slot-scope="scope">
          {{ scope.row.bookStartTime }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="结束时间" width="180">
        <template slot-scope="scope">
          {{ scope.row.bookEndTime }}
        </template>
      </el-table-column>
      <el-table-column align="center" class-name="status-col" label="状态" width="120">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.isFinished === 1" type="success">已完成</el-tag>
          <el-tag v-if="scope.row.isFinished === 0" type="danger">未完成</el-tag>
          <el-tag v-if="scope.row.isFinished === 3" type="info">已取消</el-tag>

        </template>
      </el-table-column>
      <el-table-column align="center" label="操作" prop="created_at" width="300">
        <template slot-scope="scope">
          <el-button v-if="scope.row.isFinished === 0" icon="el-icon-edit" size="small" type="primary"
                     @click="showTheEdit(scope.row)"
          >取消预订
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import { cancelOrder, getOrderByUser } from '@/api/order'
import { getStudyReport } from '@/api/showInfo'

export default {
  name: 'check',
  data() {
    return {
      bookOrderList: [],
      loading: false,
      listLoading: true,
      studyReportList: []
    }
  },

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
          this.bookOrderList = data
          this.listLoading = false
          resolve(resp)
        }).catch(error => {
          reject(error)
        })
      })
    },
    getStudyReport() {
      return new Promise(((resolve, reject) => {
        getStudyReport().then(resp => {
          const { data } = resp
          this.studyReportList = data
          resolve(resp)
        }).catch(error => {
          reject(error)
        })
      }))
    }
  },

  //生命周期 - 创建完成（可以访问当前this实例）",数据模型已加载，方法已加载,html模板已加载,html模板未渲染
  created() {
    this.getAllOrder()
    this.getStudyReport()
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
