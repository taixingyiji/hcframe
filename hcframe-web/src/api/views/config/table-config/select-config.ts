import { Component } from 'vue-property-decorator'
import { mixins } from 'vue-class-component'

import Common from '@/api/mixins/Common'
import searchCo from '@/views/config/table-config/components/searchCo.vue'
import formCo from '@/views/config/table-config/components/selectFormCo.vue'
import tableHeadCo from '@/components/CommonCo/tableHeadCo.vue'
import Utils from '@/common/Utils'
import ConfigApi from '@/api/api/tablg-config/ConfigApi'
import { Message } from 'element-ui'

@Component({
  name: 'selectConfig',
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
  tableName: any = '';
  fieldName: any = '';

  created() {
    this.primaryKey = 'selectId'
    // 设置默认排序
    this.tableHead = [
      { fieldName: 'selectKey', nameCn: '展示字段', isHead: 1 },
      { fieldName: 'selectValue', nameCn: '选项值', isHead: 1 },
      { fieldName: 'selectType', nameCn: '选择类型', isHead: 1 }
    ]
    this.tableName = this.$route.query.tableName
    this.fieldName = this.$route.query.fieldName
    // 获取列表信息
    this.getTableList()
    // 设置默认校验构造
    this.formRules = {
      selectKey: [{ required: true, message: '展示字段不能为空！', trigger: 'blur' }],
      selectValue: [{ required: true, message: '选项值不能为空！', trigger: 'blur' }],
      selectType: [{ required: true, message: '选择类型不能为空！', trigger: 'blur' }]
    }
  }

  mounted(): void{

  }

  // 获取table表格
  getTableList() {
    this.loading = true
    const param = {
      fieldId: this.$route.query.fieldId
    }
    this.configApi.list(ConfigApi.SELECT, param).then((ref: Base.IAxiosResponse<any>) => {
      this.tableList = ref.data.data
      this.loading = false
    })
  }

  // 每页显示条目监听事件
  sizeChange(val: number) {
    this.pageSize = val
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
    this.$router.push({ path: '/commonConfig/dataConfig/fieldConfig', query: { id: row[this.primaryKey] } })
  }

  // 是否显示跳转按钮
  disableFieldButton(row: any): boolean {
    const tableName = row.tableName
    return !tableName.startsWith('BASE_')
  }

  tableInfoSubmit(value: any) {
    const { val, type } = value
    this.$nextTick(() => {
      // 获取虚拟dom
      const ref: any = this.$refs.formCo
      ref.$refs[val].validate((valid: boolean) => {
        const form: any = Object.assign({}, this.formData)
        form.fieldId = this.$route.query.fieldId
        if (valid) {
          if (type === 1) {
            this.configApi.add(ConfigApi.SELECT, form).then(({ data }) => {
              if (data.code === 0) {
                this.$message.success('新增成功')
                this.getTableList()
                this.cancelForm()
              } else {
                this.$message.error(data.msg)
              }
            })
          } else {
            this.configApi.update(ConfigApi.SELECT, form).then(({ data }) => {
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
        this.configApi.delete(ConfigApi.SELECT, this.selectIds.toString()).then(ref => {
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

  backToField() {
    const param: any = {}
    if (this.$route.query.tableId) {
      param.id = this.$route.query.tableId
    }
    this.$router.push({ path: '/config/datainfo/fieldConfig', query: param })
  }

  tableFormatter(row: any, column: any, cellValue: any): any {
    if (column.property === 'selectType') {
      if (cellValue === 1) {
        return '单选'
      } else {
        return cellValue
      }
    } else {
      return cellValue
    }
  }
}
