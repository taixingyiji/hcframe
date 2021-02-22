/**
 * @author lhc
 * @description 封装Vuex类,P为参数,D为返回值
 */
import VuexClass from 'vuex-class.js'

class BaseVuexClass<P, D> extends VuexClass {
 readonly namespaced: boolean = true;
 api: any;
 constructor(api: any) {
   super()
   this.api = api
 }

 public readonly state: Base.IVuexState<P, D> = {
   params: {} as P,
   res: {
     status: 0,
     data: {
       data: {} as D,
       code: 0,
       msg: ''
     }
   },
   requestStatus: 'unrequest'
 };

 get res(): Base.IAxiosResponse<D> {
   return this.state.res
 }

 get requestStatus(): Base.RequestStatus {
   return this.state.requestStatus
 }

 $assignParams(params: P): this {
   Object.assign(this.state.params, params)
   return this
 }

 $RequestStart(): this {
   this.state.requestStatus = 'requesting'
   return this
 }

 $RequestSuccess(res: Base.IAxiosResponse<D>): this {
   if (res.data.code === 0 && res.data) {
     this.state.requestStatus = 'success'
     this.state.res = { ...res }
     return this
   } else {
     if (res.data.code === 0 && res.data) {
       this.state.res = { ...res }
     }
     this.state.requestStatus = 'error'
   }
   return this
 }
}

export default BaseVuexClass
