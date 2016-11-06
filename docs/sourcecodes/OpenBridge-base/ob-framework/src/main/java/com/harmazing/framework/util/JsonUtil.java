package com.harmazing.framework.util;
import java.io.StringWriter;
import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public abstract class JsonUtil {
	private static ObjectMapper objectMapper = new ObjectMapper();

	public static String toJsonString(Object object) throws Exception {
		StringWriter string = new StringWriter();
		JsonGenerator jsonGenerator = objectMapper.getFactory()
				.createGenerator(string);

		if (objectMapper.isEnabled(SerializationFeature.INDENT_OUTPUT)) {
			jsonGenerator.useDefaultPrettyPrinter();
		}

		objectMapper.writeValue(jsonGenerator, object);
		return string.toString();
	}
	
	public static <T> T toObject(String jsonStr, Class<?> objType) throws Exception {
		return (T) objectMapper.readValue(jsonStr, objType);
	}
	
	public static <T> T toObjectList(String jsonStr, Class<?> objType) throws Exception {
		JavaType type =  objectMapper.getTypeFactory().constructParametricType(ArrayList.class, objType);
		return objectMapper.readValue(jsonStr, type);  
	}
}
