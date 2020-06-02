package com.dwarfeng.fdrh.impl.dao.template;

import com.dwarfeng.fdr.stack.service.PersistenceValueMaintainService;
import com.dwarfeng.subgrade.sdk.jdbc.definition.TableDefinition;
import com.dwarfeng.subgrade.sdk.jdbc.template.PhoenixPresetLookupTemplate;
import com.dwarfeng.subgrade.stack.bean.key.LongIdKey;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.Objects;

@Component
public class PersistenceValuePresetLookupTemplate extends PhoenixPresetLookupTemplate {

    public PersistenceValuePresetLookupTemplate(@NotNull TableDefinition tableDefinition) {
        super(tableDefinition);
    }

    @Override
    public String buildWhereClause(String preset, Object[] args) {
        switch (preset) {
            case PersistenceValueMaintainService.CHILD_FOR_POINT:
                return childForPointWhere(preset, args);
            case PersistenceValueMaintainService.BETWEEN:
                return betweenWhere(preset, args);
            case PersistenceValueMaintainService.CHILD_FOR_POINT_BETWEEN:
                return childForPointBetweenWhere(preset, args);
            default:
                throw new IllegalArgumentException("无法识别的预设：" + preset);
        }
    }

    private String childForPointWhere(String preset, Object[] args) {
        LongIdKey key = (LongIdKey) args[0];
        if (Objects.isNull(key)) {
            return "point_id IS NULL";
        } else {
            return "point_id = ?";
        }
    }

    private String betweenWhere(String preset, Object[] args) {
        return "happened_date >= ? AND happened_date < ?";
    }

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

    @Override
    protected String buildOrderByClause(String preset, Object[] args) {
        return null;
    }

}
