import com.google.common.base.CaseFormat
import com.google.common.base.CaseFormat.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject

buildscript {
    dependencies {
        classpath(libs.guava.v241jre)
        classpath(libs.groovy.json)
        classpath("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.1")
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle.kts files

    }
}
dependencies {
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
}

fun String.caseFormat(from: CaseFormat, to: CaseFormat): String {
    return from.to(to, this)
}
fun jsonToPlaceHolder(json:JsonElement,version:String,target:String){
    json.jsonObject.toMutableMap().forEach { (category, list) ->
        if(category.equals(version)){
            list.jsonObject.toMutableMap().forEach { (item, list) ->
                if(item.equals(target)){
                    (list as JsonArray).forEach {
                        var permission = it.toString().replace("\"","")
                        var process = permission.replace("MANAGE_DEVICE_POLICY_","")
                        println("""
                            @PermissionTest($permission,34,35)
                            fun test${process.caseFormat(UPPER_UNDERSCORE,UPPER_CAMEL)}(){
                                println("The test for $permission is not implemented yet")
                            }
                        """.trimIndent())
                    }
                }
            }
        }
    }
}
tasks.register("testCamel"){
    doLast {
        //println( "HELLO_WORLD".caseFormat(UPPER_UNDERSCORE,UPPER_CAMEL))
        val input = File("codegen-permissions.json").readText()
        val json = Json.parseToJsonElement(input)
        jsonToPlaceHolder(json,"U","internal")
    }
}