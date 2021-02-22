import { Component, Vue } from 'vue-property-decorator'
import StringUtils from '@/common/StringUtils'
import DatasourceApi from '@/api/api/datasource/DatasourceApi'
import DataTypeApi from '@/api/api/datasource/DataTypeApi'
import { Form as ElForm } from 'element-ui/types/element-ui'
import { Input, Message } from 'element-ui'

@Component({
  name: 'Datasource'
})
export default class Datasource extends Vue {
  public datasourceType: any[] = [];
  public datasourceApi: DatasourceApi = new DatasourceApi();
  public dataTypeApi: DataTypeApi = new DataTypeApi();
  public passwordType = 'password';
  public capsTooltip = false;
  public loading = false;
  public showDialog = false;
  public testLoading = false;
  public disabled = true;
  public datasourceForm: any = {};
  public validateName = (rule: any, value: string, callback: Function) => {
    if (StringUtils.isEmpty(value)) {
      callback(new Error('数据库名称不能为空！'))
    }
    this.datasourceApi.unique({ name: value }).then(ref => {
      if (ref.data.code === 0) {
        callback()
      } else {
        callback(new Error(ref.data.msg))
      }
    })
  };

  public validateKey = (rule: any, value: string, callback: Function) => {
    if (StringUtils.isEmpty(value)) {
      callback(new Error('数据库KEY不能为空！'))
    }
    this.datasourceApi.unique({ key: value }).then(ref => {
      if (ref.data.code === 0) {
        callback()
      } else {
        callback(new Error(ref.data.msg))
      }
    })
  };

  public rules = {
    commonAlias: [
      { required: true, message: '请填写KEY', trigger: 'change' },
      { validator: this.validateKey, trigger: 'blur' }, {
        pattern: /^[A-Za-z_][A-Za-z0-9_]*$/,
        message: '以_和字母开头，不支持中文和特殊字符'
      }],
    sysDescription: [
      { required: true, message: '请填写数据库名称', trigger: 'change' },
      { validator: this.validateName, trigger: 'blur' }],
    commonType: [
      { required: true, message: '请选择数据库类型', trigger: 'change' }
    ],
    url: [
      { required: true, message: '请填写数据库url', trigger: 'change' }
    ],
    password: [
      { required: true, message: '请填写数据库密码', trigger: 'change' }
    ],
    username: [
      { required: true, message: '请填写数据库账号', trigger: 'change' }
    ],
    schemaName: [
      { required: true, message: '请填写Schema名称', trigger: 'change' }
    ]
  };

  public getTypeList() {
    this.dataTypeApi.getAll('').then(ref => {
      if (ref.data.code === 0) {
        this.datasourceType = ref.data.data
      }
    })
  }

  public handleTest() {
    this.testLoading = true;
    (this.$refs.initData as ElForm).validate(async(valid: boolean) => {
      if (valid) {
        for (const i of this.datasourceType) {
          if (this.datasourceForm.commonType === i.typeKey) {
            this.datasourceForm.driverClassName = i.driver
          }
        }
        this.datasourceForm.sysEnabled = 1
        this.datasourceForm.isDefault = 1
        await this.testDatasource(this.datasourceForm)
        this.testLoading = false
      }
    })
  }

  public async testDatasource(form: any) {
    const { data } = await this.datasourceApi.test(form)
    if (data.code === 0) {
      Message({
        message: '数据库连接成功',
        type: 'success',
        duration: 5 * 1000
      })
      this.disabled = false
    } else {
      Message({
        message: data.msg || 'Error',
        type: 'warning',
        duration: 5 * 1000
      })
    }
    return data.code
  }

  public handleCommit(val: number) {
    (this.$refs.initData as ElForm).validate(async(valid: boolean) => {
      if (valid) {
        const data = await this.add(this.datasourceForm, val)
        if (data.code === 0) {
          await this.$router.push({
            path: '/login'
          })
        } else {
          Message({
            message: data.msg || 'Error',
            type: 'warning',
            duration: 5 * 1000
          })
        }
      }
    })
  }

  public async add(form: any, val: any) {
    for (const i of this.datasourceType) {
      if (form.commonType === i.typeKey) {
        form.driverClassName = i.driver
      }
    }
    form.sysEnabled = 1
    form.isDefault = val
    const { data } = await this.datasourceApi.addInfo(form)
    return data
  }

  public checkCapslock(e: KeyboardEvent) {
    const { key } = e
    this.capsTooltip =
      key !== null && key.length === 1 && key >= 'A' && key <= 'Z'
  }

  public showPwd() {
    if (this.passwordType === 'password') {
      this.passwordType = ''
    } else {
      this.passwordType = 'password'
    }
    this.$nextTick(() => {
      (this.$refs.password as Input).focus()
    })
  }
}
