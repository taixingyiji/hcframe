import { Component } from 'vue-property-decorator'
import { mixins } from 'vue-class-component'

import Common from '@/api/mixins/Common'
import searchCo from '@/views/config/datasource/components/searchCo.vue'
import formCo from '@/views/config/datasource/components/formCo.vue'
import tableHeadCo from '@/views/config/table-config/components/tableHeadCo.vue'
import Utils from '@/common/Utils'
import { Message } from 'element-ui'
import Datasource from '@/api/mixins/Datasource'
import { ElForm } from 'element-ui/types/form'
import StringUtils from '@/common/StringUtils'

@Component({
  name: 'datasourceInfo',
  components: {
    searchCo,
    formCo,
    tableHeadCo
  }
})
export default class extends mixins(Common, Datasource) {
  // 表格中 '操作'列的宽度
  private operationWidth = 360
  private editField: any[] = ['url', 'username', 'password', 'schemaName'];
  private submitLoading = false;

  created() {
    this.primaryKey = 'tableId'
    // 设置默认排序
    this.tableHead = [
      { fieldName: 'sysDescription', nameCn: '名称', isHead: 1 },
      { fieldName: 'commonAlias', nameCn: 'KEY', isHead: 1 },
      { fieldName: 'commonType', nameCn: '数据库类型', isHead: 1 },
      { fieldName: 'schemaName', nameCn: '库名', isHead: 1 },
      { fieldName: 'isDefault', nameCn: '是否为默认', isHead: 1 },
      { fieldName: 'sysEnabled', nameCn: '启用/禁用', isHead: 1 }
    ]
    // 获取列表信息
    this.getTableList()
    this.getTypeList()
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
    const { data } = await this.datasourceApi.list(params)
    this.tableList = data.data.list
    this.total = data.data.total
    this.loading = false
  }

  testSubmit(value: any) {
    const { val, type } = value
    this.$nextTick(async() => {
      this.testLoading = true
      // 获取虚拟dom
      const ref: any = this.$refs.formCo
      const form: any = Object.assign({}, this.formData)
      if (type === 1) {
        ref.$refs[val].validate(async(valid: boolean) => {
          this.testLoading = true
          if (valid) {
            if (type === 1) {
              for (const i of this.datasourceType) {
                if (form.commonType === i.typeKey) {
                  form.driverClassName = i.driver
                }
              }
              await this.testDatasource(form)
              this.testLoading = false
            }
          }
        })
      } else {
        let str = '';
        (ref.$refs[val] as ElForm).validateField(this.editField, (valid) => {
          if (valid) {
            str += valid + ','
          }
        })
        if (StringUtils.isNotEmpty(str.trim())) {
          this.$message.error(str)
        } else {
          await this.testDatasource(form)
        }
      }
      this.testLoading = false
    })
  }

  async enabled(row: any) {
    if (row.isDefault === 1) {
      this.$message.error('不能禁用默认数据库！')
    } else {
      let status = 1
      if (row.sysEnabled === 1) {
        status = 0
      }
      const param = {
        status: status
      }
      await this.datasourceApi.enabled(param, row.datasourceId)
      this.getTableList()
    }
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

  async deleteDataInfo(row: any) {
    if (row.isDefault === 1) {
      this.$message.error('不能删除默认数据库！')
    } else {
      this.$confirm('是否确定删除选中记录?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async() => {
        const { data } = await this.datasourceApi.delete(row.datasourceId)
        if (data.code === 0) {
          Message.success('删除成功')
          this.getTableList()
        } else {
          Message.error('删除失败')
        }
      }).catch(() => {
        Message.info('取消删除')
      })
    }
  }

  tableInfoSubmit(value: any) {
    const { val, type } = value
    this.$nextTick(async() => {
      this.submitLoading = true
      // 获取虚拟dom
      const ref: any = this.$refs.formCo
      const form: any = Object.assign({}, this.formData)
      if (type === 1) {
        await ref.$refs[val].validate(async(valid: boolean) => {
          this.submitLoading = true
          if (valid) {
            const data = await this.add(form, 0)
            if (data.code === 0) {
              this.$message.success('新增成功')
              await this.getTableList()
              this.cancelForm()
            } else {
              this.$message.error(data.msg)
            }
            this.submitLoading = false
          }
        })
      } else {
        let str = '';
        (ref.$refs[val] as ElForm).validateField(this.editField, (valid) => {
          if (valid) {
            str += valid + ','
          }
        })
        if (StringUtils.isNotEmpty(str.trim())) {
          this.$message.error(str)
        } else {
          const { data } = await this.datasourceApi.update(form)
          if (data.code === 0) {
            this.$message.success('修改成功')
            await this.getTableList()
            this.cancelForm()
          } else {
            this.$message.error('修改失败')
          }
        }
      }
      this.submitLoading = false
    })
  }

  // 设置默认数据库
  async setDefault(val: any) {
    const { data } = await this.datasourceApi.default(val.datasourceId)
    if (data.code !== 0) {
      this.$message.error('设置失败')
    } else {
      this.getTableList()
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
