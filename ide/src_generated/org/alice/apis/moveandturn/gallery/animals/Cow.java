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
	
public class Cow extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public Cow() {
		super( "Animals/Cow" );
	}
	public enum Part {
		Body_BackBody_LeftUdder_LeftTeat01( "body", "back_body", "left_udder", "left_teat01" ),
		Body_BackBody_LeftUdder_LeftTeat02( "body", "back_body", "left_udder", "left_teat02" ),
		Body_BackBody_LeftUdder_LeftTeat03( "body", "back_body", "left_udder", "left_teat03" ),
		Body_BackBody_LeftUdder( "body", "back_body", "left_udder" ),
		Body_BackBody_RightUdder_LeftTeat04( "body", "back_body", "right_udder", "left_teat04" ),
		Body_BackBody_RightUdder_LeftTeat05( "body", "back_body", "right_udder", "left_teat05" ),
		Body_BackBody_RightUdder_LeftTeat06( "body", "back_body", "right_udder", "left_teat06" ),
		Body_BackBody_RightUdder( "body", "back_body", "right_udder" ),
		Body_BackBody_Tail01_Tail02_Tail03_Tail04_Tail05_Tail06_TailEnd( "body", "back_body", "tail01", "tail02", "tail03", "tail04", "tail05", "tail06", "tailEnd" ),
		Body_BackBody_Tail01_Tail02_Tail03_Tail04_Tail05_Tail06( "body", "back_body", "tail01", "tail02", "tail03", "tail04", "tail05", "tail06" ),
		Body_BackBody_Tail01_Tail02_Tail03_Tail04_Tail05( "body", "back_body", "tail01", "tail02", "tail03", "tail04", "tail05" ),
		Body_BackBody_Tail01_Tail02_Tail03_Tail04( "body", "back_body", "tail01", "tail02", "tail03", "tail04" ),
		Body_BackBody_Tail01_Tail02_Tail03( "body", "back_body", "tail01", "tail02", "tail03" ),
		Body_BackBody_Tail01_Tail02( "body", "back_body", "tail01", "tail02" ),
		Body_BackBody_Tail01( "body", "back_body", "tail01" ),
		Body_BackBody_LeftBackThigh_LeftBackCalf_LeftBackAnkle_LeftBackHoof( "body", "back_body", "left_back_thigh", "left_back_calf", "left_back_ankle", "left_back_hoof" ),
		Body_BackBody_LeftBackThigh_LeftBackCalf_LeftBackAnkle( "body", "back_body", "left_back_thigh", "left_back_calf", "left_back_ankle" ),
		Body_BackBody_LeftBackThigh_LeftBackCalf( "body", "back_body", "left_back_thigh", "left_back_calf" ),
		Body_BackBody_LeftBackThigh( "body", "back_body", "left_back_thigh" ),
		Body_BackBody_RightBackThigh_RightBackCalf_RightBackAnkle_RightBackHoof( "body", "back_body", "right_back_thigh", "right_back_calf", "right_back_ankle", "right_back_hoof" ),
		Body_BackBody_RightBackThigh_RightBackCalf_RightBackAnkle( "body", "back_body", "right_back_thigh", "right_back_calf", "right_back_ankle" ),
		Body_BackBody_RightBackThigh_RightBackCalf( "body", "back_body", "right_back_thigh", "right_back_calf" ),
		Body_BackBody_RightBackThigh( "body", "back_body", "right_back_thigh" ),
		Body_BackBody( "body", "back_body" ),
		Body_RightFrontThigh_RightFrontCalf_RightFrontAnkle_RightFrontHoof( "body", "right_front_thigh", "right_front_calf", "right_front_ankle", "right_front_hoof" ),
		Body_RightFrontThigh_RightFrontCalf_RightFrontAnkle( "body", "right_front_thigh", "right_front_calf", "right_front_ankle" ),
		Body_RightFrontThigh_RightFrontCalf( "body", "right_front_thigh", "right_front_calf" ),
		Body_RightFrontThigh( "body", "right_front_thigh" ),
		Body_LeftFrontThigh_LeftFrontCalf_LeftFrontAnkle_LeftFrontHoof( "body", "left_front_thigh", "left_front_calf", "left_front_ankle", "left_front_hoof" ),
		Body_LeftFrontThigh_LeftFrontCalf_LeftFrontAnkle( "body", "left_front_thigh", "left_front_calf", "left_front_ankle" ),
		Body_LeftFrontThigh_LeftFrontCalf( "body", "left_front_thigh", "left_front_calf" ),
		Body_LeftFrontThigh( "body", "left_front_thigh" ),
		Body( "body" ),
		Neck_Head_RightHorn( "neck", "head", "right_horn" ),
		Neck_Head_LeftHorn( "neck", "head", "left_horn" ),
		Neck_Head_LeftEar( "neck", "head", "left_ear" ),
		Neck_Head_RightEar( "neck", "head", "right_ear" ),
		Neck_Head_Snout_MouthTop_MouthBottom( "neck", "head", "snout", "mouthTop", "mouthBottom" ),
		Neck_Head_Snout_MouthTop( "neck", "head", "snout", "mouthTop" ),
		Neck_Head_Snout( "neck", "head", "snout" ),
		Neck_Head_LeftEye( "neck", "head", "left_eye" ),
		Neck_Head_LeftEyebrow( "neck", "head", "left_eyebrow" ),
		Neck_Head_LeftEye01( "neck", "head", "left_eye01" ),
		Neck_Head_RightEyebrow( "neck", "head", "right_eyebrow" ),
		Neck_Head( "neck", "head" ),
		Neck( "neck" );
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
