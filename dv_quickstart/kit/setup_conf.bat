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

rem # set the JDV kit to install
rem #
set "DV_JAR=jboss-dv-installer-6.3.0.redhat-1.jar"

rem # set the auto installer file to use
rem #
set "DV_AUTOFILE=auto_install.xml"


rem # set the EAP server directory name to install into
rem #
set "SERVER_DIR=dv_server"

rem # set the password to be used by the installer
rem #
set "ADMIN_PWD=redhat1!"

rem # the default host:port to use to ping the server
set "HOST=localhost"
set "PORT=9990"

rem # the default port for H2 server
set "H2PORT=9092"
