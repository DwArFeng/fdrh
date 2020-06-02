package com.dwarfeng.fdrh.impl.dao;

import com.dwarfeng.fdr.stack.bean.entity.PersistenceValue;
import com.dwarfeng.fdr.stack.dao.PersistenceValueWriteDao;
import com.dwarfeng.subgrade.impl.dao.JdbcBatchWriteDao;
import com.dwarfeng.subgrade.sdk.interceptor.analyse.BehaviorAnalyse;
import com.dwarfeng.subgrade.stack.exception.DaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PersistenceValueWriteDaoImpl implements PersistenceValueWriteDao {

    @Autowired
    private JdbcBatchWriteDao<PersistenceValue> batchWriteDao;

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
}
