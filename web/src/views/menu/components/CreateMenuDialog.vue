<!--新增菜单的弹框-->
<template>
  <el-dialog title="新增菜单" width="720px" :visible.sync="visible" @close="close">
    <el-form ref="addMenuForm1" :model="menu" :rules="rules" label-width="100px" :inline="true" label-position="right">
      <el-row>
        <el-col :span="12">
          <el-form-item label="上级菜单:" prop="parentId">
            <!--  选择器选项以树形控件展示  -->
            <el-select ref="selectParentMenu" v-model="parentMenu.id" placeholder="请选择上级菜单" clearable>
              <el-option :key="parentMenu.id" :value="parentMenu.id" :label="parentMenu.name" hidden />
              <el-tree
                :data="menuData"
                :props="menuProps"
                node-key="id"
                accordion
                highlight-current
                @node-click="handleMenuNodeClick"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="菜单类型:" prop="type">
            <el-radio-group v-model="menu.type">
              <el-radio v-for="item in menuTypes" :label="item.value">{{item.label}}</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="菜单名称:" prop="name">
            <el-input v-model="menu.name" clearable placeholder="请输入菜单名称" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="权限标识:" :prop="(menu.type === 1 || menu.type === 2) ? 'permissionId' : 'notPermissionId'">
            <el-input v-model="menu.permissionId" placeholder="请输入权限标识" clearable :disabled="menu.type === 0" />
          </el-form-item>
        </el-col>
      </el-row>
      <!--分隔符-->
      <el-row>
        <el-col :span="24">
          <div class="cutting-line el-divider el-divider--horizontal" />
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item :label="routePath.label" :prop="(menu.type === 1 || menu.type === 3) ? 'path' : 'notPath'">
            <el-input v-model="menu.path" :placeholder="routePath.placeholder" clearable />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="请求方式:" :prop="menu.type === 3 ? 'method' : 'notMethod'">
            <el-select
              v-model="menu.method"
              multiple
              placeholder="请选择请求方式"
              :disabled="menu.type === 1"
            >
              <el-option
                v-for="item in methods"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              >
                <el-tag size="small" :class="item.label" class="http_method_tag">{{ item.label }}</el-tag>
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="排序号:" prop="sortNum">
            <el-input-number v-model="menu.sortNum" :disabled="menu.type === 2" controls-position="right" :min="1" :max="99999" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否展示:" prop="hide">
            <el-switch
              v-model="menu.hide"
              :active-value="false"
              :inactive-value="true"
              :disabled="menu.type === 0"
            />
            <el-tooltip placement="top">
              <div slot="content">选择不展示只注册路由不显示在侧边栏，比如添加页面应该选择不展示</div>
              <i class="el-icon-warning-outline question" />
            </el-tooltip>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <el-form ref="addMenuForm2" :model="menu" :rules="rules" label-width="100px" label-position="right">
      <el-form-item label="菜单元数据:" prop="meta">
        <el-input v-model="menu.meta" type="textarea" :rows="5" placeholder="请输入JSON格式的路由元数据" :disabled="menu.type === 0" />
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="resetForm()">取 消</el-button>
      <el-button type="primary" @click="submitForm()">保 存</el-button>
    </div>
  </el-dialog>
</template>

<script>

import { EL_ICONS, HTTP_METHOD_ARRAY, MENU_TYPE_ARRAY } from "@/constant/commons";
import { isJSON } from "@/utils/validate";
import { addMenuApi, initMenuApi } from "@/api/menu";
import { simpleCreateUserApi } from "@/api/user";
import { Message } from "element-ui";
export default {
  name: 'CreateMenuDialog',
  props: {
    // 弹框
    createMenuDialog: {
      required: true,
      type: Boolean,
      default: function() {
        return false;
      }
    },
    // 父组件刷新菜单数据
    refreshMenu: {
      type: Function,
      default: null
    }
  },
  data() {
    const validateMeta = (rule, value, callback) => {
      isJSON(value).then(boo => {
        console.log(boo)
        if (!boo) {
          callback(new Error(""));
        } else {
          callback();
        }
      })
    };
    return {
      menuData: undefined,
      menuProps: {
        label: 'name',
        children: 'children'
      },
      parentMenu: {
        id: '',
        name: ''
      },
      icons: [],
      iconInputClass: 'default-icon-input-class',
      routePath: {
        label: '路由地址:',
        placeholder: '请输入路由地址'
      },
      methods: HTTP_METHOD_ARRAY,
      visible: false,
      menuTypes: MENU_TYPE_ARRAY,
      menu: {
        parentId: undefined,
        type: 1,
        name: undefined,
        openModel: 0,
        icon: '',
        permissionId: undefined,
        path: undefined,
        method: undefined,
        sortNum: 0,
        hide: false,
        meta: undefined,
        remark: '',
      },
      rules: {
        name: [
          { required: true, message: '请输入菜单名称', trigger: 'blur' }
        ],
        type:[
          { required: true, message: '请选择菜单类型', trigger: 'blur' }
        ],
        permissionId: [
          { required: true, max: 255, message: '权限标识必填', trigger: 'blur' }
        ],
        path: [
          { required: true, max: 255, message: '路由或接口地址必填', trigger: 'blur' }
        ],
        method:[
          { required: true, message: '接口请求方式必填', trigger: 'blur' }
        ],
        remark: [
          { required: false, max: 255, message: '备注限制最多255字符', trigger: 'blur' }
        ],
        sortNum: [
          { required: true, type: 'number', min: 1, max: 99999, message: '请输入排序号', trigger: 'blur' }
        ],
        meta: [
          { required: false, message: '请输入正确的JSON格式', trigger: 'blur', validator: validateMeta }
        ]
      }
    };
  },
  watch: {
    createMenuDialog() {
      this.visible = this.createMenuDialog;
      if (this.visible) {
        this.menuData = this.$store.getters.allMenus;
      }
    },
    'parentMenu.id'(){
      if (this.parentMenu.id == "") {
        this.parentMenu.name = ''
      }
    },
    'menu.type'() {
      // 修改菜单类型时，去除之前得验证
      this.$refs.addMenuForm1.clearValidate();
      switch (this.menu.type) { // 1：菜单；2：按钮；3：接口
        case 1:
          this.menu.method = undefined
          this.menu.hide = false
          this.routePath = {
            label: '路由地址:',
            placeholder: '请输入路由地址'
          }
          break;
        case 2:
          this.menu.method = undefined
          this.menu.hide = false
          this.routePath = {
            label: '请求地址:',
            placeholder: '请输入路由或接口地址'
          }
          break;
        case 3:
          this.menu.hide = true
          this.routePath = {
            label: '接口地址:',
            placeholder: '请输入接口地址'
          }
          break;
        default:
          break;
      }
    },
  },
  methods: {
    handleMenuNodeClick(data) {
      // 使 input 失去焦点，并隐藏下拉框
      this.$refs.selectParentMenu.blur()

      this.parentMenu = {
        id: data.id,
        name: data.name
      }
      this.menu.parentId = data.id
    },
    // 重置表单
    resetForm() {
      this.parentMenu = {
        id: '',
        name: ''
      }
      this.menu = {
        parentId: undefined,
        type: 1,
        name: undefined,
        permissionId: undefined,
        path: undefined,
        method: undefined,
        sortNum: 0,
        hide: false,
        meta: undefined,
        remark: '',
      }
      this.close();
    },
    submitForm() {
      this.$refs['addMenuForm1'].validate((valid) => {
        if (valid) {
          let data = { ...this.menu };
          data.method = JSON.stringify(this.menu.method);
          addMenuApi(data).then(data => {
            this.$message.success("添加成功");
            this.refreshMenu();
            this.close();
          })
        } else {
          return false;
        }
      });
    },
    close() {
      this.$emit("update:createMenuDialog", false)
    }
  },
}
</script>
<style lang="scss" scoped>
@import '@/styles/variables.scss';
.cutting-line{
  margin: 8px 0 30px 0;
}

.question{
  display: inline-block;
  position: absolute;
  margin: 13px 6px;
}
.el-form-item {
  .el-input-number--medium {
    width: 230px;
  }
  .el-input {
    width: 230px;
  }
  .el-select{
    width: 230px;
  }
}
::v-deep .default-icon-input-class{
  .el-input--prefix .el-input__inner {
    padding-left: 15px !important;
  }
}
::v-deep .icon-input-class {
  .el-input--prefix .el-input__inner {
    padding-left: 30px !important;
  }
}
::v-deep .icon-el-select .el-scrollbar__view {
  display: flex;
  justify-content: space-around;
  width: 230px;
  flex-flow: wrap;
  padding: 6px;
  li {
    width: 45px;
    height: 45px;
    border: 1px solid #ededed;
    border-radius: 4px;
    padding: 12px;
    margin-top: 10px;
  }
  .selected{
    border: 1px solid #409EFF;
  }
  i{
    font-size: 19px;
    position: absolute;
  }
}
.http_method_tag{
  width: 70px;
  text-align: center;
}
::v-deep .GET{
  background-color: $GET;
  color: #fff
}
::v-deep .POST{
  background-color: $POST;
  color: #fff
}
::v-deep .PUT{
  background-color: $PUT;
  color: #fff
}
::v-deep .DELETE{
  background-color: $DELETE;
  color: #fff
}
::v-deep .PATCH{
  background-color: $PATCH;
  color: #fff
}
::v-deep .OPTIONS{
  background-color: $OPTIONS;
  color: #fff
}
::v-deep .TRACE{
  background-color: $TRACE;
  color: #fff
}
::v-deep .HEAD{
  background-color: $HEAD;
  color: #fff
}
.el-select-dropdown.is-multiple .el-select-dropdown__item.selected::after {
  position: absolute;
  right: 20px;
  font-family: "element-icons";
  content: "";
  font-size: 12px;
  font-weight: bold;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}
</style>
