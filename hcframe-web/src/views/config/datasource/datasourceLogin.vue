<template>
  <div
    class="login-container"
    :style="{backgroundColor: loginBackgroundColor}"
  >
    <el-form
      ref="initData"
      :model="datasourceForm"
      :rules="rulezzz"
      class="init-form"
      autocomplete="on"
      label-position="left"
    >
      <div class="title-container">
        <h3 class="title">
          请输入数据源Token
        </h3>
      </div>
      <el-row>
        <el-col :span="3">
          <span class="init-container" />
        </el-col>
        <el-col :span="18">
          <el-tooltip
            v-model="capsTooltip"
            content="Caps lock is On"
            placement="right"
            manual
          >
            <el-form-item prop="Token">
              <el-input
                :key="passwordType"
                ref="password"
                v-model="datasourceForm.Token"
                :type="passwordType"
                placeholder="Token"
                name="Token"
                tabindex="2"
                autocomplete="on"
                @keyup.native="checkCapslock"
                @blur="capsTooltip = false"
              />
              <span
                class="show-pwd"
                @click="showPwd"
              >
                <svg-icon :name="passwordType === 'password' ? 'eye-off' : 'eye-on'" />
              </span>
            </el-form-item>
          </el-tooltip>
        </el-col>
        <el-col :span="2">
          <el-popover
            style="margin-left: 10px;margin-top: 20px"
            placement="top-start"
            title="TOKEN"
            width="300"
            trigger="hover"
            content="此Token可从后台启动日志中获取"
          >
            <i
              slot="reference"
              class="el-icon-info icon-style"
            />
          </el-popover>
        </el-col>
      </el-row>
      <div style="width: 80%; margin: 20px auto">
        <el-button
          :loading="testLoading"
          type="primary"
          style="width:100%; display:block;margin-bottom: 30px  ;"
          @click="datasourceLogin"
        >
          登陆
        </el-button>
      </div>
    </el-form>

    <el-dialog
      :title="$t('login.thirdparty')"
      :visible.sync="showDialog"
    >
      {{ $t('login.thirdpartyTips') }}
      <br>
      <br>
      <br>
      <social-sign />
    </el-dialog>
  </div>
</template>

<script lang="ts">
import { Component } from 'vue-property-decorator'
import { SettingsModule } from '@/store/modules/settings'
import variables from '@/styles/_variables.scss'
import { mixins } from 'vue-class-component'
import Datasource from '@/api/mixins/Datasource'
import { SourceKeyModule } from '@/store/modules/source-key'
import { Form as ElForm } from 'element-ui/types/element-ui'

  @Component({
    name: 'datasourceLogin',
    components: {}
  })
export default class extends mixins(Datasource) {
    public rulezzz = {
      Token: [
        { required: true, message: '请填写Token', trigger: 'change' }
      ]
    };

    mounted() {
    }

    datasourceLogin() {
      (this.$refs.initData as ElForm).validate(async(valid: boolean) => {
        if (valid) {
          SourceKeyModule.login(this.datasourceForm.Token)
        }
      })
    }

    get loginBackgroundColor() {
      if (SettingsModule.sidebarBgTheme) {
        const tintColor = (color: string, tint: number) => {
          let red = parseInt(color.slice(1, 3), 16)
          let green = parseInt(color.slice(3, 5), 16)
          let blue = parseInt(color.slice(5, 7), 16)
          if (tint === 0) {
            // when primary color is in its rgb space
            return [red, green, blue].join(',')
          } else {
            red += Math.round(tint * (255 - red))
            green += Math.round(tint * (255 - green))
            blue += Math.round(tint * (255 - blue))
            return `#${red.toString(16)}${green.toString(16)}${blue.toString(
              16
            )}`
          }
        }
        return tintColor(SettingsModule.theme, 0.2)
      } else {
        return variables.loginBg
      }
    }
}
</script>

<style lang="scss" scoped>
  // References: https://www.zhangxinxu.com/wordpress/2018/01/css-caret-color-first-line/
  @supports (-webkit-mask: none) and (not (cater-color: $loginCursorColor)) {
    .login-container .el-input {
      input {
        color: $loginCursorColor;
      }

      input::first-line {
        color: $lightGray;
      }
    }
  }

  .login-container {
    .el-input {
      display: inline-block;
      height: 47px;
      width: 100%;

      input {
        height: 47px;
        background: transparent;
        border: 0px;
        border-radius: 0px;
        padding: 12px 5px 12px 15px;
        color: $lightGray;
        caret-color: $loginCursorColor;
        -webkit-appearance: none;

        &:-webkit-autofill {
          box-shadow: 0 0 0px 1000px $loginBg inset !important;
          -webkit-text-fill-color: #fff !important;
        }
      }
    }

    /*.el-form-item {*/
    /*  border: 1px solid rgba(255, 255, 255, 0.1);*/
    /*  background: rgba(0, 0, 0, 0.1);*/
    /*  border-radius: 5px;*/
    /*  color: #454545;*/
    /*}*/

    .el-form-item__content {
      border: 1px solid transparent;
      background-color: rgba(255, 255, 255, 0.25);
    }

    .el-form-item__content:hover,
    .el-form-item__content:focus {
      color: white;
      border: 1px solid rgba(255, 255, 255, 0.1);
      background-color: transparent;
    }
  }
</style>

<style lang="scss" scoped>
  .login-container {
    height: 100%;
    width: 100%;
    overflow: hidden;
    // background-color: $loginBg;

    /*扁平化样式*/
    /*.login-form {*/
    /*  position: relative;*/
    /*  width: 520px;*/
    /*  max-width: 100%;*/
    /*  padding: 160px 35px 0;*/
    /*  margin: 0 auto;*/
    /*  overflow: hidden;*/
    /*}*/

    .init-form {
      position: relative;
      display: block;
      width: 700px;
      max-width: 100%;
      padding: 50px 40px 30px;
      margin: 0 auto;
      top: 15%;
      overflow: hidden;
      background-color: rgba(255, 255, 255, 0.09);
      box-shadow: 0 15px 30px -10px rgba(0, 0, 0, 0.85);
    }

    .tips {
      font-size: 14px;
      color: #fff;
      margin-bottom: 10px;

      span {
        &:first-of-type {
          margin-right: 16px;
        }
      }
    }

    .init-container {
      padding: 15px 6px 6px 15px;
      // color: $darkGray;
      height: 47px;
      vertical-align: middle;
      width: 200px;
      display: inline-block;
    }

    .title-container {
      position: relative;

      .title {
        font-size: 26px;
        color: $lightGray;
        margin: 0px auto 40px auto;
        text-align: center;
        font-weight: bold;
      }

      .set-language {
        color: #fff;
        position: absolute;
        top: 3px;
        font-size: 18px;
        right: 0px;
        cursor: pointer;
      }
    }

    .show-pwd {
      position: absolute;
      right: 10px;
      top: 7px;
      font-size: 16px;
      color: $darkGray;
      cursor: pointer;
      user-select: none;
    }

    .thirdparty-button {
      position: absolute;
      right: 0;
      bottom: 6px;
    }

    @media only screen and (max-width: 470px) {
      .thirdparty-button {
        display: none;
      }
    }
  }
</style>
