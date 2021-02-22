<template>
  <el-dialog
    ref="form"
    :title="formTitle"
    :visible.sync="formVisible"
    :close-on-click-modal="false"
    :before-close="beforeCancelForm"
  >
    <el-form
      ref="formCommon"
      :model="formData"
      label-width="130px"
      :rules="formRules"
    >
      <div
        v-for="col in formKey"
        :key="col.fieldName"
      >
        <component
          :is="'el-form-item'"
          v-if="col.isForm===1"
          :label="col.nameCn"
          :prop="col.fieldName"
        >
          <div>
            <component
              :is="col.webType"
              v-if="
                (col.javaType !== 'date' || isAdd) &&
                  col.webType !== 'textArea' && col.webType!=='password'
              "
              v-model="formData[col.fieldName]"
              filterable
              :type="col.javaType"
              style="width:80%"
              :value-format="timeFormat"
              :disabled="disFunction(col.isChange, col.fieldName)"
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
              v-if="col.webType === 'textArea'"
              v-model="formData[col.fieldName]"
              style="width:80%"
              type="textarea"
              :disabled="disFunction(col.isChange, col.fieldName)"
            />
            <el-input
              v-if="col.webType === 'password'"
              v-model="formData[col.fieldName]"
              style="width:80%"
              type="password"
              :disabled="disFunction(col.isChange, col.fieldName)"
            />
            <el-col
              v-if="!isAdd && col.javaType === 'date'"
              :span="8"
            >
              <el-form-item prop="date1">
                <el-date-picker
                  v-model="formData[col.fieldName + '_start']"
                  type="datetime"
                  placeholder="选择时间"
                  style="width: 95%;"
                  :value-format="timeFormat"
                />
              </el-form-item>
            </el-col>
            <el-col
              v-if="!isAdd && col.javaType === 'date'"
              class="line"
              :span="1"
            >
              -
            </el-col>
            <el-col
              v-if="!isAdd && col.javaType === 'date'"
              :span="10"
            >
              <el-form-item prop="date2">
                <el-date-picker
                  v-model="formData[col.fieldName + '_end']"
                  :type="col.javaType"
                  placeholder="选择时间"
                  :value-format="timeFormat"
                  style="width: 80%;"
                />
              </el-form-item>
            </el-col>
          </div>
        </component>
      </div>
    </el-form>
    <div
      slot="footer"
      class="dialog-footer"
    >
      <el-button @click="cancelForm">
        取消
      </el-button>
      <el-button
        type="primary"
        @click="formSubmit('formCommon', formType)"
      >
        {{ formSubBotton }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script lang="ts">
import { Prop, Component, Vue, Emit } from 'vue-property-decorator'
@Component
export default class extends Vue {
  // 时间传给后台默认格式化
  @Prop({ type: String, default: 'timestamp' }) timeFormat!: string;
  // 表单标题
  @Prop(String) formTitle!: string;
  // 表单数据  可为空
  @Prop(Object) formData!: object;
  // 展示关闭表单
  @Prop(Boolean) formVisible!: boolean;
  // 表单key值
  @Prop(Array) formKey!: Array<object>;
  // 表单提交按钮显示文字
  @Prop(String) formSubBotton!: string;
  // 表单校验规则
  @Prop(Object) formRules!: object;
  // 是否是添加或编辑 如果否,则是搜索表单
  @Prop({ type: Boolean, default: true }) isAdd!: boolean;
  // 表单提交类型 1为新增，2为编辑
  @Prop() formType!: number;
  // 表单不可编辑的部分
  @Prop() disableField!: Array<any>;

  // 禁用表单输入框方法
  disFunction(change: any, fieldName: any) {
    // eslint-disable-next-line @typescript-eslint/no-unused-vars
    let flag = false
    this.disableField.forEach((value) => {
      if (value === fieldName) {
        flag = true
      }
    })
    if (this.formType === 1) {
      let flag = false
      this.disableField.forEach((value) => {
        if (value === fieldName) {
          flag = true
        }
      })
      return flag
    } else {
      if (change === 0) {
        return true
      } else {
        let flag = false
        this.disableField.forEach((value) => {
          if (value === fieldName) {
            flag = true
          }
        })
        return flag
      }
    }
  }

  // 提交表单
  @Emit()
  formSubmit(val: any, type: number) {
    return { val: val, type: type }
  }

  // 关闭表单
  @Emit()
  cancelForm() {}

  // 关闭表单前的操作
  @Emit()
  beforeCancelForm() {}

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
