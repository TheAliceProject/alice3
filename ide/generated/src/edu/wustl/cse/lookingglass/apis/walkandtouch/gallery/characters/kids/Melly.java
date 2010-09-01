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

public class Melly extends edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.GalleryPerson { 
	private final edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Boolean> OnBoard= new edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Boolean>( false );  
	public Melly() {
		super( "kids/Melly" );
}
	public enum Part {
		LeftPonytail( "Hips", "Stomach", "Torso", "Neck", "Head", "LeftPonytail" ),
		RightPonytail( "Hips", "Stomach", "Torso", "Neck", "Head", "RightPonytail" ),
		Hat( "Hips", "Stomach", "Torso", "Neck", "Head", "Hat" ),
		RightEar( "Hips", "Stomach", "Torso", "Neck", "Head", "RightEar" ),
		LeftEar( "Hips", "Stomach", "Torso", "Neck", "Head", "LeftEar" ),
		Head( "Hips", "Stomach", "Torso", "Neck", "Head" ),
		Neck( "Hips", "Stomach", "Torso", "Neck" ),
		RightHand( "Hips", "Stomach", "Torso", "rightUpperArm", "rightLowerArm", "rightHand" ),
		RightLowerArm( "Hips", "Stomach", "Torso", "rightUpperArm", "rightLowerArm" ),
		RightUpperArm( "Hips", "Stomach", "Torso", "rightUpperArm" ),
		LeftHand( "Hips", "Stomach", "Torso", "leftUpperArm", "LeftLowerArm", "leftHand" ),
		LeftLowerArm( "Hips", "Stomach", "Torso", "leftUpperArm", "LeftLowerArm" ),
		LeftUpperArm( "Hips", "Stomach", "Torso", "leftUpperArm" ),
		Torso( "Hips", "Stomach", "Torso" ),
		Stomach( "Hips", "Stomach" ),
		RightFoot( "Hips", "rightUpperLeg", "rightLowerLeg", "rightFoot" ),
		RightLowerLeg( "Hips", "rightUpperLeg", "rightLowerLeg" ),
		RightUpperLeg( "Hips", "rightUpperLeg" ),
		LeftFoot( "Hips", "leftUpperLeg", "leftLowerLeg", "leftFoot" ),
		LeftLowerLeg( "Hips", "leftUpperLeg", "leftLowerLeg" ),
		LeftUpperLeg( "Hips", "leftUpperLeg" ),
		Hips( "Hips" );
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

	public void Kiss( final edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel Who) {
		this.walkTo( Who );
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					Melly.this.getPart(Melly.Part.Head).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1) );
				}
			},
			new Runnable() {
				public void run() {
					Melly.this.getPart(Melly.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.03) );
				}
			},
			new Runnable() {
				public void run() {
					Melly.this.getPart(Melly.Part.RightUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25) );
				}
			},
			new Runnable() {
				public void run() {
					Melly.this.getPart(Melly.Part.RightLowerArm).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25) );
				}
			},
			new Runnable() {
				public void run() {
					Melly.this.getPart(Melly.Part.LeftUpperArm).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1) );
				}
			},
			new Runnable() {
				public void run() {
					Melly.this.getPart(Melly.Part.LeftUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.3) );
				}
			},
			new Runnable() {
				public void run() {
					Melly.this.getPart(Melly.Part.LeftLowerArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1) );
				}
			},
			new Runnable() {
				public void run() {
					Melly.this.moveToward( (Melly.this.getDistanceTo(Who) + 0.5), Who, 0.5 );
				}
			}
		);

		this.delay(2.5);
		this.standUp( false );
		this.move( org.alice.apis.moveandturn.MoveDirection.BACKWARD, 0.5, 0.5 );
	}

	public void Backflip( ) {
		 final edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Double> Zero= new edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Double>( 0.0 );
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					Melly.this.getPart(Melly.Part.RightUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1) );
				}
			},
			new Runnable() {
				public void run() {
					Melly.this.getPart(Melly.Part.LeftUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1) );
				}
			},
			new Runnable() {
				public void run() {
					Melly.this.getPart(Melly.Part.RightLowerLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1) );
				}
			},
			new Runnable() {
				public void run() {
					Melly.this.getPart(Melly.Part.LeftLowerLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1) );
				}
			},
			new Runnable() {
				public void run() {
					Melly.this.move( org.alice.apis.moveandturn.MoveDirection.DOWN, 0.1 );
				}
			}
		);

		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					// DoInOrder { 
				org.alice.virtualmachine.DoTogether.invokeAndWait(
					new Runnable() {
						public void run() {
							Melly.this.getPart(Melly.Part.RightUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.1 );
						}
					},
					new Runnable() {
						public void run() {
							Melly.this.getPart(Melly.Part.LeftUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.1 );
						}
					},
					new Runnable() {
						public void run() {
							Melly.this.getPart(Melly.Part.RightLowerLeg).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.1 );
						}
					},
					new Runnable() {
						public void run() {
							Melly.this.getPart(Melly.Part.LeftLowerLeg).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.1 );
						}
					},
					new Runnable() {
						public void run() {
							Melly.this.getPart(Melly.Part.RightUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.35), 0.1 );
						}
					},
					new Runnable() {
						public void run() {
							Melly.this.getPart(Melly.Part.LeftUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.35), 0.1 );
						}
					}
				);

				org.alice.virtualmachine.DoTogether.invokeAndWait(
					new Runnable() {
						public void run() {
							Melly.this.getPart(Melly.Part.RightUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.5 );
						}
					},
					new Runnable() {
						public void run() {
							Melly.this.getPart(Melly.Part.LeftUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.5 );
						}
					},
					new Runnable() {
						public void run() {
							Melly.this.getPart(Melly.Part.RightLowerLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.5 );
						}
					},
					new Runnable() {
						public void run() {
							Melly.this.getPart(Melly.Part.LeftLowerLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.5 );
						}
					}
				);

				Melly.this.turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(1.0), 0.5, Melly.this.getPart(Melly.Part.Stomach) );
//			}
				}
			},
			new Runnable() {
				public void run() {
					Melly.this.move( org.alice.apis.moveandturn.MoveDirection.UP, 1.0, 0.5, org.alice.apis.moveandturn.AsSeenBy.SCENE );
				}
			}
		);

		this.move( org.alice.apis.moveandturn.MoveDirection.DOWN, 0.5, 0.25 );
		this.standUp( false );
	}

	public void Punch( final edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel Who) {
		this.walkTo( Who, edu.wustl.cse.lookingglass.apis.walkandtouch.SpatialRelation.IN_FRONT_OF, 0.05 );
		this.touch( Who );
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					Melly.this.touch( Who, org.alice.apis.moveandturn.MoveDirection.FORWARD, edu.wustl.cse.lookingglass.apis.walkandtouch.TouchPart.RIGHT_HAND, -0.25, 0.2 );
				}
			},
			new Runnable() {
				public void run() {
					// DoInOrder { 
				Melly.this.delay(0.2);
				Who.move( org.alice.apis.moveandturn.MoveDirection.BACKWARD, 0.5, 0.25 );
//			}
				}
			}
		);

		this.move( org.alice.apis.moveandturn.MoveDirection.BACKWARD, 0.2, 0.5 );
		this.standUp( false );
	}

	public void Shrug( ) {
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					Melly.this.getPart(Melly.Part.Head).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					Melly.this.getPart(Melly.Part.RightUpperArm).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					Melly.this.getPart(Melly.Part.RightHand).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					Melly.this.getPart(Melly.Part.RightLowerArm).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.35), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					Melly.this.getPart(Melly.Part.LeftUpperArm).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					Melly.this.getPart(Melly.Part.LeftHand).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					Melly.this.getPart(Melly.Part.LeftLowerArm).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.35), 0.5 );
				}
			}
		);

		this.delay(1.0);
		this.standUp( false );
	}
}
