import { Component } from 'vue-property-decorator'
import { mixins } from 'vue-class-component'

import Common from '@/api/mixins/Common'
import searchCo from '@/views/config/table-config/components/searchFieldCo.vue'
import formCo from '@/views/config/table-config/components/fieldFormCo.vue'
import tableHeadCo from '@/views/config/table-config/components/tableHeadCo.vue'
import Utils from '@/common/Utils'
import ConfigApi from '@/api/api/tablg-config/ConfigApi'
import { Message } from 'element-ui'

@Component({
  name: 'fieldConfig',
  components: {
    searchCo,
    formCo,
    tableHeadCo
  }
})
export default class extends mixins(Common) {
  // 表格中 '操作'列的宽度
  private operationWidth = 200
  // 表格中对齐方式，center/left/right
  private cellAlign = 'center'

  private configApi: ConfigApi = new ConfigApi();
  tableSelectList: any[] = [];
  async created() {
    this.primaryKey = 'fieldId'
    // 设置默认排序
    this.tableHead = [
      { fieldName: 'tableId', nameCn: '表名', isHead: 1 },
      { fieldName: 'fieldName', nameCn: '字段名', isHead: 1, commonWidth: 120 },
      { fieldName: 'nameCn', nameCn: '字段中文', isHead: 1, commonWidth: 120 },
      { fieldName: 'javaField', nameCn: 'JAVA字段', isHead: 1, commonWidth: 120 },
      { fieldName: 'javaType', nameCn: 'JAVA类型', isHead: 1, commonWidth: 120 },
      { fieldName: 'webType', nameCn: '前端类型', isHead: 1, commonWidth: 120 },
      { fieldName: 'fkTable', nameCn: '基表', isHead: 1, commonWidth: 120 },
      { fieldName: 'isChange', nameCn: '是否可更改', isHead: 1, commonWidth: 120 },
      { fieldName: 'isHead', nameCn: '表格', isHead: 1, commonWidth: 80 },
      { fieldName: 'isSearch', nameCn: '搜索', isHead: 1, commonWidth: 80 },
      { fieldName: 'isDetails', nameCn: '详情页', isHead: 1, commonWidth: 100 },
      { fieldName: 'isForm', nameCn: '表单', isHead: 1, commonWidth: 80 },
      { fieldName: 'isBatch', nameCn: '批量更新', isHead: 1, commonWidth: 120 }
    ]
    await this.getTableSelectList()
    // 获取列表信息
    this.getTableList()
    // 设置默认校验构造
    this.formRules = {
      tableId: [{ required: true, message: '表名不能为空', trigger: 'blur' }],
      fieldName: [{ required: true, message: '字段名不能为空', trigger: 'blur' }],
      nameCn: [{ required: true, message: '字段中文不能为空', trigger: 'blur' }],
      javaField: [{ required: true, message: 'JAVA字段不能为空', trigger: 'blur' }],
      javaType: [{ required: true, message: 'JAVA类型不能为空', trigger: 'blur' }],
      webType: [{ required: true, message: '前端类型不能为空', trigger: 'blur' }],
      isChange: [{ required: true, message: '是否可更改不能为空', trigger: 'blur' }],
      isHead: [{ required: true, message: '表头不能为空', trigger: 'blur' }],
      isSearch: [{ required: true, message: '搜索不能为空', trigger: 'blur' }],
      isDetails: [{ required: true, message: '详情页不能为空', trigger: 'blur' }],
      isForm: [{ required: true, message: '表单不能为空', trigger: 'blur' }],
      isBatch: [{ required: true, message: '批量更新不能为空', trigger: 'blur' }]
    }
  }

  mounted(): void {
    if (this.$route.query.id) {
      this.searchForm.tableId = this.$route.query.id
    }
  }

  // 获取table表格
  getTableList() {
    this.loading = true
    const param: any = {
      pageNum: this.currentPage,
      pageSize: this.pageSize,
      sort: this.sort,
      order: this.order
    }
    if (this.$route.query.id) {
      param.tableId = this.$route.query.id
    }
    const params = Object.assign(Utils.exceptNull(this.searchForm), param)
    this.configApi.list(ConfigApi.FIELD, params).then((ref: Base.IAxiosResponse<any>) => {
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
  routeToField() {
    this.$router.push('/commonConfig/dataConfig/fieldConfig')
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
            this.configApi.add(ConfigApi.FIELD, form).then(({ data }) => {
              if (data.code === 0) {
                this.$message.success('新增成功')
                this.getTableList()
                this.cancelForm()
              } else {
                this.$message.error(data.msg)
              }
            })
          } else {
            this.configApi.update(ConfigApi.FIELD, form).then(({ data }) => {
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
        this.configApi.delete(ConfigApi.FIELD, this.selectIds.toString()).then(ref => {
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

  tableFormatter(row: any, column: any, cellValue: any): any {
    if (cellValue === 0) {
      return '<div>否</div>'
    } else if (cellValue === 1) {
      return '是'
    } else {
      return cellValue
    }
  }

  getTableSelectList() {
    this.configApi.getTableSelectList().then(({ data }) => {
      if (data.code === 0) {
        this.tableSelectList = data.data
      } else {
        Message.error('获取table下拉列表失败')
      }
    })
  }

  showAdd() {
    this.dialogTitle = '新增记录'
    this.formType = 1
    this.formData = {}
    if (this.searchForm.tableId) {
      this.formData.tableId = this.searchForm.tableId
    }
    this.dialogCreateFormVisible = true
  }

  backToTableConfig() {
    this.$router.push('/config/datainfo/tableConfig')
  }

  showSelect(row: any) {
    const param: any = {
      fieldId: row.fieldId,
      tableId: row.tableId,
      fieldName: row.fieldName
    }
    this.tableSelectList.forEach(value => {
      if (value.value === row.tableId) {
        param.tableName = value.label
      }
    })
    this.$router.push({ path: '/config/datainfo/selectConfig', query: param })
  }
}
