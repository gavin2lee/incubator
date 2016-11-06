package com.harmazing.framework.web.converter;

import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;

import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JsonMessageConverter extends MappingJackson2HttpMessageConverter {
	public void setJsonPrefix(String jsonPrefix) {
		super.setJsonPrefix(jsonPrefix);
		this.jPrefix = jsonPrefix;
	}

	private String jPrefix;

	@Override
	protected void writeInternal(Object object, HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException {
		StringWriter string = new StringWriter();
		JsonEncoding encoding = getJsonEncoding(outputMessage.getHeaders().getContentType());
		this.getObjectMapper().setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		JsonGenerator jsonGenerator = this.getObjectMapper().getFactory()
				.createGenerator(string);

		if (this.getObjectMapper()
				.isEnabled(SerializationFeature.INDENT_OUTPUT)) {
			jsonGenerator.useDefaultPrettyPrinter();
		}

		try {
			if (this.jPrefix != null) {
				jsonGenerator.writeRaw(this.jPrefix);
			}
			this.getObjectMapper().writeValue(string, object);
			byte[] bytes = string.toString().getBytes(encoding.getJavaName());
			outputMessage.getHeaders().set("Content-Length", bytes.length + "");
			outputMessage.getBody().write(bytes);
		} catch (JsonProcessingException ex) {
			throw new HttpMessageNotWritableException("Could not write JSON: "
					+ ex.getMessage(), ex);
		}
	}
}
