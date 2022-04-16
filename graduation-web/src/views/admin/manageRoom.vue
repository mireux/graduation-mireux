<template>
  <div class="app-container">
    <el-button icon="el-icon-plus" style="margin-bottom: 20px" type="primary" @click="openDialog">添加</el-button>
    <el-dialog :close-on-click-modal="false" :visible.sync="dialogFormVisible" title="添加自习室">
      <el-form :model="newRoomList">
        <el-form-item :label-width="formLabelWidth" label="自习室名称">
          <el-input v-model="newRoomList.name" autocomplete="off" style="width: 300px"></el-input>
        </el-form-item>
      </el-form>
      <div>
        <span class="demonstration">容纳人数：</span>
        <el-slider
          v-model="newRoomList.capacity"
          :max="50"
          :show-input-controls="false"
          input-size="mini"
          show-input
        >

        </el-slider>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="InsertRoom">确 定</el-button>
      </div>
    </el-dialog>

    <el-table
      v-loading="listLoading"
      :data="roomList"
      border
      element-loading-text="Loading"
      fit
      highlight-current-row
      size="mini"
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
      <el-table-column align="center" label="容纳人数" width="100">
        <template slot-scope="scope">
          {{ scope.row.number }}
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
      <el-table-column :filter-method="filterTag" :filters="[{ text: '启用', value: false }, { text: '停用', value: true }]" align="center" class-name="status-col"
                       filter-placement="bottom-end"
                       label="状态"
                       width="120"
      >
        <template slot-scope="scope">
          <el-tag v-if="!scope.row.delFlag" type="success">启用</el-tag>
          <el-tag v-if="scope.row.delFlag" type="danger">停用</el-tag>
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作" prop="created_at" width="300">
        <template slot-scope="scope">
          <el-button icon="el-icon-edit" size="small" type="primary" @click="showTheEdit(scope.row)">修改</el-button>
          <el-button v-if="!scope.row.delFlag" icon="el-icon-delete" size="small" type="danger"
                     @click="disabledTheRoom(scope.row.id)"
          >停用
          </el-button>
          <el-button v-if="scope.row.delFlag" icon="el-icon-setting" size="small" type="success"
                     @click="disabledTheRoom(scope.row.id)"
          >启用
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog :close-on-click-modal="false" :visible.sync="dialogEditFormVisible" title="修改自习室">
      <el-form :model="editRoomList">
        <el-form-item :label-width="formLabelWidth" label="自习室名称">
          <el-input v-model="editRoomList.name" autocomplete="off" style="width: 300px"></el-input>
        </el-form-item>
      </el-form>
      <div>
        <span class="demonstration">容纳人数：</span>
        <el-slider
          v-model="editRoomList.capacity"
          :max="50"
          :show-input-controls="false"
          input-size="mini"
          show-input
        >

        </el-slider>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogEditFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="EditRoom">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { disabledTheRoom, EditTheRoom, getRoomList, InsertRoom } from '@/api/room'

export default {
  name: 'manageRoom',
  data() {
    return {
      listLoading: true,
      roomList: [],
      dialogFormVisible: false,
      dialogEditFormVisible: false,
      editRoomId: '',
      newRoomList: {
        name: '',
        capacity: 0
      },
      editRoomList: {
        name: '',
        capacity: 0
      },
      formLabelWidth: '120px'

    }
  },

  //监听属性 类似于data概念",
  computed: {},

  //监控data中的数据变化",
  watch: {},

  methods: {
    filterTag(value, row) {
      return row.delFlag === value
    },
    showTheEdit(row) {
      this.dialogEditFormVisible = true
      this.editRoomId = row.id
      this.editRoomList.name = row.room
      this.editRoomList.capacity = row.number
    },
    EditRoom() {
      return new Promise((resolve, reject) => {
        EditTheRoom(this.editRoomId, this.editRoomList).then(() => {
          this.dialogEditFormVisible = false
          this.listLoading = true
          this.getRoomList()
        }).catch(error => {
          reject(error)
        })
      })
    },
    disabledTheRoom(id) {
      return new Promise((resolve, reject) => {
        disabledTheRoom(id).then(() => {
          this.listLoading = true
          this.getRoomList()
        }).catch(error => {
          reject(error)
        })
      })
    },
    openDialog() {
      this.dialogFormVisible = true
    },
    InsertRoom() {
      return new Promise((resolve, reject) => {
        InsertRoom(this.newRoomList).then(() => {
          this.dialogFormVisible = false
          this.listLoading = true
          this.getRoomList()
        }).catch(error => {
          reject(error)
        })
      })
    },
    getRoomList() {
      return new Promise((resolve, reject) => {
        getRoomList(false).then(resp => {
          const { data } = resp
          this.roomList = data
          setInterval(() => {
            this.listLoading = false
          }, 200)
          resolve(data)
        }).catch(error => {
          reject(error)
        })
      })
    }
  },

  //生命周期 - 创建完成（可以访问当前this实例）",数据模型已加载，方法已加载,html模板已加载,html模板未渲染
  created() {
    this.getRoomList()
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
.el-slider {
  .el-slider--with-input {
    width: 500px;
  }
}
</style>
