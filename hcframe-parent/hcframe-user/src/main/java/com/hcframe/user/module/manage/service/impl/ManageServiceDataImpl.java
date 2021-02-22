package com.hcframe.user.module.manage.service.impl;

import com.hcframe.base.module.data.module.BaseMapper;
import com.hcframe.base.module.data.module.BaseMapperImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import com.hcframe.user.module.manage.service.ManageService;

/**
 * @author lhc
 * @date 2021-02-05
 */
@Service
public class ManageServiceDataImpl implements ManageService {

    final BaseMapper baseMapper;


    public ManageServiceDataImpl(@Qualifier(BaseMapperImpl.BASE) BaseMapper baseMapper) {
        this.baseMapper = baseMapper;
    }

}
