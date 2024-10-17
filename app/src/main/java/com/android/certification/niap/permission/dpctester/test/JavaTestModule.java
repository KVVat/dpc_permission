package com.android.certification.niap.permission.dpctester.test;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import com.android.certification.niap.permission.dpctester.test.runner.PermissionTestModuleBase;
import com.android.certification.niap.permission.dpctester.test.runner.PermissionTestRunner;
import com.android.certification.niap.permission.dpctester.test.tool.BinderTransaction;
import com.android.certification.niap.permission.dpctester.test.tool.BinderTransactsDict;
import com.android.certification.niap.permission.dpctester.test.tool.PermissionTest;
import com.android.certification.niap.permission.dpctester.test.tool.PermissionTestModule;

import java.lang.reflect.ParameterizedType;
import java.util.function.Consumer;



@PermissionTestModule(name = "Java Test")
public class JavaTestModule extends PermissionTestModuleBase {
    public JavaTestModule(@NonNull Activity ctx) {
        super(ctx);
    }

    @PermissionTest(permission = ".Java Fun",sdkMin = 34,sdkMax = 35)
    public void checkJavaIsWorking() {
        logger.system("message from java!");
        //.invoke(Context.DEVICE_PO
        BinderTransaction.getInstance().invoke(
                Context.DEVICE_POLICY_SERVICE,
                "android.app.admin.IDevicePolicyManager",
                "getNearbyNotificationStreamingPolicy",0
                );

    }
    @PermissionTest(permission = ".Protected Java",sdkMin = 34,sdkMax = 35)
    void checkProtectedMember() {
        logger.system("message from protected member!");
    }
}
