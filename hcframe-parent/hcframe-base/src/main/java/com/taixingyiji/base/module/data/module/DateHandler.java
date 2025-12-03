package com.taixingyiji.base.module.data.module;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @className DateHandler
 * @author lhc
 * @date 2025年12月03日 9:56
 * @description 描述
 * @version 1.0
 */
@MappedJdbcTypes(JdbcType.TIMESTAMP)
@MappedTypes({Date.class, LocalDateTime.class})
public class DateHandler implements TypeHandler<Date> {

    @Override
    public void setParameter(PreparedStatement ps, int i, Date parameter, JdbcType jdbcType) throws SQLException {
        if (parameter == null) {
            ps.setNull(i, Types.TIMESTAMP);
        } else {
            ps.setTimestamp(i, new Timestamp(parameter.getTime()));
        }
    }

    @Override
    public Date getResult(ResultSet rs, String columnName) throws SQLException {
        return convert(rs.getObject(columnName));
    }

    @Override
    public Date getResult(ResultSet rs, int columnIndex) throws SQLException {
        return convert(rs.getObject(columnIndex));
    }

    @Override
    public Date getResult(CallableStatement cs, int columnIndex) throws SQLException {
        return convert(cs.getObject(columnIndex));
    }

    private Date convert(Object obj) throws SQLException {
        if (obj == null) return null;

        if (obj instanceof Timestamp) {
            return new Date(((Timestamp) obj).getTime());
        }
        if (obj instanceof LocalDateTime) {
            return Timestamp.valueOf((LocalDateTime) obj);
        }
        if (obj instanceof Date) {
            return (Date) obj;
        }

        throw new SQLException("Unsupported datetime type: " + obj.getClass());
    }
}
