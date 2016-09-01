----------COM_DIC新增是否配液字段LIQUOR---------
 if not exists(select 1 from sys.all_columns where object_id=object_id('COM_DIC') and name='LIQUOR') 
    ALTER TABLE [dbo].COM_DIC 
    ADD [LIQUOR] char(1) COLLATE Chinese_PRC_CI_AS NULL 
    GO 
    if exists(select 1 from sys.all_columns where object_id=object_id('COM_DIC') and name='LIQUOR') 
    EXECUTE [sp_addextendedproperty] 
    @name = N'MS_Description', 
    @value = N'是否允许配液(0:不允许,1:允许)', 
    @level0type = 'SCHEMA', 
    @level0name = N'dbo', 
    @level1type = 'TABLE', 
    @level1name = N'COM_DIC', 
    @level2type = 'COLUMN', 
    @level2name = N'LIQUOR' 
    GO 
    
----------生命体征修改---------
USE MNIS_V3
GO 
if not exists(select 1 from sys.all_columns where object_id=object_id('PAT_BODYSIGN_MAS') and name='FIRST_DATE')
ALTER TABLE [dbo].[PAT_BODYSIGN_MAS] 
ADD [FIRST_DATE] datetime  NULL, 
[SOURCE] varchar(2) COLLATE Chinese_PRC_CI_AS NULL  default 2 
GO 

if not exists(select 1 from sys.all_columns where object_id=object_id('PAT_BODYSIGN_MAS') and name='FIRST_DATE')
EXECUTE [sp_addextendedproperty] 
@name = N'MS_Description', 
@value = N'第一次录入时间', 
@level0type = 'SCHEMA', 
@level0name = N'dbo', 
@level1type = 'TABLE', 
@level1name = N'PAT_BODYSIGN_MAS', 
@level2type = 'COLUMN', 
@level2name = N'FIRST_DATE' 
GO 
if not exists(select 1 from sys.all_columns where object_id=object_id('PAT_BODYSIGN_MAS') and name='SOURCE')
EXECUTE [sp_addextendedproperty] 
@name = N'MS_Description', 
@value = N'数据来源 1：HIS录入 2：移动护理录入', 
@level0type = 'SCHEMA', 
@level0name = N'dbo', 
@level1type = 'TABLE', 
@level1name = N'PAT_BODYSIGN_MAS', 
@level2type = 'COLUMN', 
@level2name = N'SOURCE' 
GO

use MNIS_V3
IF EXISTS (select object_name(parent_obj) from sysobjects where xtype='pk' and name='PK_PAT_BODYSIGN_MAS')
alter table [dbo].[PAT_BODYSIGN_MAS] drop constraint [PK_PAT_BODYSIGN_MAS]

if exists (select object_id from sys.indexes where name='Relationship_35_FK')
DROP index [Relationship_35_FK] on [dbo].[PAT_BODYSIGN_MAS]
GO
ALTER TABLE [dbo].[PAT_BODYSIGN_MAS]
 ADD CONSTRAINT [PK_PAT_BODYSIGN_MAS] PRIMARY KEY ([PAT_ID] ASC, [RECORD_DATE] ASC) WITH (FILLFACTOR=100,
		DATA_COMPRESSION = NONE) ON [PRIMARY]
GO

if not exists(select 1 from sys.all_columns where object_id=object_id('PAT_BODYSIGN_DETAIL') and name='PAT_ID') 
ALTER TABLE [dbo].PAT_BODYSIGN_DETAIL 
ADD [PAT_ID] varchar(20) NULL 
GO 
if not exists(select 1 from sys.all_columns where object_id=object_id('PAT_BODYSIGN_DETAIL') and name='PAT_ID') 
EXECUTE [sp_addextendedproperty] 
@name = N'MS_Description', 
@value = N'患者id', 
@level0type = 'SCHEMA', 
@level0name = N'dbo', 
@level1type = 'TABLE', 
@level1name = N'PAT_BODYSIGN_DETAIL', 
@level2type = 'COLUMN', 
@level2name = N'PAT_ID' 
GO 


if not exists(select 1 from sys.all_columns where object_id=object_id('PAT_BODYSIGN_DETAIL') and name='RECORD_DATE') 
ALTER TABLE [dbo].PAT_BODYSIGN_DETAIL 
ADD [RECORD_DATE] datetime NULL 
GO 
if not exists(select 1 from sys.all_columns where object_id=object_id('PAT_BODYSIGN_DETAIL') and name='RECORD_DATE') 
EXECUTE [sp_addextendedproperty] 
@name = N'MS_Description', 
@value = N'记录时间点', 
@level0type = 'SCHEMA', 
@level0name = N'dbo', 
@level1type = 'TABLE', 
@level1name = N'PAT_BODYSIGN_DETAIL', 
@level2type = 'COLUMN', 
@level2name = N'RECORD_DATE' 
GO 

IF EXISTS (select object_name(parent_obj) from sysobjects where xtype='pk' and name='PK_PAT_BODYSIGN_DETAIL')
alter table [dbo].[PAT_BODYSIGN_DETAIL] drop constraint [PK_PAT_BODYSIGN_DETAIL]
GO
update dbo.PAT_BODYSIGN_DETAIL set PAT_ID=(select t.PAT_ID from dbo.PAT_BODYSIGN_MAS t where t.ID=MAS_ID),
RECORD_DATE=(select t.RECORD_DATE from dbo.PAT_BODYSIGN_MAS t where t.ID=MAS_ID)
GO

ALTER TABLE [dbo].PAT_BODYSIGN_DETAIL 
ALTER COLUMN [PAT_ID] varchar(20) not NULL 
GO 
ALTER TABLE [dbo].PAT_BODYSIGN_DETAIL 
ALTER COLUMN [RECORD_DATE] datetime not  NULL 
GO 

ALTER TABLE [dbo].[PAT_BODYSIGN_DETAIL]
 ADD CONSTRAINT [PK_PAT_BODYSIGN_DETAIL] PRIMARY KEY ([PAT_ID] ASC,[ITEM_CODE] ASC, [RECORD_DATE] ASC) WITH (FILLFACTOR=100,
		DATA_COMPRESSION = NONE) ON [PRIMARY]
GO

ALTER TABLE [dbo].[PAT_BODYSIGN_DETAIL]
ALTER COLUMN [MAS_ID] bigint  NULL
GO
------------ 修改事件表（PAT_EVNET）表主键为：PAT_ID,RECORD_DATE,EVENT_CODE为复合主键---------------
if not exists(select 1 from sys.all_columns where object_id=object_id('PAT_EVENT') and name='RECORD_DATE') 
ALTER TABLE [dbo].PAT_EVENT 
ADD [RECORD_DATE] datetime  NULL 
GO 
if not exists(select 1 from sys.all_columns where object_id=object_id('PAT_EVENT') and name='RECORD_DATE') 
EXECUTE [sp_addextendedproperty] 
@name = N'MS_Description', 
@value = N'记录时间点', 
@level0type = 'SCHEMA', 
@level0name = N'dbo', 
@level1type = 'TABLE', 
@level1name = N'PAT_EVENT', 
@level2type = 'COLUMN', 
@level2name = N'RECORD_DATE' 
GO 

IF EXISTS (select object_name(parent_obj) from sysobjects where xtype='pk' and name='PK_PAT_EVENT')
alter table [dbo].[PAT_EVENT] drop constraint [PK_PAT_EVENT]
GO
update dbo.PAT_EVENT set 
RECORD_DATE=(select t.RECORD_DATE from dbo.PAT_BODYSIGN_MAS t where t.ID=BODY_SIGN_MAS_ID)
GO

ALTER TABLE [dbo].PAT_EVENT 
ALTER COLUMN [RECORD_DATE] datetime not  NULL 
GO
ALTER TABLE [dbo].PAT_EVENT 
ALTER COLUMN [EVENT_CODE] varchar(10)  not  NULL
GO

ALTER TABLE [dbo].[PAT_EVENT]
 ADD CONSTRAINT [PK_PAT_EVENT] PRIMARY KEY ([PAT_ID] ASC,[RECORD_DATE] ASC,[EVENT_CODE] ASC) WITH (FILLFACTOR=100,
		DATA_COMPRESSION = NONE) ON [PRIMARY]
GO

ALTER TABLE [dbo].[PAT_EVENT]
ALTER COLUMN [EVENT_DATE] varchar(10) COLLATE Chinese_PRC_CI_AS NULL
GO   

--患者基础信息表中添加主诉字段。
 if not exists(select 1 from sys.all_columns where object_id=object_id('PAT_CURE_INFO') and name='APPEAL') 
    ALTER TABLE [dbo].PAT_CURE_INFO 
    ADD APPEAL varchar(4000) COLLATE Chinese_PRC_CI_AS NULL 
    GO 
    if not exists(select 1 from sys.all_columns where object_id=object_id('PAT_CURE_INFO') and name='APPEAL') 
    EXECUTE [sp_addextendedproperty] 
    @name = N'MS_Description', 
    @value = N'主诉', 
    @level0type = 'SCHEMA', 
    @level0name = N'dbo', 
    @level1type = 'TABLE', 
    @level1name = N'PAT_CURE_INFO', 
    @level2type = 'COLUMN', 
    @level2name = N'APPEAL' 
    GO  
--检验表-检查日期（暂时没有用到）
	if not exists(select 1 from sys.all_columns where object_id=object_id('PAT_LAB_TEST_MAS') and name='EXAM_DATE_TIME') 
    ALTER TABLE [dbo].PAT_LAB_TEST_MAS 
    ADD EXAM_DATE_TIME char(1) COLLATE Chinese_PRC_CI_AS NULL 
    GO 
    if not exists(select 1 from sys.all_columns where object_id=object_id('PAT_LAB_TEST_MAS') and name='EXAM_DATE_TIME') 
    EXECUTE [sp_addextendedproperty] 
    @name = N'MS_Description', 
    @value = N'检查时间', 
    @level0type = 'SCHEMA', 
    @level0name = N'dbo', 
    @level1type = 'TABLE', 
    @level1name = N'PAT_LAB_TEST_MAS', 
    @level2type = 'COLUMN', 
    @level2name = N'EXAM_DATE_TIME' 
    GO 
    
--条码映射表
	CREATE TABLE [dbo].[PAT_BARCODE_RELATION](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[his_barcode] [varchar](50) NOT NULL,
	[lx_barcode] [varchar](50) NOT NULL,
	[plan_time] [datetime] NULL,
 CONSTRAINT [PK_pat_barcode_relation] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]

GO

SET ANSI_PADDING OFF
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'主键，自增长' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'pat_barcode_relation', @level2type=N'COLUMN',@level2name=N'id'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'医院条码' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'pat_barcode_relation', @level2type=N'COLUMN',@level2name=N'his_barcode'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'MNIS系统自定义条码' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'pat_barcode_relation', @level2type=N'COLUMN',@level2name=N'lx_barcode'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'条码对应医院的计划执行时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'pat_barcode_relation', @level2type=N'COLUMN',@level2name=N'plan_time'
GO

--检验项目
alter table pat_lab_test_mas alter column TEST_SUBJECT varchar(1000)
go

--出入量管理
USE [MNIS_V3]
GO
/****** Object:  Table [dbo].[PAT_IN_OUT_MANAGER]    Script Date: 11/23/2015 21:56:30 ******/
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[PAT_IN_OUT_MANAGER]') AND type in (N'U'))
DROP TABLE [dbo].[PAT_IN_OUT_MANAGER]
GO
/****** Object:  Table [dbo].[PAT_IN_OUT_MANAGER]    Script Date: 11/23/2015 21:56:30 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[PAT_IN_OUT_MANAGER]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[PAT_IN_OUT_MANAGER](
	[ID] [bigint] IDENTITY(1,1) NOT NULL,
	[PAT_ID] [varchar](20) NOT NULL,
	[DEPT_CODE] [varchar](10) NOT NULL,
	[IN_ITEM_CODE] [varchar](100) NULL,
	[IN_ITEM_NAME] [varchar](100) NULL,
	[IN_ITEM_VAL] [numeric](6, 1) NULL,
	[IN_ITEM_UNIT] [varchar](20) NULL,
	[OUT_ITEM_CODE] [varchar](100) NULL,
	[OUT_ITEM_NAME] [varchar](100) NULL,
	[OUT_ITEM_VAL] [numeric](6, 1) NULL,
	[OUT_ITEM_UNIT] [varchar](20) NULL,
	[CREATE_DATE] [datetime] NULL,
	[UPDATE_DATE] [datetime] NULL,
	[CREATE_USER_NAME] [varchar](20) NULL,
	[CREATE_USER_CODE] [varchar](20) NULL,
	[UPDATE_USER_CODE] [varchar](20) NULL,
	[UPDATE_USER_NAME] [varchar](20) NULL
) ON [PRIMARY]
END
GO
SET ANSI_PADDING OFF
GO
IF NOT EXISTS (SELECT * FROM ::fn_listextendedproperty(N'MS_Description' , N'SCHEMA',N'dbo', N'TABLE',N'PAT_IN_OUT_MANAGER', N'COLUMN',N'ID'))
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'主键，自增长' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_IN_OUT_MANAGER', @level2type=N'COLUMN',@level2name=N'ID'
GO
IF NOT EXISTS (SELECT * FROM ::fn_listextendedproperty(N'MS_Description' , N'SCHEMA',N'dbo', N'TABLE',N'PAT_IN_OUT_MANAGER', N'COLUMN',N'PAT_ID'))
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'患者Id' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_IN_OUT_MANAGER', @level2type=N'COLUMN',@level2name=N'PAT_ID'
GO
IF NOT EXISTS (SELECT * FROM ::fn_listextendedproperty(N'MS_Description' , N'SCHEMA',N'dbo', N'TABLE',N'PAT_IN_OUT_MANAGER', N'COLUMN',N'DEPT_CODE'))
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'部门code' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_IN_OUT_MANAGER', @level2type=N'COLUMN',@level2name=N'DEPT_CODE'
GO
IF NOT EXISTS (SELECT * FROM ::fn_listextendedproperty(N'MS_Description' , N'SCHEMA',N'dbo', N'TABLE',N'PAT_IN_OUT_MANAGER', N'COLUMN',N'IN_ITEM_CODE'))
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'入量code' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_IN_OUT_MANAGER', @level2type=N'COLUMN',@level2name=N'IN_ITEM_CODE'
GO
IF NOT EXISTS (SELECT * FROM ::fn_listextendedproperty(N'MS_Description' , N'SCHEMA',N'dbo', N'TABLE',N'PAT_IN_OUT_MANAGER', N'COLUMN',N'IN_ITEM_NAME'))
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'入量名称' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_IN_OUT_MANAGER', @level2type=N'COLUMN',@level2name=N'IN_ITEM_NAME'
GO
IF NOT EXISTS (SELECT * FROM ::fn_listextendedproperty(N'MS_Description' , N'SCHEMA',N'dbo', N'TABLE',N'PAT_IN_OUT_MANAGER', N'COLUMN',N'IN_ITEM_VAL'))
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'入量值' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_IN_OUT_MANAGER', @level2type=N'COLUMN',@level2name=N'IN_ITEM_VAL'
GO
IF NOT EXISTS (SELECT * FROM ::fn_listextendedproperty(N'MS_Description' , N'SCHEMA',N'dbo', N'TABLE',N'PAT_IN_OUT_MANAGER', N'COLUMN',N'IN_ITEM_UNIT'))
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'入量单位' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_IN_OUT_MANAGER', @level2type=N'COLUMN',@level2name=N'IN_ITEM_UNIT'
GO
IF NOT EXISTS (SELECT * FROM ::fn_listextendedproperty(N'MS_Description' , N'SCHEMA',N'dbo', N'TABLE',N'PAT_IN_OUT_MANAGER', N'COLUMN',N'OUT_ITEM_CODE'))
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'出量编码' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_IN_OUT_MANAGER', @level2type=N'COLUMN',@level2name=N'OUT_ITEM_CODE'
GO
IF NOT EXISTS (SELECT * FROM ::fn_listextendedproperty(N'MS_Description' , N'SCHEMA',N'dbo', N'TABLE',N'PAT_IN_OUT_MANAGER', N'COLUMN',N'OUT_ITEM_NAME'))
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'出量名称' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_IN_OUT_MANAGER', @level2type=N'COLUMN',@level2name=N'OUT_ITEM_NAME'
GO
IF NOT EXISTS (SELECT * FROM ::fn_listextendedproperty(N'MS_Description' , N'SCHEMA',N'dbo', N'TABLE',N'PAT_IN_OUT_MANAGER', N'COLUMN',N'OUT_ITEM_VAL'))
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'出量值' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_IN_OUT_MANAGER', @level2type=N'COLUMN',@level2name=N'OUT_ITEM_VAL'
GO
IF NOT EXISTS (SELECT * FROM ::fn_listextendedproperty(N'MS_Description' , N'SCHEMA',N'dbo', N'TABLE',N'PAT_IN_OUT_MANAGER', N'COLUMN',N'OUT_ITEM_UNIT'))
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'出量单位' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_IN_OUT_MANAGER', @level2type=N'COLUMN',@level2name=N'OUT_ITEM_UNIT'
GO
IF NOT EXISTS (SELECT * FROM ::fn_listextendedproperty(N'MS_Description' , N'SCHEMA',N'dbo', N'TABLE',N'PAT_IN_OUT_MANAGER', N'COLUMN',N'CREATE_DATE'))
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'创建时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_IN_OUT_MANAGER', @level2type=N'COLUMN',@level2name=N'CREATE_DATE'
GO
IF NOT EXISTS (SELECT * FROM ::fn_listextendedproperty(N'MS_Description' , N'SCHEMA',N'dbo', N'TABLE',N'PAT_IN_OUT_MANAGER', N'COLUMN',N'UPDATE_DATE'))
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'修改时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_IN_OUT_MANAGER', @level2type=N'COLUMN',@level2name=N'UPDATE_DATE'
GO
IF NOT EXISTS (SELECT * FROM ::fn_listextendedproperty(N'MS_Description' , N'SCHEMA',N'dbo', N'TABLE',N'PAT_IN_OUT_MANAGER', N'COLUMN',N'CREATE_USER_NAME'))
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'创建护士Name' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_IN_OUT_MANAGER', @level2type=N'COLUMN',@level2name=N'CREATE_USER_NAME'
GO
IF NOT EXISTS (SELECT * FROM ::fn_listextendedproperty(N'MS_Description' , N'SCHEMA',N'dbo', N'TABLE',N'PAT_IN_OUT_MANAGER', N'COLUMN',N'CREATE_USER_CODE'))
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'创建护士code' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_IN_OUT_MANAGER', @level2type=N'COLUMN',@level2name=N'CREATE_USER_CODE'
GO
IF NOT EXISTS (SELECT * FROM ::fn_listextendedproperty(N'MS_Description' , N'SCHEMA',N'dbo', N'TABLE',N'PAT_IN_OUT_MANAGER', N'COLUMN',N'UPDATE_USER_CODE'))
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'修改护士Code' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_IN_OUT_MANAGER', @level2type=N'COLUMN',@level2name=N'UPDATE_USER_CODE'
GO
IF NOT EXISTS (SELECT * FROM ::fn_listextendedproperty(N'MS_Description' , N'SCHEMA',N'dbo', N'TABLE',N'PAT_IN_OUT_MANAGER', N'COLUMN',N'UPDATE_USER_NAME'))
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'修改护士name' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_IN_OUT_MANAGER', @level2type=N'COLUMN',@level2name=N'UPDATE_USER_NAME'
GO

--体征明显表
alter table pat_bodysign_detail alter column measure_note_code varchar(20);

--护理记录单出量
 delete from DOC_METADATA where METADATA_CODE='out_name_13';
 insert into DOC_METADATA (METADATA_CODE,METADATA_NAME,METADATA_SIMPLE_NAME, 
PARENT_CODE,DATA_TYPE,ORD,REMARK 
) 
values 
( 
'out_name_13','脑脊液','脑脊液','out_name','OPT','13','一般护理记录单出量' 
) ;

--创建检验条码表
USE [MNIS_V3]
GO

/****** Object:  Table [dbo].[SYS_BARCODE_SYNC]    Script Date: 11/25/2015 15:55:54 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

SET ANSI_PADDING ON
GO

CREATE TABLE [dbo].[SYS_BARCODE_SYNC](
	[PAT_ID] [varchar](20) NOT NULL,
	[BARCODE] [varchar](20) NOT NULL,
	[HISCODE] [varchar](20) NOT NULL,
	[DBTYPE] [varchar](5) NOT NULL,
	[TUBECOLOR] [varchar](30) NULL,
	[SEND_DATE] [datetime] NULL,
	[SYNCTIME] [datetime] NOT NULL,
	[PNAME] [varchar](1000) NULL,
	[PCODE] [varchar](50) NULL,
 CONSTRAINT [PK_PAT_SYNC_BARCODE] PRIMARY KEY CLUSTERED 
(
	[PAT_ID] ASC,
	[HISCODE] ASC,
	[BARCODE] ASC,
	[DBTYPE] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]

GO

SET ANSI_PADDING OFF
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'患者id，住院号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'SYS_BARCODE_SYNC', @level2type=N'COLUMN',@level2name=N'PAT_ID'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'条码号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'SYS_BARCODE_SYNC', @level2type=N'COLUMN',@level2name=N'BARCODE'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'医院信息化对应条码' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'SYS_BARCODE_SYNC', @level2type=N'COLUMN',@level2name=N'HISCODE'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'数据来源:如HIS、LIS、EMR、PACS、包药机等' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'SYS_BARCODE_SYNC', @level2type=N'COLUMN',@level2name=N'DBTYPE'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'试管颜色' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'SYS_BARCODE_SYNC', @level2type=N'COLUMN',@level2name=N'TUBECOLOR'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'申请日期' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'SYS_BARCODE_SYNC', @level2type=N'COLUMN',@level2name=N'SEND_DATE'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'插入系统同步时间  只保存最近30天数据' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'SYS_BARCODE_SYNC', @level2type=N'COLUMN',@level2name=N'SYNCTIME'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'项目名称' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'SYS_BARCODE_SYNC', @level2type=N'COLUMN',@level2name=N'PNAME'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'项目编码' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'SYS_BARCODE_SYNC', @level2type=N'COLUMN',@level2name=N'PCODE'
GO

ALTER TABLE [dbo].[SYS_BARCODE_SYNC] ADD  CONSTRAINT [DF_PAT_SYNC_BARCODE_SYNCTIME]  DEFAULT (getdate()) FOR [SYNCTIME]
GO
--是否同步出入量
DELETE FROM SYS_CONFIG WHERE CONFIG_ID='isSyncOutInManager'
INSERT [dbo].[SYS_CONFIG] ([CONFIG_ID], [CONFIG_VALUE], [CONFIG_TYPE], [CONFIG_OWNER], [VALID_FLAG], [CONFIG_DESC]) VALUES ( N'isSyncInOutManager', N'1', N'S', NULL, N'1', N'是否同步出入量(0:否,1:是)')

DELETE FROM SYS_CONFIG WHERE CONFIG_ID='removeInfusionOrderUsageCodes'
INSERT [dbo].[SYS_CONFIG] ([CONFIG_ID], [CONFIG_VALUE], [CONFIG_TYPE], [CONFIG_OWNER], [VALID_FLAG], [CONFIG_DESC]) VALUES ( N'removeInfusionOrderUsageCodes', N'U,UZ,UT', N'S', NULL, N'1', N'医嘱入量转抄到护理文书,排除非药物用法')

--生命体征数据选择策略配置
ALTER TABLE SYS_CONFIG ALTER COLUMN CONFIG_DESC varchar(200);
DELETE FROM SYS_CONFIG WHERE CONFIG_ID='bodySignDataStrategy';
INSERT [dbo].[SYS_CONFIG] ( [CONFIG_ID], [CONFIG_VALUE], [CONFIG_TYPE], [CONFIG_OWNER], [VALID_FLAG], [CONFIG_DESC]) VALUES ( N'bodySignDataStrategy', N'2,0,3', N'S', N'', N'1', N'位置一:数据选择(0:最新数据,1:最开始数据,2:体温单点数据);位置二:数据时间点(0:时间点前4小时,1:前后2小时,2:后4小时);位置三:时间点选择(0:(0,4..),1:(1,5..),2:(2,6..),3:(3,7..))')

--检验类医嘱日期向前查询天数
insert into SYS_CONFIG(CONFIG_ID,CONFIG_VALUE,CONFIG_TYPE,CONFIG_OWNER,VALID_FLAG,CONFIG_DESC)
values ('labBeforeDay','3','S','',1,'检验类医嘱日期向前查询天数')
--体温单事件配置显示
DELETE FROM SYS_CONFIG WHERE CONFIG_ID='bodyTempEventSeparator';
INSERT INTO [dbo].[SYS_CONFIG]
           ([CONFIG_ID]
           ,[CONFIG_VALUE]
           ,[CONFIG_TYPE]
           ,[CONFIG_OWNER]
           ,[VALID_FLAG]
           ,[CONFIG_DESC])
     VALUES
           ('bodyTempEventSeparator','-','S','','1','体温单事件显示分隔符');
           
--生命体征每天一次的项目
DELETE FROM SYS_CONFIG WHERE CONFIG_ID='oncePerDayItems';
INSERT INTO [dbo].[SYS_CONFIG]
           ([CONFIG_ID],[CONFIG_VALUE],[CONFIG_TYPE],[CONFIG_OWNER],[VALID_FLAG],[CONFIG_DESC])
     VALUES
           ('oncePerDayItems','PPD,totalInput,stool,otherOutput,urine,weight,OtherOne,OtherTwo,bloodGlu,abdominalCir,other','S','','1','生命体征每天一次的项目')
           
--创建患者统计视图
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF NOT EXISTS (SELECT * FROM sys.views WHERE object_id = OBJECT_ID(N'[dbo].[PAT_STATISTIC]'))
EXEC dbo.sp_executesql @statement = N'create view [dbo].[PAT_STATISTIC] as
SELECT		COUNT(PAT_ID) AS inpatient_count, --病人总数
		SUM(CASE WHEN p.NURSE_LEVEL = 1 THEN 1 ELSE 0 END) AS tend_one_count, --一级护理
                SUM(CASE WHEN p.NURSE_LEVEL = 2 THEN 1 ELSE 0 END) AS tend_two_count, --二级护理
		SUM(CASE WHEN p.NURSE_LEVEL = 3 THEN 1 ELSE 0 END) AS tend_three_count, --三级护理
                SUM(CASE WHEN p.NURSE_LEVEL = 0 THEN 1 ELSE 0 END) AS tend_supe_count, --特级护理
		SUM(CASE WHEN p.IN_DATE > CONVERT(varchar(10), getdate(), 120) THEN 1 ELSE 0 END) AS new_count,--新患者 
		SUM(CASE WHEN (p.PREPAY_COST - OWN_COST) < 0 THEN 1 ELSE 0 END) AS dischargeCount,--欠费患者
                SUM(CASE WHEN  p.DANGER_LEVEL=''S'' THEN 1 ELSE 0 END) AS serious_count,--病重患者
                SUM(CASE WHEN  p.DANGER_LEVEL=''D'' THEN 1 ELSE 0 END) AS critical_count,--病危患者
                SUM(CASE WHEN  p.DANGER_LEVEL=''E'' THEN 1 ELSE 0 END) AS dead_count,--死亡患者
                WARD_CODE --部门
FROM         dbo.PAT_CURE_INFO AS p
WHERE     (STATUS = 1)
GROUP BY WARD_CODE;'
GO

--菜单项配置
IF  EXISTS (SELECT * FROM sys.views WHERE object_id = OBJECT_ID(N'[dbo].[PAT_STATISTIC]'))
DROP VIEW [dbo].[PAT_STATISTIC]
GO
/****** Object:  Table [dbo].[COM_MENU_CONFIG]    Script Date: 12/28/2015 16:01:03 ******/
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[COM_MENU_CONFIG]') AND type in (N'U'))
DROP TABLE [dbo].[COM_MENU_CONFIG]
GO
/****** Object:  Table [dbo].[COM_MENU_CONFIG]    Script Date: 12/28/2015 16:01:03 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[COM_MENU_CONFIG]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[COM_MENU_CONFIG](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[CODE] [varchar](30) NOT NULL,
	[NAME] [varchar](30) NULL,
	[PARENT_CODE] [varchar](30) NULL,
	[ORDER_NO] [int] NULL,
	[URL] [varchar](100) NULL,
	[IS_VALID] [char](1) NULL,
	[RESOURCE_URL] [varchar](100) NULL,
	[URL_TYPE] [char](1) NULL,
PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
SET ANSI_PADDING OFF
GO
IF NOT EXISTS (SELECT * FROM ::fn_listextendedproperty(N'MS_Description' , N'SCHEMA',N'dbo', N'TABLE',N'COM_MENU_CONFIG', N'COLUMN',N'ID'))
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'主键id' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'COM_MENU_CONFIG', @level2type=N'COLUMN',@level2name=N'ID'
GO
IF NOT EXISTS (SELECT * FROM ::fn_listextendedproperty(N'MS_Description' , N'SCHEMA',N'dbo', N'TABLE',N'COM_MENU_CONFIG', N'COLUMN',N'CODE'))
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'菜单code' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'COM_MENU_CONFIG', @level2type=N'COLUMN',@level2name=N'CODE'
GO
IF NOT EXISTS (SELECT * FROM ::fn_listextendedproperty(N'MS_Description' , N'SCHEMA',N'dbo', N'TABLE',N'COM_MENU_CONFIG', N'COLUMN',N'NAME'))
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'菜单名称' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'COM_MENU_CONFIG', @level2type=N'COLUMN',@level2name=N'NAME'
GO
IF NOT EXISTS (SELECT * FROM ::fn_listextendedproperty(N'MS_Description' , N'SCHEMA',N'dbo', N'TABLE',N'COM_MENU_CONFIG', N'COLUMN',N'PARENT_CODE'))
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'父菜单code' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'COM_MENU_CONFIG', @level2type=N'COLUMN',@level2name=N'PARENT_CODE'
GO
IF NOT EXISTS (SELECT * FROM ::fn_listextendedproperty(N'MS_Description' , N'SCHEMA',N'dbo', N'TABLE',N'COM_MENU_CONFIG', N'COLUMN',N'ORDER_NO'))
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'排序' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'COM_MENU_CONFIG', @level2type=N'COLUMN',@level2name=N'ORDER_NO'
GO
IF NOT EXISTS (SELECT * FROM ::fn_listextendedproperty(N'MS_Description' , N'SCHEMA',N'dbo', N'TABLE',N'COM_MENU_CONFIG', N'COLUMN',N'URL'))
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'目标url路径' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'COM_MENU_CONFIG', @level2type=N'COLUMN',@level2name=N'URL'
GO
IF NOT EXISTS (SELECT * FROM ::fn_listextendedproperty(N'MS_Description' , N'SCHEMA',N'dbo', N'TABLE',N'COM_MENU_CONFIG', N'COLUMN',N'IS_VALID'))
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'记录是否有效(1:有效,0：无效)' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'COM_MENU_CONFIG', @level2type=N'COLUMN',@level2name=N'IS_VALID'
GO
IF NOT EXISTS (SELECT * FROM ::fn_listextendedproperty(N'MS_Description' , N'SCHEMA',N'dbo', N'TABLE',N'COM_MENU_CONFIG', N'COLUMN',N'RESOURCE_URL'))
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'资源文件路径' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'COM_MENU_CONFIG', @level2type=N'COLUMN',@level2name=N'RESOURCE_URL'
GO
IF NOT EXISTS (SELECT * FROM ::fn_listextendedproperty(N'MS_Description' , N'SCHEMA',N'dbo', N'TABLE',N'COM_MENU_CONFIG', N'COLUMN',N'URL_TYPE'))
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'url类型(0:带参数类)' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'COM_MENU_CONFIG', @level2type=N'COLUMN',@level2name=N'URL_TYPE'
GO

--菜单数据初始化
SET IDENTITY_INSERT [dbo].[COM_MENU_CONFIG] ON
INSERT [dbo].[COM_MENU_CONFIG] ([ID], [CODE], [NAME], [PARENT_CODE], [ORDER_NO], [URL], [IS_VALID], [resource_url], [url_type]) VALUES (1, N'patientManager', N'病人一览', NULL, 1, NULL, N'1', NULL, NULL)
INSERT [dbo].[COM_MENU_CONFIG] ([ID], [CODE], [NAME], [PARENT_CODE], [ORDER_NO], [URL], [IS_VALID], [resource_url], [url_type]) VALUES (2, N'orderManager', N'医嘱管理', NULL, 2, NULL, N'1', NULL, NULL)
INSERT [dbo].[COM_MENU_CONFIG] ([ID], [CODE], [NAME], [PARENT_CODE], [ORDER_NO], [URL], [IS_VALID], [resource_url], [url_type]) VALUES (3, N'BodySignManager', N'生命体征', NULL, 3, NULL, N'1', NULL, NULL)
INSERT [dbo].[COM_MENU_CONFIG] ([ID], [CODE], [NAME], [PARENT_CODE], [ORDER_NO], [URL], [IS_VALID], [resource_url], [url_type]) VALUES (4, N'clinicReportManager', N'临床报告', NULL, 4, NULL, N'1', NULL, NULL)
INSERT [dbo].[COM_MENU_CONFIG] ([ID], [CODE], [NAME], [PARENT_CODE], [ORDER_NO], [URL], [IS_VALID], [resource_url], [url_type]) VALUES (5, N'nurseShitfManager', N'交班本', NULL, 5, N'/nur/shiftBook/shiftBookMain.do', N'0', NULL, NULL)
INSERT [dbo].[COM_MENU_CONFIG] ([ID], [CODE], [NAME], [PARENT_CODE], [ORDER_NO], [URL], [IS_VALID], [resource_url], [url_type]) VALUES (6, N'barcodeManager', N'条码打印', NULL, 6, NULL, N'1', NULL, NULL)
INSERT [dbo].[COM_MENU_CONFIG] ([ID], [CODE], [NAME], [PARENT_CODE], [ORDER_NO], [URL], [IS_VALID], [resource_url], [url_type]) VALUES (7, N'infusionManager', N'输液监控', NULL, 7, N'/nur/infusionmanager/getInfusionTimer.do', N'0', NULL, NULL)
INSERT [dbo].[COM_MENU_CONFIG] ([ID], [CODE], [NAME], [PARENT_CODE], [ORDER_NO], [URL], [IS_VALID], [resource_url], [url_type]) VALUES (8, N'moreManager', N'更多', NULL, 8, NULL, N'1', NULL, NULL)
INSERT [dbo].[COM_MENU_CONFIG] ([ID], [CODE], [NAME], [PARENT_CODE], [ORDER_NO], [URL], [IS_VALID], [resource_url], [url_type]) VALUES (9, N'bedListSubMenu', N'床位列表', N'patientManager', 1, N'/nur/patientGlance/patientGlanceMain.do', N'1', NULL, NULL)
INSERT [dbo].[COM_MENU_CONFIG] ([ID], [CODE], [NAME], [PARENT_CODE], [ORDER_NO], [URL], [IS_VALID], [resource_url], [url_type]) VALUES (10, N'outDeptSubMenu', N'出院转科', N'patientManager', 2, N'/index/patientMain2.do', N'1', NULL, NULL)
INSERT [dbo].[COM_MENU_CONFIG] ([ID], [CODE], [NAME], [PARENT_CODE], [ORDER_NO], [URL], [IS_VALID], [resource_url], [url_type]) VALUES (11, N'prescriptionAllManager', N'医嘱查看', N'orderManager', 1, N'/index/nurseMain.do?menuId=prescriptionAllManager', N'1', N'/resources/js/nur/prescriptionAllMenu.js', NULL)
INSERT [dbo].[COM_MENU_CONFIG] ([ID], [CODE], [NAME], [PARENT_CODE], [ORDER_NO], [URL], [IS_VALID], [resource_url], [url_type]) VALUES (12, N'prescriptionUnExeManager', N'未执行医嘱', N'orderManager', 2, N'/index/nurseMain.do?menuId=prescriptionUnExeManager', N'1', N'/resources/js/nur/prescriptionUnExeMenu.js', NULL)
INSERT [dbo].[COM_MENU_CONFIG] ([ID], [CODE], [NAME], [PARENT_CODE], [ORDER_NO], [URL], [IS_VALID], [resource_url], [url_type]) VALUES (13, N'bodyTempSheetManager', N'体温单批量录入', N'BodySignManager', 1, N'/index/nurseMain.do?menuId=bodyTempSheetManager', N'1', N'/resources/js/nur/bodyTempSheetMenu.js', NULL)
INSERT [dbo].[COM_MENU_CONFIG] ([ID], [CODE], [NAME], [PARENT_CODE], [ORDER_NO], [URL], [IS_VALID], [resource_url], [url_type]) VALUES (14, N'goTempSheetManager', N'体温单文书', N'BodySignManager', 2, N'/index/patientMain.do?id=', N'1', NULL, N'0')
INSERT [dbo].[COM_MENU_CONFIG] ([ID], [CODE], [NAME], [PARENT_CODE], [ORDER_NO], [URL], [IS_VALID], [resource_url], [url_type]) VALUES (15, N'labTestRecordManager', N'检验报告', N'clinicReportManager', 1, N'/index/nurseMain.do?menuId=labTestRecordManager', N'1', N'/resources/js/nur/labTestRecordMenu.js', NULL)
INSERT [dbo].[COM_MENU_CONFIG] ([ID], [CODE], [NAME], [PARENT_CODE], [ORDER_NO], [URL], [IS_VALID], [resource_url], [url_type]) VALUES (16, N'inspectionRptMainManager', N'检查报告', N'clinicReportManager', 2, N'/index/nurseMain.do?menuId=inspectionRptMainManager', N'1', N'/resources/js/nur/inspectionRptMainMenu.js', NULL)
INSERT [dbo].[COM_MENU_CONFIG] ([ID], [CODE], [NAME], [PARENT_CODE], [ORDER_NO], [URL], [IS_VALID], [resource_url], [url_type]) VALUES (17, N'patCrisValueManager', N'危急值', N'clinicReportManager', 3, N'/index/nurseMain.do?menuId=patCrisValueManager', N'1', N'/resources/js/nur/patCrisValueMenu.js', NULL)
INSERT [dbo].[COM_MENU_CONFIG] ([ID], [CODE], [NAME], [PARENT_CODE], [ORDER_NO], [URL], [IS_VALID], [resource_url], [url_type]) VALUES (18, N'wristBarcodeManager', N'打印腕带', N'barcodeManager', 1, N'/index/nurseMain.do?menuId=wristBarcodeManager', N'1', N'/resources/js/nur/wristBarcodeMenu.js', NULL)
INSERT [dbo].[COM_MENU_CONFIG] ([ID], [CODE], [NAME], [PARENT_CODE], [ORDER_NO], [URL], [IS_VALID], [resource_url], [url_type]) VALUES (19, N'labelBarcodeManager', N'打印瓶签', N'barcodeManager', 2, N'/index/nurseMain.do?menuId=labelBarcodeManager', N'1', N'/resources/js/nur/labelBarcodeMenu.js', NULL)
INSERT [dbo].[COM_MENU_CONFIG] ([ID], [CODE], [NAME], [PARENT_CODE], [ORDER_NO], [URL], [IS_VALID], [resource_url], [url_type]) VALUES (20, N'infusionDevicesSubMenu', N'设备管理', N'moreManager', 1, N'/nur/infusionmanager/showInfusionDevices.do', N'0', NULL, NULL)
INSERT [dbo].[COM_MENU_CONFIG] ([ID], [CODE], [NAME], [PARENT_CODE], [ORDER_NO], [URL], [IS_VALID], [resource_url], [url_type]) VALUES (21, N'infusionStatisticSubMenu', N'统计查询', N'moreManager', 2, N'/nur/infusionmanager/showStatisticQuery.do', N'0', NULL, NULL)
INSERT [dbo].[COM_MENU_CONFIG] ([ID], [CODE], [NAME], [PARENT_CODE], [ORDER_NO], [URL], [IS_VALID], [resource_url], [url_type]) VALUES (22, N'infusionAlarmRecordSubMenu', N'报警记录', N'moreManager', 3, N'/nur/infusionmanager/showAlarmRecord.do', N'0', NULL, NULL)
INSERT [dbo].[COM_MENU_CONFIG] ([ID], [CODE], [NAME], [PARENT_CODE], [ORDER_NO], [URL], [IS_VALID], [resource_url], [url_type]) VALUES (25, N'nurseWhiteBoardManager', N'编辑小白版', N'moreManager', 4, N'/nur/nurseWhiteBoard/nurseWhiteBoardMain.do', N'1', NULL, NULL)
SET IDENTITY_INSERT [dbo].[COM_MENU_CONFIG] OFF


---小白板元数据表
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[NURSE_WHITE_BOARD_METADATA]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[NURSE_WHITE_BOARD_METADATA](
	[ID] [bigint] IDENTITY(1,1) NOT NULL,
	[CODE] [varchar](20) NOT NULL,
	[NAME] [varchar](30) NULL,
	[PARENT_ID] [varchar](20) NULL,
	[LEVEL] [int] NULL,
	[INPUT_TYPE] [varchar](20) NULL,
	[CREATE_DATE] [datetime] NULL,
	[DEPT_CODE] [varchar](20) NULL,
	[IS_AUTO] [char](1) NULL DEFAULT ('0'),
	[IS_SHOW] [char](1) NULL DEFAULT ('1'),
	[SHOW_TITLE] [varchar](50) NULL,
	[IS_EXEC] [char](1) NULL DEFAULT ((1)),
PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
SET ANSI_PADDING OFF
GO
IF NOT EXISTS (SELECT * FROM ::fn_listextendedproperty(N'MS_Description' , N'SCHEMA',N'dbo', N'TABLE',N'NURSE_WHITE_BOARD_METADATA', N'COLUMN',N'ID'))
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'主键id' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_WHITE_BOARD_METADATA', @level2type=N'COLUMN',@level2name=N'ID'
GO
IF NOT EXISTS (SELECT * FROM ::fn_listextendedproperty(N'MS_Description' , N'SCHEMA',N'dbo', N'TABLE',N'NURSE_WHITE_BOARD_METADATA', N'COLUMN',N'CODE'))
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'元数据CODE' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_WHITE_BOARD_METADATA', @level2type=N'COLUMN',@level2name=N'CODE'
GO
IF NOT EXISTS (SELECT * FROM ::fn_listextendedproperty(N'MS_Description' , N'SCHEMA',N'dbo', N'TABLE',N'NURSE_WHITE_BOARD_METADATA', N'COLUMN',N'NAME'))
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'元数据NAME' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_WHITE_BOARD_METADATA', @level2type=N'COLUMN',@level2name=N'NAME'
GO
IF NOT EXISTS (SELECT * FROM ::fn_listextendedproperty(N'MS_Description' , N'SCHEMA',N'dbo', N'TABLE',N'NURSE_WHITE_BOARD_METADATA', N'COLUMN',N'PARENT_ID'))
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'父主键Id' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_WHITE_BOARD_METADATA', @level2type=N'COLUMN',@level2name=N'PARENT_ID'
GO
IF NOT EXISTS (SELECT * FROM ::fn_listextendedproperty(N'MS_Description' , N'SCHEMA',N'dbo', N'TABLE',N'NURSE_WHITE_BOARD_METADATA', N'COLUMN',N'LEVEL'))
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'深度' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_WHITE_BOARD_METADATA', @level2type=N'COLUMN',@level2name=N'LEVEL'
GO
IF NOT EXISTS (SELECT * FROM ::fn_listextendedproperty(N'MS_Description' , N'SCHEMA',N'dbo', N'TABLE',N'NURSE_WHITE_BOARD_METADATA', N'COLUMN',N'INPUT_TYPE'))
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'输入类型' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_WHITE_BOARD_METADATA', @level2type=N'COLUMN',@level2name=N'INPUT_TYPE'
GO
IF NOT EXISTS (SELECT * FROM ::fn_listextendedproperty(N'MS_Description' , N'SCHEMA',N'dbo', N'TABLE',N'NURSE_WHITE_BOARD_METADATA', N'COLUMN',N'CREATE_DATE'))
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'创建时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_WHITE_BOARD_METADATA', @level2type=N'COLUMN',@level2name=N'CREATE_DATE'
GO
IF NOT EXISTS (SELECT * FROM ::fn_listextendedproperty(N'MS_Description' , N'SCHEMA',N'dbo', N'TABLE',N'NURSE_WHITE_BOARD_METADATA', N'COLUMN',N'DEPT_CODE'))
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'部门' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_WHITE_BOARD_METADATA', @level2type=N'COLUMN',@level2name=N'DEPT_CODE'
GO
IF NOT EXISTS (SELECT * FROM ::fn_listextendedproperty(N'MS_Description' , N'SCHEMA',N'dbo', N'TABLE',N'NURSE_WHITE_BOARD_METADATA', N'COLUMN',N'IS_AUTO'))
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'是否自动加载' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_WHITE_BOARD_METADATA', @level2type=N'COLUMN',@level2name=N'IS_AUTO'
GO
IF NOT EXISTS (SELECT * FROM ::fn_listextendedproperty(N'MS_Description' , N'SCHEMA',N'dbo', N'TABLE',N'NURSE_WHITE_BOARD_METADATA', N'COLUMN',N'IS_SHOW'))
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'是否显示' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_WHITE_BOARD_METADATA', @level2type=N'COLUMN',@level2name=N'IS_SHOW'
GO
IF NOT EXISTS (SELECT * FROM ::fn_listextendedproperty(N'MS_Description' , N'SCHEMA',N'dbo', N'TABLE',N'NURSE_WHITE_BOARD_METADATA', N'COLUMN',N'SHOW_TITLE'))
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'显示标题' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_WHITE_BOARD_METADATA', @level2type=N'COLUMN',@level2name=N'SHOW_TITLE'
GO
--小白板元数据值表
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[NURSE_WHITE_BOARD_METADATA_VALUE]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[NURSE_WHITE_BOARD_METADATA_VALUE](
	[MV_ID] [bigint] IDENTITY(1,1) NOT NULL,
	[MV_CODE] [varchar](20) NOT NULL,
	[MV_NAME] [varchar](30) NULL,
	[MV_VALUE] [varchar](50) NULL,
	[MV_REMARK] [varchar](100) NULL,
	[IS_VALID] [char](1) NULL,
PRIMARY KEY CLUSTERED 
(
	[MV_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
SET ANSI_PADDING OFF
GO
IF NOT EXISTS (SELECT * FROM ::fn_listextendedproperty(N'MS_Description' , N'SCHEMA',N'dbo', N'TABLE',N'NURSE_WHITE_BOARD_METADATA_VALUE', N'COLUMN',N'MV_ID'))
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'主键id' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_WHITE_BOARD_METADATA_VALUE', @level2type=N'COLUMN',@level2name=N'MV_ID'
GO
IF NOT EXISTS (SELECT * FROM ::fn_listextendedproperty(N'MS_Description' , N'SCHEMA',N'dbo', N'TABLE',N'NURSE_WHITE_BOARD_METADATA_VALUE', N'COLUMN',N'MV_CODE'))
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'元数据name' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_WHITE_BOARD_METADATA_VALUE', @level2type=N'COLUMN',@level2name=N'MV_CODE'
GO
IF NOT EXISTS (SELECT * FROM ::fn_listextendedproperty(N'MS_Description' , N'SCHEMA',N'dbo', N'TABLE',N'NURSE_WHITE_BOARD_METADATA_VALUE', N'COLUMN',N'MV_NAME'))
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'元数据code' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_WHITE_BOARD_METADATA_VALUE', @level2type=N'COLUMN',@level2name=N'MV_NAME'
GO
IF NOT EXISTS (SELECT * FROM ::fn_listextendedproperty(N'MS_Description' , N'SCHEMA',N'dbo', N'TABLE',N'NURSE_WHITE_BOARD_METADATA_VALUE', N'COLUMN',N'MV_VALUE'))
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'值' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_WHITE_BOARD_METADATA_VALUE', @level2type=N'COLUMN',@level2name=N'MV_VALUE'
GO
IF NOT EXISTS (SELECT * FROM ::fn_listextendedproperty(N'MS_Description' , N'SCHEMA',N'dbo', N'TABLE',N'NURSE_WHITE_BOARD_METADATA_VALUE', N'COLUMN',N'MV_REMARK'))
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'值说明' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_WHITE_BOARD_METADATA_VALUE', @level2type=N'COLUMN',@level2name=N'MV_REMARK'
GO
--小白板记录子项表
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[NURSE_WHITE_BOARD_RECORD_ITEM]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[NURSE_WHITE_BOARD_RECORD_ITEM](
	[ITEM_ID] [bigint] IDENTITY(1,1) NOT NULL,
	[RECORD_ID] [bigint] NOT NULL,
	[ITEM_CODE] [varchar](20) NOT NULL,
	[ITEM_NAME] [varchar](30) NULL,
	[ITEM_VALUE] [varchar](200) NULL,
	[EXEC_DATE] [datetime] NULL,
	[ITEM_DATE] [datetime] NULL,
	[ITEM_PAT_ID] [varchar](30) NULL,
	[ITEM_PAT_INFO] [varchar](50) NULL,
	[IS_FINISH] [char](1) NULL DEFAULT ((0)),
	[END_ITEM_DATE] [datetime] NULL,
	[START_ITEM_DATE] [datetime] NULL,
	[IS_VALID] [char](1) NULL,
PRIMARY KEY CLUSTERED 
(
	[ITEM_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
SET ANSI_PADDING OFF
GO
IF NOT EXISTS (SELECT * FROM ::fn_listextendedproperty(N'MS_Description' , N'SCHEMA',N'dbo', N'TABLE',N'NURSE_WHITE_BOARD_RECORD_ITEM', N'COLUMN',N'ITEM_ID'))
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'主键id' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_WHITE_BOARD_RECORD_ITEM', @level2type=N'COLUMN',@level2name=N'ITEM_ID'
GO
IF NOT EXISTS (SELECT * FROM ::fn_listextendedproperty(N'MS_Description' , N'SCHEMA',N'dbo', N'TABLE',N'NURSE_WHITE_BOARD_RECORD_ITEM', N'COLUMN',N'RECORD_ID'))
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'关联记录id' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_WHITE_BOARD_RECORD_ITEM', @level2type=N'COLUMN',@level2name=N'RECORD_ID'
GO
IF NOT EXISTS (SELECT * FROM ::fn_listextendedproperty(N'MS_Description' , N'SCHEMA',N'dbo', N'TABLE',N'NURSE_WHITE_BOARD_RECORD_ITEM', N'COLUMN',N'ITEM_CODE'))
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'子项记录code' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_WHITE_BOARD_RECORD_ITEM', @level2type=N'COLUMN',@level2name=N'ITEM_CODE'
GO
IF NOT EXISTS (SELECT * FROM ::fn_listextendedproperty(N'MS_Description' , N'SCHEMA',N'dbo', N'TABLE',N'NURSE_WHITE_BOARD_RECORD_ITEM', N'COLUMN',N'ITEM_NAME'))
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'子项记录name' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_WHITE_BOARD_RECORD_ITEM', @level2type=N'COLUMN',@level2name=N'ITEM_NAME'
GO
IF NOT EXISTS (SELECT * FROM ::fn_listextendedproperty(N'MS_Description' , N'SCHEMA',N'dbo', N'TABLE',N'NURSE_WHITE_BOARD_RECORD_ITEM', N'COLUMN',N'ITEM_VALUE'))
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'z子项记录值' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_WHITE_BOARD_RECORD_ITEM', @level2type=N'COLUMN',@level2name=N'ITEM_VALUE'
GO
IF NOT EXISTS (SELECT * FROM ::fn_listextendedproperty(N'MS_Description' , N'SCHEMA',N'dbo', N'TABLE',N'NURSE_WHITE_BOARD_RECORD_ITEM', N'COLUMN',N'EXEC_DATE'))
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'子项执行时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_WHITE_BOARD_RECORD_ITEM', @level2type=N'COLUMN',@level2name=N'EXEC_DATE'
GO
IF NOT EXISTS (SELECT * FROM ::fn_listextendedproperty(N'MS_Description' , N'SCHEMA',N'dbo', N'TABLE',N'NURSE_WHITE_BOARD_RECORD_ITEM', N'COLUMN',N'ITEM_DATE'))
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'子项记录日期' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_WHITE_BOARD_RECORD_ITEM', @level2type=N'COLUMN',@level2name=N'ITEM_DATE'
GO
IF NOT EXISTS (SELECT * FROM ::fn_listextendedproperty(N'MS_Description' , N'SCHEMA',N'dbo', N'TABLE',N'NURSE_WHITE_BOARD_RECORD_ITEM', N'COLUMN',N'ITEM_PAT_ID'))
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'患者ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_WHITE_BOARD_RECORD_ITEM', @level2type=N'COLUMN',@level2name=N'ITEM_PAT_ID'
GO
IF NOT EXISTS (SELECT * FROM ::fn_listextendedproperty(N'MS_Description' , N'SCHEMA',N'dbo', N'TABLE',N'NURSE_WHITE_BOARD_RECORD_ITEM', N'COLUMN',N'ITEM_PAT_INFO'))
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'患者信息' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_WHITE_BOARD_RECORD_ITEM', @level2type=N'COLUMN',@level2name=N'ITEM_PAT_INFO'
GO
IF NOT EXISTS (SELECT * FROM ::fn_listextendedproperty(N'MS_Description' , N'SCHEMA',N'dbo', N'TABLE',N'NURSE_WHITE_BOARD_RECORD_ITEM', N'COLUMN',N'IS_FINISH'))
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'是否完成' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_WHITE_BOARD_RECORD_ITEM', @level2type=N'COLUMN',@level2name=N'IS_FINISH'
GO
IF NOT EXISTS (SELECT * FROM ::fn_listextendedproperty(N'MS_Description' , N'SCHEMA',N'dbo', N'TABLE',N'NURSE_WHITE_BOARD_RECORD_ITEM', N'COLUMN',N'END_ITEM_DATE'))
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'结束提醒时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_WHITE_BOARD_RECORD_ITEM', @level2type=N'COLUMN',@level2name=N'END_ITEM_DATE'
GO
IF NOT EXISTS (SELECT * FROM ::fn_listextendedproperty(N'MS_Description' , N'SCHEMA',N'dbo', N'TABLE',N'NURSE_WHITE_BOARD_RECORD_ITEM', N'COLUMN',N'START_ITEM_DATE'))
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'开始提醒时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_WHITE_BOARD_RECORD_ITEM', @level2type=N'COLUMN',@level2name=N'START_ITEM_DATE'
GO
--小白板记录表 
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[NURSE_WHITE_BOARD_RECORD]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[NURSE_WHITE_BOARD_RECORD](
	[RECORD_ID] [bigint] IDENTITY(1,1) NOT NULL,
	[DEPT_CODE] [varchar](20) NULL,
	[RECORD_CODE] [varchar](20) NOT NULL,
	[RECORD_NAME] [varchar](30) NULL,
	[RECORD_VALUE] [varchar](500) NULL,
	[ORDER_NO] [int] NULL,
	[PAT_ID] [varchar](30) NULL,
	[RECORD_DATE] [datetime] NULL,
	[RECORD_USER_CODE] [varchar](20) NOT NULL,
	[RECORD_USER_NAME] [varchar](20) NOT NULL,
	[PAT_INFO] [varchar](50) NULL,
	[IS_VALID] [char](1) NULL,
	[PERFORM_SCHEDULE] [varchar](50) NULL,
PRIMARY KEY CLUSTERED 
(
	[RECORD_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
SET ANSI_PADDING OFF
GO
IF NOT EXISTS (SELECT * FROM ::fn_listextendedproperty(N'MS_Description' , N'SCHEMA',N'dbo', N'TABLE',N'NURSE_WHITE_BOARD_RECORD', N'COLUMN',N'RECORD_ID'))
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'主键id' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_WHITE_BOARD_RECORD', @level2type=N'COLUMN',@level2name=N'RECORD_ID'
GO
IF NOT EXISTS (SELECT * FROM ::fn_listextendedproperty(N'MS_Description' , N'SCHEMA',N'dbo', N'TABLE',N'NURSE_WHITE_BOARD_RECORD', N'COLUMN',N'DEPT_CODE'))
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'部门code' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_WHITE_BOARD_RECORD', @level2type=N'COLUMN',@level2name=N'DEPT_CODE'
GO
IF NOT EXISTS (SELECT * FROM ::fn_listextendedproperty(N'MS_Description' , N'SCHEMA',N'dbo', N'TABLE',N'NURSE_WHITE_BOARD_RECORD', N'COLUMN',N'RECORD_CODE'))
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'记录code' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_WHITE_BOARD_RECORD', @level2type=N'COLUMN',@level2name=N'RECORD_CODE'
GO
IF NOT EXISTS (SELECT * FROM ::fn_listextendedproperty(N'MS_Description' , N'SCHEMA',N'dbo', N'TABLE',N'NURSE_WHITE_BOARD_RECORD', N'COLUMN',N'RECORD_NAME'))
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'记录name' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_WHITE_BOARD_RECORD', @level2type=N'COLUMN',@level2name=N'RECORD_NAME'
GO
IF NOT EXISTS (SELECT * FROM ::fn_listextendedproperty(N'MS_Description' , N'SCHEMA',N'dbo', N'TABLE',N'NURSE_WHITE_BOARD_RECORD', N'COLUMN',N'RECORD_VALUE'))
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'记录值' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_WHITE_BOARD_RECORD', @level2type=N'COLUMN',@level2name=N'RECORD_VALUE'
GO
IF NOT EXISTS (SELECT * FROM ::fn_listextendedproperty(N'MS_Description' , N'SCHEMA',N'dbo', N'TABLE',N'NURSE_WHITE_BOARD_RECORD', N'COLUMN',N'ORDER_NO'))
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'记录排序' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_WHITE_BOARD_RECORD', @level2type=N'COLUMN',@level2name=N'ORDER_NO'
GO
IF NOT EXISTS (SELECT * FROM ::fn_listextendedproperty(N'MS_Description' , N'SCHEMA',N'dbo', N'TABLE',N'NURSE_WHITE_BOARD_RECORD', N'COLUMN',N'PAT_ID'))
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'患者ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_WHITE_BOARD_RECORD', @level2type=N'COLUMN',@level2name=N'PAT_ID'
GO
IF NOT EXISTS (SELECT * FROM ::fn_listextendedproperty(N'MS_Description' , N'SCHEMA',N'dbo', N'TABLE',N'NURSE_WHITE_BOARD_RECORD', N'COLUMN',N'RECORD_DATE'))
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'记录时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_WHITE_BOARD_RECORD', @level2type=N'COLUMN',@level2name=N'RECORD_DATE'
GO
IF NOT EXISTS (SELECT * FROM ::fn_listextendedproperty(N'MS_Description' , N'SCHEMA',N'dbo', N'TABLE',N'NURSE_WHITE_BOARD_RECORD', N'COLUMN',N'RECORD_USER_CODE'))
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'记录护士code' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_WHITE_BOARD_RECORD', @level2type=N'COLUMN',@level2name=N'RECORD_USER_CODE'
GO
IF NOT EXISTS (SELECT * FROM ::fn_listextendedproperty(N'MS_Description' , N'SCHEMA',N'dbo', N'TABLE',N'NURSE_WHITE_BOARD_RECORD', N'COLUMN',N'RECORD_USER_NAME'))
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'记录护士name' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_WHITE_BOARD_RECORD', @level2type=N'COLUMN',@level2name=N'RECORD_USER_NAME'
GO
--小白板元数据初始化
SET IDENTITY_INSERT [dbo].[NURSE_WHITE_BOARD_METADATA_VALUE] ON
INSERT [dbo].[NURSE_WHITE_BOARD_METADATA_VALUE] ([MV_ID], [MV_CODE], [MV_NAME], [MV_VALUE], [MV_REMARK], [IS_VALID]) VALUES (1, N'freq', N'频次', N'Qd', N'一天一次', N'1')
INSERT [dbo].[NURSE_WHITE_BOARD_METADATA_VALUE] ([MV_ID], [MV_CODE], [MV_NAME], [MV_VALUE], [MV_REMARK], [IS_VALID]) VALUES (2, N'freq', N'频次', N'Bid', N'一天两次', N'1')
INSERT [dbo].[NURSE_WHITE_BOARD_METADATA_VALUE] ([MV_ID], [MV_CODE], [MV_NAME], [MV_VALUE], [MV_REMARK], [IS_VALID]) VALUES (3, N'freq', N'频次', N'Tid', N'一天三次', N'1')
INSERT [dbo].[NURSE_WHITE_BOARD_METADATA_VALUE] ([MV_ID], [MV_CODE], [MV_NAME], [MV_VALUE], [MV_REMARK], [IS_VALID]) VALUES (4, N'freq', N'频次', N'Q4h', N'四小时一次', N'1')
INSERT [dbo].[NURSE_WHITE_BOARD_METADATA_VALUE] ([MV_ID], [MV_CODE], [MV_NAME], [MV_VALUE], [MV_REMARK], [IS_VALID]) VALUES (5, N'freq', N'频次', N'Q8h', N'八小时一次', N'1')
INSERT [dbo].[NURSE_WHITE_BOARD_METADATA_VALUE] ([MV_ID], [MV_CODE], [MV_NAME], [MV_VALUE], [MV_REMARK], [IS_VALID]) VALUES (6, N'time', N'时间', N'0时', N'00:00', N'0')
INSERT [dbo].[NURSE_WHITE_BOARD_METADATA_VALUE] ([MV_ID], [MV_CODE], [MV_NAME], [MV_VALUE], [MV_REMARK], [IS_VALID]) VALUES (7, N'time', N'时间', N'1时', N'1:00', N'0')
INSERT [dbo].[NURSE_WHITE_BOARD_METADATA_VALUE] ([MV_ID], [MV_CODE], [MV_NAME], [MV_VALUE], [MV_REMARK], [IS_VALID]) VALUES (8, N'time', N'时间', N'2时', N'2:00', N'0')
INSERT [dbo].[NURSE_WHITE_BOARD_METADATA_VALUE] ([MV_ID], [MV_CODE], [MV_NAME], [MV_VALUE], [MV_REMARK], [IS_VALID]) VALUES (9, N'time', N'时间', N'3时', N'3:00', N'0')
INSERT [dbo].[NURSE_WHITE_BOARD_METADATA_VALUE] ([MV_ID], [MV_CODE], [MV_NAME], [MV_VALUE], [MV_REMARK], [IS_VALID]) VALUES (10, N'time', N'时间', N'4时', N'4:00', N'0')
INSERT [dbo].[NURSE_WHITE_BOARD_METADATA_VALUE] ([MV_ID], [MV_CODE], [MV_NAME], [MV_VALUE], [MV_REMARK], [IS_VALID]) VALUES (11, N'time', N'时间', N'5时', N'5:00', N'0')
INSERT [dbo].[NURSE_WHITE_BOARD_METADATA_VALUE] ([MV_ID], [MV_CODE], [MV_NAME], [MV_VALUE], [MV_REMARK], [IS_VALID]) VALUES (12, N'time', N'时间', N'6时', N'6:00', N'0')
INSERT [dbo].[NURSE_WHITE_BOARD_METADATA_VALUE] ([MV_ID], [MV_CODE], [MV_NAME], [MV_VALUE], [MV_REMARK], [IS_VALID]) VALUES (13, N'time', N'时间', N'7时', N'7:00', N'0')
INSERT [dbo].[NURSE_WHITE_BOARD_METADATA_VALUE] ([MV_ID], [MV_CODE], [MV_NAME], [MV_VALUE], [MV_REMARK], [IS_VALID]) VALUES (14, N'time', N'时间', N'8时', N'8:00', N'0')
INSERT [dbo].[NURSE_WHITE_BOARD_METADATA_VALUE] ([MV_ID], [MV_CODE], [MV_NAME], [MV_VALUE], [MV_REMARK], [IS_VALID]) VALUES (15, N'time', N'时间', N'9时', N'9:00', N'0')
INSERT [dbo].[NURSE_WHITE_BOARD_METADATA_VALUE] ([MV_ID], [MV_CODE], [MV_NAME], [MV_VALUE], [MV_REMARK], [IS_VALID]) VALUES (16, N'time', N'时间', N'10时', N'10:00', N'0')
INSERT [dbo].[NURSE_WHITE_BOARD_METADATA_VALUE] ([MV_ID], [MV_CODE], [MV_NAME], [MV_VALUE], [MV_REMARK], [IS_VALID]) VALUES (17, N'time', N'时间', N'11时', N'11:00', N'0')
INSERT [dbo].[NURSE_WHITE_BOARD_METADATA_VALUE] ([MV_ID], [MV_CODE], [MV_NAME], [MV_VALUE], [MV_REMARK], [IS_VALID]) VALUES (18, N'time', N'时间', N'12时', N'12:00', N'0')
INSERT [dbo].[NURSE_WHITE_BOARD_METADATA_VALUE] ([MV_ID], [MV_CODE], [MV_NAME], [MV_VALUE], [MV_REMARK], [IS_VALID]) VALUES (19, N'time', N'时间', N'13时', N'13:00', N'0')
INSERT [dbo].[NURSE_WHITE_BOARD_METADATA_VALUE] ([MV_ID], [MV_CODE], [MV_NAME], [MV_VALUE], [MV_REMARK], [IS_VALID]) VALUES (20, N'time', N'时间', N'14时', N'14:00', N'0')
INSERT [dbo].[NURSE_WHITE_BOARD_METADATA_VALUE] ([MV_ID], [MV_CODE], [MV_NAME], [MV_VALUE], [MV_REMARK], [IS_VALID]) VALUES (21, N'time', N'时间', N'15时', N'15:00', N'0')
INSERT [dbo].[NURSE_WHITE_BOARD_METADATA_VALUE] ([MV_ID], [MV_CODE], [MV_NAME], [MV_VALUE], [MV_REMARK], [IS_VALID]) VALUES (22, N'time', N'时间', N'16时', N'16:00', N'0')
INSERT [dbo].[NURSE_WHITE_BOARD_METADATA_VALUE] ([MV_ID], [MV_CODE], [MV_NAME], [MV_VALUE], [MV_REMARK], [IS_VALID]) VALUES (23, N'time', N'时间', N'17时', N'17:00', N'0')
INSERT [dbo].[NURSE_WHITE_BOARD_METADATA_VALUE] ([MV_ID], [MV_CODE], [MV_NAME], [MV_VALUE], [MV_REMARK], [IS_VALID]) VALUES (24, N'time', N'时间', N'18时', N'18:00', N'0')
INSERT [dbo].[NURSE_WHITE_BOARD_METADATA_VALUE] ([MV_ID], [MV_CODE], [MV_NAME], [MV_VALUE], [MV_REMARK], [IS_VALID]) VALUES (25, N'time', N'时间', N'19时', N'19:00', N'0')
INSERT [dbo].[NURSE_WHITE_BOARD_METADATA_VALUE] ([MV_ID], [MV_CODE], [MV_NAME], [MV_VALUE], [MV_REMARK], [IS_VALID]) VALUES (26, N'time', N'时间', N'20时', N'20:00', N'0')
INSERT [dbo].[NURSE_WHITE_BOARD_METADATA_VALUE] ([MV_ID], [MV_CODE], [MV_NAME], [MV_VALUE], [MV_REMARK], [IS_VALID]) VALUES (27, N'time', N'时间', N'21时', N'21:00', N'0')
INSERT [dbo].[NURSE_WHITE_BOARD_METADATA_VALUE] ([MV_ID], [MV_CODE], [MV_NAME], [MV_VALUE], [MV_REMARK], [IS_VALID]) VALUES (28, N'time', N'时间', N'22时', N'22:00', N'0')
INSERT [dbo].[NURSE_WHITE_BOARD_METADATA_VALUE] ([MV_ID], [MV_CODE], [MV_NAME], [MV_VALUE], [MV_REMARK], [IS_VALID]) VALUES (29, N'time', N'时间', N'23时', N'23:00', N'0')
SET IDENTITY_INSERT [dbo].[NURSE_WHITE_BOARD_METADATA_VALUE] OFF
SET IDENTITY_INSERT [dbo].[NURSE_WHITE_BOARD_METADATA] ON
INSERT [dbo].[NURSE_WHITE_BOARD_METADATA] ([ID], [CODE], [NAME], [PARENT_ID], [LEVEL], [INPUT_TYPE], [CREATE_DATE], [DEPT_CODE], [IS_AUTO], [IS_SHOW], [SHOW_TITLE], [IS_EXEC]) VALUES (1, N'dutyDoctor', N'值班医生', N'', 0, N'TEXT', CAST(0x0000A53800E351EA AS DateTime), N'5042', N'1', N'1', N'项目', N'0')
INSERT [dbo].[NURSE_WHITE_BOARD_METADATA] ([ID], [CODE], [NAME], [PARENT_ID], [LEVEL], [INPUT_TYPE], [CREATE_DATE], [DEPT_CODE], [IS_AUTO], [IS_SHOW], [SHOW_TITLE], [IS_EXEC]) VALUES (2, N'shiftDoctor', N'付班医生', N'', 0, N'TEXT', CAST(0x0000A53800E351EB AS DateTime), N'5042', N'1', N'1', N'项目', N'0')
INSERT [dbo].[NURSE_WHITE_BOARD_METADATA] ([ID], [CODE], [NAME], [PARENT_ID], [LEVEL], [INPUT_TYPE], [CREATE_DATE], [DEPT_CODE], [IS_AUTO], [IS_SHOW], [SHOW_TITLE], [IS_EXEC]) VALUES (3, N'secondDoctor', N'二线医生', N'', 0, N'TEXT', CAST(0x0000A53800E351EB AS DateTime), N'5042', N'1', N'1', N'项目', N'0')
INSERT [dbo].[NURSE_WHITE_BOARD_METADATA] ([ID], [CODE], [NAME], [PARENT_ID], [LEVEL], [INPUT_TYPE], [CREATE_DATE], [DEPT_CODE], [IS_AUTO], [IS_SHOW], [SHOW_TITLE], [IS_EXEC]) VALUES (4, N'secondHeadNurse', N'二线护士长', N'', 0, N'TEXT', CAST(0x0000A53800E351EB AS DateTime), N'5042', N'1', N'1', N'项目', N'0')
INSERT [dbo].[NURSE_WHITE_BOARD_METADATA] ([ID], [CODE], [NAME], [PARENT_ID], [LEVEL], [INPUT_TYPE], [CREATE_DATE], [DEPT_CODE], [IS_AUTO], [IS_SHOW], [SHOW_TITLE], [IS_EXEC]) VALUES (5, N'in-output', N'24h出入量', N'', 0, N'BEDLIST', CAST(0x0000A53800E351EB AS DateTime), N'5042', N'1', N'1', N'项目', N'1')
INSERT [dbo].[NURSE_WHITE_BOARD_METADATA] ([ID], [CODE], [NAME], [PARENT_ID], [LEVEL], [INPUT_TYPE], [CREATE_DATE], [DEPT_CODE], [IS_AUTO], [IS_SHOW], [SHOW_TITLE], [IS_EXEC]) VALUES (6, N'urine', N'24h尿量', N'', 0, N'BEDLIST', CAST(0x0000A53800E351EB AS DateTime), N'5042', N'1', N'1', N'项目', N'1')
INSERT [dbo].[NURSE_WHITE_BOARD_METADATA] ([ID], [CODE], [NAME], [PARENT_ID], [LEVEL], [INPUT_TYPE], [CREATE_DATE], [DEPT_CODE], [IS_AUTO], [IS_SHOW], [SHOW_TITLE], [IS_EXEC]) VALUES (7, N'abdominalCir', N'腹围', N'', 0, N'BEDLIST', CAST(0x0000A53800E351EB AS DateTime), N'5042', N'1', N'1', N'项目', N'1')
INSERT [dbo].[NURSE_WHITE_BOARD_METADATA] ([ID], [CODE], [NAME], [PARENT_ID], [LEVEL], [INPUT_TYPE], [CREATE_DATE], [DEPT_CODE], [IS_AUTO], [IS_SHOW], [SHOW_TITLE], [IS_EXEC]) VALUES (8, N'weight', N'体重', N'', 0, N'BEDLIST', CAST(0x0000A53800E351EB AS DateTime), N'5042', N'1', N'1', N'项目', N'1')
INSERT [dbo].[NURSE_WHITE_BOARD_METADATA] ([ID], [CODE], [NAME], [PARENT_ID], [LEVEL], [INPUT_TYPE], [CREATE_DATE], [DEPT_CODE], [IS_AUTO], [IS_SHOW], [SHOW_TITLE], [IS_EXEC]) VALUES (9, N'bodysign', N'生命体征', N'', 0, N'SUBBEDLISTFRQ', CAST(0x0000A53800E351EB AS DateTime), N'5042', N'0', N'1', N'项目', N'1')
INSERT [dbo].[NURSE_WHITE_BOARD_METADATA] ([ID], [CODE], [NAME], [PARENT_ID], [LEVEL], [INPUT_TYPE], [CREATE_DATE], [DEPT_CODE], [IS_AUTO], [IS_SHOW], [SHOW_TITLE], [IS_EXEC]) VALUES (10, N'bloodGlu', N'血糖监控', N'', 0, N'BEDLISTTEXT', CAST(0x0000A53800E351EB AS DateTime), N'5042', N'0', N'1', N'项目', N'1')
INSERT [dbo].[NURSE_WHITE_BOARD_METADATA] ([ID], [CODE], [NAME], [PARENT_ID], [LEVEL], [INPUT_TYPE], [CREATE_DATE], [DEPT_CODE], [IS_AUTO], [IS_SHOW], [SHOW_TITLE], [IS_EXEC]) VALUES (11, N'importantHint', N'重要提示', N'', 0, N'BEDLISTTEXT', CAST(0x0000A53800E351EB AS DateTime), N'5042', N'0', N'1', N'项目', N'1')
INSERT [dbo].[NURSE_WHITE_BOARD_METADATA] ([ID], [CODE], [NAME], [PARENT_ID], [LEVEL], [INPUT_TYPE], [CREATE_DATE], [DEPT_CODE], [IS_AUTO], [IS_SHOW], [SHOW_TITLE], [IS_EXEC]) VALUES (12, N'importantNotice', N'重要通知', N'', 0, N'TEXT', CAST(0x0000A53800E351EB AS DateTime), N'5042', N'0', N'1', N'项目', N'1')
INSERT [dbo].[NURSE_WHITE_BOARD_METADATA] ([ID], [CODE], [NAME], [PARENT_ID], [LEVEL], [INPUT_TYPE], [CREATE_DATE], [DEPT_CODE], [IS_AUTO], [IS_SHOW], [SHOW_TITLE], [IS_EXEC]) VALUES (13, N'pCount', N'病人总数', N'', 0, N'TEXT', CAST(0x0000A53800E351EB AS DateTime), N'5042', N'1', N'1', N'项目', N'0')
INSERT [dbo].[NURSE_WHITE_BOARD_METADATA] ([ID], [CODE], [NAME], [PARENT_ID], [LEVEL], [INPUT_TYPE], [CREATE_DATE], [DEPT_CODE], [IS_AUTO], [IS_SHOW], [SHOW_TITLE], [IS_EXEC]) VALUES (14, N'bw', N'病危', N'', 0, N'BEDLIST', CAST(0x0000A53800E351EB AS DateTime), N'5042', N'1', N'1', N'项目', N'0')
INSERT [dbo].[NURSE_WHITE_BOARD_METADATA] ([ID], [CODE], [NAME], [PARENT_ID], [LEVEL], [INPUT_TYPE], [CREATE_DATE], [DEPT_CODE], [IS_AUTO], [IS_SHOW], [SHOW_TITLE], [IS_EXEC]) VALUES (15, N'bz', N'病重', N'', 0, N'BEDLIST', CAST(0x0000A53800E351EB AS DateTime), N'5042', N'1', N'1', N'项目', N'0')
INSERT [dbo].[NURSE_WHITE_BOARD_METADATA] ([ID], [CODE], [NAME], [PARENT_ID], [LEVEL], [INPUT_TYPE], [CREATE_DATE], [DEPT_CODE], [IS_AUTO], [IS_SHOW], [SHOW_TITLE], [IS_EXEC]) VALUES (16, N'ry', N'新入院', N'', 0, N'BEDLIST', CAST(0x0000A53800E351EB AS DateTime), N'5042', N'1', N'1', N'项目', N'0')
INSERT [dbo].[NURSE_WHITE_BOARD_METADATA] ([ID], [CODE], [NAME], [PARENT_ID], [LEVEL], [INPUT_TYPE], [CREATE_DATE], [DEPT_CODE], [IS_AUTO], [IS_SHOW], [SHOW_TITLE], [IS_EXEC]) VALUES (17, N'cy', N'出院', N'', 0, N'BEDLIST', CAST(0x0000A53800E351EB AS DateTime), N'5042', N'1', N'1', N'项目', N'0')
INSERT [dbo].[NURSE_WHITE_BOARD_METADATA] ([ID], [CODE], [NAME], [PARENT_ID], [LEVEL], [INPUT_TYPE], [CREATE_DATE], [DEPT_CODE], [IS_AUTO], [IS_SHOW], [SHOW_TITLE], [IS_EXEC]) VALUES (18, N'dayNurse', N'日班护士', N'', 0, N'TEXT', CAST(0x0000A53800E351EC AS DateTime), N'5042', N'1', N'1', N'项目', N'0')
INSERT [dbo].[NURSE_WHITE_BOARD_METADATA] ([ID], [CODE], [NAME], [PARENT_ID], [LEVEL], [INPUT_TYPE], [CREATE_DATE], [DEPT_CODE], [IS_AUTO], [IS_SHOW], [SHOW_TITLE], [IS_EXEC]) VALUES (19, N'dutyNurse', N'值班护士', N'', 0, N'TEXT', CAST(0x0000A53800E351EC AS DateTime), N'5042', N'1', N'1', N'项目', N'0')
INSERT [dbo].[NURSE_WHITE_BOARD_METADATA] ([ID], [CODE], [NAME], [PARENT_ID], [LEVEL], [INPUT_TYPE], [CREATE_DATE], [DEPT_CODE], [IS_AUTO], [IS_SHOW], [SHOW_TITLE], [IS_EXEC]) VALUES (20, N'fasting', N'禁食', N'', 0, N'TEXT', CAST(0x0000A53800E351EC AS DateTime), N'5042', N'1', N'0', N'项目', N'0')
INSERT [dbo].[NURSE_WHITE_BOARD_METADATA] ([ID], [CODE], [NAME], [PARENT_ID], [LEVEL], [INPUT_TYPE], [CREATE_DATE], [DEPT_CODE], [IS_AUTO], [IS_SHOW], [SHOW_TITLE], [IS_EXEC]) VALUES (21, N'liquidDiet', N'流质饮食', N'', 0, N'TEXT', CAST(0x0000A53800E351EC AS DateTime), N'5042', N'1', N'0', N'项目', N'0')
INSERT [dbo].[NURSE_WHITE_BOARD_METADATA] ([ID], [CODE], [NAME], [PARENT_ID], [LEVEL], [INPUT_TYPE], [CREATE_DATE], [DEPT_CODE], [IS_AUTO], [IS_SHOW], [SHOW_TITLE], [IS_EXEC]) VALUES (22, N'semiLiquidDiet', N'半流质饮食', N'', 0, N'TEXT', CAST(0x0000A53800E351EC AS DateTime), N'5042', N'1', N'0', N'项目', N'0')
INSERT [dbo].[NURSE_WHITE_BOARD_METADATA] ([ID], [CODE], [NAME], [PARENT_ID], [LEVEL], [INPUT_TYPE], [CREATE_DATE], [DEPT_CODE], [IS_AUTO], [IS_SHOW], [SHOW_TITLE], [IS_EXEC]) VALUES (23, N'diabeticDiet', N'糖尿病饮食', N'', 0, N'TEXT', CAST(0x0000A53800E351EC AS DateTime), N'5042', N'1', N'0', N'项目', N'0')
INSERT [dbo].[NURSE_WHITE_BOARD_METADATA] ([ID], [CODE], [NAME], [PARENT_ID], [LEVEL], [INPUT_TYPE], [CREATE_DATE], [DEPT_CODE], [IS_AUTO], [IS_SHOW], [SHOW_TITLE], [IS_EXEC]) VALUES (24, N'inNurseWorker', N'护工(内)', N'', 0, N'TEXT', CAST(0x0000A53800E351EC AS DateTime), N'5042', N'1', N'0', N'项目', N'0')
INSERT [dbo].[NURSE_WHITE_BOARD_METADATA] ([ID], [CODE], [NAME], [PARENT_ID], [LEVEL], [INPUT_TYPE], [CREATE_DATE], [DEPT_CODE], [IS_AUTO], [IS_SHOW], [SHOW_TITLE], [IS_EXEC]) VALUES (25, N'outNurseWorker', N'护工(外)', N'', 0, N'TEXT', CAST(0x0000A53800E351EC AS DateTime), N'5042', N'1', N'0', N'项目', N'0')
INSERT [dbo].[NURSE_WHITE_BOARD_METADATA] ([ID], [CODE], [NAME], [PARENT_ID], [LEVEL], [INPUT_TYPE], [CREATE_DATE], [DEPT_CODE], [IS_AUTO], [IS_SHOW], [SHOW_TITLE], [IS_EXEC]) VALUES (26, N'other', N'其他', N'', 0, N'TEXT', CAST(0x0000A53800E351EC AS DateTime), N'5042', N'0', N'0', N'项目', N'0')
INSERT [dbo].[NURSE_WHITE_BOARD_METADATA] ([ID], [CODE], [NAME], [PARENT_ID], [LEVEL], [INPUT_TYPE], [CREATE_DATE], [DEPT_CODE], [IS_AUTO], [IS_SHOW], [SHOW_TITLE], [IS_EXEC]) VALUES (39, N'a_one_nurse', N'A1班', N'', 0, N'TEXT', CAST(0x0000A53800E35A6F AS DateTime), N'5042', N'0', N'1', N'A1班', N'0')
INSERT [dbo].[NURSE_WHITE_BOARD_METADATA] ([ID], [CODE], [NAME], [PARENT_ID], [LEVEL], [INPUT_TYPE], [CREATE_DATE], [DEPT_CODE], [IS_AUTO], [IS_SHOW], [SHOW_TITLE], [IS_EXEC]) VALUES (40, N'a_two_nurse', N'A2班', N'', 0, N'TEXT', CAST(0x0000A53800E35A6F AS DateTime), N'5042', N'0', N'1', N'A2班', N'0')
INSERT [dbo].[NURSE_WHITE_BOARD_METADATA] ([ID], [CODE], [NAME], [PARENT_ID], [LEVEL], [INPUT_TYPE], [CREATE_DATE], [DEPT_CODE], [IS_AUTO], [IS_SHOW], [SHOW_TITLE], [IS_EXEC]) VALUES (41, N'a_three_nurse', N'A3班', N'', 0, N'TEXT', CAST(0x0000A53800E35A6F AS DateTime), N'5042', N'0', N'1', N'A3班', N'0')
INSERT [dbo].[NURSE_WHITE_BOARD_METADATA] ([ID], [CODE], [NAME], [PARENT_ID], [LEVEL], [INPUT_TYPE], [CREATE_DATE], [DEPT_CODE], [IS_AUTO], [IS_SHOW], [SHOW_TITLE], [IS_EXEC]) VALUES (42, N'a_four_nurse', N'A4班', N'', 0, N'TEXT', CAST(0x0000A53800E35A6F AS DateTime), N'5042', N'0', N'1', N'A4班', N'0')
INSERT [dbo].[NURSE_WHITE_BOARD_METADATA] ([ID], [CODE], [NAME], [PARENT_ID], [LEVEL], [INPUT_TYPE], [CREATE_DATE], [DEPT_CODE], [IS_AUTO], [IS_SHOW], [SHOW_TITLE], [IS_EXEC]) VALUES (43, N'b_nurse', N'B班', N'', 0, N'TEXT', CAST(0x0000A53800E35A6F AS DateTime), N'5042', N'0', N'1', N'B班', N'0')
INSERT [dbo].[NURSE_WHITE_BOARD_METADATA] ([ID], [CODE], [NAME], [PARENT_ID], [LEVEL], [INPUT_TYPE], [CREATE_DATE], [DEPT_CODE], [IS_AUTO], [IS_SHOW], [SHOW_TITLE], [IS_EXEC]) VALUES (44, N'p_nurse', N'P班', N'', 0, N'TEXT', CAST(0x0000A53800E35A6F AS DateTime), N'5042', N'0', N'1', N'P班', N'0')
INSERT [dbo].[NURSE_WHITE_BOARD_METADATA] ([ID], [CODE], [NAME], [PARENT_ID], [LEVEL], [INPUT_TYPE], [CREATE_DATE], [DEPT_CODE], [IS_AUTO], [IS_SHOW], [SHOW_TITLE], [IS_EXEC]) VALUES (45, N'n_nurse', N'N班', N'', 0, N'TEXT', CAST(0x0000A53800E35A6F AS DateTime), N'5042', N'0', N'1', N'N班', N'0')
INSERT [dbo].[NURSE_WHITE_BOARD_METADATA] ([ID], [CODE], [NAME], [PARENT_ID], [LEVEL], [INPUT_TYPE], [CREATE_DATE], [DEPT_CODE], [IS_AUTO], [IS_SHOW], [SHOW_TITLE], [IS_EXEC]) VALUES (46, N'fu_nurse', N'辅班', N'', 0, N'TEXT', CAST(0x0000A53800E35A6F AS DateTime), N'5042', N'0', N'1', N'辅班', N'0')
INSERT [dbo].[NURSE_WHITE_BOARD_METADATA] ([ID], [CODE], [NAME], [PARENT_ID], [LEVEL], [INPUT_TYPE], [CREATE_DATE], [DEPT_CODE], [IS_AUTO], [IS_SHOW], [SHOW_TITLE], [IS_EXEC]) VALUES (54, N'breathe', N'呼吸', N'', 2, N'', CAST(0x0000A53800E35A70 AS DateTime), N'5042', N'0', N'1', N'子项目', N'0')
INSERT [dbo].[NURSE_WHITE_BOARD_METADATA] ([ID], [CODE], [NAME], [PARENT_ID], [LEVEL], [INPUT_TYPE], [CREATE_DATE], [DEPT_CODE], [IS_AUTO], [IS_SHOW], [SHOW_TITLE], [IS_EXEC]) VALUES (55, N'bloodPress', N'血压', N'', 2, N'', CAST(0x0000A53800E35A70 AS DateTime), N'5042', N'0', N'1', N'子项目', N'0')
INSERT [dbo].[NURSE_WHITE_BOARD_METADATA] ([ID], [CODE], [NAME], [PARENT_ID], [LEVEL], [INPUT_TYPE], [CREATE_DATE], [DEPT_CODE], [IS_AUTO], [IS_SHOW], [SHOW_TITLE], [IS_EXEC]) VALUES (65, N'secNurse', N'护理二线', N'', 0, N'', CAST(0x0000A53800E351EC AS DateTime), N'5042', N'0', N'0', N'内容', N'0')
INSERT [dbo].[NURSE_WHITE_BOARD_METADATA] ([ID], [CODE], [NAME], [PARENT_ID], [LEVEL], [INPUT_TYPE], [CREATE_DATE], [DEPT_CODE], [IS_AUTO], [IS_SHOW], [SHOW_TITLE], [IS_EXEC]) VALUES (66, N'secMedical', N'医疗二线', N'', 0, N'', CAST(0x0000A53800E351EC AS DateTime), N'5042', N'0', N'0', N'内容', N'0')
INSERT [dbo].[NURSE_WHITE_BOARD_METADATA] ([ID], [CODE], [NAME], [PARENT_ID], [LEVEL], [INPUT_TYPE], [CREATE_DATE], [DEPT_CODE], [IS_AUTO], [IS_SHOW], [SHOW_TITLE], [IS_EXEC]) VALUES (67, N'oxygenSaturation', N'血氧', N'bodysign', 2, N'text', CAST(0x0000A53800E35A70 AS DateTime), N'5042', N'0', N'1', N'子项目', N'0')
INSERT [dbo].[NURSE_WHITE_BOARD_METADATA] ([ID], [CODE], [NAME], [PARENT_ID], [LEVEL], [INPUT_TYPE], [CREATE_DATE], [DEPT_CODE], [IS_AUTO], [IS_SHOW], [SHOW_TITLE], [IS_EXEC]) VALUES (68, N'pulse', N'脉搏', N'bodysign', 2, N'text', CAST(0x0000A53800E35A70 AS DateTime), N'5042', N'0', N'1', N'子项目', N'0')
SET IDENTITY_INSERT [dbo].[NURSE_WHITE_BOARD_METADATA] OFF

--皮试是否需要护士双核对
delete from SYS_CONFIG where CONFIG_ID = 'isNeedDoubleCheck';
INSERT [dbo].[SYS_CONFIG] ( [CONFIG_ID],[CONFIG_VALUE] ,[CONFIG_TYPE],[CONFIG_OWNER] ,[VALID_FLAG],[CONFIG_DESC]) VALUES ('isNeedDoubleCheck','1','S','','1','皮试是否需要双核对')


--生命体征逻辑删除-体征明细和事件状态字段
--体征item
ALTER TABLE PAT_BODYSIGN_DETAIL ADD STATUS VARCHAR(2);
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'体征明细状态' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_BODYSIGN_DETAIL', @level2type=N'COLUMN',@level2name=N'STATUS'
GO
--事件
ALTER TABLE PAT_EVENT ADD STATUS VARCHAR(2);
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'事件状态' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_EVENT', @level2type=N'COLUMN',@level2name=N'STATUS'
GO
--------------2016-01-20----------------------
--字典表字段
alter table com_dic add IS_INFUSION_CARD char(1) default 0;
alter table com_dic add IS_ORD_LABEL char(1) default 0;

--医嘱执行单打印表
drop table PAT_ORDER_DOC_PRINT;
/****** Object:  Table [dbo].[PAT_ORDER_DOC_PRINT]    Script Date: 01/20/2016 09:11:23 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[PAT_ORDER_DOC_PRINT](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[print_date] [datetime] NULL,
	[print_nurse_code] [varchar](30) NULL,
	[print_nurse_name] [varchar](30) NULL,
	[check_nurse_code] [varchar](30) NULL,
	[check_nurse_name] [varchar](30) NULL,
	[check_date] [datetime] NULL,
	[pat_id] [varchar](30) NOT NULL,
	[dept_code] [varchar](30) NULL,
	[ord_doc_type] [varchar](30) NULL,
	[oper_date] [datetime] NULL,
	[ord_usage_type] [varchar](30) NULL,
	[ord_type] [char](1) NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO

----------------------------2016-03-24
--体征删除表
 CREATE TABLE PAT_BODYSIGN_DELETE(
	ID BIGINT PRIMARY KEY IDENTITY(1,1),
	DEPT_CODE VARCHAR(20),
	PAT_ID VARCHAR(30) NOT NULL,
	RECORD_DATE DATETIME NOT NULL,
	CODE VARCHAR(30) NOT NULL,
	OPER_DATE DATETIME
 );
 --项目入院时间差
ALTER TABLE PAT_BODYSIGN_DETAIL ADD RY_HOUR_DIFF VARCHAR(10);
--过敏有效字段
ALTER table [PAT_ALLERGY] add IS_VALID char(1); 
--小白板元数据剂量
ALTER TABLE NURSE_WHITE_BOARD_METADATA IS_DOSAGE char(1);
--小白板项目标题宽度
ALTER TABLE NURSE_WHITE_BOARD_METADATA ADD TITLE_WIDTH DECIMAL(5,1);
--小白板编辑跟模板关联
ALTER TABLE NURSE_WHITE_BOARD_EDIT_TYPE_DIC ADD TEMPLATE_ID VARCHAR(20);
--出入量增加性状和颜色
ALTER TABLE PAT_IN_OUT_MANAGER ADD OUT_SHAPE_CODE VARCHAR(20);
ALTER TABLE PAT_IN_OUT_MANAGER ADD OUT_SHAPE_NAME VARCHAR(20);
ALTER TABLE PAT_IN_OUT_MANAGER ADD OUT_COLOR_NAME VARCHAR(20);
ALTER TABLE PAT_IN_OUT_MANAGER ADD OUT_COLOR_CODE VARCHAR(20);


