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
import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.java.util.zip.ZipUtilities;
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
public abstract class Plugin {
	Plugin(Config config, File repoRoot, int version) {
		this.config = config;
		this.version = version;
		this.root = new File( repoRoot, "alice/netbeans/" + version );
		assert this.root.exists() : this.root;
		assert this.root.isDirectory() : this.root;

		InputStream manifestInputStream = Build.class.getResourceAsStream( "NetBeans" + this.version + "Plugin/manifest.mf" );
		assert manifestInputStream != null;
		this.manifestText = substituteVersionTexts( config, TextFileUtilities.read( manifestInputStream ) );
		assert this.manifestText != null;
		assert this.manifestText.length() > 0;

		InputStream libraryXmlInputStream = Build.class.getResourceAsStream( "NetBeans" + this.version + "Plugin/Alice3Library.xml" );
		assert libraryXmlInputStream != null;
		this.libraryXmlText = substituteVersionTexts( config, TextFileUtilities.read( libraryXmlInputStream ) );
		assert this.libraryXmlText != null;
		assert this.libraryXmlText.length() > 0;

		InputStream projectXmlInputStream = Build.class.getResourceAsStream( "NetBeans" + this.version + "Plugin/project.xml" );
		assert projectXmlInputStream != null;
		this.projectXmlText = substituteVersionTexts( config, TextFileUtilities.read( projectXmlInputStream ) );
		assert this.projectXmlText != null;
		assert this.projectXmlText.length() > 0;

		this.projectTemplateDir = new File( this.getRoot(), "../src/main/resources/ProjectTemplate" );
		assert this.projectTemplateDir.exists() : this.projectTemplateDir;
		assert this.projectTemplateDir.isDirectory() : this.projectTemplateDir;
	}

	protected abstract File getSuiteDir();

	protected abstract File getWizardDir();

	private File getWizardReleaseDir() {
		return new File( this.getWizardDir(), "release" );
	}

	private File getJarsDir() {
		return new File( this.getWizardReleaseDir(), "modules/ext" );
	}

	private File getDistributionDir() {
		return new File( this.getWizardReleaseDir(), "src/aliceSource.jar_root" );
	}

	private File getJavaDocZipFile() {
		return new File( this.getWizardReleaseDir(), "doc/aliceDocs.zip" );
	}

	private File getSrcZipFile() {
		return new File( this.getWizardReleaseDir(), "src/aliceSource.jar" );
	}

	protected abstract File getLibraryXmlFile();

	protected abstract File getProjectTemplateZipFile();

	protected abstract File getNbmFile();

	File getRoot() {
		return this.root;
	}

	public int getVersion() {
		return this.version;
	}

	void copyJars( BuildRepo buildRepo ) throws IOException {
		ProjectCollection coreProjectCollection = new ProjectCollection.Builder( "core" )
				.addProjectNames(
						"util",
						"scenegraph",
						"glrender",
						"story-api",
						"ast",
						"story-api-migration",
						"tweedle")
				.build();

		ProjectCollection nonfreeProjectCollection = new ProjectCollection.Builder( "nonfree/core" )
				.addProjectNames(
						"story-api-nonfree" )
				.build();

		if( this.getJarsDir().exists() ) {
			FileUtils.deleteDirectory( this.getJarsDir() );
			assert !this.getJarsDir().exists() : this.getJarsDir();
		}

		buildRepo.copyJars( coreProjectCollection, this.getJarsDir() );
		buildRepo.copyJars( nonfreeProjectCollection, this.getJarsDir() );

		List<String> jarPathsToCopyFromMaven = getJarPathsToCopyFromMaven( this.config );

		final File mavenRepository = new File( FileUtilities.getUserDirectory(), ".m2/repository" );
		for( String mavenRepoJarPath : jarPathsToCopyFromMaven ) {
			File src = new File( mavenRepository, mavenRepoJarPath );
			File dst = new File( this.getJarsDir(), src.getName() );
			assert src.exists() : src;
			FileUtilities.copyFile( src, dst );
			assert dst.exists() : dst;
			Logger.outln( dst );
		}

	}

	void copyDistribution( BuildRepo buildRepo ) throws IOException {
		if( this.getDistributionDir().exists() ) {
			FileUtils.deleteDirectory( this.getDistributionDir() );
			assert !this.getDistributionDir().exists() : this.getDistributionDir();
		}
		File distribSrc = buildRepo.getDistributionSourceDir();
		assert distribSrc.exists() : distribSrc;
		assert distribSrc.isDirectory() : distribSrc;
		FileUtilities.copyDirectory( distribSrc, this.getDistributionDir(), file -> {
			if( file.isDirectory() ) {
				String directoryName = file.getName();
				return !directoryName.equals( "application" )
					&& !directoryName.equals( "ffmpeg" )
					&& !directoryName.equals( "libvlc" );
			}
			return true;
		} );
	}

	void prepareFiles() throws IOException {
		File dstManifestFile = new File( this.getWizardDir(), "manifest.mf" );
		FileSystemUtils.deleteIfExists( dstManifestFile );
		TextFileUtilities.write( dstManifestFile, manifestText );
		assert dstManifestFile.exists() : dstManifestFile;

		File dstLibraryXmlFile = this.getLibraryXmlFile();
		FileSystemUtils.deleteIfExists( dstLibraryXmlFile );
		TextFileUtilities.write( dstLibraryXmlFile, libraryXmlText );
		assert dstLibraryXmlFile.exists() : dstLibraryXmlFile;

		File dstProjectXmlFile = new File( this.getWizardDir(), "nbproject/project.xml" );
		FileSystemUtils.deleteIfExists( dstProjectXmlFile );
		TextFileUtilities.write( dstProjectXmlFile, projectXmlText );
		assert dstProjectXmlFile.exists() : dstProjectXmlFile;

		File projectZip = this.getProjectTemplateZipFile();
		ZipUtilities.zip( this.projectTemplateDir, projectZip );
		assert projectZip.exists() : projectZip;
	}

	void zipJavaDocs( File tempDirectoryForJavaDoc ) throws IOException {
		File docZip = this.getJavaDocZipFile();
		FileUtilities.createParentDirectoriesIfNecessary( docZip );
		ZipUtilities.zip( tempDirectoryForJavaDoc, docZip );
		assert docZip.exists() : docZip;
	}

	void zipSrc( BuildRepo buildRepo ) throws IOException {
		File srcDirectory = new File( buildRepo.getCoreSrcDirectory( "story-api" ), "org/lgna/story" );
		assert srcDirectory.exists() : srcDirectory;
		assert srcDirectory.isDirectory() : srcDirectory;
		File dstZip = this.getSrcZipFile();
		ZipUtilities.zipFilesInDirectory( srcDirectory, dstZip,
										  file -> "java".equals( FileUtilities.getExtension( file ) ) );
	}

	private void _ant( String arg ) throws IOException, InterruptedException {
		List<String> command = Lists.newLinkedList();
		command.add( "ant" );
		if( arg != null ) {
			command.add( arg );
		}
		BuildRepo.runCommand( getSuiteDir(), command );
	}

	private void antClean() throws IOException, InterruptedException {
		_ant( "clean" );
	}

	private void antCompile() throws IOException, InterruptedException {
		_ant( null );
	}

	private void antNBM() throws IOException, InterruptedException {
		_ant( "nbms" );
	}

	void createNbm() throws IOException, InterruptedException {
		File nbm = this.getNbmFile();

		FileSystemUtils.deleteIfExists( nbm );

		if( this.config.isCleanDesired() ) {
			antClean();
		}
		antCompile();
		antNBM();

		assert nbm.exists() : nbm;

		File nbmVersion = new File( FileUtilities.getDefaultDirectory(), "Alice3NetBeans" + this.version + "Plugin_" + ProjectVersion.getCurrentVersionText() + ".nbm" );
		FileSystemUtils.deleteIfExists( nbmVersion );

		FileUtilities.copyFile( nbm, nbmVersion );
		assert nbmVersion.exists() : nbmVersion;

		Logger.outln( nbmVersion );
	}

	private static String substituteVersionTexts( Config config, String s ) {
		s = s.trim();
		s = s.replaceAll( "___ALICE_VERSION___", ProjectVersion.getCurrentVersionText() );
		s = s.replaceAll( "___JOGL_VERSION___", config.getJoglVersion() );
		s = s.replaceAll( "___ALICE_MODEL_SOURCE_VERSION___", config.getAliceModelSourceVersion() );
		s = s.replaceAll( "___NEBULOUS_MODEL_SOURCE_VERSION___", config.getNebulousModelSourceVersion() );
		return s;
	}

	private static List<String> getJarPathsToCopyFromMaven( Config config ) {
		List<String> list = Lists.newLinkedList();
		list.add( substituteVersionTexts( config, "org/jogamp/gluegen/gluegen-rt/___JOGL_VERSION___/gluegen-rt-___JOGL_VERSION___.jar" ) );
		list.add( substituteVersionTexts( config, "org/jogamp/jogl/jogl-all/___JOGL_VERSION___/jogl-all-___JOGL_VERSION___.jar" ) );
		list.add( "javax/media/jmf/2.1.1e/jmf-2.1.1e.jar" );
		list.add( "com/sun/javamp3/1.0/javamp3-1.0.jar" );
		list.add( "org/apache/commons/commons-text/1.1/commons-text-1.1.jar" );
		list.add( "org/apache/commons/commons-lang3/3.5/commons-lang3-3.5.jar" );
		list.add( "com/sun/media/jai-codec/1.1.3/jai-codec-1.1.3.jar" );
		list.add( "javax/media/jai-core/1.1.3/jai-core-1.1.3.jar" );
		list.add( "com/google/code/gson/gson/2.8.2/gson-2.8.2.jar" );
		list.add( substituteVersionTexts( config, "org/alice/alice-model-source/___ALICE_MODEL_SOURCE_VERSION___/alice-model-source-___ALICE_MODEL_SOURCE_VERSION___.jar" ) );
		list.add( substituteVersionTexts( config, "org/alice/nonfree/nebulous-model-source/___NEBULOUS_MODEL_SOURCE_VERSION___/nebulous-model-source-___NEBULOUS_MODEL_SOURCE_VERSION___.jar" ) );
		return Collections.unmodifiableList( list );
	}

	private final Config config;
	private final int version;
	private final File root;
	private final String manifestText;
	private final String libraryXmlText;
	private final String projectXmlText;
	private final File projectTemplateDir;
}
