#!/bin/sh

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

java -jar ./lib/dv_quickstart-2.1.0.jar 1 $DIRNAME/eap-installer.xml $JBOSS_HOME

if [ $? != 0 ] ; then
	echo ""
	echo "Stop due to error"
    exit 1
fi

#  BZ : https://bugzilla.redhat.com/show_bug.cgi?id=1225450
#  issue with using INSTALL_PATH (-DINSTALL_PATH=..)

java  -jar jboss-eap-6.4.0-installer.jar eap-installer.xml 

java -jar ./lib/dv_quickstart-2.1.0.jar 1 $DIRNAME/dv-installer.xml $JBOSS_HOME

if [ $? != 0 ] ; then
	echo ""
	echo "Stop due to error"
    exit 1
fi

java -jar jboss-dv-installer-6.2.0.ER3-redhat-1.jar dv-installer.xml 

echo "DV server has been installed, starting server"

cd $JBOSS_HOME/bin

./standalone.sh &

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





