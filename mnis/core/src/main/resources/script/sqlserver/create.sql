USE [MNIS_V3]
GO
/****** Object:  Table [dbo].[SYS_DATASYNC_LOG]    Script Date: 11/12/2015 14:32:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[SYS_DATASYNC_LOG](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[tableName] [varchar](20) NOT NULL,
	[startTime] [datetime] NOT NULL,
	[endTime] [datetime] NOT NULL,
	[upRows] [int] NOT NULL,
 CONSTRAINT [PK_datasync_log] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'主键，自增长' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'SYS_DATASYNC_LOG', @level2type=N'COLUMN',@level2name=N'id'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'更新his表名称' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'SYS_DATASYNC_LOG', @level2type=N'COLUMN',@level2name=N'tableName'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'开始更新时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'SYS_DATASYNC_LOG', @level2type=N'COLUMN',@level2name=N'startTime'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'结束更新时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'SYS_DATASYNC_LOG', @level2type=N'COLUMN',@level2name=N'endTime'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'实际更新条数' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'SYS_DATASYNC_LOG', @level2type=N'COLUMN',@level2name=N'upRows'
GO
/****** Object:  Table [dbo].[SYS_DATASYNC]    Script Date: 11/12/2015 14:32:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[SYS_DATASYNC](
	[tableName] [nvarchar](20) NOT NULL,
	[upTime] [datetime] NULL,
	[upCount] [int] NULL,
	[syncAllC] [int] NULL,
	[status] [int] NULL,
 CONSTRAINT [PK_DATASYNC] PRIMARY KEY CLUSTERED 
(
	[tableName] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'更新his表名称' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'SYS_DATASYNC', @level2type=N'COLUMN',@level2name=N'tableName'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'更新时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'SYS_DATASYNC', @level2type=N'COLUMN',@level2name=N'upTime'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'更新次数' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'SYS_DATASYNC', @level2type=N'COLUMN',@level2name=N'upCount'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'设定多少次全表更新(针对患者信息)' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'SYS_DATASYNC', @level2type=N'COLUMN',@level2name=N'syncAllC'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'更新状态(1: 正在更新   0:更新完毕)' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'SYS_DATASYNC', @level2type=N'COLUMN',@level2name=N'status'
GO
/****** Object:  Table [dbo].[SYS_CONFIG]    Script Date: 11/12/2015 14:32:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[SYS_CONFIG](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[CONFIG_ID] [varchar](30) NULL,
	[CONFIG_VALUE] [varchar](800) NULL,
	[CONFIG_TYPE] [varchar](10) NULL,
	[CONFIG_OWNER] [varchar](30) NULL,
	[VALID_FLAG] [varchar](1) NOT NULL,
	[CONFIG_DESC] [varchar](200) NULL,
 CONSTRAINT [PK_SYS_CONFIG] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY],
 CONSTRAINT [UNIQUE_SYS_CONFIG] UNIQUE NONCLUSTERED 
(
	[CONFIG_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'配置序号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'SYS_CONFIG', @level2type=N'COLUMN',@level2name=N'ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'配置ID号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'SYS_CONFIG', @level2type=N'COLUMN',@level2name=N'CONFIG_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'值' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'SYS_CONFIG', @level2type=N'COLUMN',@level2name=N'CONFIG_VALUE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'类型' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'SYS_CONFIG', @level2type=N'COLUMN',@level2name=N'CONFIG_TYPE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'拥有者' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'SYS_CONFIG', @level2type=N'COLUMN',@level2name=N'CONFIG_OWNER'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'标记(0:无效,1:有效)' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'SYS_CONFIG', @level2type=N'COLUMN',@level2name=N'VALID_FLAG'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'描述' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'SYS_CONFIG', @level2type=N'COLUMN',@level2name=N'CONFIG_DESC'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'系统配置表' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'SYS_CONFIG'
GO
/****** Object:  Table [dbo].[PAT_TRANSFER]    Script Date: 11/12/2015 14:32:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[PAT_TRANSFER](
	[ID] [bigint] IDENTITY(1,1) NOT NULL,
	[PAT_ID] [varchar](20) NOT NULL,
	[EXECUTE_DATE] [datetime] NOT NULL,
	[EXECUTOR] [varchar](10) NULL,
	[WARD_CODE] [varchar](10) NOT NULL,
	[BED_CODE] [varchar](10) NOT NULL,
	[NEW_WARD_CODE] [varchar](10) NOT NULL,
	[NEW_BED_CODE] [varchar](10) NOT NULL,
	[FLAG] [char](1) NOT NULL,
	[SYNC_CREATE] [datetime] NULL,
	[SYNC_UPDATE] [datetime] NULL,
 CONSTRAINT [PK_PAT_TRANSFER] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'出科记录自增长号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_TRANSFER', @level2type=N'COLUMN',@level2name=N'ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'住院流水号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_TRANSFER', @level2type=N'COLUMN',@level2name=N'PAT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'执行日期' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_TRANSFER', @level2type=N'COLUMN',@level2name=N'EXECUTE_DATE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'执行人工号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_TRANSFER', @level2type=N'COLUMN',@level2name=N'EXECUTOR'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'病区代码' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_TRANSFER', @level2type=N'COLUMN',@level2name=N'WARD_CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'床位号码' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_TRANSFER', @level2type=N'COLUMN',@level2name=N'BED_CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'新病区代码' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_TRANSFER', @level2type=N'COLUMN',@level2name=N'NEW_WARD_CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'新床位号码' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_TRANSFER', @level2type=N'COLUMN',@level2name=N'NEW_BED_CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'标记(0:无效,1:有效)' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_TRANSFER', @level2type=N'COLUMN',@level2name=N'FLAG'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'创建时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_TRANSFER', @level2type=N'COLUMN',@level2name=N'SYNC_CREATE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'修改时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_TRANSFER', @level2type=N'COLUMN',@level2name=N'SYNC_UPDATE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'转床转科记录表' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_TRANSFER'
GO
/****** Object:  Table [dbo].[PAT_SPECIMENS_COLLECTION]    Script Date: 11/12/2015 14:32:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[PAT_SPECIMENS_COLLECTION](
	[ID] [bigint] IDENTITY(1,1) NOT NULL,
	[PAT_ID] [varchar](20) NOT NULL,
	[PAT_NAME] [varchar](20) NULL,
	[BED_CODE] [varchar](10) NOT NULL,
	[SPECIMEN_BARCODE] [varchar](20) NOT NULL,
	[REQUISITION_STATE] [varchar](20) NULL,
	[TEST_ORDER_NAME] [varchar](50) NULL,
	[SAMPLING_PERSON] [varchar](30) NULL,
	[SAMPLING_TIME] [datetime] NOT NULL,
	[SAMPLE_CLASS_NAME] [varchar](20) NULL,
	[SAMPLING_USER] [varchar](20) NOT NULL,
	[SAMPLING_DEPT] [varchar](30) NOT NULL,
 CONSTRAINT [PK_PAT_SPECIMENS_COLLECTION] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'标本采集自增长号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_SPECIMENS_COLLECTION', @level2type=N'COLUMN',@level2name=N'ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'住院流水号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_SPECIMENS_COLLECTION', @level2type=N'COLUMN',@level2name=N'PAT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'患者姓名' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_SPECIMENS_COLLECTION', @level2type=N'COLUMN',@level2name=N'PAT_NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'床位号码' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_SPECIMENS_COLLECTION', @level2type=N'COLUMN',@level2name=N'BED_CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'标本条码' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_SPECIMENS_COLLECTION', @level2type=N'COLUMN',@level2name=N'SPECIMEN_BARCODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'申请状态' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_SPECIMENS_COLLECTION', @level2type=N'COLUMN',@level2name=N'REQUISITION_STATE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'测试项目名称' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_SPECIMENS_COLLECTION', @level2type=N'COLUMN',@level2name=N'TEST_ORDER_NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'样本要求' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_SPECIMENS_COLLECTION', @level2type=N'COLUMN',@level2name=N'SAMPLING_PERSON'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'采样时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_SPECIMENS_COLLECTION', @level2type=N'COLUMN',@level2name=N'SAMPLING_TIME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'样本类型名称' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_SPECIMENS_COLLECTION', @level2type=N'COLUMN',@level2name=N'SAMPLE_CLASS_NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'采集人姓名' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_SPECIMENS_COLLECTION', @level2type=N'COLUMN',@level2name=N'SAMPLING_USER'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'采集科室名称' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_SPECIMENS_COLLECTION', @level2type=N'COLUMN',@level2name=N'SAMPLING_DEPT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'标本采集记录' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_SPECIMENS_COLLECTION'
GO
/****** Object:  Table [dbo].[PAT_SKIN_TEST]    Script Date: 11/12/2015 14:32:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[PAT_SKIN_TEST](
	[ID] [bigint] IDENTITY(1,1) NOT NULL,
	[PAT_ID] [varchar](20) NOT NULL,
	[PAT_NAME] [varchar](20) NULL,
	[ORDER_EXEC_ID] [varchar](32) NULL,
	[STATUS] [varchar](1) NOT NULL,
	[RESULT] [varchar](20) NOT NULL,
	[INPUT_NURSE_ID] [varchar](10) NULL,
	[INPUT_NURSE_NAME] [varchar](20) NULL,
	[APPROVE_NURSE_ID] [varchar](10) NULL,
	[APPROVE_NURSE_NAME] [varchar](20) NULL,
	[APPROVE_DATE] [datetime] NULL,
	[EXEC_NURSE_ID] [varchar](10) NULL,
	[EXEC_NURSE_NAME] [varchar](20) NULL,
	[EXEC_DATE] [datetime] NULL,
	[DRUG_BATCH_NO] [varchar](30) NULL,
	[DRUG_CODE] [varchar](30) NULL,
	[DRUG_NAME] [varchar](80) NULL,
	[BODY_SIGN_MAS_ID] [bigint] NULL,
	[IMG_BEFORE] [image] NULL,
	[IMG_AFTER] [image] NULL,
	[test_index] [int] NOT NULL,
 CONSTRAINT [PK_PAT_SKIN_TEST] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'皮试i项Id' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_SKIN_TEST', @level2type=N'COLUMN',@level2name=N'ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'住院流水号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_SKIN_TEST', @level2type=N'COLUMN',@level2name=N'PAT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'住院号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_SKIN_TEST', @level2type=N'COLUMN',@level2name=N'PAT_NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'医嘱执行id' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_SKIN_TEST', @level2type=N'COLUMN',@level2name=N'ORDER_EXEC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'皮试状态(0:未皮试,1:已皮试)' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_SKIN_TEST', @level2type=N'COLUMN',@level2name=N'STATUS'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'皮试结果(0:阴性,1:阳性)' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_SKIN_TEST', @level2type=N'COLUMN',@level2name=N'RESULT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'皮试录入护士code' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_SKIN_TEST', @level2type=N'COLUMN',@level2name=N'INPUT_NURSE_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'皮试录入护士name' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_SKIN_TEST', @level2type=N'COLUMN',@level2name=N'INPUT_NURSE_NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'皮试确认护士code' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_SKIN_TEST', @level2type=N'COLUMN',@level2name=N'APPROVE_NURSE_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'皮试确认护士name' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_SKIN_TEST', @level2type=N'COLUMN',@level2name=N'APPROVE_NURSE_NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'皮试确认时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_SKIN_TEST', @level2type=N'COLUMN',@level2name=N'APPROVE_DATE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'皮试执行护士code' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_SKIN_TEST', @level2type=N'COLUMN',@level2name=N'EXEC_NURSE_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'皮试执行护士name' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_SKIN_TEST', @level2type=N'COLUMN',@level2name=N'EXEC_NURSE_NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'皮试执行时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_SKIN_TEST', @level2type=N'COLUMN',@level2name=N'EXEC_DATE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'药品批号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_SKIN_TEST', @level2type=N'COLUMN',@level2name=N'DRUG_BATCH_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'药品代码' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_SKIN_TEST', @level2type=N'COLUMN',@level2name=N'DRUG_CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'药品名称' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_SKIN_TEST', @level2type=N'COLUMN',@level2name=N'DRUG_NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'患者皮试结果记录表' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_SKIN_TEST'
GO
/****** Object:  Table [dbo].[PAT_PRINT_INFO]    Script Date: 11/12/2015 14:32:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING OFF
GO
CREATE TABLE [dbo].[PAT_PRINT_INFO](
	[PRINT_ID] [bigint] IDENTITY(1,1) NOT NULL,
	[PRINT_DATA_ID] [varchar](30) NOT NULL,
	[PRINT_TYPE] [varchar](20) NOT NULL,
	[IS_PRINT_BARCODE] [varchar](1) NULL,
	[IS_PRINT_BED] [varchar](1) NULL,
	[IS_PRINT_LABEL] [varchar](1) NULL,
	[PRINT_DATE] [datetime] NULL,
PRIMARY KEY CLUSTERED 
(
	[PRINT_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'主键id' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_PRINT_INFO', @level2type=N'COLUMN',@level2name=N'PRINT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'打印数据id' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_PRINT_INFO', @level2type=N'COLUMN',@level2name=N'PRINT_DATA_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'打印类型' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_PRINT_INFO', @level2type=N'COLUMN',@level2name=N'PRINT_TYPE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'条码打印标记(0:未打印,1:打印)' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_PRINT_INFO', @level2type=N'COLUMN',@level2name=N'IS_PRINT_BARCODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'床头卡打印标记(0:未打印,1:打印)' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_PRINT_INFO', @level2type=N'COLUMN',@level2name=N'IS_PRINT_BED'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'瓶签打印标记(0:未打印,1:打印)' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_PRINT_INFO', @level2type=N'COLUMN',@level2name=N'IS_PRINT_LABEL'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'打印时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_PRINT_INFO', @level2type=N'COLUMN',@level2name=N'PRINT_DATE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'患者信息-打印信息表' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_PRINT_INFO'
GO
/****** Object:  Table [dbo].[PAT_ORDER_PRINT_LOG]    Script Date: 11/12/2015 14:32:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[PAT_ORDER_PRINT_LOG](
	[ORDER_PRINT_ID] [bigint] IDENTITY(1,1) NOT NULL,
	[ORDER_GROUP_ID] [varchar](20) NOT NULL,
	[PRINT_DATE] [datetime] NOT NULL,
	[PRINT_USER_COCE] [varchar](10) NOT NULL,
	[PRINT_USER_NAME] [varchar](20) NULL,
	[WARD_CODE] [varchar](10) NOT NULL,
 CONSTRAINT [PK_PAT_ORDER_PRINT_LOG] PRIMARY KEY CLUSTERED 
(
	[ORDER_PRINT_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'打印流水号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_PRINT_LOG', @level2type=N'COLUMN',@level2name=N'ORDER_PRINT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'医嘱ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_PRINT_LOG', @level2type=N'COLUMN',@level2name=N'ORDER_GROUP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'打印日期时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_PRINT_LOG', @level2type=N'COLUMN',@level2name=N'PRINT_DATE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'打印人工号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_PRINT_LOG', @level2type=N'COLUMN',@level2name=N'PRINT_USER_COCE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'打印人姓名' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_PRINT_LOG', @level2type=N'COLUMN',@level2name=N'PRINT_USER_NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'病区代码' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_PRINT_LOG', @level2type=N'COLUMN',@level2name=N'WARD_CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'医嘱瓶签打印记录表' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_PRINT_LOG'
GO
/****** Object:  Table [dbo].[PAT_ORDER_ORIGINAL]    Script Date: 11/12/2015 14:32:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[PAT_ORDER_ORIGINAL](
	[ORDER_ID] [varchar](32) NOT NULL,
	[PAT_ID] [varchar](20) NULL,
	[IN_HOSP_NO] [varchar](20) NOT NULL,
	[PAT_NAME] [varchar](20) NULL,
	[BED_CODE] [varchar](10) NULL,
	[GROUP_NO] [varchar](20) NOT NULL,
	[IS_LONGTERM] [varchar](1) NOT NULL,
	[CREATE_WARD_CODE] [varchar](10) NULL,
	[CREATE_DOC_ID] [varchar](10) NULL,
	[CREATE_DOC_NAME] [varchar](20) NULL,
	[CREATE_DATETIME] [datetime] NULL,
	[CONFIRM_USER_CODE] [varchar](10) NULL,
	[CONFIRM_USER_NAME] [varchar](20) NULL,
	[CONFIRM_DATE] [datetime] NULL,
	[BEGIN_DATE] [datetime] NULL,
	[END_DATE] [datetime] NULL,
	[STOP_DOC_ID] [varchar](10) NULL,
	[STOP_DOC_NAME] [varchar](20) NULL,
	[STOP_DATE] [datetime] NULL,
	[FREQ_CODE] [varchar](10) NULL,
	[FREQ_NAME] [varchar](30) NULL,
	[ORDER_TYPE_CODE] [varchar](10) NULL,
	[ORDER_TYPE_NAME] [varchar](20) NULL,
	[ORDER_EXEC_TYPE_CODE] [varchar](20) NULL,
	[ORDER_EXEC_TYPE_NAME] [varchar](20) NULL,
	[ORDER_STATUS] [varchar](1) NULL,
	[EXEC_NUM] [int] NULL,
	[EXEC_PLAN] [varchar](30) NULL,
	[USAGE_CODE] [varchar](20) NULL,
	[USAGE_NAME] [varchar](20) NULL,
	[NEED_CHARGE] [varchar](1) NULL,
	[IS_SELF_PREPARE] [varchar](1) NULL,
	[NEED_SKINTEST] [varchar](1) NULL,
	[IS_EMERGENT] [varchar](1) NULL,
	[REMARK] [varchar](60) NULL,
	[ITEM_CODE] [varchar](30) NULL,
	[ITEM_NAME] [varchar](80) NULL,
	[ITEM_PRICE] [decimal](12, 6) NULL,
	[ITEM_REMARK] [varchar](30) NULL,
	[SPECIFICATION] [varchar](50) NULL,
	[DOSAGE] [decimal](12, 6) NULL,
	[DOSAGE_UNIT] [varchar](20) NULL,
	[SYNC_CREATE] [datetime] NULL,
	[SYNC_UPDATE] [datetime] NULL,
	[CDATE_CURMODC] [datetime] NULL,
	[DATE_NXTMODC] [datetime] NULL,
	[SYNC_FLAG] [int] NULL,
	[PERFORM_SCHEDULE] [varchar](200) NULL,
	[FREQ_COUNTER] [int] NULL,
	[FREQ_INTERVAL] [int] NULL,
	[FREQ_INTERVAL_UNIT] [varchar](20) NULL,
	[FREQ_TAG] [varchar](20) NULL,
 CONSTRAINT [PK_PAT_ORDER_ORIGINAL] PRIMARY KEY CLUSTERED 
(
	[ORDER_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'医嘱流水号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_ORIGINAL', @level2type=N'COLUMN',@level2name=N'ORDER_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'住院流水号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_ORIGINAL', @level2type=N'COLUMN',@level2name=N'PAT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'住院号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_ORIGINAL', @level2type=N'COLUMN',@level2name=N'IN_HOSP_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'患者姓名' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_ORIGINAL', @level2type=N'COLUMN',@level2name=N'PAT_NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'床号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_ORIGINAL', @level2type=N'COLUMN',@level2name=N'BED_CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'医嘱组号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_ORIGINAL', @level2type=N'COLUMN',@level2name=N'GROUP_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'是否长期医嘱(0:临时,1:长期)' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_ORIGINAL', @level2type=N'COLUMN',@level2name=N'IS_LONGTERM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'开立科室代码' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_ORIGINAL', @level2type=N'COLUMN',@level2name=N'CREATE_WARD_CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'开立医生工号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_ORIGINAL', @level2type=N'COLUMN',@level2name=N'CREATE_DOC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'开立医生姓名' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_ORIGINAL', @level2type=N'COLUMN',@level2name=N'CREATE_DOC_NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'开立日期' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_ORIGINAL', @level2type=N'COLUMN',@level2name=N'CREATE_DATETIME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'确认人代码' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_ORIGINAL', @level2type=N'COLUMN',@level2name=N'CONFIRM_USER_CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'确认人姓名' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_ORIGINAL', @level2type=N'COLUMN',@level2name=N'CONFIRM_USER_NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'确认日期' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_ORIGINAL', @level2type=N'COLUMN',@level2name=N'CONFIRM_DATE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'开始日期' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_ORIGINAL', @level2type=N'COLUMN',@level2name=N'BEGIN_DATE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'结束日期' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_ORIGINAL', @level2type=N'COLUMN',@level2name=N'END_DATE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'停止医生' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_ORIGINAL', @level2type=N'COLUMN',@level2name=N'STOP_DOC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'停止医生名称' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_ORIGINAL', @level2type=N'COLUMN',@level2name=N'STOP_DOC_NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'停止日期时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_ORIGINAL', @level2type=N'COLUMN',@level2name=N'STOP_DATE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'频次代码' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_ORIGINAL', @level2type=N'COLUMN',@level2name=N'FREQ_CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'频次名称' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_ORIGINAL', @level2type=N'COLUMN',@level2name=N'FREQ_NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'医嘱类型代码' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_ORIGINAL', @level2type=N'COLUMN',@level2name=N'ORDER_TYPE_CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'医嘱类型名称' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_ORIGINAL', @level2type=N'COLUMN',@level2name=N'ORDER_TYPE_NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'医嘱执行方式代码' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_ORIGINAL', @level2type=N'COLUMN',@level2name=N'ORDER_EXEC_TYPE_CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'医嘱执行方式名称' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_ORIGINAL', @level2type=N'COLUMN',@level2name=N'ORDER_EXEC_TYPE_NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'HIS医嘱状态,0开立，1审核，2执行，3作废，4重整，5无费退院' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_ORIGINAL', @level2type=N'COLUMN',@level2name=N'ORDER_STATUS'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'执行总次数' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_ORIGINAL', @level2type=N'COLUMN',@level2name=N'EXEC_NUM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'执行计划' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_ORIGINAL', @level2type=N'COLUMN',@level2name=N'EXEC_PLAN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'用法代码' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_ORIGINAL', @level2type=N'COLUMN',@level2name=N'USAGE_CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'用法名称' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_ORIGINAL', @level2type=N'COLUMN',@level2name=N'USAGE_NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'是否收费1是/0否' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_ORIGINAL', @level2type=N'COLUMN',@level2name=N'NEED_CHARGE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'是否自费1是/0否' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_ORIGINAL', @level2type=N'COLUMN',@level2name=N'IS_SELF_PREPARE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'是否需要皮试1是/0否' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_ORIGINAL', @level2type=N'COLUMN',@level2name=N'NEED_SKINTEST'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'是否紧急(0:否,1:是)' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_ORIGINAL', @level2type=N'COLUMN',@level2name=N'IS_EMERGENT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'医嘱备注' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_ORIGINAL', @level2type=N'COLUMN',@level2name=N'REMARK'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'项目代码' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_ORIGINAL', @level2type=N'COLUMN',@level2name=N'ITEM_CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'项目名称' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_ORIGINAL', @level2type=N'COLUMN',@level2name=N'ITEM_NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'项目价格' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_ORIGINAL', @level2type=N'COLUMN',@level2name=N'ITEM_PRICE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'项目备注' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_ORIGINAL', @level2type=N'COLUMN',@level2name=N'ITEM_REMARK'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'规格' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_ORIGINAL', @level2type=N'COLUMN',@level2name=N'SPECIFICATION'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'剂量' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_ORIGINAL', @level2type=N'COLUMN',@level2name=N'DOSAGE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'剂量单位' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_ORIGINAL', @level2type=N'COLUMN',@level2name=N'DOSAGE_UNIT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'创建时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_ORIGINAL', @level2type=N'COLUMN',@level2name=N'SYNC_CREATE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'修改时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_ORIGINAL', @level2type=N'COLUMN',@level2name=N'SYNC_UPDATE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'本次分解时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_ORIGINAL', @level2type=N'COLUMN',@level2name=N'CDATE_CURMODC'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'下次分解时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_ORIGINAL', @level2type=N'COLUMN',@level2name=N'DATE_NXTMODC'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'同步到医嘱患者主表中(默认为0，同步为1)' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_ORIGINAL', @level2type=N'COLUMN',@level2name=N'SYNC_FLAG'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'护士执行计划' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_ORIGINAL', @level2type=N'COLUMN',@level2name=N'PERFORM_SCHEDULE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'患者医嘱' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_ORIGINAL'
GO
/****** Object:  Table [dbo].[PAT_ORDER_ITEM]    Script Date: 11/12/2015 14:32:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[PAT_ORDER_ITEM](
	[PAT_ID] [varchar](20) NOT NULL,
	[ORDER_NO] [varchar](15) NOT NULL,
	[PERFORM_NO] [varchar](15) NOT NULL,
	[ORDER_SUB_NO] [varchar](15) NOT NULL,
	[ORDER_ITEM_ID] [bigint] IDENTITY(1,1) NOT NULL,
	[SKT_ORDER_GROUP_ID] [bigint] NULL,
	[ITEM_CODE] [varchar](20) NULL,
	[ITEM_NAME] [varchar](80) NULL,
	[ITEM_PRICE] [decimal](12, 6) NULL,
	[SPECIFICATION] [varchar](50) NULL,
	[DOSAGE] [decimal](12, 6) NULL,
	[DOSAGE_UNIT] [varchar](20) NULL,
	[SKIN_TEST_REQUIRED] [varchar](1) NULL,
	[REMARK] [varchar](30) NULL,
	[NEED_CHARGE] [varchar](1) NULL,
	[IS_SELF_PREPARE] [varchar](1) NULL,
	[USAGE_CODE] [varchar](20) NULL,
	[USAGE_NAME] [varchar](20) NULL,
	[ORDER_GROUP_ID] [varchar](20) NULL,
	[ITEM_CLASS_CODE] [varchar](20) NULL,
	[ITEM_CLASS_NAME] [varchar](40) NULL,
	[IS_HIGH_RISK] [varchar](1) NULL,
	[RISK_LEVEL] [varchar](10) NULL,
 CONSTRAINT [PK_PAT_ORDER_ITEM] PRIMARY KEY CLUSTERED 
(
	[PAT_ID] ASC,
	[ORDER_NO] ASC,
	[ORDER_SUB_NO] ASC,
	[PERFORM_NO] ASC,
	[ORDER_ITEM_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON, FILLFACTOR = 100) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'医嘱组号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_ITEM', @level2type=N'COLUMN',@level2name=N'ORDER_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'长期医嘱根据频次生成批次序号； 临时医嘱默认为1' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_ITEM', @level2type=N'COLUMN',@level2name=N'PERFORM_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'项目序号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_ITEM', @level2type=N'COLUMN',@level2name=N'ORDER_SUB_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'医嘱明细ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_ITEM', @level2type=N'COLUMN',@level2name=N'ORDER_ITEM_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'医嘱皮试ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_ITEM', @level2type=N'COLUMN',@level2name=N'SKT_ORDER_GROUP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'项目代码' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_ITEM', @level2type=N'COLUMN',@level2name=N'ITEM_CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'项目名称' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_ITEM', @level2type=N'COLUMN',@level2name=N'ITEM_NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'项目价格' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_ITEM', @level2type=N'COLUMN',@level2name=N'ITEM_PRICE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'规格' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_ITEM', @level2type=N'COLUMN',@level2name=N'SPECIFICATION'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'剂量' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_ITEM', @level2type=N'COLUMN',@level2name=N'DOSAGE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'剂量单位' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_ITEM', @level2type=N'COLUMN',@level2name=N'DOSAGE_UNIT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'是否皮试(0:否,1:是)' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_ITEM', @level2type=N'COLUMN',@level2name=N'SKIN_TEST_REQUIRED'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'备注' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_ITEM', @level2type=N'COLUMN',@level2name=N'REMARK'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'是否收费1是/0否' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_ITEM', @level2type=N'COLUMN',@level2name=N'NEED_CHARGE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'是否自费1是/0否' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_ITEM', @level2type=N'COLUMN',@level2name=N'IS_SELF_PREPARE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'用法代码' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_ITEM', @level2type=N'COLUMN',@level2name=N'USAGE_CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'用法名称' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_ITEM', @level2type=N'COLUMN',@level2name=N'USAGE_NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'项目类别代码' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_ITEM', @level2type=N'COLUMN',@level2name=N'ITEM_CLASS_CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'项目类别名称' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_ITEM', @level2type=N'COLUMN',@level2name=N'ITEM_CLASS_NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'是否高危药品(0:否,1:是)' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_ITEM', @level2type=N'COLUMN',@level2name=N'IS_HIGH_RISK'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'高危药品等级' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_ITEM', @level2type=N'COLUMN',@level2name=N'RISK_LEVEL'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'医嘱明细表' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_ITEM'
GO
/****** Object:  Table [dbo].[PAT_ORDER_GROUP]    Script Date: 11/12/2015 14:32:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[PAT_ORDER_GROUP](
	[PAT_ID] [varchar](20) NOT NULL,
	[ORDER_NO] [varchar](15) NOT NULL,
	[PERFORM_NO] [varchar](15) NOT NULL,
	[IN_HOSP_NO] [varchar](20) NULL,
	[ORDER_BAR] [varchar](20) NOT NULL,
	[PAT_NAME] [varchar](20) NULL,
	[BED_CODE] [varchar](10) NULL,
	[IS_LONGTERM] [varchar](1) NOT NULL,
	[CREATE_WARD_CODE] [varchar](10) NULL,
	[CONFIRM_USER_CODE] [varchar](10) NULL,
	[CONFIRM_USER_NAME] [varchar](20) NULL,
	[CONFIRM_DATE] [datetime] NULL,
	[STOP_DOC_ID] [varchar](10) NULL,
	[STOP_DOC_NAME] [varchar](20) NULL,
	[STOP_DATE] [datetime] NULL,
	[FREQ_CODE] [varchar](10) NULL,
	[FREQ_NAME] [varchar](30) NULL,
	[ORDER_TYPE_CODE] [varchar](10) NULL,
	[ORDER_TYPE_NAME] [varchar](20) NULL,
	[ORDER_EXEC_TYPE_CODE] [varchar](20) NULL,
	[ORDER_EXEC_TYPE_NAME] [varchar](20) NULL,
	[IS_SKIN_TEST] [varchar](1) NULL,
	[IS_EMERGENT] [varchar](1) NULL,
	[REMARK] [varchar](60) NULL,
	[CREATE_DATETIME] [datetime] NULL,
	[CREATE_DOC_ID] [varchar](10) NULL,
	[CREATE_DOC_NAME] [varchar](20) NULL,
	[ORDER_STATUS_CODE] [varchar](1) NULL,
	[PLAN_TIME] [datetime] NULL,
	[order_sort_no] [int] NULL,
 CONSTRAINT [PK_PAT_ORDER_GROUP] PRIMARY KEY CLUSTERED 
(
	[PAT_ID] ASC,
	[ORDER_NO] ASC,
	[PERFORM_NO] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON, FILLFACTOR = 100) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'住院流水号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_GROUP', @level2type=N'COLUMN',@level2name=N'PAT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'医嘱组号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_GROUP', @level2type=N'COLUMN',@level2name=N'ORDER_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'长期医嘱根据频次生成批次序号 临时医嘱默认为1' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_GROUP', @level2type=N'COLUMN',@level2name=N'PERFORM_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'住院号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_GROUP', @level2type=N'COLUMN',@level2name=N'IN_HOSP_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'医嘱条码' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_GROUP', @level2type=N'COLUMN',@level2name=N'ORDER_BAR'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'患者姓名' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_GROUP', @level2type=N'COLUMN',@level2name=N'PAT_NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'床号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_GROUP', @level2type=N'COLUMN',@level2name=N'BED_CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'是否长期医嘱(0:临时,1:长期)' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_GROUP', @level2type=N'COLUMN',@level2name=N'IS_LONGTERM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'开立科室代码' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_GROUP', @level2type=N'COLUMN',@level2name=N'CREATE_WARD_CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'确认护士代码' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_GROUP', @level2type=N'COLUMN',@level2name=N'CONFIRM_USER_CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'确认护士名称' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_GROUP', @level2type=N'COLUMN',@level2name=N'CONFIRM_USER_NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'确认日期' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_GROUP', @level2type=N'COLUMN',@level2name=N'CONFIRM_DATE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'停止医生' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_GROUP', @level2type=N'COLUMN',@level2name=N'STOP_DOC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'停止医生名称' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_GROUP', @level2type=N'COLUMN',@level2name=N'STOP_DOC_NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'停止日期' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_GROUP', @level2type=N'COLUMN',@level2name=N'STOP_DATE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'频次代码' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_GROUP', @level2type=N'COLUMN',@level2name=N'FREQ_CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'频次名称' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_GROUP', @level2type=N'COLUMN',@level2name=N'FREQ_NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'医嘱类型代码' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_GROUP', @level2type=N'COLUMN',@level2name=N'ORDER_TYPE_CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'医嘱类型名称' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_GROUP', @level2type=N'COLUMN',@level2name=N'ORDER_TYPE_NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'医嘱执行方式代码' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_GROUP', @level2type=N'COLUMN',@level2name=N'ORDER_EXEC_TYPE_CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'医嘱执行方式名称' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_GROUP', @level2type=N'COLUMN',@level2name=N'ORDER_EXEC_TYPE_NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'是否皮试(0:否,1:是)' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_GROUP', @level2type=N'COLUMN',@level2name=N'IS_SKIN_TEST'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'是否紧急(0:否,1:是)' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_GROUP', @level2type=N'COLUMN',@level2name=N'IS_EMERGENT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'备注' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_GROUP', @level2type=N'COLUMN',@level2name=N'REMARK'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'开立日期' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_GROUP', @level2type=N'COLUMN',@level2name=N'CREATE_DATETIME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'开立医生工号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_GROUP', @level2type=N'COLUMN',@level2name=N'CREATE_DOC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'开立医生姓名' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_GROUP', @level2type=N'COLUMN',@level2name=N'CREATE_DOC_NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'医嘱状态代码' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_GROUP', @level2type=N'COLUMN',@level2name=N'ORDER_STATUS_CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'计划时间 ' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_GROUP', @level2type=N'COLUMN',@level2name=N'PLAN_TIME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'医嘱组，存储和医嘱相关的公共信息' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_GROUP'
GO
/****** Object:  Table [dbo].[PAT_ORDER_EXEC]    Script Date: 11/12/2015 14:32:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[PAT_ORDER_EXEC](
	[ID] [bigint] IDENTITY(1,1) NOT NULL,
	[ORDER_GROUP_ID] [varchar](20) NOT NULL,
	[WARD_CODE] [varchar](10) NOT NULL,
	[PAT_ID] [varchar](20) NOT NULL,
	[BARCODE] [varchar](20) NOT NULL,
	[EXEC_TYPE] [varchar](10) NULL,
	[PLAN_DATE] [datetime] NULL,
	[EXEC_DATE] [datetime] NOT NULL,
	[EXEC_USER_CODE] [varchar](10) NOT NULL,
	[EXEC_USER_NAME] [varchar](20) NULL,
	[FINISH_DATE] [datetime] NULL,
	[FINISH_USER_CODE] [varchar](10) NULL,
	[FINISH_USER_NAME] [varchar](20) NULL,
	[APPROVE_USER_CODE] [varchar](10) NULL,
	[APPROVE_USER_NAME] [varchar](20) NULL,
	[ORDER_NO] [int] NULL,
 CONSTRAINT [PK_PAT_ORDER_EXEC] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'医嘱执行记录自增长号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_EXEC', @level2type=N'COLUMN',@level2name=N'ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'医嘱ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_EXEC', @level2type=N'COLUMN',@level2name=N'ORDER_GROUP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'病区代码' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_EXEC', @level2type=N'COLUMN',@level2name=N'WARD_CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'住院病历号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_EXEC', @level2type=N'COLUMN',@level2name=N'PAT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'条码' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_EXEC', @level2type=N'COLUMN',@level2name=N'BARCODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'执行类型' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_EXEC', @level2type=N'COLUMN',@level2name=N'EXEC_TYPE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'计划日期时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_EXEC', @level2type=N'COLUMN',@level2name=N'PLAN_DATE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'执行日期时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_EXEC', @level2type=N'COLUMN',@level2name=N'EXEC_DATE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'执行人工号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_EXEC', @level2type=N'COLUMN',@level2name=N'EXEC_USER_CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'执行人姓名' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_EXEC', @level2type=N'COLUMN',@level2name=N'EXEC_USER_NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'完成日期时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_EXEC', @level2type=N'COLUMN',@level2name=N'FINISH_DATE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'完成人工号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_EXEC', @level2type=N'COLUMN',@level2name=N'FINISH_USER_CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'完成人姓名' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_EXEC', @level2type=N'COLUMN',@level2name=N'FINISH_USER_NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'复核人工号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_EXEC', @level2type=N'COLUMN',@level2name=N'APPROVE_USER_CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'复核人姓名' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_EXEC', @level2type=N'COLUMN',@level2name=N'APPROVE_USER_NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'医嘱执行记录表' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ORDER_EXEC'
GO
/****** Object:  Table [dbo].[PAT_OPERATION_STATUS]    Script Date: 11/12/2015 14:32:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[PAT_OPERATION_STATUS](
	[ID] [bigint] IDENTITY(1,1) NOT NULL,
	[PAT_OPERATION_ID] [bigint] NOT NULL,
	[PAT_ID] [varchar](20) NOT NULL,
	[JOINT_OPERATION_DATE] [datetime] NULL,
	[JOINT_OPERATION_USER] [varchar](30) NULL,
	[JOINT_OPERATION_USER_CODE] [varchar](10) NULL,
	[JOINT_OPERATION_DEPT] [varchar](10) NULL,
	[SEND_OPERATION_DATE] [datetime] NULL,
	[SEND_OPERATION_USER] [varchar](30) NULL,
	[SEND_OPERATION_USER_CODE] [varchar](10) NULL,
	[SEND_OPERATION_DEPT] [varchar](10) NULL,
 CONSTRAINT [PK_PAT_OPERATION_STATUS] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'手术状态自增长号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_OPERATION_STATUS', @level2type=N'COLUMN',@level2name=N'ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'手术号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_OPERATION_STATUS', @level2type=N'COLUMN',@level2name=N'PAT_OPERATION_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'住院病历号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_OPERATION_STATUS', @level2type=N'COLUMN',@level2name=N'PAT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'接手术日期' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_OPERATION_STATUS', @level2type=N'COLUMN',@level2name=N'JOINT_OPERATION_DATE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'接手术人姓名' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_OPERATION_STATUS', @level2type=N'COLUMN',@level2name=N'JOINT_OPERATION_USER'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'接手术人工号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_OPERATION_STATUS', @level2type=N'COLUMN',@level2name=N'JOINT_OPERATION_USER_CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'接手术部门' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_OPERATION_STATUS', @level2type=N'COLUMN',@level2name=N'JOINT_OPERATION_DEPT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'送手术日期' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_OPERATION_STATUS', @level2type=N'COLUMN',@level2name=N'SEND_OPERATION_DATE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'送手术人姓名' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_OPERATION_STATUS', @level2type=N'COLUMN',@level2name=N'SEND_OPERATION_USER'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'送手术人工号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_OPERATION_STATUS', @level2type=N'COLUMN',@level2name=N'SEND_OPERATION_USER_CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'送手术部门' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_OPERATION_STATUS', @level2type=N'COLUMN',@level2name=N'SEND_OPERATION_DEPT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'患者手术状态记录表' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_OPERATION_STATUS'
GO
/****** Object:  Table [dbo].[PAT_OPERATION]    Script Date: 11/12/2015 14:32:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[PAT_OPERATION](
	[ID] [bigint] IDENTITY(1,1) NOT NULL,
	[PAT_ID] [varchar](20) NOT NULL,
	[IN_HOSP_NO] [varchar](20) NOT NULL,
	[WARD_CODE] [varchar](10) NOT NULL,
	[WARD_NAME] [varchar](30) NULL,
	[BED_CODE] [varchar](10) NULL,
	[OP_ROOM] [varchar](10) NULL,
	[OP_STATE] [varchar](1) NULL,
	[OP_STATE_NAME] [varchar](10) NULL,
	[OP_FLAG] [varchar](1) NULL,
	[OP_FLAG_NAME] [varchar](10) NULL,
	[REGISTRATION_DATE] [datetime] NULL,
	[OPERATION_DATE] [datetime] NULL,
	[NOTICE_DATE] [datetime] NULL,
	[DIAGNOSIS] [varchar](50) NULL,
	[SURGEON] [varchar](20) NULL,
	[FIRST_ASSISTANT] [varchar](20) NULL,
	[ANESTHESIOLOGIST] [varchar](20) NULL,
	[SECOND_ASSISTANT] [varchar](20) NULL,
	[NEEDWASH_NURSE] [varchar](1) NOT NULL,
	[WASH_NURSE] [varchar](20) NULL,
	[PATROL_NURSE] [varchar](20) NULL,
	[ANESTHESIA_CONSULTATION] [varchar](2) NULL,
	[EMERGENCY] [varchar](1) NOT NULL,
	[HAA_VALUE] [varchar](4) NULL,
	[RPR_VALUE] [varchar](4) NULL,
	[HIV_VALUE] [varchar](4) NULL,
	[REMARK] [varchar](50) NULL,
	[SYNC_CREATE] [datetime] NULL,
	[SYNC_UPDATE] [datetime] NULL,
 CONSTRAINT [PK_PAT_OPERATION] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'患者手术自增长号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_OPERATION', @level2type=N'COLUMN',@level2name=N'ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'住院流水号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_OPERATION', @level2type=N'COLUMN',@level2name=N'PAT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'住院号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_OPERATION', @level2type=N'COLUMN',@level2name=N'IN_HOSP_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'病区代码' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_OPERATION', @level2type=N'COLUMN',@level2name=N'WARD_CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'病区名称' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_OPERATION', @level2type=N'COLUMN',@level2name=N'WARD_NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'床位号码' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_OPERATION', @level2type=N'COLUMN',@level2name=N'BED_CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'手术室房间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_OPERATION', @level2type=N'COLUMN',@level2name=N'OP_ROOM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'手术状态' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_OPERATION', @level2type=N'COLUMN',@level2name=N'OP_STATE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'手术状态名称' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_OPERATION', @level2type=N'COLUMN',@level2name=N'OP_STATE_NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'手术标记' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_OPERATION', @level2type=N'COLUMN',@level2name=N'OP_FLAG'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'手术标记名称' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_OPERATION', @level2type=N'COLUMN',@level2name=N'OP_FLAG_NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'登记日期时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_OPERATION', @level2type=N'COLUMN',@level2name=N'REGISTRATION_DATE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'手术日期时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_OPERATION', @level2type=N'COLUMN',@level2name=N'OPERATION_DATE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'通知日期时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_OPERATION', @level2type=N'COLUMN',@level2name=N'NOTICE_DATE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'诊断' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_OPERATION', @level2type=N'COLUMN',@level2name=N'DIAGNOSIS'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'外科医生' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_OPERATION', @level2type=N'COLUMN',@level2name=N'SURGEON'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'手术一助' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_OPERATION', @level2type=N'COLUMN',@level2name=N'FIRST_ASSISTANT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'麻醉师' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_OPERATION', @level2type=N'COLUMN',@level2name=N'ANESTHESIOLOGIST'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'手术二助' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_OPERATION', @level2type=N'COLUMN',@level2name=N'SECOND_ASSISTANT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'洗手护士需否(0:否,1:是)' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_OPERATION', @level2type=N'COLUMN',@level2name=N'NEEDWASH_NURSE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'洗手护士' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_OPERATION', @level2type=N'COLUMN',@level2name=N'WASH_NURSE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'巡回护士' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_OPERATION', @level2type=N'COLUMN',@level2name=N'PATROL_NURSE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'麻醉会诊' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_OPERATION', @level2type=N'COLUMN',@level2name=N'ANESTHESIA_CONSULTATION'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'是否急诊(0:否,1:是)' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_OPERATION', @level2type=N'COLUMN',@level2name=N'EMERGENCY'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'HAA' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_OPERATION', @level2type=N'COLUMN',@level2name=N'HAA_VALUE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'RPR' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_OPERATION', @level2type=N'COLUMN',@level2name=N'RPR_VALUE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'HIV' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_OPERATION', @level2type=N'COLUMN',@level2name=N'HIV_VALUE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'备注' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_OPERATION', @level2type=N'COLUMN',@level2name=N'REMARK'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'创建时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_OPERATION', @level2type=N'COLUMN',@level2name=N'SYNC_CREATE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'修改时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_OPERATION', @level2type=N'COLUMN',@level2name=N'SYNC_UPDATE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'患者手术表' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_OPERATION'
GO
/****** Object:  Table [dbo].[PAT_LONGORDERTIME]    Script Date: 11/12/2015 14:32:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[PAT_LONGORDERTIME](
	[PAT_ID] [varchar](20) NOT NULL,
	[ORDER_NO] [int] NOT NULL,
	[NOWTIME] [datetime] NULL,
	[NEXTTIME] [datetime] NULL,
 CONSTRAINT [PK_PAT_LONGORDERTIME] PRIMARY KEY CLUSTERED 
(
	[PAT_ID] ASC,
	[ORDER_NO] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'患者每次住院唯一标识' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_LONGORDERTIME', @level2type=N'COLUMN',@level2name=N'PAT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'医嘱组号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_LONGORDERTIME', @level2type=N'COLUMN',@level2name=N'ORDER_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'本次分解时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_LONGORDERTIME', @level2type=N'COLUMN',@level2name=N'NOWTIME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'下次分解时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_LONGORDERTIME', @level2type=N'COLUMN',@level2name=N'NEXTTIME'
GO
/****** Object:  Table [dbo].[PAT_LAB_TEST_MAS]    Script Date: 11/12/2015 14:32:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[PAT_LAB_TEST_MAS](
	[ID] [bigint] IDENTITY(1,1) NOT NULL,
	[TEST_NO] [varchar](50) NOT NULL,
	[PAT_ID] [varchar](20) NULL,
	[IN_HOSP_NO] [varchar](20) NOT NULL,
	[TEST_SUBJECT] [varchar](1000) NULL,
	[TEST_SPECIMEN] [varchar](20) NOT NULL,
	[SEND_PEOPLE] [varchar](20) NULL,
	[SEND_DATE] [datetime] NULL,
	[TEST_PEOPLE] [varchar](20) NULL,
	[TEST_DATE] [datetime] NULL,
	[REPORT_PEOPLE] [varchar](20) NULL,
	[REPORT_DATE] [datetime] NULL,
	[CHECK_PEOPLE] [varchar](20) NULL,
	[CHECK_DATE] [datetime] NULL,
	[STATUS] [varchar](1) NULL,
	[SYNC_CREATE] [datetime] NULL,
	[SYNC_UPDATE] [datetime] NULL,
	[PAT_NAME] [varchar](20) NULL,
	[GENDER] [varchar](1) NULL,
	[BED_CODE] [varchar](10) NULL,
	[BARCODE] [varchar](20) NULL,
	[ORDER_ID] [varchar](20) NULL,
	[TUBECOLOR] [varchar](30) NULL,
	[EXAM_DATE_TIME] [datetime] NULL
 CONSTRAINT [PK_PAT_LAB_TEST_MAS_1] PRIMARY KEY CLUSTERED 
(
	[TEST_NO] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON, FILLFACTOR = 100) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'检验报告自增长号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_LAB_TEST_MAS', @level2type=N'COLUMN',@level2name=N'ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'申请单号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_LAB_TEST_MAS', @level2type=N'COLUMN',@level2name=N'TEST_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'住院病历号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_LAB_TEST_MAS', @level2type=N'COLUMN',@level2name=N'PAT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'住院流水号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_LAB_TEST_MAS', @level2type=N'COLUMN',@level2name=N'IN_HOSP_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'主题' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_LAB_TEST_MAS', @level2type=N'COLUMN',@level2name=N'TEST_SUBJECT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'样本' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_LAB_TEST_MAS', @level2type=N'COLUMN',@level2name=N'TEST_SPECIMEN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'申请人' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_LAB_TEST_MAS', @level2type=N'COLUMN',@level2name=N'SEND_PEOPLE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'申请日期时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_LAB_TEST_MAS', @level2type=N'COLUMN',@level2name=N'SEND_DATE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'测试人' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_LAB_TEST_MAS', @level2type=N'COLUMN',@level2name=N'TEST_PEOPLE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'测试日期时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_LAB_TEST_MAS', @level2type=N'COLUMN',@level2name=N'TEST_DATE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'报告人' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_LAB_TEST_MAS', @level2type=N'COLUMN',@level2name=N'REPORT_PEOPLE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'报告日期时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_LAB_TEST_MAS', @level2type=N'COLUMN',@level2name=N'REPORT_DATE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'复核人' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_LAB_TEST_MAS', @level2type=N'COLUMN',@level2name=N'CHECK_PEOPLE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'复核日期时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_LAB_TEST_MAS', @level2type=N'COLUMN',@level2name=N'CHECK_DATE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'检查报告状态：E-执行，R-报告，C-审核' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_LAB_TEST_MAS', @level2type=N'COLUMN',@level2name=N'STATUS'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'创建时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_LAB_TEST_MAS', @level2type=N'COLUMN',@level2name=N'SYNC_CREATE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'修改时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_LAB_TEST_MAS', @level2type=N'COLUMN',@level2name=N'SYNC_UPDATE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'患者姓名' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_LAB_TEST_MAS', @level2type=N'COLUMN',@level2name=N'PAT_NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'性别(F:女,M:男)' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_LAB_TEST_MAS', @level2type=N'COLUMN',@level2name=N'GENDER'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'COM_BED(床位代码)' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_LAB_TEST_MAS', @level2type=N'COLUMN',@level2name=N'BED_CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'条码' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_LAB_TEST_MAS', @level2type=N'COLUMN',@level2name=N'BARCODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'医嘱对应id' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_LAB_TEST_MAS', @level2type=N'COLUMN',@level2name=N'ORDER_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'患者检验报告' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_LAB_TEST_MAS',@level2type=N'COLUMN',@level2name=N'TUBECOLOR' 
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'检查时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_LAB_TEST_MAS',@level2type=N'COLUMN',@level2name=N'EXAM_DATE_TIME'
GO
/****** Object:  Table [dbo].[PAT_LAB_TEST_DETAIL]    Script Date: 11/12/2015 14:32:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[PAT_LAB_TEST_DETAIL](
	[ID] [bigint] IDENTITY(1,1) NOT NULL,
	[TEST_NO] [varchar](50) NOT NULL,
	[ITEM_NO] [int] NOT NULL,
	[ITEM_CODE] [varchar](20) NULL,
	[ITEM_NAME] [varchar](60) NULL,
	[RESULT_VALUE] [varchar](20) NULL,
	[RESULT_UNIT] [varchar](20) NULL,
	[REF_RANGES] [varchar](20) NULL,
	[NORMAL_FLAG] [varchar](1) NULL,
	[SYNC_CREATE] [datetime] NULL,
	[SYNC_UPDATE] [datetime] NULL,
 CONSTRAINT [PK_PAT_LAB_TEST_DETAIL_1] PRIMARY KEY CLUSTERED 
(
	[TEST_NO] ASC,
	[ITEM_NO] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON, FILLFACTOR = 100) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_LAB_TEST_DETAIL', @level2type=N'COLUMN',@level2name=N'ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'申请单号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_LAB_TEST_DETAIL', @level2type=N'COLUMN',@level2name=N'TEST_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'项目序号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_LAB_TEST_DETAIL', @level2type=N'COLUMN',@level2name=N'ITEM_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'项目代码' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_LAB_TEST_DETAIL', @level2type=N'COLUMN',@level2name=N'ITEM_CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'项目名称' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_LAB_TEST_DETAIL', @level2type=N'COLUMN',@level2name=N'ITEM_NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'测试结果' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_LAB_TEST_DETAIL', @level2type=N'COLUMN',@level2name=N'RESULT_VALUE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'单位' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_LAB_TEST_DETAIL', @level2type=N'COLUMN',@level2name=N'RESULT_UNIT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'参考范围' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_LAB_TEST_DETAIL', @level2type=N'COLUMN',@level2name=N'REF_RANGES'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'正常标志:H-High， L-Low，N-Normal, A-Abnormal' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_LAB_TEST_DETAIL', @level2type=N'COLUMN',@level2name=N'NORMAL_FLAG'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'创建时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_LAB_TEST_DETAIL', @level2type=N'COLUMN',@level2name=N'SYNC_CREATE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'修改时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_LAB_TEST_DETAIL', @level2type=N'COLUMN',@level2name=N'SYNC_UPDATE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'检验报告详情' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_LAB_TEST_DETAIL'
GO
/****** Object:  Table [dbo].[PAT_INSPECTION_ITEM]    Script Date: 11/12/2015 14:32:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[PAT_INSPECTION_ITEM](
	[EXAM_NO] [varchar](32) NOT NULL,
	[EXAM_ITEM_NO] [int] NOT NULL,
	[EXAM_ITEM] [varchar](40) NULL,
	[EXAM_ITEM_CODE] [varchar](10) NULL,
 CONSTRAINT [PK_PAT_INSPECTION_ITEM] PRIMARY KEY CLUSTERED 
(
	[EXAM_NO] ASC,
	[EXAM_ITEM_NO] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON, FILLFACTOR = 100) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'申请序号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_INSPECTION_ITEM', @level2type=N'COLUMN',@level2name=N'EXAM_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'项目序号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_INSPECTION_ITEM', @level2type=N'COLUMN',@level2name=N'EXAM_ITEM_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'EXAM_ITEM' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_INSPECTION_ITEM', @level2type=N'COLUMN',@level2name=N'EXAM_ITEM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'项目代码' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_INSPECTION_ITEM', @level2type=N'COLUMN',@level2name=N'EXAM_ITEM_CODE'
GO
/****** Object:  Table [dbo].[PAT_INSPECTION]    Script Date: 11/12/2015 14:32:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[PAT_INSPECTION](
	[ID] [bigint] IDENTITY(1,1) NOT NULL,
	[EXAM_NO] [varchar](32) NOT NULL,
	[PAT_ID] [varchar](20) NULL,
	[IN_HOSP_NO] [varchar](20) NULL,
	[PAT_NAME] [varchar](20) NOT NULL,
	[BED_CODE] [varchar](10) NULL,
	[SUBJECT] [varchar](100) NULL,
	[BODYPARTS] [varchar](100) NULL,
	[INSPECTION_RESULT] [varchar](2000) NULL,
	[INSPECTION_SUGGESTION] [varchar](2000) NULL,
	[APPLY_USER] [varchar](20) NULL,
	[APPLY_DATE] [datetime] NULL,
	[REPORT_USER] [varchar](50) NULL,
	[REPORT_DATE] [datetime] NULL,
	[EXAM_DATE_TIME] [datetime] NULL,
	[TECHNICIAN] [varchar](20) NULL,
	[CHECK_USER] [varchar](20) NULL,
	[CHECK_DATE] [datetime] NULL,
	[IS_ABNORMAL] [int] NULL,
	[SYNC_CREATE] [datetime] NULL,
	[SYNC_UPDATE] [datetime] NULL,
	[DEVICETYPENAME] [varchar](50) NULL,
 CONSTRAINT [PK_PAT_INSPECTION] PRIMARY KEY CLUSTERED 
(
	[EXAM_NO] ASC,
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON, FILLFACTOR = 100) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'检查记录自增长号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_INSPECTION', @level2type=N'COLUMN',@level2name=N'ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'检查记录自增长号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_INSPECTION', @level2type=N'COLUMN',@level2name=N'EXAM_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'住院病历号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_INSPECTION', @level2type=N'COLUMN',@level2name=N'PAT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'住院流水号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_INSPECTION', @level2type=N'COLUMN',@level2name=N'IN_HOSP_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'患者姓名' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_INSPECTION', @level2type=N'COLUMN',@level2name=N'PAT_NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'床位号码' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_INSPECTION', @level2type=N'COLUMN',@level2name=N'BED_CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'主题' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_INSPECTION', @level2type=N'COLUMN',@level2name=N'SUBJECT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'部位' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_INSPECTION', @level2type=N'COLUMN',@level2name=N'BODYPARTS'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'检查结果' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_INSPECTION', @level2type=N'COLUMN',@level2name=N'INSPECTION_RESULT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'检查建议' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_INSPECTION', @level2type=N'COLUMN',@level2name=N'INSPECTION_SUGGESTION'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'申请人' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_INSPECTION', @level2type=N'COLUMN',@level2name=N'APPLY_USER'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'申请日期' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_INSPECTION', @level2type=N'COLUMN',@level2name=N'APPLY_DATE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'报告人' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_INSPECTION', @level2type=N'COLUMN',@level2name=N'REPORT_USER'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'报告日期' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_INSPECTION', @level2type=N'COLUMN',@level2name=N'REPORT_DATE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'检查时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_INSPECTION', @level2type=N'COLUMN',@level2name=N'EXAM_DATE_TIME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'操作者' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_INSPECTION', @level2type=N'COLUMN',@level2name=N'TECHNICIAN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'复核人' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_INSPECTION', @level2type=N'COLUMN',@level2name=N'CHECK_USER'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'复核日期' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_INSPECTION', @level2type=N'COLUMN',@level2name=N'CHECK_DATE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'1-阳性，即检查可能有病变，其他为阴性' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_INSPECTION', @level2type=N'COLUMN',@level2name=N'IS_ABNORMAL'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'创建时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_INSPECTION', @level2type=N'COLUMN',@level2name=N'SYNC_CREATE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'修改时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_INSPECTION', @level2type=N'COLUMN',@level2name=N'SYNC_UPDATE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'报告类型（超声、内窥、CT等）' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_INSPECTION', @level2type=N'COLUMN',@level2name=N'DEVICETYPENAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'患者检查报告' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_INSPECTION'
GO
/****** Object:  Table [dbo].[PAT_INFUSION_MONITOR_ITEM]    Script Date: 11/12/2015 14:32:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[PAT_INFUSION_MONITOR_ITEM](
	[INFU_MONITOR_ID] [bigint] IDENTITY(1,1) NOT NULL,
	[ORDER_EXEC_ID] [varchar](30) NOT NULL,
	[REC_DATETIME] [datetime] NULL,
	[REC_NURSE_CODE] [varchar](30) NOT NULL,
	[REC_NURSE_NAME] [varchar](30) NULL,
	[ABNORMAL] [int] NULL,
	[DELIVER_SPEED] [int] NULL,
	[ANOMALY_MSG] [varchar](200) NULL,
	[ANOMALY_DISPOSAL] [varchar](200) NULL,
	[RESIDUE] [int] NULL,
	[STATUS] [varchar](1) NULL,
	[SPEED_UNIT] [varchar](20) NULL,
 CONSTRAINT [PK_PAT_INFUSION_MONITOR_ITEM] PRIMARY KEY CLUSTERED 
(
	[INFU_MONITOR_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'巡视记录ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_INFUSION_MONITOR_ITEM', @level2type=N'COLUMN',@level2name=N'INFU_MONITOR_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'医嘱执行ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_INFUSION_MONITOR_ITEM', @level2type=N'COLUMN',@level2name=N'ORDER_EXEC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'记录时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_INFUSION_MONITOR_ITEM', @level2type=N'COLUMN',@level2name=N'REC_DATETIME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'记录人' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_INFUSION_MONITOR_ITEM', @level2type=N'COLUMN',@level2name=N'REC_NURSE_CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'记录人姓名' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_INFUSION_MONITOR_ITEM', @level2type=N'COLUMN',@level2name=N'REC_NURSE_NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'异常标记' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_INFUSION_MONITOR_ITEM', @level2type=N'COLUMN',@level2name=N'ABNORMAL'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'滴速' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_INFUSION_MONITOR_ITEM', @level2type=N'COLUMN',@level2name=N'DELIVER_SPEED'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'异常信息' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_INFUSION_MONITOR_ITEM', @level2type=N'COLUMN',@level2name=N'ANOMALY_MSG'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'异常处理' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_INFUSION_MONITOR_ITEM', @level2type=N'COLUMN',@level2name=N'ANOMALY_DISPOSAL'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'剩余量' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_INFUSION_MONITOR_ITEM', @level2type=N'COLUMN',@level2name=N'RESIDUE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'状态' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_INFUSION_MONITOR_ITEM', @level2type=N'COLUMN',@level2name=N'STATUS'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'输液巡视详细记录表' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_INFUSION_MONITOR_ITEM'
GO
/****** Object:  Table [dbo].[PAT_INFUSION_MONITOR]    Script Date: 11/12/2015 14:32:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[PAT_INFUSION_MONITOR](
	[ORDER_EXEC_ID] [varchar](30) NOT NULL,
	[PAT_ID] [varchar](20) NOT NULL,
	[PAT_NAME] [varchar](20) NULL,
	[WARD_CODE] [varchar](10) NOT NULL,
	[BED_CODE] [varchar](10) NULL,
	[ORDER_CONTENT] [varchar](500) NULL,
	[USAGE_NAME] [varchar](30) NULL,
	[FREQ_CODE] [varchar](30) NULL,
	[ORDER_STATUS] [varchar](1) NULL,
 CONSTRAINT [PK_PAT_INFUSION_MONITOR] PRIMARY KEY NONCLUSTERED 
(
	[ORDER_EXEC_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'医嘱执行ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_INFUSION_MONITOR', @level2type=N'COLUMN',@level2name=N'ORDER_EXEC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'住院流水号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_INFUSION_MONITOR', @level2type=N'COLUMN',@level2name=N'PAT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'姓名' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_INFUSION_MONITOR', @level2type=N'COLUMN',@level2name=N'PAT_NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'病区代码' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_INFUSION_MONITOR', @level2type=N'COLUMN',@level2name=N'WARD_CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'床号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_INFUSION_MONITOR', @level2type=N'COLUMN',@level2name=N'BED_CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'医嘱内容' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_INFUSION_MONITOR', @level2type=N'COLUMN',@level2name=N'ORDER_CONTENT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'用法' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_INFUSION_MONITOR', @level2type=N'COLUMN',@level2name=N'USAGE_NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'频次' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_INFUSION_MONITOR', @level2type=N'COLUMN',@level2name=N'FREQ_CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'医嘱状态' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_INFUSION_MONITOR', @level2type=N'COLUMN',@level2name=N'ORDER_STATUS'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'患者输液巡视记录' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_INFUSION_MONITOR'
GO
/****** Object:  Table [dbo].[PAT_INFO_STAT]    Script Date: 11/12/2015 14:32:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[PAT_INFO_STAT](
	[SEQ_ID] [int] IDENTITY(1,1) NOT NULL,
	[PAT_ID] [varchar](20) NULL,
	[IF_OPERATION] [char](1) NULL,
	[IF_FEVER] [char](1) NULL,
	[FALL_SCORE] [char](1) NULL,
	[PRESSURE_SORE] [char](1) NULL,
	[IF_SEPARATE] [char](1) NULL,
	[CREATE_TIME] [datetime] NULL,
 CONSTRAINT [PK_PAT_INFO_STAT] PRIMARY KEY CLUSTERED 
(
	[SEQ_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY],
 CONSTRAINT [UQ_PAT_INFO_STAT_1] UNIQUE NONCLUSTERED 
(
	[PAT_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'自增长流水号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_INFO_STAT', @level2type=N'COLUMN',@level2name=N'SEQ_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'患者id' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_INFO_STAT', @level2type=N'COLUMN',@level2name=N'PAT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'是否手术   1:是 0：否' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_INFO_STAT', @level2type=N'COLUMN',@level2name=N'IF_OPERATION'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'是否发热    1:是 0：否' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_INFO_STAT', @level2type=N'COLUMN',@level2name=N'IF_FEVER'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'是否防跌 0 否 1 是' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_INFO_STAT', @level2type=N'COLUMN',@level2name=N'FALL_SCORE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'是否防压疮 0 否 1 是' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_INFO_STAT', @level2type=N'COLUMN',@level2name=N'PRESSURE_SORE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'是否隔离 0 不隔离 1 隔离' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_INFO_STAT', @level2type=N'COLUMN',@level2name=N'IF_SEPARATE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'创建时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_INFO_STAT', @level2type=N'COLUMN',@level2name=N'CREATE_TIME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'患者特殊信息设置' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_INFO_STAT'
GO

/****** Object:  Table [dbo].[PAT_DRUGBAGRECLIST]    Script Date: 11/12/2015 14:32:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[PAT_DRUGBAGRECLIST](
	[BARCODE] [varchar](13) NOT NULL,
	[PAT_ID] [varchar](20) NULL,
	[HISDRUGCD] [varchar](50) NOT NULL,
	[BED_CODE] [varchar](20) NULL,
	[STANDARDDOSE] [varchar](32) NOT NULL,
	[MAKERNM] [varchar](100) NOT NULL,
	[D_DISPENSEDOSE] [decimal](12, 4) NULL,
	[UNITNM_D_D] [varchar](20) NULL,
	[DT_TAKEDATE] [datetime] NULL,
	[TAKETIMENM] [varchar](40) NULL,
	[DTAMARK] [varchar](1) NULL,
	[N_DIVDRUGBAGCOUNT] [int] NULL,
	[DT_MAKETIME] [datetime] NULL,
	[N_ORDER_NO] [bigint] NULL,
	[N_ORDER_SUB_NO] [bigint] NULL,
	[N_PRESCNO] [bigint] NULL,
	[DRUGNM] [varchar](230) NULL,
	[FILLER] [varchar](40) NULL,
	[DRUGCD] [varchar](12) NULL,
	[PAT_NAME] [varchar](100) NULL,
	[N_BAG_NO] [bigint] NULL,
	[WARD_CODE] [varchar](40) NULL,
	[WARD_NAME] [varchar](40) NULL,
 CONSTRAINT [PK_PAT_DRUGBAGRECLIST] PRIMARY KEY CLUSTERED 
(
	[BARCODE] ASC,
	[HISDRUGCD] ASC,
	[STANDARDDOSE] ASC,
	[MAKERNM] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'药物条码' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_DRUGBAGRECLIST', @level2type=N'COLUMN',@level2name=N'BARCODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'住院流水号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_DRUGBAGRECLIST', @level2type=N'COLUMN',@level2name=N'PAT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'历史药品编号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_DRUGBAGRECLIST', @level2type=N'COLUMN',@level2name=N'HISDRUGCD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'床位号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_DRUGBAGRECLIST', @level2type=N'COLUMN',@level2name=N'BED_CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'标准剂量' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_DRUGBAGRECLIST', @level2type=N'COLUMN',@level2name=N'STANDARDDOSE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'操作者姓名' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_DRUGBAGRECLIST', @level2type=N'COLUMN',@level2name=N'MAKERNM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'分配剂量' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_DRUGBAGRECLIST', @level2type=N'COLUMN',@level2name=N'D_DISPENSEDOSE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'药品单位' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_DRUGBAGRECLIST', @level2type=N'COLUMN',@level2name=N'UNITNM_D_D'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'发药时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_DRUGBAGRECLIST', @level2type=N'COLUMN',@level2name=N'DT_TAKEDATE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'发药药品名称' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_DRUGBAGRECLIST', @level2type=N'COLUMN',@level2name=N'TAKETIMENM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'药品标记' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_DRUGBAGRECLIST', @level2type=N'COLUMN',@level2name=N'DTAMARK'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'药袋药品个数' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_DRUGBAGRECLIST', @level2type=N'COLUMN',@level2name=N'N_DIVDRUGBAGCOUNT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'药袋生成时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_DRUGBAGRECLIST', @level2type=N'COLUMN',@level2name=N'DT_MAKETIME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'医嘱编号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_DRUGBAGRECLIST', @level2type=N'COLUMN',@level2name=N'N_ORDER_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'子医嘱编号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_DRUGBAGRECLIST', @level2type=N'COLUMN',@level2name=N'N_ORDER_SUB_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'处方编号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_DRUGBAGRECLIST', @level2type=N'COLUMN',@level2name=N'N_PRESCNO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'药品名称' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_DRUGBAGRECLIST', @level2type=N'COLUMN',@level2name=N'DRUGNM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'装药人' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_DRUGBAGRECLIST', @level2type=N'COLUMN',@level2name=N'FILLER'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'药品编号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_DRUGBAGRECLIST', @level2type=N'COLUMN',@level2name=N'DRUGCD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'对应病人姓名' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_DRUGBAGRECLIST', @level2type=N'COLUMN',@level2name=N'PAT_NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'药袋号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_DRUGBAGRECLIST', @level2type=N'COLUMN',@level2name=N'N_BAG_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'部门编号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_DRUGBAGRECLIST', @level2type=N'COLUMN',@level2name=N'WARD_CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'部门名称' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_DRUGBAGRECLIST', @level2type=N'COLUMN',@level2name=N'WARD_NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'患者包药机信息' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_DRUGBAGRECLIST'
GO
/****** Object:  Table [dbo].[PAT_DIAGNOSIS]    Script Date: 11/12/2015 14:32:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[PAT_DIAGNOSIS](
	[ID] [bigint] IDENTITY(1,1) NOT NULL,
	[PAT_ID] [varchar](20) NOT NULL,
	[DIAGNOSIS_INFO] [text] NOT NULL,
	[DIAGNOSIS_DATE] [datetime] NOT NULL,
	[IS_IN_DIAG] [varchar](1) NOT NULL,
	[RECORD_USER] [varchar](10) NULL,
	[RECORD_DATE] [datetime] NULL,
	[FLAG] [varchar](1) NOT NULL,
 CONSTRAINT [PK_PAT_DIAGNOSIS] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'患者诊断记录自增长号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_DIAGNOSIS', @level2type=N'COLUMN',@level2name=N'ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'住院流水号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_DIAGNOSIS', @level2type=N'COLUMN',@level2name=N'PAT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'诊断信息' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_DIAGNOSIS', @level2type=N'COLUMN',@level2name=N'DIAGNOSIS_INFO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'诊断日期' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_DIAGNOSIS', @level2type=N'COLUMN',@level2name=N'DIAGNOSIS_DATE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'是否入院诊断(0:否,1:是)' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_DIAGNOSIS', @level2type=N'COLUMN',@level2name=N'IS_IN_DIAG'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'记录人工号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_DIAGNOSIS', @level2type=N'COLUMN',@level2name=N'RECORD_USER'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'记录日期' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_DIAGNOSIS', @level2type=N'COLUMN',@level2name=N'RECORD_DATE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'标记(0:无效,1:有效)' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_DIAGNOSIS', @level2type=N'COLUMN',@level2name=N'FLAG'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'患者诊断记录表' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_DIAGNOSIS'
GO
/****** Object:  Table [dbo].[PAT_CURE_INFO]    Script Date: 11/12/2015 14:32:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[PAT_CURE_INFO](
	[ID] [bigint] IDENTITY(1,1) NOT NULL,
	[PAT_ID] [varchar](20) NOT NULL,
	[IN_HOSP_NO] [varchar](20) NOT NULL,
	[BED_CODE] [varchar](10) NULL,
	[WARD_CODE] [varchar](10) NULL,
	[BARCODE] [varchar](20) NULL,
	[NAME] [varchar](20) NULL,
	[GENDER] [varchar](1) NULL,
	[PERSON_ID] [varchar](20) NULL,
	[BIRTH_DATE] [datetime] NULL,
	[CONTACT_PERSON] [varchar](20) NULL,
	[CONTACT_PHONE] [varchar](20) NULL,
	[CONTACT_ADDRESS] [varchar](100) NULL,
	[IS_BABY] [varchar](1) NULL,
	[DANGER_LEVEL] [varchar](2) NULL,
	[NURSE_LEVEL] [varchar](2) NULL,
	[CHARGE_TYPE] [varchar](50) NULL,
	[CHARGE_TYPE_NAME] [varchar](50) NULL,
	[DOCTOR_CODE] [varchar](10) NULL,
	[DOCTOR_NAME] [varchar](20) NULL,
	[DUTY_NURSE_CODE] [varchar](10) NULL,
	[DUTY_NURSE_NAME] [varchar](20) NULL,
	[IN_DATE] [datetime] NULL,
	[IN_DIAG] [varchar](80) NULL,
	[OUT_DATE] [datetime] NULL,
	[OUT_DIAG] [varchar](50) NULL,
	[STATUS] [varchar](1) NULL,
	[DIET_NAME] [varchar](80) NULL,
	[PREPAY_COST] [decimal](10, 2) NULL,
	[OWN_COST] [decimal](10, 2) NULL,
	[BALANCE] [decimal](10, 2) NULL,
	[REMARK] [varchar](50) NULL,
	[ALLERGEN] [varchar](100) NULL,
	[ADVERSE_REACTION_DRUGS] [varchar](100) NULL,
	[SYNC_CREATE] [datetime] NULL,
	[SYNC_UPDATE] [datetime] NULL,
	[ISSEPARATE] [int] NULL,
	[MARITAL_STATUS] [varchar](4) NULL,
	[OCCUPATION] [char](1) NULL,
	[EDUCATION] [varchar](20) NULL,
	[HOMETOWN] [varchar](20) NULL,
	[RELIGION] [varchar](20) NULL,
	[SOURCE] [varchar](20) NULL,
	[DAILYCAREGIVERS] [varchar](20) NULL,
	[ADMISSIONMODE] [varchar](20) NULL,
	[INP_NO] [varchar](30) NULL,
	[APPEAL] varchar(4000) NULL
 CONSTRAINT [PK_PAT_CURE_INFO_1] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'患者记录自增长号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_CURE_INFO', @level2type=N'COLUMN',@level2name=N'ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'住院流水号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_CURE_INFO', @level2type=N'COLUMN',@level2name=N'PAT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'住院号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_CURE_INFO', @level2type=N'COLUMN',@level2name=N'IN_HOSP_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'COM_BED(床位代码)' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_CURE_INFO', @level2type=N'COLUMN',@level2name=N'BED_CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'病区代码' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_CURE_INFO', @level2type=N'COLUMN',@level2name=N'WARD_CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'患者条码' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_CURE_INFO', @level2type=N'COLUMN',@level2name=N'BARCODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'姓名' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_CURE_INFO', @level2type=N'COLUMN',@level2name=N'NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'性别(F:女,M:男)' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_CURE_INFO', @level2type=N'COLUMN',@level2name=N'GENDER'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'身份证' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_CURE_INFO', @level2type=N'COLUMN',@level2name=N'PERSON_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'出生日期' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_CURE_INFO', @level2type=N'COLUMN',@level2name=N'BIRTH_DATE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'联系人' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_CURE_INFO', @level2type=N'COLUMN',@level2name=N'CONTACT_PERSON'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'联系电话' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_CURE_INFO', @level2type=N'COLUMN',@level2name=N'CONTACT_PHONE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'联系地址' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_CURE_INFO', @level2type=N'COLUMN',@level2name=N'CONTACT_ADDRESS'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'是否婴儿(0:否,1:是)' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_CURE_INFO', @level2type=N'COLUMN',@level2name=N'IS_BABY'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'危险级别(N:普通,  S:病重, D:重危,E:死亡)' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_CURE_INFO', @level2type=N'COLUMN',@level2name=N'DANGER_LEVEL'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'护理级别(0:特级,1:一级,2:二级,3:三级护理)' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_CURE_INFO', @level2type=N'COLUMN',@level2name=N'NURSE_LEVEL'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'收费类型' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_CURE_INFO', @level2type=N'COLUMN',@level2name=N'CHARGE_TYPE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'收费类型名称' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_CURE_INFO', @level2type=N'COLUMN',@level2name=N'CHARGE_TYPE_NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'医生代码' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_CURE_INFO', @level2type=N'COLUMN',@level2name=N'DOCTOR_CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'医生姓名' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_CURE_INFO', @level2type=N'COLUMN',@level2name=N'DOCTOR_NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'责任护士代码' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_CURE_INFO', @level2type=N'COLUMN',@level2name=N'DUTY_NURSE_CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'责任护士名称' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_CURE_INFO', @level2type=N'COLUMN',@level2name=N'DUTY_NURSE_NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'入院日期' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_CURE_INFO', @level2type=N'COLUMN',@level2name=N'IN_DATE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'入院诊断' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_CURE_INFO', @level2type=N'COLUMN',@level2name=N'IN_DIAG'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'出院日期' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_CURE_INFO', @level2type=N'COLUMN',@level2name=N'OUT_DATE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'出院诊断' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_CURE_INFO', @level2type=N'COLUMN',@level2name=N'OUT_DIAG'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'状态(0:否,1:是)' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_CURE_INFO', @level2type=N'COLUMN',@level2name=N'STATUS'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'饮食' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_CURE_INFO', @level2type=N'COLUMN',@level2name=N'DIET_NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'预交费用' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_CURE_INFO', @level2type=N'COLUMN',@level2name=N'PREPAY_COST'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'自费金额' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_CURE_INFO', @level2type=N'COLUMN',@level2name=N'OWN_COST'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'余额' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_CURE_INFO', @level2type=N'COLUMN',@level2name=N'BALANCE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'备注' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_CURE_INFO', @level2type=N'COLUMN',@level2name=N'REMARK'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'药品过敏' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_CURE_INFO', @level2type=N'COLUMN',@level2name=N'ALLERGEN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'不良反应药物' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_CURE_INFO', @level2type=N'COLUMN',@level2name=N'ADVERSE_REACTION_DRUGS'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'创建时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_CURE_INFO', @level2type=N'COLUMN',@level2name=N'SYNC_CREATE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'修改时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_CURE_INFO', @level2type=N'COLUMN',@level2name=N'SYNC_UPDATE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'婚姻状况' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_CURE_INFO', @level2type=N'COLUMN',@level2name=N'MARITAL_STATUS'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'职业' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_CURE_INFO', @level2type=N'COLUMN',@level2name=N'OCCUPATION'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'文化程度  不识字、小学、初中、高中、中专、大专或大专以上' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_CURE_INFO', @level2type=N'COLUMN',@level2name=N'EDUCATION'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'籍贯' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_CURE_INFO', @level2type=N'COLUMN',@level2name=N'HOMETOWN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'宗教' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_CURE_INFO', @level2type=N'COLUMN',@level2name=N'RELIGION'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'资料来源' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_CURE_INFO', @level2type=N'COLUMN',@level2name=N'SOURCE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'日常照顾者' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_CURE_INFO', @level2type=N'COLUMN',@level2name=N'DAILYCAREGIVERS'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'入院方式 急诊、门诊、转诊、步行、扶行、轮椅、平车、抱入' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_CURE_INFO', @level2type=N'COLUMN',@level2name=N'ADMISSIONMODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'患者基本治疗信息表' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_CURE_INFO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'主诉' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_CURE_INFO', @level2type=N'COLUMN',@level2name=N'APPEAL'
GO
/****** Object:  Table [dbo].[PAT_CRITICAL_VALUE]    Script Date: 11/12/2015 14:32:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[PAT_CRITICAL_VALUE](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[PAT_ID] [varchar](20) NOT NULL,
	[IN_HOSP_NO] [varchar](20) NOT NULL,
	[WARD_CODE] [varchar](10) NOT NULL,
	[BED_CODE] [varchar](10) NOT NULL,
	[SEND_PEOPLE] [varchar](20) NULL,
	[SEND_TIME] [datetime] NOT NULL,
	[CRITICAL_VALUE] [varchar](500) NULL,
	[DISPOSE_NURSE_CODE] [varchar](10) NULL,
	[DISPOSE_NURSE_NAME] [varchar](20) NULL,
	[DISPOSE_DATE] [datetime] NULL,
	[DISPOSE_DOCTOR_CODE] [varchar](10) NULL,
	[DISPOSE_DOCTOR_NAME] [varchar](20) NULL,
	[DISPOSE_DOCTOR_DATE] [datetime] NULL,
	[DISPOSE_STATUS] [varchar](1) NOT NULL,
	[SYNC_CREATE] [datetime] NULL,
	[SYNC_UPDATE] [datetime] NULL,
	[TEST_NO] [varchar](20) NULL,
 CONSTRAINT [PK_PAT_CRITICAL_VALUE] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'危急值自增长号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_CRITICAL_VALUE', @level2type=N'COLUMN',@level2name=N'ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'住院流水号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_CRITICAL_VALUE', @level2type=N'COLUMN',@level2name=N'PAT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'住院号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_CRITICAL_VALUE', @level2type=N'COLUMN',@level2name=N'IN_HOSP_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'病区代码' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_CRITICAL_VALUE', @level2type=N'COLUMN',@level2name=N'WARD_CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'床位号码' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_CRITICAL_VALUE', @level2type=N'COLUMN',@level2name=N'BED_CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'发送人姓名' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_CRITICAL_VALUE', @level2type=N'COLUMN',@level2name=N'SEND_PEOPLE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'发送时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_CRITICAL_VALUE', @level2type=N'COLUMN',@level2name=N'SEND_TIME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'危急值内容' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_CRITICAL_VALUE', @level2type=N'COLUMN',@level2name=N'CRITICAL_VALUE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'处置护士编号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_CRITICAL_VALUE', @level2type=N'COLUMN',@level2name=N'DISPOSE_NURSE_CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'处置护士姓名' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_CRITICAL_VALUE', @level2type=N'COLUMN',@level2name=N'DISPOSE_NURSE_NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'护士处置时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_CRITICAL_VALUE', @level2type=N'COLUMN',@level2name=N'DISPOSE_DATE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'处置医生编号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_CRITICAL_VALUE', @level2type=N'COLUMN',@level2name=N'DISPOSE_DOCTOR_CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'处置医生姓名' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_CRITICAL_VALUE', @level2type=N'COLUMN',@level2name=N'DISPOSE_DOCTOR_NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'医生处置时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_CRITICAL_VALUE', @level2type=N'COLUMN',@level2name=N'DISPOSE_DOCTOR_DATE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'处置状态(0:未处理,1:已处理)' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_CRITICAL_VALUE', @level2type=N'COLUMN',@level2name=N'DISPOSE_STATUS'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'创建时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_CRITICAL_VALUE', @level2type=N'COLUMN',@level2name=N'SYNC_CREATE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'修改时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_CRITICAL_VALUE', @level2type=N'COLUMN',@level2name=N'SYNC_UPDATE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'患者危急值记录表  未处置记录重复提醒规则，需要LIS处置状态信息' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_CRITICAL_VALUE'
GO

--生命体征
GO
/****** Object:  Table [dbo].[PAT_SKIN_TEST]    Script Date: 03/24/2016 15:52:02 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[PAT_SKIN_TEST](
	[ID] [bigint] IDENTITY(1,1) NOT NULL,
	[PAT_ID] [varchar](20) NOT NULL,
	[PAT_NAME] [varchar](20) NULL,
	[ORDER_EXEC_ID] [varchar](32) NULL,
	[STATUS] [varchar](1) NOT NULL,
	[RESULT] [varchar](20) NOT NULL,
	[INPUT_NURSE_ID] [varchar](10) NULL,
	[INPUT_NURSE_NAME] [varchar](20) NULL,
	[APPROVE_NURSE_ID] [varchar](10) NULL,
	[APPROVE_NURSE_NAME] [varchar](20) NULL,
	[APPROVE_DATE] [datetime] NULL,
	[EXEC_NURSE_ID] [varchar](10) NULL,
	[EXEC_NURSE_NAME] [varchar](20) NULL,
	[EXEC_DATE] [datetime] NULL,
	[DRUG_BATCH_NO] [varchar](30) NULL,
	[DRUG_CODE] [varchar](30) NULL,
	[DRUG_NAME] [varchar](80) NULL,
	[BODY_SIGN_MAS_ID] [bigint] NULL,
	[IMG_BEFORE] [image] NULL,
	[IMG_AFTER] [image] NULL,
	[TEST_INDEX] [int] NOT NULL,
 CONSTRAINT [PK_PAT_SKIN_TEST] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'皮试i项Id' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_SKIN_TEST', @level2type=N'COLUMN',@level2name=N'ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'住院流水号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_SKIN_TEST', @level2type=N'COLUMN',@level2name=N'PAT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'住院号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_SKIN_TEST', @level2type=N'COLUMN',@level2name=N'PAT_NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'医嘱执行id' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_SKIN_TEST', @level2type=N'COLUMN',@level2name=N'ORDER_EXEC_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'皮试状态(0:未皮试,1:已皮试)' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_SKIN_TEST', @level2type=N'COLUMN',@level2name=N'STATUS'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'皮试结果(0:阴性,1:阳性)' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_SKIN_TEST', @level2type=N'COLUMN',@level2name=N'RESULT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'皮试录入护士code' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_SKIN_TEST', @level2type=N'COLUMN',@level2name=N'INPUT_NURSE_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'皮试录入护士name' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_SKIN_TEST', @level2type=N'COLUMN',@level2name=N'INPUT_NURSE_NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'皮试确认护士code' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_SKIN_TEST', @level2type=N'COLUMN',@level2name=N'APPROVE_NURSE_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'皮试确认护士name' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_SKIN_TEST', @level2type=N'COLUMN',@level2name=N'APPROVE_NURSE_NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'皮试确认时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_SKIN_TEST', @level2type=N'COLUMN',@level2name=N'APPROVE_DATE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'皮试执行护士code' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_SKIN_TEST', @level2type=N'COLUMN',@level2name=N'EXEC_NURSE_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'皮试执行护士name' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_SKIN_TEST', @level2type=N'COLUMN',@level2name=N'EXEC_NURSE_NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'皮试执行时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_SKIN_TEST', @level2type=N'COLUMN',@level2name=N'EXEC_DATE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'药品批号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_SKIN_TEST', @level2type=N'COLUMN',@level2name=N'DRUG_BATCH_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'药品代码' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_SKIN_TEST', @level2type=N'COLUMN',@level2name=N'DRUG_CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'药品名称' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_SKIN_TEST', @level2type=N'COLUMN',@level2name=N'DRUG_NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'患者皮试结果记录表' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_SKIN_TEST'
GO
/****** Object:  Table [dbo].[PAT_EVENT]    Script Date: 03/24/2016 15:52:02 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[PAT_EVENT](
	[ID] [bigint] IDENTITY(1,1) NOT NULL,
	[PAT_ID] [varchar](20) NOT NULL,
	[PAT_NAME] [varchar](20) NULL,
	[BODY_SIGN_MAS_ID] [bigint] NULL,
	[EVENT_DATE] [varchar](10) NULL,
	[RECORD_USER_CODE] [varchar](10) NOT NULL,
	[RECORDER] [varchar](20) NULL,
	[EVENT_CODE] [varchar](10) NOT NULL,
	[EVENT_NAME] [varchar](20) NULL,
	[CHINESE_EVENT_DATE] [varchar](30) NULL,
	[RECORD_USER_NAME] [varchar](20) NULL,
	[EVENT_INDEX] [int] NOT NULL,
	[RECORD_DATE] [datetime] NOT NULL,
 CONSTRAINT [PK_PAT_EVENT] PRIMARY KEY CLUSTERED 
(
	[PAT_ID] ASC,
	[EVENT_CODE] ASC,
	[RECORD_DATE] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON, FILLFACTOR = 100) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'体温单事件自增长号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_EVENT', @level2type=N'COLUMN',@level2name=N'ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'住院流水号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_EVENT', @level2type=N'COLUMN',@level2name=N'PAT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'患者姓名' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_EVENT', @level2type=N'COLUMN',@level2name=N'PAT_NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'体征记录主ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_EVENT', @level2type=N'COLUMN',@level2name=N'BODY_SIGN_MAS_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'事件日期' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_EVENT', @level2type=N'COLUMN',@level2name=N'EVENT_DATE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'记录人工号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_EVENT', @level2type=N'COLUMN',@level2name=N'RECORD_USER_CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'记录人姓名' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_EVENT', @level2type=N'COLUMN',@level2name=N'RECORDER'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'等同体征中measureNoteCode' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_EVENT', @level2type=N'COLUMN',@level2name=N'EVENT_CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'事件名称' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_EVENT', @level2type=N'COLUMN',@level2name=N'EVENT_NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'患者事件记录表' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_EVENT'
GO
/****** Object:  Table [dbo].[PAT_BODYSIGN_MAS]    Script Date: 03/24/2016 15:52:02 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[PAT_BODYSIGN_MAS](
	[ID] [bigint] IDENTITY(1,1) NOT NULL,
	[PAT_ID] [varchar](20) NOT NULL,
	[WARD_CODE] [varchar](10) NOT NULL,
	[BED_CODE] [varchar](10) NOT NULL,
	[RECORD_DATE] [datetime] NOT NULL,
	[RECORD_USER_CODE] [varchar](10) NOT NULL,
	[RECORD_USER_NAME] [varchar](20) NULL,
	[MODIFY_DATE] [datetime] NULL,
	[MODIFY_USER_CODE] [varchar](10) NULL,
	[MODIFY_USER_NAME] [varchar](20) NULL,
	[REMARK] [varchar](50) NULL,
	[PAT_NAME] [varchar](20) NULL,
	[FIRST_DATE] [datetime] NULL,
	[SOURCE] [varchar](2) NULL,
 CONSTRAINT [PK_PAT_BODYSIGN_MAS] PRIMARY KEY CLUSTERED 
(
	[PAT_ID] ASC,
	[RECORD_DATE] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON, FILLFACTOR = 100) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'体征主表自增长号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_BODYSIGN_MAS', @level2type=N'COLUMN',@level2name=N'ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'住院流水号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_BODYSIGN_MAS', @level2type=N'COLUMN',@level2name=N'PAT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'病区代码' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_BODYSIGN_MAS', @level2type=N'COLUMN',@level2name=N'WARD_CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'床号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_BODYSIGN_MAS', @level2type=N'COLUMN',@level2name=N'BED_CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'记录日期时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_BODYSIGN_MAS', @level2type=N'COLUMN',@level2name=N'RECORD_DATE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'记录人工号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_BODYSIGN_MAS', @level2type=N'COLUMN',@level2name=N'RECORD_USER_CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'记录人' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_BODYSIGN_MAS', @level2type=N'COLUMN',@level2name=N'RECORD_USER_NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'修改日期时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_BODYSIGN_MAS', @level2type=N'COLUMN',@level2name=N'MODIFY_DATE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'修改人工号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_BODYSIGN_MAS', @level2type=N'COLUMN',@level2name=N'MODIFY_USER_CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'修改人' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_BODYSIGN_MAS', @level2type=N'COLUMN',@level2name=N'MODIFY_USER_NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'备注' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_BODYSIGN_MAS', @level2type=N'COLUMN',@level2name=N'REMARK'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'患者体征数据主表' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_BODYSIGN_MAS'
GO
/****** Object:  Table [dbo].[PAT_BODYSIGN_DETAIL]    Script Date: 03/24/2016 15:52:02 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[PAT_BODYSIGN_DETAIL](
	[ID] [bigint] IDENTITY(1,1) NOT NULL,
	[MAS_ID] [bigint] NULL,
	[ITEM_CODE] [varchar](20) NOT NULL,
	[ITEM_VALUE] [varchar](100) NULL,
	[MEASURE_NOTE_CODE] [varchar](20) NULL,
	[MEASURE_NOTE_NAME] [varchar](20) NULL,
	[ABNORMAL_FLAG] [varchar](1) NOT NULL,
	[SPEC_MARK] [varchar](1) NULL,
	[ITEM_INDEX] [int] NOT NULL,
	[PAT_ID] [varchar](20) NOT NULL,
	[RECORD_DATE] [datetime] NOT NULL,
	[UNIT] [varchar](20) NULL,
	[RY_HOUR_DIFF] [varchar](10) NULL,
 CONSTRAINT [PK_PAT_BODYSIGN_DETAIL] PRIMARY KEY NONCLUSTERED 
(
	[PAT_ID] ASC,
	[ITEM_CODE] ASC,
	[RECORD_DATE] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON, FILLFACTOR = 100) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'体征明细自增长号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_BODYSIGN_DETAIL', @level2type=N'COLUMN',@level2name=N'ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'体征主表自增长号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_BODYSIGN_DETAIL', @level2type=N'COLUMN',@level2name=N'MAS_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'体征项目代号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_BODYSIGN_DETAIL', @level2type=N'COLUMN',@level2name=N'ITEM_CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'体征项目值' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_BODYSIGN_DETAIL', @level2type=N'COLUMN',@level2name=N'ITEM_VALUE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'可选项代码' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_BODYSIGN_DETAIL', @level2type=N'COLUMN',@level2name=N'MEASURE_NOTE_CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'可选项名称' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_BODYSIGN_DETAIL', @level2type=N'COLUMN',@level2name=N'MEASURE_NOTE_NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'正常标志:H-High， L-Low，N-Normal, A-Abnormal' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_BODYSIGN_DETAIL', @level2type=N'COLUMN',@level2name=N'ABNORMAL_FLAG'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'特殊标记' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_BODYSIGN_DETAIL', @level2type=N'COLUMN',@level2name=N'SPEC_MARK'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'体征单位' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_BODYSIGN_DETAIL', @level2type=N'COLUMN',@level2name=N'UNIT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'患者体征明细表' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_BODYSIGN_DETAIL'
GO
/****** Object:  Table [dbo].[PAT_BODYSIGN_DELETE]    Script Date: 03/24/2016 15:52:02 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[PAT_BODYSIGN_DELETE](
	[ID] [bigint] IDENTITY(1,1) NOT NULL,
	[DEPT_CODE] [varchar](20) NULL,
	[PAT_ID] [varchar](30) NOT NULL,
	[RECORD_DATE] [datetime] NOT NULL,
	[CODE] [varchar](30) NOT NULL,
	[OPER_DATE] [datetime] NULL,
PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[PAT_ALLERGY]    Script Date: 03/24/2016 15:52:02 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[PAT_ALLERGY](
	[ALLERGY_ID] [bigint] IDENTITY(1,1) NOT NULL,
	[DRUG_CODE] [varchar](30) NULL,
	[DRUG_NAME] [varchar](80) NULL,
	[PAT_ID] [varchar](20) NOT NULL,
	[PAT_NAME] [varchar](20) NULL,
	[ALLERGY_DATE] [datetime] NULL,
	[SKIN_TEST_ID] [bigint] NULL,
	[IS_VALID] [char](1) NULL,
PRIMARY KEY CLUSTERED 
(
	[ALLERGY_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'主键id' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ALLERGY', @level2type=N'COLUMN',@level2name=N'ALLERGY_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'过敏药物code' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ALLERGY', @level2type=N'COLUMN',@level2name=N'DRUG_CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'过敏药物name' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ALLERGY', @level2type=N'COLUMN',@level2name=N'DRUG_NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'病人id' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ALLERGY', @level2type=N'COLUMN',@level2name=N'PAT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'病人name' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ALLERGY', @level2type=N'COLUMN',@level2name=N'PAT_NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'过敏添加时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ALLERGY', @level2type=N'COLUMN',@level2name=N'ALLERGY_DATE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'皮试ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'PAT_ALLERGY', @level2type=N'COLUMN',@level2name=N'SKIN_TEST_ID'
GO
/****** Object:  Default [DF__PAT_BODYS__ABNOR__0F4D3C5F]    Script Date: 03/24/2016 15:52:02 ******/
ALTER TABLE [dbo].[PAT_BODYSIGN_DETAIL] ADD  CONSTRAINT [DF__PAT_BODYS__ABNOR__0F4D3C5F]  DEFAULT ('N') FOR [ABNORMAL_FLAG]
GO
/****** Object:  Default [DF__PAT_BODYS__SPEC___10416098]    Script Date: 03/24/2016 15:52:02 ******/
ALTER TABLE [dbo].[PAT_BODYSIGN_DETAIL] ADD  CONSTRAINT [DF__PAT_BODYS__SPEC___10416098]  DEFAULT ('0') FOR [SPEC_MARK]
GO
/****** Object:  Default [DF__PAT_BODYS__item___58A712EB]    Script Date: 03/24/2016 15:52:02 ******/
ALTER TABLE [dbo].[PAT_BODYSIGN_DETAIL] ADD  DEFAULT ((0)) FOR [ITEM_INDEX]
GO
/****** Object:  Default [DF__PAT_BODYS__SOURC__15460CD7]    Script Date: 03/24/2016 15:52:02 ******/
ALTER TABLE [dbo].[PAT_BODYSIGN_MAS] ADD  DEFAULT ((2)) FOR [SOURCE]
GO
/****** Object:  Default [DF__PAT_EVENT__event__56BECA79]    Script Date: 03/24/2016 15:52:02 ******/
ALTER TABLE [dbo].[PAT_EVENT] ADD  CONSTRAINT [DF__PAT_EVENT__event__56BECA79]  DEFAULT ((0)) FOR [EVENT_INDEX]
GO
/****** Object:  Default [DF__PAT_SKIN___ST_IT__355DD6AE]    Script Date: 03/24/2016 15:52:02 ******/
ALTER TABLE [dbo].[PAT_SKIN_TEST] ADD  DEFAULT ('0') FOR [STATUS]
GO
/****** Object:  Default [DF__PAT_SKIN___ST_IT__3651FAE7]    Script Date: 03/24/2016 15:52:02 ******/
ALTER TABLE [dbo].[PAT_SKIN_TEST] ADD  CONSTRAINT [DF__PAT_SKIN___ST_IT__3651FAE7]  DEFAULT ('N') FOR [RESULT]
GO
/****** Object:  Default [DF__PAT_SKIN___test___57B2EEB2]    Script Date: 03/24/2016 15:52:02 ******/
ALTER TABLE [dbo].[PAT_SKIN_TEST] ADD  DEFAULT ((0)) FOR [TEST_INDEX]
GO

/****** Object:  Table [dbo].[NURSE_WARD_PATROL]    Script Date: 11/12/2015 14:32:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[NURSE_WARD_PATROL](
	[ID] [bigint] IDENTITY(1,1) NOT NULL,
	[PAT_ID] [varchar](20) NOT NULL,
	[WARD_CODE] [varchar](10) NOT NULL,
	[BED_CODE] [varchar](10) NOT NULL,
	[TEND_LEVEL] [varchar](1) NULL,
	[USER_CODE] [varchar](10) NOT NULL,
	[PATROL_DATE] [datetime] NULL,
	[NEXT_PATROL_DATE] [datetime] NULL,
 CONSTRAINT [PK_NURSE_WARD_PATROL] PRIMARY KEY NONCLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'巡视记录自增长ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_WARD_PATROL', @level2type=N'COLUMN',@level2name=N'ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'住院流水号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_WARD_PATROL', @level2type=N'COLUMN',@level2name=N'PAT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'病区代码' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_WARD_PATROL', @level2type=N'COLUMN',@level2name=N'WARD_CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'床号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_WARD_PATROL', @level2type=N'COLUMN',@level2name=N'BED_CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'护理级别' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_WARD_PATROL', @level2type=N'COLUMN',@level2name=N'TEND_LEVEL'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'巡视人工号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_WARD_PATROL', @level2type=N'COLUMN',@level2name=N'USER_CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'巡视时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_WARD_PATROL', @level2type=N'COLUMN',@level2name=N'PATROL_DATE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'下一次巡视时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_WARD_PATROL', @level2type=N'COLUMN',@level2name=N'NEXT_PATROL_DATE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'护士病房巡视记录' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_WARD_PATROL'
GO
/****** Object:  Table [dbo].[NURSE_SHIFT_RECORD]    Script Date: 11/12/2015 14:32:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[NURSE_SHIFT_RECORD](
	[RECORD_ID] [int] IDENTITY(1,1) NOT NULL,
	[NURSE_SHIFT_ID] [varchar](50) NOT NULL,
	[PAT_ID] [varchar](30) NOT NULL,
	[DEPT_CODE] [varchar](30) NOT NULL,
	[RECORD_NURSE_ID] [varchar](30) NULL,
	[RECORD_NURSE_NAME] [varchar](50) NULL,
	[RECORD_TIME] [datetime] NULL,
	[RECORD_DATA] [varchar](1000) NULL,
	[RECORD_VALID] [char](1) NULL,
	[START_DATE] [datetime] NULL,
	[END_DATE] [datetime] NULL,
PRIMARY KEY CLUSTERED 
(
	[RECORD_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'主键id' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_SHIFT_RECORD', @level2type=N'COLUMN',@level2name=N'RECORD_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'关联主记录id' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_SHIFT_RECORD', @level2type=N'COLUMN',@level2name=N'NURSE_SHIFT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'患者id' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_SHIFT_RECORD', @level2type=N'COLUMN',@level2name=N'PAT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'患者部门' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_SHIFT_RECORD', @level2type=N'COLUMN',@level2name=N'DEPT_CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'详情记录护士Id' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_SHIFT_RECORD', @level2type=N'COLUMN',@level2name=N'RECORD_NURSE_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'详情记录护士name' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_SHIFT_RECORD', @level2type=N'COLUMN',@level2name=N'RECORD_NURSE_NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'详情记录时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_SHIFT_RECORD', @level2type=N'COLUMN',@level2name=N'RECORD_TIME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'详情' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_SHIFT_RECORD', @level2type=N'COLUMN',@level2name=N'RECORD_DATA'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'详情记录是否有效' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_SHIFT_RECORD', @level2type=N'COLUMN',@level2name=N'RECORD_VALID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'交接班开始时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_SHIFT_RECORD', @level2type=N'COLUMN',@level2name=N'START_DATE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'交接班结束时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_SHIFT_RECORD', @level2type=N'COLUMN',@level2name=N'END_DATE'
GO
/****** Object:  Table [dbo].[NURSE_SHIFT]    Script Date: 11/12/2015 14:32:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[NURSE_SHIFT](
	[NURSE_SHIFT_ID] [varchar](50) NOT NULL,
	[PAT_ID] [varchar](30) NOT NULL,
	[PAT_NAME] [varchar](50) NULL,
	[DEPT_CODE] [varchar](30) NOT NULL,
	[DEPT_NAME] [varchar](50) NULL,
	[HOSP_NO] [varchar](10) NULL,
	[BED_NO] [varchar](10) NULL,
	[CLINIC_DIAGNOSE] [varchar](50) NULL,
	[TEND] [varchar](50) NULL,
	[CRITICAL_FLAG] [varchar](1) NULL,
	[SHIFT_NURSE_ID] [varchar](30) NULL,
	[SHIFT_NURSE_NAME] [varchar](50) NULL,
	[SHIFT_DATE] [datetime] NULL,
	[SUCCESSION_NURSE_ID] [varchar](30) NULL,
	[SUCCESSION_NURSE_NAME] [varchar](50) NULL,
	[SUCCESSION_DATE] [datetime] NULL,
	[SHIFT_VALID] [varchar](1) NULL,
	[SHIFT_STATUS] [varchar](1) NULL,
	[START_DATE] [datetime] NULL,
	[END_DATE] [datetime] NULL,
PRIMARY KEY CLUSTERED 
(
	[NURSE_SHIFT_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'主键id' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_SHIFT', @level2type=N'COLUMN',@level2name=N'NURSE_SHIFT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'患者id' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_SHIFT', @level2type=N'COLUMN',@level2name=N'PAT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'患者name' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_SHIFT', @level2type=N'COLUMN',@level2name=N'PAT_NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'部门id' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_SHIFT', @level2type=N'COLUMN',@level2name=N'DEPT_CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'部门name' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_SHIFT', @level2type=N'COLUMN',@level2name=N'DEPT_NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'住院号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_SHIFT', @level2type=N'COLUMN',@level2name=N'HOSP_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'床位号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_SHIFT', @level2type=N'COLUMN',@level2name=N'BED_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'临床诊断' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_SHIFT', @level2type=N'COLUMN',@level2name=N'CLINIC_DIAGNOSE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'护理等级' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_SHIFT', @level2type=N'COLUMN',@level2name=N'TEND'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'病危等级' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_SHIFT', @level2type=N'COLUMN',@level2name=N'CRITICAL_FLAG'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'交班护士id' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_SHIFT', @level2type=N'COLUMN',@level2name=N'SHIFT_NURSE_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'交班护士name' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_SHIFT', @level2type=N'COLUMN',@level2name=N'SHIFT_NURSE_NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'交班时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_SHIFT', @level2type=N'COLUMN',@level2name=N'SHIFT_DATE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'接班护士id' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_SHIFT', @level2type=N'COLUMN',@level2name=N'SUCCESSION_NURSE_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'接班护士name' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_SHIFT', @level2type=N'COLUMN',@level2name=N'SUCCESSION_NURSE_NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'接班时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_SHIFT', @level2type=N'COLUMN',@level2name=N'SUCCESSION_DATE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'是否有效' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_SHIFT', @level2type=N'COLUMN',@level2name=N'SHIFT_VALID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'交接班状态' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_SHIFT', @level2type=N'COLUMN',@level2name=N'SHIFT_STATUS'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'开始时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_SHIFT', @level2type=N'COLUMN',@level2name=N'START_DATE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'结束时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_SHIFT', @level2type=N'COLUMN',@level2name=N'END_DATE'
GO
/****** Object:  Table [dbo].[NURSE_LIQUOR]    Script Date: 11/12/2015 14:32:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[NURSE_LIQUOR](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[ORDER_GROUP_ID] [varchar](30) NOT NULL,
	[PREPARE_NURSE_ID] [varchar](30) NULL,
	[PREPARE_NURSE_NAME] [varchar](30) NULL,
	[PREPARE_TIME] [datetime] NULL,
	[EXEC_NURSE_ID] [varchar](30) NULL,
	[EXEC_NURSE_NAME] [varchar](30) NULL,
	[EXEC_TIME] [datetime] NULL,
	[STATE] [char](1) NOT NULL,
	[VERIFY_NURSE_ID] [varchar](30) NULL,
	[VERIFY_NURSE_NAME] [varchar](30) NULL,
	[VERIFY_TIME] [datetime] NULL,
 CONSTRAINT [PK__nurse_li__3213E83F6E765879] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'主键Id' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_LIQUOR', @level2type=N'COLUMN',@level2name=N'id'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'医嘱Id' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_LIQUOR', @level2type=N'COLUMN',@level2name=N'ORDER_GROUP_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'备药护士id' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_LIQUOR', @level2type=N'COLUMN',@level2name=N'PREPARE_NURSE_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'备药护士name' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_LIQUOR', @level2type=N'COLUMN',@level2name=N'PREPARE_NURSE_NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'备药时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_LIQUOR', @level2type=N'COLUMN',@level2name=N'PREPARE_TIME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'配液护士Id' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_LIQUOR', @level2type=N'COLUMN',@level2name=N'EXEC_NURSE_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'配液护士name' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_LIQUOR', @level2type=N'COLUMN',@level2name=N'EXEC_NURSE_NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'配液时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_LIQUOR', @level2type=N'COLUMN',@level2name=N'EXEC_TIME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'配液状态(P备药,V审核,F配液)' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_LIQUOR', @level2type=N'COLUMN',@level2name=N'STATE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'审核护士Id' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_LIQUOR', @level2type=N'COLUMN',@level2name=N'VERIFY_NURSE_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'审核护士Name' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_LIQUOR', @level2type=N'COLUMN',@level2name=N'VERIFY_NURSE_NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'审核时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_LIQUOR', @level2type=N'COLUMN',@level2name=N'VERIFY_TIME'
GO
/****** Object:  Table [dbo].[lx_white_board_item]    Script Date: 11/12/2015 14:32:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[lx_white_board_item](
	[item_code] [varchar](30) NOT NULL,
	[item_name] [varchar](50) NOT NULL,
	[item_desc] [varchar](500) NULL,
PRIMARY KEY CLUSTERED 
(
	[item_code] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[lx_white_board]    Script Date: 11/12/2015 14:32:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[lx_white_board](
	[dept_id] [varchar](30) NOT NULL,
	[item_code] [varchar](30) NOT NULL,
	[item_value] [varchar](1000) NOT NULL,
	[operate_time] [date] NULL,
	[show_date] [varchar](10) NOT NULL,
	[id] [int] IDENTITY(1,1) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[INM_WORKLOAD_STAT]    Script Date: 11/12/2015 14:32:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[INM_WORKLOAD_STAT](
	[SEQ_ID] [int] IDENTITY(1,1) NOT NULL,
	[DEPT_CODE] [varchar](10) NULL,
	[WORK_TYPE] [varchar](10) NULL,
	[WORK_NAME] [varchar](30) NULL,
	[NATURAL_STAT_DATE] [int] NULL,
	[EVERYDAY_NUM] [int] NULL,
	[CREATE_TIME] [datetime] NULL,
 CONSTRAINT [PK_INM_WORKLOAD_STAT] PRIMARY KEY CLUSTERED 
(
	[SEQ_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'自增长流水号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'INM_WORKLOAD_STAT', @level2type=N'COLUMN',@level2name=N'SEQ_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'科室代码' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'INM_WORKLOAD_STAT', @level2type=N'COLUMN',@level2name=N'DEPT_CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'工作类型' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'INM_WORKLOAD_STAT', @level2type=N'COLUMN',@level2name=N'WORK_TYPE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'工作名称' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'INM_WORKLOAD_STAT', @level2type=N'COLUMN',@level2name=N'WORK_NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'自然统计日期' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'INM_WORKLOAD_STAT', @level2type=N'COLUMN',@level2name=N'NATURAL_STAT_DATE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'单天总次数' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'INM_WORKLOAD_STAT', @level2type=N'COLUMN',@level2name=N'EVERYDAY_NUM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'创建时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'INM_WORKLOAD_STAT', @level2type=N'COLUMN',@level2name=N'CREATE_TIME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'输液监测-工作量统计' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'INM_WORKLOAD_STAT'
GO
/****** Object:  Table [dbo].[INM_PERBAG_INFO]    Script Date: 11/12/2015 14:32:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[INM_PERBAG_INFO](
	[SEQ_ID] [int] IDENTITY(1,1) NOT NULL,
	[INFUSION_BAG_ID] [varchar](32) NULL,
	[MAC_ADDRESS] [varchar](20) NULL,
	[BEGIN_TIME] [datetime] NULL,
	[END_TIME] [datetime] NULL,
	[ACT_TOTAL_CAPACITY] [decimal](7, 2) NULL,
	[ACT_TOTAL_WEIGHT] [decimal](7, 2) NULL,
	[STD_TOTAL_CAPACITY] [decimal](7, 2) NULL,
	[STD_TOTAL_WEIGHT] [decimal](7, 2) NULL,
	[INFUSION_STATUS] [char](1) NULL,
	[ORDER_NO] [varchar](32) NULL,
	[CREATE_TIME] [datetime] NULL,
 CONSTRAINT [PK_INM_PERBAG_INFO] PRIMARY KEY CLUSTERED 
(
	[SEQ_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON, FILLFACTOR = 100) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'自增长流水号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'INM_PERBAG_INFO', @level2type=N'COLUMN',@level2name=N'SEQ_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'输液袋序号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'INM_PERBAG_INFO', @level2type=N'COLUMN',@level2name=N'INFUSION_BAG_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'MAC地址' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'INM_PERBAG_INFO', @level2type=N'COLUMN',@level2name=N'MAC_ADDRESS'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'开始时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'INM_PERBAG_INFO', @level2type=N'COLUMN',@level2name=N'BEGIN_TIME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'结束时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'INM_PERBAG_INFO', @level2type=N'COLUMN',@level2name=N'END_TIME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'实际总容量' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'INM_PERBAG_INFO', @level2type=N'COLUMN',@level2name=N'ACT_TOTAL_CAPACITY'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'实际总重量' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'INM_PERBAG_INFO', @level2type=N'COLUMN',@level2name=N'ACT_TOTAL_WEIGHT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'标准总容量' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'INM_PERBAG_INFO', @level2type=N'COLUMN',@level2name=N'STD_TOTAL_CAPACITY'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'标准总重量' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'INM_PERBAG_INFO', @level2type=N'COLUMN',@level2name=N'STD_TOTAL_WEIGHT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'输液状态' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'INM_PERBAG_INFO', @level2type=N'COLUMN',@level2name=N'INFUSION_STATUS'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'医嘱号码' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'INM_PERBAG_INFO', @level2type=N'COLUMN',@level2name=N'ORDER_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'创建时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'INM_PERBAG_INFO', @level2type=N'COLUMN',@level2name=N'CREATE_TIME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'输液监测-输液监测器每一袋的输液信息' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'INM_PERBAG_INFO'
GO
/****** Object:  Table [dbo].[INM_PERBAG_DETAIL_INFO]    Script Date: 11/12/2015 14:32:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[INM_PERBAG_DETAIL_INFO](
	[SEQ_ID] [int] IDENTITY(1,1) NOT NULL,
	[INFUSION_BAG_ID] [varchar](32) NULL,
	[MAC_ADDRESS] [varchar](20) NULL,
	[SENT_TIME] [datetime] NULL,
	[DROP_SPEED] [int] NULL,
	[REST_WEIGHT] [decimal](7, 2) NULL,
	[REST_CAPACITY] [decimal](7, 2) NULL,
	[INM_PERCENT] [int] NULL,
	[REST_TIME] [int] NULL,
	[CREATE_TIME] [datetime] NULL,
 CONSTRAINT [PK_INM_PERBAG_DETAIL_INFO] PRIMARY KEY CLUSTERED 
(
	[SEQ_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON, FILLFACTOR = 100) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'自增长流水号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'INM_PERBAG_DETAIL_INFO', @level2type=N'COLUMN',@level2name=N'SEQ_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'输液袋序号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'INM_PERBAG_DETAIL_INFO', @level2type=N'COLUMN',@level2name=N'INFUSION_BAG_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'MAC地址' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'INM_PERBAG_DETAIL_INFO', @level2type=N'COLUMN',@level2name=N'MAC_ADDRESS'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'发送时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'INM_PERBAG_DETAIL_INFO', @level2type=N'COLUMN',@level2name=N'SENT_TIME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'滴速' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'INM_PERBAG_DETAIL_INFO', @level2type=N'COLUMN',@level2name=N'DROP_SPEED'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'剩余重量' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'INM_PERBAG_DETAIL_INFO', @level2type=N'COLUMN',@level2name=N'REST_WEIGHT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'剩余液量' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'INM_PERBAG_DETAIL_INFO', @level2type=N'COLUMN',@level2name=N'REST_CAPACITY'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'输液百分比' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'INM_PERBAG_DETAIL_INFO', @level2type=N'COLUMN',@level2name=N'INM_PERCENT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'剩余时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'INM_PERBAG_DETAIL_INFO', @level2type=N'COLUMN',@level2name=N'REST_TIME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'创建时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'INM_PERBAG_DETAIL_INFO', @level2type=N'COLUMN',@level2name=N'CREATE_TIME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'输液监测-输液监测器每一袋的输液明细信息' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'INM_PERBAG_DETAIL_INFO'
GO
/****** Object:  Table [dbo].[INM_INFUSION_STAT]    Script Date: 11/12/2015 14:32:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[INM_INFUSION_STAT](
	[SEQ_ID] [int] IDENTITY(1,1) NOT NULL,
	[DEPT_CODE] [varchar](10) NULL,
	[NATURAL_STAT_DATE] [int] NULL,
	[INFUSION_BAG_NUM] [int] NULL,
	[INFUSION_ML_NUM] [int] NULL,
	[CREATE_TIME] [datetime] NULL,
 CONSTRAINT [PK_INM_INFUSION_STAT] PRIMARY KEY CLUSTERED 
(
	[SEQ_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'自增长流水号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'INM_INFUSION_STAT', @level2type=N'COLUMN',@level2name=N'SEQ_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'科室代码' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'INM_INFUSION_STAT', @level2type=N'COLUMN',@level2name=N'DEPT_CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'自然统计日期' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'INM_INFUSION_STAT', @level2type=N'COLUMN',@level2name=N'NATURAL_STAT_DATE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'单天总输液袋数' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'INM_INFUSION_STAT', @level2type=N'COLUMN',@level2name=N'INFUSION_BAG_NUM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'单天总输液量毫升数' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'INM_INFUSION_STAT', @level2type=N'COLUMN',@level2name=N'INFUSION_ML_NUM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'创建时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'INM_INFUSION_STAT', @level2type=N'COLUMN',@level2name=N'CREATE_TIME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'输液监测-输液量总量统计' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'INM_INFUSION_STAT'
GO
/****** Object:  Table [dbo].[INM_GATHER_INFO]    Script Date: 11/12/2015 14:32:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[INM_GATHER_INFO](
	[SEQ_ID] [int] IDENTITY(1,1) NOT NULL,
	[MAC_ADDRESS] [varchar](20) NULL,
	[INFUSION_BAG_ID] [varchar](32) NULL,
	[INM_WEIGHT] [decimal](7, 2) NULL,
	[CREATE_TIME] [datetime] NULL,
 CONSTRAINT [PK_INM_GATHER_INFO] PRIMARY KEY CLUSTERED 
(
	[SEQ_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON, FILLFACTOR = 100) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'自增长流水号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'INM_GATHER_INFO', @level2type=N'COLUMN',@level2name=N'SEQ_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'MAC地址' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'INM_GATHER_INFO', @level2type=N'COLUMN',@level2name=N'MAC_ADDRESS'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'输液袋序号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'INM_GATHER_INFO', @level2type=N'COLUMN',@level2name=N'INFUSION_BAG_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'检测器获取重量' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'INM_GATHER_INFO', @level2type=N'COLUMN',@level2name=N'INM_WEIGHT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'创建时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'INM_GATHER_INFO', @level2type=N'COLUMN',@level2name=N'CREATE_TIME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'输液监测-输液监测器收集信息' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'INM_GATHER_INFO'
GO
/****** Object:  Table [dbo].[INM_DEVICE_SET]    Script Date: 11/12/2015 14:32:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[INM_DEVICE_SET](
	[SEQ_ID] [int] IDENTITY(1,1) NOT NULL,
	[MAC_ADDRESS] [varchar](20) NULL,
	[ALARM_SWITCH] [char](1) NULL,
	[REST_ML_ALARM] [decimal](7, 2) NULL,
	[DROP_SPEED_FLOOR] [int] NULL,
	[DROP_SPEED_UPPER] [int] NULL,
	[REST_TIME_ALARM] [int] NULL,
	[IDC] [int] NULL,
	[CREATE_TIME] [datetime] NULL,
 CONSTRAINT [PK_INM_DEVICE_SET] PRIMARY KEY CLUSTERED 
(
	[SEQ_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'自增长流水号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'INM_DEVICE_SET', @level2type=N'COLUMN',@level2name=N'SEQ_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'MAC地址' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'INM_DEVICE_SET', @level2type=N'COLUMN',@level2name=N'MAC_ADDRESS'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'报警开关 0 关；1 开' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'INM_DEVICE_SET', @level2type=N'COLUMN',@level2name=N'ALARM_SWITCH'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'剩余液量报警值' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'INM_DEVICE_SET', @level2type=N'COLUMN',@level2name=N'REST_ML_ALARM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'输液滴速下限' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'INM_DEVICE_SET', @level2type=N'COLUMN',@level2name=N'DROP_SPEED_FLOOR'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'输液滴速上限' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'INM_DEVICE_SET', @level2type=N'COLUMN',@level2name=N'DROP_SPEED_UPPER'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'剩余时间报警值' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'INM_DEVICE_SET', @level2type=N'COLUMN',@level2name=N'REST_TIME_ALARM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'点滴系数' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'INM_DEVICE_SET', @level2type=N'COLUMN',@level2name=N'IDC'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'创建时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'INM_DEVICE_SET', @level2type=N'COLUMN',@level2name=N'CREATE_TIME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'输液监测-监测器设备设置' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'INM_DEVICE_SET'
GO
/****** Object:  Table [dbo].[INM_DEVICE_INFO]    Script Date: 11/12/2015 14:32:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[INM_DEVICE_INFO](
	[SEQ_ID] [int] IDENTITY(1,1) NOT NULL,
	[MAC_ADDRESS] [varchar](20) NULL,
	[DEVICE_NAME] [varchar](50) NULL,
	[DEVICE_LNK] [char](1) NULL,
	[REST_KWH] [int] NULL,
	[BEARLOAD_STATUS] [char](1) NULL,
	[DEPT_CODE] [varchar](10) NULL,
	[BED_CODE] [varchar](10) NULL,
	[IN_HOSP_NO] [varchar](20) NULL,
	[PAT_ID] [varchar](20) NULL,
	[CREATE_TIME] [datetime] NULL,
 CONSTRAINT [PK_INM_DEVICE_INFO] PRIMARY KEY CLUSTERED 
(
	[SEQ_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'自增长流水号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'INM_DEVICE_INFO', @level2type=N'COLUMN',@level2name=N'SEQ_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'MAC地址' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'INM_DEVICE_INFO', @level2type=N'COLUMN',@level2name=N'MAC_ADDRESS'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'设备名称' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'INM_DEVICE_INFO', @level2type=N'COLUMN',@level2name=N'DEVICE_NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'设备连接状态 0 否；1 是' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'INM_DEVICE_INFO', @level2type=N'COLUMN',@level2name=N'DEVICE_LNK'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'剩余电量' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'INM_DEVICE_INFO', @level2type=N'COLUMN',@level2name=N'REST_KWH'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'负重状态 0 否；1 是' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'INM_DEVICE_INFO', @level2type=N'COLUMN',@level2name=N'BEARLOAD_STATUS'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'科室代码' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'INM_DEVICE_INFO', @level2type=N'COLUMN',@level2name=N'DEPT_CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'床位代码' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'INM_DEVICE_INFO', @level2type=N'COLUMN',@level2name=N'BED_CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'住院号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'INM_DEVICE_INFO', @level2type=N'COLUMN',@level2name=N'IN_HOSP_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'创建时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'INM_DEVICE_INFO', @level2type=N'COLUMN',@level2name=N'CREATE_TIME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'输液监测-监测器设备信息' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'INM_DEVICE_INFO'
GO
/****** Object:  Table [dbo].[INM_ALARM_INFO]    Script Date: 11/12/2015 14:32:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[INM_ALARM_INFO](
	[SEQ_ID] [int] IDENTITY(1,1) NOT NULL,
	[MAC_ADDRESS] [varchar](20) NULL,
	[DEPT_CODE] [varchar](10) NULL,
	[ALARM_TIME] [datetime] NULL,
	[ALARM_TYPE] [char](1) NULL,
	[ALARM_CONTENT] [varchar](200) NULL,
	[ALARM_STATUS] [char](1) NULL,
	[OPER_PERSON] [varchar](50) NULL,
	[OPER_TIME] [datetime] NULL,
	[CREATE_TIME] [datetime] NULL,
 CONSTRAINT [PK_INM_ALARM_INFO] PRIMARY KEY CLUSTERED 
(
	[SEQ_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'自增长流水号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'INM_ALARM_INFO', @level2type=N'COLUMN',@level2name=N'SEQ_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'MAC地址' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'INM_ALARM_INFO', @level2type=N'COLUMN',@level2name=N'MAC_ADDRESS'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'科室代码' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'INM_ALARM_INFO', @level2type=N'COLUMN',@level2name=N'DEPT_CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'报警时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'INM_ALARM_INFO', @level2type=N'COLUMN',@level2name=N'ALARM_TIME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'报警类型' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'INM_ALARM_INFO', @level2type=N'COLUMN',@level2name=N'ALARM_TYPE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'报警内容' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'INM_ALARM_INFO', @level2type=N'COLUMN',@level2name=N'ALARM_CONTENT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'报警状态' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'INM_ALARM_INFO', @level2type=N'COLUMN',@level2name=N'ALARM_STATUS'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'处理人' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'INM_ALARM_INFO', @level2type=N'COLUMN',@level2name=N'OPER_PERSON'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'处理时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'INM_ALARM_INFO', @level2type=N'COLUMN',@level2name=N'OPER_TIME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'创建时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'INM_ALARM_INFO', @level2type=N'COLUMN',@level2name=N'CREATE_TIME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'输液监测-报警信息' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'INM_ALARM_INFO'
GO
/****** Object:  Table [dbo].[DOC_TYPE]    Script Date: 11/12/2015 14:32:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[DOC_TYPE](
	[DOC_TYPE_ID] [int] NOT NULL,
	[DOC_TYPE_NAME] [varchar](1024) NOT NULL,
	[DOC_TYPE_VALID] [char](1) NOT NULL,
	[ORD] [int] NULL,
 CONSTRAINT [PK_DOC_TYPE] PRIMARY KEY CLUSTERED 
(
	[DOC_TYPE_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'流水号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_TYPE', @level2type=N'COLUMN',@level2name=N'DOC_TYPE_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'分类名称' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_TYPE', @level2type=N'COLUMN',@level2name=N'DOC_TYPE_NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'是否有效 Y 是; N 否' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_TYPE', @level2type=N'COLUMN',@level2name=N'DOC_TYPE_VALID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'排序数字' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_TYPE', @level2type=N'COLUMN',@level2name=N'ORD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'护理文书-模板分类表' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_TYPE'
GO
/****** Object:  Table [dbo].[DOC_REPORT_TEMPLATE_ITEM]    Script Date: 11/12/2015 14:32:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[DOC_REPORT_TEMPLATE_ITEM](
	[ITEM_ID] [varchar](64) NOT NULL,
	[BAND_ID] [varchar](64) NOT NULL,
	[ITEM_POS_X] [int] NOT NULL,
	[ITEM_POS_Y] [int] NOT NULL,
	[ITEM_WIDTH] [int] NOT NULL,
	[ITEM_HEIGHT] [int] NOT NULL,
	[ITEM_NAME] [varchar](300) NOT NULL,
	[ITEM_TYPE] [varchar](30) NOT NULL,
	[ITEM_INDEX] [int] NOT NULL,
	[ALIGN] [varchar](16) NULL,
	[MARGIN] [varchar](16) NULL,
	[ISBOLD] [varchar](16) NULL,
	[SIZE] [varchar](16) NULL,
	[DIRECTION] [varchar](16) NULL,
 CONSTRAINT [PK_DOC_REPORT_TEMPLATE_ITEM] PRIMARY KEY CLUSTERED 
(
	[ITEM_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'流水号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_REPORT_TEMPLATE_ITEM', @level2type=N'COLUMN',@level2name=N'ITEM_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'区域ID（关联doc_report_band主键）' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_REPORT_TEMPLATE_ITEM', @level2type=N'COLUMN',@level2name=N'BAND_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'X轴坐标' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_REPORT_TEMPLATE_ITEM', @level2type=N'COLUMN',@level2name=N'ITEM_POS_X'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Y轴坐标' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_REPORT_TEMPLATE_ITEM', @level2type=N'COLUMN',@level2name=N'ITEM_POS_Y'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'宽' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_REPORT_TEMPLATE_ITEM', @level2type=N'COLUMN',@level2name=N'ITEM_WIDTH'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'高' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_REPORT_TEMPLATE_ITEM', @level2type=N'COLUMN',@level2name=N'ITEM_HEIGHT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'数据源名字（与doc_metadata.metadata_clde对应）' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_REPORT_TEMPLATE_ITEM', @level2type=N'COLUMN',@level2name=N'ITEM_NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'组件类型' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_REPORT_TEMPLATE_ITEM', @level2type=N'COLUMN',@level2name=N'ITEM_TYPE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'排序' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_REPORT_TEMPLATE_ITEM', @level2type=N'COLUMN',@level2name=N'ITEM_INDEX'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'左右对齐' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_REPORT_TEMPLATE_ITEM', @level2type=N'COLUMN',@level2name=N'ALIGN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'上下对齐' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_REPORT_TEMPLATE_ITEM', @level2type=N'COLUMN',@level2name=N'MARGIN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'字体是否加粗' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_REPORT_TEMPLATE_ITEM', @level2type=N'COLUMN',@level2name=N'ISBOLD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'字体大小' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_REPORT_TEMPLATE_ITEM', @level2type=N'COLUMN',@level2name=N'SIZE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'表格分割线防线' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_REPORT_TEMPLATE_ITEM', @level2type=N'COLUMN',@level2name=N'DIRECTION'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'护理文书-模板元素表' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_REPORT_TEMPLATE_ITEM'
GO
/****** Object:  Table [dbo].[DOC_REPORT_TEMPLATE]    Script Date: 11/12/2015 14:32:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[DOC_REPORT_TEMPLATE](
	[TEMPLATE_ID] [varchar](64) NOT NULL,
	[TEMPLATE_NAME] [varchar](64) NOT NULL,
	[DOC_TYPE] [int] NOT NULL,
	[DEPT_CODE] [varchar](30) NOT NULL,
	[SHOW_INDEX] [int] NOT NULL,
	[VALID] [int] NOT NULL,
	[MEMO] [varchar](50) NULL,
	[TEMPLATE_SHOW_NAME] [varchar](50) NOT NULL,
	[TEMPLATE_FILE_NAME] [varchar](1024) NOT NULL,
	[ORIENTATION] [varchar](30) NOT NULL,
	[HEIGHT] [int] NOT NULL,
	[WIDTH] [int] NOT NULL,
	[CREATE_TIME] [datetime] NOT NULL,
	[REPORT_TYPE] [varchar](2) NULL,
 CONSTRAINT [PK_DOC_REPORT_TEMPLATE] PRIMARY KEY CLUSTERED 
(
	[TEMPLATE_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'流水号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_REPORT_TEMPLATE', @level2type=N'COLUMN',@level2name=N'TEMPLATE_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'模板名字' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_REPORT_TEMPLATE', @level2type=N'COLUMN',@level2name=N'TEMPLATE_NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'文书类型，0为病例类，1为护理记录（表格类），2为病程类' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_REPORT_TEMPLATE', @level2type=N'COLUMN',@level2name=N'DOC_TYPE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'科室编号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_REPORT_TEMPLATE', @level2type=N'COLUMN',@level2name=N'DEPT_CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'左边显示排序' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_REPORT_TEMPLATE', @level2type=N'COLUMN',@level2name=N'SHOW_INDEX'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'0为起用状态，1为禁用状态' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_REPORT_TEMPLATE', @level2type=N'COLUMN',@level2name=N'VALID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'备注' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_REPORT_TEMPLATE', @level2type=N'COLUMN',@level2name=N'MEMO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'模板展示的名字' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_REPORT_TEMPLATE', @level2type=N'COLUMN',@level2name=N'TEMPLATE_SHOW_NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'模版内容' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_REPORT_TEMPLATE', @level2type=N'COLUMN',@level2name=N'TEMPLATE_FILE_NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'打印方向' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_REPORT_TEMPLATE', @level2type=N'COLUMN',@level2name=N'ORIENTATION'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'高度' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_REPORT_TEMPLATE', @level2type=N'COLUMN',@level2name=N'HEIGHT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'宽度' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_REPORT_TEMPLATE', @level2type=N'COLUMN',@level2name=N'WIDTH'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'创建时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_REPORT_TEMPLATE', @level2type=N'COLUMN',@level2name=N'CREATE_TIME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'文书分类' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_REPORT_TEMPLATE', @level2type=N'COLUMN',@level2name=N'REPORT_TYPE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'护理文书-模板表' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_REPORT_TEMPLATE'
GO
/****** Object:  Table [dbo].[DOC_REPORT_DATA_SPEC]    Script Date: 11/12/2015 14:32:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[DOC_REPORT_DATA_SPEC](
	[DATA_ID] [int] IDENTITY(1,1) NOT NULL,
	[DATA_KEY] [varchar](64) NULL,
	[DATA_VALUE] [varchar](2000) NULL,
	[TEMPLATE_ID] [varchar](64) NULL,
	[INPATIENT_NO] [varchar](14) NULL,
	[TYPE] [char](2) NULL,
 CONSTRAINT [PK_DOC_REPORT_DATA_SPEC] PRIMARY KEY CLUSTERED 
(
	[DATA_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'流水号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_REPORT_DATA_SPEC', @level2type=N'COLUMN',@level2name=N'DATA_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'模板元素ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_REPORT_DATA_SPEC', @level2type=N'COLUMN',@level2name=N'DATA_KEY'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'模板元素填充的值' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_REPORT_DATA_SPEC', @level2type=N'COLUMN',@level2name=N'DATA_VALUE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'模板ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_REPORT_DATA_SPEC', @level2type=N'COLUMN',@level2name=N'TEMPLATE_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'病人ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_REPORT_DATA_SPEC', @level2type=N'COLUMN',@level2name=N'INPATIENT_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'数据分类（基本数据，动态表头数据等）' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_REPORT_DATA_SPEC', @level2type=N'COLUMN',@level2name=N'TYPE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'护理文书-特殊数据记录表' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_REPORT_DATA_SPEC'
GO
/****** Object:  Table [dbo].[DOC_REPORT_DATA_ITEM]    Script Date: 11/12/2015 14:32:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[DOC_REPORT_DATA_ITEM](
	[RECORD_ITEM_ID] [int] IDENTITY(1,1) NOT NULL,
	[RECORD_ID] [varchar](64) NOT NULL,
	[TEMPLATE_ITEM_ID] [varchar](64) NOT NULL,
	[RECORD_VALUE] [varchar](2000) NULL,
	[PARENT_ID] [int] NULL,
 CONSTRAINT [PK_DOC_REPORT_DATA_ITEM] PRIMARY KEY CLUSTERED 
(
	[RECORD_ITEM_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'流水号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_REPORT_DATA_ITEM', @level2type=N'COLUMN',@level2name=N'RECORD_ITEM_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'记录ID（与doc_report_data主键关联）' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_REPORT_DATA_ITEM', @level2type=N'COLUMN',@level2name=N'RECORD_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'数据源ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_REPORT_DATA_ITEM', @level2type=N'COLUMN',@level2name=N'TEMPLATE_ITEM_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'数据内容' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_REPORT_DATA_ITEM', @level2type=N'COLUMN',@level2name=N'RECORD_VALUE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'父元素ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_REPORT_DATA_ITEM', @level2type=N'COLUMN',@level2name=N'PARENT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'护理文书-护理文书数据子表' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_REPORT_DATA_ITEM'
GO
/****** Object:  Table [dbo].[DOC_REPORT_DATA]    Script Date: 11/12/2015 14:32:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[DOC_REPORT_DATA](
	[TEMPLATE_ID] [varchar](64) NOT NULL,
	[INPATIENT_NO] [varchar](14) NOT NULL,
	[RECORD_ID] [varchar](64) NOT NULL,
	[CREATE_TIME] [datetime] NOT NULL,
	[MODIFY_TIME] [datetime] NULL,
	[PRINTFLAG] [int] NULL,
	[VALID] [int] NULL,
	[APPROVE_STATUS] [char](1) NULL,
	[APPROVE_PERSON] [varchar](50) NULL,
	[APPROVE_TIME] [datetime] NULL,
	[DATE_LIST] [date] NULL,
	[TIME_LIST] [time](7) NULL,
	[CREATE_PERSON] [varchar](50) NULL,
 CONSTRAINT [PK_DOC_REPORT_DATA] PRIMARY KEY CLUSTERED 
(
	[RECORD_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'模板ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_REPORT_DATA', @level2type=N'COLUMN',@level2name=N'TEMPLATE_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'病人ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_REPORT_DATA', @level2type=N'COLUMN',@level2name=N'INPATIENT_NO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'记录ID(主键)' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_REPORT_DATA', @level2type=N'COLUMN',@level2name=N'RECORD_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'创建时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_REPORT_DATA', @level2type=N'COLUMN',@level2name=N'CREATE_TIME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'修改时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_REPORT_DATA', @level2type=N'COLUMN',@level2name=N'MODIFY_TIME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'打印状态 1:已打印 0：未打印' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_REPORT_DATA', @level2type=N'COLUMN',@level2name=N'PRINTFLAG'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'数据是否有效（只给客户做逻辑删除）0有效1无效' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_REPORT_DATA', @level2type=N'COLUMN',@level2name=N'VALID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'审签状态 N 未审签; Y  已审签' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_REPORT_DATA', @level2type=N'COLUMN',@level2name=N'APPROVE_STATUS'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'审签人' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_REPORT_DATA', @level2type=N'COLUMN',@level2name=N'APPROVE_PERSON'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'审签时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_REPORT_DATA', @level2type=N'COLUMN',@level2name=N'APPROVE_TIME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'日期' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_REPORT_DATA', @level2type=N'COLUMN',@level2name=N'DATE_LIST'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_REPORT_DATA', @level2type=N'COLUMN',@level2name=N'TIME_LIST'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'创建人' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_REPORT_DATA', @level2type=N'COLUMN',@level2name=N'CREATE_PERSON'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'护理文书-护理文书数据主表' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_REPORT_DATA'
GO
/****** Object:  Table [dbo].[DOC_REPORT_BAND]    Script Date: 11/12/2015 14:32:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[DOC_REPORT_BAND](
	[BAND_ID] [varchar](64) NOT NULL,
	[TEMPLATE_ID] [varchar](64) NOT NULL,
	[BAND_NAME] [varchar](50) NOT NULL,
	[BAND_POS_Y] [int] NOT NULL,
	[BAND_HEIGHT] [int] NOT NULL,
 CONSTRAINT [PK_DOC_REPORT_BAND] PRIMARY KEY CLUSTERED 
(
	[BAND_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'流水号（主键）' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_REPORT_BAND', @level2type=N'COLUMN',@level2name=N'BAND_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'模板ID（与DOC_TEMPLATE主键对应）' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_REPORT_BAND', @level2type=N'COLUMN',@level2name=N'TEMPLATE_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'区域名字' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_REPORT_BAND', @level2type=N'COLUMN',@level2name=N'BAND_NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Y轴坐标' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_REPORT_BAND', @level2type=N'COLUMN',@level2name=N'BAND_POS_Y'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'高度' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_REPORT_BAND', @level2type=N'COLUMN',@level2name=N'BAND_HEIGHT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'护理文书-模板区域表' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_REPORT_BAND'
GO
/****** Object:  Table [dbo].[DOC_PRINT_SHOW]    Script Date: 11/12/2015 14:32:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[DOC_PRINT_SHOW](
	[SHOW_TYPE] [varchar](16) NOT NULL
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'护理记录打印时，列表展示的格式：NEW显示最新记录OLD显示最早的记录OLDTONEW显示显示最早和最新的CHANGE显示该页变化的记录' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_PRINT_SHOW', @level2type=N'COLUMN',@level2name=N'SHOW_TYPE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'护理文书-打印方式配置表' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_PRINT_SHOW'
GO
/****** Object:  Table [dbo].[DOC_METADATA]    Script Date: 11/12/2015 14:32:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[DOC_METADATA](
	[METADATA_NAME] [nvarchar](60) NULL,
	[METADATA_SIMPLE_NAME] [nvarchar](60) NULL,
	[METADATA_NAME_PINYIN] [nvarchar](100) NULL,
	[METADATA_CODE] [varchar](32) NOT NULL,
	[INPUT_TYPE] [nvarchar](10) NULL,
	[DATA_TYPE] [varchar](32) NULL,
	[READONLY_FLAG] [char](1) NULL,
	[MIN_VALUE] [numeric](10, 2) NULL,
	[MAX_VALUE] [numeric](10, 2) NULL,
	[VERIFY_POLICY_CODE] [nvarchar](100) NULL,
	[UNIT] [nvarchar](30) NULL,
	[PRECISION] [int] NULL,
	[REMARK] [nvarchar](300) NULL,
	[ACTIVE] [bit] NULL,
	[REQUIRED] [bit] NULL,
	[DEFAULT_VALUE] [varchar](256) NULL,
	[PARENT_CODE] [varchar](32) NULL,
	[WIDTH] [int] NULL,
	[ORD] [int] NULL,
	[IS_SECODE] [char](1) NULL,
	[IS_DYNA] [char](1) NULL,
	[SHOW_TYPE] [varchar](32) NULL,
	[SCORE] [int] NULL,
 CONSTRAINT [PK_METADATA] PRIMARY KEY CLUSTERED 
(
	[METADATA_CODE] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'元数据名称' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_METADATA', @level2type=N'COLUMN',@level2name=N'METADATA_NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'元数据名称简称' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_METADATA', @level2type=N'COLUMN',@level2name=N'METADATA_SIMPLE_NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'元数据名称拼音与拼音简写' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_METADATA', @level2type=N'COLUMN',@level2name=N'METADATA_NAME_PINYIN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'元数据代码' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_METADATA', @level2type=N'COLUMN',@level2name=N'METADATA_CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'输入类型。包括键入(INPT)、字串(STR)、数值(NUM)、邮箱(MAL)、体温(TP)、脉搏(PLS)、开关(SWT)、登录人(LGN)、护士长(NUS_MNG)、系统时间(SYS_TIM)、日期(DAT)、时间(TIM)、单选(SEL)、多选(MSEL)、单选带输入(SEL_INPT)、多选带输入(MSEL_INPT)、操作(OPT)' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_METADATA', @level2type=N'COLUMN',@level2name=N'INPUT_TYPE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'数据类型。包括：数值（NUM)、字串(STR)、日期(DAT)、日期时间(DAT_TIM)、温度（TP)、脉搏(PLS)等' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_METADATA', @level2type=N'COLUMN',@level2name=N'DATA_TYPE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'是否只读.只读(Y),可写(N)' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_METADATA', @level2type=N'COLUMN',@level2name=N'READONLY_FLAG'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'最小值。用于检验结果标识正常值范围。对于一些特殊的校验方式，也可用于输入值校验' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_METADATA', @level2type=N'COLUMN',@level2name=N'MIN_VALUE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'最大值。用于检验结果标识正常值范围。对于一些特殊的校验方式，也可用于输入值校验' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_METADATA', @level2type=N'COLUMN',@level2name=N'MAX_VALUE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'校验规则。除依据数据类型自动校验外的特殊校验规则。不同规则之间以逗号分隔' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_METADATA', @level2type=N'COLUMN',@level2name=N'VERIFY_POLICY_CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'单位名称' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_METADATA', @level2type=N'COLUMN',@level2name=N'UNIT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'小数位数' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_METADATA', @level2type=N'COLUMN',@level2name=N'PRECISION'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'备注' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_METADATA', @level2type=N'COLUMN',@level2name=N'REMARK'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'标示是否有效' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_METADATA', @level2type=N'COLUMN',@level2name=N'ACTIVE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'是否必填 1、 必填' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_METADATA', @level2type=N'COLUMN',@level2name=N'REQUIRED'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'默认值' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_METADATA', @level2type=N'COLUMN',@level2name=N'DEFAULT_VALUE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'父节点元素' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_METADATA', @level2type=N'COLUMN',@level2name=N'PARENT_CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'输入框的宽度' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_METADATA', @level2type=N'COLUMN',@level2name=N'WIDTH'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'输出顺序' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_METADATA', @level2type=N'COLUMN',@level2name=N'ORD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'是否第二级节点（输入框为T复选框为S）' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_METADATA', @level2type=N'COLUMN',@level2name=N'IS_SECODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'是否可以用作护理记录单中的动态列表数据源' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_METADATA', @level2type=N'COLUMN',@level2name=N'IS_DYNA'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'针对复选框类型时，展示的名字：ROMAN:罗马 ENGLISH:英文 ARAB：阿拉伯数字 RING：带圆圈的阿拉伯数字' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_METADATA', @level2type=N'COLUMN',@level2name=N'SHOW_TYPE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'分数' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_METADATA', @level2type=N'COLUMN',@level2name=N'SCORE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'护理文书-护理文书元数据表' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_METADATA'
GO
/****** Object:  Table [dbo].[DOC_CONFIG]    Script Date: 11/12/2015 14:32:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[DOC_CONFIG](
	[SEQ_ID] [int] IDENTITY(1,1) NOT NULL,
	[DOC_TYPE] [int] NOT NULL,
	[TRI_GRADE] [int] NOT NULL,
	[FREQUENCY] [int] NOT NULL,
	[IS_VALID] [char](1) NOT NULL,
	[CONFIG_TYPE] [char](1) NOT NULL,
	[CREATE_TIME] [datetime] NULL,
	[REF_COLUMN] [varchar](64) NULL,
 CONSTRAINT [PK_DOC_CONFIG] PRIMARY KEY CLUSTERED 
(
	[SEQ_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'自增长流水号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_CONFIG', @level2type=N'COLUMN',@level2name=N'SEQ_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'文书类型' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_CONFIG', @level2type=N'COLUMN',@level2name=N'DOC_TYPE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'触发分数' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_CONFIG', @level2type=N'COLUMN',@level2name=N'TRI_GRADE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'频次提醒时间（小时）' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_CONFIG', @level2type=N'COLUMN',@level2name=N'FREQUENCY'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'是否生效 Y 是；N 否' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_CONFIG', @level2type=N'COLUMN',@level2name=N'IS_VALID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'配置类型（在床位栏显示还是频次提醒）  0 展示；1 频次提醒' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_CONFIG', @level2type=N'COLUMN',@level2name=N'CONFIG_TYPE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'创建时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_CONFIG', @level2type=N'COLUMN',@level2name=N'CREATE_TIME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'关联的字段' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_CONFIG', @level2type=N'COLUMN',@level2name=N'REF_COLUMN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'文书配置表' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DOC_CONFIG'
GO
/****** Object:  Table [dbo].[DIC_USAGE]    Script Date: 11/12/2015 14:32:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[DIC_USAGE](
	[USAGE_CODE] [varchar](10) NOT NULL,
	[USAGE_NAME] [varchar](30) NULL,
	[FLAG] [varchar](1) NOT NULL,
	[MODIFY_DATE] [datetime] NULL,
 CONSTRAINT [PK_DIC_USAGE] PRIMARY KEY CLUSTERED 
(
	[USAGE_CODE] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'用法代码' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DIC_USAGE', @level2type=N'COLUMN',@level2name=N'USAGE_CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'用法名称' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DIC_USAGE', @level2type=N'COLUMN',@level2name=N'USAGE_NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'标记(0:无效,1:有效)' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DIC_USAGE', @level2type=N'COLUMN',@level2name=N'FLAG'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'修改日期时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DIC_USAGE', @level2type=N'COLUMN',@level2name=N'MODIFY_DATE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'用法字典表' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DIC_USAGE'
GO
/****** Object:  Table [dbo].[DIC_ORDER_TYPE]    Script Date: 11/12/2015 14:32:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[DIC_ORDER_TYPE](
	[ORDER_TYPE_CODE] [varchar](10) NOT NULL,
	[ORDER_TYPE_NAME] [varchar](30) NULL,
	[FLAG] [varchar](1) NOT NULL,
	[MODIFY_DATE] [datetime] NULL,
 CONSTRAINT [PK_DIC_ORDER_TYPE] PRIMARY KEY CLUSTERED 
(
	[ORDER_TYPE_CODE] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'医嘱类型代码' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DIC_ORDER_TYPE', @level2type=N'COLUMN',@level2name=N'ORDER_TYPE_CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'医嘱类型名称' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DIC_ORDER_TYPE', @level2type=N'COLUMN',@level2name=N'ORDER_TYPE_NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'标记(0:无效,1:有效)' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DIC_ORDER_TYPE', @level2type=N'COLUMN',@level2name=N'FLAG'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'修改日期' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DIC_ORDER_TYPE', @level2type=N'COLUMN',@level2name=N'MODIFY_DATE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'医嘱类型字典表' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DIC_ORDER_TYPE'
GO
/****** Object:  Table [dbo].[DIC_FREQ]    Script Date: 11/12/2015 14:32:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[DIC_FREQ](
	[FREQ_CODE] [varchar](10) NOT NULL,
	[FREQ_NAME] [varchar](30) NULL,
	[FLAG] [varchar](1) NOT NULL,
	[MODIFY_DATE] [datetime] NULL,
	[FREQUENCY] [int] NULL,
	[FREQUENCY_TIME] [varchar](300) NULL,
	[TIME_TAG] [varchar](2) NULL,
 CONSTRAINT [PK_DIC_FREQ] PRIMARY KEY CLUSTERED 
(
	[FREQ_CODE] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'频次代码' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DIC_FREQ', @level2type=N'COLUMN',@level2name=N'FREQ_CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'频次名称' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DIC_FREQ', @level2type=N'COLUMN',@level2name=N'FREQ_NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'标记(0:无效,1:有效)' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DIC_FREQ', @level2type=N'COLUMN',@level2name=N'FLAG'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'修改日期' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DIC_FREQ', @level2type=N'COLUMN',@level2name=N'MODIFY_DATE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'频次字典表' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DIC_FREQ'
GO
/****** Object:  Table [dbo].[DIC_BODY_SIGN]    Script Date: 11/12/2015 14:32:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[DIC_BODY_SIGN](
	[ITEM_CODE] [varchar](20) NOT NULL,
	[ITEM_NAME] [varchar](20) NOT NULL,
	[ITEM_UNIT] [varchar](10) NULL,
 CONSTRAINT [PK_DIC_BODY_SIGN] PRIMARY KEY NONCLUSTERED 
(
	[ITEM_CODE] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'体征项目编号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DIC_BODY_SIGN', @level2type=N'COLUMN',@level2name=N'ITEM_CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'体征项目名称' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DIC_BODY_SIGN', @level2type=N'COLUMN',@level2name=N'ITEM_NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'体征项目单位' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DIC_BODY_SIGN', @level2type=N'COLUMN',@level2name=N'ITEM_UNIT'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'体征明细字典表' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'DIC_BODY_SIGN'
GO
/****** Object:  Table [dbo].[COM_WARD_USER]    Script Date: 11/12/2015 14:32:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[COM_WARD_USER](
	[WARD_CODE] [varchar](10) NOT NULL,
	[USER_CODE] [varchar](10) NOT NULL,
	[ID] [int] IDENTITY(1,1) NOT NULL,
 CONSTRAINT [PK_COM_WARD_USER] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'病区代码' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'COM_WARD_USER', @level2type=N'COLUMN',@level2name=N'WARD_CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'用户编号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'COM_WARD_USER', @level2type=N'COLUMN',@level2name=N'USER_CODE'
GO
/****** Object:  Table [dbo].[COM_WARD]    Script Date: 11/12/2015 14:32:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[COM_WARD](
	[CODE] [varchar](10) NOT NULL,
	[DEPT_CODE] [varchar](10) NULL,
	[NAME] [varchar](30) NULL,
	[SEARCH_CODE] [varchar](20) NULL,
	[TAGS] [varchar](50) NULL,
	[FLAG] [varchar](1) NULL,
	[SYNC_CREATE] [datetime] NULL,
	[SYNC_UPDATE] [datetime] NULL,
	[IS_DEPT] [varchar](1) NULL,
	[ID] [int] IDENTITY(1,1) NOT NULL,
 CONSTRAINT [PK_COM_WARD_1] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY],
 CONSTRAINT [UNIQUE_COM_WARD] UNIQUE NONCLUSTERED 
(
	[CODE] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'病区代码' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'COM_WARD', @level2type=N'COLUMN',@level2name=N'CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'科室代码' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'COM_WARD', @level2type=N'COLUMN',@level2name=N'DEPT_CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'病区名称' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'COM_WARD', @level2type=N'COLUMN',@level2name=N'NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'病区查询代码' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'COM_WARD', @level2type=N'COLUMN',@level2name=N'SEARCH_CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'附加说明' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'COM_WARD', @level2type=N'COLUMN',@level2name=N'TAGS'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'标记(0:无效,1:有效)' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'COM_WARD', @level2type=N'COLUMN',@level2name=N'FLAG'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'创建时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'COM_WARD', @level2type=N'COLUMN',@level2name=N'SYNC_CREATE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'修改时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'COM_WARD', @level2type=N'COLUMN',@level2name=N'SYNC_UPDATE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'病区表' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'COM_WARD'
GO
/****** Object:  Table [dbo].[COM_USER_ROLE]    Script Date: 11/12/2015 14:32:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[COM_USER_ROLE](
	[USER_CODE] [varchar](10) NOT NULL,
	[ROLE_CODE] [varchar](10) NOT NULL
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'用户编号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'COM_USER_ROLE', @level2type=N'COLUMN',@level2name=N'USER_CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'角色代码' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'COM_USER_ROLE', @level2type=N'COLUMN',@level2name=N'ROLE_CODE'
GO
/****** Object:  Table [dbo].[COM_USER_FINGER]    Script Date: 11/12/2015 14:32:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[COM_USER_FINGER](
	[ID] [bigint] IDENTITY(1,1) NOT NULL,
	[USER_CODE] [varchar](10) NULL,
	[FINGER_FEATURE] [varchar](2048) NOT NULL,
	[SEC_KEY] [varchar](50) NOT NULL,
	[CREATE_DATE] [datetime] NULL,
	[DEPT_CODE] [varchar](10) NULL,
 CONSTRAINT [PK_COM_USER_FINGER] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'指纹特征自增长号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'COM_USER_FINGER', @level2type=N'COLUMN',@level2name=N'ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'用户编号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'COM_USER_FINGER', @level2type=N'COLUMN',@level2name=N'USER_CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'指纹特征码' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'COM_USER_FINGER', @level2type=N'COLUMN',@level2name=N'FINGER_FEATURE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'用户密钥' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'COM_USER_FINGER', @level2type=N'COLUMN',@level2name=N'SEC_KEY'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'创建日期' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'COM_USER_FINGER', @level2type=N'COLUMN',@level2name=N'CREATE_DATE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'用户指纹记录表' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'COM_USER_FINGER'
GO
/****** Object:  Table [dbo].[COM_USER_BED]    Script Date: 11/12/2015 14:32:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[COM_USER_BED](
	[USER_CODE] [varchar](10) NOT NULL,
	[BED_CODE] [varchar](10) NOT NULL,
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[WARD_CODE] [varchar](10) NOT NULL,
 CONSTRAINT [PK_COM_USER_BED] PRIMARY KEY NONCLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY],
 CONSTRAINT [UNIQUE_COM_USER_BED] UNIQUE NONCLUSTERED 
(
	[USER_CODE] ASC,
	[WARD_CODE] ASC,
	[BED_CODE] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'用户编号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'COM_USER_BED', @level2type=N'COLUMN',@level2name=N'USER_CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'床位代码' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'COM_USER_BED', @level2type=N'COLUMN',@level2name=N'BED_CODE'
GO
/****** Object:  Table [dbo].[COM_USER]    Script Date: 11/12/2015 14:32:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[COM_USER](
	[ID] [bigint] IDENTITY(1,1) NOT NULL,
	[CODE] [varchar](10) NOT NULL,
	[NAME] [varchar](20) NULL,
	[PASSWORD] [varchar](32) NULL,
	[GENDER] [char](1) NULL,
	[PHONE] [varchar](20) NULL,
	[EMAIL] [varchar](50) NULL,
	[REMARK] [varchar](50) NULL,
	[FLAG] [char](1) NOT NULL,
	[SYNC_CREATE] [datetime] NULL,
	[SYNC_UPDATE] [datetime] NULL,
	[LOGIN_NAME] [varchar](10) NULL,
 CONSTRAINT [PK_COM_USER_1] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY],
 CONSTRAINT [UNIQUE_CODE] UNIQUE NONCLUSTERED 
(
	[CODE] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'用户自增长号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'COM_USER', @level2type=N'COLUMN',@level2name=N'ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'用户编号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'COM_USER', @level2type=N'COLUMN',@level2name=N'CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'用户姓名' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'COM_USER', @level2type=N'COLUMN',@level2name=N'NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'密码' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'COM_USER', @level2type=N'COLUMN',@level2name=N'PASSWORD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'性别' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'COM_USER', @level2type=N'COLUMN',@level2name=N'GENDER'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'电话' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'COM_USER', @level2type=N'COLUMN',@level2name=N'PHONE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'邮件' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'COM_USER', @level2type=N'COLUMN',@level2name=N'EMAIL'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'备注' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'COM_USER', @level2type=N'COLUMN',@level2name=N'REMARK'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'标记(0:无效,1:有效)' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'COM_USER', @level2type=N'COLUMN',@level2name=N'FLAG'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'创建时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'COM_USER', @level2type=N'COLUMN',@level2name=N'SYNC_CREATE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'修改时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'COM_USER', @level2type=N'COLUMN',@level2name=N'SYNC_UPDATE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'用户表' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'COM_USER'
GO
/****** Object:  Table [dbo].[COM_ROLE_PERMISSION]    Script Date: 11/12/2015 14:32:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[COM_ROLE_PERMISSION](
	[ROLE_CODE] [varchar](10) NOT NULL,
	[PERMISSION_CODE] [varchar](10) NOT NULL,
 CONSTRAINT [PK_COM_ROLE_PERMISSION] PRIMARY KEY CLUSTERED 
(
	[ROLE_CODE] ASC,
	[PERMISSION_CODE] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'角色代码' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'COM_ROLE_PERMISSION', @level2type=N'COLUMN',@level2name=N'ROLE_CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'权限代码' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'COM_ROLE_PERMISSION', @level2type=N'COLUMN',@level2name=N'PERMISSION_CODE'
GO
/****** Object:  Table [dbo].[COM_ROLE]    Script Date: 11/12/2015 14:32:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[COM_ROLE](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[CODE] [varchar](10) NOT NULL,
	[NAME] [varchar](30) NULL,
	[LEVEL] [int] NOT NULL,
	[REMARK] [varchar](50) NULL,
	[FLAG] [varchar](1) NOT NULL,
 CONSTRAINT [PK_COM_ROLE] PRIMARY KEY CLUSTERED 
(
	[ID] ASC,
	[CODE] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'角色自增长号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'COM_ROLE', @level2type=N'COLUMN',@level2name=N'ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'角色代码' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'COM_ROLE', @level2type=N'COLUMN',@level2name=N'CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'角色名称' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'COM_ROLE', @level2type=N'COLUMN',@level2name=N'NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'角色级别' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'COM_ROLE', @level2type=N'COLUMN',@level2name=N'LEVEL'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'角色备注说明' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'COM_ROLE', @level2type=N'COLUMN',@level2name=N'REMARK'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'标记(0:无效,1:有效)' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'COM_ROLE', @level2type=N'COLUMN',@level2name=N'FLAG'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'角色表' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'COM_ROLE'
GO
/****** Object:  Table [dbo].[COM_PERMISSION]    Script Date: 11/12/2015 14:32:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[COM_PERMISSION](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[CODE] [varchar](10) NOT NULL,
	[NAME] [varchar](50) NULL,
	[FLAG] [varchar](1) NOT NULL,
 CONSTRAINT [PK_COM_PERMISSION] PRIMARY KEY CLUSTERED 
(
	[ID] ASC,
	[CODE] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'权限自增长号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'COM_PERMISSION', @level2type=N'COLUMN',@level2name=N'ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'权限代码' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'COM_PERMISSION', @level2type=N'COLUMN',@level2name=N'CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'权限名称' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'COM_PERMISSION', @level2type=N'COLUMN',@level2name=N'NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'标记(0:无效,1:有效)' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'COM_PERMISSION', @level2type=N'COLUMN',@level2name=N'FLAG'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'权限表' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'COM_PERMISSION'
GO
/****** Object:  Table [dbo].[COM_NAV_TREE]    Script Date: 11/12/2015 14:32:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[COM_NAV_TREE](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[SHOWNAME] [varchar](100) NOT NULL,
	[FIELDTYPE] [varchar](20) NULL,
	[ORD] [int] NULL,
	[PARENT_ID] [int] NOT NULL,
	[TYPE] [int] NOT NULL,
	[STATUS] [int] NOT NULL,
	[URL] [varchar](100) NULL,
 CONSTRAINT [PK_COM_NAV_TREE] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'主键、自增长' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'COM_NAV_TREE', @level2type=N'COLUMN',@level2name=N'ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'显示名称' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'COM_NAV_TREE', @level2type=N'COLUMN',@level2name=N'SHOWNAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'字段类型（前端可根据类型做相应的业务逻辑）' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'COM_NAV_TREE', @level2type=N'COLUMN',@level2name=N'FIELDTYPE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'顺序' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'COM_NAV_TREE', @level2type=N'COLUMN',@level2name=N'ORD'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'树节点，根节点为0,记录父节点ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'COM_NAV_TREE', @level2type=N'COLUMN',@level2name=N'PARENT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'1、文书  0：其他' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'COM_NAV_TREE', @level2type=N'COLUMN',@level2name=N'TYPE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'1:作废' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'COM_NAV_TREE', @level2type=N'COLUMN',@level2name=N'STATUS'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'前端请求url' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'COM_NAV_TREE', @level2type=N'COLUMN',@level2name=N'URL'
GO
/****** Object:  Table [dbo].[COM_DIC]    Script Date: 11/12/2015 14:32:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[COM_DIC](
	[ID] [bigint] IDENTITY(1,1) NOT NULL,
	[DIC_TYPE] [varchar](20) NOT NULL,
	[CODE] [varchar](10) NOT NULL,
	[NAME] [varchar](50) NOT NULL,
	[HIS_CODE] [varchar](30) NULL,
	[HIS_NAME] [varchar](50) NULL,
	[FLAG] [char](1) NULL,
	[SORT_NO] [int] NULL,
	[LIQUOR] [char](1) NULL,
	IS_INFUSION_CARD char(1) default 0,
	IS_ORD_LABEL char(1) default 0
 CONSTRAINT [PK_COM_DIC_1] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'自增长，序列' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'COM_DIC', @level2type=N'COLUMN',@level2name=N'ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'用法：USAGE  收费类型：CHARGE' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'COM_DIC', @level2type=N'COLUMN',@level2name=N'DIC_TYPE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'编码' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'COM_DIC', @level2type=N'COLUMN',@level2name=N'CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'名称' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'COM_DIC', @level2type=N'COLUMN',@level2name=N'NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'HIS编码' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'COM_DIC', @level2type=N'COLUMN',@level2name=N'HIS_CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'HIS名称' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'COM_DIC', @level2type=N'COLUMN',@level2name=N'HIS_NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'标记(0:无效,1:有效)' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'COM_DIC', @level2type=N'COLUMN',@level2name=N'FLAG'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'是否允许配液(0:不允许,1:允许)' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'COM_DIC', @level2type=N'COLUMN',@level2name=N'LIQUOR'
GO
/****** Object:  Table [dbo].[COM_DEPARTMENT]    Script Date: 11/12/2015 14:32:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[COM_DEPARTMENT](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[CODE] [varchar](10) NOT NULL,
	[NAME] [varchar](30) NULL,
	[SEARCH_CODE] [varchar](20) NULL,
	[FLAG] [varchar](1) NOT NULL,
	[SYNC_CREATE] [datetime] NULL,
	[SYNC_UPDATE] [datetime] NULL,
	[CODE_SUP] [varchar](10) NULL,
 CONSTRAINT [PK_COM_DEPARTMENT] PRIMARY KEY CLUSTERED 
(
	[ID] ASC,
	[CODE] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'科室自增长号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'COM_DEPARTMENT', @level2type=N'COLUMN',@level2name=N'ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'科室代码' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'COM_DEPARTMENT', @level2type=N'COLUMN',@level2name=N'CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'科室名称' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'COM_DEPARTMENT', @level2type=N'COLUMN',@level2name=N'NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'科室查询代码' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'COM_DEPARTMENT', @level2type=N'COLUMN',@level2name=N'SEARCH_CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'标记(0:无效,1:有效)' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'COM_DEPARTMENT', @level2type=N'COLUMN',@level2name=N'FLAG'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'创建时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'COM_DEPARTMENT', @level2type=N'COLUMN',@level2name=N'SYNC_CREATE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'修改时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'COM_DEPARTMENT', @level2type=N'COLUMN',@level2name=N'SYNC_UPDATE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'科室表' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'COM_DEPARTMENT'
GO
/****** Object:  Table [dbo].[COM_BED]    Script Date: 11/12/2015 14:32:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[COM_BED](
	[ID] [bigint] IDENTITY(1,1) NOT NULL,
	[CODE] [varchar](10) NOT NULL,
	[WARD_CODE] [varchar](10) NOT NULL,
	[BED_TYPE] [varchar](10) NULL,
	[BED_TYPE_NAME] [varchar](30) NULL,
	[BED_PRICE] [decimal](5, 2) NULL,
	[TAGS] [varchar](50) NULL,
	[FLAG] [varchar](1) NULL,
	[SYNC_CREATE] [datetime] NULL,
	[SYNC_UPDATE] [datetime] NULL,
	[HIS_CODE] [varchar](20) NULL,
 CONSTRAINT [PK_COM_BED] PRIMARY KEY CLUSTERED 
(
	[ID] ASC,
	[CODE] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY],
 CONSTRAINT [UNIQUE_COM_BED] UNIQUE NONCLUSTERED 
(
	[CODE] ASC,
	[WARD_CODE] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'床位自增长号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'COM_BED', @level2type=N'COLUMN',@level2name=N'ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'床位代码' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'COM_BED', @level2type=N'COLUMN',@level2name=N'CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'COM_WARD(病区代码)' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'COM_BED', @level2type=N'COLUMN',@level2name=N'WARD_CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'床位类型' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'COM_BED', @level2type=N'COLUMN',@level2name=N'BED_TYPE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'床位类型名称' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'COM_BED', @level2type=N'COLUMN',@level2name=N'BED_TYPE_NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'床位价格' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'COM_BED', @level2type=N'COLUMN',@level2name=N'BED_PRICE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'附加说明' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'COM_BED', @level2type=N'COLUMN',@level2name=N'TAGS'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'标记(0:无效,1:有效)' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'COM_BED', @level2type=N'COLUMN',@level2name=N'FLAG'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'创建时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'COM_BED', @level2type=N'COLUMN',@level2name=N'SYNC_CREATE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'修改时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'COM_BED', @level2type=N'COLUMN',@level2name=N'SYNC_UPDATE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'床位表' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'COM_BED'
GO
/****** Object:  Default [DF__COM_BED__BED_PRI__664B26CC]    Script Date: 11/12/2015 14:32:34 ******/
ALTER TABLE [dbo].[COM_BED] ADD  CONSTRAINT [DF__COM_BED__BED_PRI__664B26CC]  DEFAULT ((0)) FOR [BED_PRICE]
GO
/****** Object:  Default [DF__COM_BED__FLAG__673F4B05]    Script Date: 11/12/2015 14:32:34 ******/
ALTER TABLE [dbo].[COM_BED] ADD  CONSTRAINT [DF__COM_BED__FLAG__673F4B05]  DEFAULT ('1') FOR [FLAG]
GO
/****** Object:  Default [DF__COM_DEPART__FLAG__6A1BB7B0]    Script Date: 11/12/2015 14:32:34 ******/
ALTER TABLE [dbo].[COM_DEPARTMENT] ADD  CONSTRAINT [DF__COM_DEPART__FLAG__6A1BB7B0]  DEFAULT ('1') FOR [FLAG]
GO
/****** Object:  Default [DF_COM_DIC_FLAG]    Script Date: 11/12/2015 14:32:34 ******/
ALTER TABLE [dbo].[COM_DIC] ADD  CONSTRAINT [DF_COM_DIC_FLAG]  DEFAULT ((1)) FOR [FLAG]
GO
/****** Object:  Default [DF__COM_DIC__SORT_NO__54CB950F]    Script Date: 11/12/2015 14:32:34 ******/
ALTER TABLE [dbo].[COM_DIC] ADD  DEFAULT ((0)) FOR [SORT_NO]
GO
/****** Object:  Default [DF_COM_NAV_TREE_PATIENT_ID]    Script Date: 11/12/2015 14:32:34 ******/
ALTER TABLE [dbo].[COM_NAV_TREE] ADD  CONSTRAINT [DF_COM_NAV_TREE_PATIENT_ID]  DEFAULT ((0)) FOR [PARENT_ID]
GO
/****** Object:  Default [DF_COM_NAV_TREE_TYPE]    Script Date: 11/12/2015 14:32:34 ******/
ALTER TABLE [dbo].[COM_NAV_TREE] ADD  CONSTRAINT [DF_COM_NAV_TREE_TYPE]  DEFAULT ((0)) FOR [TYPE]
GO
/****** Object:  Default [DF_COM_NAV_TREE_status]    Script Date: 11/12/2015 14:32:34 ******/
ALTER TABLE [dbo].[COM_NAV_TREE] ADD  CONSTRAINT [DF_COM_NAV_TREE_status]  DEFAULT ((0)) FOR [STATUS]
GO
/****** Object:  Default [DF__COM_PERMIS__FLAG__6CF8245B]    Script Date: 11/12/2015 14:32:34 ******/
ALTER TABLE [dbo].[COM_PERMISSION] ADD  CONSTRAINT [DF__COM_PERMIS__FLAG__6CF8245B]  DEFAULT ('1') FOR [FLAG]
GO
/****** Object:  Default [DF__COM_ROLE__FLAG__6FD49106]    Script Date: 11/12/2015 14:32:34 ******/
ALTER TABLE [dbo].[COM_ROLE] ADD  CONSTRAINT [DF__COM_ROLE__FLAG__6FD49106]  DEFAULT ('1') FOR [FLAG]
GO
/****** Object:  Default [DF__COM_USER__FLAG__73A521EA]    Script Date: 11/12/2015 14:32:34 ******/
ALTER TABLE [dbo].[COM_USER] ADD  CONSTRAINT [DF__COM_USER__FLAG__73A521EA]  DEFAULT ('1') FOR [FLAG]
GO
/****** Object:  Default [DF__COM_WARD__FLAG__7A521F79]    Script Date: 11/12/2015 14:32:34 ******/
ALTER TABLE [dbo].[COM_WARD] ADD  CONSTRAINT [DF__COM_WARD__FLAG__7A521F79]  DEFAULT ('1') FOR [FLAG]
GO
/****** Object:  Default [DF__DIC_FREQ__FLAG__000AF8CF]    Script Date: 11/12/2015 14:32:34 ******/
ALTER TABLE [dbo].[DIC_FREQ] ADD  CONSTRAINT [DF__DIC_FREQ__FLAG__000AF8CF]  DEFAULT ('1') FOR [FLAG]
GO
/****** Object:  Default [DF__DIC_ORDER___FLAG__02E7657A]    Script Date: 11/12/2015 14:32:34 ******/
ALTER TABLE [dbo].[DIC_ORDER_TYPE] ADD  CONSTRAINT [DF__DIC_ORDER___FLAG__02E7657A]  DEFAULT ('1') FOR [FLAG]
GO
/****** Object:  Default [DF__DIC_USAGE__FLAG__05C3D225]    Script Date: 11/12/2015 14:32:34 ******/
ALTER TABLE [dbo].[DIC_USAGE] ADD  CONSTRAINT [DF__DIC_USAGE__FLAG__05C3D225]  DEFAULT ('1') FOR [FLAG]
GO
/****** Object:  Default [DF__doc_confi__CREAT__552B87C2]    Script Date: 11/12/2015 14:32:34 ******/
ALTER TABLE [dbo].[DOC_CONFIG] ADD  CONSTRAINT [DF__doc_confi__CREAT__552B87C2]  DEFAULT (getdate()) FOR [CREATE_TIME]
GO
/****** Object:  Default [DF_METADATA_is_dyna]    Script Date: 11/12/2015 14:32:34 ******/
ALTER TABLE [dbo].[DOC_METADATA] ADD  CONSTRAINT [DF_METADATA_is_dyna]  DEFAULT ('N') FOR [IS_DYNA]
GO
/****** Object:  Default [DF_DOC_REPORT_DATA_template_id]    Script Date: 11/12/2015 14:32:34 ******/
ALTER TABLE [dbo].[DOC_REPORT_DATA] ADD  CONSTRAINT [DF_DOC_REPORT_DATA_template_id]  DEFAULT ((1)) FOR [TEMPLATE_ID]
GO
/****** Object:  Default [DF_DOC_REPORT_DATA_INPATIENT_NO]    Script Date: 11/12/2015 14:32:34 ******/
ALTER TABLE [dbo].[DOC_REPORT_DATA] ADD  CONSTRAINT [DF_DOC_REPORT_DATA_INPATIENT_NO]  DEFAULT ((1)) FOR [INPATIENT_NO]
GO
/****** Object:  Default [DF_DOC_REPORT_DATA_valid]    Script Date: 11/12/2015 14:32:34 ******/
ALTER TABLE [dbo].[DOC_REPORT_DATA] ADD  CONSTRAINT [DF_DOC_REPORT_DATA_valid]  DEFAULT ((0)) FOR [VALID]
GO
/****** Object:  Default [DF__DOC_REPOR__APPRO__55F4C372]    Script Date: 11/12/2015 14:32:34 ******/
ALTER TABLE [dbo].[DOC_REPORT_DATA] ADD  DEFAULT ('N') FOR [APPROVE_STATUS]
GO
/****** Object:  Default [DF_lx_report_template_show_index]    Script Date: 11/12/2015 14:32:34 ******/
ALTER TABLE [dbo].[DOC_REPORT_TEMPLATE] ADD  CONSTRAINT [DF_lx_report_template_show_index]  DEFAULT ((0)) FOR [SHOW_INDEX]
GO
/****** Object:  Default [DF_lx_report_template_using]    Script Date: 11/12/2015 14:32:34 ******/
ALTER TABLE [dbo].[DOC_REPORT_TEMPLATE] ADD  CONSTRAINT [DF_lx_report_template_using]  DEFAULT ((0)) FOR [VALID]
GO
/****** Object:  Default [DF_doc_type_doc_type_valid]    Script Date: 11/12/2015 14:32:34 ******/
ALTER TABLE [dbo].[DOC_TYPE] ADD  CONSTRAINT [DF_doc_type_doc_type_valid]  DEFAULT ('Y') FOR [DOC_TYPE_VALID]
GO
/****** Object:  Default [DF__INM_ALARM__CREAT__7AF13DF7]    Script Date: 11/12/2015 14:32:34 ******/
ALTER TABLE [dbo].[INM_ALARM_INFO] ADD  DEFAULT (getdate()) FOR [CREATE_TIME]
GO
/****** Object:  Default [DF__INM_DEVIC__CREAT__7BE56230]    Script Date: 11/12/2015 14:32:34 ******/
ALTER TABLE [dbo].[INM_DEVICE_INFO] ADD  DEFAULT (getdate()) FOR [CREATE_TIME]
GO
/****** Object:  Default [DF__INM_DEVIC__CREAT__7CD98669]    Script Date: 11/12/2015 14:32:34 ******/
ALTER TABLE [dbo].[INM_DEVICE_SET] ADD  DEFAULT (getdate()) FOR [CREATE_TIME]
GO
/****** Object:  Default [DF__INM_GATHE__CREAT__7DCDAAA2]    Script Date: 11/12/2015 14:32:34 ******/
ALTER TABLE [dbo].[INM_GATHER_INFO] ADD  DEFAULT (getdate()) FOR [CREATE_TIME]
GO
/****** Object:  Default [DF__INM_INFUS__CREAT__7EC1CEDB]    Script Date: 11/12/2015 14:32:34 ******/
ALTER TABLE [dbo].[INM_INFUSION_STAT] ADD  DEFAULT (getdate()) FOR [CREATE_TIME]
GO
/****** Object:  Default [DF__INM_PERBA__CREAT__7FB5F314]    Script Date: 11/12/2015 14:32:34 ******/
ALTER TABLE [dbo].[INM_PERBAG_DETAIL_INFO] ADD  DEFAULT (getdate()) FOR [CREATE_TIME]
GO
/****** Object:  Default [DF__INM_PERBA__CREAT__00AA174D]    Script Date: 11/12/2015 14:32:34 ******/
ALTER TABLE [dbo].[INM_PERBAG_INFO] ADD  DEFAULT (getdate()) FOR [CREATE_TIME]
GO
/****** Object:  Default [DF__INM_WORKL__CREAT__019E3B86]    Script Date: 11/12/2015 14:32:34 ******/
ALTER TABLE [dbo].[INM_WORKLOAD_STAT] ADD  DEFAULT (getdate()) FOR [CREATE_TIME]
GO
/****** Object:  Default [DF__NURSE_SHI__SHIFT__69C6B1F5]    Script Date: 11/12/2015 14:32:34 ******/
ALTER TABLE [dbo].[NURSE_SHIFT] ADD  DEFAULT ('1') FOR [SHIFT_VALID]
GO
/****** Object:  Default [DF__NURSE_SHI__SHIFT__6ABAD62E]    Script Date: 11/12/2015 14:32:34 ******/
ALTER TABLE [dbo].[NURSE_SHIFT] ADD  DEFAULT ('0') FOR [SHIFT_STATUS]
GO
/****** Object:  Default [DF__NURSE_SHI__RECOR__6501FCD8]    Script Date: 11/12/2015 14:32:34 ******/
ALTER TABLE [dbo].[NURSE_SHIFT_RECORD] ADD  DEFAULT ('1') FOR [RECORD_VALID]
GO
/****** Object:  Default [DF__NURSE_WHI__TEMPL__2FEF161B]    Script Date: 11/12/2015 14:32:34 ******/
ALTER TABLE [dbo].[NURSE_WHITE_BOARD_TEMPLATE] ADD  DEFAULT ((0)) FOR [TEMPLATE_VALID]
GO
/****** Object:  Default [DF__NURSE_WHI__IS_BO__34B3CB38]    Script Date: 11/12/2015 14:32:34 ******/
ALTER TABLE [dbo].[NURSE_WHITE_BOARD_TEMPLATE_ITEM] ADD  DEFAULT ((1)) FOR [IS_BOLD]
GO
/****** Object:  Default [DF__NURSE_WHI__IS_Sh__35A7EF71]    Script Date: 11/12/2015 14:32:34 ******/
ALTER TABLE [dbo].[NURSE_WHITE_BOARD_TEMPLATE_ITEM] ADD  DEFAULT ((1)) FOR [IS_Show]
GO
/****** Object:  Default [DF__NURSE_WHI__VALID__369C13AA]    Script Date: 11/12/2015 14:32:34 ******/
ALTER TABLE [dbo].[NURSE_WHITE_BOARD_TEMPLATE_ITEM] ADD  DEFAULT ((1)) FOR [VALID]
GO
/****** Object:  Default [DF__PAT_BODYS__ABNOR__0F4D3C5F]    Script Date: 11/12/2015 14:32:34 ******/
ALTER TABLE [dbo].[PAT_BODYSIGN_DETAIL] ADD  CONSTRAINT [DF__PAT_BODYS__ABNOR__0F4D3C5F]  DEFAULT ('N') FOR [ABNORMAL_FLAG]
GO
/****** Object:  Default [DF__PAT_BODYS__SPEC___10416098]    Script Date: 11/12/2015 14:32:34 ******/
ALTER TABLE [dbo].[PAT_BODYSIGN_DETAIL] ADD  CONSTRAINT [DF__PAT_BODYS__SPEC___10416098]  DEFAULT ('0') FOR [SPEC_MARK]
GO
/****** Object:  Default [DF__PAT_BODYS__item___2A164134]    Script Date: 11/12/2015 14:32:34 ******/
ALTER TABLE [dbo].[PAT_BODYSIGN_DETAIL] ADD  DEFAULT ((0)) FOR [item_index]
GO
/****** Object:  Default [DF__PAT_BODYS__SOURC__420DC656]    Script Date: 11/12/2015 14:32:34 ******/
ALTER TABLE [dbo].[PAT_BODYSIGN_MAS] ADD  DEFAULT ((2)) FOR [SOURCE]
GO
/****** Object:  Default [DF__PAT_CRITI__DISPO__150615B5]    Script Date: 11/12/2015 14:32:34 ******/
ALTER TABLE [dbo].[PAT_CRITICAL_VALUE] ADD  CONSTRAINT [DF__PAT_CRITI__DISPO__150615B5]  DEFAULT ('0') FOR [DISPOSE_STATUS]
GO
/****** Object:  Default [DF__PAT_CURE___IS_BA__17E28260]    Script Date: 11/12/2015 14:32:34 ******/
ALTER TABLE [dbo].[PAT_CURE_INFO] ADD  CONSTRAINT [DF__PAT_CURE___IS_BA__17E28260]  DEFAULT ('0') FOR [IS_BABY]
GO
/****** Object:  Default [DF__PAT_CURE___DANGE__18D6A699]    Script Date: 11/12/2015 14:32:34 ******/
ALTER TABLE [dbo].[PAT_CURE_INFO] ADD  CONSTRAINT [DF__PAT_CURE___DANGE__18D6A699]  DEFAULT ('N') FOR [DANGER_LEVEL]
GO
/****** Object:  Default [DF__PAT_CURE___NURSE__19CACAD2]    Script Date: 11/12/2015 14:32:34 ******/
ALTER TABLE [dbo].[PAT_CURE_INFO] ADD  CONSTRAINT [DF__PAT_CURE___NURSE__19CACAD2]  DEFAULT ('3') FOR [NURSE_LEVEL]
GO
/****** Object:  Default [DF__PAT_CURE___STATU__1ABEEF0B]    Script Date: 11/12/2015 14:32:34 ******/
ALTER TABLE [dbo].[PAT_CURE_INFO] ADD  CONSTRAINT [DF__PAT_CURE___STATU__1ABEEF0B]  DEFAULT ('1') FOR [STATUS]
GO
/****** Object:  Default [DF__PAT_CURE___PREPA__1BB31344]    Script Date: 11/12/2015 14:32:34 ******/
ALTER TABLE [dbo].[PAT_CURE_INFO] ADD  CONSTRAINT [DF__PAT_CURE___PREPA__1BB31344]  DEFAULT ((0)) FOR [PREPAY_COST]
GO
/****** Object:  Default [DF__PAT_CURE___OWN_C__1CA7377D]    Script Date: 11/12/2015 14:32:34 ******/
ALTER TABLE [dbo].[PAT_CURE_INFO] ADD  CONSTRAINT [DF__PAT_CURE___OWN_C__1CA7377D]  DEFAULT ((0)) FOR [OWN_COST]
GO
/****** Object:  Default [DF__PAT_CURE___BALAN__1D9B5BB6]    Script Date: 11/12/2015 14:32:34 ******/
ALTER TABLE [dbo].[PAT_CURE_INFO] ADD  CONSTRAINT [DF__PAT_CURE___BALAN__1D9B5BB6]  DEFAULT ((0)) FOR [BALANCE]
GO
/****** Object:  Default [DF__PAT_CURE___ISSEP__5DCAEF64]    Script Date: 11/12/2015 14:32:34 ******/
ALTER TABLE [dbo].[PAT_CURE_INFO] ADD  DEFAULT ((0)) FOR [ISSEPARATE]
GO
/****** Object:  Default [DF__PAT_DIAGN__IS_IN__2077C861]    Script Date: 11/12/2015 14:32:34 ******/
ALTER TABLE [dbo].[PAT_DIAGNOSIS] ADD  CONSTRAINT [DF__PAT_DIAGN__IS_IN__2077C861]  DEFAULT ('0') FOR [IS_IN_DIAG]
GO
/****** Object:  Default [DF__PAT_DIAGNO__FLAG__216BEC9A]    Script Date: 11/12/2015 14:32:34 ******/
ALTER TABLE [dbo].[PAT_DIAGNOSIS] ADD  CONSTRAINT [DF__PAT_DIAGNO__FLAG__216BEC9A]  DEFAULT ('1') FOR [FLAG]
GO
/****** Object:  Default [DF__PAT_INFO___IF_OP__76B698BF]    Script Date: 11/12/2015 14:32:34 ******/
ALTER TABLE [dbo].[PAT_INFO_STAT] ADD  DEFAULT ('0') FOR [IF_OPERATION]
GO
/****** Object:  Default [DF__PAT_INFO___IF_FE__77AABCF8]    Script Date: 11/12/2015 14:32:34 ******/
ALTER TABLE [dbo].[PAT_INFO_STAT] ADD  DEFAULT ('0') FOR [IF_FEVER]
GO
/****** Object:  Default [DF__PAT_INFO___FALL___789EE131]    Script Date: 11/12/2015 14:32:34 ******/
ALTER TABLE [dbo].[PAT_INFO_STAT] ADD  DEFAULT ('0') FOR [FALL_SCORE]
GO
/****** Object:  Default [DF__PAT_INFO___PRESS__7993056A]    Script Date: 11/12/2015 14:32:34 ******/
ALTER TABLE [dbo].[PAT_INFO_STAT] ADD  DEFAULT ('0') FOR [PRESSURE_SORE]
GO
/****** Object:  Default [DF__PAT_INFO___IF_SE__7A8729A3]    Script Date: 11/12/2015 14:32:34 ******/
ALTER TABLE [dbo].[PAT_INFO_STAT] ADD  DEFAULT ('0') FOR [IF_SEPARATE]
GO
/****** Object:  Default [DF__PAT_INFO___CREAT__7B7B4DDC]    Script Date: 11/12/2015 14:32:34 ******/
ALTER TABLE [dbo].[PAT_INFO_STAT] ADD  DEFAULT (getdate()) FOR [CREATE_TIME]
GO
/****** Object:  Default [DF__PAT_INFUS__ABNOR__644DCFC1]    Script Date: 11/12/2015 14:32:34 ******/
ALTER TABLE [dbo].[PAT_INFUSION_MONITOR_ITEM] ADD  CONSTRAINT [DF__PAT_INFUS__ABNOR__644DCFC1]  DEFAULT ((0)) FOR [ABNORMAL]
GO
/****** Object:  Default [DF__PAT_INFUS__DELIV__6541F3FA]    Script Date: 11/12/2015 14:32:34 ******/
ALTER TABLE [dbo].[PAT_INFUSION_MONITOR_ITEM] ADD  CONSTRAINT [DF__PAT_INFUS__DELIV__6541F3FA]  DEFAULT ((0)) FOR [DELIVER_SPEED]
GO
/****** Object:  Default [DF__PAT_INFUS__RESID__66361833]    Script Date: 11/12/2015 14:32:34 ******/
ALTER TABLE [dbo].[PAT_INFUSION_MONITOR_ITEM] ADD  CONSTRAINT [DF__PAT_INFUS__RESID__66361833]  DEFAULT ((0)) FOR [RESIDUE]
GO
/****** Object:  Default [DF__PAT_LAB_T__NORMA__57A801BA]    Script Date: 11/12/2015 14:32:34 ******/
ALTER TABLE [dbo].[PAT_LAB_TEST_DETAIL] ADD  CONSTRAINT [DF__PAT_LAB_T__NORMA__57A801BA]  DEFAULT ('N') FOR [NORMAL_FLAG]
GO
/****** Object:  Default [DF__PAT_LAB_T__STATU__5A846E65]    Script Date: 11/12/2015 14:32:34 ******/
ALTER TABLE [dbo].[PAT_LAB_TEST_MAS] ADD  CONSTRAINT [DF__PAT_LAB_T__STATU__5A846E65]  DEFAULT ('R') FOR [STATUS]
GO
/****** Object:  Default [DF__PAT_OPERA__NEEDW__3296789C]    Script Date: 11/12/2015 14:32:34 ******/
ALTER TABLE [dbo].[PAT_OPERATION] ADD  CONSTRAINT [DF__PAT_OPERA__NEEDW__3296789C]  DEFAULT ('0') FOR [NEEDWASH_NURSE]
GO
/****** Object:  Default [DF__PAT_OPERA__EMERG__338A9CD5]    Script Date: 11/12/2015 14:32:34 ******/
ALTER TABLE [dbo].[PAT_OPERATION] ADD  CONSTRAINT [DF__PAT_OPERA__EMERG__338A9CD5]  DEFAULT ('0') FOR [EMERGENCY]
GO
/****** Object:  Default [DF__PAT_ORDER__IS_LO__0AF29B96]    Script Date: 11/12/2015 14:32:34 ******/
ALTER TABLE [dbo].[PAT_ORDER_GROUP] ADD  CONSTRAINT [DF__PAT_ORDER__IS_LO__0AF29B96]  DEFAULT ('0') FOR [IS_LONGTERM]
GO
/****** Object:  Default [DF__PAT_ORDER__IS_SK__0BE6BFCF]    Script Date: 11/12/2015 14:32:34 ******/
ALTER TABLE [dbo].[PAT_ORDER_GROUP] ADD  CONSTRAINT [DF__PAT_ORDER__IS_SK__0BE6BFCF]  DEFAULT ('0') FOR [IS_SKIN_TEST]
GO
/****** Object:  Default [DF__PAT_ORDER__IS_EM__0CDAE408]    Script Date: 11/12/2015 14:32:34 ******/
ALTER TABLE [dbo].[PAT_ORDER_GROUP] ADD  CONSTRAINT [DF__PAT_ORDER__IS_EM__0CDAE408]  DEFAULT ('0') FOR [IS_EMERGENT]
GO
/****** Object:  Default [DF__PAT_ORDER__SKIN___119F9925]    Script Date: 11/12/2015 14:32:34 ******/
ALTER TABLE [dbo].[PAT_ORDER_ITEM] ADD  CONSTRAINT [DF__PAT_ORDER__SKIN___119F9925]  DEFAULT ('0') FOR [SKIN_TEST_REQUIRED]
GO
/****** Object:  Default [DF__PAT_ORDER__IS_HI__59063A47]    Script Date: 11/12/2015 14:32:34 ******/
ALTER TABLE [dbo].[PAT_ORDER_ITEM] ADD  CONSTRAINT [DF__PAT_ORDER__IS_HI__59063A47]  DEFAULT ('0') FOR [IS_HIGH_RISK]
GO
/****** Object:  Default [DF__PAT_ORDER__IS_LO__42CCE065]    Script Date: 11/12/2015 14:32:34 ******/
ALTER TABLE [dbo].[PAT_ORDER_ORIGINAL] ADD  CONSTRAINT [DF__PAT_ORDER__IS_LO__42CCE065]  DEFAULT ('0') FOR [IS_LONGTERM]
GO
/****** Object:  Default [DF__PAT_ORDER__ORDER__43C1049E]    Script Date: 11/12/2015 14:32:34 ******/
ALTER TABLE [dbo].[PAT_ORDER_ORIGINAL] ADD  CONSTRAINT [DF__PAT_ORDER__ORDER__43C1049E]  DEFAULT ('0') FOR [ORDER_STATUS]
GO
/****** Object:  Default [DF__PAT_ORDER__ITEM___44B528D7]    Script Date: 11/12/2015 14:32:34 ******/
ALTER TABLE [dbo].[PAT_ORDER_ORIGINAL] ADD  CONSTRAINT [DF__PAT_ORDER__ITEM___44B528D7]  DEFAULT ((0)) FOR [ITEM_PRICE]
GO
/****** Object:  Default [DF__PAT_PRINT__IS_PR__5A846E65]    Script Date: 11/12/2015 14:32:34 ******/
ALTER TABLE [dbo].[PAT_PRINT_INFO] ADD  DEFAULT ('0') FOR [IS_PRINT_BARCODE]
GO
/****** Object:  Default [DF__PAT_PRINT__IS_PR__5B78929E]    Script Date: 11/12/2015 14:32:34 ******/
ALTER TABLE [dbo].[PAT_PRINT_INFO] ADD  DEFAULT ('0') FOR [IS_PRINT_BED]
GO
/****** Object:  Default [DF__PAT_PRINT__IS_PR__5C6CB6D7]    Script Date: 11/12/2015 14:32:34 ******/
ALTER TABLE [dbo].[PAT_PRINT_INFO] ADD  DEFAULT ('0') FOR [IS_PRINT_LABEL]
GO
/****** Object:  Default [DF__PAT_SKIN___STATU__2FCF1A8A]    Script Date: 11/12/2015 14:32:34 ******/
ALTER TABLE [dbo].[PAT_SKIN_TEST] ADD  DEFAULT ('0') FOR [STATUS]
GO
/****** Object:  Default [DF__PAT_SKIN___ST_IT__3651FAE7]    Script Date: 11/12/2015 14:32:34 ******/
ALTER TABLE [dbo].[PAT_SKIN_TEST] ADD  CONSTRAINT [DF__PAT_SKIN___ST_IT__3651FAE7]  DEFAULT ('N') FOR [RESULT]
GO
/****** Object:  Default [DF__PAT_SKIN___test___31B762FC]    Script Date: 11/12/2015 14:32:34 ******/
ALTER TABLE [dbo].[PAT_SKIN_TEST] ADD  DEFAULT ((0)) FOR [test_index]
GO
/****** Object:  Default [DF__PAT_TRANSF__FLAG__4F32B74A]    Script Date: 11/12/2015 14:32:34 ******/
ALTER TABLE [dbo].[PAT_TRANSFER] ADD  CONSTRAINT [DF__PAT_TRANSF__FLAG__4F32B74A]  DEFAULT ('1') FOR [FLAG]
GO
/****** Object:  Default [DELFAULT_CONFIG_TYPE]    Script Date: 11/12/2015 14:32:34 ******/
ALTER TABLE [dbo].[SYS_CONFIG] ADD  CONSTRAINT [DELFAULT_CONFIG_TYPE]  DEFAULT ('S') FOR [CONFIG_TYPE]
GO
/****** Object:  Default [DF__SYS_CONFI__VALID__520F23F5]    Script Date: 11/12/2015 14:32:34 ******/
ALTER TABLE [dbo].[SYS_CONFIG] ADD  CONSTRAINT [DF__SYS_CONFI__VALID__520F23F5]  DEFAULT ('1') FOR [VALID_FLAG]
GO

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
	[UPDATE_USER_NAME] [varchar](20) NULL,
	[OUT_SHAPE_CODE] [varchar](20) NULL,
	[OUT_SHAPE_NAME] [varchar](20) NULL,
	[OUT_COLOR_CODE] [varchar](20) NULL,
	[OUT_COLOR_NAME] [varchar](20) NULL
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

--小白板数据库脚本
/****** Object:  Table [dbo].[WHITE_BOARD_ITEM]    Script Date: 03/24/2016 15:32:51 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[WHITE_BOARD_ITEM](
	[ID] [bigint] IDENTITY(1,1) NOT NULL,
	[ITEM_CODE] [varchar](20) NULL,
	[ITEM_NAME] [varchar](100) NULL,
	[HIS_CODE] [varchar](30) NULL,
	[HIS_NAME] [varchar](100) NULL,
	[SEARCH_KEY] [varchar](50) NULL,
	[QUERY_ORDER] [int] NULL,
	[IS_ADDFREQ] [int] NULL,
	[QUERY_TYPE] [int] NULL,
	[ps] [varchar](50) NULL
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'小白板关注项目id 递增长' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WHITE_BOARD_ITEM', @level2type=N'COLUMN',@level2name=N'ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'项目编号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WHITE_BOARD_ITEM', @level2type=N'COLUMN',@level2name=N'ITEM_CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'项目名称' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WHITE_BOARD_ITEM', @level2type=N'COLUMN',@level2name=N'ITEM_NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'HIS项目编码' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WHITE_BOARD_ITEM', @level2type=N'COLUMN',@level2name=N'HIS_CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'his项目名称' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WHITE_BOARD_ITEM', @level2type=N'COLUMN',@level2name=N'HIS_NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'查询关键字' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WHITE_BOARD_ITEM', @level2type=N'COLUMN',@level2name=N'SEARCH_KEY'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'1:从医嘱中提取 0：默认手工录入' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WHITE_BOARD_ITEM', @level2type=N'COLUMN',@level2name=N'QUERY_ORDER'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'1:不需要根据频次分组显示 默认为0' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WHITE_BOARD_ITEM', @level2type=N'COLUMN',@level2name=N'IS_ADDFREQ'
GO
/****** Object:  Table [dbo].[WHITE_BOARD_DATA]    Script Date: 03/24/2016 15:32:51 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[WHITE_BOARD_DATA](
	[ID] [bigint] IDENTITY(1,1) NOT NULL,
	[PAT_ID] [varchar](20) NULL,
	[BOARD_ITEM_ID] [bigint] NULL,
	[ITEM_NAME] [varchar](100) NULL,
	[FREQ_CODE] [varchar](10) NULL,
	[FREQ_NAME] [varchar](30) NULL,
	[IS_STOP] [int] NULL,
	[ITEM_CODE] [varchar](30) NULL,
	[ITEM_DATE] [datetime] NULL,
	[IS_LONGTERM] [varchar](1) NULL,
	[CREATE_DATETIME] [datetime] NULL,
	[STOP_DATE] [datetime] NULL,
	[ORDER_ID] [varchar](32) NULL,
	[DOSAGE] [decimal](12, 5) NULL,
	[DOSAGE_UNIT] [varchar](20) NULL
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'患者id' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WHITE_BOARD_DATA', @level2type=N'COLUMN',@level2name=N'PAT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'小白板项目id' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WHITE_BOARD_DATA', @level2type=N'COLUMN',@level2name=N'BOARD_ITEM_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'项目名称' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WHITE_BOARD_DATA', @level2type=N'COLUMN',@level2name=N'ITEM_NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'频次编号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WHITE_BOARD_DATA', @level2type=N'COLUMN',@level2name=N'FREQ_CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'频次名称' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WHITE_BOARD_DATA', @level2type=N'COLUMN',@level2name=N'FREQ_NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'1:停用  0：正常' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WHITE_BOARD_DATA', @level2type=N'COLUMN',@level2name=N'IS_STOP'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'是否长期医嘱(0:临时,1:长期)' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WHITE_BOARD_DATA', @level2type=N'COLUMN',@level2name=N'IS_LONGTERM'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'开立时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WHITE_BOARD_DATA', @level2type=N'COLUMN',@level2name=N'CREATE_DATETIME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'停止时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WHITE_BOARD_DATA', @level2type=N'COLUMN',@level2name=N'STOP_DATE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'医嘱id' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WHITE_BOARD_DATA', @level2type=N'COLUMN',@level2name=N'ORDER_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'剂量' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WHITE_BOARD_DATA', @level2type=N'COLUMN',@level2name=N'DOSAGE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'剂量单位' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'WHITE_BOARD_DATA', @level2type=N'COLUMN',@level2name=N'DOSAGE_UNIT'
GO
/****** Object:  Table [dbo].[NURSE_WHITE_BOARD_TEMPLATE]    Script Date: 03/24/2016 15:32:51 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[NURSE_WHITE_BOARD_TEMPLATE](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[DEPT_CODE] [varchar](20) NOT NULL,
	[TITLE] [varchar](100) NULL,
	[TITLE_FONT_SIZE] [int] NULL,
	[ROW_COUNT] [int] NULL,
	[BACKGROUND] [varchar](10) NULL,
	[IS_VALID] [char](1) NULL,
PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[NURSE_WHITE_BOARD_RECORD_ITEM]    Script Date: 03/24/2016 15:32:51 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
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
	[IS_FINISH] [char](1) NULL,
	[END_ITEM_DATE] [datetime] NULL,
	[START_ITEM_DATE] [datetime] NULL,
	[IS_VALID] [char](1) NULL,
PRIMARY KEY CLUSTERED 
(
	[ITEM_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'主键id' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_WHITE_BOARD_RECORD_ITEM', @level2type=N'COLUMN',@level2name=N'ITEM_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'关联记录id' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_WHITE_BOARD_RECORD_ITEM', @level2type=N'COLUMN',@level2name=N'RECORD_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'子项记录code' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_WHITE_BOARD_RECORD_ITEM', @level2type=N'COLUMN',@level2name=N'ITEM_CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'子项记录name' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_WHITE_BOARD_RECORD_ITEM', @level2type=N'COLUMN',@level2name=N'ITEM_NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'z子项记录值' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_WHITE_BOARD_RECORD_ITEM', @level2type=N'COLUMN',@level2name=N'ITEM_VALUE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'子项执行时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_WHITE_BOARD_RECORD_ITEM', @level2type=N'COLUMN',@level2name=N'EXEC_DATE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'子项记录日期' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_WHITE_BOARD_RECORD_ITEM', @level2type=N'COLUMN',@level2name=N'ITEM_DATE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'患者ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_WHITE_BOARD_RECORD_ITEM', @level2type=N'COLUMN',@level2name=N'ITEM_PAT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'患者信息' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_WHITE_BOARD_RECORD_ITEM', @level2type=N'COLUMN',@level2name=N'ITEM_PAT_INFO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'是否完成' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_WHITE_BOARD_RECORD_ITEM', @level2type=N'COLUMN',@level2name=N'IS_FINISH'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'结束提醒时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_WHITE_BOARD_RECORD_ITEM', @level2type=N'COLUMN',@level2name=N'END_ITEM_DATE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'开始提醒时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_WHITE_BOARD_RECORD_ITEM', @level2type=N'COLUMN',@level2name=N'START_ITEM_DATE'
GO
/****** Object:  Table [dbo].[NURSE_WHITE_BOARD_RECORD]    Script Date: 03/24/2016 15:32:51 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING OFF
GO
CREATE TABLE [dbo].[NURSE_WHITE_BOARD_RECORD](
	[RECORD_ID] [bigint] IDENTITY(1,1) NOT NULL,
	[DEPT_CODE] [varchar](20) NULL,
	[RECORD_CODE] [varchar](30) NULL,
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
	[random_id] [varchar](100) NULL,
PRIMARY KEY CLUSTERED 
(
	[RECORD_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[NURSE_WHITE_BOARD_METADATA_VALUE]    Script Date: 03/24/2016 15:32:51 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[NURSE_WHITE_BOARD_METADATA_VALUE](
	[MV_ID] [bigint] IDENTITY(1,1) NOT NULL,
	[MV_TYPE] [varchar](20) NOT NULL,
	[MV_NAME] [varchar](30) NULL,
	[MV_VALUE] [varchar](50) NULL,
	[MV_REMARK] [varchar](100) NULL,
	[IS_VALID] [char](1) NULL,
	[MV_CODE] [varchar](30) NULL,
PRIMARY KEY CLUSTERED 
(
	[MV_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'主键id' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_WHITE_BOARD_METADATA_VALUE', @level2type=N'COLUMN',@level2name=N'MV_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'元数据name' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_WHITE_BOARD_METADATA_VALUE', @level2type=N'COLUMN',@level2name=N'MV_TYPE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'元数据code' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_WHITE_BOARD_METADATA_VALUE', @level2type=N'COLUMN',@level2name=N'MV_NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'值' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_WHITE_BOARD_METADATA_VALUE', @level2type=N'COLUMN',@level2name=N'MV_VALUE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'值说明' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_WHITE_BOARD_METADATA_VALUE', @level2type=N'COLUMN',@level2name=N'MV_REMARK'
GO
/****** Object:  Table [dbo].[NURSE_WHITE_BOARD_METADATA]    Script Date: 03/24/2016 15:32:51 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[NURSE_WHITE_BOARD_METADATA](
	[ID] [bigint] IDENTITY(1,1) NOT NULL,
	[CODE] [varchar](20) NOT NULL,
	[NAME] [varchar](30) NULL,
	[PARENT_ID] [varchar](20) NULL,
	[LEVEL] [int] NULL,
	[INPUT_TYPE] [varchar](20) NULL,
	[CREATE_DATE] [datetime] NULL,
	[DEPT_CODE] [varchar](20) NULL,
	[IS_AUTO] [char](1) NULL,
	[IS_VALID] [char](1) NULL,
	[SIMPLE_NAME] [varchar](50) NULL,
	[IS_EXEC] [char](1) NULL,
	[ROW_NO] [int] NULL,
	[COLUMN_NO] [int] NULL,
	[SHOW_TYPE] [varchar](20) NULL,
	[IS_EDIT] [char](1) NULL,
	[COLUMN_TYPE] [char](1) NULL,
	[background_color] [varchar](20) NULL,
	[IS_BED_CODE] [char](1) NULL,
	[is_colspan] [char](1) NULL,
	[HEIGHT] [decimal](5, 1) NULL,
	[WIDTH] [decimal](5, 1) NULL,
	[FONT_SIZE] [decimal](5, 1) NULL,
	[TEMPLATE_ID] [varchar](20) NULL,
	[IS_SHOW_TITLE] [char](1) NULL,
	[IS_SHOW_DATA] [char](1) NULL,
	[IS_SHOW_LINE_R] [char](1) NULL,
	[IS_SHOW_LINE_B] [char](1) NULL,
	[IS_SHOW_LINE_T] [char](1) NULL,
	[INCLUDE_ROW] [int] NULL,
	[TITLE_FONT_SIZE] [int] NULL,
	[CONTENT_FONT_SIZE] [int] NULL,
	[IS_DOSAGE] [char](1) NULL,
	[TITLE_WIDTH] [decimal](5, 1) NULL,
PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'主键id' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_WHITE_BOARD_METADATA', @level2type=N'COLUMN',@level2name=N'ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'元数据CODE' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_WHITE_BOARD_METADATA', @level2type=N'COLUMN',@level2name=N'CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'元数据NAME' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_WHITE_BOARD_METADATA', @level2type=N'COLUMN',@level2name=N'NAME'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'父主键Id' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_WHITE_BOARD_METADATA', @level2type=N'COLUMN',@level2name=N'PARENT_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'深度' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_WHITE_BOARD_METADATA', @level2type=N'COLUMN',@level2name=N'LEVEL'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'输入类型' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_WHITE_BOARD_METADATA', @level2type=N'COLUMN',@level2name=N'INPUT_TYPE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'创建时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_WHITE_BOARD_METADATA', @level2type=N'COLUMN',@level2name=N'CREATE_DATE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'部门' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_WHITE_BOARD_METADATA', @level2type=N'COLUMN',@level2name=N'DEPT_CODE'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'是否自动加载' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_WHITE_BOARD_METADATA', @level2type=N'COLUMN',@level2name=N'IS_AUTO'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'是否显示' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_WHITE_BOARD_METADATA', @level2type=N'COLUMN',@level2name=N'IS_VALID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'显示标题' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'NURSE_WHITE_BOARD_METADATA', @level2type=N'COLUMN',@level2name=N'SIMPLE_NAME'
GO
/****** Object:  Table [dbo].[NURSE_WHITE_BOARD_EDIT_TYPE_DIC]    Script Date: 03/24/2016 15:32:51 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[NURSE_WHITE_BOARD_EDIT_TYPE_DIC](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[TYPE] [varchar](20) NULL,
	[CODE] [varchar](20) NULL,
	[IS_MULTI] [char](1) NULL,
	[IS_INNER] [char](1) NULL,
	[IS_VALID] [char](1) NULL,
	[METADATA_CODE] [varchar](30) NULL,
	[TEMPLATE_ID] [varchar](20) NULL,
PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Default [DF__NURSE_WHI__COLUM__7C9A5A9E]    Script Date: 03/24/2016 15:32:51 ******/
ALTER TABLE [dbo].[NURSE_WHITE_BOARD_METADATA] ADD  DEFAULT ((0)) FOR [COLUMN_TYPE]
GO
/****** Object:  Default [DF__NURSE_WHI__is_co__7D8E7ED7]    Script Date: 03/24/2016 15:32:51 ******/
ALTER TABLE [dbo].[NURSE_WHITE_BOARD_METADATA] ADD  DEFAULT ('0') FOR [is_colspan]
GO
/****** Object:  Default [DF__NURSE_WHI__IS_FI__703483B9]    Script Date: 03/24/2016 15:32:51 ******/
ALTER TABLE [dbo].[NURSE_WHITE_BOARD_RECORD_ITEM] ADD  DEFAULT ((0)) FOR [IS_FINISH]
GO
/****** Object:  Default [DF_WIHTE_BOARD_DATA_IS_STOP]    Script Date: 03/24/2016 15:32:51 ******/
ALTER TABLE [dbo].[WHITE_BOARD_DATA] ADD  CONSTRAINT [DF_WIHTE_BOARD_DATA_IS_STOP]  DEFAULT ((0)) FOR [IS_STOP]
GO
/****** Object:  Default [DF_WHITE_BOARD_ITEM_QUERY_ORDER]    Script Date: 03/24/2016 15:32:51 ******/
ALTER TABLE [dbo].[WHITE_BOARD_ITEM] ADD  CONSTRAINT [DF_WHITE_BOARD_ITEM_QUERY_ORDER]  DEFAULT ((0)) FOR [QUERY_ORDER]
GO
/****** Object:  Default [DF_WHITE_BOARD_ITEM_IS_ADDFREQ]    Script Date: 03/24/2016 15:32:51 ******/
ALTER TABLE [dbo].[WHITE_BOARD_ITEM] ADD  CONSTRAINT [DF_WHITE_BOARD_ITEM_IS_ADDFREQ]  DEFAULT ((0)) FOR [IS_ADDFREQ]
GO
/****** Object:  Default [DF__WHITE_BOA__QUERY__21CBDF4D]    Script Date: 03/24/2016 15:32:51 ******/
ALTER TABLE [dbo].[WHITE_BOARD_ITEM] ADD  DEFAULT ((0)) FOR [QUERY_TYPE]
GO

--小白板数据视图
CREATE VIEW [dbo].[VIEW_NURSE_WHITE_BOARD_RECORD]
AS
/*数据同步数据*/ SELECT w.ITEM_CODE RANDOM_ID, CONVERT(VARCHAR, W.ID) RECORD_ID, W.ITEM_CODE RECORD_CODE, W.ITEM_NAME RECORD_NAME, null RECORD_VALUE, 
                      W.FREQ_NAME PERFORM_SCHEDULE, P.WARD_CODE DEPT_CODE, P.PAT_ID, P.BED_CODE,
                      P.BED_CODE + '床-' + P.NAME PAT_INFO,
                      0 ITEM_ID, NULL ITEM_CODE, NULL ITEM_NAME, NULL ITEM_VALUE, NULL ITEM_DATE, NULL IS_FINISH, NULL IS_EXEC, w.item_date record_date, 1 IS_VALID, 
                      IS_STOP
FROM         WHITE_BOARD_DATA W INNER JOIN
                      PAT_CURE_INFO P ON P.PAT_ID = W.PAT_ID AND p.STATUS = 1
WHERE     P.BED_CODE != '' AND P.BED_CODE IS NOT NULL AND w.is_stop = 0 and w.IS_LONGTERM=1

union all

SELECT w.ITEM_CODE RANDOM_ID, CONVERT(VARCHAR, W.ID) RECORD_ID, W.ITEM_CODE RECORD_CODE, W.ITEM_NAME RECORD_NAME, null RECORD_VALUE, 
                      W.FREQ_NAME PERFORM_SCHEDULE, P.WARD_CODE DEPT_CODE, P.PAT_ID, P.BED_CODE,
                      P.BED_CODE + '床-' + P.NAME PAT_INFO,
                      0 ITEM_ID, NULL ITEM_CODE, NULL ITEM_NAME, NULL ITEM_VALUE, NULL ITEM_DATE, NULL IS_FINISH, NULL IS_EXEC, w.item_date record_date, 1 IS_VALID, 
                      0 IS_STOP
FROM         WHITE_BOARD_DATA W INNER JOIN
                      PAT_CURE_INFO P ON P.PAT_ID = W.PAT_ID AND p.STATUS = 1
WHERE     P.BED_CODE != '' AND P.BED_CODE IS NOT NULL and w.IS_LONGTERM=0
and w.ITEM_DATE >= CONVERT(varchar(10),GETDATE()) and w.ITEM_DATE < CONVERT(varchar(10),GETDATE()+1)
UNION ALL
SELECT     NR.RANDOM_ID, CONVERT(VARCHAR, NR.RECORD_ID) RECORD_ID, NR.RECORD_CODE, NR.RECORD_NAME, NR.RECORD_VALUE, NR.PERFORM_SCHEDULE, 
                      NR.DEPT_CODE, NR.PAT_ID, P.BED_CODE, NR.PAT_INFO, NRI.ITEM_ID, NRI.ITEM_CODE, NRI.ITEM_NAME, NRI.ITEM_VALUE, NRI.ITEM_DATE, NRI.IS_FINISH, 
                      M.IS_EXEC, nr.RECORD_DATE, nr.IS_VALID, '0' IS_STOP
FROM         NURSE_WHITE_BOARD_RECORD NR INNER JOIN
                      PAT_CURE_INFO p ON p.PAT_ID = nr.PAT_ID AND p.status = 1 INNER JOIN
                      NURSE_WHITE_BOARD_METADATA M ON NR.RECORD_CODE = M.CODE LEFT JOIN
                      NURSE_WHITE_BOARD_RECORD_ITEM NRI ON NR.RECORD_ID = NRI.RECORD_ID AND NRI.IS_FINISH = 1 AND NRI.IS_VALID = 1
WHERE     nr.RECORD_CODE NOT IN ('ry', 'cy', 'zr', 'zc') and m.SHOW_TYPE = 'listScroll'
UNION ALL
SELECT     NR.RANDOM_ID, CONVERT(VARCHAR, NR.RECORD_ID) RECORD_ID, NR.RECORD_CODE, NR.RECORD_NAME, NR.RECORD_VALUE,  NR.PERFORM_SCHEDULE, 
                      NR.DEPT_CODE, NR.PAT_ID, P.BED_CODE, NR.PAT_INFO, NRI.ITEM_ID, NRI.ITEM_CODE, NRI.ITEM_NAME, NRI.ITEM_VALUE, NRI.ITEM_DATE, NRI.IS_FINISH, 
                      M.IS_EXEC, nr.RECORD_DATE, nr.IS_VALID, '0' IS_STOP
FROM         NURSE_WHITE_BOARD_RECORD NR LEFT JOIN
                      PAT_CURE_INFO p ON p.PAT_ID = nr.PAT_ID AND p.status = 1 INNER JOIN
                      NURSE_WHITE_BOARD_METADATA M ON NR.RECORD_CODE = M.CODE LEFT JOIN
                      NURSE_WHITE_BOARD_RECORD_ITEM NRI ON NR.RECORD_ID = NRI.RECORD_ID AND NRI.IS_FINISH = 1 AND NRI.IS_VALID = 1
WHERE     nr.RECORD_CODE NOT IN ('ry', 'cy', 'zr', 'zc') and m.SHOW_TYPE != 'listScroll'
UNION ALL
SELECT     NR.RANDOM_ID, CONVERT(VARCHAR, NR.RECORD_ID) RECORD_ID, NR.RECORD_CODE, NR.RECORD_NAME, NR.RECORD_VALUE,  NR.PERFORM_SCHEDULE, 
                      NR.DEPT_CODE, NR.PAT_ID, P.BED_CODE, NR.PAT_INFO, NRI.ITEM_ID, NRI.ITEM_CODE, NRI.ITEM_NAME, NRI.ITEM_VALUE, NRI.ITEM_DATE, NRI.IS_FINISH, 
                      M.IS_EXEC, nr.RECORD_DATE, nr.IS_VALID, '0' IS_STOP
FROM         NURSE_WHITE_BOARD_RECORD NR INNER JOIN
                      PAT_CURE_INFO p ON p.PAT_ID = nr.PAT_ID AND p.STATUS = 1 INNER JOIN
                      NURSE_WHITE_BOARD_METADATA M ON NR.RECORD_CODE = M.CODE LEFT JOIN
                      NURSE_WHITE_BOARD_RECORD_ITEM NRI ON NR.RECORD_ID = NRI.RECORD_ID AND NRI.IS_FINISH = 1 AND NRI.IS_VALID = 1
WHERE     P.BED_CODE != '' AND P.BED_CODE IS NOT NULL AND nr.RECORD_CODE IN ('ry', 'cy', 'zr', 'zc') AND ((CONVERT(VARCHAR(13), GETDATE(), 120) 
                      >= (CONVERT(VARCHAR(10), GETDATE(), 120) + ' 08') AND CONVERT(VARCHAR(13), nr.RECORD_DATE, 120) >= (CONVERT(VARCHAR(10), GETDATE(), 120) + ' 08') AND 
                      CONVERT(VARCHAR(13), nr.RECORD_DATE, 120) < (CONVERT(VARCHAR(10), GETDATE() + 1, 120) + ' 08')) OR
                      (CONVERT(VARCHAR(13), GETDATE(), 120) < (CONVERT(VARCHAR(10), GETDATE(), 120) + ' 08') AND CONVERT(VARCHAR(13), nr.RECORD_DATE, 120) 
                      < (CONVERT(VARCHAR(10), GETDATE(), 120) + ' 08') AND CONVERT(VARCHAR(13), nr.RECORD_DATE, 120) >= (CONVERT(VARCHAR(10), GETDATE() - 1, 120) + ' 08')))
UNION ALL
/*转出*/ SELECT 'zc' RANDOM_ID, R.ID RECORD_ID, 'zc' RECORD_CODE, '转出' RECORD_NAME, stuff((select '，'+BED_CODE from [PAT_TRANSFER] p where p.ward_code = R.ward_code
AND P.WARD_CODE != P.NEW_WARD_CODE AND ((CONVERT(VARCHAR(13), GETDATE(), 120) >= (CONVERT(VARCHAR(10), GETDATE(), 120) + ' 08') AND 
                      CONVERT(VARCHAR(13), p.EXECUTE_DATE, 120) >= (CONVERT(VARCHAR(10), GETDATE(), 120) + ' 08') AND CONVERT(VARCHAR(13), p.EXECUTE_DATE, 120) 
                      < (CONVERT(VARCHAR(10), GETDATE() + 1, 120) + ' 08')) OR
                      (CONVERT(VARCHAR(13), GETDATE(), 120) < (CONVERT(VARCHAR(10), GETDATE(), 120) + ' 08') AND CONVERT(VARCHAR(13), p.EXECUTE_DATE, 120) 
                      < (CONVERT(VARCHAR(10), GETDATE(), 120) + ' 08') AND CONVERT(VARCHAR(13), p.EXECUTE_DATE, 120) >= (CONVERT(VARCHAR(10), GETDATE() - 1, 120) + ' 08')))

 for xml path('')),1,1,'') RECORD_VALUE, NULL PERFORM_SCHEDULE, 
                      R.WARD_CODE DEPT_CODE, null PAT_ID,null BED_CODE, null PAT_INFO, 0 ITEM_ID, NULL ITEM_CODE, NULL ITEM_NAME, NULL 
                      ITEM_VALUE, NULL ITEM_DATE, NULL IS_FINISH, NULL IS_EXEC, R.EXECUTE_DATE record_date, 1 IS_VALID, '0' IS_STOP
FROM         DBO.PAT_TRANSFER R
WHERE     R.WARD_CODE = R.NEW_WARD_CODE AND ((CONVERT(VARCHAR(13), GETDATE(), 120) >= (CONVERT(VARCHAR(10), GETDATE(), 120) + ' 08') AND 
                      CONVERT(VARCHAR(13), R.EXECUTE_DATE, 120) >= (CONVERT(VARCHAR(10), GETDATE(), 120) + ' 08') AND CONVERT(VARCHAR(13), R.EXECUTE_DATE, 120) 
                      < (CONVERT(VARCHAR(10), GETDATE() + 1, 120) + ' 08')) OR
                      (CONVERT(VARCHAR(13), GETDATE(), 120) < (CONVERT(VARCHAR(10), GETDATE(), 120) + ' 08') AND CONVERT(VARCHAR(13), R.EXECUTE_DATE, 120) 
                      < (CONVERT(VARCHAR(10), GETDATE(), 120) + ' 08') AND CONVERT(VARCHAR(13), R.EXECUTE_DATE, 120) >= (CONVERT(VARCHAR(10), GETDATE() - 1, 120) + ' 08')))
UNION ALL
/*转入*/ SELECT 'zr' RANDOM_ID, P.ID RECORD_ID, 'zr' RECORD_CODE, '转入' RECORD_NAME, NULL RECORD_VALUE, NULL PERFORM_SCHEDULE, 
                      R.NEW_WARD_CODE DEPT_CODE, P.PAT_ID, R.NEW_BED_CODE BED_CODE, R.NEW_BED_CODE + '床-' + P.NAME PAT_INFO, 0 ITEM_ID, NULL ITEM_CODE, NULL 
                      ITEM_NAME, NULL ITEM_VALUE, NULL ITEM_DATE, NULL IS_FINISH, NULL IS_EXEC, p.SYNC_UPDATE record_date, 1 IS_VALID, '0' IS_STOP
FROM         DBO.PAT_CURE_INFO P INNER JOIN
                      DBO.PAT_TRANSFER R ON P.PAT_ID = R.PAT_ID AND P.STATUS = 1 AND P.WARD_CODE = R.NEW_WARD_CODE
WHERE    R.WARD_CODE != R.NEW_WARD_CODE AND  P.BED_CODE != '' AND P.BED_CODE IS NOT NULL AND ((CONVERT(VARCHAR(13), GETDATE(), 120) >= (CONVERT(VARCHAR(10), GETDATE(), 120) + ' 08') AND 
                      CONVERT(VARCHAR(13), R.EXECUTE_DATE, 120) >= (CONVERT(VARCHAR(10), GETDATE(), 120) + ' 08') AND CONVERT(VARCHAR(13), R.EXECUTE_DATE, 120) 
                      < (CONVERT(VARCHAR(10), GETDATE() + 1, 120) + ' 08')) OR
                      (CONVERT(VARCHAR(13), GETDATE(), 120) < (CONVERT(VARCHAR(10), GETDATE(), 120) + ' 08') AND CONVERT(VARCHAR(13), R.EXECUTE_DATE, 120) 
                      < (CONVERT(VARCHAR(10), GETDATE(), 120) + ' 08') AND CONVERT(VARCHAR(13), R.EXECUTE_DATE, 120) >= (CONVERT(VARCHAR(10), GETDATE() - 1, 120) + ' 08')))
UNION ALL
/*新入院*/ SELECT 'ry' RANDOM_ID, P.ID RECORD_ID, 'ry' RECORD_CODE, '新入院' RECORD_NAME, NULL RECORD_VALUE, NULL PERFORM_SCHEDULE, 
                      P.WARD_CODE DEPT_CODE, P.PAT_ID, P.BED_CODE, P.BED_CODE + '床-' + P.NAME PAT_INFO, 0 ITEM_ID, NULL ITEM_CODE, NULL ITEM_NAME, NULL 
                      ITEM_VALUE, NULL ITEM_DATE, NULL IS_FINISH, NULL IS_EXEC, p.SYNC_UPDATE record_date, 1 IS_VALID, '0' IS_STOP
FROM         DBO.PAT_CURE_INFO P
WHERE     p.STATUS = 1 AND P.BED_CODE != '' AND P.BED_CODE IS NOT NULL AND ((CONVERT(VARCHAR(13), GETDATE(), 120) >= (CONVERT(VARCHAR(10), GETDATE(), 120) 
                      + ' 08') AND CONVERT(VARCHAR(13), p.IN_DATE, 120) >= (CONVERT(VARCHAR(10), GETDATE(), 120) + ' 08') AND CONVERT(VARCHAR(13), p.IN_DATE, 120) 
                      < (CONVERT(VARCHAR(10), GETDATE() + 1, 120) + ' 08')) OR
                      (CONVERT(VARCHAR(13), GETDATE(), 120) < (CONVERT(VARCHAR(10), GETDATE(), 120) + ' 08') AND CONVERT(VARCHAR(13), p.IN_DATE, 120) 
                      < (CONVERT(VARCHAR(10), GETDATE(), 120) + ' 08') AND CONVERT(VARCHAR(13), p.IN_DATE, 120) >= (CONVERT(VARCHAR(10), GETDATE() - 1, 120) + ' 08')))
UNION ALL
/*出院*/ SELECT 'cy' RANDOM_ID, P.ID RECORD_ID, 'cy' RECORD_CODE, '出院' RECORD_NAME, NULL RECORD_VALUE, NULL PERFORM_SCHEDULE, 
                      P.WARD_CODE DEPT_CODE, P.PAT_ID, P.BED_CODE, P.BED_CODE + '床-' + P.NAME PAT_INFO, 0 ITEM_ID, NULL ITEM_CODE, NULL ITEM_NAME, NULL 
                      ITEM_VALUE, NULL ITEM_DATE, NULL IS_FINISH, NULL IS_EXEC, p.SYNC_UPDATE record_date, 1 IS_VALID, '0' IS_STOP
FROM         DBO.PAT_CURE_INFO P
WHERE     P.OUT_DATE IS NOT NULL AND ((CONVERT(VARCHAR(13), GETDATE(), 120) >= (CONVERT(VARCHAR(10), GETDATE(), 120) + ' 08') AND CONVERT(VARCHAR(13), 
                      P.OUT_DATE, 120) >= (CONVERT(VARCHAR(10), GETDATE(), 120) + ' 08') AND CONVERT(VARCHAR(13), P.OUT_DATE, 120) < (CONVERT(VARCHAR(10), GETDATE() + 1, 
                      120) + ' 08')) OR
                      (CONVERT(VARCHAR(13), GETDATE(), 120) < (CONVERT(VARCHAR(10), GETDATE(), 120) + ' 08') AND CONVERT(VARCHAR(13), P.OUT_DATE, 120) 
                      < (CONVERT(VARCHAR(10), GETDATE(), 120) + ' 08') AND CONVERT(VARCHAR(13), P.OUT_DATE, 120) >= (CONVERT(VARCHAR(10), GETDATE() - 1, 120) + ' 08')))
UNION ALL
/*病人总数*/ SELECT 'pCount' RANDOM_ID, P.WARD_CODE RECORD_ID, 'pCount' RECORD_CODE, '病人总数' RECORD_NAME, CONVERT(varchar, COUNT(p.ID)) 
                      RECORD_VALUE, NULL PERFORM_SCHEDULE, P.WARD_CODE DEPT_CODE, NULL BED_CODE, NULL PAT_ID, NULL PAT_INFO, 0 ITEM_ID, NULL ITEM_CODE, NULL 
                      ITEM_NAME, NULL ITEM_VALUE, NULL ITEM_DATE, NULL IS_FINISH, NULL IS_EXEC, GETDATE() record_date, 1 IS_VALID, '0' IS_STOP
FROM         DBO.PAT_CURE_INFO P
WHERE     p.STATUS = 1 AND p.WARD_CODE IS NOT NULL
GROUP BY p.WARD_CODE
GO



--医嘱执行单打印表
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

