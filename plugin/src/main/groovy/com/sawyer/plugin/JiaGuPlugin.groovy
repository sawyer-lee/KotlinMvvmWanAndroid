package com.sawyer.plugin

import com.android.build.gradle.AppExtension
import com.android.build.gradle.api.ApplicationVariant
import com.android.build.gradle.api.BaseVariantOutput
import com.android.builder.model.SigningConfig
import org.gradle.api.Plugin
import org.gradle.api.Project

//Plugin为gradleApi中提供的Java源文件
class JiaGuPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        //创建可以在app/build.gradle中使用的扩展
        JiaGu jiaGu = project.extensions.create("jiagu",JiaGu)
        //在解析完app/build.gradle之后，会进行回调
        project.afterEvaluate {
            //获取AGP_Extension,即 com.android.application这个插件
            AppExtension android = project.extensions.android
            //获取应用的变体信息(即app/build.gradle中的配置信息)
            android.applicationVariants.all {ApplicationVariant variant ->
                //获取对应变体(debug/release)的签名配置信息
                SigningConfig signingConfig = variant.signingConfig

                variant.outputs.all { BaseVariantOutput variantOutput ->
                    //获取输出的apk文件
                    File apkFile = variantOutput.outputFile

                    //创建加固任务Task,即创建jiaGuDebug/jiaGuRelease
                    JiaGuTask jiaGuTask = project.tasks.create(
                            "jiagu${variant.baseName.capitalize()}",JiaGuTask)

                    jiaGuTask.jiaGu = jiaGu
                    jiaGuTask.signingConfig = signingConfig
                    jiaGuTask.apkFile = apkFile
                }
            }
        }
    }
}