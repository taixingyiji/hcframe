import BaseAxios from '@/common/http'

class UserApi extends BaseAxios<any, Users.IUser> {
  public getCurrentUser(): Promise<Base.IAxiosResponse<Users.IUser>> {
    return this.axios.post('/UserController.getCurrentUser.do', '')
  }

  // 注册
  public register(param: any): Promise<Base.IAxiosResponse<any>> {
    return this.axios.post('/ftUser/register', param)
  }

  // 登陆
  public Login(param: any): Promise<Base.IAxiosResponse<any>> {
    return this.axios.post('/ftUser/login', param)
  }

  // 登出
  public logout(): Promise<Base.IAxiosResponse<any>> {
    return this.axios.post('/ftUser/logout', '')
  }

  // 获取用户列表
  public list(param: any): Promise<Base.IAxiosResponse<any>> {
    return this.axios.post('/ftUser/list', param)
  }

  // 更新用户列表
  public update(param: any): Promise<Base.IAxiosResponse<any>> {
    return this.axios.post('/ftUser/update', param)
  }

  // 删除用户
  public delete(param: any): Promise<Base.IAxiosResponse<any>> {
    return this.axios.post('/ftUser/delete', param)
  }

  // 新增用户
  public add(param: any): Promise<Base.IAxiosResponse<any>> {
    return this.axios.post('/ftUser/add', param)
  }

  // 启用禁用
  public enabled(param: any): Promise<Base.IAxiosResponse<any>> {
    return this.axios.post('/ftUser/enabled', param)
  }

  // 根据用户id获取角色id
  public getRoleByUserId(param: any): Promise<Base.IAxiosResponse<any>> {
    return this.axios.get('/ftUser/getRoleByUserId', param)
  }

  // 给用户添加角色
  public addRole(param: any): Promise<Base.IAxiosResponse<any>> {
    return this.axios.post('/ftUser/addRole', param)
  }

  // 验证用户唯一性
  public unique(param: any): Promise<Base.IAxiosResponse<any>> {
    return this.axios.post('/ftUser/unique', param)
  }

  // 发送邮箱验证码
  public sendCode(param: any): Promise<Base.IAxiosResponse<any>> {
    return this.axios.post('/ftUser/sendCode', param)
  }

  // 验证邮箱验证码
  public validateEmail(param: any): Promise<Base.IAxiosResponse<any>> {
    return this.axios.post('/ftUser/validateEmail', param)
  }

  // 用户找回密码设置新密码
  public setNewPassword(param: any): Promise<Base.IAxiosResponse<any>> {
    return this.axios.post('/ftUser/setNewPassword', param)
  }

  // 首页获取用户数量
  public getCount(): Promise<Base.IAxiosResponse<any>> {
    return this.axios.post('/ftUser/getCount', null)
  }

  // 登陆用户获取个人信息
  public getUserInfo(): Promise<Base.IAxiosResponse<any>> {
    return this.axios.post('/ftUser/getUserInfo', null)
  }

  // 输入邀请码
  public addInvitedCode(param: any): Promise<Base.IAxiosResponse<any>> {
    return this.axios.post('/ftUser/addInvitedCode', param)
  }

  // 修改用户密码
  public changePassword(param: any): Promise<Base.IAxiosResponse<any>> {
    return this.axios.post('/ftUser/changePassword', param)
  }
}

export default UserApi
