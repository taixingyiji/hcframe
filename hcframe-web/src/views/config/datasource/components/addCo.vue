<template>
  <el-row>
    <el-form
      :inline="true"
      class="user-query-option"
      :model="searchData"
    >
      <el-form-item
        label="数据库:"
        prop="commonType"
      >
        <el-select
          v-model="searchData.commonType"
          name="commonType"
          placeholder="请选择数据库"
          autocomplete="on"
          style="width:80%"
        >
          <el-option
            v-for="item in datasourceType"
            :key="item.datasourceId"
            :label="item.sysDescription"
            :value="item.datasourceId"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button
          type="primary"
          size="small"
          @click="addSubmit"
        >
          新增
        </el-button>
        <el-popover
          style="margin-left: 10px"
          placement="top-start"
          title="数据库运行状态"
          width="300"
          trigger="hover"
          content="此页面显示的数据库为当前程序可用数据库及数据库状态，若数据库不在此页面显示，可进行数据库添加。"
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

  @Prop(Array) datasourceType!: Array<any>;
  // 提交表单
  @Emit()
  // eslint-disable-next-line @typescript-eslint/no-empty-function
  addSubmit() {}
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
