package io.github.honoriuss.mapr.utils;

import io.github.honoriuss.mapr.fileSystem.MapRFSAdapter;

import java.io.IOException;

/**
 * @author H0n0riuss
 */
public class MapRUtils {

    /**
     * creates the path with all subdirectories, if they don`t exist
     * @param fullPath should not contain the table name
     * @return true if creation succeeds
     */
    public boolean createDirectoryPath(String fullPath) throws IOException {
        MapRFSAdapter mapRFSAdapter = new MapRFSAdapter();

        return true;
    }
}
