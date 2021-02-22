/***
 * @author lhc
 * @description 借口获取数据基类,配置
 *
 */
import axios from 'axios'
import qs from 'qs'
import prompt from '@/common/promptEnum'
import Utils from '@/common/Utils'
import { UserModule } from '@/store/modules/user'
import router from '@/router'
import { Message } from 'element-ui'
class LocalAxios {
   public axios: any;
   constructor(url: any) {
     this.axios = axios.create({
       baseURL: url,
       timeout: 100000
       // withCredentials: true
     })
     this.onRequest()
     this.onResponse()
   }

   private onRequest() {
     this.axios.interceptors.request.use(
       (config: any) => {
         /* config.startTime = new Date().getTime();
      console.log('拦截器生效');
      return config; */
         if (config.method === 'post' || config.method === 'put') {
           config.data = qs.stringify(
             Utils.exceptNull(config.data)
           )
         } else {
           config.params = Utils.exceptNull(config.params)
         }
         // config.method === 'post'
         //   ? (config.data = qs.stringify(
         //     Utils.exceptNull(config.data)
         //      ))
         //   : (config.params = Utils.exceptNull(config.params));
         config.headers['Content-Type'] =
               'application/x-www-form-urlencoded'
         if (UserModule.token) {
           config.headers['X-Access-Token'] = UserModule.token
         }
         return config
       },
       (error: any) => {
         Message.error('服务器Request出错')
         console.log(error, prompt.ERROR)
         return Promise.reject(error)
       }
     )
   }

   private onResponse() {
     this.axios.interceptors.response.use(
       (response: any) => {
         /* console.log(
          `路由${
            response.config.url
            }请求成功，耗时${new Date().getTime() -
          response.config.startTime}ms`
        );
        return response.data; */
         if (response.data.code !== 3) {
           return response
         } else {
           UserModule.ResetToken()
           router.push('/')
         }
       },
       (err: any) => {
         /* console.log(`错误信息 ${err.message}`);
        err.data = {
          code: -10000,
          data: '网络出错'
        };
        console.log(
          `路由${err.config.url}请求失败，耗时${new Date().getTime() -
          err.config.startTime}ms`
        );
        return Promise.resolve(err.data); */
         console.log('error')
         console.log(err)
         console.log(JSON.stringify(err))
         Message.error('服务器Request出错')
         return Promise.reject(err)
       }
     )
   }
}

export default LocalAxios
