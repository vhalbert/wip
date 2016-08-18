@ECHO OFF

@if not "%ECHO%" == ""  echo %ECHO%
@if "%OS%" == "Windows_NT" setlocal

if "%OS%" == "Windows_NT" (
  set "SETUPDIR=%~dp0%"
) else (
  set SETUPDIR=.
)

rem Read an optional configuration file.
if "x%SETUP_CONF%" == "x" (
   set "SETUP_CONF=%SETUPDIR%setup_conf.bat"
)
if exist "%SETUP_CONF%" (
   echo Calling "%SETUP_CONF%"
   call "%SETUP_CONF%" %*
) else (
   echo Config file not found "%SETUP_CONF%"
   goto END
)

set "CP=%SETUPDIR%\lib\*"

java -cp %CP% org.h2.tools.Server -tcpShutdown tcp://localhost:%H2PORT% -tcpShutdownForce


echo **********************
echo H2 database is shutdown
echo **********************

:END