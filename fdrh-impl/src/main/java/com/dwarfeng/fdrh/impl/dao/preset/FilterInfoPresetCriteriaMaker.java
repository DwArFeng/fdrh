package com.dwarfeng.fdrh.impl.dao.preset;

import com.dwarfeng.fdr.stack.service.FilterInfoMaintainService;
import com.dwarfeng.subgrade.sdk.hibernate.criteria.PresetCriteriaMaker;
import com.dwarfeng.subgrade.stack.bean.key.LongIdKey;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class FilterInfoPresetCriteriaMaker implements PresetCriteriaMaker {

    @Override
    public void makeCriteria(DetachedCriteria detachedCriteria, String s, Object[] objects) {
        switch (s) {
            case FilterInfoMaintainService.CHILD_FOR_POINT:
                childForPoint(detachedCriteria, objects);
                break;
            case FilterInfoMaintainService.CHILD_FOR_POINT_SET:
                childForPointSet(detachedCriteria, objects);
                break;
            case FilterInfoMaintainService.ENABLED_CHILD_FOR_POINT:
                enabledChildForPointSet(detachedCriteria, objects);
                break;
            default:
                throw new IllegalArgumentException("无法识别的预设: " + s);
        }
    }

    private void childForPoint(DetachedCriteria detachedCriteria, Object[] objects) {
        try {
            if (Objects.isNull(objects[0])) {
                detachedCriteria.add(Restrictions.isNull("pointLongId"));
            } else {
                LongIdKey longIdKey = (LongIdKey) objects[0];
                detachedCriteria.add(Restrictions.eqOrIsNull("pointLongId", longIdKey.getLongId()));
            }
            detachedCriteria.addOrder(Order.asc("longId"));
        } catch (Exception e) {
            throw new IllegalArgumentException("非法的参数:" + Arrays.toString(objects));
        }
    }

    private void childForPointSet(DetachedCriteria detachedCriteria, Object[] objects) {
        try {
            if (Objects.isNull(objects[0])) {
                detachedCriteria.add(Restrictions.isNull("pointLongId"));
            } else {
                @SuppressWarnings("unchecked")
                List<LongIdKey> longIdKeys = (List<LongIdKey>) objects[0];
                if (longIdKeys.isEmpty()) {
                    detachedCriteria.add(Restrictions.isNull("pointLongId"));
                } else {
                    detachedCriteria.add(Restrictions.in("pointLongId", longList(longIdKeys)));
                }
            }
            detachedCriteria.addOrder(Order.asc("longId"));
        } catch (Exception e) {
            throw new IllegalArgumentException("非法的参数:" + Arrays.toString(objects));
        }
    }

    private void enabledChildForPointSet(DetachedCriteria detachedCriteria, Object[] objects) {
        try {
            if (Objects.isNull(objects[0])) {
                detachedCriteria.add(Restrictions.eqOrIsNull("enabled", true));
                detachedCriteria.add(Restrictions.isNull("pointLongId"));
            } else {
                LongIdKey longIdKey = (LongIdKey) objects[0];
                detachedCriteria.add(Restrictions.eqOrIsNull("enabled", true));
                detachedCriteria.add(Restrictions.eqOrIsNull("pointLongId", longIdKey.getLongId()));
            }
            detachedCriteria.addOrder(Order.asc("longId"));
        } catch (Exception e) {
            throw new IllegalArgumentException("非法的参数:" + Arrays.toString(objects));
        }
    }

    private List<Long> longList(List<LongIdKey> list) {
        return list.stream().map(LongIdKey::getLongId).collect(Collectors.toList());
    }
}
