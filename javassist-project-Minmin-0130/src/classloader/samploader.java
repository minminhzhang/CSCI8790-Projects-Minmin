package classloader;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.Modifier;
import javassist.NotFoundException;
import util.UtilMenu;

public class samploader extends ClassLoader {
   static String WORK_DIR = System.getProperty("user.dir");
   static String INPUT_DIR = WORK_DIR + File.separator + "classfiles";
   //static String COMP_APP = "ComponentApp";
   //static String SERV_APP = "ServceApp";
   private ClassPool pool;
   
   public static void main(String[] args) throws Throwable{
	   while (true) {
		   UtilMenu.showMenuOptions();
		   String[] inputname = UtilMenu.getArguments();
		   if (inputname.length == 2) {
			   try {
				   samploader loader = new samploader();
				   //Class<?> c = loader.loadClass(inputname[0]);
				   Class<?> c = loader.findClass(inputname[0], inputname[1]);
				   displayFieldVal(c, inputname[1]);
				   c.getDeclaredMethod("main", new Class[] {String[].class}).
				   invoke(null, new Object[] {inputname});
				   
			    
			   }catch (Exception e) {
			         e.printStackTrace();
			      }
		   }
		   else {
			   System.out.print("[WRN] Invalid Input size!!\n"); 
		   }
	   }
   }
   
   public samploader() throws NotFoundException{
	      pool = new ClassPool();
	      pool.insertClassPath(INPUT_DIR); // Search App.class in this path.
   }
   
   protected Class<?> findClass(String classname, String fieldname) throws ClassNotFoundException{
	   try {
		   CtClass cc = pool.get(classname);
		   if (classname.equals("ComponentApp") || classname.equals("ServiceApp")) {
			   CtField f = new CtField(CtClass.doubleType, fieldname, cc);
			   f.setModifiers(Modifier.PUBLIC);
			   cc.addField(f, CtField.Initializer.constant(0.0));

		   }
		   byte[] b = cc.toBytecode();
		   return defineClass(classname, b, 0, b.length);
		   
	   }catch (NotFoundException e) {
	         throw new ClassNotFoundException();
	      } catch (IOException e) {
	         throw new ClassNotFoundException();
	      } catch (CannotCompileException e) {
	         throw new ClassNotFoundException();
	      }
   }
   
   public static void displayFieldVal(Class<?> c, String fieldname) throws Exception {
	      Field field = c.getField(fieldname);
	      String fName = field.getName();
	      Object fVal = field.get(c.newInstance());
	      System.out.println(" Field name: " + fName + ", value: " + fVal);
	   }
   


}
