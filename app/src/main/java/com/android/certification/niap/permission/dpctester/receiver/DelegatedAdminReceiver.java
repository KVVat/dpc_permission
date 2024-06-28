package com.android.certification.niap.permission.dpctester.receiver;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build.VERSION_CODES;
import android.util.Log;
import android.widget.Toast;

import com.android.certification.niap.permission.dpctester.R;

@TargetApi(VERSION_CODES.Q)
public class DelegatedAdminReceiver extends android.app.admin.DelegatedAdminReceiver {

    private static final String TAG = "DelegatedAdminReceiver";

    @Override
    public String onChoosePrivateKeyAlias(
            Context context, Intent intent, int uid, Uri uri, String alias) {
        return CommonReceiverOperations.onChoosePrivateKeyAlias(context, uid);
    }

    @Override
    public void onNetworkLogsAvailable(
            Context context, Intent intent, long batchToken, int networkLogsCount) {
        CommonReceiverOperations.onNetworkLogsAvailable(context, null, batchToken, networkLogsCount);
    }

    @Override
    public void onSecurityLogsAvailable(
            Context context, Intent intent) {
        Log.i(TAG, "onSecurityLogsAvailable() called");
        Toast.makeText(context,  R.string.on_security_logs_available, Toast.LENGTH_LONG).show();
    }
}