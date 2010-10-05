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

public class Sheriff extends edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.GalleryPerson { 
	public Sheriff() {
		super( "adults/Sheriff" );
}
	public enum Part {
		Sling( "belt", "sling" ),
		Belt( "belt" ),
		LeftFoot( "leftUpperLeg", "leftLowerLeg", "leftFoot" ),
		LeftLowerLeg( "leftUpperLeg", "leftLowerLeg" ),
		LeftUpperLeg( "leftUpperLeg" ),
		RightFoot( "rightUpperLeg", "rightLowerLeg", "rightFoot" ),
		RightLowerLeg( "rightUpperLeg", "rightLowerLeg" ),
		RightUpperLeg( "rightUpperLeg" ),
		LeftHand( "chest", "leftUpperArm", "leftLowerArm", "leftHand" ),
		LeftLowerArm( "chest", "leftUpperArm", "leftLowerArm" ),
		LeftUpperArm( "chest", "leftUpperArm" ),
		Hat( "chest", "neck", "head", "hat" ),
		Head( "chest", "neck", "head" ),
		Neck( "chest", "neck" ),
		RightHand( "chest", "rightUpperArm", "rightLowerArm", "rightHand" ),
		RightLowerArm( "chest", "rightUpperArm", "rightLowerArm" ),
		RightUpperArm( "chest", "rightUpperArm" ),
		Chest( "chest" );
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

	public void TipHat( ) {
		this.touch( this.getPart(Sheriff.Part.Hat), org.alice.apis.moveandturn.MoveDirection.FORWARD, edu.wustl.cse.lookingglass.apis.walkandtouch.TouchPart.RIGHT_HAND, 0.15 );
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					// DoInOrder { 
				Sheriff.this.getPart(Sheriff.Part.Hat).move( org.alice.apis.moveandturn.MoveDirection.UP, 0.1, 0.5 );
				Sheriff.this.getPart(Sheriff.Part.Hat).move( org.alice.apis.moveandturn.MoveDirection.DOWN, 0.1, 0.5 );
//			}
				}
			},
			new Runnable() {
				public void run() {
					Sheriff.this.keepTouching( Sheriff.this.getPart(Sheriff.Part.Hat), org.alice.apis.moveandturn.MoveDirection.FORWARD, edu.wustl.cse.lookingglass.apis.walkandtouch.TouchPart.RIGHT_HAND, 0.0, 2.0 );
				}
			}
		);

		this.standUp( false );
	}

	public void LookTough( ) {
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					Sheriff.this.touch( Sheriff.this, org.alice.apis.moveandturn.MoveDirection.RIGHT, edu.wustl.cse.lookingglass.apis.walkandtouch.TouchPart.RIGHT_HAND, 0.0, 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					Sheriff.this.touch( Sheriff.this, org.alice.apis.moveandturn.MoveDirection.LEFT, edu.wustl.cse.lookingglass.apis.walkandtouch.TouchPart.LEFT_HAND, 0.0, 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					Sheriff.this.getPart(Sheriff.Part.Chest).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.04), 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					Sheriff.this.getPart(Sheriff.Part.LeftUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.02), 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					Sheriff.this.getPart(Sheriff.Part.LeftFoot).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.02), 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					Sheriff.this.getPart(Sheriff.Part.RightUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.02), 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					Sheriff.this.getPart(Sheriff.Part.RightFoot).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.02), 0.25 );
				}
			}
		);

		this.delay(1.0);
		this.standUp( false );
	}

	public void Intimidate( final edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel Who) {
		this.walkTo( Who, edu.wustl.cse.lookingglass.apis.walkandtouch.SpatialRelation.IN_FRONT_OF, 0.05 );
		this.delay(1.0);
		this.LookTough(  );
	}

	public void Toss( final edu.wustl.cse.lookingglass.apis.walkandtouch.Person Who) {
		this.walkTo( Who, edu.wustl.cse.lookingglass.apis.walkandtouch.SpatialRelation.BEHIND, 0.1 );
		this.touch( Who, org.alice.apis.moveandturn.MoveDirection.BACKWARD );
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					// DoInOrder { 
				Who.turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 1.0, Who );
				Who.move( org.alice.apis.moveandturn.MoveDirection.UP, 0.3, 1.0, Who );
//			}
				}
			},
			new Runnable() {
				public void run() {
					Sheriff.this.keepTouching( Who, org.alice.apis.moveandturn.MoveDirection.BACKWARD, edu.wustl.cse.lookingglass.apis.walkandtouch.TouchPart.RIGHT_HAND, 0.0, 2.0 );
				}
			}
		);

		Who.turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(3.0), 1.0, Who );
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					// DoInOrder { 
				Sheriff.this.keepTouching( Who, org.alice.apis.moveandturn.MoveDirection.BACKWARD );
				Sheriff.this.standUp( false );
//			}
				}
			},
			new Runnable() {
				public void run() {
					Who.move( org.alice.apis.moveandturn.MoveDirection.DOWN, 10.0, 2.0 );
				}
			},
			new Runnable() {
				public void run() {
					// DoInOrder { 
				Who.move( org.alice.apis.moveandturn.MoveDirection.UP, 1.0, 1.0, Who );
				Who.move( org.alice.apis.moveandturn.MoveDirection.DOWN, Who.getDistanceAbove(Sheriff.this.getGround()), 1.0, Who );
//			}
				}
			}
		);

		Who.fallDown(  );
	}
}
