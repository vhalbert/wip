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

PINGFILE="hostport_found.txt"

if [ -f "$PINGFILE" ]; then
        rm $PINGFILE
fi

# check for running server
java -jar ./lib/dv_quickstart-2.1.0.jar 2 $HOST $PORT true


if [ -f "$PINGFILE" ]; then
        echo ""
        echo "Stop, server at $HOST:$PORT is already running"
        exit 1
fi

        cd $JBOSS_HOME/bin

        ./standalone.sh &

        cd $DIRNAME

        echo ""
        echo "**********************"
        echo "DV Server is started"
        echo "**********************"





