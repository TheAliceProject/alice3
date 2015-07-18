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
	private java.io.File _createCoreSrcDirectory( String subName ) {
		return new java.io.File( this.buildRepo.getRoot(), "core/" + subName + "/src/main/java" );
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
		processBuilder.directory( this.repo.getPlugin8().getSuite() );

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

	private void prepareToDevelopPlugin8() throws java.io.IOException, InterruptedException {
		this.buildRepo.compileJars();
		this.repo.getPlugin8().copyJars( this.buildRepo, this.repo );

		edu.cmu.cs.dennisc.java.io.FileSystemUtils.deleteIfExists( this.repo.getPlugin8().getDistribution() );

		java.io.File distribSrc = this.buildRepo.getDistributionSource();
		assert distribSrc.exists() : distribSrc;
		assert distribSrc.isDirectory() : distribSrc;
		edu.cmu.cs.dennisc.java.io.FileUtilities.copyDirectory( distribSrc, this.repo.getPlugin8().getDistribution(), new edu.cmu.cs.dennisc.pattern.Criterion<java.io.File>() {
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

		java.io.File srcDirectory = new java.io.File( _createCoreSrcDirectory( "story-api" ), "org/lgna/story" );

		java.io.File dstZip = new java.io.File( this.repo.getPlugin8().getSuite(), "Alice3Module/release/src/aliceSource.jar" );
		edu.cmu.cs.dennisc.java.util.zip.ZipUtilities.zipFilesInDirectory( srcDirectory, dstZip, new java.io.FileFilter() {
			@Override
			public boolean accept( java.io.File file ) {
				return "java".equals( edu.cmu.cs.dennisc.java.io.FileUtilities.getExtension( file ) );
			}
		} );

		this.repo.getPlugin8().prepare();

		java.io.File projectZip = new java.io.File( this.repo.getPlugin8().getSuite(), "Alice3Module/src/org/alice/netbeans/ProjectTemplate.zip" );
		edu.cmu.cs.dennisc.java.util.zip.ZipUtilities.zip( this.repo.getPlugin8().getProjectTemplate(), projectZip );
		assert projectZip.exists() : projectZip;

		java.io.File userPropertiesFile = NetBeans8Utils.getUserPropertiesFile();
		java.io.File platformPrivatePropertiesFile = new java.io.File( this.repo.getPlugin8().getSuite(), "nbproject/private/platform-private.properties" );
		edu.cmu.cs.dennisc.java.io.TextFileUtilities.write( platformPrivatePropertiesFile, "user.properties.file=" + userPropertiesFile.getAbsolutePath().replaceAll( "\\\\", "\\\\\\\\" ) );

		java.io.File tempDirectoryForJavaDoc = new java.io.File( edu.cmu.cs.dennisc.java.io.FileUtilities.getDefaultDirectory(), "tempDirectoryForJavaDoc" );
		edu.cmu.cs.dennisc.java.io.FileSystemUtils.deleteIfExists( tempDirectoryForJavaDoc );
		tempDirectoryForJavaDoc.mkdirs();

		StringBuilder sb = new StringBuilder();
		String[] subNames = { "util", "scenegraph", "ast", "story-api" };
		for( String subName : subNames ) {
			sb.append( _createCoreSrcDirectory( subName ).getAbsolutePath() );
			sb.append( edu.cmu.cs.dennisc.java.lang.SystemUtilities.PATH_SEPARATOR );
		}
		String srcPath = sb.toString();

		com.sun.tools.javadoc.Main.execute( new String[] { "-d", tempDirectoryForJavaDoc.getAbsolutePath(), "-sourcepath", srcPath, "-encoding", "UTF-8", "-docencoding", "UTF-8", "-subpackages", "org.lgna.story", "-exclude", "org.lgna.story.implementation:org.lgna.story.resourceutilities" } );

		java.io.File docZip = new java.io.File( this.repo.getPlugin8().getSuite(), "Alice3Module/release/doc/aliceDocs.zip" );
		edu.cmu.cs.dennisc.java.io.FileUtilities.createParentDirectoriesIfNecessary( docZip );
		edu.cmu.cs.dennisc.java.util.zip.ZipUtilities.zip( tempDirectoryForJavaDoc, docZip );
		assert docZip.exists() : docZip;
	}

	private void createNbm() throws java.io.IOException, InterruptedException {
		java.io.File nbm = new java.io.File( this.repo.getPlugin8().getSuite(), "build/updates/org-alice-netbeans.nbm" );

		edu.cmu.cs.dennisc.java.io.FileSystemUtils.deleteIfExists( nbm );

		String jdk8Home = System.getenv( "JDK8_HOME" );
		assert jdk8Home != null;
		java.io.File javaHomeDir = new java.io.File( jdk8Home );
		assert javaHomeDir.exists() : javaHomeDir;
		assert javaHomeDir.isDirectory() : javaHomeDir;

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

	public Build( Config config ) {
		this.buildRepo = new BuildRepo( config );
		if( config.isDevMode() ) {
			this.repo = new DevRepo( config );
		} else {
			this.repo = this.buildRepo;
		}
	}

	private final BuildRepo buildRepo;
	private final GitRepo repo;

	public static void main( String[] args ) throws Exception {
		Config config = new Config.Builder()
				.isDevMode( false )
				.joglVersion( "2.2.4" )
				.aliceModelSourceVersion( "2014.08.20" )
				.nebulousModelSourceVersion( "2014.09.11" )
				.netBeans8Version( "8.0.2" )
				.build();

		NetBeans8Utils.initialize( config.getNetBeans8Version() );
		MavenUtils.initialize();
		AntUtils.initialize();

		assert System.getenv( "JAVA_HOME" ) != null;
		assert System.getenv( "JDK8_HOME" ) != null;

		Build build = new Build( config );

		edu.cmu.cs.dennisc.timing.Timer timer = new edu.cmu.cs.dennisc.timing.Timer( "build" );
		timer.start();
		timer.mark( build );
		build.prepareToDevelopPlugin8();

		if( config.isDevMode() ) {
			//pass
		} else {
			build.createNbm();
		}
		timer.stopAndPrintResults();

		edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "done" );
		edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "JAVA_HOME", System.getenv( "JAVA_HOME" ) );
		edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "JDK8_HOME", System.getenv( "JDK8_HOME" ) );
		edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "MAVEN_HOME", System.getenv( "MAVEN_HOME" ) );
		edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "ANT_HOME", System.getenv( "ANT_HOME" ) );
		edu.cmu.cs.dennisc.java.util.logging.Logger.outln( config );
	}
}
