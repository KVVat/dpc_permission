package com.android.certification.niap.permission.dpctester.test.exception

class UnexpectedTestFailureException(t: Throwable): RuntimeException(t){
    constructor(s:String):this(Exception(s))
}