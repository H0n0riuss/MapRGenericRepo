package io.github.honoriuss.mapr.fileSystem;

import oadd.org.apache.hadoop.conf.Configuration;
import oadd.org.apache.hadoop.fs.FileSystem;
import oadd.org.apache.hadoop.fs.LocatedFileStatus;
import oadd.org.apache.hadoop.fs.Path;
import oadd.org.apache.hadoop.fs.RemoteIterator;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MapRFSAdapter {
    public InputStream readFile(String filePath) throws IOException {
        return getFs().open(new Path(filePath));
    }

    public List<String> listFiles(String filePath, boolean recursiv) throws IOException {
        var files = new ArrayList<String>();
        RemoteIterator<LocatedFileStatus> iterator = getFs().listFiles(new Path(filePath), recursiv);
        while (iterator.hasNext()) {
            var file = iterator.next();
            if (file.isFile()) {
                files.add(file.getPath().getName());
            }
        }
        return files;
    }

    public FileSystem getFs() throws IOException {
        Configuration config = new Configuration();
        return FileSystem.get(config);
    }
}
