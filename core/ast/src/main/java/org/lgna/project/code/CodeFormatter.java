/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
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
package org.lgna.project.code;

/**
 * @author Dennis Cosgrove
 */
public class CodeFormatter {
	private static String[] splitIntoLines( String src ) {
		src = src.replace( ";", ";\n" );
		src = src.replace( "{", "{\n" );
		src = src.replace( "}", "\n}\n" );
		return src.split( "\n" );
	}

	public static String format( String src, int indent ) {
		//todo: do not format comments
		StringBuilder sb = new StringBuilder();
		int tabSkipCount = 0;
		int newLineSkipCount = 0;
		String[] lines = splitIntoLines( src );
		for( String line : lines ) {
			line = line.trim();
			if( line.length() > 0 ) {
				if( line.startsWith( "}" ) ) {
					indent--;
				}
				if( tabSkipCount == 0 ) {
					for( int i = 0; i < indent; i++ ) {
						sb.append( '\t' );
					}
				} else {
					tabSkipCount--;
				}
				if( line.startsWith( "for(" ) ) {
					tabSkipCount = 2;
					newLineSkipCount = 2;
				}
				if( line.endsWith( "{" ) ) {
					indent++;
				}
				sb.append( line );
				if( newLineSkipCount == 0 ) {
					sb.append( '\n' );
				} else {
					newLineSkipCount--;
				}
			}
		}
		String s = sb.toString();
		s = s.replaceAll( "\\}\\s*,", "}," ); //s = s.replaceAll( "\\}\\s*,\\(", "},(" );
		s = s.replaceAll( "\\}\\s*\\)", "})" );

		// remove whitespace before else
		s = s.replaceAll( "\\}\\s*else\\{", "} else{" );

		s = s.replaceAll( "=", " = " );
		s = s.replaceAll( "=\\s*=", "==" );
		s = s.replaceAll( ":", " : " );
		s = s.replaceAll( ";", "; " );
		s = s.replaceAll( ",", ", " );
		s = s.replaceAll( "\\{", " { " );
		s = s.replaceAll( "\\(", "( " );
		s = s.replaceAll( "\\)", " )" );
		s = s.replaceAll( "\\} ,", "}," );
		// remove extra spaces
		s = s.replaceAll( "  +", " " );
		s = s.replaceAll( "\t +", "\t" );
		// remove space from empty parens
		s = s.replaceAll( "\\( \\)", "()" );
		// remove whitespace from end of lines
		s = s.replaceAll( "\\s*\n+", "\n" );

		s = s.replaceAll( "@Override ", "@Override\n\t" );
		return s;
	}

	public static String format( String src ) {
		return format( src, 0 );
	}
}
