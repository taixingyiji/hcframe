import BaseAxios from '@/common/http'

/**
 * @author lhc
 * @description 通用查询接口Api
 * @date 2020-03-17
 */
class CommonApi extends BaseAxios<any, any> {
  /**
   *  通用查询带分页
   * @param param 传输的数据
   * @param tableAlias 表别名
   */
  /* public list(param:any,tableAlias:string): Promise<Base.IAxiosResponse<any>> {
    return this.axios.post('/'+tableAlias+'/getList', param);
  } */
  public list(param: any, tableAlias: string): Promise<Base.IAxiosResponse<any>> {
    return this.axios.get('/common/' + tableAlias, param)
  }

  /**
   * 删除数据
   * @param ids 批量删除ids
   * @param tableAlias 表别名
   */
  public delete(ids: string, tableAlias: string): Promise<Base.IAxiosResponse<any>> {
    return this.axios.delete('/common/' + tableAlias, { ids: ids })
  }

  /**
   *  保存带时间戳
   * @param param 传值
   * @param tableAlias 表别名
   */
  public saveWithDate(param: any, tableAlias: string): Promise<Base.IAxiosResponse<any>> {
    return this.axios.post('/common/' + tableAlias + '/date', param)
  }

  /**
   * 保存不带时间戳
   * @param param 传值
   * @param tableAlias 表别名
   */
  public save(param: any, tableAlias: string): Promise<Base.IAxiosResponse<any>> {
    return this.axios.post('/common/' + tableAlias, param)
  }

  /**
   * 更新不带时间戳
   * @param param 传值
   * @param tableAlias 表别名
   */
  public update(param: any, tableAlias: string): Promise<Base.IAxiosResponse<any>> {
    return this.axios.put('/common/' + tableAlias, param)
  }

  /**
   * 更新带时间戳
   * @param param 传值
   * @param tableAlias 表别名
   */
  public updateWithDate(param: any, tableAlias: string): Promise<Base.IAxiosResponse<any>> {
    return this.axios.put('/common/' + tableAlias + '/date', param)
  }

  /**
   * 获取单表信息
   * @param tableAlias
   */
  public getTableInfo(tableAlias: string): Promise<Base.IAxiosResponse<any>> {
    return this.axios.get('/common/' + tableAlias + '/getTableInfo/', '')
  }

  /**
   * 批量更新
   * @param tableAlias 表别名
   * @param params 参数
   */
  public updateBatch(tableAlias: string, params: any): Promise<Base.IAxiosResponse<any>> {
    return this.axios.put('/common/' + tableAlias + '/batch', params)
  }

  /**
   * 批量更新（带日期）
   * @param tableAlias 表别名
   * @param params 参数
   */
  public updateBatchWithDate(tableAlias: string, params: any): Promise<Base.IAxiosResponse<any>> {
    return this.axios.put('/common/' + tableAlias + '/batchWithDate', params)
  }

  /**
   * 批量新增
   * @param tableAlias 表别名
   * @param data
   */
  public saveBatch(tableAlias: string, data: string): Promise<Base.IAxiosResponse<any>> {
    return this.axios.post('/common/' + tableAlias + '/batch', { data })
  }

  /**
   * 批量新增（带日期）
   * @param tableAlias 表别名
   * @param data 参数
   */
  public saveBatchWithDate(tableAlias: string, data: string): Promise<Base.IAxiosResponse<any>> {
    return this.axios.post('/common/' + tableAlias + '/batchWithDate', { data })
  }

  /**
   * 获取基表信息
   * @param tableNames 表名数组.toString
   */
  public getBaseTableInfo(tableNames: string): Promise<Base.IAxiosResponse<any>> {
    return this.axios.get('/common/getBaseTableInfo', { tableNames })
  }
}

export default CommonApi
