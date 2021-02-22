import { Component } from 'vue-property-decorator'
import { mixins } from 'vue-class-component'

import Common from '@/api/mixins/Common'
import searchKeyCo from '@/components/CommonCo/searchKeyCo.vue'
import formCo from '@/components/CommonCo/formCo.vue'
import batchUpdateForm from '@/components/CommonCo/batchUpdateForm.vue'
import tableHeadCo from '@/components/CommonCo/tableHeadCo.vue'
import editTableCo from '@/components/CommonCo/editTableCo.vue'
import StringUtils from '@/common/StringUtils'

@Component({
  name: 'commonExample',
  components: {
    searchKeyCo,
    formCo,
    tableHeadCo,
    batchUpdateForm,
    editTableCo
  }
})
export default class extends mixins(Common) {
  // 表格中 '操作'列的宽度
  private operationWidth = 150

  // 表格中对齐方式，center/left/right
  private cellAlign = 'center'

  async created() {
    // 设置默认排序
    this.sort = 'UPDATE_TIME'
    this.order = 'DESC'
    // 设置表别名
    this.tableAlias = 'user'
    // 获取表头及表单信息
    await this.acquireTableInfo()
    // 获取基表信息格式化下拉框及表格数据
    await this.getBaseTableInfo()
    // 获取列表信息
    this.acquireTable()
    // 设置默认校验构造
    this.formRules = {
      PASSWORD: [{ required: true, message: '密码不能为空', trigger: 'blur' }],
      USERNAME: [{ required: true, message: '用户名不能为空', trigger: 'blur' }]
    }
    // 设置默认禁用字段
    this.disableField = ['create_time', 'update_time']
  }

  // 编辑表格新增行
  addRow() {
    const obj: any = {
      index: this.editList.length,
      edit: true
    }
    this.editList.push(obj)
  }

  // 可编辑表格select
  editTableSelectionChange(val: any) {
    this.editTableSelectList = val
  }

  // 表格校验规则
  tableRules(val: any) {
    const { rows, rules, callback } = val
    let message = ''
    Object.getOwnPropertyNames(rules).forEach(function(key) {
      if (key !== '__ob__') {
        if (StringUtils.isEmpty(rows[key])) {
          message += rules[key][0].message
          message += ','
        }
      }
    })
    callback(message)
  }
}
