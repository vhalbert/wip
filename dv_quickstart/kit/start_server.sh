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

if [ ! -f "$JBOSS_HOME/bin/standalone.sh" ]; then
	echo ""
	echo "Stop: first run setup_dv.sh to install DV server"
    exit 1
fi

echo "Starting H2 database..."

./start_h2_server.sh &

echo "Starting DV Server ..."

cd $JBOSS_HOME/bin

./standalone.sh  &

cd $DIRNAME

echo ""
echo "********************************************
echo "DV Server is ready"
echo ""
echo "Installed at $JBOSS_HOME"
echo ""
echo "Connect to URL: jdbc:teiid:portfolio@mm://$HOST:31000"
echo "using Teiid User: Username/Password: teiidUser / $ADMIN_PWD"

echo "********************************************
        
echo ""
echo "**********************"
echo "H2 Server Accounts database is available at:  jdbc:h2:tcp://localhost:$H2PORT/accounts"
echo "**********************"

        





