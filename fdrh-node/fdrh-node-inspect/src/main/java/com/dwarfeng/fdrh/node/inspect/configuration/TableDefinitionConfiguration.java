package com.dwarfeng.fdrh.node.inspect.configuration;

import com.dwarfeng.subgrade.sdk.database.definition.ColumnTypes;
import com.dwarfeng.subgrade.sdk.database.definition.PhoenixHelper;
import com.dwarfeng.subgrade.sdk.database.definition.TableDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Collections;

@SuppressWarnings("DuplicatedCode")
@Configuration
public class TableDefinitionConfiguration {

    @Bean("persistenceValueTableDefinition")
    public TableDefinition persistenceValueTableDefinition() {
        TableDefinition tableDefinition = new TableDefinition();
        PhoenixHelper.setSchemaName(tableDefinition, "fdrh");
        PhoenixHelper.setTableName(tableDefinition, "persistence_value");

        PhoenixHelper.addColumn(tableDefinition, "id", ColumnTypes.bigint(), null,
                null);
        PhoenixHelper.addColumn(tableDefinition, "point_id", ColumnTypes.bigint(), null,
                null);
        PhoenixHelper.addColumn(tableDefinition, "happened_date", ColumnTypes.date(),
                null, null);
        PhoenixHelper.addColumn(tableDefinition, "column_value", ColumnTypes.varchar(),
                null, null);

        PhoenixHelper.setPrimaryKey(tableDefinition, Collections.singletonList("id"), Collections.singletonList("id"),
                null, null);

        PhoenixHelper.setTableSaltBuckets(tableDefinition, 30);
        PhoenixHelper.setTableImmutableRows(tableDefinition, true);

        PhoenixHelper.addIndex(tableDefinition, "idx_persistence_value_point_id",
                Arrays.asList("point_id", "happened_date"), PhoenixHelper.IndexType.LOCAL,
                Collections.singletonList("happened_date"), null,
                Collections.singletonList("column_value"), null);
        PhoenixHelper.addIndex(tableDefinition, "idx_persistence_value_happened_date",
                Collections.singletonList("happened_date"), PhoenixHelper.IndexType.LOCAL,
                Collections.singletonList("happened_date"), null,
                Arrays.asList("point_id", "column_value"), null);
        return tableDefinition;
    }

    @Bean("filteredValueTableDefinition")
    public TableDefinition filteredValueTableDefinition() {
        TableDefinition tableDefinition = new TableDefinition();
        PhoenixHelper.setSchemaName(tableDefinition, "fdrh");
        PhoenixHelper.setTableName(tableDefinition, "filtered_value");

        PhoenixHelper.addColumn(tableDefinition, "id", ColumnTypes.bigint(), null,
                null);
        PhoenixHelper.addColumn(tableDefinition, "point_id", ColumnTypes.bigint(), null,
                null);
        PhoenixHelper.addColumn(tableDefinition, "filter_id", ColumnTypes.bigint(), null,
                null);
        PhoenixHelper.addColumn(tableDefinition, "happened_date", ColumnTypes.date(),
                null, null);
        PhoenixHelper.addColumn(tableDefinition, "column_value", ColumnTypes.varchar(),
                null, null);
        PhoenixHelper.addColumn(tableDefinition, "message", ColumnTypes.varchar(),
                null, null);

        PhoenixHelper.setPrimaryKey(tableDefinition, Collections.singletonList("id"), Collections.singletonList("id"),
                null, null);

        PhoenixHelper.setTableSaltBuckets(tableDefinition, 30);
        PhoenixHelper.setTableImmutableRows(tableDefinition, true);

        PhoenixHelper.addIndex(tableDefinition, "idx_filtered_value_point_id",
                Arrays.asList("point_id", "happened_date"), PhoenixHelper.IndexType.LOCAL,
                Collections.singletonList("happened_date"), null,
                Arrays.asList("filter_id", "column_value"), null);
        PhoenixHelper.addIndex(tableDefinition, "idx_filtered_value_filter_id",
                Arrays.asList("filter_id", "happened_date"), PhoenixHelper.IndexType.LOCAL,
                Collections.singletonList("happened_date"), null,
                Arrays.asList("point_id", "column_value"), null);
        PhoenixHelper.addIndex(tableDefinition, "idx_filtered_value_happened_date",
                Collections.singletonList("happened_date"), PhoenixHelper.IndexType.LOCAL,
                Collections.singletonList("happened_date"), null,
                Arrays.asList("point_id", "filter_id", "column_value"), null);
        return tableDefinition;
    }

    @Bean("triggeredValueTableDefinition")
    public TableDefinition triggeredValueTableDefinition() {
        TableDefinition tableDefinition = new TableDefinition();
        PhoenixHelper.setSchemaName(tableDefinition, "fdrh");
        PhoenixHelper.setTableName(tableDefinition, "triggered_value");

        PhoenixHelper.addColumn(tableDefinition, "id", ColumnTypes.bigint(), null,
                null);
        PhoenixHelper.addColumn(tableDefinition, "point_id", ColumnTypes.bigint(), null,
                null);
        PhoenixHelper.addColumn(tableDefinition, "trigger_id", ColumnTypes.bigint(), null,
                null);
        PhoenixHelper.addColumn(tableDefinition, "happened_date", ColumnTypes.date(),
                null, null);
        PhoenixHelper.addColumn(tableDefinition, "column_value", ColumnTypes.varchar(),
                null, null);
        PhoenixHelper.addColumn(tableDefinition, "message", ColumnTypes.varchar(),
                null, null);

        PhoenixHelper.setPrimaryKey(tableDefinition, Collections.singletonList("id"), Collections.singletonList("id"),
                null, null);

        PhoenixHelper.setTableSaltBuckets(tableDefinition, 30);
        PhoenixHelper.setTableImmutableRows(tableDefinition, true);

        PhoenixHelper.addIndex(tableDefinition, "idx_triggered_value_point_id",
                Arrays.asList("point_id", "happened_date"), PhoenixHelper.IndexType.LOCAL,
                Collections.singletonList("happened_date"), null,
                Arrays.asList("trigger_id", "column_value"), null);
        PhoenixHelper.addIndex(tableDefinition, "idx_triggered_value_trigger_id",
                Arrays.asList("trigger_id", "happened_date"), PhoenixHelper.IndexType.LOCAL,
                Collections.singletonList("happened_date"), null,
                Arrays.asList("point_id", "column_value"), null);
        PhoenixHelper.addIndex(tableDefinition, "idx_triggered_value_happened_date",
                Collections.singletonList("happened_date"), PhoenixHelper.IndexType.LOCAL,
                Collections.singletonList("happened_date"), null,
                Arrays.asList("point_id", "trigger_id", "column_value"), null);
        return tableDefinition;
    }
}
