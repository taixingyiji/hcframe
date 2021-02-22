<template>
  <div class="table-dropdown">
    <el-table
      id="commonTable"
      ref="commonTable"
      v-loading="loading"
      :data="formList"
      border
      highlight-current-row
      style="width: 100%;"
      @sort-change="handleSortChange"
      @selection-change="handleSelectionChange"
    >
      <el-table-column
        v-show="showSelection"
        type="selection"
      />
      <el-table-column
        type="index"
        width="60"
      />
      <template v-for="col in tableHeads">
        <el-table-column
          v-if="col.isHead ===1"
          :key="col.nameCn"
          :prop="col.fieldName"
          :label="col.nameCn"
          :formatter="formatter"
          sortable
        />
      </template>
      <el-table-column
        label="操作"
        :width="width"
      >
        <template
          slot-scope="scope"
          style="text-align: center"
        >
          <el-button
            v-show="editTable"
            size="mini"
            @click="handleEdit(scope.$index, scope.row)"
          >
            编辑
          </el-button>
          <el-button
            v-show="enabledTable"
            size="mini"
            @click="handleDisabled(scope.$index, scope.row)"
          >
            启用/禁用
          </el-button>
          <el-button
            v-show="grantRoleTable"
            size="mini"
            @click="grantRole(scope.$index, scope.row)"
          >
            授权
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script lang="ts">
import { Prop, Component, Vue, Emit } from 'vue-property-decorator'
@Component
export default class extends Vue {
  // 表头
  @Prop(Array) tableHeads!: Array<object>;
  // 获取详情数据
  @Prop(Array) formInfoList!: Array<object>;
  // 获取的数据
  @Prop(Array) formList!: Array<object>;
  // 是否显示表格单选框
  @Prop({ default: true }) showSelection!: boolean;
  // 表格操作部分宽度设置
  @Prop({ default: 200 }) width!: number;
  // 是否显示表格操作编辑按钮
  @Prop({ default: true }) editTable!: boolean;
  // 是否显示表格操作中启用禁用按钮
  @Prop({ default: true }) enabledTable!: boolean;
  // 是否显示表格操作授权按钮
  @Prop({ default: false }) grantRoleTable!: boolean;
  // 加载动画
  @Prop(Boolean) loading!: boolean;
  // 数据格式化
  @Prop(Function) formatter!: any;
  // 排序
  @Emit()
  handleSortChange(val: any) {
    return val
  }

  // 复选框
  @Emit()
  handleSelectionChange(val: any) {
    return val
  }

  // 编辑
  @Emit()
  handleEdit(index: number, row: any) {
    return { index, row }
  }

  // 是否禁用
  @Emit()
  handleDisabled(index: number, row: any) {
    return { index, row }
  }

  // 是否授权
  @Emit()
  grantRole(index: number, row: any) {
    return { index, row }
  }
}
</script>
<style lang="scss">
  .el-table th {
    background-color: $lightGray;
    font-weight: 200;
  }
  .form-expend .el-form-item {
    margin-right: 0;
    margin-bottom: 0;
    width: 25%;
  }
  .form-expend {
    font-size: 0;
    margin-left: 20px;
    .el-form-item__label {
      font-size: 12px;
      color: $middleGray;
    }
  }
  .form-expend label {
    width: 90px;
    color: #99a9bf;
  }
  .table-dropdown{
    .el-table--medium th, .el-table--medium td {
      padding: 5px 0;
    }
  }
</style>
