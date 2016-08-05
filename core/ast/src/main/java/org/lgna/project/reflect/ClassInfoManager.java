/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
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
 *******************************************************************************/
package org.lgna.project.reflect;

/**
 * @author Dennis Cosgrove
 */
public class ClassInfoManager {
	private static java.util.Map<String, edu.cmu.cs.dennisc.pattern.Lazy<ClassInfo>> s_map = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();

	private ClassInfoManager() {
	}

	public static void addClassInfosFrom( java.io.InputStream is ) throws java.io.IOException {
		java.util.zip.ZipInputStream zis = new java.util.zip.ZipInputStream( is );
		while( true ) {
			java.util.zip.ZipEntry zipEntry = zis.getNextEntry();
			if( zipEntry != null ) {
				if( zipEntry.isDirectory() ) {
					//pass
				} else {
					String clsName = edu.cmu.cs.dennisc.java.io.FileUtilities.getBaseName( zipEntry.getName() );
					final byte[] data = edu.cmu.cs.dennisc.java.util.zip.ZipUtilities.extractBytes( zis, zipEntry );
					s_map.put( clsName, new edu.cmu.cs.dennisc.pattern.Lazy<ClassInfo>() {
						@Override
						protected ClassInfo create() {
							ClassInfo rv = edu.cmu.cs.dennisc.codec.CodecUtilities.decodeBinary( data, ClassInfo.class );
							//edu.cmu.cs.dennisc.java.util.logging.Logger.outln( rv );
							return rv;
						}
					} );
				}
			} else {
				break;
			}
		}
	}

	public static java.util.Set<String> getKeys() {
		return java.util.Collections.unmodifiableSet( s_map.keySet() );
	}

	public static ClassInfo getInstance( String clsName ) {
		if( clsName != null ) {
			edu.cmu.cs.dennisc.pattern.Lazy<ClassInfo> lazyClassInfo = s_map.get( clsName );
			if( lazyClassInfo != null ) {
				try {
					return lazyClassInfo.get();
				} catch( Throwable t ) {
					edu.cmu.cs.dennisc.java.util.logging.Logger.throwable( t, clsName );
					return null;
				}
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	public static ClassInfo getInstance( Class<?> cls ) {
		if( cls != null ) {
			return getInstance( cls.getName() );
		} else {
			return null;
		}
	}

	public static java.util.List<MethodInfo> getMethodInfos( String clsName ) {
		ClassInfo clsInfo = getInstance( clsName );
		if( clsInfo != null ) {
			return clsInfo.getMethodInfos();
		} else {
			//throw new NullPointerException();
			return null;
		}
	}

	public static java.util.List<MethodInfo> getMethodInfos( Class<?> cls ) {
		if( cls != null ) {
			return getMethodInfos( cls.getName() );
		} else {
			return null;
		}
	}

	public static String[] getParameterNamesFor( java.lang.reflect.Method mthd ) {
		ClassInfo clsInfo = getInstance( mthd.getDeclaringClass() );
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
}
