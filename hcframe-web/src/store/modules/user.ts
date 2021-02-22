import { VuexModule, Module, Action, Mutation, getModule } from 'vuex-module-decorators'
import { getToken, setToken, removeToken, removeSourceKey } from '@/utils/cookies'
import router, { resetRouter } from '@/router'
import { PermissionModule } from './permission'
import { TagsViewModule } from './tags-view'
import store from '@/store'
import UserApi from '@/api/api/app/UserApi'
import { Message } from 'element-ui'
import { SourceKeyModule } from '@/store/modules/source-key'
import AuthApi from '@/api/api/auth/AuthApi'

export interface IUserState {
  token: string
  name: string
  avatar: string
  introduction: string
  roles: string[]
  email: string
  menu: any[]
}

@Module({ dynamic: true, store, name: 'user' })
class User extends VuexModule implements IUserState {
  public token = getToken() || ''
  public name = ''
  public avatar = ''
  public introduction = ''
  public roles: string[] = []
  public email = ''
  public menu: any[] = []
  public userApi: UserApi = new UserApi()
  public authApi: AuthApi = new AuthApi()

  @Mutation
  private SET_MENU(menu: any[]) {
    this.menu = menu
  }

  @Mutation
  private SET_TOKEN(token: string) {
    this.token = token
  }

  @Mutation
  private SET_NAME(name: string) {
    this.name = name
  }

  @Mutation
  private SET_AVATAR(avatar: string) {
    this.avatar = avatar
  }

  @Mutation
  private SET_INTRODUCTION(introduction: string) {
    this.introduction = introduction
  }

  @Mutation
  private SET_ROLES(roles: string[]) {
    this.roles = roles
  }

  @Mutation
  private SET_EMAIL(email: string) {
    this.email = email
  }

  @Action
  public async Login(userInfo: { username: string, password: string}) {
    let { username, password } = userInfo
    username = username.trim()
    // const { data } = await login({ username, password })
    await this.userApi.Login({ username, password }).then((ref: Base.IAxiosResponse<any>) => {
      const data = ref.data
      if (data.code === 0) {
        setToken(data.data.token)
        this.SET_TOKEN(data.data.token)
        // let a: Array<string> = ['admin'];
        // this.SET_ROLES(a);
        // console.log('roles', this.roles);
        router.push('/')
      } else {
        Message({
          message: data.msg || 'Error',
          type: 'warning',
          duration: 5 * 1000
        })
      }
    })
  }

  @Action
  public ResetToken() {
    removeToken()
    this.SET_TOKEN('')
    this.SET_ROLES([])
  }

  @Action
  public async GetUserInfo() {
    // if (this.token === '') {
    //   throw Error('GetUserInfo: token is undefined!')
    // }
    // const { data } = await getUserInfo({ /* Your params here */ })
    // if (!data) {
    //   throw Error('Verification failed, please Login again.')
    // }
    // const { roles, name, avatar, introduction, email } = data.user
    // // roles must be a non-empty array
    // if (!roles || roles.length <= 0) {
    //   throw Error('GetUserInfo: roles must be a non-null array!')
    // }
    this.SET_ROLES(['admin'])
    this.SET_NAME('admin')
    this.SET_AVATAR('avatar')
    this.SET_INTRODUCTION('introduction')
    this.SET_EMAIL('ta@1.com')
  }

  @Action
  public async getMenu() {
    const { data } = await this.authApi.menu()
    if (data.code === 0) {
      this.SET_MENU(data.data)
    }
  }

  @Action
  public async ChangeRoles(role: string) {
    // Dynamically modify permissions
    const token = role + '-token'
    this.SET_TOKEN(token)
    setToken(token)
    await this.GetUserInfo()
    resetRouter()
    // Generate dynamic accessible routes based on roles
    PermissionModule.GenerateRoutes(this.menu)
    // Add generated routes
    router.addRoutes(PermissionModule.dynamicRoutes)
    // Reset visited views and cached views
    TagsViewModule.delAllViews()
  }

  @Action
  public async LogOut() {
    if (this.token === '') {
      throw Error('LogOut: token is undefined!')
    }
    await this.userApi.logout()
    removeToken()
    resetRouter()
    removeSourceKey()
    SourceKeyModule.clearToken()
    // Reset visited views and cached views
    TagsViewModule.delAllViews()
    this.SET_TOKEN('')
    this.SET_ROLES([])
  }
}

export const UserModule = getModule(User)
