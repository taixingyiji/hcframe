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
      <el-form-item
        label="展示字段"
        prop="selectKey"
      >
        <el-input
          v-model="formData.selectKey"
          style="width:80%"
        />
        <el-popover
          style="margin-left: 10px"
          placement="top-start"
          title="展示字段"
          width="300"
          trigger="hover"
          content="用于展示在下拉列表中的信息"
        >
          <i
            slot="reference"
            class="el-icon-info icon-style"
          />
        </el-popover>
      </el-form-item>
      <el-form-item
        label="选项值"
        prop="selectValue"
      >
        <el-input
          v-model="formData.selectValue"
          style="width:80%"
        />
        <el-popover
          style="margin-left: 10px"
          placement="top-start"
          title="关于选项值"
          width="300"
          trigger="hover"
          content="用于传给数据库的值，在此字段下必须唯一！"
        >
          <i
            slot="reference"
            class="el-icon-info icon-style"
          />
        </el-popover>
      </el-form-item>
      <el-form-item
        label="选择类型"
        prop="selectType"
      >
        <el-select
          v-model="formData.selectType"
          style="width:80%"
          placeholder="请选择"
        >
          <el-option
            v-for="item in selectField"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
        <el-popover
          style="margin-left: 10px"
          placement="top-start"
          title="关于选择类型"
          width="300"
          trigger="hover"
          content="目前只实现了单选，多选功能敬请期待！"
        >
          <i
            slot="reference"
            class="el-icon-info icon-style"
          />
        </el-popover>
      </el-form-item>
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
        提交
      </el-button>
    </div>
  </el-dialog>
</template>
<script lang="ts">
import { Prop, Component, Vue, Emit } from 'vue-property-decorator'

  /**
   * @author lhc
   * @date 2020-06-09
   * @description 动态表单组件
   */
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
    // 表单提交按钮显示文字
    @Prop(String) formSubBotton!: string;
    // 表单校验规则
    @Prop(Object) formRules!: object;
    // 禁用字段
    @Prop() disableField!: Array<any>;
    // 表单提交类型 1为新增，2为编辑
    @Prop() formType!: number;
    @Prop() tableSelectList!: any[];

    // 提交表单
    @Emit()
    formSubmit(val: any, type: number) {
      return { val: val, type: type }
    }

    // 关闭表单
    @Emit()
    // eslint-disable-next-line @typescript-eslint/no-empty-function
    cancelForm() {
    }

    // 关闭表单前的操作
    @Emit()
    // eslint-disable-next-line @typescript-eslint/no-empty-function
    beforeCancelForm() {
    }

    selectField: any[] = [
      {
        value: 1,
        label: '单选'
      }
    ]
}
</script>

<style scoped>
  .icon-style {
    font-size: 20px;
    color: #8c9394;
  }
</style>
