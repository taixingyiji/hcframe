<template>
  <div class="dashboard-editor-container">
    <el-row>
      <el-col
        :span="22"
        :offset="1"
      >
        <el-card
          shadow="always"
          class="select-card"
        >
          <search-key-co
            :form-key="tableHead"
            :search-data="searchForm"
            :time-format="'yyyy-MM-dd HH:mm:ss'"
            @search-submit="query"
            @reset-search-form="resetSearch"
          />
        </el-card>
      </el-col>
    </el-row>
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
            @click="showBatchAdd"
          >
            批量新增
          </el-button>
          <el-button
            type="primary"
            size="mini"
            @click="showBatchUpdate"
          >
            批量修改
          </el-button>
          <el-button
            type="primary"
            size="mini"
            @click="deleteData"
          >
            批量删除
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
          @handle-sort-change="handleSortChange"
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
        <el-pagination
          :current-page="currentPage"
          :page-sizes="pageSizes"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          class="pagStyle"
          @size-change="handleSizePagChange"
          @current-change="handleCurrentPagChange"
        />
      </el-card>
    </el-col>

    <form-co
      ref="formCo"
      :form-title="dialogTitle"
      :form-visible="dialogCreateFormVisible"
      :form-sub-botton="'提交'"
      :form-key="tableHead"
      :form-data="formData"
      :form-rules="formRules"
      :form-type="formType"
      :disable-field="disableField"
      @form-submit="formSubmit"
      @cancel-form="cancelForm"
      @before-cancel-form="cancelForm"
    />
    <batch-update-form
      ref="batchForm"
      :form-title="'批量更新'"
      :form-visible="batchVisible"
      :form-sub-botton="'提交'"
      :form-key="tableHead"
      :form-data="formData"
      :form-rules="formRules"
      :form-type="formType"
      :disable-field="disableField"
      @form-submit="formBatchSubmit"
      @cancel-form="cancelBatchForm"
      @before-cancel-form="cancelBatchForm"
    />
    <el-dialog
      ref="form"
      :title="'批量新增'"
      :visible.sync="batchSaveDialogVisible"
      :close-on-click-modal="false"
      :before-close="beforeSaveDialogCancel"
      width="80%"
    >
      <edit-table-co
        ref="editTableCo"
        :check-box-visible="true"
        :formatter="tableFormatter"
        :form-list="editList"
        :table-heads="tableHead"
        :slot-visible="true"
        :form-rules="formRules"
        @table-rule="tableRules"
        @handle-selection-change="editTableSelectionChange"
        @add-row="addRow"
      />
      <div
        slot="footer"
        class="dialog-footer"
      >
        <el-button @click="beforeSaveDialogCancel">
          取消
        </el-button>
        <el-button
          type="primary"
          :loading="batchCommitLoading"
          @click="batchSaveSubmit"
        >
          提交
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script lang="ts" src="../../api/views/commonExample/commonExample.ts"></script>

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
