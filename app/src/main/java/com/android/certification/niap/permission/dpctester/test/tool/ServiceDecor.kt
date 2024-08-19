package com.android.certification.niap.permission.dpctester.test.tool

import android.content.Context
import java.util.Objects

class ServiceDecorImpl(context: Context) : ServiceDecor {
    override fun <T> getService(serviceClass: Class<T>): T? {
        TODO("Not yet implemented")
    }

    override fun <T> systemService(clazz: Class<T>): T {
        return Objects.requireNonNull(getService(clazz), "[npe_system_service]" + clazz.simpleName)!!
    }

}

interface ServiceDecor {
    fun <T> getService(serviceClass: Class<T>): T?
    //fun systemService(serviceClass: Class<*>): Any?
    fun <T> systemService(clazz: Class<T>): T;
}


//
//    {
//        return Objects.requireNonNull(getService(clazz), "[npe_system_service]" + clazz.simpleName)
//    }