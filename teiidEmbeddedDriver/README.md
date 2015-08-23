This describes the usage of the Teiid Embedded Driver.

*  [Basic Process] (#basicprocess)
*  [Supporting Translators] (#supporttranslator)
*  [Supporting Connectors] (#supportconnector)
*  [Supporting Txn Mgrs] (#supporttxmgr)
*  [Tuning Server] (#tuningserver)

-------------------
<a id="basicprocess"></a>
Basic Process 
---------------------

The basic process for using the driver is the following:
 
{code}
 
                  TeiidEmbeddedDriver driver = new TeiidEmbeddedDriver();
                  
                  String configFileName = ...(see quickstarts or src/test/resources/BenchmarkConfiguration.xml 
                                as an example)
                  
                  driver.initialize(configFileName)
                  
                  {optional, configure data source(s), see next example}

                  driver.startServer();
 
                  driver.deployVDB(vdbFileName);
 
                  driver.getConnection(teiidJdbcURL, properties);
{code}
 
(This is the more on the connectors) If you create a data source on your own, like the H2 data source in the portfolio example, you would still need to make the following call:
 
{code}
                  
                  DataSource ds = .....(create data source)
                  driver.getEmbeddedServer().addConnectionFactory("jndi-name", ds);
                  
{code}
 
 
 -------------------
<a id="supporttranslator"></a>
Supporting Translators 
---------------------

To add support for a new Translator:

-  edit the src/main/resources/TranslatorFactoryMapping.properties file to add the following mapping:
        translator-type=translator factory class name
        
        Example:  file=org.teiid.translator.file.FileExecutionFactory
        
   To use a specific translator, specify the translator-type as the <type> of a <translator> in the configuration.
   Example:
   
        <translator name="file-translator">
            <connectorName>file-connector</connectorName>
            <type>file</type>
            <properties>
            </properties>
        </translator>     
        

 -------------------
<a id="supportconnector"></a>
Supporting Connectors 
---------------------


To add support for a new Connector:

-  Create a new class that extends TeiidConnectorWrapper.  see existing connectors in org.teiid.embedded.connector package.
-  edit the src/main/resources/ConnectorMapping.txt file to add the following mapping:
        connector-type=new connector class name
        
   To use a specific connector, specify the connector-type as the <type> of a <connector> in the configuration.
   
   
   Example:
   
        <connector name="file-connector">
            <jndiName>java:/marketdata-file</jndiName>
            <type>file</type>
            <properties>
                <property name="ParentDirectory">data</property>
            </properties>       
        </connector> 
        

 -------------------
<a id="supporttxmgr"></a>
Supporting Transaction Mgr 
---------------------

To use a transaction manager in your environment, add the following to your configuration:

    <transactionmgr>
        <className>org.teiid.embedded.transactionmgr.ArjunaTransactionManager</className>
        <properties>
            <property name="NodeIdentifier">1</property>
            <property name="SocketProcessIdPort">0</property>
            <property name="SocketProcessIdMaxPorts">10</property>
            
            <property name="EnableStatistics">false</property>
            <property name="DefaultTimeout">300</property>
            <property name="TransactionStatusManagerEnable">false</property>
            <property name="TxReaperTimeout">120000</property>
        </properties>          
    </transactionmgr>
    
 If you want to add your own transaction manager, create your own class that extends TeiidTransactionMgrWrapper. 
 Then specify that as the <className> in the <transactionmgr> section.
 
 
  -------------------
<a id="tuningserver"></a>
Tuning Server 
---------------------

 Tuning the Embedded Server
 
 If the embedded server needs tuning, add the properties to the following section of your configuration:
 
    <embedded name="teiidEmbeddedInstance">
        <properties>
            <property name="xx">value</property>
        </properties>
    
    </embedded>
    
         