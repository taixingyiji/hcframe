package com.taixingyiji.user.module.manage.service.impl;

import com.taixingyiji.base.module.data.module.BaseMapper;
import com.taixingyiji.base.module.data.module.BaseMapperImpl;
import com.taixingyiji.user.module.manage.service.ManageDataService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;


/**
 * @author lhc
 * @date 2021-02-05
 */
@Service
public class ManageServiceImpl implements ManageDataService {


    final BaseMapper baseMapper;

    public ManageServiceImpl(@Qualifier(BaseMapperImpl.BASE) BaseMapper baseMapper) {
        this.baseMapper = baseMapper;
    }

    public static void main(String[] args) {
        System.out.println(URLEncoder.encode("http://192.168.1.130:9527/#/login"));
    }
}
