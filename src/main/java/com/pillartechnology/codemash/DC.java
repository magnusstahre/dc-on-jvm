package com.pillartechnology.codemash;

import org.objectweb.asm.ClassWriter;

import java.io.FileOutputStream;
import java.io.IOException;

import static org.objectweb.asm.Opcodes.*;

public class DC {
    public static void main(String[] args) throws IOException {
        FileOutputStream fos = new FileOutputStream("DC.class");
        try {
            fos.write(dcIfy());
        } finally {
           fos.close();
        }
    }

    private static byte[] dcIfy() {
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        cw.visit(V1_5, ACC_PUBLIC + ACC_SUPER, "DC", null, "java/lang/Object", null);
        cw.visitEnd();
        return cw.toByteArray();
    }
}