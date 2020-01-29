package ex04.toclass;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
//import javassist.CtMethod;
import javassist.NotFoundException;
import util.UtilMenu;


public class ToClass {
   private static final String PKG_NAME = "target" + ".";
   public static void main(String[] args) {

      while (true) {
         UtilMenu.showMenuOptions();
         switch (UtilMenu.getOption()) {
         case 1:
            System.out.println("Enter a class name(e.g., CommonServiceA or CommonComponentB)");
            String[] inputs = UtilMenu.getArguments();
            if (inputs.length == 1) {
            	process(inputs[0], "id", "year");
            	
            }
            else {
            	System.out.println("[WRN] Invalid Input\n");
            }
            
            break;
         default:
            break;
         }
      }
      // process("HelloA", "iA", "jA");
      // System.out.println("------------------------------------------");
      // process("HelloB", "iB", "jB");
      // System.out.println("==========================================");
   }
   
   static void process(String clazz, String field1, String field2) {
	   try {
		   ClassPool cp = ClassPool.getDefault();
		   CtClass cc = cp.get(PKG_NAME + clazz);
		   
		   CtConstructor declaredConstructor = cc.getDeclaredConstructor(new CtClass[0]);
	       String block1 = "{ " //
	             + "System.out.println(\"" + field1 + ": \" + " + field1 + "); }";
	       //System.out.println("[DBG] BLOCK1: " + block1);
	       declaredConstructor.insertAfter(block1);
	           
	       String block2 = "{ " //
		       + "System.out.println(\"" + field2 + ": \" + " + field2 + "); }";
	       //System.out.println("[DBG] BLOCK2: " + block2);
		   declaredConstructor.insertAfter(block2);    
	           
	       Class<?> c = cc.toClass();
	       c.newInstance();   
	           
	   }catch (NotFoundException | CannotCompileException | //
	            InstantiationException | IllegalAccessException e) {
	         System.out.println(e.toString());
	      }
   }
   
   
   
   
   
   
   
   
   
   
   

}
