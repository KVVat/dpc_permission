package com.android.certification.niap.permission.dpctester.test

import android.util.Log
import java.util.stream.Collectors

class ReflectionTool {
    companion object {
        /**
         * Invokes the specified `methodName` defined in the `targetClass` against the
         * `targetObject` (or `null` for a static method) that accepts the provided `parameterClasses` with values `parameters`.
         *
         *
         * To support running reflective calls for permission tests if the reflective method fails
         * due to a SecurityException then the SecurityException is rethrown as is; any other exception
         * results in a [BasePermissionTester.UnexpectedPermissionTestFailureException].
         *
         * @return the result of invoking the specified method
         */
        fun invoke(
            targetClass: Class<*>, methodName: String?,
            targetObject: Any?, parameterClasses: Array<Class<*>?>, vararg parameters: Any?
        ): Any {
            try {
                val method = targetClass.getMethod(methodName, *parameterClasses)
                return method.invoke(targetObject, *parameters)
            } catch (e: ReflectiveOperationException) {
                Log.e("TAG", "Reflection failed.")
                e.printStackTrace()
                val cause = e.cause
                if (cause is SecurityException) {
                    throw (cause as SecurityException?)!!
                } else {
                    throw cause!!//UnexpectedPermissionTestFailureException(e)
                }
            } catch (e:SecurityException){
                Log.e("TAG", "Other error")
                throw e
            }
        }

        /**
         * List up declared methods of specified object. it is intended to check the prototype of
         * transacts api.
         */
        fun checkDeclaredMethod(target: Any, filter: String?): List<String> {
            val a: MutableList<String> = ArrayList()
            val clazz: Class<*> = target.javaClass
            val methods = clazz.declaredMethods
            for (m in methods) {
                val method = StringBuilder(m.name + "(")
                val types = m.parameterTypes
                for (t in types) method.append(" ").append(t.typeName)
                a.add("$method)")
            }
            return a.stream().filter { str: String ->
                str.startsWith(
                    filter!!
                )
            }.collect(Collectors.toList())
        }

        fun checkDeclaredFields(target: Any, filter: String?): List<String> {
            val a: MutableList<String> = ArrayList()
            val clazz: Class<*> = target.javaClass
            val fields = clazz.declaredFields
            for (f in fields) {
                val field = StringBuilder(f.name + "(")
                a.add(field.toString() + "<" + f.type + ">")
            }
            return a.stream().filter { str: String ->
                str.startsWith(
                    filter!!
                )
            }.collect(Collectors.toList())
        }

        fun checkPermissionTestMethod(target: Any): List<PermissionTestRunner.Data> {
            val a: MutableList<PermissionTestRunner.Data> = ArrayList()
            val clazz: Class<*> = target.javaClass
            val methods = clazz.declaredMethods
            //Take all methods that has PermissionTest annotation
            for (m in methods) {
                val ann = m.getAnnotation(PermissionTest::class.java)
                if (ann == null) continue;
                val container =
                    PermissionTestRunner.Data(ann.permission, ann.sdkMin, ann.sdkMax, m.name)
                //Log.d("TAG",container.toString())
                a.add(container)
            }
            return a.toList()
        }
        /*
        fun deviceConfigSetProperty(
            namespace: String,
            name: String,
            value: String,
            makeDefault: Boolean
        ): Boolean {
            try {
                val r: Any =
                    invokeReflectionCall(
                        Class.forName("android.provider.DeviceConfig"),
                        "setProperty", null,
                        arrayOf<Class<*>?>(
                            String::class.java, String::class.java, String::class.java,
                            Boolean::class.javaPrimitiveType
                        ), namespace, name, value, makeDefault
                    )
                return r as Boolean
            } catch (e: Exception) {
                debug("DeviceConfig.setProperty failed.($namespace,$name,$value)")
                e.printStackTrace()
                return false
            }
        }

        fun deviceConfigGetProperty(namespace: String, name: String): String? {
            try {
                return invokeReflectionCall(
                    Class.forName("android.provider.DeviceConfig"),
                    "getProperty", null,
                    arrayOf<Class<*>>(String::class.java, String::class.java), namespace, name
                )
            } catch (e: Exception) {
                debug("DeviceConfig.getProperty failed.($namespace,$name)")
                return null
            }
        }*/
    }//companion object
}
