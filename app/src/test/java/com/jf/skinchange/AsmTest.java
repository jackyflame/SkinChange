package com.jf.skinchange;

import org.junit.Test;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;
import org.objectweb.asm.commons.Method;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class AsmTest {

    @Test
    public void test(){
        try {
            FileInputStream fis = new FileInputStream(new File("src/test/java/com/jf/skinchange/target/InAsmTarget.class"));
            ClassReader cr = new ClassReader(fis);
            ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

            cr.accept(new MyClassVisitor(Opcodes.ASM7,cw),ClassReader.EXPAND_FRAMES);

            byte[] bytes = cw.toByteArray();
            FileOutputStream fos = new FileOutputStream("src/test/java/com/jf/skinchange/target/InAsmTarget2.class");
            fos.write(bytes);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class MyClassVisitor extends ClassVisitor {

        private int api;

        public MyClassVisitor(int api, ClassVisitor classVisitor) {
            super(api, classVisitor);
            this.api = api;
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
            MethodVisitor superMv = super.visitMethod(access, name, descriptor, signature, exceptions);
            return new MyMethodVisitor(api, superMv, access,name, descriptor);
        }
    }

    static class MyMethodVisitor extends AdviceAdapter {

        /**
         * Constructs a new {@link AdviceAdapter}.
         *
         * @param api           the ASM API version implemented by this visitor. Must be one of {@link
         *                      Opcodes#ASM4}, {@link Opcodes#ASM5}, {@link Opcodes#ASM6} or {@link Opcodes#ASM7}.
         * @param methodVisitor the method visitor to which this adapter delegates calls.
         * @param access        the method's access flags (see {@link Opcodes}).
         * @param name          the method's name.
         * @param descriptor    the method's descriptor (see {@link Type Type}).
         */
        protected MyMethodVisitor(int api, MethodVisitor methodVisitor, int access, String name, String descriptor) {
            super(api, methodVisitor, access, name, descriptor);
        }

        private boolean isNeedInject = false;

        @Override
        public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
            System.out.println(getName()+":" + descriptor);
            if("Lcom/jf/skinchange/target/AMSTest;".equals(descriptor)){
                isNeedInject = true;
            }else{
                isNeedInject = false;
            }
            return super.visitAnnotation(descriptor, visible);
        }

        int s;

        @Override
        protected void onMethodEnter() {
            super.onMethodEnter();
            if(!isNeedInject){
                return;
            }
            //创建System.currentTimeMillis()
            invokeStatic(Type.getType("Ljava/lang/System;"),new Method("currentTimeMillis","()J"));
            //创建本地变量索引
            s = newLocal(Type.LONG_TYPE);
            //将上一步执行结果保存到本地变量中
            storeLocal(s);
        }

        @Override
        protected void onMethodExit(int opcode) {
            super.onMethodExit(opcode);

            if(!isNeedInject){
                return;
            }

            //创建System.currentTimeMillis()
            invokeStatic(Type.getType("Ljava/lang/System;"),new Method("currentTimeMillis","()J"));
            //创建本地变量索引
            int e = newLocal(Type.LONG_TYPE);
            //将上一步执行结果保存到本地变量中
            storeLocal(e);

            getStatic(Type.getType("Ljava/lang/System;"),"out", Type.getType("Ljava/io/PrintStream;"));
            newInstance(Type.getType("Ljava/lang/StringBuilder;"));
            dup();
            invokeConstructor(Type.getType("Ljava/lang/StringBuilder;"),new Method("<init>","()V"));
            visitLdcInsn("execute:");
            invokeVirtual(Type.getType("Ljava/lang/StringBuilder;"),new Method("append","(Ljava/lang/String;)Ljava/lang/StringBuilder;"));
            loadLocal(s);
            loadLocal(e);
            math(SUB,Type.LONG_TYPE);

            invokeVirtual(Type.getType("Ljava/lang/StringBuilder;"),new Method("append","(J)Ljava/lang/StringBuilder;"));
            visitLdcInsn("ms");

            invokeVirtual(Type.getType("Ljava/lang/StringBuilder;"),new Method("append","(Ljava/lang/String;)Ljava/lang/StringBuilder;"));
            invokeVirtual(Type.getType("Ljava/lang/StringBuilder;"),new Method("toString","()Ljava/lang/String;"));
            invokeVirtual(Type.getType("Ljava/lang/PrintStream;"),new Method("println","(Ljava/lang/String;)V"));


        }
    }

}
