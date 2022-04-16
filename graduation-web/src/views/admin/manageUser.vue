<template>
  <div class="help">
    <el-button style="margin-top: 20px;margin-left: 20px" type="primary" @click="dialogFormVisible = true">添加用户
    </el-button>
    <el-table
      :data="userList"
      border
      element-loading-text="Loading"
      fit
      highlight-current-row
      size="mini"
      style="margin-top: 20px"
    >
      <el-table-column align="center" label="编号" width="100">
        <template slot-scope="scope">
          {{ scope.row.id }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="账号">
        <template slot-scope="scope">
          {{ scope.row.userName }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="昵称" width="100">
        <template slot-scope="scope">
          {{ scope.row.nickName }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="性别" width="180">
        <template slot-scope="scope">
          <span>{{ scope.row.sex }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="创建人" width="180">
        <template slot-scope="scope">
          {{ scope.row.createBy }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="创建时间" width="180">
        <template slot-scope="scope">
          <span>{{ scope.row.createTime }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="更新人" width="180">
        <template slot-scope="scope">
          <span>{{ scope.row.updateBy }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="更新时间" width="180">
        <template slot-scope="scope">
          <span>{{ scope.row.updateTime }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="状态" width="180">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.status === '0'"
            active-color="#13ce66"
            active-text="启用"
            inactive-color="#ff4949"
            inactive-text="停用"
            @change="changeTheStatus(scope.row.id,scope.row.status === '0' ? '1' : '0')"
          >
          </el-switch>
        </template>
      </el-table-column>
      <el-table-column align="center" class-name="status-col" label="操作" width="240">
        <template slot-scope="scope">
          <el-button icon="el-icon-delete" size="small" type="danger" @click="del(scope.row.id)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog :visible.sync="dialogFormVisible" title="添加用户">
      <el-form :model="newUserList">
        <el-form-item :label-width="formLabelWidth" label="账号">
          <el-input v-model="newUserList.userName" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item :label-width="formLabelWidth" label="昵称">
          <el-input v-model="newUserList.nickName" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item :label-width="formLabelWidth" label="密码">
          <el-input v-model="newUserList.password" :disabled="true" autocomplete="off" style="width: 200px"></el-input>
        </el-form-item>
        <el-form-item :label-width="formLabelWidth" label="性别">
          <el-select v-model="newUserList.sex" placeholder="性别">
            <el-option label="男" value="男"></el-option>
            <el-option label="女" value="女"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="InsertUser()">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { changeTheStatus, deleteUserById, getAllUser, insertUser } from '@/api/user'

export default {
  name: 'manageUser',
  data() {
    return {
      userList: [],
      dialogFormVisible: false,
      newUserList: {
        userName: '',
        nickName: '',
        sex: '',
        password: '123456'
      },
      formLabelWidth: '120px'
    }
  },

  computed: {},

  watch: {},

  methods: {
    InsertUser() {
      return new Promise((resolve, reject) => {
        insertUser(this.newUserList).then(() => {
          this.dialogFormVisible = false
          this.getAllUser()
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },
    del(id) {
      return new Promise((resolve, reject) => {
        deleteUserById(id).then(() => {
          this.getAllUser()
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },
    changeTheStatus(id, status) {
      // 回传给后端
      return new Promise((resolve, reject) => {
        changeTheStatus(id, status).then(() => {
          this.getAllUser()
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },
    getAllUser() {
      return new Promise((resolve, reject) => {
        getAllUser().then(resp => {
          const { data } = resp
          this.userList = data
          console.log(this.userList)
          resolve(resp)
        }).catch(error => {
          reject(error)
        })
      })
    }
  },

  created() {
    this.getAllUser()
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
