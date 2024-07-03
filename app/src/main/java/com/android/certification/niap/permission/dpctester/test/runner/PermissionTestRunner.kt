package com.android.certification.niap.permission.dpctester.test.runner

import androidx.core.util.Consumer
import com.android.certification.niap.permission.dpctester.common.ReflectionUtil
import com.android.certification.niap.permission.dpctester.test.tool.ReflectionTool
import java.lang.reflect.InvocationTargetException

class PermissionTestRunner {
    val TAG: String = PermissionTestRunner::class.java.simpleName

    companion object {
        private var instance_ : PermissionTestRunner? = null
        var running = false
        var finished = 0
        fun getInstance(): PermissionTestRunner {
            if(instance_ == null){
                instance_ = PermissionTestRunner()
            }
            return instance_!!
        }
    }
    fun start(tests_: PermissionTestModuleBase, callback: Consumer<Result>?) {
        //Runner
        if(running){
            throw IllegalStateException("Already running")
        } else {
            running = true
            finished = 0;
        }

        val testCases = ReflectionTool.checkPermissionTestMethod(tests_)
        val testSize = testCases.size
        for (testCase in testCases) {
            // Block If the version is not supported.
            // If the permission has a corresponding task then run it.
            val thread = Thread {
                var success = true
                var throwable:Throwable? = null

                try {
                    ReflectionUtil.invoke(tests_,testCase.methodName)
                } catch(ex: ReflectionUtil.ReflectionIsTemporaryException){
                    //ex.printStackTrace()
                    if(ex.cause is InvocationTargetException){
                        val exi = ex.cause
                        throwable = exi?.cause
                    } else {
                        throwable = ex.cause
                    }
                    success=false
                } catch(ex:SecurityException){
                    throwable = ex
                    success= false
                } catch (ex:Exception){
                    ex.printStackTrace()
                    throwable = ex
                    success = false
                }
                //safe call

                finished++
                callback?.accept(Result(success,throwable,testCase, finished,testSize))
                if(finished >=testSize){
                    running = false
                }
            }
            thread.start();
        }
    }

    data class Result(var success:Boolean, val throwable:Throwable? = null, val source: Data, val finished: Int,
                      val testSize: Int)
    data class Data(
        var permission: String,
        val sdkMin: Int,
        val sdkMax: Int,
        val methodName: String,

    ){
        init {
            if(!permission.contains(".")){
                permission ="android.permission."+permission;
            }
        }
    }
}