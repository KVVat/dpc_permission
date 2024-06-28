package com.android.certification.niap.permission.dpctester.comp;

import android.os.IBinder;

public interface ServiceInterfaceConverter<T> {
  T convert(IBinder iBinder);
}
