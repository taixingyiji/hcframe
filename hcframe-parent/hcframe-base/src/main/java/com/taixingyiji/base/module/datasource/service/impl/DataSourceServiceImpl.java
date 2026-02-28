package com.taixingyiji.base.module.datasource.service.impl;

import com.alibaba.druid.pool.DruidDataSource;
import com.taixingyiji.base.common.ResultVO;
import com.taixingyiji.base.common.ServiceException;
import com.taixingyiji.base.common.WebPageInfo;
import com.taixingyiji.base.common.utils.MyPageHelper;
import com.taixingyiji.base.module.data.dao.TableMapper;
import com.taixingyiji.base.module.datasource.dao.DatasourceConfigDao;
import com.taixingyiji.base.module.datasource.dao.DatasourceTokenDao;
import com.taixingyiji.base.module.datasource.dao.DatasourceTypeDao;
import com.taixingyiji.base.module.datasource.dynamic.DBContextHolder;
import com.taixingyiji.base.module.datasource.entity.DatasourceConfig;
import com.taixingyiji.base.module.datasource.entity.DatasourceToken;
import com.taixingyiji.base.module.datasource.entity.DatasourceType;
import com.taixingyiji.base.module.datasource.service.DataSourceService;
import com.taixingyiji.base.module.datasource.utils.DataSourceUtil;
import com.taixingyiji.base.module.datasource.utils.DataUnit;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author lhc
 * @date 2020-09-23
 */
@Service
public class DataSourceServiceImpl implements DataSourceService {

    @Autowired
    DatasourceConfigDao datasourceConfigDao;

    @Autowired
    DatasourceTypeDao datasourceTypeDao;

    @Autowired
    DatasourceTokenDao datasourceTokenDao;

    @Autowired
    TableMapper tableMapper;

    @Override
    public PageInfo<DatasourceConfig> list(WebPageInfo webPageInfo, DatasourceConfig datasourceConfig) {
        MyPageHelper.start(webPageInfo);
        Example example = new Example(DatasourceConfig.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(datasourceConfig.getCommonAlias())) {
            criteria = criteria.andLike("commonAlias", "%" + datasourceConfig.getCommonAlias() + "%");
        }
        if (StringUtils.isNotBlank(datasourceConfig.getCommonType())) {
            criteria = criteria.andEqualTo("commonType", datasourceConfig.getCommonType());
        }
        if (StringUtils.isNotBlank(datasourceConfig.getSysDescription())) {
            criteria.andEqualTo("sysDescription", datasourceConfig.getSysDescription());
        }
        List<DatasourceConfig> list = datasourceConfigDao.selectByExample(example);
        for (DatasourceConfig datasourceConfig1 : list) {
            datasourceConfig1.setPassword(DataUnit.PASSWORD);
        }
        return new PageInfo<>(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultVO save(DatasourceConfig datasourceConfig) {
        datasourceConfig.setUpdateTime(new Date());
        datasourceConfig.setCreateTime(new Date());
        boolean flag = false;
        if (datasourceConfig.getIsDefault() == DatasourceConfig.DEFAULT) {
            datasourceConfig.setIsDefault(DatasourceConfig.UNDEFAULT);
            flag = true;
        }
        int i = datasourceConfigDao.insertSelective(datasourceConfig);
        if (flag) {
            setDefault(datasourceConfig.getDatasourceId());
        }
        if (datasourceConfig.getSysEnabled() == DatasourceConfig.ENABLE) {
            DruidDataSource druidDataSource = DataSourceUtil.initDruid(datasourceConfig.getCommonType());
            BeanUtils.copyProperties(datasourceConfig, druidDataSource);
            DataSourceUtil.addMapData(datasourceConfig.getCommonAlias(), druidDataSource, datasourceConfig);
            if (datasourceConfig.getIsDefault() == DatasourceConfig.DEFAULT) {
                DataSourceUtil.addMapData(DataUnit.MASTER, druidDataSource, datasourceConfig);
            }
        }
        DataSourceUtil.flushDataSource();
        if (i < 1) {
            throw new ServiceException("新增数据源失败");
        }
        return ResultVO.getSuccess();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultVO update(DatasourceConfig datasourceConfig) {
        boolean flag = false;
        // 是否是master节点
        boolean masterFlag = false;
        DatasourceConfig config = datasourceConfigDao.selectByPrimaryKey(datasourceConfig.getDatasourceId());
        // 判断是否已经启用
        if (DataSourceUtil.dataSourceMap.containsKey(datasourceConfig.getCommonAlias())) {
            flag = true;
            // 启用状态移除数据源
            DataSourceUtil.removeSource(datasourceConfig.getCommonAlias());
            // 判断是否是master节点
            if (config.getIsDefault() == DatasourceConfig.DEFAULT) {
                // 若是移除数据源
                DataSourceUtil.removeSource(DataUnit.MASTER);
                masterFlag = true;
            }
        }
        // 设置更新时间
        datasourceConfig.setUpdateTime(new Date());
        if (DataUnit.PASSWORD.equals(datasourceConfig.getPassword())) {
            datasourceConfig.setPassword(null);
        }
        // 更新数据库
        int i = datasourceConfigDao.updateByPrimaryKeySelective(datasourceConfig);
        if (flag) {
            // 将更新后的数据加入
            DruidDataSource druidDataSource = DataSourceUtil.initDruid(datasourceConfig.getCommonType());
            BeanUtils.copyProperties(datasourceConfig, druidDataSource);
            DataSourceUtil.addMapData(datasourceConfig.getCommonAlias(), druidDataSource, datasourceConfig);
            if (masterFlag) {
                DataSourceUtil.addMapData(DataUnit.MASTER, druidDataSource, datasourceConfig);
            }
            DataSourceUtil.flushDataSource();
        }
        if (i < 1) {
            throw new ServiceException("更新数据源失败");
        }
        return ResultVO.getSuccess();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultVO delete(Integer id) {
        DatasourceConfig datasourceConfig = datasourceConfigDao.selectByPrimaryKey(id);
        if (datasourceConfig.getIsDefault() == DatasourceConfig.DEFAULT) {
            throw new ServiceException("不能删除默认数据库！");
        }
        if (datasourceConfig.getSysEnabled() == DatasourceConfig.ENABLE) {
            DataSourceUtil.removeSource(datasourceConfig.getCommonAlias());
            DataSourceUtil.flushDataSource();
        }
        int i = datasourceConfigDao.deleteByPrimaryKey(id);
        if (i < 1) {
            throw new ServiceException("删除数据失败");
        }
        return ResultVO.getSuccess();
    }

    @Override
    public ResultVO hasSource() {
        int i=datasourceConfigDao.getCount();
        if (i > 0) {
            return ResultVO.getSuccess();
        } else {
            return ResultVO.getFailed("");
        }
    }

    @Override
    public ResultVO getUnique(String key, String name) {
        DatasourceConfig datasourceConfig = new DatasourceConfig();
        boolean flag = true;
        if (StringUtils.isNotBlank(key)) {
             datasourceConfig.setCommonAlias(key);
        }
        if (StringUtils.isNotBlank(name)) {
            datasourceConfig.setSysDescription(name);
            flag = false;
        }
        if (DataUnit.SQLITE.equals(key)) {
            throw new ServiceException("key值不可叫"+DataUnit.SQLITE);
        }
        if (DataUnit.MASTER.equals(key)) {
            throw new ServiceException("key值不可叫" + DataUnit.MASTER);
        }
        int i = datasourceConfigDao.selectCount(datasourceConfig);
        String str = "数据名称重复！";
        if (flag) {
            str = "数据库KEY值重复！";
        }
        if (i > 0) {
            throw new ServiceException(str);
        }
        return ResultVO.getSuccess();
    }

    @Override
    public ResultVO test(DatasourceConfig datasourceConfig) {
        DatasourceConfig datasourceConfig1 = datasourceConfigDao.selectByPrimaryKey(datasourceConfig.getDatasourceId());
        if (datasourceConfig1 != null&&!DataUnit.PASSWORD.equals(datasourceConfig1.getPassword())) {
            datasourceConfig = datasourceConfig1;
        }
        DatasourceType datasourceType = datasourceTypeDao.selectOne(DatasourceType.builder().typeKey(datasourceConfig.getCommonType()).build());
        DruidDataSource druidDataSource = new DruidDataSource();
        BeanUtils.copyProperties(datasourceConfig, druidDataSource);
        druidDataSource.setBreakAfterAcquireFailure(true);
        druidDataSource.setConnectionErrorRetryAttempts(0);
        druidDataSource.setMaxWait(5000);
        long time = System.currentTimeMillis();
        String uuid = UUID.randomUUID().toString();
        String key = uuid + time;
        DataSourceUtil.addMapData(key, druidDataSource, datasourceConfig);
        DataSourceUtil.flushDataSource();
        DBContextHolder.clearDataSource();
        DBContextHolder.setDataSource(key);
        try {
            tableMapper.useSqlByTest(datasourceType.getValidateQuery());
        } catch (Exception e) {
            throw new ServiceException("数据库连接失败");
        }finally {
            DataSourceUtil.removeSource(key);
        }
        return ResultVO.getSuccess();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultVO enableDatasource(Integer id, Integer status) {
        DatasourceConfig datasourceConfig = datasourceConfigDao.selectOne(DatasourceConfig.builder().datasourceId(id).build());
        datasourceConfig.setSysEnabled(status);
        int i = datasourceConfigDao.updateByPrimaryKeySelective(datasourceConfig);
        if (i < 1) {
            throw new ServiceException("状态更新失败");
        }
        if (status == DatasourceConfig.ENABLE) {
            DruidDataSource druidDataSource = DataSourceUtil.initDruid(datasourceConfig.getCommonType());
            DataSourceUtil.addMapData(datasourceConfig.getCommonAlias(), druidDataSource, datasourceConfig);
        } else {
            DataSourceUtil.removeSource(datasourceConfig.getCommonAlias());
        }
        DataSourceUtil.flushDataSource();
        DBContextHolder.clearDataSource();
        return ResultVO.getSuccess();
    }

    @Override
    public ResultVO setDefault(Integer id) {
        Example example = new Example(DatasourceConfig.class);
        example.createCriteria().andEqualTo("isDefault", DatasourceConfig.DEFAULT);
        int count = datasourceConfigDao.selectCountByExample(example);
        int i;
        if (count > 0) {
             i =datasourceConfigDao.updateByExampleSelective(DatasourceConfig.builder().isDefault(DatasourceConfig.UNDEFAULT).build(), example);
            if (i < 1) {
                throw new ServiceException("设置失败");
            }
        }
        i = datasourceConfigDao.updateByPrimaryKeySelective(DatasourceConfig.builder().isDefault(DatasourceConfig.DEFAULT).sysEnabled(DatasourceConfig.ENABLE).datasourceId(id).build());
        if (i < 1) {
            throw new ServiceException("设置失败");
        }
        DatasourceConfig config = datasourceConfigDao.selectOne(DatasourceConfig.builder().datasourceId(id).build());
        DruidDataSource druidDataSource = DataSourceUtil.initDruid(config.getCommonType());
        BeanUtils.copyProperties(config, druidDataSource);
        DataSourceUtil.addMapData(DataUnit.MASTER, druidDataSource, config);
        DataSourceUtil.addMapData(config.getCommonAlias(),druidDataSource,config);
        DataSourceUtil.flushDataSource();
        return ResultVO.getSuccess();
    }

    @Override
    public ResultVO getAllList() {
        return ResultVO.getSuccess(datasourceConfigDao.select(DatasourceConfig.builder().sysEnabled(DatasourceConfig.ENABLE).build()));
    }

    @Override
    public ResultVO validateToken(String token) {
        int i = datasourceTokenDao.selectCount(DatasourceToken.builder().token(token).build());
        if (i > 0) {
            return ResultVO.getSuccess();
        } else {
            return ResultVO.getFailed("token不正确");
        }
    }
}
