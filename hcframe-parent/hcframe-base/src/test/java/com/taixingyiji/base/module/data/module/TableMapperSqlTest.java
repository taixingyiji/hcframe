package com.taixingyiji.base.module.data.module;

import com.taixingyiji.base.module.datasource.dynamic.DBContextHolder;
import com.taixingyiji.base.module.datasource.utils.DataSourceUtil;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.session.Configuration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TableMapperSqlTest {

    private static final String DATA_SOURCE_KEY = "table-mapper-sql-test";
    private static final String NAMESPACE = "com.taixingyiji.base.module.data.dao.TableMapper.";

    private Configuration configuration;

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

        configuration = new Configuration();
        String resource = "mapping/data/TableMapper.xml";
        try (InputStream input = Resources.getResourceAsStream(resource)) {
            new XMLMapperBuilder(input, configuration, resource, configuration.getSqlFragments()).parse();
        }
    }

    @AfterEach
    void tearDown() {
        DBContextHolder.clearDataSource();
        DataSourceUtil.dataSourceMap.remove(DATA_SOURCE_KEY);
    }

    @Test
    void quotesInsertTableAndColumns() {
        Map<String, Object> info = new LinkedHashMap<>();
        info.put("ORDER", 1);
        info.put("VALUE", "test");

        String sql = render("saveInfoWithNull", params("GROUP", info));

        assertTrue(sql.startsWith("INSERT INTO \"GROUP\""));
        assertTrue(sql.contains("( \"ORDER\" , \"VALUE\" )"));
        assertFalse(sql.contains("#{"));
    }

    @Test
    void quotesUpdateTableAndSetColumns() {
        Map<String, Object> params = params("GROUP", Map.of("ORDER", 2));
        params.put("sql", "WHERE \"ID\" = #{id}");
        params.put("id", 10);

        String sql = render("updateByWhere", params);

        assertTrue(sql.startsWith("update \"GROUP\" SET \"ORDER\" = ?"));
        assertTrue(sql.endsWith("WHERE \"ID\" = ?"));
    }

    @Test
    void quotesBatchInsertColumns() {
        Map<String, Object> row = new LinkedHashMap<>();
        row.put("ORDER", 1);
        row.put("VALUE", "test");
        Map<String, Object> params = new HashMap<>();
        params.put("tableName", "GROUP");
        params.put("list", List.of(row));

        String sql = render("insertBatch", params);

        assertTrue(sql.startsWith("INSERT INTO \"GROUP\""));
        assertTrue(sql.contains("( \"ORDER\" , \"VALUE\" )"));
    }

    @Test
    void bindsDeleteValuesInsteadOfInjectingThem() {
        Map<String, Object> params = new HashMap<>();
        params.put("tableName", "GROUP");
        params.put("pkName", "ORDER");
        params.put("ids", new Object[]{1L, 2L});

        BoundSql boundSql = boundSql("deleteByPrimary", params);
        String sql = normalize(boundSql.getSql());

        assertTrue(sql.startsWith("DELETE FROM \"GROUP\" WHERE \"ORDER\" in"));
        assertTrue(sql.endsWith("( ? , ? )"));
        assertEquals(List.of(1L, 2L), parameterValues(boundSql));
    }

    @Test
    void bindsNullAndNonNullMapValuesInWithNullUpdates() {
        Map<String, Object> info = new LinkedHashMap<>();
        info.put("id", 51L);
        info.put("ORDER", "changed");
        info.put("VALUE", null);

        BoundSql byId = boundSql("updateInfoWithNull", params("GROUP", info));
        assertEquals(Arrays.asList("changed", null, 51L), parameterValues(byId));

        Map<String, Object> byWhereParams = params("GROUP", info);
        byWhereParams.put("sql", "\"KEY\" = 151");
        BoundSql byWhere = boundSql("updateByWhereWithNull", byWhereParams);
        assertEquals(Arrays.asList("changed", null), parameterValues(byWhere));
    }

    @Test
    void quotesSequenceTableAndPrimaryKeyIdentifiers() {
        Map<String, Object> params = new HashMap<>();
        params.put("tableName", "GROUP");
        params.put("pkName", "ORDER");

        String nextValueSql = render("getSequence", params);
        String createSql = render("createSequenceSafeDm", params);

        assertTrue(nextValueSql.contains("\"GROUP_seq\".nextval"));
        assertTrue(createSql.contains("MAX(\"ORDER\")"));
        assertTrue(createSql.contains("FROM \"GROUP\""));
        assertTrue(createSql.contains("CREATE SEQUENCE"));
    }

    private Map<String, Object> params(String tableName, Map<String, Object> info) {
        Map<String, Object> params = new HashMap<>();
        params.put("tableName", tableName);
        params.put("info", info);
        return params;
    }

    private String render(String statement, Map<String, Object> params) {
        return normalize(boundSql(statement, params).getSql());
    }

    private BoundSql boundSql(String statement, Map<String, Object> params) {
        return configuration.getMappedStatement(NAMESPACE + statement).getBoundSql(params);
    }

    private String normalize(String sql) {
        return sql.replaceAll("\\s+", " ").trim();
    }

    private List<Object> parameterValues(BoundSql boundSql) {
        return boundSql.getParameterMappings().stream()
                .map(mapping -> boundSql.hasAdditionalParameter(mapping.getProperty())
                        ? boundSql.getAdditionalParameter(mapping.getProperty())
                        : configuration.newMetaObject(boundSql.getParameterObject())
                        .getValue(mapping.getProperty()))
                .toList();
    }
}
