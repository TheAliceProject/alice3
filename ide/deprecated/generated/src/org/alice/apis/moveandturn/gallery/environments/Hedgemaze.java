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
package org.alice.apis.moveandturn.gallery.environments;
	
public class Hedgemaze extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public Hedgemaze() {
		super( "Environments/hedgemaze" );
	}
	public enum Part {
		HedgeWallFront_HedgeWallRight_InsideRightWall07_InsideRightWall06_InsideRightWall05_InsideRightWall04_InsideRightWall03_InsideRightWall02_InsideRightWall01( "HedgeWall_Front", "HedgeWall_Right", "InsideRightWall_07", "InsideRightWall_06", "InsideRightWall_05", "InsideRightWall_04", "InsideRightWall_03", "InsideRightWall_02", "InsideRightWall_01" ),
		HedgeWallFront_HedgeWallRight_InsideRightWall07_InsideRightWall06_InsideRightWall05_InsideRightWall04_InsideRightWall03_InsideRightWall02( "HedgeWall_Front", "HedgeWall_Right", "InsideRightWall_07", "InsideRightWall_06", "InsideRightWall_05", "InsideRightWall_04", "InsideRightWall_03", "InsideRightWall_02" ),
		HedgeWallFront_HedgeWallRight_InsideRightWall07_InsideRightWall06_InsideRightWall05_InsideRightWall04_InsideRightWall03( "HedgeWall_Front", "HedgeWall_Right", "InsideRightWall_07", "InsideRightWall_06", "InsideRightWall_05", "InsideRightWall_04", "InsideRightWall_03" ),
		HedgeWallFront_HedgeWallRight_InsideRightWall07_InsideRightWall06_InsideRightWall05_InsideRightWall04( "HedgeWall_Front", "HedgeWall_Right", "InsideRightWall_07", "InsideRightWall_06", "InsideRightWall_05", "InsideRightWall_04" ),
		HedgeWallFront_HedgeWallRight_InsideRightWall07_InsideRightWall06_InsideRightWall05( "HedgeWall_Front", "HedgeWall_Right", "InsideRightWall_07", "InsideRightWall_06", "InsideRightWall_05" ),
		HedgeWallFront_HedgeWallRight_InsideRightWall07_InsideRightWall06( "HedgeWall_Front", "HedgeWall_Right", "InsideRightWall_07", "InsideRightWall_06" ),
		HedgeWallFront_HedgeWallRight_InsideRightWall07_InsideRightWall08_InsideRightWall09_InsideRightWall10_InsideRightWall11_InsideRightWall12_InsideRightWall13_InsideRightWall14_InsideRightWall15_InsideRightWall16( "HedgeWall_Front", "HedgeWall_Right", "InsideRightWall_07", "InsideRightWall_08", "InsideRightWall_09", "InsideRightWall_10", "InsideRightWall_11", "InsideRightWall_12", "InsideRightWall_13", "InsideRightWall_14", "InsideRightWall_15", "InsideRightWall_16" ),
		HedgeWallFront_HedgeWallRight_InsideRightWall07_InsideRightWall08_InsideRightWall09_InsideRightWall10_InsideRightWall11_InsideRightWall12_InsideRightWall13_InsideRightWall14_InsideRightWall15( "HedgeWall_Front", "HedgeWall_Right", "InsideRightWall_07", "InsideRightWall_08", "InsideRightWall_09", "InsideRightWall_10", "InsideRightWall_11", "InsideRightWall_12", "InsideRightWall_13", "InsideRightWall_14", "InsideRightWall_15" ),
		HedgeWallFront_HedgeWallRight_InsideRightWall07_InsideRightWall08_InsideRightWall09_InsideRightWall10_InsideRightWall11_InsideRightWall12_InsideRightWall13_InsideRightWall14( "HedgeWall_Front", "HedgeWall_Right", "InsideRightWall_07", "InsideRightWall_08", "InsideRightWall_09", "InsideRightWall_10", "InsideRightWall_11", "InsideRightWall_12", "InsideRightWall_13", "InsideRightWall_14" ),
		HedgeWallFront_HedgeWallRight_InsideRightWall07_InsideRightWall08_InsideRightWall09_InsideRightWall10_InsideRightWall11_InsideRightWall12_InsideRightWall13( "HedgeWall_Front", "HedgeWall_Right", "InsideRightWall_07", "InsideRightWall_08", "InsideRightWall_09", "InsideRightWall_10", "InsideRightWall_11", "InsideRightWall_12", "InsideRightWall_13" ),
		HedgeWallFront_HedgeWallRight_InsideRightWall07_InsideRightWall08_InsideRightWall09_InsideRightWall10_InsideRightWall11_InsideRightWall12( "HedgeWall_Front", "HedgeWall_Right", "InsideRightWall_07", "InsideRightWall_08", "InsideRightWall_09", "InsideRightWall_10", "InsideRightWall_11", "InsideRightWall_12" ),
		HedgeWallFront_HedgeWallRight_InsideRightWall07_InsideRightWall08_InsideRightWall09_InsideRightWall10_InsideRightWall11( "HedgeWall_Front", "HedgeWall_Right", "InsideRightWall_07", "InsideRightWall_08", "InsideRightWall_09", "InsideRightWall_10", "InsideRightWall_11" ),
		HedgeWallFront_HedgeWallRight_InsideRightWall07_InsideRightWall08_InsideRightWall09_InsideRightWall10( "HedgeWall_Front", "HedgeWall_Right", "InsideRightWall_07", "InsideRightWall_08", "InsideRightWall_09", "InsideRightWall_10" ),
		HedgeWallFront_HedgeWallRight_InsideRightWall07_InsideRightWall08_InsideRightWall09( "HedgeWall_Front", "HedgeWall_Right", "InsideRightWall_07", "InsideRightWall_08", "InsideRightWall_09" ),
		HedgeWallFront_HedgeWallRight_InsideRightWall07_InsideRightWall08( "HedgeWall_Front", "HedgeWall_Right", "InsideRightWall_07", "InsideRightWall_08" ),
		HedgeWallFront_HedgeWallRight_InsideRightWall07( "HedgeWall_Front", "HedgeWall_Right", "InsideRightWall_07" ),
		HedgeWallFront_HedgeWallRight_HedgeWallBack_HedgeWallLeft_InsideLeftWall07_InsideLeftWall08_InsideLeftWall09_InsideLeftWall10_InsideLeftWall11_InsideLeftWall12_InsideLeftWall13_InsideLeftWall14( "HedgeWall_Front", "HedgeWall_Right", "HedgeWall_Back", "HedgeWall_Left", "InsideLeftWall_07", "InsideLeftWall_08", "InsideLeftWall_09", "InsideLeftWall_10", "InsideLeftWall_11", "InsideLeftWall_12", "InsideLeftWall_13", "InsideLeftWall_14" ),
		HedgeWallFront_HedgeWallRight_HedgeWallBack_HedgeWallLeft_InsideLeftWall07_InsideLeftWall08_InsideLeftWall09_InsideLeftWall10_InsideLeftWall11_InsideLeftWall12_InsideLeftWall13( "HedgeWall_Front", "HedgeWall_Right", "HedgeWall_Back", "HedgeWall_Left", "InsideLeftWall_07", "InsideLeftWall_08", "InsideLeftWall_09", "InsideLeftWall_10", "InsideLeftWall_11", "InsideLeftWall_12", "InsideLeftWall_13" ),
		HedgeWallFront_HedgeWallRight_HedgeWallBack_HedgeWallLeft_InsideLeftWall07_InsideLeftWall08_InsideLeftWall09_InsideLeftWall10_InsideLeftWall11_InsideLeftWall12( "HedgeWall_Front", "HedgeWall_Right", "HedgeWall_Back", "HedgeWall_Left", "InsideLeftWall_07", "InsideLeftWall_08", "InsideLeftWall_09", "InsideLeftWall_10", "InsideLeftWall_11", "InsideLeftWall_12" ),
		HedgeWallFront_HedgeWallRight_HedgeWallBack_HedgeWallLeft_InsideLeftWall07_InsideLeftWall08_InsideLeftWall09_InsideLeftWall10_InsideLeftWall11( "HedgeWall_Front", "HedgeWall_Right", "HedgeWall_Back", "HedgeWall_Left", "InsideLeftWall_07", "InsideLeftWall_08", "InsideLeftWall_09", "InsideLeftWall_10", "InsideLeftWall_11" ),
		HedgeWallFront_HedgeWallRight_HedgeWallBack_HedgeWallLeft_InsideLeftWall07_InsideLeftWall08_InsideLeftWall09_InsideLeftWall10( "HedgeWall_Front", "HedgeWall_Right", "HedgeWall_Back", "HedgeWall_Left", "InsideLeftWall_07", "InsideLeftWall_08", "InsideLeftWall_09", "InsideLeftWall_10" ),
		HedgeWallFront_HedgeWallRight_HedgeWallBack_HedgeWallLeft_InsideLeftWall07_InsideLeftWall08_InsideLeftWall09( "HedgeWall_Front", "HedgeWall_Right", "HedgeWall_Back", "HedgeWall_Left", "InsideLeftWall_07", "InsideLeftWall_08", "InsideLeftWall_09" ),
		HedgeWallFront_HedgeWallRight_HedgeWallBack_HedgeWallLeft_InsideLeftWall07_InsideLeftWall08( "HedgeWall_Front", "HedgeWall_Right", "HedgeWall_Back", "HedgeWall_Left", "InsideLeftWall_07", "InsideLeftWall_08" ),
		HedgeWallFront_HedgeWallRight_HedgeWallBack_HedgeWallLeft_InsideLeftWall07_InsideLeftWall06_InsideLeftWall05_InsideLeftWall04_InsideLeftWall03_InsideLeftWall02_InsideLeftWall01( "HedgeWall_Front", "HedgeWall_Right", "HedgeWall_Back", "HedgeWall_Left", "InsideLeftWall_07", "InsideLeftWall_06", "InsideLeftWall_05", "InsideLeftWall_04", "InsideLeftWall_03", "InsideLeftWall_02", "InsideLeftWall_01" ),
		HedgeWallFront_HedgeWallRight_HedgeWallBack_HedgeWallLeft_InsideLeftWall07_InsideLeftWall06_InsideLeftWall05_InsideLeftWall04_InsideLeftWall03_InsideLeftWall02( "HedgeWall_Front", "HedgeWall_Right", "HedgeWall_Back", "HedgeWall_Left", "InsideLeftWall_07", "InsideLeftWall_06", "InsideLeftWall_05", "InsideLeftWall_04", "InsideLeftWall_03", "InsideLeftWall_02" ),
		HedgeWallFront_HedgeWallRight_HedgeWallBack_HedgeWallLeft_InsideLeftWall07_InsideLeftWall06_InsideLeftWall05_InsideLeftWall04_InsideLeftWall03( "HedgeWall_Front", "HedgeWall_Right", "HedgeWall_Back", "HedgeWall_Left", "InsideLeftWall_07", "InsideLeftWall_06", "InsideLeftWall_05", "InsideLeftWall_04", "InsideLeftWall_03" ),
		HedgeWallFront_HedgeWallRight_HedgeWallBack_HedgeWallLeft_InsideLeftWall07_InsideLeftWall06_InsideLeftWall05_InsideLeftWall04( "HedgeWall_Front", "HedgeWall_Right", "HedgeWall_Back", "HedgeWall_Left", "InsideLeftWall_07", "InsideLeftWall_06", "InsideLeftWall_05", "InsideLeftWall_04" ),
		HedgeWallFront_HedgeWallRight_HedgeWallBack_HedgeWallLeft_InsideLeftWall07_InsideLeftWall06_InsideLeftWall05( "HedgeWall_Front", "HedgeWall_Right", "HedgeWall_Back", "HedgeWall_Left", "InsideLeftWall_07", "InsideLeftWall_06", "InsideLeftWall_05" ),
		HedgeWallFront_HedgeWallRight_HedgeWallBack_HedgeWallLeft_InsideLeftWall07_InsideLeftWall06( "HedgeWall_Front", "HedgeWall_Right", "HedgeWall_Back", "HedgeWall_Left", "InsideLeftWall_07", "InsideLeftWall_06" ),
		HedgeWallFront_HedgeWallRight_HedgeWallBack_HedgeWallLeft_InsideLeftWall07( "HedgeWall_Front", "HedgeWall_Right", "HedgeWall_Back", "HedgeWall_Left", "InsideLeftWall_07" ),
		HedgeWallFront_HedgeWallRight_HedgeWallBack_HedgeWallLeft( "HedgeWall_Front", "HedgeWall_Right", "HedgeWall_Back", "HedgeWall_Left" ),
		HedgeWallFront_HedgeWallRight_HedgeWallBack( "HedgeWall_Front", "HedgeWall_Right", "HedgeWall_Back" ),
		HedgeWallFront_HedgeWallRight( "HedgeWall_Front", "HedgeWall_Right" ),
		HedgeWallFront( "HedgeWall_Front" );
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
