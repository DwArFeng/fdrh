package com.dwarfeng.fdrh.impl.dao;

import com.dwarfeng.fdr.stack.bean.entity.TriggeredValue;
import com.dwarfeng.fdr.stack.dao.TriggeredValueDao;
import com.dwarfeng.subgrade.impl.dao.JdbcBatchBaseDao;
import com.dwarfeng.subgrade.impl.dao.JdbcBatchWriteDao;
import com.dwarfeng.subgrade.impl.dao.JdbcEntireLookupDao;
import com.dwarfeng.subgrade.impl.dao.JdbcPresetLookupDao;
import com.dwarfeng.subgrade.sdk.database.definition.TableDefinition;
import com.dwarfeng.subgrade.sdk.database.executor.DatabaseTask;
import com.dwarfeng.subgrade.sdk.database.executor.JdbcDatabaseExecutor;
import com.dwarfeng.subgrade.sdk.interceptor.analyse.BehaviorAnalyse;
import com.dwarfeng.subgrade.stack.bean.dto.PagingInfo;
import com.dwarfeng.subgrade.stack.bean.key.LongIdKey;
import com.dwarfeng.subgrade.stack.exception.DaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Repository
public class TriggeredValueDaoImpl implements TriggeredValueDao {

    @Autowired
    private JdbcBatchBaseDao<LongIdKey, TriggeredValue> batchBaseDao;
    @Autowired
    private JdbcEntireLookupDao<TriggeredValue> entireLookupDao;
    @Autowired
    private JdbcPresetLookupDao<TriggeredValue> presetLookupDao;
    @Autowired
    private JdbcBatchWriteDao<TriggeredValue> batchWriteDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired(required = false)
    @Qualifier("triggeredValueInitDatabaseTask")
    private DatabaseTask<?> initDatabaseTask;

    @Autowired
    @Qualifier("triggeredValueTableDefinition")
    private TableDefinition tableDefinition;

    @PostConstruct
    public void init() {
        if (Objects.nonNull(initDatabaseTask)) {
            JdbcDatabaseExecutor<Object> executor = new JdbcDatabaseExecutor<>(jdbcTemplate);
            executor.executeTask(initDatabaseTask);
        }
    }

    @Override
    @BehaviorAnalyse
    public LongIdKey insert(TriggeredValue entity) throws DaoException {
        return batchBaseDao.insert(entity);
    }

    @Override
    @BehaviorAnalyse
    public void update(TriggeredValue entity) throws DaoException {
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
    public TriggeredValue get(LongIdKey key) throws DaoException {
        return batchBaseDao.get(key);
    }

    @Override
    @BehaviorAnalyse
    public List<LongIdKey> batchInsert(List<TriggeredValue> entities) throws DaoException {
        return batchBaseDao.batchInsert(entities);
    }

    @Override
    @BehaviorAnalyse
    public void batchUpdate(List<TriggeredValue> entities) throws DaoException {
        batchBaseDao.batchUpdate(entities);
    }

    @Override
    @BehaviorAnalyse
    public void batchDelete(List<LongIdKey> keys) throws DaoException {
        batchBaseDao.batchDelete(keys);
    }

    @Override
    @BehaviorAnalyse
    public boolean allExists(List<LongIdKey> keys) throws DaoException {
        return batchBaseDao.allExists(keys);
    }

    @Override
    @BehaviorAnalyse
    public boolean nonExists(List<LongIdKey> keys) throws DaoException {
        return batchBaseDao.nonExists(keys);
    }

    @Override
    @BehaviorAnalyse
    public List<TriggeredValue> batchGet(List<LongIdKey> keys) throws DaoException {
        return batchBaseDao.batchGet(keys);
    }

    @Override
    @BehaviorAnalyse
    public List<TriggeredValue> lookup() throws DaoException {
        return entireLookupDao.lookup();
    }

    @Override
    @BehaviorAnalyse
    public List<TriggeredValue> lookup(PagingInfo pagingInfo) throws DaoException {
        return entireLookupDao.lookup(pagingInfo);
    }

    @Override
    @BehaviorAnalyse
    public int lookupCount() throws DaoException {
        return entireLookupDao.lookupCount();
    }

    @Override
    @BehaviorAnalyse
    public List<TriggeredValue> lookup(String preset, Object[] objs) throws DaoException {
        return presetLookupDao.lookup(preset, objs);
    }

    @Override
    @BehaviorAnalyse
    public List<TriggeredValue> lookup(String preset, Object[] objs, PagingInfo pagingInfo) throws DaoException {
        return presetLookupDao.lookup(preset, objs, pagingInfo);
    }

    @Override
    @BehaviorAnalyse
    public int lookupCount(String preset, Object[] objs) throws DaoException {
        return presetLookupDao.lookupCount(preset, objs);
    }

    @Override
    @BehaviorAnalyse
    public void write(TriggeredValue entity) throws DaoException {
        batchWriteDao.write(entity);
    }

    @Override
    @BehaviorAnalyse
    public void batchWrite(List<TriggeredValue> entities) throws DaoException {
        batchWriteDao.batchWrite(entities);
    }

    @Override
    public TriggeredValue previous(LongIdKey pointKey, Date date) throws DaoException {
        try {
            return DaoUtil.previous(jdbcTemplate, tableDefinition, pointKey, date, resultSet -> {
                if (resultSet.next()) {
                    return new TriggeredValue(
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
