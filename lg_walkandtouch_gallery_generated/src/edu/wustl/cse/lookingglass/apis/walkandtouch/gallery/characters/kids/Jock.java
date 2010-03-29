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

public class Jock extends edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.GalleryPerson { 
	public Jock() {
		super( "kids/Jock" );
}
	public enum Part {
		RightHand( "Torso", "rightUpperArm", "rightLowerArm", "rightHand" ),
		RightLowerArm( "Torso", "rightUpperArm", "rightLowerArm" ),
		RightUpperArm( "Torso", "rightUpperArm" ),
		LeftHand( "Torso", "leftUpperArm", "leftLowerArm", "leftHand" ),
		LeftLowerArm( "Torso", "leftUpperArm", "leftLowerArm" ),
		LeftUpperArm( "Torso", "leftUpperArm" ),
		Head( "Torso", "neck", "head" ),
		Neck( "Torso", "neck" ),
		Torso( "Torso" ),
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

	public void StrokeHair( ) {
		this.touch( this.getPart(Jock.Part.Head), org.alice.apis.moveandturn.MoveDirection.RIGHT );
		for (int index1 = 0; index1 < 2; index1 = index1 + 1) {
			this.getPart(Jock.Part.RightLowerArm).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05) );
			this.getPart(Jock.Part.RightLowerArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05) );
		}

		this.standUp( false );
	}

	public void Punch( final edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel Who) {
		this.walkTo( Who, edu.wustl.cse.lookingglass.apis.walkandtouch.SpatialRelation.IN_FRONT_OF, 0.15 );
		this.move( org.alice.apis.moveandturn.MoveDirection.LEFT, 0.5, 0.25 );
		this.touch( Who, org.alice.apis.moveandturn.MoveDirection.FORWARD, edu.wustl.cse.lookingglass.apis.walkandtouch.TouchPart.RIGHT_HAND, 0.15, 0.25 );
		this.delay(0.5);
		this.touch( Who, org.alice.apis.moveandturn.MoveDirection.FORWARD, edu.wustl.cse.lookingglass.apis.walkandtouch.TouchPart.RIGHT_HAND, 0.0, 0.25 );
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					Jock.this.keepTouching( Who, org.alice.apis.moveandturn.MoveDirection.FORWARD, edu.wustl.cse.lookingglass.apis.walkandtouch.TouchPart.RIGHT_HAND, 0.0, 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					Who.move( org.alice.apis.moveandturn.MoveDirection.BACKWARD, 1.0, 0.25 );
				}
			}
		);

		this.standUp( false );
	}

	public void LookEmbarassed( ) {
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					Jock.this.look( edu.wustl.cse.lookingglass.apis.walkandtouch.LookDirection.DOWN );
				}
			},
			new Runnable() {
				public void run() {
					Jock.this.getPart(Jock.Part.Head).setColor( new org.alice.apis.moveandturn.Color(1.0f, 0.2f, 0.6f) );
				}
			},
			new Runnable() {
				public void run() {
					Jock.this.getPart(Jock.Part.RightUpperArm).move( org.alice.apis.moveandturn.MoveDirection.UP, 0.05 );
				}
			},
			new Runnable() {
				public void run() {
					Jock.this.getPart(Jock.Part.RightUpperArm).move( org.alice.apis.moveandturn.MoveDirection.FORWARD, 0.05 );
				}
			},
			new Runnable() {
				public void run() {
					Jock.this.getPart(Jock.Part.LeftUpperArm).move( org.alice.apis.moveandturn.MoveDirection.UP, 0.05 );
				}
			},
			new Runnable() {
				public void run() {
					Jock.this.getPart(Jock.Part.LeftUpperArm).move( org.alice.apis.moveandturn.MoveDirection.FORWARD, 0.05 );
				}
			}
		);

		this.delay(2.0);
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					Jock.this.look( edu.wustl.cse.lookingglass.apis.walkandtouch.LookDirection.FORWARD );
				}
			},
			new Runnable() {
				public void run() {
					Jock.this.getPart(Jock.Part.Head).setColor( new org.alice.apis.moveandturn.Color(1.0f, 1.0f, 1.0f) );
				}
			},
			new Runnable() {
				public void run() {
					Jock.this.getPart(Jock.Part.RightUpperArm).move( org.alice.apis.moveandturn.MoveDirection.DOWN, 0.05 );
				}
			},
			new Runnable() {
				public void run() {
					Jock.this.getPart(Jock.Part.RightUpperArm).move( org.alice.apis.moveandturn.MoveDirection.BACKWARD, 0.05 );
				}
			},
			new Runnable() {
				public void run() {
					Jock.this.getPart(Jock.Part.LeftUpperArm).move( org.alice.apis.moveandturn.MoveDirection.DOWN, 0.05 );
				}
			},
			new Runnable() {
				public void run() {
					Jock.this.getPart(Jock.Part.LeftUpperArm).move( org.alice.apis.moveandturn.MoveDirection.BACKWARD, 0.05 );
				}
			}
		);

	}

	public void PretendToThrowBall( ) {
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					Jock.this.touch( Jock.this.getPart(Jock.Part.Head), org.alice.apis.moveandturn.MoveDirection.RIGHT, edu.wustl.cse.lookingglass.apis.walkandtouch.TouchPart.RIGHT_HAND, -0.05 );
				}
			},
			new Runnable() {
				public void run() {
					Jock.this.getPart(Jock.Part.Torso).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1) );
				}
			},
			new Runnable() {
				public void run() {
					Jock.this.getPart(Jock.Part.Torso).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.15) );
				}
			}
		);

		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					Jock.this.touch( Jock.this.getPart(Jock.Part.Neck), org.alice.apis.moveandturn.MoveDirection.FORWARD, edu.wustl.cse.lookingglass.apis.walkandtouch.TouchPart.RIGHT_HAND, 1.0, 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					Jock.this.getPart(Jock.Part.Torso).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					Jock.this.getPart(Jock.Part.Torso).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					Jock.this.getPart(Jock.Part.LeftUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					Jock.this.getPart(Jock.Part.RightUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					Jock.this.getPart(Jock.Part.RightLowerLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					Jock.this.move( org.alice.apis.moveandturn.MoveDirection.FORWARD, 0.05, 0.25 );
				}
			}
		);

		this.delay(0.5);
		this.standUp( false );
	}
}
