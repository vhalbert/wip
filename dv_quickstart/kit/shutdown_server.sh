#!/bin/sh

#-DTRACE=TRUE

DIRNAME=`pwd`

JBOSS_HOME=$DIRNAME/dv_server

cd $JBOSS_HOME/bin

./jboss-cli.sh --connect command=:shutdown

cd $DIRNAME