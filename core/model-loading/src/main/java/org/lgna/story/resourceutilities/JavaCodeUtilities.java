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
package org.lgna.story.resourceutilities;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import com.sun.tools.javac.Main;
import org.lgna.project.License;

//note: requires tools.jar (from the jdk) in classpath

/**
 * @author Alice Build
 */
public class JavaCodeUtilities {

	private static String COPYRIGHT_COMMENT = null;

	public static final String LINE_RETURN = "\r\n";

	public static String getCopyrightComment( String lineEnding ) {
		if( COPYRIGHT_COMMENT == null ) {
			String copyright = License.TEXT.replace( "\r\n", lineEnding + " * " );
			COPYRIGHT_COMMENT = "/*" + lineEnding + "* " + copyright + lineEnding + " */" + lineEnding;
		}
		return COPYRIGHT_COMMENT;
	}

	public static String getCopyrightComment() {
		return getCopyrightComment( LINE_RETURN );
	}

	public static String getDirectoryStringForPackage( String packageString ) {
		StringBuilder sb = new StringBuilder();
		String[] splitString = packageString.split( "\\." );
		for( String s : splitString ) {
			sb.append( s );
			sb.append( File.separator );
		}
		return sb.toString();
	}

	public static void compileJavaFile( File javaFile ) throws IOException {
		String[] args = new String[] { javaFile.getAbsolutePath(), "-target", "1.8", "-classpath", System.getProperty( "java.class.path" ), "-Xlint:unchecked" };
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintWriter pw = new PrintWriter( baos );
		int status = Main.compile( args, pw );

		String compileOutput = baos.toString( "UTF-8" );
		if( status != 0 ) {
			throw new IOException( "Java code for " + javaFile.getName() + " failed to compile: " + compileOutput );
		}
	}

}
