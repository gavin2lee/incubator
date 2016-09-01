package com.lachesis.core.persistence.service;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;

import com.lachesis.core.persistence.ICrudGenericDAO;
import com.lachesis.mnisqm.core.CommRuntimeException;

public abstract class DefaultCrudService<K extends Serializable, T> implements ICrudService<K, T> {

    public abstract ICrudGenericDAO<K, T> getCrudMapper();

    private Method cacheUpdateMethod;

    @Override
    public T findByPrimaryKey(K primaryKey, Integer accountId) {
        return getCrudMapper().selectByPrimaryKey(primaryKey);
    }

    @Override
    public Integer saveWithSession(T record, String username) {
        if (!StringUtils.isBlank(username)) {
            try {
                PropertyUtils.setProperty(record, "createduser", username);
            } catch (Exception e) {
            }
        }

        try {
            PropertyUtils.setProperty(record, "createdtime", new GregorianCalendar().getTime());
            PropertyUtils.setProperty(record, "lastupdatedtime", new GregorianCalendar().getTime());
        } catch (Exception e) {
        }

        getCrudMapper().insertAndReturnKey(record);
        try {
            return (Integer) PropertyUtils.getProperty(record, "id");
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public Integer updateWithSession(T record, String username) {
        try {
            PropertyUtils.setProperty(record, "lastupdatedtime", new GregorianCalendar().getTime());
        } catch (Exception e) {
        }

        if (cacheUpdateMethod == null) {
            findCacheUpdateMethod();
        }
        try {
            cacheUpdateMethod.invoke(getCrudMapper(), record);
            return 1;
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new CommRuntimeException(e);
        }
    }

    @SuppressWarnings("rawtypes")
    private void findCacheUpdateMethod() {
        ICrudGenericDAO<K, T> crudMapper = getCrudMapper();
        Class<? extends ICrudGenericDAO> crudMapperCls = crudMapper.getClass();
        for (Method method : crudMapperCls.getMethods()) {
            if ("updateByPrimaryKeyWithBLOBs".equals(method.getName())) {
                cacheUpdateMethod = method;
                return;
            } else if ("updateByPrimaryKey".equals(method.getName())) {
                cacheUpdateMethod = method;
            }
        }
    }

    public Integer updateSelectiveWithSession(T record, String username) {
        try {
            PropertyUtils.setProperty(record, "lastupdatedtime", new GregorianCalendar().getTime());
        } catch (Exception e) {
        }
        return getCrudMapper().updateByPrimaryKeySelective(record);
    }

    @Override
    public final void removeWithSession(T item, String username, Integer accountId) {
        massRemoveWithSession(Arrays.asList(item), username, accountId);
    }

    @Override
    public void massRemoveWithSession(List<T> items, String username, Integer accountId) {
        List<T> primaryKeys = new ArrayList<>(items.size());
        for (T item : items) {
            try {
                @SuppressWarnings("unchecked")
				T primaryKey = (T) PropertyUtils.getProperty(item, "id");
                primaryKeys.add(primaryKey);
            } catch (Exception e) {
                throw new CommRuntimeException(e);
            }
        }
        getCrudMapper().removeKeysWithSession(primaryKeys);
    }

    @Override
    public void massUpdateWithSession(T record, List<K> primaryKeys, Integer accountId) {
        getCrudMapper().massUpdateWithSession(record, primaryKeys);
    }
}
