/***
 * @author lhc
 * @description 通用工具类
 */

import StringUtils from '@/common/StringUtils'
import { MessageBox } from 'element-ui'
import prompt from '@/common/promptEnum'

class Utils {
  /**
   * 弹出框信息信息
   * @param message
   * @param alertStatus
   */
  public static alertUtil = (
    message: string,
    alertStatus: prompt
  ): Promise<any> => {
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
  };

  /**
   * 将对象转换成formdata
   * @param Object
   * @returns {URLSearchParams}
   */
  public static serializeObjectToURLSearchParams = (
    Object: any
  ): URLSearchParams => {
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
  };

  /**
   * confirm工具类
   * @param message
   * @returns {*} Promise
   */
  public static confirmUtil = (message: string): Promise<any> => {
    return MessageBox.confirm(message, '提示', {
      type: 'warning'
    })
  };

  /**
   * 格式化文件大小参数 传入a即可
   * @param a----文件大小数字
   * @param b
   * @param c
   */
  public static formatSize = (a: any, b: any, c: any): any => {
    let d
    for (c = c || ['B', 'K', 'M', 'G', 'TB']; (d = c.shift()) && a > 1024;) {
      a /= 1024
    }
    return (d === 'B' ? a : a.toFixed(b || 2)) + d
  };

  /**
   * 剔除传值为空的
   * @param params
   */
  public static exceptNull = (params: any): any => {
    const data: any = {}
    for (const item in params) {
      // eslint-disable-next-line no-prototype-builtins
      if (params.hasOwnProperty(item)) {
        if (StringUtils.isNotEmpty(params[item])) {
          if (typeof params[item] === 'object') {
            const a = params[item] as object
            data[item] = JSON.stringify(a)
          } else {
            data[item] = params[item]
          }
        }
      }
    }
    return data
  };

  /**
   * 去除标签
   * @param val
   */
  public static exceptTag = (val: any): any => {
    return val.replace(/<[^>]*>/g, '')
  };

  /**
   * 将java字段转换成mysql字段 如：将字段userId转换成user_id
   * @param word
   */
  public static changeJavaTypeToMysql = (word: string): any => {
    let uni
    for (let i = 0; i < word.length; i++) {
      uni = word.charCodeAt(i)
      if (uni >= 65 && uni <= 90) { // 否则都是大写
        const str = word.charAt(i)
        word = word.replace(str, '_' + str.toLocaleLowerCase())
      }
    }
    return word
  };

  public static charCounts=(str: string, char: string): number => {
    let pos
    const arr = []
    pos = str.indexOf(char)
    while (pos > -1) {
      arr.push(pos)
      pos = str.indexOf(char, pos + 1)
    }
    return arr.length
  }
}

export default Utils
