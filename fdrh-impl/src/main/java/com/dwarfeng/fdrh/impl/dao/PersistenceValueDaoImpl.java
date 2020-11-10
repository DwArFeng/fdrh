package com.dwarfeng.fdrh.impl.dao;

import com.dwarfeng.fdr.stack.bean.entity.PersistenceValue;
import com.dwarfeng.fdr.stack.dao.PersistenceValueDao;
import com.dwarfeng.subgrade.impl.dao.JdbcBatchBaseDao;
import com.dwarfeng.subgrade.impl.dao.JdbcBatchWriteDao;
import com.dwarfeng.subgrade.impl.dao.JdbcEntireLookupDao;
import com.dwarfeng.subgrade.impl.dao.JdbcPresetLookupDao;
import com.dwarfeng.subgrade.sdk.database.definition.TableDefinition;
import com.dwarfeng.subgrade.sdk.interceptor.analyse.BehaviorAnalyse;
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
public class PersistenceValueDaoImpl implements PersistenceValueDao {

    @Autowired
    private JdbcBatchBaseDao<LongIdKey, PersistenceValue> batchBaseDao;
    @Autowired
    private JdbcEntireLookupDao<PersistenceValue> entireLookupDao;
    @Autowired
    private JdbcPresetLookupDao<PersistenceValue> presetLookupDao;
    @Autowired
    private JdbcBatchWriteDao<PersistenceValue> batchWriteDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired(required = false)
    @Qualifier("persistenceValueInitDatabaseTask")
    private DatabaseTask<?> initDatabaseTask;

    @Autowired
    @Qualifier("persistenceValueTableDefinition")
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
    public LongIdKey insert(PersistenceValue entity) throws DaoException {
        return batchBaseDao.insert(entity);
    }

    @Override
    @BehaviorAnalyse
    public void update(PersistenceValue entity) throws DaoException {
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
    public PersistenceValue get(LongIdKey key) throws DaoException {
        return batchBaseDao.get(key);
    }

    @Override
    @BehaviorAnalyse
    public List<LongIdKey> batchInsert(List<PersistenceValue> entities) throws DaoException {
        return batchBaseDao.batchInsert(entities);
    }

    @Override
    @BehaviorAnalyse
    public void batchUpdate(List<PersistenceValue> entities) throws DaoException {
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
    public List<PersistenceValue> batchGet(List<LongIdKey> keys) throws DaoException {
        return batchBaseDao.batchGet(keys);
    }

    @Override
    @BehaviorAnalyse
    public List<PersistenceValue> lookup() throws DaoException {
        return entireLookupDao.lookup();
    }

    @Override
    @BehaviorAnalyse
    public List<PersistenceValue> lookup(PagingInfo pagingInfo) throws DaoException {
        return entireLookupDao.lookup(pagingInfo);
    }

    @Override
    @BehaviorAnalyse
    public int lookupCount() throws DaoException {
        return entireLookupDao.lookupCount();
    }

    @Override
    @BehaviorAnalyse
    public List<PersistenceValue> lookup(String preset, Object[] objs) throws DaoException {
        return presetLookupDao.lookup(preset, objs);
    }

    @Override
    @BehaviorAnalyse
    public List<PersistenceValue> lookup(String preset, Object[] objs, PagingInfo pagingInfo) throws DaoException {
        return presetLookupDao.lookup(preset, objs, pagingInfo);
    }

    @Override
    @BehaviorAnalyse
    public int lookupCount(String preset, Object[] objs) throws DaoException {
        return presetLookupDao.lookupCount(preset, objs);
    }

    @Override
    @BehaviorAnalyse
    public void write(PersistenceValue entity) throws DaoException {
        batchWriteDao.write(entity);
    }

    @Override
    @BehaviorAnalyse
    public void batchWrite(List<PersistenceValue> entities) throws DaoException {
        batchWriteDao.batchWrite(entities);
    }

    @Override
    public PersistenceValue previous(LongIdKey pointKey, Date date) throws DaoException {
        try {
            return DaoUtil.previous(jdbcTemplate, tableDefinition, pointKey, date, resultSet -> {
                if (resultSet.next()) {
                    return new PersistenceValue(
                            new LongIdKey(resultSet.getLong(1)),
                            pointKey,
                            new Date(resultSet.getTimestamp(2).getTime()),
                            resultSet.getString(3)
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
