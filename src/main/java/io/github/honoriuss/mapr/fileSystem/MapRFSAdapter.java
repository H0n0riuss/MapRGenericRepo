package io.github.honoriuss.mapr.fileSystem;

import oadd.org.apache.hadoop.conf.Configuration;
import oadd.org.apache.hadoop.fs.FileSystem;
import oadd.org.apache.hadoop.fs.LocatedFileStatus;
import oadd.org.apache.hadoop.fs.Path;
import oadd.org.apache.hadoop.fs.RemoteIterator;
import oadd.org.apache.hadoop.fs.FileStatus;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public abstract class MapRFSAdapter {
    public static InputStream readFile(String filePath) throws IOException {
        return getFs().open(new Path(filePath));
    }

    public static List<String> listFiles(String filePath, boolean recursive) throws IOException {
        var files = new ArrayList<String>();
        RemoteIterator<LocatedFileStatus> iterator = getFs().listFiles(new Path(filePath), recursive);
        while (iterator.hasNext()) {
            var file = iterator.next();
            if (file.isFile()) {
                files.add(file.getPath().getName());
            }
        }
        return files;
    }

    public static List<FileStatus> getFileStatusList(Path path) throws IOException {
        var fileStatusList = new ArrayList<FileStatus>();
        var fileStatuses = getFs().listStatus(path);

        for (var fileStatus : fileStatuses) {
            if (fileStatus.isDirectory()) {
                fileStatusList.addAll(getFileStatusList(fileStatus.getPath()));
            } else {
                fileStatusList.add(fileStatus);
            }
        }

        return fileStatusList;
    }

    public static List<FileStatus> getFileStatusList(Path path, List<String> ignoredRegexList) throws IOException {
        var fileStatusList = new ArrayList<FileStatus>();
        var fileStatuses = getFs().listStatus(path);

        for (var fileStatus : fileStatuses) {
            var ignore = false;
            for (String ignoredRegex : ignoredRegexList) {
                if (Pattern.matches(ignoredRegex, fileStatus.getPath().toString())) {
                    ignore = true;
                    break;
                }
            }
            if (ignore) {
                continue;
            }

            if (fileStatus.isDirectory()) {
                fileStatusList.addAll(getFileStatusList(fileStatus.getPath(), ignoredRegexList));
            } else {
                fileStatusList.add(fileStatus);
            }
        }

        return fileStatusList;
    }

    public static FileSystem getFs() throws IOException {
        Configuration config = new Configuration();
        return FileSystem.get(config);
    }
}
