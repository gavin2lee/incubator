@echo off
rem -----------------------------------------------------------------------------
rem Start Script for the mnisqm Server
rem -----------------------------------------------------------------------------

if "%OS%" == "Windows_NT" setlocal
call mnisqm.bat --start

:end
