package com.dwarfeng.fdrh.impl.dao.definition;

import com.dwarfeng.subgrade.sdk.jdbc.definition.PhoenixConstants;
import com.dwarfeng.subgrade.sdk.jdbc.definition.PhoenixTableDefinitionHelper;
import com.dwarfeng.subgrade.sdk.jdbc.definition.TableDefinition;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class FilteredValueTableDefinition implements TableDefinition {

    private final TableDefinition delegate;

    public FilteredValueTableDefinition() {
        PhoenixTableDefinitionHelper helper = new PhoenixTableDefinitionHelper();
        helper.setTableName("fdrh.filtered_value");
        helper.addColumn("id", "BIGINT");
        helper.addColumn("point_id", "BIGINT");
        helper.addColumn("filter_id", "BIGINT");
        helper.addColumn("happened_date", "DATE");
        helper.addColumn("column_value", "VARCHAR");
        helper.addColumn("message", "VARCHAR");
        helper.setPrimaryKey("id");
        helper.setPrimaryKeyAsc("id");
        helper.setTableSaltBuckets(30);
        helper.setTableImmutableRows(true);
        helper.addIndex("idx_filtered_value_point_id", PhoenixConstants.IndexType.LOCAL,
                "point_id", "happened_date");
        helper.setIndexAsc("idx_filtered_value_point_id", "happened_date");
        helper.setIndexInclude("idx_filtered_value_point_id", "filter_id", "column_value",
                "message");
        helper.addIndex("idx_filtered_value_filter_id", PhoenixConstants.IndexType.LOCAL,
                "filter_id", "happened_date");
        helper.setIndexAsc("idx_filtered_value_filter_id", "happened_date");
        helper.setIndexInclude("idx_filtered_value_filter_id", "point_id", "column_value",
                "message");
        helper.addIndex("idx_filtered_value_happened_date", PhoenixConstants.IndexType.LOCAL,
                "happened_date");
        helper.setIndexAsc("idx_filtered_value_happened_date", "happened_date");
        helper.setIndexInclude("idx_filtered_value_happened_date", "point_id", "filter_id",
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
