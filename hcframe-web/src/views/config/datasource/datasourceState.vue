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
          <add-co
            :search-data="searchForm"
            :time-format="'yyyy-MM-dd HH:mm:ss'"
            :datasource-type="datasourceType"
            @add-submit="addSubmit"
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
              @click="getTableList"
            >
              全局测试
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
                @click="testConnect(scope.item.row)"
              >
                测试连接
              </el-button>
              <el-button
                type="danger"
                size="mini"
                plain
                @click="stop(scope.item.row)"
              >
                终止
              </el-button>
            </template>
          </table-head-co>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>
<script lang="ts" src="../../../api/views/config/datasource/datasourceState.ts"></script>
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
