package com.wso2.support;

import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.wso2.carbon.registry.core.Collection;
import org.wso2.carbon.registry.core.Registry;
import org.wso2.carbon.registry.core.Resource;
import org.wso2.carbon.registry.ws.client.registry.WSRegistryServiceClient;

/**
 * Registry data cannot export and import by checkin-client.sh between two different carbon kernel version servers.
 * For an example esb 4.0.0 registry data cannot export and import into esb 4.9.0 due to dump format incompatibility.
 * This class written to overcome that issue. Basically  what this class does is get registry data from given instance
 * and put it to other instance. User has to provide server url of reading instance, server url of writing instance and
 * path to start reading. i.e. /_system/governance
 */
public class RegistryCopyClient {

    private static ConfigurationContext configContext = null;
    private static String axis2Repo = "src/main/resources/client";
    private static String axis2Conf = "src/main/resources/conf/axis2_client.xml";

    /**
     * Initialize registry service client object of given server instance
     *
     * @param serverURL server instance url
     * @return WSRegistryServiceClient object
     * @throws Exception
     */
    private static WSRegistryServiceClient initialize(String serverURL, String username,
                                                      String password) throws Exception {
        System.setProperty("javax.net.ssl.trustStore", "src/main/resources/security/wso2carbon.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "wso2carbon");
        System.setProperty("javax.net.ssl.trustStoreType", "JKS");
        configContext = ConfigurationContextFactory.createConfigurationContextFromFileSystem(
                axis2Repo, axis2Conf);
        return new WSRegistryServiceClient(serverURL, username, password, configContext);
    }

    /**
     * Traverse export server registry tree and copy data to import server registry tree
     *
     * @param exportServerURL export server instance url
     * @param exportServerUsername export server username
     * @param exportServerPassword export server password
     * @param importServerURL import server instance url
     * @param importServerUsername import server instance username
     * @param importServerPassword import server instance password
     * @param path path to start
     * @throws Exception
     */
    public void traverseRegistryTree(String exportServerURL, String exportServerUsername, String exportServerPassword,
                                      String importServerURL, String importServerUsername, String importServerPassword,
                                      String path) throws Exception {
        Registry exportRegistry = initialize(exportServerURL, exportServerUsername, exportServerPassword);
        Registry importRegistry = initialize(importServerURL, importServerUsername, importServerPassword);
        try {
            getAndPutRegistryData(exportServerURL, exportServerUsername, exportServerPassword, importServerURL,
                    importServerUsername, importServerPassword, path, exportRegistry, importRegistry);
        } finally {
            //Close the session
            ((WSRegistryServiceClient) exportRegistry).logut();
            ((WSRegistryServiceClient) importRegistry).logut();
        }
    }

    /**
     *  Get data from export registry and put data to import registry while recursively traversing registry tree
     *
     * @param exportServerURL export server instance url
     * @param exportServerUsername export server username
     * @param exportServerPassword export server password
     * @param importServerURL import server instance url
     * @param importServerUsername import server instance username
     * @param importServerPassword import server instance password
     * @param path path to start
     * @param exportRegistry export registry
     * @param importRegistry import registry
     * @throws Exception
     */
    private void getAndPutRegistryData(String exportServerURL, String exportServerUsername, String exportServerPassword,
                                       String importServerURL, String importServerUsername, String importServerPassword,
                                       String path, Registry exportRegistry, Registry importRegistry) throws Exception {
        //get registry resource from export server
        Resource resource = exportRegistry.get(path);
        //put registry resource to import server
        importRegistry.put(resource.getPath(), resource);
        System.out.println("Copied " + resource.getPath());
        if (resource instanceof Collection) {
            String[] children = ((Collection) resource).getChildren();
            for (String child : children) {
                getAndPutRegistryData(exportServerURL, exportServerUsername, exportServerPassword, importServerURL,
                        importServerUsername, importServerPassword, child, exportRegistry, importRegistry);
            }
        }
    }
}
