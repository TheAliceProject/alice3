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
package org.alice.apis.moveandturn.gallery.holidays.birthday;
	
public class Partyfavor extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public Partyfavor() {
		super( "Holidays/Birthday/partyfavor" );
	}
	public enum Part {
		Extension1_Extension2_Extension3_Extension4_Extension5_Extension6_Extension7_Extension8( "Extension1", "Extension2", "Extension3", "Extension4", "Extension5", "Extension6", "Extension7", "Extension8" ),
		Extension1_Extension2_Extension3_Extension4_Extension5_Extension6_Extension7( "Extension1", "Extension2", "Extension3", "Extension4", "Extension5", "Extension6", "Extension7" ),
		Extension1_Extension2_Extension3_Extension4_Extension5_Extension6( "Extension1", "Extension2", "Extension3", "Extension4", "Extension5", "Extension6" ),
		Extension1_Extension2_Extension3_Extension4_Extension5( "Extension1", "Extension2", "Extension3", "Extension4", "Extension5" ),
		Extension1_Extension2_Extension3_Extension4( "Extension1", "Extension2", "Extension3", "Extension4" ),
		Extension1_Extension2_Extension3( "Extension1", "Extension2", "Extension3" ),
		Extension1_Extension2( "Extension1", "Extension2" ),
		Extension1( "Extension1" ),
		MouthPiece_Ribbon08( "mouth_piece", "ribbon08" ),
		MouthPiece_Ribbon03( "mouth_piece", "ribbon03" ),
		MouthPiece_Ribbon( "mouth_piece", "ribbon" ),
		MouthPiece_Ribbon05( "mouth_piece", "ribbon05" ),
		MouthPiece_Ribbon07( "mouth_piece", "ribbon07" ),
		MouthPiece_Ribbon06( "mouth_piece", "ribbon06" ),
		MouthPiece_Ribbon04( "mouth_piece", "ribbon04" ),
		MouthPiece_Ribbon2( "mouth_piece", "ribbon2" ),
		MouthPiece( "mouth_piece" );
		private String[] m_path;
		Part( String... path ) {
			m_path = path;
		}
		public String[] getPath() {
			return m_path;
		}
	}
	public org.alice.apis.moveandturn.Model getPart( Part part ) {
		return getDescendant( org.alice.apis.moveandturn.Model.class, part.getPath() );
	}

}
