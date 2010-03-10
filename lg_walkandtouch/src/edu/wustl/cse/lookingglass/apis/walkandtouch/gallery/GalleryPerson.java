/*
 * Copyright (c) 2008-2010, Washington University in St. Louis. All rights reserved.
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
 * 3. Products derived from the software may not be called "Looking Glass", nor 
 *    may "Looking Glass" appear in their name, without prior written permission
 *    of Washington University in St. Louis.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software 
 *    developed by Washington University in St. Louis"
 *
 * 5. The gallery of art assets and animations provided with this software is 
 *    contributed by Electronic Arts Inc. and may be used for personal, 
 *    non-commercial, and academic use only. Redistributions of any program 
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in 
 *    The Alice 3.0 Art Gallery License.
 *
 * DISCLAIMER:
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.  ANY AND ALL 
 * EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A PARTICULAR PURPOSE, 
 * TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHORS, 
 * COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, 
 * INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO 
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 */
package edu.wustl.cse.lookingglass.apis.walkandtouch.gallery;

import edu.cmu.cs.dennisc.print.PrintUtilities;

public class GalleryPerson extends edu.wustl.cse.lookingglass.apis.walkandtouch.Person {
	public GalleryPerson( String path ) {
		java.io.File file = new java.io.File( GalleryRoot.getGalleryRootDirectory(), "assets/edu.wustl.cse.lookingglass.apis.walkandtouch.gallery/characters/" + path + ".zip" );
		
		PrintUtilities.println("opening character..." +  file);
		assert file.exists();
		edu.cmu.cs.dennisc.codec.CodecUtilities.decodeZippedReferenceableBinary( this, file.getAbsolutePath(), "element.bin" );	
	}
////	private static edu.cmu.cs.dennisc.xml.Decoder s_decoder = new edu.cmu.cs.dennisc.xml.Decoder();
//	public GalleryPerson( String path ) {
//		try {
//			java.util.zip.ZipFile file = new java.util.zip.ZipFile( "/Alice/3.alpha.0/gallery/assets/edu.wustl.cse.ckelleher.apis.walkandtouch.gallery/characters/" + path + ".zip" );
//			
//			edu.cmu.cs.dennisc.print.PrintUtilities.println(file.getName());
//			java.util.zip.ZipEntry entry = file.getEntry( "element.bin" );
//			edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder = new edu.cmu.cs.dennisc.codec.InputStreamBinaryDecoder( file.getInputStream( entry ) );
//			java.util.Map< Integer, edu.cmu.cs.dennisc.codec.ReferenceableBinaryEncodableAndDecodable > map = new java.util.HashMap< Integer, edu.cmu.cs.dennisc.codec.ReferenceableBinaryEncodableAndDecodable >();
//			binaryDecoder.decodeReferenceableBinaryEncodableAndDecodable( this, map );
//		} catch( java.io.IOException ioe ) {
//			throw new RuntimeException( ioe );
//		}
//	}
}
