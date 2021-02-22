<template>
  <div>
    <el-table
      id="commonTable"
      ref="commonTable"
      v-loading="loading"
      :data="formList"
      border
      highlight-current-row
      style="width: 100%;"
      @sort-change="handleSortChange"
    >
      <!--      <el-table-column type="selection" />-->
      <el-table-column
        type="index"
        width="60"
      />
      <el-table-column
        v-for="col in tableHeads"
        :key="col.fieldnameInCn"
        :prop="col.fieldname"
        :label="col.fieldnameInCn"
        :formatter="formatter"
        sortable
      />
    </el-table>
  </div>
</template>

<script lang="ts">
import { Prop, Component, Vue, Emit } from 'vue-property-decorator'
@Component
export default class extends Vue {
    // 表头
    @Prop(Array) tableHeads!: Array<object>;
    // 获取的数据
    @Prop(Array) formList!: Array<object>;
    // 加载动画
    @Prop(Boolean) loading!: boolean;
    // 数据格式化
    @Prop(Function) formatter!: any;
    // 排序
    @Emit()
    // eslint-disable-next-line @typescript-eslint/no-empty-function
    handleSortChange(val: any) {
      return val
    }

    // 复选框
    @Emit()
    // eslint-disable-next-line @typescript-eslint/no-empty-function
    handleSelectionChange(val: any) {
      return val
    }
}
</script>
<style lang="scss">
  .el-table th {
    background-color: $lightGray;
    font-weight: 200;
  }

</style>
