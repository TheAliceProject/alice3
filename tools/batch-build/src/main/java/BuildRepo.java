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

import com.sun.tools.javadoc.Main;
import edu.cmu.cs.dennisc.java.io.FileUtilities;
import edu.cmu.cs.dennisc.java.lang.ProcessUtilities;
import edu.cmu.cs.dennisc.java.lang.SystemUtilities;
import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author Dennis Cosgrove
 */
public class BuildRepo extends GitRepo {
	public BuildRepo( Config config ) {
		super( config, "alice_for_build" );
		this.distributionSourceDir = new File( this.getRootDir(), "/core/resources/target/distribution" );
	}

	public File getDistributionSourceDir() {
		return this.distributionSourceDir;
	}

	public File getCoreSrcDirectory( String projectName ) {
		return new File( this.getRootDir(), "core/" + projectName + "/src/main/java" );
	}

	public void compileJars() throws IOException, InterruptedException {
		List<String> command = Lists.newLinkedList();
		command.add( "mvn" );
		if( this.getConfig().isCleanDesired() ) {
			command.add( "clean" );
		}
		command.add( "compile" );
		command.add( "install" );

		runCommand( getRootDir(), command );
	}

	static void runCommand( File dir, List<String> command) throws IOException, InterruptedException {
		ProcessBuilder processBuilder = new ProcessBuilder( command );
		processBuilder.environment()
					  .put( "JAVA_HOME", "/Library/Java/JavaVirtualMachines/jdk1.8.0_162.jdk/Contents/Home/" );
		processBuilder.directory( dir );
		ProcessUtilities.startAndWaitFor( processBuilder, System.out, System.err );
	}

	public void copyJars( ProjectCollection projectCollection, File dstDir ) throws IOException {
		for( String projectName : projectCollection.getProjectNames() ) {
			String filename = projectName + "-0.0.1-SNAPSHOT.jar";
			File src = new File( this.getRootDir(), projectCollection.getDirName() + "/" + projectName + "/target/" + filename );
			File dst = new File( dstDir, filename );
			assert src.exists() : src;
			FileUtilities.copyFile( src, dst );
			assert dst.exists() : dst;
			Logger.outln( dst );
		}
	}

	public File generateJavaDocs() throws IOException {
		File tempDirectoryForJavaDoc = new File( FileUtilities.getDefaultDirectory(), "tempDirectoryForJavaDoc" );
		boolean isRequired = ( this.getConfig().isJavaDocGenerationDesired() ) || ( tempDirectoryForJavaDoc.exists() == false );
		if( isRequired ) {
			if( tempDirectoryForJavaDoc.exists() ) {
				FileUtils.deleteDirectory( tempDirectoryForJavaDoc );
			}
			assert tempDirectoryForJavaDoc.exists() == false : tempDirectoryForJavaDoc;
			tempDirectoryForJavaDoc.mkdirs();

			StringBuilder sb = new StringBuilder();
			String[] subNames = { "util", "scenegraph", "ast", "story-api" };
			for( String subName : subNames ) {
				sb.append( this.getCoreSrcDirectory( subName ).getAbsolutePath() );
				sb.append( SystemUtilities.PATH_SEPARATOR );
			}
			String srcPath = sb.toString();

			Main.execute( new String[] { "-d", tempDirectoryForJavaDoc.getAbsolutePath(), "-sourcepath", srcPath, "-encoding", "UTF-8", "-docencoding", "UTF-8", "-subpackages", "org.lgna.story", "-exclude", "org.lgna.story.implementation:org.lgna.story.resourceutilities" } );
		}
		return tempDirectoryForJavaDoc;
	}

	private final File distributionSourceDir;
}
