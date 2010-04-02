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
package org.alice.apis.moveandturn.gallery.animals;
	
public class Husky extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public Husky() {
		super( "Animals/Husky" );
	}
	public enum Part {
		BodyRear_Tail1_Tail2_Tail3_Tail4_Tail5_Tail6( "body-rear", "tail-1", "tail-2", "tail-3", "tail-4", "tail-5", "tail-6" ),
		BodyRear_Tail1_Tail2_Tail3_Tail4_Tail5( "body-rear", "tail-1", "tail-2", "tail-3", "tail-4", "tail-5" ),
		BodyRear_Tail1_Tail2_Tail3_Tail4( "body-rear", "tail-1", "tail-2", "tail-3", "tail-4" ),
		BodyRear_Tail1_Tail2_Tail3( "body-rear", "tail-1", "tail-2", "tail-3" ),
		BodyRear_Tail1_Tail2( "body-rear", "tail-1", "tail-2" ),
		BodyRear_Tail1( "body-rear", "tail-1" ),
		BodyRear_LeftThigh_LeftLeg_LeftShin_LeftFoot( "body-rear", "left-thigh", "left-leg", "left-shin", "left-foot" ),
		BodyRear_LeftThigh_LeftLeg_LeftShin( "body-rear", "left-thigh", "left-leg", "left-shin" ),
		BodyRear_LeftThigh_LeftLeg( "body-rear", "left-thigh", "left-leg" ),
		BodyRear_LeftThigh( "body-rear", "left-thigh" ),
		BodyRear_RightThigh_RightLeg_RightShin_RightFoot( "body-rear", "right-thigh", "right-leg", "right-shin", "right-foot" ),
		BodyRear_RightThigh_RightLeg_RightShin( "body-rear", "right-thigh", "right-leg", "right-shin" ),
		BodyRear_RightThigh_RightLeg( "body-rear", "right-thigh", "right-leg" ),
		BodyRear_RightThigh( "body-rear", "right-thigh" ),
		BodyRear( "body-rear" ),
		BodyFront_LeftUpperArm_LeftLowerArm_LaftHand( "body-front", "left-upper-arm", "left-lower-arm", "laft-hand" ),
		BodyFront_LeftUpperArm_LeftLowerArm( "body-front", "left-upper-arm", "left-lower-arm" ),
		BodyFront_LeftUpperArm( "body-front", "left-upper-arm" ),
		BodyFront_RightUpperArm_RightLowerArm_RightHand( "body-front", "right-upper-arm", "right-lower-arm", "right-hand" ),
		BodyFront_RightUpperArm_RightLowerArm( "body-front", "right-upper-arm", "right-lower-arm" ),
		BodyFront_RightUpperArm( "body-front", "right-upper-arm" ),
		BodyFront_Neck_Head_Snout_Nose( "body-front", "neck", "head", "snout", "nose" ),
		BodyFront_Neck_Head_Snout( "body-front", "neck", "head", "snout" ),
		BodyFront_Neck_Head_Jaw( "body-front", "neck", "head", "jaw" ),
		BodyFront_Neck_Head_RightEar( "body-front", "neck", "head", "right-ear" ),
		BodyFront_Neck_Head_LeftEar( "body-front", "neck", "head", "left-ear" ),
		BodyFront_Neck_Head_RightEye( "body-front", "neck", "head", "right-eye" ),
		BodyFront_Neck_Head_LeftEye( "body-front", "neck", "head", "left-eye" ),
		BodyFront_Neck_Head_LeftEyebrow( "body-front", "neck", "head", "left-eyebrow" ),
		BodyFront_Neck_Head_RightEyebrow( "body-front", "neck", "head", "right-eyebrow" ),
		BodyFront_Neck_Head( "body-front", "neck", "head" ),
		BodyFront_Neck( "body-front", "neck" ),
		BodyFront( "body-front" );
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
