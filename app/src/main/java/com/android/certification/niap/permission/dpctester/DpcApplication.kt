package com.android.certification.niap.permission.dpctester

import android.app.Application
import android.os.Handler
import android.os.Looper
import androidx.core.os.HandlerCompat
import com.android.certification.niap.permission.dpctester.test.tool.BinderTransaction
import com.android.certification.niap.permission.dpctester.test.tool.BinderTransactsDict
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class DpcApplication : Application() {
    var executorService: ExecutorService = Executors.newFixedThreadPool(5)
    var mainThreadHandler: Handler = HandlerCompat.createAsync(Looper.getMainLooper())
    override fun onCreate() {
        super.onCreate()

        /*invokeReflectionCall(statusBarManager.getClass(), "expandNotificationsPanel",
                statusBarManager, null);

        mTransacts.invokeTransact(Transacts.DEVICE_POLICY_SERVICE,
                Transacts.DEVICE_POLICY_DESCRIPTOR,
                Transacts.getNearbyNotificationStreamingPolicy, 0);*/
        //call it in more suitable place

        BinderTransaction.Builder(applicationContext).build()
        BinderTransactsDict.Builder(applicationContext).build()
    }

}