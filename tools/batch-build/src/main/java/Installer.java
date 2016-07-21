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
public class Installer {
	public Installer( Config config, java.io.File repoRoot ) {
		this.config = config;
		this.root = new java.io.File( repoRoot, "installer" );
		assert this.root.exists() : this.root;
		assert this.root.isDirectory() : this.root;

		java.io.InputStream is = Build.class.getResourceAsStream( "Installer/aliceinstallerproject.install4j" );
		assert is != null;
		this.markedUpInstall4jText = edu.cmu.cs.dennisc.java.io.TextFileUtilities.read( is );
		assert this.markedUpInstall4jText != null;
		assert this.markedUpInstall4jText.length() > 0;
	}

	private java.io.File getTargetDir() {
		return new java.io.File( this.root, "aliceInstallData/Alice3" );
	}

	private static boolean isFileNameStartsWith( java.io.File file, java.util.List<String> candidates ) {
		String name = file.getName();
		for( String candidate : candidates ) {
			if( name.startsWith( candidate ) ) {
				return true;
			}
		}
		return false;
	}

	public void copyJarsFromMaven() throws java.io.IOException {
		final java.util.List<String> batchDependencyPrefixes = java.util.Collections.unmodifiableList( edu.cmu.cs.dennisc.java.util.Lists.newArrayList(
				"commons-io",
				"commons-cli" ) );
		java.io.File extDir = new java.io.File( this.getTargetDir(), "ext" );
		if( extDir.exists() ) {
			org.apache.commons.io.FileUtils.deleteDirectory( extDir );
		}
		java.io.File mavenRepositoryRootDirectory = MavenUtils.getMavenRepositoryDir();
		for( String pathname : edu.cmu.cs.dennisc.java.lang.SystemUtilities.getClassPath() ) {
			java.io.File file = new java.io.File( pathname );
			if( file.isDirectory() ) {
				edu.cmu.cs.dennisc.java.util.logging.Logger.errln( "skipping directory:", file );
			} else {
				if( isFileNameStartsWith( file, batchDependencyPrefixes ) ) {
					edu.cmu.cs.dennisc.java.util.logging.Logger.errln( "skipping batch dependency:", file );
				} else {
					if( edu.cmu.cs.dennisc.java.io.FileUtilities.isExtensionAmoung( file, "jar" ) ) {
						if( edu.cmu.cs.dennisc.java.io.FileUtilities.isDescendantOf( file, mavenRepositoryRootDirectory ) ) {
							java.io.File dstFile = edu.cmu.cs.dennisc.java.io.FileUtilities.getAnalogousFile( file, mavenRepositoryRootDirectory, extDir );
							edu.cmu.cs.dennisc.java.io.FileUtilities.copyFile( file, dstFile );
							edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "++ adding: " + dstFile );
						} else {
							edu.cmu.cs.dennisc.java.util.logging.Logger.errln( "skipping non descendant:", file );
						}
					} else {
						edu.cmu.cs.dennisc.java.util.logging.Logger.errln( "skipping non jar:", file );
					}
				}
			}
		}
		System.err.flush();
	}

	public void copyJarsFromBuild( BuildRepo buildRepo ) throws java.io.IOException {
		java.io.File libDir = new java.io.File( this.getTargetDir(), "lib" );
		if( libDir.exists() ) {
			org.apache.commons.io.FileUtils.deleteDirectory( libDir );
		}

		java.util.List<ProjectCollection> projectCollections = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();

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

	public void copyDistribution( BuildRepo buildRepo ) throws java.io.IOException {
		java.io.File distibDst = this.getTargetDir();

		java.io.File distribSrc = buildRepo.getDistributionSourceDir();
		assert distribSrc.exists() : distribSrc;
		assert distribSrc.isDirectory() : distribSrc;
		edu.cmu.cs.dennisc.java.io.FileUtilities.copyDirectory( distribSrc, distibDst );

		assert distibDst.isDirectory() : distibDst;
	}

	private java.io.File getInstall4JFile() {
		return new java.io.File( this.root, "aliceinstallerproject.install4j" );
	}

	public void prepareInstall4jFile() throws java.io.IOException {
		String[] dirNames = { "ext", "lib" };

		java.util.List<java.io.File> jarFiles = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
		for( String dirName : dirNames ) {
			java.io.File[] descendants = edu.cmu.cs.dennisc.java.io.FileUtilities.listDescendants( new java.io.File( this.getTargetDir(), dirName ), "jar" );
			java.util.Collections.addAll( jarFiles, descendants );
		}

		java.util.Collections.sort( jarFiles );

		final int N = this.getTargetDir().getAbsolutePath().length() + 1;
		StringBuilder sb = new StringBuilder();
		for( java.io.File jarFile : jarFiles ) {
			sb.append( "          <archive location=\"" + jarFile.getAbsolutePath().substring( N ).replaceAll( "\\\\", "/" ) + "\" failOnError=\"true\" />\n" );
		}

		String finalInstall4jText = this.markedUpInstall4jText;
		finalInstall4jText = finalInstall4jText.replaceAll( "___ALICE_VERSION___", org.lgna.project.ProjectVersion.getCurrentVersionText() );
		finalInstall4jText = finalInstall4jText.replaceAll( "___CLASSPATH___", sb.toString() );
		finalInstall4jText = finalInstall4jText.replaceAll( "___INCLUDED_JVM_VERSION___", config.getInstallerIncludedJvmVersion() );

		edu.cmu.cs.dennisc.java.util.logging.Logger.outln( sb );

		java.io.File dstInstall4JFile = this.getInstall4JFile();
		edu.cmu.cs.dennisc.java.io.FileSystemUtils.deleteIfExists( dstInstall4JFile );
		edu.cmu.cs.dennisc.java.io.TextFileUtilities.write( dstInstall4JFile, finalInstall4jText );
		assert dstInstall4JFile.exists() : dstInstall4JFile;
	}

	public void createInstallers() throws InterruptedException, java.io.IOException {
		java.io.File installerOutputDir = new java.io.File( this.root, "installerOutput" );
		String underscoreVersionText = org.lgna.project.ProjectVersion.getCurrentVersionText().replaceAll( "\\.", "_" );
		java.io.File win64File = new java.io.File( installerOutputDir, "Alice3_windows-x64_Offline_" + underscoreVersionText + ".exe" );
		java.io.File win32File = new java.io.File( installerOutputDir, "Alice3_windows_Offline_" + underscoreVersionText + ".exe" );
		java.io.File linuxFile = new java.io.File( installerOutputDir, "Alice3_unix_Offline_" + underscoreVersionText + ".sh" );
		java.io.File macFile = new java.io.File( installerOutputDir, "Alice3_macos_Offline_" + underscoreVersionText + ".dmg" );

		edu.cmu.cs.dennisc.java.io.FileSystemUtils.deleteIfExists( win64File );
		edu.cmu.cs.dennisc.java.io.FileSystemUtils.deleteIfExists( win32File );
		edu.cmu.cs.dennisc.java.io.FileSystemUtils.deleteIfExists( linuxFile );
		edu.cmu.cs.dennisc.java.io.FileSystemUtils.deleteIfExists( macFile );

		java.util.List<String> command = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
		command.add( Install4JUtils.getInstall4JCommandFile().getAbsolutePath() );
		command.add( "--build-selected" );
		command.add( this.getInstall4JFile().getAbsolutePath() );
		ProcessBuilder processBuilder = new ProcessBuilder( command );
		edu.cmu.cs.dennisc.java.lang.ProcessUtilities.startAndWaitFor( processBuilder, System.out, System.err );

		assert win64File.exists() : win64File;
		assert win32File.exists() : win32File;
		assert linuxFile.exists() : linuxFile;
		assert macFile.exists() : macFile;

		//todo: copy files
	}

	private final Config config;
	private final java.io.File root;
	private final String markedUpInstall4jText;
}
