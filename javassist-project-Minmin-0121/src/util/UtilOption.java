package util;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.junit.jupiter.api.Test;
import ex02.definenewclass.*;
import javassist.ClassPool;
import javassist.CtClass;
import ex03.frozenclasses.*;

public class UtilOption {
   private static Scanner scanner = new Scanner(System.in);

   @Test
   public void testGetInputs() {
      System.out.println("=============================================");
      System.out.println("Example Program");

      while (true) {
         System.out.println("=============================================");
         System.out.print("Enter three classes (\"q\" to terminate)\n");
         String[] inputs = getInputs();
         if (inputs.length == 3) {
    		 ClassPool pool = ClassPool.getDefault();
    		 DefineNewClass cls = new DefineNewClass();
    		 FrozenClass fro = new FrozenClass();
       		 
                 for (int i = 0; i < inputs.length; i++) { 
                	 //cls.makeClass(pool, inputs[i]);
                     System.out.println("[DBG] " + i + ": " + inputs[i]);
        	 }

    		 CtClass cc1 = cls.makeClass(pool, inputs[0]);
    		 CtClass cc2 = cls.makeClass(pool, inputs[1]);
    		 CtClass cc3 = cls.makeClass(pool, inputs[2]);
    		 fro.frozen(pool, cc1, cc2, cc3);
         }
         else {
        	 System.out.print("[WRN] Invalid Input\n");
         }
        	 

      }
   }

   public static String[] getInputs() {
      String input = scanner.nextLine();
      if (input.trim().equalsIgnoreCase("q")) {
         System.err.println("Terminated.");
         System.exit(0);
      }
      List<String> list = new ArrayList<String>();
      String[] inputArr = input.split(",");
      for (String iElem : inputArr) {
         list.add(iElem.trim());
      }
      return list.toArray(new String[0]);
   }
}
