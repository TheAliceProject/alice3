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
	public Installer( Config config, java.io.File repoRoot ) throws java.io.IOException {
		this.config = config;
		this.root = new java.io.File( repoRoot, "installer" );
		assert this.root.exists() : this.root;
		assert this.root.isDirectory() : this.root;

		java.io.InputStream manifestInputStream = Build.class.getResourceAsStream( "Installer/aliceinstallerproject.install4j" );
		assert manifestInputStream != null;

		StringBuilder sb = new StringBuilder();

		java.io.File distributionRootDirectory = new java.io.File( this.root, "aliceInstallData/Alice3/ext" );

		java.io.File mavenRepositoryRootDirectory = MavenUtils.getMavenRepositoryDir();
		java.util.List<java.io.File> jarFiles = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
		for( String pathname : edu.cmu.cs.dennisc.java.lang.SystemUtilities.getClassPath() ) {
			java.io.File file = new java.io.File( pathname );
			if( file.isDirectory() ) {
				edu.cmu.cs.dennisc.java.util.logging.Logger.errln( "skipping directory:", file );
			} else {
				if( file.getName().startsWith( "commons-io" ) ) {
					edu.cmu.cs.dennisc.java.util.logging.Logger.errln( "skipping commons-io:", file );
				} else {
					if( edu.cmu.cs.dennisc.java.io.FileUtilities.isExtensionAmoung( file, "jar" ) ) {
						if( edu.cmu.cs.dennisc.java.io.FileUtilities.isDescendantOf( file, mavenRepositoryRootDirectory ) ) {
							//edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "++ adding: " + file );
							jarFiles.add( file );
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

		java.util.Collections.sort( jarFiles );

		java.util.List<ProjectCollection> projectCollections = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
		projectCollections.add( new ProjectCollection
				.Builder( "core" )
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
								"ide"
						).build() );
		projectCollections.add( new ProjectCollection
				.Builder( "alice" )
						.addProjectNames(
								"alice-ide"
						).build() );

		for( ProjectCollection projectCollection : projectCollections ) {
			edu.cmu.cs.dennisc.java.util.logging.Logger.errln( projectCollection );
		}

		final int N = mavenRepositoryRootDirectory.getAbsolutePath().length() + 1;
		for( java.io.File jarFile : jarFiles ) {
			java.io.File distibutionFile = edu.cmu.cs.dennisc.java.io.FileUtilities.getAnalogousFile( jarFile, mavenRepositoryRootDirectory, distributionRootDirectory );
			edu.cmu.cs.dennisc.java.io.FileUtilities.copyFile( jarFile, distibutionFile );
			sb.append( "\t<archive location=\"ext/" + jarFile.getAbsolutePath().substring( N ).replaceAll( "\\\\", "/" ) + "\" failOnError=\"true\" />\n" );
		}

		edu.cmu.cs.dennisc.java.util.logging.Logger.outln( sb.toString() );
		String s = edu.cmu.cs.dennisc.java.io.TextFileUtilities.read( manifestInputStream );
		s = s.replaceAll( "___ALICE_VERSION___", org.lgna.project.ProjectVersion.getCurrentVersionText() );
		s = s.replaceAll( "___CLASSPATH___", sb.toString() );
		this.manifestText = s;
		assert this.manifestText != null;
		assert this.manifestText.length() > 0;
	}

	public void copyDistribution( BuildRepo buildRepo ) throws java.io.IOException {
		java.io.File distibDst = new java.io.File( this.root, "aliceInstallData/Alice3" );

		java.io.File distribSrc = buildRepo.getDistributionSourceDir();
		assert distribSrc.exists() : distribSrc;
		assert distribSrc.isDirectory() : distribSrc;
		edu.cmu.cs.dennisc.java.io.FileUtilities.copyDirectory( distribSrc, distibDst );

		assert distibDst.isDirectory() : distibDst;
	}

	public void prepareFiles() throws java.io.IOException {
		java.io.File dstManifestFile = new java.io.File( this.root, "aliceinstallerproject.install4j" );
		edu.cmu.cs.dennisc.java.io.FileSystemUtils.deleteIfExists( dstManifestFile );
		edu.cmu.cs.dennisc.java.io.TextFileUtilities.write( dstManifestFile, this.manifestText );
		assert dstManifestFile.exists() : dstManifestFile;
	}

	private final Config config;
	private final java.io.File root;
	private final String manifestText;
}
