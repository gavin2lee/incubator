package com.harmazing.framework.common;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonResponse implements Serializable {
	public static JsonResponse success(Object data) {
		return new JsonResponse(data);
	}

	public static JsonResponse success(Object data, String msg) {
		return new JsonResponse(0, data, msg);
	}

	public static JsonResponse success() {
		return success(null);
	}

	public static JsonResponse failure(int code, String msg) {
		return new JsonResponse(code, null, msg);
	}

	public static JsonResponse failure(int code, Object data, String msg) {
		return new JsonResponse(code, data, msg);
	}

	private Map<String, Object> data = new HashMap<String, Object>();
	private static final long serialVersionUID = 1L;
	private Integer code = 0;
	private String msg = "";

	public JsonResponse() {
		this(0);
	}

	public JsonResponse(int code) {
		this(code, null);
	}

	public JsonResponse(int code, Object obj) {
		this(code, obj, null);
	}

	public JsonResponse(Object obj) {
		this(0, obj, null);
	}

	public JsonResponse(int code, Object obj, String msg) {
		this.code = code;
		if (msg != null) {
			this.msg = msg;
		}
		if (obj != null) {
			if (obj instanceof Page) {
				this.setRecordCount(((Page<?>) obj).getRecordCount());
				this.data.put("list", obj);
			} else if (obj instanceof Collection) {
				this.data.put("list", obj);
			} else {
				this.data.put("root", obj);
			}
		}
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public JsonResponse addData(String key, Object obj) {
		data.put(key, obj);
		return this;
	}

	public JsonResponse setRecordCount(int count) {
		data.put("recordCount", Integer.valueOf(count));
		return this;
	}

	public JsonResponse delData(String key) {
		data.remove(key);
		return this;
	}

	@SuppressWarnings("rawtypes")
	public Object getData() {
		if (data == null || data.isEmpty()) {
			return new HashMap();
		} else if (data.size() == 1) {
			Object obj = data.entrySet().iterator().next().getValue();
			if (obj instanceof List) {
				return data;
			} else {
				return obj;
			}
		} else {
			return data;
		}
	}

	public JsonResponse extractInclude(Object object, String fieldNames) {
		if (fieldNames == null) {
			return this;
		}
		String[] fieldNamesAry = fieldNames.split(",");
		return extractInclude(object, fieldNamesAry);
	}

	public JsonResponse extractExclude(Object object, String fieldNames) {
		if (fieldNames == null) {
			String[] emptyAry = {};
			extractExclude(object, emptyAry);
		}
		String[] fieldNamesAry = fieldNames.split(",");
		return extractExclude(object, fieldNamesAry);
	}

	public JsonResponse extractInclude(Object object, String[] fieldNames) {
		for (String fname : fieldNames) {
			try {
				Field f = object.getClass().getDeclaredField(fname);
				f.setAccessible(true);
				Object fieldValue = f.get(object);
				this.addData(fname, fieldValue);
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}
		return this;
	}

	public JsonResponse extractExclude(Object object, String[] fieldNames) {
		Field[] fields = object.getClass().getDeclaredFields();
		List<String> fieldsList = Arrays.asList(fieldNames);
		for (Field field : fields) {
			try {
				if (!fieldsList.contains(field.getName())) {
					field.setAccessible(true);
					Object fieldValue = field.get(object);
					this.addData(field.getName(), fieldValue);
				}
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}
		return this;
	}
}
