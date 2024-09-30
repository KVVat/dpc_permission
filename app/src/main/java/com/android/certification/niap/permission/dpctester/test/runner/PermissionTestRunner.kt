package com.android.certification.niap.permission.dpctester.test.runner

import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import com.android.certification.niap.permission.dpctester.common.ReflectionUtil
import com.android.certification.niap.permission.dpctester.data.LogBox
import com.android.certification.niap.permission.dpctester.test.exception.BypassTestException
import com.android.certification.niap.permission.dpctester.test.exception.UnexpectedTestFailureException
import com.android.certification.niap.permission.dpctester.test.log.StaticLogger
import com.android.certification.niap.permission.dpctester.test.tool.ReflectionTool
import kotlinx.coroutines.sync.Mutex
import java.lang.reflect.InvocationTargetException
import java.util.concurrent.atomic.AtomicInteger
import java.util.function.Consumer
import kotlin.reflect.KClass

class PermissionTestRunner {
    val TAG: String = PermissionTestRunner::class.java.simpleName

    companion object {
        private var instance_ : PermissionTestRunner? = null
        var running = false
        @JvmField
        var inverse_test_result = false
        //var finished = 0
        var finished: AtomicInteger = AtomicInteger(0)
        fun getInstance(): PermissionTestRunner {
            if(instance_ == null){
                instance_ = PermissionTestRunner()
            }
            return instance_!!
        }
        var testThreadMutex = Mutex(false);
    }
    /**
     * Inverse the results of the test cases
     */
    fun setInverseTestResult(inverse:Boolean){
        inverse_test_result = inverse
    }


    fun newTestThread(root: PermissionTestModuleBase,testCase:Data,callback: Consumer<Result>?):Thread {
        return Thread {
            val is_inverse = inverse_test_result || root.inverseForPlatformTesting
            val B_SUCCESS = if(is_inverse) false else true
            val B_FAILURE = if(is_inverse) true else false
            var success=B_SUCCESS

            var throwable:Throwable? = null
            var bypassed = false
            var message = "(none)"
            try {
                try {
                    //Preliminary Conditions Check
                    // Check required Permissions
                    testCase.requiredPermissions.forEach {
                        if (ActivityCompat.checkSelfPermission(root.mContext, it)
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
                    StaticLogger.debug("running=>"+testCase.methodName)
                    ReflectionUtil.invoke(root, testCase.methodName)

                } catch (ex: ReflectionUtil.ReflectionIsTemporaryException) {
                    if (ex.cause is InvocationTargetException) {
                        val exi = ex.cause
                        throwable = exi?.cause
                    } else {
                        throwable = ex.cause
                    }
                    //StaticLogger.info(">>>"+throwable?.message)
                    throw throwable!! //rethrow
                }
            } catch(ex:NullPointerException){
                //Is intended null pointer exception? (missing system service=>bypass)
                if(ex.message !=null && ex.message!!.startsWith("[npe_system_service]")){
                    throwable = ex
                    success=true //bypassed test always returns true
                    bypassed=true
                    message = "The system does not have the hardware feature required to run this test."
                } else {
                    throwable = ex
                    success = B_FAILURE
                    //@TODO cause fatal error?
                    message = if (ex.message != null) ex.message!! else ex.toString()
                }//ex.message!!
            } catch(ex:SecurityException) {
                throwable = ex
                success = B_FAILURE
                if(ex.message != null)
                    message = ex.message!!

            } catch(ex: BypassTestException){
                throwable = ex
                success=true //bypassed test always returns true
                bypassed=true
                message = ex.message!!
            } catch (ex:UnexpectedTestFailureException) {
                //Unexpected Failures
                //StaticLogger.info("Unexpected:"+ex.message);
                //ex.printStackTrace()
                throwable = ex.cause
                success = B_FAILURE
                bypassed=false
                message = ex.cause?.message!!
            } catch (ex:Exception){
                //Unexpected Failures
                ex.printStackTrace()
                throwable = ex
                success = B_FAILURE
                message = if(ex.message != null) ex.message!! else ex.toString()
            }
            if(bypassed){
                suite.info.count_bypassed += 1
                root.info.count_bypassed  += 1 // suite.info.count_bypassed + 1
                root.info.moduleLog.add(
                    LogBox(type = "bypassed", name =testCase.permission, description = message));
            }
            if(!success){
                suite.info.count_errors += 1
                root.info.count_errors  += 1 // suite.info.count_errors + 1
                root.info.moduleLog.add(
                    LogBox(type = "error", name =testCase.permission, description = message));

            }
            //safe call
            val finished_cnt = finished.incrementAndGet()
            //testLatch = CountDownLatch(1);
            //suite.info.count_errors = suite.info.count_errors + if(success) 0 else 1
            root.mActivity.runOnUiThread{
                callback?.accept(Result(success,throwable,testCase, finished_cnt,root.testSize,bypassed,message))
                suite.cbModuleControl?.accept(root.info)
                suite.cbTestControl?.accept(root.info)
            }

        }
    }


    var modulePos = 0;

    fun runNextModule(suite_: PermissionTestSuiteBase, callback: Consumer<Result>?):Boolean {

        if(modulePos>=suite_.modules.size){
            //suite_.info.ellapsed_time = System.currentTimeMillis() - suite_.info.start_time
            suite_.info.ellapsed_time = System.currentTimeMillis() - suite_.info.start_time;
            //mActivity.runOnUiThread {
            suite_.cbSuiteFinish?.accept(suite_.info)
            //}
            return false;
        }
        val m:PermissionTestModuleBase = suite_.modules.get(modulePos);
        modulePos+=1;
        if(m.enabled) {
            val prepareInfo = m.prepare(callback)
            val testCases = ReflectionTool.checkPermissionTestMethod(m)
            m.info.moduleLog.clear()
            m.info.count_tests = prepareInfo.count_tests + testCases.size
            m.info.count_errors = prepareInfo.count_errors
            m.info.count_bypassed = prepareInfo.count_bypassed
            if (prepareInfo.count_tests > 0) {
                StaticLogger.info("there are ${prepareInfo.count_tests} pre-running testcases")
            }
            m.mActivity.runOnUiThread {
                suite_.cbModuleStart?.accept(m.info)
            }
            val threads = mutableListOf<Thread>()

            for (testCase in testCases) {
                // Block If the version is not supported.
                // If the permission has a corresponding task then run it.
                val thread = newTestThread(m, testCase, callback)
                //m.mActivity.runOnUiThread(thread);
                thread.start();
                synchronized(threads) {
                    thread.join(1000)
                }
                if(m.isSync) testThreadMutex.tryLock()
            }
            //wait all thread finished
            /*for (thread in threads) {
                thread.join(500)
            }*/
            m.mActivity.runOnUiThread{
                this.suite.cbModuleFinish?.accept(m.info)
            }
        }

        return true
    }

    lateinit var suite: PermissionTestSuiteBase
    fun start(suite_: PermissionTestSuiteBase, callback: Consumer<Result>?) {
        this.suite = suite_;
        this.suite.methodCallback = callback;
        this.suite.info.start_time = System.currentTimeMillis()
        suite_.cbSuiteStart?.accept(suite_.info)
        if(running){
            throw IllegalStateException("Other Suite Already running")
        }
        running = true;
        modulePos=0
        runNextModule(suite_,this.suite.methodCallback)


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
        constructor(permission: String) : this(permission=permission,
            sdkMin = 0,sdkMax=1000, methodName = "",
            requiredPermissions = arrayOf(),requiredServices= arrayOf()
        )

        init {
            if(!permission.contains(".")){
                permission ="android.permission."+permission;
            }
        }
    }
}