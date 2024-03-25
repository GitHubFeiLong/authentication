<template>
  <div class="lock">
    <div>
      <img src="../../assets/img/lock/lock-icon.png" alt="">
      <el-input class="lockPasswdInp" v-show="!passwdError" ref="lockPasswdInp" size="small" show-password placeholder="密码" v-model="passwd">
        <template slot="append">
          <el-button @click="unLock()" style="background: transparent;">
            <i class="el-icon-right"></i>
          </el-button>
        </template>
      </el-input>
      <div class="passwdError-container" v-show="passwdError">
        <div class="passwdError">密码不正确。请再试一次。</div>
        <div class="confirm-btn" @click="reset()">确认</div>
      </div>
    </div>
    <!-- <img @click="logout" class="logout" src="../../assets/img/system/logout.png"> -->
  </div>
</template>
<script>
/**
 * 注意：由于遮罩层优先级太高 类似于$message之类的消息提示等会显示不出来
 */
export default {
  data() {
    return {
      passwd: '',
      passwdError: false
    }
  },
  computed: {
    lockPassword: () => process.env.VUE_APP_LOCK_PASSWORD || '123456'
  },
  mounted() {
    this.reset()
    document.addEventListener('keydown', this.keyDownHandle)
    document.addEventListener('contextmenu', this.contextmenuHandle)
  },
  destroyed() {
    document.removeEventListener('keydown', this.keyDownHandle)
    document.removeEventListener('contextmenu', this.contextmenuHandle)
  },
  methods:{
    // 重置初始化
    reset() {
      this.passwdError = false
      this.passwd = ''
      this.$nextTick(() => {
        this.$refs.lockPasswdInp.focus()
      })
    },
    // 解锁
    unLock() {
      if(this.passwd != this.lockPassword) {
        return this.passwdError = true
      }
      this.$store.commit('common/updatelockTime', 0)
    },
    // 监听鼠标按下事件，阻止 F12 事件
    keyDownHandle(event) {
      if(event.keyCode == 13) {
        if(this.passwdError) {
          this.reset()
        } else {
          this.unLock()
        }
      }
      return (event.keyCode !== 123 ) || (event.returnValue = false)
    },
    // 阻止鼠标右键事件
    contextmenuHandle(event) {
      return event.returnValue = false;
    },
    // // 退出登录
    // logout() {
    //   this.$store.commit('common/updatelockTime', 0)
    //   this.$router.replace('/login')
    //   /**
    //    * 走接口 清楚本地缓存等
    //    * ...
    //    */
    // }
  }
}
</script>
<style scoped>
.lock {
  width: 100%;
  height: 100vh;
  background: #ccc;
  position: fixed;
  top: 0;
  left: 0;
  z-index: 999999;
  display: flex;
  justify-content: center;
  align-items: center;
  background-image: url('../../assets/img/lock/lock-bg.jpg');
  background-repeat: no-repeat;
  background-size: 100% 100%;
}
.lock > div {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.lockPasswdInp {
  margin-top: 8px;
}

/deep/ .el-input__inner {
  background-color: transparent !important;
  border: 1px solid #0076c8 !important;
  color: #fff;
}
/deep/ .el-input-group__append {
  background-color: rgba(6, 14, 22, .5);
  border: 1px solid #0076c8;
  border-left-color: transparent;
}

/deep/ .el-input-group__append:hover {
  background-color: rgba(6, 14, 22, .6);
  cursor: pointer;
}

.passwdError-container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.passwdError {
  width: 260px;
  text-align: center;
  color: #fff;
  font-size: 13px;
  margin: 10px 0;
}

.confirm-btn {
  width: 70px;
  height: 28px;
  text-align: center;
  line-height: 28px;
  color: #fff;
  font-size: 13px;
  background-color: rgba(6, 14, 22, .5);
  border: 1px solid #0076c8;
  border-radius: 3px;
  cursor: pointer;
}

.confirm-btn:hover {
  background-color: rgba(6, 14, 22, .8);
}

/* .logout {
  position: fixed;
  bottom: 20px;
  right: 20px;
  height: 40px;
  cursor: pointer;
} */
</style>
