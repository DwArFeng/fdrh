package com.dwarfeng.fdrh.impl.dao.processor;

import com.dwarfeng.fdr.stack.bean.entity.TriggeredValue;
import com.dwarfeng.fdr.stack.service.TriggeredValueMaintainService;
import com.dwarfeng.subgrade.sdk.database.definition.ColumnDefinition;
import com.dwarfeng.subgrade.sdk.jdbc.handle.BaseHandle;
import com.dwarfeng.subgrade.sdk.jdbc.handle.PresetLookupHandle;
import com.dwarfeng.subgrade.sdk.jdbc.handle.QueryInfo;
import com.dwarfeng.subgrade.sdk.jdbc.processor.SQLUtil;
import com.dwarfeng.subgrade.stack.bean.key.LongIdKey;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@SuppressWarnings("DuplicatedCode")
@Component
public class TriggeredValueHandle implements BaseHandle<LongIdKey, TriggeredValue>, PresetLookupHandle<TriggeredValue> {

    private static final Map<String, QueryInfo.Ordering> ORDERING_MAP;

    static {
        ORDERING_MAP = new HashMap<>();
        ORDERING_MAP.put("happened_date", QueryInfo.Ordering.ASC);
    }

    @Override
    public TriggeredValue newInstance() {
        return new TriggeredValue();
    }

    @Override
    public Object getKeyProperty(LongIdKey key, ColumnDefinition columnDefinition) {
        if (Objects.equals(columnDefinition.getName(), "id")) {
            return key.getLongId();
        }
        throw new IllegalArgumentException("未知的 columnDefinition: " + columnDefinition);
    }

    @Override
    public Object getEntityProperty(TriggeredValue entity, ColumnDefinition columnDefinition) {
        switch (columnDefinition.getName()) {
            case "id":
                return entity.getKey().getLongId();
            case "point_id":
                return entity.getPointKey().getLongId();
            case "trigger_id":
                return entity.getTriggerKey().getLongId();
            case "happened_date":
                return entity.getHappenedDate();
            case "column_value":
                return entity.getValue();
            case "message":
                return entity.getMessage();
            default:
                throw new IllegalArgumentException("未知的 columnDefinition: " + columnDefinition);
        }
    }

    @Override
    public void setProperty(
            TriggeredValue entity, ColumnDefinition columnDefinition, ResultSet resultSet, int index)
            throws SQLException {
        switch (columnDefinition.getName()) {
            case "id":
                if (Objects.isNull(entity.getKey())) {
                    entity.setKey(new LongIdKey());
                }
                entity.getKey().setLongId(resultSet.getLong(index));
                break;
            case "point_id":
                if (Objects.isNull(entity.getPointKey())) {
                    entity.setPointKey(new LongIdKey());
                }
                entity.getPointKey().setLongId(resultSet.getLong(index));
                break;
            case "trigger_id":
                if (Objects.isNull(entity.getTriggerKey())) {
                    entity.setTriggerKey(new LongIdKey());
                }
                entity.getTriggerKey().setLongId(resultSet.getLong(index));
                break;
            case "happened_date":
                entity.setHappenedDate(resultSet.getDate(index));
                break;
            case "column_value":
                entity.setValue(resultSet.getString(index));
                break;
            case "message":
                entity.setMessage(resultSet.getString(index));
                break;
            default:
                throw new IllegalArgumentException("未知的 columnDefinition: " + columnDefinition);
        }
    }

    @Override
    public QueryInfo getQueryInfo(String preset, Object[] args) {
        switch (preset) {
            case TriggeredValueMaintainService.BETWEEN:
                return presetBetween(args);
            case TriggeredValueMaintainService.CHILD_FOR_TRIGGER:
                return presetChildForTrigger(args);
            case TriggeredValueMaintainService.CHILD_FOR_TRIGGER_BETWEEN:
                return presetChildForTriggerBetween(args);
            case TriggeredValueMaintainService.CHILD_FOR_TRIGGER_SET:
                return presetChildForTriggerSet(args);
            case TriggeredValueMaintainService.CHILD_FOR_POINT:
                return presetChildForPoint(args);
            case TriggeredValueMaintainService.CHILD_FOR_POINT_BETWEEN:
                return presetChildForPointBetween(args);
            default:
                throw new IllegalArgumentException("无法识别的预设：" + preset);
        }
    }

    private QueryInfo presetBetween(Object[] args) {
        String whereClause;
        Object[] parameters;
        whereClause = "happened_date >= ? AND happened_date < ?";
        parameters = new Object[]{args[0], args[1]};
        return new QueryInfo(whereClause, ORDERING_MAP, parameters);
    }

    private QueryInfo presetChildForTrigger(Object[] args) {
        String whereClause;
        Object[] parameters;
        LongIdKey key = (LongIdKey) args[0];
        if (Objects.isNull(key)) {
            whereClause = "trigger_id IS NULL";
            parameters = new Object[0];
        } else {
            whereClause = "trigger_id=?";
            parameters = new Object[]{key.getLongId()};
        }
        return new QueryInfo(whereClause, ORDERING_MAP, parameters);
    }

    private QueryInfo presetChildForTriggerBetween(Object[] args) {
        String whereClause;
        Object[] parameters;
        LongIdKey key = (LongIdKey) args[0];
        if (Objects.isNull(key)) {
            whereClause = "trigger_id IS NULL AND happened_date >= ? AND happened_date < ?";
            parameters = new Object[]{args[1], args[2]};
        } else {
            whereClause = "trigger_id = ? AND happened_date >= ? AND happened_date < ?";
            parameters = new Object[]{key.getLongId(), args[1], args[2]};
        }
        return new QueryInfo(whereClause, ORDERING_MAP, parameters);
    }

    private QueryInfo presetChildForTriggerSet(Object[] args) {
        String whereClause;
        Object[] parameters;
        if (Objects.isNull(args[0])) {
            whereClause = "trigger_id IS NULL";
            parameters = new Object[0];
        } else {
            @SuppressWarnings("unchecked")
            List<LongIdKey> longIdKeys = (List<LongIdKey>) args[0];
            if (longIdKeys.isEmpty()) {
                whereClause = "trigger_id IS NULL";
                parameters = new Object[0];
            } else {
                whereClause = String.format("trigger_id IN (%s)", SQLUtil.placeHolder(longIdKeys.size()));
                parameters = longIdKeys.stream().map(LongIdKey::getLongId).toArray();
            }
        }
        return new QueryInfo(whereClause, ORDERING_MAP, parameters);
    }

    private QueryInfo presetChildForPoint(Object[] args) {
        String whereClause;
        Object[] parameters;
        LongIdKey key = (LongIdKey) args[0];
        if (Objects.isNull(key)) {
            whereClause = "point_id IS NULL";
            parameters = new Object[0];
        } else {
            whereClause = "point_id=?";
            parameters = new Object[]{key.getLongId()};
        }
        return new QueryInfo(whereClause, ORDERING_MAP, parameters);
    }

    private QueryInfo presetChildForPointBetween(Object[] args) {
        String whereClause;
        Object[] parameters;
        LongIdKey key = (LongIdKey) args[0];
        if (Objects.isNull(key)) {
            whereClause = "point_id IS NULL AND happened_date >= ? AND happened_date < ?";
            parameters = new Object[]{args[1], args[2]};
        } else {
            whereClause = "point_id = ? AND happened_date >= ? AND happened_date < ?";
            parameters = new Object[]{key.getLongId(), args[1], args[2]};
        }
        return new QueryInfo(whereClause, ORDERING_MAP, parameters);
    }
}
