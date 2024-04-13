<!--字典明细编码模糊下拉分页-->
<template>
  <el-select
      v-model="selectedValue"
      v-loadmore="loadMore"
      style="width: 230px;"
      :loading="loading"
      :clearable="clearable"
      :disabled="disabled"
      filterable
      placeholder="请输入字典明细编码"
      @change="change"
      @clear="clear"
  >
    <el-option
        v-for="item in data"
        :key="item.id"
        :label="item.code"
        :value="item.id"
    />
  </el-select>
</template>

<script>

import {dropDownDictApi, dropDownDictTypeApi} from '@/api/dropDown';
import {loadmore} from '@/directive/select/index'
import {getBaseDictByIdApi, getBaseDictTypeByIdApi} from "@/api/dict";
export default {
  directives: { loadmore },
  props: {
    // 字典类型ID，如果传了字典类型ID，就需要查询指定字典类型下的字典
    dictTypeId:{
      required: false,
      type: String,
      default() {
        return null;
      }
    },
    // 字典ID
    defaultSelectId:{
      required: false,
      type: String,
    },
    // 清空按钮
    clearable:{
      required: false,
      type: Boolean,
      default() {
        return true
      }
    },
    // 可编辑
    disabled:{
      required: false,
      type: Boolean,
      default() {
        return false
      }
    }
  },
  data() {
    return {
      code: undefined,
      loading: false,
      selectedValue: undefined,
      data: [],
      page: 1,
      size: 10,
      totalPage: undefined, // 下拉总页码
    }
  },
  mounted() {
    // 优先加载表格数据
    this.load()
  },
  watch:{
    dictTypeId:{
      handler(n, o){
        this.selectedValue = undefined
        this.page = 1
        this.data = []
        this.load();
      }
    },
    /**
     * 变更时，需要选中指定下拉选项
     */
    defaultSelectId: {
      handler(n, o){
        if (n !== undefined && n !== null && n !== '') {
          let item = this.data.find(item => item.id === n);
          // 不存在，就查询出来
          if (item === undefined || item === null) {
            getBaseDictByIdApi(n).then(dt => {
              let op = {id: dt.id, code: dt.code, name: dt.name};
              // 只有不存在时，才添加到数组中
              if (this.data.find(item => item.id === op.id) === undefined) {
                this.data.push(op)
              }
              this.selectedValue = n
            })
          } else { // 存在就默认选中
            this.selectedValue = n
          }
        }
      }
    }
  },
  methods: {
    load() {
      this.loading = true
      const page = { page: this.page, size: this.size, dictTypeId: this.dictTypeId, code: this.code }
      dropDownDictApi(page).then(data => {
        this.totalPage = Number(data.totalPage)
        const content = data.content
        // 添加到数组中
        content.forEach(item => {
          // 只有不存在时，才添加到数组中
          let op = {id: item.id, code: item.code, name: item.name};
          if (this.data.find(item => item.id === op.id) === undefined) {
            this.data.push(op)
          }
        })

        // 首次加载时，监视器watch不会执行，这里需要查询默认选项
        if (this.defaultSelectId !== undefined && this.defaultSelectId !== null && this.defaultSelectId !== '') {
          let findItem = this.data.find(item => item.id === this.defaultSelectId);
          if (findItem === undefined || findItem === null) {
            // 不存在，就查询出来
            getBaseDictByIdApi(this.defaultSelectId).then(dt => {
              let op = {id: dt.id, code: dt.code, name: dt.name};
              // 只有不存在时，才添加到数组中
              if (this.data.find(item => item.id === op.id) === undefined) {
                this.data.push(op)
              }
              this.selectedValue = this.defaultSelectId
            })
          } else { // 存在就默认选中
            this.selectedValue = this.defaultSelectId
          }
        }

      }).catch(err => {
        console.warn('err', err)
      }).finally(() => {
        this.loading = false
      })
    },
    /**
     * 下拉选修改，返回选中的项
     * @param id
     */
    change(id) {
      let item = this.data.find(item => item.id === id);
      console.log(item)
      // 给父组件传递值
      /*
        item.id
        item.code
        item.name
       */
      this.$emit('changeDict', item)
    },
    clear() {
      // 去掉框中的值
      this.code = undefined;
      // 给父组件传递值
      this.$emit('changeDict', undefined)
    },
    loadMore: function() {
      // 总页数大于当前页，请求下一页数据
      if (this.totalPage > this.page) {
        this.page += 1;
        this.load();
      }
    }
  }
}
</script>

<style scoped>

</style>
