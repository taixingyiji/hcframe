declare namespace Base {
  export interface IAxiosResponse<A> {
    // promise 状态码
    status: number
    data: {
      data: A
      code: number
      msg: string
    }
  }

  export interface IVuexState<P, D> {
    params: P
    res: IAxiosResponse<D>
    requestStatus: RequestStatus
  }

  export type RequestStatus =
    | 'unrequest'
    | 'requesting'
    | 'success'
    | 'error'
    | 'done';
}
