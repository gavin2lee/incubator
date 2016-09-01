-- create database
CREATE DATABASE mnis;

-- force drop database
USE master;
ALTER DATABASE mnis SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
DROP DATABASE mnis;

-- query all user tables
select * from sys.tables where is_ms_shipped = 'False';

-- query all user objects
SELECT * FROM sys.objects WHERE is_ms_shipped = 'False';

-- 检查约束
ALTER TABLE <yourtable> CHECK CONSTRAINT ALL
-- 不检查约束
ALTER TABLE <yourtable> NOCHECK CONSTRAINT ALL

-- 允放触发器
ALTER TABLE <yourtable> ENABLE TRIGGER ALL
-- 禁止触发器
ALTER TABLE <yourtable> DISABLE TRIGGER ALL

-- 生成table dml脚本
SELECT 'ALTER TABLE ' + name + ' NOCHECK CONSTRAINT ALL;' FROM sys.tables WHERE is_ms_shipped = 'False';

SELECT 'DROP TABLE ' + name + ';' FROM sys.tables WHERE is_ms_shipped = 'False';

