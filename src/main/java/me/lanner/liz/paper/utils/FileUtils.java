package me.lanner.liz.paper.utils;

import java.io.File;
import java.io.IOException;

public class FileUtils {

    private FileUtils() {
        throw new UnsupportedOperationException();
    }

    public static File openNewFile(String filePath) throws IOException {
        File file = new File(filePath);
        File parentPath = file.getParentFile();
        if (parentPath != null && ! parentPath.exists()) {
            parentPath.mkdirs();
        }
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
        return file;
    }

}
