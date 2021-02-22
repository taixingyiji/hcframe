/***
 * @author lhc
 * @description 继承BaseAxios<M>,
 */
import LocalAxios from '@/common/CommonLocalAxios'

class Axios<A, D> extends LocalAxios {
  post(url: string, params: A): Promise<Base.IAxiosResponse<D>> {
    return this.axios.post(url, params)
  }

  get(url: string, params: A): Promise<Base.IAxiosResponse<D>> {
    return this.axios.get(url, { params })
  }

  put(url: string, params: A): Promise<Base.IAxiosResponse<D>> {
    return this.axios.put(url, params)
  }

  delete(url: string, params: A): Promise<Base.IAxiosResponse<D>> {
    return this.axios.delete(url, { params })
  }
}

class BaseAxios<A, D> {
  axios: Axios<A, D>;
  constructor(url: any) {
    this.axios = new Axios(url)
  }
}
export default BaseAxios
