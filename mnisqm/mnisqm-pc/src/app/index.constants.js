/* global moment:false */
(function() {
  'use strict';

  angular
    .module('nqms')
    .constant('moment', moment)
    .constant('apiHost', (function() {
      //var path = 'http://127.0.0.1:8003/mnisqm';
      var path = '/mnisqm';

      return {
        system: {
          login: path + '/system/login',
          getRoleList: path + '/system/getRoleLst',
          getDic: {
            url: path + '/system/getSysDic',
            method: 'post'
          },
          getNavigation: path + '/system/getFunction',

          calendar: {
            setter: {
              url: path + '/system/creatSysDate',
              method: 'post'
            },
            update: {
              url: path + '/system/updateSysDate',
              method: 'post'
            },
            getter: {
              url: path + '/system/querySysDate',
              method: 'get'
            }
          }
        },
        user: {
          getDept: path + '/user/getUserDepts',
          quit: {
            url: path + '/user/changeUserType',
            method: 'post'
          },
          retire: {
            url: path + '/user/changeUserType',
            method: 'post'
          },
          getComUserChange: {
            url: path + '/user/getAllComUserChange',
            method: 'get'
          },
          setUserHeader: {
            url: path + '/user/saveDeptNurseHeader',
            method: 'post'
          }
        },
        files: {
          getUserList: path + '/user/getUserList',
          getDimissionUserList: path + '/user/getDimissionUserList',
          getUserDetailInfo: path + '/user/getUserDetailInfo',
          add: path + '/user/insertObj',
          delete: path + '/user/deleteUserInfo',
          update: path + '/user/updateObj',
          save: path + '/user/saveComUser',
          saveChange: path + '/user/saveComUserChange',
          saveUserDept: path + '/user/saveUserDept',
          getUserStatistics: path + '/user/getUserStatistics'
        },
        roles: {
          getNotHaveFunction: path + '/system/getNotHaveFunction',
          getFunction: path + '/system/getFunction',
          getHaveFunction: path + '/system/getHaveFunction',
          updateRole: path + '/system/saveSysRoleFuns',
          deleteRole: path + '/system/deleteRole',
          saveRole: path + '/system/saveRole'
        },
        users: {
          getUserList: path + '/system/getSysUserList',
          deleteUser: path + '/system/deleteSysUser',
          deleteUserRole: path + '/system/deleteSysUserRole',
          saveUserRole: path + '/system/saveSysUserRole',
          saveUser: path + '/system/saveSysUser'
        },
        events: {
          //获取不良是类型
          getEventTypeList: path + '/event/getEventTypeList',
          getEventList: path + '/event/getEventList',
          getEventReport: path + '/event/getEventReport',
          saveEventReport: path + '/event/saveEventReport',
          deleteEventType: path + '/event/deleteEventType',
          deleteEventReport: path + '/event/deleteEventReport',
          getReportDetail: path + '/event/getReportDetail',
          // 不良事件调查信息新增／修改
          saveReportDetail: path + '/event/saveReportDetail',
          // 不良事件要求新增／修改
          saveEventRequirement: path + '/event/saveEventRequirement',
          // 不良事件事件要求查询
          getEventRequirement: path + '/event/getEventRequirement',
          // 不良事件要求删除
          deleteEventRequirement: path + '/event/deleteEventRequirement',
          // 不良事件措施新增／修改
          saveEventMeasures: path + '/event/saveEventMeasures',
          // 不良事件措施查询
          getEventMeasures: path + '/event/getEventMeasures',
          // 不良事件措施删除
          deleteEventMeasures: path + '/event/deleteEventMeasures',
          // 不良事件报告关闭
          // 待定
          // 不良事件级别保存
          saveEventLevel: path + '/event/saveEventLevel',
          // 不良事件级别查询
          getEventLevels: path + '/event/getEventLevels',
          // 不良事件分级删除
          deleteEventLevel: path + '/event/deleteEventLevel',
          // 不良事件处理
          handleEventReport: path + '/event/handleEventReport'
        },
        schedule: {
          copySchedule: path + "/schedule/copySchedule",
          //新建排班
          newSchedule: path + '/schedule/newSchedule',
          //保存排班
          saveSchedule: path + '/schedule/saveSchedule',
          //获取排班
          getSchedule: path + '/schedule/getSchedule',
          //查询科室分组管理
          getGroupList: path + '/schedule/getGroupList',
          //删除分组
          deleteGroup: path + '/schedule/deleteGroup',
          //保存分组
          saveGroup: path + '/schedule/saveGroup',
          //查询科室班次信息
          getClassList: path + '/schedule/getClassList',
          //保存班次新
          saveClass: path + '/schedule/saveClass',
          //删除班次信息
          deleteClass: path + '/schedule/deleteClass',
          //查询请假信息
          getLeaveList: path + '/schedule/getLeaveList',
          //保存请假
          saveScheduleLeave: path + '/schedule/saveScheduleLeave',
          //删除请假信息
          deleteScheduleLeave: path + '/schedule/deleteScheduleLeave',
          //审批请假
          apprvScheduleLeave: path + '/schedule/apprvScheduleLeave',
          //排班规则查询
          getRuleList: path + '/schedule/getRuleList',
          //保存排班规则
          saveRule: path + '/schedule/saveRule',
          //删除排班规则
          deleteRule: path + '/schedule/deleteRule',
          //查询病区护理人员
          getUserNurses: path + '/schedule/getUserNurses',
          //保存病区护理人员
          batchSaveUserNurse: path + '/schedule/batchSaveUserNurse',
          //获取调班信息
          getChangeClass: path + '/schedule/getChangeClass',
          //保存调班
          saveChangeClass: path + '/schedule/saveChangeClass',
          //删除调班
          deleteChangeClass: path + '/schedule/deleteChangeClass',
          //审批调班
          apprvChangeClass: path + '/schedule/apprvChangeClass',
          statistics: {
            getByLeave: path + '/schedule/getCountByLeave',
            getByDays: path + '/schedule/getCountByDays',
            getByClass: path + '/schedule/getCountByClass'
          }
        },
        manual: {
          item: {
            get: path + '/manualWork/queryManualWorkItemByDeptCode',
            save: path + '/manualWork/saveManualWorkItem',
            delete: path + '/manualWork/deleteManualWorkItem',
            update: path + '/manualWork/updateManualWorkItem'
          },
          workManual: {
            get: path + '/manualWork/queryManualWorkByDeptCodeAndDate',
            save: path + '/manualWork/saveManualWork',
            delete: path + '/manualWork/saveManualWorkDetail',
            update: path + '/manualWork/updateManualWorkDetail'
          }
        },
        research: {
          //查询科研项目列表
          getResearchItems: path + '/research/getResearchItems',
          //保存科研项目
          saveResearchItem: path + '/research/saveResearchItem',
          //删除科研项目
          deleteResearchItem: path + '/research/deleteResearchItem',
          //保存论文
          saveResearchPaper: path + '/research/saveResearchPaper',
          //查询论文
          getResearchPapers: path + '/research/getResearchPapers',
          //删除论文
          deleteResearchPaper: path + '/research/deleteResearchPaper'

        },
        qualityManager: {
          //获取质量检查表单列表
          getQualityFormList: path + '/quality/queryQualityFormByDeptCodeAndType',
          //保存修改质量检查表单模板
          editQualityForm: path + '/quality/updateQualityForm',
          //保存质量检查表单模板
          saveQualityForm: path + '/quality/saveQualityForm',
          //删除质量检查表单模板
          deleteQualityForm: path + '/quality/deleteQualityForm',
          //获取检查任务列表
          getQualityTaskList: path + '/quality/queryQTRByTimeAndTypeAndDeptCode',
          //修改检查任务
          editQualityTask: path + '/quality/updateQualityTask',
          //保存检查任务
          saveQualityTask: path + '/quality/saveQualityTask',
          //删除检查任务
          deleteQualityTask: path + '/quality/deleteQualityTask',
          //获取所有检查表单
          getAllQualityForm: path + '/quality/queryAllQualityForm',
          //查看检查情况
          getQualityCheckDetail: path + '/quality/queryQRByTimeAndFormNameAndDeptCode',
          //获取质量检查列表
          getQualityCheckList: path + '/quality/queryQRByTimeAndFormNameAndDeptCode',
          //修改质量检查
          editQualityCheck: path + '/quality/updateQualityResult',
          //删除质量检查
          deleteQualityCheck: path + '/quality/deleteQualityResult',
          //获取质量检查问题处理列表
          getQualityDealList: path + '/quality/getQualityIssueManageByResultCode',
          //保存质量检查问题处理
          saveQualityDeal: path + '/quality/saveQualityIssueManage',
          //修改质量检查问题处理
          editQualityDeal: path + '/quality/updateQualityIssueManage',
          //删除质量检查问题处理
          deleteQualityDeal: path + '/quality/deleteQualityIssueManage',
          //获取resultCode
          getQualityResultCode: path + '/quality/queryQIMByTimeAndTypeAndDeptCode',
          group: {
            setter: {
              url: path + '/quality/saveQualityTeam',
              method: 'post'
            },
            getter: {
              url: path + '/quality/queryQualityTeamByTeamName',
              method: 'post'
            },
            deleter: {
              url: path + '/quality/deleteQualityTeam',
              method: 'get'
            }
          },
          model: {
            setter: {
              url: path + '/quality/saveQualityModel',
              method: 'post'
            },
            getter: {
              url: path + '/quality/getQualityModel',
              method: 'get'
            },
            deleter: {
              url: path + '/quality/deleteQualityModel',
              method: 'get'
            },
            delOption: {
              url: path + '/quality/deleteQualityFormDetail',
              method: 'get'
            },
            copyModel: {
              url: path + '/quality/copyQualityModel',
              method: 'get'
            }
          },
          plan: {
            setter: {
              url: path + '/quality/saveQualityPlan',
              method: 'post'
            },
            getter: {
              url: path + '/quality/getQualityPlan',
              method: 'get'
            },
            deleter: {
              url: path + '/quality/deleteQualityPlan',
              method: 'get'
            }
          },
          result: {
            setter: {
              url: path + '/quality/saveQualityResult',
              method: 'post'
            }
          }
        },
        techFiles: {
          //获取护士列表
          getNurseList: {
            url: path + '/user/getUserList',
            method: 'get'
          },
          updateNurseInfo: {
            url: path + '/user/saveComUser',
            method: 'post'
          },
          //学术会议、学习
          academic: {
            setter: {
              url: path + '/ProfSkillDoc/saveAcademicConference',
              method: 'post'
            },
            getter: {
              url: path + '/ProfSkillDoc/getAcademicConferences',
              method: 'post'
            },
            deleter: {
              url: path + '/ProfSkillDoc/deleteAcademicConference',
              method: 'get'
            }
          },
          //评估考核
          assessment: {
            setter: {
              url: path + '/ProfSkillDoc/saveAssessment',
              method: 'post'
            },
            getter: {
              url: path + '/ProfSkillDoc/getAssessment',
              method: 'post'
            },
            deleter: {
              url: path + '/ProfSkillDoc/deleteAssessment',
              method: 'get'
            }
          },
          //奖励惩罚
          awardPunish: {
            setter: {
              url: path + '/ProfSkillDoc/saveAwardPunishmentInfo',
              method: 'post'
            },
            getter: {
              url: path + '/ProfSkillDoc/getAwardPunishmentInfo',
              method: 'post'
            },
            deleter: {
              url: path + '/ProfSkillDoc/deleteAwardPunishmentInfo',
              method: 'get'
            }
          },
          //继续教育
          conEducation: {
            setter: {
              url: path + '/ProfSkillDoc/saveContinuingEducation',
              method: 'post'
            },
            getter: {
              url: path + '/ProfSkillDoc/getContinuingEducation',
              method: 'post'
            },
            deleter: {
              url: path + '/ProfSkillDoc/deleteContinuingEducation',
              method: 'get'
            }
          },
          //CPR/ACLS证书
          cprAcls: {
            setter: {
              url: path + '/ProfSkillDoc/saveCprAcls',
              method: 'post'
            },
            getter: {
              url: path + '/ProfSkillDoc/getCprAcls',
              method: 'post'
            },
            deleter: {
              url: path + '/ProfSkillDoc/deleteCprAcls',
              method: 'get'
            }
          },
          //教育记录
          eduRecord: {
            setter: {
              url: path + '/ProfSkillDoc/saveEducationRecordInspection',
              method: 'post'
            },
            getter: {
              url: path + '/ProfSkillDoc/getEducationRecordInspection',
              method: 'post'
            },
            deleter: {
              url: path + '/ProfSkillDoc/deleteEducationRecordInspection',
              method: 'get'
            }
          },
          //护士分层分级
          nurseHirReg: {
            setter: {
              url: path + '/ProfSkillDoc/saveNurseHirarchicalRegistration',
              method: 'post'
            },
            getter: {
              url: path + '/ProfSkillDoc/getNurseHirarchicalRegistration',
              method: 'post'
            },
            deleter: {
              url: path + '/ProfSkillDoc/deleteNurseHirarchicalRegistration',
              method: 'get'
            }
          },
          //个人考核
          perAssessment: {
            setter: {
              url: path + '/ProfSkillDoc/savePersonalAssessment',
              method: 'post'
            },
            getter: {
              url: path + '/ProfSkillDoc/getPersonalAssessment',
              method: 'post'
            },
            deleter: {
              url: path + '/ProfSkillDoc/deletePersonalAssessment',
              method: 'get'
            }
          },
          //岗位职责
          posStatement: {
            setter: {
              url: path + '/ProfSkillDoc/savePositionStatement',
              method: 'post'
            },
            getter: {
              url: path + '/ProfSkillDoc/getPositionStatement',
              method: 'post'
            },
            deleter: {
              url: path + '/ProfSkillDoc/deletePositionStatement',
              method: 'get'
            }
          },
          //岗前培训
          preJobTrain: {
            setter: {
              url: path + '/ProfSkillDoc/savePreJobTraining',
              method: 'post'
            },
            getter: {
              url: path + '/ProfSkillDoc/getPreJobTraining',
              method: 'post'
            },
            deleter: {
              url: path + '/ProfSkillDoc/deletePreJobTraining',
              method: 'get'
            }
          },
          //护士经历
          nurseExp: {
            setter: {
              url: path + '/ProfSkillDoc/saveSpecialistNursesExperience',
              method: 'post'
            },
            getter: {
              url: path + '/ProfSkillDoc/getSpecialistNursesExperience',
              method: 'post'
            },
            deleter: {
              url: path + '/ProfSkillDoc/deleteSpecialistNursesExperience',
              method: 'get'
            }
          }
        },
        satisfaction: {
          satTemp: {
            single: {
              url: path + '/satTemplate/getSatTemplateByFormCode',
              method: 'get'
            },
            setter: {
              url: path + '/satTemplate/saveSatTemplate',
              method: 'post'
            },
            updater: {
              url: path + '/satTemplate/updateSatTemplate',
              method: 'post'
            },
            deleter: {
              url: path + '/satTemplate/deleteSatTemplate',
              method: 'post'
            },
            getter: {
              url: path + '/satTemplate/querySatTemplateByFormType',
              method: 'get'
            }
          },
          satResult: {
            setter: {
              url: path + '/satTemplate/saveSatResult',
              method: 'post'
            },
            updater: {
              url: path + '/satTemplate/updateSatResult',
              method: 'post'
            },
            deleter: {
              url: path + '/satTemplate/deleteSatResult',
              method: 'post'
            },
            getter: {
              url: path + '/satTemplate/queryByDateAndFormTypeAndPatient',
              method: 'get'
            }
          }
        },
        deptManager: {
          dept: {
            setter: {
              url: path + '/user/saveDept',
              method: 'post'
            },
            deleter: {
              url: path + '/user/deleteDept',
              method: 'get'
            },
            getter: {
              url: path + '/user/getAllDepts',
              method: 'get'
            }
          },
          userDept: {
            setter: {
              url: path + '/user/saveUserDept',
              method: 'post'
            },
            deleter: {
              url: path + '/user/deleteUserDept',
              method: 'get'
            }
          }
        },
        businessAnalyser: {
          ba: {
            getter: {
              url: path + '/smart/getMnisStatis',
              method: 'get'
            }
          }
        }
      };
    })());
})();
