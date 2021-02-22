<template>
  <div class="dashboard-editor-container">
    <div
      style="background-color: #f6f6f6;padding-top: 10px"
    >
      <el-breadcrumb
        separator-class="el-icon-arrow-right"
        style="margin-top: 10px;margin-bottom: 10px;margin-left: 20px"
      >
        <el-breadcrumb-item>表：{{ tableName }}</el-breadcrumb-item>
        <el-breadcrumb-item>字段：{{ fieldName }}</el-breadcrumb-item>
      </el-breadcrumb>
      <el-form
        :inline="true"
        class="user-button-option"
      >
        <el-button
          type="primary"
          size="mini"
          @click="showAdd"
        >
          新增
        </el-button>
        <el-button
          type="primary"
          size="mini"
          @click="deleteTableInfo"
        >
          批量删除
        </el-button>
        <el-button
          type="primary"
          size="mini"
          @click="backToField"
        >
          返回字段配置页面
        </el-button>
        <el-popover
          style="margin-left: 10px"
          placement="top-start"
          title="下拉框配置"
          width="300"
          trigger="hover"
          content="本页面用于配置表单下拉选项及表格格式化数据，若基表字段有值，则本页面配置不生效！使用前请详细查看新增页面每个字段含义"
        >
          <i
            slot="reference"
            class="el-icon-info icon-style"
          />
        </el-popover>
      </el-form>
    </div>
    <table-head-co
      ref="tableCo"
      :check-box-visible="true"
      :formatter="tableFormatter"
      :form-list="tableList"
      :table-heads="tableHead"
      :loading="loading"
      :slot-visible="true"
      :width="operationWidth"
      @handle-sort-change="sortChangeFunction"
      @handle-selection-change="handleSelectionChange"
    >
      <template
        v-slot="scope"
        style="text-align: center"
      >
        <el-button
          size="mini"
          @click="showEdit(scope.item.row)"
        >
          编辑
        </el-button>
      </template>
    </table-head-co>
    <form-co
      ref="formCo"
      :form-title="dialogTitle"
      :form-visible="dialogCreateFormVisible"
      :form-sub-botton="'提交'"
      :form-data="formData"
      :form-rules="formRules"
      :disable-field="disableField"
      :form-type="formType"
      @form-submit="tableInfoSubmit"
      @cancel-form="cancelForm"
      @before-cancel-form="cancelForm"
    />
  </div>
</template>
<script lang="ts" src="../../../api/views/config/table-config/select-config.ts"></script>
<style lang="scss">
  .dashboard-editor-container {
    /*padding: 32px;*/
    background-color: #ffffff;
    position: relative;

    .github-corner {
      position: absolute;
      top: 0;
      border: 0;
      right: 0;
    }

    .chart-wrapper {
      background: #fff;
      padding: 16px 16px 0;
      margin-bottom: 32px;
    }
  }

  .chart-container {
    position: relative;
    width: 100%;
    height: calc(100vh - 200px);
  }

  .el-select-dropdown__item {
    font-size: 12px;
    height: 30px;
    line-height: 30px;
  }

  @media (max-width: 1024px) {
    .chart-wrapper {
      padding: 8px;
    }
  }

  .user-query-option {
    padding: 10px;
    background: #fff;
    text-align: center;

    .el-form-item {
      margin-bottom: 1px;
    }
  }

  .user-button-option {
    padding: 10px;
  }

  .pagStyle {
    background: #fff;
    text-align: center;
    padding: 10px;
  }

  .custom-tree-node {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: space-between;
    font-size: 14px;
    padding-right: 8px;
  }
  .icon-style {
    font-size: 20px;
    color: #8c9394;
  }
</style>
