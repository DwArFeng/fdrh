package com.dwarfeng.fdrh.impl.dao.mapper;

import com.dwarfeng.fdr.stack.bean.entity.FilteredValue;
import com.dwarfeng.subgrade.sdk.jdbc.mapper.CrudMapper;
import com.dwarfeng.subgrade.stack.bean.key.LongIdKey;
import org.springframework.stereotype.Component;

@Component
public class FilteredValueCrudMapper implements CrudMapper<LongIdKey, FilteredValue> {

    @Override
    public Object[] insert2Args(FilteredValue entity) {
        return new Object[]{entity.getKey().getLongId(), entity.getPointKey().getLongId(),
                entity.getFilterKey().getLongId(), entity.getHappenedDate(), entity.getValue(), entity.getMessage()};
    }

    @Override
    public Object[] update2Args(FilteredValue entity) {
        return new Object[]{entity.getKey().getLongId(), entity.getPointKey().getLongId(),
                entity.getFilterKey().getLongId(), entity.getHappenedDate(), entity.getValue(), entity.getMessage()};
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
