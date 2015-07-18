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
	}

	protected abstract java.io.File getSuite();

	protected abstract java.io.File getJdkToUseForNbmAntCommand();

	protected abstract java.io.File getNbmFile();

	protected Config getConfig() {
		return this.config;
	}

	protected java.io.File getRoot() {
		return this.root;
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
		antClean( javaHomeDir );
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
}
