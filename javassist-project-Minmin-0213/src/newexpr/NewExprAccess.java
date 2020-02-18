package newexpr;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;

import javassist.CannotCompileException;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.NotFoundException;
import javassist.expr.ExprEditor;
import javassist.expr.NewExpr;
import util.UtilMenu;

public class NewExprAccess extends ClassLoader {
	static final String WORK_DIR = System.getProperty("user.dir");
	static final String CLASS_PATH = WORK_DIR + File.separator + "classfiles";
	static final String TARGET_MY_APP2 = "target.MyAppField";
	static String _L_ = System.lineSeparator();
	static int number;

	public static void main(String[] args) throws Throwable {
		while (true) {
			UtilMenu.showMenuOptions();
			String[] inputdata = UtilMenu.getArguments();
			if (inputdata.length == 2) {
				NewExprAccess s = new NewExprAccess();
				number = Integer.parseInt(inputdata[1]);
				Class<?> c = s.loadClass(inputdata[0]);

				Method mainMethod = c.getDeclaredMethod("main", new Class[] { String[].class });
				mainMethod.invoke(null, new Object[] { args });
			} else {
				System.out.print("[WRN] Invalid Input size!!\n");
			}

		}
	}

	private ClassPool pool;

	public NewExprAccess() throws NotFoundException {
		pool = new ClassPool();
		pool.insertClassPath(new ClassClassPath(new java.lang.Object().getClass()));
		pool.insertClassPath(CLASS_PATH); // TARGET must be there.
	}

	/*
	 * Finds a specified class. The bytecode for that class can be modified.
	 */
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		CtClass cc = null;
		try {
			cc = pool.get(name);
			cc.instrument(new ExprEditor() {
				public void edit(NewExpr newExpr) throws CannotCompileException {
					try {
						String longName = newExpr.getConstructor().getLongName();
						if (longName.startsWith("java.")) {
							return;
						}
					} catch (NotFoundException e) {
						e.printStackTrace();
					}
					String log = String.format("[Edited by ClassLoader] new expr: %s, " //
							+ "line: %d, signature: %s", newExpr.getEnclosingClass().getName(), //
							newExpr.getLineNumber(), newExpr.getSignature());
					System.out.println(log);

					int numprint = number;
					CtField fields[] = newExpr.getEnclosingClass().getDeclaredFields();
					if (number > fields.length) {
						numprint = fields.length;
					}

					String pro = "{" + _L_ //
							+ "$_ = $proceed($$);" + _L_ //
							+ "{" + _L_ //
							+ "String cName = $_.getClass().getName();";
					for (int i = 0; i < numprint; i++) {

						try {
							String fieldName = fields[i].getName();
							String fieldType = fields[i].getType().getName();
							pro += _L_ //
									+ "String fName = $_.getClass().getDeclaredFields()[" + i + "].getName();" + _L_ //
									+ "String fieldFullName = cName + \".\" + fName;" + _L_ //
									+ fieldType + " fieldValue = $_." + fieldName + ";" + _L_ //
									+ "System.out.println(\"  [Instrument] \" + fieldFullName + \": \" + fieldValue);";

						} catch (NotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
					pro += _L_ //
							+ "}" + _L_ //
							+ "}";
					System.out.println(pro);
					newExpr.replace(pro);
				}
			});
			byte[] b = cc.toBytecode();
			return defineClass(name, b, 0, b.length);
		} catch (NotFoundException e) {
			throw new ClassNotFoundException();
		} catch (IOException e) {
			throw new ClassNotFoundException();
		} catch (CannotCompileException e) {
			e.printStackTrace();
			throw new ClassNotFoundException();
		}
	}
}