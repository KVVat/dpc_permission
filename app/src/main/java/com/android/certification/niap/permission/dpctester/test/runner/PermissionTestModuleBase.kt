package com.android.certification.niap.permission.dpctester.test.runner

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.Signature
import android.os.IBinder
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.certification.niap.permission.dpctester.DpcApplication
import com.android.certification.niap.permission.dpctester.common.Constants
import com.android.certification.niap.permission.dpctester.common.SignatureUtils
import com.android.certification.niap.permission.dpctester.test.exception.UnexpectedTestFailureException
import com.android.certification.niap.permission.dpctester.test.log.ActivityLogger
import com.android.certification.niap.permission.dpctester.test.log.LoggerFactory
import com.android.certification.niap.permission.dpctester.test.tool.PermissionTestModule
import com.android.certification.niap.permission.dpctester.test.tool.ReflectionTool
import java.util.function.Consumer

//Base class for test cases
open class PermissionTestModuleBase(activity: Activity) {
    open var TAG: String = PermissionTestModuleBase::class.java.simpleName
    val title: String? = javaClass.getAnnotation(PermissionTestModule::class.java)?.name

    @JvmField
    protected var logger: ActivityLogger
    @JvmField
    var testCases: List<PermissionTestRunner.Data>
    @JvmField
    val testSize: Int
    @JvmField
    val mActivity: Activity = activity
    @JvmField
    val mContext: Context = activity.applicationContext
    @JvmField
    protected val mContentResolver = mContext.contentResolver
    @JvmField
    protected val mPackageManager = mContext.packageManager
    @JvmField
    protected val mExecutor = (mContext.applicationContext as DpcApplication).executorService
    @JvmField
    val appUid = mContext.applicationInfo.uid

    protected val mAppSignature : Signature =
        SignatureUtils.getTestAppSigningCertificate(mContext);
    @JvmField
    protected val isPlatformSignatureMatch:Boolean =
        mPackageManager.hasSigningCertificate(
            Constants.PLATFORM_PACKAGE,
            mAppSignature.toByteArray(), PackageManager.CERT_INPUT_RAW_X509
        );

    init {
        testCases = ReflectionTool.checkPermissionTestMethod(this)
        testSize = testCases.size
        logger = LoggerFactory.createActivityLogger(
            title!!,
            activity as ActivityLogger.LogListAdaptable
        ) as ActivityLogger
        logger.system("The module `$title` has ${testSize} test cases.")
    }


    fun checkPermissionGranted(permission: String): Boolean {
        return ActivityCompat.checkSelfPermission(mContext,permission)==PackageManager.PERMISSION_GRANTED
    }
    fun <T> getService(serviceClass: Class<T>): T? {
        val service = ContextCompat.getSystemService(mContext, serviceClass)
        return service
    }

    fun <T> getService(serviceClass: Class<T>,contextName:String): T? {
        //ContextCompat.getSystemService(mActivity,serviceClass)
        val service_ = mContext.getSystemService(contextName)

        //val service = ContextCompat.getSystemService(mContext, serviceClass)
        if(service_.javaClass == serviceClass){
            return service_ as T
        }
        return null
    }

    /**
     * Returns the [IBinder] token for the current activity.
     *
     *
     * This token can be used in any binder transaction that requires the activity's token.
     */
    @SuppressLint("DiscouragedPrivateApi")
    fun getActivityToken(): IBinder {
        try {
            val tokenField = Activity::class.java.getDeclaredField("mToken")
            tokenField.isAccessible = true
            return tokenField[mActivity] as IBinder
        } catch (e: ReflectiveOperationException) {
            throw UnexpectedTestFailureException(e)
        }
    }

    open fun start(callback: Consumer<PermissionTestRunner.Result>?){

        PermissionTestRunner.getInstance().start(this,callback);

    }
    open fun finalize()
    {
        //PermissionTestRunner.getInstance().finalize(this)
    }

}