package com.lachesis.mnis.core.hisinterface.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.Service;

import org.apache.axis.client.Call;
import org.apache.axis.encoding.XMLType;
import org.apache.axis.utils.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.tree.BaseElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lachesis.mnis.core.bodysign.entity.BodySignItem;
import com.lachesis.mnis.core.bodysign.entity.BodySignRecord;
import com.lachesis.mnis.core.hisinterface.IHisBodySign;
import com.lachesis.mnis.core.hisinterface.entity.Item;
import com.lachesis.mnis.core.util.DateUtil;
import com.lachesis.mnis.core.util.DateUtil.DateFormat;

/**
 * 内江资中人民医院实时反写实现
 * @author qingzhi.liu
 *
 */
public class ZiZhongBodySignImpl implements IHisBodySign {
	static final Logger LOGGER = LoggerFactory
			.getLogger(ZiZhongBodySignImpl.class);

	@Override
	public boolean insertBodySign(BodySignRecord bodySignRecord) {
		boolean bool = false;
		String parXML = null;
		String reXml = null;
		List<Item> items =getItems(bodySignRecord);
		if(items==null
				|| items.size()==0){
			return false;
		}
		try {
			parXML = getXml(items); // 获取传输XML信息
			System.out.println(parXML);
			
			Service service = new org.apache.axis.client.Service();
			Call call = (Call) service.createCall();
			call.setTargetEndpointAddress("http://192.168.101.11:8084/NurseService.asmx?WSDL");
			call.setOperationName(new QName("http://tempuri.org/", "SaveSigns"));// WSDL里面描述的接口名称
			call.addParameter(new QName("http://tempuri.org/", "requestXml"),
					XMLType.XSD_STRING, ParameterMode.IN);
			call.setReturnType(XMLType.XSD_STRING);// 设置返回类型
			call.setUseSOAPAction(true);
			call.setSOAPActionURI("http://tempuri.org/SaveSigns");
			reXml = (String) call.invoke(new Object[] {parXML}); // 获取返回结果

			if (reXml != null) {
				Document document = null;
				try {
					document = DocumentHelper.parseText(reXml);
				} catch (DocumentException e) {
					LOGGER.error("体征返回，非xml格式！");
					e.printStackTrace();
				}
				if (document != null) {
					Element xh = document.getRootElement()
							.element("response").element("resultcode");
					String val = xh.getText();
					if ("0".equals(val)) {
						bool = true;
					}else{
						LOGGER.error("体征反写失败："+document.getRootElement()
								.element("response").element("errormsg").getText());
						bool = false;
					}
				}
			}
		} catch (Exception e) {
			bool = false;
			LOGGER.error("调用体征接口异常！"+e.getMessage());
			//e.printStackTrace();
		}
		return bool;
	}
	
	
	private static String getXml(List<Item> items) {
		Document doc = DocumentHelper.createDocument();
		doc.setXMLEncoding("UTF-8");
		Element el = doc.addElement("request");

		// 传入tzms的长度必须大于0
		//Item item = new Item();
		Field[] fds = items.get(0).getClass().getDeclaredFields();

		Element child = null;
		Object val = null;

		Element root = new BaseElement("items");
		for (Item item : items) {
			Element root1 = new BaseElement("item");
			root.add(root1);
			for (Field fd : fds) {
				child = new BaseElement(fd.getName().toLowerCase()); // 计生证号
				try {
					val = fd.get(item);
					child.setText(val == null ? "" : val.toString().trim());

				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
				root1.add(child);
			}
		}
		el.add(root);
		return doc.asXML();
	}

	
	private static List<Item> getItems(BodySignRecord bodySignRecord){
		
		List<Item> items = null;
		if(null!=bodySignRecord){
			items = new ArrayList<Item>();
			Item item = new Item();
			item.setPatientno(bodySignRecord.getPatientId());
			item.setInputoperid(bodySignRecord.getModifyNurseCode());
			item.setInputdeptid(bodySignRecord.getDeptCode());
			
			item.setMeasuredate(bodySignRecord.getRecordDay());
			String time = bodySignRecord.getRecordTime();
			if(!StringUtils.isEmpty(time)){
				item.setTimepoint(getTimePoint(time)+"");
			}
			item.setOperationid(getOperationid(bodySignRecord));
			item.setOperationtext(bodySignRecord.getEvent().getChineseEventDate());
			item.setOperationtime(DateUtil.format(bodySignRecord.getModifyTime(), DateFormat.FULL));
			//
			setBodyDetails(bodySignRecord.getBodySignItemList(), item);
			items.add(item);
		}
		return items;
	}
	
	private static int getTimePoint(String time){
		int retVal = 0;
		String[] strs = time.split(":");
		
		int h = Integer.parseInt(strs[0]);
		
		if(h>0 &&h<=4){
			retVal = 1;
		}else if(h>4&&h<=8){
			retVal = 2;
		}else if(h>8&&h<=12){
			retVal = 3;
		}else if(h>12&&h<=16){
			retVal = 4;
		}else if(h>16&&h<=20){
			retVal = 5;
		}else if(h>20&&h<=24){
			retVal = 6;
		}
		return retVal;
	}

	private static String getOperationid(BodySignRecord bodySignRecord){
		String eventCode = bodySignRecord.getEvent()==null?"":bodySignRecord.getEvent().getEventCode();
		if(StringUtils.isEmpty(eventCode)){
			return "";
		}
		/*'入院','ry','出院','cy','转入','zr', '手术','ss','回室','hs','外出','wc','分娩','fm','死亡','sw','召回','zh','qt'*/
		/*<!--id:1.入院 2.转入 3.手术 4.出院 5.分娩-->*/
		Map<String, String> map = new HashMap<String, String>();
		map.put("ry", "1");
		map.put("zr", "2");
		map.put("ss", "3");
		map.put("cy", "4");
		map.put("fm", "5");
		
		map.put("hs", "6");
		map.put("wc", "7");
		map.put("zh", "8");
		map.put("qt", "9");
		return map.get(eventCode);
		
	}

	private static void setBodyDetails(List<BodySignItem> bodyItems,Item item){
		if(bodyItems==null||bodyItems.size()==0){
			return;
		}
		
		String itemCode = null;
		String itemValue = null;
		for (BodySignItem bodyItem : bodyItems) {
			itemCode = bodyItem.getBodySignDict().getItemCode();
			itemValue =bodyItem.getItemValue();
			if("out".equals(bodyItem.getMeasureNoteCode())
					||"jc".equals(bodyItem.getMeasureNoteCode())
					||"qj".equals(bodyItem.getMeasureNoteCode())){
				item.setStatus("1");
				continue;
			}
			
			if("temperature".equals(itemCode)){  //体温
				item.setT(itemValue);
			}else if("pulse".equals(itemCode)){  //脉搏
				item.setP(itemValue);
			}else if("breath".equals(itemCode)){  // 呼吸
				item.setR(itemValue);
			}else if("heartRate".equals(itemCode)){  //心率
				item.setHr(itemValue);
			}else if("cooledTemperature".equals(itemCode)){  //降温后体温
				item.setIspc("是");
				item.setPt(itemValue);
			}else if("hxj".equals(bodyItem.getMeasureNoteCode())){// 呼吸机
				item.setIsbm("是");
			}else if("inTake".equals(itemCode)){  //入量
				item.setTransfuse(itemValue);
			}else if("totalInput".equals(itemCode)){  //总出量
				item.setAllin(itemValue);
			}else if("stool".equals(itemCode)){   //大便次数
				item.setStool(itemValue);
			}else if("urine".equals(itemCode)){  //尿量
				item.setPee(itemValue);
			}else if("otherOutput".equals(itemCode)){ //    其他出量
				item.setOtherout(itemValue);
			}else if("totalOutput".equals(itemCode)){  //总出量
				item.setAllout(itemValue);
			}else if("bloodPress".equals(itemCode)&&bodyItem.getIndex()==0){   //血压1
				item.setBp1(itemValue);
			}else if("bloodPress".equals(itemCode)&& bodyItem.getIndex()==1){  //血压2
				item.setBp2(itemValue);
			}else if("height".equals(itemCode)){  //身高
				item.setHeight(itemValue);
			}else if("weight".equals(itemCode)){  //体重
				item.setWeight(itemValue);  
			}
			/*
			 * Otherin:其他入量
			 * drainage：排出量
			   sputum：吸痰量
			   ispm：是否心脏起搏器 
			   drink：引入量
			*/
		}
	}
	
	/*服务地址：http://192.168.101.11:8084/NurseService.asmx
		服务描述地址：http://192.168.101.11:8084/NurseService.asmx?WSDL

		请求XML格式：
		<request>
		  <items>
		    <item>
		      <!--住院号-->
		      <patientno>1501123</patientno>
		      <!--操作员代码-->
		      <inputoperid>709</inputoperid>
		      <!--录入科室-->
		      <inputdeptid>2051</inputdeptid>
		      <!--录入时间-->
		      <measuredate>2015-12-01</measuredate>
		      <!--录入时间点1、2、3、4、5、6-->
		      <timepoint>1</timepoint>
		    <!--特殊操作标志-->
		      <!--id:1.入院 2.转入 3.手术 4.出院 5.分娩-->
		      <operationid>1</operationid>
		      <!--text-->
		      <operationtext>入院于九时十二分</operationtext>
		      <!--操作时间-->
		      <operationtime>2015-12-01 09:12:00</operationtime>      
		    <!--体征数据-->      
		      <!--体温-->
		      <t>40</t>
		      <!--脉搏-->
		      <p>78</p>
		      <!--呼吸-->
		      <r>23</r>
		      <!--心率-->
		      <hr></hr>
		      <!--是否物理降温 是、否-->
		      <ispc>是</ispc>
		      <!--物理降温后体温-->
		      <pt>37.8</pt>
		      <!--是否呼吸机 是、否-->
		      <isbm>否</isbm>
		      <!--是否心脏起搏器 是、否-->
		      <ispm>否</ispm>
		      <!--状态 1.测量 2.请假 3.拒测 4.外出-->
		      <status>1</status>
		    <!--出入量-->
		      <!--引入量-->
		      <drink>1200</drink>
		      <!--输入量-->
		      <transfuse>300</transfuse>
		      <!--其它入量-->
		      <otherin></otherin>
		      <!--总入量-->
		      <allin>1500</allin>
		      <!--大便次数-->
		      <stool>1 3/e</stool>
		      <!--小便量-->
		      <pee>1000</pee>
		      <!--排出量-->
		      <drainage></drainage>
		      <!--吸痰量-->
		      <sputum></sputum>
		      <!--其它出量-->
		      <otherout></otherout>
		      <!--总出量-->
		      <allout>1400</allout>
		      <!--血压1-->
		      <bp1>120/74</bp1>
		      <!--血压2-->
		      <bp2>121/82</bp2>
		      <!--身高-->
		      <height>173</height>
		      <!--体重-->
		      <weight>73.5</weight>
		    </item>
		    <!--多条则多个item节点-->
		  </items>
		</request>


		返回XML格式：
		<response>
		  <!--0成功，其他值失败，失败时errormsg有值-->
		  <resultcode>0</resultcode>
		  <errormsg>错误信息</errormsg>
		</response>*/
}
