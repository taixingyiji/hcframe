<template>
  <div class="dashboard-editor-container">
    <el-row style="margin-top: 10px">
      <el-col
        :span="22"
        :offset="1"
      >
        <el-card
          shadow="always"
          class="select-card"
        >
          <search-co
            :search-data="searchForm"
            :time-format="'yyyy-MM-dd HH:mm:ss'"
            :table-select-list="tableSelectList"
            @search-submit="queryTables"
            @reset-search-form="resetTableSearch"
          />
        </el-card>
      </el-col>
    </el-row>
    <el-row style="margin-top: 20px">
      <el-col
        :span="22"
        :offset="1"
      >
        <el-card
          shadow="always"
          class="select-card"
        >
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
              @click="backToTableConfig"
            >
              返回配置表
            </el-button>
          </el-form>

          <table-head-co
            ref="tableCo"
            :check-box-visible="true"
            :formatter="tableFormatter"
            :form-list="tableList"
            :table-heads="tableHead"
            :loading="loading"
            :slot-visible="true"
            :width="operationWidth"
            :cell-align="cellAlign"
            :table-select-list="tableSelectList"
            @handle-sort-change="sortChangeFunction"
            @handle-selection-change="handleSelectionChange"
          >
            <template v-slot="scope">
              <el-button
                size="mini"
                type="primary"
                plain
                @click="showEdit(scope.item.row)"
              >
                编辑
              </el-button>
              <el-button
                v-if="!scope.item.row.fkTable&&scope.item.row.webType==='el-select'"
                size="mini"
                type="success"
                plain
                @click="showSelect(scope.item.row)"
              >
                编辑下拉选项
              </el-button>
            </template>
          </table-head-co>
          <el-pagination
            :current-page="currentPage"
            :page-sizes="pageSizes"
            :page-size="pageSize"
            layout="total, sizes, prev, pager, next, jumper"
            :total="total"
            class="pagStyle"
            @size-change="sizeChange"
            @current-change="currentChange"
          />
        </el-card>
      </el-col>
    </el-row>
    <div style="background-color: #f6f6f6" />

    <form-co
      ref="formCo"
      :form-title="dialogTitle"
      :form-visible="dialogCreateFormVisible"
      :form-sub-botton="'提交'"
      :form-data="formData"
      :form-rules="formRules"
      :disable-field="disableField"
      :form-type="formType"
      :table-select-list="tableSelectList"
      @form-submit="tableInfoSubmit"
      @cancel-form="cancelForm"
      @before-cancel-form="cancelForm"
    />
  </div>
</template>
<script lang="ts" src="../../../api/views/config/table-config/field-config.ts"></script>
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
</style>
