import BaseAxios from '@/common/http'

/**
 * @author lhc
 * @description 日志查询
 * @date 2020-05-29
 */
class AuthApi extends BaseAxios<any, any> {
  /**
   *  获取日志列表
   * @param param 传输的数据
   */
  public menu(): Promise<Base.IAxiosResponse<any>> {
    return this.axios.get('/auth/function/menu', '')
  }
}

export default AuthApi
