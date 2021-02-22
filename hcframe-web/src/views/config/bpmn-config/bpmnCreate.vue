<template>
  <el-container>
    <el-main>
      <el-row style="margin-top: 50px">
        <bpmn
          :update-date="updateDate"
          :update-bpmn-flag="updateBpmnFlag"
          @createData="createData"
          @editData="editData"
        />
      </el-row>
    </el-main>
  </el-container>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator'
import bpmn from '@/components/Bpmn/index.vue'
import ActivitiApi from '@/api/api/activiti/ActivitiApi'
import CONFIG from '@/config'

@Component({
  name: 'BpmnCreate',
  components: {
    bpmn
  }
})
export default class extends Vue {
    private ActivitiApi: ActivitiApi = new ActivitiApi(CONFIG.ACTIVTI_URL);

    private updateBpmnFlag: any = undefined
    private bpmnNameFrom: any = undefined
    private editBpmnParams: any = undefined
    private editBpmnFile: any = undefined

    private updateDate: any

    created() {
      // 由于页面切换时会导致参数丢失，采用localStorage来存储
      const bpmnLocalstorage: any = JSON.parse(localStorage.getItem('bpmnLocalstorage') as string)
      this.updateBpmnFlag = bpmnLocalstorage.updateBpmnFlag
      if (!this.updateBpmnFlag) {
        this.bpmnNameFrom = bpmnLocalstorage.bpmnNameFrom
        this.updateDate = this.bpmnNameFrom
      } else {
        this.editBpmnParams = bpmnLocalstorage.editBpmnParams
        this.editBpmnFile = bpmnLocalstorage.editBpmnFile
        this.updateDate = this.editBpmnFile
      }

      /* -------以下代码不必删除------------ */
      // 正常传参
      // this.updateBpmnFlag = this.$route.params.updateBpmnFlag
      // if(!this.updateBpmnFlag){
      //     this.bpmnNameFrom = this.$route.params.bpmnNameFrom
      //     this.updateDate =this.bpmnNameFrom
      // }else {
      //     this.editBpmnParams = this.$route.params.editBpmnParams
      //     this.editBpmnFile = this.$route.params.editBpmnFile
      //     this.updateDate =this.editBpmnFile
      // }
    }

    // 上传新建数据
    async createData(file: any, fileName: string, name: string) {
      // 新建工作流
      const { data } = await this.ActivitiApi.updateProcessXml({ file, fileName, name })
      if (data.code !== 0) {
        this.$message.error(data.msg)
      } else {
        this.$message.success('上传成功！')
        this.$router.push({ name: 'bpmnList' })
      }
    }

    private async editData(file: any) {
      // 修改工作流
      Object.assign(this.editBpmnParams, { file })
      const { data } = await this.ActivitiApi.editProcessXml(this.editBpmnParams)
      if (data.code !== 0) {
        this.$message.error(data.msg)
      } else {
        this.$message.success('修改成功！')
        this.$router.push({ name: 'bpmnList' })
      }
    }
}
</script>
