package com.android.certification.niap.permission.dpctester.comp;

import static android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_DEFAULT;
import static android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_DISABLED;
import static android.content.pm.PackageManager.DONT_KILL_APP;

import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import androidx.annotation.Nullable;
import com.android.certification.niap.permission.dpctester.common.Util;
import com.android.certification.niap.permission.dpctester.receiver.DeviceAdminReceiver;
import com.android.certification.niap.permission.dpctester.activity.PolicyManagementActivity;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * Handle cross user call from a DPC instance in other side.
 *
 * @see {@link DevicePolicyManager#bindDeviceAdminServiceAsUser( ComponentName, Intent,
 *     ServiceConnection, int, UserHandle)}
 */
public class ProfileOwnerService extends Service {
    private Binder mBinder;
    private static final String TAG = "ProfileOwnerService";

    @Override
    public void onCreate() {
        super.onCreate();
        mBinder = new ProfileOwnerServiceImpl(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    static class ProfileOwnerServiceImpl extends IProfileOwnerService.Stub {
        private Context mContext;
        private DevicePolicyManager mDpm;
        private PackageManager mPm;

        public ProfileOwnerServiceImpl(Context context) {
            mContext = context;
            mDpm = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
            mPm = context.getPackageManager();
        }

        @Override
        public void setLauncherIconHidden(boolean hidden) throws RemoteException {
            mPm.setComponentEnabledSetting(
                    new ComponentName(mContext, PolicyManagementActivity.class),
                    hidden ? COMPONENT_ENABLED_STATE_DISABLED : COMPONENT_ENABLED_STATE_DEFAULT,
                    DONT_KILL_APP);
        }

        @Override
        public boolean isLauncherIconHidden() throws RemoteException {
            return mPm.getComponentEnabledSetting(
                    new ComponentName(mContext, PolicyManagementActivity.class))
                    == COMPONENT_ENABLED_STATE_DISABLED;
        }

        @Override
        public boolean installCaCertificate(AssetFileDescriptor afd) {
            try (FileInputStream fis = afd.createInputStream()) {
                return Util.installCaCertificate(fis, mDpm, DeviceAdminReceiver.getComponentName(mContext));
            } catch (IOException e) {
                Log.e(TAG, "Unable to install a certificate", e);
                return false;
            }
        }
    }
}