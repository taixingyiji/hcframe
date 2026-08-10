package com.taixingyiji.base.module.data.module;

import com.taixingyiji.base.module.datasource.dynamic.DBContextHolder;
import com.taixingyiji.base.module.datasource.utils.DataSourceUtil;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SqlIdentifierQuoterTest {

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
    void usesIdentifierRulesFromCurrentDynamicDataSource() throws Exception {
        assertQuoteFromMetadata("postgres-test", "\"", true, false,
                "\"create_time\"", "\"CREATE_TIME\"");
        assertQuoteFromMetadata("dameng-test", "\"", false, true,
                "\"CREATE_TIME\"", "\"CREATE_TIME\"");
        assertQuoteFromMetadata("mysql-test", "`", false, false,
                "`CREATE_TIME`", "`CREATE_TIME`");
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
        DataSource dataSource = mock(DataSource.class);
        Connection connection = mock(Connection.class);
        DatabaseMetaData metadata = mock(DatabaseMetaData.class);
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.getMetaData()).thenReturn(metadata);
        when(metadata.getIdentifierQuoteString()).thenReturn(quote);
        when(metadata.storesLowerCaseIdentifiers()).thenReturn(storesLowerCaseIdentifiers);
        when(metadata.storesUpperCaseIdentifiers()).thenReturn(storesUpperCaseIdentifiers);
        DataSourceUtil.dataSourceMap.put(dataSourceKey, dataSource);

        try {
            DBContextHolder.setDataSource(dataSourceKey);
            assertEquals(expectedUnquoted, SqlIdentifierQuoter.quote("CREATE_TIME"));
            assertEquals(expectedExplicitlyQuoted, SqlIdentifierQuoter.quote("\"CREATE_TIME\""));
        } finally {
            DBContextHolder.clearDataSource();
            DataSourceUtil.dataSourceMap.remove(dataSourceKey);
        }
    }
}
