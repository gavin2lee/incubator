USE [MNIS_QM]
GO
/****** Object:  Table [dbo].[QUALITY_INDEX]    Script Date: 08/25/2016 13:25:57 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[QUALITY_INDEX](
	[SEQ_ID] [bigint] IDENTITY(1,1) NOT NULL,
	[DEPT_CODE] [varchar](20) NULL,
	[NUMERATOR_ID] [varchar](20) NOT NULL,
	[DENOMINATOR_ID] [varchar](20) NOT NULL,
	[NUMERATOR_VAL] [int] NULL,
	[DENOMINATOR_VAL] [int] NULL,
	[INDEX_VAL] [float] NULL,
	[BIG_TYPE] [varchar](100) NOT NULL,
	[TARGET] [varchar](2) NOT NULL,
	[LIMIT_VAL] [varchar](100) NULL,
	[IS_USE] [char](1) NULL,
	[ORDER_VAL] [int] NULL,
	[STATUS] [varchar](2) NOT NULL,
	[CREATE_TIME] [datetime] NOT NULL,
	[UPDATE_TIME] [datetime] NOT NULL,
	[CREATE_PERSON] [varchar](100) NOT NULL,
	[UPDATE_PERSON] [varchar](100) NOT NULL,
	[INDEX_NAME] [varchar](200) NULL,
 CONSTRAINT [PK_QUALITY_INDEX] PRIMARY KEY CLUSTERED 
(
	[SEQ_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
