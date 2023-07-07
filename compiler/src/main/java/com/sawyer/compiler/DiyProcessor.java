package com.sawyer.compiler;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Completion;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

//在resources/META-INF/services/javax.annotation.processing.Processor文件中进行注册
//类比于Activity需要在AndroidManifest中注册一样

//方式二：允许此注解处理器处理的直接
@SupportedAnnotationTypes({"com.sawyer.annotation.SawyerAnnotation"})
public class DiyProcessor extends AbstractProcessor{

    /**
     * 在javac进行编译时，会调用此方法，我们就可以做一些想要做的事
     * @param annotations
     * @param roundEnv
     * @return
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        //在build中打印日志
        Messager messager = processingEnv.getMessager();
        messager.printMessage(Diagnostic.Kind.NOTE,"====================>");
        return false;
    }

    /**
     * 方式一：允许此注解处理器处理的直接
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return super.getSupportedAnnotationTypes();
    }
}