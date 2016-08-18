#!/bin/sh

DIRNAME=`pwd`

RUN_CONF="$DIRNAME/setup.conf"
if [ -r "$RUN_CONF" ]; then
    . "$RUN_CONF"
fi

CP=""

CP="${DIRNAME}/lib/h2*.jar"

echo ${CP}

# check for running server
java -cp ${CP} org.h2.tools.Server -tcpShutdown tcp://localhost:$H2PORT -tcpShutdownForce

cd $DIRNAME

echo ""
echo "**********************"
echo "H2 Server Accounts database shutdown"
echo "**********************"




