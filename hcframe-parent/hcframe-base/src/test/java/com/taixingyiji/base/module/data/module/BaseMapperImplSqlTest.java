package com.taixingyiji.base.module.data.module;

import com.alibaba.druid.pool.DruidDataSource;
import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import com.taixingyiji.base.common.WebPageInfo;
import com.taixingyiji.base.common.config.FrameConfig;
import com.taixingyiji.base.module.data.dao.TableMapper;
import com.taixingyiji.base.module.datasource.dynamic.DBContextHolder;
import com.taixingyiji.base.module.datasource.utils.DataSourceUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mybatis.spring.SqlSessionTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BaseMapperImplSqlTest {

    private static final String DATA_SOURCE_KEY = "base-mapper-sql-test";

    private SqlSessionTemplate sqlSessionTemplate;
    private BaseMapperImpl baseMapper;

    @BeforeEach
    void setUp() throws Exception {
        DataSource dataSource = mock(DataSource.class);
        Connection connection = mock(Connection.class);
        DatabaseMetaData metadata = mock(DatabaseMetaData.class);
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.getMetaData()).thenReturn(metadata);
        when(metadata.getIdentifierQuoteString()).thenReturn("\"");
        DataSourceUtil.dataSourceMap.put(DATA_SOURCE_KEY, dataSource);
        DBContextHolder.setDataSource(DATA_SOURCE_KEY);

        sqlSessionTemplate = mock(SqlSessionTemplate.class);
        FrameConfig frameConfig = mock(FrameConfig.class);
        baseMapper = new BaseMapperImpl(mock(TableMapper.class), sqlSessionTemplate,
                mock(DruidDataSource.class), frameConfig, null);
    }

    @AfterEach
    void tearDown() {
        DBContextHolder.clearDataSource();
        DataSourceUtil.dataSourceMap.remove(DATA_SOURCE_KEY);
    }

    @Test
    void updateBatchQuotesEveryIdentifier() {
        Map<String, Object> row = new LinkedHashMap<>();
        row.put("ID", 1);
        row.put("ORDER", 2);
        row.put("VALUE", "test");
        when(sqlSessionTemplate.update(eq(BaseMapperImpl.TABLE_MAPPER_PACKAGE + "updateBatchByPk"), anyMap()))
                .thenReturn(1);

        assertEquals(1, baseMapper.updateBatchByPk("GROUP", "ID", List.of(row)));

        @SuppressWarnings("rawtypes")
        ArgumentCaptor<Map> captor = ArgumentCaptor.forClass(Map.class);
        verify(sqlSessionTemplate).update(
                eq(BaseMapperImpl.TABLE_MAPPER_PACKAGE + "updateBatchByPk"), captor.capture());
        String sql = String.valueOf(captor.getValue().get("sql"));
        assertTrue(sql.startsWith("UPDATE \"GROUP\" SET \"ORDER\" = #{item0_field0}"));
        assertTrue(sql.contains("\"VALUE\" = #{item0_field1}"));
        assertTrue(sql.endsWith("WHERE \"ID\" = #{item0_pk};"));
        assertFalse(sql.contains("item0_ORDER"));
    }

    @Test
    void startsPaginationForNonCachedTableConditionQuery() {
        when(sqlSessionTemplate.selectList(
                eq(BaseMapperImpl.TABLE_MAPPER_PACKAGE + "useSql"), anyMap()))
                .thenReturn(List.of());
        WebPageInfo pageInfo = WebPageInfo.builder()
                .pageNum(2)
                .pageSize(5)
                .sortField("ORDER")
                .order(WebPageInfo.ASC)
                .enableSort(true)
                .build();

        try {
            baseMapper.selectByCondition(
                    "GROUP", Condition.creatCriteria().build(), pageInfo);

            Page<?> page = PageMethod.getLocalPage();
            assertNotNull(page);
            assertEquals(2, page.getPageNum());
            assertEquals(5, page.getPageSize());
            assertEquals("\"ORDER\" asc", page.getOrderBy());
        } finally {
            PageMethod.clearPage();
        }
    }
}
