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
      :header-cell-style="headerCellStyle"
      @sort-change="handleSortChange"
      @selection-change="handleSelectionChange"
    >
      <el-table-column
        v-if="checkBoxVisible"
        type="selection"
        :align="cellAlign"
      />
      <el-table-column
        type="index"
        :label="'#'"
        :align="cellAlign"
      />
      <template v-for="col in tableHeads">
        <el-table-column
          v-if="col.isHead ===1"
          :key="col.fieldName"
          :prop="col.fieldName"
          :label="col.nameCn"
          :formatter="formatter"
          sortable
          :align="cellAlign"
          :min-width="col.commonWidth"
        >
          <template slot-scope="{row}">
            <span
              v-if="col.fieldName==='tableId'"
            >{{ formatterTable(row[col.fieldName],col.fieldName) }}</span>
            <span v-else-if="col.fieldName==='status'">
              <el-tag
                v-if="row[col.fieldName]===1"
                type="success"
                :hit="true"
                size="small"
              > <i
                style="font-size: 15px"
                class="el-icon-success"
              />在线</el-tag>
              <el-tag
                v-else
                :hit="true"
                type="danger"
                size="small"
              > <i
                style="font-size: 15px"
                class="el-icon-error"
              />离线</el-tag>
            </span>
            <span v-else-if="col.fieldName==='isDefault'">
              <i
                v-if="row[col.fieldName]===1"
                style="font-size: 15px"
                class="el-icon-star-on"
              />
            </span>
            <span v-else>{{ formatterTable(row[col.fieldName],col.fieldName) }}</span>
          </template>
        </el-table-column>
      </template>
      <el-table-column
        v-if="slotVisible"
        label="操作"
        :width="width"
      >
        <template v-slot="scope">
          <slot :item="scope" />
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script lang="ts">
import { Prop, Component, Vue, Emit } from 'vue-property-decorator'
import StringUtils from '@/common/StringUtils'
import { SettingsModule } from '@/store/modules/settings'

/**
 * @author lhc
 * @date 2020-06-09
 * @description 动态表头组件
 */
@Component({
  name: 'tableCo'
})
export default class TableCo extends Vue {
  // 是否显示复选框
  @Prop({ default: false, type: Boolean }) checkBoxVisible!: boolean;
  // 表头
  @Prop(Array) tableHeads!: Array<object>;
  // 获取的数据
  @Prop(Array) formList!: Array<object>;
  // 加载动画
  @Prop(Boolean) loading!: boolean;
  // 数据格式化
  @Prop(Function) formatter!: any;
  // 是否显示插槽
  @Prop({ default: false, type: Boolean }) slotVisible!: boolean;
  // 插槽的数量
  @Prop({ default: 1, type: Number }) slotNumber!: number;
  // 是否插槽宽度
  @Prop({ type: Number, default: 200 }) width!: number;
  @Prop() tableSelectList!: any[];
  // 表格对齐方式
  @Prop({ type: String, default: 'center' }) cellAlign!: string;

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

  formatterTable(row: any, fieldName: string): string {
    if (fieldName === 'tableId') {
      let table = ''
      this.tableSelectList.forEach((value: any) => {
        if (value.value === row) {
          table = value.label
        }
      })
      if (StringUtils.isEmpty(table)) {
        return row
      } else {
        return table
      }
    } else {
      return row
    }
  }

  // 设置tableHeader的样式
  private headerCellStyle() {
    const headerCellStyle = {
      color: '#fff',
      background: this.themeColor,
      fontWeight: 'bold'
    }
    return headerCellStyle
  }

  get themeColor() {
    return SettingsModule.theme
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
    // color: $middleGray;
  }
}

.form-expend label {
  width: 90px;
  color: #99a9bf;
}

.table-dropdown {
  .el-table--medium th,
  .el-table--medium td {
    padding: 5px 0;
  }

  /*.el-table-column--selection{*/
  /*  display:none;*/
  /*}*/
}
</style>
