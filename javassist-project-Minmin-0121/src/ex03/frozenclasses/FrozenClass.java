package ex03.frozenclasses;

import java.io.File;
import java.io.IOException;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;

public class FrozenClass {
   static String workDir = System.getProperty("user.dir");
   static String outputDir = workDir + File.separator + "output";
   File folder = new File(outputDir);
   
   public void frozen(ClassPool pool,CtClass c1, CtClass c2, CtClass c3) {
	   if (!folder.exists() && !folder.isDirectory()) {
		   folder.mkdir();
	   }
	   
	   try {
         //ClassPool pool = ClassPool.getDefault();
         //insertClassPath(pool);

         //CtClass ccPoint2 = pool.makeClass("Point2");
  		String superClass, currClass1, currClass2;
  		int x = 0;
  		String[] current = new String[3];
  		String[] names = {c1.getName(),c2.getName(),c3.getName()};
		String tmp = "";
		for (int i=0; i<3; i++) {
			if (names[i].startsWith("Common")) {
				if (names[i].length()>tmp.length()) {
					if (tmp.length()>0) {
						current[x] = tmp;
						x++;
					}
					tmp = names[i];
				}
				else {
					current[x] = names[i];
					x++;
				}				
			}
			else {
				current[x] = names[i];
				x++;
			}
		}
		if (tmp.length()>0) {
			superClass = tmp;
			currClass1 = current[0];
			currClass2 = current[1];
		}
		else {
			superClass = current[0];
			currClass1 = current[1];
			currClass2 = current[2];
		}
		//ClassPool pool = ClassPool.getDefault();
		insertClassPath(pool);
		CtClass sup = pool.get(superClass);
		CtClass ctc1 = pool.get(currClass1);
		CtClass ctc2 = pool.get(currClass2);
		//sup.defrost();
		ctc1.defrost();
		ctc2.defrost();
		ctc1.setSuperclass(sup);
		ctc2.setSuperclass(sup);
		sup.writeFile(outputDir);
        ctc1.writeFile(outputDir);
        ctc2.writeFile(outputDir);
		System.out.println("[DBG] write output to: " + outputDir);
    	      	  
         /*ccPoint2.writeFile(outputDir); // debugWriteFile
         System.out.println("[DBG] write output to: " + outputDir);

         CtClass ccRectangle2 = pool.makeClass("Rectangle2");
         ccRectangle2.writeFile(outputDir);
         System.out.println("[DBG] write output to: " + outputDir);

         ccRectangle2.defrost(); // modifications of the class definition will be permitted.
         ccRectangle2.setSuperclass(ccPoint2);
         ccRectangle2.writeFile(outputDir);
         System.out.println("[DBG] write output to: " + outputDir);
*/
      } catch (NotFoundException | CannotCompileException | IOException e) {
         e.printStackTrace();
      }
   }

   static void insertClassPath(ClassPool pool) throws NotFoundException {
      String strClassPath = outputDir;
      pool.insertClassPath(strClassPath);
      System.out.println("[DBG] insert classpath: " + strClassPath);
   }
}
