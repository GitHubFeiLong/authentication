<template>
  <el-form ref="changePassword" :model="user" :rules="changePasswordRules">
    <el-form-item label="旧密码" prop="oldPassword">
      <el-input v-model="user.oldPassword" :type="passwordType" show-password placeholder="请输入当前账户的密码" />
    </el-form-item>
    <el-form-item label="新密码" prop="newPassword">
      <el-input v-model="user.newPassword" :type="passwordType" show-password placeholder="请输入新密码8~20位包含数字、字母、符号组合的强密码" />
    </el-form-item>
    <el-form-item label="确认密码" prop="confirmNewPassword">
      <el-input v-model.trim="user.confirmNewPassword" :type="passwordType" show-password placeholder="请再次输入新密码" />
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="submit">提 交</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
import {changeOwnPasswordApi} from "@/api/user";
import * as validate from "@/utils/validate";
import { password, strongPassword } from "@/utils/validate";
import { Message } from "element-ui";
import store from "@/store";
import Router from "@/router";

export default {
  name: 'Account',
  props: {
    user1: {
      type: Object,
      default: () => {
        return {
          username: '',
          email: '',
          oldPassword: '',
          password: '',
          newPassword: '',
        }
      }
    }
  },
  data() {
    return {
      passwordType: 'password',
      changePasswordRules: {
        oldPassword: [
          { required: true, message: "请输入当前账户的密码", trigger: 'blur'}
        ],
        newPassword: [
          { required: true, validator: validate.strongPassword, trigger: 'blur' },
        ],
        confirmNewPassword: [
          { required: true, validator: validate.strongPassword, trigger: 'blur' },
        ],
      },
      user:{
        oldPassword: '',
        newPassword: '',
        confirmNewPassword: '',
      },
    }
  },
  methods: {
    submit() {
      this.$refs.changePassword.validate((valid) => {
        if (valid) {
          if (this.user.newPassword !== this.user.oldPassword) {
            this.$message.error("新密码和旧密码不能相同")
            return;
          }
          if (this.user.newPassword !== this.user.confirmNewPassword) {
            this.$message.error("新密码和确认密码不匹配")
            return;
          }
          changeOwnPasswordApi({oldPassword: this.user.oldPassword, newPassword: this.user.newPassword}).then(data => {
            if (data) {
              this.$message.success("修改成功")
              // 退出登录
              store.dispatch('user/logout')
              Router.push({ path: '/login' })
            } else {
              this.$message.error("修改失败")
            }
          })
        } else {
          return false;
        }
      });
    },
  }
}
</script>
<style lang="scss" scoped>

</style>
