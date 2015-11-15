package com.mars.trackerdump.fs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public abstract class FSReader {

    public String path = null;
    public final static String CSV_DELIM = ";";

    @Nullable
    public abstract <T> T reader(String csvLine);

    public abstract <T> void writeDB(@Nonnull T obj);

    public abstract boolean checkFName(@Nullable String fName);

    public final void scanPath() throws IOException {
        if (StringUtils.isEmpty(path)) {
            return;
        }
        scanPathInner(path);
    }

    private final void scanPathInner(@Nonnull String path) throws IOException {
        File f = new File(path);
        File[] files = f.listFiles();
        if (ArrayUtils.isEmpty(files)) {
            return;
        }
        for (File curFile : files) {
            if (curFile.isDirectory()) {
                scanPathInner(curFile.getPath());
            } else if (curFile.isFile()) {
                read(curFile.getPath());
            }
        }
    }

    private final void read(@Nullable String... fileNames) throws IOException {
        if (ArrayUtils.isEmpty(fileNames)) {
            return;
        }

        for (String fName : fileNames) {
            if (!checkFName(fName)) {
                continue;
            }

            try (BufferedReader br = new BufferedReader(new FileReader(fName))) {
                String line;
                while ((line = br.readLine()) != null) {
                    System.out.print("FROM:" + fName + "\tREAD:\t" + line);
                    writeDB(reader(line));
                }
            }
        }
    }

    public FSReader setPath(String path) {
        this.path = path;
        return this;
    }

}
