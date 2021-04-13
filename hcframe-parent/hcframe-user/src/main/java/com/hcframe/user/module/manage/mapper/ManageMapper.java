package com.hcframe.user.module.manage.mapper;

import java.util.List;
import java.util.Map;

/**
 * @author lhc
 * @version 1.0
 * @className ManageMapper
 * @date 2021年04月06日 4:26 下午
 * @description 描述
 */
public interface ManageMapper {
    List<Map<String, Object>> selectPersonList(String name, String department);
}
