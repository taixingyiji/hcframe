<!--
@author 刘昊晟
@description 显示按钮右上角数字的小红点
-->
<template>
  <span
    v-show="showNum !== 0 && showRedPoint"
    style="display: inline-block;padding-top: 10px"
  >
    <span
      style="border-radius: 50%;
                                     height: 20px;
                                     width: 20px;
                                     display: inline-block;
                                     background: #f30303;
                                     vertical-align: top;
                                     margin-left: -12px;
                                     margin-top: -10px;"
    >
      <span
        style="display: block;
                                         color: #FFFFFF;
                                         height: 20px;
                                         line-height: 20px;
                                         text-align: center"
      >{{ showNum > 99 ? 99 : showNum }}</span>
    </span>
  </span>
</template>
<script lang="ts">
import { Prop, Component, Vue } from 'vue-property-decorator'
@Component
export default class extends Vue {
   // 小红点展示数字超过99展示99 ,0的话不展示
   @Prop(Number) showNum!: number;
   // 是否显示小红点
   @Prop(Boolean) showRedPoint!: boolean;
}
</script>
