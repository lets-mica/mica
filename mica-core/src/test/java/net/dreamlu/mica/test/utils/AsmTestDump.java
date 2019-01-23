package net.dreamlu.mica.test.utils;

import org.springframework.asm.*;

public class AsmTestDump implements Opcodes {

	public static byte[] dump() throws Exception {
		ClassWriter cw = new ClassWriter(0);
		MethodVisitor mv;

		cw.visit(V1_8, ACC_PUBLIC + ACC_SUPER, "net/dreamlu/mica/test/utils/AsmTest", null, "java/lang/Object", null);

		{
			mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
			mv.visitCode();
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
			mv.visitInsn(RETURN);
			mv.visitMaxs(1, 1);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC, "copy", "(Lnet/dreamlu/mica/test/utils/User;Ljava/util/Map;Lorg/springframework/core/convert/converter/Converter;)V", "(Lnet/dreamlu/mica/test/utils/User;Ljava/util/Map<Ljava/lang/String;Ljava/lang/Object;>;Lorg/springframework/core/convert/converter/Converter;)V", null);
			mv.visitCode();
			mv.visitVarInsn(ALOAD, 2);
			mv.visitLdcInsn("id");
			mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Map", "get", "(Ljava/lang/Object;)Ljava/lang/Object;", true);
			mv.visitVarInsn(ASTORE, 4);
			mv.visitVarInsn(ALOAD, 4);
			//=== 备忘，主要看这部分代码 ===//
			Label l0 = new Label();
			mv.visitJumpInsn(IFNULL, l0);
			mv.visitLdcInsn(Type.getType("Ljava/lang/Integer;"));
			mv.visitVarInsn(ALOAD, 4);
			mv.visitMethodInsn(INVOKESTATIC, "net/dreamlu/mica/core/utils/ClassUtil", "isAssignableValue", "(Ljava/lang/Class;Ljava/lang/Object;)Z", false);
			Label l1 = new Label();
			mv.visitJumpInsn(IFEQ, l1);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitVarInsn(ALOAD, 4);
			mv.visitTypeInsn(CHECKCAST, "java/lang/Integer");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/dreamlu/mica/test/utils/User", "setId", "(Ljava/lang/Integer;)V", false);
			mv.visitJumpInsn(GOTO, l0);
			mv.visitLabel(l1);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitVarInsn(ALOAD, 3);
			mv.visitVarInsn(ALOAD, 4);
			mv.visitMethodInsn(INVOKEINTERFACE, "org/springframework/core/convert/converter/Converter", "convert", "(Ljava/lang/Object;)Ljava/lang/Object;", true);
			mv.visitTypeInsn(CHECKCAST, "java/lang/Integer");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/dreamlu/mica/test/utils/User", "setId", "(Ljava/lang/Integer;)V", false);
			mv.visitLabel(l0);
			//=== 备忘，主要看这部分代码 ===//
			mv.visitInsn(RETURN);
			mv.visitMaxs(3, 5);
			mv.visitEnd();
		}
		cw.visitEnd();

		return cw.toByteArray();
	}
}
