USE [master]
GO
IF  EXISTS (SELECT * FROM sys.server_principals WHERE name = N'uih')
ALTER LOGIN [uih] WITH PASSWORD=N'Uih888'
GO
IF NOT EXISTS (SELECT * FROM sys.server_principals WHERE name = N'uih')
CREATE LOGIN [uih] WITH PASSWORD=N'Uih888', DEFAULT_DATABASE=[master], CHECK_EXPIRATION=OFF, CHECK_POLICY=OFF
GO
EXEC master..sp_addsrvrolemember @loginame = N'uih', @rolename = N'sysadmin'
GO
