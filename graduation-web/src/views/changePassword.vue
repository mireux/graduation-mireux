<template>
  <div class="changePassword">
    <el-form :model="ruleForm" status-icon :rules="rules" ref="ruleForm" label-width="100px">
      <el-form-item label="旧密码" prop="oldPass">
        <el-input type="password" v-model.number="ruleForm.oldPass" style="width: 400px"></el-input>
      </el-form-item>
      <el-form-item label="密码" prop="newPass">
        <el-input type="password" v-model="ruleForm.newPass" autocomplete="off" style="width: 400px"></el-input>
      </el-form-item>
      <el-form-item label="确认密码" prop="checkPass">
        <el-input type="password" v-model="ruleForm.checkPass" autocomplete="off" style="width: 400px"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitForm('ruleForm')" style="margin-left: 100px">提交</el-button>
        <el-button @click="resetForm('ruleForm')">重置</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { changePassword } from '@/api/user'

export default {
  data() {
    const checkOldPass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('旧密码不能为空'))
      }
      callback()
    }
    const validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入密码'))
      } else {
        if (this.ruleForm.checkPass !== '') {
          this.$refs.ruleForm.validateField('checkPass')
        }
        callback()
      }
    }
    const validatePass2 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'))
      } else if (value !== this.ruleForm.newPass) {
        callback(new Error('两次输入密码不一致!'))
      } else {
        callback()
      }
    }
    return {
      ruleForm: {
        oldPass: '',
        newPass: '',
        checkPass: ''
      },
      rules: {
        oldPass: [
          { validator: checkOldPass, trigger: 'blur' }
        ],
        newPass: [
          { validator: validatePass, trigger: 'blur' },
          {min: 6,max:15,message:"长度必须在6-15个字符之间",trigger: 'blur'}
        ],
        checkPass: [
          { validator: validatePass2, trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          return new Promise((resolve, reject) => {
            changePassword(this.ruleForm).then(() => {
              this.$router.go(0)
            }).catch(error => {
              reject(error)
            })
          })
        } else {
          console.log('error submit!!')
          return false
        }
      })
    },
    resetForm(formName) {
      this.$refs[formName].resetFields()
    }
  }
}
</script>

<style scoped>
.changePassword {
  margin-top: 40px;
  margin-left: 20%;
}
</style>
