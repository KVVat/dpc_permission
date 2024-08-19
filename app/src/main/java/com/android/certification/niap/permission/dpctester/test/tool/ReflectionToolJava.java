package com.android.certification.niap.permission.dpctester.test.tool;

import com.android.certification.niap.permission.dpctester.test.exception.UnexpectedTestFailureException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionToolJava {


    public static Object invokeReflectionCall(Class<?> targetClass, String methodName,
                                              Object targetObject, Class<?>[] parameterClasses, Object... parameters) {
        try {
            Method method = targetClass.getMethod(methodName, parameterClasses);
            return method.invoke(targetObject, parameters);
        } catch (ReflectiveOperationException e) {
            Throwable cause = e.getCause();
            if (cause instanceof SecurityException) {
                throw (SecurityException) cause;
            } else {
                throw new UnexpectedTestFailureException(e);
            }
        }
    }

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
