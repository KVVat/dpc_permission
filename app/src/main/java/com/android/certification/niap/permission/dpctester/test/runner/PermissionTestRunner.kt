package com.android.certification.niap.permission.dpctester.test.runner

import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import com.android.certification.niap.permission.dpctester.common.ReflectionUtil
import com.android.certification.niap.permission.dpctester.test.exception.BypassTestException
import com.android.certification.niap.permission.dpctester.test.exception.UnexpectedTestFailureException
import com.android.certification.niap.permission.dpctester.test.tool.ReflectionTool
import java.lang.reflect.InvocationTargetException
import java.util.function.Consumer
import kotlin.reflect.KClass

class PermissionTestRunner {
    val TAG: String = PermissionTestRunner::class.java.simpleName

    companion object {
        private var instance_ : PermissionTestRunner? = null
        var running = false
        var inverse_test_result = false
        var finished = 0
        fun getInstance(): PermissionTestRunner {
            if(instance_ == null){
                instance_ = PermissionTestRunner()
            }
            return instance_!!
        }
    }
    /**
     * Inverse the results of the test cases
     */
    fun setInverseTestResult(inverse:Boolean){
        inverse_test_result = inverse
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
                var success = if(inverse_test_result) false else true
                var throwable:Throwable? = null
                var bypassed = false
                var message:String = "(none)"
                try {
                    try {
                        //Preliminary Conditions Check
                        // Check required Permissions
                        testCase.requiredPermissions.forEach {
                            if (ActivityCompat.checkSelfPermission(tests_.mContext, it)
                                != PackageManager.PERMISSION_GRANTED
                            ) {
                                throw BypassTestException(
                                    "${testCase.permission} : `$it`permission should be granted to run ."
                                )
                            }
                        }
                        // Check Required Services : difficult to check.
                        // Check Android Version
                        if(Build.VERSION.SDK_INT<testCase.sdkMin){
                            throw BypassTestException(
                                "${testCase.permission} : SDK${Build.VERSION.SDK_INT} is not supported to run.(SDK MIN:${testCase.sdkMin})"
                            )
                        }
                        if(Build.VERSION.SDK_INT>testCase.sdkMax){
                            throw BypassTestException(
                                "${testCase.permission} : SDK${Build.VERSION.SDK_INT} is not supported to run.(SDK MAX:${testCase.sdkMax})"
                            )
                        }
                        // testCase.re
                        ReflectionUtil.invoke(tests_, testCase.methodName)

                    } catch (ex: ReflectionUtil.ReflectionIsTemporaryException) {
                        if (ex.cause is InvocationTargetException) {
                            val exi = ex.cause
                            throwable = exi?.cause
                        } else {
                            throwable = ex.cause
                        }
                        throw throwable!! //rethrow
                    }
                }catch(ex:NullPointerException){
                    //Is intended null pointer exception? (missing system service=>bypass)
                    throwable = ex
                    success=!success
                    //@TODO cause fatal error?
                    message = ex.message!!
                } catch(ex:SecurityException){
                    throwable = ex
                    success=!success
                    message = ex.message!!
                } catch(ex: BypassTestException){
                    throwable = ex
                    success=true //bypassed test always returns true
                    bypassed=true
                    message = ex.message!!
                } catch (ex:UnexpectedTestFailureException) {
                    //Unexpected Failures
                    ex.printStackTrace()
                    throwable = ex
                    success = !success
                    message = ex.message!!
                } catch (ex:Exception){
                    //Unexpected Failures
                    ex.printStackTrace()
                    throwable = ex
                    success = !success
                    message = if(ex.message != null) ex.message!! else ex.toString()
                }
                //safe call

                finished++
                callback?.accept(Result(success,throwable,testCase, finished,testSize,bypassed,message))
                if(finished >=testSize){
                    running = false
                }
            }
            thread.start();
        }
    }

    data class Result(var success:Boolean, val throwable:Throwable? = null, val source: Data, val finished: Int,
                      val testSize: Int, var bypassed: Boolean = false,var message:String="")

    data class Data(
        var permission: String,
        val sdkMin: Int,
        val sdkMax: Int,
        val methodName: String,
        val requiredPermissions: Array<String>,
        val requiredServices: Array<KClass<out Any>>,
    ){
        init {
            if(!permission.contains(".")){
                permission ="android.permission."+permission;
            }
        }
    }
}