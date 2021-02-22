<template>
  <el-form
    ref="formCo"
    :model="searchFormData"
    label-width="130px"
    :rules="formRules"
  >
    <component
      :is="'el-form-item'"
      v-for="col in formKey"
      :key="col.fieldname"
      :label="col.fieldnameInCn"
      :prop="col.fieldname"
    >
      <component
        :is="col.webType"
        v-if="
          (col.type !== 'date' || isAdd) &&
            col.webType !== 'textArea'
        "
        v-model="formData[col.fieldname]"
        filterable
        style="width:80%"
        :disabled="disabledFunction(col.change, col.fieldname)"
      >
        <component
          :is="'el-option'"
          v-for="sel in col.select"
          :key="sel.selectVal"
          :label="sel.select"
          :value="sel.selectVal"
        />
      </component>
      <el-input
        v-if="col.webType === 'textArea'"
        v-model="formData[col.fieldname]"
        style="width:80%"
        type="textarea"
      />
      <el-col
        v-if="!isAdd && col.type === 'date'"
        :span="8"
      >
        <el-form-item prop="date1">
          <el-date-picker
            v-model="formData[col.fieldname + '_start']"
            type="datetime"
            placeholder="选择时间"
            style="width: 95%;"
          />
        </el-form-item>
      </el-col>
      <el-col
        v-if="!isAdd && col.type === 'date'"
        class="line"
        :span="1"
      >
        -
      </el-col>
      <el-col
        v-if="!isAdd && col.type === 'date'"
        :span="10"
      >
        <el-form-item prop="date2">
          <el-date-picker
            v-model="formData[col.fieldname + '_end']"
            type="datetime"
            placeholder="选择时间"
            style="width: 80%;"
          />
        </el-form-item>
      </el-col>
    </component>
    <el-form-item>
      <el-button
        type="primary"
        @click="queryTable"
      >
        查询
      </el-button>
    </el-form-item>
  </el-form>
</template>

<script lang="ts">
import { Prop, Component, Vue, Emit } from 'vue-property-decorator'
  @Component
export default class extends Vue {
    // 表单数据  可为空
    @Prop(Object) searchFormData!: object;
    // 表单key值
    @Prop(Array) formKey!: Array<object>;
    // 表单校验规则
    @Prop(Object) formRules!: object;
    // 是否是添加或编辑 如果否,则是搜索表单
    @Prop({ type: Boolean, default: true }) isAdd!: boolean;
    // 表单提交类型
    @Prop() formType!: number;
    // 禁用表单输入框方法
    @Emit()
    // eslint-disable-next-line @typescript-eslint/no-empty-function
    queryTable() {}
}
</script>

<style scoped></style>
