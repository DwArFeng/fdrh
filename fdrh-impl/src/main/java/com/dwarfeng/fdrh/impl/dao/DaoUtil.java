package com.dwarfeng.fdrh.impl.dao;

import com.dwarfeng.subgrade.sdk.database.definition.TableDefinition;
import com.dwarfeng.subgrade.sdk.jdbc.processor.SQLUtil;
import com.dwarfeng.subgrade.stack.bean.key.LongIdKey;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

/**
 * 数据访问层工具类。
 *
 * @author DwArFeng
 * @since 1.2.0
 */
final class DaoUtil {

    public static <T> T previous(
            JdbcTemplate jdbcTemplate, TableDefinition tableDefinition, LongIdKey pointKey, Date date,
            ResultSetExtractor<T> extractor) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT ");
        sqlBuilder.append(SQLUtil.fullColumnSerial(tableDefinition));
        sqlBuilder.append(" FROM ");
        sqlBuilder.append(SQLUtil.fullTableName(tableDefinition));
        sqlBuilder.append(" WHERE ");
        {
            if (Objects.isNull(pointKey)) {
                sqlBuilder.append("point_id IS NULL AND ");
            } else {
                sqlBuilder.append("point_id=? AND ");
            }
            sqlBuilder.append("happened_date<? ");
        }
        sqlBuilder.append("ORDER BY ");
        {
            sqlBuilder.append("happened_date DESC ");
        }
        sqlBuilder.append("LIMIT 1");
        return jdbcTemplate.query(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlBuilder.toString());
            if (Objects.isNull(pointKey)) {
                preparedStatement.setTimestamp(1, new Timestamp(date.getTime()));
            } else {
                preparedStatement.setLong(1, pointKey.getLongId());
                preparedStatement.setTimestamp(2, new Timestamp(date.getTime()));
            }
            return preparedStatement;
        }, extractor);
    }

    private DaoUtil() {
        throw new IllegalStateException("禁止实例化");
    }
}
