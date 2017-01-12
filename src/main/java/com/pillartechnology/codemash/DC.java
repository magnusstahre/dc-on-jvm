package com.pillartechnology.codemash;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

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

        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "main", "([Ljava/lang/String;)V", null, null);
        mv.visitCode();
        mv.visitTypeInsn(NEW, "java/util/ArrayDeque");
        mv.visitInsn(DUP);
        mv.visitMethodInsn(INVOKESPECIAL, "java/util/ArrayDeque", "<init>", "()V", false);
        mv.visitVarInsn(ASTORE, 1);

        readStuff(mv);

        mv.visitInsn(RETURN);

        mv.visitMaxs(0, 0);
        mv.visitEnd();

        cw.visitEnd();
        return cw.toByteArray();
    }

    private static void readStuff(MethodVisitor mv) {
        Scanner scanner = new Scanner(System.in);
        try {
            while (scanner.hasNext()) {
                if (scanner.hasNextDouble()) {
                    mv.visitVarInsn(ALOAD, 1);
                    mv.visitLdcInsn(scanner.nextDouble());
                    push(mv);
                } else {
                    String op = scanner.next();

                    switch(op) {
                    case "p":
                        mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
                        mv.visitVarInsn(ALOAD, 1);
                        mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/ArrayDeque", "peek", "()Ljava/lang/Object;", false);
                        mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/Object;)V", false);
                        break;
                    case "+":
                        simplyBinaryOp(mv, DADD);
                        break;
                    case "-":
                        simplyBinaryOp(mv, DSUB);
                        break;
                    case "*":
                        simplyBinaryOp(mv, DMUL);
                        break;
                    case "/":
                        simplyBinaryOp(mv, DDIV);
                        break;
                    case "^":
                        pop2(mv);
                        mv.visitMethodInsn(INVOKESTATIC, "java/lang/Math", "pow", "(DD)D", false);
                        push(mv);
                        break;
                    }
                }
            }
        } finally {
            scanner.close();
        }
    }

    private static void simplyBinaryOp(MethodVisitor mv, int op) {
        pop2(mv);
        mv.visitInsn(op);
        push(mv);
    }

    private static void push(MethodVisitor mv) {
        mv.visitMethodInsn(INVOKESTATIC, "java/lang/Double", "valueOf", "(D)Ljava/lang/Double;", false);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/ArrayDeque", "push", "(Ljava/lang/Object;)V", false);
    }

    private static void pop2(MethodVisitor mv) {
        mv.visitVarInsn(ALOAD, 1);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/ArrayDeque", "pop", "()Ljava/lang/Object;", false);
        mv.visitTypeInsn(CHECKCAST, "java/lang/Double");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Double", "doubleValue", "()D", false);
        mv.visitVarInsn(DSTORE, 2);
        mv.visitVarInsn(ALOAD, 1);
        mv.visitVarInsn(ALOAD, 1);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/ArrayDeque", "pop", "()Ljava/lang/Object;", false);
        mv.visitTypeInsn(CHECKCAST, "java/lang/Double");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Double", "doubleValue", "()D", false);
        mv.visitVarInsn(DLOAD, 2);
    }
}