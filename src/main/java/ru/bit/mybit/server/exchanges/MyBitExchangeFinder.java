package ru.bit.mybit.server.exchanges;

import org.knowm.xchange.Exchange;

import java.io.File;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.reflections.Reflections;

public class MyBitExchangeFinder {
    private static final char DT = '.';
    private static final char SL = '/';
    private static final String CLASS = ".class";
    private static final String ERROR = "Resources not found for '%s'";

    public static List<Class<? extends Exchange>> findNew(String scPkg) {

        List<Class<? extends Exchange>> exchangeClasses = new ArrayList<>();

        // Find every Exchange
        Reflections reflections = new Reflections(scPkg);
        for (Class<? extends Exchange> exchangeClass : reflections.getSubTypesOf(Exchange.class)) {
            if (Modifier.isAbstract(exchangeClass.getModifiers())) {
                continue;
            }

            exchangeClasses.add(exchangeClass);
        }

        return exchangeClasses;

    }

    public static Iterable<Object[]> data() {

        List<Object[]> exchangeClasses = new ArrayList<>();

        // Find every Exchange
        Reflections reflections = new Reflections("org.knowm.xchange");
        for (Class<? extends Exchange> exchangeClass : reflections.getSubTypesOf(Exchange.class)) {
            if (Modifier.isAbstract(exchangeClass.getModifiers())) {
                continue;
            }

            exchangeClasses.add(new Object[]{exchangeClass, exchangeClass.getSimpleName()});
        }

        return exchangeClasses;
    }

    public static List<Class<?>> find(String scPkg) {
        String scPath = scPkg.replace(DT, SL);
        URL scUrl = Thread.currentThread().getContextClassLoader().getResource(scPath);


        if (scUrl == null) {
            throw new IllegalArgumentException(String.format(ERROR, scPath, scPkg));
        }
        File scannedDir = new File(scUrl.getFile());
        List<Class<?>> classes = new ArrayList<Class<?>>();
        for (File file : scannedDir.listFiles()) {
            classes.addAll(find(file, scPkg));
        }
        return classes;
    }

    private static List<Class<?>> find(File file, String scPkg) {
        List<Class<?>> classes = new ArrayList<Class<?>>();
        String resource = scPkg + DT + file.getName();


        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                classes.addAll(find(child, resource));
            }
        } else if (resource.endsWith(CLASS)) {
            int endIndex = resource.length() - CLASS.length();
            String className = resource.substring(0, endIndex);
            try {
                classes.add(Class.forName(className));
            } catch (ClassNotFoundException ignore) {
            }
        }
        return classes;
    }
}
