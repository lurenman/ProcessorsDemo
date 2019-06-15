package lrm.com.bindview_compiler;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;

import lrm.com.annotations.BindView;
import lrm.com.bindview_compiler.entity.AnnotationInfoEntity;

/**
 * 创建日期：2019/6/15
 * 作者:baiyang
 * bindview 小练习
 */
@AutoService(Processor.class)
public class BindViewProcessors extends AbstractProcessor {
    private Filer mFiler;
    Elements mElementUtils;
    //用来标记注解所属类 的map key=“类名” value="注解实体信息'
    Map<String, AnnotationInfoEntity> AnnotationInfoEntityMap = new HashMap<>();

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotataions = new LinkedHashSet<>();
        annotataions.add(BindView.class.getCanonicalName());
        return annotataions;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    /**
     * 通过这个初始化可以拿到一些称工具类
     *
     * @param processingEnvironment
     */
    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mFiler = processingEnvironment.getFiler();
        mElementUtils = processingEnvironment.getElementUtils();
    }

    /**
     * 核心方法，真正的去做一些注解核验及java代码生成的操作
     *
     * @param set
     * @param roundEnvironment
     * @return
     */
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        AnnotationInfoEntityMap.clear();
        for (Element element : roundEnvironment.getElementsAnnotatedWith(BindView.class)) {
            //如果不是标记在变量上的注解就不处理
            if (!(element instanceof VariableElement)) {
                return false;
            }
            VariableElement variableElement = (VariableElement) element;
            /*获取类信息 也就是获取到BindViewActivity类信息*/
            TypeElement typeElement = (TypeElement) variableElement.getEnclosingElement();
            /*类的绝对路径*/
            String qualifiedName = typeElement.getQualifiedName().toString();
            /*类名*/
            String clsName = typeElement.getSimpleName().toString();
            /*获取包名*/
            String packageName = mElementUtils.getPackageOf(typeElement).getQualifiedName().toString();
            System.out.println("qualifiedName :" + qualifiedName);
            System.out.println("clsName :" + clsName);
            System.out.println("packageName :" + packageName);
//            qualifiedName :lrm.com.processorsdemo.activity.BindViewActivity
//            clsName :BindViewActivity
//            packageName :lrm.com.processorsdemo.activity

            BindView annotation = variableElement.getAnnotation(BindView.class);
            int id = annotation.value();
            //拿到当前注解所属类的 实体信息
            AnnotationInfoEntity infoEntity = AnnotationInfoEntityMap.get(qualifiedName);
            if (infoEntity == null) {
                infoEntity = new AnnotationInfoEntity(packageName, clsName);
                //设置当前类实体信息中map 相关variableElement
                infoEntity.getVarMap().put(id, variableElement);
                //保存当前类 map
                AnnotationInfoEntityMap.put(qualifiedName, infoEntity);
            } else {
                //设置当前类实体信息中map 相关variableElement
                infoEntity.getVarMap().put(id, variableElement);
            }
        }
        //接下来的操作 就是根据这些注解实体信息 生成java文件
        generateJavaFile();
        return true;
    }

    /**
     * 生成java文件
     */
    private void generateJavaFile() {
        for (String key : AnnotationInfoEntityMap.keySet()) {
            ClassName InterfaceName = ClassName.bestGuess("lrm.com.api.ViewInjector");
            //拿到类型名字
            ClassName host = ClassName.bestGuess(key);
            AnnotationInfoEntity infoEntity = AnnotationInfoEntityMap.get(key);

            StringBuilder builder = new StringBuilder();
            builder.append(" if(object instanceof android.app.Activity){\n");
            builder.append(code(infoEntity.getVarMap(), "android.app.Activity"));
            builder.append("}\n");
            builder.append("else{\n");
            builder.append(code(infoEntity.getVarMap(), "android.view.View"));
            builder.append("}\n");

            MethodSpec inject = MethodSpec.methodBuilder("inject")
                    .addModifiers(Modifier.PUBLIC)
                    .returns(void.class)
                    .addAnnotation(Override.class)
                    .addParameter(host, "host")
                    .addParameter(Object.class, "object")
                    .addCode(builder.toString())
                    .build();

            TypeSpec typeSpec = TypeSpec.classBuilder(infoEntity.getClsName() + "ViewInjector")
                    .addModifiers(Modifier.PUBLIC)
                    .addMethod(inject)
                    .addSuperinterface(ParameterizedTypeName.get(InterfaceName, host))
                    .build();

            try {
                JavaFile javaFile = JavaFile.builder(infoEntity.getPk(), typeSpec)
                        .addFileComment(" This codes are generated automatically. Do not modify!")
                        .build();
                javaFile.writeTo(mFiler);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("e--->" + e.getMessage());
            }
        }
    }

    /**
     * 根据注解对象生成code方法体
     *
     * @param map
     * @param pk
     * @return
     */
    private String code(Map<Integer, VariableElement> map, String pk) {
        StringBuilder builder = new StringBuilder();
        for (Integer id : map.keySet()) {
            VariableElement variableElement = map.get(id);
            String name = variableElement.getSimpleName().toString();
            String type = variableElement.asType().toString();//变量类型

            builder.append("host." + name + " = (" + type + ")(((" + pk + ")object).findViewById(" + id + "));\n");
        }
        return builder.toString();
    }
}
