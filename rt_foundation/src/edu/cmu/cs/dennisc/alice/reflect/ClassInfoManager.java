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
package edu.cmu.cs.dennisc.alice.reflect;

/**
 * @author Dennis Cosgrove
 */
public final class ClassInfoManager {
	private static java.util.Map< Class< ? >, ClassInfo > s_map = new java.util.HashMap< Class< ? >, ClassInfo >();
	private ClassInfoManager() {
	}
	private static void add( java.io.File inFile ) {
		//ClassInfo clsInfo = (ClassInfo)edu.cmu.cs.dennisc.io.SerializationUtilities.unserialize( inFile );
		try {
			ClassInfo clsInfo = edu.cmu.cs.dennisc.codec.CodecUtilities.decodeBinary( inFile, ClassInfo.class );
			s_map.put( clsInfo.getCls(), clsInfo );
		} catch( RuntimeException re ) {
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "warning: could not load class information from:", inFile );
		}
	}
	
	public static void setDirectory( java.io.File directory ) {
		assert directory != null;
		if( directory.exists() ) {
			//assert directory.isDirectory();
			//double t0 = edu.cmu.cs.dennisc.clock.Clock.getCurrentTime();
			for( java.io.File file : directory.listFiles() ) {
				ClassInfoManager.add( file );
			}
			//double t1 = edu.cmu.cs.dennisc.clock.Clock.getCurrentTime();
			//edu.cmu.cs.dennisc.print.PrintUtilities.println( t1-t0 );
		} else {
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "could not find class info directory: " + directory );
		}
	}
	
	public static ClassInfo get( Class<?> cls ) {
		return s_map.get( cls );
	}
	public static String[] getParameterNamesFor( java.lang.reflect.Method mthd ) {
		ClassInfo clsInfo = get( mthd.getDeclaringClass() );
		if( clsInfo != null ) {
			MethodInfo methodInfo = clsInfo.lookupInfo( mthd );
			if( methodInfo != null ) {
				return methodInfo.getParameterNames();
			} else {
				return null;
			}
		} else {
			return null;
		}
	}
	public static Iterable< MethodInfo > getMethodInfos( Class<?> cls ) {
		ClassInfo clsInfo = get( cls );
		if( clsInfo != null ) {
			return clsInfo.getMethodInfos();
		} else {
			return null;
		}
	}
}
