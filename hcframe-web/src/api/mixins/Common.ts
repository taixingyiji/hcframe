import { Component, Vue } from 'vue-property-decorator'
import TimeUtils from '@/common/TimeUtils'
import CommonApi from '@/api/api/CommonApi'
import { Message, MessageBox } from 'element-ui'
import StringUtils from '@/common/StringUtils'
import DataModule from '@/common/data/DataModule'

@Component({
  name: 'Common',
  components: {}
})
export default class Common extends Vue {
  // 表单禁用字段数组
  public disableField: Array<any> = []
  // 表单校验规则
  public formRules: any = {};
  // 数据库表主键
  public primaryKey = '';
  // 表名
  public tableAlias = '';
  // 表格加载动画
  public loading = false;
  // 初始化axios数据
  public commonApi: CommonApi = new CommonApi()
  // 搜索数据
  public searchForm: any = {};
  // 初始化新增，修改表单数据
  public formData: any = {};
  // 初始化表头数据
  public tableHead: any = [];
  public tableList: any = [];
  // 初始化表排序字段
  public SortChange: any;
  public sort: any = '';
  public order: any = '';
  // 选中数据的id数组
  public selectIds: Array<any> = []
  // 分页
  public currentPage = 1;
  public pageSizes: Array<number> = [10, 20, 30, 40];
  public pageSize = 10;
  public total = 0;
  public selectList = [];

  // 是否显示新增表单
  public dialogCreateFormVisible = false;
  // 表单标题
  public dialogTitle = '新增记录';
  public formType = 1;
  // 批量更新模态框
  public batchVisible = false
  // 批量新增模态框
  public batchSaveDialogVisible = false
  // 批量提交按钮loading效果
  public batchCommitLoading = false
  // 可编辑表格list
  public editList: any[] = [];
  // 可编辑表格复选框List
  public editTableSelectList: any[] = [];

  // 关闭批量新增模态框方法
  beforeSaveDialogCancel() {
    if (this.editList.length > 0) {
      MessageBox.confirm('关闭弹窗后所有表格数据都将遗失, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.batchSaveDialogVisible = false
      })
    } else {
      this.batchSaveDialogVisible = false
    }
  }

  // 展示批量新增模态框
  showBatchAdd() {
    this.editList = []
    this.batchSaveDialogVisible = true
  }

  // 查询表格
  query() {
    this.currentPage = 1
    this.acquireTable()
  }

  acquireTable() {
    const param: any = {
      pageNum: this.currentPage,
      pageSize: this.pageSize,
      sort: this.sort,
      order: this.order
    }
    const data: DataModule = DataModule.formatToModul(this.searchForm, this.tableHead)
    if (data.hasContent()) {
      param.data = data.jsons
    }
    const params = Object.assign({}, param)
    this.getTableList(params, this.tableAlias)
  }

  resetSearch() {
    this.searchForm = {}
    this.acquireTable()
  }

  // 获取数据list
  getTableList(param: any, typeName: any) {
    this.loading = true
    this.commonApi.list(param, typeName).then((ref: Base.IAxiosResponse<any>) => {
      if (ref.data.code === 0) {
        this.tableList = ref.data.data.list
        this.total = ref.data.data.total
      } else {
        Message.error(ref.data.msg)
      }
      this.loading = false
    })
  }

  // 获取表头及字段信息
  async acquireTableInfo() {
    this.loading = true
    await this.commonApi.getTableInfo(this.tableAlias).then(ref => {
      if (ref.data.code === 0) {
        this.tableHead = ref.data.data.fieldList
        this.primaryKey = ref.data.data.tablePk
      }
    })
  }

  // 获取基表信息用于格式化下拉框及表格数据
  async getBaseTableInfo() {
    const arr: string[] = []
    for (let i = 0; i < this.tableHead.length; i++) {
      if (StringUtils.isNotEmpty(this.tableHead[i].fkTable)) {
        arr.push(this.tableHead[i].fkTable)
      }
    }
    if (arr.length > 0) {
      await this.commonApi.getBaseTableInfo(arr.toString()).then(ref => {
        if (ref.data.code === 0) {
          const data = ref.data.data
          for (let i = 0; i < this.tableHead.length; i++) {
            if (StringUtils.isNotEmpty(this.tableHead[i].fkTable)) {
              const fkTable: string = this.tableHead[i].fkTable
              const list = data[fkTable]
              const selectList: any[] = []
              for (let k = 0; k < list.length; k++) {
                const param = {
                  selectKey: list[k].BASE_NAME,
                  selectValue: list[k].BASE_VALUE
                }
                selectList.push(param)
              }
              this.tableHead[i].selectList = selectList
            }
          }
        } else {
          console.log('获取基表信息出错')
        }
      })
    }
  }

  // 表格排序
  handleSortChange(val: any) {
    this.SortChange = val
    this.sort = this.SortChange.prop
    if (this.SortChange.order === 'descending') {
      this.order = 'DESC'
    } else {
      this.order = 'ASC'
    }
    this.acquireTable()
  }

  // 分页
  handleSizePagChange(val: number) {
    this.pageSize = val
    this.acquireTable()
  }

  handleCurrentPagChange(val: number) {
    this.currentPage = val
    this.acquireTable()
  }

  // 展示编辑弹窗接口
  showEdit(row: any) {
    this.dialogTitle = '编辑记录'
    this.formType = 2
    this.formData = {}
    this.formData = Object.assign({}, row)
    this.dialogCreateFormVisible = true
  }

  // 展示新增弹窗方法
  showAdd() {
    this.dialogTitle = '新增记录'
    this.formType = 1
    this.formData = {}
    this.dialogCreateFormVisible = true
  }

  // 展示批量修改弹窗方法
  showBatchUpdate() {
    if (this.selectIds.length === 0) {
      Message.warning('请选择需批量修改的资料')
    } else {
      this.formType = 3
      this.formData = {}
      this.batchVisible = true
    }
  }

  // 关闭弹窗方法
  cancelForm() {
    this.formData = {}
    this.$nextTick(() => {
      const ref: any = this.$refs.formCo
      ref.$refs.formCommon.resetFields()
    })
    this.dialogCreateFormVisible = false
  }

  // 关闭批量更新模态框方法
  cancelBatchForm() {
    this.formData = {}
    this.$nextTick(() => {
      const ref: any = this.$refs.batchForm
      ref.$refs.formCommon.resetFields()
    })
    this.batchVisible = false
  }

  // 表单提交事件
  formSubmit(value: any) {
    const { val, type } = value
    this.$nextTick(() => {
      // 获取虚拟dom
      const ref: any = this.$refs.formCo
      ref.$refs[val].validate((valid: boolean) => {
        const form: any = Object.assign({}, this.formData)
        // 校验表带校验规则
        if (valid) {
          if (type === 1) {
            this.commonApi.saveWithDate(form, this.tableAlias).then(ref => {
              if (ref.data.code === 0) {
                Message.success('新增成功')
                this.acquireTable()
                this.cancelForm()
              } else {
                Message.error('新增失败')
              }
            })
          } else if (type === 2) {
            delete form.password
            this.commonApi.updateWithDate(form, this.tableAlias).then(ref => {
              if (ref.data.code === 0) {
                Message.success('修改成功')
                this.acquireTable()
                this.cancelForm()
              } else {
                Message.error('修改失败')
              }
            })
          } else {

          }
        }
      })
    })
  }

  // 批量更新方法
  formBatchSubmit(value: any) {
    const { val } = value
    this.$nextTick(() => {
      const ref: any = this.$refs.batchForm
      ref.$refs[val].validate((valid: boolean) => {
        const form: any = Object.assign({}, this.formData)
        if (valid) {
          form[this.primaryKey] = this.selectIds.toString()
          this.commonApi.updateBatchWithDate(this.tableAlias, form).then(ref => {
            if (ref.data.code === 0) {
              Message.success('批量修改成功')
              this.acquireTable()
              this.cancelBatchForm()
            } else {
              Message.error('批量修改失败')
            }
          })
        }
      })
    })
  }

  // 表格复选框选中改变监听事件
  handleSelectionChange(val: any) {
    const list: Array<any> = []
    for (let i = 0; i < val.length; i++) {
      list[i] = val[i][this.primaryKey]
    }
    this.selectIds = list
    this.selectList = val
  }

  // 批量删除方法
  deleteData() {
    if (this.selectIds.length === 0) {
      Message.warning('请选择要删除的资料')
    } else {
      MessageBox.confirm('是否确定删除选中记录?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.commonApi.delete(this.selectIds.toString(), this.tableAlias).then(ref => {
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

  // 格式化表格数据
  tableFormatter(row: any, column: any, cellValue: any): any {
    let key: any = null
    this.tableHead.forEach((val: any) => {
      if (column.property === val.fieldName) {
        if (val.webType === 'el-select') {
          val.selectList.forEach((child: any) => {
            let value = child.selectValue
            if (val.javaType === 'Integer') {
              value = +value
            }
            if (cellValue === value) {
              key = child.selectKey
            }
          })
        }
        if (val.javaType === 'datetime') {
          if (StringUtils.isNotEmpty(cellValue)) {
            key = TimeUtils.dateFtt('yyyy-MM-dd hh:mm:ss', new Date(cellValue))
          }
        }
        if (val.javaType === 'date') {
          if (StringUtils.isNotEmpty(cellValue)) {
            key = TimeUtils.dateFtt('yyyy-MM-dd', new Date(cellValue))
          }
        }
      }
    })
    if (key !== null) {
      return key
    } else {
      return cellValue
    }
  }

  // 批量提交
  batchSaveSubmit() {
    if (this.editList.length > 0) {
      this.batchCommitLoading = true
      const list: any[] = []
      let isEdit = false
      this.editList.forEach(value => {
        if (value.edit) {
          isEdit = true
        } else {
          const param = Object.assign({}, value)
          delete param.index
          delete param.edit
          list.push(param)
        }
      })
      if (isEdit) {
        Message.warning('表格中还有未保存的数据！')
        this.batchCommitLoading = false
      } else {
        this.commonApi.saveBatchWithDate(this.tableAlias, JSON.stringify(list)).then(ref => {
          if (ref.data.code === 0) {
            Message.success('新增成功')
            this.batchCommitLoading = false
            this.acquireTable()
            this.batchSaveDialogVisible = false
          } else {
            Message.warning('新增失败')
          }
        })
      }
    } else {
      Message.warning('请添加记录')
    }
  }
}
