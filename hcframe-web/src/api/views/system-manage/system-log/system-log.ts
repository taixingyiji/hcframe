import { Component } from 'vue-property-decorator'
import { mixins } from 'vue-class-component'

import Common from '@/api/mixins/Common'
import searchCo from '@/components/CommonCo/searchCo.vue'
import formCo from '@/components/CommonCo/formCo.vue'
import tableHeadCo from '@/components/CommonCo/tableHeadCo.vue'
import Utils from '@/common/Utils'
import LogApi from '@/api/api/app/LogApi'

@Component({
  name: 'systemLog',
  components: {
    searchCo,
    formCo,
    tableHeadCo
  }
})
export default class extends mixins(Common) {
  // 表格中 '操作'列的宽度
  private operationWidth = 200

  private logApi: LogApi = new LogApi();

  async created() {
    // 设置默认排序
    this.sort = 'date_time'
    this.order = 'DESC'
    this.tableHead = [
      { fieldName: 'username', nameCn: '用户名', isHead: 1 },
      { fieldName: 'dateTime', nameCn: '访问时间', isHead: 1 },
      { fieldName: 'ip', nameCn: 'IP', isHead: 1 },
      { fieldName: 'url', nameCn: 'URL', isHead: 1 },
      { fieldName: 'content', nameCn: '内容', isHead: 1 }
    ]
    // 获取列表信息
    this.getLogList()
    // 设置默认校验构造
    // this.formRules = {
    //   PASSWORD: [{ required: true, message: '密码不能为空', trigger: 'blur' }],
    //   USERNAME: [{ required: true, message: '邮箱不能为空', trigger: 'blur' },
    //     { type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change'] }]
    // }
    // 设置默认禁用字段
    // this.disableField = ['CREATE_TIME', 'UPDATE_TIME']
  }

  getLogList() {
    const param = {
      pageNum: this.currentPage,
      pageSize: this.pageSize,
      sort: this.sort,
      order: this.order
    }
    const params = Object.assign(Utils.exceptNull(this.searchForm), param)
    this.logApi.list(params).then((ref: Base.IAxiosResponse<any>) => {
      const list: Array<any> = ref.data.data.list
      list.forEach(value => {
        value.username = value.ftUser.username
      })
      this.tableList = list
      this.total = ref.data.data.total
    })
  }

  // 每页显示条目监听事件
  sizeChange(val: number) {
    this.pageSize = val
    this.getLogList()
  }

  // 翻页监听事件
  currentChange(val: number) {
    this.currentPage = val
    this.getLogList()
  }

  sortChangeFunction(val: any) {
    this.SortChange = val
    this.sort = Utils.changeJavaTypeToMysql(this.SortChange.prop)
    if (this.SortChange.order === 'descending') {
      this.order = 'DESC'
    } else {
      this.order = 'ASC'
    }
    this.getLogList()
  }
}
