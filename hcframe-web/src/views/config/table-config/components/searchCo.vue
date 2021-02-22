<template>
  <el-row>
    <el-form
      :inline="true"
      class="user-query-option"
      :model="searchData"
    >
      <el-form-item
        label="表名:"
        prop="tableName"
      >
        <el-input
          v-model="searchData.tableName"
          size="small"
          placeholder="请输入表名"
          style="width:80%"
        />
      </el-form-item>
      <el-form-item
        label="表别名:"
        prop="tableAlias"
      >
        <el-input
          v-model="searchData.tableAlias"
          size="small"
          placeholder="请输入表别名"
          style="width:80%"
        />
      </el-form-item>
      <el-form-item
        label="主键:"
        prop="tablePk"
      >
        <el-input
          v-model="searchData.tablePk"
          size="small"
          placeholder="请输入主键"
          style="width:80%"
        />
      </el-form-item>
      <el-form-item
        label="表描述:"
        prop="tableContent"
      >
        <el-input
          v-model="searchData.tableContent"
          size="small"
          placeholder="请输入表描述"
          style="width:80%"
        />
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
          title="单表信息配置"
          width="300"
          trigger="hover"
          content="本页面用于配置单表信息页面，新增表信息前请阅读新增表单中，每个字段后的注意事项！！！需要严格遵守注意事项所写内容！！！"
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
