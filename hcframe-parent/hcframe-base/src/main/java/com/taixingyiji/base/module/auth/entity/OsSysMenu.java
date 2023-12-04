package com.taixingyiji.base.module.auth.entity;

import com.taixingyiji.base.module.data.annotation.DataIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 菜单权限表(OsSysMenu)实体类
 *
 * @author makejava
 * @since 2020-12-09 09:57:20
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
@Table(name = "OS_SYS_MENU")
public class OsSysMenu implements Serializable {
    private static final long serialVersionUID = -36592644935215591L;

    public static final String M = "M";
    public static final String C = "C";
    public static final String F = "F";
    public static final String I = "I";


    /**
    * 菜单ID
    */
    @Id
    private Long menuId;
    /**
    * 菜单名称
    */
    private String menuName;
    /**
    * 父菜单ID
    */
    private Long parentId;
    /**
    * 显示顺序
    */
    private Integer orderNum;
    /**
    * 路由地址
    */
    private String path;
    /**
    * 组件路径
    */
    private String component;
    /**
    * 是否为外链（0是 1否）
    */
    private String isFrame;
    /**
    * 是否缓存（0缓存 1不缓存）
    */
    private String isCache;
    /**
    * 菜单类型（M目录 C菜单 F按钮）
    */
    private String menuType;
    /**
    * 菜单状态（0显示 1隐藏）
    */
    private String visible;
    /**
    * 菜单状态（0正常 1停用）
    */
    private String status;
    /**
    * 权限标识
    */
    private String perms;
    /**
    * 菜单图标
    */
    private String icon;
    /**
    * 创建时间
    */
    private Date createTime;
    /**
    * 更新时间
    */
    private Date updateTime;
    /**
    * 备注
    */
    private String remark;
    /**
     * if true, will always show the root menu (default is false)
     * if false, hide the root menu when has less or equal than one children route
     */
    private String alwaysShow;

    /**
     * if false, the item will be hidden in breadcrumb (default is true)
     */
    private String breadcrumb;
    /**
     * if true, the tag will affix in the tags-view
     */
    private String affix;

    private Long osId;

    private Integer version;

    private Integer deleted;

    @DataIgnore
    private List<OsSysMenu> children;

}
