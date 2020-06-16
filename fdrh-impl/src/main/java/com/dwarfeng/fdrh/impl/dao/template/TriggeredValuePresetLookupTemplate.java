package com.dwarfeng.fdrh.impl.dao.template;

import com.dwarfeng.fdr.stack.service.TriggeredValueMaintainService;
import com.dwarfeng.fdrh.impl.dao.definition.TriggeredValueTableDefinition;
import com.dwarfeng.subgrade.sdk.jdbc.definition.TableDefinitionUtil;
import com.dwarfeng.subgrade.sdk.jdbc.template.PhoenixPresetLookupTemplate;
import com.dwarfeng.subgrade.stack.bean.key.LongIdKey;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

@Component
public class TriggeredValuePresetLookupTemplate extends PhoenixPresetLookupTemplate {

    public TriggeredValuePresetLookupTemplate(@NotNull TriggeredValueTableDefinition tableDefinition) {
        super(tableDefinition);
    }

    @Override
    public String buildWhereClause(String preset, Object[] args) {
        switch (preset) {
            case TriggeredValueMaintainService.BETWEEN:
                return betweenWhere(preset, args);
            case TriggeredValueMaintainService.CHILD_FOR_POINT:
                return childForPointWhere(preset, args);
            case TriggeredValueMaintainService.CHILD_FOR_TRIGGER:
                return childForTriggerWhere(preset, args);
            case TriggeredValueMaintainService.CHILD_FOR_TRIGGER_SET:
                return childForTriggerSetWhere(preset, args);
            case TriggeredValueMaintainService.CHILD_FOR_POINT_BETWEEN:
                return childForPointBetweenWhere(preset, args);
            case TriggeredValueMaintainService.CHILD_FOR_TRIGGER_BETWEEN:
                return childForTriggerSetBetweenWhere(preset, args);
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
    private String childForTriggerWhere(String preset, Object[] args) {
        LongIdKey key = (LongIdKey) args[0];
        if (Objects.isNull(key)) {
            return "trigger_id IS NULL";
        } else {
            return "trigger_id = ?";
        }
    }

    private String childForTriggerSetWhere(String preset, Object[] args) {
        String clause;
        if (Objects.isNull(args[0])) {
            clause = "trigger_id IS NULL";
        } else {
            @SuppressWarnings("unchecked")
            List<LongIdKey> longIdKeys = (List<LongIdKey>) args[0];
            if (longIdKeys.isEmpty()) {
                clause = "trigger_id IS NULL";
            } else {
                clause = String.format("trigger_id IN (%s)", TableDefinitionUtil.placeHolder(longIdKeys.size()));
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

    private String childForTriggerSetBetweenWhere(String preset, Object[] args) {
        String clause;
        LongIdKey key = (LongIdKey) args[0];
        if (Objects.isNull(key)) {
            clause = "trigger_id IS NULL";
        } else {
            clause = "trigger_id = ?";
        }
        clause += " AND happened_date >= ? AND happened_date < ?";
        return clause;
    }

    @Override
    protected String buildOrderByClause(String preset, Object[] args) {
        return null;
    }
}
