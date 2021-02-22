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
        label="表名"
        prop="tableName"
      >
        <el-input
          v-model="formData.tableName"
          style="width:80%"
        />
        <el-popover
          style="margin-left: 10px"
          placement="top-start"
          title="关于表名"
          width="300"
          trigger="hover"
          content="基表名称必须以'BASE_'开头"
        >
          <i
            slot="reference"
            class="el-icon-info icon-style"
          />
        </el-popover>
      </el-form-item>
      <el-form-item
        label="表别名"
        prop="tableAlias"
      >
        <el-input
          v-model="formData.tableAlias"
          style="width:80%"
        />
        <el-popover
          style="margin-left: 10px"
          placement="top-start"
          title="关于表别名"
          width="300"
          trigger="hover"
          content="表别名用于前端及后台通信转义用，为了不在前端暴露数据库表名，因此使用表别名，且表别名不可重复！"
        >
          <i
            slot="reference"
            class="el-icon-info icon-style"
          />
        </el-popover>
      </el-form-item>
      <el-form-item
        label="主键"
        prop="tablePk"
      >
        <el-input
          v-model="formData.tablePk"
          style="width:80%"
        />
        <el-popover
          style="margin-left: 10px"
          placement="top-start"
          title="关于主键"
          width="300"
          trigger="hover"
          content="请设置成当前数据表的主键字段"
        >
          <i
            slot="reference"
            class="el-icon-info icon-style"
          />
        </el-popover>
      </el-form-item>
      <el-form-item
        label="表描述"
        prop="tableContent"
      >
        <el-input
          v-model="formData.tableContent"
          style="width:80%"
        />
        <el-popover
          style="margin-left: 10px"
          placement="top-start"
          title="关于表描述"
          width="300"
          trigger="hover"
          content="描述此表用途，如用户表等，系统的单表操作日志记录，中文表名会从字段获得！"
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
}
</script>

<style scoped>
  .icon-style{
    font-size:20px;
    color: #8c9394;
  }
</style>
