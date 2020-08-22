package com.huihui.basic.vm;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ConstPool;
import javassist.bytecode.FieldInfo;
import javassist.bytecode.annotation.Annotation;

import java.lang.reflect.Field;


public class JavaassistTest {
    public static void main(String[] args) throws Exception{
        ClassPool classPool = ClassPool.getDefault();
        CtClass ctClass = classPool.getCtClass("com.winphysoft.test.vm.JavaassistAnno");
        for(CtField ctField : ctClass.getDeclaredFields()){
            FieldInfo fieldInfo = ctField.getFieldInfo();
            ConstPool cp = fieldInfo.getConstPool();
            AnnotationsAttribute att = (AnnotationsAttribute) fieldInfo.getAttribute(AnnotationsAttribute.visibleTag);
            AnnotationsAttribute attribute = new AnnotationsAttribute(cp, AnnotationsAttribute.visibleTag);
            for (Annotation ann : att.getAnnotations()){
                if(ann.getTypeName().startsWith("vm")){
                    Annotation annotation = new Annotation("vm.anno.B", cp);
                    attribute.addAnnotation(annotation);
                } else {
                    attribute.addAnnotation(ann);
                }
            }
            fieldInfo.addAttribute(attribute);
        }
        ctClass.toClass();
        Class clz = JavaassistAnno.class;
        Field field = clz.getDeclaredField("ob");
        for (java.lang.annotation.Annotation annotation : field.getAnnotations()){
            System.out.println(annotation);
        }
    }
}
