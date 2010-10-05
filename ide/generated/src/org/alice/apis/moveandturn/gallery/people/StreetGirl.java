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
package org.alice.apis.moveandturn.gallery.people;
	
public class StreetGirl extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public StreetGirl() {
		super( "People/street_girl" );
	}
	public enum Part {
		Neck_Head_Ear( "neck", "head", "ear" ),
		Neck_Head_HairMain( "neck", "head", "hair main" ),
		Neck_Head_Hair01( "neck", "head", "hair01" ),
		Neck_Head_Hair02( "neck", "head", "hair02" ),
		Neck_Head_Hair03( "neck", "head", "hair03" ),
		Neck_Head_LeftEye( "neck", "head", "left eye" ),
		Neck_Head_LeftEyebrow( "neck", "head", "left eyebrow" ),
		Neck_Head_RightEye( "neck", "head", "right eye" ),
		Neck_Head_RightEyebrow( "neck", "head", "right eyebrow" ),
		Neck_Head_HairMain2( "neck", "head", "hair main2" ),
		Neck_Head( "neck", "head" ),
		Neck( "neck" ),
		LeftArm_LeftForearm_LeftHand( "left arm", "left forearm", "left hand" ),
		LeftArm_LeftForearm( "left arm", "left forearm" ),
		LeftArm( "left arm" ),
		LeftLeg( "left leg" ),
		RightArm_RightForearm_Righthand( "right arm", "right forearm", "righthand" ),
		RightArm_RightForearm( "right arm", "right forearm" ),
		RightArm( "right arm" ),
		RightLeg( "right leg" );
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
