import BaseAxios from '@/common/http'

/**
 * @author lhc
 * @description 日志查询
 * @date 2020-05-29
 */
class DatasourceApi extends BaseAxios<any, any> {
  /**
   *  获取数据源是否配置
   * @param param 传输的数据
   */
  public hasSource(param: any): Promise<Base.IAxiosResponse<any>> {
    return this.axios.get('/datasource/hasSource', param)
  }

  /**
   * 添加数据
   * @param param
   */
  public addInfo(param: any): Promise<Base.IAxiosResponse<any>> {
    return this.axios.post('/datasource', param)
  }

  /**
   * 判断数据源是否唯一
   * @param param
   */
  public unique(param: any): Promise<Base.IAxiosResponse<any>> {
    return this.axios.get('/datasource/unique', param)
  }

  /**
   * 测试数据库是否连接成功
   * @param param
   */
  public test(param: any): Promise<Base.IAxiosResponse<any>> {
    return this.axios.post('/datasource/test', param)
  }

  /**
   * 获取数据库信息列表
   * @param param
   */
  public list(param: any): Promise<Base.IAxiosResponse<any>> {
    return this.axios.get('/datasource/list', param)
  }

  /**
   * 获取全部数据库信息列表
   * @param param
   */
  public all(param: any): Promise<Base.IAxiosResponse<any>> {
    return this.axios.get('/datasource/all', param)
  }

  /**
   * 删除数据库信息
   * @param id
   */
  public delete(id: any): Promise<Base.IAxiosResponse<any>> {
    return this.axios.delete('/datasource/' + id, '')
  }

  /**
   * 更新数据库信息
   * @param param
   */
  public update(param: any): Promise<Base.IAxiosResponse<any>> {
    return this.axios.put('/datasource/', param)
  }

  /**
   * 启用/禁用
   * @param param
   * @param id
   */
  public enabled(param: any, id: any): Promise<Base.IAxiosResponse<any>> {
    return this.axios.put('/datasource/enabled/' + id, param)
  }

  /**
   * 启用/禁用
   * @param id
   */
  public default(id: any): Promise<Base.IAxiosResponse<any>> {
    return this.axios.put('/datasource/default/' + id, '')
  }

  /**
   * 启用/禁用
   * @param param
   */
  public token(param: any): Promise<Base.IAxiosResponse<any>> {
    return this.axios.post('/datasource/token', param)
  }
}

export default DatasourceApi
