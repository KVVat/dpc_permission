package com.android.certification.niap.permission.dpctester.test.tool

import kotlin.reflect.KClass

annotation class PermissionTestSuite(
    val name:String="title",
    val label:String="suite label",
    val details:String="details"
)

annotation class PermissionTestModule(
    val name:String="title",
    val label:String="module label",
    val priority:Int=0,
    val sync:Boolean=false,
)

annotation class PermissionTest(
    val permission:String="title",
    val sdkMin:Int=0,
    val sdkMax:Int=100000,
    val priority:Int=-1,
    val customCase:Boolean=false,
    val requiredServices:Array<KClass<out Any>> =[],
    val requiredPermissions:Array<String> =[],

    )