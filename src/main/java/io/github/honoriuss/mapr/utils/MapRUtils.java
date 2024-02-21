package io.github.honoriuss.mapr.utils;

import com.mapr.db.MapRDB;
import com.mapr.db.exceptions.DBException;
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
     * Creates a mapr Database
     * @param tablePath path to the table
     * @param tableName Database name
     * @return true if everything works out
     * @throws IOException folder cant be created
     * @throws DBException table cant be created
     */
    public static boolean createTable(String tablePath, String tableName) throws IOException, DBException {
        if (tablePath.endsWith("/")) {
            return createTable(tablePath.concat(tableName));
        }
        return createTable(tablePath.concat("/").concat(tableName));
    }

    /**
     * Creates a mapr Database
     * example: "your/path/to/table/tableName"
     * @param tableFullPath with tableName
     * @return true if create directory and table creation succeeds
     */
    public static boolean createTable(String tableFullPath) throws IOException, DBException {
        var lasIndexOf = tableFullPath.lastIndexOf("/");
        var dir = tableFullPath.substring(0, lasIndexOf);
        createDirectoryPath(dir);
        if (!MapRDB.tableExists(tableFullPath)) {
            MapRDB.createTable(tableFullPath).close();
            return true;
        }
        return false;
    }
}
