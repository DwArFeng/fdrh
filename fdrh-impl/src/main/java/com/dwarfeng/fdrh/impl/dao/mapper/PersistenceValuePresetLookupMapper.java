package com.dwarfeng.fdrh.impl.dao.mapper;

import com.dwarfeng.fdr.stack.service.PersistenceValueMaintainService;
import com.dwarfeng.subgrade.sdk.jdbc.mapper.PresetLookupMapper;
import com.dwarfeng.subgrade.stack.bean.dto.PagingInfo;
import com.dwarfeng.subgrade.stack.bean.key.LongIdKey;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Objects;

@Component
public class PersistenceValuePresetLookupMapper implements PresetLookupMapper {

    @Override
    public Object[] lookup2Args(String preset, Object[] args) {
        return general2Args(preset, args);
    }

    @Override
    public Object[] paging2Args(String preset, Object[] args, PagingInfo pagingInfo) {
        return general2Args(preset, args);
    }

    @Override
    public Object[] count2Args(String preset, Object[] args) {
        return general2Args(preset, args);
    }

    private Object[] general2Args(String preset, Object[] args) {
        switch (preset) {
            case PersistenceValueMaintainService.CHILD_FOR_POINT:
                return childForPoint(preset, args);
            case PersistenceValueMaintainService.BETWEEN:
                return between(preset, args);
            case PersistenceValueMaintainService.CHILD_FOR_POINT_BETWEEN:
                return childForPointBetween(preset, args);
            default:
                throw new IllegalArgumentException("无法识别的预设：" + preset);
        }
    }

    private Object[] childForPoint(String preset, Object[] args) {
        LongIdKey key = (LongIdKey) args[0];
        if (Objects.isNull(key)) {
            return new Object[0];
        } else {
            return new Object[]{key.getLongId()};
        }
    }

    private Object[] between(String preset, Object[] args) {
        Date date1 = (Date) args[0];
        Date date2 = (Date) args[1];
        return new Object[]{date1, date2};
    }

    private Object[] childForPointBetween(String preset, Object[] args) {
        LongIdKey key = (LongIdKey) args[0];
        Date date1 = (Date) args[1];
        Date date2 = (Date) args[2];
        if (Objects.isNull(key)) {
            return new Object[]{date1, date2};
        } else {
            return new Object[]{key.getLongId(), date1, date2};
        }
    }
}
