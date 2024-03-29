package com.taixingyiji.user.common.config;

import com.taixingyiji.base.module.cache.CacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * @author lhc
 * @date 2020-10-09
 * @description springboot启动执行配置类
 */
@Component
public class CommandLineRunnerImpl implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(CommandLineRunnerImpl.class);

    @Autowired
    List<CacheService> cacheServices;

    @Override
    public void run(String... args) {
//        DataSourceUtil.initDataSource();
        // 初始化缓存
//        for (CacheService cacheService : cacheServices) {
//            cacheService.initTableCache();
//        }
        logger.info("start success");
    }
}
