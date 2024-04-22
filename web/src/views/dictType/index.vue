<!--字典管理-->
<template>
  <div class="app-container">
    <!--  查询条件  -->
    <div class="filter-container">
      <div class="filter-item">
        <span class="filter-item-label">类型编码: </span>
        <el-input v-model="table.filter.code" placeholder="请输入需要查询字典类型编码" clearable/>
      </div>
      <div class="filter-item">
        <span class="filter-item-label">类型名称: </span>
        <el-input v-model="table.filter.name" placeholder="请输入需要查询字典类型名称" clearable/>
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
        <el-button v-permission="'sys:user:add'" class="el-button--small" icon="el-icon-plus" type="primary" @click="dialog.dictType.create.enabled=true">
          新增
        </el-button>
        <el-button v-permission="'sys:user:delete'" class="el-button--small" icon="el-icon-delete" type="danger" @click="deleteDictTypes">
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
          <div class="right-tool-btn" @click="loadPageDictType">
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
          v-loading="table.isLoading"
          border
          :data="table.data"
          row-key="id"
          style="width: 100%"
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
            label="序号"
            prop="serialNumber"
            align="center"
        />
        <el-table-column
            label="类型编码"
            min-width="50"
            prop="code"
            sortable
        />
        <el-table-column
            label="类型名称"
            prop="name"
            min-width="50"
            sortable
            show-overflow-tooltip
        />
        <el-table-column
            label="字典数量"
            prop="dictNumber"
            min-width="50"
            sortable
        />


        <el-table-column
            label="状态"
            width="80"
            prop="enabled"
            align="center"
        >
          <!--   :disabled="permissionDisabled('sys:dict:enable')"     -->
          <template v-slot="scope">
            <el-switch
                v-model="scope.row.enabled"

                :active-value="true"
                :inactive-value="false"
                @change="changeDictTypeEnabled(scope.row)"
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
                  icon="el-icon-info"
                  :underline="false"
                  type="primary"
                  @click="drawerDictOpen(scope.row)"
              >详情</el-link>
              <el-link
                  v-permission="'sys:user:edit'"
                  icon="el-icon-edit"
                  :underline="false"
                  type="primary"
                  @click="editDictType(scope.row)"
              >编辑</el-link>
              <el-link
                  v-permission="'sys:user:delete'"
                  icon="el-icon-delete"
                  :underline="false"
                  type="danger"
                  @click="deleteDictType(scope.row)"
              >删除</el-link>
            </div>
          </template>
        </el-table-column>
      </el-table>
      <!-- 分页控件 -->
      <el-pagination
          :current-page="table.page"
          :pager-count="table.pagerCount"
          :page-size="table.size"
          :page-sizes="table.pageSizes"
          :total="table.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
      />
    </div>

    <!--  新增字典类型  -->
    <el-dialog title="新增类型" width="600px" :visible.sync="dialog.dictType.create.enabled" @close="dialogDictTypeCreateCancel">
      <el-form ref="dialogDictTypeCreateForm" :model="dialog.dictType.create.data" :rules="dialog.dictType.rules" label-width="80px">
        <el-form-item label="类型编码" prop="code">
          <el-input v-model="dialog.dictType.create.data.code" placeholder="请输入类型编码" clearable/>
        </el-form-item>
        <el-form-item label="类型名称" prop="name">
          <el-input v-model="dialog.dictType.create.data.name" placeholder="请输入类型名称" clearable/>
        </el-form-item>
        <el-form-item label="配置模板" prop="template">
          <el-input v-model="dialog.dictType.create.data.template"  type="textarea" :rows="4" placeholder="请输入类型基础配置JSON模板" clearable/>
        </el-form-item>
        <el-form-item label="激活状态" prop="enabled">
          <el-select
              v-model="dialog.dictType.create.data.enabled"
              placeholder="请选择激活状态"
          >
            <el-option
                v-for="item in [{label : '已激活', value : true},{label : '未激活', value : false}]"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="dialog.dictType.create.data.remark" placeholder="请输入类型备注" clearable/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogDictTypeCreateCancel">取 消</el-button>
        <el-button type="primary" @click="dialogDictTypeCreateSubmit()">确 定</el-button>
      </div>
    </el-dialog>

    <!--  编辑字典类型  -->
    <el-dialog title="修改类型" width="600px" :visible.sync="dialog.dictType.edit.enabled" @close="dialogDictTypeEditCancel">
      <el-form ref="dialogDictTypeEditForm" :model="dialog.dictType.edit.data" :rules="dialog.dictType.rules" label-width="80px">
        <el-form-item label="类型编码" prop="code">
          <el-input v-model="dialog.dictType.edit.data.code" placeholder="请输入类型编码" clearable/>
        </el-form-item>
        <el-form-item label="类型名称" prop="name">
          <el-input v-model="dialog.dictType.edit.data.name" placeholder="请输入类型名称" clearable/>
        </el-form-item>
        <el-form-item label="配置模板" prop="template">
          <el-input v-model="dialog.dictType.edit.data.template"  type="textarea" :rows="4" placeholder="请输入类型基础配置JSON模板" clearable/>
        </el-form-item>
        <el-form-item label="激活状态" prop="enabled">
          <el-select
              v-model="dialog.dictType.edit.data.enabled"
              placeholder="请选择激活状态"
          >
            <el-option
                v-for="item in [{label : '已激活', value : true},{label : '未激活', value : false}]"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="dialog.dictType.edit.data.remark" placeholder="请输入类型备注" clearable/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogDictTypeEditCancel">取 消</el-button>
        <el-button type="primary" @click="dialogDictTypeEditSubmit()">确 定</el-button>
      </div>
    </el-dialog>

    <!--  导入字典类型  -->
    <UploadSingleExcel
        @close="closeUploadSingleExcelHandler"
        :import-dialog-title="uploadSingleExcelAttr.title"
        :show-import-dialog="uploadSingleExcelAttr.showImportDialog"
        :action="uploadSingleExcelAttr.action"
        :on-success-callback="loadPageDictType"
        :download-template="downloadImportTemplate"
    />
  </div>
</template>

<script>
import {
  changeEnabledBaseDictTypeApi,
  createBaseDictTypeApi,
  deleteDictTypesApi,
  getBaseDictTypeByIdApi,
  pageDictTypeApi,
  updateBaseDictTypeApi,
} from '@/api/dict'
import {isNotEmpty} from "@/utils/assertUtil";
import {API_PREFIX} from "@/constant/commons";
import {JsonText, SimpleCode} from "@/utils/ElementValidatorUtil";
import {Message} from "element-ui";
import {filterNullUndefined} from "@/utils/common";
import {exportDictTypeApi, exportDictTypeTemplateApi} from "@/api/file";

export default {
  name: 'DictTypePage',
  components: {
    DictTypeSelect: () => import('@/components/Dict/DictTypeSelect'),
    UploadSingleExcel: () => import('@/components/UploadExcel/UploadSingleExcel.vue'),
  },
  data() {
    return {
      elDropdownItemClass: ['el-dropdown-item--click', undefined, undefined],
      EL_TABLE: {
        // 显示大小
        size: 'medium'
      },
      // 表格中的用户
      table: {
        filter: {
          code: undefined,
          name: undefined,
        },
        isLoading: false,
        // 复选框选中的行ID
        checkIds: [],
        data: [],
        page: 1,
        size: this.$globalVariable.PAGE_SIZES[0],
        pagerCount: this.$globalVariable.PAGER_COUNT,
        total: 0,
        totalPage: 0,
        pageSizes: this.$globalVariable.PAGE_SIZES
      },
      // 新增编辑弹窗
      dialog:{
        dictType: {
          create: {
            enabled: false,
            data: {
              code: null,
              name: null,
              template: null,
              enabled: true,
              remark: null,
            },
          },
          edit: {
            enabled: false,
            data: {
              id: null,
              code: null,
              name: null,
              template: null,
              enabled: true,
              remark: null,
            },
          },
          rules: { // 弹窗的规则
            code: [
              {required: true, max: 16, message: '请输入16位及以内的类型编码', trigger: 'blur'},
              {required: true, validator: SimpleCode, trigger: 'blur'}
            ],
            name: [
              {required: true, max: 16, message: '请输入16位及以内的类型名称', trigger: 'blur'},
            ],
            template: [
              {required: false, validator: JsonText, trigger: 'blur'}
            ],
            remark: [
              {max: 255, message: '长度在 0 到 255 个字符', trigger: 'blur'},
            ],
          }
        }
      },

      headers: {}, // 请求头

      // 导入组件所需相关参数
      uploadSingleExcelAttr: {
        title: '导入字典类型',
        showImportDialog: false,
        action: `${API_PREFIX}/import-export/import-dict-type`
      },
    }
  },
  mounted() {
    // 优先加载表格数据
    this.loadPageDictType()
    console.log(this.$router.currentRoute);
    // 强制渲染，解决表格 固定列后，列错位问题
    this.$nextTick(() => {
      this.$refs.table.doLayout()
    })
  },
  methods: {
    /**
     * 点击查询按钮
     */
    searchFunc() {
      this.table.page = 1
      this.loadPageDictType()
    },
    /**
     * 重置查询条件，并重新查询数据
     */
    resetSearchFilter() {
      // 赋默认值
      this.table.filter = {
        code: undefined,
        name: undefined,
      };
    },
    /**
     * 分页查询字典类型
     */
    loadPageDictType() {
      this.table.isLoading = true;
      const pageParam = {
        page: this.table.page,
        size: this.table.size,
        code: this.table.filter.code,
        name: this.table.filter.name,
      }
      pageDictTypeApi(pageParam).then(data => {
        const content = data.content

        // 修改分页组件
        this.table.page = Number(data.page)
        this.table.size = Number(data.size)
        this.table.total = Number(data.total)
        this.table.totalPage = Number(data.totalPage)

        // 将原先的数据丢弃
        this.table.data = []

        // 添加到数据集合
        content.forEach((item, index, arr) => {
          const column = {
            ...item,
          }
          this.table.data.push(column)
        })
      }).finally(() => {
        this.table.isLoading = false;
      })
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
    /**
     * 更改每页显示多少条
     * @param {Number} val 每页显示多少记录数
     */
    handleSizeChange(val) {
      console.log(`每页 ${val} 条`)
      this.table.size = val
      this.loadPageDictType()
    },
    /**
     * 修改当前页码
     * @param {Number} val 分页的页码值
     */
    handleCurrentChange(val) {
      console.log(`当前页: ${val}`)
      this.table.page = val
      this.loadPageDictType()
    },
    /**
     * 编辑字典类型
     * @param {Object} row 被编辑的行数据
     */
    editDictType(row) {
      // 查询最新的数据
      getBaseDictTypeByIdApi(row.id).then(data => {
        // 数据赋值
        this.dialog.dictType.edit.data = {
          id: data.id,
          code: data.code,
          name: data.name,
          template: data.template,
          enabled: data.enabled,
          remark: data.remark,
        };
        // 打开修改弹窗
        this.dialog.dictType.edit.enabled = true;
      })
    },
    /**
     * 切换激活状态
     * @param row
     */
    changeDictTypeEnabled(row) {
      let parameter = {id: row.id};
      changeEnabledBaseDictTypeApi(parameter).then(response => {
        // 保存成功
        Message({
          message: '修改成功',
          type: 'success',
        })
      })
    },
    /**
     * 批量删除字典类型
     */
    deleteDictTypes() {
      const ids = this.table.checkIds;
      isNotEmpty(ids, () => this.$message.warning("请勾选需要删除的字典类型"))
        .then(() => {
          this.$confirm('此操作将永久删除所选类型, 是否继续?', '删除', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            deleteDictTypesApi(ids).then(data => {
              this.$message.success("删除成功")
              this.loadPageDictType()
            })
          }).catch(reason => {
            console.error(reason)
            this.$message.info("已取消删除");
          })
        }).catch(() => {});
    },
    /**
     * 删除字典类型
     * @param {Object} row 被删除的行数据
     */
    deleteDictType(row) {
      const id = row.id;
      this.$confirm('此操作将永久删除所选类型, 是否继续?', '删除', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteDictTypesApi([id]).then(data => {
          this.$message.success("删除成功")
          this.loadPageDictType()
        })
      }).catch(() => {
        this.$message.info("已取消删除");
      })
    },
    /**
     * 复选框选中
     * @param {Object} rows 选中的行数据
     */
    selectionChangeFunc(rows) {
      this.table.checkIds = rows.map(m => m.id)
    },

    /**
     * 下载模板
     */
    downloadImportTemplate() {
      exportDictTypeTemplateApi();
    },
    /**
     * 关闭弹窗
     */
    closeUploadSingleExcelHandler() {
      this.uploadSingleExcelAttr.showImportDialog = false
    },
    /**
     * 导出Excel
     */
    exportExcel() {
      const pageParam = {
        code: this.table.filter.code,
        name: this.table.filter.name,
      }
      // 如果勾选了就导出勾选的
      const data = {
        ids: this.table.checkIds,
        pageReq: { // 查询条件
          ...pageParam
        },
      }
      exportDictTypeApi(data);
    },

    /**
     * 字典类型创建弹框点击取消
     */
    dialogDictTypeCreateCancel() {
      this.dialog.dictType.create.enabled = false;
      this.$refs.dialogDictTypeCreateForm.resetFields();
    },
    /**
     * 字典类型创建弹框点击提交
     */
    dialogDictTypeCreateSubmit() {
      console.log("dialogDictTypeCreateSubmit")
      this.$refs.dialogDictTypeCreateForm.validate((valid) => {
        if (valid) {
          createBaseDictTypeApi(filterNullUndefined(this.dialog.dictType.create.data)).then(response => {
            // 保存成功
            Message({
              message: '字典类型创建成功',
              type: 'success',
            })
            this.dialogDictTypeCreateCancel()
            this.loadPageDictType();
          })
        } else {
          console.error("校验参数失败")
          return false;
        }
      });
    },
    /**
     * 字典类型编辑弹框点击取消
     */
    dialogDictTypeEditCancel() {
      this.dialog.dictType.edit.enabled = false;
      this.$refs.dialogDictTypeEditForm.resetFields();
    },
    /**
     * 字典类型编辑弹框点击提交
     */
    dialogDictTypeEditSubmit() {
      this.$refs.dialogDictTypeEditForm.validate((valid) => {
        if (valid) {
          updateBaseDictTypeApi(this.dialog.dictType.edit.data).then(response => {
            // 保存成功
            Message({
              message: '修改成功',
              type: 'success',
            })
            this.dialogDictTypeEditCancel()
            this.loadPageDictType();
          })
        } else {
          return false;
        }
      });
    },

  }
}
</script>

<style lang="scss">

</style>
