package com.dwarfeng.fdrh.impl.dao.processor;

import com.dwarfeng.fdr.stack.bean.entity.FilteredValue;
import com.dwarfeng.fdr.stack.service.FilteredValueMaintainService;
import com.dwarfeng.subgrade.sdk.database.definition.ColumnDefinition;
import com.dwarfeng.subgrade.sdk.database.jdbc.BaseHandle;
import com.dwarfeng.subgrade.sdk.database.jdbc.PresetLookupHandle;
import com.dwarfeng.subgrade.sdk.database.jdbc.QueryInfo;
import com.dwarfeng.subgrade.sdk.database.jdbc.SQLUtil;
import com.dwarfeng.subgrade.stack.bean.key.LongIdKey;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@Component
public class FilteredValueHandle implements BaseHandle<LongIdKey, FilteredValue>, PresetLookupHandle<FilteredValue> {

    @Override
    public FilteredValue newInstance() {
        return new FilteredValue();
    }

    @Override
    public Object getKeyProperty(LongIdKey key, ColumnDefinition columnDefinition) {
        if (Objects.equals(columnDefinition.getName(), "id")) {
            return key.getLongId();
        }
        throw new IllegalArgumentException("未知的 columnDefinition: " + columnDefinition);
    }

    @Override
    public Object getEntityProperty(FilteredValue entity, ColumnDefinition columnDefinition) {
        switch (columnDefinition.getName()) {
            case "id":
                return entity.getKey().getLongId();
            case "point_id":
                return entity.getPointKey().getLongId();
            case "filter_id":
                return entity.getFilterKey().getLongId();
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
            FilteredValue entity, ColumnDefinition columnDefinition, ResultSet resultSet, int index)
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
            case "filter_id":
                if (Objects.isNull(entity.getFilterKey())) {
                    entity.setFilterKey(new LongIdKey());
                }
                entity.getFilterKey().setLongId(resultSet.getLong(index));
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
            case FilteredValueMaintainService.BETWEEN:
                return presetBetween(args);
            case FilteredValueMaintainService.CHILD_FOR_FILTER:
                return presetChildForFilter(args);
            case FilteredValueMaintainService.CHILD_FOR_FILTER_BETWEEN:
                return presetChildForFilterBetween(args);
            case FilteredValueMaintainService.CHILD_FOR_FILTER_SET:
                return presetChildForFilterSet(args);
            case FilteredValueMaintainService.CHILD_FOR_POINT:
                return presetChildForPoint(args);
            case FilteredValueMaintainService.CHILD_FOR_POINT_BETWEEN:
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

    private QueryInfo presetChildForFilter(Object[] args) {
        String whereClause;
        Object[] parameters;
        LongIdKey key = (LongIdKey) args[0];
        if (Objects.isNull(key)) {
            whereClause = "filter_id IS NULL";
            parameters = new Object[0];
        } else {
            whereClause = "filter_id=?";
            parameters = new Object[]{key.getLongId()};
        }
        return new QueryInfo(whereClause, null, parameters);
    }

    private QueryInfo presetChildForFilterBetween(Object[] args) {
        String whereClause;
        Object[] parameters;
        LongIdKey key = (LongIdKey) args[0];
        if (Objects.isNull(key)) {
            whereClause = "filter_id IS NULL AND happened_date >= ? AND happened_date < ?";
            parameters = new Object[]{args[1], args[2]};
        } else {
            whereClause = "filter_id = ? AND happened_date >= ? AND happened_date < ?";
            parameters = new Object[]{key.getLongId(), args[1], args[2]};
        }
        return new QueryInfo(whereClause, null, parameters);
    }

    private QueryInfo presetChildForFilterSet(Object[] args) {
        String whereClause;
        Object[] parameters;
        if (Objects.isNull(args[0])) {
            whereClause = "filter_id IS NULL";
            parameters = new Object[0];
        } else {
            @SuppressWarnings("unchecked")
            List<LongIdKey> longIdKeys = (List<LongIdKey>) args[0];
            if (longIdKeys.isEmpty()) {
                whereClause = "filter_id IS NULL";
                parameters = new Object[0];
            } else {
                whereClause = String.format("filter_id IN (%s)", SQLUtil.placeHolder(longIdKeys.size()));
                parameters = longIdKeys.stream().map(LongIdKey::getLongId).toArray();
            }
        }
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
