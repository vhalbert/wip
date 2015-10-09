@ECHO OFF

REM wipe screen.
cls

rem -------------------------------------------------------------------------
rem JDV Quickstart Installation Script
rem -------------------------------------------------------------------------

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

 echo JBOSS_HOME %JBOSS_HOME%

if not exist "%EAP_JAR%" (
        echo Need to download EAP %EAP_JAR% package from the Customer Support Portal
        echo and place it in the %DIRNAME% directory to proceed...
  goto END
 )
 
if not exist "%DV_JAR%" (
       echo Need to download DV %DV_JAR% package from the Customer Support Portal
       echo and place it in the %DIRNAME% directory to proceed...
  goto END
 )  

if exist "%JBOSS_HOME%" (
    echo Removing JBOSS_HOME '%JBOSS_HOME%' 

    rmdir %JBOSS_HOME% /s /q
)

echo JBOSS_HOME '%JBOSS_HOME%' 
mkdir %JBOSS_HOME%

echo
echo Installing EAP Server kit %EAP_JAR%...

rem # substitute the correct installation path
call java -jar %DIRNAME%\lib\dv_quickstart-2.1.0.jar 1 %DIRNAME%\%EAP_AUTOFILE% %JBOSS_HOME%

if not "%ERRORLEVEL%" == "0" (
  echo.
	echo Error Occurred During setting installation path!
	echo.
	GOTO :EOF
)

rem run headless installation
call java -jar %EAP_JAR% %EAP_AUTOFILE%

if not "%ERRORLEVEL%" == "0" (
  echo.
	echo Error Occurred During JBoss EAP Installation!
	echo.
	GOTO :EOF
)

echo Waiting for EAP to install
echo.

timeout 25 /nobreak

echo Installed EAP Server kit

cd %DIRNAME%

echo
echo Installing DV Server kit %DV_JAR%...


rem run headless installation
call java -DINSTALL_PATH=%JBOSS_HOME% -jar %DV_JAR% %DV_AUTOFILE%

echo Installing DV,wait for 110 seconds
echo.

timeout 110 /nobreak

echo Installed DV kit

echo Starting DV Server
echo.

start "" "%JBOSS_HOME%\bin\standalone.bat"

echo Starting DV and wait for 40 seconds
echo.

timeout 40 /nobreak

echo Configure quickstart data sources...
echo.
xcopy /Y /Q /S "%DIRNAME%\deployment\teiidfiles\*" "%JBOSS_HOME%\teiidfiles\"

call "%JBOSS_HOME%\bin\jboss-cli.bat  --connect --file=%DIRNAME%\deployment\scripts\setup.cli"


xcopy /Y /Q /S "%JBOSS_HOME%\teiidfiles\vdb\*" "%JBOSS_HOME%\standalone\deployments"


echo Completed configuring quickstart data sources

echo.
echo ********************************************
echo DV Server is ready to run the DV Quickstart
echo ********************************************

:END