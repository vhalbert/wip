@echo off
rem -------------------------------------------------------------------------
rem JDV Quickstart Installation Script
rem -------------------------------------------------------------------------

@if not "%ECHO%" == ""  echo %ECHO%
@if "%OS%" == "Windows_NT" setlocal

if "%OS%" == "Windows_NT" (
  set "DIRNAME=%~dp0%"
) else (
  set DIRNAME=.\
)


#-DTRACE=TRUE

DIRNAME=`pwd`

RUN_CONF="$DIRNAME/setup.conf"
if [ -r "$RUN_CONF" ]; then
    . "$RUN_CONF"
fi

if [ "x$JBOSS_HOME" = "x" ]; then
        JBOSS_HOME=$DIRNAME/dv_server
fi

if [ -d "$JBOSS_HOME" ]; then
	rm -rf $JBOSS_HOME
fi

mkdir $JBOSS_HOME

# substitute the correct installation path
java -jar ./lib/dv_quickstart-2.1.0.jar 1 $DIRNAME/eap-installer.xml $JBOSS_HOME

if [ $? != 0 ] ; then
	echo ""
	echo "Stop due to error"
    exit 1
fi

#  BZ : https://bugzilla.redhat.com/show_bug.cgi?id=1225450
#  issue with using INSTALL_PATH (-DINSTALL_PATH=..)

# install EAP
java  -jar jboss-eap-6.4.0-installer.jar eap-installer.xml 


#  start server install the eap 6.4.3 patch
cd $JBOSS_HOME/bin

./standalone.sh >>console.log &

cd $DIRNAME

echo "Started DV Server"

java -jar ./lib/dv_quickstart-2.1.0.jar 2 localhost 9990

echo "Ping Status: " $?

if [ $? != 0 ] ; then
	echo ""
	echo "Stop due to error"
    exit 1
fi


PATCH_DIR=$DIRNAME

echo "Installing EAP 6.4.3 patch ..."

# install patch
$JBOSS_HOME/bin/jboss-cli.sh --command="patch apply $PATCH_DIR/jboss-eap-6.4.3-patch.zip"

echo "Installed EAP 6.4.3 Patch"

echo "Shutting down server ..."

./shutdown_server.sh

#  pause for 20 seconds until the server is fully available
#  otherwise, the admin config options are not enabled
java -jar ./lib/dv_quickstart-2.1.0.jar 3 20

echo "Server shut down"

echo "Installing DV ..."

# substitute the correct installation path
java -jar ./lib/dv_quickstart-2.1.0.jar 1 $DIRNAME/dv-installer.xml $JBOSS_HOME

if [ $? != 0 ] ; then
	echo ""
	echo "Stop due to error"
    exit 1
fi

# install DV
java -jar jboss-dv-installer-6.2.0.redhat-2.jar dv-installer.xml 

echo "DV server has been installed, starting server"

cd $JBOSS_HOME/bin

./standalone.sh >>console.log &

cd $DIRNAME

echo "Started DV Server"

java -jar ./lib/dv_quickstart-2.1.0.jar 2 localhost 9990

echo "Ping Status: " $?

if [ $? != 0 ] ; then
	echo ""
	echo "Stop due to error"
    exit 1
fi

#  pause for 20 seconds until the server is fully available
#  otherwise, the admin config options are not enabled
java -jar ./lib/dv_quickstart-2.1.0.jar 3 30

echo "Configure quickstart data sources..."

cp -r ./deployment/teiidfiles  $JBOSS_HOME

cd $JBOSS_HOME/

./bin/jboss-cli.sh  --connect --file=$DIRNAME/deployment/scripts/setup.cli

cd $DIRNAME

cp $JBOSS_HOME/teiidfiles/vdb/*.*  $JBOSS_HOME/standalone/deployments

echo "Completed configuring quickstart data sources"

echo ""
echo "********************************************
echo "DV Server is ready to run the DV Quickstart"
echo "********************************************





