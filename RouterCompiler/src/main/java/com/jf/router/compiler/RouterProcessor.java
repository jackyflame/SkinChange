package com.jf.router.compiler;

import com.google.auto.service.AutoService;
import com.jf.router.annotation.Router;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.OutputStream;
import java.util.Set;
import java.util.function.Consumer;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.QualifiedNameable;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

//自动注册
@AutoService(Processor.class)
//指定JDK版本
@SupportedSourceVersion(SourceVersion.RELEASE_7)
//指定处理器处理的目标注解
@SupportedAnnotationTypes("com.jf.router.annotation.Router")
public class RouterProcessor extends AbstractProcessor {

    private Filer filer;//文件相关的辅助类
    private Messager messager;//日志相关的辅助类
    private Elements elements; //元素相关的辅助类
    private Types types;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        this.filer = processingEnvironment.getFiler();
        this.messager = processingEnvironment.getMessager();
        this.elements = processingEnvironment.getElementUtils();
        this.types = processingEnvironment.getTypeUtils();
        messager.printMessage(Diagnostic.Kind.NOTE,"-------------------RouterPrDexFile find classocessor init ------------------------");
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        messager.printMessage(Diagnostic.Kind.NOTE,"---- createSourceFile process >> ----");
        scanToCreateClass(roundEnvironment);
        //generateCode();
        return false;
    }

    private void scanToCreateClass(RoundEnvironment roundEnvironment){
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(Router.class);
        if(elements == null || elements.isEmpty()){
            messager.printMessage(Diagnostic.Kind.WARNING,"scanToCreateClass failed cause Router annotation is empty!");
            return;
        }
        elements.forEach(new Consumer<Element>() {
            @Override
            public void accept(Element element) {
                if(element instanceof TypeElement){
                    generateClass((TypeElement) element);
                }else{
                    messager.printMessage(Diagnostic.Kind.WARNING,"Element forEach is not TypeElement:"+element.getSimpleName());
                }
            }
        });
    }

    private void generateClass(TypeElement element){
        //如果不是Activity子类，则跳过
        //TypeElement declaredType = elements.getTypeElement("android.support.v7.app.AppCompatActivity");
        TypeElement declaredType = elements.getTypeElement("android.app.Activity");
        if(!types.isSubtype(element.asType(),declaredType.asType())){
            messager.printMessage(Diagnostic.Kind.NOTE,"generateClass jump: "+element.getSimpleName() + " is not extends from Activity");
            return;
        }
        //get the package info
        QualifiedNameable qualifiedElement = (QualifiedNameable) element.getEnclosingElement();
        //PackageElement packageElement;
        //if(innerElment instanceof TypeElement){
        //    packageElement = (PackageElement) innerElment.getEnclosingElement();
        //}else{
        //    packageElement = (PackageElement) innerElment;
        //}
        messager.printMessage(Diagnostic.Kind.NOTE,"full-path:"+element.getQualifiedName());
        String routerValue = element.getAnnotation(Router.class).value();
        //messager.printMessage(Diagnostic.Kind.NOTE,"element getAnnotation value:"+element.getAnnotation(Router.class).value());
        //get the class by full rout info
        ClassName elementActivity = ClassName.get(qualifiedElement.getQualifiedName().toString(),element.getSimpleName().toString());
        //override annotation declare
        ClassName override = ClassName.get("java.lang", "Override");
        //build the interface
        ClassName iRouterRegister = ClassName.get("com.jf.router.api.interfaces","IRouterRegister");
        //setting the target class
        TypeSpec.Builder routerAuto = TypeSpec.classBuilder("Router2_"+element.getSimpleName())
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(iRouterRegister);
        //combine the target class member method
        ClassName onRegistParamType = ClassName.get("com.jf.router.api","RouterManager");
        ParameterSpec registParam = ParameterSpec.builder(onRegistParamType,"manager").build();
        MethodSpec onRegistMehtod = MethodSpec.methodBuilder("onRegist")
                .addParameter(registParam)
                .addAnnotation(override)
                .addModifiers(Modifier.PUBLIC)
                .beginControlFlow("if(manager != null)")
                .addCode("manager.registRoute($S, $T.class);\n",routerValue,elementActivity)
                .endControlFlow()
                .build();

        routerAuto.addMethod(onRegistMehtod);
        JavaFile javaFile = JavaFile.builder("com.jf.router.register",routerAuto.build()).build();
        try{
            javaFile.writeTo(filer);
        }catch (Exception e){
            e.printStackTrace();
        }
        messager.printMessage(Diagnostic.Kind.NOTE,"generateClass "+element.getSimpleName()+" with rout "+ routerValue);
    }

    private void generateCode(){
        StringBuilder sb = new StringBuilder();
        sb.append("package com.jf.router.register;\n").append(
                "\n" ).append(
                "import com.jf.modela.AHomeActivity;\n" ).append(
                "import com.jf.router.api.RouterManager;\n" ).append(
                "import com.jf.router.api.interfaces.IRouterRegister;\n" ).append(
                "\n" ).append(
                "public class Router_AHomeActivity implements IRouterRegister {\n" ).append(
                "\n" ).append(
                "    @Override\n" ).append(
                "    public void onRegist(RouterManager manager) {\n" ).append(
                "        if(manager != null){\n" ).append(
                "            manager.registRoute(\"/a/home\", AHomeActivity.class);\n" ).append(
                "        }\n" ).append(
                "    }\n" ).append(
                "}");
        try{
            messager.printMessage(Diagnostic.Kind.NOTE,"-------------------createSourceFile------------------------");
            JavaFileObject fileObject = filer.createSourceFile("com.jf.router.register.Router_AHomeActivity");
            OutputStream outputStream = fileObject.openOutputStream();
            outputStream.write(sb.toString().getBytes());
            outputStream.flush();
            outputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
