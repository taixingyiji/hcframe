package com.taixingyiji.user.module.userinfo.service;

import com.github.pagehelper.PageInfo;
import com.hcframe.base.common.ResultVO;
import com.hcframe.base.common.WebPageInfo;

import java.util.List;
import java.util.Map;

public interface OrgService {
    ResultVO<Object> addOrg(Map<String, Object> org);

    ResultVO<Integer> updateOrg(Map<String, Object> org, Integer version);

    ResultVO<Object> deleteOrg(String ids);

    ResultVO<PageInfo<Map<String, Object>>> getOrgList(String data, WebPageInfo webPageInfo, String parentId);

    List<Map<String,Object>> getOrgTree();

    ResultVO<Object> getFormat();

    List<Long> getOrgChildIdList(Long parentId);
}
