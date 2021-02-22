import { Component } from 'vue-property-decorator'
import { mixins } from 'vue-class-component'

import Common from '@/api/mixins/Common'
import addCo from '@/views/config/datasource/components/addCo.vue'
import formCo from '@/views/config/datasource/components/formCo.vue'
import tableHeadCo from '@/views/config/datasource/components/tableHeadCo.vue'
import Utils from '@/common/Utils'
import Datasource from '@/api/mixins/Datasource'
import DatasourceStateApi from '@/api/api/datasource/DatasourceStateApi'

@Component({
  name: 'datasourceState',
  components: {
    addCo,
    formCo,
    tableHeadCo
  }
})
export default class extends mixins(Common, Datasource) {
  // 表格中 '操作'列的宽度
  private operationWidth = 200
  private datasourceStateApi: DatasourceStateApi = new DatasourceStateApi()

  created() {
    this.primaryKey = 'tableId'
    // 设置默认排序
    this.tableHead = [
      { fieldName: 'sysDescription', nameCn: '名称', isHead: 1 },
      { fieldName: 'commonAlias', nameCn: 'KEY', isHead: 1 },
      { fieldName: 'commonType', nameCn: '数据库类型', isHead: 1 },
      { fieldName: 'schemaName', nameCn: '库名', isHead: 1 },
      { fieldName: 'status', nameCn: '状态', isHead: 1 },
      { fieldName: 'isDefault', nameCn: '是否为默认', isHead: 1 }
    ]
    // 获取列表信息
    this.getTableList()
    this.getAllList()
  }

  async getAllList() {
    const { data } = await this.datasourceApi.all('')
    this.datasourceType = data.data
  }

  // 获取table表格
  async getTableList() {
    this.loading = true
    const param = {
      pageNum: this.currentPage,
      pageSize: this.pageSize,
      sort: this.sort,
      order: this.order
    }
    const params = Object.assign(Utils.exceptNull(this.searchForm), param)
    const { data } = await this.datasourceStateApi.list(params)
    this.tableList = data.data
    this.loading = false
  }

  async testConnect(row: any) {
    this.loading = true
    const obj = Object.assign({}, row)
    delete obj.updateTime
    delete obj.createTime
    const code = await this.testDatasource(obj)
    if (code !== 0) {
      row.status = 0
    } else {
      row.status = 1
    }
    this.loading = false
  }

  addSubmit() {
    const param = {
      id: this.searchForm.commonType
    }
    this.datasourceStateApi.add(param).then(ref => {
      if (ref.data.code !== 0) {
        this.$message.error('新增失败')
      } else {
        this.getTableList()
      }
    })
  }

  async stop(row: any) {
    if (row.isDefault === 1) {
      this.$message.error('不能终止默认数据库！')
    } else {
      const param = row.commonAlias
      await this.datasourceStateApi.delete(param)
      this.getTableList()
    }
  }

  // 排序监听事件
  sortChangeFunction(val: any) {
    this.SortChange = val
    this.sort = Utils.changeJavaTypeToMysql(this.SortChange.prop)
    if (this.SortChange.order === 'descending') {
      this.order = 'DESC'
    } else {
      this.order = 'ASC'
    }
    this.getTableList()
  }
}
