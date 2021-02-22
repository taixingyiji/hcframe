<template>
  <div
    class="login-container"
    :style="{backgroundColor: loginBackgroundColor}"
  >
    <el-form
      ref="initData"
      :model="datasourceForm"
      :rules="rules"
      class="init-form"
      autocomplete="on"
      label-position="left"
    >
      <div class="title-container">
        <h3 class="title">
          数据初始化
        </h3>
      </div>
      <el-row>
        <el-col :span="3">
          <span class="init-container">
            名称：
          </span>
        </el-col>
        <el-col :span="18">
          <el-form-item prop="sysDescription">
            <el-input
              ref="sysDescription"
              v-model="datasourceForm.sysDescription"
              placeholder="名称/描述"
              name="sysDescription"
              type="text"
              tabindex="1"
              autocomplete="on"
            />
          </el-form-item>
        </el-col>
        <el-col :span="2">
          <el-popover
            style="margin-left: 10px;margin-top: 20px"
            placement="top-start"
            title="名称/描述"
            width="300"
            trigger="hover"
            content="使用者自己定义的名称，用于标识数据库作用等，可包含中文"
          >
            <i
              slot="reference"
              class="el-icon-info icon-style"
            />
          </el-popover>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="3">
          <span class="init-container">
            KEY：
          </span>
        </el-col>
        <el-col :span="18">
          <el-form-item prop="commonAlias">
            <el-input
              ref="commonAlias"
              v-model="datasourceForm.commonAlias"
              placeholder="KEY"
              name="commonAlias"
              type="text"
              tabindex="1"
              autocomplete="on"
            />
          </el-form-item>
        </el-col>
        <el-col :span="2">
          <el-popover
            style="margin-left: 10px;margin-top: 20px"
            placement="top-start"
            title="KEY"
            width="300"
            trigger="hover"
            content="用于系统识别的代表数据库的唯一key，不可包含空格，不可包含中文"
          >
            <i
              slot="reference"
              class="el-icon-info icon-style"
            />
          </el-popover>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="3">
          <span class="init-container">
            类型：
          </span>
        </el-col>
        <el-col :span="18">
          <el-form-item prop="commonType">
            <el-select
              v-model="datasourceForm.commonType"
              name="commonType"
              placeholder="请选择数据库类型"
              class="el-input"
              autocomplete="on"
            >
              <el-option
                v-for="item in datasourceType"
                :key="item.typeKey"
                :label="item.typeKey"
                :value="item.typeKey"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="2">
          <el-popover
            style="margin-left: 10px;margin-top: 20px"
            placement="top-start"
            title="数据库类型"
            width="300"
            trigger="hover"
            content="选择数据库类型，如Mysql,Oracle等"
          >
            <i
              slot="reference"
              class="el-icon-info icon-style"
            />
          </el-popover>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="3">
          <span class="init-container">
            URL：
          </span>
        </el-col>
        <el-col :span="18">
          <el-form-item prop="url">
            <el-input
              ref="url"
              v-model="datasourceForm.url"
              placeholder="URL"
              name="url"
              type="text"
              tabindex="1"
              autocomplete="on"
            />
          </el-form-item>
        </el-col>
        <el-col :span="2">
          <el-popover
            style="margin-left: 10px;margin-top: 20px"
            placement="top-start"
            title="URL"
            width="300"
            trigger="hover"
            content="用于设置数据库的url，与之前yml文件格式保持一致即可"
          >
            <i
              slot="reference"
              class="el-icon-info icon-style"
            />
          </el-popover>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="3">
          <span class="init-container">
            用户名：
          </span>
        </el-col>
        <el-col :span="18">
          <el-form-item prop="username">
            <el-input
              ref="username"
              v-model="datasourceForm.username"
              placeholder="用户名"
              name="username"
              type="text"
              tabindex="1"
              autocomplete="on"
            />
          </el-form-item>
        </el-col>
        <el-col :span="2">
          <el-popover
            style="margin-left: 10px;margin-top: 20px"
            placement="top-start"
            title="用户名"
            width="300"
            trigger="hover"
            content="数据库用户名"
          >
            <i
              slot="reference"
              class="el-icon-info icon-style"
            />
          </el-popover>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="3">
          <span class="init-container">
            密码：
          </span>
        </el-col>
        <el-col :span="18">
          <el-tooltip
            v-model="capsTooltip"
            content="Caps lock is On"
            placement="right"
            manual
          >
            <el-form-item prop="password">
              <el-input
                :key="passwordType"
                ref="password"
                v-model="datasourceForm.password"
                :type="passwordType"
                placeholder="密码"
                name="password"
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
            title="密码"
            width="300"
            trigger="hover"
            content="数据库密码"
          >
            <i
              slot="reference"
              class="el-icon-info icon-style"
            />
          </el-popover>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="3">
          <span class="init-container">
            库名：
          </span>
        </el-col>
        <el-col :span="18">
          <el-form-item prop="schemaName">
            <el-input
              ref="schemaName"
              v-model="datasourceForm.schemaName"
              placeholder="库名"
              name="schemaName"
              type="text"
              tabindex="1"
              autocomplete="on"
              @keyup.enter.native="handleCommit(1)"
            />
          </el-form-item>
        </el-col>
        <el-col :span="2">
          <el-popover
            style="margin-left: 10px;margin-top: 20px"
            placement="top-start"
            title="数据库名"
            width="300"
            trigger="hover"
            content="Mysql中为databases中的名称，达梦为模式名，Oracle为创建的用户名"
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
          @click.native.prevent="handleTest"
        >
          测试连接
        </el-button>
        <el-button
          :loading="loading"
          type="primary"
          style="width:100%; display:block;margin-bottom: 30px ;margin-left: 0 !important; ;"
          :disabled="disabled"
          @click.native.prevent="handleCommit(1)"
        >
          保存连接
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
import StringUtils from '@/common/StringUtils'

  @Component({
    name: 'datasourceInit',
    components: {}
  })
export default class extends mixins(Datasource) {
  mounted() {
    if (StringUtils.isEmpty(SourceKeyModule.sourceToken)) {
      this.$router.push({
        path: '/dataLogin'
      })
    }
    this.checkDatasource()
    this.getTypeList()
  }

  private checkDatasource() {
    // this.datasourceApi.hasSource("").then(ref=>{
    //   if (ref.data.code === 0) {
    //     this.$router.push({
    //       path: "/",
    //     });
    //   }
    // });
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
