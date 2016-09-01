package com.lachesis.mnisqm;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.gson.Gson;
import com.lachesis.mnisqm.controller.UserController;
import com.lachesis.mnisqm.module.system.domain.SysUser;
import com.lachesis.mnisqm.module.user.domain.ComUser;
import com.lachesis.mnisqm.module.user.domain.ComUserEducation;
import com.lachesis.mnisqm.module.user.domain.ComUserFamily;
import com.lachesis.mnisqm.module.user.domain.ComUserLearning;
import com.lachesis.mnisqm.module.user.domain.ComUserNursing;
import com.lachesis.mnisqm.module.user.domain.ComUserPosition;
import com.lachesis.mnisqm.module.user.domain.ComUserTraining;

/**
 * Copyright (c) 2016, Lachesis-mh.com
 * All rights reserved.
 * <p/>
 * Discription:用户界面控制器测试类
 * <p/>
 * Created by junming.ren
 * on 2016/3/17.
 */
@RunWith(value=SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class UserControllerTest extends AbstractJUnit4SpringContextTests {
    @Autowired
    UserController controller;

    @Test
    public void getUserDeptsTest(){
        String deptCode = "0001";
        SysUser user = new SysUser();
        user.setComUserCode("000283");
        user.setLoginName("123");
        BaseDataVo vo = new BaseDataVo();
       /* vo  = controller.getUserDepts(user, deptCode);*/
        vo  = controller.getUserDepts(deptCode);
        Gson json = new Gson();
        String result = json.toJson(vo);
        System.out.print(result);
    }
    
    @Test
    public void inserObj(){
    	/*String saveType = "ComUserEducation";
    	ComUserEducation comUserEducation = createEducationInfo();
    	BaseDataVo baseVo = controller.insertObj(JsonHelper.beanToJson(comUserEducation));
    	System.out.println(baseVo.toJsonString());
    	
    	saveType = "ComUserFamily";
    	ComUserFamily comUserFamily = createFamilyInfo();;
    	baseVo = controller.insertObj(JsonHelper.beanToJson(comUserFamily));
    	System.out.println(baseVo.toJsonString());
    	
    	saveType = "ComUserLearning";
    	ComUserLearning comUserLearning = createLearningInfo();;
    	baseVo = controller.insertObj(JsonHelper.beanToJson(comUserLearning));
    	System.out.println(baseVo.toJsonString());
    	
    	saveType = "ComUserTraining";
    	ComUserTraining comUserTraining = createTrainingInfo();;
    	baseVo = controller.insertObj(JsonHelper.beanToJson(comUserTraining));
    	System.out.println(baseVo.toJsonString());
    	
    	saveType = "ComUserPosition";
    	ComUserPosition comUserPosition = createPositionInfo();
    	baseVo = controller.insertObj(JsonHelper.beanToJson(comUserPosition));
    	System.out.println(baseVo.toJsonString());
    	
    	saveType = "ComUserNursing";
    	ComUserNursing comUserNursing = createNursingInfo();
    	baseVo = controller.insertObj(JsonHelper.beanToJson(comUserNursing));
    	System.out.println(baseVo.toJsonString());*/
    	
    }
    
    @Test
    public void deleteObj(){
    	/*String saveType = "ComUserEducation";
    	ComUserEducation comUserEducation = createEducationInfo();
    	BaseDataVo baseVo = controller.deleteObj(JsonHelper.beanToJson(comUserEducation));
    	System.out.println(baseVo.toJsonString());
    	
    	saveType = "ComUserFamily";
    	ComUserFamily comUserFamily = createFamilyInfo();;
    	baseVo = controller.deleteObj(JsonHelper.beanToJson(comUserFamily));
    	System.out.println(baseVo.toJsonString());
    	
    	saveType = "ComUserLearning";
    	ComUserLearning comUserLearning = createLearningInfo();;
    	baseVo = controller.deleteObj(JsonHelper.beanToJson(comUserLearning));
    	System.out.println(baseVo.toJsonString());
    	
    	saveType = "ComUserTraining";
    	ComUserTraining comUserTraining = createTrainingInfo();;
    	baseVo = controller.deleteObj(JsonHelper.beanToJson(comUserTraining));
    	System.out.println(baseVo.toJsonString());
    	
    	saveType = "ComUserPosition";
    	ComUserPosition comUserPosition = createPositionInfo();
    	baseVo = controller.deleteObj(JsonHelper.beanToJson(comUserPosition));
    	System.out.println(baseVo.toJsonString());
    	
    	saveType = "ComUserNursing";
    	ComUserNursing comUserNursing = createNursingInfo();
    	baseVo = controller.deleteObj(JsonHelper.beanToJson(comUserNursing));
    	System.out.println(baseVo.toJsonString());*/
    	
    }
    
    @Test
    public void updateObj(){
    	/*String saveType = "ComUserEducation";
    	ComUserEducation comUserEducation = createEducationInfo();
    	BaseDataVo baseVo = controller.updateObj(JsonHelper.beanToJson(comUserEducation));
    	System.out.println(baseVo.toJsonString());
    	
    	saveType = "ComUserFamily";
    	ComUserFamily comUserFamily = createFamilyInfo();;
    	baseVo = controller.updateObj(JsonHelper.beanToJson(comUserFamily));
    	System.out.println(baseVo.toJsonString());
    	
    	saveType = "ComUserLearning";
    	ComUserLearning comUserLearning = createLearningInfo();;
    	baseVo = controller.updateObj(JsonHelper.beanToJson(comUserLearning));
    	System.out.println(baseVo.toJsonString());
    	
    	saveType = "ComUserTraining";
    	ComUserTraining comUserTraining = createTrainingInfo();;
    	baseVo = controller.updateObj(JsonHelper.beanToJson(comUserTraining));
    	System.out.println(baseVo.toJsonString());
    	
    	saveType = "ComUserPosition";
    	ComUserPosition comUserPosition = createPositionInfo();
    	baseVo = controller.updateObj(JsonHelper.beanToJson(comUserPosition));
    	System.out.println(baseVo.toJsonString());
    	
    	saveType = "ComUserNursing";
    	ComUserNursing comUserNursing = createNursingInfo();
    	baseVo = controller.updateObj(JsonHelper.beanToJson(comUserNursing));
    	System.out.println(baseVo.toJsonString());*/
    	
    }
    

    @Test
    public void getUserDetailInfo() {
        String userCode = "000283";
        BaseDataVo vo = new BaseDataVo();
        vo = controller.getUserDetailInfo(userCode);
        Gson gson = new Gson();
        String result = gson.toJson(vo);
        System.out.println(result);
    }

    @Test
    @Rollback
    public void saveUserInfo(){
        BaseDataVo outUser = new BaseDataVo();
        ComUser baseInfo = createBaseInfo();
        ComUserFamily familyInfo = createFamilyInfo();
        ComUserLearning learningInfo = createLearningInfo();
        ComUserTraining trainingInfo = createTrainingInfo();
        ComUserEducation educationInfo = createEducationInfo();
        ComUserNursing nursingInfo = createNursingInfo();
        ComUserPosition positionInfo = createPositionInfo();

       /* controller.saveUserInfo(baseInfo);
        controller.saveUserEducationInfo(educationInfo);
        controller.saveUserFamilyInfo(familyInfo);
        controller.saveUserLearningInfo(learningInfo);
        controller.saveUserNursingInfo(nursingInfo);
        controller.saveUserPositionInfo(positionInfo);
        controller.saveUserTrainingInfo(trainingInfo);*/

        outUser = controller.getUserDetailInfo("111111");
        Gson gson = new Gson();
        String result = gson.toJson(outUser);
        System.out.println(result);
    }

    private ComUser createBaseInfo(){
        ComUser baseInfo = new ComUser();
        baseInfo.setCode("111111");
        baseInfo.setEmail("123@mail.com");
        baseInfo.setStatus("1");
        baseInfo.setGender("1");
        baseInfo.setUserType("1");
//        baseInfo.setLoginName("123");
//        baseInfo.setPassword("123");
        baseInfo.setName("name");
        baseInfo.setPhone("123456789");
//        baseInfo.setRemark("备注");
        baseInfo.setCreateTime(new Date());
        baseInfo.setUpdateTime(new Date());
        return baseInfo;
    }

    private ComUserFamily createFamilyInfo(){
        ComUserFamily familyInfo = new ComUserFamily();
        familyInfo.setId(3l);
        familyInfo.setUserCode("123");
        familyInfo.setRemark("备注");
        familyInfo.setEmail("123@mail.com");
        familyInfo.setPhone("123456789");
        familyInfo.setName("name01");
        familyInfo.setRelation("relation");
        familyInfo.setStatus("1");
        familyInfo.setCreateBy("tester01");
        familyInfo.setUpdateBy("tester01");
        Date date = new Date();
        familyInfo.setCreateTime(date);
        familyInfo.setUpdateTime(date);
        return familyInfo;
    }

    private ComUserLearning createLearningInfo(){
        ComUserLearning learningInfo = new ComUserLearning();
        learningInfo.setId(17l);
        learningInfo.setUserCode("123");
        learningInfo.setStatus("1");
        learningInfo.setCourseContent("内容");
        learningInfo.setCourseTopics("话题");
        learningInfo.setEducationType("01");
        learningInfo.setLearningTime(new Long(12));
        learningInfo.setStartDate("2016-03-21");
        learningInfo.setEndDate("2016-03-21");
        learningInfo.setCreateBy("tester01");
        learningInfo.setUpdateBy("tester01");
        learningInfo.setCreateTime(new Date());
        learningInfo.setUpdateTime(new Date());

        return learningInfo;
    }

    private ComUserTraining createTrainingInfo(){
        ComUserTraining trainingInfo = new ComUserTraining();
        trainingInfo.setId(3l);
        trainingInfo.setUserCode("123");
        trainingInfo.setDescribe("秒速");
        trainingInfo.setDiploma("学位");
        trainingInfo.setProfessional("专业");
        trainingInfo.setSchool("学校");
        trainingInfo.setStartDate("2016-03-21");
        trainingInfo.setEndDate("2016-03-21");
        trainingInfo.setCreateBy("tester01");
        trainingInfo.setUpdateBy("tester01");
        trainingInfo.setStatus("1");
        trainingInfo.setCreateTime(new Date());
        trainingInfo.setUpdateTime(new Date());
        return trainingInfo;
    }

    private ComUserEducation createEducationInfo(){
        ComUserEducation educationInfo = new ComUserEducation();
        educationInfo.setId(3L);
        educationInfo.setUserCode("123");
        educationInfo.setSchool("学校01");
        educationInfo.setProfessional("专业1");
        educationInfo.setDiploma("学位");
        educationInfo.setDescribe("描述");
        educationInfo.setStatus("1");
        educationInfo.setStartDate("2016-03-21");
        educationInfo.setEndDate("2016-03-21");
        educationInfo.setCreateBy("tester01");
        educationInfo.setUpdateBy("tester01");
        educationInfo.setCreateTime(new Date());
        educationInfo.setUpdataTime(new Date());

        return educationInfo;
    }

    private ComUserNursing createNursingInfo(){
        ComUserNursing nursingInfo = new ComUserNursing();
        nursingInfo.setId(3l);
        nursingInfo.setUserCode("123");
        nursingInfo.setStatus("1");
        nursingInfo.setPosition("职位");
        nursingInfo.setReviewUnit("部门");
        nursingInfo.setUnitLevel("1");
        nursingInfo.setStartDate("2016-03-21");
        nursingInfo.setEndDate("2016-03-21");
        nursingInfo.setCreateBy("tester01");
        nursingInfo.setUpdateBy("tester01");
        nursingInfo.setCreateTime(new Date());
        nursingInfo.setUpdateTime(new Date());
        return nursingInfo;
    }

    private ComUserPosition createPositionInfo(){
        ComUserPosition positionInfo = new ComUserPosition();
        positionInfo.setId(3l);
        positionInfo.setUserCode("123");
        positionInfo.setReviewUnit("部门");
        positionInfo.setPositionName("职务");
        positionInfo.setStatus("1");
        positionInfo.setStartDate("2016-03-21");
        positionInfo.setEndDate("2016-03-21");
        positionInfo.setCreateBy("tester01");
        positionInfo.setUpdateBy("tester10");
        positionInfo.setCreateTime(new Date());
        positionInfo.setUpdateTime(new Date());
        return positionInfo;
    }
}
