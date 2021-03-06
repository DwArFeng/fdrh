package com.dwarfeng.fdrh.impl.dao;

import com.dwarfeng.fdr.stack.bean.entity.FilteredValue;
import com.dwarfeng.fdr.stack.dao.FilteredValueDao;
import com.dwarfeng.subgrade.impl.dao.JdbcBatchBaseDao;
import com.dwarfeng.subgrade.impl.dao.JdbcBatchWriteDao;
import com.dwarfeng.subgrade.impl.dao.JdbcEntireLookupDao;
import com.dwarfeng.subgrade.impl.dao.JdbcPresetLookupDao;
import com.dwarfeng.subgrade.sdk.database.definition.TableDefinition;
import com.dwarfeng.subgrade.sdk.interceptor.analyse.BehaviorAnalyse;
import com.dwarfeng.subgrade.sdk.interceptor.analyse.SkipRecord;
import com.dwarfeng.subgrade.stack.bean.dto.PagingInfo;
import com.dwarfeng.subgrade.stack.bean.key.LongIdKey;
import com.dwarfeng.subgrade.stack.exception.DaoException;
import com.dwarfeng.subgrade.stack.handler.DatabaseHandler;
import com.dwarfeng.subgrade.stack.handler.DatabaseTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Repository
public class FilteredValueDaoImpl implements FilteredValueDao {

    @Autowired
    private JdbcBatchBaseDao<LongIdKey, FilteredValue> batchBaseDao;
    @Autowired
    private JdbcEntireLookupDao<FilteredValue> entireLookupDao;
    @Autowired
    private JdbcPresetLookupDao<FilteredValue> presetLookupDao;
    @Autowired
    private JdbcBatchWriteDao<FilteredValue> batchWriteDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired(required = false)
    @Qualifier("filteredValueInitDatabaseTask")
    private DatabaseTask<?> initDatabaseTask;

    @Autowired
    @Qualifier("filteredValueTableDefinition")
    private TableDefinition tableDefinition;

    @Autowired
    private DatabaseHandler<Object> databaseHandler;

    @PostConstruct
    public void init() throws Exception {
        if (Objects.nonNull(initDatabaseTask)) {
            databaseHandler.executeTask(initDatabaseTask);
        }
    }

    @Override
    @BehaviorAnalyse
    public LongIdKey insert(FilteredValue entity) throws DaoException {
        return batchBaseDao.insert(entity);
    }

    @Override
    @BehaviorAnalyse
    public void update(FilteredValue entity) throws DaoException {
        batchBaseDao.update(entity);
    }

    @Override
    @BehaviorAnalyse
    public void delete(LongIdKey key) throws DaoException {
        batchBaseDao.delete(key);
    }

    @Override
    @BehaviorAnalyse
    public boolean exists(LongIdKey key) throws DaoException {
        return batchBaseDao.exists(key);
    }

    @Override
    @BehaviorAnalyse
    public FilteredValue get(LongIdKey key) throws DaoException {
        return batchBaseDao.get(key);
    }

    @Override
    @BehaviorAnalyse
    @SkipRecord
    public List<LongIdKey> batchInsert(@SkipRecord List<FilteredValue> entities) throws DaoException {
        return batchBaseDao.batchInsert(entities);
    }

    @Override
    @BehaviorAnalyse
    public void batchUpdate(@SkipRecord List<FilteredValue> entities) throws DaoException {
        batchBaseDao.batchUpdate(entities);
    }

    @Override
    @BehaviorAnalyse
    public void batchDelete(@SkipRecord List<LongIdKey> keys) throws DaoException {
        batchBaseDao.batchDelete(keys);
    }

    @Override
    @BehaviorAnalyse
    public boolean allExists(@SkipRecord List<LongIdKey> keys) throws DaoException {
        return batchBaseDao.allExists(keys);
    }

    @Override
    @BehaviorAnalyse
    public boolean nonExists(@SkipRecord List<LongIdKey> keys) throws DaoException {
        return batchBaseDao.nonExists(keys);
    }

    @Override
    @BehaviorAnalyse
    @SkipRecord
    public List<FilteredValue> batchGet(@SkipRecord List<LongIdKey> keys) throws DaoException {
        return batchBaseDao.batchGet(keys);
    }

    @Override
    @BehaviorAnalyse
    @SkipRecord
    public List<FilteredValue> lookup() throws DaoException {
        return entireLookupDao.lookup();
    }

    @Override
    @BehaviorAnalyse
    @SkipRecord
    public List<FilteredValue> lookup(PagingInfo pagingInfo) throws DaoException {
        return entireLookupDao.lookup(pagingInfo);
    }

    @Override
    @BehaviorAnalyse
    public int lookupCount() throws DaoException {
        return entireLookupDao.lookupCount();
    }

    @Override
    @BehaviorAnalyse
    @SkipRecord
    public List<FilteredValue> lookup(String preset, Object[] objs) throws DaoException {
        return presetLookupDao.lookup(preset, objs);
    }

    @Override
    @BehaviorAnalyse
    @SkipRecord
    public List<FilteredValue> lookup(String preset, Object[] objs, PagingInfo pagingInfo) throws DaoException {
        return presetLookupDao.lookup(preset, objs, pagingInfo);
    }

    @Override
    @BehaviorAnalyse
    public int lookupCount(String preset, Object[] objs) throws DaoException {
        return presetLookupDao.lookupCount(preset, objs);
    }

    @Override
    @BehaviorAnalyse
    public void write(FilteredValue entity) throws DaoException {
        batchWriteDao.write(entity);
    }

    @Override
    @BehaviorAnalyse
    public void batchWrite(@SkipRecord List<FilteredValue> entities) throws DaoException {
        batchWriteDao.batchWrite(entities);
    }

    @Override
    @BehaviorAnalyse
    public FilteredValue previous(LongIdKey pointKey, Date date) throws DaoException {
        try {
            return DaoUtil.previous(jdbcTemplate, tableDefinition, pointKey, date, resultSet -> {
                if (resultSet.next()) {
                    return new FilteredValue(
                            new LongIdKey(resultSet.getLong(1)),
                            pointKey,
                            new LongIdKey(resultSet.getLong(2)),
                            new Date(resultSet.getTimestamp(3).getTime()),
                            resultSet.getString(4),
                            resultSet.getString(5)
                    );
                } else {
                    return null;
                }
            });
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }
}
