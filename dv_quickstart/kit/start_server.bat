@ECHO OFF

@if not "%ECHO%" == ""  echo %ECHO%
@if "%OS%" == "Windows_NT" setlocal

if "%OS%" == "Windows_NT" (
  set "DIRNAME=%~dp0%"
) else (
  set DIRNAME=.
)

rem Read an optional configuration file.
if "x%STANDALONE_CONF%" == "x" (
   set "STANDALONE_CONF=%DIRNAME%setup_conf.bat"
)
if exist "%STANDALONE_CONF%" (
   echo Calling "%STANDALONE_CONF%"
   call "%STANDALONE_CONF%" %*
) else (
   echo Config file not found "%STANDALONE_CONF%"
)

set "RESOLVED_JBOSS_HOME=%DIRNAME%"
popd

if "x%JBOSS_HOME%" == "x" (
  set "JBOSS_HOME=%RESOLVED_JBOSS_HOME%%SERVER_DIR%"
)

if !exist "%JBOSS_HOME%\bin\standalone.bat" (
   echo "Stop, first run setup_dv.bat to install DV Server"
   goto END
)


call java -jar %DIRNAME%\lib\dv_quickstart-2.1.0.jar 2 %HOST% %PORT% true
if errorlevel == 0 (
        echo *** Server is already running at %HOST%:%PORT% ***
        goto END
)


cd %JBOSS_HOME%\bin

call standalone.bat %*

cd %DIRNAME%

echo **********************
echo DV Server is started
echo **********************

:END
