package com.taixingyiji.base.module.data.module;

import com.alibaba.druid.pool.DruidDataSource;
import com.taixingyiji.base.common.utils.SpringContextUtil;
import com.taixingyiji.base.module.datasource.dynamic.DBContextHolder;
import com.taixingyiji.base.module.datasource.entity.DatasourceConfig;
import com.taixingyiji.base.module.datasource.utils.DataSourceUtil;
import com.taixingyiji.base.module.datasource.utils.DataUnit;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.StringJoiner;
import java.util.WeakHashMap;

public final class SqlIdentifierQuoter {

    private static final String ANSI_QUOTE = "\"";
    private static final Map<DataSource, IdentifierDialect> DIALECT_CACHE =
            Collections.synchronizedMap(new WeakHashMap<>());

    private SqlIdentifierQuoter() {
    }

    public static String quote(String identifier) {
        return quote(identifier, resolveDialect());
    }

    public static String quote(String identifier, String quoteString) {
        return quote(identifier, new IdentifierDialect(quoteString, IdentifierCase.PRESERVE));
    }

    public static String quoteWithSuffix(String identifier, String suffix) {
        validateSuffix(suffix);
        return quoteWithSuffix(identifier, suffix, resolveDialect());
    }

    public static String normalizeWithSuffix(String identifier, String suffix) {
        validateSuffix(suffix);
        return normalizeWithSuffix(identifier, suffix, resolveDialect());
    }

    public static String quoteField(String field) {
        return quoteField(field, resolveDialect());
    }

    public static String quoteField(String field, String quoteString) {
        return quoteField(field, new IdentifierDialect(quoteString, IdentifierCase.PRESERVE));
    }

    public static String quoteFieldList(String fields) {
        if (fields == null || fields.isBlank()) {
            throw new IllegalArgumentException("fields can not be blank");
        }
        IdentifierDialect dialect = resolveDialect();
        StringJoiner quotedFields = new StringJoiner(",");
        for (String field : splitFields(fields)) {
            quotedFields.add(quoteField(field, dialect));
        }
        return quotedFields.toString();
    }

    private static String quote(String identifier, IdentifierDialect dialect) {
        if (identifier == null || identifier.isBlank()) {
            throw new IllegalArgumentException("key can not be blank");
        }

        String openingQuote = dialect.quoteString;
        String closingQuote = "[".equals(openingQuote) ? "]" : openingQuote;
        StringJoiner quotedIdentifier = new StringJoiner(".");
        for (String part : identifier.trim().split("\\.", -1)) {
            quotedIdentifier.add(quotePart(part, openingQuote, closingQuote, dialect.identifierCase));
        }
        return quotedIdentifier.toString();
    }

    private static String quoteWithSuffix(String identifier, String suffix, IdentifierDialect dialect) {
        if (identifier == null || identifier.isBlank()) {
            throw new IllegalArgumentException("key can not be blank");
        }

        String[] parts = identifier.trim().split("\\.", -1);
        String openingQuote = dialect.quoteString;
        String closingQuote = "[".equals(openingQuote) ? "]" : openingQuote;
        StringJoiner quotedIdentifier = new StringJoiner(".");
        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];
            if (i == parts.length - 1) {
                part = appendSuffix(part, suffix);
            }
            quotedIdentifier.add(quotePart(part, openingQuote, closingQuote, dialect.identifierCase));
        }
        return quotedIdentifier.toString();
    }

    private static String normalizeWithSuffix(String identifier, String suffix, IdentifierDialect dialect) {
        if (identifier == null || identifier.isBlank()) {
            throw new IllegalArgumentException("key can not be blank");
        }

        String[] parts = identifier.trim().split("\\.", -1);
        StringJoiner normalizedIdentifier = new StringJoiner(".");
        for (int i = 0; i < parts.length; i++) {
            String part = parts[i].trim();
            if (part.isEmpty() || "*".equals(part)) {
                throw new IllegalArgumentException("suffix requires a concrete identifier");
            }
            boolean explicitlyQuoted = hasExistingQuotes(part);
            part = removeExistingQuotes(part);
            if (i == parts.length - 1) {
                part += suffix;
            }
            if (!explicitlyQuoted) {
                part = normalizeIdentifierCase(part, dialect.identifierCase);
            }
            normalizedIdentifier.add(part);
        }
        return normalizedIdentifier.toString();
    }

    private static void validateSuffix(String suffix) {
        if (suffix == null || !suffix.matches("[A-Za-z0-9_$]*")) {
            throw new IllegalArgumentException("identifier suffix contains illegal characters");
        }
    }

    private static String appendSuffix(String identifier, String suffix) {
        String identifierPart = identifier.trim();
        if (identifierPart.isEmpty() || "*".equals(identifierPart)) {
            throw new IllegalArgumentException("suffix requires a concrete identifier");
        }
        if (isWrapped(identifierPart, '"', '"')) {
            return '"' + identifierPart.substring(1, identifierPart.length() - 1) + suffix + '"';
        }
        if (isWrapped(identifierPart, '`', '`')) {
            return '`' + identifierPart.substring(1, identifierPart.length() - 1) + suffix + '`';
        }
        if (isWrapped(identifierPart, '[', ']')) {
            return '[' + identifierPart.substring(1, identifierPart.length() - 1) + suffix + ']';
        }
        return identifierPart + suffix;
    }

    private static String quoteField(String field, IdentifierDialect dialect) {
        if (field == null || field.isBlank()) {
            throw new IllegalArgumentException("field can not be blank");
        }
        String expression = field.trim();
        if (isSimpleIdentifier(expression)) {
            return quote(expression, dialect);
        }

        int openingParenthesis = expression.indexOf('(');
        int closingParenthesis = expression.lastIndexOf(')');
        if (openingParenthesis > 0 && closingParenthesis == expression.length() - 1) {
            String function = expression.substring(0, openingParenthesis).trim();
            String argument = expression.substring(openingParenthesis + 1, closingParenthesis).trim();
            if (function.matches("[A-Za-z_][A-Za-z0-9_$]*") && isSimpleIdentifier(argument)) {
                return function + "(" + quote(argument, dialect) + ")";
            }
        }

        // Complex SQL expressions are an explicit escape hatch and remain caller-owned SQL.
        return expression;
    }

    private static boolean isSimpleIdentifier(String value) {
        if ("*".equals(value)) {
            return true;
        }
        for (String part : value.split("\\.", -1)) {
            String identifierPart = part.trim();
            if (identifierPart.isEmpty()) {
                return false;
            }
            if ("*".equals(identifierPart) || hasExistingQuotes(identifierPart)) {
                continue;
            }
            if (!identifierPart.matches("[A-Za-z_][A-Za-z0-9_$]*")) {
                return false;
            }
        }
        return true;
    }

    private static Iterable<String> splitFields(String fields) {
        java.util.List<String> result = new java.util.ArrayList<>();
        StringBuilder field = new StringBuilder();
        int parenthesisDepth = 0;
        char quote = 0;
        for (int i = 0; i < fields.length(); i++) {
            char current = fields.charAt(i);
            if (quote != 0) {
                field.append(current);
                if (current == quote || (quote == '[' && current == ']')) {
                    quote = 0;
                }
                continue;
            }
            if (current == '"' || current == '`' || current == '[' || current == '\'') {
                quote = current;
                field.append(current);
            } else if (current == '(') {
                parenthesisDepth++;
                field.append(current);
            } else if (current == ')') {
                parenthesisDepth--;
                field.append(current);
            } else if (current == ',' && parenthesisDepth == 0) {
                result.add(field.toString());
                field.setLength(0);
            } else {
                field.append(current);
            }
        }
        result.add(field.toString());
        return result;
    }

    private static String quotePart(String part, String openingQuote, String closingQuote,
                                    IdentifierCase identifierCase) {
        String identifierPart = part.trim();
        if (identifierPart.isEmpty()) {
            throw new IllegalArgumentException("key contains an empty identifier part");
        }
        if ("*".equals(identifierPart)) {
            return identifierPart;
        }

        boolean explicitlyQuoted = hasExistingQuotes(identifierPart);
        identifierPart = removeExistingQuotes(identifierPart);
        if (!explicitlyQuoted) {
            identifierPart = normalizeIdentifierCase(identifierPart, identifierCase);
        }
        String escapedPart = identifierPart.replace(closingQuote, closingQuote + closingQuote);
        return openingQuote + escapedPart + closingQuote;
    }

    private static boolean hasExistingQuotes(String identifier) {
        return isWrapped(identifier, '"', '"')
                || isWrapped(identifier, '`', '`')
                || isWrapped(identifier, '[', ']');
    }

    private static String normalizeIdentifierCase(String identifier, IdentifierCase identifierCase) {
        if (identifierCase == IdentifierCase.LOWER) {
            return identifier.toLowerCase(Locale.ROOT);
        }
        if (identifierCase == IdentifierCase.UPPER) {
            return identifier.toUpperCase(Locale.ROOT);
        }
        return identifier;
    }

    private static String removeExistingQuotes(String identifier) {
        if (isWrapped(identifier, '"', '"') || isWrapped(identifier, '`', '`')) {
            char quote = identifier.charAt(0);
            String value = identifier.substring(1, identifier.length() - 1);
            return value.replace(String.valueOf(quote) + quote, String.valueOf(quote));
        }
        if (isWrapped(identifier, '[', ']')) {
            return identifier.substring(1, identifier.length() - 1).replace("]]", "]");
        }
        return identifier;
    }

    private static boolean isWrapped(String value, char openingQuote, char closingQuote) {
        return value.length() >= 2
                && value.charAt(0) == openingQuote
                && value.charAt(value.length() - 1) == closingQuote;
    }

    private static String normalizeQuote(String quoteString) {
        if (quoteString == null || quoteString.isBlank()) {
            return ANSI_QUOTE;
        }
        return quoteString.trim();
    }

    private static IdentifierDialect resolveDialect() {
        String dataSourceKey = DBContextHolder.getDataSource();
        DataSource dataSource = findDataSource(dataSourceKey);
        if (dataSource == null) {
            return inferDialect(findDataSourceConfig(dataSourceKey));
        }

        synchronized (DIALECT_CACHE) {
            IdentifierDialect cachedDialect = DIALECT_CACHE.get(dataSource);
            if (cachedDialect != null) {
                return cachedDialect;
            }
            IdentifierDialect dialect = readDialect(dataSource);
            DIALECT_CACHE.put(dataSource, dialect);
            return dialect;
        }
    }

    private static DataSource findDataSource(String dataSourceKey) {
        String lookupKey = dataSourceKey == null ? DataUnit.MASTER : dataSourceKey;
        Object configuredDataSource = DataSourceUtil.dataSourceMap.get(lookupKey);
        if (configuredDataSource instanceof DataSource dataSource) {
            return dataSource;
        }

        try {
            if (SpringContextUtil.getApplicationContext() != null) {
                Object masterDataSource = SpringContextUtil.getBean(DataUnit.MASTERBEAN);
                if (masterDataSource instanceof DataSource dataSource) {
                    return dataSource;
                }
            }
        } catch (RuntimeException ignored) {
            // The ANSI quote is used when the Spring context is not ready.
        }
        return null;
    }

    private static IdentifierDialect readDialect(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metadata = connection.getMetaData();
            IdentifierCase identifierCase = IdentifierCase.PRESERVE;
            if (metadata.storesLowerCaseIdentifiers()) {
                identifierCase = IdentifierCase.LOWER;
            } else if (metadata.storesUpperCaseIdentifiers()) {
                identifierCase = IdentifierCase.UPPER;
            }
            return new IdentifierDialect(metadata.getIdentifierQuoteString(), identifierCase);
        } catch (Exception ignored) {
            return inferDialect(dataSource);
        }
    }

    private static DatasourceConfig findDataSourceConfig(String dataSourceKey) {
        String lookupKey = dataSourceKey == null ? DataUnit.MASTER : dataSourceKey;
        return DataSourceUtil.get(lookupKey);
    }

    private static IdentifierDialect inferDialect(Object dataSourceConfig) {
        String databaseDescription = "";
        if (dataSourceConfig instanceof DruidDataSource dataSource) {
            databaseDescription = String.join(" ",
                    nullToEmpty(dataSource.getDbType()),
                    nullToEmpty(dataSource.getDriverClassName()),
                    nullToEmpty(dataSource.getUrl()));
        } else if (dataSourceConfig instanceof DatasourceConfig config) {
            databaseDescription = String.join(" ",
                    nullToEmpty(config.getCommonType()),
                    nullToEmpty(config.getDriverClassName()),
                    nullToEmpty(config.getUrl()));
        }

        String normalizedDescription = databaseDescription.toLowerCase(Locale.ROOT);
        if (normalizedDescription.contains("mysql")
                || normalizedDescription.contains("mariadb")
                || normalizedDescription.contains("tidb")) {
            return new IdentifierDialect("`", IdentifierCase.PRESERVE);
        }
        if (normalizedDescription.contains("postgres")
                || normalizedDescription.contains("postgre")
                || normalizedDescription.contains("highgo")
                || normalizedDescription.contains("hgdb")) {
            return new IdentifierDialect(ANSI_QUOTE, IdentifierCase.LOWER);
        }
        if (normalizedDescription.contains("oracle")
                || normalizedDescription.contains("dameng")
                || normalizedDescription.contains("dmdriver")
                || normalizedDescription.contains("dm.jdbc")
                || normalizedDescription.contains("jdbc:dm:")) {
            return new IdentifierDialect(ANSI_QUOTE, IdentifierCase.UPPER);
        }
        return new IdentifierDialect(ANSI_QUOTE, IdentifierCase.PRESERVE);
    }

    private static String nullToEmpty(String value) {
        return value == null ? "" : value;
    }

    private enum IdentifierCase {
        PRESERVE,
        LOWER,
        UPPER
    }

    private static final class IdentifierDialect {
        private final String quoteString;
        private final IdentifierCase identifierCase;

        private IdentifierDialect(String quoteString, IdentifierCase identifierCase) {
            this.quoteString = normalizeQuote(quoteString);
            this.identifierCase = identifierCase;
        }
    }
}
