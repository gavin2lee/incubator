package com.harmazing.framework.common.mybatis;

import java.util.Map;

import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.property.PropertyTokenizer;
import org.apache.ibatis.reflection.wrapper.BeanWrapper;

import com.harmazing.framework.common.model.Attach;
import com.harmazing.framework.common.model.BaseModel;

public class MyObjectWrapper extends BeanWrapper {
	private Object obj;

	public MyObjectWrapper(MetaObject metaObject, Object object) {
		super(metaObject, object);
		this.obj = object;
	}

	public String findProperty(String name, boolean useCamelCaseMapping) {
		String property = super.findProperty(name, useCamelCaseMapping);
		if (property == null && name.indexOf("[") > 0) {
			property = name;
		}
		return property;
	}

	public boolean hasSetter(String name) {
		boolean has = super.hasGetter(name);
		if (has == false && name.indexOf("[") > 0) {
			return true;
		}
		return has;
	}

	public Class<?> getSetterType(String name) {
		Class<?> c = super.getSetterType(name);
		if (c == Map.class) {
			return Attach.class;
		} else {
			return c;
		}
	}

	@Override
	public void set(PropertyTokenizer prop, Object value) {
		if (obj instanceof BaseModel) {
			if (value instanceof Attach
					|| (value == null && prop.getIndexedName().contains("["))) {
				BaseModel m = (BaseModel) obj;
				Attach attach = (Attach) value;
				m.attObject(prop.getIndex(),
						value == null ? null : attach.getRoot());
			} else {
				super.set(prop, value);
			}
		} else {
			super.set(prop, value);
		}
	}
}
