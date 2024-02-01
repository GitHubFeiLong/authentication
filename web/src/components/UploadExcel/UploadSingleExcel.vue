<!--Excel上传及模板下载-->
<template>
  <!--导入-->
  <el-dialog :title="importDialogTitle" :visible.sync="showImportDialogSon" custom-class="el-dialog-import-table" @close="handleClose"
  >
    <el-upload
        class="el-dialog-import-table-upload"
        :action="action"
        drag
        :show-file-list="false"
        :multiple="false"
        :before-upload="beforeUpload"
        :on-success="onSuccess"
        :on-error="onError"
        :headers="headers"
    >
      <i class="el-icon-upload" />
      <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
      <div slot="tip" class="el-upload__tip">只能上传xls、xlsx文件，<a class="a-download-template" @click="downloadTemplate">下载模板</a></div>
    </el-upload>
  </el-dialog>
</template>

<script>
import { beforeUploadExcel } from "@/utils/updateUtil";
import {beforeUploadFillHttpHeader} from "@/utils/fileUtil";

export default {
  name: 'UploadSingleExcel',
  props: {
    importDialogTitle: {
      type: String,
      default: '导入',
    },
    // 是否显示导入弹窗
    showImportDialog: {
      type: Boolean,
      required: true,
      default: false,
    },
    // 上传接口
    action: {
      type: String,
      required: true,
    },
    // 成功回调函数
    onSuccessCallback: {
      type: Function,
    },
    // 下载模板
    downloadTemplate: {
      type: Function,
    }
  },
  data() {
    return {
      // 调用接口时额外添加请求头
      headers: {},
      // 是否显示导入弹窗 使用自己的属性。这样console才不会报错 Avoid mutating a prop directly since the value will be overwritten whenever the parent component re-renders. Instead, use a data or computed property based on the prop's value. Prop being mutated: "showImportDialog"
      showImportDialogSon: false,
    }
  },
  watch: {
    showImportDialog(val, valOld) {
      this.showImportDialogSon = this.showImportDialog
    }
  },
  methods: {
    // 弹窗关闭时
    handleClose() {
      this.$emit('close')
    },
    // 导入用户之前
    beforeUpload(file) {
      beforeUploadExcel(file);
      beforeUploadFillHttpHeader(this.headers);
      console.log("this.headers", this.headers)
    },
    // 导入用户成功
    onSuccess(data, file, fileList) {
      console.log("上传成功")
      this.$emit('close')
      this.$message.success("上传成功")
      this.onSuccessCallback();
    },
    // 上传失败
    onError(err, file, fileList) {
      console.error("导入失败", err)
      // 获取失败的信息
      const errMessage = JSON.parse(err["message"]);
      if (errMessage.clientMessage) {
        this.$message.error(errMessage.clientMessage)
      } else {
        this.$message.error("上传失败，请稍后再试")
      }
    },
  }
}
</script>

<style lang="scss" scoped>

</style>
