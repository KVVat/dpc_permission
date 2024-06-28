package com.android.certification.niap.permission.dpctester.test

import android.content.Context
import androidx.core.util.Consumer
import com.android.certification.niap.permission.dpctester.test.log.ActivityLogger
import com.android.certification.niap.permission.dpctester.test.log.LoggerFactory

//Base class for test cases
open class PermissionTestModuleBase(ctx: Context) {
    open var TAG:String = PermissionTestModuleBase::class.java.simpleName
    val logger = LoggerFactory.createActivityLogger("TAG",ctx as ActivityLogger.LogListAdaptable)
    val title:String? = javaClass.getAnnotation(PermissionTestModule::class.java)?.name
    open fun start(callback: Consumer<PermissionTestRunner.Result>?){

        PermissionTestRunner.getInstance().start(this,callback);

    }
}