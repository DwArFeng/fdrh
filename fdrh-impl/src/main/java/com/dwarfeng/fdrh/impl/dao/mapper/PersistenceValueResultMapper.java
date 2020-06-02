package com.dwarfeng.fdrh.impl.dao.mapper;

import com.dwarfeng.fdr.stack.bean.entity.PersistenceValue;
import com.dwarfeng.subgrade.sdk.jdbc.mapper.ResultMapper;
import com.dwarfeng.subgrade.stack.bean.key.LongIdKey;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class PersistenceValueResultMapper implements ResultMapper<PersistenceValue> {

    @Override
    public PersistenceValue result2Entity(ResultSet resultSet) throws SQLException {
        return new PersistenceValue(
                new LongIdKey(resultSet.getLong(1)),
                new LongIdKey(resultSet.getLong(2)),
                resultSet.getDate(3),
                resultSet.getString(4)
        );
    }
}
