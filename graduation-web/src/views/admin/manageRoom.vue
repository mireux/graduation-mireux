<template>
  <div class="app-container">
    <el-button style="margin-bottom: 20px" type="primary" icon="el-icon-plus" @click="openDialog">添加</el-button>
    <el-dialog title="添加自习室" :visible.sync="dialogFormVisible" :close-on-click-modal="false">
      <el-form :model="newRoomList">
        <el-form-item label="自习室名称" :label-width="formLabelWidth">
          <el-input v-model="newRoomList.name" autocomplete="off" style="width: 300px"></el-input>
        </el-form-item>
      </el-form>
      <div>
        <span class="demonstration">容纳人数：</span>
        <el-slider
          v-model="newRoomList.capacity"
          show-input
          :show-input-controls="false"
          :max="50"
          input-size="mini"
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
      element-loading-text="Loading"
      border
      fit
      size="mini"
      highlight-current-row
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
      <el-table-column label="容纳人数" align="center" width="100">
        <template slot-scope="scope">
          {{ scope.row.number }}
        </template>
      </el-table-column>
      <el-table-column label="创建人" width="180" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.createBy }}</span>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" width="180" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.createTime }}</span>
        </template>
      </el-table-column>
      <el-table-column label="更新人" width="180" align="center">
        <template slot-scope="scope">
          {{ scope.row.updateBy }}
        </template>
      </el-table-column>
      <el-table-column label="更新时间" width="180" align="center">
        <template slot-scope="scope">
          {{ scope.row.updateTime }}
        </template>
      </el-table-column>
      <el-table-column class-name="status-col" label="状态" width="120" align="center"
                       :filters="[{ text: '启用', value: false }, { text: '停用', value: true }]"
                       :filter-method="filterTag"
                       filter-placement="bottom-end"
      >
        <template slot-scope="scope">
          <el-tag type="success" v-if="!scope.row.delFlag">启用</el-tag>
          <el-tag type="danger" v-if="scope.row.delFlag">停用</el-tag>
        </template>
      </el-table-column>
      <el-table-column align="center" prop="created_at" label="操作" width="300">
        <template slot-scope="scope">
          <el-button size="small" type="primary" icon="el-icon-edit" @click="showTheEdit(scope.row)">修改</el-button>
          <el-button size="small" type="danger" icon="el-icon-delete" @click="disabledTheRoom(scope.row.id)" v-if="!scope.row.delFlag">停用
          </el-button>
          <el-button size="small" type="success" icon="el-icon-setting" @click="disabledTheRoom(scope.row.id)" v-if="scope.row.delFlag">启用
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog title="修改自习室" :visible.sync="dialogEditFormVisible" :close-on-click-modal="false">
      <el-form :model="editRoomList">
        <el-form-item label="自习室名称" :label-width="formLabelWidth">
          <el-input v-model="editRoomList.name" autocomplete="off" style="width: 300px"></el-input>
        </el-form-item>
      </el-form>
      <div>
        <span class="demonstration">容纳人数：</span>
        <el-slider
          v-model="editRoomList.capacity"
          show-input
          :show-input-controls="false"
          :max="50"
          input-size="mini"
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
      return row.delFlag === value;
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
