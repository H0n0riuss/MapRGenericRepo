package io.github.honoriuss.mapr.utils;

import com.mapr.db.MapRDB;
import io.github.honoriuss.mapr.fileSystem.MapRFSAdapter;
import org.apache.hadoop.fs.Path;

import java.io.IOException;

/**
 * @author H0n0riuss
 */
public abstract class MapRUtils {

    /**
     * creates the path with all subdirectories, if they don`t exist
     *
     * @param fullPath should not contain the table name
     * @return true if creation succeeds or already exists
     */
    public static boolean createDirectoryPath(String fullPath) throws IOException {
        var fs = MapRFSAdapter.getFs();
        var dirPath = new Path(fullPath);
        if (fs.exists(dirPath))
            return true;
        return fs.mkdirs(dirPath);
    }

    /**
     * @param tableFullPath
     * @return
     */
    public static boolean createTable(String tableFullPath) throws IOException {
        var lasIndexOf = tableFullPath.lastIndexOf("/");
        var dir = tableFullPath.substring(0, lasIndexOf);
        createDirectoryPath(dir);
        if(!MapRDB.tableExists(tableFullPath)){ //TODO

        }
        return false;
    }
}
