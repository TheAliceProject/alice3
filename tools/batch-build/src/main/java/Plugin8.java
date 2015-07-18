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
public class Plugin8 extends Plugin {
	public Plugin8( Config config, java.io.File repoRoot ) {
		super( repoRoot, 8 );
		this.suite = new java.io.File( repoRoot, "alice/netbeans/8/Alice3ModuleSuite" );
		assert this.suite.exists() : this.suite;
		assert this.suite.isDirectory() : this.suite;

		this.jars = new java.io.File( this.suite, "Alice3Module/release/modules/ext" );
		assert this.jars.exists() : this.jars;
		assert this.jars.isDirectory() : this.jars;

		this.distribution = new java.io.File( this.suite, "Alice3Module/release/src/aliceSource.jar_root" );

		this.projectTemplate = new java.io.File( repoRoot, "alice/netbeans/8/ProjectTemplate" );
		assert this.projectTemplate.exists() : this.projectTemplate;
		assert this.projectTemplate.isDirectory() : this.projectTemplate;

		this.dstManifestFile = new java.io.File( this.getSuite(), "Alice3Module/manifest.mf" );
		this.dstLibraryXmlFile = new java.io.File( this.getSuite(), "Alice3Module/src/org/alice/netbeans/Alice3Library.xml" );
		this.dstProjectXmlFile = new java.io.File( this.getSuite(), "Alice3Module/nbproject/project.xml" );

		java.io.InputStream manifestInputStream = Build.class.getResourceAsStream( "NetBeans8Plugin/manifest.mf" );
		assert manifestInputStream != null;
		this.manifestText = PluginCommon.substituteVersionTexts( config, edu.cmu.cs.dennisc.java.io.TextFileUtilities.read( manifestInputStream ) );
		assert this.manifestText != null;
		assert this.manifestText.length() > 0;

		java.io.InputStream libraryXmlInputStream = Build.class.getResourceAsStream( "NetBeans8Plugin/Alice3Library.xml" );
		assert libraryXmlInputStream != null;
		this.libraryXmlText = PluginCommon.substituteVersionTexts( config, edu.cmu.cs.dennisc.java.io.TextFileUtilities.read( libraryXmlInputStream ) );
		assert this.libraryXmlText != null;
		assert this.libraryXmlText.length() > 0;

		java.io.InputStream projectXmlInputStream = Build.class.getResourceAsStream( "NetBeans8Plugin/project.xml" );
		assert projectXmlInputStream != null;
		this.projectXmlText = PluginCommon.substituteVersionTexts( config, edu.cmu.cs.dennisc.java.io.TextFileUtilities.read( projectXmlInputStream ) );
		assert this.projectXmlText != null;
		assert this.projectXmlText.length() > 0;

		this.jarPathsToCopyFromMaven = PluginCommon.getJarPathsToCopyFromMaven( config );
	}

	public java.io.File getSuite() {
		return this.suite;
	}

	public java.io.File getJars() {
		return this.jars;
	}

	public java.io.File getDistribution() {
		return this.distribution;
	}

	public java.io.File getProjectTemplate() {
		return this.projectTemplate;
	}

	public void prepareFiles() throws java.io.IOException {
		edu.cmu.cs.dennisc.java.io.FileSystemUtils.deleteIfExists( this.dstManifestFile );
		edu.cmu.cs.dennisc.java.io.TextFileUtilities.write( this.dstManifestFile, this.manifestText );
		assert this.dstManifestFile.exists() : this.dstManifestFile;

		edu.cmu.cs.dennisc.java.io.FileSystemUtils.deleteIfExists( this.dstLibraryXmlFile );
		edu.cmu.cs.dennisc.java.io.TextFileUtilities.write( this.dstLibraryXmlFile, this.libraryXmlText );
		assert this.dstLibraryXmlFile.exists() : this.dstLibraryXmlFile;

		edu.cmu.cs.dennisc.java.io.FileSystemUtils.deleteIfExists( this.dstProjectXmlFile );
		edu.cmu.cs.dennisc.java.io.TextFileUtilities.write( this.dstProjectXmlFile, this.projectXmlText );
		assert this.dstProjectXmlFile.exists() : this.dstProjectXmlFile;

		java.io.File projectZip = new java.io.File( this.getSuite(), "Alice3Module/src/org/alice/netbeans/ProjectTemplate.zip" );
		edu.cmu.cs.dennisc.java.util.zip.ZipUtilities.zip( this.getProjectTemplate(), projectZip );
		assert projectZip.exists() : projectZip;

		java.io.File userPropertiesFile = NetBeans8Utils.getUserPropertiesFile();
		java.io.File platformPrivatePropertiesFile = new java.io.File( this.getSuite(), "nbproject/private/platform-private.properties" );
		edu.cmu.cs.dennisc.java.io.TextFileUtilities.write( platformPrivatePropertiesFile, "user.properties.file=" + userPropertiesFile.getAbsolutePath().replaceAll( "\\\\", "\\\\\\\\" ) );
	}

	public void copyJars( BuildRepo buildRepo ) throws java.io.IOException {
		ProjectCollection coreProjectCollection = new ProjectCollection
				.Builder( "core" )
						.addProjectNames(
								"util",
								"scenegraph",
								"glrender",
								"story-api",
								"ast",
								"story-api-migration"
						).build();

		for( String projectName : coreProjectCollection.getProjectNames() ) {
			String filename = projectName + "-0.0.1-SNAPSHOT.jar";
			java.io.File src = new java.io.File( buildRepo.getRoot(), coreProjectCollection.getDirName() + "/" + projectName + "/target/" + filename );
			java.io.File dst = new java.io.File( this.getJars(), filename );
			assert src.exists() : src;
			edu.cmu.cs.dennisc.java.io.FileUtilities.copyFile( src, dst );
			assert dst.exists() : dst;
			edu.cmu.cs.dennisc.java.util.logging.Logger.outln( dst );
		}

		for( String mavenRepoJarPath : this.jarPathsToCopyFromMaven ) {
			java.io.File src = new java.io.File( MavenUtils.getMavenRepositoryDir(), mavenRepoJarPath );
			java.io.File dst = new java.io.File( this.getJars(), src.getName() );
			assert src.exists() : src;
			edu.cmu.cs.dennisc.java.io.FileUtilities.copyFile( src, dst );
			assert dst.exists() : dst;
			edu.cmu.cs.dennisc.java.util.logging.Logger.outln( dst );
		}
	}

	public void copyDistribution( BuildRepo buildRepo ) throws java.io.IOException {
		if( this.getDistribution().exists() ) {
			org.apache.commons.io.FileUtils.deleteDirectory( this.getDistribution() );
			assert this.getDistribution().exists() == false : this.getDistribution();
		}

		java.io.File distribSrc = buildRepo.getDistributionSource();
		assert distribSrc.exists() : distribSrc;
		assert distribSrc.isDirectory() : distribSrc;
		edu.cmu.cs.dennisc.java.io.FileUtilities.copyDirectory( distribSrc, this.getDistribution(), new edu.cmu.cs.dennisc.pattern.Criterion<java.io.File>() {
			@Override
			public boolean accept( java.io.File file ) {
				if( file.isDirectory() ) {
					String directoryName = file.getName();
					if( directoryName.equals( "application" ) || directoryName.equals( "ffmpeg" ) || directoryName.equals( "libvlc" ) ) {
						return false;
					}
				}
				edu.cmu.cs.dennisc.java.util.logging.Logger.outln( file );
				return true;
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
		processBuilder.directory( this.getSuite() );

		edu.cmu.cs.dennisc.java.util.logging.Logger.outln( processBuilder.directory() );
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
		java.io.File nbm = new java.io.File( this.getSuite(), "build/updates/org-alice-netbeans.nbm" );

		edu.cmu.cs.dennisc.java.io.FileSystemUtils.deleteIfExists( nbm );

		java.io.File javaHomeDir = JdkUtils.getJdk8HomeDir();
		antClean( javaHomeDir );
		antCompile( javaHomeDir );
		antNBM( javaHomeDir );

		assert nbm.exists() : nbm;

		java.io.File nbmVersion = new java.io.File( edu.cmu.cs.dennisc.java.io.FileUtilities.getDefaultDirectory(), "Alice3NetBeans8Plugin_" + org.lgna.project.ProjectVersion.getCurrentVersionText() + ".nbm" );
		edu.cmu.cs.dennisc.java.io.FileSystemUtils.deleteIfExists( nbmVersion );

		edu.cmu.cs.dennisc.java.io.FileUtilities.copyFile( nbm, nbmVersion );
		assert nbmVersion.exists() : nbmVersion;

		edu.cmu.cs.dennisc.java.util.logging.Logger.outln( nbmVersion );
	}

	private final java.io.File suite;
	private final java.io.File jars;
	private final java.io.File distribution;
	private final java.io.File projectTemplate;

	private final String manifestText;
	private final String libraryXmlText;
	private final String projectXmlText;
	private final java.io.File dstManifestFile;
	private final java.io.File dstLibraryXmlFile;
	private final java.io.File dstProjectXmlFile;

	private final java.util.List<String> jarPathsToCopyFromMaven;
}
