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
package org.alice.ide.preferences;

/**
 * @author Dennis Cosgrove
 */
public class PathsPreference extends edu.cmu.cs.dennisc.preference.ListPreference< String > {
	public PathsPreference() {
		super( new java.util.LinkedList<String>() );
	}
	
	@Override
	protected int getItemVersion() {
		return 1;
	}
	@Override
	protected String decode( int itemVersion, edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder) {
		if( itemVersion == 1 ) {
			return binaryDecoder.decodeString();
		} else {
			return null;
		}
	}
	@Override
	protected void encode( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder, String item ) {
		binaryEncoder.encode(item);
	}


	public void clear() {
		java.util.List< String > list = java.util.Collections.emptyList();
		this.setAndCommitValue( list );
	}
	public void add( java.io.File file, int maxSize ) {
		if( file != null && file.canWrite() ) {
			java.util.List< String > list = this.getValue();
			String path = edu.cmu.cs.dennisc.java.io.FileUtilities.getCanonicalPathIfPossible( file );
			if( path != null && path.length() > 0 ) {
				int index = list.indexOf( path );
				if( index > 0 ) {
					list.remove( index );
				}
				if( index != 0 ) {
					list.add( 0, path );
					if( list.size() > maxSize ) {
						list = list.subList( 0, maxSize );
					}
					this.setAndCommitValue( list );
				}
			}
		}
	}
}
