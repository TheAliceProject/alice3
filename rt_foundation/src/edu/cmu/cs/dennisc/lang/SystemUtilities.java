/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
 */
package edu.cmu.cs.dennisc.lang;

/**
 * @author Dennis Cosgrove
 */
public class SystemUtilities {
	private static java.io.ByteArrayOutputStream getPropertiesAsXMLByteArrayOutputStream() {
		java.util.Properties properties = System.getProperties();
		java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
		try {
			properties.storeToXML( baos, "comment" );
		} catch( java.io.IOException ioe ) {
			ioe.printStackTrace();
		}
		return baos;
	}

	public static byte[] getPropertiesAsXMLByteArray() {
		return getPropertiesAsXMLByteArrayOutputStream().toByteArray();
	}
	public static String getPropertiesAsXMLString() {
		return getPropertiesAsXMLByteArrayOutputStream().toString();
	}

	private enum Platform {
		WINDOWS, OSX, LINUX
	}

	private static Platform platform;
	static {
		String lowercaseOSName = System.getProperty( "os.name" ).toLowerCase();
		if( lowercaseOSName.contains( "windows" ) ) {
			platform = Platform.WINDOWS;
		} else if( lowercaseOSName.startsWith( "mac os x" ) ) {
			platform = Platform.OSX;
		} else if( lowercaseOSName.startsWith( "linux" ) ) {
			platform = Platform.LINUX;
		} else {
			//todo
			platform = null;
		}
	}

	public static boolean isMac() {
		return SystemUtilities.platform == Platform.OSX;
	}
	public static boolean isWindows() {
		return SystemUtilities.platform == Platform.WINDOWS;
	}
	
	public static <E> E[] returnArray( Class<E> componentType, E... rv ) {
		return rv;
	}
	public static <E> E[] createArray( Class<E> componentType, E[]... arrays ) {
		int n = 0;
		for( E[] array : arrays ) {
			if( array != null ) {
				n += array.length;
			}
		}
		E[] rv = (E[])edu.cmu.cs.dennisc.lang.reflect.ReflectionUtilities.newArrayInstance( componentType, n );
		int offset = 0;
		for( E[] array : arrays ) {
			if( array != null ) {
				System.arraycopy( array, 0, rv, offset, array.length );
				offset += array.length;
			}
		}
		return rv;
	}
}
