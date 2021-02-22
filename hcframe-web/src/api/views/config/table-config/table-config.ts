import { Component } from 'vue-property-decorator'
import { mixins } from 'vue-class-component'

import Common from '@/api/mixins/Common'
import searchCo from '@/views/config/table-config/components/searchCo.vue'
import formCo from '@/views/config/table-config/components/formCo.vue'
import tableHeadCo from '@/components/CommonCo/tableHeadCo.vue'
import Utils from '@/common/Utils'
import ConfigApi from '@/api/api/tablg-config/ConfigApi'
import { Message } from 'element-ui'

@Component({
  name: 'tableConfig',
  components: {
    searchCo,
    formCo,
    tableHeadCo
  }
})
export default class extends mixins(Common) {
  // 表格中 '操作'列的宽度
  private operationWidth = 200

  private configApi: ConfigApi = new ConfigApi();

  created() {
    this.primaryKey = 'tableId'
    // 设置默认排序
    this.tableHead = [
      { fieldName: 'tableName', nameCn: '表名', isHead: 1 },
      { fieldName: 'tableAlias', nameCn: '表别名', isHead: 1 },
      { fieldName: 'tablePk', nameCn: '主键', isHead: 1 },
      { fieldName: 'tableContent', nameCn: '表描述', isHead: 1 }
    ]
    // 获取列表信息
    this.getTableList()
    // 设置默认校验构造
    this.formRules = {
      tableName: [{ required: true, message: '表名不能为空', trigger: 'blur' }],
      tableAlias: [{ required: true, message: '表别名不能为空', trigger: 'blur' }],
      tablePk: [{ required: true, message: '主键不能为空', trigger: 'blur' }],
      tableContent: [{ required: true, message: '表描述不能为空', trigger: 'blur' }]
    }
  }

  // 获取table表格
  getTableList() {
    this.loading = true
    const param = {
      pageNum: this.currentPage,
      pageSize: this.pageSize,
      sort: this.sort,
      order: this.order
    }
    const params = Object.assign(Utils.exceptNull(this.searchForm), param)
    this.configApi.list(ConfigApi.TABLE, params).then((ref: Base.IAxiosResponse<any>) => {
      this.tableList = ref.data.data.list
      this.total = ref.data.data.total
      this.loading = false
    })
  }

  // 每页显示条目监听事件
  sizeChange(val: number) {
    this.pageSize = val
    this.getTableList()
  }

  // 翻页监听事件
  currentChange(val: number) {
    this.currentPage = val
    this.getTableList()
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

  // 跳转到字段页面
  routeToField(row: any) {
    this.$router.push({ path: '/config/datainfo/fieldConfig', query: { id: row[this.primaryKey] } })
  }

  // 是否显示跳转按钮
  disableFieldButton(): boolean {
    return true
  }

  tableInfoSubmit(value: any) {
    const { val, type } = value
    this.$nextTick(() => {
      // 获取虚拟dom
      const ref: any = this.$refs.formCo
      ref.$refs[val].validate((valid: boolean) => {
        const form: any = Object.assign({}, this.formData)
        if (valid) {
          if (type === 1) {
            this.configApi.add(ConfigApi.TABLE, form).then(({ data }) => {
              if (data.code === 0) {
                this.$message.success('新增成功')
                this.getTableList()
                this.cancelForm()
              } else {
                this.$message.error(data.msg)
              }
            })
          } else {
            this.configApi.update(ConfigApi.TABLE, form).then(({ data }) => {
              if (data.code === 0) {
                this.$message.success('编辑成功')
                this.getTableList()
                this.cancelForm()
              } else {
                this.$message.error(data.msg)
              }
            })
          }
        }
      })
    })
  }

  // 批量删除
  deleteTableInfo() {
    if (this.selectIds.length === 0) {
      Message.warning('请选择要删除的资料')
    } else {
      this.$confirm('是否确定删除选中记录?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.configApi.delete(ConfigApi.TABLE, this.selectIds.toString()).then(ref => {
          if (ref.data.code === 0) {
            Message.success('删除成功')
            this.acquireTable()
          } else {
            Message.error('删除失败')
          }
        })
      }).catch(() => {
        Message.info('取消删除')
      })
    }
  }

  // 重制搜索
  resetTableSearch() {
    this.searchForm = {}
    this.getTableList()
  }

  // 查询表格
  queryTables() {
    this.currentPage = 1
    this.getTableList()
  }
}
