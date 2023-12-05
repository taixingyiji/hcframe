package com.taixingyiji.user.module.manage.mapper.impl;

import com.taixingyiji.base.module.data.module.BaseMapper;
import com.taixingyiji.base.module.data.module.BaseMapperImpl;
import com.taixingyiji.user.module.manage.mapper.ManageMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author lhc
 * @version 1.0
 * @className ManageMapperImpl
 * @date 2021年04月06日 4:27 下午
 * @description 描述
 */
@Component
public class ManageMapperImpl implements ManageMapper {

    final BaseMapper baseMapper;

    public ManageMapperImpl(@Qualifier(BaseMapperImpl.BASE) BaseMapper baseMapper){
        this.baseMapper = baseMapper;
    }

    @Override
    public List<Map<String, Object>> selectPersonList(String name, String department) {
        String sql = "SELECT GB_CAS_MEMBER.ID,GB_CAS_MEMBER.NAME,GB_CAS_DEPT.NAME \n" +
                "FROM GBCAS.GB_CAS_MEMBER \n" +
                "LEFT JOIN GBCAS.GB_CAS_DEPT ON GB_CAS_MEMBER.DEPT_CODE = GB_CAS_DEPT.CODE\n" +
                "WHERE GB_CAS_MEMBER.NAME = '" + name + "' AND GB_CAS_DEPT.NAME = '"+department+"' AND GB_CAS_MEMBER.DELETED = 1";
        return baseMapper.selectSql(sql);
    }
}
