//Auto generated file InstallPermissionTestModule.java by CoderPorterPlugin
/*
 * Copyright 2024 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.certification.niap.permission.dpctester.test;


import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PermissionGroupInfo;
import android.content.pm.PermissionInfo;

import androidx.annotation.NonNull;

import com.android.certification.niap.permission.dpctester.common.Constants;
import com.android.certification.niap.permission.dpctester.test.log.StaticLogger;
import com.android.certification.niap.permission.dpctester.test.runner.PermissionTestRunner;
import com.android.certification.niap.permission.dpctester.test.runner.PermissionTestRunner.Result;

import com.android.certification.niap.permission.dpctester.test.runner.SignaturePermissionTestModuleBase;
import com.android.certification.niap.permission.dpctester.test.tool.PermissionTestModule;
import com.android.certification.niap.permission.dpctester.test.tool.PermissionTool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/////////////////////////
//Regex for converting

//permission=(.*[A-Z_]{1}),
//permission="$1",

//$1"$2"$3
//$1"$2"$3

//BinderTransaction.getInstance().invoke(
//BinderTransaction.getInstance().invoke(

@PermissionTestModule(name="NonPlatform Permission Test Cases",label = "Run Non Platform Test")
public class NonPlatformTestModule extends SignaturePermissionTestModuleBase {
	public NonPlatformTestModule(@NonNull Activity activity) {
		super(activity);
	}

	private int mCntDRNEP=0;//DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION
	@NonNull
	@Override
	public PrepareInfo prepare(Consumer<PermissionTestRunner.Result> callback) {
		//super.start(callback);
		//safe call
		AtomicInteger finished= new AtomicInteger();

		/*callback?.accept(Result(success,throwable,testCase, finished,root.testSize,bypassed,message))
		if(finished >=root.testSize){
			running = false
		}*/
		PrepareInfo pp = super.prepare(callback);

		List<PermissionInfo> permissions = PermissionTool.Companion.getAllDeclaredPermissions(mContext);

		//Non Framework Signature Permission Test
		Map<String,String> permissionToPackage = new HashMap<>();
		// Ensure that the permission has signature protection level with no other
		// protection flags; the most common seen are privileged and preinstalled.
		permissions.forEach(p -> {
			if(!p.packageName.equals(Constants.PLATFORM_PACKAGE)
				&& p.getProtection() == PermissionInfo.PROTECTION_SIGNATURE
				&& p.getProtectionFlags() == 0)
					permissionToPackage.put(p.name,p.packageName);
        });
		byte[] signatureBytes = mAppSignature.toByteArray();
		List<String> targetPermissions = new ArrayList<>((permissionToPackage.keySet()));
		Map<String,Boolean> packageSignatureMatch = new HashMap<>();

		AtomicInteger cnt = new AtomicInteger(0);
		AtomicInteger err = new AtomicInteger(0);
		final int total = targetPermissions.size();
		//testSize = total;
		//setTestSize(total);
		pp.setCount_tests(total);
		for(String permission:targetPermissions){
			//int finalTestSize = getTestSize();
			Thread thread = new Thread(() -> {
				// Only test those signature permissions that are declared on the device to avoid false
				// positives when the permission is expected to be granted.
				//String tester = this.getClass().getSimpleName();
				//boolean success = false;
				if (!permissionToPackage.containsKey(permission)) {
					logger.debug("Permission " + permission
							+ " is not declared by a non-platform package on this device");
					//success = true;
							//new Result(true, permission, aiIncl(cnt), total,err.get(),tester));
					//continue;
					pp.setCount_passed(pp.getCount_passed()+1);
				} else {
					String packageName = permissionToPackage.get(permission);
					boolean signatureMatch;
					boolean permissionGranted = checkPermissionGranted(permission);
					if (!packageSignatureMatch.containsKey(packageName)) {
						signatureMatch = mPackageManager.hasSigningCertificate(packageName,
								signatureBytes, PackageManager.CERT_INPUT_RAW_X509);
						packageSignatureMatch.put(packageName, signatureMatch);
					} else {
						signatureMatch = Boolean.TRUE.equals(packageSignatureMatch.get(packageName));
					}
					if (permissionGranted != (signatureMatch || isPlatformSignatureMatch)) {
						if(permission.contains("DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION")){
							mCntDRNEP++;
							logger.debug(permission+" is autogenerated permission by androidx. totalcount="+mCntDRNEP);
							//success = true;
							pp.setCount_passed(pp.getCount_passed()+1);
						} else {
							pp.setCount_errors(pp.getCount_errors()+1);
						}
					} else {
						//success = true;
						pp.setCount_passed(pp.getCount_passed()+1);
					}

					//logger.info(permission, permissionGranted, signatureMatch,
					//		isPlatformSignatureMatch);
				}
				finished.getAndIncrement();
				/*callback.accept(
						new Result(success,null,
								new PermissionTestRunner.Data(permission),
								finished.get(), finalTestSize,
								false,""));*/

			});
			thread.start();
            try {
                thread.join(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
		return pp;
	}

	private <T> T systemService(Class<T> clazz){
		return Objects.requireNonNull(getService(clazz),"[npe_system_service]"+clazz.getSimpleName());
	}


}









