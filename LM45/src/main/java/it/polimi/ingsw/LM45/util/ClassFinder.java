package it.polimi.ingsw.LM45.util;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ClassFinder {

	public static List<Class<?>> find(String scannedPackage) {
        String scannedPath = scannedPackage.replace('.', '/');
        URL scannedUrl = ClassFinder.class.getClassLoader().getResource(scannedPath);
        if (scannedUrl == null) {
            throw new IllegalArgumentException(String.format("error", scannedPath, scannedPackage));
        }
        File scannedDir = new File(scannedUrl.getFile());
        List<Class<?>> classes = new ArrayList<Class<?>>();
        for (File file : scannedDir.listFiles()) {
            classes.addAll(find(file, scannedPackage));
        }
        return classes;
    }
	
	private static List<Class<?>> find(File file, String scannedPackage) {
        List<Class<?>> classes = new ArrayList<Class<?>>();
        String resource = scannedPackage + '.' + file.getName();
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                classes.addAll(find(child, resource));
            }
        } else if (resource.endsWith(".class")) {
            int endIndex = resource.length() - ".class".length();
            String className = resource.substring(0, endIndex);
            try {
                classes.add(Class.forName(className));
            } catch (ClassNotFoundException ignore) {
            }
        }
        return classes;
    }
	
}
