package com.dwarfeng.fdrh.impl.dao.processor;

import com.dwarfeng.fdr.stack.bean.entity.PersistenceValue;
import com.dwarfeng.fdr.stack.service.PersistenceValueMaintainService;
import com.dwarfeng.subgrade.sdk.database.definition.ColumnDefinition;
import com.dwarfeng.subgrade.sdk.database.jdbc.BaseHandle;
import com.dwarfeng.subgrade.sdk.database.jdbc.PresetLookupHandle;
import com.dwarfeng.subgrade.sdk.database.jdbc.QueryInfo;
import com.dwarfeng.subgrade.stack.bean.key.LongIdKey;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

@Component
public class PersistenceValueHandle implements BaseHandle<LongIdKey, PersistenceValue>,
        PresetLookupHandle<PersistenceValue> {

    @Override
    public PersistenceValue newInstance() {
        return new PersistenceValue();
    }

    @Override
    public Object getKeyProperty(LongIdKey key, ColumnDefinition columnDefinition) {
        if (Objects.equals(columnDefinition.getName(), "id")) {
            return key.getLongId();
        }
        throw new IllegalArgumentException("未知的 columnDefinition: " + columnDefinition);
    }

    @Override
    public Object getEntityProperty(PersistenceValue entity, ColumnDefinition columnDefinition) {
        switch (columnDefinition.getName()) {
            case "id":
                return entity.getKey().getLongId();
            case "point_id":
                return entity.getPointKey().getLongId();
            case "happened_date":
                return entity.getHappenedDate();
            case "column_value":
                return entity.getValue();
            default:
                throw new IllegalArgumentException("未知的 columnDefinition: " + columnDefinition);
        }
    }

    @Override
    public void setProperty(
            PersistenceValue entity, ColumnDefinition columnDefinition, ResultSet resultSet, int index)
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
            case "happened_date":
                entity.setHappenedDate(resultSet.getDate(index));
                break;
            case "column_value":
                entity.setValue(resultSet.getString(index));
                break;
            default:
                throw new IllegalArgumentException("未知的 columnDefinition: " + columnDefinition);
        }
    }

    @Override
    public QueryInfo getQueryInfo(String preset, Object[] args) {
        switch (preset) {
            case PersistenceValueMaintainService.BETWEEN:
                return presetBetween(args);
            case PersistenceValueMaintainService.CHILD_FOR_POINT:
                return presetChildForPoint(args);
            case PersistenceValueMaintainService.CHILD_FOR_POINT_BETWEEN:
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
        return new QueryInfo(whereClause, null, parameters);
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
        return new QueryInfo(whereClause, null, parameters);
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
        return new QueryInfo(whereClause, null, parameters);
    }
}
