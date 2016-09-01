/*==============================================================*/
/* DBMS name:      Microsoft SQL Server 2005                    */
/* Created on:     2014/10/15 15:42:59                          */
/*==============================================================*/


if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('ALLERGY_DRUG') and o.name = 'FK_ALLERGY__REFERENCE_ALLERGY_')
alter table ALLERGY_DRUG
   drop constraint FK_ALLERGY__REFERENCE_ALLERGY_
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('ALLERGY_DRUG_ITEM') and o.name = 'FK_ALLY_REF_ALLERGY_DRUG')
alter table ALLERGY_DRUG_ITEM
   drop constraint FK_ALLY_REF_ALLERGY_DRUG
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('EXECUTIVE_ORDER_SUMMARY_ITEM') and o.name = 'FK_EXE_ORD_SUM_REF_ITEM')
alter table EXECUTIVE_ORDER_SUMMARY_ITEM
   drop constraint FK_EXE_ORD_SUM_REF_ITEM
go

if exists (select 1
            from  sysobjects
           where  id = object_id('V_EXECUTIVE_ORDER_SUMMARY')
            and   type = 'V')
   drop view V_EXECUTIVE_ORDER_SUMMARY
go

if exists (select 1
            from  sysobjects
           where  id = object_id('ALLERGY_DRUG')
            and   type = 'U')
   drop table ALLERGY_DRUG
go

if exists (select 1
            from  sysobjects
           where  id = object_id('ALLERGY_DRUG_CATEGORY')
            and   type = 'U')
   drop table ALLERGY_DRUG_CATEGORY
go

if exists (select 1
            from  sysobjects
           where  id = object_id('ALLERGY_DRUG_ITEM')
            and   type = 'U')
   drop table ALLERGY_DRUG_ITEM
go

if exists (select 1
            from  sysobjects
           where  id = object_id('DEPARTMENT_PATIENT_SUMMARY')
            and   type = 'U')
   drop table DEPARTMENT_PATIENT_SUMMARY
go

if exists (select 1
            from  sysobjects
           where  id = object_id('EXECUTIVE_ORDER_SUMMARY')
            and   type = 'U')
   drop table EXECUTIVE_ORDER_SUMMARY
go

if exists (select 1
            from  sysobjects
           where  id = object_id('EXECUTIVE_ORDER_SUMMARY_ITEM')
            and   type = 'U')
   drop table EXECUTIVE_ORDER_SUMMARY_ITEM
go

/*==============================================================*/
/* Table: ALLERGY_DRUG                                          */
/*==============================================================*/
create table ALLERGY_DRUG (
   REFID                varchar(32)          not null,
   CATEGORY_REFID       varchar(32)          null,
   DRUG_NAME            nvarchar(60)         null,
   ORD                  int                  null,
   CREATE_USER_REFID    varchar(32)          null,
   CREATE_DATE_TIME     datetime             null,
   MODIFY_USER_REFID    varchar(32)          null,
   MODIFY_DATE_TIME     datetime             null,
   VERSION              int                  null,
   ACTIVE               bit                  null,
   constraint PK_ALLERGY_DRUG primary key (REFID)
)
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '过敏药物',
   'user', @CurrentUser, 'table', 'ALLERGY_DRUG'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '过敏药物分类Id',
   'user', @CurrentUser, 'table', 'ALLERGY_DRUG', 'column', 'CATEGORY_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '过敏药物名称',
   'user', @CurrentUser, 'table', 'ALLERGY_DRUG', 'column', 'DRUG_NAME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '次序',
   'user', @CurrentUser, 'table', 'ALLERGY_DRUG', 'column', 'ORD'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改人',
   'user', @CurrentUser, 'table', 'ALLERGY_DRUG', 'column', 'CREATE_USER_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改时间',
   'user', @CurrentUser, 'table', 'ALLERGY_DRUG', 'column', 'CREATE_DATE_TIME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改人',
   'user', @CurrentUser, 'table', 'ALLERGY_DRUG', 'column', 'MODIFY_USER_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改时间',
   'user', @CurrentUser, 'table', 'ALLERGY_DRUG', 'column', 'MODIFY_DATE_TIME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '版本号',
   'user', @CurrentUser, 'table', 'ALLERGY_DRUG', 'column', 'VERSION'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '标示是否有效',
   'user', @CurrentUser, 'table', 'ALLERGY_DRUG', 'column', 'ACTIVE'
go

/*==============================================================*/
/* Table: ALLERGY_DRUG_CATEGORY                                 */
/*==============================================================*/
create table ALLERGY_DRUG_CATEGORY (
   REFID                varchar(32)          not null,
   CATEGORY_CODE        nvarchar(20)         null,
   CATEGORY_NAME        nvarchar(60)         null,
   ORD                  int                  null,
   CREATE_USER_REFID    varchar(32)          null,
   CREATE_DATE_TIME     datetime             null,
   MODIFY_USER_REFID    varchar(32)          null,
   MODIFY_DATE_TIME     datetime             null,
   VERSION              int                  null,
   ACTIVE               bit                  null,
   constraint PK_ALLERGY_DRUG_CATEGORY primary key (REFID)
)
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '过敏药物分类',
   'user', @CurrentUser, 'table', 'ALLERGY_DRUG_CATEGORY'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '过敏药物分类代码',
   'user', @CurrentUser, 'table', 'ALLERGY_DRUG_CATEGORY', 'column', 'CATEGORY_CODE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '过敏药物分类名称',
   'user', @CurrentUser, 'table', 'ALLERGY_DRUG_CATEGORY', 'column', 'CATEGORY_NAME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '次序',
   'user', @CurrentUser, 'table', 'ALLERGY_DRUG_CATEGORY', 'column', 'ORD'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改人',
   'user', @CurrentUser, 'table', 'ALLERGY_DRUG_CATEGORY', 'column', 'CREATE_USER_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改时间',
   'user', @CurrentUser, 'table', 'ALLERGY_DRUG_CATEGORY', 'column', 'CREATE_DATE_TIME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改人',
   'user', @CurrentUser, 'table', 'ALLERGY_DRUG_CATEGORY', 'column', 'MODIFY_USER_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改时间',
   'user', @CurrentUser, 'table', 'ALLERGY_DRUG_CATEGORY', 'column', 'MODIFY_DATE_TIME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '版本号',
   'user', @CurrentUser, 'table', 'ALLERGY_DRUG_CATEGORY', 'column', 'VERSION'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '标示是否有效',
   'user', @CurrentUser, 'table', 'ALLERGY_DRUG_CATEGORY', 'column', 'ACTIVE'
go

/*==============================================================*/
/* Table: ALLERGY_DRUG_ITEM                                     */
/*==============================================================*/
create table ALLERGY_DRUG_ITEM (
   REFID                varchar(32)          not null,
   ALLERGY_DRUG_REFID   varchar(32)          null,
   DRUG_ID              varchar(32)          null,
   R_DRUG_NAME          nvarchar(60)         null,
   CREATE_USER_REFID    varchar(32)          null,
   CREATE_DATE_TIME     datetime             null,
   MODIFY_USER_REFID    varchar(32)          null,
   MODIFY_DATE_TIME     datetime             null,
   VERSION              int                  null,
   ACTIVE               bit                  null,
   constraint PK_ALLERGY_DRUG_ITEM primary key (REFID)
)
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '过敏药物HIS药物',
   'user', @CurrentUser, 'table', 'ALLERGY_DRUG_ITEM'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '过敏药物Id',
   'user', @CurrentUser, 'table', 'ALLERGY_DRUG_ITEM', 'column', 'ALLERGY_DRUG_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   'HIS药物Id',
   'user', @CurrentUser, 'table', 'ALLERGY_DRUG_ITEM', 'column', 'DRUG_ID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   'HIS药物名称',
   'user', @CurrentUser, 'table', 'ALLERGY_DRUG_ITEM', 'column', 'R_DRUG_NAME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改人',
   'user', @CurrentUser, 'table', 'ALLERGY_DRUG_ITEM', 'column', 'CREATE_USER_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改时间',
   'user', @CurrentUser, 'table', 'ALLERGY_DRUG_ITEM', 'column', 'CREATE_DATE_TIME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改人',
   'user', @CurrentUser, 'table', 'ALLERGY_DRUG_ITEM', 'column', 'MODIFY_USER_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改时间',
   'user', @CurrentUser, 'table', 'ALLERGY_DRUG_ITEM', 'column', 'MODIFY_DATE_TIME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '版本号',
   'user', @CurrentUser, 'table', 'ALLERGY_DRUG_ITEM', 'column', 'VERSION'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '标示是否有效',
   'user', @CurrentUser, 'table', 'ALLERGY_DRUG_ITEM', 'column', 'ACTIVE'
go

/*==============================================================*/
/* Table: DEPARTMENT_PATIENT_SUMMARY                            */
/*==============================================================*/
create table DEPARTMENT_PATIENT_SUMMARY (
   REFID                varchar(32)          not null,
   DEPT_REFID           varchar(32)          null,
   SUMMARY_TIME         datetime             null,
   SUMMARY              nvarchar(300)        null,
   CREATE_USER_REFID    varchar(32)          null,
   CREATE_DATE_TIME     datetime             null,
   MODIFY_USER_REFID    varchar(32)          null,
   MODIFY_DATE_TIME     datetime             null,
   VERSION              int                  null,
   ACTIVE               bit                  null,
   constraint PK_DEPARTMENT_PATIENT_SUMMARY primary key (REFID)
)
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '部门患者摘要',
   'user', @CurrentUser, 'table', 'DEPARTMENT_PATIENT_SUMMARY'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '科室。填充科室编码',
   'user', @CurrentUser, 'table', 'DEPARTMENT_PATIENT_SUMMARY', 'column', 'DEPT_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '统计时间',
   'user', @CurrentUser, 'table', 'DEPARTMENT_PATIENT_SUMMARY', 'column', 'SUMMARY_TIME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '患者摘要',
   'user', @CurrentUser, 'table', 'DEPARTMENT_PATIENT_SUMMARY', 'column', 'SUMMARY'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改人',
   'user', @CurrentUser, 'table', 'DEPARTMENT_PATIENT_SUMMARY', 'column', 'CREATE_USER_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改时间',
   'user', @CurrentUser, 'table', 'DEPARTMENT_PATIENT_SUMMARY', 'column', 'CREATE_DATE_TIME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改人',
   'user', @CurrentUser, 'table', 'DEPARTMENT_PATIENT_SUMMARY', 'column', 'MODIFY_USER_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改时间',
   'user', @CurrentUser, 'table', 'DEPARTMENT_PATIENT_SUMMARY', 'column', 'MODIFY_DATE_TIME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '版本号',
   'user', @CurrentUser, 'table', 'DEPARTMENT_PATIENT_SUMMARY', 'column', 'VERSION'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '标示是否有效',
   'user', @CurrentUser, 'table', 'DEPARTMENT_PATIENT_SUMMARY', 'column', 'ACTIVE'
go

/*==============================================================*/
/* Table: EXECUTIVE_ORDER_SUMMARY                               */
/*==============================================================*/
create table EXECUTIVE_ORDER_SUMMARY (
   REFID                varchar(32)          not null,
   NAME                 nvarchar(60)         null,
   EMPTY_SHOW_FLAG      char(1)              null,
   CATEGORY_NAME        nvarchar(60)         null,
   ORD                  int                  null,
   CREATE_USER_REFID    varchar(32)          null,
   CREATE_DATE_TIME     datetime             null,
   MODIFY_USER_REFID    varchar(32)          null,
   MODIFY_DATE_TIME     datetime             null,
   VERSION              int                  null,
   ACTIVE               bit                  null,
   constraint PK_EXECUTIVE_ORDER_SUMMARY primary key (REFID)
)
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '执行医嘱统计',
   'user', @CurrentUser, 'table', 'EXECUTIVE_ORDER_SUMMARY'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '统计名称',
   'user', @CurrentUser, 'table', 'EXECUTIVE_ORDER_SUMMARY', 'column', 'NAME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '空值时是否显示',
   'user', @CurrentUser, 'table', 'EXECUTIVE_ORDER_SUMMARY', 'column', 'EMPTY_SHOW_FLAG'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '分类Code',
   'user', @CurrentUser, 'table', 'EXECUTIVE_ORDER_SUMMARY', 'column', 'CATEGORY_NAME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '次序',
   'user', @CurrentUser, 'table', 'EXECUTIVE_ORDER_SUMMARY', 'column', 'ORD'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改人',
   'user', @CurrentUser, 'table', 'EXECUTIVE_ORDER_SUMMARY', 'column', 'CREATE_USER_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改时间',
   'user', @CurrentUser, 'table', 'EXECUTIVE_ORDER_SUMMARY', 'column', 'CREATE_DATE_TIME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改人',
   'user', @CurrentUser, 'table', 'EXECUTIVE_ORDER_SUMMARY', 'column', 'MODIFY_USER_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改时间',
   'user', @CurrentUser, 'table', 'EXECUTIVE_ORDER_SUMMARY', 'column', 'MODIFY_DATE_TIME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '版本号',
   'user', @CurrentUser, 'table', 'EXECUTIVE_ORDER_SUMMARY', 'column', 'VERSION'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '标示是否有效',
   'user', @CurrentUser, 'table', 'EXECUTIVE_ORDER_SUMMARY', 'column', 'ACTIVE'
go

/*==============================================================*/
/* Table: EXECUTIVE_ORDER_SUMMARY_ITEM                          */
/*==============================================================*/
create table EXECUTIVE_ORDER_SUMMARY_ITEM (
   REFID                varchar(32)          not null,
   EXE_ORDER_SUMMARY_REFID varchar(32)          null,
   ITEM_CODE            nvarchar(20)         null,
   R_ITEM_NAME          nvarchar(60)         null,
   FREQ_CODE            nvarchar(10)         null,
   CREATE_USER_REFID    varchar(32)          null,
   CREATE_DATE_TIME     datetime             null,
   MODIFY_USER_REFID    varchar(32)          null,
   MODIFY_DATE_TIME     datetime             null,
   VERSION              int                  null,
   ACTIVE               bit                  null,
   constraint PK_EXECUTIVE_ORDER_SUMMARY_ITE primary key (REFID)
)
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '执行医嘱统计医嘱条目映射',
   'user', @CurrentUser, 'table', 'EXECUTIVE_ORDER_SUMMARY_ITEM'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '执行医嘱统计Refid',
   'user', @CurrentUser, 'table', 'EXECUTIVE_ORDER_SUMMARY_ITEM', 'column', 'EXE_ORDER_SUMMARY_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '统计时间',
   'user', @CurrentUser, 'table', 'EXECUTIVE_ORDER_SUMMARY_ITEM', 'column', 'ITEM_CODE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '患者摘要',
   'user', @CurrentUser, 'table', 'EXECUTIVE_ORDER_SUMMARY_ITEM', 'column', 'R_ITEM_NAME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '执行频次要求',
   'user', @CurrentUser, 'table', 'EXECUTIVE_ORDER_SUMMARY_ITEM', 'column', 'FREQ_CODE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改人',
   'user', @CurrentUser, 'table', 'EXECUTIVE_ORDER_SUMMARY_ITEM', 'column', 'CREATE_USER_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改时间',
   'user', @CurrentUser, 'table', 'EXECUTIVE_ORDER_SUMMARY_ITEM', 'column', 'CREATE_DATE_TIME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改人',
   'user', @CurrentUser, 'table', 'EXECUTIVE_ORDER_SUMMARY_ITEM', 'column', 'MODIFY_USER_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改时间',
   'user', @CurrentUser, 'table', 'EXECUTIVE_ORDER_SUMMARY_ITEM', 'column', 'MODIFY_DATE_TIME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '版本号',
   'user', @CurrentUser, 'table', 'EXECUTIVE_ORDER_SUMMARY_ITEM', 'column', 'VERSION'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '标示是否有效',
   'user', @CurrentUser, 'table', 'EXECUTIVE_ORDER_SUMMARY_ITEM', 'column', 'ACTIVE'
go

/*==============================================================*/
/* View: V_EXECUTIVE_ORDER_SUMMARY                              */
/*==============================================================*/
create view V_EXECUTIVE_ORDER_SUMMARY as
SELECT     eos.REFID as SUMMARY_ID,  eos.NAME, EMPTY_SHOW_FLAG, CATEGORY_NAME, ORD, item.ITEM_CODE, R_ITEM_NAME, FREQ_CODE
FROM         EXECUTIVE_ORDER_SUMMARY eos join  EXECUTIVE_ORDER_SUMMARY_ITEM item on eos.refid = item.EXE_ORDER_SUMMARY_REFID
where eos.active=1 and item.active=1
go

alter table ALLERGY_DRUG
   add constraint FK_ALLERGY__REFERENCE_ALLERGY_ foreign key (CATEGORY_REFID)
      references ALLERGY_DRUG_CATEGORY (REFID)
go

alter table ALLERGY_DRUG_ITEM
   add constraint FK_ALLY_REF_ALLERGY_DRUG foreign key (ALLERGY_DRUG_REFID)
      references ALLERGY_DRUG (REFID)
go

alter table EXECUTIVE_ORDER_SUMMARY_ITEM
   add constraint FK_EXE_ORD_SUM_REF_ITEM foreign key (EXE_ORDER_SUMMARY_REFID)
      references EXECUTIVE_ORDER_SUMMARY (REFID)
go

