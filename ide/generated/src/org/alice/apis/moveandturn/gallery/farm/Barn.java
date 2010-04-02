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
package org.alice.apis.moveandturn.gallery.farm;
	
public class Barn extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public Barn() {
		super( "Farm/Barn" );
	}
	public enum Part {
		Building_FrontWindow( "building", "frontWindow" ),
		Building_Roof( "building", "roof" ),
		Building_LeftDoor( "building", "leftDoor" ),
		Building_RightDoor( "building", "rightDoor" ),
		Building_BackWindow( "building", "backWindow" ),
		Building( "building" ),
		LeftMainRafter_Rafter( "leftMainRafter", "rafter" ),
		LeftMainRafter_Post( "leftMainRafter", "post" ),
		LeftMainRafter_Post01( "leftMainRafter", "post01" ),
		LeftMainRafter_Post02( "leftMainRafter", "post02" ),
		LeftMainRafter_Post03( "leftMainRafter", "post03" ),
		LeftMainRafter_Post04( "leftMainRafter", "post04" ),
		LeftMainRafter_Post05( "leftMainRafter", "post05" ),
		LeftMainRafter_Post06( "leftMainRafter", "post06" ),
		LeftMainRafter_Post07( "leftMainRafter", "post07" ),
		LeftMainRafter_Post08( "leftMainRafter", "post08" ),
		LeftMainRafter_Rafter01( "leftMainRafter", "rafter01" ),
		LeftMainRafter_Rafter02( "leftMainRafter", "rafter02" ),
		LeftMainRafter_Rafter03( "leftMainRafter", "rafter03" ),
		LeftMainRafter_Rafter04( "leftMainRafter", "rafter04" ),
		LeftMainRafter_Rafter05( "leftMainRafter", "rafter05" ),
		LeftMainRafter_Rafter06( "leftMainRafter", "rafter06" ),
		LeftMainRafter_Rafter07( "leftMainRafter", "rafter07" ),
		LeftMainRafter_Rafter10( "leftMainRafter", "rafter10" ),
		LeftMainRafter_Rafter11( "leftMainRafter", "rafter11" ),
		LeftMainRafter_Rafter12( "leftMainRafter", "rafter12" ),
		LeftMainRafter_Rafter13( "leftMainRafter", "rafter13" ),
		LeftMainRafter_Rafter14( "leftMainRafter", "rafter14" ),
		LeftMainRafter_Rafter15( "leftMainRafter", "rafter15" ),
		LeftMainRafter_Rafter16( "leftMainRafter", "rafter16" ),
		LeftMainRafter_Rafter17( "leftMainRafter", "rafter17" ),
		LeftMainRafter( "leftMainRafter" ),
		RightMainRafter_Rafter18( "rightMainRafter", "rafter18" ),
		RightMainRafter_Rafter19( "rightMainRafter", "rafter19" ),
		RightMainRafter_Rafter20( "rightMainRafter", "rafter20" ),
		RightMainRafter_Post09( "rightMainRafter", "post09" ),
		RightMainRafter_Post10( "rightMainRafter", "post10" ),
		RightMainRafter_Post11( "rightMainRafter", "post11" ),
		RightMainRafter_Post12( "rightMainRafter", "post12" ),
		RightMainRafter_Post13( "rightMainRafter", "post13" ),
		RightMainRafter_Post14( "rightMainRafter", "post14" ),
		RightMainRafter_Post15( "rightMainRafter", "post15" ),
		RightMainRafter_Post16( "rightMainRafter", "post16" ),
		RightMainRafter_Rafter21( "rightMainRafter", "rafter21" ),
		RightMainRafter_Rafter22( "rightMainRafter", "rafter22" ),
		RightMainRafter_Rafter23( "rightMainRafter", "rafter23" ),
		RightMainRafter_Rafter24( "rightMainRafter", "rafter24" ),
		RightMainRafter_Rafter25( "rightMainRafter", "rafter25" ),
		RightMainRafter_Rafter26( "rightMainRafter", "rafter26" ),
		RightMainRafter_Rafter27( "rightMainRafter", "rafter27" ),
		RightMainRafter_Rafter28( "rightMainRafter", "rafter28" ),
		RightMainRafter_Rafter29( "rightMainRafter", "rafter29" ),
		RightMainRafter_Rafter30( "rightMainRafter", "rafter30" ),
		RightMainRafter_Rafter31( "rightMainRafter", "rafter31" ),
		RightMainRafter_Rafter32( "rightMainRafter", "rafter32" ),
		RightMainRafter_Post17( "rightMainRafter", "post17" ),
		RightMainRafter_Rafter33( "rightMainRafter", "rafter33" ),
		RightMainRafter( "rightMainRafter" );
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
