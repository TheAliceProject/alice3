/*******************************************************************************
 * Copyright (c) 2006, 2016, Carnegie Mellon University. All rights reserved.
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

import edu.cmu.cs.dennisc.java.io.FileUtilities;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.timing.Timer;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import java.io.File;
import java.util.List;

/**
 * @author Dennis Cosgrove
 */
public class Build {
	public static void main( String[] args ) throws Exception {
		Options options = new Options();
		options.addOption( new Option( "isDev", "mode=Mode.DEV" ) );
		options.addOption( new Option( "skipPlugin8", "isPlugin8Desired=false" ) );
		options.addOption( new Option( "skipInstaller", "isInstallerDesired=false" ) );
		options.addOption( new Option( "skipClean", "isCleanDesired=false" ) );
		options.addOption( new Option( "skipJavaDocs", "isJavaDocGenerationDesired=false" ) );

		CommandLineParser parser = new DefaultParser();
		CommandLine commandLine = parser.parse( options, args );

		// @formatter:off
		Config config = new Config.Builder()
				.rootDir( new File( FileUtilities.getDefaultDirectory(), "Projects/Alice/Code" ) )
				.mode( commandLine.hasOption( "isDev" ) ? Mode.DEV : Mode.BUILD )

		.isPlugin8Desired( commandLine.hasOption( "skipPlugin8" ) == false )
		.isInstallerDesired( commandLine.hasOption( "skipInstaller" ) == false )

		.isCleanDesired( commandLine.hasOption( "skipClean" ) == false )
		.isJavaDocGenerationDesired( commandLine.hasOption( "skipJavaDocs" ) == false )

				.joglVersion( "2.3.2" )
				.aliceModelSourceVersion( "2016.08.19" )
				.nebulousModelSourceVersion( "2016.07.15" )

		.netBeans8Version( "8.1" )

		.installerIncludedJvmVersion( "1.8.0_102" )

		.build();
		// @formatter:on

		JdkUtils.initialize();
		MavenUtils.initialize();
		AntUtils.initialize();
		if( config.isInstallerDesired() ) {
			Install4JUtils.initialize( config );
		}

		BuildRepo buildRepo = new BuildRepo( config );
		GitRepo repo;
		if( config.getMode().isDev() ) {
			repo = new DevRepo( config );
		} else {
			repo = buildRepo;
		}

		Timer timer = new Timer( "build" );
		timer.start();
		buildRepo.compileJars();
		timer.mark( "compileJars" );

		List<Plugin> plugins = repo.getPlugins();
		if( plugins.size() > 0 ) {
			File tempDirectoryForJavaDoc = buildRepo.generateJavaDocs();
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
				installer.createInstallers( config );
				timer.mark( "createInstallers" );
			}
		}

		Logger.outln();
		Logger.outln();

		timer.stopAndPrintResults();

		Logger.outln();
		Logger.outln();
		Logger.outln( config );
		Logger.outln( "assertions:", Build.class.desiredAssertionStatus() );
		Logger.outln( "javaHomeDir:", JdkUtils.getJavaHomeDir() );

		Logger.outln( "mavenCommandFile:", MavenUtils.getMavenCommandFile() );
		Logger.outln( "antCommand:", AntUtils.getAntCommandFile() );
		Logger.outln();
		Logger.outln();
		Logger.outln( "done" );
	}
}
