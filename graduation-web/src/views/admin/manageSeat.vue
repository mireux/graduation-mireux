<template>
  <div class="manageSeat">
    <el-select v-model="selectValue" class="selector" placeholder="请选择" @change="show()">
      <el-option
        v-for="item in roomList"
        :key="item.id"
        :label="item.room"
        :value="item.id"
      >
      </el-option>
    </el-select>
    <el-table
      v-loading="listLoading"
      :data="seatList"
      border
      element-loading-text="Loading"
      fit
      highlight-current-row
      max-height="550px"
      size="mini"
      style="margin-top: 20px"
    >
      <el-table-column align="center" label="编号" width="110">
        <template slot-scope="scope">
          {{ scope.row.id }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="座位号">
        <template slot-scope="scope">
          {{ scope.row.seat }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="教室" width="100">
        <template slot-scope="scope">
          {{ scope.row.roomName }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="创建人" width="180">
        <template slot-scope="scope">
          <span>{{ scope.row.createBy }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="创建时间" width="180">
        <template slot-scope="scope">
          <span>{{ scope.row.createTime }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="更新人" width="180">
        <template slot-scope="scope">
          {{ scope.row.updateBy }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="更新时间" width="180">
        <template slot-scope="scope">
          {{ scope.row.updateTime }}
        </template>
      </el-table-column>
      <el-table-column align="center" class-name="status-col" label="是否被选择" width="120"
      >
        <template slot-scope="scope">
          <el-tag v-if="!scope.row.isChoosed" type="success">未占用</el-tag>
          <el-tag v-if="scope.row.isChoosed" type="danger">已占用</el-tag>
        </template>
      </el-table-column>
      <el-table-column align="center" class-name="status-col" label="座位状态" width="120"
      >
        <template slot-scope="scope">
          <el-tag v-if="!scope.row.delFlag" type="success">启用</el-tag>
          <el-tag v-if="scope.row.delFlag" type="danger">停用</el-tag>
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作" prop="created_at" width="300">
        <template slot-scope="scope">
          <el-button v-if="!scope.row.isChoosed" icon="el-icon-edit" size="small" type="danger"
                     @click="showTheEdit(scope.row.id)"
          >锁座
          </el-button>
          <el-button v-if="scope.row.isChoosed" icon="el-icon-edit" size="small" type="primary"
                     @click="showTheEdit(scope.row.id)"
          >解座
          </el-button>
          <el-button v-if="!scope.row.delFlag" icon="el-icon-delete" size="small" type="danger"
                     @click="disabledTheSeat(scope.row.id)"
          >停用
          </el-button>
          <el-button v-if="scope.row.delFlag" icon="el-icon-setting" size="small" type="success"
                     @click="disabledTheSeat(scope.row.id)"
          >启用
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import { changeSeatStatus, getSeatByRoomId, lockSeat } from '@/api/seat'
import { getRoomList } from '@/api/room'

export default {
  name: 'manageSeat',
  data() {
    return {
      roomList: [],
      selectValue: '',
      seatList: [],
      listLoading: false
    }
  },

  //监听属性 类似于data概念",
  computed: {},

  //监控data中的数据变化",
  watch: {},

  methods: {
    showTheEdit(row) {
      return new Promise((resolve, reject) => {
        lockSeat(row).then(() => {
          this.getSeatListByRoomId(this.selectValue)
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },
    disabledTheSeat(row) {
      return new Promise((resolve, reject) => {
        changeSeatStatus(row).then(() => {
          this.getSeatListByRoomId(this.selectValue)
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },
    show() {
      //  通过roomId获取对应的教室座位 放入SeatList
      this.listLoading = true
      return new Promise((resolve, reject) => {
        getSeatByRoomId(this.selectValue).then(resp => {
          const { data } = resp
          this.seatList = data
          this.listLoading = false
          console.log(this.seatList)
          resolve(resp)
        }).catch(error => {
          reject(error)
        })
      })
    },
    getSeatListByRoomId(id) {
      return new Promise((resolve, reject) => {
        getSeatByRoomId(id).then((resp) => {
          const { data } = resp
          this.seatList = data
          resolve(resp)
        }).catch(error => {
          reject(error)
        })
      })
    },
    getAllRoom() {
      this.isActive = true
      return new Promise((resolve, reject) => {
        getRoomList(true).then(resp => {
          const { data } = resp
          this.roomList = data
          console.log(data)
          resolve(resp)
          setInterval(() => {
            this.isActive = false
          }, 1000)
          resolve(resp)
        }).catch(error => {
          reject(error)
        })
      })
    }
  },

  //生命周期 - 创建完成（可以访问当前this实例）",数据模型已加载，方法已加载,html模板已加载,html模板未渲染
  created() {
    this.getAllRoom()
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
.selector {
  margin-top: 20px;
  margin-left: 20px;
}
</style>
