package com.taixingyiji.base.common.utils;

import com.taixingyiji.base.common.SortItem;
import com.taixingyiji.base.module.datasource.dynamic.DBContextHolder;
import com.taixingyiji.base.module.datasource.utils.DataSourceUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MyPageHelperTest {

    private static final String DATA_SOURCE_KEY = "page-helper-test";

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
    }

    @AfterEach
    void tearDown() {
        DBContextHolder.clearDataSource();
        DataSourceUtil.dataSourceMap.remove(DATA_SOURCE_KEY);
    }

    @Test
    void quotesReservedSortFields() {
        List<SortItem> sortItems = List.of(
                SortItem.builder().field("ORDER").order("desc").build(),
                SortItem.builder().field("GROUP").order("asc").build()
        );

        assertEquals("\"ORDER\" desc, \"GROUP\" asc", MyPageHelper.buildOrderBy(sortItems, Set.of()));
    }
}
