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

package batch;

/**
 * @author Dennis Cosgrove
 */
public class SourceCopyrightBatch {
	public static void main( String[] args ) throws Exception {
		String[] txtResourcePaths = {
				"2010_art_a.txt",
				"2010_art_b.txt",
				"2010_art_c.txt",
				"2011_art_a.txt",
				"2011_art_b.txt",
				"2012_art_a.txt",
				"2012_art_b.txt",
				"2012_art_c.txt",
				"2013_art_a.txt",
				"2013_art_b.txt",
				"2013_art_generated.txt",
				"2014_art.txt",
		};
		java.util.List<String> headers = edu.cmu.cs.dennisc.java.util.Lists.newArrayListWithInitialCapacity( txtResourcePaths.length );

		for( String txtResourcePath : txtResourcePaths ) {
			java.io.InputStream is = SourceCopyrightBatch.class.getResourceAsStream( "headers/" + txtResourcePath );
			assert is != null : txtResourcePath;
			headers.add( edu.cmu.cs.dennisc.java.io.TextFileUtilities.read( is ) );
		}

		edu.cmu.cs.dennisc.java.util.logging.Logger.outln( headers );

		java.io.File inRoot = new java.io.File( edu.cmu.cs.dennisc.java.io.FileUtilities.getDefaultDirectory(), "alice" );
		assert inRoot.exists() : inRoot;
		java.io.File[] inFiles = edu.cmu.cs.dennisc.java.io.FileUtilities.listDescendants( inRoot, "java" );
		int countNoCopyright = 0;
		int count = 0;
		for( java.io.File inFile : inFiles ) {
			String inFileText = edu.cmu.cs.dennisc.java.io.TextFileUtilities.read( inFile );
			boolean isFound = false;
			for( String header : headers ) {
				if( inFileText.startsWith( header ) ) {
					isFound = true;
				}
			}
			if( isFound ) {
				//edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "Y", inFile );
			} else {
				if( inFileText.startsWith( "package " ) || inFileText.startsWith( "import " ) ) {
					countNoCopyright++;
				} else {
					count++;
					edu.cmu.cs.dennisc.java.util.logging.Logger.errln( "N", inFile );
				}
			}
		}
		edu.cmu.cs.dennisc.java.util.logging.Logger.outln( countNoCopyright );
		edu.cmu.cs.dennisc.java.util.logging.Logger.outln( count );
	}
}
