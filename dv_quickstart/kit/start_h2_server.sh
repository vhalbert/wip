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
java -cp ${CP} org.h2.tools.Server -tcp -tcpPort ${H2PORT}

#  do not add to starting of H2 if you understand the possible risk
# -tcpAllowOthers


cd $DIRNAME



