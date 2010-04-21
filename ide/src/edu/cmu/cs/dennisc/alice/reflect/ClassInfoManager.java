/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution.
 *
 * 3. Products derived from the software may not be called "Alice", nor may 
 *    "Alice" appear in their name, without prior written permission of 
 *    Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software 
 *    developed by Carnegie Mellon University"
 *
 * 5. The gallery of art assets and animations provided with this software is 
 *    contributed by Electronic Arts Inc. and may be used for personal, 
 *    non-commercial, and academic use only. Redistributions of any program 
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in 
 *    The Alice 3.0 Art Gallery License.
 * 
 * DISCLAIMER:
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.  
 * ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT 
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A 
 * PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT 
 * SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO 
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
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
				String clsName = edu.cmu.cs.dennisc.java.io.FileUtilities.getBaseName( f );
				s_map.put( clsName, new edu.cmu.cs.dennisc.pattern.LazilyInitialized< ClassInfo >() {
					@Override
					protected ClassInfo initialize() {
						return edu.cmu.cs.dennisc.codec.CodecUtilities.decodeBinary( f, ClassInfo.class );
					}
				} );
//				ClassInfo classInfo = s_map.get( clsName ).get();
//				for( MethodInfo methodInfo : classInfo.getMethodInfos() ) {
//					try {
//						java.lang.reflect.Method mthd = methodInfo.getMthd();
//					} catch( RuntimeException re ) {
//						re.printStackTrace();
//					}
//				}
			}
		} else {
			try {
				final java.util.zip.ZipFile zipFile = new java.util.zip.ZipFile( file ); 
				java.util.Enumeration< ? extends java.util.zip.ZipEntry > e = zipFile.entries();
				while( e.hasMoreElements() ) {
					java.util.zip.ZipEntry zipEntry = e.nextElement();
					final java.io.InputStream is = zipFile.getInputStream( zipEntry );
					String clsName = edu.cmu.cs.dennisc.java.io.FileUtilities.getBaseName( zipEntry.getName() );
					s_map.put( clsName, new edu.cmu.cs.dennisc.pattern.LazilyInitialized< ClassInfo >() {
						@Override
						protected edu.cmu.cs.dennisc.alice.reflect.ClassInfo initialize() {
							return edu.cmu.cs.dennisc.codec.CodecUtilities.decodeBinary( is, ClassInfo.class );
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
//		if( s_map.isEmpty() ) {
//			edu.cmu.cs.dennisc.print.PrintUtilities.println( "EMPTY", cls.getName() );
//			if( cls.getName().contains( "Model" ) ) {
//				Thread.dumpStack();
//			}
//		}
		ClassInfo clsInfo = get( cls );
		if( clsInfo != null ) {
			return clsInfo.getMethodInfos();
		} else {
			return null;
		}
	}
}
