package com.taixingyiji.base.module.data.module;

import com.taixingyiji.base.common.config.FrameConfig;
import com.taixingyiji.base.common.utils.SpringContextUtil;
import com.taixingyiji.base.module.datasource.dynamic.DBContextHolder;
import com.taixingyiji.base.module.datasource.utils.DataSourceUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.StaticApplicationContext;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SqlIdentifierQuoterTest {

    private ApplicationContext originalApplicationContext;

    @BeforeEach
    void rememberApplicationContext() {
        originalApplicationContext = SpringContextUtil.getApplicationContext();
    }

    @AfterEach
    void restoreApplicationContext() {
        new SpringContextUtil().setApplicationContext(originalApplicationContext);
        DBContextHolder.clearDataSource();
    }

    @Test
    void quotesAnsiAndQualifiedIdentifiers() {
        assertEquals("\"ORDER\"", SqlIdentifierQuoter.quote("ORDER", "\""));
        assertEquals("\"OS_SYS_MENU\".\"ORDER\"",
                SqlIdentifierQuoter.quote("OS_SYS_MENU.ORDER", "\""));
    }

    @Test
    void quotesMysqlIdentifiersAndNormalizesExistingQuotes() {
        assertEquals("`ORDER`", SqlIdentifierQuoter.quote("ORDER", "`"));
        assertEquals("`OS_SYS_MENU`.`ORDER`",
                SqlIdentifierQuoter.quote("OS_SYS_MENU.\"ORDER\"", "`"));
    }

    @Test
    void quotesSelectFieldsAndPreservesExplicitExpressions() {
        assertEquals("\"T\".\"ORDER\"", SqlIdentifierQuoter.quoteField("T.ORDER", "\""));
        assertEquals("count(\"T\".\"ORDER\")", SqlIdentifierQuoter.quoteField("count(T.ORDER)", "\""));
        assertEquals("CASE WHEN T.VALUE > 0 THEN 1 END",
                SqlIdentifierQuoter.quoteField("CASE WHEN T.VALUE > 0 THEN 1 END", "\""));
    }

    @Test
    void selectBuilderQuotesTablesFieldsAndJoinColumns() {
        SelectCondition select = SelectCondition.sqlJoinBuilder("GROUP")
                .field(List.of("GROUP.ORDER", "count(GROUP.VALUE)"))
                .join("USER")
                .on("KEY", "GROUP", "KEY")
                .build();

        String sql = select.getSql();
        String quote = sql.contains("`GROUP`") ? "`" : "\"";
        assertTrue(sql.contains(quote + "GROUP" + quote + "." + quote + "ORDER" + quote));
        assertTrue(sql.contains("count(" + quote + "GROUP" + quote + "." + quote + "VALUE" + quote + ")"));
        assertTrue(sql.contains("JOIN " + quote + "USER" + quote));
        assertTrue(sql.contains(quote + "USER" + quote + "." + quote + "KEY" + quote
                + "=" + quote + "GROUP" + quote + "." + quote + "KEY" + quote));
    }

    @Test
    void usesIdentifierRulesFromCurrentDynamicDataSource() throws Exception {
        assertQuoteFromMetadata("postgres-test", "\"", true, false,
                "\"create_time\"", "\"CREATE_TIME\"");
        assertQuoteFromMetadata("dameng-test", "\"", false, true,
                "\"CREATE_TIME\"", "\"CREATE_TIME\"");
        assertQuoteFromMetadata("mysql-test", "`", false, false,
                "`CREATE_TIME`", "`CREATE_TIME`");
    }

    @Test
    void usesApplicationDataSourceWhenMultiDataSourceIsDisabled() throws Exception {
        DataSource dataSource = mockDataSource("\"", true, false);
        DataSource ignoredDynamicDataSource = mockDataSource("\"", false, true);
        installApplicationContext(false, dataSource);
        DataSourceUtil.dataSourceMap.put("ignored-dynamic-test", ignoredDynamicDataSource);

        try {
            DBContextHolder.setDataSource("ignored-dynamic-test");
            assertEquals("\"tb_args\"", SqlIdentifierQuoter.quote("TB_ARGS"));
            assertTrue(Condition.creatCriteria()
                    .andEqual("ARGS_NAME", "test")
                    .build()
                    .getSql()
                    .contains("\"args_name\"="));
        } finally {
            DataSourceUtil.dataSourceMap.remove("ignored-dynamic-test");
        }
    }

    @Test
    void conditionBuilderQuotesKeysForEveryConditionShape() {
        Condition condition = Condition.creatCriteria()
                .andEqual("ORDER", 1)
                .andLike("T.KEY", "value")
                .andIn("GROUP", List.of(1, 2))
                .andBetween("RANGE", 1, 2)
                .andLt("LIMIT", 10)
                .andNotEqual("USER", "test")
                .andGt("VALUE", 0)
                .andLte("FROM", 10)
                .andGte("TO", 1)
                .groupBy("GROUP")
                .build();

        String sql = condition.getSql();
        String quote = sql.contains("`ORDER`") ? "`" : "\"";
        assertTrue(sql.contains(quote + "ORDER" + quote + "="));
        assertTrue(sql.contains(quote + "T" + quote + "." + quote + "KEY" + quote + " LIKE"));
        assertTrue(sql.contains(quote + "GROUP" + quote + " IN"));
        assertTrue(sql.contains("GROUP BY " + quote + "GROUP" + quote));
    }

    private void assertQuoteFromMetadata(String dataSourceKey, String quote,
                                         boolean storesLowerCaseIdentifiers,
                                         boolean storesUpperCaseIdentifiers,
                                         String expectedUnquoted,
                                         String expectedExplicitlyQuoted) throws Exception {
        DataSource dataSource = mockDataSource(quote, storesLowerCaseIdentifiers, storesUpperCaseIdentifiers);
        DataSourceUtil.dataSourceMap.put(dataSourceKey, dataSource);

        try {
            DBContextHolder.setDataSource(dataSourceKey);
            assertEquals(expectedUnquoted, SqlIdentifierQuoter.quote("CREATE_TIME"));
            assertEquals(expectedExplicitlyQuoted, SqlIdentifierQuoter.quote("\"CREATE_TIME\""));
            String expectedSequenceName = storesLowerCaseIdentifiers ? "create_time_seq"
                    : storesUpperCaseIdentifiers ? "CREATE_TIME_SEQ" : "CREATE_TIME_seq";
            assertEquals(expectedSequenceName,
                    SqlIdentifierQuoter.normalizeWithSuffix("CREATE_TIME", "_seq"));
        } finally {
            DBContextHolder.clearDataSource();
            DataSourceUtil.dataSourceMap.remove(dataSourceKey);
        }
    }

    private DataSource mockDataSource(String quote, boolean storesLowerCaseIdentifiers,
                                      boolean storesUpperCaseIdentifiers) throws Exception {
        DataSource dataSource = mock(DataSource.class);
        Connection connection = mock(Connection.class);
        DatabaseMetaData metadata = mock(DatabaseMetaData.class);
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.getMetaData()).thenReturn(metadata);
        when(metadata.getIdentifierQuoteString()).thenReturn(quote);
        when(metadata.storesLowerCaseIdentifiers()).thenReturn(storesLowerCaseIdentifiers);
        when(metadata.storesUpperCaseIdentifiers()).thenReturn(storesUpperCaseIdentifiers);
        return dataSource;
    }

    private void installApplicationContext(boolean multiDataSource, DataSource dataSource) {
        FrameConfig frameConfig = new FrameConfig();
        frameConfig.setMultiDataSource(multiDataSource);
        StaticApplicationContext applicationContext = new StaticApplicationContext();
        applicationContext.getBeanFactory().registerSingleton("frameConfig", frameConfig);
        applicationContext.getBeanFactory().registerSingleton("druidDataSource", dataSource);
        new SpringContextUtil().setApplicationContext(applicationContext);
    }
}
