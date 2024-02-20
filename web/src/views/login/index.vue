<template>
  <div class="login-container">
    <el-dialog
      title="请选择登录应用"
      :visible.sync="showSelectApp"
      width="100%"
      class="choice-dialog"
      :close-on-click-modal="false"
    >
      <div class="choice-page">
        <el-card :body-style="{ padding: '0px' }" @click.native.prevent="selectApp(realHomePage)" shadow="hover">
          <img :src="serverAdminImage" class="image">
          <div class="app-name">
            <span>{{realAppName}}</span>
          </div>
        </el-card>
        <el-card :body-style="{ padding: '0px' }" @click.native.prevent="selectApp(homePage)" shadow="hover">
          <img :src="appImage" class="image">
          <div class="app-name">
            <span>{{appName}}</span>
          </div>
        </el-card>
      </div>
    </el-dialog>


    <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form" autocomplete="on" label-position="left">

      <div class="title-container">
        <h3 class="title">Login Form</h3>
      </div>

      <el-form-item prop="selectAppId">
        <span class="svg-container">
          <svg-icon icon-class="iconfont-yingyongguanli" />
        </span>
        <el-select v-model="loginForm.selectAppId" placeholder="请选择应用登录" clearable>
          <el-option
            v-for="item in apps"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
      </el-form-item>

      <el-form-item prop="username">
        <span class="svg-container">
          <svg-icon icon-class="user" />
        </span>
        <el-input
          ref="username"
          v-model="loginForm.username"
          placeholder="Username"
          name="username"
          type="text"
          tabindex="1"
          autocomplete="on"
          clearable
        />
      </el-form-item>

      <el-tooltip v-model="capsTooltip" content="Caps lock is On" placement="right" manual>
        <el-form-item prop="password">
          <span class="svg-container">
            <svg-icon icon-class="password" />
          </span>
          <el-input
            :key="passwordType"
            ref="password"
            v-model="loginForm.password"
            :type="passwordType"
            placeholder="Password"
            name="password"
            tabindex="2"
            autocomplete="on"
            @keyup.native="checkCapslock"
            @blur="capsTooltip = false"
            @keyup.enter.native="handleLogin"
          />
          <span class="show-pwd" @click="showPwd">
            <svg-icon :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'" />
          </span>
        </el-form-item>
      </el-tooltip>

      <el-button :loading="loading" type="primary" style="width:100%;margin-bottom:30px;" @click.native.prevent="handleLogin">Login</el-button>

    </el-form>
  </div>
</template>

<script>
// import SocialSign from './components/SocialSignin'
import { dropDownAllAppApi } from '@/api/dropDown';
import { loginApi } from "@/api/user";
import log from "echarts/src/scale/Log";
import LocalStorageUtil from "@/utils/LocalStorageUtil";
import * as commons from "@/constant/commons";
import appImage from '@/assets/jpg/pexels-fauxels-3184418.jpg'
import serverAdminImage from '@/assets/jpg/pexels-pixabay-461064.jpg'
export default {
  name: 'Login',
  // components: { SocialSign },
  data() {
    return {
      loginForm: {
        selectAppId: '',
        username: 'admin',
        password: '123456'
      },
      // 规则
      loginRules: {
        username: [{ required: true, trigger: 'blur', message: "请输入登录账户名"}],
        password: [{ required: true, trigger: 'blur', message: "请输入登录账户密码"}]
      },
      passwordType: 'password',
      capsTooltip: false,
      loading: false,
      redirect: undefined,
      otherQuery: {},
      apps: [],
      token: {},  // 登录成功返回的url
      homePage: '',
      realHomePage: '',
      appImage: appImage,
      serverAdminImage: serverAdminImage,
      realAppName: 'Admin',  // 账户真实应用名
      appName: '小米',      // 认证后台应用名
      showSelectApp: false, //  是否显示
    }
  },
  watch: {
    $route: {
      handler: function(route) {
        const query = route.query
        if (query) {
          this.redirect = query.redirect
          this.otherQuery = this.getOtherQuery(query)
        }
      },
      immediate: true
    }
  },
  mounted() {
    // 获取应用下拉
    dropDownAllAppApi().then(data => {
      console.log(data);
      this.apps = data
    })
  },
  methods: {
    checkCapslock(e) {
      const { key } = e
      this.capsTooltip = key && key.length === 1 && (key >= 'A' && key <= 'Z')
    },
    showPwd() {
      if (this.passwordType === 'password') {
        this.passwordType = ''
      } else {
        this.passwordType = 'password'
      }
      this.$nextTick(() => {
        this.$refs.password.focus()
      })
    },
    handleLogin() {
      // element的表单验证
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true
          // 设置应用id到本次存储，请求时取出放在请求头
          let selectAppId = this.loginForm.selectAppId;
          LocalStorageUtil.setXAppId(selectAppId !== '' ? selectAppId : commons.X_APP_ID)
          loginApi(this.loginForm.username.trim(), encodeURIComponent(this.loginForm.password), this.loginForm.selectAppId).then(data => {
            const { homePage, realHomePage, token, appName, realAppName } = data
            const {accessToken, refreshToken, accessExpires, refreshExpires} = token
            this.token = token
            this.homePage = homePage
            this.realHomePage = realHomePage
            this.appName = appName
            this.realAppName = realAppName
            // 同一个应用
            if (this.appName === this.realAppName) {
              const url = this.getFullUrl(this.homePage)
              window.location.href = url
            } else {
              this.homePage = this.getFullUrl(this.homePage)
              this.realHomePage = this.getFullUrl(this.realHomePage)
              this.showSelectApp = true
            }
          }).finally(() => {
            this.loading = false
          })
        } else {
          console.log('error submit!!')
          return false
        }
      })
    },
    getFullUrl(homePage) {
      const {accessToken, refreshToken, accessExpires, refreshExpires} = this.token
      return `${homePage}?accessToken=${accessToken}&refreshToken=${refreshToken}&accessExpires=${accessExpires}&refreshExpires=${refreshExpires}`
    },
    getOtherQuery(query) {
      return Object.keys(query).reduce((acc, cur) => {
        if (cur !== 'redirect') {
          acc[cur] = query[cur]
        }
        return acc
      }, {})
    },
    selectApp(url) {
      console.log(url)
      window.location.href = url
    }
  }
}
</script>

<style lang="scss">
/* 修复input 背景不协调 和光标变色 */
/* Detail see https://github.com/PanJiaChen/vue-element-admin/pull/927 */

$bg:#283443;
$light_gray:#fff;
$cursor: #fff;

@supports (-webkit-mask: none) and (not (cater-color: $cursor)) {
  .login-container .el-input input {
    color: $cursor;
  }
}

/* reset element-ui css */
.login-container {
  .el-input{
    width: calc(100% - 32px);
  }
  .el-select{
    width: calc(100% - 32px);
    .el-input{
      width: 100%;
    }
  }
  .el-input {
    display: inline-block;
    height: 47px;
    input {
      background: transparent;
      border: 0px;
      -webkit-appearance: none;
      border-radius: 0px;
      padding: 12px 5px 12px 15px;
      color: $light_gray;
      height: 47px;
      caret-color: $cursor;

      &:-webkit-autofill {
        box-shadow: 0 0 0px 1000px $bg inset !important;
        -webkit-text-fill-color: $cursor !important;
      }
    }
  }

  .el-form-item {
    border: 1px solid rgba(255, 255, 255, 0.1);
    background: rgba(0, 0, 0, 0.1);
    border-radius: 5px;
    color: #454545;
  }
}
</style>

<style lang="scss" scoped>
$bg:#2d3a4b;
$dark_gray:#889aa4;
$light_gray:#eee;

.login-container {
  min-height: 100%;
  width: 100%;
  background-color: $bg;
  overflow: hidden;
  .choice-dialog {
    width: 1400px;
    max-width: 100%;
    margin: 0 auto;
    position: absolute;
    .choice-page{
      background-color: white;
      display: flex;
      flex-direction: row;
      flex-wrap: wrap;
      justify-content: space-around;
      align-items: center;
      .el-card{
        width: 630px;
        position: relative;
        padding: 0px 10px 0px 5px;
        .image{
          width: 100%;
          height: 100%;
        }
        .app-name{
          padding: 14px;
          display: flex;
          justify-content: center;
          font-size: 18px;
        }
        transition: all 0.2s linear;
        &:hover {
          transform: scale(1.1, 1.1);
          filter: contrast(130%);
          cursor: pointer;
          .app-name{
            font-weight: bold;
            color: #3a3a3a;
          }
        }
      }
    }
  }

  .login-form {
    position: relative;
    width: 520px;
    max-width: 100%;
    padding: 160px 35px 0;
    margin: 0 auto;
    overflow: hidden;
  }

  .tips {
    font-size: 14px;
    color: #fff;
    margin-bottom: 10px;

    span {
      &:first-of-type {
        margin-right: 16px;
      }
    }
  }

  .svg-container {
    padding: 6px 5px 6px 15px;
    color: $dark_gray;
    vertical-align: middle;
    width: 30px;
    display: inline-block;
  }

  .title-container {
    position: relative;

    .title {
      font-size: 26px;
      color: $light_gray;
      margin: 0px auto 40px auto;
      text-align: center;
      font-weight: bold;
    }
  }

  .show-pwd {
    position: absolute;
    right: 10px;
    top: 7px;
    font-size: 16px;
    color: $dark_gray;
    cursor: pointer;
    user-select: none;
  }

  .thirdparty-button {
    position: absolute;
    right: 0;
    bottom: 6px;
  }

  @media only screen and (max-width: 470px) {
    .thirdparty-button {
      display: none;
    }
    .choice-dialog {
      width: 90%;
      max-width: 100%;
      margin: 0 auto;
      position: absolute;
      .choice-page{
        background-color: white;
        display: flex;
        flex-direction: column;
        flex-wrap: wrap;
        justify-content: space-around;
        align-items: center;
        .el-card{
          width: 100%;
          position: relative;
          padding: 10px 0px 10px 0px;
          .image{
            width: 100%;
            height: 100%;
          }
          .app-name{
            padding: 14px;
            display: flex;
            justify-content: center;
            font-size: 18px;
          }
          transition: all 0.2s linear;
          &:hover {
            transform: scale(1.1, 1.1);
            filter: contrast(130%);
            cursor: pointer;
            .app-name{
              font-weight: bold;
              color: #3a3a3a;
            }
          }
        }
      }
    }
  }
}
</style>
