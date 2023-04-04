package telran.reflection.tests;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class TestsFramework {
	
	public static void main(String[] args) throws Exception {
		try {
			if (args.length == 0) {
				throw new IllegalArgumentException("Must be class name as first argument");
			}
			Class<?> clazz = Class.forName(args[0]);
			Constructor<?> constructor = clazz.getConstructor();
			Object obj = constructor.newInstance();
			Method[] methods = obj.getClass().getDeclaredMethods();
			runTests(obj, methods);
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException("Argument contains wrong class name");
		} catch (NoSuchMethodException | SecurityException e) {
			throw new IllegalArgumentException("Class doesn't contain public default constructor");
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}
	}

	private static void runTests(Object obj, Method[] methods) {
		for (Method method: methods) {
			String name = method.getName();
			if (name.startsWith("test") &&
				method.getParameterCount() == 0) {
				method.setAccessible(true); //for making possible to call private methods
				try {
					method.invoke(obj);
					System.out.printf("Test %s passed\n", name);
				} catch (Exception e) {
					System.out.printf("Test %s failed, exception %s\n", name, e.toString());
				}
			}
		}
	}
	
}
