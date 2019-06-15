package lrm.com.hello_compiler.processors;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

import lrm.com.annotations.HelloWordAtion;

/**
 * 关于javapote 参考https://github.com/square/javapoet
 */
@AutoService(Processor.class)
public class HelloWordProcessor extends AbstractProcessor {

    private Filer mFiler;

    private Elements mElementUtils;

    private Messager mMessager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mElementUtils = processingEnv.getElementUtils();
        mMessager = processingEnv.getMessager();
        mFiler = processingEnv.getFiler();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotations = new LinkedHashSet<>();
        annotations.add(HelloWordAtion.class.getCanonicalName());
        return annotations;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
       // System.out.println("set:" + set);set:[lrm.com.annotations.HelloWordAtion]
        for (Element element : roundEnvironment.getElementsAnnotatedWith(HelloWordAtion.class)) {
            if (!(element instanceof TypeElement)) {
                return false;
            }
            HelloWordAtion elementAnnotation = element.getAnnotation(HelloWordAtion.class);
            String value = elementAnnotation.value();
            String clsName = element.getSimpleName().toString();
            Element enclosingElement = element.getEnclosingElement();
            Name qualifiedName = ((TypeElement) element).getQualifiedName();

//            System.out.println("clsName:" + clsName);
////            System.out.println("value:" + value);
////            System.out.println("enclosingElement:" + enclosingElement.toString());
////            System.out.println("qualifiedName:" + qualifiedName.toString());
//            clsName:HelloActivity
//            value:Hello初探
//            enclosingElement:lrm.com.processorsdemo.activity
//            qualifiedName:lrm.com.processorsdemo.activity.HelloActivity


            // 创建main方法
            MethodSpec main = MethodSpec.methodBuilder("main")
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .returns(void.class)
                    .addParameter(String[].class, "args")
                    .addStatement("$T.out.println($S)", System.class, clsName + "-" + value)
                    .addCode(""
                            + "int total = 0;\n"
                            + "for (int i = 0; i < 10; i++) {\n"
                            + "  total += i;\n"
                            + "}\n")
                    .build();


            TypeSpec helloWorld = TypeSpec.classBuilder("HelloWorld")
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .addMethod(main)
                    .build();

            try {

                JavaFile javaFile = JavaFile.builder("com.lrm.hello_complier", helloWorld)
                        //添加文件注释
                        .addFileComment(" This codes are generated automatically. Do not modify!")
                        .build();

                javaFile.writeTo(mFiler);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return true;
    }

    private void error(String msg, Object... args) {
        mMessager.printMessage(Diagnostic.Kind.ERROR, String.format(msg, args));
    }

    private void info(String msg, Object... args) {
        mMessager.printMessage(Diagnostic.Kind.NOTE, String.format(msg, args));
    }
}
