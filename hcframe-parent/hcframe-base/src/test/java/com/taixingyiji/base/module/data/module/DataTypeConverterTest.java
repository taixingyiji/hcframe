package com.taixingyiji.base.module.data.module;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DataTypeConverterTest {

    @Test
    void convertPostgreSqlNumericDateAndBooleanTypes() {
        Map<String, String> columnTypes = new HashMap<>();
        columnTypes.put("id", "int8");
        columnTypes.put("age", "int4");
        columnTypes.put("amount", "numeric");
        columnTypes.put("enabled", "bool");
        columnTypes.put("create_time", "timestamp");

        assertEquals(1L, DataTypeConverter.convertDataTypes("id", "1", columnTypes));
        assertEquals(18, DataTypeConverter.convertDataTypes("age", "18", columnTypes));
        assertEquals(new BigDecimal("12.50"), DataTypeConverter.convertDataTypes("amount", "12.50", columnTypes));
        assertTrue((Boolean) DataTypeConverter.convertDataTypes("enabled", "1", columnTypes));
        assertInstanceOf(Date.class, DataTypeConverter.convertDataTypes("create_time", "2026-05-25 10:20:30", columnTypes));
    }
}
