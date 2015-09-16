#!/bin/sh


DIRNAME=`pwd`

echo "Downloading EAP and DV kits"
echo "  "
echo "******************************"
echo " -- WARNING --- The downloads will fail you are not currently logged into www.jboss.org"
echo "******************************"


echo "Downloading EAP kit ..."

#EAPKIT="https://access.cdn.redhat.com/content/origin/files/sha256/36/363ab89d81b1f496da3e745f6cac7f9456d76bd4a9d1908c597f514232f84c0a/jboss-eap-6.4.0-installer.jar?_auth_=1438900181_410e282f1c492c5e788a9d90b85c0bde#!project=eap"


EAPKIT="http://www.jboss.org/download-manager/file/jboss-eap-6.4.0.GA-installer.jar"

wget $EAPKIT $DIRNAME/jboss-eap-6.4.0.GA-installer.jar

#java -jar ./lib/dv_quickstart-2.1.0.jar 4 $EAPKIT $DIRNAME/jboss-eap-6.4.0.GA-installer.jar

echo "Completed downloading EAP kit ..."

echo "Downloading DV kit ..."

#DVKIT="http://dev138.mw.lab.eng.bos.redhat.com/candidate/dv-6.2.0-ER4/jboss-dv-installer-6.2.0.ER4-redhat-1.jar"

DVKIT="http://www.jboss.org/download-manager/file/jboss-dv-installer-6.1.0.redhat-3.jar"
#java -jar ./lib/dv_quickstart-2.1.0.jar 4 $DVKIT $DIRNAME/jboss-dv-installer-6.2.0.ER4-redhat-1.jar

echo "Completed downloading DV kit ..."
