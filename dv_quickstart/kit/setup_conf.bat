rem ### -*- batch file -*- ######################################################
rem #                                                                          ##
rem #  JBoss DV Quickstart Bootstrap Script Configuration                                    ##
rem #                                                                          ##
rem #############################################################################

rem #
rem # This batch file is executed by setup_dv.bat to initialize the environment
rem # variables that setup_dv.bat uses. It is recommended to use this file to
rem # configure these variables, rather than modifying setup_dv.bat itself.
rem #

rem # Set JBOSS_HOME

rem set "JBOSS_HOME=  "

rem # set the EAP kit to install
rem #
set "EAP_JAR=jboss-eap-6.4.0-installer.jar"

rem # set the auto installer file to use
rem #
set "EAP_AUTOFILE=eap-server.xml"

rem # set the EAP patch kit to install
rem #
rem set "EAP_PATCH_ZIP=jboss-eap-6.4.3-patch.zip"


rem # set the JDV kit to install
rem #
set "DV_JAR=jboss-dv-installer-6.2.0.redhat-2.jar"

rem # set the auto installer file to use
rem #
set "DV_AUTOFILE=dv-installer.xml"


rem # set the EAP server directory name to install into
rem #
set "SERVER_DIR=eap_server"