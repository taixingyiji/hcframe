<template>
  <el-row>
    <el-form
      :inline="true"
      class="user-query-option"
      :model="searchData"
    >
      <el-form-item
        label="名称:"
        prop="sysDescription"
      >
        <el-input
          v-model="searchData.sysDescription"
          size="small"
          placeholder="请输入名称"
          style="width:80%"
        />
      </el-form-item>
      <el-form-item
        label="KEY:"
        prop="commonAlias"
      >
        <el-input
          v-model="searchData.commonAlias"
          size="small"
          placeholder="请输入KEY"
          style="width:80%"
        />
      </el-form-item>
      <el-form-item
        label="数据库类型:"
        prop="commonType"
      >
        <el-select
          v-model="searchData.commonType"
          name="commonType"
          placeholder="请选择数据库类型"
          autocomplete="on"
          style="width:80%"
        >
          <el-option
            v-for="item in datasourceType"
            :key="item.typeKey"
            :label="item.typeKey"
            :value="item.typeKey"
          />
        </el-select>
      </el-form-item>
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
          title="数据库信息配置"
          width="300"
          trigger="hover"
          content="本页面用于配置系统数据库信息，请按照表单说明进行数据库配置！"
        >
          <i
            slot="reference"
            class="el-icon-info icon-style"
          />
        </el-popover>
      </el-form-item>
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
export default class SearchCo extends Vue {
  // 时间传给后台默认格式化
  @Prop({ type: String, default: 'timestamp' }) timeFormat!: string;
  // 表单数据  可为空
  @Prop(Object) searchData!: object;
  // 展示关闭表单
  @Prop(Boolean) formVisible!: boolean;
  // 表单key值
  @Prop(Array) formKey!: Array<object>;

  @Prop(Array) datasourceType!: Array<any>;
  // 提交表单
  @Emit()
  // eslint-disable-next-line @typescript-eslint/no-empty-function
  searchSubmit() {}

  // 重制搜索表单
  @Emit()
  // eslint-disable-next-line @typescript-eslint/no-empty-function
  resetSearchForm() {}
}
</script>

<style scoped>
  .icon-style{
    font-size:20px;
    color: #8c9394;
  }

  .el-form .el-form-item:last-child{
    margin-top: 20px;
  }
</style>
