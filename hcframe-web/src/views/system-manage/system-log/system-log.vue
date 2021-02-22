<template>
  <div class="dashboard-editor-container">
    <!--    <search-co-->
    <!--      :form-key="tableHead"-->
    <!--      :search-data="searchForm"-->
    <!--      :time-format="'yyyy-MM-dd HH:mm:ss'"-->
    <!--      @search-submit="query"-->
    <!--      @reset-search-form="resetSearch"-->
    <!--    />-->
    <div
      style="background-color: #f6f6f6"
    />
    <table-head-co
      ref="tableCo"
      :check-box-visible="true"
      :formatter="tableFormatter"
      :form-list="tableList"
      :table-heads="tableHead"
      :loading="loading"
      :slot-visible="false"
      :width="operationWidth"
      @handle-sort-change="sortChangeFunction"
      @handle-selection-change="handleSelectionChange"
    />
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
  </div>
</template>

<script lang="ts" src="../../../api/views/system-manage/system-log/system-log.ts"></script>

<style lang="scss">
.dashboard-editor-container {
  /*padding: 32px;*/
  background-color: #ffffff;
  position: relative;

  .github-corner {
    position: absolute;
    top: 0px;
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
