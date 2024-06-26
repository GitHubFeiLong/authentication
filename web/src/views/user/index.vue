<template>
  <div class="app-container">
    <!--  查询条件  -->
    <div class="filter-container">
      <div class="filter-item">
        <span class="filter-item-label">用户名: </span>
        <!--<UserSelect ref="userSelectRef" @getSelectedUser="getSelectedUser" />-->
        <el-input v-model="filter.username" placeholder="请输入需要查询的用户名" clearable/>
      </div>
      <div class="filter-item">
        <span class="filter-item-label">有效日期: </span>
        <el-date-picker
          v-model="filter.validTime"
          :picker-options="pickerOptions"
          align="center"
          end-placeholder="结束日期"
          range-separator="至"
          start-placeholder="开始日期"
          type="daterange"
          unlink-panels
          value-format="yyyy-MM-dd"
        />
      </div>
      <div class="filter-item">
        <el-button
          v-permission="'sys:user:query'"
          icon="el-icon-search"
          type="primary"
          @click="searchFunc"
        >
          查询
        </el-button>
      </div>
      <div class="filter-item">
        <!--不加icon会小一个像素的高度-->
        <el-button icon="el-icon-setting" @click="resetSearchFilter">重置</el-button>
      </div>
    </div>
    <!--顶部操作栏-->
    <div class="el-table-tool">
      <div class="left-tool">
        <el-button v-permission="'sys:user:add'" class="el-button--small" icon="el-icon-plus" type="primary" @click="createUserDialog=true">
          新增
        </el-button>
        <el-button v-permission="'sys:user:delete'" class="el-button--small" icon="el-icon-delete" type="danger" @click="deleteUsers">
          删除
        </el-button>
        <el-button v-permission="'sys:user:import'" class="el-button--small" icon="el-icon-upload2" @click="uploadSingleExcelAttr.showImportDialog=true">
          导入
        </el-button>
        <el-button v-permission="'sys:user:export'" class="el-button--small" icon="el-icon-download" @click="exportExcel">
          导出
        </el-button>
      </div>
      <div class="right-tool">
        <el-tooltip class="right-tool-btn-tooltip" effect="dark" content="刷新" placement="top">
          <div class="right-tool-btn" @click="loadPageUser">
            <i class="el-icon-refresh-right" />
          </div>
        </el-tooltip>
        <el-tooltip class="right-tool-btn-tooltip" effect="dark" content="密度" placement="top">
          <el-dropdown trigger="click" @command="changeElTableSizeCommand">
            <div class="right-tool-btn">
              <i class="el-icon-s-operation" />
            </div>
            <el-dropdown-menu slot="dropdown" size="small">
              <el-dropdown-item :class="elDropdownItemClass[0]" command="0,medium">默认</el-dropdown-item>
              <el-dropdown-item :class="elDropdownItemClass[1]" command="1,small">中等</el-dropdown-item>
              <el-dropdown-item :class="elDropdownItemClass[2]" command="2,mini">紧凑</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </el-tooltip>
      </div>
    </div>
    <div class="el-table__body__pagination">
      <!-- 表格  -->
      <el-table
          ref="table"
          v-loading="isLoading"
          border
          :data="user.users"
          row-key="id"
          style="width: 100%"
          :row-class-name="tableRowClassName"
          :header-cell-style="{background:'#FAFAFA', color:'#000', height: '30px',}"
          :header-row-class-name="EL_TABLE.size"
          :size="EL_TABLE.size"
          @selection-change="selectionChangeFunc"
      >
        <el-table-column
            width="50"
            type="selection"
            header-align="center"
            align="center"
            class-name="selection"
        />
        <el-table-column
            width="50"
            fixed
            label="序号"
            prop="serialNumber"
            align="center"
        />
        <el-table-column
            label="用户名"
            min-width="50"
            prop="username"
            sortable
        />
        <el-table-column
            label="角色"
            min-width="100"
            sortable
            show-overflow-tooltip
        >
          <template v-slot="scope">
          <span v-for="item in scope.row.roles" :key="item.id">
            <el-tag size="small">{{ item.name }}</el-tag> <br>
          </span>
          </template>
        </el-table-column>
        <el-table-column
            label="账号有效期"
            width="170"
            prop="validTime"
            show-overflow-tooltip
            sortable
        />

        <el-table-column
            label="激活"
            width="80"
            prop="enabled"
            align="center"
        >
          <template v-slot="scope">
            <el-switch
                v-model="scope.row.enabled"
                :disabled="permissionDisabled('sys:user:enable')"
                :active-value="true"
                :inactive-value="false"
                @change="changeEnabled(scope.row)"
            />
          </template>
        </el-table-column>
        <el-table-column
            label="锁定"
            width="80"
            prop="locked"
            align="center"
        >
          <template v-slot="scope">
            <el-switch
                v-model="scope.row.locked"
                :disabled="permissionDisabled('sys:user:lock')"
                :active-value="true"
                :inactive-value="false"
                active-color="#F56C6C"
                inactive-color="#C0CCDA"
                @change="changeLocked(scope.row)"
            />
          </template>
        </el-table-column>
        <el-table-column
            label="备注"
            min-width="180"
            prop="remark"
            show-overflow-tooltip
        />
        <el-table-column
            label="创建时间"
            width="170"
            prop="createdDate"
            show-overflow-tooltip
            sortable
        />
        <el-table-column
            label="操作"
            width="230"
            align="center"
        >
          <template v-slot="scope">
            <div class="el-link-parent">
              <el-link
                  v-permission="'sys:user:edit'"
                  icon="el-icon-edit"
                  :underline="false"
                  type="primary"
                  @click="editUser(scope.row)"
              >编辑</el-link>
              <el-link
                  v-permission="'sys:user:reset-password'"
                  icon="el-icon-key"
                  :underline="false"
                  type="warning"
                  @click="resetPassword(scope.row)"
              >重置密码</el-link>
              <el-link
                  v-permission="'sys:user:delete'"
                  icon="el-icon-delete"
                  :underline="false"
                  type="danger"
                  @click="deleteUser(scope.row)"
              >删除</el-link>
            </div>
          </template>
        </el-table-column>
      </el-table>
      <!-- 分页控件 -->
      <el-pagination
          :current-page="user.page"
          :pager-count="user.pagerCount"
          :page-size="user.size"
          :page-sizes="user.pageSizes"
          :total="user.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
      />
    </div>
    <!-- 新增用户弹框 -->
    <CreateUserDialog :create-user-dialog.sync="createUserDialog" />
    <!-- 编辑用户弹框 -->
    <EditUserDialog :edit-user-dialog.sync="editUserDialog" :edit-user-info="editUserInfo" @closeEditUserDialog="closeEditUserDialog" />
    <!--  导入角色  -->
    <UploadSingleExcel
        @close="closeUploadSingleExcelHandler"
        :import-dialog-title="uploadSingleExcelAttr.title"
        :show-import-dialog="uploadSingleExcelAttr.showImportDialog"
        :action="uploadSingleExcelAttr.action"
        :on-success-callback="loadPageUser"
        :download-template="downloadImportTemplate"
    />
  </div>
</template>

<script>
import waves from '@/directive/waves' // waves directive
import {
  pageUsersApi,
  deleteUserById,
  resetPasswordApi,
  changeEnabledApi,
  deleteUserByIdsApi,
  changeLockedApi,
} from '@/api/user'
import { exportUserApi, exportUserTemplateApi } from "@/api/file";
import { isNotEmpty, isTrue } from "@/utils/assertUtil";
import { beforeUploadExcel } from "@/utils/updateUtil";
import {API_PREFIX, DATE_PICKER_DEFAULT_OPTIONS} from "@/constant/commons";
import LocalStorageUtil from "@/utils/LocalStorageUtil";
import { AUTHORIZATION, X_APP_ID } from "@/constant/HttpHeaderConst";
import {beforeUploadFillHttpHeader} from "@/utils/fileUtil";

export default {
  name: 'UserPage',
  directives: { waves },
  components: {
    UserSelect: () => import('@/components/User/UserSelect'),
    CreateUserDialog: () => import('@/views/user/components/CreateUserDialog'),
    EditUserDialog: () => import('@/views/user/components/EditUserDialog'),
    UploadSingleExcel: () => import('@/components/UploadExcel/UploadSingleExcel.vue'),
  },
  data() {
    return {
      filter: {
        id: undefined,
        username: undefined,
        validTime: undefined,
        startValidTime: undefined,
        endValidTime: undefined,
      },
      pickerOptions: DATE_PICKER_DEFAULT_OPTIONS,
      // 新增用户 子组件新增用户 弹框显示变量
      createUserDialog: false,
      // 修改用户 子组件弹框 弹框显示变量
      editUserDialog: false,
      // 被编辑的用户信息
      editUserInfo: undefined,
      isLoading: false,
      elDropdownItemClass: ['el-dropdown-item--click', undefined, undefined],
      EL_TABLE: {
        // 显示大小
        size: 'medium'
      },
      // 表格中的用户
      user: {
        users: [],
        page: 1,
        size: this.$globalVariable.PAGE_SIZES[0],
        pagerCount: this.$globalVariable.PAGER_COUNT,
        total: 0,
        totalPage: 0,
        pageSizes: this.$globalVariable.PAGE_SIZES
      },
      // 复选框选中的用户
      checkUserIds: [],
      headers: {}, // 请求头

      // 导入组件所需相关参数
      uploadSingleExcelAttr: {
        title: '导入角色',
        showImportDialog: false,
        action: `${API_PREFIX}/import-export/import-user`
      },
    }
  },
  mounted() {
    // 优先加载表格数据
    this.loadPageUser()
    console.log(this.$router.currentRoute);
    // 强制渲染，解决表格 固定列后，列错位问题
    this.$nextTick(() => {
      this.$refs.table.doLayout()
    })
  },
  methods: {
    // 用户下拉
    getSelectedUser(user) {
      this.filter.id = user ? user.value : undefined;
    },
    // 点击查询按钮
    searchFunc() {
      this.user.page = 1
      this.loadPageUser()
    },
    // 点击重置按钮
    resetSearchFilter() {
      // 清空子组件（用户名下拉）值
      this.$refs.userSelectRef.clear();
      // 赋默认值
      this.filter = {
        id: undefined,
        validTime: undefined,
        startValidTime: undefined,
        endValidTime: undefined,
      };
    },
    filterTimeHandler() {
      const validTime = this.filter.validTime
      if (validTime && validTime.length > 0) {
        this.filter.startValidTime = this.$moment(validTime[0]).format(this.$globalVariable.DATE_TIME_FORMATTER).toString()
        this.filter.endValidTime = this.$moment(validTime[1])
          .add(1, 'd')
          .subtract(1, 's')
          .format(this.$globalVariable.DATE_TIME_FORMATTER).toString()
      } else {
        this.filter.startValidTime = undefined
        this.filter.endValidTime = undefined
      }

      const createTime = this.filter.createTime
      if (createTime && createTime.length > 0) {
        this.filter.startCreateTime = this.$moment(createTime[0]).format(this.$globalVariable.DATE_TIME_FORMATTER).toString()
        this.filter.endCreateTime = this.$moment(createTime[1])
          .add(1, 'd')
          .subtract(1, 's')
          .format(this.$globalVariable.DATE_TIME_FORMATTER).toString()
      } else {
        this.filter.startCreateTime = undefined
        this.filter.endCreateTime = undefined
      }
    },
    loadPageUser() {
      this.isLoading = true;
      this.filterTimeHandler();
      const pageParam = {
        page: this.user.page,
        size: this.user.size,
        id: this.filter.id,
        username: this.filter.username,
        startValidTime: this.filter.startValidTime,
        endValidTime: this.filter.endValidTime,
      }
      pageUsersApi(pageParam).then(data => {
        const content = data.content

        // 修改分页组件
        this.user.page = Number(data.page)
        this.user.size = Number(data.size)
        this.user.total = Number(data.total)
        this.user.totalPage = Number(data.totalPage)

        // 将原先的数据丢弃
        this.user.users = []

        // 添加到数据集合
        content.forEach((item, index, arr) => {
          const column = {
            ...item,
          }
          this.user.users.push(column)
        })
      }).finally(() => {
        this.isLoading = false;
      })
    },
    tableRowClassName({ row, rowIndex }) {
      // 最近一周创建的显示绿色
      if (row.createTime) {
        const diff = this.$moment(new Date())
          .diff(this.$moment(row.createTime).format(this.$globalVariable.DATE_TIME_FORMATTER), 'days')
        if (diff >= 0 && diff <= 7) {
          return 'success-row'
        }
      }
      // 一个月内过期的账号的显示黄色
      if (row.validTime) {
        const diff = this.$moment(new Date())
          .diff(this.$moment(row.validTime).format(this.$globalVariable.DATE_TIME_FORMATTER), 'days')
        if (diff >= 0 && diff <= 30) {
          return 'warning-row'
        }
      }
      // 其它不显示颜色
      return ''
    },
    // 修改表格大小
    changeElTableSizeCommand(val) {
      const args = val.split(",");
      const idx = Number(args[0]);
      console.log(args)
      this.elDropdownItemClass.map((value, index, array) => {
        if (index === idx) {
          array[index] = "el-dropdown-item--click";
        } else {
          array[index] = undefined;
        }
      })
      console.log(this.elDropdownItemClass)
      this.elDropdownItemClass[args[0]]
      this.EL_TABLE.size = args[1];
    },
    addUser() {
      this.$router.push({ path: '/user/create-user', query: this.otherQuery })
      // this.$router.push({ path: '/user/page/create-user' })
    },
    // 更改每页显示多少条
    handleSizeChange(val) {
      console.log(`每页 ${val} 条`)
      this.user.size = val
      this.loadPageUser()
    },
    // 修改当前页码
    handleCurrentChange(val) {
      console.log(`当前页: ${val}`)
      this.user.page = val
      this.loadPageUser()
    },
    // 编辑用户
    editUser(row) {
      console.log(row, 'row')
      this.editUserInfo = row;
      this.editUserDialog = true;
    },
    // 重置密码
    resetPassword(row) {
      const userId = row.id;
      if (userId <= 100) {
        this.$message.error("不能操作预置用户")
        return
      }
      this.$confirm('此操作将重置该用户密码, 是否继续?', '重置密码', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        resetPasswordApi(userId).then(data => {
          this.$message.success("重置密码成功")
        })
      }).catch(() => {
        this.$message.info("已取消重置密码");
      })
    },
    closeEditUserDialog() {
      this.editUserDialog = false;
    },
    // 切换激活状态
    changeEnabled(row) {
      const userId = row.id;
      if (userId <= 100) {
        this.$message.error("不能操作预置用户")
        return
      }
      changeEnabledApi(userId).then(data => {
        this.$message.success("修改成功")
      })
    },
    // 切换锁定状态
    changeLocked(row) {
      const userId = row.id;
      if (userId <= 100) {
        this.$message.error("不能操作预置用户")
        return
      }
      changeLockedApi(userId).then(data => {
        this.$message.success("修改成功")
      })
    },
    // 批量删除用户
    deleteUsers() {
      const ids = this.checkUserIds;
      isNotEmpty(ids, () => this.$message.warning("请勾选需要删除的用户"))
        .then(() => {
          this.$confirm('此操作将永久删除所选用户, 是否继续?', '删除', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            deleteUserByIdsApi(this.checkUserIds).then(data => {
              this.$message.success("删除成功")
              this.loadPageUser()
            })
          }).catch(reason => {
            console.error(reason)
            this.$message.info("已取消删除");
          })
        }).catch(() => {});
    },
    // 删除单条用户
    deleteUser(row) {
      const userId = row.id;
      if (userId <= 100) {
        this.$message.error("不能操作预置用户")
        return
      }
      this.$confirm('此操作将永久删除该用户, 是否继续?', '删除', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteUserByIdsApi([userId]).then(data => {
          this.$message.success("删除成功")
          this.loadPageUser()
        })
      }).catch(() => {
        this.$message.info("已取消删除");
      })
    },
    // 复选框勾选事件
    selectionChangeFunc(users) {
      const ids = users.map(m => m.id)
      this.checkUserIds = ids
    },

    // 下载模板
    downloadImportTemplate() {
      exportUserTemplateApi();
    },
    // 关闭弹窗
    closeUploadSingleExcelHandler() {
      this.uploadSingleExcelAttr.showImportDialog = false
    },
    // 导出用户
    exportExcel() {
      this.filterTimeHandler();
      const pageParam = {
        id: this.filter.id,
        startValidTime: this.filter.startValidTime,
        endValidTime: this.filter.endValidTime,
      }
      // 如果勾选了就导出勾选的
      const data = {
        ids: this.checkUserIds,
        pageReq: { // 查询条件
          ...pageParam
        },
      }
      exportUserApi(data);
    },
  }
}
</script>

<style lang="scss">

</style>
