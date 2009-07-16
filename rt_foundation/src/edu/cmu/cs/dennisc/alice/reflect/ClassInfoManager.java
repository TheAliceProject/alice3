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
	private static java.util.Map< String, edu.cmu.cs.dennisc.pattern.LazilyInitialized< ClassInfo > > s_map = new java.util.HashMap< String, edu.cmu.cs.dennisc.pattern.LazilyInitialized< ClassInfo > >();
	private ClassInfoManager() {
	}
	public static void addClassInfosFrom( java.io.File file ) {
		assert file != null;
		if( file.isDirectory() ) {
			for( final java.io.File f : file.listFiles() ) {
				String clsName = edu.cmu.cs.dennisc.io.FileUtilities.getBaseName( f );
				s_map.put( clsName, new edu.cmu.cs.dennisc.pattern.LazilyInitialized< ClassInfo >() {
					@Override
					protected ClassInfo initialize() {
						try {
							return edu.cmu.cs.dennisc.codec.CodecUtilities.decodeBinary( f, ClassInfo.class );
						} catch( Exception e ) {
							return null;
						}
					}
				} );
			}
		} else {
			try {
				final java.util.zip.ZipFile zipFile = new java.util.zip.ZipFile( file ); 
				java.util.Enumeration< ? extends java.util.zip.ZipEntry > e = zipFile.entries();
				while( e.hasMoreElements() ) {
					final java.util.zip.ZipEntry zipEntry = e.nextElement();
					String clsName = edu.cmu.cs.dennisc.io.FileUtilities.getBaseName( zipFile.getName() );
					s_map.put( clsName, new edu.cmu.cs.dennisc.pattern.LazilyInitialized< ClassInfo >() {
						@Override
						protected edu.cmu.cs.dennisc.alice.reflect.ClassInfo initialize() {
							try {
								return edu.cmu.cs.dennisc.codec.CodecUtilities.decodeBinary( zipFile.getInputStream( zipEntry ), ClassInfo.class );
							//} catch( java.io.IOException ioe ) {
							} catch( Exception e ) {
								return null;
							}
						}
					} );
				}
			} catch( java.io.IOException ioe ) {
				throw new RuntimeException( ioe );
			}
		}
	}
	
	public static ClassInfo get( Class<?> cls ) {
		if( cls != null ) {
			edu.cmu.cs.dennisc.pattern.LazilyInitialized< ClassInfo > lazyClassInfo = s_map.get( cls.getName() );
			if( lazyClassInfo != null ) {
				return lazyClassInfo.get();
			} else {
				return null;
			}
		} else {
			return null;
		}
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
