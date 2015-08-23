TEIID_PATH=..\..\lib\*;..\..\optional\cassandra\*;..\..\optional\netty-3.6.10.Final.jar;..\..\optional\*

javac -cp %TEIID_PATH% src\org\teiid\example\*.java 

java -cp .\src;%TEIID_PATH% org.teiid.example.TeiidEmbeddedCassandraDataSource %*

