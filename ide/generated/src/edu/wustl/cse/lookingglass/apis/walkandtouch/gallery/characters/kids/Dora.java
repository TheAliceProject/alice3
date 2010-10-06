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
package edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.characters.kids;

public class Dora extends edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.GalleryPerson { 
	public Dora() {
		super( "kids/Dora" );
}
	public enum Part {
		RightHand( "body", "rightUpperArm", "rightLowerArm", "rightHand" ),
		RightLowerArm( "body", "rightUpperArm", "rightLowerArm" ),
		RightUpperArm( "body", "rightUpperArm" ),
		LeftHand( "body", "leftUpperArm", "leftLowerArm", "leftHand" ),
		LeftLowerArm( "body", "leftUpperArm", "leftLowerArm" ),
		LeftUpperArm( "body", "leftUpperArm" ),
		Braid( "body", "neck", "head", "Braid" ),
		Head( "body", "neck", "head" ),
		Neck( "body", "neck" ),
		Body( "body" ),
		LeftFoot( "leftUpperLeg", "leftLowerLeg", "leftFoot" ),
		LeftLowerLeg( "leftUpperLeg", "leftLowerLeg" ),
		LeftUpperLeg( "leftUpperLeg" ),
		RightFoot( "rightUpperLeg", "rightLowerLeg", "rightFoot" ),
		RightLowerLeg( "rightUpperLeg", "rightLowerLeg" ),
		RightUpperLeg( "rightUpperLeg" );
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

	public void Hug( final edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel Who) {
		this.walkTo( Who, edu.wustl.cse.lookingglass.apis.walkandtouch.SpatialRelation.IN_FRONT_OF, 0.1 );
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					Dora.this.touch( Who, org.alice.apis.moveandturn.MoveDirection.LEFT );
				}
			},
			new Runnable() {
				public void run() {
					Dora.this.touch( Who, org.alice.apis.moveandturn.MoveDirection.RIGHT, edu.wustl.cse.lookingglass.apis.walkandtouch.TouchPart.LEFT_HAND );
				}
			}
		);

		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					Dora.this.getPart(Dora.Part.Body).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05) );
				}
			},
			new Runnable() {
				public void run() {
					Dora.this.look( edu.wustl.cse.lookingglass.apis.walkandtouch.LookDirection.RIGHT );
				}
			},
			new Runnable() {
				public void run() {
					Dora.this.keepTouching( Who );
				}
			},
			new Runnable() {
				public void run() {
					Dora.this.keepTouching( Who, org.alice.apis.moveandturn.MoveDirection.FORWARD, edu.wustl.cse.lookingglass.apis.walkandtouch.TouchPart.LEFT_HAND );
				}
			}
		);

		this.delay(1.0);
		this.standUp( false );
	}

	public void Kick( final edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel Who) {
		this.walkTo( Who, edu.wustl.cse.lookingglass.apis.walkandtouch.SpatialRelation.BEHIND, 0.25 );
		this.touch( Who, org.alice.apis.moveandturn.MoveDirection.BACKWARD, edu.wustl.cse.lookingglass.apis.walkandtouch.TouchPart.RIGHT_FOOT );
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					Dora.this.keepTouching( Who, org.alice.apis.moveandturn.MoveDirection.BACKWARD, edu.wustl.cse.lookingglass.apis.walkandtouch.TouchPart.RIGHT_FOOT );
				}
			},
			new Runnable() {
				public void run() {
					// DoInOrder { 
				Who.move( org.alice.apis.moveandturn.MoveDirection.UP, 0.5, 0.5 );
				Who.move( org.alice.apis.moveandturn.MoveDirection.DOWN, 0.5, 0.5 );
//			}
				}
			},
			new Runnable() {
				public void run() {
					Who.move( org.alice.apis.moveandturn.MoveDirection.FORWARD, 1.0 );
				}
			}
		);

		this.standUp( false );
	}

	public void PointAndLaughAt( final edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel Who) {
		this.turnToFace( Who );
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					Dora.this.look( edu.wustl.cse.lookingglass.apis.walkandtouch.LookDirection.DOWN, 0.02, 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					Dora.this.touch( Who, org.alice.apis.moveandturn.MoveDirection.FORWARD, edu.wustl.cse.lookingglass.apis.walkandtouch.TouchPart.LEFT_HAND, 0.0, 0.5 );
				}
			}
		);

		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					Dora.this.touch( Dora.this.getPart(Dora.Part.Head) );
				}
			},
			new Runnable() {
				public void run() {
					Dora.this.getPart(Dora.Part.RightHand).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.3) );
				}
			}
		);

		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					Dora.this.keepTouching( Dora.this.getPart(Dora.Part.Head), org.alice.apis.moveandturn.MoveDirection.FORWARD, edu.wustl.cse.lookingglass.apis.walkandtouch.TouchPart.RIGHT_HAND, 0.0, 1.5 );
				}
			},
			new Runnable() {
				public void run() {
					for (int index1 = 0; index1 < 3; index1 = index1 + 1) {
				Dora.this.look( edu.wustl.cse.lookingglass.apis.walkandtouch.LookDirection.DOWN, 0.02, 0.25 );
				Dora.this.look( edu.wustl.cse.lookingglass.apis.walkandtouch.LookDirection.FORWARD, 0.02, 0.25 );
			}
				}
			}
		);

		this.standUp( false );
		//this.turnAwayFrom( Who );
	}

	public void TapFootImpatiently( final Number Duration) {
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					Dora.this.touch( Dora.this.getPart(Dora.Part.LeftUpperArm) );
				}
			},
			new Runnable() {
				public void run() {
					Dora.this.touch( Dora.this.getPart(Dora.Part.RightUpperArm), org.alice.apis.moveandturn.MoveDirection.FORWARD, edu.wustl.cse.lookingglass.apis.walkandtouch.TouchPart.LEFT_HAND );
				}
			},
			new Runnable() {
				public void run() {
					Dora.this.getPart(Dora.Part.RightUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.15) );
				}
			}
		);

		for (int index1 = 0; index1 < (Duration.doubleValue() / 0.5); index1 = index1 + 1) {
			this.getPart(Dora.Part.RightFoot).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.25 );
			this.getPart(Dora.Part.RightFoot).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.25 );
		}

		this.standUp( false );
	}
}
