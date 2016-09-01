package com.lachesis.mnis.web.intercept;


//import com.uih.anyi.base.util.DateUtil;
//import com.uih.anyi.mnis.intf.bodysign.BodySignRecord;
//import com.uih.anyi.mnis.intf.common.BusException;
//import com.uih.anyi.mnis.intf.common.MidwareException;
//import com.uih.anyi.mnis.intf.common.SysException;

public class CallingInterceptor {
//	private static final Log LOGGER = LogFactory.getLog(CallingInterceptor.class);
//	@Autowired
//	private IdentityService identityService;
//	@Autowired
//	private RbacService rbacService;
//	private static final long ONE_DAY_MILLIS = 1000*60*60*24;
//	private static final long ONE_HOUR_MILLIS = 1000*60*60;
//	private static final String NO_PERMISSION_STR="对不起，您无权修改此记录！";
//
//	public Object validateToken(final ProceedingJoinPoint joinPoint) throws Throwable {
//		Object result = null;
//		String token = (String) joinPoint.getArgs()[0];
//		int i = identityService.validateSessionToken(token);
//		boolean ackResult = false;
//		boolean midException = false;
//		
//		if (i < 0) {
//			// Validation failed
//			result = ((MethodSignature) joinPoint.getSignature()).getReturnType().newInstance();
//			for(Field field : result.getClass().getDeclaredFields()){
//				if(field.getName().equals("ackResult")){
//					ackResult = true;
//				}else if(field.getName().equals("midException")){
//					midException = true;
//				}
//			}
//			if(ackResult){
//				Method setAckResult = result.getClass().getMethod("setAckResult", int.class);
//				setAckResult.invoke(result, i);
//			}
//			if(midException){
//				Method setMidException = result.getClass().getMethod("setMidException",
//						MidwareException.class);
//				setMidException.invoke(result, new MidwareException(null, new BusException(
//						com.uih.anyi.base.user.service.impl.SessionManager.validateError.get(i))));
//			}
//		} else {
//			try {
//				//权限控制
//				Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
//				if(method.getName().equals("updateBodySignRecord")){
//					boolean havePermission = false;
//					BodySignRecord bodySignRecord = (BodySignRecord) joinPoint.getArgs()[1];
//					Date recordDate = DateUtil.formatStringToDate(bodySignRecord.getRecordDay()+" "
//							+bodySignRecord.getRecordTime(), DateUtil.DATE_FORMAT_YMDHM);
//					UserInformation userInformation = identityService.getUserByToken(token);
//					// token验证通过，userInformation 不可能为null
//					assert userInformation != null;
//					
//					if(userInformation.getSysUser().getEmplCode().equals(bodySignRecord.getRecordNurseCode())){
//						//当前日期距生命体征保存日期超过24小时，不允许修改
//						if((System.currentTimeMillis() - recordDate.getTime()) > ONE_DAY_MILLIS){
//							throw new RuntimeException("当前日期距生命体征首次录入日期超过24小时，不允许修改！");
//						}else{
//							havePermission = true;
//						}
//					}else{
//						//录入用户信息
//						UserInformation recordUser = identityService.selectUserByEmplCode(bodySignRecord.getRecordNurseCode());
//						//当前操作用户与生命体征记录用户是否上下级关系
//						boolean imBoss = false;
//						for(SysRole recordUserRole : recordUser.getRoleList()){
//							for(SysRole role : userInformation.getRoleList()){
//								if(recordUserRole.getRoleId()>role.getRoleId()){
//									imBoss = true;
//									break;
//								}
//							}
//						}
//						//我是老大我怕谁
//						if(imBoss){
//							for(SysRole role : userInformation.getRoleList()){
//								RolePermission rolePermissions =  rbacService.getRolePermissions(role.getRoleId());
//								for(Permission permission : rolePermissions.getListPermission()){
//									if(permission.getModule().getModuleCode()!=null&&permission.getModule().getModuleCode().equals("bodySign")&&
//											permission.getOperate().getOperateCode()!=null&&permission.getOperate().getOperateCode().equals("update")){
//										havePermission = true;
//										//允许多少小时之内修改
//										int validTime = permission.getValidTime();
//										if(validTime <= 0){
//											break;
//										}
//										if((System.currentTimeMillis() - recordDate.getTime()) > (validTime*ONE_HOUR_MILLIS)){
//											throw new RuntimeException("当前日期距生命体征首次录入日期超过"+validTime+"小时，不允许修改！");
//										}									
//									}
//								}
//							}
//						}
//					}
//					if(havePermission){
//						result = joinPoint.proceed();
//					}else{
//						throw new RuntimeException(NO_PERMISSION_STR);
//					}
//				}else{
//					result = joinPoint.proceed();	
//				}
//			} catch (Exception e) {
//				LOGGER.error("",e);
//				result = ((MethodSignature) joinPoint.getSignature()).getReturnType().newInstance();
//				for(Field field : result.getClass().getDeclaredFields()){
//					if(field.getName().equals("ackResult")){
//						ackResult = true;
//					}else if(field.getName().equals("midException")){
//						midException = true;
//					}
//				}
//				if(ackResult){
//					Method setAckResult = result.getClass().getMethod("setAckResult", int.class);
//					setAckResult.invoke(result, -1);
//				}
//				if(midException){
//					Method setMidException = result.getClass().getMethod("setMidException",
//							MidwareException.class);
//					setMidException.invoke(result, new MidwareException(
//							new SysException(e.getMessage()), null));
//				}
//			}
//		}
//		return result;
//	}
}
