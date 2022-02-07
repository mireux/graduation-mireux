<!-- test -->
<template>
  <div>
    <div id="useravatar">
      <el-row>
        <el-col :span="7">
          <div class="tupian" @click="uploadavatar" />
          <div>
            <img id="aaa" :src="userinfoList.avatar">
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
              <i v-else class="el-icon-plus avatar-uploader-icon" />
            </el-upload>
            <el-button type="primary" @click="updateavatar">确定</el-button>
            <el-button @click="canceldialog">取消</el-button>
          </el-dialog>
        </el-col>
        <el-col :span="14">
          <div class="nickname">
            <span>姓名：{{ userinfoList.name }}</span>
          </div></el-col>
      </el-row>
    </div>
    <div v-if="!isChange" id="userinfo">
      <el-row>
        <el-col :span="18">
          <div id="usertitle"><h3>个人信息</h3></div>
        </el-col>
        <el-col :span="6">
          <div id="changeuserBtn">
            <el-button round @click="changeUser">修改</el-button>
          </div>
        </el-col>
        <el-col><div id="usernickname" style="margin-left: 20px; margin-top: 20px">
          <span style="">名字：</span>&nbsp;{{
            userinfoList.name === null ? "未填写" : userinfoList.name
          }}
        </div></el-col>
        <el-col><div id="usergender" style="margin-left: 20px; margin-top: 20px">
          <span style="">性别：</span>&nbsp;{{
            userinfoList.gender === null ? "未填写" : userinfoList.gender
          }}
        </div></el-col>
        <el-col><div id="userrole" style="margin-left: 20px; margin-top: 20px">
          <span style="">身份：</span>&nbsp;{{
            userinfoList.role === "Student" ? "学生" : "教师"
          }}
        </div></el-col>
        <el-col><div id="userbirthday" style="margin-left: 20px; margin-top: 20px">
          <span style="">出生日期：</span>&nbsp;{{
            userinfoList.birthday === null ? "未填写" : userinfoList.birthday
          }}
        </div></el-col>
        <el-col><div  style="margin-left: 20px; margin-top: 20px">
          <span style="">年龄：</span>&nbsp;{{
            userinfoList.age === null ? "未填写" : userinfoList.age
          }}
        </div></el-col>
        <el-col><div id="userage" style="margin-left: 20px; margin-top: 20px">
          <span style="">手机号：</span>&nbsp;{{
            userinfoList.telphone === null ? "未填写" : userinfoList.telphone
          }}
        </div></el-col>
        <el-col><div id="userplace" style="margin-left: 20px; margin-top: 20px">
          <span style="">所在地区：</span>&nbsp;{{
            userinfoList.place === null ? "未填写" : userinfoList.place
          }}
        </div></el-col>
      </el-row>
    </div>
    <div v-if="isChange" id="ChangeUserinfo">
      <el-form ref="form" :model="changeUserInfoList" label-width="80px">
        <el-form-item label="名字" style="width: 300px">
          <el-input v-model="changeUserInfoList.name" />
        </el-form-item>
        <el-form-item label="性别" style="width: 300px">
          <el-select
            v-model="changeUserInfoList.gender"
            placeholder="请选择性别"
          >
            <el-option label="男" value="男" />
            <el-option label="女" value="女" />
          </el-select>
        </el-form-item>
        <el-form-item label="身份" style="width: 300px">
          <el-input v-model="changeUserInfoList.role" :disabled="true" />
        </el-form-item>
        <el-form-item label="出生日期" style="width: 300px">
          <el-date-picker
            v-model="changeUserInfoList.birthday"
            type="date"
            placeholder="选择日期"
            format="yyyy 年 MM 月 dd 日"
            value-format="yyyy-MM-dd"
          />
        </el-form-item>
        <el-form-item label="年龄" style="width: 300px">
          <el-input v-model="changeUserInfoList.age" />
        </el-form-item>
        <el-form-item label="手机号" style="width: 300px">
          <el-input v-model="changeUserInfoList.telphone" />
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
import { getInfo, EditRoleInfo, UpdateLoad } from '@/api/user.js'
import { getToken } from '@/utils/auth'
// import { cancelAvatarUp } from '@/api/avatar'

export default {
  data() {
    return {
      dialogWidth: 0,
      fileList: [],
      token: '',
      userinfoList: [],
      isChange: false,
      province: '',
      city: '',
      area: '',
      changeUserInfoList: {
        // nickname: this.userinfoList.nickname,
        // gender: this.userinfoList.gender,
        // birthday: this.userinfoList.birthday,
        // place: this.userinfoList.place
        id: '',
        name: '',
        gender: '',
        birthday: '',
        place: '',
        telphone: '',
        role: '',
        age: 0
      },
      dialogImageUrl: '',
      dialogVisible: false,
      imageUrl: '',
      uploadImageUrl: ''
    }
  },
  computed: {
    headers() {
      return {
        'Token': this.token // 直接从本地获取token就行
      }
    }
  },
  created() {
    this.getUserInfo()
    this.setDialogWidth()
    this.token = getToken()
  },
  mounted() {
    window.onresize = () => {
      return (() => {
        this.setDialogWidth()
      })()
    }
  },
  methods: {
    setDialogWidth() {
      console.log(document.body.clientWidth)
      var val = document.body.clientWidth
      const def = 300 // 默认宽度
      if (val < def) {
        this.dialogWidth = '100%'
      } else {
        this.dialogWidth = def + 'px'
      }
    },
    canceldialog() {
      this.dialogVisible = false
    },
    handleAvatarSuccess(res, file) {
      this.imageUrl = URL.createObjectURL(file.raw)
      console.log(this.imageUrl)
      this.uploadImageUrl = res.data
    },
    beforeAvatarUpload(file) {
      const isLt2M = file.size / 1024 / 1024 < 2

      if (!isLt2M) {
        this.$message.error('上传头像图片大小不能超过 2MB!')
      }
      return isLt2M
    },
    updateavatar() {
      return new Promise((resolve, reject) => {
        UpdateLoad(this.uploadImageUrl).then((resp) => {
          const { data } = resp
          console.log(data)
          this.$store.commit('user/SET_AVATAR', data)
          this.getUserInfo()
          this.imageUrl = ''
          this.dialogVisible = false
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },
    uploadavatar() {
      this.dialogVisible = true
    },
    updateUserinfo() {
      if (this.province === '') {
        this.changeUserInfoList.place = null
      } else {
        this.changeUserInfoList.place =
      this.province + '-' + this.city + '-' + this.area
      }
      return new Promise((resolve, reject) => {
        EditRoleInfo(this.changeUserInfoList)
          .then((resp) => {
            console.log(resp)
            resolve(resp)
            this.getUserInfo()
            this.isChange = false
          })
          .catch((error) => {
            reject(error)
          })
      })
    },
    cancel() {
      this.isChange = false
    },
    getUserInfo() {
      return new Promise((resolve, reject) => {
        getInfo()
          .then((resp) => {
            const { data } = resp
            this.userinfoList = data
            resolve(data)
          })
          .catch((error) => {
            reject(error)
          })
      })
    },
    changeUser() {
    // name gender role  birthday age place
      this.isChange = true
      this.changeUserInfoList.id = this.userinfoList.id
      this.changeUserInfoList.name = this.userinfoList.name
      this.changeUserInfoList.gender = this.userinfoList.gender
      this.changeUserInfoList.birthday = this.userinfoList.birthday
      // this.changeUserInfoList.role =
      this.userinfoList.role === 'Student' ? '学生' : '教师'
      this.changeUserInfoList.age = this.userinfoList.age
      this.changeUserInfoList.telphone = this.userinfoList.telphone
      if (this.userinfoList.place != null) {
        var str = this.userinfoList.place.split('-')
        this.province = str[0]
        this.city = str[1]
        this.area = str[2]
      }
    }
  }
}
</script>
<style lang='scss' scoped>
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
.nickname span {
  font-size: 30px;
  line-height: 200px;
}
#userinfo {
  margin-left: 70px;
}
#changeuserBtn {
  position: relative;
}
@media screen and (max-width: 690px) {
  #userinfo {
    margin-left: 20px;
  }
  #ChangeUserinfo {
    margin-left: 20px;
  }
  .nickname {
    display: none;
  }
}
</style>
