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
            :datasource-type="datasourceType"
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
            @handle-sort-change="sortChangeFunction"
            @handle-selection-change="handleSelectionChange"
          >
            <template
              v-slot="scope"
              style="text-align: center"
            >
              <el-button
                type="primary"
                size="mini"
                plain
                @click="showEdit(scope.item.row)"
              >
                编辑
              </el-button>
              <el-button
                type="warning"
                size="mini"
                plain
                @click="enabled(scope.item.row)"
              >
                启用/禁用
              </el-button>
              <el-button
                type="info"
                size="mini"
                plain
                @click="setDefault(scope.item.row)"
              >
                设为默认
              </el-button>
              <el-button
                type="danger"
                size="mini"
                plain
                @click="deleteDataInfo(scope.item.row)"
              >
                删除
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
    <form-co
      ref="formCo"
      :form-title="dialogTitle"
      :form-visible="dialogCreateFormVisible"
      :form-sub-botton="'提交'"
      :form-data="formData"
      :form-rules="rules"
      :disable-field="disableField"
      :form-type="formType"
      :test-loading="testLoading"
      :submit-loading="submitLoading"
      @form-submit="tableInfoSubmit"
      @cancel-form="cancelForm"
      @before-cancel-form="cancelForm"
      @test-submit="testSubmit"
    />
  </div>
</template>
<script lang="ts" src="../../../api/views/config/datasource/datasourceInfo.ts"></script>
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
