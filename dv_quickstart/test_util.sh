#!/bin/sh

#-DTRACE=TRUE

DIRNAME=`pwd`

JBOSS_HOME=$DIRNAME/dv_server


JAVA_OPTS="-agentlib:jdwp=transport=dt_socket,address=8787,server=y,suspend=y"

#java  -jar ./target/dv_quickstart-2.1.0.jar '1' $DIRNAME/eap-installer.xml $JBOSS_HOME

#java -jar ./target/dv_quickstart-2.1.0.jar '1' $DIRNAME/dv-installer.xml $JBOSS_HOME

java -jar ./target/dv_quickstart-2.1.0.jar '2' localhost 8888




