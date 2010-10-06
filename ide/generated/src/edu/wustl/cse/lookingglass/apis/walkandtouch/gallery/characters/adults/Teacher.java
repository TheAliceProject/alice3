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
package edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.characters.adults;

public class Teacher extends edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.GalleryPerson { 
	public Teacher() {
		super( "adults/Teacher" );
}
	public enum Part {
		LeftHand( "body", "leftUpperArm", "leftLowerArm", "leftHand" ),
		LeftLowerArm( "body", "leftUpperArm", "leftLowerArm" ),
		LeftUpperArm( "body", "leftUpperArm" ),
		RightHand( "body", "rightUpperArm", "rightLowerArm", "rightHand" ),
		RightLowerArm( "body", "rightUpperArm", "rightLowerArm" ),
		RightUpperArm( "body", "rightUpperArm" ),
		Head( "body", "neck", "head" ),
		Neck( "body", "neck" ),
		Body( "body" ),
		RightFoot( "rightUpperLeg", "rightFoot" ),
		RightUpperLeg( "rightUpperLeg" ),
		LeftFoot( "leftUpperLeg", "leftFoot" ),
		LeftUpperLeg( "leftUpperLeg" );
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

	public void SmoothHair( ) {
		this.look( edu.wustl.cse.lookingglass.apis.walkandtouch.LookDirection.LEFT );
		for (int index1 = 0; index1 < 2; index1 = index1 + 1) {
			this.touch( this.getPart(Teacher.Part.Head), org.alice.apis.moveandturn.MoveDirection.UP );
			this.touch( this.getPart(Teacher.Part.Head), org.alice.apis.moveandturn.MoveDirection.UP, edu.wustl.cse.lookingglass.apis.walkandtouch.TouchPart.RIGHT_HAND, -0.1 );
		}

		this.standUp( false );
	}

	public void Scold( ) {
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					Teacher.this.getPart(Teacher.Part.RightUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					Teacher.this.getPart(Teacher.Part.RightLowerArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.4), 0.5 );
				}
			}
		);

		for (int index1 = 0; index1 < 3; index1 = index1 + 1) {
			this.getPart(Teacher.Part.RightLowerArm).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.25 );
			this.getPart(Teacher.Part.RightLowerArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.25 );
		}

		this.standUp( false );
	}

	public void PutHandsOnHips( ) {
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					Teacher.this.touch( Teacher.this, org.alice.apis.moveandturn.MoveDirection.RIGHT, edu.wustl.cse.lookingglass.apis.walkandtouch.TouchPart.RIGHT_HAND, -0.15, 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					Teacher.this.touch( Teacher.this, org.alice.apis.moveandturn.MoveDirection.LEFT, edu.wustl.cse.lookingglass.apis.walkandtouch.TouchPart.LEFT_HAND, -0.15, 0.5 );
				}
			}
		);

		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					Teacher.this.touch( Teacher.this, org.alice.apis.moveandturn.MoveDirection.RIGHT, edu.wustl.cse.lookingglass.apis.walkandtouch.TouchPart.RIGHT_HAND, 0.0, 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					Teacher.this.touch( Teacher.this, org.alice.apis.moveandturn.MoveDirection.LEFT, edu.wustl.cse.lookingglass.apis.walkandtouch.TouchPart.LEFT_HAND, 0.0, 0.25 );
				}
			}
		);

	}

	public void Faint( ) {
		this.touch( this.getPart(Teacher.Part.Head), org.alice.apis.moveandturn.MoveDirection.FORWARD, edu.wustl.cse.lookingglass.apis.walkandtouch.TouchPart.RIGHT_HAND, 0.1 );
		this.delay(0.5);
		for (int index1 = 0; index1 < 2; index1 = index1 + 1) {
			this.turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.01) );
			this.turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.02) );
		}

		this.fallDown( edu.wustl.cse.lookingglass.apis.walkandtouch.FallDirection.BACKWARD, 0.5 );
	}
}
