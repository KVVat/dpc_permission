package com.android.certification.niap.permission.dpctester.test.tool;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ReflectionToolJava {

    public static Object stubHiddenObject(String classname)  {
        try {
            Class<?> remoteCallbackClass = Class.forName(classname);
            Constructor<?> remoteCallbackConstructor = remoteCallbackClass.getConstructor();
            return remoteCallbackConstructor.newInstance();
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException |
                 InstantiationException | InvocationTargetException e){
            e.printStackTrace();
            return null;
        }
    }
    public static Object stubHiddenObject(String classname,Class<?>[] parameters,Object... args)  {
        try {
            Class<?> remoteCallbackClass = Class.forName(classname);
            Constructor<?> remoteCallbackConstructor = remoteCallbackClass.getDeclaredConstructor(parameters);
            return remoteCallbackConstructor.newInstance(args);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException |
                 InstantiationException | InvocationTargetException e){
            e.printStackTrace();
            return null;
        }
    }
}
