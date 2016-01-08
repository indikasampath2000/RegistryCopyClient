package com.wso2.support;

/**
 * Executable class of registry copy client
 */
public class Main {

    private static final String exportServerURL = "https://localhost:9443/services/";
    private static final String exportServerUsername = "admin";
    private static final String exportServerPassword = "admin";
    private static final String importServerURL = "https://localhost:9444/services/";
    private static final String importServerUsername = "admin";
    private static final String importServerPassword = "admin";
    private static final String path = "/_system/governance";

    /**
     * Main execution class
     * @param args argument array to do registry copy
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        if (args != null && args.length == 7) {
            String fromServerURL = args[0];
            String fromServerUsername = args[1];
            String fromServerPassword = args[2];
            String toServerURL = args[3];
            String toServerUsername = args[4];
            String toServerPassword = args[5];
            String registryPath = args[6];
            validateCommandLineArguments(fromServerURL, fromServerUsername, fromServerPassword, toServerURL,
                    toServerUsername, toServerPassword, registryPath);
            RegistryCopyClient client = new RegistryCopyClient();
            client.traverseRegistryTree(fromServerURL, fromServerUsername, fromServerPassword, toServerURL,
                    toServerUsername, toServerPassword, registryPath);
            System.out.println("Done!");
            System.exit(0);
        }else {
            throw new RuntimeException("Command line arguments count not matched. Please pass these arguments in respective order: " +
                    "'ExportServerURL' 'ExportServerUsername' 'ExportServerPassword' 'ImportServerURL' 'ImportServerUsername' 'ImportServerPassword' 'RegistryPathToRead'");
        }
    }

    /**
     * Validate argument passed in command line
     *
     * @param fromServerURL export server url
     * @param fromServerUsername export server username
     * @param fromServerPassword export server password
     * @param toServerURL import server url
     * @param toServerUsername import server username
     * @param toServerPassword import server password
     * @param registryPath registry path
     * @throws RuntimeException
     */
    private static void validateCommandLineArguments(String fromServerURL, String fromServerUsername,
                                              String fromServerPassword, String toServerURL, String toServerUsername,
                                              String toServerPassword, String registryPath) throws RuntimeException {
        if (fromServerURL.isEmpty()) {
            throw new RuntimeException("Export server URL cannot be empty.");
        }
        if (fromServerUsername.isEmpty()) {
            throw new RuntimeException("Export server username cannot be empty.");
        }
        if (fromServerPassword.isEmpty()) {
            throw new RuntimeException("Export server password cannot be empty.");
        }
        if (toServerURL.isEmpty()) {
            throw new RuntimeException("Import server URL cannot be empty.");
        }
        if (toServerUsername.isEmpty()) {
            throw new RuntimeException("Import server username cannot be empty.");
        }
        if (toServerPassword.isEmpty()) {
            throw new RuntimeException("Import server password cannot be empty.");
        }
        if (registryPath.isEmpty()) {
            throw new RuntimeException("Registry path cannot be empty.");
        }
    }
}
