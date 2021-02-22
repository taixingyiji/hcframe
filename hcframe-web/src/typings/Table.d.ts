declare namespace Table {
  export interface ITableHead {
    data: Array<IXmlOption>
    sort: ISort
  }

  export interface IXmlOption {
    change: string | number
    fieldname: string
    fieldnameInCn: string
    notNull: string
    type: string | number
    webType: string
    width: string | number
  }

  export interface ISort {
    field: string
    id: string
    sort: sortType
  }

  export type sortType = 'desc' | 'asc';

  export interface ITableHeadRequest {
    tableName: string
  }
}
