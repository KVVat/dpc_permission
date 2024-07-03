package com.android.certification.niap.permission.dpctester.test.runner

import android.content.Context
import androidx.core.util.Consumer
import com.android.certification.niap.permission.dpctester.test.tool.PermissionTestModule
import com.android.certification.niap.permission.dpctester.test.log.ActivityLogger
import com.android.certification.niap.permission.dpctester.test.log.LoggerFactory
import com.android.certification.niap.permission.dpctester.test.tool.ReflectionTool

//Base class for test cases
open class PermissionTestModuleBase(ctx: Context) {
    open var TAG:String = PermissionTestModuleBase::class.java.simpleName
    val logger = LoggerFactory.createActivityLogger("TAG",ctx as ActivityLogger.LogListAdaptable)
    val title:String? = javaClass.getAnnotation(PermissionTestModule::class.java)?.name
    var testCases:List<PermissionTestRunner.Data>
    val testSize:Int
    init {
        testCases = ReflectionTool.checkPermissionTestMethod(this)
        testSize = testCases.size
        logger.system("The module `$title` has ${testSize} test cases")
    }

    open fun start(callback: Consumer<PermissionTestRunner.Result>?){

        PermissionTestRunner.getInstance().start(this,callback);

    }
}