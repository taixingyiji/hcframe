import BaseAxios from '@/common/http'

/**
 * @author lhc
 * @description 日志查询
 * @date 2020-05-29
 */
class LogApi extends BaseAxios<any, any> {
  /**
   *  获取日志列表
   * @param param 传输的数据
   */
  public list(param: any): Promise<Base.IAxiosResponse<any>> {
    return this.axios.get('/log/list', param)
  }
}

export default LogApi
