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

public abstract class Batch {
	protected abstract void handle( java.io.File inFile, java.io.File outFile );
	protected abstract boolean isSkipExistingOutFilesDesirable();
	public void process( String inRootPath, String outRootPath, String inExt, String outExt ) {
		boolean isSkipExistingOutFilesDesirable = this.isSkipExistingOutFilesDesirable();
		
		java.io.File inRoot = new java.io.File(inRootPath);
		java.io.File outRoot = new java.io.File(outRootPath);
		System.out.print( "FileUtilities.listDescendants... " );
		java.io.File[] inFiles = edu.cmu.cs.dennisc.java.io.FileUtilities.listDescendants( inRoot, inExt );
		System.out.println( "Done.  ( " + inFiles.length + " files )" );

		java.util.Set< java.io.File > unhandledFiles = new java.util.HashSet< java.io.File >();
		//Runtime.getRuntime().gc();
		//long freeMemory0 = Runtime.getRuntime().freeMemory();
		for( java.io.File inFile : inFiles ) {
			java.io.File outFile = edu.cmu.cs.dennisc.java.io.FileUtilities.getAnalogousFile(inFile, inRoot, outRoot, inExt, outExt );
			if( isSkipExistingOutFilesDesirable && edu.cmu.cs.dennisc.java.io.FileUtilities.existsAndHasLengthGreaterThanZero( outFile ) ) {
				//System.out.println( "SKIPPING: " + outFile + " exists." );
			} else {
				if( inFile.exists() ) {
					//todo:
					if( inFile.length() < 100L * 1024L * 1024L ) {
						try {
							handle( inFile, outFile );
						} catch( RuntimeException re ) {
							re.printStackTrace();
							unhandledFiles.add( inFile );
						}
					} else {
						unhandledFiles.add( inFile );
					}
				}
			}
			//Runtime.getRuntime().gc();
			//long freeMemoryI = Runtime.getRuntime().freeMemory();
			//System.err.println( freeMemoryI + " " + (freeMemoryI-freeMemory0) );
		}
		System.out.flush();
		for( java.io.File unhandledFile : unhandledFiles ) {
			System.err.println( "UNHANDLED FILE: " + unhandledFile );
		}
	}
}
