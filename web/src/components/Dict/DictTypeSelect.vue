<!--字典类型编码模糊下拉分页-->
<template>
  <el-select
      v-model="code"
      v-loadmore="loadMore"
      style="width: 230px;"
      :loading="loading"
      clearable
      filterable
      placeholder="请输入字典类型编码"
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

import {dropDownDictTypeApi} from '@/api/dropDown';
import {loadmore} from '@/directive/select/index'
export default {
  directives: { loadmore },
  props: ['value'],
  data() {
    return {
      code: undefined,
      loading: false,
      data: [],
      page: 1,
      size: 10,
      totalPage: undefined, // 下拉总页码
    }
  },
  mounted() {
    // 优先加载表格数据
    this.load(this.code)
  },
  methods: {
    load() {
      this.loading = true
      const page = { page: this.page, size: this.size, code: this.code }
      dropDownDictTypeApi(page).then(data => {
        console.log(data)
        this.totalPage = Number(data.totalPage)
        const content = data.content
        content.forEach(item => {
          this.data.push({ id: item.id, code: item.code, name: item.name })
        })
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
      this.$emit('changeDictType', item)
    },
    clear() {
      // 去掉框中的值
      this.code = undefined;
      // 给父组件传递值
      this.$emit('changeDictTypeId', undefined)
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
