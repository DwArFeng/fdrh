package com.dwarfeng.fdrh.impl.dao;

import com.dwarfeng.fdr.stack.bean.entity.PersistenceValue;
import com.dwarfeng.fdr.stack.dao.PersistenceValueDao;
import com.dwarfeng.fdr.stack.service.PersistenceValueMaintainService;
import com.dwarfeng.subgrade.stack.bean.key.LongIdKey;
import org.apache.commons.beanutils.PropertyUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/application-context*.xml")
public class PersistenceValueDaoImplTest {

    @Autowired
    private PersistenceValueDao persistenceValueDao;

    private final List<PersistenceValue> persistenceValues = new ArrayList<>();

    @Before
    public void setUp() {
        for (int i = 0; i < 5; i++) {
            persistenceValues.add(new PersistenceValue(
                    new LongIdKey(Long.MIN_VALUE + i),
                    new LongIdKey(-1L),
                    new Date(i),
                    "110101010266"
            ));
        }
        for (int i = 0; i < 5; i++) {
            persistenceValues.add(new PersistenceValue(
                    new LongIdKey(Long.MIN_VALUE + i + 10),
                    new LongIdKey(-2L),
                    new Date(100 + i),
                    "110101010266"
            ));
        }
        for (int i = 0; i < 5; i++) {
            persistenceValues.add(new PersistenceValue(
                    new LongIdKey(Long.MIN_VALUE + i + 20),
                    new LongIdKey(-3L),
                    new Date(200 + i),
                    "110101010266"
            ));
        }
    }

    @After
    public void tearDown() {
        persistenceValues.clear();
    }

    @Test
    public void crudTest() throws Exception {
        try {
            for (PersistenceValue persistenceValue : persistenceValues) {
                assertFalse(persistenceValueDao.exists(persistenceValue.getKey()));
            }

            for (PersistenceValue persistenceValue : persistenceValues) {
                if (!persistenceValueDao.exists(persistenceValue.getKey())) {
                    persistenceValueDao.insert(persistenceValue);
                }
            }
            for (PersistenceValue persistenceValue : persistenceValues) {
                assertTrue(persistenceValueDao.exists(persistenceValue.getKey()));
                PersistenceValue another = persistenceValueDao.get(persistenceValue.getKey());
                another.setHappenedDate(new Date(another.getHappenedDate().getTime()));
                assertEquals(PropertyUtils.describe(another), PropertyUtils.describe(persistenceValue));
            }
        } finally {
            for (PersistenceValue persistenceValue : persistenceValues) {
                if (persistenceValueDao.exists(persistenceValue.getKey())) {
                    persistenceValueDao.delete(persistenceValue.getKey());
                }
            }
        }
    }

    @Test
    public void batchCrudTest() throws Exception {
        try {
            for (PersistenceValue persistenceValue : persistenceValues) {
                if (persistenceValueDao.exists(persistenceValue.getKey())) {
                    persistenceValueDao.delete(persistenceValue.getKey());
                }
            }
            assertTrue(persistenceValueDao.nonExists(
                    persistenceValues.stream().map(PersistenceValue::getKey).collect(Collectors.toList())));
            assertFalse(persistenceValueDao.allExists(
                    persistenceValues.stream().map(PersistenceValue::getKey).collect(Collectors.toList())));
            persistenceValueDao.batchInsert(persistenceValues);
            assertFalse(persistenceValueDao.nonExists(
                    persistenceValues.stream().map(PersistenceValue::getKey).collect(Collectors.toList())));
            assertTrue(persistenceValueDao.allExists(
                    persistenceValues.stream().map(PersistenceValue::getKey).collect(Collectors.toList())));
            List<PersistenceValue> anotherList = persistenceValueDao.batchGet(
                    this.persistenceValues.stream().map(PersistenceValue::getKey).collect(Collectors.toList()));
            assertEquals(persistenceValues.size(), anotherList.size());
            for (int i = 0; i < persistenceValues.size(); i++) {
                PersistenceValue persistenceValue = persistenceValues.get(i);
                PersistenceValue another = anotherList.get(i);
                another.setHappenedDate(new Date(another.getHappenedDate().getTime()));
                assertEquals(PropertyUtils.describe(another), PropertyUtils.describe(persistenceValue));
                another.setHappenedDate(new Date(another.getHappenedDate().getTime()));
                assertEquals(PropertyUtils.describe(another), PropertyUtils.describe(persistenceValue));
            }
        } finally {
            for (PersistenceValue persistenceValue : persistenceValues) {
                if (persistenceValueDao.exists(persistenceValue.getKey())) {
                    persistenceValueDao.delete(persistenceValue.getKey());
                }
            }
        }
    }

    @Test
    public void presetLookupTest() throws Exception {
        try {
            for (PersistenceValue persistenceValue : persistenceValues) {
                if (!persistenceValueDao.exists(persistenceValue.getKey())) {
                    persistenceValueDao.insert(persistenceValue);
                }
            }
            assertEquals(5, persistenceValueDao.lookupCount(PersistenceValueMaintainService.CHILD_FOR_POINT,
                    new Object[]{new LongIdKey(-1L)}));
            assertEquals(5, persistenceValueDao.lookupCount(PersistenceValueMaintainService.BETWEEN,
                    new Object[]{new Date(100L), new Date(199L)}));
            assertEquals(0, persistenceValueDao.lookupCount(PersistenceValueMaintainService.CHILD_FOR_POINT_BETWEEN,
                    new Object[]{new LongIdKey(-1L), new Date(100L), new Date(199L)}));
            assertEquals(5, persistenceValueDao.lookupCount(PersistenceValueMaintainService.CHILD_FOR_POINT_BETWEEN,
                    new Object[]{new LongIdKey(-1L), new Date(0L), new Date(999L)}));
        } finally {
            for (PersistenceValue persistenceValue : persistenceValues) {
                if (persistenceValueDao.exists(persistenceValue.getKey())) {
                    persistenceValueDao.delete(persistenceValue.getKey());
                }
            }
        }
    }

    @Test
    public void previousTest() throws Exception {
        try {
            for (PersistenceValue persistenceValue : persistenceValues) {
                if (!persistenceValueDao.exists(persistenceValue.getKey())) {
                    persistenceValueDao.insert(persistenceValue);
                }
            }
            PersistenceValue previous = persistenceValueDao.previous(new LongIdKey(-2L), new Date(101));
            assertNotNull(previous);
            assertEquals(persistenceValues.get(5).getKey(), previous.getKey());
            previous = persistenceValueDao.previous(new LongIdKey(-2L), new Date(100));
            assertNull(previous);
        } finally {
            for (PersistenceValue persistenceValue : persistenceValues) {
                if (persistenceValueDao.exists(persistenceValue.getKey())) {
                    persistenceValueDao.delete(persistenceValue.getKey());
                }
            }
        }
    }
}
