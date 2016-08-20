package org.mybatis.generator.ext.plugin;

import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Properties;

/**
 * mapper文件可被重写的插件，默认情况下，重新生成代码时，mapper文件不会被覆盖
 * 通过参数mapperOverwrite决定是否覆盖mapper文件，该值默认为true
 */
public class MapperFileOverwritePlugin extends PluginAdapter {
	@Override
	public boolean validate(List<String> warnings) {
		return true;
	}
	private boolean mapperOverwrite = true;

	@Override
	public void setProperties(Properties properties) {
		super.setProperties(properties);
		mapperOverwrite = Boolean.valueOf(properties.getProperty("mapperOverwrite", String.valueOf(mapperOverwrite)));
	}

	@Override
	public boolean sqlMapGenerated(GeneratedXmlFile sqlMap, IntrospectedTable introspectedTable) {
		if(mapperOverwrite){
			try {
				Field mergedField = GeneratedXmlFile.class.getDeclaredField("isMergeable");
				mergedField.setAccessible(true);
				mergedField.setBoolean(sqlMap, false);
				return true;
			} catch (Exception e){
				e.printStackTrace();
			}
		}
		return super.sqlMapGenerated(sqlMap, introspectedTable);
	}
}
