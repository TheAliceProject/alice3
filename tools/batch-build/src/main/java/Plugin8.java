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
		this.manifestText = Build.substituteVersionTexts( config, edu.cmu.cs.dennisc.java.io.TextFileUtilities.read( manifestInputStream ) );
		assert this.manifestText != null;
		assert this.manifestText.length() > 0;

		java.io.InputStream libraryXmlInputStream = Build.class.getResourceAsStream( "NetBeans8Plugin/Alice3Library.xml" );
		assert libraryXmlInputStream != null;
		this.libraryXmlText = Build.substituteVersionTexts( config, edu.cmu.cs.dennisc.java.io.TextFileUtilities.read( libraryXmlInputStream ) );
		assert this.libraryXmlText != null;
		assert this.libraryXmlText.length() > 0;

		java.io.InputStream projectXmlInputStream = Build.class.getResourceAsStream( "NetBeans8Plugin/project.xml" );
		assert projectXmlInputStream != null;
		this.projectXmlText = Build.substituteVersionTexts( config, edu.cmu.cs.dennisc.java.io.TextFileUtilities.read( projectXmlInputStream ) );
		assert this.projectXmlText != null;
		assert this.projectXmlText.length() > 0;
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

	public void prepare() {
		edu.cmu.cs.dennisc.java.io.FileSystemUtils.deleteIfExists( this.dstManifestFile );
		edu.cmu.cs.dennisc.java.io.TextFileUtilities.write( this.dstManifestFile, this.manifestText );
		assert this.dstManifestFile.exists() : this.dstManifestFile;

		edu.cmu.cs.dennisc.java.io.FileSystemUtils.deleteIfExists( this.dstLibraryXmlFile );
		edu.cmu.cs.dennisc.java.io.TextFileUtilities.write( this.dstLibraryXmlFile, this.libraryXmlText );
		assert this.dstLibraryXmlFile.exists() : this.dstLibraryXmlFile;

		edu.cmu.cs.dennisc.java.io.FileSystemUtils.deleteIfExists( this.dstProjectXmlFile );
		edu.cmu.cs.dennisc.java.io.TextFileUtilities.write( this.dstProjectXmlFile, this.projectXmlText );
		assert this.dstProjectXmlFile.exists() : this.dstProjectXmlFile;
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

}
