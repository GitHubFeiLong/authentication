<!--字典明细抽屉-->
<template>
  <!--   抽屉 字典明细   -->
  <el-drawer custom-class="el-drawer__table"
             size="60%"
             title="字典配置"
             :visible.sync="dictSettingDrawerVisible"
             :append-to-body="true"
  >
    <div>
      <!--  查询条件  -->
      <div class="filter-container">
        <div class="filter-item">
          <span class="filter-item-label">类型编码: </span>
          <DictTypeSelect :default-select-id="dictSetting.table.filter.dictTypeId" :clearable="false" :disabled="true" @changeDictType="changeDictType"/>
        </div>
        <div class="filter-item">
          <span class="filter-item-label">字典编码: </span>
          <DictSelect key="one" :default-select-id="dictSetting.table.filter.dictId" :dict-type-id="dictSetting.table.filter.dictTypeId" :clearable="false" :disabled="true" @changeDict="changeDict"/>
        </div>
        <div class="filter-item">
          <span class="filter-item-label">配置名称: </span>
          <el-input v-model="dictSetting.table.filter.name" placeholder="请输入需要查询字典配置名称" clearable/>
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
      </div>
      <!--顶部操作栏-->
      <div class="el-table-tool">
        <div class="left-tool">
          <el-button v-permission="'sys:user:add'" class="el-button--small" icon="el-icon-plus" type="primary" @click="dictSetting.dialog.create.enabled=true">
            新增
          </el-button>
          <el-button v-permission="'sys:user:delete'" class="el-button--small" icon="el-icon-delete" type="danger" @click="deleteDict(dictSetting.table.checkIds)">
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
            <div class="right-tool-btn" @click="loadPageDictSetting">
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
            v-loading="dictSetting.table.isLoading"
            border
            :data="dictSetting.table.data"
            row-key="id"
            style="width: 100%"
            :header-cell-style="{background:'#FAFAFA', color:'#000', height: '30px',}"
            :header-row-class-name="dictSetting.EL_TABLE.size"
            :size="dictSetting.EL_TABLE.size"
            @selection-change="selectionChangeFunc"
        >
          <el-table-column type="expand">
            <template slot-scope="props">
              <el-form label-position="left" inline class="demo-table-expand">
                <el-form-item label="商品名称">
                  <el-input v-model="props.row.template" type="textarea" :rows="4" placeholder="请输入字典基础配置JSON模板"/>
                </el-form-item>
              </el-form>
            </template>
          </el-table-column>
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
              label="配置名称"
              prop="name"
              min-width="50"
              sortable
              show-overflow-tooltip
          />
          <el-table-column
              label="备注"
              min-width="100"
              prop="remark"
              show-overflow-tooltip
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
                  :disabled="permissionDisabled('sys:usr:enable')"
                  :active-value="true"
                  :inactive-value="false"
                  @change="changeDictTypeEnabled(scope.row)"
              />
            </template>
          </el-table-column>
          <el-table-column
              label="默认"
              width="80"
              prop="defaulted"
              align="center"
          >
            <template v-slot="scope">
              <el-switch
                  v-model="scope.row.defaulted"
                  :disabled="permissionDisabled('sys:usr:enable')"
                  :active-value="true"
                  :inactive-value="false"
                  @change="changeDictTypeEnabled(scope.row)"
              />
            </template>
          </el-table-column>
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
                    @click="editDictType(scope.row)"
                >编辑</el-link>
                <el-link
                    v-permission="'sys:user:delete'"
                    icon="el-icon-delete"
                    :underline="false"
                    type="danger"
                    @click="deleteDict([scope.row.id])"
                >删除</el-link>
              </div>
            </template>
          </el-table-column>
        </el-table>
        <!-- 分页控件 -->
        <el-pagination
            :current-page="dictSetting.table.page"
            :pager-count="dictSetting.table.pagerCount"
            :page-size="dictSetting.table.size"
            :page-sizes="dictSetting.table.pageSizes"
            :total="dictSetting.table.total"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
        />
      </div>
    </div>

    <!--  新增字典配置  -->
    <el-dialog title="新增字典配置" width="600px" :visible.sync="dictSetting.dialog.create.enabled" @close="dialogCreateCancel" :append-to-body="true">
      <el-form ref="dialogCreateForm" :model="dictSetting.dialog.create.data" :rules="dictSetting.dialog.rules" label-width="80px">
        <el-form-item label="字典类型" prop="dictId">
          <DictTypeSelect :default-select-id="dictSetting.dialog.create.data.dictTypeId"
                          :clearable="false"
                          :disabled="true"
                          @changeDictType="changeDictTypeByDialog"/>
        </el-form-item>
        <el-form-item label="字典明细" prop="dictId">
          <DictSelect ref="dialogDictSelectRef"
              :default-select-id.sync="dictSetting.dialog.create.data.dictId"
                      :dict-type-id.sync="dictSetting.dialog.create.data.dictTypeId"
                      :clearable="false"
                      :disabled="true"
                      @changeDict="changeDictByDialog"
          />
        </el-form-item>
        <el-form-item label="配置名称" prop="name">
          <el-input v-model="dictSetting.dialog.create.data.name" placeholder="请输入字典配置名称" clearable/>
        </el-form-item>
        <el-form-item label="配置模板" prop="template">
          <el-input v-model="dictSetting.dialog.create.data.template" type="textarea" :rows="4" placeholder="请输入字典配置明细的JSON注释"/>
        </el-form-item>
        <el-form-item label="配置明细" prop="template">
          <el-input v-model="dictSetting.dialog.create.data.template" type="textarea" :rows="4" placeholder="请输入字典基础配置JSON模板"/>
          <el-button plain class="el-button--small" @click="useParentTemplate(dictSetting.dialog.create.data.dictTypeId)">使用上级配置模板</el-button>
        </el-form-item>
        <el-form-item label="激活状态" prop="enabled">
          <el-select
              v-model="dictSetting.dialog.create.data.enabled"
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
        <el-form-item label="默认状态" prop="enabled">
          <el-select
              v-model="dictSetting.dialog.create.data.enabled"
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
          <el-input v-model="dictSetting.dialog.create.data.remark" placeholder="请输入字典备注" clearable/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogCreateCancel">取 消</el-button>
        <el-button type="primary" @click="dialogCreateSubmit()">确 定</el-button>
      </div>
    </el-dialog>


    <!--  导入字典类型  -->
<!--    <UploadSingleExcel
        @close="closeUploadSingleExcelHandler"
        :import-dialog-title="uploadSingleExcelAttr.title"
        :show-import-dialog="uploadSingleExcelAttr.showImportDialog"
        :action="uploadSingleExcelAttr.action"
        :on-success-callback="loadPageDictSetting"
        :download-template="downloadImportTemplate"
    />-->
  </el-drawer>
</template>

<script>
import DictTypeSelect from "@/components/Dict/DictTypeSelect.vue";
import {
  createBaseDictApi,
  createBaseDictTypeApi,
  deleteDictApi,
  getBaseDictByIdApi,
  getBaseDictTypeByIdApi,
  pageDictApi, pageDictSettingApi, updateBaseDictApi
} from "@/api/dict";
import {isNotEmpty} from "@/utils/assertUtil";
import UploadSingleExcel from "@/components/UploadExcel/UploadSingleExcel.vue";
import {API_PREFIX} from "@/constant/commons";
import {exportDictApi, exportDictTemplateApi} from "@/api/file";
import {JsonText, SimpleCode} from "@/utils/ElementValidatorUtil";
import {Message} from "element-ui";
import DictSelect from "@/components/Dict/DictSelect.vue";

export default {
  name: 'DictSettingDrawer',
  components: {DictTypeSelect, DictSelect, UploadSingleExcel},
  props: {
    // 字典类型ID
    dictTypeId:{
      required: true,
      type: String,
    },
    // 字典ID
    dictId:{
      required: true,
      type: String,
    },
    // 字典编码
    dictCode:{
      required: true,
      type: String,
    },
    // 字典名称
    dictName:{
      required: true,
      type: String,
    },
    visible:{
      required: true,
      type: Boolean,
      default: function() {
        return false
      },
    },
  },
  data() {
    return {
      // 抽屉
      // 字典明细
      dictSetting: {
        EL_TABLE: {
          // 显示大小
          size: 'medium'
        },
        table: {
          filter: {
            dictTypeId: undefined,
            dictId: undefined,
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
        dialog: {
          create: {
            enabled: false,
            data: {
              dictTypeId: null,
              dictId: null,
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
              dictId: null,
              code: null,
              name: null,
              template: null,
              enabled: true,
              remark: null,
            },
          },
          rules: { // 弹窗的规则
            code: [
              {required: true, max: 16, message: '请输入16位及以内的字典编码', trigger: 'blur'},
              {required: true, validator: SimpleCode, trigger: 'blur'}
            ],
            name: [
              {required: true, max: 16, message: '请输入16位及以内的字典名称', trigger: 'blur'},
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

      elDropdownItemClass: ['el-dropdown-item--click', undefined, undefined],
      // 导入组件所需相关参数
      uploadSingleExcelAttr: {
        title: '导入字典配置',
        showImportDialog: false,
        action: `${API_PREFIX}/import-export/import-dict-setting`
      },
    };
  },
  watch: {
    /**
     * 监听字典类型ID变化
     */
    dictTypeId: {
      handler(n, o) {
        this.dictSetting.table.filter.dictTypeId = n;
        this.dictSetting.dialog.create.data.dictTypeId = n;
        this.dictSetting.dialog.edit.data.dictTypeId = n;
      },
      deep: true // 深度监听父组件传过来对象变化
    },
    /**
     * 监听字典ID变化，重新查询数据
     */
    dictId: {
      handler(n, o) {
        this.dictSetting.table.filter.dictId = n;
        this.dictSetting.dialog.create.data.dictId = n;
        this.dictSetting.dialog.edit.data.dictId = n;
      },
      deep: true // 深度监听父组件传过来对象变化
    },
    /**
     * 监听字典类型ID变化，重新查询数据
     */
    'dictSetting.table.filter.dictId': {
      handler(n, o) {
        this.dictSetting.table.filter.dictId = n;
      },
      deep: true // 深度监听父组件传过来对象变化
    },
    dictSettingDrawerVisible: {
      handler(n, o) {
        if (n) {
          this.loadPageDictSetting();
        } else {
          this.dictSetting.table.filter.dictTypeId = undefined
          this.dictSetting.table.filter.dictId = undefined
        }
      }
    }
  },
  computed: {
    /**
     * 计算属性：抽屉显示隐藏变量
     */
    dictSettingDrawerVisible: {
      get() {
        return this.visible
      },
      /**
       * 设置变量值，修改父组件变量值
       */
      set() {
        this.$emit('close', false)
      }
    },
  },
  methods: {
    //~ 搜索条件
    //==================================================================================================================
    /**
     * 获取字典类型下拉选，选中的字典类型编码ID
     * @param dictType 字典类型
     */
    changeDictType(dictType) {
      if (dictType) {
        this.dictSetting.table.filter.dictTypeId = dictType.id;
        // 设置为空，不然子组件DictSelect选中值了
        this.dictSetting.table.filter.dictId = undefined
        // this.dictSetting.dialog.create.data.dictTypeId = undefined;
        // this.dictSetting.dialog.create.data.dictTypeId = dictType.id;
        // this.dictSetting.dialog.create.data.dictId = undefined;
        // 重新查询字典明细下拉
      } else {
        this.dictSetting.table.filter.dictTypeId = undefined;
        this.dictSetting.table.filter.dictId = undefined
      }
    },
    /**
     * 获取字典类型下拉选，选中的字典类型编码ID(弹窗中的下拉)
     * @param dictType 字典类型
     */
    changeDictTypeByDialog(dictType) {
      console.log("changeDictTypeByDialog", dictType)
      if (dictType) {
        this.dictSetting.dialog.create.data.dictId = undefined;
        this.dictSetting.dialog.create.data.dictTypeId = dictType.id;
      }
    },
    /**
     * 获取字典类型下拉选，选中的字典类型编码ID
     * @param dict 字典
     */
    changeDict(dict) {
      if (dict) {
        this.dictSetting.table.filter.dictId = dict.id;
        // this.dictSetting.dialog.create.data.dictId = dict.id;
        // this.dictSetting.dialog.edit.data.dictId = dict.id;
      } else {
        this.dictSetting.table.filter.dictId = undefined;
        // this.dictSetting.dialog.create.data.dictId = undefined;
        // this.dictSetting.dialog.edit.data.dictId = undefined;
      }
    },
    /**
     * 获取字典类型下拉选，选中的字典类型编码ID(弹窗中的下拉)
     * @param dict 字典
     */
    changeDictByDialog(dict) {
      if (dict) {
        this.dictSetting.dialog.create.data.dictId = dict.id;
        this.dictSetting.dialog.edit.data.dictId = dict.id;
      } else {
        this.dictSetting.dialog.create.data.dictId = undefined;
        this.dictSetting.dialog.edit.data.dictId = undefined;
      }
    },
    /**
     * 重置查询条件，并重新查询数据
     */
    resetSearchFilter() {
      // 赋默认值
      this.dictSetting.table.filter.code = undefined;
      this.dictSetting.table.filter.name = undefined;
    },
    /**
     * 点击查询按钮
     */
    searchFunc() {
      this.dictSetting.table.page = 1
      this.loadPageDictSetting()
    },
    /**
     * 分页查询字典明细表格数据
     */
    loadPageDictSetting() {
      this.dictSetting.table.isLoading = true;
      const pageParam = {
        page: this.dictSetting.table.page,
        size: this.dictSetting.table.size,
        dictId: this.dictSetting.table.filter.dictId,
        code: this.dictSetting.table.filter.code,
        name: this.dictSetting.table.filter.name,
      }
      pageDictSettingApi(pageParam).then(data => {
        const content = data.content

        // 修改分页组件
        this.dictSetting.table.page = Number(data.page)
        this.dictSetting.table.size = Number(data.size)
        this.dictSetting.table.total = Number(data.total)
        this.dictSetting.table.totalPage = Number(data.totalPage)

        // 将原先的数据丢弃
        this.dictSetting.table.data = []

        // 添加到数据集合
        content.forEach((item, index, arr) => {
          const column = {
            ...item,
          }
          this.dictSetting.table.data.push(column)
        })
      }).finally(() => {
        this.dictSetting.table.isLoading = false;
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
     * 复选框选中
     * @param {Object} rows 选中的行数据
     */
    selectionChangeFunc(rows) {
      this.dictSetting.table.checkIds = rows.map(m => m.id)
    },

    /**
     * 批量删除字典明细
     */
    deleteDict(ids) {
      isNotEmpty(ids, () => this.$message.warning("请勾选需要删除的字典明细"))
          .then(() => {
            this.$confirm('此操作将永久删除所选明细, 是否继续?', '删除', {
              confirmButtonText: '确定',
              cancelButtonText: '取消',
              type: 'warning'
            }).then(() => {
              deleteDictApi(ids).then(data => {
                this.$message.success("删除成功")
                this.loadPageDict()
              })
            }).catch(reason => {
              console.error(reason)
              this.$message.info("已取消删除");
            })
          }).catch(() => {});
    },

    /**
     * 编辑字典明细
     */
    editDictType(row) {
      getBaseDictByIdApi(row.id).then(data => {
        console.log("dd", data)
        this.dictSetting.dialog.edit.data = {}
        this.dictSetting.dialog.edit.data.id = row.id
        this.dictSetting.dialog.edit.data.dictTypeId = data.dictTypeId
        this.dictSetting.dialog.edit.data.code = data.code
        this.dictSetting.dialog.edit.data.name = data.name
        this.dictSetting.dialog.edit.data.template = data.template
        this.dictSetting.dialog.edit.data.enabled = data.enabled
        this.dictSetting.dialog.edit.data.remark = data.remark
        this.dictSetting.dialog.edit.enabled = true
      })
    },

    /**
     * 关闭前的回调，会暂停 Drawer 的关闭
     * @param done 使用done()关闭抽屉
     */
    handleClose(done) {
      // 给父组件传递值
      this.$emit('close', false)
      this.dictSetting.table.filter = {}
      this.dictSetting.table.data = []
      done();
    },

    //~ 新增字典
    //==================================================================================================================
    /**
     * 组件嵌套太深，强制更新
     */
    inputChange() {
      this.$forceUpdate()
    },

    /**
     * 查询条件得字典类型下拉变更
     * @param dictType
     */
    dialogChangeDictType(dictType) {
      if (dictType) {
        this.dictSetting.dialog.create.data.dictTypeId = dictType.id;
      } else {
        this.dictSetting.dialog.create.data.dictTypeId = undefined;
      }
    },

    /**
     * 创建弹框点击取消
     */
    dialogCreateCancel() {
      this.dictSetting.dialog.create.enabled = false;
      this.dictSetting.dialog.create.data = {}
      this.dictSetting.dialog.create.data.dictTypeId = this.dictSetting.table.filter.dictTypeId;
      this.dictSetting.dialog.create.data.enabled = true;
      this.$refs.dialogCreateForm.resetFields();
    },
    /**
     * 使用上级模板
     * @param {Number | String} parentId 上级ID
     */
    useParentTemplate(parentId) {
      getBaseDictTypeByIdApi(parentId).then(data => {
        // 模板
        let template = data.template;
        // 弹框设置值
        this.dictSetting.dialog.create.data.template = undefined
        this.dictSetting.dialog.edit.data.template = undefined
        // 重新赋值。
        this.dictSetting.dialog.create.data.template = template
        this.dictSetting.dialog.edit.data.template = template
      })
    },
    /**
     * 创建弹框点击提交
     */
    dialogCreateSubmit() {
      this.$refs.dialogCreateForm.validate((valid) => {
        if (valid) {
          createBaseDictApi(this.dictSetting.dialog.create.data).then(response => {
            // 保存成功
            Message({
              message: '字典明细创建成功',
              type: 'success',
            })
            this.dialogCreateCancel()
            this.loadPageDict();
          })
        } else {
          console.error("校验参数失败")
          return false;
        }
      });
    },

    //~ 修改字典
    //==================================================================================================================
    editDialogChangeDictType(dictType) {
      if (dictType) {
        this.dictSetting.dialog.create.data.dictTypeId = dictType.id;
      } else {
        this.dictSetting.dialog.create.data.dictTypeId = undefined;
      }
    },

    /**
     * 修改字典明细弹窗关闭
     */
    dialogEditCancel() {
      this.dictSetting.dialog.edit.enabled = false;
      this.dictSetting.dialog.edit.data = {}
      this.dictSetting.dialog.edit.data.dictTypeId = this.dictSetting.table.filter.dictTypeId;
      this.$refs.dialogEditForm.resetFields();
    },
    /**
     * 编辑弹框点击提交
     */
    dialogEditSubmit() {
      this.$refs.dialogEditForm.validate((valid) => {
        if (valid) {
          updateBaseDictApi(this.dictSetting.dialog.edit.data).then(response => {
            // 保存成功
            Message({
              message: '字典明细修改成功',
              type: 'success',
            })
            this.dialogEditCancel()
            this.loadPageDictSetting();
          })
        } else {
          console.error("校验参数失败")
          return false;
        }
      });
    },

    //~ 导入/导出相关方法
    //==================================================================================================================
    /**
     * 关闭弹窗
     */
    closeUploadSingleExcelHandler() {
      this.uploadSingleExcelAttr.showImportDialog = false
    },
    /**
     * 下载模板
     */
    downloadImportTemplate() {
      exportDictTemplateApi();
    },

    /**
     * 导出Excel
     */
    exportExcel() {
      const pageParam = {
        dictTypeId: this.dictSetting.table.filter.dictTypeId,
        code: this.dictSetting.table.filter.code,
        name: this.dictSetting.table.filter.name,
      }
      // 如果勾选了就导出勾选的
      const data = {
        ids: this.dictSetting.table.checkIds,
        pageReq: { // 查询条件
          ...pageParam
        },
      }
      exportDictApi(data);
    },

  },
}
</script>
<style lang="scss" scoped>

</style>
