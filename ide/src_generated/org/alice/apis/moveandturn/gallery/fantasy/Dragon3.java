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
package org.alice.apis.moveandturn.gallery.fantasy;
	
public class Dragon3 extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public Dragon3() {
		super( "Fantasy/dragon3" );
	}
	public enum Part {
		Box09( "Box09" ),
		Box08( "Box08" ),
		Box07( "Box07" ),
		Box06( "Box06" ),
		Box03( "Box03" ),
		Box18( "Box18" ),
		Box19( "Box19" ),
		Box20( "Box20" ),
		Box21( "Box21" ),
		Box22( "Box22" ),
		Box24( "Box24" ),
		Box23( "Box23" ),
		FrontLeftUpperLeg_FrontLeftLowerLeg_FrontLeftFoot1_Cylinder08( "frontLeftUpperLeg", "frontLeftLowerLeg", "frontLeftFoot1", "Cylinder08" ),
		FrontLeftUpperLeg_FrontLeftLowerLeg_FrontLeftFoot1_Cylinder07( "frontLeftUpperLeg", "frontLeftLowerLeg", "frontLeftFoot1", "Cylinder07" ),
		FrontLeftUpperLeg_FrontLeftLowerLeg_FrontLeftFoot1_Cylinder06( "frontLeftUpperLeg", "frontLeftLowerLeg", "frontLeftFoot1", "Cylinder06" ),
		FrontLeftUpperLeg_FrontLeftLowerLeg_FrontLeftFoot1_Cylinder05( "frontLeftUpperLeg", "frontLeftLowerLeg", "frontLeftFoot1", "Cylinder05" ),
		FrontLeftUpperLeg_FrontLeftLowerLeg_FrontLeftFoot1_Cylinder04( "frontLeftUpperLeg", "frontLeftLowerLeg", "frontLeftFoot1", "Cylinder04" ),
		FrontLeftUpperLeg_FrontLeftLowerLeg_FrontLeftFoot1( "frontLeftUpperLeg", "frontLeftLowerLeg", "frontLeftFoot1" ),
		FrontLeftUpperLeg_FrontLeftLowerLeg( "frontLeftUpperLeg", "frontLeftLowerLeg" ),
		FrontLeftUpperLeg( "frontLeftUpperLeg" ),
		BackLeftUpperLeg_BackLeftLowerLeg_BackLeftFootvv_Cylinder15( "backLeftUpperLeg", "backLeftLowerLeg", "backLeftFootvv", "Cylinder15" ),
		BackLeftUpperLeg_BackLeftLowerLeg_BackLeftFootvv_Cylinder13( "backLeftUpperLeg", "backLeftLowerLeg", "backLeftFootvv", "Cylinder13" ),
		BackLeftUpperLeg_BackLeftLowerLeg_BackLeftFootvv_Cylinder10( "backLeftUpperLeg", "backLeftLowerLeg", "backLeftFootvv", "Cylinder10" ),
		BackLeftUpperLeg_BackLeftLowerLeg_BackLeftFootvv_Cylinder12( "backLeftUpperLeg", "backLeftLowerLeg", "backLeftFootvv", "Cylinder12" ),
		BackLeftUpperLeg_BackLeftLowerLeg_BackLeftFootvv_Cylinder09( "backLeftUpperLeg", "backLeftLowerLeg", "backLeftFootvv", "Cylinder09" ),
		BackLeftUpperLeg_BackLeftLowerLeg_BackLeftFootvv( "backLeftUpperLeg", "backLeftLowerLeg", "backLeftFootvv" ),
		BackLeftUpperLeg_BackLeftLowerLeg( "backLeftUpperLeg", "backLeftLowerLeg" ),
		BackLeftUpperLeg( "backLeftUpperLeg" ),
		LeftWingClose_LeftWingFar( "leftWingClose", "leftWingFar" ),
		LeftWingClose( "leftWingClose" ),
		RightWingClose_RightWingFar( "rightWingClose", "rightWingFar" ),
		RightWingClose( "rightWingClose" ),
		BackRightUpperLeg_BackRightLowerLeg_BackRightFoot_Cylinder19( "backRightUpperLeg", "backRightLowerLeg", "backRightFoot", "Cylinder19" ),
		BackRightUpperLeg_BackRightLowerLeg_BackRightFoot_Cylinder29( "backRightUpperLeg", "backRightLowerLeg", "backRightFoot", "Cylinder29" ),
		BackRightUpperLeg_BackRightLowerLeg_BackRightFoot_Cylinder27( "backRightUpperLeg", "backRightLowerLeg", "backRightFoot", "Cylinder27" ),
		BackRightUpperLeg_BackRightLowerLeg_BackRightFoot_Cylinder30( "backRightUpperLeg", "backRightLowerLeg", "backRightFoot", "Cylinder30" ),
		BackRightUpperLeg_BackRightLowerLeg_BackRightFoot_Cylinder26( "backRightUpperLeg", "backRightLowerLeg", "backRightFoot", "Cylinder26" ),
		BackRightUpperLeg_BackRightLowerLeg_BackRightFoot( "backRightUpperLeg", "backRightLowerLeg", "backRightFoot" ),
		BackRightUpperLeg_BackRightLowerLeg( "backRightUpperLeg", "backRightLowerLeg" ),
		BackRightUpperLeg( "backRightUpperLeg" ),
		FrontRtUpperLeg_FrontRtLowerLeg_FrontRightFoot_Cylinder22( "frontRtUpperLeg", "frontRtLowerLeg", "frontRightFoot", "Cylinder22" ),
		FrontRtUpperLeg_FrontRtLowerLeg_FrontRightFoot_Cylinder23( "frontRtUpperLeg", "frontRtLowerLeg", "frontRightFoot", "Cylinder23" ),
		FrontRtUpperLeg_FrontRtLowerLeg_FrontRightFoot_Cylinder24( "frontRtUpperLeg", "frontRtLowerLeg", "frontRightFoot", "Cylinder24" ),
		FrontRtUpperLeg_FrontRtLowerLeg_FrontRightFoot_Cylinder25( "frontRtUpperLeg", "frontRtLowerLeg", "frontRightFoot", "Cylinder25" ),
		FrontRtUpperLeg_FrontRtLowerLeg_FrontRightFoot_Cylinder20( "frontRtUpperLeg", "frontRtLowerLeg", "frontRightFoot", "Cylinder20" ),
		FrontRtUpperLeg_FrontRtLowerLeg_FrontRightFoot( "frontRtUpperLeg", "frontRtLowerLeg", "frontRightFoot" ),
		FrontRtUpperLeg_FrontRtLowerLeg( "frontRtUpperLeg", "frontRtLowerLeg" ),
		FrontRtUpperLeg( "frontRtUpperLeg" ),
		Neck_Box17( "neck", "Box17" ),
		Neck_Box16( "neck", "Box16" ),
		Neck_Box15( "neck", "Box15" ),
		Neck_Box14( "neck", "Box14" ),
		Neck_Box13( "neck", "Box13" ),
		Neck_Box12( "neck", "Box12" ),
		Neck_Box11( "neck", "Box11" ),
		Neck_Box10( "neck", "Box10" ),
		Neck_Head_Jaw( "neck", "head", "jaw" ),
		Neck_Head_Box37( "neck", "head", "Box37" ),
		Neck_Head_Box35( "neck", "head", "Box35" ),
		Neck_Head_Box36( "neck", "head", "Box36" ),
		Neck_Head( "neck", "head" ),
		Neck( "neck" ),
		Tail_Box25( "tail", "Box25" ),
		Tail_Box26( "tail", "Box26" ),
		Tail_Box27( "tail", "Box27" ),
		Tail_Box28( "tail", "Box28" ),
		Tail_Box30( "tail", "Box30" ),
		Tail_Box31( "tail", "Box31" ),
		Tail_Box29( "tail", "Box29" ),
		Tail_Box32( "tail", "Box32" ),
		Tail_Box33( "tail", "Box33" ),
		Tail_Box34( "tail", "Box34" ),
		Tail( "tail" ),
		TailJoint( "tailJoint" ),
		NeckJoint( "neckJoint" );
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
