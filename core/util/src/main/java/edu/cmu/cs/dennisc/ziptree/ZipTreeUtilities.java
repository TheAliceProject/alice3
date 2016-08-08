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
package edu.cmu.cs.dennisc.ziptree;

/**
 * @author Dennis Cosgrove
 */
public class ZipTreeUtilities {
	public static DirectoryZipTreeNode createTreeNode( java.util.zip.ZipInputStream zis, boolean isDataExtractionDesired ) throws java.io.IOException {
		java.util.Map<String, DirectoryZipTreeNode> map = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();
		DirectoryZipTreeNode rv = new DirectoryZipTreeNode( null );
		map.put( "", rv );
		java.util.zip.ZipEntry zipEntry;
		while( ( zipEntry = zis.getNextEntry() ) != null ) {
			String name = zipEntry.getName();
			ZipTreeNode zipTreeNode;
			if( zipEntry.isDirectory() ) {
				DirectoryZipTreeNode directoryZipTreeNode = new DirectoryZipTreeNode( name );
				map.put( name, directoryZipTreeNode );
				zipTreeNode = directoryZipTreeNode;
			} else {
				byte[] data;
				if( isDataExtractionDesired ) {
					data = edu.cmu.cs.dennisc.java.util.zip.ZipUtilities.extractBytes( zis, zipEntry );
				} else {
					data = null;
				}

				zipTreeNode = new FileZipTreeNode( name, data );
			}
			String parentName;
			int index = name.lastIndexOf( '/', name.length() - 2 );
			if( index != -1 ) {
				parentName = name.substring( 0, index + 1 );
			} else {
				parentName = "";
			}
			DirectoryZipTreeNode parent = map.get( parentName );
			assert parent != null : parentName;
			zipTreeNode.setParent( parent );
		}
		return rv;
	}

	public static DirectoryZipTreeNode createTreeNode( java.io.InputStream is, boolean isDataExtractionDesired ) throws java.io.IOException {
		java.util.zip.ZipInputStream zis;
		if( is instanceof java.util.zip.ZipInputStream ) {
			zis = (java.util.zip.ZipInputStream)is;
		} else {
			zis = new java.util.zip.ZipInputStream( is );
		}
		return createTreeNode( zis, isDataExtractionDesired );
	}

	public static DirectoryZipTreeNode createTreeNode( java.io.File file, boolean isDataExtractionDesired ) throws java.io.IOException {
		return createTreeNode( new java.io.FileInputStream( file ), isDataExtractionDesired );
	}

	public static DirectoryZipTreeNode createTreeNode( String path, boolean isDataExtractionDesired ) throws java.io.IOException {
		return createTreeNode( new java.io.File( path ), isDataExtractionDesired );
	}
}
