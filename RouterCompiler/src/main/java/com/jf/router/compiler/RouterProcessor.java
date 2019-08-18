package com.jf.router.compiler;

import com.google.auto.service.AutoService;
import com.jf.router.annotation.Router;

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
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

//自动注册
@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_7) //java
//指定处理器处理的目标注解
@SupportedAnnotationTypes("com.jf.router.annotation.Router")
public class RouterProcessor extends AbstractProcessor {

    private Filer filer;//文件相关的辅助类
    private Messager messager;//日志相关的辅助类
    public Elements elements; //元素相关的辅助类

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        this.filer = processingEnvironment.getFiler();
        this.messager = processingEnvironment.getMessager();
        this.elements = processingEnvironment.getElementUtils();
        messager.printMessage(Diagnostic.Kind.NOTE,"-------------------RouterProcessor init ------------------------");
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        messager.printMessage(Diagnostic.Kind.NOTE,"-------------------createSourceFile process------------------------");
        scanToCreateClass(roundEnvironment);
        return false;
    }

    private void scanToCreateClass(RoundEnvironment roundEnvironment){
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(Router.class);
        if(elements == null || elements.isEmpty()){
            messager.printMessage(Diagnostic.Kind.ERROR,"scanToCreateClass failed cause Router annotation is empty!");
            return;
        }
        elements.forEach(new Consumer<Element>() {
            @Override
            public void accept(Element element) {
                generateClass(element);
            }
        });
    }

    private void generateClass(Element element){


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
