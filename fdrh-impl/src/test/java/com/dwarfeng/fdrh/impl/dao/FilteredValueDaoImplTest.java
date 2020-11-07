package com.dwarfeng.fdrh.impl.dao;

import com.dwarfeng.fdr.stack.bean.entity.FilteredValue;
import com.dwarfeng.fdr.stack.dao.FilteredValueDao;
import com.dwarfeng.fdr.stack.service.FilteredValueMaintainService;
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
public class FilteredValueDaoImplTest {

    private final List<FilteredValue> filteredValues = new ArrayList<>();
    @Autowired
    private FilteredValueDao filteredValueDao;

    @Before
    public void setUp() {
        for (int i = 0; i < 5; i++) {
            filteredValues.add(new FilteredValue(
                    new LongIdKey(Long.MIN_VALUE + i),
                    new LongIdKey(-1L),
                    new LongIdKey(-10L),
                    new Date(i),
                    "110101010266",
                    "this is a test"
            ));
        }
        for (int i = 0; i < 5; i++) {
            filteredValues.add(new FilteredValue(
                    new LongIdKey(Long.MIN_VALUE + i + 10),
                    new LongIdKey(-2L),
                    new LongIdKey(-20L),
                    new Date(100 + i),
                    "110101010266",
                    "this is a test"
            ));
        }
        for (int i = 0; i < 5; i++) {
            filteredValues.add(new FilteredValue(
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
        filteredValues.clear();
    }

    @Test
    public void crudTest() throws Exception {
        try {
            for (FilteredValue filteredValue : filteredValues) {
                assertFalse(filteredValueDao.exists(filteredValue.getKey()));
            }

            for (FilteredValue filteredValue : filteredValues) {
                if (!filteredValueDao.exists(filteredValue.getKey())) {
                    filteredValueDao.insert(filteredValue);
                }
            }
            for (FilteredValue filteredValue : filteredValues) {
                assertTrue(filteredValueDao.exists(filteredValue.getKey()));
                FilteredValue another = filteredValueDao.get(filteredValue.getKey());
                another.setHappenedDate(new Date(another.getHappenedDate().getTime()));
                assertEquals(PropertyUtils.describe(another), PropertyUtils.describe(filteredValue));
            }
        } finally {
            for (FilteredValue filteredValue : filteredValues) {
                if (filteredValueDao.exists(filteredValue.getKey())) {
                    filteredValueDao.delete(filteredValue.getKey());
                }
            }
        }
    }

    @Test
    public void batchCrudTest() throws Exception {
        try {
            for (FilteredValue filteredValue : filteredValues) {
                if (filteredValueDao.exists(filteredValue.getKey())) {
                    filteredValueDao.delete(filteredValue.getKey());
                }
            }
            assertTrue(filteredValueDao.nonExists(
                    filteredValues.stream().map(FilteredValue::getKey).collect(Collectors.toList())));
            assertFalse(filteredValueDao.allExists(
                    filteredValues.stream().map(FilteredValue::getKey).collect(Collectors.toList())));
            filteredValueDao.batchInsert(filteredValues);
            assertFalse(filteredValueDao.nonExists(
                    filteredValues.stream().map(FilteredValue::getKey).collect(Collectors.toList())));
            assertTrue(filteredValueDao.allExists(
                    filteredValues.stream().map(FilteredValue::getKey).collect(Collectors.toList())));
            List<FilteredValue> anotherList = filteredValueDao.batchGet(
                    this.filteredValues.stream().map(FilteredValue::getKey).collect(Collectors.toList()));
            assertEquals(filteredValues.size(), anotherList.size());
            for (int i = 0; i < filteredValues.size(); i++) {
                FilteredValue filteredValue = filteredValues.get(i);
                FilteredValue another = anotherList.get(i);
                another.setHappenedDate(new Date(another.getHappenedDate().getTime()));
                assertEquals(PropertyUtils.describe(another), PropertyUtils.describe(filteredValue));
                another.setHappenedDate(new Date(another.getHappenedDate().getTime()));
                assertEquals(PropertyUtils.describe(another), PropertyUtils.describe(filteredValue));
            }
        } finally {
            for (FilteredValue filteredValue : filteredValues) {
                if (filteredValueDao.exists(filteredValue.getKey())) {
                    filteredValueDao.delete(filteredValue.getKey());
                }
            }
        }
    }

    @Test
    public void presetLookupTest() throws Exception {
        try {
            for (FilteredValue filteredValue : filteredValues) {
                if (!filteredValueDao.exists(filteredValue.getKey())) {
                    filteredValueDao.insert(filteredValue);
                }
            }
            assertEquals(5, filteredValueDao.lookupCount(FilteredValueMaintainService.CHILD_FOR_POINT,
                    new Object[]{new LongIdKey(-1L)}));
            assertEquals(5, filteredValueDao.lookupCount(FilteredValueMaintainService.BETWEEN,
                    new Object[]{new Date(100L), new Date(199L)}));
            assertEquals(0, filteredValueDao.lookupCount(FilteredValueMaintainService.CHILD_FOR_POINT_BETWEEN,
                    new Object[]{new LongIdKey(-1L), new Date(100L), new Date(199L)}));
            assertEquals(5, filteredValueDao.lookupCount(FilteredValueMaintainService.CHILD_FOR_POINT_BETWEEN,
                    new Object[]{new LongIdKey(-1L), new Date(0L), new Date(999L)}));
            assertEquals(5, filteredValueDao.lookupCount(FilteredValueMaintainService.CHILD_FOR_FILTER,
                    new Object[]{new LongIdKey(-10L)}));
            assertEquals(15, filteredValueDao.lookupCount(FilteredValueMaintainService.CHILD_FOR_FILTER_SET,
                    new Object[]{Arrays.asList(new LongIdKey(-10L), new LongIdKey(-20L), new LongIdKey(-30L))}));
            assertEquals(0, filteredValueDao.lookupCount(FilteredValueMaintainService.CHILD_FOR_FILTER_BETWEEN,
                    new Object[]{new LongIdKey(-10L), new Date(100L), new Date(199L)}));
            assertEquals(5, filteredValueDao.lookupCount(FilteredValueMaintainService.CHILD_FOR_FILTER_BETWEEN,
                    new Object[]{new LongIdKey(-10L), new Date(0L), new Date(999L)}));
        } finally {
            for (FilteredValue filteredValue : filteredValues) {
                if (filteredValueDao.exists(filteredValue.getKey())) {
                    filteredValueDao.delete(filteredValue.getKey());
                }
            }
        }
    }

    @Test
    public void previousTest() throws Exception {
        try {
            for (FilteredValue filteredValue : filteredValues) {
                if (!filteredValueDao.exists(filteredValue.getKey())) {
                    filteredValueDao.insert(filteredValue);
                }
            }
            FilteredValue previous = filteredValueDao.previous(new LongIdKey(-2L), new Date(101));
            assertNotNull(previous);
            assertEquals(filteredValues.get(5).getKey(), previous.getKey());
            previous = filteredValueDao.previous(new LongIdKey(-2L), new Date(100));
            assertNull(previous);
        } finally {
            for (FilteredValue filteredValue : filteredValues) {
                if (filteredValueDao.exists(filteredValue.getKey())) {
                    filteredValueDao.delete(filteredValue.getKey());
                }
            }
        }
    }
}
