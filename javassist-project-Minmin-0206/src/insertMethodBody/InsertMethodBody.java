package insertMethodBody;

import java.io.File;
import java.io.IOException;


//import insertmethodbodycflow.InsertMethodBodyCflow;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import util.UtilFile;
import util.UtilMenu;
//import util.UtilStr;

public class InsertMethodBody extends ClassLoader{
   static String WORK_DIR = System.getProperty("user.dir");
   static String INPUT_DIR = WORK_DIR + File.separator + "classfiles";
   static String OUTPUT_DIR = WORK_DIR + File.separator + "output";

   static String PACK = "target.";

   static String _L_ = System.lineSeparator();
   private ClassPool pool;

   public static void main(String[] args) throws Throwable {
	   while(true) {
		   UtilMenu.showMenuOptions1();
		   if(UtilMenu.getOption() == 1) {
			   UtilMenu.showMenuOptions2();
			   String[] input = UtilMenu.getArguments();
			   if(input.length == 3) {
				   process(input[0], input[1], input[2]);
			       InsertMethodBody s = new InsertMethodBody();
		           Class<?> c = s.loadClass(PACK + input[0]);
				   c.getDeclaredMethod("main", new Class[] {String[].class}).
				   invoke(null, new Object[] { args });
			   }
			   else {
				   System.out.println("[WRN] Invalid Input\n");
			   }
		   }


 
	   }

   }
   public static void process(String clsname, String medname, String index) throws Throwable {
	      try {
	          ClassPool pl = ClassPool.getDefault();
	          pl.insertClassPath(INPUT_DIR);
	          String cname = PACK + clsname;
	          CtClass cc = pl.get(cname);
	          CtMethod m = cc.getDeclaredMethod(medname);
	          String i = "+" + "$" + index;
	          String block1 = "{ " + _L_ //
	                + "System.out.println(\"[Inserted] " + cname + "." + medname + "'s param " + index + ": \"" + i + "); " + _L_ + //
	                
	                "}";
	          System.out.println(block1);
	          m.insertBefore(block1);
	          cc.writeFile(OUTPUT_DIR);
	          System.out.println("[DBG] write output to: " + OUTPUT_DIR);
	          System.out.println("[DBG] \t" + UtilFile.getShortFileName(OUTPUT_DIR));


	          
	          
	          
	       } catch (NotFoundException | CannotCompileException | IOException e) {
	          e.printStackTrace();
	       } 
	   }
   
   public InsertMethodBody() throws NotFoundException {
	      pool = new ClassPool();
	      pool.insertClassPath(OUTPUT_DIR); // TARGET must be there.
	      //UtilStr.print("[CLASS-LOADER] CLASS_PATH: " + OUTPUT_DIR);
	      //System.out.println("------------------------------------------");
	   }


   protected Class<?> findClass(String name) throws ClassNotFoundException {
	      CtClass cc = null;
	      try {
	         cc = pool.get(name);
	         byte[] b = cc.toBytecode();
	         return defineClass(name, b, 0, b.length);
	      } catch (NotFoundException e) {
	         throw new ClassNotFoundException();
	      } catch (IOException e) {
	         throw new ClassNotFoundException();
	      } catch (CannotCompileException e) {
	         throw new ClassNotFoundException();
	      }
	   }
}
