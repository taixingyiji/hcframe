<template>
  <div class="dashboard-editor-container">
    菜单管理
  </div>
</template>

<script lang="ts" src="../../../api/views/system-manage/function-auth/index.ts"></script>

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
