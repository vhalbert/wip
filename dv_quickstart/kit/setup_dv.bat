@ECHO OFF

REM wipe screen.
cls

rem -------------------------------------------------------------------------
rem JDV Quickstart Installation Script
rem -------------------------------------------------------------------------

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

 echo JBOSS_HOME %JBOSS_HOME%
 
if not exist "%DV_JAR%" (
       echo Need to download DV %DV_JAR% package from the Customer Support Portal
       echo and place it in the %SETUPDIR% directory to proceed...
  goto END
 )  

if exist "%JBOSS_HOME%" (
    echo Removing JBOSS_HOME '%JBOSS_HOME%' 

    rmdir %JBOSS_HOME% /s /q
)

echo JBOSS_HOME '%JBOSS_HOME%' 
mkdir %JBOSS_HOME%

cd %SETUPDIR%

echo
echo Installing DV Server kit %DV_JAR%...

call java -jar %SETUPDIR%\lib\dv_quickstart-2.1.0.jar 1 auto_install.xml.variables admin.pwd %ADMIN_PWD%
call java -jar %SETUPDIR%\lib\dv_quickstart-2.1.0.jar 1 auto_install.xml install.path %JBOSS_HOME%

rem run headless installation
java -jar %DV_JAR% %DV_AUTOFILE%

echo Installing DV,wait for 210 seconds
echo.

timeout 210 /nobreak

echo Installed DV kit

xcopy /Y /Q /S "%SETUPDIR%\deployment\teiidfiles\*.*" "%JBOSS_HOME%\teiidfiles\"

call java -jar %SETUPDIR%\lib\dv_quickstart-2.1.0.jar 1 %JBOSS_HOME%\teiidfiles\standalone.xml teiid_files_loc %JBOSS_HOME%\teiidfiles\ true

copy "%SETUPDIR%\lib\h2-1.3.168.redhat-4.jar" "%JBOSS_HOME%\modules\system\layers\base\com\h2database\h2\main\h2-1.3.168.redhat-4.jar"
copy "%JBOSS_HOME%\teiidfiles\standalone.xml" "%JBOSS_HOME%\standalone\configuration\standalone.xml"

xcopy /Y /Q /S "%JBOSS_HOME%\teiidfiles\vdb\*.*" "%JBOSS_HOME%\standalone\deployments"
xcopy /Y /Q /S "%JBOSS_HOME%\teiidfiles\war\*.*" "%JBOSS_HOME%\standalone\deployments"

echo Starting DV Server
echo.

call "%SETUPDIR%\start_server.bat"

echo Starting DV and wait for 40 seconds
echo.

timeout 40 /nobreak


echo.
echo ********************************************
echo DV Server is ready to run the DV Quickstart
echo 
echo Installed at %JBOSS_HOME%
echo
echo Connect to URL: jdbc:teiid:portfolio@mm://%HOST%:31000
echo using Teiid User: Username/Password: teiidUser / %ADMIN_PWD%
echo ********************************************

:END
