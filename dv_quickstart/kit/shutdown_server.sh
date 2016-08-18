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


cd $JBOSS_HOME/bin

./jboss-cli.sh --connect command=:shutdown

cd $DIRNAME

echo "Stopping H2 database..."

./stop_h2_server.sh

echo ""
echo "**********************"
echo "DV Server is shutdown"
echo "**********************"