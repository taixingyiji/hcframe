/**
 * 作者:刘昊晟
 * 通用工具类
 */
import { MessageBox } from 'element-ui'
import StringUtils from './StringUtils'
import prompt from '@/common/promptEnum'

// 实例
export let _this: any = ''

/**
 * 获取实例
 * @param that
 */
export function getPoint(that: any) {
  _this = that
}
export type Prompt = 'success' | 'error' | 'warning' | 'info';
/**
 * 弹出框信息信息
 * @param message
 * @param alertStatus
 */
export const alertUtil = (message: string, alertStatus: Prompt) => {
  if (StringUtils.isEmpty(message)) {
    if (alertStatus === prompt.SUCCESS) {
      message = '操作成功'
    }
    if (alertStatus === prompt.ERROR) {
      message = '操作失败'
    }
    if (alertStatus === prompt.WARNING) {
      message = '操作警告'
    }
    if (alertStatus === prompt.INFO) {
      message = '好的'
    }
  }
  return MessageBox.alert(message, '提示', {
    type: alertStatus,
    confirmButtonText: '确定'
  })
}

/**
 * 将对象转换成formdata
 * @param Object
 * @returns {URLSearchParams}
 */
export const serializeObjectToURLSearchParams = (Object: any) => {
  const addFormUrl = new URLSearchParams()
  for (const item in Object) {
    // eslint-disable-next-line no-prototype-builtins
    if (Object.hasOwnProperty(item)) {
      if (StringUtils.isNotEmpty(Object[item])) {
        addFormUrl.append(item, Object[item])
      }
    }
  }
  return addFormUrl
}

/**
 * confirm工具类
 * @param message
 * @param alertStatus
 * @returns {*} Promise
 */
export const confirmUtil = (message: string, alertStatus: Prompt) => {
  return MessageBox.confirm(message, '提示', {
    type: alertStatus
  })
}

/**
 * 表单初始化
 */
export const serializeForm = (cols: any) => {
  return cols.reduce((obj: any, item: any) => {
    /* obj[item.fieldname] = {
            value: '',
            component: item.webType,
            label: item.fieldname,
            labelcn: item.fieldnameInCn
        } */
    obj[item.fieldname] = ''
    return obj
  }, {})
}
