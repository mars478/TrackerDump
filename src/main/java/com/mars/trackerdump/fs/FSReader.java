package com.mars.trackerdump.fs;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
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

            try (RandomAccessFile raf = new RandomAccessFile(fName, "r");
                    FileChannel fc = raf.getChannel();) {

                ByteBuffer buf = ByteBuffer.allocate(1024);
                while (fc.read(buf) > 0) {
                    buf.flip();
                    for (int i = 0; i < buf.limit(); i++) {
                        System.out.print((char) buf.get());
                    }
                    buf.clear();
                }
            }
        }
    }

    public FSReader setPath(String path) {
        this.path = path;
        return this;
    }

}
