package target;

public class ServiceApp {
   int srcX = 22, srcY = 22;
   String srcID = "Service";

   public void move(int dx, int dy, String str) {
	   setX(dx);
	   setY(dy);
	   setStr(str);
   }
   
   int setX(int dx) {
	   this.srcX = dx;
	   return srcX;
   }
   
   int setY(int dy) {
	   this.srcY = dy;
	   return srcY;
   }
   
   String setStr(String str) {
	   this.srcID = str;
	   return srcID;
   }
   
	   

   public static void main(String[] args) throws Exception {
      System.out.println("[ServiceApp] Run...");
      ServiceApp a = new ServiceApp();
      a.move(0, 0, null);
      System.out.println("[ServiceApp] Done. ");
   }
}
