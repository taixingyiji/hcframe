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
        label="名称"
        prop="sysDescription"
      >
        <el-input
          v-model="formData.sysDescription"
          style="width:80%"
          :disabled="formType===2"
        />
        <el-popover
          style="margin-left: 10px"
          placement="top-start"
          title="名称/描述"
          width="300"
          trigger="hover"
          content="使用者自己定义的名称，用于标识数据库作用等，可包含中文"
        >
          <i
            slot="reference"
            class="el-icon-info icon-style"
          />
        </el-popover>
      </el-form-item>
      <el-form-item
        label="KEY"
        prop="commonAlias"
      >
        <el-input
          v-model="formData.commonAlias"
          style="width:80%"
          :disabled="formType===2"
        />
        <el-popover
          style="margin-left: 10px"
          placement="top-start"
          title="KEY"
          width="300"
          trigger="hover"
          content="用于系统识别的代表数据库的唯一key，不可包含空格，不可包含中文"
        >
          <i
            slot="reference"
            class="el-icon-info icon-style"
          />
        </el-popover>
      </el-form-item>
      <el-form-item
        label="类型"
        prop="commonType"
      >
        <el-select
          v-model="formData.commonType"
          name="commonType"
          placeholder="请选择数据库类型"
          class="el-input"
          autocomplete="on"
          style="width:80%"
          :disabled="formType===2"
        >
          <el-option
            v-for="item in datasourceType"
            :key="item.typeKey"
            :label="item.typeKey"
            :value="item.typeKey"
            v-if="item===1"
          />
        </el-select>
        <el-popover
          style="margin-left: 10px"
          placement="top-start"
          title="数据库类型"
          width="300"
          trigger="hover"
          content="选择数据库类型，如Mysql,Oracle等"
        >
          <i
            slot="reference"
            class="el-icon-info icon-style"
          />
        </el-popover>
      </el-form-item>
      <el-form-item
        label="URL"
        prop="url"
      >
        <el-input
          v-model="formData.url"
          type="textarea"
          style="width:80%"
        />
        <el-popover
          style="margin-left: 10px"
          placement="top-start"
          title="URL"
          width="300"
          trigger="hover"
          content="用于设置数据库的url，与之前yml文件格式保持一致即可"
        >
          <i
            slot="reference"
            class="el-icon-info icon-style"
          />
        </el-popover>
      </el-form-item>
      <el-form-item
        label="用户名"
        prop="username"
      >
        <el-input
          v-model="formData.username"
          style="width:80%"
        />
        <el-popover
          style="margin-left: 10px"
          placement="top-start"
          title="用户名"
          width="300"
          trigger="hover"
          content="数据库用户名"
        >
          <i
            slot="reference"
            class="el-icon-info icon-style"
          />
        </el-popover>
      </el-form-item>
      <el-form-item
        label="密码"
        prop="password"
      >
        <el-input
          :key="passwordType"
          ref="password"
          v-model="formData.password"
          :type="passwordType"
          placeholder="密码"
          name="password"
          tabindex="2"
          autocomplete="on"
          style="width:80%"
        />
        <span
          style="margin-left: 5px"
          @click="showPwd"
        >
          <svg-icon :name="passwordType === 'password' ? 'eye-off' : 'eye-on'" />
        </span>
        <el-popover
          style="margin-left: 10px"
          placement="top-start"
          title="密码"
          width="300"
          trigger="hover"
          content="用于设置数据库的url，与之前yml文件格式保持一致即可"
        >
          <i
            slot="reference"
            class="el-icon-info icon-style"
          />
        </el-popover>
      </el-form-item>
      <el-form-item
        label="库名"
        prop="schemaName"
      >
        <el-input
          v-model="formData.schemaName"
          style="width:80%"
        />
        <el-popover
          style="margin-left: 10px"
          placement="top-start"
          title="库名"
          width="300"
          trigger="hover"
          content="Mysql中为databases中的名称，达梦为模式名，Oracle为创建的用户名"
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
        :loading="testLoading"
        @click="testSubmit('formCommon', formType)"
      >
        测试
      </el-button>
      <el-button
        type="primary"
        :loading="submitLoading"
        @click="formSubmit('formCommon', formType)"
      >
        提交
      </el-button>
    </div>
  </el-dialog>
</template>

<script lang="ts">
import { Prop, Component, Emit } from 'vue-property-decorator'
import { mixins } from 'vue-class-component'
import Datasource from '@/api/mixins/Datasource'

  /**
   * @author lhc
   * @date 2020-06-09
   * @description 动态表单组件
   */
  @Component
export default class extends mixins(Datasource) {
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
    @Prop({ type: Boolean, default: false }) submitLoading!: boolean;
    @Prop({ type: Boolean, default: false }) testLoading!: boolean;

    // 提交表单
    @Emit()
    formSubmit(val: any, type: number) {
      return { val: val, type: type }
    }

    @Emit()
    testSubmit(val: any, type: number) {
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

    mounted() {
      this.getTypeList()
    }
}
</script>

<style scoped>
  .icon-style {
    font-size: 20px;
    color: #8c9394;
  }
</style>
