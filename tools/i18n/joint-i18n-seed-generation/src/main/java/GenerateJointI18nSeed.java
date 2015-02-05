/**
 * Copyright (c) 2006-2012, Carnegie Mellon University. All rights reserved.
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

/**
 * @author Dennis Cosgrove
 */
public class GenerateJointI18nSeed {
	private static String fixName( String methodName ) {
		StringBuilder sb = new StringBuilder();
		for( char ch : methodName.substring( 3 ).toCharArray() ) {
			if( Character.isUpperCase( ch ) || Character.isDigit( ch ) ) {
				sb.append( ' ' );
			}
			sb.append( Character.toLowerCase( ch ) );
		}
		return sb.toString().trim();
	}

	public static void main( String[] args ) throws Exception {
		Class<?>[] clses = {
				org.lgna.story.SBiped.class,
				org.lgna.story.SQuadruped.class,
				org.lgna.story.SFlyer.class,
				org.lgna.story.SSwimmer.class,
				org.lgna.story.STransport.class
		};
		java.util.Set<String> methodNames = edu.cmu.cs.dennisc.java.util.Sets.newHashSet();
		for( Class<?> cls : clses ) {
			for( java.lang.reflect.Method method : cls.getDeclaredMethods() ) {
				if( org.lgna.story.SJoint.class.isAssignableFrom( method.getReturnType() ) ) {
					if( method.getParameterTypes().length == 0 ) {
						if( java.lang.reflect.Modifier.isPublic( method.getModifiers() ) ) {
							String methodName = method.getName();
							if( methodName.startsWith( "get" ) ) {
								methodNames.add( method.getName() );
							}
						}
					}
				}
			}
		}
		java.util.List<String> list = edu.cmu.cs.dennisc.java.util.Lists.newArrayList( methodNames );
		java.util.Collections.sort( list );
		StringBuilder sb = new StringBuilder();
		for( String methodName : list ) {
			sb.append( methodName );
			sb.append( " = </expression/>'s " );
			sb.append( fixName( methodName ) );
			sb.append( "\n" );
		}

		edu.cmu.cs.dennisc.java.util.logging.Logger.outln( sb );

		//		java.io.File repoRoot = new java.io.File( edu.cmu.cs.dennisc.java.io.FileUtilities.getDefaultDirectory(), "/gits/alice/" );
		//		java.io.File srcRoot = new java.io.File( repoRoot, "core/i18n/src/main/resources" );//"core/ide/src/main/java/" );
		//		java.io.File packageDirectory = new java.io.File( srcRoot, "org/lgna/story/resources" );
		//		java.io.File namesFile = new java.io.File( packageDirectory, "GalleryNames.properties" );
		//		edu.cmu.cs.dennisc.java.io.TextFileUtilities.write( namesFile, sbNames.toString() );
	}
}
