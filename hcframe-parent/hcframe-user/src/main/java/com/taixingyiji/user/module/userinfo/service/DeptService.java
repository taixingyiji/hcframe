package com.taixingyiji.user.module.userinfo.service;

import com.github.pagehelper.PageInfo;
import com.taixingyiji.base.common.ResultVO;
import com.taixingyiji.base.common.WebPageInfo;

import java.util.List;
import java.util.Map;

public interface DeptService {
    ResultVO<Object> addDept(Map<String, Object> org);

    ResultVO<Integer> updateDept(Map<String, Object> org, Integer version);

    ResultVO<Object> deleteDept(String ids);

    ResultVO<PageInfo<Map<String, Object>>> getDeptList(String data, WebPageInfo webPageInfo);

    ResultVO<List<Map<String, Object>>> getDeptTree();
}
