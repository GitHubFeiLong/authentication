<!--新增用户的弹框-->
<template>
  <el-dialog title="新增用户" width="600px" :visible.sync="visible" @close="close">
    <el-form ref="user" :model="user" :rules="rules" label-width="80px" class="demo-ruleForm">
      <el-form-item label="用户名" prop="username">
        <el-input v-model="user.username" placeholder="请输入用户名" clearable />
      </el-form-item>
      <el-form-item label="密码" prop="password">
        <el-input v-model="user.password" placeholder="请输入登录密码，如不输入，即使用默认密码" clearable />
      </el-form-item>
      <el-form-item label="角色" prop="roleIds">
        <RoleSelect ref="roleSelectRef" :role-multiple="true" @getSelectRoles="getSelectRoles" />
      </el-form-item>
      <el-form-item label="备注">
        <el-input v-model="user.remark" placeholder="请输入用户备注" clearable/>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="resetForm('user')">重 置</el-button>
      <el-button type="primary" @click="submitForm('user')">确 定</el-button>
    </div>
  </el-dialog>
</template>

<script>
import * as validate from '@/utils/validate'
import { simpleCreateUserApi } from '@/api/user'
import { Message } from "element-ui"
import { ComplexPwd } from "@/utils/ElementValidatorUtil";

export default {
  name: 'CreateUserDialog',
  components: {
    RoleSelect: () => import('@/components/User/RoleSelect')
  },
  props: {
    // 弹框
    createUserDialog: { required: true, type: Boolean, default: false }
  },
  data() {
    return {
      visible: false,
      user: {
        username: '',
        phone: '',
        email: '',
        password: '',
        roleIds: [],
        remark: '',
      },
      rules: {
        username: [
          { required: true, trigger: 'blur', message: '请输入用户名' },
        ],
        password_del: [
          { required: true, trigger: 'blur', message: '请输入登录密码' },
          {validator: ComplexPwd, trigger: 'blur'}
        ],
        roleIds: [
          { type: 'array', required: true, message: '请至少选择一个角色', trigger: 'blur' }
        ],
      },
    };
  },
  watch: {
    // 监听 createUserDialog
    createUserDialog() {
      this.visible = this.createUserDialog;
    },
  },
  methods: {
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          simpleCreateUserApi(this.user).then(response => {
            // 保存成功
            Message({
              message: '保存成功',
              type: 'success',
            })
            this.visible = false
            // 调用父组件的方法
            this.$parent.loadPageUser();
          })
        } else {
          return false;
        }
      });
    },
    resetForm(formName) {
      this.$refs.roleSelectRef.reset()
      this.$refs[formName].resetFields();
    },
    // 获取子组件的角色
    getSelectRoles(selectedRoles) {
      this.user.roleIds = selectedRoles.map(item=>item.value);
    },
    close() {
      this.$emit("update:createUserDialog", false)
    }
  },
}
</script>
<style lang="scss" scoped>

</style>
