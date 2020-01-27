package setSuper;
import java.io.File;
import java.io.IOException;

import javassist.CannotCompileException;
//import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
//import target.Rectangle;


public class setSuperClass {
	static final String SEP = File.separator;
	static String workDir = System.getProperty("user.dir");
	static String outputDir = workDir + SEP + "output";
	
	public static void main(String[] args) {
		String superClass, currClass1, currClass2;
		int x = 0;
		String[] current = new String[3];
		if (args.length == 3) {
			try {
				String tmp = "";
				for (int i=0; i<3; i++) {
					if (args[i].startsWith("Common")) {
						if (args[i].length()>tmp.length()) {
							if (tmp.length()>0) {
								current[x] = tmp;
								x++;
							}
							tmp = args[i];
						}
						else {
							current[x] = args[i];
							x++;
						}				
					}
					else {
						current[x] = args[i];
						x++;
					}
				}
				if (tmp.length()>0) {
					superClass = "target." + tmp;
					currClass1 = "target." + current[0];
					currClass2 = "target." + current[1];
				}
				else {
					superClass = "target." + current[0];
					currClass1 = "target." + current[1];
					currClass2 = "target." + current[2];
				}
				ClassPool pool = ClassPool.getDefault();
				insertClassPath(pool);
				CtClass ctc1 = pool.get(currClass1);
				CtClass ctc2 = pool.get(currClass2);
				setSuperclass(ctc1, superClass, pool);
				setSuperclass(ctc2, superClass, pool);
		        ctc1.writeFile(outputDir);
		        ctc2.writeFile(outputDir);
				System.out.println("[DBG] write output to: " + outputDir);
				
			}catch (NotFoundException | CannotCompileException | IOException e) {
		         e.printStackTrace();
		      }
		}
		else {
			System.out.println("[DBG] The lenght of the argument array is not three!");
		}
			
	}
   static void insertClassPath(ClassPool pool) throws NotFoundException {
	      String strClassPath = workDir + SEP + "bin"; // eclipse compile dir
	      // String strClassPath = workDir + SEP + "classfiles"; // separate dir
	      pool.insertClassPath(strClassPath);
	      System.out.println("[DBG] insert classpath: " + strClassPath);
	   }

   static void setSuperclass(CtClass curClass, String superClass, ClassPool pool) throws NotFoundException, CannotCompileException {
      curClass.setSuperclass(pool.get(superClass));
      System.out.println("[DBG] set superclass: " + curClass.getSuperclass().getName() + //
            ", subclass: " + curClass.getName());
   } 


}
