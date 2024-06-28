package com.android.certification.niap.permission.dpctester.comp;

import android.annotation.TargetApi;
import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build.VERSION_CODES;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.UserHandle;
import android.util.Log;

import com.android.certification.niap.permission.dpctester.receiver.DeviceAdminReceiver;

/**
 * Helper class for {@link DevicePolicyManager#bindDeviceAdminServiceAsUser( ComponentName, Intent,
 * ServiceConnection, int, UserHandle)}.
 */
@TargetApi(VERSION_CODES.O)
public class BindDeviceAdminServiceHelper<T> {
    private static final String TAG = "BindDeviceAdminService";

    private final Context mContext;
    private final DevicePolicyManager mDpm;
    private final Intent mServiceIntent;
    private final UserHandle mTargetUserHandle;
    private final ServiceInterfaceConverter<T> mServiceInterfaceConverter;

    /**
     * @param context
     * @param serviceClass Which service we are going to bind with.
     * @param serviceInterfaceConverter Used to convert {@link IBinder} to service interface.
     * @param targetUserHandle Who we are talking to.
     */
    public BindDeviceAdminServiceHelper(
            Context context,
            Class<? extends Service> serviceClass,
            ServiceInterfaceConverter<T> serviceInterfaceConverter,
            UserHandle targetUserHandle) {
        mContext = context;
        mDpm = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        mServiceInterfaceConverter = serviceInterfaceConverter;
        mServiceIntent = new Intent(context, serviceClass);
        mTargetUserHandle = targetUserHandle;
    }

    /**
     * Provide an easy way to run a one-off cross user call. You should run your service call in
     * {@link OnServiceConnectedListener#onServiceConnected(Object)}. Note that the listener is always
     * called in main thread, so if your service call is time consuming, please make sure you either
     * run it in worker thread or implement a callback mechanism.
     *
     * @param listener Called when service is connected.
     * @return Whether the binding is successful.
     */
    public boolean crossUserCall(OnServiceConnectedListener<T> listener) {
        final ServiceConnection serviceConnection =
                new ServiceConnection() {
                    @Override
                    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                        Log.d(TAG, "onServiceConnected() called");
                        T service = mServiceInterfaceConverter.convert(iBinder);
                        try {
                            listener.onServiceConnected(service);
                        } catch (RemoteException e) {
                            Log.e(TAG, "onServiceConnected: ", e);
                        } finally {
                            mContext.unbindService(this);
                        }
                    }

                    @Override
                    public void onServiceDisconnected(ComponentName componentName) {
                        Log.e(TAG, "onServiceDisconnected() called");
                        mContext.unbindService(this);
                    }
                };
        try {
            return mDpm.bindDeviceAdminServiceAsUser(
                    DeviceAdminReceiver.getComponentName(mContext),
                    mServiceIntent,
                    serviceConnection,
                    Context.BIND_AUTO_CREATE,
                    mTargetUserHandle);
        } catch (SecurityException | IllegalArgumentException e) {
            Log.e(TAG, "Cannot bind to user " + mTargetUserHandle, e);
            return false;
        }
    }
}
