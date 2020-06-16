package com.dwarfeng.fdrh.impl.dao;

import com.dwarfeng.fdr.stack.bean.entity.TriggeredValue;
import com.dwarfeng.fdr.stack.dao.TriggeredValueDao;
import com.dwarfeng.fdr.stack.service.TriggeredValueMaintainService;
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
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/application-context*.xml")
public class TriggeredValueDaoImplTest {

    private final List<TriggeredValue> triggeredValues = new ArrayList<>();
    @Autowired
    private TriggeredValueDao triggeredValueDao;

    @Before
    public void setUp() {
        for (int i = 0; i < 5; i++) {
            triggeredValues.add(new TriggeredValue(
                    new LongIdKey(Long.MIN_VALUE + i),
                    new LongIdKey(-1L),
                    new LongIdKey(-10L),
                    new Date(i),
                    "110101010266",
                    "this is a test"
            ));
        }
        for (int i = 0; i < 5; i++) {
            triggeredValues.add(new TriggeredValue(
                    new LongIdKey(Long.MIN_VALUE + i + 10),
                    new LongIdKey(-2L),
                    new LongIdKey(-20L),
                    new Date(100 + i),
                    "110101010266",
                    "this is a test"
            ));
        }
        for (int i = 0; i < 5; i++) {
            triggeredValues.add(new TriggeredValue(
                    new LongIdKey(Long.MIN_VALUE + i + 20),
                    new LongIdKey(-3L),
                    new LongIdKey(-30L),
                    new Date(200 + i),
                    "110101010266",
                    "this is a test"
            ));
        }
    }

    @After
    public void tearDown() {
        triggeredValues.clear();
    }

    @Test
    public void crudTest() throws Exception {
        try {
            for (TriggeredValue triggeredValue : triggeredValues) {
                assertFalse(triggeredValueDao.exists(triggeredValue.getKey()));
            }

            for (TriggeredValue triggeredValue : triggeredValues) {
                if (!triggeredValueDao.exists(triggeredValue.getKey())) {
                    triggeredValueDao.insert(triggeredValue);
                }
            }
            for (TriggeredValue triggeredValue : triggeredValues) {
                assertTrue(triggeredValueDao.exists(triggeredValue.getKey()));
                TriggeredValue another = triggeredValueDao.get(triggeredValue.getKey());
                another.setHappenedDate(new Date(another.getHappenedDate().getTime()));
                assertEquals(PropertyUtils.describe(another), PropertyUtils.describe(triggeredValue));
            }
        } finally {
            for (TriggeredValue triggeredValue : triggeredValues) {
                if (triggeredValueDao.exists(triggeredValue.getKey())) {
                    triggeredValueDao.delete(triggeredValue.getKey());
                }
            }
        }
    }

    @Test
    public void batchCrudTest() throws Exception {
        try {
            for (TriggeredValue triggeredValue : triggeredValues) {
                if (triggeredValueDao.exists(triggeredValue.getKey())) {
                    triggeredValueDao.delete(triggeredValue.getKey());
                }
            }
            assertTrue(triggeredValueDao.nonExists(
                    triggeredValues.stream().map(TriggeredValue::getKey).collect(Collectors.toList())));
            assertFalse(triggeredValueDao.allExists(
                    triggeredValues.stream().map(TriggeredValue::getKey).collect(Collectors.toList())));
            triggeredValueDao.batchInsert(triggeredValues);
            assertFalse(triggeredValueDao.nonExists(
                    triggeredValues.stream().map(TriggeredValue::getKey).collect(Collectors.toList())));
            assertTrue(triggeredValueDao.allExists(
                    triggeredValues.stream().map(TriggeredValue::getKey).collect(Collectors.toList())));
            List<TriggeredValue> anotherList = triggeredValueDao.batchGet(
                    this.triggeredValues.stream().map(TriggeredValue::getKey).collect(Collectors.toList()));
            assertEquals(triggeredValues.size(), anotherList.size());
            for (int i = 0; i < triggeredValues.size(); i++) {
                TriggeredValue triggeredValue = triggeredValues.get(i);
                TriggeredValue another = anotherList.get(i);
                another.setHappenedDate(new Date(another.getHappenedDate().getTime()));
                assertEquals(PropertyUtils.describe(another), PropertyUtils.describe(triggeredValue));
                another.setHappenedDate(new Date(another.getHappenedDate().getTime()));
                assertEquals(PropertyUtils.describe(another), PropertyUtils.describe(triggeredValue));
            }
        } finally {
            for (TriggeredValue triggeredValue : triggeredValues) {
                if (triggeredValueDao.exists(triggeredValue.getKey())) {
                    triggeredValueDao.delete(triggeredValue.getKey());
                }
            }
        }
    }

    @Test
    public void presetLookupTest() throws Exception {
        try {
            for (TriggeredValue triggeredValue : triggeredValues) {
                if (!triggeredValueDao.exists(triggeredValue.getKey())) {
                    triggeredValueDao.insert(triggeredValue);
                }
            }
            assertEquals(5, triggeredValueDao.lookupCount(TriggeredValueMaintainService.CHILD_FOR_POINT,
                    new Object[]{new LongIdKey(-1L)}));
            assertEquals(5, triggeredValueDao.lookupCount(TriggeredValueMaintainService.BETWEEN,
                    new Object[]{new Date(100L), new Date(199L)}));
            assertEquals(0, triggeredValueDao.lookupCount(TriggeredValueMaintainService.CHILD_FOR_POINT_BETWEEN,
                    new Object[]{new LongIdKey(-1L), new Date(100L), new Date(199L)}));
            assertEquals(5, triggeredValueDao.lookupCount(TriggeredValueMaintainService.CHILD_FOR_POINT_BETWEEN,
                    new Object[]{new LongIdKey(-1L), new Date(0L), new Date(999L)}));
            assertEquals(5, triggeredValueDao.lookupCount(TriggeredValueMaintainService.CHILD_FOR_TRIGGER,
                    new Object[]{new LongIdKey(-10L)}));
            assertEquals(15, triggeredValueDao.lookupCount(TriggeredValueMaintainService.CHILD_FOR_TRIGGER_SET,
                    new Object[]{Arrays.asList(new LongIdKey(-10L), new LongIdKey(-20L), new LongIdKey(-30L))}));
            assertEquals(0, triggeredValueDao.lookupCount(TriggeredValueMaintainService.CHILD_FOR_TRIGGER_BETWEEN,
                    new Object[]{new LongIdKey(-10L), new Date(100L), new Date(199L)}));
            assertEquals(5, triggeredValueDao.lookupCount(TriggeredValueMaintainService.CHILD_FOR_TRIGGER_BETWEEN,
                    new Object[]{new LongIdKey(-10L), new Date(0L), new Date(999L)}));
        } finally {
            for (TriggeredValue triggeredValue : triggeredValues) {
                if (triggeredValueDao.exists(triggeredValue.getKey())) {
                    triggeredValueDao.delete(triggeredValue.getKey());
                }
            }
        }
    }
}
