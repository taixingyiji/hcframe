<template>
  <el-row>
    <el-form
      :inline="true"
      class="user-query-option"
      :model="searchData"
    >
      <div
        v-for="col in formKey"
        :key="col.fieldName"
      >
        <el-form-item
          v-if="col.isSearch===1"
          :prop="col.fieldName"
        >
          <component
            :is="col.webType"
            v-if="col.webType !== 'el-date-picker'&&col.webType!=='password'"
            v-model="searchData[col.fieldName]"
            :type="col.javaType"
            :value-format="timeFormat"
            :placeholder="col.nameCn"
          >
            <div v-if="col.selectList">
              <component
                :is="'el-option'"
                v-for="(sel, val) in col.selectList"
                :key="val"
                :label="sel.selectKey"
                :value="formatSelectVal(sel.selectValue,col.javaType)"
              />
            </div>
          </component>
          <el-col
            v-if=" col.webType === 'el-date-picker'"
            :span="10"
          >
            <el-form-item prop="date1">
              <el-date-picker
                v-model="searchData[col.fieldName + '_start']"
                type="datetime"
                :placeholder="col.nameCn+'开始'"
                style="width: 95%;"
                :value-format="timeFormat"
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
            :span="12"
          >
            <el-form-item prop="date2">
              <el-date-picker
                v-model="searchData[col.fieldName + '_end']"
                :type="col.javaType"
                :placeholder="col.nameCn+'结束'"
                :value-format="timeFormat"
                style="width: 80%;"
              />
            </el-form-item>
          </el-col>
        </el-form-item>
      </div>

      <el-form-item>
        <el-button
          type="primary"
          @click="searchSubmit"
        >
          查询
        </el-button>
      </el-form-item>
      <el-form-item>
        <el-button @click="resetSearchForm">
          重置
        </el-button>
      </el-form-item>
    </el-form>
  </el-row>
</template>

<script lang="ts">
import { Prop, Component, Vue, Emit } from 'vue-property-decorator'
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

  // 重制搜索表单
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

<style scoped></style>
