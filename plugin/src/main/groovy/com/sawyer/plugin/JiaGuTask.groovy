package com.sawyer.plugin

import com.android.builder.model.SigningConfig
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class JiaGuTask extends DefaultTask{

    JiaGu jiaGu
    SigningConfig signingConfig
    File apkFile

    JiaGuTask() {
        //在右侧Gradle窗口中创建新的分组任务，不然此任务只能出现在other中
        group = "jiagu"
    }

    //添加此注解后，就可以在点击Gradle中的Task后，执行此方法
    @TaskAction
    def runTask(){
        //groovy中调用命令行工具
        project.exec{
            print(jiaGu.toString())
            //执行登录命令: java -jar jiagu.jar -login username password
            it.commandLine("java","-jar",jiaGu.jiaGuTools,"-login",jiaGu.userName,jiaGu.password)
        }

        if (signingConfig) {
            project.exec {
                //执行添加签名配置
                it.commandLine("java", "-jar", jiaGu.jiaGuTools, "-importsign",
                        signingConfig.storeFile.absolutePath, signingConfig.storePassword,
                        signingConfig.keyAlias, signingConfig.keyPassword)
            }
        }

        project.exec{
            //启动APK加固任务
            it.commandLine("java","-jar",jiaGu.jiaGuTools,"-jiagu",
                    apkFile.absolutePath,apkFile.parent,"-autosign")
        }
    }

}
