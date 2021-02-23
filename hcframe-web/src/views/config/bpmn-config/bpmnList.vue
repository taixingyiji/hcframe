<template>
  <el-container>
    <el-main>
      <el-row>
        <el-col
          :span="22"
          :offset="1"
        >
          <el-card
            shadow="always"
            class="select-card"
          >
            <el-col :span="4">
              <el-popover
                style="padding-right: 10px;"
                placement="right"
                title="流程图信息配置"
                width="300"
                trigger="hover"
                content="本页面用于配置流程图信息页面，新增表流程图前请阅读新增表单中，每个字段后的注意事项！！！需要严格遵守注意事项所写内容！！！"
              >
                <i
                  slot="reference"
                  class="el-icon-info icon-style icon-style-middle"
                />
              </el-popover>
              <span
                style="line-height: 36px;font-weight: bold;color:#42b983;text-align: center"
              >新增工作流 |</span>
            </el-col>
            <el-col :span="20">
              <el-form
                ref="bpmnNameFrom"
                :model="bpmnNameFrom"
                :rules="bpmnNameFromRules"
                :inline="true"
              >
                <el-popover
                  placement="right"
                  title="流程图标识"
                  width="300"
                  trigger="hover"
                  content="流程图标识必须以_和字母开头，不支持中文和特殊字符，且标识名不可重复！"
                >
                  <i
                    slot="reference"
                    class="el-icon-info icon-style icon-style-small"
                  />
                </el-popover>
                <el-form-item
                  label="标识："
                  prop="key"
                  width="100%"
                >
                  <el-input
                    v-model="bpmnNameFrom.key"
                    placeholder="填写工作流唯一标识"
                    type="text"
                  />
                </el-form-item>

                <el-popover
                  placement="right"
                  title="流程图名称"
                  width="300"
                  trigger="hover"
                  content="流程图名称不可重复！"
                >
                  <i
                    slot="reference"
                    class="el-icon-info icon-style icon-style-small"
                  />
                </el-popover>
                <el-form-item
                  label="名称："
                  prop="name"
                >
                  <el-input
                    v-model="bpmnNameFrom.name"
                    type="text"
                    placeholder="填写工作流名称"
                  />
                </el-form-item>
                <el-form-item
                  label=""
                  style="margin-left: 50px;"
                >
                  <el-button
                    type="primary"
                    icon="el-icon-plus"
                    size="medium"
                    @click="handleUpdateData('bpmnNameFrom')"
                  >
                    新增
                  </el-button>
                </el-form-item>
              </el-form>
            </el-col>
          </el-card>
        </el-col>

        <el-col
          :span="22"
          :offset="1"
        >
          <el-card
            shadow="always"
            class="select-card"
          >
            <el-col :span="3">
              <span
                style="line-height: 36px;font-weight: bold;color:#42b983;text-align: center"
              >查询条件 |</span>
            </el-col>
            <el-col :span="21">
              <el-form
                ref="getListParams"
                :model="getListParams"
                label-width="80px"
                :inline="true"
              >
                <el-form-item label="最新版本">
                  <el-select
                    v-model="getListParams.isLatestVersion"
                    placeholder="是否为最新版本"
                  >
                    <el-option
                      label="否"
                      value="false"
                    />
                    <el-option
                      label="是"
                      value="true"
                    />
                  </el-select>
                </el-form-item>
                <el-form-item label="名称">
                  <el-input
                    v-model="getListParams.name"
                    placeholder="填写工作流名称"
                    type="text"
                  />
                </el-form-item>
                <el-form-item label="版本号">
                  <el-input
                    v-model="getListParams.version"
                    type="number"
                    placeholder="填写版本号"
                    :min="1"
                  />
                </el-form-item>
              </el-form>
            </el-col>
          </el-card>
        </el-col>

        <el-col
          :span="22"
          :offset="1"
        >
          <el-table
            :data="tableData"
            style="width: 100%;"
            :resizable="false"
            border
            :header-cell-style="{background: themeColor,color: '#fff',textAlign: 'center',fontWeight: 'bold'}"
            :cell-style="{textAlign: 'center'}"
          >
            <el-table-column
              type="selection"
              align="center"
            />
            <el-table-column
              type="index"
              label="序号"
              width="50"
            />
            <el-table-column
              prop="name"
              label="名称"
              width="180"
              :resizable="false"
              sortable
            />
            <el-table-column
              prop="key"
              label="标识"
              width="180"
              :resizable="false"
              sortable
            />
            <el-table-column
              prop="version"
              label="版本"
              width="90"
              :resizable="false"
              sortable
            />
            <el-table-column
              prop="deploymentId"
              label="开发ID"
              :resizable="false"
            />
            <el-table-column
              label="操作"
              width="220"
              :resizable="false"
            >
              <template slot-scope="scope">
                <el-button
                  size="mini"
                  @click="showBpmn(scope.row)"
                >
                  查看
                </el-button>
                <el-button
                  size="mini"
                  type="primary"
                  @click="handleEditBpmn(scope.row)"
                >
                  编辑
                </el-button>
                <el-button
                  size="mini"
                  type="danger"
                  @click="handleDeleteBpmn(scope.row)"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination
            style="text-align: center"
            :current-page="currentPage"
            :page-sizes="pageSizes"
            :page-size="pageSize"
            layout="total, sizes, prev, pager, next, jumper"
            :total="total"
            @size-change="handleSizePagChange"
            @current-change="handleCurrentPagChange"
          />
        </el-col>
      </el-row>

      <!--      删除提示框-->
      <el-dialog
        class="deleteDialog"
        title="提示信息"
        :visible.sync="dialogDeleteVisible"
        width="30%"
      >
        <el-row>
          <el-col
            :span="14"
            :offset="3"
          >
            <span class="deleteTip">删除全部关联数据</span>
          </el-col>
          <el-col :span="6">
            <el-button
              size="mini"
              type="danger"
              @click="deleteBpmn('true')"
            >
              确定
            </el-button>
          </el-col>
        </el-row>
        <hr style="border:none; border-bottom:1px solid #DCDFE6">
        <el-row>
          <el-col
            :span="14"
            :offset="3"
          >
            <span class="deleteTip">只删除工作流</span>
          </el-col>
          <el-col :span="6">
            <el-button
              size="mini"
              type="danger"
              @click="deleteBpmn('false')"
            >
              确定
            </el-button>
          </el-col>
        </el-row>
      </el-dialog>
    </el-main>
  </el-container>
</template>

<script lang="ts">
import { Component, Vue, Watch } from 'vue-property-decorator'
import { Message, Form } from 'element-ui'
import bpmnView from '@/components/Bpmn/bpmnView.vue'
import ActivitiApi from '@/api/api/activiti/ActivitiApi'
import CONFIG from '@/config'
import { SettingsModule } from '@/store/modules/settings'

@Component({
  name: 'bpmnList',
  components: {
    bpmnView
  }
})
export default class extends Vue {
  private ActivitiApi: ActivitiApi = new ActivitiApi(CONFIG.ACTIVTI_URL);

  // 获取工作流列表参数
  private getListParams: { [key: string]: any } = {
    isLatestVersion: 'true',
    order: undefined,
    pageNum: 1,
    pageSize: 10,
    key: undefined,
    name: undefined,
    sortField: undefined,
    version: undefined
  };

  // 保留初始化工作流关键参数,this.$option.data()数据获取在TS中有问题
  private defaultListParams: { [key: string]: any } = {
    isLatestVersion: 'true',
    name: undefined,
    version: undefined
  };

  // 验证新建bpmn，标识符和名字是否唯一
  private bpmnNameFrom: { [key: string]: any } = {
    key: undefined,
    name: undefined
  };

  private bpmnNameFromRules = {
    key: [
      { required: true, message: '请填写工作流唯一标识', trigger: 'change' },
      {
        pattern: /^[A-Za-z_][A-Za-z0-9_]*$/,
        message: '以_和字母开头，不支持中文和特殊字符'
      }
    ],
    name: [{ required: true, message: '请填写工作流名称', trigger: 'change' }]
  };

  // 删除工作流参数
  private deleteParams: { [key: string]: any } = {
    id: undefined,
    state: undefined
  };

  // 编辑工作流参数
  private editBpmnParams: { [key: string]: any } = {
    file: undefined,
    fileName: undefined,
    name: undefined,
    version: undefined
  };

  // 展示工作流参数
  private showBpmnParams: { [key: string]: any } = {
    deploymentId: undefined,
    resourceName: undefined
  };

  // 工作流表格数据
  private tableData: any = [];

  // 分页
  private total = 0;
  private currentPage = 1;
  public pageSizes: Array<number> = [10, 20, 30, 40];
  public pageSize = 10;

  private dialogDeleteVisible = false; // 删除模态框

  // 编辑文件
  private updateBpmnFlag = false; // false为新建，true为更新

  mounted() {
    this.$nextTick(() => {
      this.initList()
    })
  }

  @Watch('getListParams.isLatestVersion', { immediate: true })
  @Watch('getListParams.name', { immediate: true })
  @Watch('getListParams.version', { immediate: true })
  onGetListParamsChanged() {
    this.getListParams.pageNum = 1
    this.initList()
  }

  // 刷新，解决this.$router.push页面不刷新问题
  @Watch('$route', { immediate: true, deep: true })
  routerChanged() {
    this.initList()
  }

  // 初始化列表
  private async initList() {
    const { data } = await this.ActivitiApi.getList(this.getListParams)
    this.tableData = data.data.list
    this.total = data.data.total
  }

  // 删除工作流
  private async handleDeleteBpmn(row: any) {
    this.deleteParams.id = row.deploymentId
    this.dialogDeleteVisible = true
  }

  private async deleteBpmn(state: string) {
    this.deleteParams.state = state
    const { data } = await this.ActivitiApi.deleteProcess(this.deleteParams)
    this.dialogDeleteVisible = false
    if (data.code !== 0) {
      Message.error(data.msg)
    } else {
      // 重置默认值
      Object.assign(this.getListParams, this.defaultListParams)
      Message.success('删除成功！')
      await this.initList()
    }
  }

  // 验证新建工作流的标识符和名称唯一性
  private handleUpdateData(bpmnNameFrom: string) {
    this.updateBpmnFlag = false;
    (this.$refs[bpmnNameFrom] as Form).validate(async(valid) => {
      if (valid) {
        const { data } = await this.ActivitiApi.verifyProcess(
          this.bpmnNameFrom
        )
        if (data.code !== 0) {
          Message.error(data.msg)
        } else {
          const params = {
            bpmnNameFrom: this.bpmnNameFrom,
            updateBpmnFlag: this.updateBpmnFlag
          }
          // eslint-disable-next-line @typescript-eslint/ban-ts-ignore
          // @ts-ignore
          await this.$router.push({ name: 'BpmnCreate', params: params })
          localStorage.setItem('bpmnLocalstorage', JSON.stringify(params))
        }
      }
    })
  }

  // 仅展示工作流
  private async showBpmn(row: any) {
    this.showBpmnParams.deploymentId = row.deploymentId
    this.showBpmnParams.resourceName = row.resourceName
    const { data } = await this.ActivitiApi.showBpmn(this.showBpmnParams)
    // eslint-disable-next-line @typescript-eslint/ban-ts-ignore
    // @ts-ignore
    await this.$router.push({ name: 'OnlyViewBpmn', params: data })
    localStorage.setItem('showData', JSON.stringify(data))
  }

  // 点击编辑按钮
  private async handleEditBpmn(row: any) {
    this.updateBpmnFlag = true

    this.showBpmnParams.deploymentId = row.deploymentId
    this.showBpmnParams.resourceName = row.resourceName

    this.editBpmnParams.name = row.name
    this.editBpmnParams.version = row.version
    this.editBpmnParams.fileName = `${row.key}.bpmn`

    const { data } = await this.ActivitiApi.showBpmn(this.showBpmnParams)
    const params = {
      editBpmnParams: this.editBpmnParams,
      updateBpmnFlag: this.updateBpmnFlag,
      editBpmnFile: data
    }
    // eslint-disable-next-line @typescript-eslint/ban-ts-ignore
    // @ts-ignore
    await this.$router.push({ name: 'BpmnCreate', params: params })
    localStorage.setItem('bpmnLocalstorage', JSON.stringify(params))
  }

  // 处理分页
  handleSizePagChange(val: number) {
    this.pageSize = val
    this.getListParams.pageSize = val
    this.initList()
  }

  handleCurrentPagChange(val: number) {
    this.currentPage = val
    this.getListParams.pageNum = val
    this.initList()
  }

  // 获取主题颜色
  get themeColor() {
    return SettingsModule.theme
  }
}
</script>
<style lang="scss" scoped>
.add-action {
  position: fixed;
  bottom: 10px;
  right: 584px;
  display: flex;
}

.select-card {
  margin-bottom: 20px;
}

.deleteDialog {
  .el-row:first-child {
    margin-top: -20px;
    margin-bottom: 10px;
  }

  .deleteTip {
    line-height: 28px;
  }
}

.icon-style {
  color: #eb8c1f;
}

.icon-style-middle {
  font-size: 20px;
}

.icon-style-small {
  font-size: 16px;
}
</style>
