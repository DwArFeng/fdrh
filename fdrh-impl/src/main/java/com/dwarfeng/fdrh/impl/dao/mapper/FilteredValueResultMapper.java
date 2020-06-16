package com.dwarfeng.fdrh.impl.dao.mapper;

import com.dwarfeng.fdr.stack.bean.entity.FilteredValue;
import com.dwarfeng.subgrade.sdk.jdbc.mapper.ResultMapper;
import com.dwarfeng.subgrade.stack.bean.key.LongIdKey;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class FilteredValueResultMapper implements ResultMapper<FilteredValue> {

    @Override
    public FilteredValue result2Entity(ResultSet resultSet) throws SQLException {
        return new FilteredValue(
                new LongIdKey(resultSet.getLong(1)),
                new LongIdKey(resultSet.getLong(2)),
                new LongIdKey(resultSet.getLong(3)),
                resultSet.getDate(4),
                resultSet.getString(5),
                resultSet.getString(6)
        );
    }
}
