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
        prop="tableId"
      >
        <el-select
          v-model="formData.tableId"
          style="width:80%"
          filterable
          placeholder="请选择"
        >
          <el-option
            v-for="item in tableSelectList"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
        <el-popover
          style="margin-left: 10px"
          placement="top-start"
          title="关于表名"
          width="300"
          trigger="hover"
          content="表名下拉框，带搜索功能，列表不包含基表"
        >
          <i
            slot="reference"
            class="el-icon-info icon-style"
          />
        </el-popover>
      </el-form-item>
      <el-form-item
        label="字段名"
        prop="fieldName"
      >
        <el-input
          v-model="formData.fieldName"
          style="width:80%"
        />
        <el-popover
          style="margin-left: 10px"
          placement="top-start"
          title="关于字段名"
          width="300"
          trigger="hover"
          content="字段名需与数据库字段名一致，区分大小写"
        >
          <i
            slot="reference"
            class="el-icon-info icon-style"
          />
        </el-popover>
      </el-form-item>
      <el-form-item
        label="字段中文"
        prop="nameCn"
      >
        <el-input
          v-model="formData.nameCn"
          style="width:80%"
        />
        <el-popover
          style="margin-left: 10px"
          placement="top-start"
          title="关于字段中文"
          width="300"
          trigger="hover"
          content="设置字段前端显示的中文"
        >
          <i
            slot="reference"
            class="el-icon-info icon-style"
          />
        </el-popover>
      </el-form-item>
      <el-form-item
        label="JAVA字段"
        prop="javaField"
      >
        <el-input
          v-model="formData.javaField"
          style="width:80%"
        />
        <el-popover
          style="margin-left: 10px"
          placement="top-start"
          title="关于JAVA字段"
          width="300"
          trigger="hover"
          content="此字段必须与java类中字段保持一致"
        >
          <i
            slot="reference"
            class="el-icon-info icon-style"
          />
        </el-popover>
      </el-form-item>
      <el-form-item
        label="JAVA类型"
        prop="javaType"
      >
        <el-radio-group v-model="formData.javaType">
          <el-radio-button
            v-for="item in javaFieldList"
            :key="item.value"
            :label="item.value"
          >
            {{ item.label }}
          </el-radio-button>
        </el-radio-group>
        <el-popover
          style="margin-left: 10px"
          placement="top-start"
          title="关于JAVA类型"
          width="300"
          trigger="hover"
          content="主要包含四种，数字为Integer，字符串为String,日期为date,日期加时间为datetime"
        >
          <i
            slot="reference"
            class="el-icon-info icon-style"
          />
        </el-popover>
      </el-form-item>
      <el-form-item
        label="前端类型"
        prop="webType"
      >
        <el-radio-group v-model="formData.webType">
          <el-radio-button
            v-for="item in webTypeList"
            :key="item.value"
            :label="item.value"
          >
            {{ item.label }}
          </el-radio-button>
        </el-radio-group>
        <el-popover
          style="margin-left: 10px"
          placement="top-start"
          title="关于前端类型"
          width="300"
          trigger="hover"
          content="主要包含五种，输入框为el-input，密码为password,下拉选择框el-select,时间选择器为el-date-picker，大文本输入框为textArea"
        >
          <i
            slot="reference"
            class="el-icon-info icon-style"
          />
        </el-popover>
      </el-form-item>
      <el-form-item
        label="基表"
        prop="fkTable"
      >
        <el-input
          v-model="formData.fkTable"
          style="width:80%"
        />
        <el-popover
          style="margin-left: 10px"
          placement="top-start"
          title="关于基表"
          width="300"
          trigger="hover"
          content="此字段写入基表的表别名，此字段若不为空，则下拉选项从基表获取，select表中关于此字段设置不生效！"
        >
          <i
            slot="reference"
            class="el-icon-info icon-style"
          />
        </el-popover>
      </el-form-item>
      <el-row>
        <el-col :span="10">
          <el-form-item
            label="是否可更改"
            prop="isChange"
          >
            <el-radio-group v-model="formData.isChange">
              <el-radio-button :label="1">
                是
              </el-radio-button>
              <el-radio-button :label="0">
                否
              </el-radio-button>
            </el-radio-group>
            <el-popover
              style="margin-left: 10px"
              placement="top-start"
              title="关于是否可更改"
              width="300"
              trigger="hover"
              content="此字段若为是，则新增的时候表单可写入，但是新增完成后，在编辑弹窗中不可修改此字段值"
            >
              <i
                slot="reference"
                class="el-icon-info icon-style"
              />
            </el-popover>
          </el-form-item>
        </el-col>
        <el-col :span="14">
          <el-form-item
            label="表格"
            prop="isHead"
          >
            <el-radio-group v-model="formData.isHead">
              <el-radio-button :label="1">
                是
              </el-radio-button>
              <el-radio-button :label="0">
                否
              </el-radio-button>
            </el-radio-group>
            <el-popover
              style="margin-left: 10px"
              placement="top-start"
              title="关于表格"
              width="300"
              trigger="hover"
              content="此字段若为是，则会显示在表格中"
            >
              <i
                slot="reference"
                class="el-icon-info icon-style"
              />
            </el-popover>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="10">
          <el-form-item
            label="搜索"
            prop="isSearch"
          >
            <el-radio-group v-model="formData.isSearch">
              <el-radio-button :label="1">
                是
              </el-radio-button>
              <el-radio-button :label="0">
                否
              </el-radio-button>
            </el-radio-group>
            <el-popover
              style="margin-left: 10px"
              placement="top-start"
              title="关于搜索"
              width="300"
              trigger="hover"
              content="此字段若为是，则会显示在搜索表单中"
            >
              <i
                slot="reference"
                class="el-icon-info icon-style"
              />
            </el-popover>
          </el-form-item>
        </el-col>
        <el-col :span="14">
          <el-form-item
            label="详情页"
            prop="isDetails"
          >
            <el-radio-group v-model="formData.isDetails">
              <el-radio-button :label="1">
                是
              </el-radio-button>
              <el-radio-button :label="0">
                否
              </el-radio-button>
            </el-radio-group>
            <el-popover
              style="margin-left: 10px"
              placement="top-start"
              title="关于详情页"
              width="300"
              trigger="hover"
              content="此字段若为是，则会显示在详情页中"
            >
              <i
                slot="reference"
                class="el-icon-info icon-style"
              />
            </el-popover>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="10">
          <el-form-item
            label="表单"
            prop="isForm"
          >
            <el-radio-group v-model="formData.isForm">
              <el-radio-button :label="1">
                是
              </el-radio-button>
              <el-radio-button :label="0">
                否
              </el-radio-button>
            </el-radio-group>
            <el-popover
              style="margin-left: 10px"
              placement="top-start"
              title="关于表单"
              width="300"
              trigger="hover"
              content="此字段若为是，则会显示在新增及编辑表单中"
            >
              <i
                slot="reference"
                class="el-icon-info icon-style"
              />
            </el-popover>
          </el-form-item>
        </el-col>
        <el-col :span="14">
          <el-form-item
            label="批量更新"
            prop="isBatch"
          >
            <el-radio-group v-model="formData.isBatch">
              <el-radio-button :label="1">
                是
              </el-radio-button>
              <el-radio-button :label="0">
                否
              </el-radio-button>
            </el-radio-group>
            <el-popover
              style="margin-left: 10px"
              placement="top-start"
              title="关于批量更新"
              width="300"
              trigger="hover"
              content="此字段若为是，则会显示在批量更新表单中"
            >
              <i
                slot="reference"
                class="el-icon-info icon-style"
              />
            </el-popover>
          </el-form-item>
        </el-col>
      </el-row>
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

    javaFieldList: any[] = [
      {
        value: 'Integer',
        label: '数字'
      }, {
        value: 'String',
        label: '字符串'
      }, {
        value: 'datetime',
        label: '日期+时间'
      }, {
        value: 'date',
        label: '日期'
      }
    ]

    webTypeList: any[] = [
      {
        value: 'el-input',
        label: '输入框'
      }, {
        value: 'password',
        label: '密码框'
      }, {
        value: 'el-select',
        label: '下拉框'
      }, {
        value: 'el-date-picker',
        label: '日期框'
      }, {
        value: 'textArea',
        label: '大文本'
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
