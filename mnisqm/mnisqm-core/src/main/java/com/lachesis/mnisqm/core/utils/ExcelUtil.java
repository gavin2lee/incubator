package com.lachesis.mnisqm.core.utils;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;

public class ExcelUtil<T> {
	
	/**
	 * 通过前端传回的数据，导出excel表格
	 * @param headers 表格标题
	 * @param lists 表格体
	 * @return
	 */
	public HSSFWorkbook exportExcelByArray(List<String> headers,List<List<String>> lists) {
		// 声明一个工作薄  
        HSSFWorkbook workbook = new HSSFWorkbook();  
        // 生成一个表格  
        HSSFSheet sheet = workbook.createSheet();  
        // 设置表格默认列宽度为15个字节  
        sheet.setDefaultColumnWidth(15);  
        // 生成标题样式  
        HSSFCellStyle styleHead = workbook.createCellStyle();  
        // 设置样式  
        styleHead.setFillForegroundColor(HSSFColor.SKY_BLUE.index);  
        styleHead.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);  
        styleHead.setBorderBottom(HSSFCellStyle.BORDER_THIN);  
        styleHead.setBorderLeft(HSSFCellStyle.BORDER_THIN);  
        styleHead.setBorderRight(HSSFCellStyle.BORDER_THIN);  
        styleHead.setBorderTop(HSSFCellStyle.BORDER_THIN);  
        styleHead.setAlignment(HSSFCellStyle.ALIGN_CENTER);  
        // 生成标题字体  
        HSSFFont fontHead = workbook.createFont();  
        fontHead.setColor(HSSFColor.VIOLET.index);  
        fontHead.setFontHeightInPoints((short) 12);  
        fontHead.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);  
        // 把字体应用到当前的样式  
        styleHead.setFont(fontHead);  
        // 生成并设置表体样式  
        HSSFCellStyle styleBody = workbook.createCellStyle();  
        styleBody.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);  
        styleBody.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);  
        styleBody.setBorderBottom(HSSFCellStyle.BORDER_THIN);  
        styleBody.setBorderLeft(HSSFCellStyle.BORDER_THIN);  
        styleBody.setBorderRight(HSSFCellStyle.BORDER_THIN);  
        styleBody.setBorderTop(HSSFCellStyle.BORDER_THIN);  
        styleBody.setAlignment(HSSFCellStyle.ALIGN_CENTER);  
        styleBody.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);  
        // 生成表体字体  
        HSSFFont fontBody = workbook.createFont();  
        fontBody.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);  
        // 把字体应用到当前的样式  
        styleBody.setFont(fontBody);  

        //产生表格标题行  
        HSSFRow row = sheet.createRow(0);  
        for (int i = 0; i < headers.size(); i++) {  
           HSSFCell cell = row.createCell(i);  
           cell.setCellStyle(styleHead);  
           HSSFRichTextString text = new HSSFRichTextString(headers.get(i));  
           cell.setCellValue(text);  
        }  
        //产生数据行
        int index = 0; 
        for (List<String> list : lists) {
        	index++;  
            row = sheet.createRow(index);
            for (int i = 0; i < list.size(); i++){
            	HSSFCell cell = row.createCell(i);  
                cell.setCellStyle(styleBody);
            	cell.setCellValue(Double.parseDouble(list.get(i)));
            }
		}
        return workbook;
	}

	/**
	 * 导出excel表格
	 * @param title sheet名称
	 * @param headers 表格标题栏
	 * @param dataset 表格体
	 * @param pattern 时间格式，如：yyyy-MM-dd
	 * @return
	 */
	public HSSFWorkbook exportExcel(String title, String[] headers,Collection<T> dataset, String pattern) {
		// 声明一个工作薄  
        HSSFWorkbook workbook = new HSSFWorkbook();  
        // 生成一个表格  
        HSSFSheet sheet = workbook.createSheet(title);  
        // 设置表格默认列宽度为15个字节  
        sheet.setDefaultColumnWidth(15);  
        // 生成标题样式  
        HSSFCellStyle styleHead = workbook.createCellStyle();  
        // 设置样式  
        styleHead.setFillForegroundColor(HSSFColor.SKY_BLUE.index);  
        styleHead.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);  
        styleHead.setBorderBottom(HSSFCellStyle.BORDER_THIN);  
        styleHead.setBorderLeft(HSSFCellStyle.BORDER_THIN);  
        styleHead.setBorderRight(HSSFCellStyle.BORDER_THIN);  
        styleHead.setBorderTop(HSSFCellStyle.BORDER_THIN);  
        styleHead.setAlignment(HSSFCellStyle.ALIGN_CENTER);  
        // 生成标题字体  
        HSSFFont fontHead = workbook.createFont();  
        fontHead.setColor(HSSFColor.VIOLET.index);  
        fontHead.setFontHeightInPoints((short) 12);  
        fontHead.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);  
        // 把字体应用到当前的样式  
        styleHead.setFont(fontHead);  
        // 生成并设置表体样式  
        HSSFCellStyle styleBody = workbook.createCellStyle();  
        styleBody.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);  
        styleBody.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);  
        styleBody.setBorderBottom(HSSFCellStyle.BORDER_THIN);  
        styleBody.setBorderLeft(HSSFCellStyle.BORDER_THIN);  
        styleBody.setBorderRight(HSSFCellStyle.BORDER_THIN);  
        styleBody.setBorderTop(HSSFCellStyle.BORDER_THIN);  
        styleBody.setAlignment(HSSFCellStyle.ALIGN_CENTER);  
        styleBody.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);  
        // 生成表体字体  
        HSSFFont fontBody = workbook.createFont();  
        fontBody.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);  
        // 把字体应用到当前的样式  
        styleBody.setFont(fontBody);  
          

        //产生表格标题行  
        HSSFRow row = sheet.createRow(0);  
        for (int i = 0; i < headers.length; i++) {  
           HSSFCell cell = row.createCell(i);  
           cell.setCellStyle(styleHead);  
           HSSFRichTextString text = new HSSFRichTextString(headers[i]);  
           cell.setCellValue(text);  
        }  
     

        //遍历集合数据，产生数据行  
        Iterator<T> it = dataset.iterator();  
        int index = 0;  
        while (it.hasNext()) {  
           index++;  
           row = sheet.createRow(index);  
           T t = (T) it.next();  
           //利用反射，根据javabean属性的先后顺序，动态调用getXxx()方法得到属性值  
           Field[] fields = t.getClass().getDeclaredFields();  
           for (int i = 0; i < fields.length; i++) {  
              HSSFCell cell = row.createCell(i);  
              cell.setCellStyle(styleBody);  
              Field field = fields[i];  
              String fieldName = field.getName();  
              String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);  
              try {  
                  Class<? extends Object> tCls = t.getClass();  
                  Method getMethod = tCls.getMethod(getMethodName,  
                        new Class[] {});  
                  Object value = getMethod.invoke(t, new Object[] {});  
                  //判断值的类型后进行强制类型转换  
                  String textValue = null;  
                  if(value == null){
                	  textValue = "";
                  }else if (value instanceof Date) {  
                     Date date = (Date) value;  
                     SimpleDateFormat sdf = new SimpleDateFormat(pattern);  
                      textValue = sdf.format(date);  
                  }  else if (value instanceof byte[]) {  
                     // TODO
                  } else{  
                     //其它数据类型都当作字符串简单处理  
                     textValue = value.toString();  
                  }  
                  //如果不是图片数据，就利用正则表达式判断textValue是否全部由数字组成  
                  if(textValue!=null){  
                     Pattern p = Pattern.compile("^//d+(//.//d+)?$");     
                     Matcher matcher = p.matcher(textValue);  
                     if(matcher.matches()){  
                        //是数字当作double处理  
                        cell.setCellValue(Double.parseDouble(textValue));  
                     }else{  
                        HSSFRichTextString richString = new HSSFRichTextString(textValue);  
                        HSSFFont font3 = workbook.createFont();  
                        font3.setColor(HSSFColor.BLUE.index);  
                        richString.applyFont(font3);  
                        cell.setCellValue(richString);  
                     }  
                  }  
              } catch (Exception e) {  
                  e.printStackTrace();  
              }finally {  
                  //清理资源  
              }  
           }  
        }
        return workbook;
	}
	
	/**
	 * 从excel表格中获取数据
	 * @param is
	 * @param model excel中的数据对应的对象
	 * @param className 对象的完整名称，如：com.jxufe.bean.Student
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> readExcelContent(InputStream is, Object model, String className) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			POIFSFileSystem fs = new POIFSFileSystem(is);
			HSSFWorkbook wb = new HSSFWorkbook(fs);

			HSSFSheet sheet = wb.getSheetAt(0);
			// 得到总行数
			int rowNum = sheet.getLastRowNum();
			HSSFRow row = sheet.getRow(0);
			Field[] field = model.getClass().getDeclaredFields(); // 获取实体类的所有属性，返回Field数组

			// 正文内容从第二行开始,第一行为表头的标题
			int j = 0;
			for (int i = 1; i <= rowNum; i++) {
				Object object = null;
				object = Class.forName(className);
				object = (Object) ((Class<? extends Object>) object).newInstance();
				row = sheet.getRow(i);
				for (j = 0; j < field.length; j++) { // 遍历所有属性
					String name = field[j].getName(); // 获取属性的名字
					name = name.substring(0, 1).toUpperCase() + name.substring(1); // 将属性的首字符大写，构造get，set方法
					String type = field[j].getGenericType().toString(); // 获取属性的类型
					row.getCell(j).setCellType(Cell.CELL_TYPE_STRING);
					if (type.contains("String")) {
						Method m = model.getClass().getMethod("set" + name, String.class);
						m.invoke(object, row.getCell(j).getStringCellValue());
					} else if (type.contains("int")) {
						Method m = model.getClass().getMethod("set" + name, int.class);
						m.invoke(object, Integer.parseInt(row.getCell(j).getStringCellValue()));
					} else if (type.contains("long")) {
						Method m = model.getClass().getMethod("set" + name, long.class);
						m.invoke(object, Long.parseLong(row.getCell(j).getStringCellValue()));
					} else if (type.contains("boolean")) {
						Method m = model.getClass().getMethod("set" + name, boolean.class);
						if ("男".equals(row.getCell(j).getStringCellValue().trim())) {
							m.invoke(object, false);
						} else {
							m.invoke(object, true);
						}
					}
					if (type.contains("Date")) {
						if(!StringUtils.isEmpty(row.getCell(j).getStringCellValue())){
							Method m = model.getClass().getMethod("set" + name, Date.class);
							m.invoke(object, DateTimeUtils.toDate(row.getCell(j).getStringCellValue(), "yyyy-MM-dd"));
						}
					}
				}
				map.put(i + "", object);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

}
