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
public abstract class Plugin {
	public Plugin( Config config, java.io.File repoRoot, int version ) {
		this.config = config;
		this.version = version;
		this.root = new java.io.File( repoRoot, "alice/netbeans/" + version );
		assert this.root.exists() : this.root;
		assert this.root.isDirectory() : this.root;

		java.io.InputStream manifestInputStream = Build.class.getResourceAsStream( "NetBeans" + this.version + "Plugin/manifest.mf" );
		assert manifestInputStream != null;
		this.manifestText = PluginCommon.substituteVersionTexts( config, edu.cmu.cs.dennisc.java.io.TextFileUtilities.read( manifestInputStream ) );
		assert this.manifestText != null;
		assert this.manifestText.length() > 0;

		java.io.InputStream libraryXmlInputStream = Build.class.getResourceAsStream( "NetBeans" + this.version + "Plugin/Alice3Library.xml" );
		assert libraryXmlInputStream != null;
		this.libraryXmlText = PluginCommon.substituteVersionTexts( config, edu.cmu.cs.dennisc.java.io.TextFileUtilities.read( libraryXmlInputStream ) );
		assert this.libraryXmlText != null;
		assert this.libraryXmlText.length() > 0;

		java.io.InputStream projectXmlInputStream = Build.class.getResourceAsStream( "NetBeans" + this.version + "Plugin/project.xml" );
		assert projectXmlInputStream != null;
		this.projectXmlText = PluginCommon.substituteVersionTexts( config, edu.cmu.cs.dennisc.java.io.TextFileUtilities.read( projectXmlInputStream ) );
		assert this.projectXmlText != null;
		assert this.projectXmlText.length() > 0;

		this.projectTemplateDir = new java.io.File( this.getRoot(), "ProjectTemplate" );
		assert this.projectTemplateDir.exists() : this.projectTemplateDir;
		assert this.projectTemplateDir.isDirectory() : this.projectTemplateDir;
	}

	protected abstract java.io.File getSuiteDir();

	protected abstract java.io.File getWizardDir();

	private java.io.File getWizardReleaseDir() {
		return new java.io.File( this.getWizardDir(), "release" );
	}

	protected final java.io.File getJarsDir() {
		return new java.io.File( this.getWizardReleaseDir(), "modules/ext" );
	}

	protected final java.io.File getDistributionDir() {
		return new java.io.File( this.getWizardReleaseDir(), "src/aliceSource.jar_root" );
	}

	protected final java.io.File getJavaDocZipFile() {
		return new java.io.File( this.getWizardReleaseDir(), "doc/aliceDocs.zip" );
	}

	protected final java.io.File getSrcZipFile() {
		return new java.io.File( this.getWizardReleaseDir(), "src/aliceSource.jar" );
	}

	protected abstract java.io.File getLibraryXmlFile();

	protected abstract java.io.File getProjectTemplateZipFile();

	protected abstract java.io.File getNbmFile();

	protected abstract java.io.File getJdkToUseForNbmAntCommand();

	protected Config getConfig() {
		return this.config;
	}

	protected java.io.File getRoot() {
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

	public void copyJars( BuildRepo buildRepo ) throws java.io.IOException {
		ProjectCollection coreProjectCollection = new ProjectCollection.Builder( "core" ).addProjectNames( "util", "scenegraph", "glrender", "story-api", "ast", "story-api-migration" ).build();

		if( this.getJarsDir().exists() ) {
			org.apache.commons.io.FileUtils.deleteDirectory( this.getJarsDir() );
			assert this.getJarsDir().exists() == false : this.getJarsDir();
		}

		buildRepo.copyJars( coreProjectCollection, this.getJarsDir() );

		java.util.List<String> jarPathsToCopyFromMaven = PluginCommon.getJarPathsToCopyFromMaven( this.config );

		for( String mavenRepoJarPath : jarPathsToCopyFromMaven ) {
			java.io.File src = new java.io.File( MavenUtils.getMavenRepositoryDir(), mavenRepoJarPath );
			java.io.File dst = new java.io.File( this.getJarsDir(), src.getName() );
			assert src.exists() : src;
			edu.cmu.cs.dennisc.java.io.FileUtilities.copyFile( src, dst );
			assert dst.exists() : dst;
			edu.cmu.cs.dennisc.java.util.logging.Logger.outln( dst );
		}
	}

	public void copyDistribution( BuildRepo buildRepo ) throws java.io.IOException {
		if( this.getDistributionDir().exists() ) {
			org.apache.commons.io.FileUtils.deleteDirectory( this.getDistributionDir() );
			assert this.getDistributionDir().exists() == false : this.getDistributionDir();
		}

		java.io.File distribSrc = buildRepo.getDistributionSourceDir();
		assert distribSrc.exists() : distribSrc;
		assert distribSrc.isDirectory() : distribSrc;
		edu.cmu.cs.dennisc.java.io.FileUtilities.copyDirectory( distribSrc, this.getDistributionDir(), new edu.cmu.cs.dennisc.pattern.Criterion<java.io.File>() {
			@Override
			public boolean accept( java.io.File file ) {
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

	public void prepareFiles() throws java.io.IOException {
		java.io.File dstManifestFile = new java.io.File( this.getWizardDir(), "manifest.mf" );
		edu.cmu.cs.dennisc.java.io.FileSystemUtils.deleteIfExists( dstManifestFile );
		edu.cmu.cs.dennisc.java.io.TextFileUtilities.write( dstManifestFile, this.getManifestText() );
		assert dstManifestFile.exists() : dstManifestFile;

		java.io.File dstLibraryXmlFile = this.getLibraryXmlFile();
		edu.cmu.cs.dennisc.java.io.FileSystemUtils.deleteIfExists( dstLibraryXmlFile );
		edu.cmu.cs.dennisc.java.io.TextFileUtilities.write( dstLibraryXmlFile, this.getLibraryXmlText() );
		assert dstLibraryXmlFile.exists() : dstLibraryXmlFile;

		java.io.File dstProjectXmlFile = new java.io.File( this.getWizardDir(), "nbproject/project.xml" );
		edu.cmu.cs.dennisc.java.io.FileSystemUtils.deleteIfExists( dstProjectXmlFile );
		edu.cmu.cs.dennisc.java.io.TextFileUtilities.write( dstProjectXmlFile, this.getProjectXmlText() );
		assert dstProjectXmlFile.exists() : dstProjectXmlFile;

		java.io.File projectZip = this.getProjectTemplateZipFile();
		edu.cmu.cs.dennisc.java.util.zip.ZipUtilities.zip( this.projectTemplateDir, projectZip );
		assert projectZip.exists() : projectZip;

		java.io.File userPropertiesFile = NetBeansUtils.getUserPropertiesFile( this.version );
		java.io.File platformPrivatePropertiesFile = new java.io.File( this.getSuiteDir(), "nbproject/private/platform-private.properties" );
		edu.cmu.cs.dennisc.java.io.TextFileUtilities.write( platformPrivatePropertiesFile, "user.properties.file=" + userPropertiesFile.getAbsolutePath().replaceAll( "\\\\", "\\\\\\\\" ) );
	}

	public void zipJavaDocs( java.io.File tempDirectoryForJavaDoc ) throws java.io.IOException {
		java.io.File docZip = this.getJavaDocZipFile();
		edu.cmu.cs.dennisc.java.io.FileUtilities.createParentDirectoriesIfNecessary( docZip );
		edu.cmu.cs.dennisc.java.util.zip.ZipUtilities.zip( tempDirectoryForJavaDoc, docZip );
		assert docZip.exists() : docZip;
	}

	public void zipSrc( BuildRepo buildRepo ) throws java.io.IOException {
		java.io.File srcDirectory = new java.io.File( buildRepo.getCoreSrcDirectory( "story-api" ), "org/lgna/story" );
		assert srcDirectory.exists() : srcDirectory;
		assert srcDirectory.isDirectory() : srcDirectory;
		java.io.File dstZip = this.getSrcZipFile();
		edu.cmu.cs.dennisc.java.util.zip.ZipUtilities.zipFilesInDirectory( srcDirectory, dstZip, new java.io.FileFilter() {
			@Override
			public boolean accept( java.io.File file ) {
				return "java".equals( edu.cmu.cs.dennisc.java.io.FileUtilities.getExtension( file ) );
			}
		} );
	}

	private void _ant( String arg, java.io.File javaHomeDir ) throws java.io.IOException, InterruptedException {
		java.util.List<String> command = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
		command.add( AntUtils.getAntCommandFile().getAbsolutePath() );
		if( arg != null ) {
			command.add( arg );
		}

		ProcessBuilder processBuilder = new ProcessBuilder( command );
		if( javaHomeDir != null ) {
			java.util.Map<String, String> env = processBuilder.environment();
			env.put( "JAVA_HOME", javaHomeDir.getAbsolutePath() );
		}
		processBuilder.directory( this.getSuiteDir() );
		edu.cmu.cs.dennisc.java.lang.ProcessUtilities.startAndWaitFor( processBuilder, System.out, System.err );
	}

	private void antClean( java.io.File javaHomeDir ) throws java.io.IOException, InterruptedException {
		_ant( "clean", javaHomeDir );
	}

	private void antCompile( java.io.File javaHomeDir ) throws java.io.IOException, InterruptedException {
		_ant( null, javaHomeDir );
	}

	private void antNBM( java.io.File javaHomeDir ) throws java.io.IOException, InterruptedException {
		_ant( "nbms", javaHomeDir );
	}

	public void createNbm() throws java.io.IOException, InterruptedException {
		java.io.File nbm = this.getNbmFile();

		edu.cmu.cs.dennisc.java.io.FileSystemUtils.deleteIfExists( nbm );

		java.io.File javaHomeDir = JdkUtils.getJdk8HomeDir();
		if( this.config.isCleanDesired() ) {
			antClean( javaHomeDir );
		}
		antCompile( javaHomeDir );
		antNBM( javaHomeDir );

		assert nbm.exists() : nbm;

		java.io.File nbmVersion = new java.io.File( edu.cmu.cs.dennisc.java.io.FileUtilities.getDefaultDirectory(), "Alice3NetBeans" + this.version + "Plugin_" + org.lgna.project.ProjectVersion.getCurrentVersionText() + ".nbm" );
		edu.cmu.cs.dennisc.java.io.FileSystemUtils.deleteIfExists( nbmVersion );

		edu.cmu.cs.dennisc.java.io.FileUtilities.copyFile( nbm, nbmVersion );
		assert nbmVersion.exists() : nbmVersion;

		edu.cmu.cs.dennisc.java.util.logging.Logger.outln( nbmVersion );
	}

	private final Config config;
	private final int version;
	private final java.io.File root;
	private final String manifestText;
	private final String libraryXmlText;
	private final String projectXmlText;
	private final java.io.File projectTemplateDir;
}
