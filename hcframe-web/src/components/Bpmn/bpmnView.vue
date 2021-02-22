<template>
  <div ref="showCanvas" />
</template>

<script lang="ts">
import { Component, Prop, Vue } from 'vue-property-decorator'
import BpmnViewer from 'bpmn-js/lib/Viewer'

@Component({
  name: 'bpmnViewer',
  components: {}
})
export default class extends Vue {
  @Prop({ required: true }) private showData!: any;
  private bpmnViewer = new BpmnViewer({});

  created() {
    window.addEventListener('resize', () => {
      this.showBpmn(this.showData)
    })
  }

  mounted() {
    if (this.showData) {
      this.showBpmn(this.showData)
    }
  }

  showBpmn(showData: any) {
    this.bpmnViewer && this.bpmnViewer.destroy()
    this.bpmnViewer = new BpmnViewer({ container: this.$refs.showCanvas })
    // 数据替换为camunda
    showData = showData.replace(new RegExp('activiti:', 'gm'), 'camunda:')
    this.bpmnViewer.importXML(showData, (err: Error) => {
      if (err) {
        this.$message({
          message: '打开模型出错,请确认该模型符合Bpmn2.0规范',
          type: 'error'
        })
      } else {
        const ref: any = this.$refs
        const viewportHeight = ref.showCanvas.querySelector('.viewport').getBBox().height
        ref.showCanvas.style.height = viewportHeight + 'px'
        this.bpmnViewer.get('canvas').zoom('fit-viewport', 'auto') // 居中显示
      }
    })
  }
}
</script>
<style lang="scss">
.bjs-powered-by {
  display: none;
}
</style>
