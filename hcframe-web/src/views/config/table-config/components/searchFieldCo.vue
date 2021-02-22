<template>
  <el-row>
    <el-form
      :inline="true"
      :model="searchData"
      label-width="90px"
      label-position="right"
    >
      <el-row>
        <el-col :span="8">
          <el-form-item
            label="表名:"
            prop="tableName"
          >
            <el-select
              v-model="searchData.tableId"
              filterable
              placeholder="请选择表名"
              style="width:70%"
              size="small"
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
              title="表名选择"
              width="300"
              trigger="hover"
              content="表名下拉框，带搜索功能，不包含基表，本页面默认显示全部表的全部字段"
            >
              <i
                slot="reference"
                class="el-icon-info icon-style"
              />
            </el-popover>
          </el-form-item>
        </el-col>

        <el-col :span="8">
          <el-form-item
            label="字段名:"
            prop="tableName"
          >
            <el-input
              v-model="searchData.fieldName"
              style="width:80%"
              size="small"
              placeholder="请填写字段名"
            />
          </el-form-item>
        </el-col>

        <el-col :span="8">
          <el-form-item
            label="字段中文:"
            prop="tableAlias"
          >
            <el-input
              v-model="searchData.nameCn"
              style="width:80%"
              size="small"
              placeholder="请填写字段中文"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="8">
          <el-form-item
            label="JAVA字段:"
            prop="tablePk"
          >
            <el-input
              v-model="searchData.javaField"
              style="width:80%"
              size="small"
              placeholder="请填写JAVA字段"
            />
          </el-form-item>
        </el-col>

        <el-col :span="8">
          <el-form-item
            label="JAVA类型:"
            prop="tableContent"
          >
            <el-select
              v-model="searchData.javaType"
              style="width:80%"
              placeholder="请选择JAVA类型"
              size="small"
            >
              <el-option
                v-for="item in javaFieldList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>

        <el-col :span="8">
          <el-form-item
            label="前端类型:"
            prop="tableContent"
          >
            <el-select
              v-model="searchData.webType"
              style="width:80%"
              placeholder="请选择前端类型"
              size="small"
            >
              <el-option
                v-for="item in webTypeList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row style="text-align: center">
        <el-form-item>
          <el-button
            type="primary"
            size="small"
            @click="searchSubmit"
          >
            查询
          </el-button>
          <el-button
            size="small"
            @click="resetSearchForm"
          >
            重置
          </el-button>

          <el-popover
            style="margin-left: 10px"
            placement="top-start"
            title="单表字段信息配置"
            width="300"
            trigger="hover"
            content="本页面用于配置单表字段信息页面，新增字段信息前，请先选择搜索框中的表名，不选择需要在新增表单中每个字段逐一选择！请阅读新增表单中，每个字段后的注意事项！！！需要严格遵守注意事项所写内容！！！"
          >
            <i
              slot="reference"
              class="el-icon-info icon-style"
            />
          </el-popover>
        </el-form-item>
      </el-row>
    </el-form>
  </el-row>
</template>

<script lang="ts">
import { Prop, Component, Vue, Emit } from 'vue-property-decorator'

/**
 * @author lhc
 * @date 2020-06-09
 * @description 搜索表单组件，不带key值
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

  // 重制搜索表单
  @Emit()
  // eslint-disable-next-line @typescript-eslint/no-empty-function
  resetSearchForm() {}

  // 表名
  @Prop(Array) tableSelectList!: any[];

  javaFieldList: any[] = [
    {
      value: 'Integer',
      label: '数字'
    },
    {
      value: 'String',
      label: '字符串'
    },
    {
      value: 'datetime',
      label: '日期+时间'
    },
    {
      value: 'date',
      label: '日期'
    }
  ];

  webTypeList: any[] = [
    {
      value: 'el-input',
      label: '输入框'
    },
    {
      value: 'password',
      label: '密码框'
    },
    {
      value: 'el-select',
      label: '下拉框'
    },
    {
      value: 'el-date-picker',
      label: '日期框'
    },
    {
      value: 'textArea',
      label: '大文本'
    }
  ];
}
</script>

<style scoped>
.icon-style {
  font-size: 20px;
  color: #8c9394;
}
</style>
