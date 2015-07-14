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
	private static final String ALICE_VERSION = org.lgna.project.ProjectVersion.getCurrentVersionText();
	private static final String JOGL_VERSION = "2.2.4";
	private static final String ALICE_MODEL_SOURCE_VERSION = "2014.08.20";
	private static final String NEBULOUS_MODEL_SOURCE_VERSION = "2014.09.11";

	private static final java.io.File DEV_ROOT;
	private static final java.io.File BUILD_ROOT;

	private static final java.io.File DEV_PLUGIN_8;
	private static final java.io.File DEV_PLUGIN_8_JARS;
	private static final java.io.File DEV_PLUGIN_8_DISTRIBUTION;

	private static final java.io.File DEV_PROJECT_TEMPLATE_8;

	private static final java.io.File MAVEN_REPOSITORY_DIRECTORY = new java.io.File( edu.cmu.cs.dennisc.java.io.FileUtilities.getUserDirectory(), ".m2/repository" );

	private static final java.io.File BUILD_DISTRIBUTION_DIR;

	private static final String[] MAVEN_REPO_JARS = {
			"org/jogamp/gluegen/gluegen-rt/" + JOGL_VERSION + "/gluegen-rt-" + JOGL_VERSION + ".jar",
			"org/jogamp/jogl/jogl-all/" + JOGL_VERSION + "/jogl-all-" + JOGL_VERSION + ".jar",
			"javax/media/jmf/2.1.1e/jmf-2.1.1e.jar",
			"com/sun/javamp3/1.0/javamp3-1.0.jar",
			"org/alice/alice-model-source/" + ALICE_MODEL_SOURCE_VERSION + "/alice-model-source-" + ALICE_MODEL_SOURCE_VERSION + ".jar",
			"org/alice/nonfree/nebulous-model-source/" + NEBULOUS_MODEL_SOURCE_VERSION + "/nebulous-model-source-" + NEBULOUS_MODEL_SOURCE_VERSION + ".jar",
	};

	static {
		DEV_ROOT = new java.io.File( edu.cmu.cs.dennisc.java.io.FileUtilities.getDefaultDirectory(), "gits/alice" );
		assert DEV_ROOT.exists() : DEV_ROOT;
		assert DEV_ROOT.isDirectory() : DEV_ROOT;

		DEV_PLUGIN_8 = new java.io.File( DEV_ROOT, "alice/netbeans/8/Alice3ModuleSuite" );
		assert DEV_PLUGIN_8.exists() : DEV_PLUGIN_8;
		assert DEV_PLUGIN_8.isDirectory() : DEV_PLUGIN_8;

		DEV_PLUGIN_8_JARS = new java.io.File( DEV_PLUGIN_8, "Alice3Module/release/modules/ext" );
		assert DEV_PLUGIN_8_JARS.exists() : DEV_PLUGIN_8_JARS;
		assert DEV_PLUGIN_8_JARS.isDirectory() : DEV_PLUGIN_8_JARS;

		DEV_PLUGIN_8_DISTRIBUTION = new java.io.File( DEV_PLUGIN_8, "Alice3Module/release/src/aliceSource.jar_root" );

		DEV_PROJECT_TEMPLATE_8 = new java.io.File( DEV_ROOT, "alice/netbeans/8/ProjectTemplate" );
		assert DEV_PROJECT_TEMPLATE_8.exists() : DEV_PROJECT_TEMPLATE_8;
		assert DEV_PROJECT_TEMPLATE_8.isDirectory() : DEV_PROJECT_TEMPLATE_8;

		BUILD_ROOT = new java.io.File( edu.cmu.cs.dennisc.java.io.FileUtilities.getDefaultDirectory(), "gits/alice_for_build" );
		assert BUILD_ROOT.exists() : BUILD_ROOT;
		assert BUILD_ROOT.isDirectory() : BUILD_ROOT;

		BUILD_DISTRIBUTION_DIR = new java.io.File( BUILD_ROOT, "/core/resources/target/distribution" );
	}

	private static class JarInfo {
		public JarInfo( String dirName, String... projectNames ) {
			this.dirName = dirName;
			this.projectNames = projectNames;
		}

		public void copyJarsToNetBeans8() throws java.io.IOException {
			for( String projectName : this.projectNames ) {
				String filename = projectName + "-0.0.1-SNAPSHOT.jar";
				java.io.File src = new java.io.File( BUILD_ROOT, this.dirName + "/" + projectName + "/target/" + filename );
				java.io.File dst = new java.io.File( DEV_PLUGIN_8_JARS, filename );
				assert src.exists() : src;
				edu.cmu.cs.dennisc.java.io.FileUtilities.copyFile( src, dst );
				assert dst.exists() : dst;
				edu.cmu.cs.dennisc.java.util.logging.Logger.outln( dst );
			}
			for( String mavenRepoJarPath : MAVEN_REPO_JARS ) {
				java.io.File src = new java.io.File( MAVEN_REPOSITORY_DIRECTORY, mavenRepoJarPath );
				java.io.File dst = new java.io.File( DEV_PLUGIN_8_JARS, src.getName() );
				assert src.exists() : src;
				edu.cmu.cs.dennisc.java.io.FileUtilities.copyFile( src, dst );
				assert dst.exists() : dst;
				edu.cmu.cs.dennisc.java.util.logging.Logger.outln( dst );
			}
		}

		private final String dirName;
		private final String[] projectNames;
	}

	private static String substituteVersionTexts( String s ) {
		s = s.trim();
		s = s.replace( "___ALICE_VERSION___", ALICE_VERSION );
		s = s.replace( "___JOGL_VERSION___", JOGL_VERSION );
		s = s.replace( "___ALICE_MODEL_SOURCE_VERSION___", ALICE_MODEL_SOURCE_VERSION );
		s = s.replace( "___NEBULOUS_MODEL_SOURCE_VERSION___", NEBULOUS_MODEL_SOURCE_VERSION );
		return s;
	}

	private static void copyJars() throws java.io.IOException {
		JarInfo jarInfo = new JarInfo( "core",
				"util",
				"scenegraph",
				"glrender",
				"story-api",
				"ast",
				"story-api-migration" );

		jarInfo.copyJarsToNetBeans8();
	}

	private static void compileJars() throws java.io.IOException, InterruptedException {
		ProcessBuilder processBuilder = new ProcessBuilder(
				System.getenv( "MAVEN_HOME" ) + "\\bin\\mvn.bat",
				//"clean"
				//, 
				"compile"
				//, 
				//"install" 
				);
		processBuilder.directory( BUILD_ROOT );
		edu.cmu.cs.dennisc.java.lang.ProcessUtilities.startAndWaitFor( processBuilder, System.out, System.err );
	}

	private static java.io.File _createCoreSrcDirectory( String subName ) {
		return new java.io.File( BUILD_ROOT, "core/" + subName + "/src/main/java" );
	}

	private static void _ant( String arg ) throws java.io.IOException, InterruptedException {
		java.util.List<String> command = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
		command.add( System.getenv( "ANT_HOME" ) + "\\bin\\ant.bat" );
		if( arg != null ) {
			command.add( arg );
		}
		ProcessBuilder processBuilder = new ProcessBuilder( command );
		java.util.Map<String, String> env = processBuilder.environment();
		env.put( "JAVA_HOME", "C:\\Program Files\\Java\\jdk1.8.0_25" );
		processBuilder.directory( DEV_PLUGIN_8 );
		edu.cmu.cs.dennisc.java.lang.ProcessUtilities.startAndWaitFor( processBuilder, System.out, System.err );
	}

	private static void antClean() throws java.io.IOException, InterruptedException {
		_ant( "clean" );
	}

	private static void antCompile() throws java.io.IOException, InterruptedException {
		_ant( null );
	}

	private static void antNBM() throws java.io.IOException, InterruptedException {
		_ant( "nbms" );
	}

	private static void prepareToDevelopPlugin8() throws java.io.IOException, InterruptedException {
		compileJars();
		copyJars();

		if( DEV_PLUGIN_8_DISTRIBUTION.exists() ) {
			edu.cmu.cs.dennisc.java.io.FileUtilities.delete( DEV_PLUGIN_8_DISTRIBUTION );
			assert DEV_PLUGIN_8_DISTRIBUTION.exists() == false : DEV_PLUGIN_8_DISTRIBUTION;
		}

		assert BUILD_DISTRIBUTION_DIR.exists() : BUILD_DISTRIBUTION_DIR;
		assert BUILD_DISTRIBUTION_DIR.isDirectory() : BUILD_DISTRIBUTION_DIR;
		edu.cmu.cs.dennisc.java.io.FileUtilities.copyDirectory( BUILD_DISTRIBUTION_DIR, DEV_PLUGIN_8_DISTRIBUTION, new edu.cmu.cs.dennisc.pattern.Criterion<java.io.File>() {
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

		java.io.File dstZip = new java.io.File( DEV_PLUGIN_8, "Alice3Module/release/src/aliceSource.jar" );
		edu.cmu.cs.dennisc.java.util.zip.ZipUtilities.zipFilesInDirectory( srcDirectory, dstZip, new java.io.FileFilter() {
			@Override
			public boolean accept( java.io.File file ) {
				return "java".equals( edu.cmu.cs.dennisc.java.io.FileUtilities.getExtension( file ) );
			}
		} );

		edu.cmu.cs.dennisc.java.io.TextFileUtilities.write(
				new java.io.File( DEV_PLUGIN_8, "Alice3Module/manifest.mf" ),
				substituteVersionTexts(
				edu.cmu.cs.dennisc.java.io.TextFileUtilities.read( Build.class.getResourceAsStream( "NetBeans8Plugin/manifest.mf" ) )
				) );
		edu.cmu.cs.dennisc.java.io.TextFileUtilities.write(
				new java.io.File( DEV_PLUGIN_8, "Alice3Module/src/org/alice/netbeans/Alice3Library.xml" ),
				substituteVersionTexts(
				edu.cmu.cs.dennisc.java.io.TextFileUtilities.read( Build.class.getResourceAsStream( "NetBeans8Plugin/Alice3Library.xml" ) )
				) );
		edu.cmu.cs.dennisc.java.io.TextFileUtilities.write(
				new java.io.File( DEV_PLUGIN_8, "Alice3Module/nbproject/project.xml" ),
				substituteVersionTexts(
				edu.cmu.cs.dennisc.java.io.TextFileUtilities.read( Build.class.getResourceAsStream( "NetBeans8Plugin/project.xml" ) )
				) );

		java.io.File projectZip = new java.io.File( DEV_PLUGIN_8, "Alice3Module/src/org/alice/netbeans/ProjectTemplate.zip" );
		edu.cmu.cs.dennisc.java.util.zip.ZipUtilities.zip( DEV_PROJECT_TEMPLATE_8, projectZip );
		assert projectZip.exists() : projectZip;

		java.io.File tempDirectoryForJavaDoc = new java.io.File( edu.cmu.cs.dennisc.java.io.FileUtilities.getDefaultDirectory(), "tempDirectoryForJavaDoc" );
		if( tempDirectoryForJavaDoc.exists() ) {
			tempDirectoryForJavaDoc.delete();
		}
		assert tempDirectoryForJavaDoc.exists() == false : tempDirectoryForJavaDoc;
		tempDirectoryForJavaDoc.mkdirs();

		StringBuilder sb = new StringBuilder();
		String[] subNames = { "util", "scenegraph", "ast", "story-api" };
		for( String subName : subNames ) {
			sb.append( _createCoreSrcDirectory( subName ).getAbsolutePath() );
			sb.append( edu.cmu.cs.dennisc.java.lang.SystemUtilities.PATH_SEPARATOR );
		}
		String srcPath = sb.toString();

		com.sun.tools.javadoc.Main.execute( new String[] { "-d", tempDirectoryForJavaDoc.getAbsolutePath(), "-sourcepath", srcPath, "-encoding", "UTF-8", "-docencoding", "UTF-8", "-subpackages", "org.lgna.story", "-exclude", "org.lgna.story.implementation:org.lgna.story.resourceutilities" } );

		java.io.File docZip = new java.io.File( DEV_PLUGIN_8, "Alice3Module/release/doc/aliceDocs.zip" );
		edu.cmu.cs.dennisc.java.io.FileUtilities.createParentDirectoriesIfNecessary( docZip );
		edu.cmu.cs.dennisc.java.util.zip.ZipUtilities.zip( tempDirectoryForJavaDoc, docZip );
		assert docZip.exists() : docZip;

		java.io.File nbm = new java.io.File( DEV_PLUGIN_8, "build/updates/org-alice-netbeans.nbm" );

		if( nbm.exists() ) {
			nbm.delete();
			assert nbm.exists() == false : nbm;
		}
		antClean();
		antCompile();
		antNBM();

		assert nbm.exists() : nbm;

		java.io.File nbmVersion = new java.io.File( edu.cmu.cs.dennisc.java.io.FileUtilities.getDefaultDirectory(), "Alice3NetBeans8Plugin_" + ALICE_VERSION + ".nbm" );
		if( nbmVersion.exists() ) {
			nbmVersion.delete();
			assert nbmVersion.exists() == false : nbmVersion;
		}
		edu.cmu.cs.dennisc.java.io.FileUtilities.copyFile( nbm, nbmVersion );
		assert nbmVersion.exists() : nbmVersion;

		edu.cmu.cs.dennisc.java.util.logging.Logger.outln( nbmVersion );
	}

	public static void main( String[] args ) throws Exception {
		prepareToDevelopPlugin8();
		edu.cmu.cs.dennisc.java.util.logging.Logger.outln( System.getenv( "JAVA_HOME" ) );
		edu.cmu.cs.dennisc.java.util.logging.Logger.outln( System.getenv( "MAVEN_HOME" ) );
		edu.cmu.cs.dennisc.java.util.logging.Logger.outln( System.getenv( "ANT_HOME" ) );
		edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "done" );
	}
}
