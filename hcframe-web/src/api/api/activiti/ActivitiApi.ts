import BaseAxios from '@/common/CommonHttp'

class ActivitiApi extends BaseAxios<any, any> {
  // 获取工作流列表
  public getList(param: any): Promise<Base.IAxiosResponse<any>> {
    return this.axios.get('/process', param)
  }

  // 验证工作流标志和名称是否唯一
  public verifyProcess(param: any): Promise<Base.IAxiosResponse<any>> {
    return this.axios.post('/process/unique', param)
  }

  // 删除工作流列表
  public deleteProcess(param: any): Promise<Base.IAxiosResponse<any>> {
    return this.axios.delete(`/process/${param.id}`, param)
  }

  // 更新or添加工作流
  public updateProcessXml(param: any): Promise<Base.IAxiosResponse<any>> {
    return this.axios.post('/process/byXml', param)
  }

  // 获取流程图bpmn流程图并展示
  public showBpmn(param: any): Promise<Base.IAxiosResponse<any>> {
    return this.axios.get('/process/bpmn', param)
  }

  // 获取流程图bpmn流程图并展示
  public editProcessXml(param: any): Promise<Base.IAxiosResponse<any>> {
    return this.axios.put('/process/byXml', param)
  }
}

export default ActivitiApi
