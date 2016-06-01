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

if [ ! -f "$DV_JAR" ]; then
	echo ""
	echo "Stop: The DV $DV_JAR is not found in $DIRNAME"
    exit 1
fi

mkdir $JBOSS_HOME

echo "Installing DV kit $DV_JAR ..."

java -jar ./lib/dv_quickstart-2.1.0.jar 1 auto_install.xml.variables admin.pwd $ADMIN_PWD

# install DV
java -DINSTALL_PATH=$JBOSS_HOME  -jar $DV_JAR auto_install.xml 

echo "DV kit has been installed, starting server"

echo "Starting DV Server ..."

cd $JBOSS_HOME/bin

./standalone.sh >>console.log &

cd $DIRNAME

java -jar ./lib/dv_quickstart-2.1.0.jar 2 $HOST $PORT

echo "Ping Status: " $?

if [ $? != 0 ] ; then
	echo ""
	echo "Stop due to server has not started on $HOST:$PORT"
    exit 1
fi

echo "Started DV Server"

#  pause for 20 seconds until the server is fully available
#  otherwise, the admin config options are not enabled
java -jar ./lib/dv_quickstart-2.1.0.jar 3 30

echo "Configure quickstart data sources..."

cp -r ./deployment/teiidfiles  $JBOSS_HOME

cd $JBOSS_HOME/

./bin/jboss-cli.sh  --connect --file=$DIRNAME/deployment/scripts/setup.cli

cd $DIRNAME

echo "Completed configuring quickstart data sources"

echo ""
echo "********************************************
echo "DV Server is ready to run the DV Quickstart"
echo ""
echo "Installed at $JBOSS_HOME"
echo ""
echo "Connect to URL: jdbc:teiid:portfolio@mm://$HOST:31000"
echo "using Teiid User: Username/Password: teiidUser / $ADMIN_PWD"

echo "********************************************





