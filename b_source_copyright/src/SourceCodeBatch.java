/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
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

import edu.cmu.cs.dennisc.batch.Batch;

public class SourceCodeBatch extends Batch {
	private static java.util.Set< String > specialCases = edu.cmu.cs.dennisc.java.util.Collections.newHashSet();

	static {
		specialCases.add( "ImagesToMOVEncoder.java" );
	}

	private String allRightsReservedCopyright;
	private String[] oldCopyrights;
	private String newCopyright;

	public SourceCodeBatch( String allRightsReservedCopyright, String[] oldCopyrights, String newCopyright ) {
		this.allRightsReservedCopyright = allRightsReservedCopyright;
		this.oldCopyrights = oldCopyrights;
		this.newCopyright = newCopyright;
	}

	@Override
	protected void handle( java.io.File inFile, java.io.File outFile ) {
		String s = edu.cmu.cs.dennisc.io.TextFileUtilities.read( inFile );
		if( s.startsWith( allRightsReservedCopyright ) ) {
			throw new AssertionError( outFile );
		} else {
			if( s.startsWith( newCopyright ) ) {
				//pass
			} else {
				for( String oldCopyright : oldCopyrights ) {
					if( s.startsWith( oldCopyright ) ) {
						edu.cmu.cs.dennisc.print.PrintUtilities.println( inFile, outFile );
						s = s.substring( oldCopyright.length() );
						break;
					} else {
						//pass
					}
				}
				String name = inFile.getName();
				if( name.equals( "License.java" ) ) {
					//pass
				} else {
					if( specialCases.contains( name ) ) {
						//pass
					} else {
						if( s.toLowerCase().contains( "copyright" ) ) {
							System.err.println( s );
							throw new AssertionError( outFile );
						}
					}
				}
				s = newCopyright + s;
				edu.cmu.cs.dennisc.io.TextFileUtilities.write( outFile, s );
			}
		}
	}

	@Override
	protected boolean isSkipExistingOutFilesDesirable() {
		return true;
	}

	public static void main( String[] args ) {
		final String ALL_RIGHTS_RESERVED_COPYRIGHT = edu.cmu.cs.dennisc.io.TextFileUtilities.read( SourceCodeBatch.class.getResourceAsStream( "COPYRIGHT_ALL_RIGHTS_RESERVED.txt" ) );
		final String[] OLD_COPYRIGHTS = { 
				edu.cmu.cs.dennisc.io.TextFileUtilities.read( SourceCodeBatch.class.getResourceAsStream( "COPYRIGHT_A.txt" ) ), 
				edu.cmu.cs.dennisc.io.TextFileUtilities.read( SourceCodeBatch.class.getResourceAsStream( "COPYRIGHT_B.txt" ) ), 
				edu.cmu.cs.dennisc.io.TextFileUtilities.read( SourceCodeBatch.class.getResourceAsStream( "COPYRIGHT_C.txt" ) ), 
				edu.cmu.cs.dennisc.io.TextFileUtilities.read( SourceCodeBatch.class.getResourceAsStream( "COPYRIGHT_C_DOUBLE_STAR.txt" ) ),
				edu.cmu.cs.dennisc.io.TextFileUtilities.read( SourceCodeBatch.class.getResourceAsStream( "COPYRIGHT_C_DOUBLE_STAR_MISSING_LINE.txt" ) ) };
		final String NEW_COPYRIGHT = edu.cmu.cs.dennisc.io.TextFileUtilities.read( SourceCodeBatch.class.getResourceAsStream( "COPYRIGHT_D.txt" ) );
		SourceCodeBatch sourceCodeBatch = new SourceCodeBatch( ALL_RIGHTS_RESERVED_COPYRIGHT, OLD_COPYRIGHTS, NEW_COPYRIGHT );
		final String USER_HOME = System.getProperty( "user.home" );
		final String IN_ROOT = USER_HOME + "/My Documents/My Workspaces/Alice3";
		final String OUT_ROOT = USER_HOME + "/My Documents/Alice3_SourceCode_PostBatch";
		final String EXTENSION = "java";
		String[] subs = { 
				"rt_foundation", 
				"rt_issue", 
				"x_ide", 
				"x_ide_stage", 
				"x_youtube",
				
				"rt_moveandturn",
				"rt_moveandturn_generated",
				"rt_storytelling",
				"rt_storytelling_deprecated",
				"rt_storytelling_generated_depended_upon",
				"rt_storytelling_generated_post",
		};
		for( String sub : subs ) {
			sub =  "/" + sub + "/src";
			sourceCodeBatch.process( IN_ROOT+sub, OUT_ROOT+sub, EXTENSION, EXTENSION );
		}

		for( String specialCase : specialCases ) {
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "SPECIAL CASE:", specialCase );
		}
	}
}
