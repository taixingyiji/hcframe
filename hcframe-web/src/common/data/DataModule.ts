import StringUtils from '@/common/StringUtils'
import Utils from '@/common/Utils'
import { curves, logic, sign } from '@/common/data/QueryType'

export interface IDataModule{
  // key值
  key: string
  // value值
  value: any
  // 符号
  sign: sign
  // between value值
  sValue?: any
  // 多值toString
  values?: string
  // 逻辑值
  logic?: logic
  // 括号
  curves?: curves
}

class DataModule {
  private _datas: IDataModule[] = []

  get datas(): IDataModule[] {
    return this._datas
  }

  set datas(value: IDataModule[]) {
    this._datas = value
  }

  get jsons(): string {
    return encodeURI(JSON.stringify(this.datas))
  }

  public add(data: IDataModule): DataModule {
    this.datas.push(data)
    return this
  }

  public addList(datas: IDataModule[]): DataModule {
    this.datas.concat(datas)
    return this
  }

  public hasContent(): boolean {
    if (this.datas.length > 0) {
      return true
    } else {
      return false
    }
  }

  public static formatToModul(searchForm: any, tableHead: any): DataModule {
    searchForm = Utils.exceptNull(searchForm)
    const dataModule: DataModule = new DataModule()
    for (const item in searchForm) {
      // eslint-disable-next-line no-prototype-builtins
      if (searchForm.hasOwnProperty(item)) {
        if (StringUtils.isNotEmpty(searchForm[item])) {
          const data: IDataModule = {
            value: searchForm[item],
            key: item,
            sign: sign.LIKE
          }
          let info
          if (item.indexOf('_start') !== -1) {
            const key = item.replace('_start', '')
            info = tableHead.filter((th: any) => (th.fieldName === item))
            data.sign = sign.GTE
            data.key = key
          } else if (item.indexOf('end') !== -1) {
            const key = item.replace('_end', '')
            info = tableHead.filter((th: any) => (th.fieldName === item))
            data.sign = sign.LTE
            data.key = key
          } else {
            info = tableHead.filter((th: any) => (th.fieldName === item))
            data.sign = info[0].sign
          }
          if (info) {
            dataModule.add(data)
          }
        }
      }
    }
    return dataModule
  }
}

export default DataModule
