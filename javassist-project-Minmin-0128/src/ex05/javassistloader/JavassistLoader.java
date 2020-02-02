package ex05.javassistloader;

import java.io.File;
//import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

//import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Loader;
import javassist.NotFoundException;
import util.UtilMenu;

public class JavassistLoader {
   private static final String WORK_DIR = System.getProperty("user.dir");
   //private static final String TARGET_POINT = "target.Point";
   //private static final String TARGET_RECTANGLE = "target.Rectangle";
   //private static final String TARGET_CIRCLE = "target.Circle";

   public static void main(String[] args) {
	  List<String> modifiedmethod = new ArrayList<>();

      while (true) {
         UtilMenu.showMenuOptions1();
         String[] inputname = UtilMenu.getInputs();
         String[] sortClass = findrelationship(inputname);
         if(sortClass != null) {
        	 UtilMenu.showMenuOptions2();
        	 String[] inputmethod = UtilMenu.getInputs();
        	 if (!modifiedmethod.contains(inputmethod[0])) {
            	 if (inputmethod.length == 3) {
            		 
            		 try {
            	         ClassPool cp = ClassPool.getDefault();
            	         insertClassPath(cp);
            	         CtClass ctc1 = cp.get("target." + sortClass[1]);
            	         CtClass ctc2 = cp.get("target." + sortClass[2]);
            	         ctc1.setSuperclass(cp.get("target." + sortClass[0]));
            	         ctc2.setSuperclass(cp.get("target." + sortClass[0]));
            	         analysisProcess(cp,ctc1,inputmethod);
            	         analysisProcess(cp,ctc2,inputmethod);
            	         modifiedmethod.add(inputmethod[0]);
            	         ctc1.defrost();
            	         ctc2.defrost();

            		 } catch (Exception e) {
            	         e.printStackTrace();
            	 }
            			 
            	 }
            	 else {
            		 System.out.print("[WRN] Invalid Input size!!\n"); 
        	      }
    		      		 
        	 }
        	 else {
        		 System.out.print("[WRN] This method " + inputmethod[0] + " has been modified!!\n");
        	 }
         }
         else {
        	 System.out.print("[WRN] Invalid Input size!!\n");
         }
      }
   }
   
   static String[] findrelationship(String[] inputname) {
	   if (inputname.length == 3) {
		   
		   String[] result = new String[3];
	  		//String superClass, currClass1, currClass2;
	  		int x = 0;
	  		String[] current = new String[3];
	  		//String[] names = {c1.getName(),c2.getName(),c3.getName()};
			String tmp = "";
			for (int i=0; i<3; i++) {
				if (inputname[i].startsWith("Common")) {
					if (inputname[i].length()>tmp.length()) {
						if (tmp.length()>0) {
							current[x] = tmp;
							x++;
						}
						tmp = inputname[i];
					}
					else {
						current[x] = inputname[i];
						x++;
					}				
				}
				else {
					current[x] = inputname[i];
					x++;
				}
			}
			if (tmp.length()>0) {
				result[0] = tmp;
				result[1] = current[0];
				result[2] = current[1];
			}
			else {
				result[0] = current[0];
				result[1] = current[1];
				result[2] = current[2];
			}
			return result;

				   
	   }
		else {
			
			return null;
		}
   }
   

   
   static void analysisProcess(ClassPool cp, CtClass cc, String[] inputmethod) {
      try {
         CtMethod m1 = cc.getDeclaredMethod(inputmethod[0]);
         String methodcall = " + " + inputmethod[2] + "()";
         System.out.println();
         String BLK1 = "\n{\n" //
               + "\t" + inputmethod[1] + "();" + "\n" //
               + "\t" + "System.out.println(\"[TR]" + inputmethod[2] + " result : \"" + methodcall + "); " + "\n" + "}";
         System.out.println("[DBG] Block: " + BLK1);
         m1.insertBefore(BLK1);

         Loader cl = new Loader(cp);
         Class<?> c = cl.loadClass(cc.getName());
         Object rect = c.newInstance();
         System.out.println("[DBG] Created a " + cc.getName() + " object.");

         Class<?> rectClass = rect.getClass();
         Method m = rectClass.getDeclaredMethod(inputmethod[0], new Class[] {});
         System.out.println("[DBG] Called getDeclaredMethod.");
         Object invoker = m.invoke(rect, new Object[] {});
         System.out.println("[DBG] " + inputmethod[0] + " return result: " + invoker);
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   static void insertClassPath(ClassPool pool) throws NotFoundException {
      String strClassPath = WORK_DIR + File.separator + "classfiles";
      pool.insertClassPath(strClassPath);
      System.out.println("[DBG] insert classpath: " + strClassPath);
   }
}
