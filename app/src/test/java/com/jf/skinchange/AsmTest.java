package com.jf.skinchange;

import org.junit.Test;
import org.ow2.util.asm.ClassReader;
import org.ow2.util.asm.ClassWriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class AsmTest {

    @Test
    public void test(){
        try {
            FileInputStream fis = new FileInputStream(new File("src/test/java/com/jf/skinchange/InAsmTarget.class"));
            ClassReader cr = new ClassReader(fis);
            ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
            //cr.accept();

            byte[] bytes = cw.toByteArray();
            FileOutputStream fos = new FileOutputStream("src/test/java/com/jf/skinchange/InAsmTarget2.class");
            fos.write(bytes);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
