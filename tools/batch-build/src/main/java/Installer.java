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

import edu.cmu.cs.dennisc.java.io.FileSystemUtils;
import edu.cmu.cs.dennisc.java.io.FileUtilities;
import edu.cmu.cs.dennisc.java.io.TextFileUtilities;
import edu.cmu.cs.dennisc.java.lang.ProcessUtilities;
import edu.cmu.cs.dennisc.java.lang.SystemUtilities;
import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.lgna.project.ProjectVersion;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

/**
 * @author Dennis Cosgrove
 */
public class Installer {
	public Installer( Config config, File repoRoot ) {
		this.config = config;
		this.root = new File( repoRoot, "installer" );
		assert this.root.exists() : this.root;
		assert this.root.isDirectory() : this.root;

		InputStream is = Build.class.getResourceAsStream( "Installer/aliceinstallerproject.install4j" );
		assert is != null;
		this.markedUpInstall4jText = TextFileUtilities.read( is );
		assert this.markedUpInstall4jText != null;
		assert this.markedUpInstall4jText.length() > 0;
	}

	private File getTargetDir() {
		return new File( this.root, "aliceInstallData/Alice3" );
	}

	private static boolean isFileNameStartsWith( File file, List<String> candidates ) {
		String name = file.getName();
		for( String candidate : candidates ) {
			if( name.startsWith( candidate ) ) {
				return true;
			}
		}
		return false;
	}

	public void copyJarsFromMaven() throws IOException {
		final List<String> batchDependencyPrefixes = Collections.unmodifiableList( Lists.newArrayList(
				"commons-io",
				"commons-cli" ) );
		File extDir = new File( this.getTargetDir(), "ext" );
		if( extDir.exists() ) {
			FileUtils.deleteDirectory( extDir );
		}
		File mavenRepositoryRootDirectory = MavenUtils.getMavenRepositoryDir();
		for( String pathname : SystemUtilities.getClassPath() ) {
			File file = new File( pathname );
			if( file.isDirectory() ) {
				Logger.errln( "skipping directory:", file );
			} else {
				if( isFileNameStartsWith( file, batchDependencyPrefixes ) ) {
					Logger.errln( "skipping batch dependency:", file );
				} else {
					if( FileUtilities.isExtensionAmoung( file, "jar" ) ) {
						if( FileUtilities.isDescendantOf( file, mavenRepositoryRootDirectory ) ) {
							File dstFile = FileUtilities.getAnalogousFile( file, mavenRepositoryRootDirectory, extDir );
							FileUtilities.copyFile( file, dstFile );
							Logger.outln( "++ adding: " + dstFile );
						} else {
							Logger.errln( "skipping non descendant:", file );
						}
					} else {
						Logger.errln( "skipping non jar:", file );
					}
				}
			}
		}
		System.err.flush();
	}

	public void copyJarsFromBuild( BuildRepo buildRepo ) throws IOException {
		File libDir = new File( this.getTargetDir(), "lib" );
		if( libDir.exists() ) {
			FileUtils.deleteDirectory( libDir );
		}

		List<ProjectCollection> projectCollections = Lists.newLinkedList();

		projectCollections.add( new ProjectCollection.Builder( "core" )
				.addProjectNames(
						"util",
						"scenegraph",
						"glrender",
						"story-api",
						"ast",
						"story-api-migration",
						"croquet",
						"i18n",
						"image-editor",
						"issue-reporting",
						"ide" )
				.build() );

		projectCollections.add( new ProjectCollection.Builder( "alice" )
				.addProjectNames(
						"alice-ide" )
				.build() );

		projectCollections.add( new ProjectCollection.Builder( "nonfree/core" )
				.addProjectNames(
						"ide-nonfree",
						"resources-nonfree",
						"story-api-nonfree" )
				.build() );

		for( ProjectCollection projectCollection : projectCollections ) {
			buildRepo.copyJars( projectCollection, libDir );
		}
	}

	public void copyDistribution( BuildRepo buildRepo ) throws IOException {
		File distibDst = this.getTargetDir();

		File distribSrc = buildRepo.getDistributionSourceDir();
		assert distribSrc.exists() : distribSrc;
		assert distribSrc.isDirectory() : distribSrc;
		FileUtilities.copyDirectory( distribSrc, distibDst );

		assert distibDst.isDirectory() : distibDst;
	}

	private File getInstall4JFile() {
		return new File( this.root, "aliceinstallerproject.install4j" );
	}

	public void prepareInstall4jFile() throws IOException {
		String[] dirNames = { "ext", "lib" };

		List<File> jarFiles = Lists.newLinkedList();
		for( String dirName : dirNames ) {
			File[] descendants = FileUtilities.listDescendants( new File( this.getTargetDir(), dirName ), "jar" );
			Collections.addAll( jarFiles, descendants );
		}

		Collections.sort( jarFiles );

		final int N = this.getTargetDir().getAbsolutePath().length() + 1;
		StringBuilder sb = new StringBuilder();
		for( File jarFile : jarFiles ) {
			sb.append( "          <archive location=\"" + jarFile.getAbsolutePath().substring( N ).replaceAll( "\\\\", "/" ) + "\" failOnError=\"true\" />\n" );
		}

		String finalInstall4jText = this.markedUpInstall4jText;
		finalInstall4jText = finalInstall4jText.replaceAll( "___ALICE_VERSION___", ProjectVersion.getCurrentVersionText() );
		finalInstall4jText = finalInstall4jText.replaceAll( "___CLASSPATH___", sb.toString() );
		finalInstall4jText = finalInstall4jText.replaceAll( "___INCLUDED_JVM_VERSION___", config.getInstallerIncludedJvmVersion() );

		Logger.outln( sb );

		File dstInstall4JFile = this.getInstall4JFile();
		FileSystemUtils.deleteIfExists( dstInstall4JFile );
		TextFileUtilities.write( dstInstall4JFile, finalInstall4jText );
		assert dstInstall4JFile.exists() : dstInstall4JFile;
	}

	public void createInstallers( Config config ) throws InterruptedException, IOException {
		File installerOutputDir = new File( this.root, "installerOutput" );
		String underscoreVersionText = ProjectVersion.getCurrentVersionText().replaceAll( "\\.", "_" );
		File win64File = new File( installerOutputDir, "Alice3_windows-x64_" + underscoreVersionText + ".exe" );
		File win32File = new File( installerOutputDir, "Alice3_windows_" + underscoreVersionText + ".exe" );
		File linuxFile = new File( installerOutputDir, "Alice3_unix_" + underscoreVersionText + ".sh" );
		File macFile = new File( installerOutputDir, "Alice3_macos_" + underscoreVersionText + ".dmg" );

		FileSystemUtils.deleteIfExists( win64File );
		FileSystemUtils.deleteIfExists( win32File );
		FileSystemUtils.deleteIfExists( linuxFile );
		FileSystemUtils.deleteIfExists( macFile );

		List<String> command = Lists.newLinkedList();
		command.add( Install4JUtils.getInstall4JCommandFile( config ).getAbsolutePath() );
		command.add( "--build-selected" );
		command.add( this.getInstall4JFile().getAbsolutePath() );
		ProcessBuilder processBuilder = new ProcessBuilder( command );
		ProcessUtilities.startAndWaitFor( processBuilder, System.out, System.err );

		assert win64File.exists() : win64File;
		assert win32File.exists() : win32File;
		assert linuxFile.exists() : linuxFile;
		assert macFile.exists() : macFile;

		//todo: copy files
	}

	private final Config config;
	private final File root;
	private final String markedUpInstall4jText;
}
