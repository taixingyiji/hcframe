<template>
  <el-form
    :inline="true"
    :model="searchData"
  >
    <el-form-item
      v-for="col in formKey"
      :key="col.fieldName"
      :prop="col.fieldName"
    >
      <el-row
        v-if="col.isSearch===1 && col.webType !== 'el-date-picker' && col.webType!=='password'"
      >
        <el-col :span="8">
          {{ col.nameCn }}:
        </el-col>
        <el-col :span="16">
          <component
            :is="col.webType"
            v-if="col.webType !== 'el-date-picker'&&col.webType!=='password'"
            v-model="searchData[col.fieldName]"
            :type="col.javaType"
            :value-format="timeFormat"
            :placeholder="'请输入'+col.nameCn"
            size="small"
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
        </el-col>
      </el-row>
      <el-row v-if="col.isSearch===1 && col.webType === 'el-date-picker'">
        <el-col :span="4">
          {{ col.nameCn }}:
        </el-col>
        <el-col :span="9">
          <el-form-item prop="date1">
            <el-date-picker
              v-model="searchData[col.fieldName + '_start']"
              type="datetime"
              :placeholder="col.nameCn+'开始'"
              style="width: 95%;"
              :value-format="timeFormat"
              size="small"
            />
          </el-form-item>
        </el-col>
        <el-col
          v-if=" col.webType === 'el-date-picker'"
          class="line"
          :span="1"
        >
          -
        </el-col>
        <el-col
          v-if="col.webType === 'el-date-picker'"
          :span="10"
        >
          <el-form-item prop="date2">
            <el-date-picker
              v-model="searchData[col.fieldName + '_end']"
              :type="col.javaType"
              :placeholder="col.nameCn+'结束'"
              :value-format="timeFormat"
              style="width: 80%;"
              size="small"
            />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form-item>
    <el-row>
      <el-form-item>
        <el-button
          type="primary"
          size="small"
          @click="searchSubmit"
        >
          查询
        </el-button>
      </el-form-item>
      <el-form-item>
        <el-button
          size="small"
          @click="resetSearchForm"
        >
          重置
        </el-button>
      </el-form-item>
    </el-row>
  </el-form>
</template>

<script lang="ts">
import { Prop, Component, Vue, Emit } from 'vue-property-decorator'

/**
 * @author lhc
 * @date 2020-06-09
 * @description 搜索表单组件，带Key值
 */
@Component
export default class extends Vue {
  // 时间传给后台默认格式化
  @Prop({ type: String, default: 'timestamp' }) timeFormat!: string;
  // 表单数据  可为空
  @Prop(Object) searchData!: object;
  // 展示关闭表单
  @Prop(Boolean) formVisible!: boolean;
  // 表单key值
  @Prop(Array) formKey!: Array<object>;

  // 提交表单
  @Emit()
  // eslint-disable-next-line @typescript-eslint/no-empty-function
  searchSubmit() {}

  // 重置搜索表单
  @Emit()
  // eslint-disable-next-line @typescript-eslint/no-empty-function
  resetSearchForm() {}

  // 判断下拉框value是否为数字，且自动转换类型
  formatSelectVal(val: any, type: string) {
    if (type === 'Integer') {
      return +val
    } else {
      return val
    }
  }
}
</script>

<style lang="scss" scoped>
.select-card {
  margin-top: 10px;
  margin-bottom: 20px;

  .el-row {
    margin-bottom: 15px;
    margin-right: 15px;

    &:last-child {
      margin-bottom: 0px;
      text-align: center;

      .el-form-item {
        margin-bottom: 0px;
      }
    }
  }
}
</style>
