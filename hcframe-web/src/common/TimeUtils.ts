/***
 * @author wxy
 * @description 通用时间控件类
 */

class TimeUtils {
  // 格式化时间插件
  public static dateFtt = (fmt: any, date: Date): any => {
    const o: any = {
      'M+': date.getMonth() + 1, // 月份
      'd+': date.getDate(), // 日
      'h+': date.getHours(), // 小时
      'm+': date.getMinutes(), // 分
      's+': date.getSeconds(), // 秒
      'q+': Math.floor((date.getMonth() + 3) / 3), // 季度
      S: date.getMilliseconds() // 毫秒
    }
    if (/(y+)/.test(fmt)) {
      fmt = fmt.replace(RegExp.$1, (date.getFullYear() + '').substr(4 - RegExp.$1.length))
    }
    for (const k in o) {
      if (new RegExp('(' + k + ')').test(fmt)) {
        fmt = fmt.replace(RegExp.$1, (RegExp.$1.length === 1) ? (o[k]) : (('00' + o[k]).substr(('' + o[k]).length)))
      }
    }
    return fmt
  };

  // 格式化时间插件-星期
  public static dateWeek = (date: Date): any => {
    const wk = date.getDay()
    const weeks = ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六']
    const week = weeks[wk]
    return week
  };
}

export default TimeUtils
