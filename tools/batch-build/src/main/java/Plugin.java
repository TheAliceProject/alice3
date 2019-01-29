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
import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.java.util.zip.ZipUtilities;
import edu.cmu.cs.dennisc.pattern.Criterion;
import org.apache.commons.io.FileUtils;
import org.lgna.project.ProjectVersion;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * @author Dennis Cosgrove
 */
public abstract class Plugin {
	public Plugin( Config config, File repoRoot, int version ) {
		this.config = config;
		this.version = version;
		this.root = new File( repoRoot, "alice/netbeans/" + version );
		assert this.root.exists() : this.root;
		assert this.root.isDirectory() : this.root;

		InputStream manifestInputStream = Build.class.getResourceAsStream( "NetBeans" + this.version + "Plugin/manifest.mf" );
		assert manifestInputStream != null;
		this.manifestText = PluginCommon.substituteVersionTexts( config, TextFileUtilities.read( manifestInputStream ) );
		assert this.manifestText != null;
		assert this.manifestText.length() > 0;

		InputStream libraryXmlInputStream = Build.class.getResourceAsStream( "NetBeans" + this.version + "Plugin/Alice3Library.xml" );
		assert libraryXmlInputStream != null;
		this.libraryXmlText = PluginCommon.substituteVersionTexts( config, TextFileUtilities.read( libraryXmlInputStream ) );
		assert this.libraryXmlText != null;
		assert this.libraryXmlText.length() > 0;

		InputStream projectXmlInputStream = Build.class.getResourceAsStream( "NetBeans" + this.version + "Plugin/project.xml" );
		assert projectXmlInputStream != null;
		this.projectXmlText = PluginCommon.substituteVersionTexts( config, TextFileUtilities.read( projectXmlInputStream ) );
		assert this.projectXmlText != null;
		assert this.projectXmlText.length() > 0;

		this.projectTemplateDir = new File( this.getRoot(), "ProjectTemplate" );
		assert this.projectTemplateDir.exists() : this.projectTemplateDir;
		assert this.projectTemplateDir.isDirectory() : this.projectTemplateDir;
	}

	protected abstract File getSuiteDir();

	protected abstract File getWizardDir();

	private File getWizardReleaseDir() {
		return new File( this.getWizardDir(), "release" );
	}

	protected final File getJarsDir() {
		return new File( this.getWizardReleaseDir(), "modules/ext" );
	}

	protected final File getDistributionDir() {
		return new File( this.getWizardReleaseDir(), "src/aliceSource.jar_root" );
	}

	protected final File getJavaDocZipFile() {
		return new File( this.getWizardReleaseDir(), "doc/aliceDocs.zip" );
	}

	protected final File getSrcZipFile() {
		return new File( this.getWizardReleaseDir(), "src/aliceSource.jar" );
	}

	protected abstract File getLibraryXmlFile();

	protected abstract File getProjectTemplateZipFile();

	protected abstract File getNbmFile();

	protected abstract File getJdkToUseForNbmAntCommand();

	protected Config getConfig() {
		return this.config;
	}

	protected File getRoot() {
		return this.root;
	}

	protected String getManifestText() {
		return this.manifestText;
	}

	protected String getLibraryXmlText() {
		return this.libraryXmlText;
	}

	protected String getProjectXmlText() {
		return this.projectXmlText;
	}

	public int getVersion() {
		return this.version;
	}

	public void copyJars( BuildRepo buildRepo ) throws IOException {
		ProjectCollection coreProjectCollection = new ProjectCollection.Builder( "core" )
				.addProjectNames(
						"util",
						"scenegraph",
						"glrender",
						"story-api",
						"ast",
						"story-api-migration" )
				.build();

		ProjectCollection nonfreeProjectCollection = new ProjectCollection.Builder( "nonfree/core" )
				.addProjectNames(
						"story-api-nonfree" )
				.build();

		if( this.getJarsDir().exists() ) {
			FileUtils.deleteDirectory( this.getJarsDir() );
			assert this.getJarsDir().exists() == false : this.getJarsDir();
		}

		buildRepo.copyJars( coreProjectCollection, this.getJarsDir() );
		buildRepo.copyJars( nonfreeProjectCollection, this.getJarsDir() );

		List<String> jarPathsToCopyFromMaven = PluginCommon.getJarPathsToCopyFromMaven( this.config );

		for( String mavenRepoJarPath : jarPathsToCopyFromMaven ) {
			File src = new File( MavenUtils.getMavenRepositoryDir(), mavenRepoJarPath );
			File dst = new File( this.getJarsDir(), src.getName() );
			assert src.exists() : src;
			FileUtilities.copyFile( src, dst );
			assert dst.exists() : dst;
			Logger.outln( dst );
		}
	}

	public void copyDistribution( BuildRepo buildRepo ) throws IOException {
		if( this.getDistributionDir().exists() ) {
			FileUtils.deleteDirectory( this.getDistributionDir() );
			assert this.getDistributionDir().exists() == false : this.getDistributionDir();
		}

		File distribSrc = buildRepo.getDistributionSourceDir();
		assert distribSrc.exists() : distribSrc;
		assert distribSrc.isDirectory() : distribSrc;
		FileUtilities.copyDirectory( distribSrc, this.getDistributionDir(), new Criterion<File>() {
			@Override
			public boolean accept( File file ) {
				if( file.isDirectory() ) {
					String directoryName = file.getName();
					if( directoryName.equals( "application" ) || directoryName.equals( "ffmpeg" ) || directoryName.equals( "libvlc" ) ) {
						return false;
					}
				}
				return true;
			}
		} );
	}

	public void prepareFiles() throws IOException {
		File dstManifestFile = new File( this.getWizardDir(), "manifest.mf" );
		FileSystemUtils.deleteIfExists( dstManifestFile );
		TextFileUtilities.write( dstManifestFile, this.getManifestText() );
		assert dstManifestFile.exists() : dstManifestFile;

		File dstLibraryXmlFile = this.getLibraryXmlFile();
		FileSystemUtils.deleteIfExists( dstLibraryXmlFile );
		TextFileUtilities.write( dstLibraryXmlFile, this.getLibraryXmlText() );
		assert dstLibraryXmlFile.exists() : dstLibraryXmlFile;

		File dstProjectXmlFile = new File( this.getWizardDir(), "nbproject/project.xml" );
		FileSystemUtils.deleteIfExists( dstProjectXmlFile );
		TextFileUtilities.write( dstProjectXmlFile, this.getProjectXmlText() );
		assert dstProjectXmlFile.exists() : dstProjectXmlFile;

		File projectZip = this.getProjectTemplateZipFile();
		ZipUtilities.zip( this.projectTemplateDir, projectZip );
		assert projectZip.exists() : projectZip;
	}

	public void zipJavaDocs( File tempDirectoryForJavaDoc ) throws IOException {
		File docZip = this.getJavaDocZipFile();
		FileUtilities.createParentDirectoriesIfNecessary( docZip );
		ZipUtilities.zip( tempDirectoryForJavaDoc, docZip );
		assert docZip.exists() : docZip;
	}

	public void zipSrc( BuildRepo buildRepo ) throws IOException {
		File srcDirectory = new File( buildRepo.getCoreSrcDirectory( "story-api" ), "org/lgna/story" );
		assert srcDirectory.exists() : srcDirectory;
		assert srcDirectory.isDirectory() : srcDirectory;
		File dstZip = this.getSrcZipFile();
		ZipUtilities.zipFilesInDirectory( srcDirectory, dstZip, new FileFilter() {
			@Override
			public boolean accept( File file ) {
				return "java".equals( FileUtilities.getExtension( file ) );
			}
		} );
	}

	private void _ant( String arg, File javaHomeDir ) throws IOException, InterruptedException {
		List<String> command = Lists.newLinkedList();
		command.add( AntUtils.getAntCommandFile().getAbsolutePath() );
		if( arg != null ) {
			command.add( arg );
		}

		ProcessBuilder processBuilder = new ProcessBuilder( command );
		if( javaHomeDir != null ) {
			Map<String, String> env = processBuilder.environment();
			env.put( "JAVA_HOME", javaHomeDir.getAbsolutePath() );
		}
		processBuilder.directory( this.getSuiteDir() );
		ProcessUtilities.startAndWaitFor( processBuilder, System.out, System.err );
	}

	private void antClean( File javaHomeDir ) throws IOException, InterruptedException {
		_ant( "clean", javaHomeDir );
	}

	private void antCompile( File javaHomeDir ) throws IOException, InterruptedException {
		_ant( null, javaHomeDir );
	}

	private void antNBM( File javaHomeDir ) throws IOException, InterruptedException {
		_ant( "nbms", javaHomeDir );
	}

	public void createNbm() throws IOException, InterruptedException {
		File nbm = this.getNbmFile();

		FileSystemUtils.deleteIfExists( nbm );

		File javaHomeDir = JdkUtils.getJdk8HomeDir();
		if( this.config.isCleanDesired() ) {
			antClean( javaHomeDir );
		}
		antCompile( javaHomeDir );
		antNBM( javaHomeDir );

		assert nbm.exists() : nbm;

		File nbmVersion = new File( FileUtilities.getDefaultDirectory(), "Alice3NetBeans" + this.version + "Plugin_" + ProjectVersion.getCurrentVersionText() + ".nbm" );
		FileSystemUtils.deleteIfExists( nbmVersion );

		FileUtilities.copyFile( nbm, nbmVersion );
		assert nbmVersion.exists() : nbmVersion;

		Logger.outln( nbmVersion );
	}

	private final Config config;
	private final int version;
	private final File root;
	private final String manifestText;
	private final String libraryXmlText;
	private final String projectXmlText;
	private final File projectTemplateDir;
}
