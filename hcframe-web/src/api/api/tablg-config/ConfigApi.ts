import BaseAxios from '@/common/http'

/**
 * @author lhc
 * @description 通用查询接口Api
 * @date 2020-03-17
 */
class ConfigApi extends BaseAxios<any, any> {
  public static SELECT = 'select'
  public static TABLE = 'table'
  public static FIELD = 'field'

  /**
   * 获取列表
   * @param type 获取列表类型
   * @param param 传值
   */
  public list(type: string, param: any): Promise<Base.IAxiosResponse<any>> {
    return this.axios.get('/tableConfig/' + type, param)
  }

  /**
   * 删除数据
   * @param type 获取数据类型
   * @param ids 批量删除ids
   */
  public delete(type: string, ids: string): Promise<Base.IAxiosResponse<any>> {
    return this.axios.delete('/tableConfig/' + type, { ids })
  }

  /**
   *  保存
   * @param type 类型
   * @param param 传值
   */
  public add(type: string, param: any): Promise<Base.IAxiosResponse<any>> {
    return this.axios.post('/tableConfig/' + type, param)
  }

  /**
   * 保存不带时间戳
   * @param type 类型
   * @param param 传值
   */
  public update(type: string, param: any): Promise<Base.IAxiosResponse<any>> {
    return this.axios.put('/tableConfig/' + type, param)
  }

  /**
   * 获取table下拉列表
   */
  public getTableSelectList(): Promise<Base.IAxiosResponse<any>> {
    return this.axios.get('/tableConfig/tableSelect', '')
  }
}

export default ConfigApi
