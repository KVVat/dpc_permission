package com.android.certification.niap.permission.dpctester.comp;

import android.os.RemoteException;
import androidx.annotation.UiThread;

public interface OnServiceConnectedListener<T> {
  @UiThread
  void onServiceConnected(T service) throws RemoteException;
}
