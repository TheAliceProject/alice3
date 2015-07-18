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
public class Build {
	public static void main( String[] args ) throws Exception {
		Config config = new Config.Builder()
				.mode( Mode.DEV_BARE_MINIMUM )
				.joglVersion( "2.2.4" )
				.aliceModelSourceVersion( "2014.08.20" )
				.nebulousModelSourceVersion( "2014.09.11" )
				.netBeans8Version( "8.0.2" )
				.build();

		JdkUtils.initialize();
		MavenUtils.initialize();
		AntUtils.initialize();
		NetBeans8Utils.initialize( config.getNetBeans8Version() );

		BuildRepo buildRepo = new BuildRepo( config );
		GitRepo repo;
		if( config.getMode().isDev() ) {
			repo = new DevRepo( config );
		} else {
			repo = buildRepo;
		}

		edu.cmu.cs.dennisc.timing.Timer timer = new edu.cmu.cs.dennisc.timing.Timer( "build" );
		timer.start();
		buildRepo.compileJars();
		timer.mark( "compileJars" );

		java.io.File tempDirectoryForJavaDoc = buildRepo.generateJavaDocs();
		timer.mark( "generateJavaDocs" );

		java.io.File docZip = new java.io.File( repo.getPlugin8().getSuite(), "Alice3Module/release/doc/aliceDocs.zip" );
		edu.cmu.cs.dennisc.java.io.FileUtilities.createParentDirectoriesIfNecessary( docZip );
		edu.cmu.cs.dennisc.java.util.zip.ZipUtilities.zip( tempDirectoryForJavaDoc, docZip );
		assert docZip.exists() : docZip;
		timer.mark( "zipJavaDocs" );

		java.io.File srcDirectory = new java.io.File( buildRepo.getCoreSrcDirectory( "story-api" ), "org/lgna/story" );
		java.io.File dstZip = new java.io.File( repo.getPlugin8().getSuite(), "Alice3Module/release/src/aliceSource.jar" );
		edu.cmu.cs.dennisc.java.util.zip.ZipUtilities.zipFilesInDirectory( srcDirectory, dstZip, new java.io.FileFilter() {
			@Override
			public boolean accept( java.io.File file ) {
				return "java".equals( edu.cmu.cs.dennisc.java.io.FileUtilities.getExtension( file ) );
			}
		} );
		timer.mark( "zipSrc" );

		repo.getPlugin8().copyJars( buildRepo );
		timer.mark( "copyJars" );
		repo.getPlugin8().copyDistribution( buildRepo );
		timer.mark( "copyDistribution" );
		repo.getPlugin8().prepareFiles();
		timer.mark( "prepareFiles" );

		if( config.getMode().isDev() ) {
			//pass
		} else {
			repo.getPlugin8().createNbm();
			timer.mark( "nbm" );
		}

		edu.cmu.cs.dennisc.java.util.logging.Logger.outln();
		edu.cmu.cs.dennisc.java.util.logging.Logger.outln();

		timer.stopAndPrintResults();

		edu.cmu.cs.dennisc.java.util.logging.Logger.outln();
		edu.cmu.cs.dennisc.java.util.logging.Logger.outln();
		edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "JAVA_HOME", System.getenv( "JAVA_HOME" ) );
		edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "JDK8_HOME", System.getenv( "JDK8_HOME" ) );
		edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "MAVEN_HOME", System.getenv( "MAVEN_HOME" ) );
		edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "ANT_HOME", System.getenv( "ANT_HOME" ) );
		edu.cmu.cs.dennisc.java.util.logging.Logger.outln( config );
		edu.cmu.cs.dennisc.java.util.logging.Logger.outln();
		edu.cmu.cs.dennisc.java.util.logging.Logger.outln();
		edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "done" );
	}
}
