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
package org.alice.build;

import java.io.IOException;

/**
 * @author Dennis Cosgrove
 */
public class CopyDistributionJarsUtilities {

	private static java.io.File getUserDirectory() {
		java.io.File defaultDirectory = edu.cmu.cs.dennisc.java.io.FileUtilities.getDefaultDirectory();
		java.io.File rv;
		if( edu.cmu.cs.dennisc.java.lang.SystemUtilities.isWindows() ) {
			rv = defaultDirectory.getParentFile();
		} else {
			rv = defaultDirectory;
		}
		return rv;
	}

	public static void copyDistributionJars( java.io.File distributionRootDirectory ) throws java.io.IOException {
		java.io.File userDirectory = getUserDirectory();
		java.io.File mavenRepositoryRootDirectory = new java.io.File( userDirectory, ".m2/repository" );
		java.util.List<java.io.File> jarFiles = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
		for( String pathname : edu.cmu.cs.dennisc.java.lang.SystemUtilities.getClassPath() ) {
			java.io.File file = new java.io.File( pathname );
			if( file.isDirectory() ) {
				edu.cmu.cs.dennisc.java.util.logging.Logger.errln( "skipping directory:", file );
			} else {
				if( edu.cmu.cs.dennisc.java.io.FileUtilities.isExtensionAmoung( file, "jar" ) ) {
					if( edu.cmu.cs.dennisc.java.io.FileUtilities.isDescendantOf( file, mavenRepositoryRootDirectory ) ) {
						edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "++ adding: " + file );
						jarFiles.add( file );
					} else {
						edu.cmu.cs.dennisc.java.util.logging.Logger.errln( "skipping non descendant:", file );
					}
				} else {
					edu.cmu.cs.dennisc.java.util.logging.Logger.errln( "skipping non jar:", file );
				}
			}
		}
		System.err.flush();
		edu.cmu.cs.dennisc.java.io.FileUtilities.delete( distributionRootDirectory );
		java.util.Collections.sort( jarFiles );
		for( java.io.File jarFile : jarFiles ) {
			java.io.File distibutionFile = edu.cmu.cs.dennisc.java.io.FileUtilities.getAnalogousFile( jarFile, mavenRepositoryRootDirectory, distributionRootDirectory );
			edu.cmu.cs.dennisc.java.io.FileUtilities.copyFile( jarFile, distibutionFile );
			edu.cmu.cs.dennisc.java.util.logging.Logger.outln( distibutionFile );
		}
	}

	public static void copyDistributionJars( String path ) throws IOException {
		copyDistributionJars( new java.io.File( path ) );
	}

	public static void main( String[] args ) throws Exception {
		java.io.File userDirectory = edu.cmu.cs.dennisc.java.io.FileUtilities.getUserDirectory();
		//java.io.File distributionRootDirectory = new java.io.File( userDirectory, "Documents/aliceBuildProcess/jars/lib" );
		java.io.File distributionRootDirectory = new java.io.File( userDirectory, "Documents/gits/alice/installer/aliceInstallData/Alice3/ext" );
		copyDistributionJars( distributionRootDirectory );
	}
}
