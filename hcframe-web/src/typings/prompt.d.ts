import Vue from 'vue'
declare module 'vue/types/vue' {
  // 可以使用 `VueConstructor` 接口
  // 来声明全局属性
  // eslint-disable-next-line @typescript-eslint/interface-name-prefix
  interface Vue {
    $myAlert: any
    $myConfirm: any
    $myEum: any
    $get: any
    $post: any
    $echarts: any
  }
}
