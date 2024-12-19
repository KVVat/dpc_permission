/*
 * Copyright 2022 The Android Open Source Project
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

package com.android.certification.niap.permission.dpctester.service;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import com.android.certification.niap.permission.dpctester.MainActivity;
import com.android.certification.niap.permission.dpctester.R;
import com.android.certification.niap.permission.dpctester.common.Constants;
import com.android.certification.niap.permission.dpctester.test.log.Logger;
import com.android.certification.niap.permission.dpctester.test.log.LoggerFactory;


/**
 * Started service that can be used to test permissions that require APIs invoked from a {link
 * Service}.
 */
public class TestService extends Service {
    private static final String TAG = "PermissionTesterService";
    private static final Logger sLogger = LoggerFactory.createDefaultLogger(TAG);

    private static final int NOTIFICATION_ID = 1;
    private final IBinder mBinder = new LocalBinder();
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String permission = intent.getStringExtra(Constants.EXTRA_PERMISSION_NAME);
        boolean permissionGranted = intent.getBooleanExtra(Constants.EXTRA_PERMISSION_GRANTED, false);
        try {
            switch (permission) {
                case Manifest.permission.FOREGROUND_SERVICE: {
                    Intent notificationIntent = new Intent(this, MainActivity.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                            notificationIntent, PendingIntent.FLAG_IMMUTABLE);

                    // Create a NotificationChannel to be used to start the service in the
                    // foreground.
                    CharSequence channelName = getString(R.string.service_channel_name);
                    NotificationChannel channel = new NotificationChannel(TAG, channelName,
                            NotificationManager.IMPORTANCE_DEFAULT);
                    NotificationManager notificationManager = getSystemService(
                            NotificationManager.class);
                    notificationManager.createNotificationChannel(channel);

                    Notification notification =
                            new Notification.Builder(this, TAG)
                                    .setContentTitle(getText(R.string.service_notification_title))
                                    .setContentText(getText(R.string.service_notification_message))
                                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                                    .setContentIntent(pendingIntent)
                                    .build();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        startForeground(NOTIFICATION_ID, notification,flags);
                        //foregroundInfo = new ForegroundInfo(notificationId, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC);
                    } else {
                        startForeground(NOTIFICATION_ID, notification);
                        //foregroundInfo = new ForegroundInfo(notificationId, notification);
                    }

                    stopForeground(true);
                    //StatusLogger.logTestStatus(permission, permissionGranted, true);
                    sLogger.info(permission+ permissionGranted);
                    //getAndLogTestStatus
                    break;
                }
                default:
                    sLogger.error(
                            "Unexpected permission provided to TestService: " + permission);
                    break;
            }
        } catch (SecurityException e) {
            sLogger.debug("Caught a SecurityException invoking the test: ", e);
            sLogger.info(permission+ permissionGranted);//, false);
        } catch (Throwable t) {
            Log.e("TestService>",">"+permission+":"+t.getMessage());
        }
        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class LocalBinder extends Binder {
        TestService getService() {
            return TestService.this;
        }
    }
}
