package com.dwarfeng.fdrh.impl.dao.mapper;

import com.dwarfeng.fdr.stack.service.TriggeredValueMaintainService;
import com.dwarfeng.subgrade.sdk.jdbc.mapper.PresetLookupMapper;
import com.dwarfeng.subgrade.stack.bean.dto.PagingInfo;
import com.dwarfeng.subgrade.stack.bean.key.LongIdKey;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Component
public class TriggeredValuePresetLookupMapper implements PresetLookupMapper {

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
            case TriggeredValueMaintainService.BETWEEN:
                return between(preset, args);
            case TriggeredValueMaintainService.CHILD_FOR_POINT:
                return childForPoint(preset, args);
            case TriggeredValueMaintainService.CHILD_FOR_TRIGGER:
                return childForTrigger(preset, args);
            case TriggeredValueMaintainService.CHILD_FOR_TRIGGER_SET:
                return childForTriggerSet(preset, args);
            case TriggeredValueMaintainService.CHILD_FOR_POINT_BETWEEN:
                return childForPointBetween(preset, args);
            case TriggeredValueMaintainService.CHILD_FOR_TRIGGER_BETWEEN:
                return childForTriggerSetBetween(preset, args);
            default:
                throw new IllegalArgumentException("无法识别的预设：" + preset);
        }
    }

    @SuppressWarnings("unused")
    private Object[] between(String preset, Object[] args) {
        Date date1 = (Date) args[0];
        Date date2 = (Date) args[1];
        return new Object[]{date1, date2};
    }

    @SuppressWarnings("unused")
    private Object[] childForPoint(String preset, Object[] args) {
        LongIdKey key = (LongIdKey) args[0];
        if (Objects.isNull(key)) {
            return new Object[0];
        } else {
            return new Object[]{key.getLongId()};
        }
    }

    @SuppressWarnings("unused")
    private Object[] childForTrigger(String preset, Object[] args) {
        LongIdKey key = (LongIdKey) args[0];
        if (Objects.isNull(key)) {
            return new Object[0];
        } else {
            return new Object[]{key.getLongId()};
        }
    }

    @SuppressWarnings({"unused", "DuplicatedCode"})
    private Object[] childForTriggerSet(String preset, Object[] args) {
        if (Objects.isNull(args[0])) {
            return new Object[0];
        } else {
            @SuppressWarnings("unchecked")
            List<LongIdKey> longIdKeys = (List<LongIdKey>) args[0];
            if (longIdKeys.isEmpty()) {
                return new Object[0];
            } else {
                return longIdKeys.stream().map(LongIdKey::getLongId).toArray();
            }
        }
    }

    @SuppressWarnings({"unused", "DuplicatedCode"})
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

    @SuppressWarnings({"unused", "DuplicatedCode"})
    private Object[] childForTriggerSetBetween(String preset, Object[] args) {
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
