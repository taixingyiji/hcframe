package com.taixingyiji.base.module.data.module;

import com.taixingyiji.base.common.config.FrameConfig;
import com.taixingyiji.base.module.data.dao.TableMapper;
import com.taixingyiji.base.module.data.exception.BaseMapperException;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.transaction.SpringManagedTransactionFactory;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.transaction.support.TransactionTemplate;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BaseMapperRawSqlTest {

    private SingleConnectionDataSource dataSource;
    private BaseMapper baseMapper;

    @BeforeEach
    void setUp() throws Exception {
        dataSource = new SingleConnectionDataSource("jdbc:sqlite::memory:", true);
        Configuration configuration = new Configuration(new Environment("test",
                new SpringManagedTransactionFactory(), dataSource));
        String resource = "mapping/data/TableMapper.xml";
        try (InputStream input = Resources.getResourceAsStream(resource)) {
            new XMLMapperBuilder(input, configuration, resource, configuration.getSqlFragments()).parse();
        }
        SqlSessionTemplate session = new SqlSessionTemplate(new SqlSessionFactoryBuilder().build(configuration));
        FrameConfig config = new FrameConfig();
        config.setMultiDataSource(false);
        baseMapper = new BaseMapperImpl(session.getMapper(TableMapper.class), session, null, config, null);
        baseMapper.executeSql("CREATE TABLE raw_sql_test (id INTEGER PRIMARY KEY, name TEXT)");
    }

    @AfterEach
    void tearDown() {
        if (dataSource != null) {
            dataSource.destroy();
        }
    }

    @Test
    void bindsValuesAndSupportsImmutableParameters() {
        String name = "O'Reilly'); DROP TABLE raw_sql_test; --";
        Map<String, Object> params = Map.of("id", 1, "name", name);
        assertEquals(1, baseMapper.insertSql(
                "INSERT INTO raw_sql_test (id, name) VALUES (#{id}, #{name})", params));
        assertFalse(params.containsKey("sql"));
        assertEquals(name, baseMapper.selectOneSql("SELECT name FROM raw_sql_test WHERE id = 1").get("name"));
        assertEquals(1L, baseMapper.countSql("SELECT COUNT(*) FROM raw_sql_test WHERE name = #{name}", params));
        assertEquals(1, baseMapper.updateSql("UPDATE raw_sql_test SET name = #{name} WHERE id = #{id}",
                Map.of("id", 1, "name", "updated")));
        assertEquals(0, baseMapper.updateSql("UPDATE raw_sql_test SET name = #{name} WHERE id = #{id}",
                Map.of("id", 2, "name", "missing")));
        assertEquals(1, baseMapper.deleteSql("DELETE FROM raw_sql_test WHERE id = #{id}", Map.of("id", 1)));
        assertEquals(0L, baseMapper.countSql("SELECT COUNT(*) FROM raw_sql_test"));
    }

    @Test
    void supportsNoParametersAndCompatibilitySpelling() {
        assertEquals(1, baseMapper.insertSql("INSERT INTO raw_sql_test VALUES (1, 'first')"));
        assertEquals(1, baseMapper.updateSql("UPDATE raw_sql_test SET name = 'second' WHERE id = 1"));
        assertEquals(1, baseMapper.executeSql("UPDATE raw_sql_test SET name = #{name} WHERE id = 1",
                Map.of("name", "third")));
        assertEquals(1, baseMapper.excuteSql("INSERT INTO raw_sql_test VALUES (2, 'fourth')"));
        assertEquals(1, baseMapper.excuteSql("DELETE FROM raw_sql_test WHERE id = #{id}", Map.of("id", 2)));
        assertEquals(1, baseMapper.deleteSql("DELETE FROM raw_sql_test WHERE id = 1"));
        assertEquals(0, baseMapper.deleteSql("DELETE FROM raw_sql_test", null));
        assertEquals(0L, baseMapper.countSql("SELECT COUNT(*) FROM raw_sql_test", null));
        baseMapper.executeSql("DROP TABLE raw_sql_test", null);
    }

    @Test
    void preservesMutableParametersAndBindsNull() {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("id", 1);
        params.put("name", null);
        Map<String, Object> original = new LinkedHashMap<>(params);
        assertEquals(1, baseMapper.insertSql(
                "INSERT INTO raw_sql_test VALUES (#{id}, #{name,jdbcType=VARCHAR})", params));
        assertEquals(original, params);
        assertEquals(1L, baseMapper.countSql("SELECT COUNT(*) FROM raw_sql_test WHERE name IS NULL"));
    }

    @Test
    void participatesInSpringTransactionsAndInvalidatesCachedQueries() {
        TransactionTemplate transaction = new TransactionTemplate(new DataSourceTransactionManager(dataSource));
        transaction.executeWithoutResult(status -> {
            assertEquals(0L, baseMapper.countSql("SELECT COUNT(*) FROM raw_sql_test"));
            baseMapper.insertSql("INSERT INTO raw_sql_test VALUES (1, 'before')");
            assertEquals(1L, baseMapper.countSql("SELECT COUNT(*) FROM raw_sql_test"));
            assertEquals("before", baseMapper.selectOneSql("SELECT name FROM raw_sql_test").get("name"));
            baseMapper.updateSql("UPDATE raw_sql_test SET name = 'after'");
            assertEquals("after", baseMapper.selectOneSql("SELECT name FROM raw_sql_test").get("name"));
            baseMapper.deleteSql("DELETE FROM raw_sql_test");
            assertEquals(0L, baseMapper.countSql("SELECT COUNT(*) FROM raw_sql_test"));
            baseMapper.executeSql("INSERT INTO raw_sql_test VALUES (2, 'rollback')");
            assertEquals(1L, baseMapper.countSql("SELECT COUNT(*) FROM raw_sql_test"));
            status.setRollbackOnly();
        });
        assertEquals(0L, baseMapper.countSql("SELECT COUNT(*) FROM raw_sql_test"));
    }

    @Test
    void rejectsBlankSql() {
        for (String sql : new String[]{null, "", " \n\t"}) {
            assertThrows(BaseMapperException.class, () -> baseMapper.executeSql(sql));
            assertThrows(BaseMapperException.class, () -> baseMapper.excuteSql(sql));
            assertThrows(BaseMapperException.class, () -> baseMapper.insertSql(sql));
            assertThrows(BaseMapperException.class, () -> baseMapper.updateSql(sql));
            assertThrows(BaseMapperException.class, () -> baseMapper.deleteSql(sql));
            assertThrows(BaseMapperException.class, () -> baseMapper.countSql(sql));
        }
    }
}
