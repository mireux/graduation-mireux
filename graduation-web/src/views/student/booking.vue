<template>
  <div class="container">
    <el-select v-model="selectValue" placeholder="请选择" class="selector" @change="show()">
      <el-option
        v-for="item in roomList"
        :key="item.id"
        :label="item.room"
        :value="item.id"
      >
      </el-option>
    </el-select>

      <el-radio-group v-model="radio1">
<!--        <transition name="el-fade-in-linear">-->
        <el-radio-button
          v-for="(item, j) in SeatList"
          :key="j"
          class="user-radio"
          :label="item.seat"
          :disabled="item.isChoosed === true"
          @click.native.prevent="open(item.isChoosed, item.seat, item.id)"
        />
        <!-- @click.native.prevent="open" -->
<!--        </transition>-->
      </el-radio-group>

    <el-dialog
      title="预约时间"
      :visible.sync="dialogFormVisible"
      :close-on-click-modal="false"
    >
      <el-form :model="form">
        <el-row>
          <el-form-item label="预约日期">
            <el-col :span="7">
              <!-- readonly -->
              <el-date-picker
                v-model="date1"
                type="date"
                placeholder="选择日期"
                style="width: 100%"
                format="yyyy 年 MM 月 dd 日"
                value-format="yyyy-MM-dd"
                :picker-options="pickerOptions"
                @change="getNowTime()"
              />
            </el-col>
          </el-form-item>
        </el-row>
        <el-row>
          <el-form-item label="预约时间">
            <el-col :span="5">
              <el-time-select
                v-model="DataForm.startTime"
                placeholder="起始时间"
                :picker-options="{
                  start: nowTime,
                  step: '00:05',
                  end: '22:30',
                }"
                time-arrow-control
              />
            </el-col>
            <el-col class="line" :span="1" style="margin-left: 50px">--</el-col>
            <el-col :span="5">
              <el-time-select
                v-model="DataForm.endTime"
                placeholder="结束时间"
                :picker-options="{
                  start: nowTime,
                  step: '00:05',
                  end: '22:30',
                  minTime: DataForm.startTime,
                }"
                time-arrow-control
              />
            </el-col>
          </el-form-item>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button
          type="primary"
          @click="dialogFormVisibled()"
        >确 定
        </el-button>
      </div>
    </el-dialog>
    <!-- <div>{{ radio1 }}</div> -->
  </div>
</template>
<script>
import { bookTheSeat, getRoomList } from '@/api/room'
import { getSeatByRoomId } from '@/api/seat'

export default {
  data() {
    return {
      isActive: false,
      selectValue: '',
      roomList: [],
      dialogFormVisible: false,
      formLabelWidth: '50px',
      radio1: '',
      IsChoosed: false,
      SeatList: [],
      form: {},
      nowTime: '',
      date1: '',
      DataForm: {
        startTime: '',
        endTime: '',
        finish: 0,
        roomId: '',
        seatId: ''
      },
      pickerOptions: {
        disabledDate(time) {
          return (
            time.getTime() < Date.now() - 3600 * 1000 * 24 ||
            time.getTime() > Date.now() + 3600 * 1000 * 24
          )
        }
      }
    }
  },
  //监听属性 类似于data概念",
  computed: {},

  //监控data中的数据变化",
  watch: {},

  methods: {
    TransformTime(hour, min) {
      var z = min % 10
      var ten = parseInt(min / 10)
      if (z > 0 && z < 5) {
        z = 5
      } else if (z > 5 && z <= 9) {
        z = 0
        ten = ten + 1
      }
      if (ten === 6) {
        hour = hour + 1
        ten = 0
      }
      min = ten * 10 + z
      if (hour < 8) {
        hour = 7
        min = 30
      }
      var hourstr = hour < 10 ? '0' + hour.toString() : hour.toString()
      var minstr = min < 10 ? '0' + min.toString() : min.toString()
      this.nowTime = hourstr + ':' + minstr
      console.log(this.nowTime)
    },
    getNowTime() {
      const date = new Date()
      const month = date.getMonth() + 1
      const day = date.getDate()
      var hour = date.getHours()
      var min = date.getMinutes()
      var strmonth = month < 10 ? '0' + month.toString() : month.toString()
      var strday = day < 10 ? '0' + day.toString() : day.toString()
      // 2021-11-04
      if (this.date1 != null) {
        var choosemonth = this.date1.substring(5, 7)
        var chooseadte = this.date1.substring(8, 10)
      }
      if (choosemonth > strmonth || chooseadte > strday) {
        hour = 7
        min = 30
      }
      this.TransformTime(hour, min)
    },
    open(flag, seat, seatId) {
      if (flag === false) {
        this.$confirm('是否要预定该座位？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
          .then(() => {
            // this.DataForm.seat = seat
            this.dialogFormVisible = true
            this.DataForm.seatId = seatId
            this.getNowTime()
          })
          .catch(() => {
            this.$message({
              type: 'info',
              message: '已取消'
            })
          })
      }
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
          },1000)
          resolve(resp)
        }).catch(error => {
          reject(error)
        })
      })
    },
    show() {
      console.log(this.selectValue)
      //  通过roomId获取对应的教室座位 放入SeatList
      return new Promise((resolve, reject) => {
        getSeatByRoomId(this.selectValue).then(resp => {
          const { data } = resp
          console.log(data)
          this.SeatList = data
          resolve(resp)
        }).catch(error => {
          reject(error)
        })
      })
    },
    dialogFormVisibled() {
      // this.$message({
      //   type: 'success',
      //   message: '预约成功!'
      // })
      // this.changeSeat()
      this.DataForm.roomId = this.selectValue
      console.log(this.DataForm)
      this.DataForm.startTime = this.TimeFormat(this.DataForm.startTime)
      this.DataForm.endTime = this.TimeFormat(this.DataForm.endTime)
      // this.DataForm.startTime = this.TimeFormat('23:50')
      // this.DataForm.endTime = this.TimeFormat('23:55')
      this.dialogFormVisible = false
      return new Promise((resolve, reject) => {
        bookTheSeat(this.DataForm).then(resp => {
          this.DataForm.startTime = ''
          this.DataForm.endTime = ''
          this.show()
          resolve(resp)
        }).catch(error => {
          this.DataForm.startTime = ''
          this.DataForm.endTime = ''
          reject(error)
        })
      })
    },
    TimeFormat(time) {
      // console.log(this.date1 + ' ' + time)
      var date = this.date1 + ' ' + time + ':00'
      return date
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

<style lang="scss" scoped>
.user-radio {
  margin: 50px 30px 20px;

  &-div {
    font-size: 24px;
  }

  &-p {
    font-size: 20px;
  }

  // 因为设置了scoped，所以固定的classname要用/deep/操作
  ::v-deep {
    .el-radio-button__inner {
      border: none;
      border-radius: 4px;
      background: #78e0ffa4;
    }

    .el-radio-button__orig-radio:checked + .el-radio-button__inner {
      background: #1e6abc;
    }
  }
}

.ShowSeat {
  width: 100%;
  height: 580px;
  margin-top: 20px;
  background: #20a0ff;
}

.selector {
  margin-top: 50px;
  margin-left: 20px;
}
</style>
