import BaseAxios from '@/common/http'

/**
 * @author lhc
 * @description 日志查询
 * @date 2020-05-29
 */
class DataTypeApi extends BaseAxios<any, any> {
  /**
   *  获取全部的数据源类型信息
   * @param param 传输的数据
   */
  public getAll(param: any): Promise<Base.IAxiosResponse<any>> {
    return this.axios.get('/datatype/all', param)
  }
}

export default DataTypeApi
