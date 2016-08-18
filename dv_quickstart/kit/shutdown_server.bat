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

set "RESOLVED_JBOSS_HOME=%SETUPDIR%"
popd

if "x%JBOSS_HOME%" == "x" (
  set "JBOSS_HOME=%RESOLVED_JBOSS_HOME%%SERVER_DIR%"
)

cd %JBOSS_HOME%\bin

call jboss-cli.bat --connect command=:shutdown

cd %SETUPDIR%

echo Stoping H2 database...

call "%SETUPDIR%\stop_h2_server.bat"

echo **********************
echo DV Server is shutdown
echo **********************

:END