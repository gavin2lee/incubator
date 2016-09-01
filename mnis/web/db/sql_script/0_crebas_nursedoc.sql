/*==============================================================*/
/* DBMS name:      Microsoft SQL Server 2005                    */
/* Created on:     2014/10/15 15:43:58                          */
/*==============================================================*/


if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('DEPARTMENT_TABLE_TEMPLATE') and o.name = 'FK_DEPARTME_REF_TABLE_TE')
alter table DEPARTMENT_TABLE_TEMPLATE
   drop constraint FK_DEPARTME_REF_TABLE_TE
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('DEPARTMENT_TEMPALTE') and o.name = 'FK_DEPARTME_REFERENCE_TEMPLATE')
alter table DEPARTMENT_TEMPALTE
   drop constraint FK_DEPARTME_REFERENCE_TEMPLATE
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('DOC_ITEM_OPTION') and o.name = 'FK_DOC_ITEM_REFERENCE_NURSE_DO')
alter table DOC_ITEM_OPTION
   drop constraint FK_DOC_ITEM_REFERENCE_NURSE_DO
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('DOC_ITEM_OPTION') and o.name = 'FK_DOC_ITEM_REFERENCE_METADATA')
alter table DOC_ITEM_OPTION
   drop constraint FK_DOC_ITEM_REFERENCE_METADATA
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('DOC_TABLE_ITEM') and o.name = 'FK_DOC_TBL_REF_NRSE_DO')
alter table DOC_TABLE_ITEM
   drop constraint FK_DOC_TBL_REF_NRSE_DO
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('DOC_TABLE_ITEM') and o.name = 'FK_DOC_TBL_REF_TBL_TE')
alter table DOC_TABLE_ITEM
   drop constraint FK_DOC_TBL_REF_TBL_TE
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('DOC_TABLE_ITEM_OPTION') and o.name = 'FK_DOC_TABL_REF_DOC_TBL')
alter table DOC_TABLE_ITEM_OPTION
   drop constraint FK_DOC_TABL_REF_DOC_TBL
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('DOC_TABLE_PATTERN') and o.name = 'FK_DOC_TBL_REF_NURS_DO')
alter table DOC_TABLE_PATTERN
   drop constraint FK_DOC_TBL_REF_NURS_DO
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('DOC_TABLE_PATTERN') and o.name = 'FK_DOC_TBL_REF_TBLE_TE')
alter table DOC_TABLE_PATTERN
   drop constraint FK_DOC_TBL_REF_TBLE_TE
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('DOC_TABLE_PATTERN_ITEM') and o.name = 'FK_DOC_TABL_REE_DOC_TABL')
alter table DOC_TABLE_PATTERN_ITEM
   drop constraint FK_DOC_TABL_REE_DOC_TABL
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('DOC_TABLE_PATTERN_ITEM') and o.name = 'FK_DOC_TBL_REF_DOC_TABL')
alter table DOC_TABLE_PATTERN_ITEM
   drop constraint FK_DOC_TBL_REF_DOC_TABL
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('FIELD_SWITCH') and o.name = 'FK_FIELD_SW_REFERENCE_OPERATE')
alter table FIELD_SWITCH
   drop constraint FK_FIELD_SW_REFERENCE_OPERATE
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('METADATA') and o.name = 'FK_METADATA_REFERENCE_DATA_SOU')
alter table METADATA
   drop constraint FK_METADATA_REFERENCE_DATA_SOU
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('METADATA_OPTION') and o.name = 'FK_METADATA_REFERENCE_METADATA')
alter table METADATA_OPTION
   drop constraint FK_METADATA_REFERENCE_METADATA
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('NURSE_DOCUMENT') and o.name = 'FK_NURSE_DO_REF_TMPL')
alter table NURSE_DOCUMENT
   drop constraint FK_NURSE_DO_REF_TMPL
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('NURSE_DOC_DATA') and o.name = 'FK_NRSE_DO_REF_NRS_DO')
alter table NURSE_DOC_DATA
   drop constraint FK_NRSE_DO_REF_NRS_DO
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('NURSE_DOC_DATA_HIS') and o.name = 'FK_NRSE_DO_REF_NS_DO')
alter table NURSE_DOC_DATA_HIS
   drop constraint FK_NRSE_DO_REF_NS_DO
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('NURSE_DOC_ITEM') and o.name = 'FK_NRS_DO_REF_TEMPLATE')
alter table NURSE_DOC_ITEM
   drop constraint FK_NRS_DO_REF_TEMPLATE
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('NURSE_DOC_ITEM') and o.name = 'FK_NRS_DO_REF_NRS_DO')
alter table NURSE_DOC_ITEM
   drop constraint FK_NRS_DO_REF_NRS_DO
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('NURSE_DOC_OPERATE') and o.name = 'FK_NRS_DO_REF_NURSE_DO')
alter table NURSE_DOC_OPERATE
   drop constraint FK_NRS_DO_REF_NURSE_DO
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('NURSE_DOC_OPERATE') and o.name = 'FK_NURSE_DO_REFERENCE_OPERATE')
alter table NURSE_DOC_OPERATE
   drop constraint FK_NURSE_DO_REFERENCE_OPERATE
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('OPERATE_FIELD_SWITCH') and o.name = 'FK_OPERATE__REFERENCE_OPERATE')
alter table OPERATE_FIELD_SWITCH
   drop constraint FK_OPERATE__REFERENCE_OPERATE
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('OPERATE_FIELD_SWITCH') and o.name = 'FK_OPERATE__REFERENCE_FIELD_SW')
alter table OPERATE_FIELD_SWITCH
   drop constraint FK_OPERATE__REFERENCE_FIELD_SW
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('TABLE_TEMPLATE_ITEM') and o.name = 'FK_TBL_TE_REF_TBL_TE')
alter table TABLE_TEMPLATE_ITEM
   drop constraint FK_TBL_TE_REF_TBL_TE
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('TABLE_TEMPLATE_ITEM') and o.name = 'FK_TABLE_TE_REFERENCE_METADATA')
alter table TABLE_TEMPLATE_ITEM
   drop constraint FK_TABLE_TE_REFERENCE_METADATA
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('TABLE_TEMPLATE_OPERATE') and o.name = 'FK_TABLE_TE_REFERENCE_TABLE_TE')
alter table TABLE_TEMPLATE_OPERATE
   drop constraint FK_TABLE_TE_REFERENCE_TABLE_TE
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('TABLE_TEMPLATE_OPERATE') and o.name = 'FK_TABLE_TE_REFERENCE_OPERATE')
alter table TABLE_TEMPLATE_OPERATE
   drop constraint FK_TABLE_TE_REFERENCE_OPERATE
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('TABLE_TEMPLATE_PATTERN') and o.name = 'FK_TBL_TE_REF_TABLE_TE')
alter table TABLE_TEMPLATE_PATTERN
   drop constraint FK_TBL_TE_REF_TABLE_TE
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('TABLE_TEMPLATE_PATTERN_ITEM') and o.name = 'FK_TABLE_TE_REF_TBL_TE')
alter table TABLE_TEMPLATE_PATTERN_ITEM
   drop constraint FK_TABLE_TE_REF_TBL_TE
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('TABLE_TEMPLATE_PATTERN_ITEM') and o.name = 'FK_TBLE_TE_REF_TBL_TE')
alter table TABLE_TEMPLATE_PATTERN_ITEM
   drop constraint FK_TBLE_TE_REF_TBL_TE
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('TEMPLATE_ITEM') and o.name = 'FK_TEMPLATE_REFERENCE_TABLE_TE')
alter table TEMPLATE_ITEM
   drop constraint FK_TEMPLATE_REFERENCE_TABLE_TE
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('TEMPLATE_ITEM') and o.name = 'FK_TMPT_REF_TEMPLATE')
alter table TEMPLATE_ITEM
   drop constraint FK_TMPT_REF_TEMPLATE
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('TEMPLATE_ITEM') and o.name = 'FK_TEMPLATE_REFERENCE_METADATA')
alter table TEMPLATE_ITEM
   drop constraint FK_TEMPLATE_REFERENCE_METADATA
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('TEMPLATE_OPERATE') and o.name = 'FK_TEMPLATE_REF_TMPL')
alter table TEMPLATE_OPERATE
   drop constraint FK_TEMPLATE_REF_TMPL
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('TEMPLATE_OPERATE') and o.name = 'FK_TEMPLATE_REFERENCE_OPERATE')
alter table TEMPLATE_OPERATE
   drop constraint FK_TEMPLATE_REFERENCE_OPERATE
go

if exists (select 1
            from  sysobjects
           where  id = object_id('DATA_SOURCE')
            and   type = 'U')
   drop table DATA_SOURCE
go

if exists (select 1
            from  sysobjects
           where  id = object_id('DEPARTMENT_TABLE_TEMPLATE')
            and   type = 'U')
   drop table DEPARTMENT_TABLE_TEMPLATE
go

if exists (select 1
            from  sysobjects
           where  id = object_id('DEPARTMENT_TEMPALTE')
            and   type = 'U')
   drop table DEPARTMENT_TEMPALTE
go

if exists (select 1
            from  sysobjects
           where  id = object_id('DOC_ITEM_OPTION')
            and   type = 'U')
   drop table DOC_ITEM_OPTION
go

if exists (select 1
            from  sysobjects
           where  id = object_id('DOC_TABLE_ITEM')
            and   type = 'U')
   drop table DOC_TABLE_ITEM
go

if exists (select 1
            from  sysobjects
           where  id = object_id('DOC_TABLE_ITEM_OPTION')
            and   type = 'U')
   drop table DOC_TABLE_ITEM_OPTION
go

if exists (select 1
            from  sysobjects
           where  id = object_id('DOC_TABLE_PATTERN')
            and   type = 'U')
   drop table DOC_TABLE_PATTERN
go

if exists (select 1
            from  sysobjects
           where  id = object_id('DOC_TABLE_PATTERN_ITEM')
            and   type = 'U')
   drop table DOC_TABLE_PATTERN_ITEM
go

if exists (select 1
            from  sysobjects
           where  id = object_id('FIELD_SWITCH')
            and   type = 'U')
   drop table FIELD_SWITCH
go

if exists (select 1
            from  sysobjects
           where  id = object_id('METADATA')
            and   type = 'U')
   drop table METADATA
go

if exists (select 1
            from  sysobjects
           where  id = object_id('METADATA_OPTION')
            and   type = 'U')
   drop table METADATA_OPTION
go

if exists (select 1
            from  sysobjects
           where  id = object_id('NURSE_DOCUMENT')
            and   type = 'U')
   drop table NURSE_DOCUMENT
go

if exists (select 1
            from  sysobjects
           where  id = object_id('NURSE_DOC_DATA')
            and   type = 'U')
   drop table NURSE_DOC_DATA
go

if exists (select 1
            from  sysobjects
           where  id = object_id('NURSE_DOC_DATA_HIS')
            and   type = 'U')
   drop table NURSE_DOC_DATA_HIS
go

if exists (select 1
            from  sysobjects
           where  id = object_id('NURSE_DOC_ITEM')
            and   type = 'U')
   drop table NURSE_DOC_ITEM
go

if exists (select 1
            from  sysobjects
           where  id = object_id('NURSE_DOC_OPERATE')
            and   type = 'U')
   drop table NURSE_DOC_OPERATE
go

if exists (select 1
            from  sysobjects
           where  id = object_id('OPERATE')
            and   type = 'U')
   drop table OPERATE
go

if exists (select 1
            from  sysobjects
           where  id = object_id('OPERATE_FIELD_SWITCH')
            and   type = 'U')
   drop table OPERATE_FIELD_SWITCH
go

if exists (select 1
            from  sysobjects
           where  id = object_id('PAGER')
            and   type = 'U')
   drop table PAGER
go

if exists (select 1
            from  sysobjects
           where  id = object_id('TABLE_TEMPLATE')
            and   type = 'U')
   drop table TABLE_TEMPLATE
go

if exists (select 1
            from  sysobjects
           where  id = object_id('TABLE_TEMPLATE_ITEM')
            and   type = 'U')
   drop table TABLE_TEMPLATE_ITEM
go

if exists (select 1
            from  sysobjects
           where  id = object_id('TABLE_TEMPLATE_OPERATE')
            and   type = 'U')
   drop table TABLE_TEMPLATE_OPERATE
go

if exists (select 1
            from  sysobjects
           where  id = object_id('TABLE_TEMPLATE_PATTERN')
            and   type = 'U')
   drop table TABLE_TEMPLATE_PATTERN
go

if exists (select 1
            from  sysobjects
           where  id = object_id('TABLE_TEMPLATE_PATTERN_ITEM')
            and   type = 'U')
   drop table TABLE_TEMPLATE_PATTERN_ITEM
go

if exists (select 1
            from  sysobjects
           where  id = object_id('TEMPLATE')
            and   type = 'U')
   drop table TEMPLATE
go

if exists (select 1
            from  sysobjects
           where  id = object_id('TEMPLATE_ITEM')
            and   type = 'U')
   drop table TEMPLATE_ITEM
go

if exists (select 1
            from  sysobjects
           where  id = object_id('TEMPLATE_OPERATE')
            and   type = 'U')
   drop table TEMPLATE_OPERATE
go

if exists (select 1
            from  sysobjects
           where  id = object_id('VERIFY_POLICY')
            and   type = 'U')
   drop table VERIFY_POLICY
go

/*==============================================================*/
/* Table: DATA_SOURCE                                           */
/*==============================================================*/
create table DATA_SOURCE (
   REFID                varchar(32)          not null,
   DATA_SOURCE_TYPE     nvarchar(10)         null,
   SOURCE_NAME          nvarchar(60)         null,
   JSON_PATH            nvarchar(60)         null,
   PRAGRME              nvarchar(100)        null,
   REMARK               nvarchar(300)        null,
   CREATE_USER_REFID    varchar(32)          null,
   CREATE_DATE_TIME     datetime             null,
   MODIFY_USER_REFID    varchar(32)          null,
   MODIFY_DATE_TIME     datetime             null,
   VERSION              int                  null,
   ACTIVE               bit                  null,
   constraint PK_DATA_SOURCE primary key (REFID)
)
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '数据源',
   'user', @CurrentUser, 'table', 'DATA_SOURCE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '数据源类型。患者信息(PAT)、系统信息(SYS)、其他对象(BEN)、共享数据(SHR)、文书数据(NUS)',
   'user', @CurrentUser, 'table', 'DATA_SOURCE', 'column', 'DATA_SOURCE_TYPE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '数据源名称',
   'user', @CurrentUser, 'table', 'DATA_SOURCE', 'column', 'SOURCE_NAME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '数据源数据的JSON节点',
   'user', @CurrentUser, 'table', 'DATA_SOURCE', 'column', 'JSON_PATH'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '程序',
   'user', @CurrentUser, 'table', 'DATA_SOURCE', 'column', 'PRAGRME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '备注',
   'user', @CurrentUser, 'table', 'DATA_SOURCE', 'column', 'REMARK'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改人',
   'user', @CurrentUser, 'table', 'DATA_SOURCE', 'column', 'CREATE_USER_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改时间',
   'user', @CurrentUser, 'table', 'DATA_SOURCE', 'column', 'CREATE_DATE_TIME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改人',
   'user', @CurrentUser, 'table', 'DATA_SOURCE', 'column', 'MODIFY_USER_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改时间',
   'user', @CurrentUser, 'table', 'DATA_SOURCE', 'column', 'MODIFY_DATE_TIME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '版本号',
   'user', @CurrentUser, 'table', 'DATA_SOURCE', 'column', 'VERSION'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '标示是否有效',
   'user', @CurrentUser, 'table', 'DATA_SOURCE', 'column', 'ACTIVE'
go

/*==============================================================*/
/* Table: DEPARTMENT_TABLE_TEMPLATE                             */
/*==============================================================*/
create table DEPARTMENT_TABLE_TEMPLATE (
   REFID                varchar(32)          not null,
   DEPT_REFID           varchar(32)          null,
   FORM_TITLE           nvarchar(60)         null,
   DOC_TEMPLATE_TYPE    nvarchar(10)         null,
   TABLE_TEMPLATE_REFID varchar(32)          null,
   ORD                  integer              null,
   CREATE_USER_REFID    varchar(32)          null,
   CREATE_DATE_TIME     datetime             null,
   MODIFY_USER_REFID    varchar(32)          null,
   MODIFY_DATE_TIME     datetime             null,
   VERSION              int                  null,
   ACTIVE               bit                  null,
   constraint PK_DEPARTMENT_TABLE_TEMPLATE primary key (REFID)
)
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '科室表格模板',
   'user', @CurrentUser, 'table', 'DEPARTMENT_TABLE_TEMPLATE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '科室Id',
   'user', @CurrentUser, 'table', 'DEPARTMENT_TABLE_TEMPLATE', 'column', 'DEPT_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '表单名称',
   'user', @CurrentUser, 'table', 'DEPARTMENT_TABLE_TEMPLATE', 'column', 'FORM_TITLE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '关联文书模板类型。快速血糖单、心电监护单、护理记录等',
   'user', @CurrentUser, 'table', 'DEPARTMENT_TABLE_TEMPLATE', 'column', 'DOC_TEMPLATE_TYPE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '表格模板ID模板',
   'user', @CurrentUser, 'table', 'DEPARTMENT_TABLE_TEMPLATE', 'column', 'TABLE_TEMPLATE_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '次序。同一上级中次序',
   'user', @CurrentUser, 'table', 'DEPARTMENT_TABLE_TEMPLATE', 'column', 'ORD'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改人',
   'user', @CurrentUser, 'table', 'DEPARTMENT_TABLE_TEMPLATE', 'column', 'CREATE_USER_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改时间',
   'user', @CurrentUser, 'table', 'DEPARTMENT_TABLE_TEMPLATE', 'column', 'CREATE_DATE_TIME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改人',
   'user', @CurrentUser, 'table', 'DEPARTMENT_TABLE_TEMPLATE', 'column', 'MODIFY_USER_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改时间',
   'user', @CurrentUser, 'table', 'DEPARTMENT_TABLE_TEMPLATE', 'column', 'MODIFY_DATE_TIME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '版本号',
   'user', @CurrentUser, 'table', 'DEPARTMENT_TABLE_TEMPLATE', 'column', 'VERSION'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '标示是否有效',
   'user', @CurrentUser, 'table', 'DEPARTMENT_TABLE_TEMPLATE', 'column', 'ACTIVE'
go

/*==============================================================*/
/* Table: DEPARTMENT_TEMPALTE                                   */
/*==============================================================*/
create table DEPARTMENT_TEMPALTE (
   REFID                varchar(32)          not null,
   DEPT_REFID           varchar(32)          null,
   NODE_TYPE            nvarchar(10)         null,
   NODE_NAME            nvarchar(60)         null,
   NODE_PARENT_REFID    varchar(32)          null,
   ORD                  integer              null,
   TEMPLATE_TYPE        nvarchar(10)         null,
   NUMBER_TYPE          nvarchar(10)         null,
   TEMPLATE_REFID       varchar(32)          null,
   URL                  nvarchar(100)        null,
   CREATE_USER_REFID    varchar(32)          null,
   CREATE_DATE_TIME     datetime             null,
   MODIFY_USER_REFID    varchar(32)          null,
   MODIFY_DATE_TIME     datetime             null,
   VERSION              int                  null,
   ACTIVE               bit                  null,
   constraint PK_DEPARTMENT_TEMPALTE primary key (REFID)
)
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '科室模板',
   'user', @CurrentUser, 'table', 'DEPARTMENT_TEMPALTE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '科室Id',
   'user', @CurrentUser, 'table', 'DEPARTMENT_TEMPALTE', 'column', 'DEPT_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '节点类型。目录、模板',
   'user', @CurrentUser, 'table', 'DEPARTMENT_TEMPALTE', 'column', 'NODE_TYPE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '节点名称',
   'user', @CurrentUser, 'table', 'DEPARTMENT_TEMPALTE', 'column', 'NODE_NAME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '上级节点',
   'user', @CurrentUser, 'table', 'DEPARTMENT_TEMPALTE', 'column', 'NODE_PARENT_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '次序。同一上级中次序',
   'user', @CurrentUser, 'table', 'DEPARTMENT_TEMPALTE', 'column', 'ORD'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '模板类型。评估、住院评估、护理记录等',
   'user', @CurrentUser, 'table', 'DEPARTMENT_TEMPALTE', 'column', 'TEMPLATE_TYPE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '数量类型。一个(ONCE)、多个(MUL)、时间点多个(TIM_MUL)',
   'user', @CurrentUser, 'table', 'DEPARTMENT_TEMPALTE', 'column', 'NUMBER_TYPE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '文书模板',
   'user', @CurrentUser, 'table', 'DEPARTMENT_TEMPALTE', 'column', 'TEMPLATE_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '文书入口URL',
   'user', @CurrentUser, 'table', 'DEPARTMENT_TEMPALTE', 'column', 'URL'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改人',
   'user', @CurrentUser, 'table', 'DEPARTMENT_TEMPALTE', 'column', 'CREATE_USER_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改时间',
   'user', @CurrentUser, 'table', 'DEPARTMENT_TEMPALTE', 'column', 'CREATE_DATE_TIME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改人',
   'user', @CurrentUser, 'table', 'DEPARTMENT_TEMPALTE', 'column', 'MODIFY_USER_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改时间',
   'user', @CurrentUser, 'table', 'DEPARTMENT_TEMPALTE', 'column', 'MODIFY_DATE_TIME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '版本号',
   'user', @CurrentUser, 'table', 'DEPARTMENT_TEMPALTE', 'column', 'VERSION'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '标示是否有效',
   'user', @CurrentUser, 'table', 'DEPARTMENT_TEMPALTE', 'column', 'ACTIVE'
go

/*==============================================================*/
/* Table: DOC_ITEM_OPTION                                       */
/*==============================================================*/
create table DOC_ITEM_OPTION (
   REFID                varchar(32)          not null,
   DOC_ITEM_REFID       varchar(32)          null,
   R_NURSE_DOC_REFID    varchar(32)          not null,
   METADATA_OPTION_REFID varchar(32)          null,
   METADATA_REFID       varchar(32)          null,
   OPTION_NAME          nvarchar(60)         null,
   OPTION_CODE          nvarchar(20)         null,
   SCORE                integer              null,
   OPTION_TYPE          nvarchar(10)         null,
   ORD                  integer              null,
   CREATE_USER_REFID    varchar(32)          null,
   CREATE_DATE_TIME     datetime             null,
   MODIFY_USER_REFID    varchar(32)          null,
   MODIFY_DATE_TIME     datetime             null,
   VERSION              int                  null,
   ACTIVE               bit                  null,
   constraint PK_DOC_ITEM_OPTION primary key (REFID)
)
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '护理文书选项元数据选项',
   'user', @CurrentUser, 'table', 'DOC_ITEM_OPTION'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '护理文书选项Id',
   'user', @CurrentUser, 'table', 'DOC_ITEM_OPTION', 'column', 'DOC_ITEM_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '护理文书Id',
   'user', @CurrentUser, 'table', 'DOC_ITEM_OPTION', 'column', 'R_NURSE_DOC_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '元数据选项Id',
   'user', @CurrentUser, 'table', 'DOC_ITEM_OPTION', 'column', 'METADATA_OPTION_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '元数据Id',
   'user', @CurrentUser, 'table', 'DOC_ITEM_OPTION', 'column', 'METADATA_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '选项名称',
   'user', @CurrentUser, 'table', 'DOC_ITEM_OPTION', 'column', 'OPTION_NAME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '选项代码',
   'user', @CurrentUser, 'table', 'DOC_ITEM_OPTION', 'column', 'OPTION_CODE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '选项评分。对于评估项，表示选项所占分数。对于评估结果，表示选项最低分数',
   'user', @CurrentUser, 'table', 'DOC_ITEM_OPTION', 'column', 'SCORE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '选项类型。仅仅选项(OPT)，选项带输入(INPT)',
   'user', @CurrentUser, 'table', 'DOC_ITEM_OPTION', 'column', 'OPTION_TYPE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '次序。选项次序',
   'user', @CurrentUser, 'table', 'DOC_ITEM_OPTION', 'column', 'ORD'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改人',
   'user', @CurrentUser, 'table', 'DOC_ITEM_OPTION', 'column', 'CREATE_USER_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改时间',
   'user', @CurrentUser, 'table', 'DOC_ITEM_OPTION', 'column', 'CREATE_DATE_TIME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改人',
   'user', @CurrentUser, 'table', 'DOC_ITEM_OPTION', 'column', 'MODIFY_USER_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改时间',
   'user', @CurrentUser, 'table', 'DOC_ITEM_OPTION', 'column', 'MODIFY_DATE_TIME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '版本号',
   'user', @CurrentUser, 'table', 'DOC_ITEM_OPTION', 'column', 'VERSION'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '标示是否有效',
   'user', @CurrentUser, 'table', 'DOC_ITEM_OPTION', 'column', 'ACTIVE'
go

/*==============================================================*/
/* Table: DOC_TABLE_ITEM                                        */
/*==============================================================*/
create table DOC_TABLE_ITEM (
   REFID                varchar(32)          not null,
   DOC_ITEM_REFID       varchar(32)          not null,
   R_NURSE_DOC_REFID    varchar(32)          not null,
   TABLE_ITEM_REFID     varchar(32)          null,
   TABLE_REFID          varchar(32)          null,
   METADATA_REFID       varchar(32)          null,
   TITLE                nvarchar(60)         null,
   KEY_FLAG             char(1)              null,
   SHOW_FLAG            char(1)              null,
   COPY_FLAG            char(1)              null,
   ORD                  integer              null,
   ALTERNATE_FLAG       char(1)              null,
   EMPTY_FLAG           char(1)              null,
   WIDTH                integer              null,
   HEIGHT               integer              null,
   READONLY_FLAG        char(1)              null,
   TIME_KEY_FLAG        char(1)              null,
   MIN_VALUE            numeric(10,3)        null,
   MAX_VALUE            numeric(10,3)        null,
   NORMAL_MIN_VALUE     numeric(10,2)        null,
   NORMAL_MAX_VALUE     numeric(10,2)        null,
   VERIFY_POLICY_CODE   nvarchar(100)        null,
   SEARCH_FLAG          char(1)              null,
   TEMPLATE_MENU_FLAG   char(1)              null,
   SAVE_FLAG            char(1)              null,
   EDIT_FLAG            char(1)              null,
   MUST_INPUT_FLAG      char(1)              null,
   NDA_FLAG             char(1)              null,
   METADATA_NAME        nvarchar(60)         null,
   METADATA_SIMPLE_NAME nvarchar(60)         null,
   METADATA_CODE        nvarchar(20)         null,
   DATA_SOURCE_REFID    varchar(32)          null,
   R_DATA_SOURCE_TYPE   nvarchar(10)         null,
   DATA_SOURCE_PATH     nvarchar(60)         null,
   INPUT_TYPE           nvarchar(10)         null,
   DATA_TYPE            nvarchar(10)         null,
   SCOPE_TYPE           nvarchar(10)         null,
   EVALUATE_TYPE        nvarchar(10)         null,
   EVALUATE_CODE        nvarchar(30)         null,
   EVALUATE_SCORE       integer              null,
   UNIT                 nvarchar(30)         null,
   PRECISIONS           integer              null,
   CREATE_USER_REFID    varchar(32)          null,
   CREATE_DATE_TIME     datetime             null,
   MODIFY_USER_REFID    varchar(32)          null,
   MODIFY_DATE_TIME     datetime             null,
   VERSION              int                  null,
   ACTIVE               bit                  null,
   constraint PK_DOC_TABLE_ITEM primary key (REFID)
)
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '护理文书项表格数据项',
   'user', @CurrentUser, 'table', 'DOC_TABLE_ITEM'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '文书选项Id',
   'user', @CurrentUser, 'table', 'DOC_TABLE_ITEM', 'column', 'DOC_ITEM_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '护理文书Id',
   'user', @CurrentUser, 'table', 'DOC_TABLE_ITEM', 'column', 'R_NURSE_DOC_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '表格项Id',
   'user', @CurrentUser, 'table', 'DOC_TABLE_ITEM', 'column', 'TABLE_ITEM_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '表格ID',
   'user', @CurrentUser, 'table', 'DOC_TABLE_ITEM', 'column', 'TABLE_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '数据项Id',
   'user', @CurrentUser, 'table', 'DOC_TABLE_ITEM', 'column', 'METADATA_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '标题路径。下级|上级|上级的方式组织。空白项时可以为空',
   'user', @CurrentUser, 'table', 'DOC_TABLE_ITEM', 'column', 'TITLE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '行主键标识。主键(Y),非主键(N).标识行数据。表格存在多个主键标识，则多个主键标识依次连接，数据依次连接。如果表格没有主键标识，行数据默认以自动编号作为规则。',
   'user', @CurrentUser, 'table', 'DOC_TABLE_ITEM', 'column', 'KEY_FLAG'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '显示标识。显示(Y)、隐藏(N)。主要用于需要保存自动值，又不需要显示的数据。比如系统当前时间',
   'user', @CurrentUser, 'table', 'DOC_TABLE_ITEM', 'column', 'SHOW_FLAG'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '允许转抄标志。可以转抄(Y),不允许转抄(N)用于生产查询条件。',
   'user', @CurrentUser, 'table', 'DOC_TABLE_ITEM', 'column', 'COPY_FLAG'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '次序',
   'user', @CurrentUser, 'table', 'DOC_TABLE_ITEM', 'column', 'ORD'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '备选标志。备选(Y),非备选(N)。备选项可以在文书中选择',
   'user', @CurrentUser, 'table', 'DOC_TABLE_ITEM', 'column', 'ALTERNATE_FLAG'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '空白项标志。空白项(Y),非空白项（N)。空白项将会被备选项替代',
   'user', @CurrentUser, 'table', 'DOC_TABLE_ITEM', 'column', 'EMPTY_FLAG'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '宽度。单位px。初始值从METADATA复制',
   'user', @CurrentUser, 'table', 'DOC_TABLE_ITEM', 'column', 'WIDTH'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '高度。初始值从METADATA复制',
   'user', @CurrentUser, 'table', 'DOC_TABLE_ITEM', 'column', 'HEIGHT'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '是否只读。只读(Y),可写(N)。初始值从METADATA复制',
   'user', @CurrentUser, 'table', 'DOC_TABLE_ITEM', 'column', 'READONLY_FLAG'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '时间主键标识。主键(Y),非主键(N)。初始值从METADATA复制',
   'user', @CurrentUser, 'table', 'DOC_TABLE_ITEM', 'column', 'TIME_KEY_FLAG'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最小值。用于检验结果标识正常值范围。对于一些特殊的校验方式，也可用于输入值校验。初始值从METADATA复制',
   'user', @CurrentUser, 'table', 'DOC_TABLE_ITEM', 'column', 'MIN_VALUE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最大值。用于检验结果标识正常值范围。对于一些特殊的校验方式，也可用于输入值校验。',
   'user', @CurrentUser, 'table', 'DOC_TABLE_ITEM', 'column', 'MAX_VALUE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '正常范围最小值。用于检验结果标识正常值范围。',
   'user', @CurrentUser, 'table', 'DOC_TABLE_ITEM', 'column', 'NORMAL_MIN_VALUE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '正常范围最大值。用于检验结果标识正常值范围。',
   'user', @CurrentUser, 'table', 'DOC_TABLE_ITEM', 'column', 'NORMAL_MAX_VALUE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '校验规则。除依据数据类型自动校验外的特殊校验规则。不同规则之间以逗号分隔',
   'user', @CurrentUser, 'table', 'DOC_TABLE_ITEM', 'column', 'VERIFY_POLICY_CODE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '查询条件标志。查询条件(Y),非查询条件(N)。用于生产查询条件。查询条件视字段类型处理',
   'user', @CurrentUser, 'table', 'DOC_TABLE_ITEM', 'column', 'SEARCH_FLAG'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '模板身份标志。身份标识(Y)，非身份标识(N)。用于多模板数据时，菜单名称',
   'user', @CurrentUser, 'table', 'DOC_TABLE_ITEM', 'column', 'TEMPLATE_MENU_FLAG'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '允许护理文书保存标志。允许保存(Y),不允许保存(N)。用于控制是否存储到文书数据中',
   'user', @CurrentUser, 'table', 'DOC_TABLE_ITEM', 'column', 'SAVE_FLAG'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '允许编辑标志。允许编辑(Y),只读(N)。用于控制是否允许用户改动',
   'user', @CurrentUser, 'table', 'DOC_TABLE_ITEM', 'column', 'EDIT_FLAG'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '必填项标志。必填字段(Y),非必填字段(N)用于输入控制校验。',
   'user', @CurrentUser, 'table', 'DOC_TABLE_ITEM', 'column', 'MUST_INPUT_FLAG'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   'NDA端标识。NDA端显示(Y),NDA端不显示(N)',
   'user', @CurrentUser, 'table', 'DOC_TABLE_ITEM', 'column', 'NDA_FLAG'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '元数据名称',
   'user', @CurrentUser, 'table', 'DOC_TABLE_ITEM', 'column', 'METADATA_NAME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '元数据名称简称',
   'user', @CurrentUser, 'table', 'DOC_TABLE_ITEM', 'column', 'METADATA_SIMPLE_NAME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '元数据代码',
   'user', @CurrentUser, 'table', 'DOC_TABLE_ITEM', 'column', 'METADATA_CODE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '数据源Id',
   'user', @CurrentUser, 'table', 'DOC_TABLE_ITEM', 'column', 'DATA_SOURCE_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '数据源类型。患者信息(PAT)、系统信息(SYS)、其他对象(BEN)、共享数据(SHR)、文书数据(NUS)',
   'user', @CurrentUser, 'table', 'DOC_TABLE_ITEM', 'column', 'R_DATA_SOURCE_TYPE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '数据源JSON路径',
   'user', @CurrentUser, 'table', 'DOC_TABLE_ITEM', 'column', 'DATA_SOURCE_PATH'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '输入类型。包括键入(INPT)、字串(STR)、数值(NUM)、邮箱(MAL)、体温(TP)、脉搏(PLS)、开关(SWT)、登录人(LGN)、护士长(NUS_MNG)、系统时间(SYS_TIM)、日期(DAT)、时间(TIM)、单选(SEL)、多选(MSEL)、单选带输入(SEL_INPT)、多选带输入(MSEL_INPT)、操作(OPT)',
   'user', @CurrentUser, 'table', 'DOC_TABLE_ITEM', 'column', 'INPUT_TYPE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '数据类型。包括：数值（INPT)、字串(STR)、日期(DAT)、日期时间(DAT_TIM)、温度（TP)、脉搏(PLS)等',
   'user', @CurrentUser, 'table', 'DOC_TABLE_ITEM', 'column', 'DATA_TYPE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '效用范围。包括住院共享(SHR)、文书私有(PRV)',
   'user', @CurrentUser, 'table', 'DOC_TABLE_ITEM', 'column', 'SCOPE_TYPE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '评估类型。包括非评估(NOT)，评估项(ITM)，评估结果(RSLT)',
   'user', @CurrentUser, 'table', 'DOC_TABLE_ITEM', 'column', 'EVALUATE_TYPE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '评估代码。评估项时，填充评估结果的元数据代码',
   'user', @CurrentUser, 'table', 'DOC_TABLE_ITEM', 'column', 'EVALUATE_CODE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '评估分数。用于评估项，且没有数据元选项时。比如开关项',
   'user', @CurrentUser, 'table', 'DOC_TABLE_ITEM', 'column', 'EVALUATE_SCORE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '单位名称',
   'user', @CurrentUser, 'table', 'DOC_TABLE_ITEM', 'column', 'UNIT'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '小数位数',
   'user', @CurrentUser, 'table', 'DOC_TABLE_ITEM', 'column', 'PRECISIONS'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改人',
   'user', @CurrentUser, 'table', 'DOC_TABLE_ITEM', 'column', 'CREATE_USER_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改时间',
   'user', @CurrentUser, 'table', 'DOC_TABLE_ITEM', 'column', 'CREATE_DATE_TIME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改人',
   'user', @CurrentUser, 'table', 'DOC_TABLE_ITEM', 'column', 'MODIFY_USER_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改时间',
   'user', @CurrentUser, 'table', 'DOC_TABLE_ITEM', 'column', 'MODIFY_DATE_TIME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '版本号',
   'user', @CurrentUser, 'table', 'DOC_TABLE_ITEM', 'column', 'VERSION'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '标示是否有效',
   'user', @CurrentUser, 'table', 'DOC_TABLE_ITEM', 'column', 'ACTIVE'
go

/*==============================================================*/
/* Table: DOC_TABLE_ITEM_OPTION                                 */
/*==============================================================*/
create table DOC_TABLE_ITEM_OPTION (
   REFID                varchar(32)          not null,
   DOC_TABLE_ITEM_REFID varchar(32)          null,
   R_NURSE_DOC_REFID    varchar(32)          not null,
   METADATA_OPTION_REFID varchar(32)          null,
   METADATA_REFID       varchar(32)          null,
   OPTION_NAME          nvarchar(60)         null,
   OPTION_CODE          nvarchar(20)         null,
   SCORE                integer              null,
   OPTION_TYPE          nvarchar(10)         null,
   ORD                  integer              null,
   CREATE_USER_REFID    varchar(32)          null,
   CREATE_DATE_TIME     datetime             null,
   MODIFY_USER_REFID    varchar(32)          null,
   MODIFY_DATE_TIME     datetime             null,
   VERSION              int                  null,
   ACTIVE               bit                  null,
   constraint PK_DOC_TABLE_ITEM_OPTION primary key (REFID)
)
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '护理文书表格选项元数据选项',
   'user', @CurrentUser, 'table', 'DOC_TABLE_ITEM_OPTION'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '护理文书选项Id',
   'user', @CurrentUser, 'table', 'DOC_TABLE_ITEM_OPTION', 'column', 'DOC_TABLE_ITEM_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '护理文书Id',
   'user', @CurrentUser, 'table', 'DOC_TABLE_ITEM_OPTION', 'column', 'R_NURSE_DOC_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '元数据选项Id',
   'user', @CurrentUser, 'table', 'DOC_TABLE_ITEM_OPTION', 'column', 'METADATA_OPTION_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '元数据Id',
   'user', @CurrentUser, 'table', 'DOC_TABLE_ITEM_OPTION', 'column', 'METADATA_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '选项名称',
   'user', @CurrentUser, 'table', 'DOC_TABLE_ITEM_OPTION', 'column', 'OPTION_NAME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '选项代码',
   'user', @CurrentUser, 'table', 'DOC_TABLE_ITEM_OPTION', 'column', 'OPTION_CODE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '选项评分。对于评估项，表示选项所占分数。对于评估结果，表示选项最低分数',
   'user', @CurrentUser, 'table', 'DOC_TABLE_ITEM_OPTION', 'column', 'SCORE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '选项类型。仅仅选项(OPT)，选项带输入(INPT)',
   'user', @CurrentUser, 'table', 'DOC_TABLE_ITEM_OPTION', 'column', 'OPTION_TYPE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '次序。选项次序',
   'user', @CurrentUser, 'table', 'DOC_TABLE_ITEM_OPTION', 'column', 'ORD'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改人',
   'user', @CurrentUser, 'table', 'DOC_TABLE_ITEM_OPTION', 'column', 'CREATE_USER_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改时间',
   'user', @CurrentUser, 'table', 'DOC_TABLE_ITEM_OPTION', 'column', 'CREATE_DATE_TIME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改人',
   'user', @CurrentUser, 'table', 'DOC_TABLE_ITEM_OPTION', 'column', 'MODIFY_USER_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改时间',
   'user', @CurrentUser, 'table', 'DOC_TABLE_ITEM_OPTION', 'column', 'MODIFY_DATE_TIME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '版本号',
   'user', @CurrentUser, 'table', 'DOC_TABLE_ITEM_OPTION', 'column', 'VERSION'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '标示是否有效',
   'user', @CurrentUser, 'table', 'DOC_TABLE_ITEM_OPTION', 'column', 'ACTIVE'
go

/*==============================================================*/
/* Table: DOC_TABLE_PATTERN                                     */
/*==============================================================*/
create table DOC_TABLE_PATTERN (
   REFID                varchar(32)          not null,
   NURSE_DOC_REFID      varchar(32)          not null,
   TABLE_PATTERN_REFID  varchar(32)          null,
   TABLE_REFID          varchar(32)          not null,
   PATTERN_NAME         nvarchar(60)         null,
   ORD                  integer              null,
   CREATE_USER_REFID    varchar(32)          null,
   CREATE_DATE_TIME     datetime             null,
   MODIFY_USER_REFID    varchar(32)          null,
   MODIFY_DATE_TIME     datetime             null,
   VERSION              int                  null,
   ACTIVE               bit                  null,
   constraint PK_DOC_TABLE_PATTERN primary key (REFID)
)
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '护理文书表格备选模式',
   'user', @CurrentUser, 'table', 'DOC_TABLE_PATTERN'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '护理文书Id',
   'user', @CurrentUser, 'table', 'DOC_TABLE_PATTERN', 'column', 'NURSE_DOC_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '表格选项模式Id',
   'user', @CurrentUser, 'table', 'DOC_TABLE_PATTERN', 'column', 'TABLE_PATTERN_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '次序',
   'user', @CurrentUser, 'table', 'DOC_TABLE_PATTERN', 'column', 'ORD'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改人',
   'user', @CurrentUser, 'table', 'DOC_TABLE_PATTERN', 'column', 'CREATE_USER_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改时间',
   'user', @CurrentUser, 'table', 'DOC_TABLE_PATTERN', 'column', 'CREATE_DATE_TIME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改人',
   'user', @CurrentUser, 'table', 'DOC_TABLE_PATTERN', 'column', 'MODIFY_USER_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改时间',
   'user', @CurrentUser, 'table', 'DOC_TABLE_PATTERN', 'column', 'MODIFY_DATE_TIME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '版本号',
   'user', @CurrentUser, 'table', 'DOC_TABLE_PATTERN', 'column', 'VERSION'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '标示是否有效',
   'user', @CurrentUser, 'table', 'DOC_TABLE_PATTERN', 'column', 'ACTIVE'
go

/*==============================================================*/
/* Table: DOC_TABLE_PATTERN_ITEM                                */
/*==============================================================*/
create table DOC_TABLE_PATTERN_ITEM (
   REFID                varchar(32)          not null,
   DOC_TABLE_ITEM_REFID varchar(32)          not null,
   R_NURSE_DOC_REFID    varchar(32)          not null,
   DOC_TABLE_PATTERN_REFID varchar(32)          null,
   R_METADATA_NAME      nvarchar(60)         null,
   ORD                  integer              null,
   CREATE_USER_REFID    varchar(32)          null,
   CREATE_DATE_TIME     datetime             null,
   MODIFY_USER_REFID    varchar(32)          null,
   MODIFY_DATE_TIME     datetime             null,
   VERSION              int                  null,
   ACTIVE               bit                  null,
   constraint PK_DOC_TABLE_PATTERN_ITEM primary key (REFID)
)
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '护理文书表格备选模式备选项',
   'user', @CurrentUser, 'table', 'DOC_TABLE_PATTERN_ITEM'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '文书表格项Id',
   'user', @CurrentUser, 'table', 'DOC_TABLE_PATTERN_ITEM', 'column', 'DOC_TABLE_ITEM_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '护理文书Id',
   'user', @CurrentUser, 'table', 'DOC_TABLE_PATTERN_ITEM', 'column', 'R_NURSE_DOC_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '文书表格模式Id',
   'user', @CurrentUser, 'table', 'DOC_TABLE_PATTERN_ITEM', 'column', 'DOC_TABLE_PATTERN_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '元数据名称',
   'user', @CurrentUser, 'table', 'DOC_TABLE_PATTERN_ITEM', 'column', 'R_METADATA_NAME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '备选次序',
   'user', @CurrentUser, 'table', 'DOC_TABLE_PATTERN_ITEM', 'column', 'ORD'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改人',
   'user', @CurrentUser, 'table', 'DOC_TABLE_PATTERN_ITEM', 'column', 'CREATE_USER_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改时间',
   'user', @CurrentUser, 'table', 'DOC_TABLE_PATTERN_ITEM', 'column', 'CREATE_DATE_TIME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改人',
   'user', @CurrentUser, 'table', 'DOC_TABLE_PATTERN_ITEM', 'column', 'MODIFY_USER_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改时间',
   'user', @CurrentUser, 'table', 'DOC_TABLE_PATTERN_ITEM', 'column', 'MODIFY_DATE_TIME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '版本号',
   'user', @CurrentUser, 'table', 'DOC_TABLE_PATTERN_ITEM', 'column', 'VERSION'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '标示是否有效',
   'user', @CurrentUser, 'table', 'DOC_TABLE_PATTERN_ITEM', 'column', 'ACTIVE'
go

/*==============================================================*/
/* Table: FIELD_SWITCH                                          */
/*==============================================================*/
create table FIELD_SWITCH (
   REFID                varchar(32)          not null,
   FROM_FIELD_CODE      nvarchar(20)         null,
   FROM_FIELD_NAME      nvarchar(60)         null,
   TO_TYPE              nvarchar(20)         null,
   TO_FIELD_CODE        nvarchar(20)         null,
   TO_FIELD_NAME        nvarchar(60)         null,
   SWITCH_PROGRAME_REFID varchar(32)          null,
   REMARK               nvarchar(300)        null,
   CREATE_USER_REFID    varchar(32)          null,
   CREATE_DATE_TIME     datetime             null,
   MODIFY_USER_REFID    varchar(32)          null,
   MODIFY_DATE_TIME     datetime             null,
   VERSION              int                  null,
   ACTIVE               bit                  null,
   constraint PK_FIELD_SWITCH primary key (REFID)
)
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '字段转换映射',
   'user', @CurrentUser, 'table', 'FIELD_SWITCH'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '源字段代码',
   'user', @CurrentUser, 'table', 'FIELD_SWITCH', 'column', 'FROM_FIELD_CODE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '源字段名称',
   'user', @CurrentUser, 'table', 'FIELD_SWITCH', 'column', 'FROM_FIELD_NAME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '目的类型。包括文书、其他',
   'user', @CurrentUser, 'table', 'FIELD_SWITCH', 'column', 'TO_TYPE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '目的字段代码。对于文书是METADATA_CODE，对于数据库，是字段CODE，对于Map，是KEY',
   'user', @CurrentUser, 'table', 'FIELD_SWITCH', 'column', 'TO_FIELD_CODE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '目的字段名称',
   'user', @CurrentUser, 'table', 'FIELD_SWITCH', 'column', 'TO_FIELD_NAME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '转换程序。无转换程序为直接复制，否则是SWT类型的Operate',
   'user', @CurrentUser, 'table', 'FIELD_SWITCH', 'column', 'SWITCH_PROGRAME_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '备注',
   'user', @CurrentUser, 'table', 'FIELD_SWITCH', 'column', 'REMARK'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改人',
   'user', @CurrentUser, 'table', 'FIELD_SWITCH', 'column', 'CREATE_USER_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改时间',
   'user', @CurrentUser, 'table', 'FIELD_SWITCH', 'column', 'CREATE_DATE_TIME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改人',
   'user', @CurrentUser, 'table', 'FIELD_SWITCH', 'column', 'MODIFY_USER_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改时间',
   'user', @CurrentUser, 'table', 'FIELD_SWITCH', 'column', 'MODIFY_DATE_TIME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '版本号',
   'user', @CurrentUser, 'table', 'FIELD_SWITCH', 'column', 'VERSION'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '标示是否有效',
   'user', @CurrentUser, 'table', 'FIELD_SWITCH', 'column', 'ACTIVE'
go

/*==============================================================*/
/* Table: METADATA                                              */
/*==============================================================*/
create table METADATA (
   REFID                varchar(32)          not null,
   METADATA_NAME        nvarchar(60)         null,
   METADATA_SIMPLE_NAME nvarchar(60)         null,
   METADATA_NAME_PINYIN nvarchar(100)        null,
   METADATA_CODE        nvarchar(20)         null,
   DATA_SOURCE_REFID    varchar(32)          null,
   R_DATA_SOURCE_TYPE   nvarchar(10)         null,
   DATA_SOURCE_PATH     nvarchar(60)         null,
   INPUT_TYPE           nvarchar(10)         null,
   DATA_TYPE            nvarchar(10)         null,
   SCOPE_TYPE           nvarchar(10)         null,
   READONLY_FLAG        char(1)              null,
   TIME_KEY_FLAG        char(1)              null,
   MIN_VALUE            numeric(10,2)        null,
   MAX_VALUE            numeric(10,2)        null,
   NORMAL_MIN_VALUE     numeric(10,2)        null,
   NORMAL_MAX_VALUE     numeric(10,2)        null,
   VERIFY_POLICY_CODE   nvarchar(100)        null,
   EVALUATE_TYPE        nvarchar(10)         null,
   EVALUATE_CODE        nvarchar(30)         null,
   EVALUATE_SCORE       integer              null,
   WIDTH                integer              null,
   HEIGHT               integer              null,
   UNIT                 nvarchar(30)         null,
   PRECISIONS           integer              null,
   REMARK               nvarchar(300)        null,
   CREATE_USER_REFID    varchar(32)          null,
   CREATE_DATE_TIME     datetime             null,
   MODIFY_USER_REFID    varchar(32)          null,
   MODIFY_DATE_TIME     datetime             null,
   VERSION              int                  null,
   ACTIVE               bit                  null,
   constraint PK_METADATA primary key (REFID)
)
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '元数据',
   'user', @CurrentUser, 'table', 'METADATA'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '元数据名称',
   'user', @CurrentUser, 'table', 'METADATA', 'column', 'METADATA_NAME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '元数据名称简称',
   'user', @CurrentUser, 'table', 'METADATA', 'column', 'METADATA_SIMPLE_NAME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '元数据名称拼音与拼音简写',
   'user', @CurrentUser, 'table', 'METADATA', 'column', 'METADATA_NAME_PINYIN'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '元数据代码',
   'user', @CurrentUser, 'table', 'METADATA', 'column', 'METADATA_CODE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '数据源Id',
   'user', @CurrentUser, 'table', 'METADATA', 'column', 'DATA_SOURCE_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '数据源类型。患者信息(PAT)、系统信息(SYS)、其他对象(BEN)、共享数据(SHR)、文书数据(NUS)',
   'user', @CurrentUser, 'table', 'METADATA', 'column', 'R_DATA_SOURCE_TYPE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '数据源JSON路径',
   'user', @CurrentUser, 'table', 'METADATA', 'column', 'DATA_SOURCE_PATH'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '输入类型。包括键入(INPT)、字串(STR)、数值(NUM)、邮箱(MAL)、体温(TP)、脉搏(PLS)、开关(SWT)、登录人(LGN)、护士长(NUS_MNG)、系统时间(SYS_TIM)、日期(DAT)、时间(TIM)、单选(SEL)、多选(MSEL)、单选带输入(SEL_INPT)、多选带输入(MSEL_INPT)、操作(OPT)',
   'user', @CurrentUser, 'table', 'METADATA', 'column', 'INPUT_TYPE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '数据类型。包括：数值（NUM)、字串(STR)、日期(DAT)、日期时间(DAT_TIM)、温度（TP)、脉搏(PLS)等',
   'user', @CurrentUser, 'table', 'METADATA', 'column', 'DATA_TYPE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '效用范围。包括住院共享(SHR)、文书私有(PRV)',
   'user', @CurrentUser, 'table', 'METADATA', 'column', 'SCOPE_TYPE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '是否只读.只读(Y),可写(N)',
   'user', @CurrentUser, 'table', 'METADATA', 'column', 'READONLY_FLAG'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '时间主键标识。主键(Y),非主键(N)',
   'user', @CurrentUser, 'table', 'METADATA', 'column', 'TIME_KEY_FLAG'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最小值。用于输入值校验',
   'user', @CurrentUser, 'table', 'METADATA', 'column', 'MIN_VALUE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最大值。用于输入值校验',
   'user', @CurrentUser, 'table', 'METADATA', 'column', 'MAX_VALUE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '正常范围最小值。用于检验结果标识正常值范围。',
   'user', @CurrentUser, 'table', 'METADATA', 'column', 'NORMAL_MIN_VALUE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '正常范围最大值。用于检验结果标识正常值范围。',
   'user', @CurrentUser, 'table', 'METADATA', 'column', 'NORMAL_MAX_VALUE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '校验规则。除依据数据类型自动校验外的特殊校验规则。不同规则之间以逗号分隔',
   'user', @CurrentUser, 'table', 'METADATA', 'column', 'VERIFY_POLICY_CODE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '评估类型。包括非评估(NOT)，评估项(ITM)，评估结果(RSLT)，计算量(CAL_ITM)，计算量结果(CAL_RSLT)',
   'user', @CurrentUser, 'table', 'METADATA', 'column', 'EVALUATE_TYPE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '评估代码。评估项时，填充评估结果的元数据代码',
   'user', @CurrentUser, 'table', 'METADATA', 'column', 'EVALUATE_CODE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '评估分数。用于评估项，且没有数据元选项时。比如开关项',
   'user', @CurrentUser, 'table', 'METADATA', 'column', 'EVALUATE_SCORE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '宽度。单位px',
   'user', @CurrentUser, 'table', 'METADATA', 'column', 'WIDTH'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '高度',
   'user', @CurrentUser, 'table', 'METADATA', 'column', 'HEIGHT'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '单位名称',
   'user', @CurrentUser, 'table', 'METADATA', 'column', 'UNIT'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '小数位数',
   'user', @CurrentUser, 'table', 'METADATA', 'column', 'PRECISIONS'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '备注',
   'user', @CurrentUser, 'table', 'METADATA', 'column', 'REMARK'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改人',
   'user', @CurrentUser, 'table', 'METADATA', 'column', 'CREATE_USER_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改时间',
   'user', @CurrentUser, 'table', 'METADATA', 'column', 'CREATE_DATE_TIME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改人',
   'user', @CurrentUser, 'table', 'METADATA', 'column', 'MODIFY_USER_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改时间',
   'user', @CurrentUser, 'table', 'METADATA', 'column', 'MODIFY_DATE_TIME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '版本号',
   'user', @CurrentUser, 'table', 'METADATA', 'column', 'VERSION'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '标示是否有效',
   'user', @CurrentUser, 'table', 'METADATA', 'column', 'ACTIVE'
go

/*==============================================================*/
/* Table: METADATA_OPTION                                       */
/*==============================================================*/
create table METADATA_OPTION (
   REFID                varchar(32)          not null,
   METADATA_REFID       varchar(32)          null,
   OPTION_NAME          nvarchar(60)         null,
   OPTION_CODE          nvarchar(20)         null,
   SCORE                integer              null,
   OPTION_TYPE          nvarchar(10)         null,
   ORD                  integer              null,
   REMARK               nvarchar(300)        null,
   CREATE_USER_REFID    varchar(32)          null,
   CREATE_DATE_TIME     datetime             null,
   MODIFY_USER_REFID    varchar(32)          null,
   MODIFY_DATE_TIME     datetime             null,
   VERSION              int                  null,
   ACTIVE               bit                  null,
   constraint PK_METADATA_OPTION primary key (REFID)
)
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '元数据选项',
   'user', @CurrentUser, 'table', 'METADATA_OPTION'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '元数据Id',
   'user', @CurrentUser, 'table', 'METADATA_OPTION', 'column', 'METADATA_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '选项名称',
   'user', @CurrentUser, 'table', 'METADATA_OPTION', 'column', 'OPTION_NAME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '选项代码',
   'user', @CurrentUser, 'table', 'METADATA_OPTION', 'column', 'OPTION_CODE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '选项评分。对于评估项，表示选项所占分数。对于评估结果，表示选项最低分数',
   'user', @CurrentUser, 'table', 'METADATA_OPTION', 'column', 'SCORE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '选项类型。仅仅选项(OPT)，选项带输入(INPT)',
   'user', @CurrentUser, 'table', 'METADATA_OPTION', 'column', 'OPTION_TYPE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '次序。选项次序',
   'user', @CurrentUser, 'table', 'METADATA_OPTION', 'column', 'ORD'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '备注',
   'user', @CurrentUser, 'table', 'METADATA_OPTION', 'column', 'REMARK'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改人',
   'user', @CurrentUser, 'table', 'METADATA_OPTION', 'column', 'CREATE_USER_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改时间',
   'user', @CurrentUser, 'table', 'METADATA_OPTION', 'column', 'CREATE_DATE_TIME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改人',
   'user', @CurrentUser, 'table', 'METADATA_OPTION', 'column', 'MODIFY_USER_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改时间',
   'user', @CurrentUser, 'table', 'METADATA_OPTION', 'column', 'MODIFY_DATE_TIME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '版本号',
   'user', @CurrentUser, 'table', 'METADATA_OPTION', 'column', 'VERSION'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '标示是否有效',
   'user', @CurrentUser, 'table', 'METADATA_OPTION', 'column', 'ACTIVE'
go

/*==============================================================*/
/* Table: NURSE_DOCUMENT                                        */
/*==============================================================*/
create table NURSE_DOCUMENT (
   REFID                varchar(32)          not null,
   HOSPITAL_NO_TEMPLATE_REFID varchar(32)          null,
   TEMPLATE_REFID       varchar(32)          null,
   R_TEMPLATE_NAME      nvarchar(60)         null,
   R_TEMPLATE_TYPE      nvarchar(10)         null,
   PATIENT_REFID        varchar(32)          null,
   R_PATIENT_NAME       nvarchar(60)         null,
   R_PATIENT_BARCODE    nvarchar(10)         null,
   HOSPITAL_NO          nvarchar(10)         null,
   DEPT_REFID           varchar(32)          null,
   BABY_REFID           varchar(32)          null,
   R_BABY_NAME          nvarchar(60)         null,
   DOC_NO               nvarchar(10)         null,
   DOC_TYPE             nvarchar(10)         null,
   NUMBER_TYPE          nvarchar(10)         null,
   PAGER_CODE           nvarchar(10)         null,
   VERTICAL_FLAG        char(1)              null,
   LOCAL_DATA_FLAG      char(1)              null,
   DATA_PRAGRAM         nvarchar(60)         null,
   BEGIN_TIME           datetime             null,
   END_TIME             datetime             null,
   END_FLAG             char(1)              null,
   STATUS               nvarchar(10)         null,
   WRITE_NURSER         varchar(32)          null,
   R_WRITE_NURSER_NAME  nvarchar(60)         null,
   WRITE_TIME           datetime             null,
   APPROVE_NURSER       varchar(32)          null,
   R_APPROVE_NURSER_NAME nvarchar(60)         null,
   APPROVE_TIME         datetime             null,
   PRINT_COUNT          integer              null,
   PRINT_NURSER         varchar(32)          null,
   R_PRINT_NURSER_NAME  nvarchar(60)         null,
   PRINT_TIME           datetime             null,
   TABLE_PATTERN        varchar(32)          null,
   TEMPLATE_HTML        text                 null,
   INSTANT_URL          nvarchar(100)        null,
   NDA_URL              nvarchar(100)        null,
   SEARCH_URL           nvarchar(100)        null,
   ORD                  integer              null,
   CREATE_USER_REFID    varchar(32)          null,
   CREATE_DATE_TIME     datetime             null,
   MODIFY_USER_REFID    varchar(32)          null,
   MODIFY_DATE_TIME     datetime             null,
   VERSION              int                  null,
   ACTIVE               bit                  null,
   constraint PK_NURSE_DOCUMENT primary key (REFID)
)
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '护理文书',
   'user', @CurrentUser, 'table', 'NURSE_DOCUMENT'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '住院号模板ID',
   'user', @CurrentUser, 'table', 'NURSE_DOCUMENT', 'column', 'HOSPITAL_NO_TEMPLATE_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '模板Id',
   'user', @CurrentUser, 'table', 'NURSE_DOCUMENT', 'column', 'TEMPLATE_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '模板名称',
   'user', @CurrentUser, 'table', 'NURSE_DOCUMENT', 'column', 'R_TEMPLATE_NAME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '模板类型。护理记录(RCD)，体温单(TP)，评估(EVT),健康宣教(EDU)',
   'user', @CurrentUser, 'table', 'NURSE_DOCUMENT', 'column', 'R_TEMPLATE_TYPE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '患者Id',
   'user', @CurrentUser, 'table', 'NURSE_DOCUMENT', 'column', 'PATIENT_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '患者姓名',
   'user', @CurrentUser, 'table', 'NURSE_DOCUMENT', 'column', 'R_PATIENT_NAME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '患者条码号',
   'user', @CurrentUser, 'table', 'NURSE_DOCUMENT', 'column', 'R_PATIENT_BARCODE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '住院号',
   'user', @CurrentUser, 'table', 'NURSE_DOCUMENT', 'column', 'HOSPITAL_NO'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '科室Id',
   'user', @CurrentUser, 'table', 'NURSE_DOCUMENT', 'column', 'DEPT_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '婴儿Id',
   'user', @CurrentUser, 'table', 'NURSE_DOCUMENT', 'column', 'BABY_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '婴儿名称',
   'user', @CurrentUser, 'table', 'NURSE_DOCUMENT', 'column', 'R_BABY_NAME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '文书编号',
   'user', @CurrentUser, 'table', 'NURSE_DOCUMENT', 'column', 'DOC_NO'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '文书类型。护理记录(RCD)、评估(EVT)',
   'user', @CurrentUser, 'table', 'NURSE_DOCUMENT', 'column', 'DOC_TYPE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '数量类型。一个(ONCE)、多个(MUL)、时间点多个(TIM_MUL)',
   'user', @CurrentUser, 'table', 'NURSE_DOCUMENT', 'column', 'NUMBER_TYPE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '纸张代码',
   'user', @CurrentUser, 'table', 'NURSE_DOCUMENT', 'column', 'PAGER_CODE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '打印纵向标志。Y表示纵向，不需要调换纸张，N表示横向，需要调换纸张宽高',
   'user', @CurrentUser, 'table', 'NURSE_DOCUMENT', 'column', 'VERTICAL_FLAG'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '数据存储本地DB标志。Y数据存储在本地，文书作为每一个文书来用，N数据存储在其它系统，文书作为模板来用',
   'user', @CurrentUser, 'table', 'NURSE_DOCUMENT', 'column', 'LOCAL_DATA_FLAG'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '数据程序。数据读写程序Spring bean名称。当数据存储为本地时，固定为LocalNurseDoc',
   'user', @CurrentUser, 'table', 'NURSE_DOCUMENT', 'column', 'DATA_PRAGRAM'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '共享数据时间开始',
   'user', @CurrentUser, 'table', 'NURSE_DOCUMENT', 'column', 'BEGIN_TIME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '共享数据时间结束。空表示未结束',
   'user', @CurrentUser, 'table', 'NURSE_DOCUMENT', 'column', 'END_TIME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '是否终止。终止(Y),未终止(N)。用于同一模板的多个实例',
   'user', @CurrentUser, 'table', 'NURSE_DOCUMENT', 'column', 'END_FLAG'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '状态。临时(TMP)、草稿(DRAFT),已提交审签(SUBMIT),已审签(APPROVE)。临时的如果未保存过，将会再下一次重新创建时删除。',
   'user', @CurrentUser, 'table', 'NURSE_DOCUMENT', 'column', 'STATUS'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '填写护士',
   'user', @CurrentUser, 'table', 'NURSE_DOCUMENT', 'column', 'WRITE_NURSER'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '填写护士姓名',
   'user', @CurrentUser, 'table', 'NURSE_DOCUMENT', 'column', 'R_WRITE_NURSER_NAME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '填写时间',
   'user', @CurrentUser, 'table', 'NURSE_DOCUMENT', 'column', 'WRITE_TIME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '审签护士',
   'user', @CurrentUser, 'table', 'NURSE_DOCUMENT', 'column', 'APPROVE_NURSER'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '审签护士姓名',
   'user', @CurrentUser, 'table', 'NURSE_DOCUMENT', 'column', 'R_APPROVE_NURSER_NAME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '审签时间',
   'user', @CurrentUser, 'table', 'NURSE_DOCUMENT', 'column', 'APPROVE_TIME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '打印次数',
   'user', @CurrentUser, 'table', 'NURSE_DOCUMENT', 'column', 'PRINT_COUNT'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '打印护士',
   'user', @CurrentUser, 'table', 'NURSE_DOCUMENT', 'column', 'PRINT_NURSER'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '打印护士姓名',
   'user', @CurrentUser, 'table', 'NURSE_DOCUMENT', 'column', 'R_PRINT_NURSER_NAME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '打印时间',
   'user', @CurrentUser, 'table', 'NURSE_DOCUMENT', 'column', 'PRINT_TIME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '护理文书表格项模式。一般为空',
   'user', @CurrentUser, 'table', 'NURSE_DOCUMENT', 'column', 'TABLE_PATTERN'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '模板HTML',
   'user', @CurrentUser, 'table', 'NURSE_DOCUMENT', 'column', 'TEMPLATE_HTML'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '实例化URL。特殊的模板通过更改URL来实现',
   'user', @CurrentUser, 'table', 'NURSE_DOCUMENT', 'column', 'INSTANT_URL'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   'NDA入口URL',
   'user', @CurrentUser, 'table', 'NURSE_DOCUMENT', 'column', 'NDA_URL'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '查询区域URL。特殊的查询页面通过更改URL来实现',
   'user', @CurrentUser, 'table', 'NURSE_DOCUMENT', 'column', 'SEARCH_URL'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '序号。相同模板的文书次序序号。',
   'user', @CurrentUser, 'table', 'NURSE_DOCUMENT', 'column', 'ORD'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改人',
   'user', @CurrentUser, 'table', 'NURSE_DOCUMENT', 'column', 'CREATE_USER_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改时间',
   'user', @CurrentUser, 'table', 'NURSE_DOCUMENT', 'column', 'CREATE_DATE_TIME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改人',
   'user', @CurrentUser, 'table', 'NURSE_DOCUMENT', 'column', 'MODIFY_USER_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改时间',
   'user', @CurrentUser, 'table', 'NURSE_DOCUMENT', 'column', 'MODIFY_DATE_TIME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '版本号',
   'user', @CurrentUser, 'table', 'NURSE_DOCUMENT', 'column', 'VERSION'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '标示是否有效',
   'user', @CurrentUser, 'table', 'NURSE_DOCUMENT', 'column', 'ACTIVE'
go

/*==============================================================*/
/* Table: NURSE_DOC_DATA                                        */
/*==============================================================*/
create table NURSE_DOC_DATA (
   REFID                varchar(32)          not null,
   NURSE_DOC_REFID      varchar(32)          null,
   FROM_DOC_DATA_REFID  varchar(32)          null,
   HOSPITAL_NO          nvarchar(10)         null,
   PATIENT_REFID        varchar(32)          null,
   ITEM_CODE            nvarchar(20)         null,
   ITEM_VALUE           nvarchar(100)        null,
   ITEM_INPUT_VALUE     nvarchar(100)        null,
   ITEM_VALUE_FULL      nvarchar(100)        null,
   ITEM_KEY_CODE        nvarchar(100)        null,
   ITEM_KEY_VALUE       nvarchar(300)        null,
   NORMAL_MIN_VALUE     numeric(10,2)        null,
   NORMAL_MAX_VALUE     numeric(10,2)        null,
   DOC_METADATA_REFID   varchar(32)          null,
   JSON_PATH            nvarchar(100)        null,
   DELETE_FLAG          char(1)              null,
   ORD                  integer              null,
   CREATE_USER_REFID    varchar(32)          null,
   CREATE_DATE_TIME     datetime             null,
   MODIFY_USER_REFID    varchar(32)          null,
   MODIFY_DATE_TIME     datetime             null,
   VERSION              int                  null,
   ACTIVE               bit                  null,
   constraint PK_NURSE_DOC_DATA primary key (REFID)
)
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '护理文书数据项',
   'user', @CurrentUser, 'table', 'NURSE_DOC_DATA'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '文书Id，对于共享数据，护理文书Id为空',
   'user', @CurrentUser, 'table', 'NURSE_DOC_DATA', 'column', 'NURSE_DOC_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '转抄自数据Id',
   'user', @CurrentUser, 'table', 'NURSE_DOC_DATA', 'column', 'FROM_DOC_DATA_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '住院号',
   'user', @CurrentUser, 'table', 'NURSE_DOC_DATA', 'column', 'HOSPITAL_NO'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '患者',
   'user', @CurrentUser, 'table', 'NURSE_DOC_DATA', 'column', 'PATIENT_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '值代码',
   'user', @CurrentUser, 'table', 'NURSE_DOC_DATA', 'column', 'ITEM_CODE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '值.选项时填Id',
   'user', @CurrentUser, 'table', 'NURSE_DOC_DATA', 'column', 'ITEM_VALUE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '输入值。用于其它项的输入等。',
   'user', @CurrentUser, 'table', 'NURSE_DOC_DATA', 'column', 'ITEM_INPUT_VALUE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '值描述。例如：身高：175cm',
   'user', @CurrentUser, 'table', 'NURSE_DOC_DATA', 'column', 'ITEM_VALUE_FULL'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '行数据Key代码',
   'user', @CurrentUser, 'table', 'NURSE_DOC_DATA', 'column', 'ITEM_KEY_CODE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '行数据Key值',
   'user', @CurrentUser, 'table', 'NURSE_DOC_DATA', 'column', 'ITEM_KEY_VALUE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '正常范围最小值。用于检验结果标识正常值范围。',
   'user', @CurrentUser, 'table', 'NURSE_DOC_DATA', 'column', 'NORMAL_MIN_VALUE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '正常范围最大值。用于检验结果标识正常值范围。',
   'user', @CurrentUser, 'table', 'NURSE_DOC_DATA', 'column', 'NORMAL_MAX_VALUE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '文书元数据Id',
   'user', @CurrentUser, 'table', 'NURSE_DOC_DATA', 'column', 'DOC_METADATA_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '全JsonPath',
   'user', @CurrentUser, 'table', 'NURSE_DOC_DATA', 'column', 'JSON_PATH'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '数据删除标识',
   'user', @CurrentUser, 'table', 'NURSE_DOC_DATA', 'column', 'DELETE_FLAG'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '序号。来自ITEM或表格中的ORD。共享时无效',
   'user', @CurrentUser, 'table', 'NURSE_DOC_DATA', 'column', 'ORD'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改人',
   'user', @CurrentUser, 'table', 'NURSE_DOC_DATA', 'column', 'CREATE_USER_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改时间',
   'user', @CurrentUser, 'table', 'NURSE_DOC_DATA', 'column', 'CREATE_DATE_TIME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改人',
   'user', @CurrentUser, 'table', 'NURSE_DOC_DATA', 'column', 'MODIFY_USER_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改时间',
   'user', @CurrentUser, 'table', 'NURSE_DOC_DATA', 'column', 'MODIFY_DATE_TIME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '版本号',
   'user', @CurrentUser, 'table', 'NURSE_DOC_DATA', 'column', 'VERSION'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '标示是否有效',
   'user', @CurrentUser, 'table', 'NURSE_DOC_DATA', 'column', 'ACTIVE'
go

/*==============================================================*/
/* Table: NURSE_DOC_DATA_HIS                                    */
/*==============================================================*/
create table NURSE_DOC_DATA_HIS (
   REFID                varchar(32)          not null,
   NURSE_DOC_DATA_REFID varchar(32)          null,
   NURSE_DOC_REFID      varchar(32)          null,
   FROM_DOC_DATA_REFID  varchar(32)          null,
   HOSPITAL_NO          nvarchar(10)         null,
   PATIENT_REFID        varchar(32)          null,
   ITEM_CODE            nvarchar(20)         null,
   ITEM_VALUE           nvarchar(100)        null,
   ITEM_INPUT_VALUE     nvarchar(100)        null,
   NEW_ITEM_VALUE       nvarchar(100)        null,
   NEW_ITEM_INPUT_VALUE nvarchar(100)        null,
   CHANGE_NURSER_REFID  varchar(32)          null,
   R_CHANGE_NURSER_NAME nvarchar(60)         null,
   CHANGE_TIME          datetime             null,
   ITEM_VALUE_FULL      nvarchar(100)        null,
   ITEM_KEY_CODE        nvarchar(100)        null,
   ITEM_KEY_VALUE       nvarchar(300)        null,
   NORMAL_MIN_VALUE     numeric(10,2)        null,
   NORMAL_MAX_VALUE     numeric(10,2)        null,
   DOC_METADATA_REFID   varchar(32)          null,
   JSON_PATH            nvarchar(100)        null,
   DELETE_FLAG          char(1)              null,
   ORD                  integer              null,
   CREATE_USER_REFID    varchar(32)          null,
   CREATE_DATE_TIME     datetime             null,
   MODIFY_USER_REFID    varchar(32)          null,
   MODIFY_DATE_TIME     datetime             null,
   VERSION              int                  null,
   ACTIVE               bit                  null,
   constraint PK_NURSE_DOC_DATA_HIS primary key (REFID)
)
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '护理文书数据项修改历史',
   'user', @CurrentUser, 'table', 'NURSE_DOC_DATA_HIS'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '文书数据ID',
   'user', @CurrentUser, 'table', 'NURSE_DOC_DATA_HIS', 'column', 'NURSE_DOC_DATA_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '文书Id，对于共享数据，护理文书Id为空',
   'user', @CurrentUser, 'table', 'NURSE_DOC_DATA_HIS', 'column', 'NURSE_DOC_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '转抄自数据Id',
   'user', @CurrentUser, 'table', 'NURSE_DOC_DATA_HIS', 'column', 'FROM_DOC_DATA_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '住院号',
   'user', @CurrentUser, 'table', 'NURSE_DOC_DATA_HIS', 'column', 'HOSPITAL_NO'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '患者',
   'user', @CurrentUser, 'table', 'NURSE_DOC_DATA_HIS', 'column', 'PATIENT_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '值代码',
   'user', @CurrentUser, 'table', 'NURSE_DOC_DATA_HIS', 'column', 'ITEM_CODE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '值',
   'user', @CurrentUser, 'table', 'NURSE_DOC_DATA_HIS', 'column', 'ITEM_VALUE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '输入值。用于其它项的输入等。',
   'user', @CurrentUser, 'table', 'NURSE_DOC_DATA_HIS', 'column', 'ITEM_INPUT_VALUE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '新值',
   'user', @CurrentUser, 'table', 'NURSE_DOC_DATA_HIS', 'column', 'NEW_ITEM_VALUE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '新输入值。用于其它项的输入等。',
   'user', @CurrentUser, 'table', 'NURSE_DOC_DATA_HIS', 'column', 'NEW_ITEM_INPUT_VALUE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '修改护士',
   'user', @CurrentUser, 'table', 'NURSE_DOC_DATA_HIS', 'column', 'CHANGE_NURSER_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '修改护士姓名',
   'user', @CurrentUser, 'table', 'NURSE_DOC_DATA_HIS', 'column', 'R_CHANGE_NURSER_NAME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '修改时间',
   'user', @CurrentUser, 'table', 'NURSE_DOC_DATA_HIS', 'column', 'CHANGE_TIME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '值描述。例如：身高：175cm',
   'user', @CurrentUser, 'table', 'NURSE_DOC_DATA_HIS', 'column', 'ITEM_VALUE_FULL'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '行数据Key代码',
   'user', @CurrentUser, 'table', 'NURSE_DOC_DATA_HIS', 'column', 'ITEM_KEY_CODE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '行数据Key值',
   'user', @CurrentUser, 'table', 'NURSE_DOC_DATA_HIS', 'column', 'ITEM_KEY_VALUE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '正常范围最小值。用于检验结果标识正常值范围。',
   'user', @CurrentUser, 'table', 'NURSE_DOC_DATA_HIS', 'column', 'NORMAL_MIN_VALUE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '正常范围最大值。用于检验结果标识正常值范围。',
   'user', @CurrentUser, 'table', 'NURSE_DOC_DATA_HIS', 'column', 'NORMAL_MAX_VALUE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '文书元数据Id',
   'user', @CurrentUser, 'table', 'NURSE_DOC_DATA_HIS', 'column', 'DOC_METADATA_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '全JsonPath',
   'user', @CurrentUser, 'table', 'NURSE_DOC_DATA_HIS', 'column', 'JSON_PATH'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '数据删除标识',
   'user', @CurrentUser, 'table', 'NURSE_DOC_DATA_HIS', 'column', 'DELETE_FLAG'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '序号。来自ITEM或表格中的ORD。共享时无效',
   'user', @CurrentUser, 'table', 'NURSE_DOC_DATA_HIS', 'column', 'ORD'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改人',
   'user', @CurrentUser, 'table', 'NURSE_DOC_DATA_HIS', 'column', 'CREATE_USER_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改时间',
   'user', @CurrentUser, 'table', 'NURSE_DOC_DATA_HIS', 'column', 'CREATE_DATE_TIME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改人',
   'user', @CurrentUser, 'table', 'NURSE_DOC_DATA_HIS', 'column', 'MODIFY_USER_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改时间',
   'user', @CurrentUser, 'table', 'NURSE_DOC_DATA_HIS', 'column', 'MODIFY_DATE_TIME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '版本号',
   'user', @CurrentUser, 'table', 'NURSE_DOC_DATA_HIS', 'column', 'VERSION'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '标示是否有效',
   'user', @CurrentUser, 'table', 'NURSE_DOC_DATA_HIS', 'column', 'ACTIVE'
go

/*==============================================================*/
/* Table: NURSE_DOC_ITEM                                        */
/*==============================================================*/
create table NURSE_DOC_ITEM (
   REFID                varchar(32)          not null,
   NURSE_DOC_REFID      varchar(32)          not null,
   TEMPLATE_ITEM_REFID  varchar(32)          not null,
   TEMPLATE_REFID       varchar(32)          null,
   ITEM_FROM            nvarchar(10)         null,
   METADATA_REFID       varchar(32)          null,
   TABLE_REFID          varchar(32)          null,
   WIDTH                integer              null,
   HEIGHT               integer              null,
   COPY_FLAG            char(1)              null,
   SEARCH_FLAG          char(1)              null,
   TEMPLATE_MENU_FLAG   char(1)              null,
   NDA_FIELD_TYPE       nvarchar(10)         null,
   SAVE_FLAG            char(1)              null,
   EDIT_FLAG            char(1)              null,
   MUST_INPUT_FLAG      char(1)              null,
   AREA_NAME            nvarchar(60)         null,
   ORD                  integer              null,
   TABLE_NAME           nvarchar(60)         null,
   TABLE_CODE           nvarchar(30)         null,
   TABLE_TYPE           nvarchar(10)         null,
   HEAD_DIRECTION       nvarchar(10)         null,
   EDIT_LAYOUT_TYPE     nvarchar(10)         null,
   MODIFY_SUMMARY_PROGRAM nvarchar(100)        null,
   EDIT_SAVE_PROGRAME   nvarchar(100)        null,
   EDIT_URL             nvarchar(100)        null,
   METADATA_NAME        nvarchar(60)         null,
   METADATA_SIMPLE_NAME nvarchar(60)         null,
   METADATA_CODE        nvarchar(20)         null,
   DATA_SOURCE_REFID    varchar(32)          null,
   R_DATA_SOURCE_TYPE   nvarchar(10)         null,
   DATA_SOURCE_PATH     nvarchar(60)         null,
   INPUT_TYPE           nvarchar(10)         null,
   DATA_TYPE            nvarchar(10)         null,
   SCOPE_TYPE           nvarchar(10)         null,
   READONLY_FLAG        char(1)              null,
   TIME_KEY_FLAG        char(1)              null,
   MIN_VALUE            numeric(10,2)        null,
   MAX_VALUE            numeric(10,2)        null,
   NORMAL_MIN_VALUE     numeric(10,2)        null,
   NORMAL_MAX_VALUE     numeric(10,2)        null,
   VERIFY_POLICY_CODE   nvarchar(100)        null,
   EVALUATE_TYPE        nvarchar(10)         null,
   EVALUATE_CODE        nvarchar(30)         null,
   EVALUATE_SCORE       integer              null,
   UNIT                 nvarchar(30)         null,
   PRECISIONS           integer              null,
   CREATE_USER_REFID    varchar(32)          null,
   CREATE_DATE_TIME     datetime             null,
   MODIFY_USER_REFID    varchar(32)          null,
   MODIFY_DATE_TIME     datetime             null,
   VERSION              int                  null,
   ACTIVE               bit                  null,
   constraint PK_NURSE_DOC_ITEM primary key (REFID)
)
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '护理文书项',
   'user', @CurrentUser, 'table', 'NURSE_DOC_ITEM'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '护理文书Id',
   'user', @CurrentUser, 'table', 'NURSE_DOC_ITEM', 'column', 'NURSE_DOC_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '模板项Id',
   'user', @CurrentUser, 'table', 'NURSE_DOC_ITEM', 'column', 'TEMPLATE_ITEM_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '模板Id',
   'user', @CurrentUser, 'table', 'NURSE_DOC_ITEM', 'column', 'TEMPLATE_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '选项来源。元数据(DATA)、表格(TBL)',
   'user', @CurrentUser, 'table', 'NURSE_DOC_ITEM', 'column', 'ITEM_FROM'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '元数据Id。选项来源是元数据时填充',
   'user', @CurrentUser, 'table', 'NURSE_DOC_ITEM', 'column', 'METADATA_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '表格Id。选项来源是表格时填充',
   'user', @CurrentUser, 'table', 'NURSE_DOC_ITEM', 'column', 'TABLE_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '宽',
   'user', @CurrentUser, 'table', 'NURSE_DOC_ITEM', 'column', 'WIDTH'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '高',
   'user', @CurrentUser, 'table', 'NURSE_DOC_ITEM', 'column', 'HEIGHT'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '允许转抄标志。可以转抄(Y),不允许转抄(N)用于生产查询条件。',
   'user', @CurrentUser, 'table', 'NURSE_DOC_ITEM', 'column', 'COPY_FLAG'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '查询条件标志。查询字段(Y),非查询自段(N)用于生产查询条件。查询条件视字段类型处理',
   'user', @CurrentUser, 'table', 'NURSE_DOC_ITEM', 'column', 'SEARCH_FLAG'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '模板身份标志。身份(Y),非身份(N)。用于多模板数据时，菜单名称',
   'user', @CurrentUser, 'table', 'NURSE_DOC_ITEM', 'column', 'TEMPLATE_MENU_FLAG'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '非NDA字段（NOT)，NDA标题字段(TTL)，NDA内容字段(CTX)。对表格无效',
   'user', @CurrentUser, 'table', 'NURSE_DOC_ITEM', 'column', 'NDA_FIELD_TYPE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '允许护理文书保存标志。允许保存(Y),不允许保存(N)。用于控制是否存储到文书数据中',
   'user', @CurrentUser, 'table', 'NURSE_DOC_ITEM', 'column', 'SAVE_FLAG'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '允许编辑标志。允许编辑(Y),只读(N)。用于控制是否允许用户改动',
   'user', @CurrentUser, 'table', 'NURSE_DOC_ITEM', 'column', 'EDIT_FLAG'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '必填项标志。必填字段(Y),非必填字段(N)用于输入控制校验。',
   'user', @CurrentUser, 'table', 'NURSE_DOC_ITEM', 'column', 'MUST_INPUT_FLAG'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '区域名称',
   'user', @CurrentUser, 'table', 'NURSE_DOC_ITEM', 'column', 'AREA_NAME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '次序',
   'user', @CurrentUser, 'table', 'NURSE_DOC_ITEM', 'column', 'ORD'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '表格名称',
   'user', @CurrentUser, 'table', 'NURSE_DOC_ITEM', 'column', 'TABLE_NAME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '表格代码',
   'user', @CurrentUser, 'table', 'NURSE_DOC_ITEM', 'column', 'TABLE_CODE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '表格类型。护理记录(RCD)、评估(EVT)',
   'user', @CurrentUser, 'table', 'NURSE_DOC_ITEM', 'column', 'TABLE_TYPE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '列表头方向。纵向(TOP)、横向(LEFT)',
   'user', @CurrentUser, 'table', 'NURSE_DOC_ITEM', 'column', 'HEAD_DIRECTION'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '编辑布局方式。右、下、列表中编辑、弹框',
   'user', @CurrentUser, 'table', 'NURSE_DOC_ITEM', 'column', 'EDIT_LAYOUT_TYPE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '变更摘要提取程序',
   'user', @CurrentUser, 'table', 'NURSE_DOC_ITEM', 'column', 'MODIFY_SUMMARY_PROGRAM'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '编辑处理程序',
   'user', @CurrentUser, 'table', 'NURSE_DOC_ITEM', 'column', 'EDIT_SAVE_PROGRAME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '编辑处理入口',
   'user', @CurrentUser, 'table', 'NURSE_DOC_ITEM', 'column', 'EDIT_URL'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '元数据名称',
   'user', @CurrentUser, 'table', 'NURSE_DOC_ITEM', 'column', 'METADATA_NAME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '元数据名称简称',
   'user', @CurrentUser, 'table', 'NURSE_DOC_ITEM', 'column', 'METADATA_SIMPLE_NAME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '元数据代码',
   'user', @CurrentUser, 'table', 'NURSE_DOC_ITEM', 'column', 'METADATA_CODE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '数据源Id',
   'user', @CurrentUser, 'table', 'NURSE_DOC_ITEM', 'column', 'DATA_SOURCE_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '数据源类型。患者信息(PAT)、系统信息(SYS)、其他对象(BEN)、共享数据(SHR)、文书数据(NUS)',
   'user', @CurrentUser, 'table', 'NURSE_DOC_ITEM', 'column', 'R_DATA_SOURCE_TYPE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '数据源JSON路径',
   'user', @CurrentUser, 'table', 'NURSE_DOC_ITEM', 'column', 'DATA_SOURCE_PATH'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '输入类型。包括键入(INPT)、字串(STR)、数值(NUM)、邮箱(MAL)、体温(TP)、脉搏(PLS)、开关(SWT)、登录人(LGN)、护士长(NUS_MNG)、系统时间(SYS_TIM)、日期(DAT)、时间(TIM)、单选(SEL)、多选(MSEL)、单选带输入(SEL_INPT)、多选带输入(MSEL_INPT)、操作(OPT)',
   'user', @CurrentUser, 'table', 'NURSE_DOC_ITEM', 'column', 'INPUT_TYPE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '数据类型。包括：数值（INPT)、字串(STR)、日期(DAT)、日期时间(DAT_TIM)、温度（TP)、脉搏(PLS)等',
   'user', @CurrentUser, 'table', 'NURSE_DOC_ITEM', 'column', 'DATA_TYPE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '效用范围。包括住院共享(SHR)、文书私有(PRV)',
   'user', @CurrentUser, 'table', 'NURSE_DOC_ITEM', 'column', 'SCOPE_TYPE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '是否只读.只读(Y),可写(N)',
   'user', @CurrentUser, 'table', 'NURSE_DOC_ITEM', 'column', 'READONLY_FLAG'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '时间主键标识。主键(Y),非主键(N)',
   'user', @CurrentUser, 'table', 'NURSE_DOC_ITEM', 'column', 'TIME_KEY_FLAG'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最小值。用于检验结果标识正常值范围。对于一些特殊的校验方式，也可用于输入值校验',
   'user', @CurrentUser, 'table', 'NURSE_DOC_ITEM', 'column', 'MIN_VALUE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最大值。用于检验结果标识正常值范围。对于一些特殊的校验方式，也可用于输入值校验',
   'user', @CurrentUser, 'table', 'NURSE_DOC_ITEM', 'column', 'MAX_VALUE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '正常范围最小值。用于检验结果标识正常值范围。',
   'user', @CurrentUser, 'table', 'NURSE_DOC_ITEM', 'column', 'NORMAL_MIN_VALUE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '正常范围最大值。用于检验结果标识正常值范围。',
   'user', @CurrentUser, 'table', 'NURSE_DOC_ITEM', 'column', 'NORMAL_MAX_VALUE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '校验规则。除依据数据类型自动校验外的特殊校验规则。不同规则之间以逗号分隔',
   'user', @CurrentUser, 'table', 'NURSE_DOC_ITEM', 'column', 'VERIFY_POLICY_CODE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '评估类型。包括非评估(NOT)，评估项(ITM)，评估结果(RSLT)',
   'user', @CurrentUser, 'table', 'NURSE_DOC_ITEM', 'column', 'EVALUATE_TYPE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '评估代码。评估项时，填充评估结果的元数据代码',
   'user', @CurrentUser, 'table', 'NURSE_DOC_ITEM', 'column', 'EVALUATE_CODE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '评估分数。用于评估项，且没有数据元选项时。比如开关项',
   'user', @CurrentUser, 'table', 'NURSE_DOC_ITEM', 'column', 'EVALUATE_SCORE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '单位名称',
   'user', @CurrentUser, 'table', 'NURSE_DOC_ITEM', 'column', 'UNIT'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '小数位数',
   'user', @CurrentUser, 'table', 'NURSE_DOC_ITEM', 'column', 'PRECISIONS'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改人',
   'user', @CurrentUser, 'table', 'NURSE_DOC_ITEM', 'column', 'CREATE_USER_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改时间',
   'user', @CurrentUser, 'table', 'NURSE_DOC_ITEM', 'column', 'CREATE_DATE_TIME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改人',
   'user', @CurrentUser, 'table', 'NURSE_DOC_ITEM', 'column', 'MODIFY_USER_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改时间',
   'user', @CurrentUser, 'table', 'NURSE_DOC_ITEM', 'column', 'MODIFY_DATE_TIME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '版本号',
   'user', @CurrentUser, 'table', 'NURSE_DOC_ITEM', 'column', 'VERSION'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '标示是否有效',
   'user', @CurrentUser, 'table', 'NURSE_DOC_ITEM', 'column', 'ACTIVE'
go

/*==============================================================*/
/* Table: NURSE_DOC_OPERATE                                     */
/*==============================================================*/
create table NURSE_DOC_OPERATE (
   REFID                varchar(32)          not null,
   NURSE_DOC_REFID      varchar(32)          null,
   TEMPLATE_REFID       varchar(32)          null,
   OPERATE_REFID        varchar(32)          null,
   ORD                  integer              null,
   CREATE_USER_REFID    varchar(32)          null,
   CREATE_DATE_TIME     datetime             null,
   MODIFY_USER_REFID    varchar(32)          null,
   MODIFY_DATE_TIME     datetime             null,
   VERSION              int                  null,
   ACTIVE               bit                  null,
   constraint PK_NURSE_DOC_OPERATE primary key (REFID)
)
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '模板操作',
   'user', @CurrentUser, 'table', 'NURSE_DOC_OPERATE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '护理文书Id',
   'user', @CurrentUser, 'table', 'NURSE_DOC_OPERATE', 'column', 'NURSE_DOC_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '模板Id',
   'user', @CurrentUser, 'table', 'NURSE_DOC_OPERATE', 'column', 'TEMPLATE_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '操作Id',
   'user', @CurrentUser, 'table', 'NURSE_DOC_OPERATE', 'column', 'OPERATE_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '次序',
   'user', @CurrentUser, 'table', 'NURSE_DOC_OPERATE', 'column', 'ORD'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改人',
   'user', @CurrentUser, 'table', 'NURSE_DOC_OPERATE', 'column', 'CREATE_USER_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改时间',
   'user', @CurrentUser, 'table', 'NURSE_DOC_OPERATE', 'column', 'CREATE_DATE_TIME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改人',
   'user', @CurrentUser, 'table', 'NURSE_DOC_OPERATE', 'column', 'MODIFY_USER_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改时间',
   'user', @CurrentUser, 'table', 'NURSE_DOC_OPERATE', 'column', 'MODIFY_DATE_TIME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '版本号',
   'user', @CurrentUser, 'table', 'NURSE_DOC_OPERATE', 'column', 'VERSION'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '标示是否有效',
   'user', @CurrentUser, 'table', 'NURSE_DOC_OPERATE', 'column', 'ACTIVE'
go

/*==============================================================*/
/* Table: OPERATE                                               */
/*==============================================================*/
create table OPERATE (
   REFID                varchar(32)          not null,
   OPERATE_TYPE         nvarchar(10)         null,
   OPERATE_NAME         nvarchar(60)         null,
   OPERATE_CODE         nvarchar(30)         null,
   PRAGRME              nvarchar(100)        null,
   REMARK               nvarchar(300)        null,
   CREATE_USER_REFID    varchar(32)          null,
   CREATE_DATE_TIME     datetime             null,
   MODIFY_USER_REFID    varchar(32)          null,
   MODIFY_DATE_TIME     datetime             null,
   VERSION              int                  null,
   ACTIVE               bit                  null,
   constraint PK_OPERATE primary key (REFID)
)
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '操作',
   'user', @CurrentUser, 'table', 'OPERATE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '操作类型。查询(SCH)、打印(PRT)、保存(SAV)、转抄(CPY)、审签(APV）、引用',
   'user', @CurrentUser, 'table', 'OPERATE', 'column', 'OPERATE_TYPE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '操作名称',
   'user', @CurrentUser, 'table', 'OPERATE', 'column', 'OPERATE_NAME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '操作代码',
   'user', @CurrentUser, 'table', 'OPERATE', 'column', 'OPERATE_CODE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '程序',
   'user', @CurrentUser, 'table', 'OPERATE', 'column', 'PRAGRME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '备注',
   'user', @CurrentUser, 'table', 'OPERATE', 'column', 'REMARK'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改人',
   'user', @CurrentUser, 'table', 'OPERATE', 'column', 'CREATE_USER_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改时间',
   'user', @CurrentUser, 'table', 'OPERATE', 'column', 'CREATE_DATE_TIME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改人',
   'user', @CurrentUser, 'table', 'OPERATE', 'column', 'MODIFY_USER_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改时间',
   'user', @CurrentUser, 'table', 'OPERATE', 'column', 'MODIFY_DATE_TIME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '版本号',
   'user', @CurrentUser, 'table', 'OPERATE', 'column', 'VERSION'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '标示是否有效',
   'user', @CurrentUser, 'table', 'OPERATE', 'column', 'ACTIVE'
go

/*==============================================================*/
/* Table: OPERATE_FIELD_SWITCH                                  */
/*==============================================================*/
create table OPERATE_FIELD_SWITCH (
   REFID                varchar(32)          not null,
   OPERATE_REFID        varchar(32)          null,
   FIELD_SWITCH_REFID   varchar(32)          null,
   CREATE_USER_REFID    varchar(32)          null,
   CREATE_DATE_TIME     datetime             null,
   MODIFY_USER_REFID    varchar(32)          null,
   MODIFY_DATE_TIME     datetime             null,
   VERSION              int                  null,
   ACTIVE               bit                  null,
   constraint PK_OPERATE_FIELD_SWITCH primary key (REFID)
)
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '操作的字串转换映射',
   'user', @CurrentUser, 'table', 'OPERATE_FIELD_SWITCH'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '操作Id',
   'user', @CurrentUser, 'table', 'OPERATE_FIELD_SWITCH', 'column', 'OPERATE_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '字段映射Id',
   'user', @CurrentUser, 'table', 'OPERATE_FIELD_SWITCH', 'column', 'FIELD_SWITCH_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改人',
   'user', @CurrentUser, 'table', 'OPERATE_FIELD_SWITCH', 'column', 'CREATE_USER_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改时间',
   'user', @CurrentUser, 'table', 'OPERATE_FIELD_SWITCH', 'column', 'CREATE_DATE_TIME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改人',
   'user', @CurrentUser, 'table', 'OPERATE_FIELD_SWITCH', 'column', 'MODIFY_USER_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改时间',
   'user', @CurrentUser, 'table', 'OPERATE_FIELD_SWITCH', 'column', 'MODIFY_DATE_TIME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '版本号',
   'user', @CurrentUser, 'table', 'OPERATE_FIELD_SWITCH', 'column', 'VERSION'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '标示是否有效',
   'user', @CurrentUser, 'table', 'OPERATE_FIELD_SWITCH', 'column', 'ACTIVE'
go

/*==============================================================*/
/* Table: PAGER                                                 */
/*==============================================================*/
create table PAGER (
   REFID                varchar(32)          not null,
   PAGER_NAME           nvarchar(60)         null,
   PAGER_CODE           nvarchar(10)         null,
   WIDTH                int                  null,
   HEIGHT               int                  null,
   CREATE_USER_REFID    varchar(32)          null,
   CREATE_DATE_TIME     datetime             null,
   MODIFY_USER_REFID    varchar(32)          null,
   MODIFY_DATE_TIME     datetime             null,
   VERSION              int                  null,
   ACTIVE               bit                  null,
   constraint PK_PAGER primary key (REFID)
)
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '纸张',
   'user', @CurrentUser, 'table', 'PAGER'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '纸张名称',
   'user', @CurrentUser, 'table', 'PAGER', 'column', 'PAGER_NAME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '纸张代码，用于打印机识别',
   'user', @CurrentUser, 'table', 'PAGER', 'column', 'PAGER_CODE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '宽',
   'user', @CurrentUser, 'table', 'PAGER', 'column', 'WIDTH'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '高',
   'user', @CurrentUser, 'table', 'PAGER', 'column', 'HEIGHT'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改人',
   'user', @CurrentUser, 'table', 'PAGER', 'column', 'CREATE_USER_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改时间',
   'user', @CurrentUser, 'table', 'PAGER', 'column', 'CREATE_DATE_TIME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改人',
   'user', @CurrentUser, 'table', 'PAGER', 'column', 'MODIFY_USER_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改时间',
   'user', @CurrentUser, 'table', 'PAGER', 'column', 'MODIFY_DATE_TIME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '版本号',
   'user', @CurrentUser, 'table', 'PAGER', 'column', 'VERSION'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '标示是否有效',
   'user', @CurrentUser, 'table', 'PAGER', 'column', 'ACTIVE'
go

/*==============================================================*/
/* Table: TABLE_TEMPLATE                                        */
/*==============================================================*/
create table TABLE_TEMPLATE (
   REFID                varchar(32)          not null,
   TABLE_NAME           nvarchar(60)         null,
   TABLE_CODE           nvarchar(30)         null,
   TABLE_TYPE           nvarchar(10)         null,
   DATA_SOURCE_PATH     nvarchar(60)         null,
   HEAD_DIRECTION       nvarchar(10)         null,
   EDIT_LAYOUT_TYPE     nvarchar(10)         null,
   MODIFY_SUMMARY_PROGRAM nvarchar(100)        null,
   EDIT_SAVE_PROGRAME   nvarchar(100)        null,
   EDIT_URL             nvarchar(100)        null,
   CREATE_USER_REFID    varchar(32)          null,
   CREATE_DATE_TIME     datetime             null,
   MODIFY_USER_REFID    varchar(32)          null,
   MODIFY_DATE_TIME     datetime             null,
   VERSION              int                  null,
   ACTIVE               bit                  null,
   constraint PK_TABLE_TEMPLATE primary key (REFID)
)
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '表格模板',
   'user', @CurrentUser, 'table', 'TABLE_TEMPLATE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '表格名称',
   'user', @CurrentUser, 'table', 'TABLE_TEMPLATE', 'column', 'TABLE_NAME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '表格代码',
   'user', @CurrentUser, 'table', 'TABLE_TEMPLATE', 'column', 'TABLE_CODE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '表格类型。护理记录(RCD)、评估(EVT)',
   'user', @CurrentUser, 'table', 'TABLE_TEMPLATE', 'column', 'TABLE_TYPE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '数据源JSON路径',
   'user', @CurrentUser, 'table', 'TABLE_TEMPLATE', 'column', 'DATA_SOURCE_PATH'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '列表头方向。纵向(TOP)、横向(LEFT)',
   'user', @CurrentUser, 'table', 'TABLE_TEMPLATE', 'column', 'HEAD_DIRECTION'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '编辑布局方式。右(RIGHT)、下(DOWN)、列表中编辑(LST)、弹框(DLG)',
   'user', @CurrentUser, 'table', 'TABLE_TEMPLATE', 'column', 'EDIT_LAYOUT_TYPE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '变更摘要提取程序',
   'user', @CurrentUser, 'table', 'TABLE_TEMPLATE', 'column', 'MODIFY_SUMMARY_PROGRAM'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '编辑处理程序',
   'user', @CurrentUser, 'table', 'TABLE_TEMPLATE', 'column', 'EDIT_SAVE_PROGRAME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '编辑处理入口',
   'user', @CurrentUser, 'table', 'TABLE_TEMPLATE', 'column', 'EDIT_URL'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改人',
   'user', @CurrentUser, 'table', 'TABLE_TEMPLATE', 'column', 'CREATE_USER_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改时间',
   'user', @CurrentUser, 'table', 'TABLE_TEMPLATE', 'column', 'CREATE_DATE_TIME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改人',
   'user', @CurrentUser, 'table', 'TABLE_TEMPLATE', 'column', 'MODIFY_USER_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改时间',
   'user', @CurrentUser, 'table', 'TABLE_TEMPLATE', 'column', 'MODIFY_DATE_TIME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '版本号',
   'user', @CurrentUser, 'table', 'TABLE_TEMPLATE', 'column', 'VERSION'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '标示是否有效',
   'user', @CurrentUser, 'table', 'TABLE_TEMPLATE', 'column', 'ACTIVE'
go

/*==============================================================*/
/* Table: TABLE_TEMPLATE_ITEM                                   */
/*==============================================================*/
create table TABLE_TEMPLATE_ITEM (
   REFID                varchar(32)          not null,
   TABLE_REFID          varchar(32)          null,
   METADATA_REFID       varchar(32)          null,
   R_METADATA_NAME      nvarchar(60)         null,
   TITLE                nvarchar(60)         null,
   KEY_FLAG             char(1)              null,
   SHOW_FLAG            char(1)              null,
   COPY_FLAG            char(1)              null,
   ORD                  integer              null,
   ALTERNATE_FLAG       char(1)              null,
   EMPTY_FLAG           char(1)              null,
   WIDTH                integer              null,
   HEIGHT               integer              null,
   READONLY_FLAG        char(1)              null,
   TIME_KEY_FLAG        char(1)              null,
   MIN_VALUE            numeric(10,3)        null,
   MAX_VALUE            numeric(10,3)        null,
   VERIFY_POLICY_CODE   nvarchar(100)        null,
   SEARCH_FLAG          char(1)              null,
   TEMPLATE_MENU_FLAG   char(1)              null,
   SAVE_FLAG            char(1)              null,
   EDIT_FLAG            char(1)              null,
   MUST_INPUT_FLAG      char(1)              null,
   NDA_FLAG             char(1)              null,
   CREATE_USER_REFID    varchar(32)          null,
   CREATE_DATE_TIME     datetime             null,
   MODIFY_USER_REFID    varchar(32)          null,
   MODIFY_DATE_TIME     datetime             null,
   VERSION              int                  null,
   ACTIVE               bit                  null,
   constraint PK_TABLE_TEMPLATE_ITEM primary key (REFID)
)
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '表格数据项',
   'user', @CurrentUser, 'table', 'TABLE_TEMPLATE_ITEM'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '表格ID',
   'user', @CurrentUser, 'table', 'TABLE_TEMPLATE_ITEM', 'column', 'TABLE_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '数据项Id。空白项时可以为空',
   'user', @CurrentUser, 'table', 'TABLE_TEMPLATE_ITEM', 'column', 'METADATA_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '元数据名称。模板中允许修改元数据名称，用于nda生成Label',
   'user', @CurrentUser, 'table', 'TABLE_TEMPLATE_ITEM', 'column', 'R_METADATA_NAME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '标题路径。下级|上级|上级的方式组织。空白项时可以为空',
   'user', @CurrentUser, 'table', 'TABLE_TEMPLATE_ITEM', 'column', 'TITLE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '行主键标识。主键(Y),非主键(N).标识行数据。表格存在多个主键标识，则多个主键标识依次连接，数据依次连接。如果表格没有主键标识，行数据默认以自动编号作为规则。',
   'user', @CurrentUser, 'table', 'TABLE_TEMPLATE_ITEM', 'column', 'KEY_FLAG'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '显示标识。显示(Y)、隐藏(N)。主要用于需要保存自动值，又不需要显示的数据。比如系统当前时间',
   'user', @CurrentUser, 'table', 'TABLE_TEMPLATE_ITEM', 'column', 'SHOW_FLAG'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '允许转抄标志。可以转抄(Y),不允许转抄(N)用于生产查询条件。',
   'user', @CurrentUser, 'table', 'TABLE_TEMPLATE_ITEM', 'column', 'COPY_FLAG'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '次序',
   'user', @CurrentUser, 'table', 'TABLE_TEMPLATE_ITEM', 'column', 'ORD'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '备选标志。备选(Y),非备选(N)。备选项可以在文书中选择',
   'user', @CurrentUser, 'table', 'TABLE_TEMPLATE_ITEM', 'column', 'ALTERNATE_FLAG'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '空白项标志。空白项(Y),非空白项（N)。空白项将会被备选项替代',
   'user', @CurrentUser, 'table', 'TABLE_TEMPLATE_ITEM', 'column', 'EMPTY_FLAG'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '宽度。单位px。初始值从METADATA复制',
   'user', @CurrentUser, 'table', 'TABLE_TEMPLATE_ITEM', 'column', 'WIDTH'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '高度。初始值从METADATA复制',
   'user', @CurrentUser, 'table', 'TABLE_TEMPLATE_ITEM', 'column', 'HEIGHT'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '是否只读。只读(Y),可写(N)。初始值从METADATA复制',
   'user', @CurrentUser, 'table', 'TABLE_TEMPLATE_ITEM', 'column', 'READONLY_FLAG'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '时间主键标识。主键(Y),非主键(N)。初始值从METADATA复制',
   'user', @CurrentUser, 'table', 'TABLE_TEMPLATE_ITEM', 'column', 'TIME_KEY_FLAG'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最小值。用于检验结果标识正常值范围。对于一些特殊的校验方式，也可用于输入值校验。初始值从METADATA复制',
   'user', @CurrentUser, 'table', 'TABLE_TEMPLATE_ITEM', 'column', 'MIN_VALUE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最大值。用于检验结果标识正常值范围。对于一些特殊的校验方式，也可用于输入值校验。',
   'user', @CurrentUser, 'table', 'TABLE_TEMPLATE_ITEM', 'column', 'MAX_VALUE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '校验规则。除依据数据类型自动校验外的特殊校验规则。不同规则之间以逗号分隔',
   'user', @CurrentUser, 'table', 'TABLE_TEMPLATE_ITEM', 'column', 'VERIFY_POLICY_CODE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '查询条件标志。查询条件(Y),非查询条件(N)。用于生产查询条件。查询条件视字段类型处理',
   'user', @CurrentUser, 'table', 'TABLE_TEMPLATE_ITEM', 'column', 'SEARCH_FLAG'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '模板身份标志。身份标识(Y)，非身份标识(N)。用于多模板数据时，菜单名称',
   'user', @CurrentUser, 'table', 'TABLE_TEMPLATE_ITEM', 'column', 'TEMPLATE_MENU_FLAG'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '允许护理文书保存标志。允许保存(Y),不允许保存(N)。用于控制是否存储到文书数据中',
   'user', @CurrentUser, 'table', 'TABLE_TEMPLATE_ITEM', 'column', 'SAVE_FLAG'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '允许编辑标志。允许编辑(Y),只读(N)。用于控制是否允许用户改动',
   'user', @CurrentUser, 'table', 'TABLE_TEMPLATE_ITEM', 'column', 'EDIT_FLAG'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '必填项标志。必填字段(Y),非必填字段(N)用于输入控制校验。',
   'user', @CurrentUser, 'table', 'TABLE_TEMPLATE_ITEM', 'column', 'MUST_INPUT_FLAG'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   'NDA端标识。NDA端显示(Y),NDA端不显示(N)',
   'user', @CurrentUser, 'table', 'TABLE_TEMPLATE_ITEM', 'column', 'NDA_FLAG'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改人',
   'user', @CurrentUser, 'table', 'TABLE_TEMPLATE_ITEM', 'column', 'CREATE_USER_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改时间',
   'user', @CurrentUser, 'table', 'TABLE_TEMPLATE_ITEM', 'column', 'CREATE_DATE_TIME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改人',
   'user', @CurrentUser, 'table', 'TABLE_TEMPLATE_ITEM', 'column', 'MODIFY_USER_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改时间',
   'user', @CurrentUser, 'table', 'TABLE_TEMPLATE_ITEM', 'column', 'MODIFY_DATE_TIME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '版本号',
   'user', @CurrentUser, 'table', 'TABLE_TEMPLATE_ITEM', 'column', 'VERSION'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '标示是否有效',
   'user', @CurrentUser, 'table', 'TABLE_TEMPLATE_ITEM', 'column', 'ACTIVE'
go

/*==============================================================*/
/* Table: TABLE_TEMPLATE_OPERATE                                */
/*==============================================================*/
create table TABLE_TEMPLATE_OPERATE (
   REFID                varchar(32)          not null,
   TABLE_REFID          varchar(32)          null,
   OPERATE_REFID        varchar(32)          null,
   ORD                  integer              null,
   CREATE_USER_REFID    varchar(32)          null,
   CREATE_DATE_TIME     datetime             null,
   MODIFY_USER_REFID    varchar(32)          null,
   MODIFY_DATE_TIME     datetime             null,
   VERSION              int                  null,
   ACTIVE               bit                  null,
   constraint PK_TABLE_TEMPLATE_OPERATE primary key (REFID)
)
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '表格模板操作',
   'user', @CurrentUser, 'table', 'TABLE_TEMPLATE_OPERATE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '表格模板Id',
   'user', @CurrentUser, 'table', 'TABLE_TEMPLATE_OPERATE', 'column', 'TABLE_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '操作Id',
   'user', @CurrentUser, 'table', 'TABLE_TEMPLATE_OPERATE', 'column', 'OPERATE_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '次序',
   'user', @CurrentUser, 'table', 'TABLE_TEMPLATE_OPERATE', 'column', 'ORD'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改人',
   'user', @CurrentUser, 'table', 'TABLE_TEMPLATE_OPERATE', 'column', 'CREATE_USER_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改时间',
   'user', @CurrentUser, 'table', 'TABLE_TEMPLATE_OPERATE', 'column', 'CREATE_DATE_TIME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改人',
   'user', @CurrentUser, 'table', 'TABLE_TEMPLATE_OPERATE', 'column', 'MODIFY_USER_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改时间',
   'user', @CurrentUser, 'table', 'TABLE_TEMPLATE_OPERATE', 'column', 'MODIFY_DATE_TIME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '版本号',
   'user', @CurrentUser, 'table', 'TABLE_TEMPLATE_OPERATE', 'column', 'VERSION'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '标示是否有效',
   'user', @CurrentUser, 'table', 'TABLE_TEMPLATE_OPERATE', 'column', 'ACTIVE'
go

/*==============================================================*/
/* Table: TABLE_TEMPLATE_PATTERN                                */
/*==============================================================*/
create table TABLE_TEMPLATE_PATTERN (
   REFID                varchar(32)          not null,
   TABLE_REFID          varchar(32)          not null,
   PATTERN_NAME         nvarchar(60)         null,
   ORD                  integer              null,
   CREATE_USER_REFID    varchar(32)          null,
   CREATE_DATE_TIME     datetime             null,
   MODIFY_USER_REFID    varchar(32)          null,
   MODIFY_DATE_TIME     datetime             null,
   VERSION              int                  null,
   ACTIVE               bit                  null,
   constraint PK_TABLE_TEMPLATE_PATTERN primary key (REFID)
)
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '表格备选模式',
   'user', @CurrentUser, 'table', 'TABLE_TEMPLATE_PATTERN'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '次序',
   'user', @CurrentUser, 'table', 'TABLE_TEMPLATE_PATTERN', 'column', 'ORD'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改人',
   'user', @CurrentUser, 'table', 'TABLE_TEMPLATE_PATTERN', 'column', 'CREATE_USER_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改时间',
   'user', @CurrentUser, 'table', 'TABLE_TEMPLATE_PATTERN', 'column', 'CREATE_DATE_TIME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改人',
   'user', @CurrentUser, 'table', 'TABLE_TEMPLATE_PATTERN', 'column', 'MODIFY_USER_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改时间',
   'user', @CurrentUser, 'table', 'TABLE_TEMPLATE_PATTERN', 'column', 'MODIFY_DATE_TIME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '版本号',
   'user', @CurrentUser, 'table', 'TABLE_TEMPLATE_PATTERN', 'column', 'VERSION'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '标示是否有效',
   'user', @CurrentUser, 'table', 'TABLE_TEMPLATE_PATTERN', 'column', 'ACTIVE'
go

/*==============================================================*/
/* Table: TABLE_TEMPLATE_PATTERN_ITEM                           */
/*==============================================================*/
create table TABLE_TEMPLATE_PATTERN_ITEM (
   REFID                varchar(32)          not null,
   TABLE_ITEM_REFID     varchar(32)          null,
   TABLE_PATTERN_REFID  varchar(32)          null,
   ORD                  integer              null,
   CREATE_USER_REFID    varchar(32)          null,
   CREATE_DATE_TIME     datetime             null,
   MODIFY_USER_REFID    varchar(32)          null,
   MODIFY_DATE_TIME     datetime             null,
   VERSION              int                  null,
   ACTIVE               bit                  null,
   constraint PK_TABLE_TEMPLATE_PATTERN_ITEM primary key (REFID)
)
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '表格备选模式备选项',
   'user', @CurrentUser, 'table', 'TABLE_TEMPLATE_PATTERN_ITEM'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '表格项Id',
   'user', @CurrentUser, 'table', 'TABLE_TEMPLATE_PATTERN_ITEM', 'column', 'TABLE_ITEM_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '表格备选样式Id',
   'user', @CurrentUser, 'table', 'TABLE_TEMPLATE_PATTERN_ITEM', 'column', 'TABLE_PATTERN_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '备选次序',
   'user', @CurrentUser, 'table', 'TABLE_TEMPLATE_PATTERN_ITEM', 'column', 'ORD'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改人',
   'user', @CurrentUser, 'table', 'TABLE_TEMPLATE_PATTERN_ITEM', 'column', 'CREATE_USER_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改时间',
   'user', @CurrentUser, 'table', 'TABLE_TEMPLATE_PATTERN_ITEM', 'column', 'CREATE_DATE_TIME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改人',
   'user', @CurrentUser, 'table', 'TABLE_TEMPLATE_PATTERN_ITEM', 'column', 'MODIFY_USER_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改时间',
   'user', @CurrentUser, 'table', 'TABLE_TEMPLATE_PATTERN_ITEM', 'column', 'MODIFY_DATE_TIME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '版本号',
   'user', @CurrentUser, 'table', 'TABLE_TEMPLATE_PATTERN_ITEM', 'column', 'VERSION'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '标示是否有效',
   'user', @CurrentUser, 'table', 'TABLE_TEMPLATE_PATTERN_ITEM', 'column', 'ACTIVE'
go

/*==============================================================*/
/* Table: TEMPLATE                                              */
/*==============================================================*/
create table TEMPLATE (
   REFID                varchar(32)          not null,
   TEMPLATE_NAME        nvarchar(60)         null,
   TEMPLATE_CODE        nvarchar(30)         null,
   TEMPLATE_TYPE        nvarchar(10)         null,
   TEMPLATE_HTML        text                 null,
   PAGER_CODE           nvarchar(10)         null,
   PAGER_WIDTH          int                  null,
   PAGER_HEIGHT         int                  null,
   VERTICAL_FLAG        char(1)              null,
   LOCAL_DATA_FLAG      char(1)              null,
   DATA_PRAGRAM         nvarchar(60)         null,
   INSTANT_URL          nvarchar(100)        null,
   NDA_URL              nvarchar(100)        null,
   SEARCH_URL           nvarchar(100)        null,
   CREATE_USER_REFID    varchar(32)          null,
   CREATE_DATE_TIME     datetime             null,
   MODIFY_USER_REFID    varchar(32)          null,
   MODIFY_DATE_TIME     datetime             null,
   VERSION              int                  null,
   ACTIVE               bit                  null,
   constraint PK_TEMPLATE primary key (REFID)
)
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '模板',
   'user', @CurrentUser, 'table', 'TEMPLATE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '模板名称',
   'user', @CurrentUser, 'table', 'TEMPLATE', 'column', 'TEMPLATE_NAME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '模板代码',
   'user', @CurrentUser, 'table', 'TEMPLATE', 'column', 'TEMPLATE_CODE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '模板类型。护理记录(RCD)，体温单(TP)，评估(EVT), 健康宣教(EDU)',
   'user', @CurrentUser, 'table', 'TEMPLATE', 'column', 'TEMPLATE_TYPE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '模板HTML',
   'user', @CurrentUser, 'table', 'TEMPLATE', 'column', 'TEMPLATE_HTML'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '纸张代码',
   'user', @CurrentUser, 'table', 'TEMPLATE', 'column', 'PAGER_CODE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '纸张宽',
   'user', @CurrentUser, 'table', 'TEMPLATE', 'column', 'PAGER_WIDTH'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '纸张高',
   'user', @CurrentUser, 'table', 'TEMPLATE', 'column', 'PAGER_HEIGHT'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '打印纵向标志',
   'user', @CurrentUser, 'table', 'TEMPLATE', 'column', 'VERTICAL_FLAG'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '数据存储本地DB标志。Y数据存储在本地，文书作为每一个文书来用，N数据存储在其它系统，文书作为模板来用',
   'user', @CurrentUser, 'table', 'TEMPLATE', 'column', 'LOCAL_DATA_FLAG'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '数据程序。数据读写程序Spring bean名称。当数据存储为本地时，固定为LocalNurseDoc',
   'user', @CurrentUser, 'table', 'TEMPLATE', 'column', 'DATA_PRAGRAM'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '实例化URL。特殊的模板通过更改URL来实现',
   'user', @CurrentUser, 'table', 'TEMPLATE', 'column', 'INSTANT_URL'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   'NDA入口URL',
   'user', @CurrentUser, 'table', 'TEMPLATE', 'column', 'NDA_URL'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '查询区域URL。特殊的查询页面通过更改URL来实现',
   'user', @CurrentUser, 'table', 'TEMPLATE', 'column', 'SEARCH_URL'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改人',
   'user', @CurrentUser, 'table', 'TEMPLATE', 'column', 'CREATE_USER_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改时间',
   'user', @CurrentUser, 'table', 'TEMPLATE', 'column', 'CREATE_DATE_TIME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改人',
   'user', @CurrentUser, 'table', 'TEMPLATE', 'column', 'MODIFY_USER_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改时间',
   'user', @CurrentUser, 'table', 'TEMPLATE', 'column', 'MODIFY_DATE_TIME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '版本号',
   'user', @CurrentUser, 'table', 'TEMPLATE', 'column', 'VERSION'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '标示是否有效',
   'user', @CurrentUser, 'table', 'TEMPLATE', 'column', 'ACTIVE'
go

/*==============================================================*/
/* Table: TEMPLATE_ITEM                                         */
/*==============================================================*/
create table TEMPLATE_ITEM (
   REFID                varchar(32)          not null,
   TEMPLATE_REFID       varchar(32)          null,
   ITEM_FROM            nvarchar(10)         null,
   METADATA_REFID       varchar(32)          null,
   R_METADATA_NAME      nvarchar(60)         null,
   TABLE_REFID          varchar(32)          null,
   WIDTH                integer              null,
   HEIGHT               integer              null,
   COPY_FLAG            char(1)              null,
   SEARCH_FLAG          char(1)              null,
   TEMPLATE_MENU_FLAG   char(1)              null,
   NDA_FIELD_TYPE       nvarchar(10)         null,
   SAVE_FLAG            char(1)              null,
   EDIT_FLAG            char(1)              null,
   MUST_INPUT_FLAG      char(1)              null,
   AREA_NAME            nvarchar(60)         null,
   ORD                  integer              null,
   CREATE_USER_REFID    varchar(32)          null,
   CREATE_DATE_TIME     datetime             null,
   MODIFY_USER_REFID    varchar(32)          null,
   MODIFY_DATE_TIME     datetime             null,
   VERSION              int                  null,
   ACTIVE               bit                  null,
   constraint PK_TEMPLATE_ITEM primary key (REFID)
)
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '模板项',
   'user', @CurrentUser, 'table', 'TEMPLATE_ITEM'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '模板Id',
   'user', @CurrentUser, 'table', 'TEMPLATE_ITEM', 'column', 'TEMPLATE_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '选项来源。元数据(DATA)、表格(TBL)',
   'user', @CurrentUser, 'table', 'TEMPLATE_ITEM', 'column', 'ITEM_FROM'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '元数据Id。选项来源是元数据时填充',
   'user', @CurrentUser, 'table', 'TEMPLATE_ITEM', 'column', 'METADATA_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '元数据名称。模板中允许修改元数据名称，用于nda生成Label',
   'user', @CurrentUser, 'table', 'TEMPLATE_ITEM', 'column', 'R_METADATA_NAME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '表格Id。选项来源是表格时填充',
   'user', @CurrentUser, 'table', 'TEMPLATE_ITEM', 'column', 'TABLE_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '宽',
   'user', @CurrentUser, 'table', 'TEMPLATE_ITEM', 'column', 'WIDTH'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '高',
   'user', @CurrentUser, 'table', 'TEMPLATE_ITEM', 'column', 'HEIGHT'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '允许转抄标志。可以转抄(Y),不允许转抄(N)用于生产查询条件。',
   'user', @CurrentUser, 'table', 'TEMPLATE_ITEM', 'column', 'COPY_FLAG'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '查询条件标志。查询字段(Y),非查询自段(N)用于生产查询条件。查询条件视字段类型处理',
   'user', @CurrentUser, 'table', 'TEMPLATE_ITEM', 'column', 'SEARCH_FLAG'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '模板身份标志。身份(Y),非身份(N)。用于多模板数据时，菜单名称',
   'user', @CurrentUser, 'table', 'TEMPLATE_ITEM', 'column', 'TEMPLATE_MENU_FLAG'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '非NDA字段（NOT)，NDA标题字段(TTL)，NDA内容字段(CTX)。对表格无效',
   'user', @CurrentUser, 'table', 'TEMPLATE_ITEM', 'column', 'NDA_FIELD_TYPE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '允许护理文书保存标志。允许保存(Y),不允许保存(N)。用于控制是否存储到文书数据中',
   'user', @CurrentUser, 'table', 'TEMPLATE_ITEM', 'column', 'SAVE_FLAG'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '允许编辑标志。允许编辑(Y),只读(N)。用于控制是否允许用户改动',
   'user', @CurrentUser, 'table', 'TEMPLATE_ITEM', 'column', 'EDIT_FLAG'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '必填项标志。必填字段(Y),非必填字段(N)用于输入控制校验。',
   'user', @CurrentUser, 'table', 'TEMPLATE_ITEM', 'column', 'MUST_INPUT_FLAG'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '区域名称',
   'user', @CurrentUser, 'table', 'TEMPLATE_ITEM', 'column', 'AREA_NAME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '次序',
   'user', @CurrentUser, 'table', 'TEMPLATE_ITEM', 'column', 'ORD'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改人',
   'user', @CurrentUser, 'table', 'TEMPLATE_ITEM', 'column', 'CREATE_USER_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改时间',
   'user', @CurrentUser, 'table', 'TEMPLATE_ITEM', 'column', 'CREATE_DATE_TIME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改人',
   'user', @CurrentUser, 'table', 'TEMPLATE_ITEM', 'column', 'MODIFY_USER_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改时间',
   'user', @CurrentUser, 'table', 'TEMPLATE_ITEM', 'column', 'MODIFY_DATE_TIME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '版本号',
   'user', @CurrentUser, 'table', 'TEMPLATE_ITEM', 'column', 'VERSION'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '标示是否有效',
   'user', @CurrentUser, 'table', 'TEMPLATE_ITEM', 'column', 'ACTIVE'
go

/*==============================================================*/
/* Table: TEMPLATE_OPERATE                                      */
/*==============================================================*/
create table TEMPLATE_OPERATE (
   REFID                varchar(32)          not null,
   TEMPLATE_REFID       varchar(32)          null,
   OPERATE_REFID        varchar(32)          null,
   ORD                  integer              null,
   CREATE_USER_REFID    varchar(32)          null,
   CREATE_DATE_TIME     datetime             null,
   MODIFY_USER_REFID    varchar(32)          null,
   MODIFY_DATE_TIME     datetime             null,
   VERSION              int                  null,
   ACTIVE               bit                  null,
   constraint PK_TEMPLATE_OPERATE primary key (REFID)
)
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '模板操作',
   'user', @CurrentUser, 'table', 'TEMPLATE_OPERATE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '模板Id',
   'user', @CurrentUser, 'table', 'TEMPLATE_OPERATE', 'column', 'TEMPLATE_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '操作Id',
   'user', @CurrentUser, 'table', 'TEMPLATE_OPERATE', 'column', 'OPERATE_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '次序',
   'user', @CurrentUser, 'table', 'TEMPLATE_OPERATE', 'column', 'ORD'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改人',
   'user', @CurrentUser, 'table', 'TEMPLATE_OPERATE', 'column', 'CREATE_USER_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改时间',
   'user', @CurrentUser, 'table', 'TEMPLATE_OPERATE', 'column', 'CREATE_DATE_TIME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改人',
   'user', @CurrentUser, 'table', 'TEMPLATE_OPERATE', 'column', 'MODIFY_USER_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改时间',
   'user', @CurrentUser, 'table', 'TEMPLATE_OPERATE', 'column', 'MODIFY_DATE_TIME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '版本号',
   'user', @CurrentUser, 'table', 'TEMPLATE_OPERATE', 'column', 'VERSION'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '标示是否有效',
   'user', @CurrentUser, 'table', 'TEMPLATE_OPERATE', 'column', 'ACTIVE'
go

/*==============================================================*/
/* Table: VERIFY_POLICY                                         */
/*==============================================================*/
create table VERIFY_POLICY (
   REFID                varchar(32)          not null,
   VERIFY_NAME          nvarchar(60)         null,
   VERIFY_CODE          nvarchar(30)         null,
   PRAGRME              nvarchar(100)        null,
   REMARK               nvarchar(300)        null,
   CREATE_USER_REFID    varchar(32)          null,
   CREATE_DATE_TIME     datetime             null,
   MODIFY_USER_REFID    varchar(32)          null,
   MODIFY_DATE_TIME     datetime             null,
   VERSION              int                  null,
   ACTIVE               bit                  null,
   constraint PK_VERIFY_POLICY primary key (REFID)
)
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '校验策略',
   'user', @CurrentUser, 'table', 'VERIFY_POLICY'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '校验名称',
   'user', @CurrentUser, 'table', 'VERIFY_POLICY', 'column', 'VERIFY_NAME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '校验代码',
   'user', @CurrentUser, 'table', 'VERIFY_POLICY', 'column', 'VERIFY_CODE'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '程序',
   'user', @CurrentUser, 'table', 'VERIFY_POLICY', 'column', 'PRAGRME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '备注',
   'user', @CurrentUser, 'table', 'VERIFY_POLICY', 'column', 'REMARK'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改人',
   'user', @CurrentUser, 'table', 'VERIFY_POLICY', 'column', 'CREATE_USER_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改时间',
   'user', @CurrentUser, 'table', 'VERIFY_POLICY', 'column', 'CREATE_DATE_TIME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改人',
   'user', @CurrentUser, 'table', 'VERIFY_POLICY', 'column', 'MODIFY_USER_REFID'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '最后修改时间',
   'user', @CurrentUser, 'table', 'VERIFY_POLICY', 'column', 'MODIFY_DATE_TIME'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '版本号',
   'user', @CurrentUser, 'table', 'VERIFY_POLICY', 'column', 'VERSION'
go

declare @CurrentUser sysname
select @CurrentUser = user_name()
execute sp_addextendedproperty 'MS_Description', 
   '标示是否有效',
   'user', @CurrentUser, 'table', 'VERIFY_POLICY', 'column', 'ACTIVE'
go

alter table DEPARTMENT_TABLE_TEMPLATE
   add constraint FK_DEPARTME_REF_TABLE_TE foreign key (TABLE_TEMPLATE_REFID)
      references TABLE_TEMPLATE (REFID)
go

alter table DEPARTMENT_TEMPALTE
   add constraint FK_DEPARTME_REFERENCE_TEMPLATE foreign key (TEMPLATE_REFID)
      references TEMPLATE (REFID)
go

alter table DOC_ITEM_OPTION
   add constraint FK_DOC_ITEM_REFERENCE_NURSE_DO foreign key (DOC_ITEM_REFID)
      references NURSE_DOC_ITEM (REFID)
go

alter table DOC_ITEM_OPTION
   add constraint FK_DOC_ITEM_REFERENCE_METADATA foreign key (METADATA_OPTION_REFID)
      references METADATA_OPTION (REFID)
go

alter table DOC_TABLE_ITEM
   add constraint FK_DOC_TBL_REF_NRSE_DO foreign key (DOC_ITEM_REFID)
      references NURSE_DOC_ITEM (REFID)
go

alter table DOC_TABLE_ITEM
   add constraint FK_DOC_TBL_REF_TBL_TE foreign key (TABLE_ITEM_REFID)
      references TABLE_TEMPLATE_ITEM (REFID)
go

alter table DOC_TABLE_ITEM_OPTION
   add constraint FK_DOC_TABL_REF_DOC_TBL foreign key (DOC_TABLE_ITEM_REFID)
      references DOC_TABLE_ITEM (REFID)
go

alter table DOC_TABLE_PATTERN
   add constraint FK_DOC_TBL_REF_NURS_DO foreign key (NURSE_DOC_REFID)
      references NURSE_DOCUMENT (REFID)
go

alter table DOC_TABLE_PATTERN
   add constraint FK_DOC_TBL_REF_TBLE_TE foreign key (TABLE_PATTERN_REFID)
      references TABLE_TEMPLATE_PATTERN (REFID)
go

alter table DOC_TABLE_PATTERN_ITEM
   add constraint FK_DOC_TABL_REE_DOC_TABL foreign key (DOC_TABLE_PATTERN_REFID)
      references DOC_TABLE_PATTERN (REFID)
go

alter table DOC_TABLE_PATTERN_ITEM
   add constraint FK_DOC_TBL_REF_DOC_TABL foreign key (DOC_TABLE_ITEM_REFID)
      references DOC_TABLE_ITEM (REFID)
go

alter table FIELD_SWITCH
   add constraint FK_FIELD_SW_REFERENCE_OPERATE foreign key (SWITCH_PROGRAME_REFID)
      references OPERATE (REFID)
go

alter table METADATA
   add constraint FK_METADATA_REFERENCE_DATA_SOU foreign key (DATA_SOURCE_REFID)
      references DATA_SOURCE (REFID)
go

alter table METADATA_OPTION
   add constraint FK_METADATA_REFERENCE_METADATA foreign key (METADATA_REFID)
      references METADATA (REFID)
go

alter table NURSE_DOCUMENT
   add constraint FK_NURSE_DO_REF_TMPL foreign key (TEMPLATE_REFID)
      references TEMPLATE (REFID)
go

alter table NURSE_DOC_DATA
   add constraint FK_NRSE_DO_REF_NRS_DO foreign key (NURSE_DOC_REFID)
      references NURSE_DOCUMENT (REFID)
go

alter table NURSE_DOC_DATA_HIS
   add constraint FK_NRSE_DO_REF_NS_DO foreign key (NURSE_DOC_DATA_REFID)
      references NURSE_DOC_DATA (REFID)
go

alter table NURSE_DOC_ITEM
   add constraint FK_NRS_DO_REF_TEMPLATE foreign key (TEMPLATE_ITEM_REFID)
      references TEMPLATE_ITEM (REFID)
go

alter table NURSE_DOC_ITEM
   add constraint FK_NRS_DO_REF_NRS_DO foreign key (NURSE_DOC_REFID)
      references NURSE_DOCUMENT (REFID)
go

alter table NURSE_DOC_OPERATE
   add constraint FK_NRS_DO_REF_NURSE_DO foreign key (NURSE_DOC_REFID)
      references NURSE_DOCUMENT (REFID)
go

alter table NURSE_DOC_OPERATE
   add constraint FK_NURSE_DO_REFERENCE_OPERATE foreign key (OPERATE_REFID)
      references OPERATE (REFID)
go

alter table OPERATE_FIELD_SWITCH
   add constraint FK_OPERATE__REFERENCE_OPERATE foreign key (OPERATE_REFID)
      references OPERATE (REFID)
go

alter table OPERATE_FIELD_SWITCH
   add constraint FK_OPERATE__REFERENCE_FIELD_SW foreign key (FIELD_SWITCH_REFID)
      references FIELD_SWITCH (REFID)
go

alter table TABLE_TEMPLATE_ITEM
   add constraint FK_TBL_TE_REF_TBL_TE foreign key (TABLE_REFID)
      references TABLE_TEMPLATE (REFID)
go

alter table TABLE_TEMPLATE_ITEM
   add constraint FK_TABLE_TE_REFERENCE_METADATA foreign key (METADATA_REFID)
      references METADATA (REFID)
go

alter table TABLE_TEMPLATE_OPERATE
   add constraint FK_TABLE_TE_REFERENCE_TABLE_TE foreign key (TABLE_REFID)
      references TABLE_TEMPLATE (REFID)
go

alter table TABLE_TEMPLATE_OPERATE
   add constraint FK_TABLE_TE_REFERENCE_OPERATE foreign key (OPERATE_REFID)
      references OPERATE (REFID)
go

alter table TABLE_TEMPLATE_PATTERN
   add constraint FK_TBL_TE_REF_TABLE_TE foreign key (TABLE_REFID)
      references TABLE_TEMPLATE (REFID)
go

alter table TABLE_TEMPLATE_PATTERN_ITEM
   add constraint FK_TABLE_TE_REF_TBL_TE foreign key (TABLE_ITEM_REFID)
      references TABLE_TEMPLATE_ITEM (REFID)
go

alter table TABLE_TEMPLATE_PATTERN_ITEM
   add constraint FK_TBLE_TE_REF_TBL_TE foreign key (TABLE_PATTERN_REFID)
      references TABLE_TEMPLATE_PATTERN (REFID)
go

alter table TEMPLATE_ITEM
   add constraint FK_TEMPLATE_REFERENCE_TABLE_TE foreign key (TABLE_REFID)
      references TABLE_TEMPLATE (REFID)
go

alter table TEMPLATE_ITEM
   add constraint FK_TMPT_REF_TEMPLATE foreign key (TEMPLATE_REFID)
      references TEMPLATE (REFID)
go

alter table TEMPLATE_ITEM
   add constraint FK_TEMPLATE_REFERENCE_METADATA foreign key (METADATA_REFID)
      references METADATA (REFID)
go

alter table TEMPLATE_OPERATE
   add constraint FK_TEMPLATE_REF_TMPL foreign key (TEMPLATE_REFID)
      references TEMPLATE (REFID)
go

alter table TEMPLATE_OPERATE
   add constraint FK_TEMPLATE_REFERENCE_OPERATE foreign key (OPERATE_REFID)
      references OPERATE (REFID)
go

