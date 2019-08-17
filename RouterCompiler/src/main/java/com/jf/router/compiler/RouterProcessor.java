package com.jf.router.compiler;

import com.google.auto.service.AutoService;

import java.io.OutputStream;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;

//自动注册
@AutoService(Processor.class)
//指定处理器处理的目标注解
@SupportedAnnotationTypes("com.jf.router.api.annotation.Router")
public class RouterProcessor extends AbstractProcessor {

    private Filer filer;
    private Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        this.filer = processingEnvironment.getFiler();
        this.messager = processingEnvironment.getMessager();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        generateCode();
        return false;
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
