USE [MNIS_QM];
GO
SET ANSI_NULLS ON;
GO
SET QUOTED_IDENTIFIER ON;
GO
ALTER TABLE [dbo].[SAT_RESULT] ADD [RESULT_NAME] varchar(50) COLLATE Chinese_PRC_CI_AS NOT NULL
GO
EXEC [sys].[sp_addextendedproperty] @name = N'MS_Description', @value = N'±íµ¥Ãû³Æ', @level0type = N'SCHEMA', @level0name = N'dbo', @level1type = N'TABLE', @level1name = N'SAT_RESULT', @level2type = N'COLUMN', @level2name = N'RESULT_NAME';
