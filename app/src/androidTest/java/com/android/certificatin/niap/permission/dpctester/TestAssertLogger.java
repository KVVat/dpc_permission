/*
 * Copyright 2023 The Android Open Source Project
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

package com.android.certificatin.niap.permission.dpctester;

import android.util.Log;

import org.junit.rules.TestName;

public class TestAssertLogger {
    int inc = 0;

    TestName name;
    public TestAssertLogger(TestName name){
        this.name = name;
    }
    public String Msg(String desc){
        inc++;
        String line = name.getMethodName() + "(" + String.format("%03d",inc) +"):"+ desc;
        Log.d("tag",line);

        return line;
    }
}
