package com.dwarfeng.fdrh.impl.dao.definition;

import com.dwarfeng.subgrade.sdk.jdbc.definition.PhoenixConstants;
import com.dwarfeng.subgrade.sdk.jdbc.definition.PhoenixTableDefinitionHelper;
import com.dwarfeng.subgrade.sdk.jdbc.definition.TableDefinition;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class TriggeredValueTableDefinition implements TableDefinition {

    private final TableDefinition delegate;

    public TriggeredValueTableDefinition() {
        PhoenixTableDefinitionHelper helper = new PhoenixTableDefinitionHelper();
        helper.setTableName("fdrh.triggered_value");
        helper.addColumn("id", "BIGINT");
        helper.addColumn("point_id", "BIGINT");
        helper.addColumn("trigger_id", "BIGINT");
        helper.addColumn("happened_date", "DATE");
        helper.addColumn("column_value", "VARCHAR");
        helper.addColumn("message", "VARCHAR");
        helper.setPrimaryKey("id");
        helper.setPrimaryKeyAsc("id");
        helper.setTableSaltBuckets(30);
        helper.setTableImmutableRows(true);
        helper.addIndex("idx_triggered_value_point_id", PhoenixConstants.IndexType.LOCAL,
                "point_id", "happened_date");
        helper.setIndexAsc("idx_triggered_value_point_id", "happened_date");
        helper.setIndexInclude("idx_triggered_value_point_id", "trigger_id", "column_value",
                "message");
        helper.addIndex("idx_triggered_value_trigger_id", PhoenixConstants.IndexType.LOCAL,
                "trigger_id", "happened_date");
        helper.setIndexAsc("idx_triggered_value_trigger_id", "happened_date");
        helper.setIndexInclude("idx_triggered_value_trigger_id", "point_id", "column_value",
                "message");
        helper.addIndex("idx_triggered_value_happened_date", PhoenixConstants.IndexType.LOCAL,
                "happened_date");
        helper.setIndexAsc("idx_triggered_value_happened_date", "happened_date");
        helper.setIndexInclude("idx_triggered_value_happened_date", "point_id", "trigger_id",
                "column_value");
        delegate = helper.buildTableDefinition();
    }

    @Override
    public String getSchemaName() {
        return delegate.getSchemaName();
    }

    @Override
    public String getTableName() {
        return delegate.getTableName();
    }

    @Override
    public List<ColumnDefinition> getColumnDefinitions() {
        return delegate.getColumnDefinitions();
    }

    @Override
    public List<ConstraintDefinition> getConstraintDefinitions() {
        return delegate.getConstraintDefinitions();
    }

    @Override
    public List<IndexDefinition> getIndexDefinitions() {
        return delegate.getIndexDefinitions();
    }

    @Override
    public Map<String, Object> getCustomDefinition() {
        return delegate.getCustomDefinition();
    }
}
