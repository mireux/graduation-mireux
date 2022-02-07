<template>
  <div class="personal">
    <div class="user-avatar">
      <el-row>
        <el-col :span="7">
          <div class="tupian" @click="uploadAvatar"/>
          <div>
            <img id="aaa" :src="avatar">
          </div>
          <el-dialog
            title="上传头像"
            :visible.sync="dialogVisible"
            :width="dialogWidth"
          >
            <el-upload
              class="avatar-uploader"
              action="/upload"
              :headers="headers"
              :show-file-list="false"
              :on-success="handleAvatarSuccess"
              :before-upload="beforeAvatarUpload"
            >
              <img v-if="imageUrl" :src="imageUrl" class="avatar">
              <i v-else class="el-icon-plus avatar-uploader-icon"/>
            </el-upload>
            <el-button type="primary" @click="updateAvatar">确定</el-button>
            <el-button @click="cancelDialog">取消</el-button>
          </el-dialog>
        </el-col>
      </el-row>
    </div>
    <el-divider></el-divider>
    <div v-if="!isChange" id="userInfo">
      <vue-element-loading :active="isActive" spinner="bar-fade-scale" color="#FF6700" background-color="rgba(255, 255, 255,.99)"/>
      <el-row>
        <el-col :span="18">
          <div><h3>个人信息</h3></div>
        </el-col>
        <el-col :span="6">
          <div style="position: relative;margin-top: 15px">
            <el-button round @click="changeUser">修改</el-button>
          </div>
        </el-col>
        <el-col>
          <div style="margin-left: 20px; margin-top: 20px">
            <span style="">名字：</span>&nbsp;{{
              userInfoList.nickName === null ? '未填写' : userInfoList.nickName
            }}
          </div>
        </el-col>
        <el-col>
          <div style="margin-left: 20px; margin-top: 20px">
            <span style="">性别：</span>&nbsp;{{
              userInfoList.sex === null ? '未填写' : userInfoList.sex
            }}
          </div>
        </el-col>
        <el-col>
          <div style="margin-left: 20px; margin-top: 20px">
            <span style="">身份：</span>&nbsp;{{
              userInfoList.role.name
            }}
          </div>
        </el-col>
        <el-col>
          <div style="margin-left: 20px; margin-top: 20px">
            <span style="">年龄：</span>&nbsp;{{
              userInfoList.age === null ? '未填写' : userInfoList.age
            }}
          </div>
        </el-col>
        <el-col>
          <div style="margin-left: 20px; margin-top: 20px">
            <span style="">手机号：</span>&nbsp;{{
              userInfoList.phoneNumber === null ? '未填写' : userInfoList.phoneNumber
            }}
          </div>
        </el-col>
        <el-col>
          <div style="margin-left: 20px; margin-top: 20px">
            <span style="">邮箱：</span>&nbsp;{{
              userInfoList.email === null ? '未填写' : userInfoList.email
            }}
          </div>
        </el-col>
        <el-col>
          <div style="margin-left: 20px; margin-top: 20px">
            <span style="">所在地区：</span>&nbsp;{{
              userInfoList.address === null ? '未填写' : userInfoList.address
            }}
          </div>
        </el-col>

      </el-row>
    </div>
    <div v-if="isChange" id="ChangeUserinfo">
      <el-form ref="form" :model="changeUserInfoList" label-width="80px">
        <el-form-item label="名字" style="width: 300px">
          <el-input v-model="changeUserInfoList.nickName"/>
        </el-form-item>
        <el-form-item label="性别" style="width: 300px">
          <el-select
            v-model="changeUserInfoList.sex"
            placeholder="请选择性别"
          >
            <el-option label="男" value="男"/>
            <el-option label="女" value="女"/>
          </el-select>
        </el-form-item>
        <el-form-item label="身份" style="width: 300px">
          <el-input v-model="changeUserInfoList.role" :disabled="true"/>
        </el-form-item>
        <el-form-item label="年龄" style="width: 300px">
          <el-input v-model="changeUserInfoList.age"/>
        </el-form-item>
        <el-form-item label="手机号" style="width: 300px">
          <el-input v-model="changeUserInfoList.phoneNumber"/>
        </el-form-item>
        <el-form-item label="邮箱" style="width: 300px">
          <el-input v-model="changeUserInfoList.email"/>
        </el-form-item>
        <el-form-item label="所在地">
          <myDistrict
            :province.sync="province"
            :city.sync="city"
            :area.sync="area"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="updateUserinfo">确定</el-button>
          <el-button @click="cancel">取消</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import { getToken } from '@/utils/auth'
import { getInfo, UpdateRoleInfo, UpdateLoad, CancelUpdateAvatar } from '@/api/user'
import { mapGetters } from 'vuex'
import VueElementLoading from 'vue-element-loading'
export default {
  name: 'personal',
  components: {
    VueElementLoading
  },
  data() {
    return {
      isActive: true,
      userInfoList: {
        nickName: '',
        role: [],
        sex: '',
        age: '',
        phoneNumber: '',
        email: '',
        place: ''
      },
      dialogVisible: false,
      imageUrl: '',
      dialogWidth: '',
      isChange: false,
      changeUserInfoList: {
        nickName: '',
        sex: '',
        role: '',
        age: '',
        phoneNumber: '',
        email: '',
        address: ''
      },
      province: '',
      city: '',
      area: ''
    }
  },

  //监听属性 类似于data概念",
  computed: {
    headers() {
      return {
        'Token': this.token // 直接从本地获取token就行
      }
    },
    ...mapGetters([
      'avatar'
    ])
  },

  //监控data中的数据变化",
  watch: {},

  methods: {
    updateUserinfo() {
      if (this.province === '') {
        this.changeUserInfoList.address = null
      } else {
        this.changeUserInfoList.address =
          this.province + '-' + this.city + '-' + this.area
      }
      return new Promise(((resolve, reject) => {
        UpdateRoleInfo(this.changeUserInfoList)
          .then(resp => {
            resolve(resp)
              this.isChange = false
              this.isActive = true
              this.getUserInfo()


        })
        .catch(error => {
          reject(error)
        })
      }))
    },
    cancel() {
      this.isChange = false
    },
    changeUser() {
      this.isChange = true
      this.changeUserInfoList.nickName = this.userInfoList.nickName
      this.changeUserInfoList.sex = this.userInfoList.sex
      this.changeUserInfoList.age = this.userInfoList.age
      this.changeUserInfoList.phoneNumber = this.userInfoList.phoneNumber
      this.changeUserInfoList.email = this.userInfoList.email
      this.changeUserInfoList.role = this.userInfoList.role.name
      if (this.userInfoList.address != null) {
        const str = this.userInfoList.address.split('-')
        this.province = str[0]
        this.city = str[1]
        this.area = str[2]
      }
    },
    setDialogWidth() {
      const val = document.body.clientWidth
      const def = 300 // 默认宽度
      if (val < def) {
        this.dialogWidth = '100%'
      } else {
        this.dialogWidth = def + 'px'
      }
    },
    handleAvatarSuccess(res, file) {
      this.imageUrl = URL.createObjectURL(file.raw)
      this.uploadImageUrl = res.data
    },
    beforeAvatarUpload(file) {
      const isLt2M = file.size / 1024 / 1024 < 2

      if (!isLt2M) {
        this.$message.error('上传头像图片大小不能超过 2MB!')
      }
      return isLt2M
    },
    uploadAvatar() {
      this.dialogVisible = true
    },
    updateAvatar() {
      return new Promise(((resolve, reject) => {
        UpdateLoad().then((resp) => {
          const { data } = resp
          this.$store.commit('user/SET_AVATAR',data)
          this.getUserInfo()
          this.imageUrl = ''
          this.dialogVisible = false
          resolve()
        }).catch(error => {
          reject(error)
        })
      }))
    },
    cancelDialog() {
      return new Promise((resolve, reject) =>  {
        CancelUpdateAvatar()
        .then((resp) => {
          this.dialogVisible = false
          resolve()
        })
        .catch(error => {
          reject(error)
        })
      })

    },
    getUserInfo() {
      return new Promise((resolve, reject) => {
        getInfo()
          .then((resp) => {
            const { data } = resp
            this.userInfoList = data
            setInterval(() => {
              this.isActive = false
            },100)
            resolve(data)
          })
          .catch((error) => {
            reject(error)
          })
      })
    }
  },

  //生命周期 - 创建完成（可以访问当前this实例）",数据模型已加载，方法已加载,html模板已加载,html模板未渲染
  created() {
    this.setDialogWidth()
    this.getUserInfo()
    this.token = getToken()
  },

  //生命周期 - 挂载完成（可以访问DOM元素）",html模板已渲染
  mounted() {
    window.onresize = () => {
      return (() => {
        this.setDialogWidth()
      })()
    }
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
#userInfo {
  margin-left: 70px;
}

.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}

.avatar-uploader .el-upload:hover {
  border-color: #409eff;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}

.avatar {
  width: 178px;
  height: 178px;
  display: block;
}

#ChangeUserinfo {
  margin-left: 70px;
}

#useravatar::after {
  content: "";
  width: 100%;
  height: 1px;
  border-bottom: 1px solid #ccc;
  display: inline-block;
  margin-bottom: 10px;
}

.tupian {
  position: absolute;
  width: 150px;
  height: 150px;
  border-radius: 50%;
  margin-left: 50px;
  margin-top: 50px;
}

.tupian:hover {
  background: rgb(0, 0, 0);
  opacity: 0.5;
  cursor: pointer;
  transition: 0.6s;
}

.tupian:hover::after {
  content: "上传头像";
  position: absolute;
  top: 60px;
  left: 40px;
  z-index: 999;
  opacity: 1;
  transition: 0.6s;
  color: rgb(255, 245, 245);
}

#aaa {
  margin-left: 50px;
  margin-top: 50px;
  width: 150px;
  height: 150px;
  z-index: -1;
  border-radius: 50%;
}
</style>
