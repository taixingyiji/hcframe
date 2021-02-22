<template>
  <div>
    <el-row
      style="padding-bottom: 10px;
    padding-top:10px;
    background-color: #f6f6f6;
    padding-left:10px
    "
    >
      <el-button
        type="success"
        size="mini"
        icon="el-icon-plus"
        circle
        @click="addRow"
      />
      <el-button
        type="danger"
        size="mini"
        icon="el-icon-minus"
        circle
        @click="deleteRow"
      />
    </el-row>
    <el-row>
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
          v-if="checkBoxVisible"
          type="selection"
        />
        <el-table-column
          type="index"
          width="60"
          :label="'#'"
        />

        <template v-for="col in tableHeads">
          <el-table-column
            v-if="col.isForm ===1"
            :key="col.fieldName"
            :prop="col.fieldName"
            :label="col.nameCn"
            :formatter="formatter"
            sortable
          >
            <template slot-scope="{row}">
              <el-col v-if="row.edit">
                <component
                  :is="col.webType"
                  v-if="
                    (col.javaType !== 'date') &&
                      col.webType!=='password'
                  "
                  v-model="row[col.fieldName]"
                  filterable
                  :type="col.javaType"
                  style="width:80%"
                  :value-format="timeFormat"
                >
                  <div v-if="col.selectList">
                    <component
                      :is="'el-option'"
                      v-for="(sel,val) in col.selectList"
                      :key="val"
                      :label="sel.selectKey"
                      :value="formatSelectVal(sel.selectValue,col.javaType)"
                    />
                  </div>
                </component>
                <el-input
                  v-if="col.webType === 'password'"
                  v-model="row[col.fieldName]"
                  style="width:80%"
                />
              </el-col>
              <span v-else>{{ formatter(row, {property: col.fieldName}, row[col.fieldName], 0) }}</span>
            </template>
          </el-table-column>
        </template>
        <el-table-column
          v-if="slotVisible"
          label="操作"
          :width="width"
        >
          <template
            v-slot="scope"
            style="text-align: center"
          >
            <el-button
              v-if="!scope.row.edit"
              type="primary"
              size="mini"
              @click="edit(scope.row)"
            >
              编辑
            </el-button>
            <el-button
              v-if="scope.row.edit"
              type="success"
              size="mini"
              @click="save(scope.row)"
            >
              保存
            </el-button>
            <el-button
              v-if="scope.row.edit"
              type="warning"
              size="mini"
              @click="cancelEdit(scope.row)"
            >
              取消
            </el-button>
            <slot :item="scope" />
          </template>
        </el-table-column>
      </el-table>
    </el-row>
  </div>
</template>

<script lang="ts">
import { Prop, Component, Vue, Emit } from 'vue-property-decorator'
import { Message } from 'element-ui'
import StringUtils from '@/common/StringUtils'

/**
 * @author lhc
 * @date 2020-06-09
 * @description 动态表头组件
 */
@Component({
  name: 'editTableCo'
})
export default class extends Vue {
  // 时间传给后台默认格式化
  @Prop({ type: String, default: 'timestamp' }) timeFormat!: string;
  // 是否显示复选框
  @Prop({ default: false, type: Boolean }) checkBoxVisible!: boolean;
  // 表头
  @Prop(Array) tableHeads!: Array<object>;
  // 获取的数据
  @Prop({ default: [], type: Array }) formList!: Array<object>;
  // 加载动画
  @Prop(Boolean) loading!: boolean;
  // 数据格式化
  @Prop(Function) formatter!: any;
  // 是否显示插槽
  @Prop({ default: false, type: Boolean }) slotVisible!: boolean;
  // 是否插槽宽度
  @Prop({ type: Number, default: 200 }) width!: number;
  private tempTable: Map<number, any> = new Map<number, any>();
  @Prop(Object)
  formRules!: any;

  private selectList: any[] = [];

  // 排序
  @Emit()
  handleSortChange(val: any) {
    return val
  }

  // 复选框
  @Emit()
  handleSelectionChange(val: any) {
    this.selectList = val
    return val
  }

  // 表格新增一条记录
  @Emit()
  // eslint-disable-next-line @typescript-eslint/no-empty-function
  addRow() {
  }

  // 删除选中记录
  @Emit()
  deleteRow() {
    if (this.selectList.length === 0) {
      Message.warning('请选择需要删除的行！')
    } else {
      this.selectList.forEach((value) => {
        const index: number = this.formList.findIndex(
          (item: any) => item.index === value.index
        )
        if (index !== -1) {
          this.formList.splice(index, 1)
        }
      })
    }
  }

  // 编辑一条记录
  edit(row: any) {
    row.edit = true
    this.tempTable.set(row.index, Object.assign({}, row))
  }

  // 保存一条记录
  @Emit()
  save(row: any) {
    const rules: any = this.formRules
    const rows: any = Object.assign({}, row)
    this.tableRule(rows, rules, (message: string) => {
      if (StringUtils.isNotEmpty(message)) {
        Message.warning(message)
      } else {
        row.edit = false
        this.tempTable.set(row.index, Object.assign({}, row))
      }
    })
  }

  @Emit()
  tableRule(rows: any, rules: any, callback: Function) {
    return { rows: rows, rules: rules, callback: callback }
  }

  formatSelectVal(val: any, type: string) {
    if (type === 'Integer') {
      return +val
    } else {
      return val
    }
  }

  // 取消一条记录
  async cancelEdit(row: any) {
    const rowValue = Object.assign({}, row)
    console.log(Object.keys(row).length)
    if (Object.keys(rowValue).length === 2) {
      const index: number = this.formList.findIndex(
        (item: any) => item.index === row.index
      )
      if (index !== -1) {
        this.formList.splice(index, 1)
      }
    } else {
      try {
        const param = this.tempTable.get(row.index)
        await Object.getOwnPropertyNames(row).forEach(
          await function(key) {
            if (StringUtils.isNotEmpty(param[key])) {
              row[key] = param[key]
            } else {
              row[key] = ''
            }
          }
        )
      } catch (e) {
        const index: number = this.formList.findIndex(
          (item: any) => item.index === row.index
        )
        if (index !== -1) {
          this.formList.splice(index, 1)
        }
      } finally {
        row.edit = false
      }
    }
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
