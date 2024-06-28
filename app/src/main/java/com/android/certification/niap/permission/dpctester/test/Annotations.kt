package com.android.certification.niap.permission.dpctester.test

annotation class PermissionTestSuite(
    val name:String="title"
)

annotation class PermissionTestModule(
    val name:String="title"
)

annotation class PermissionTest(
    val permission:String="title",
    val sdkMin:Int=0,
    val sdkMax:Int=100000,
    val priority:Int=-1,
    val block:Boolean=false)