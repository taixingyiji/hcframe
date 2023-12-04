package com.taixingyiji.user.module.auth.service.impl;

import com.hcframe.base.common.utils.StringUtils;
import com.hcframe.base.module.auth.constants.AuthConstants;
import com.hcframe.base.module.auth.dao.OsSysMenuDao;
import com.hcframe.base.module.auth.entity.OsSysMenu;
import com.hcframe.base.module.auth.vo.MetaVo;
import com.hcframe.base.module.auth.vo.RouterVo;
import com.hcframe.base.module.data.module.*;
import com.hcframe.redis.RedisUtil;
import com.taixingyiji.user.module.auth.mapper.AuthDao;
import com.taixingyiji.user.module.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author lhc
 * @version 1.0
 * @className AuthServiceImpl
 * @date 2021年04月21日 10:41 上午
 * @description 描述
 */
@Service
public class AuthServiceImpl implements AuthService {

    final BaseMapper baseMapper;
    final RedisUtil redisUtil;

    final OsSysMenuDao osSysMenuDao;

    final AuthDao authDao;

    public AuthServiceImpl(@Qualifier(BaseMapperImpl.BASE) BaseMapper baseMapper,
                           RedisUtil redisUtil,
                           OsSysMenuDao osSysMenuDao,
                           AuthDao authDao) {
        this.baseMapper = baseMapper;
        this.redisUtil = redisUtil;
        this.osSysMenuDao = osSysMenuDao;
        this.authDao = authDao;
    }


    @Override
    public List<String> getUserRoleAuth(String userId) {
        return authDao.getUserRoleAuth(userId);
    }

    @Override
    public List<String> getUserRoleGroupAuth(String userId) {
        return authDao.getUserRoleGroupAuth(userId);

    }

    @Override
    public List<String> getOrgRoleAuth(String orgCode) {
        return authDao.getOrgRoleAuth(orgCode);

    }

    @Override
    public List<String> getOrgGroupAuth(String orgCode) {
        return authDao.getOrgGroupAuth(orgCode);
    }

    @Override
    public Set<String> getUserAuth(String userId) {
        Set<String> authSet = (Set<String>) redisUtil.hget("auth", userId);
        if (authSet == null) {
            Map<String, Object> user = baseMapper.selectByPk(DataMap.builder().tableName("GB_CAS_MEMBER").pkName("ID").pkValue(userId).build());
            if (user != null && "admin".equals(user.get("NAME"))) {
                authSet = getAllAuth();
                redisUtil.hset("auth", userId, authSet, 24 * 3600);
                return getAllAuth();
            }
            List<String> roleAuth = getUserRoleAuth(String.valueOf(user.get("ID")));
            List<String> groupAuth = getUserRoleAuth(String.valueOf(user.get("ID")));
            List<String> orgAui;
            List<String> orgGroupAuth;
            if (!org.springframework.util.StringUtils.isEmpty(user.get("DEPT_CODE"))) {
                orgAui = getOrgRoleAuth(String.valueOf(user.get("DEPT_CODE")));
                orgGroupAuth = getOrgGroupAuth(String.valueOf(user.get("DEPT_CODE")));
            } else {
                orgAui = new ArrayList<>();
                orgGroupAuth = new ArrayList<>();
            }
            List<String> orgGuobo = getOrgGroupAuth("guobo");
            List<String> orgGuoboGroup = getOrgGroupAuth("guobo");
            authSet = new HashSet<>(roleAuth);
            authSet.addAll(groupAuth);
            authSet.addAll(orgAui);
            authSet.addAll(orgGroupAuth);
            authSet.addAll(orgGuobo);
            authSet.addAll(orgGuoboGroup);
            if (!org.springframework.util.StringUtils.isEmpty(user.get("DEPT_CODE")) && String.valueOf(user.get("DEPT_CODE")).length() == 6) {
                String code = String.valueOf(user.get("DEPT_CODE"));
                code = code.substring(0, 4);
                List<String> orgAuiParent = getOrgGroupAuth(code);
                List<String> orgGroupAuthParent = getOrgGroupAuth(code);
                authSet.addAll(orgAuiParent);
                authSet.addAll(orgGroupAuthParent);
            }
            redisUtil.hset("auth", userId, authSet, 24 * 3600);
        }
        return authSet;
    }

    @Override
    public List<OsSysMenu> getMenuResult() {
        List<OsSysMenu> osSysMenus = osSysMenuDao.selectMenu();
        return getChildPerms(osSysMenus, 0);
    }

    /**
     * 根据父节点的ID获取所有子节点
     *
     * @param list     分类表
     * @param parentId 传入的父节点ID
     * @return String
     */
    public List<OsSysMenu> getChildPerms(List<OsSysMenu> list, long parentId) {
        List<OsSysMenu> returnList = new ArrayList<OsSysMenu>();
        for (Iterator<OsSysMenu> iterator = list.iterator(); iterator.hasNext(); ) {
            OsSysMenu t = iterator.next();
            // 一、根据传入的某个父节点ID,遍历该父节点的所有子节点
            if (t.getParentId() == parentId) {
                recursionFn(list, t);
                returnList.add(t);
            }
        }
        return returnList;
    }

    /**
     * 递归列表
     *
     * @param list
     * @param t
     */
    private void recursionFn(List<OsSysMenu> list, OsSysMenu t) {
        // 得到子节点列表
        List<OsSysMenu> childList = getChildList(list, t);
        t.setChildren(childList);
        for (OsSysMenu tChild : childList) {
            if (hasChild(list, tChild)) {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<OsSysMenu> getChildList(List<OsSysMenu> list, OsSysMenu t) {
        List<OsSysMenu> tlist = new ArrayList<OsSysMenu>();
        Iterator<OsSysMenu> it = list.iterator();
        while (it.hasNext()) {
            OsSysMenu n = it.next();
            if (n.getParentId().longValue() == t.getMenuId().longValue()) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    private boolean hasChild(List<OsSysMenu> list, OsSysMenu t) {
        return getChildList(list, t).size() > 0;
    }

    @Override
    public List<RouterVo> formatMenu(List<OsSysMenu> menus) {
        List<RouterVo> routers = new LinkedList<RouterVo>();
        for (OsSysMenu menu : menus) {
            RouterVo router = new RouterVo();
            router.setName(getRouteName(menu));
            router.setPath(getRouterPath(menu));
            router.setComponent(getComponent(menu));
            MetaVo metaVo = MetaVo
                    .builder()
                    .title(menu.getMenuName())
                    .icon(menu.getIcon())
                    .noCache(StringUtils.equals("0", menu.getIsCache()))
                    .hidden("0".equals(menu.getVisible()))
                    .breadcrumb("1".equals(menu.getBreadcrumb()))
                    .affix("1".equals(menu.getAffix()))
                    .alwaysShow("1".equals(menu.getAlwaysShow()))
                    .build();
            List<OsSysMenu> cMenus = menu.getChildren();
            if (cMenus != null && !cMenus.isEmpty() && AuthConstants.TYPE_DIR.equals(menu.getMenuType())) {
                router.setRedirect("noredirect");
                router.setChildren(formatMenu(cMenus));
            } else if (isMeunFrame(menu)) {
                List<RouterVo> childrenList = new ArrayList<RouterVo>();
                RouterVo children = new RouterVo();
                children.setPath(menu.getPath());
                children.setComponent(menu.getComponent());
                children.setName(StringUtils.capitalize(menu.getPath()));
                metaVo = metaVo.toBuilder()
                        .title(menu.getMenuName())
                        .icon(menu.getIcon())
                        .noCache(StringUtils.equals("0", menu.getIsCache()))
                        .alwaysShow("1".equals(menu.getAlwaysShow()))
                        .build();
                childrenList.add(children);
                router.setChildren(childrenList);
            } else if (cMenus == null || cMenus.isEmpty()) {
                metaVo.setAlwaysShow(false);
            }
            router.setMeta(metaVo);
            routers.add(router);
        }
        return routers;
    }

    @Override
    public List<OsSysMenu> getUserMenuResult(Set<String> set) {
        if (set == null || set.size() == 0) {
            return new ArrayList<>();
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (String str : set) {
            stringBuilder.append("'").append(str).append("'").append(",");
        }
        List<OsSysMenu> list = osSysMenuDao.selectMenuByUser(stringBuilder.substring(0, stringBuilder.length() - 1));
        return getChildPerms(list, 0);
    }

    @Override
    public Set<String> getAllAuth() {
        return osSysMenuDao.selectAllAuth();
    }

    @Override
    public List<Map<String, Object>> getMenuResultList(OsSysMenu osSysMenu) {
        return authDao.selectMenuList(osSysMenu);
    }

    @Override
    public Long getUserOs(String userId) {
        Map<String, Object> user = baseMapper.selectByPk("GB_CAS_MEMBER", "ID", userId.replaceAll("\"", ""));
        if (user != null && "admin".equals(user.get("NAME"))) {
            return baseMapper.count("OS_SYS_OS", Condition.creatCriteria().build());
        }
        Long count = 0L;
        count += getRoleOs(userId);
        count += getGroupOs(userId);
        count += getOrgOs("guobo");
        count += getOrgGroupOs("guobo");
        if (!org.springframework.util.StringUtils.isEmpty(user.get("DEPT_CODE"))) {
            String deptCode = String.valueOf(user.get("DEPT_CODE"));
            count += getOrgOs(deptCode);
            count += getOrgGroupOs(deptCode);
            if (!"guobo".equals(deptCode) && deptCode.length() > 4) {
                String parentCode = deptCode.substring(0, 4);
                count += getOrgOs(parentCode);
                count += getOrgGroupOs(parentCode);
            }
        }
        return count;
    }

    public Long getRoleOs(String userId) {
        return authDao.getRoleOs(userId);
    }

    public Long getGroupOs(String userId) {
        return authDao.getGroupOs(userId);
    }

    public Long getOrgOs(String orgCode) {
        return authDao.getOrgOs(orgCode);

    }

    public Long getOrgGroupOs(String orgCode) {
        return authDao.getOrgGroupOs(orgCode);
    }


    /**
     * 获取路由名称
     *
     * @param menu 菜单信息
     * @return 路由名称
     */
    public String getRouteName(OsSysMenu menu) {
        String routerName = StringUtils.capitalize(menu.getPath());
        // 非外链并且是一级目录（类型为目录）
        if (isMeunFrame(menu)) {
            routerName = StringUtils.EMPTY;
        }
        return routerName;
    }

    /**
     * 获取路由地址
     *
     * @param menu 菜单信息
     * @return 路由地址
     */
    public String getRouterPath(OsSysMenu menu) {
        String routerPath = menu.getPath();
        // 非外链并且是一级目录（类型为目录）
        if (0 == menu.getParentId().intValue() && AuthConstants.TYPE_DIR.equals(menu.getMenuType())
                && AuthConstants.NO_FRAME.equals(menu.getIsFrame())) {
            routerPath = "/" + menu.getPath();
        }
        // 非外链并且是一级目录（类型为菜单）
        else if (isMeunFrame(menu)) {
            routerPath = "/";
        }
        return routerPath;
    }

    /**
     * 获取组件信息
     *
     * @param menu 菜单信息
     * @return 组件信息
     */
    public String getComponent(OsSysMenu menu) {
        String component = AuthConstants.LAYOUT;
        if (StringUtils.isNotEmpty(menu.getComponent()) && !isMeunFrame(menu)) {
            component = menu.getComponent();
        }
        if (StringUtils.isEmpty(menu.getComponent()) && isNotParentMenuFrame(menu)) {
            component = AuthConstants.UN_LAYOUT;
        }
        return component;
    }

    /**
     * 是否为菜单内部跳转
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isMeunFrame(OsSysMenu menu) {
        return menu.getParentId().intValue() == 0 && AuthConstants.TYPE_MENU.equals(menu.getMenuType())
                && menu.getIsFrame().equals(AuthConstants.NO_FRAME);
    }

    public boolean isNotParentMenuFrame(OsSysMenu menu) {
        return menu.getParentId().intValue() != 0 && AuthConstants.TYPE_DIR.equals(menu.getMenuType())
                && menu.getIsFrame().equals(AuthConstants.NO_FRAME);
    }
}
