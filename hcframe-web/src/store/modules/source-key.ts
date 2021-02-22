import { VuexModule, Module, Action, Mutation, getModule } from 'vuex-module-decorators'
import { getSourceKey, removeSourceKey, setSourceKey } from '@/utils/cookies'
import store from '@/store'
import DatasourceApi from '@/api/api/datasource/DatasourceApi'
import router from '@/router'
import { Message } from 'element-ui'

export interface ISourceKeyState {
  sourceToken: string
}

@Module({ dynamic: true, store, name: 'sourceKey' })
class SourceKey extends VuexModule implements ISourceKeyState {
  public sourceToken = getSourceKey() || ''
  public datasourceApi: DatasourceApi = new DatasourceApi();

  @Mutation
  private SET_SOURCE_TOKEN(token: string) {
    this.sourceToken = token
  }

  @Action
  public login(token: string) {
    const param = { token: token }
    this.datasourceApi.token(param).then(ref => {
      if (ref.data.code === 0) {
        this.SET_SOURCE_TOKEN(token)
        setSourceKey(token)
        router.push('/dataInit')
      } else {
        Message.warning(ref.data.msg || '数据不正确')
      }
    })
  }

  @Action
  public clearToken() {
    this.SET_SOURCE_TOKEN('')
    removeSourceKey()
  }

  @Action
  public ResetToken() {
    removeSourceKey()
    this.SET_SOURCE_TOKEN('')
  }
}

export const SourceKeyModule = getModule(SourceKey)
