<template>
  <el-container>
    <el-main>
      <el-row style="margin-top: 50px">
        <bpmn-view :show-data="showBpmnData()" />
      </el-row>
    </el-main>
  </el-container>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator'
import bpmnView from '@/components/Bpmn/bpmnView.vue'

@Component({
  name: 'onlyViewBpmn',
  components: {
    bpmnView
  }
})
export default class extends Vue {
  showBpmnData() {
    const bpmnData: any = JSON.parse(localStorage.getItem('showData') as string)
    return bpmnData
  }
}
</script>
