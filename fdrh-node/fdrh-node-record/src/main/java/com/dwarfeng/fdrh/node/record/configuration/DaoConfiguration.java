package com.dwarfeng.fdrh.node.record.configuration;

import com.dwarfeng.fdr.impl.bean.entity.HibernateFilteredValue;
import com.dwarfeng.fdr.impl.bean.entity.HibernateTriggeredValue;
import com.dwarfeng.fdr.sdk.bean.entity.FastJsonRealtimeValue;
import com.dwarfeng.fdr.stack.bean.entity.*;
import com.dwarfeng.fdrh.impl.bean.entity.*;
import com.dwarfeng.fdrh.impl.dao.preset.*;
import com.dwarfeng.fdrh.impl.dao.processor.FilteredValueHandle;
import com.dwarfeng.fdrh.impl.dao.processor.PersistenceValueHandle;
import com.dwarfeng.fdrh.impl.dao.processor.TriggeredValueHandle;
import com.dwarfeng.subgrade.impl.bean.DozerBeanTransformer;
import com.dwarfeng.subgrade.impl.dao.*;
import com.dwarfeng.subgrade.sdk.bean.key.HibernateLongIdKey;
import com.dwarfeng.subgrade.sdk.bean.key.HibernateStringIdKey;
import com.dwarfeng.subgrade.sdk.database.ddl.PhoenixCreateTableDatabaseTask;
import com.dwarfeng.subgrade.sdk.database.definition.TableDefinition;
import com.dwarfeng.subgrade.sdk.database.executor.DatabaseTask;
import com.dwarfeng.subgrade.sdk.database.jdbc.PhoenixBatchBaseProcessor;
import com.dwarfeng.subgrade.sdk.database.jdbc.PhoenixBatchWriteProcessor;
import com.dwarfeng.subgrade.sdk.database.jdbc.PhoenixEntireLookupProcessor;
import com.dwarfeng.subgrade.sdk.database.jdbc.PhoenixPresetLookupProcessor;
import com.dwarfeng.subgrade.sdk.hibernate.modification.DefaultDeletionMod;
import com.dwarfeng.subgrade.sdk.redis.formatter.LongIdStringKeyFormatter;
import com.dwarfeng.subgrade.stack.bean.key.LongIdKey;
import com.dwarfeng.subgrade.stack.bean.key.StringIdKey;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate5.HibernateTemplate;

@Configuration
public class DaoConfiguration {

    @Autowired
    private HibernateTemplate hibernateTemplate;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private RedisTemplate<String, ?> redisTemplate;
    @Autowired
    private Mapper mapper;

    @Autowired
    private FilterInfoPresetCriteriaMaker filterInfoPresetCriteriaMaker;
    @Autowired
    private PointPresetCriteriaMaker pointPresetCriteriaMaker;
    @Autowired
    private TriggerInfoPresetCriteriaMaker triggerInfoPresetCriteriaMaker;
    @Autowired
    private FilterSupportPresetCriteriaMaker filterSupportPresetCriteriaMaker;
    @Autowired
    private TriggerSupportPresetCriteriaMaker triggerSupportPresetCriteriaMaker;
    @Autowired
    private MapperSupportPresetCriteriaMaker mapperSupportPresetCriteriaMaker;

    @Autowired
    @Qualifier("persistenceValueTableDefinition")
    private TableDefinition persistenceValueTableDefinition;
    @Autowired
    @Qualifier("filteredValueTableDefinition")
    private TableDefinition filteredValueTableDefinition;
    @Autowired
    @Qualifier("triggeredValueTableDefinition")
    private TableDefinition triggeredValueTableDefinition;

    @Autowired
    private PersistenceValueHandle persistenceValueHandle;
    @Autowired
    private FilteredValueHandle filteredValueHandle;
    @Autowired
    private TriggeredValueHandle triggeredValueHandle;

    @Value("${redis.dbkey.realtime_value}")
    private String realtimeValueDbKey;

    @Value("${hibernate.jdbc.batch_size}")
    private int batchSize;

    @Bean
    public HibernateBatchBaseDao<LongIdKey, HibernateLongIdKey, FilteredValue, HibernateFilteredValue> filteredValueHibernateBatchBaseDao() {
        return new HibernateBatchBaseDao<>(
                hibernateTemplate,
                new DozerBeanTransformer<>(LongIdKey.class, HibernateLongIdKey.class, mapper),
                new DozerBeanTransformer<>(FilteredValue.class, HibernateFilteredValue.class, mapper),
                HibernateFilteredValue.class,
                new DefaultDeletionMod<>(),
                batchSize
        );
    }

    @Bean
    public HibernateEntireLookupDao<FilteredValue, HibernateFilteredValue> filteredValueHibernateEntireLookupDao() {
        return new HibernateEntireLookupDao<>(
                hibernateTemplate,
                new DozerBeanTransformer<>(FilteredValue.class, HibernateFilteredValue.class, mapper),
                HibernateFilteredValue.class
        );
    }

    @Bean
    public HibernateBatchBaseDao<LongIdKey, HibernateLongIdKey, FilterInfo, HibernateFilterInfo> filterInfoHibernateBatchBaseDao() {
        return new HibernateBatchBaseDao<>(
                hibernateTemplate,
                new DozerBeanTransformer<>(LongIdKey.class, HibernateLongIdKey.class, mapper),
                new DozerBeanTransformer<>(FilterInfo.class, HibernateFilterInfo.class, mapper),
                HibernateFilterInfo.class,
                new DefaultDeletionMod<>(),
                batchSize
        );
    }

    @Bean
    public HibernatePresetLookupDao<FilterInfo, HibernateFilterInfo> filterInfoHibernatePresetLookupDao() {
        return new HibernatePresetLookupDao<>(
                hibernateTemplate,
                new DozerBeanTransformer<>(FilterInfo.class, HibernateFilterInfo.class, mapper),
                HibernateFilterInfo.class,
                filterInfoPresetCriteriaMaker
        );
    }

    @Bean
    public HibernateBatchBaseDao<LongIdKey, HibernateLongIdKey, Point, HibernatePoint> pointHibernateBatchBaseDao() {
        return new HibernateBatchBaseDao<>(
                hibernateTemplate,
                new DozerBeanTransformer<>(LongIdKey.class, HibernateLongIdKey.class, mapper),
                new DozerBeanTransformer<>(Point.class, HibernatePoint.class, mapper),
                HibernatePoint.class,
                new DefaultDeletionMod<>(),
                batchSize
        );
    }

    @Bean
    public HibernateEntireLookupDao<Point, HibernatePoint> pointHibernateEntireLookupDao() {
        return new HibernateEntireLookupDao<>(
                hibernateTemplate,
                new DozerBeanTransformer<>(Point.class, HibernatePoint.class, mapper),
                HibernatePoint.class
        );
    }

    @Bean
    public HibernatePresetLookupDao<Point, HibernatePoint> pointHibernatePresetLookupDao() {
        return new HibernatePresetLookupDao<>(
                hibernateTemplate,
                new DozerBeanTransformer<>(Point.class, HibernatePoint.class, mapper),
                HibernatePoint.class,
                pointPresetCriteriaMaker
        );
    }

    @Bean
    @SuppressWarnings("unchecked")
    public RedisBatchBaseDao<LongIdKey, RealtimeValue, FastJsonRealtimeValue> realtimeValueRedisBatchBaseDao() {
        return new RedisBatchBaseDao<>(
                (RedisTemplate<String, FastJsonRealtimeValue>) redisTemplate,
                new LongIdStringKeyFormatter("key."),
                new DozerBeanTransformer<>(RealtimeValue.class, FastJsonRealtimeValue.class, mapper),
                realtimeValueDbKey
        );
    }

    @Bean
    @SuppressWarnings("unchecked")
    public RedisEntireLookupDao<LongIdKey, RealtimeValue, FastJsonRealtimeValue> realtimeValueRedisEntireLookupDao() {
        return new RedisEntireLookupDao<>(
                (RedisTemplate<String, FastJsonRealtimeValue>) redisTemplate,
                new LongIdStringKeyFormatter("key."),
                new DozerBeanTransformer<>(RealtimeValue.class, FastJsonRealtimeValue.class, mapper),
                realtimeValueDbKey
        );
    }

    @Bean
    public HibernateBatchBaseDao<LongIdKey, HibernateLongIdKey, TriggeredValue, HibernateTriggeredValue> triggeredValueHibernateBatchBaseDao() {
        return new HibernateBatchBaseDao<>(
                hibernateTemplate,
                new DozerBeanTransformer<>(LongIdKey.class, HibernateLongIdKey.class, mapper),
                new DozerBeanTransformer<>(TriggeredValue.class, HibernateTriggeredValue.class, mapper),
                HibernateTriggeredValue.class,
                new DefaultDeletionMod<>(),
                batchSize
        );
    }

    @Bean
    public HibernateEntireLookupDao<TriggeredValue, HibernateTriggeredValue> triggeredValueHibernateEntireLookupDao() {
        return new HibernateEntireLookupDao<>(
                hibernateTemplate,
                new DozerBeanTransformer<>(TriggeredValue.class, HibernateTriggeredValue.class, mapper),
                HibernateTriggeredValue.class
        );
    }

    @Bean
    public HibernateBatchBaseDao<LongIdKey, HibernateLongIdKey, TriggerInfo, HibernateTriggerInfo> triggerInfoHibernateBatchBaseDao() {
        return new HibernateBatchBaseDao<>(
                hibernateTemplate,
                new DozerBeanTransformer<>(LongIdKey.class, HibernateLongIdKey.class, mapper),
                new DozerBeanTransformer<>(TriggerInfo.class, HibernateTriggerInfo.class, mapper),
                HibernateTriggerInfo.class,
                new DefaultDeletionMod<>(),
                batchSize
        );
    }

    @Bean
    public HibernatePresetLookupDao<TriggerInfo, HibernateTriggerInfo> triggerInfoHibernatePresetLookupDao() {
        return new HibernatePresetLookupDao<>(
                hibernateTemplate,
                new DozerBeanTransformer<>(TriggerInfo.class, HibernateTriggerInfo.class, mapper),
                HibernateTriggerInfo.class,
                triggerInfoPresetCriteriaMaker
        );
    }

    @Bean
    public HibernateBaseDao<StringIdKey, HibernateStringIdKey, FilterSupport, HibernateFilterSupport> filterSupportHibernateBaseDao() {
        return new HibernateBaseDao<>(
                hibernateTemplate,
                new DozerBeanTransformer<>(StringIdKey.class, HibernateStringIdKey.class, mapper),
                new DozerBeanTransformer<>(FilterSupport.class, HibernateFilterSupport.class, mapper),
                HibernateFilterSupport.class
        );
    }

    @Bean
    public HibernateEntireLookupDao<FilterSupport, HibernateFilterSupport> filterSupportHibernateEntireLookupDao() {
        return new HibernateEntireLookupDao<>(
                hibernateTemplate,
                new DozerBeanTransformer<>(FilterSupport.class, HibernateFilterSupport.class, mapper),
                HibernateFilterSupport.class
        );
    }

    @Bean
    public HibernatePresetLookupDao<FilterSupport, HibernateFilterSupport> filterSupportHibernatePresetLookupDao() {
        return new HibernatePresetLookupDao<>(
                hibernateTemplate,
                new DozerBeanTransformer<>(FilterSupport.class, HibernateFilterSupport.class, mapper),
                HibernateFilterSupport.class,
                filterSupportPresetCriteriaMaker
        );
    }

    @Bean
    public HibernateBaseDao<StringIdKey, HibernateStringIdKey, TriggerSupport, HibernateTriggerSupport> triggerSupportHibernateBaseDao() {
        return new HibernateBaseDao<>(
                hibernateTemplate,
                new DozerBeanTransformer<>(StringIdKey.class, HibernateStringIdKey.class, mapper),
                new DozerBeanTransformer<>(TriggerSupport.class, HibernateTriggerSupport.class, mapper),
                HibernateTriggerSupport.class
        );
    }

    @Bean
    public HibernateEntireLookupDao<TriggerSupport, HibernateTriggerSupport> triggerSupportHibernateEntireLookupDao() {
        return new HibernateEntireLookupDao<>(
                hibernateTemplate,
                new DozerBeanTransformer<>(TriggerSupport.class, HibernateTriggerSupport.class, mapper),
                HibernateTriggerSupport.class
        );
    }

    @Bean
    public HibernatePresetLookupDao<TriggerSupport, HibernateTriggerSupport> triggerSupportHibernatePresetLookupDao() {
        return new HibernatePresetLookupDao<>(
                hibernateTemplate,
                new DozerBeanTransformer<>(TriggerSupport.class, HibernateTriggerSupport.class, mapper),
                HibernateTriggerSupport.class,
                triggerSupportPresetCriteriaMaker
        );
    }

    @Bean
    public HibernateEntireLookupDao<FilterInfo, HibernateFilterInfo> filterInfoHibernateEntireLookupDao() {
        return new HibernateEntireLookupDao<>(
                hibernateTemplate,
                new DozerBeanTransformer<>(FilterInfo.class, HibernateFilterInfo.class, mapper),
                HibernateFilterInfo.class
        );
    }

    @Bean
    public HibernateEntireLookupDao<TriggerInfo, HibernateTriggerInfo> triggerInfoHibernateEntireLookupDao() {
        return new HibernateEntireLookupDao<>(
                hibernateTemplate,
                new DozerBeanTransformer<>(TriggerInfo.class, HibernateTriggerInfo.class, mapper),
                HibernateTriggerInfo.class
        );
    }

    @Bean
    public HibernateBaseDao<StringIdKey, HibernateStringIdKey, MapperSupport, HibernateMapperSupport> mapperSupportHibernateBaseDao() {
        return new HibernateBaseDao<>(
                hibernateTemplate,
                new DozerBeanTransformer<>(StringIdKey.class, HibernateStringIdKey.class, mapper),
                new DozerBeanTransformer<>(MapperSupport.class, HibernateMapperSupport.class, mapper),
                HibernateMapperSupport.class
        );
    }

    @Bean
    public HibernateEntireLookupDao<MapperSupport, HibernateMapperSupport> mapperSupportHibernateEntireLookupDao() {
        return new HibernateEntireLookupDao<>(
                hibernateTemplate,
                new DozerBeanTransformer<>(MapperSupport.class, HibernateMapperSupport.class, mapper),
                HibernateMapperSupport.class
        );
    }

    @Bean
    public HibernatePresetLookupDao<MapperSupport, HibernateMapperSupport> mapperSupportHibernatePresetLookupDao() {
        return new HibernatePresetLookupDao<>(
                hibernateTemplate,
                new DozerBeanTransformer<>(MapperSupport.class, HibernateMapperSupport.class, mapper),
                HibernateMapperSupport.class,
                mapperSupportPresetCriteriaMaker
        );
    }

    @Bean
    public HibernateBatchWriteDao<FilteredValue, HibernateFilteredValue> filteredValueHibernateBatchWriteDao() {
        return new HibernateBatchWriteDao<>(
                hibernateTemplate,
                new DozerBeanTransformer<>(FilteredValue.class, HibernateFilteredValue.class, mapper),
                batchSize
        );
    }

    @Bean
    public HibernateBatchWriteDao<TriggeredValue, HibernateTriggeredValue> triggeredValueHibernateBatchWriteDao() {
        return new HibernateBatchWriteDao<>(
                hibernateTemplate,
                new DozerBeanTransformer<>(TriggeredValue.class, HibernateTriggeredValue.class, mapper),
                batchSize
        );
    }

    @Bean
    public JdbcBatchBaseDao<LongIdKey, PersistenceValue> persistenceValueJdbcBatchBaseDao() {
        return new JdbcBatchBaseDao<>(
                jdbcTemplate,
                new PhoenixBatchBaseProcessor<>(persistenceValueTableDefinition, persistenceValueHandle)
        );
    }

    @Bean
    public JdbcBatchWriteDao<PersistenceValue> persistenceValueJdbcBatchWriteDao() {
        return new JdbcBatchWriteDao<>(
                jdbcTemplate,
                new PhoenixBatchWriteProcessor<>(persistenceValueTableDefinition, persistenceValueHandle)
        );
    }

    @Bean
    public JdbcEntireLookupDao<PersistenceValue> persistenceValueJdbcEntireLookupDao() {
        return new JdbcEntireLookupDao<>(
                jdbcTemplate,
                new PhoenixEntireLookupProcessor<>(persistenceValueTableDefinition, persistenceValueHandle)
        );
    }

    @Bean
    public JdbcPresetLookupDao<PersistenceValue> persistenceValueJdbcPresetLookupDao() {
        return new JdbcPresetLookupDao<>(
                jdbcTemplate,
                new PhoenixPresetLookupProcessor<>(persistenceValueTableDefinition, persistenceValueHandle)
        );
    }

    @Bean("persistenceValueInitDatabaseTask")
    public DatabaseTask<?> persistenceValueInitDatabaseTask() {
        return new PhoenixCreateTableDatabaseTask(persistenceValueTableDefinition);
    }

    @Bean
    public JdbcBatchBaseDao<LongIdKey, FilteredValue> filteredValueJdbcBatchBaseDao() {
        return new JdbcBatchBaseDao<>(
                jdbcTemplate,
                new PhoenixBatchBaseProcessor<>(filteredValueTableDefinition, filteredValueHandle)
        );
    }

    @Bean
    public JdbcBatchWriteDao<FilteredValue> filteredValueJdbcBatchWriteDao() {
        return new JdbcBatchWriteDao<>(
                jdbcTemplate,
                new PhoenixBatchWriteProcessor<>(filteredValueTableDefinition, filteredValueHandle)
        );
    }

    @Bean
    public JdbcEntireLookupDao<FilteredValue> filteredValueJdbcEntireLookupDao() {
        return new JdbcEntireLookupDao<>(
                jdbcTemplate,
                new PhoenixEntireLookupProcessor<>(filteredValueTableDefinition, filteredValueHandle)
        );
    }

    @Bean
    public JdbcPresetLookupDao<FilteredValue> filteredValueJdbcPresetLookupDao() {
        return new JdbcPresetLookupDao<>(
                jdbcTemplate,
                new PhoenixPresetLookupProcessor<>(filteredValueTableDefinition, filteredValueHandle)
        );
    }

    @Bean("filteredValueInitDatabaseTask")
    public DatabaseTask<?> filteredValueInitDatabaseTask() {
        return new PhoenixCreateTableDatabaseTask(filteredValueTableDefinition);
    }

    @Bean
    public JdbcBatchBaseDao<LongIdKey, TriggeredValue> triggeredValueJdbcBatchBaseDao() {
        return new JdbcBatchBaseDao<>(
                jdbcTemplate,
                new PhoenixBatchBaseProcessor<>(triggeredValueTableDefinition, triggeredValueHandle)
        );
    }

    @Bean
    public JdbcBatchWriteDao<TriggeredValue> triggeredValueJdbcBatchWriteDao() {
        return new JdbcBatchWriteDao<>(
                jdbcTemplate,
                new PhoenixBatchWriteProcessor<>(triggeredValueTableDefinition, triggeredValueHandle)
        );
    }

    @Bean
    public JdbcEntireLookupDao<TriggeredValue> triggeredValueJdbcEntireLookupDao() {
        return new JdbcEntireLookupDao<>(
                jdbcTemplate,
                new PhoenixEntireLookupProcessor<>(triggeredValueTableDefinition, triggeredValueHandle)
        );
    }

    @Bean
    public JdbcPresetLookupDao<TriggeredValue> triggeredValueJdbcPresetLookupDao() {
        return new JdbcPresetLookupDao<>(
                jdbcTemplate,
                new PhoenixPresetLookupProcessor<>(triggeredValueTableDefinition, triggeredValueHandle)
        );
    }

    @Bean("triggeredValueInitDatabaseTask")
    public DatabaseTask<?> triggeredValueInitDatabaseTask() {
        return new PhoenixCreateTableDatabaseTask(triggeredValueTableDefinition);
    }
}
