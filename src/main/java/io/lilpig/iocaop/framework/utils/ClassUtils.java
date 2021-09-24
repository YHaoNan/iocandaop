package io.lilpig.iocaop.framework.utils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

public class ClassUtils {

    public static Set<Class<?>> getAllClassesInPackage(String pkg) throws IOException, ClassNotFoundException {
        String pkgDir = pkg.replace('.', '/');
        Enumeration<URL> dirs = Thread.currentThread().getContextClassLoader().getResources(pkgDir);
        Set<Class<?>> resultSet = new HashSet<>();
        while (dirs.hasMoreElements()) {
            URL url = dirs.nextElement();
            String protocol = url.getProtocol();
            String realPath = URLDecoder.decode(url.getFile(), "UTF-8");
            if ("file".equals(protocol)) {
                resultSet.addAll(findAllClassesInPackageByFile(pkg, realPath));
            } else if ("jar".equals(protocol)) {
                resultSet.addAll(findAllClassesInPackageByJar(pkg, realPath));
            }
        }
        return resultSet;
    }

    private static Set<Class<?>> findAllClassesInPackageByJar(String pkg, String realPath) {
        return new HashSet<>();
    }

    private static Set<Class<?>> findAllClassesInPackageByFile(String pkg, String realPath) throws ClassNotFoundException {
        Set<Class<?>> resultSet = new HashSet<>();

        File packageDir = new File(realPath);
        if (!packageDir.exists() || !packageDir.isDirectory()) return new HashSet<>();

        File files[] = packageDir.listFiles();
        for (File file: files) {
            if (file.isDirectory()) {
                Set<Class<?>> clzInSubFolder = findAllClassesInPackageByFile(
                        pkg + "." + file.getName(),
                        file.getAbsolutePath()
                );
                resultSet.addAll(clzInSubFolder);
            } else if (FileUtils.extName(file).equals("class")) {
                String classFullQualifierName = pkg + "." + FileUtils.fileNameWithoutExtName(file);
                Class<?> clz = Thread.currentThread().getContextClassLoader().loadClass(classFullQualifierName);
                resultSet.add(clz);
            }
        }
        return resultSet;
    }



}
