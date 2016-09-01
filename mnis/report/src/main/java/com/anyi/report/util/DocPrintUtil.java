package com.anyi.report.util;

import com.anyi.report.DocReportConstants;
import com.anyi.report.constants.DocPrintConstants;
import com.anyi.report.entity.DocReportPrintData;
import com.anyi.report.entity.DocReportPrintDataDetail;
import com.anyi.report.entity.DocTemplateItem;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DocPrintUtil {
	//内部类，用于存储字符串行数计算中每个字符的相关信息
	private static class CharInfo{
		private final int charIndex; //字符所在字符串中的索引
		private final String charStr;//字符内容
		private final int width;//字符所占宽度
		//构造函数
		public CharInfo(int index, String str, int width){
			this.charIndex = index;
			this.charStr = str;
			this.width = width;
		}

		public int getCharIndex() {
			return charIndex;
		}

		public String getCharStr() {
			return charStr;
		}

		public int getWidth() {
			return width;
		}
	}
	/**
	 * 获取一行的高度
	 * @return
	 */
	public static int getOneRowHightForDocPrint(){
		return (DocPrintConstants.oneRowHight + DocPrintConstants.lineHight);
	}
	
	/**
	 * 更加行数，获取高度
	 * @param rowCount
	 * @return
	 */
	public static int countItemHightByRowCount(int rowCount){
		int itemHight = 0;
		if(1<rowCount){
			//多行
			itemHight = DocPrintConstants.charHight * rowCount + DocPrintConstants.lineHight;
		}else {
			//默认是1行
			itemHight = getOneRowHightForDocPrint();
		}

		return itemHight;
	}
	
	/**
	 *计算记录中某个组件数据所需要的行高
	 * @param itemContent 组件数据内容
	 * @param with        组件模板的宽度
	 * @return 指定组件的打印行高
	 */
	public static int countItemHight(String itemContent,int width){
		if(StringUtils.isEmpty(itemContent) || 0>=width){
			return 0;
		}
		//适当缩紧行宽的判定范围
		width = width - 1;

		int widthCount = 0;//单行字符宽度的累计值
		int rowCount = 1;
		int lastWidth = 0;
		for(int i=0;i<itemContent.length();i++) {
			String strChar = itemContent.substring(i, i + 1);
			int charWidth = 0;
			/*计算单个字符的像素宽度
				1、使用FontMetrics获取字符像素宽度
				2、支持中文英文采用不同的字体类型
				3、根据字节数区分字符，占用1个字符使用英文字体宽度，超过1个字符使用中文字体宽度
			*/
			final Font enFont = new Font(DocPrintConstants.enCharType, Font.PLAIN, DocPrintConstants.charSize);
			final FontMetrics enMetrics = (new JLabel()).getFontMetrics(enFont);
			final Font chFont = new Font(DocPrintConstants.chCharType, Font.PLAIN, DocPrintConstants.charSize);
			final FontMetrics chMetrics = (new JLabel()).getFontMetrics(chFont);
			if (strChar.getBytes().length == 1) {
				charWidth = enMetrics.stringWidth(strChar);
			} else {
				charWidth = chMetrics.stringWidth(strChar);
			}
			if (0 >= charWidth || charWidth > width) {
				//单个字符的宽度超标
				return 0;
			}
			widthCount += charWidth;
			if(widthCount >= width){
				//超过控件宽度，增加一行
				rowCount++;
				widthCount = charWidth;
				if(isNonStartChar(strChar)){
					//如果该子串不能作为分行起始字符，则从上一字符开始分行
					//NOTE:目前这种处理只是一种暂时的解决方案，如果想要前后端行高保持精准的一致性，
					// 需要使用相同的计算规则约定
					widthCount += lastWidth;
				}
			}
			//保存为上一子串宽度
			lastWidth = charWidth;
		}
		return DocPrintUtil.countItemHightByRowCount(rowCount);
	}

	//尝试使用另外一种算法计算分行问题，以解决特殊的规则（例如某些字符不能作为开头，结尾）
	//由于结尾的判断尚不准确，作废。
//	public static int countItemHight(String itemContent,int width) {
//		if (StringUtils.isEmpty(itemContent) || 0 >= width) {
//			return 0;
//		}
//		//适当缩紧行宽的判定范围
//		width = width - 1;
//
//		List<List<CharInfo>> rowListAll = new ArrayList<List<CharInfo>>();//分行后的所有字符信息
//		List<CharInfo> rowList = null;//单行字符集合
//		int widthCount = 0;//单行字符宽度的累计值
////			int rowCount = 1;
////			int lastWidth = 0;
//		for (int i = 0; i < itemContent.length(); i++) {
//			String strChar = itemContent.substring(i, i + 1);
//			int charWidth = 0;
//			/*计算单个字符的像素宽度
//				1、使用FontMetrics获取字符像素宽度
//				2、支持中文英文采用不同的字体类型
//				3、根据字节数区分字符，占用1个字符使用英文字体宽度，超过1个字符使用中文字体宽度
//			*/
//			final Font enFont = new Font(DocPrintConstants.enCharType, Font.PLAIN, DocPrintConstants.charSize);
//			final FontMetrics enMetrics = (new JLabel()).getFontMetrics(enFont);
//			final Font chFont = new Font(DocPrintConstants.chCharType, Font.PLAIN, DocPrintConstants.charSize);
//			final FontMetrics chMetrics = (new JLabel()).getFontMetrics(chFont);
//			if (strChar.getBytes().length == 1) {
//				charWidth = enMetrics.stringWidth(strChar);
//			} else {
//				charWidth = chMetrics.stringWidth(strChar);
//			}
//			if (0 >= charWidth || charWidth > width) {
//				//单个字符的宽度超标
//				return 0;
//			}
//			if (rowList == null) {
//				rowList = new ArrayList<CharInfo>();
//			}
//			CharInfo charInfo = new CharInfo(i, strChar, charWidth);
//			widthCount += charWidth;
//			if (widthCount >= width) {
//				rowListAll.add(rowList);
//				rowList = new ArrayList<>();
//				widthCount = charWidth;
//			}
//			rowList.add(charInfo);
//		}
//		//对分行后的字符串进行后处理，以应对需要特殊处理的字符或规则
//		PostProcessWithRowedCharInfo(rowListAll, width);
//
//		//rowListAll的个数即为分行的行数
//		return countItemHightByRowCount(rowListAll.size());
//	}

//	/**
//	 * 对分行后的字符信息进行处理，以应对某些特殊的规则（例如某些字符不能作为开头，结尾）
//	 * @param rowListAll 分行后的字符信息
//	 * @param width 单行最大宽度
//     */
//    private static void PostProcessWithRowedCharInfo(List<List<CharInfo>> rowListAll, int width){
//		if (rowListAll == null) {
//			return;
//		}
//		for (int i = 0; i < rowListAll.size(); i++) {
//			List<CharInfo> rowList = rowListAll.get(i);
////			List<CharInfo> tempList = new ArrayList<CharInfo>();
//			//1、检查起始字符是否合法
//			CharInfo charInfo = rowList.get(0);
//			int index = 0;
//			if(isNonStartChar(charInfo)){
//				if(0<i){
//					//如果还有上一行字串
//					List<CharInfo> preRowList = rowListAll.get(i-1);
//					index = getStartCharIndexFromEnd(preRowList);
//					if(0<index){
//						//从上一行截取末尾字符串添加到本行开头
//						rowList.addAll(0, preRowList.subList(index, preRowList.size()));
//					}
//				}
//			}
//			//2、检查行串的长度是否超标
//			List<CharInfo> tempList = getExceedStringFromRow(rowList, width);
//			if (tempList == null) {
//				tempList = new ArrayList<CharInfo>();
//			}
//			//3、检查结尾字符是否合法
//			charInfo = rowList.get(rowList.size() - 1);
//			if(isNonEndChar(charInfo)){
//				//末尾字符不能作为结尾，需要继续往前截取
//				int sIndex = getStartCharIndexFromEnd(rowList);
//				if(0<sIndex){
//					//从索引开始截取字符
//					tempList.addAll(0, rowList.subList(sIndex, rowList.size()));
//				}
//			}
//			//4、截取出的元素放到下一行
//			if(0<tempList.size()){
//				List<CharInfo> nextRowList = rowListAll.get(i+1);
//				if (nextRowList == null) {
//					//最后一行没有了，将截取出的字符串作为最后一行
//					rowListAll.add(tempList);
//				}else {
//					nextRowList.addAll(0, tempList);
//				}
//			}
//		}
//	}

	/**
	 * 分行的字符串信息中，将超过规定宽度的字符串截取出来，保留剩余的部分
	 * @param rowList
	 * @param width
     * @return
     */
    private static List<CharInfo> getExceedStringFromRow(List<CharInfo> rowList, int width){
		ArrayList<CharInfo> list = new ArrayList<>();
		if(null==rowList || 0>=rowList.size() || 0>= width){
			return list;
		}
		int widthCount = 0;
		for (int i = 0; i < rowList.size(); i++) {
			widthCount += rowList.get(i).getWidth();
			if(widthCount>width){
				list.addAll(rowList.subList(i, rowList.size()));
				//rowList只保留前半段
				//变量后续没再使用，这句代码会被优化掉吗？好可怕……
				rowList = rowList.subList(0, i);
				break;
			}
		}

		return list;
	}

	private static boolean isNonStartChar(String strChar) {
		return DocPrintConstants.NON_START_CHAR.contains(strChar);
	}

	private static boolean isNonStartChar(CharInfo strChar) {
		return DocPrintConstants.NON_START_CHAR.contains(strChar.getCharStr());
	}

	//结尾字符的判断不能根据单字符简单判断，需要根据当时的上下文环境（例如小数点的位置）
	//以下方法作废。
	private static boolean isNonEndChar(CharInfo strChar) {
		return DocPrintConstants.NON_END_CHAR.contains(strChar.getCharStr());
	}
	
	/**
	 * 获取文书显示的数据
	 * @param itemInfo
	 * @return
	 */
	public static String getItemContentForPrint(DocReportPrintDataDetail itemInfo){
		String itemContent = itemInfo.getRecord_value();
		if(StringUtils.isEmpty(itemContent)){
			return null;
		}
		if(DocReportConstants.DATA_TYPE_OPT.equals(itemInfo.getData_type())
				&& 0<itemContent.indexOf("、")){
			//此处有一个历史问题，对于包含中文顿号的OPT选项，只显示顿号之前的内容
			itemContent = itemContent.substring(0, itemContent.indexOf("、"));
		}else if("Y".equals(itemInfo.getRecord_value())
				|| "N".equals(itemInfo.getRecord_value())) {
			//DocReportConstants.DATA_TYPE_SWT.equals(itemInfo.getData_type())
			//由于元数据中没有关联SWT，因此无法获取到此数据类型，无法使用以上语句判定
			//SWT选项，勾选显示对号，否则显示为空
			itemContent = "Y".equals(itemInfo.getRecord_value())? "√":" ";
		}else if(DocReportConstants.CREATE_PERSON.equals(itemInfo.getTemplate_item_id())
				||DocReportConstants.APPROVE_PERSON.equals(itemInfo.getTemplate_item_id())) {
			//创建人和审核者信息，只显示姓名
			String[] str = itemInfo.getRecord_value().split("-");
			if(1<str.length){
				itemContent = str[1];
			}else {
				itemContent = str[0];
			}
		}
		return itemContent;
	}
	
	/**
	 * 计算文书记录打印内容所需的行高
	 * @param printData        文书记录的打印数据
	 * @param templateItemInfo 所在模板的所有组件信息
	 * @return 打印内容所需要的行高
	 */
	public static int countRowHightForRecord(DocReportPrintData printData, Map<String, DocTemplateItem> templateItemInfo){
		if(null==printData || null==templateItemInfo || 0>=templateItemInfo.size()){
			return 0;
		}
		int maxHight = 0;
		for(DocReportPrintDataDetail detail : printData.getData_list()) {
			//1、获取组件打印内容
			String itemContent = getItemContentForPrint(detail);
			//2、获取组件信息
			DocTemplateItem itemInfo = templateItemInfo.get(detail.getTemplate_item_id());
			if (null == itemInfo) {
				continue;
			}
			//3、计算组件行高
			int itemHight = countItemHight(itemContent, itemInfo.getWidth());
			if (maxHight < itemHight) {
				maxHight = itemHight;
			}
		}

		return maxHight;
	}
}