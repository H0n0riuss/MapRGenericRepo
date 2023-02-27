package io.github.honoriuss.mapr.utils;

import io.github.honoriuss.mapr.fileSystem.MapRFSAdapter;
import oadd.org.apache.hadoop.fs.Path;

import java.io.IOException;

/**
 * @author H0n0riuss
 */
public class MapRUtils {

    /**
     * creates the path with all subdirectories, if they don`t exist
     * @param fullPath should not contain the table name
     * @return true if creation succeeds or already exists
     */
    public boolean createDirectoryPath(String fullPath) throws IOException {
        var fs = MapRFSAdapter.getFs();
        var dirPath = new Path(fullPath);
        if(fs.exists(dirPath))
            return true;
        return fs.mkdirs(dirPath);
    }
}
