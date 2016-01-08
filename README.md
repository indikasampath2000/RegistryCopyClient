Guideline to run registry copy client
=====================================
Supported java version 1.7

Registry copy client written to overcome issue of migrating registry data between two different carbon kernel version
servers. i.e. esb 4.0.0 and esb 4.8.1. This client used WSRegistryServiceClient stub and it's not by default available
in carbon server except governance registry. Therefore we have download following set of jar files relevant to it's
released version and copy those to dropins folder.

For an example let's say we want to migrate data from esb 4.0.0 to esb 4.8.1. Esb 4.0.0 based on carbon kernel 3.2.0
and esb 4.8.1 based on carbon kernel 4.2.0. Please refer to WSO2 Carbon Platform Releases matrix [1]. So we have to
download below three jar files of each kernel version and copy to dropins folder.

org.wso2.carbon.registry.ws.api_x.x.x.jar
org.wso2.carbon.registry.ws.client_x.x.x.jar
org.wso2.carbon.registry.ws.stub_x.x.x.jar

Let's take above example again.

For esb 4.0.0 we need to have below jars:
org.wso2.carbon.registry.ws.api_3.2.0.jar
org.wso2.carbon.registry.ws.client_3.2.0.jar
org.wso2.carbon.registry.ws.stub_3.2.0.jar

For esb 4.8.1 we need to have below jars:
org.wso2.carbon.registry.ws.api_4.2.0.jar
org.wso2.carbon.registry.ws.client_4.2.0.jar
org.wso2.carbon.registry.ws.stub_4.2.0.jar

Once you download above jars, copy those to relevant {esb_home}/repository/components/dropins folder and restart the
server. After this is completed, you can execute registry copy client in command line passing below set of arguments.

ExportServerURL - This should be the server URL which you want to read registry resource
ExportServerUsername - Username of privilege user who has necessary permission to execute registry operation i.e admin
ExportServerPassword - Password of user
ImportServerURL - This should be the server URL which you want to copy registry resource
ImportServerUsername - Username of privilege user who has necessary permission to execute registry operation i.e admin
ImportServerPassword - Password of user
RegistryPathToRead - Registry path to start reading and copy to same location in destination server

Once you build the project, executable jar is registry-copy-client-1.0-SNAPSHOT.jar. You can run it with below command. 
Please execute jar keep in the same distribution folder without copying to any other location because there are some 
dependencies and resources it required at runtime.

java -jar registry-copy-client-1.0-SNAPSHOT.jar 'https://localhost:9443/services/' 'admin' 'admin' 'https://localhost:9444/services/' 'admin' 'admin' '/_system/governance'

[1] http://wso2.com/products/carbon/release-matrix/
