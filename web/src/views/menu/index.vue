<!--菜单页面-->
<template>
  <div class="app-container">
    <!--  查询条件  -->
    <div class="filter-container">
      <div class="filter-item">
        <span class="filter-item-label">菜单名称: </span>
        <el-input v-model="filter.name" clearable placeholder="请输入" />
      </div>
      <div class="filter-item">
        <span class="filter-item-label">菜单类型: </span>
        <el-select v-model="filter.type" clearable>
          <el-option
            v-for="item in menuTypeArray"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </div>
      <div class="filter-item">
        <span class="filter-item-label">权限标识: </span>
        <el-input v-model="filter.permissionId" clearable placeholder="请输入" />
      </div>
      <div class="filter-item">
        <span class="filter-item-label">资源路径: </span>
        <el-input v-model="filter.path" clearable placeholder="请输入" />
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
        <el-button v-permission="'sys:menu:add'" class="el-button--small" icon="el-icon-plus" type="primary"
                   @click="addMenu"
        >新增
        </el-button>
        <el-button v-permission="'sys:menu:delete'" class="el-button--small" icon="el-icon-delete" type="danger" @click="deleteMenus">
          删除
        </el-button>
        <el-button v-permission="'sys:menu:sort'" class="el-button--small" icon="el-icon-s-promotion" type="primary"
                   @click="switchOpenDrag"
        >{{ switchButtonName }}
        </el-button>
        <el-button class="el-button--small" type="primary" @click="openTable(true)">展开</el-button>
        <el-button class="el-button--small" type="primary" @click="openTable(false)">折叠</el-button>
        <el-button v-permission="'sys:menu:import'" class="el-button--small" icon="el-icon-upload2" @click="uploadSingleExcelAttr.showImportDialog=true">
          导入
        </el-button>
        <el-button v-permission="'sys:menu:export'" class="el-button--small" icon="el-icon-download" @click="exportExcel">
          导出
        </el-button>
      </div>
      <div class="right-tool">
        <el-tooltip class="right-tool-btn-tooltip" effect="dark" content="刷新" placement="top">
          <div class="right-tool-btn">
            <i class="el-icon-refresh-right"/>
          </div>
        </el-tooltip>
        <el-tooltip class="right-tool-btn-tooltip" effect="dark" content="密度" placement="top">
          <el-dropdown trigger="click" @command="changeElTableSizeCommand">
            <div class="right-tool-btn">
              <i class="el-icon-s-operation"/>
            </div>
            <el-dropdown-menu slot="dropdown" size="small">
              <el-dropdown-item :class="table.elDropdownItemClass[0]" command="0,medium">默认</el-dropdown-item>
              <el-dropdown-item :class="table.elDropdownItemClass[1]" command="1,small">中等</el-dropdown-item>
              <el-dropdown-item :class="table.elDropdownItemClass[2]" command="2,mini">紧凑</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </el-tooltip>
      </div>
    </div>

    <div class="el-table__body__pagination">
      <!-- 表格  -->
      <!--      @selection-change="selectionChangeFunc"-->
      <el-table
          ref="table"
          v-loading="table.isLoading"
          border
          :data="table.data"
          :key="dataKey"
          :expand-row-keys="table.expandKeys"
          row-key="id"
          style="width: 100%"
          :header-cell-style="{background:'#FAFAFA', color:'#000', height: '30px',}"
          :header-row-class-name="table.EL_TABLE.size"
          :size="table.EL_TABLE.size"

          @select="select"
          @select-all="selectAll"
      >
        <el-table-column
            width="50"
            type="selection"
            header-align="center"
            align="center"
            class-name="selection"
        />
        <el-table-column
            type="index"
            label="排序"
            width="50"
            align="center"
            :class-name="switchClassName"
        >
          <i class="el-icon-s-promotion handle" />
        </el-table-column>
        <el-table-column
            label="名称"
            min-width="150"
            prop="name"
            show-overflow-tooltip
        />
        <el-table-column
            label="权限标识"
            min-width="150"
            prop="permissionId"
            show-overflow-tooltip
        >
          <template v-slot="scope">
          <span class="copy_column_class el-icon-document-copy" @click="copyPermissionId(scope.row)">
            <span style="width: 5px; display: inline-block;"></span>{{scope.row.permissionId}}
          </span>
          </template>
        </el-table-column>
        <el-table-column
            label="类型"
            prop="type"
            max-width="50"
        >
          <template v-slot="scope">
            <span v-if="scope.row.type === 1">菜单</span>
            <span v-else-if="scope.row.type === 2">按钮</span>
            <span v-else-if="scope.row.type === 3">接口</span>
            <span v-else>未知类型</span>
          </template>
        </el-table-column>
        <el-table-column
            label="资源路径"
            width="170"
            prop="path"
            show-overflow-tooltip
        >
          <template v-slot="scope">
          <span class="copy_column_class el-icon-document-copy" @click="copyPath(scope.row)">
            <span style="width: 5px; display: inline-block;"></span>{{scope.row.path}}
          </span>
          </template>
        </el-table-column>
        <el-table-column
            label="请求方式"
            width="100"
            prop="method"
            align="left"
            show-overflow-tooltip
        >
          <template v-slot="scope">
          <span v-for="item in getMethod(scope.row)" :key="item">
            <el-tag size="small" :class="item" class="http_method_tag">{{ item }}</el-tag> <br>
          </span>
          </template>
        </el-table-column>
        <el-table-column
            label="排序"
            width="50"
            prop="sortNum"
        />
        <el-table-column
            label="隐藏"
            width="50"
            prop="hide"
        >
          <template v-slot="scope">
            <span>{{ scope.row.hide ? '隐藏' : '可见' }}</span>
          </template>
        </el-table-column>
        <el-table-column
            label="备注"
            width="100"
            prop="remark"
            show-overflow-tooltip
        />
        <el-table-column
            label="创建时间"
            width="150"
            prop="createdDate"
            show-overflow-tooltip
        />
        <el-table-column
            label="操作"
            width="230"
            align="center"
        >
          <template v-slot="scope">
            <div class="el-link-parent">
              <el-link
                  v-permission="'sys:menu:edit'"
                  icon="el-icon-edit"
                  :underline="false"
                  type="primary"
                  @click="updateMenu(scope.row)"
              >编辑</el-link>
              <el-link
                  v-permission="'sys:menu:delete'"
                  icon="el-icon-delete"
                  :underline="false"
                  type="danger"
                  @click="deleteMenu(scope.row)"
              >删除</el-link>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!--  新增菜单弹窗  -->
    <CreateMenuDialog :create-menu-dialog.sync="createMenuDialog" :refresh-menu="load" />
    <!--  修改菜单弹窗  -->
    <UpdateMenuDialog :update-menu-dialog.sync="updateMenuDialog" :update-menu-data="updateMenuData" :refresh-menu="load" />

    <!--  导入  -->
    <UploadSingleExcel
      @close="closeUploadSingleExcelHandler"
      :import-dialog-title="uploadSingleExcelAttr.title"
      :show-import-dialog="uploadSingleExcelAttr.showImportDialog"
      :action="uploadSingleExcelAttr.action"
      :on-success-callback="load"
      :download-template="downloadImportTemplate"
    />
  </div>
</template>

<script>
import {changeSortNumApi, deleteMenuByIdsApi, initMenuApi, listMenuApi} from "@/api/menu";
import { menuTreeHandler } from "@/utils/tree";
import { goudongWebAdminResource } from "@/router/modules/goudong-web-admin-router";
import {API_PREFIX, MENU_TYPE_ARRAY} from "@/constant/commons"
import Sortable from 'sortablejs';
import { copyText } from "@/utils/StringUtil";
import {exportMenuTemplateApi, exportMenuApi} from "@/api/file";
import {isNotEmpty} from "@/utils/assertUtil";
import {deleteUserByIdsApi} from "@/api/user";

export default {
  name: 'MenuPage',
  components: {
    CreateMenuDialog: () => import('@/views/menu/components/CreateMenuDialog'),
    UpdateMenuDialog: () => import('@/views/menu/components/UpdateMenuDialog'),
    DetailMenu: () => import('@/views/menu/components/DetailMenu'),
    UploadSingleExcel: () => import('@/components/UploadExcel/UploadSingleExcel.vue'),
  },
  data() {
    return {
      menuTypeArray: MENU_TYPE_ARRAY,
      dataKey: new Date().getTime(),
      filter: {
        name: undefined,
        type: undefined,
        permissionId: undefined,
        path: undefined,
      },
      props: {
        label: 'name',
        children: 'children'
      },
      // menuVisible: false,
      createMenuDialog: false, // 创建菜单弹窗
      updateMenuDialog: false, // 修改菜单弹窗
      updateMenuData: undefined, // 修改菜单得数据
      checkIds: [], // 选中的id
      table: {
        expandKeys: undefined,
        isLoading: false,
        rawData: [], // 初始数据
        data: [], // 渲染的数据
        activeRows: [], // 转换为列表(一维数组)的数据
        elDropdownItemClass: ['el-dropdown-item--click', undefined, undefined],
        EL_TABLE: {
          // 显示大小
          size: 'medium'
        },
        sort_able: undefined,
      },
      openDrag: false, // 开启拖拽
      switchButtonName: '开启排序', // 拖拽的按钮文字
      switchClassName: 'close-switch-drag', // 拖拽列得class

      // 导入组件所需相关参数
      uploadSingleExcelAttr: {
        title: '导入菜单',
        showImportDialog: false,
        action: `${API_PREFIX}/import-export/import-menu`
      },
    }
  },
  watch: {

  },
  mounted() {
    this.load()
  },
  methods: {
    openTable(flag) { // 展开/折叠
      this.$nextTick(() => {
        this.handleArr(this.table.data, flag);
      });

    },
    handleArr(arr, flag) {
      arr.forEach(i => {
        this.$refs.table.toggleRowExpansion(i, flag);
        if (i.children) {
          this.handleArr(i.children);
        }
      });
    },
    // 切换拖拽
    switchOpenDrag() {
      // 状态取反
      this.openDrag = !this.openDrag;
      this.switchButtonName = this.openDrag ? "关闭排序" : "开启排序"
      if (!this.openDrag) {
        this.cancelRowDrop()
        this.switchClassName = "close-switch-drag"
      } else {
        this.rowDrop()
        this.switchClassName = "open-switch-drag"
      }
    },
    load() {
      this.table.isLoading = true;
      this.dataKey = new Date().getTime();
      listMenuApi({}).then(data => {
        console.log("data", data.records)
        this.table.data = data.records;
        this.table.rawData = data.records;
        this.$store.dispatch('menu/setAllMenus', data.records)
        // this.table.expandKeys = data.records.map(m => m.id);

      }).finally(() => {
        this.table.isLoading = false
      })
    },
    getMethod(row) {
      if (row.method) {
        return JSON.parse(row.method)
      }
      return []
    },
    // 将树数据转化为平铺数据
    treeToTile(treeData, childKey = 'children') {
      const arr = []
      const expanded = data => {
        if (data && data.length > 0) {
          data.filter(d => d).forEach(e => {
            arr.push(e)
            expanded(e[childKey] || [])
          })
        }
      }
      expanded(treeData)
      return arr
    },
    // 行拖拽
    rowDrop() {
      const tbody = document.querySelector('.el-table__body-wrapper tbody')
      const _this = this
      this.sort_able = Sortable.create(tbody, {
        //  指定父元素下可被拖拽的子元素
        handle: ".handle",
        onMove: () => {
          _this.table.activeRows = this.treeToTile(_this.table.data) // 把树形的结构转为列表再进行拖拽
        },
        onEnd({ newIndex, oldIndex }) {
          // 1. 校验只能同级
          if (_this.table.activeRows[newIndex].parentId !== _this.table.activeRows[oldIndex].parentId) {
            _this.$message.error("只能同级进行移动");
            _this.dataKey=new Date().getTime()
            _this.switchOpenDrag()
            return;
          } else {
            const beforeId = _this.table.activeRows[oldIndex].id
            const afterId = _this.table.activeRows[newIndex].id
            console.log(oldIndex, newIndex);
            console.log(beforeId, afterId);
            const currRow = _this.table.activeRows.splice(oldIndex, 1)[0]
            _this.table.activeRows.splice(newIndex, 0, currRow)
            changeSortNumApi({beforeId: beforeId, afterId: afterId}).then(data => {
              _this.load();
            })
          }
        }
      })
    },
    // 取消拖拽
    cancelRowDrop() {
      if (this.sort_able) {
        this.openDrag = false;
        this.switchButtonName = this.openDrag ? "关闭排序" : "开启排序"
        this.sort_able.destroy()
      }
    },
    getReturnNode(node, _array, value) {
      const isPass =
        node.data &&
        node.data.name &&
        node.data.name.indexOf(value) !== -1;
      isPass ? _array.push(isPass) : "";
      if (!isPass && node.level !== 1 && node.parent) {
        this.getReturnNode(node.parent, _array, value);
      }
    },
    // 查询
    searchFunc() {
      // 筛选
      const arr = []
      this.table.rawData.filter((item) => {
        return this.menuFilter(item, arr);
      })
      this.table.data = arr
      console.log(JSON.stringify(arr));

      let findParent1 = this.findParent(this.table.data, 1100);
      console.log("find", findParent1)
    },
    menuFilter(menu, arr) {
      // 本节点满足条件，直接添加到数组arr。
      let flag = true
      // 菜单名
      flag = flag && (!this.filter.name || (this.filter.name && menu.name.indexOf(this.filter.name) !== -1))
      // 菜单类型
      flag = flag && (!this.filter.type || (this.filter.type && menu.type === this.filter.type))
      // 权限标识
      flag = flag && (!this.filter.permissionId || (this.filter.permissionId && menu.permissionId.indexOf(this.filter.permissionId) !== -1))
      // 资源路径
      flag = flag && (!this.filter.path || (this.filter.path && menu.path.indexOf(this.filter.path) !== -1))
      // 所有条件满足，添加到集合
      if (flag) {
        arr.push(menu)
        return true;
      }
      // 本节点不满足条件，继续遍历子节点
      if (menu.children) {
        menu.children.filter((item) => {
          return this.menuFilter(item, arr);
        })
      }
      return false;
    },
    resetSearchFilter() {
      this.filter = {}
      // 重新加载
      this.load();
    },
    generate(item) {
      const obj = {
        name: item.name,
        type: item.type,
        openModel: item.openModel,
        path: item.path,
        permissionId: item.permissionId,
        method: item.method,
        metadata: item.meta,
      }
      if (item.meta) {
        if (item.meta.icon) {
          obj.icon = item.meta.icon
        }
        if (!item.meta.title) {
          obj.metadata.title = obj.name
        }

        obj.metadata = JSON.stringify(obj.metadata)
      }
      // 菜单（组件）未单独配置组件路由
      if (item.type === 1 && !item.componentPath) {
        obj.componentPath = item.path
      }

      if (item.children) {
        // 子元素
        obj.children = [];
        item.children.forEach((i, index, arr) => {
          obj.children.push(this.generate(i));
        })
      }
      return obj;
    },
    addMenu() { // 新增菜单
      this.createMenuDialog = true
    },
    // 复选框勾选事件
    /**
     * 复选框勾选事件
     * @param rows  已被勾选的行
     */
    selectionChangeFunc(rows) {
      console.log("勾选事件 = ", rows);
      const ids = rows.map(m => {
        return m.id
      })
      // const ids = [];
      // this.getAllId(rows, ids);
      console.log("ids = " + ids);

      // this.table.data.map((value, index, array) => {
      //   console.log(value)
      //   // this.$refs.table.toggleRowSelection(value)
      //   this.$refs.table.toggleRowSelection(value)
      // })
      this.checkIds = ids
    },
    /**
     * 选中/取消选中行的所有子元素
     * @param {Array} rows      需要勾选/取消勾选的行
     * @param {Boolean} checked true：勾选；false：取消勾选
     */
    childrenSelection(rows, checked) {
      if (rows) {
        rows.map(c => {
          this.$refs.table.toggleRowSelection(c, checked)
          c.isChecked = checked
          // 勾选就添加,取消勾选就删除
          if (checked) {
            this.addId(c)
          } else {
            this.deleteId(c)
          }
          if (c.children) {
            this.childrenSelection(c.children, checked);
          }
        })
      }
    },
    /**
     * 行的下级存在选中
     * @param row
     * @returns {*}
     */
    rowChildrenExistsChecked(row) {
      if (row && row.children) {
        // 先查询一次层级
        let checked = row.children.some(el => el.isChecked === true)
        // 如果没有就需要递归查询第二层级和第三层级
        if (!checked) {
          let length = row.children.length;
          for (let i = 0; i < length; i++) {
            checked = this.rowChildrenExistsChecked(row.children[i]);
            if (checked) {
              return checked;
            }
          }
        }
        return checked;
      }
    },
    /**
     * 判断行的下级(第一层)是否全部选择了
     * @param {object} row 行
     * @returns {boolean}
     */
    isRowChildrenAllChecked(row) {
      if (row && row.children) {
        let children = row.children
        for (let i = 0; i < children.length; i++) {
          let e = children[i];
          if (e.isChecked === undefined || e.isChecked === null || e.isChecked === false) {
            return false;
          }
        }
        return true;
      }
    },
    /**
     * 判断行的下级(第一层)是否存在
     * @param row
     * @returns {boolean}
     */
    isRowChildrenExistsChecked(row) {
      if (row && row.children) {
        let children = row.children
        for (let i = 0; i < children.length; i++) {
          let e = children[i];
          if (e.isChecked === true) {
            return true;
          }
        }
        return false;
      }
    },
    /**
     * 找到id==parentId的元素
     * @param {Array} els
     * @param {Object} parentId
     * @returns {{id}|*|null}
     */
    findParent(els, parentId) {
      console.log(els, parentId)
      if (els != null && parentId != null) {
        for (let i= 0; i < els.length; i++) {
          let ce = els[i];
          if (ce != null && ce.id != null) {
            console.log("判断菜单【"+ce.name+"】是否满足", ce.id, parentId)
            // 只能用双等于，类型不匹配
            if (ce.id == parentId) {
              console.log("满足", ce.name)
              return ce;
            } else {
              console.log("不满足", ce.name)
              if (ce.children) {
                let p = this.findParent(ce.children, parentId)
                if (p != null) {
                  return p;
                }
              }
            }
          }
        }
      }
      return null;
    },
    /**
     * 点击每行的复选框事件事件
     * @param {Array} selection 已经选中的行
     * @param {Object} row      本次勾选/取消勾选的行
     */
    select(selection, row) {
      let vm = this
      console.log(selection)
      console.log(row)
      // 操作行 row 在 已选范围 selection 内，认为是选中，否则是取消选中
      if (selection.some((el) => el !== null && el !== undefined && row.id === el.id)) {
        // 设置当前行选中状态
        row.isChecked = true
        // 记录选中id
        this.addId(row)
        // 若存在子级，子集全选
        this.childrenSelection(row.children, true);
        // // 若存在父级，且父级下的所有元素都选中了，就设置父级选中
        // if (row.parentId) {
        //   let parent = this.findParent(vm.table.data, row.parentId);
        //   // 查询父级下的第一层子元素是否都选中了，如果选中了就将父级菜单选中
        //   let allChildrenChecked = this.isRowChildrenAllChecked(parent);
        //   if (allChildrenChecked) {
        //     this.$refs.table.toggleRowSelection(parent, true)
        //     parent.isChecked = true
        //     this.addId(parent)
        //   }
        // }
      } else {
        // 取消选中本行和所有子行
        row.isChecked = false
        this.deleteId(row)
        // 若存在子级，子级全部取消选中
        this.childrenSelection(row.children, false);
        // // 若存在父级，判断父级的子级(当前行的兄弟) ，若全部未选中，取消父级选中
        // if (row.parentId) {
        //   console.log("parentId", vm.table.data, row.parentId, row.name)
        //   let pNode = this.findParent(vm.table.data, row.parentId);
        //   console.log("pNode", pNode)
        //   // 判断，父菜单的子元素是否都选中了，选中了才
        //   let allChildrenExistsChecked = this.isRowChildrenExistsChecked(pNode);
        //   console.log("allChildrenExistsChecked", allChildrenExistsChecked)
        //   if (!allChildrenExistsChecked) {
        //     this.$refs.table.toggleRowSelection(pNode, false)
        //     pNode.isChecked = false
        //     this.deleteId(pNode)
        //   }
        // }
      }
    },
    selectAll(selection) {
      console.log("selectAll")
      // 判断当前是否有已选中的
      let rA = this.table.data.some(el => {
        let r = false
        if (el.children) {
          r = el.children.some(e => e.isChecked)
        }
        if (r) return r
        return el.isChecked
      })
      // 若有选中则全部取消，否则全部选中
      if (rA) {
        this.table.data.forEach(el => {
          el.isChecked = false
          this.$refs.table.toggleRowSelection(el, false)
          this.deleteId(el)
          this.childrenSelection(el.children, false)
        })
      } else {
        this.table.data.forEach(el => {
          el.isChecked = true
          this.$refs.table.toggleRowSelection(el, true)
          this.addId(el)
          this.childrenSelection(el.children, true)
        })
      }
    },
    // 增加选中
    addId(o) {
      let id = o.id
      if (this.checkIds.indexOf(id) < 0) {
        this.checkIds.push(id)
      }
    },
    // 删除选中
    deleteId(o) {
      let id = o.id
      this.checkIds = this.checkIds.filter(item => item !== id);
    },
    // 批量删除菜单
    deleteMenus() {
      const ids = this.checkIds;
      isNotEmpty(ids, () => this.$message.warning("请勾选需要删除的菜单"))
          .then(() => {
            this.$confirm('此操作将永久删除所选菜单及其所有子菜单, 是否继续?', '删除', {
              confirmButtonText: '确定',
              cancelButtonText: '取消',
              type: 'warning'
            }).then(() => {
              deleteMenuByIdsApi(ids).then(data => {
                this.$message.success("删除成功")
                this.load()
              })
            }).catch(reason => {
              console.error(reason)
              this.$message.info("已取消删除");
            })
          }).catch(() => {});
    },
    updateMenu(row) { // 修改菜单
      this.updateMenuData = row
      this.updateMenuDialog = true
      console.log("row", this.updateMenuData)
    },
    deleteMenu(row) { // 删除菜单
      this.$confirm(`此操作将永久删除”${row.name}“菜单及其所有子菜单, 是否继续?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteMenuByIdsApi([row.id]).then(response => {
          this.$message.success("删除成功")
          this.load()
        })
      }).catch(() => {
        this.$message.info("已取消删除");
      })
    },
    copyPermissionId(row) { // 拷贝权限标识
      copyText(row.permissionId, this)
    },
    copyPath(row) { // 拷贝资源路径
      copyText(row.path, this)
    },
    // 修改表格大小
    changeElTableSizeCommand(val) {
      const args = val.split(",");
      const idx = Number(args[0]);
      console.log(args)
      this.table.elDropdownItemClass.map((value, index, array) => {
        if (index === idx) {
          array[index] = "el-dropdown-item--click";
        } else {
          array[index] = undefined;
        }
      })
      console.log(this.table.elDropdownItemClass)
      this.table.elDropdownItemClass[args[0]]
      this.table.EL_TABLE.size = args[1];
    },

    // 下载模板
    downloadImportTemplate() {
      exportMenuTemplateApi();
    },
    // 关闭导入弹窗
    closeUploadSingleExcelHandler() {
      this.uploadSingleExcelAttr.showImportDialog = false
    },
    // 导出角色
    exportExcel() {
      const pageParam = this.filter
      // 如果勾选了就导出勾选的
      const data = {
        ids: this.checkIds,
        pageReq: { // 查询条件
          ...pageParam
        },
      }
      exportMenuApi(data);
    }
  }
}
</script>
<style lang="scss" scoped>
@import '@/styles/variables.scss';

::v-deep .open-switch-drag{
  cursor: move;
  .cell {

  }
}
::v-deep .close-switch-drag{
  cursor: not-allowed;
  .cell {
    opacity: 0.4;
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
</style>
