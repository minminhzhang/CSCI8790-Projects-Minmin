package newfield;

import java.io.File;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.NotFoundException;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.AnnotationImpl;
import javassist.bytecode.annotation.MemberValue;
import util.UtilMenu;

public class AnnotatedFieldExample0218 {
   static String workDir = System.getProperty("user.dir");
   static String inputDir = workDir + File.separator + "classfiles";
   static String outputDir = workDir + File.separator + "output";
   static ClassPool pool;
   static final String PAK = "target.";

   
   public static void main(String[] args) {
	   while(true) {
	      try {
	      	 UtilMenu.showMenuOptions();
	      	 String[] input = UtilMenu.getArguments();
	      	 if (input.length == 3) {
	               ClassPool pool = ClassPool.getDefault();
	               pool.insertClassPath(inputDir);
	               
	               CtClass ct = pool.get(PAK + input[0]);
	               
	               CtField[] cf = ct.getFields();

	               //process(ct.getAnnotations());
	               System.out.println();
	               
	               for (int i = 0; i < cf.length; i++) {
	                   process(cf[i].getAnnotations(),input[1],input[2]);
	               }
	      	 }
	      	 else {
	      		 System.out.println("[WRN] Invalid input size!!");
	      	 }

	        } catch (NotFoundException | ClassNotFoundException e) {
	           e.printStackTrace();
	        }   
	   }

   }

   static void process(Object[] annoList, String firstanno, String secondanno) {
/*      final String TARGET_TABLE = "target.Table";
      final String TARGET_COLUMN = "target.Column";
      final String TARGET_AUTHOR = "target.Author";
      final String TARGET_Row = "target.Row";*/
      final String PAK = "target.";

      Annotation anno2 = null;

      for (int i = 0; i < annoList.length; i++) {
         Annotation annotation = getAnnotation(annoList[i]);
         if (annotation.getTypeName().equals(PAK + firstanno)) {
        	 for (int j = 0; j < annoList.length; j++) {
        		 Annotation annotmp = getAnnotation(annoList[j]);
                 if (annotmp.getTypeName().equals(PAK + secondanno)) {
                	 anno2 = annotmp;
                 }
        		 
        	 }
             
          }
/*         else if (annotation.getTypeName().equals(TARGET_COLUMN)) {
            showAnnotation(annotation);
         }
         else if (annotation.getTypeName().equals(TARGET_AUTHOR)) {
            showAnnotation(annotation);
         }
         else if (annotation.getTypeName().equals(TARGET_Row)) {
             showAnnotation(annotation);
          }*/
      }
      if (!(anno2 == null)) {
     	 showAnnotation(anno2);
      }
   }

   static Annotation getAnnotation(Object obj) {
      // Get the underlying type of a proxy object in java
      AnnotationImpl annotationImpl = //
            (AnnotationImpl) Proxy.getInvocationHandler(obj);
      return annotationImpl.getAnnotation();
   }

   static void showAnnotation(Annotation annotation) {
      Iterator<?> iterator = annotation.getMemberNames().iterator();
      List<String> outp = new ArrayList<>();
      while (iterator.hasNext()) {
         Object keyObj = (Object) iterator.next();
         MemberValue value = annotation.getMemberValue(keyObj.toString());
         String tmp = keyObj.toString() + ": " + value;
         outp.add(tmp);

      }
      System.out.println(outp.get(0) + ", " + outp.get(1));
   }
}
