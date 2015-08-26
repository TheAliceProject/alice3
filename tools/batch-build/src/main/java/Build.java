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
		org.apache.commons.cli.Options options = new org.apache.commons.cli.Options();
		options.addOption( new org.apache.commons.cli.Option( "isDev", "mode=Mode.DEV" ) );
		options.addOption( new org.apache.commons.cli.Option( "skipPlugin6", "isPlugin6Desired=false" ) );
		options.addOption( new org.apache.commons.cli.Option( "skipPlugin8", "isPlugin8Desired=false" ) );
		options.addOption( new org.apache.commons.cli.Option( "skipInstaller", "isInstallerDesired=false" ) );
		options.addOption( new org.apache.commons.cli.Option( "skipClean", "isCleanDesired=false" ) );
		options.addOption( new org.apache.commons.cli.Option( "skipJavaDocs", "isJavaDocGenerationDesired=false" ) );

		org.apache.commons.cli.CommandLineParser parser = new org.apache.commons.cli.DefaultParser();
		org.apache.commons.cli.CommandLine commandLine = parser.parse( options, args );

		// @formatter:off
		Config config = new Config.Builder()
				.rootDir( new java.io.File( edu.cmu.cs.dennisc.java.io.FileUtilities.getDefaultDirectory(), "Projects/Alice/Code" ) )
				.mode( commandLine.hasOption( "isDev" ) ? Mode.DEV : Mode.BUILD )

		.isPlugin6Desired( commandLine.hasOption( "skipPlugin6" ) == false )
		.isPlugin8Desired( commandLine.hasOption( "skipPlugin8" ) == false )
		.isInstallerDesired( commandLine.hasOption( "skipInstaller" ) == false )

		.isCleanDesired( commandLine.hasOption( "skipClean" ) == false )
		.isJavaDocGenerationDesired( commandLine.hasOption( "skipJavaDocs" ) == false )

				.joglVersion( "2.2.4" )
				.aliceModelSourceVersion( "2015.08.13" )
				.nebulousModelSourceVersion( "2014.09.11" )

		//getUserProperties6File is expected to be in 6.9 even for 6.9.1
		.netBeans6Version( "6.9" )
		.netBeans8Version( "8.0.2" )

		.build();
		// @formatter:on

		JdkUtils.initialize();
		MavenUtils.initialize();
		AntUtils.initialize();
		NetBeansUtils.initialize( config );
		if( config.isInstallerDesired() ) {
			Install4JUtils.initialize();
		}

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

		java.util.List<Plugin> plugins = repo.getPlugins();
		if( plugins.size() > 0 ) {
			java.io.File tempDirectoryForJavaDoc = buildRepo.generateJavaDocs();
			timer.mark( "generateJavaDocs" );

			for( Plugin plugin : repo.getPlugins() ) {
				plugin.copyJars( buildRepo );
				timer.mark( "copyJars" + plugin.getVersion() );

				plugin.copyDistribution( buildRepo );
				timer.mark( "copyDistribution" + plugin.getVersion() );

				plugin.prepareFiles();
				timer.mark( "prepareFiles" + plugin.getVersion() );

				plugin.zipSrc( buildRepo );
				timer.mark( "zipSrc" + plugin.getVersion() );

				plugin.zipJavaDocs( tempDirectoryForJavaDoc );
				timer.mark( "zipJavaDocs" + plugin.getVersion() );

				if( config.getMode().isDev() ) {
					//pass
				} else {
					plugin.createNbm();
					timer.mark( "nbm" + plugin.getVersion() );
				}
			}
		}

		if( config.isInstallerDesired() ) {
			Installer installer = new Installer( config, repo.getRootDir() );

			installer.copyJarsFromMaven();
			timer.mark( "copyJarsFromMaven" );

			installer.copyJarsFromBuild( buildRepo );
			timer.mark( "copyJarsFromBuild" );

			installer.copyDistribution( buildRepo );
			timer.mark( "copyDistribution" );

			installer.prepareInstall4jFile();
			timer.mark( "prepareInstall4jFile" );

			if( config.getMode().isDev() ) {
				//pass
			} else {
				installer.createInstallers();
				timer.mark( "createInstallers" );
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
		if( config.isPlugin6Desired() ) {
			edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "netbeansUserProperties6:", NetBeansUtils.getUserProperties6File() );
		}
		if( config.isPlugin8Desired() ) {
			edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "netbeansUserProperties8:", NetBeansUtils.getUserProperties8File() );
		}

		edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "mavenCommandFile:", MavenUtils.getMavenCommandFile() );
		edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "antCommand:", AntUtils.getAntCommandFile() );
		edu.cmu.cs.dennisc.java.util.logging.Logger.outln();
		edu.cmu.cs.dennisc.java.util.logging.Logger.outln();
		edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "done" );
	}
}
