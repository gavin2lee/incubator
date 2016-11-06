package com.harmazing.framework.common.mybatis;

import java.util.Collection;
import java.util.Map;

import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.wrapper.BeanWrapper;
import org.apache.ibatis.reflection.wrapper.CollectionWrapper;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.MapWrapper;
import org.apache.ibatis.reflection.wrapper.ObjectWrapper;

import com.harmazing.framework.common.model.IBaseModel;

public class MyObjectWrapperFactory extends DefaultObjectWrapperFactory {
	@Override
	public boolean hasWrapperFor(Object object) {
		if (object instanceof IBaseModel) {
			return true;
		}
		return false;
	}

	@Override
	public ObjectWrapper getWrapperFor(MetaObject metaObject, Object object) {
		if (object instanceof ObjectWrapper) {
			return (ObjectWrapper) object;
		} else if (object instanceof Map) {
			return new MapWrapper(metaObject, (Map) object);
		} else if (object instanceof Collection) {
			return new CollectionWrapper(metaObject, (Collection) object);
		} else if (object instanceof IBaseModel) {
			return new MyObjectWrapper(metaObject, object);
		}
		return new BeanWrapper(metaObject, object);
	}
}
