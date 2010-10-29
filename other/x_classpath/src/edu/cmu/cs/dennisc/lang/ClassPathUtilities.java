package edu.cmu.cs.dennisc.lang;

public class ClassPathUtilities {
	public static String[] getClassPath() {
		String classPath = System.getProperty( "java.class.path" );
		return classPath.split( System.getProperty( "path.separator" ) );
	}
}
