import BaseAxios from '@/common/http'

/**
 * @author lhc
 * @description 日志查询
 * @date 2020-05-29
 */
class DatasourceStateApi extends BaseAxios<any, any> {
  /**
   *  获取数据源是否配置
   * @param param 传输的数据
   */
  public list(param: any): Promise<Base.IAxiosResponse<any>> {
    return this.axios.get('/dataconfig/', param)
  }

  /**
   * 终止运行中的数据源
   * @param param
   */
  public delete(param: any): Promise<Base.IAxiosResponse<any>> {
    return this.axios.delete('/dataconfig/' + param, '')
  }

  /**
   * 新增运行中的数据源
   * @param param
   */
  public add(param: any): Promise<Base.IAxiosResponse<any>> {
    return this.axios.post('/dataconfig/add', param)
  }
}

export default DatasourceStateApi
