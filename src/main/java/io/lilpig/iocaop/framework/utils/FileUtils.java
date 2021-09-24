package io.lilpig.iocaop.framework.utils;

import java.io.File;

public class FileUtils {
    public static String extName(File file) {
        String fileName = file.getName();
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    public static String fileNameWithoutExtName(File file) {
        String fileName = file.getName();
        return fileName.substring(0, fileName.lastIndexOf("."));
    }
}
