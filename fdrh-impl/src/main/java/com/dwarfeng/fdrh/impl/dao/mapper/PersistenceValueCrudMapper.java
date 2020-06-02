package com.dwarfeng.fdrh.impl.dao.mapper;

import com.dwarfeng.fdr.stack.bean.entity.PersistenceValue;
import com.dwarfeng.subgrade.sdk.jdbc.mapper.CrudMapper;
import com.dwarfeng.subgrade.stack.bean.key.LongIdKey;
import org.springframework.stereotype.Component;

@Component
public class PersistenceValueCrudMapper implements CrudMapper<LongIdKey, PersistenceValue> {

    @Override
    public Object[] insert2Args(PersistenceValue entity) {
        return new Object[]{entity.getKey().getLongId(), entity.getPointKey().getLongId(),
                entity.getHappenedDate(), entity.getValue()};
    }

    @Override
    public Object[] update2Args(PersistenceValue entity) {
        return new Object[]{entity.getKey().getLongId(), entity.getPointKey().getLongId(),
                entity.getHappenedDate(), entity.getValue()};
    }

    @Override
    public Object[] delete2Args(LongIdKey key) {
        return new Object[]{key.getLongId()};
    }

    @Override
    public Object[] get2Args(LongIdKey key) {
        return new Object[]{key.getLongId()};
    }

    @Override
    public Object[] exists2Args(LongIdKey key) {
        return new Object[]{key.getLongId()};
    }
}
