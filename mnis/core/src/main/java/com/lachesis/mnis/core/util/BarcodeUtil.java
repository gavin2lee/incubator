package com.lachesis.mnis.core.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.lachesis.mnis.core.order.entity.BarCodeSet;
import com.lachesis.mnis.core.util.OrderUtil.OrderType;

public final class BarcodeUtil {
	
	//条码设置参数
	public static List<BarCodeSet> barParams; 
    private BarcodeUtil(){}

    public enum BarcodeType {
		DRUG_INFUSION,
		DRUG_ORAL,
		LAB_TEST,
		PATIENT_ID,
		PATIENT_BEDCODE,
    }
    
    static final String[] BARCODE_TYPE_PREFIXES = {
    	"I",
    	"O",
    	"L",
    	"P",
    	"B",
    	"C",
    	"N"
    };
    
    public static String[] barcodeTypes() {
    	return Arrays.copyOf( BARCODE_TYPE_PREFIXES, BARCODE_TYPE_PREFIXES.length);
    }
    
	public static String makePatientBarcodeFromPatId(String patientId) {
		if(patientId == null) {
			return null;
		} else {
			return "P"+patientId;
		}
	}
	
	public static String makeOrderBarcodeFrom(String orderExecId, String usage, String classCode) {
		if( orderExecId == null || (usage==null&&classCode==null) ) {
			return null;
		} else {
			OrderType orderType = OrderUtil.parseOrderType(usage, classCode);
			if(orderType == null) {
				return null;
			}
			switch(orderType) {
				case DRUG_INFUSION:
				case CURE_BLOOD: 
					return "I"+orderExecId;
				case DRUG_ORAL:
					return "O"+orderExecId;
				case LAB_TEST:
					return "L"+orderExecId;
				case CURE:
					return "C" + orderExecId;
				case INSPECTION:
					return "N" + orderExecId;
				default:
						return null;
			}			
		}

	}
	
	
	
	/**
	 * 获取条码配置
	 * @param barCode  数据配置
	 * @param resultType  1:返回条码截取后字段  2：返回条码类型
	 * @return
	 */
	public static BarCodeVo getBarCodeSet(String barCodeSets,String barCode){
		
		if(!StringUtils.isEmpty(barCodeSets)
				&& !StringUtils.isEmpty(barCode)){
			String[] barCodes = barCodeSets.split(";");
			
			initBarParams(barCodes);
			
			
			
			int len= barCode.length();
			String resultBarCode=null;
			String barType = null;
			BarCodeVo vo = null;
			for (BarCodeSet bar : barParams) {
				vo = new BarCodeVo();
				if(bar.getIssub()==1
						&& len>=bar.getBarlens()
						&& len<=bar.getBarlene()){   //条码需截取
					resultBarCode = barCode.substring(bar.getSubstart(), bar.getSubend());
					barType = bar.getBarType();
				}else if(bar.getIssub()==0
						&& len>=bar.getBarlens()
						&& len<=bar.getBarlene()){
					resultBarCode = barCode;
					barType = bar.getBarType();
				}
				
				vo.setBarCode(resultBarCode);
				vo.setBarType(barType);
				//先查找特殊字符
				if(StringUtil.hasValue(bar.getIndex())){
					int i = barCode.indexOf(bar.getIndex());
					if(i>0){
						barType =bar.getBarType();
						vo.setBarCode(barCode);
						vo.setBarType(barType);
						break;
					}
				}
			}
			
			return vo;
			/*if("1".equals(resultType)){
				return resultBarCode;   //返回截取后的条码
			}else if("2".equals(resultType)){
				return barType;   //返回条码类型
			}*/
		}
		return null;
	}

	private static void initBarParams(String[] barCodes) {
		String[] vals = null;
		String[] fields = null;
		BarCodeSet barCodeSet = null;
		
		if(barParams==null){   //判断静态缓存是否为空
			
			barParams = new ArrayList<BarCodeSet>();
			//根据
			for (String bars : barCodes) {
				barCodeSet = new BarCodeSet();
				vals = bars.split(",");
				for (String val : vals) {
					fields = val.split("=");
					try {
						if(fields.length==2){
							setFieldValue(fields[0],fields[1], barCodeSet);
						}
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				barParams.add(barCodeSet);
			}
		}
	}
	
	
	/**  
     * 设置bean 属性值  
     * @param map  
     * @param bean  
     * @throws Exception  
     */  
    public static void setFieldValue(String name,Object objValue,Object bean) throws Exception{ 
    	Class<?> cls = bean.getClass();
    	Field field = cls.getDeclaredField(name);
    	String fldtype = field.getType().getSimpleName();
    	String setMethod = pareSetName(name);  
        Method method = cls.getMethod(setMethod, field.getType());
        
        if(null != objValue){  
            if("String".equals(fldtype)){  
                method.invoke(bean, (String)objValue);  
            }else if("int".equals(fldtype)){  
                int val = Integer.valueOf((String)objValue);  
                method.invoke(bean, val);  
            }  
        }  
       /* Class<?> cls = bean.getClass();  
        Method methods[] = cls.getDeclaredMethods();  
        Field fields[] = cls.getDeclaredFields();  
          
        for(Field field:fields){  
            String fldtype = field.getType().getSimpleName();  
            String fldSetName = field.getName();  
            String setMethod = pareSetName(fldSetName);  
            if(!checkMethod(methods, setMethod)){  
                continue;  
            }  
            Method method = cls.getMethod(setMethod, field.getType());  
            System.out.println(method.getName());  
            if(null != objValue){  
                if("String".equals(fldtype)){  
                    method.invoke(bean, (String)objValue);  
                }else if("int".equals(fldtype)){  
                    int val = Integer.valueOf((String)objValue);  
                    method.invoke(bean, val);  
                }  
            }  
              
        }      */
    }  
    
    /**  
     * 判断该方法是否存在  
     * @param methods  
     * @param met  
     * @return  
     */  
    public static boolean checkMethod(Method methods[],String met){  
        if(null != methods ){  
            for(Method method:methods){  
                if(met.equals(method.getName())){  
                    return true;  
                }  
            }  
        }          
        return false;  
    }
    
    /**  
     * 拼接某属性set 方法  
     * @param fldname  
     * @return  
     */  
    public static String pareSetName(String fldname){  
        if(null == fldname || "".equals(fldname)){  
            return null;  
        }  
        String pro = "set"+fldname.substring(0,1).toUpperCase()+fldname.substring(1);  
        return pro;  
    }  
    
    public static void main(String[] args) {
		String barParams = "barType=INFUSION,issub=1,len=13,sublen=12,substart=0,subend=12,barlens=12,barlene=14,index=;barType=INFUSION,issub=0,len=0,sublen=0,substart=0,subend=0,barlens=8,barlene=9,index=;barType=LAB,issub=0,len=0,sublen=0,substart=0,subend=0,barlens=10,barlene=11,index=;barType=ORAL,issub=0,len=0,sublen=0,substart=0,subend=0,barlens=6,barlene=7,index=;";
		String barCode ="2000004654371";
		
		System.out.println(getBarCodeSet(barParams, barCode));
	}
    
	
}
