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
package edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.characters.heroic;

public class Samurai extends edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.GalleryPerson { 
	public Samurai() {
		super( "heroic/Samurai" );
}
	public enum Part {
		LeftFoot( "leftUpperLeg", "leftLowerLeg", "leftFoot" ),
		LeftLowerLeg( "leftUpperLeg", "leftLowerLeg" ),
		LeftUpperLeg( "leftUpperLeg" ),
		RightFoot( "rightUpperLeg", "rightLowerLeg", "rightFoot" ),
		RightLowerLeg( "rightUpperLeg", "rightLowerLeg" ),
		RightUpperLeg( "rightUpperLeg" ),
		Belt( "torso", "belt" ),
		Mask( "torso", "head", "helmet", "mask" ),
		Helmet( "torso", "head", "helmet" ),
		Head( "torso", "head" ),
		LeftHand( "torso", "leftUpperArm", "leftLowerArm", "leftHand" ),
		LeftLowerArm( "torso", "leftUpperArm", "leftLowerArm" ),
		LeftUpperArm( "torso", "leftUpperArm" ),
		RightHand( "torso", "rightUpperArm", "rightLowerArm", "rightHand" ),
		RightLowerArm( "torso", "rightUpperArm", "rightLowerArm" ),
		RightUpperArm( "torso", "rightUpperArm" ),
		Torso( "torso" );
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

	public void Bow( ) {
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					Samurai.this.touch( Samurai.this.getPart(Samurai.Part.Torso), org.alice.apis.moveandturn.MoveDirection.FORWARD, edu.wustl.cse.lookingglass.apis.walkandtouch.TouchPart.RIGHT_HAND, 0.0, 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					Samurai.this.touch( Samurai.this.getPart(Samurai.Part.Torso), org.alice.apis.moveandturn.MoveDirection.FORWARD, edu.wustl.cse.lookingglass.apis.walkandtouch.TouchPart.LEFT_HAND, 0.0, 0.5 );
				}
			}
		);

		this.delay(0.5);
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					Samurai.this.look( edu.wustl.cse.lookingglass.apis.walkandtouch.LookDirection.DOWN );
				}
			},
			new Runnable() {
				public void run() {
					Samurai.this.getPart(Samurai.Part.Torso).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1) );
				}
			}
		);

		this.delay(1.0);
		this.standUp( false );
	}

	public void Flip( final edu.wustl.cse.lookingglass.apis.walkandtouch.Person Who) {
		this.walkTo( Who, edu.wustl.cse.lookingglass.apis.walkandtouch.SpatialRelation.BEHIND, 0.25 );
		this.touch( Who );
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					Samurai.this.keepTouching( Who, org.alice.apis.moveandturn.MoveDirection.FORWARD, edu.wustl.cse.lookingglass.apis.walkandtouch.TouchPart.RIGHT_HAND, 0.0, 2.0 );
				}
			},
			new Runnable() {
				public void run() {
					// DoInOrder { 
				org.alice.virtualmachine.DoTogether.invokeAndWait(
					new Runnable() {
						public void run() {
							Who.turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 1.0, Who );
						}
					},
					new Runnable() {
						public void run() {
							Who.move( org.alice.apis.moveandturn.MoveDirection.UP, 0.7, 1.0, Who );
						}
					}
				);

				org.alice.virtualmachine.DoTogether.invokeAndWait(
					new Runnable() {
						public void run() {
							Who.move( org.alice.apis.moveandturn.MoveDirection.DOWN, 5.0 );
						}
					},
					new Runnable() {
						public void run() {
							// DoInOrder { 
						Who.move( org.alice.apis.moveandturn.MoveDirection.UP, 1.0, 0.5, Who );
						Who.move( org.alice.apis.moveandturn.MoveDirection.DOWN, Who.getDistanceAbove(Samurai.this.getGround()), 0.5, Who );
//					}
						}
					}
				);

//			}
				}
			}
		);

		Who.fallDown(  );
		this.standUp( false );
	}

	public void ScrewdriverMove( final edu.wustl.cse.lookingglass.apis.walkandtouch.Person Who) {
		this.move( org.alice.apis.moveandturn.MoveDirection.UP, 1.5 );
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					Samurai.this.getPart(Samurai.Part.LeftUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1) );
				}
			},
			new Runnable() {
				public void run() {
					Samurai.this.getPart(Samurai.Part.LeftLowerLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.07) );
				}
			},
			new Runnable() {
				public void run() {
					Samurai.this.getPart(Samurai.Part.RightUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.12) );
				}
			},
			new Runnable() {
				public void run() {
					Samurai.this.getPart(Samurai.Part.RightLowerLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.07) );
				}
			},
			new Runnable() {
				public void run() {
					Samurai.this.getPart(Samurai.Part.Torso).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.07) );
				}
			}
		);

		this.moveTo( Who, edu.wustl.cse.lookingglass.apis.walkandtouch.SpatialRelation.ABOVE, 1.0 );
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					Samurai.this.getPart(Samurai.Part.LeftUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.5) );
				}
			},
			new Runnable() {
				public void run() {
					Samurai.this.getPart(Samurai.Part.RightUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.5) );
				}
			},
			new Runnable() {
				public void run() {
					Samurai.this.getPart(Samurai.Part.RightUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.3) );
				}
			},
			new Runnable() {
				public void run() {
					Samurai.this.getPart(Samurai.Part.LeftUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1) );
				}
			}
		);

		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					Samurai.this.move( org.alice.apis.moveandturn.MoveDirection.DOWN, (Samurai.this.getDistanceTo(Samurai.this.getGround()) + 1.3) );
				}
			},
			new Runnable() {
				public void run() {
					Samurai.this.turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(6.0) );
				}
			},
			new Runnable() {
				public void run() {
					// DoInOrder { 
				Samurai.this.move( org.alice.apis.moveandturn.MoveDirection.LEFT, 0.3, 0.25 );
				Who.fallDown( edu.wustl.cse.lookingglass.apis.walkandtouch.FallDirection.LEFT );
//			}
				}
			}
		);

		this.standUp( false );
	}
}
