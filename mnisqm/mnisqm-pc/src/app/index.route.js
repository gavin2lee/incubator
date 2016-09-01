(function() {
  'use strict';

  angular
    .module('nqms')
    .config(routerConfig);

  /** @ngInject */
  function routerConfig($stateProvider, $urlRouterProvider, $locationProvider) {
    $stateProvider
    // .state('/', {
    //   url: '/home',
    //   templateUrl: 'app/main/main.html',
    //   controller: 'MainController',
    //   controllerAs: 'main'
    // })
      .state('home', {
        url: '/home',
        templateUrl: 'app/main/main.html',
        controller: 'MainController',
        controllerAs: 'main',
        data: {
          displayName: '首页'
        }
      })
      .state('home.files', {
        url: '/files',
        templateUrl: 'app/nursingWorkers/files/files.tpl.html',
        controller: 'NursingWorkersFileController',
        controllerAs: 'vm',
        data: {
          displayName: '员工档案'
        }
      })
      .state('home.add', {
        url: '/add',
        templateUrl: 'app/nursingWorkers/add/add.tpl.html',
        controller: 'HrAddController',
        controllerAs: 'vm',
        data: {
          displayName: '科护长维护'
        }
      })
      .state('home.add.detail', {
        url: '/add/:deptCode',
        templateUrl: 'app/nursingWorkers/add/add.detail.html',
        controller: 'AddDetailController',
        controllerAs: 'vm',
        data: {
          displayName: '员工列表'
        }
      })
      .state('home.alt', {
        url: '/alt',
        templateUrl: 'app/nursingWorkers/alt/alt.tpl.html',
        controller: 'AltCtrl',
        controllerAs: 'vm',
        data: {
          displayName: '员工变更'
        }
      })
      .state('home.alt.detail', {
        url: '/altDetail/:userCode?deptCode&deptName',
        templateUrl: 'app/nursingWorkers/alt/alt.detail.html',
        controller: 'AltDetailCtrl',
        controllerAs: 'vm',
        data: {
          displayName: '变更细则'
        }
      })
      .state('home.dept', {
        url: '/dept',
        templateUrl: 'app/nursingWorkers/dept/dept.tpl.html',
        controller: 'DeptController',
        controllerAs: 'vm'
      })
      .state('home.quit', {
        url: '/quit',
        templateUrl: 'app/nursingWorkers/quit/quit.tpl.html',
        controller: 'HrQuitController',
        controllerAs: 'vm'
      })
      .state('home.search', {
        url: '/search',
        templateUrl: 'app/nursingWorkers/search/search.tpl.html',
        controller: 'HrSearchController',
        controllerAs: 'vm'
      })
      .state('home.qrcode', {
        url: '/qrcode',
        templateUrl: 'app/nursingWorkers/qrcode/qrcode.tpl.html',
        controller: 'HrQrcodeController',
        controllerAs: 'vm'
      })
      .state('home.baseInfo', {
        url: '/baseInfo',
        templateUrl: 'app/nursingWorkers/baseInfo/base.tpl.html',
        controller: 'BaseInfoCtrl',
        controllerAs: 'vm',
        data: {
          displayName: '员工档案'
        }
      })
      .state('home.baseInfo.stat', {
        url: '/stat/:id',
        templateUrl: 'app/nursingWorkers/baseInfo/stat.tpl.html',
        controller: 'BaseInfoStatCtrl',
        controllerAs: 'vm',
        data: {
          displayName: '科室概况'
        }
      })
      .state('home.baseInfo.detail', {
        url: '/detail/:id?dept&deptN&hisCode',
        templateUrl: 'app/nursingWorkers/baseInfo/detail.tpl.html',
        controller: 'BaseInfoDetailCtrl',
        controllerAs: 'vm',
        data: {
          displayName: '详细信息'
        }
      })
      .state('home.internship', {
        url: '/internship',
        templateUrl: 'app/nursingWorkers/internship/internship.tpl.html',
        controller: 'NursingWorkersInternshipController',
        controllerAs: 'vm'
      })
      .state('home.graduate', {
        url: '/graduate',
        templateUrl: 'app/nursingWorkers/graduate/graduate.tpl.html',
        controller: 'NursingWorkersGraduateController',
        controllerAs: 'vm'
      })
      .state('home.technical', {
        url: '/technical',
        templateUrl: 'app/nursingWorkers/technical/technical.tpl.html',
        controller: 'NursingWorkersTechnicalController',
        controllerAs: 'vm'
      })
      .state('home.ulteriorly', {
        url: '/ulteriorly',
        templateUrl: 'app/nursingWorkers/ulteriorly/ulteriorly.tpl.html',
        controller: 'NursingWorkersUlteriorlyController',
        controllerAs: 'vm'
      })
      .state('home.endemicArea', {
        url: '/endemicArea',
        templateUrl: 'app/duty/setting/setting.tpl.html',
        data: {
          displayName: '排班配置'
        }
      })
      .state('home.turns', {
        url: '/turns',
        templateUrl: 'app/duty/turns/turns.tpl.html',
        controller: 'DepartmentManagerTurnsController',
        controllerAs: 'vm',
        data: {
          displayName: '排班表'
        }
      })
      .state('home.leave', {
        url: '/leave',
        templateUrl: 'app/duty/leave/leave.tpl.html',
        data: {
          displayName: '请假'
        }
      })
      .state('home.departmentStatistics', {
        url: '/departmentStatistics',
        templateUrl: 'app/duty/statistics/statistics.tpl.html',
        data: {
          displayName: '统计上报'
        }
      })
      /**
       * 不良事件 start
       */
       .state('home.config', {
         url: '/eventConfig',
         templateUrl: 'app/malCaseUp/config/config.tpl.html',
         controller: 'MalCaseConfigController',
         controllerAs: 'vm',
         data: {
           displayName: '事件配置'
         }
       })
       .state('home.call', {
         url: '/eventCall',
         templateUrl: 'app/malCaseUp/call/call.tpl.html',
         controller: 'MalCaseUpCallController',
         controllerAs: 'vm',
         data: {
           displayName: '不良事件上报'
         }
       })
         .state('home.call.callForm', {
           url: '/callForm/:reportCode',
           templateUrl: 'app/malCaseUp/call/callForm/callForm.tpl.html',
           controller: 'CallFormController',
           controllerAs: 'vm',
           data: {
             displayName: '上报信息'
           },
           params: {
             report: null
           }
         })
         .state('home.call.survey', {
           url: '/survey/:reportCode',
           templateUrl: 'app/malCaseUp/call/survey/survey.tpl.html',
           controller: 'SurveyController',
           controllerAs: 'vm',
           data: {
             displayName: '调查处理表'
           }
         })

       .state('home.fix', {
         url: '/eventFix',
         templateUrl: 'app/malCaseUp/fix/fix.tpl.html',
         controller: 'MalCaseUpHandleController',
         controllerAs: 'vm',
         data: {
           displayName: '不良事件整改'
         }
       })
         .state('home.fix.fixCenter', {
           url: '/eventFixCenter/:reportCode',
           templateUrl: 'app/malCaseUp/fix/abstract.html',
           controller: function ($stateParams) {
             console.log($stateParams);
           },
           abstract: true
         })
         .state('home.fix.fixCenter.caseInfo', {
           url: '/caseInfo',
           templateUrl: 'app/malCaseUp/fix/caseInfo/caseInfo.tpl.html',
           controller: 'CaseInfoController',
           controllerAs: 'vm',
           data: {
             displayName: '不良事件信息'
           }
         })
         .state('home.fix.fixCenter.surveyInfo', {
           url: '/surveyInfo',
           templateUrl: 'app/malCaseUp/fix/surveyInfo/surveyInfo.tpl.html',
           controller: 'SurveyInfoController',
           controllerAs: 'vm',
           data: {
             displayName: '调查处理信息'
           }
         })
         .state('home.fix.fixCenter.requirement', {
           url: '/requirement',
           templateUrl: 'app/malCaseUp/fix/requirement/requirement.tpl.html',
           controller: 'RequirementController',
           controllerAs: 'vm',
           data: {
             displayName: '整改要求'
           }
         })
         .state('home.fix.fixCenter.measures', {
           url: '/measures',
           templateUrl: 'app/malCaseUp/fix/measures/measures.tpl.html',
           controller: 'MeasuresController',
           controllerAs: 'vm',
           data: {
             displayName: '整改措施'
           }
         })
         .state('home.fix.fixCenter.estimate', {
           url: '/estimate',
           templateUrl: 'app/malCaseUp/fix/estimate/estimate.tpl.html',
           controller: 'EstimateController',
           controllerAs: 'vm',
           data: {
             displayName: '整改效果评估'
           }
         })
       .state('home.query', {
         url: '/eventQuery',
         templateUrl: 'app/malCaseUp/handle/handle.tpl.html',
         controller: 'MalCaseUpHandleController',
         controllerAs: 'vm',
         data: {
           displayName: '不良事件整改'
         }
       })
      .state('home.type', {
        url: '/type',
        templateUrl: 'app/malCaseUp/type/type.tpl.html',
        controller: 'MalCaseUpTypeController',
        controllerAs: 'vm',
        data: {
          displayName: '不良事件上报类型'
        }
      })

      .state('home.level', {
        url: '/level',
        templateUrl: 'app/malCaseUp/level/level.tpl.html',
        controller: 'MalCaseUpLevelController',
        controllerAs: 'vm',
        data: {
          displayName: '不良事件分级标准'
        }
      })
      .state('home.handle', {
        url: '/handle',
        templateUrl: 'app/malCaseUp/handle/handle.tpl.html',
        controller: 'MalCaseUpHandleController',
        controllerAs: 'vm',
        data: {
          displayName: '不良事件处理'
        }
      })
      // 不良事件处理、整改、效果评估  start
      .state('home.dispose', {
        url: '/dispose/:reportCode',
        templateUrl: 'app/malCaseUp/handle/dispose/placeholder.html',
        controller: 'MalCaseUpDisposeController',
        controllerAs: 'vm',
        data: {
          displayName: '不良事件整改'
        }
      })
      // 整改
      .state('home.rectify', {
        url: '/rectify/:reportCode',
        templateUrl: 'app/malCaseUp/handle/rectify/rectify.tpl.html',
        controller: 'MalCaseUpRectifyController',
        controllerAs: 'vm'
      })
      // 评估
      .state('home.estimate', {
        url: '/estimate/:reportCode',
        templateUrl: 'app/malCaseUp/handle/estimate/estimate.tpl.html',
        controller: 'MalCaseUpEstimateController',
        controllerAs: 'vm'
      })
      // 不良事件处理、整改、效果评估  end
      .state('home.handle.malCase', {
        url: '/malCase',
        templateUrl: 'app/malCaseUp/handle/malCaseInfo.tpl.html'
      })
      .state('home.handle.report', {
        url: '/report',
        templateUrl: 'app/malCaseUp/handle/report.tpl.html'
      })
      .state('home.handle.reportXk', {
        url: '/reportXk',
        templateUrl: 'app/malCaseUp/handle/reportXk.tpl.html'
      })
      .state('home.handle.requirement', {
        url: '/requirement',
        templateUrl: 'app/malCaseUp/handle/requirement.tpl.html'
      })
      .state('home.handle.measures', {
        url: '/measures',
        templateUrl: 'app/malCaseUp/handle/measures.tpl.html'
      })
      .state('home.handle.estimate', {
        url: '/estimate',
        templateUrl: 'app/malCaseUp/handle/estimate.tpl.html'
      })

      .state('home.element', {
        url: '/element',
        templateUrl: 'app/malCaseUp/element/element.tpl.html',
        controller: 'MalCaseUpElementController',
        controllerAs: 'vm'
      })
      .state('malCaseUpStatistics', {
        url: '/statistics',
        templateUrl: 'app/malCaseUp/statistics/statistics.tpl.html',
        controller: 'MalCaseUpStatisticsController',
        controllerAs: 'vm'
      })
      /**
       * 不良事件 end
       */
      .state('home.roles', {
        url: '/roles',
        templateUrl: 'app/system/roles/roles.tpl.html',
        controller: 'RolesController',
        controllerAs: 'vm'
      })
      .state('home.users', {
        url: '/users',
        templateUrl: 'app/system/users/users.tpl.html',
        controller: 'UsersController',
        controllerAs: 'vm'
      })
      .state('home.workManual', {
        url: '/manualWork',
        templateUrl: 'app/manual/workManual/workManual.tpl.html',
        controller: 'ManualWorkController',
        controllerAs: 'vm'
      })
      .state('home.manualItem', {
        url: '/manualItem',
        templateUrl: 'app/manual/item/item.tpl.html',
        controller: 'ManualItemController',
        controllerAs: 'vm'
      })
      .state('home.qualityCheck', {
        url: '/qualityCheck',
        templateUrl: 'app/quality/check/check.tpl.html',
        controller: 'QualityCheckController',
        controllerAs: 'vm'
      })
      .state('home.qualityDeal', {
        url: '/qualityDeal',
        templateUrl: 'app/quality/deal/deal.tpl.html',
        controller: 'QualityDealController',
        controllerAs: 'vm'
      })
      .state('home.qualityForm', {
        url: '/qualityForm',
        templateUrl: 'app/quality/setting/group/group.tpl.html'
      })
      .state('home.qualityModel', {
        url: '/qualityModel',
        templateUrl: 'app/quality/setting/template/template.tpl.html'
      })
      .state('home.qualityTask', {
        url: '/qualityTask',
        templateUrl: 'app/quality/task/task.tpl.html',
        controller: 'QualityTaskController',
        controllerAs: 'vm'
      })
      .state('home.qualityFormAdd', {
        url: '/qualityFormAdd?id&from',
        templateUrl: 'app/quality/setting/template/add.tpl.html',
        controller: 'QualityAddFormController',
        controllerAs: 'vm'
      })
      .state('home.qualityTaskAdd', {
        url: '/qualityTaskAdd?id',
        templateUrl: 'app/quality/task/add.tpl.html',
        controller: 'QualityTaskAddController',
        controllerAs: 'vm'
      })
      .state('home.qualityUpload', {
        url: '/qualityUpload',
        templateUrl: 'app/quality/upload/upload.tpl.html',
        controller: 'QualityTaskUploadCtrl',
        controllerAs: 'vm'
      })
      .state('home.researchItem', {
        url: '/researchItem',
        templateUrl: 'app/research/item/item.tpl.html',
        controller: 'ResearchItemController',
        controllerAs: 'vm'
      })
      .state('home.researchPapers', {
        url: '/researchPapers',
        templateUrl: 'app/research/papers/papers.tpl.html',
        controller: 'ResearchPapersController',
        controllerAs: 'vm'
      })
      .state('home.techFiles', {
        url: '/techFiles',
        templateUrl: 'app/techFiles/techFiles/techFiles.tpl.html',
        controller: 'TechFileController',
        controllerAs: 'vm'
      })
      .state('home.jci', {
        url: '/jci',
        templateUrl: 'app/techFiles/jci/jci.tpl.html',
        controller: 'TechFileJCIController',
        controllerAs: 'vm'
      })
      .state('home.nurseSatTemp', {
        url: '/nurseSatTemp',
        templateUrl: 'app/sat/nurse/temp.tpl.html',
        controller: 'SatNurseTempController',
        controllerAs: 'vm'
      })
      .state('home.nurseSatResult', {
        url: '/nurseSatResult',
        templateUrl: 'app/sat/nurse/result.tpl.html',
        controller: 'SatNurseResultController',
        controllerAs: 'vm'
      })
      .state('home.patientSatTemp', {
        url: '/patientSatTemp',
        templateUrl: 'app/sat/patient/temp.tpl.html',
        controller: 'SatPatientTempController',
        controllerAs: 'vm'
      })
      .state('home.patientSatResult', {
        url: '/patientSatResult',
        templateUrl: 'app/sat/patient/result.tpl.html',
        controller: 'SatPatientResultController',
        controllerAs: 'vm'
      })
      .state('home.statistics', {
        url: '/statisticsGlance',
        templateUrl: 'app/statistics/main.tpl.html',
        controller: 'StatisticsController',
        controllerAs: 'vm'
      })
      .state('home.nurseManagerStat', {
        templateUrl: 'app/statistics/nurseManager/stat.tpl.html'
      })
      .state('home.nurseManagerStat.form', {
        url: '/nurseManagerStatForm',
        templateUrl: 'app/statistics/nurseManager/form.tpl.html',
        controller: 'NurseManagerStatFormCtrl',
        controllerAs: 'vm'
      })
      .state('home.nurseManagerStat.transfer', {
        url: '/nurseManagerStatTransfer',
        templateUrl: 'app/statistics/nurseManager/transfer.tpl.html',
        controller: 'NurseManagerStatTransferCtrl',
        controllerAs: 'vm'
      })
      .state('home.researchStat', {
        url: '/researchStat',
        templateUrl: 'app/statistics/research/stat.tpl.html',
        controller: 'ResearchStatCtrl',
        controllerAs: 'vm'
      })
      .state('home.dutyStat', {
        url: '/dutyStat',
        templateUrl: 'app/statistics/duty/stat.tpl.html',
        controller: 'DutyStatCtrl',
        controllerAs: 'vm'
      })
      .state('home.examStat', {
        url: '/examStat',
        templateUrl: 'app/statistics/exam/stat.tpl.html',
        controller: 'ExamStatCtrl',
        controllerAs: 'vm'
      })
      .state('home.satStat', {
        templateUrl: 'app/statistics/sat/stat.tpl.html'
      })
      .state('home.satStat.patient', {
        url: '/patientSatStat',
        templateUrl: 'app/statistics/sat/patient.tpl.html',
        controller: 'PatientSatStatCtrl',
        controllerAs: 'vm'
      })
      .state('home.satStat.nurse', {
        url: '/nurseSatStat',
        templateUrl: 'app/statistics/sat/nurse.tpl.html',
        controller: 'NurseSatStatCtrl',
        controllerAs: 'vm'
      })
      .state('home.malCaseStat', {
        url: '/malCaseStat',
        templateUrl: 'app/statistics/malCase/stat.tpl.html',
        controller: 'MalCaseStatCtrl',
        controllerAs: 'vm'
      })
      .state('home.qualityStat', {
        url: '/qualityStat',
        templateUrl: 'app/statistics/quality/stat.tpl.html',
        controller: 'QualityStatCtrl',
        controllerAs: 'vm'
      })
      .state('home.bedNursing', {
        templateUrl: 'app/statistics/bedNursing/stat.tpl.html'
      })
      .state('home.bedNursing.liqDis', {
        url: '/bedNursingLiqDisStat',
        templateUrl: 'app/statistics/bedNursing/liqDis.tpl.html',
        controller: 'BedNursingLiqDisStatCtrl',
        controllerAs: 'vm'
      })
      .state('home.bedNursing.doctor', {
        url: '/bedNursingDoctorStat',
        templateUrl: 'app/statistics/bedNursing/doctor.tpl.html',
        controller: 'BedNursingDoctorStatCtrl',
        controllerAs: 'vm'
      })
      .state('home.bedNursing.doc', {
        url: '/bedNursingDocStat',
        templateUrl: 'app/statistics/bedNursing/doc.tpl.html',
        controller: 'BedNursingDocStatCtrl',
        controllerAs: 'vm'
      })
      .state('home.bedNursing.patrol', {
        url: '/bedNursinPatrolStat',
        templateUrl: 'app/statistics/bedNursing/patrol.tpl.html',
        controller: 'BedNursingPatrolStatCtrl',
        controllerAs: 'vm'
      })
      .state('home.bedNursing.sign', {
        url: '/bedNursinSignStat',
        templateUrl: 'app/statistics/bedNursing/sign.tpl.html',
        controller: 'BedNursingSignStatCtrl',
        controllerAs: 'vm'
      })
      .state('home.systemCalendar', {
        url: '/systemCalendar',
        templateUrl: 'app/system/calendar/calendar.tpl.html',
        controller: 'CalendarController',
        controllerAs: 'vm'
      })
      .state('home.ba', {
        url: '/busAnalyser',
        templateUrl: 'app/bd/ba/ba.tpl.html',
        controller: 'BDBusAnalyserController',
        controllerAs: 'vm',
        data: {
          displayName: '护理业务分析'
        }
      })
      .state('home.er', {
        url: '/errorRate',
        templateUrl: 'app/bd/er/er.tpl.html',
        controller: 'BDErrorRateController',
        controllerAs: 'vm',
        data: {
          displayName: '近似差错率分析'
        }
      })
      .state('home.du', {
        url: '/deptUse',
        templateUrl: 'app/bd/du/du.tpl.html',
        controller: 'BDDeptUseController',
        controllerAs: 'vm',
        data: {
          displayName: '科室使用情况'
        }
      })
      .state('login', {
        url: '/login',
        templateUrl: 'app/login/login.html',
        controller: 'LoginController',
        controllerAs: 'vm'
      });

    $urlRouterProvider.otherwise('/login');
    // $locationProvider.html5Mode(true);
  }
})();
