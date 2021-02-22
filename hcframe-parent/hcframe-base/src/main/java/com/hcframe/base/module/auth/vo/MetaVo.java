/**/
package com.hcframe.base.module.auth.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 路由显示信息
 *
 * @author ruoyi
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class MetaVo implements Serializable {
    private static final long serialVersionUID = -4261993881269135509L;
    /**
     * 设置该路由在侧边栏和面包屑中展示的名字
     */
    private String title;

    /**
     * 设置该路由的图标，对应路径src/assets/icons/svg
     */
    private String icon;

    /**
     * 设置为true，则不会被 <keep-alive>缓存
     */
    private boolean noCache;

    /**
     * 设置为是否隐藏，true为隐藏
     */
    private boolean hidden;

    /**
     * if true, will always show the root menu (default is false)
     * if false, hide the root menu when has less or equal than one children route
     */
    private boolean alwaysShow;

    /**
     * if false, the item will be hidden in breadcrumb (default is true)
     */
    private boolean breadcrumb;
    /**
     * if true, the tag will affix in the tags-view
     */
    private boolean affix;



}
