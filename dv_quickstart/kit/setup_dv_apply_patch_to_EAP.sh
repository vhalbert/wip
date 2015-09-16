#!/bin/sh

#-DTRACE=TRUE

#DIRNAME=`dirname "$0"`

DIRNAME=`pwd`

RUN_CONF="$DIRNAME/setup.conf"
if [ -r "$RUN_CONF" ]; then
    . "$RUN_CONF"
fi

if [ "x$JBOSS_HOME" = "x" ]; then
        JBOSS_HOME=$DIRNAME/dv_server
fi


PATCH_DIR=DIRNAME

$JBOSS_HOME/bin/jboss-cli.sh --command="patch apply $PATCH_DIR/jboss-eap-6.4.3-patch.zip"






