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

public class MrsMiller extends edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.GalleryPerson { 
	edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Boolean> IsAlive= new edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Boolean>();  
	edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Boolean> IsAsleep= new edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Boolean>();  
	public MrsMiller() {
		super( "adults/Mrs Miller" );
		edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Boolean> IsAlive= new edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Boolean>();  
		edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Boolean> IsAsleep= new edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Boolean>();  
}
	public enum Part {
		LeftHand( "leftUpperArm", "leftLowerArm", "leftHand" ),
		LeftLowerArm( "leftUpperArm", "leftLowerArm" ),
		LeftUpperArm( "leftUpperArm" ),
		RightHand( "rightUpperArm", "rightLowerArm", "rightHand" ),
		RightLowerArm( "rightUpperArm", "rightLowerArm" ),
		RightUpperArm( "rightUpperArm" ),
		Head( "head" ),
		RightFoot( "rightUpperLeg", "rightLowerLeg", "rightFoot" ),
		RightLowerLeg( "rightUpperLeg", "rightLowerLeg" ),
		RightUpperLeg( "rightUpperLeg" ),
		LeftFoot( "leftUpperLeg", "leftLowerLeg", "leftFoot" ),
		LeftLowerLeg( "leftUpperLeg", "leftLowerLeg" ),
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

	public void PatOnTheHead( final edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel Who) {
		this.walkTo( Who, edu.wustl.cse.lookingglass.apis.walkandtouch.SpatialRelation.IN_FRONT_OF, 0.15 );
		this.touch( Who, org.alice.apis.moveandturn.MoveDirection.UP );
		for (int index1 = 0; index1 < 3; index1 = index1 + 1) {
			this.touch( Who, org.alice.apis.moveandturn.MoveDirection.UP, edu.wustl.cse.lookingglass.apis.walkandtouch.TouchPart.RIGHT_HAND, 0.04, 0.25 );
			this.touch( Who, org.alice.apis.moveandturn.MoveDirection.UP, edu.wustl.cse.lookingglass.apis.walkandtouch.TouchPart.RIGHT_HAND, 0.0, 0.25 );
		}

		this.standUp( false );
	}

	public void CrossArmsAndTurnRed( ) {
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					MrsMiller.this.touch( MrsMiller.this.getPart(MrsMiller.Part.LeftUpperArm), org.alice.apis.moveandturn.MoveDirection.FORWARD, edu.wustl.cse.lookingglass.apis.walkandtouch.TouchPart.RIGHT_HAND, 0.15 );
				}
			},
			new Runnable() {
				public void run() {
					MrsMiller.this.touch( MrsMiller.this.getPart(MrsMiller.Part.RightUpperArm), org.alice.apis.moveandturn.MoveDirection.FORWARD, edu.wustl.cse.lookingglass.apis.walkandtouch.TouchPart.LEFT_HAND, 0.01 );
				}
			}
		);

		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					MrsMiller.this.look( edu.wustl.cse.lookingglass.apis.walkandtouch.LookDirection.DOWN, 0.1 );
				}
			},
			new Runnable() {
				public void run() {
					MrsMiller.this.getPart(MrsMiller.Part.LeftUpperArm).move( org.alice.apis.moveandturn.MoveDirection.UP, 0.04 );
				}
			},
			new Runnable() {
				public void run() {
					MrsMiller.this.getPart(MrsMiller.Part.RightUpperArm).move( org.alice.apis.moveandturn.MoveDirection.UP, 0.04 );
				}
			},
			new Runnable() {
				public void run() {
					MrsMiller.this.getPart(MrsMiller.Part.Head).setColor( new org.alice.apis.moveandturn.Color(1.0f, 0.6f, 0.6f) );
				}
			}
		);

	}

	public void Spank( final edu.wustl.cse.lookingglass.apis.walkandtouch.Person Who) {
		this.walkTo( Who, edu.wustl.cse.lookingglass.apis.walkandtouch.SpatialRelation.IN_FRONT_OF, 0.25 );
		this.turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25) );
		this.kneel(  );
		Who.walkTo( Who, edu.wustl.cse.lookingglass.apis.walkandtouch.SpatialRelation.RIGHT_OF, -0.1 );
		Who.roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 1.0, Who );
		this.touch( Who, org.alice.apis.moveandturn.MoveDirection.BACKWARD, edu.wustl.cse.lookingglass.apis.walkandtouch.TouchPart.RIGHT_HAND, -0.1, 0.5 );
		for (int index1 = 0; index1 < 3; index1 = index1 + 1) {
			this.touch( Who, org.alice.apis.moveandturn.MoveDirection.BACKWARD, edu.wustl.cse.lookingglass.apis.walkandtouch.TouchPart.RIGHT_HAND, 0.25 );
			this.touch( Who, org.alice.apis.moveandturn.MoveDirection.BACKWARD, edu.wustl.cse.lookingglass.apis.walkandtouch.TouchPart.RIGHT_HAND, -0.1, 0.25 );
			this.delay(0.25);
		}

		this.touch( Who, org.alice.apis.moveandturn.MoveDirection.BACKWARD, edu.wustl.cse.lookingglass.apis.walkandtouch.TouchPart.RIGHT_HAND, 0.25, 0.5 );
		Who.roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 1.0, Who );
		this.standUp( false );
	}

	public void CoverEyesAndShakeHead( ) {
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					MrsMiller.this.touch( MrsMiller.this.getPart(MrsMiller.Part.Head), org.alice.apis.moveandturn.MoveDirection.FORWARD, edu.wustl.cse.lookingglass.apis.walkandtouch.TouchPart.RIGHT_HAND, 0.1 );
				}
			},
			new Runnable() {
				public void run() {
					MrsMiller.this.getPart(MrsMiller.Part.RightHand).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25) );
				}
			}
		);

		this.getPart(MrsMiller.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.02), 0.2 );
		for (int index1 = 0; index1 < 3; index1 = index1 + 1) {
			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						MrsMiller.this.keepTouching( MrsMiller.this.getPart(MrsMiller.Part.Head), org.alice.apis.moveandturn.MoveDirection.FORWARD, edu.wustl.cse.lookingglass.apis.walkandtouch.TouchPart.RIGHT_HAND, 0.1, 0.8 );
					}
				},
				new Runnable() {
					public void run() {
						// DoInOrder { 
					MrsMiller.this.getPart(MrsMiller.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.04), 0.4 );
					MrsMiller.this.getPart(MrsMiller.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.04), 0.4 );
//				}
					}
				}
			);

		}

		this.getPart(MrsMiller.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.02), 0.2 );
		this.delay(0.5);
		this.standUp( false );
	}
}
