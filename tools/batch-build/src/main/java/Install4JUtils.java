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

import edu.cmu.cs.dennisc.java.io.FileUtilities;
import edu.cmu.cs.dennisc.java.lang.SystemUtilities;

import java.io.File;

/**
 * @author Dennis Cosgrove
 */
public class Install4JUtils {
	private static void checkFile( Config config, File directory, String prefix, String postfix ) {
		File file = new File( directory, prefix + config.getInstallerIncludedJvmVersion() + postfix );
		assert file.exists() : file;
	}

	public static void initialize( Config config ) {
		File install4jHomeDir = SystemUtilities.getEnvironmentVariableDirectory( "INSTALL4J_HOME" );
		install4JCommandFile = new File( install4jHomeDir, "bin/install4jc.exe" );
		assert install4JCommandFile.exists() : install4JCommandFile;

		File directory = new File( FileUtilities.getUserDirectory(), ".install4j5/jres" );
		assert directory.isDirectory() : directory;

		checkFile( config, directory, "windows-amd64-", ".tar.gz" );
		checkFile( config, directory, "windows-x86-", ".tar.gz" );

		//note: should only need one of the following two files
		checkFile( config, directory, "macosx-amd64-", ".tar.gz" );
		checkFile( config, directory, "macosx-amd64-", "_unpacked.tar.gz" );
	}

	public static File getInstall4JCommandFile( Config config ) {
		if( install4JCommandFile != null ) {
			//pass
		} else {
			initialize( config );
		}
		return install4JCommandFile;
	}

	private static File install4JCommandFile;

	private Install4JUtils() {
		throw new AssertionError();
	}

}
