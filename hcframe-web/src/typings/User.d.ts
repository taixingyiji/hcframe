declare namespace Users {

  export interface IUser {
    userId: number
    userName: string
    userPassword: string
    userEnabled: number
    userinfoId: number
    createTime: Date
    modifyTime: Date
  }

  export interface IUserInfo {
    auth: Array<IAuthItem>
    user_id: number
    user_loginname: string
    user_name: string
  }

  export interface IAuthItem {
    role_id: number
    role_name: string
    role_name_cn: string
  }
}
