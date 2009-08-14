package edu.cmu.cs.dennisc.alice.virtualmachine;

public class ExceptionDetailUtilities {
	//todo
	private static String PREFIX = "Alice has caught an exception: ";
	public static String createExceptionDetail( String detail ) {
		return PREFIX + detail;
	}
	public static String createExceptionDetail() {
		return createExceptionDetail( "details not provided." );
	}
}
