package com.dwarfeng.fdrh.impl.dao.template;

import com.dwarfeng.fdr.stack.service.FilteredValueMaintainService;
import com.dwarfeng.fdrh.impl.dao.definition.FilteredValueTableDefinition;
import com.dwarfeng.subgrade.sdk.jdbc.definition.TableDefinitionUtil;
import com.dwarfeng.subgrade.sdk.jdbc.template.PhoenixPresetLookupTemplate;
import com.dwarfeng.subgrade.stack.bean.key.LongIdKey;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

@Component
public class FilteredValuePresetLookupTemplate extends PhoenixPresetLookupTemplate {

    public FilteredValuePresetLookupTemplate(@NotNull FilteredValueTableDefinition tableDefinition) {
        super(tableDefinition);
    }

    @Override
    public String buildWhereClause(String preset, Object[] args) {
        switch (preset) {
            case FilteredValueMaintainService.BETWEEN:
                return betweenWhere(preset, args);
            case FilteredValueMaintainService.CHILD_FOR_POINT:
                return childForPointWhere(preset, args);
            case FilteredValueMaintainService.CHILD_FOR_FILTER:
                return childForFilterWhere(preset, args);
            case FilteredValueMaintainService.CHILD_FOR_FILTER_SET:
                return childForFilterSetWhere(preset, args);
            case FilteredValueMaintainService.CHILD_FOR_POINT_BETWEEN:
                return childForPointBetweenWhere(preset, args);
            case FilteredValueMaintainService.CHILD_FOR_FILTER_BETWEEN:
                return childForFilterSetBetweenWhere(preset, args);
            default:
                throw new IllegalArgumentException("无法识别的预设：" + preset);
        }
    }

    @SuppressWarnings("unused")
    private String betweenWhere(String preset, Object[] args) {
        return "happened_date >= ? AND happened_date < ?";
    }

    @SuppressWarnings("unused")
    private String childForPointWhere(String preset, Object[] args) {
        LongIdKey key = (LongIdKey) args[0];
        if (Objects.isNull(key)) {
            return "point_id IS NULL";
        } else {
            return "point_id = ?";
        }
    }

    @SuppressWarnings("unused")
    private String childForFilterWhere(String preset, Object[] args) {
        LongIdKey key = (LongIdKey) args[0];
        if (Objects.isNull(key)) {
            return "filter_id IS NULL";
        } else {
            return "filter_id = ?";
        }
    }

    private String childForFilterSetWhere(String preset, Object[] args) {
        String clause;
        if (Objects.isNull(args[0])) {
            clause = "filter_id IS NULL";
        } else {
            @SuppressWarnings("unchecked")
            List<LongIdKey> longIdKeys = (List<LongIdKey>) args[0];
            if (longIdKeys.isEmpty()) {
                clause = "filter_id IS NULL";
            } else {
                clause = String.format("filter_id IN (%s)", TableDefinitionUtil.placeHolder(longIdKeys.size()));
            }
        }
        return clause;
    }

    @SuppressWarnings("unused")
    private String childForPointBetweenWhere(String preset, Object[] args) {
        String clause;
        LongIdKey key = (LongIdKey) args[0];
        if (Objects.isNull(key)) {
            clause = "point_id IS NULL";
        } else {
            clause = "point_id = ?";
        }
        clause += " AND happened_date >= ? AND happened_date < ?";
        return clause;
    }

    private String childForFilterSetBetweenWhere(String preset, Object[] args) {
        String clause;
        LongIdKey key = (LongIdKey) args[0];
        if (Objects.isNull(key)) {
            clause = "filter_id IS NULL";
        } else {
            clause = "filter_id = ?";
        }
        clause += " AND happened_date >= ? AND happened_date < ?";
        return clause;
    }

    @Override
    protected String buildOrderByClause(String preset, Object[] args) {
        return null;
    }
}
