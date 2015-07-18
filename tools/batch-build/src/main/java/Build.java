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
		Mode mode;
		if( args.length > 0 ) {
			mode = Mode.valueOf( args[ 0 ] );
		} else {
			mode = Mode.BUILD;
		}

		Config config = new Config.Builder()
				.mode( mode )
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

		for( Plugin plugin : repo.getPlugins() ) {
			plugin.copyJars( buildRepo );
			timer.mark( "copyJars" );

			plugin.copyDistribution( buildRepo );
			timer.mark( "copyDistribution" );

			plugin.prepareFiles();
			timer.mark( "prepareFiles" );

			plugin.zipSrc( buildRepo );
			timer.mark( "zipSrc" );

			plugin.zipJavaDocs( tempDirectoryForJavaDoc );
			timer.mark( "zipJavaDocs" );

			if( config.getMode().isDev() ) {
				//pass
			} else {
				plugin.createNbm();
				timer.mark( "nbm" );
			}
		}

		edu.cmu.cs.dennisc.java.util.logging.Logger.outln();
		edu.cmu.cs.dennisc.java.util.logging.Logger.outln();

		timer.stopAndPrintResults();

		edu.cmu.cs.dennisc.java.util.logging.Logger.outln();
		edu.cmu.cs.dennisc.java.util.logging.Logger.outln();
		edu.cmu.cs.dennisc.java.util.logging.Logger.outln( config );
		edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "assertions:", Build.class.desiredAssertionStatus() );
		edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "javaHomeDir:", JdkUtils.getJavaHomeDir() );
		edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "jdk8HomeDir:", JdkUtils.getJdk8HomeDir() );
		edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "mavenCommandFile:", MavenUtils.getMavenCommandFile() );
		edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "antCommand:", AntUtils.getAntCommandFile() );
		edu.cmu.cs.dennisc.java.util.logging.Logger.outln();
		edu.cmu.cs.dennisc.java.util.logging.Logger.outln();
		edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "done" );
	}
}
