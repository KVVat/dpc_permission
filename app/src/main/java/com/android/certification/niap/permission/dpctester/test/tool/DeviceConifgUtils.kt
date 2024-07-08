package com.android.certification.niap.permission.dpctester.test.tool

class DeviceConfigTool {
    companion object {
        fun getProperty(namespace:String,name:String):String? {
            return Class.forName("android.provider.DeviceConfig")!!.
                getMethod("getProperty", String::class.java,String::class.java).run {
                invoke(null, namespace, name) as String
            }
        }
        fun getBoolean(namespace:String,name:String):Boolean? {
            return Class.forName("android.provider.DeviceConfig")!!.
            getMethod("getBoolean", String::class.java,String::class.java).run {
                invoke(null, namespace, name) as Boolean
            }
        }
        fun setProperty(namespace:String,name:String,value:String,makeDefault:Boolean=false):Boolean {
            return Class.forName("android.provider.DeviceConfig")!!.
            getMethod("setProperty", String::class.java,String::class.java,String::class.java,Boolean::class.java).run {
                invoke(null, namespace, name,value,makeDefault) as Boolean
            }
        }
    }
}