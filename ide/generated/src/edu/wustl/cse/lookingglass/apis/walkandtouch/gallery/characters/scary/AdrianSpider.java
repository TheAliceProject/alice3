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
package edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.characters.scary;

public class AdrianSpider extends edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.GalleryCharacter { 
	public AdrianSpider() {
		super( "scary/AdrianSpider" );
}
	public enum Part {
		RightFrontFoot( "rightFrontUpperLeg", "rightFrontLowerLeg", "rightFrontFoot" ),
		RightFrontLowerLeg( "rightFrontUpperLeg", "rightFrontLowerLeg" ),
		RightFrontUpperLeg( "rightFrontUpperLeg" ),
		RightSecondFoot( "rightSecondUpperLeg", "rightSecondLower", "rightSecondFoot" ),
		RightSecondLower( "rightSecondUpperLeg", "rightSecondLower" ),
		RightSecondUpperLeg( "rightSecondUpperLeg" ),
		RightThirdFoot( "rightThirdUpperLeg", "rightThirdMiddleLeg", "rightThirdFoot" ),
		RightThirdMiddleLeg( "rightThirdUpperLeg", "rightThirdMiddleLeg" ),
		RightThirdUpperLeg( "rightThirdUpperLeg" ),
		RightRearFoot( "rightRearUpperLeg", "rightRearLowerLeg", "rightRearFoot" ),
		RightRearLowerLeg( "rightRearUpperLeg", "rightRearLowerLeg" ),
		RightRearUpperLeg( "rightRearUpperLeg" ),
		LeftRearFoot( "leftRearUpperLeg", "leftRearLowerLeg", "leftRearFoot" ),
		LeftRearLowerLeg( "leftRearUpperLeg", "leftRearLowerLeg" ),
		LeftRearUpperLeg( "leftRearUpperLeg" ),
		LeftThirdFoot( "leftThirdUpperLeg", "leftThirdLowerLeg", "leftThirdFoot" ),
		LeftThirdLowerLeg( "leftThirdUpperLeg", "leftThirdLowerLeg" ),
		LeftThirdUpperLeg( "leftThirdUpperLeg" ),
		LeftSecondFoot( "leftSecondUpperLeg", "leftSecondLowerLeg", "leftSecondFoot" ),
		LeftSecondLowerLeg( "leftSecondUpperLeg", "leftSecondLowerLeg" ),
		LeftSecondUpperLeg( "leftSecondUpperLeg" ),
		LeftFrontFoot( "leftFrontUpperLeg", "leftFrontLowerLeg", "leftFrontFoot" ),
		LeftFrontLowerLeg( "leftFrontUpperLeg", "leftFrontLowerLeg" ),
		LeftFrontUpperLeg( "leftFrontUpperLeg" ),
		LeftEye( "neck", "head", "leftEye" ),
		RightEyelid( "neck", "head", "rightEyelid" ),
		RightEye( "neck", "head", "rightEye" ),
		RightEyebrow( "neck", "head", "rightEyebrow" ),
		LeftEyebrow( "neck", "head", "leftEyebrow" ),
		LeftEyelid( "neck", "head", "leftEyelid" ),
		Head( "neck", "head" ),
		Neck( "neck" );
		private String[] m_path;
		Part( String... path ) {
			m_path = path;
		}
		public String[] getPath() {
			return m_path;
		}
	}
	public edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel getPart( Part part ) {
		return getDescendant( edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel.class, part.getPath() );
	}

	public void NodYes( ) {
		this.getPart(AdrianSpider.Part.Neck).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.2 );
		this.getPart(AdrianSpider.Part.Neck).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.25 );
		this.getPart(AdrianSpider.Part.Neck).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.25 );
		this.getPart(AdrianSpider.Part.Neck).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.2 );
		this.getPart(AdrianSpider.Part.Neck).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.25 );
		this.getPart(AdrianSpider.Part.Neck).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.2 );
	}

	public void ShakeHeadNo( ) {
		this.getPart(AdrianSpider.Part.Neck).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.3 );
		this.getPart(AdrianSpider.Part.Neck).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.5 );
		this.getPart(AdrianSpider.Part.Neck).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.5 );
		this.getPart(AdrianSpider.Part.Neck).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.3 );
	}
}
