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

public class Kristen extends edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.GalleryPerson { 
	private final edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Double> BoardOffset= new edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Double>( 0.0 );
	private final edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Boolean> OnBoard= new edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Boolean>( false );  
	public Kristen() {
		super( "kids/Kristen" );
}
	public enum Part {
		LeftFoot( "hips", "leftUpperLeg", "leftLowerLeg", "leftFoot" ),
		LeftLowerLeg( "hips", "leftUpperLeg", "leftLowerLeg" ),
		LeftUpperLeg( "hips", "leftUpperLeg" ),
		RightFoot( "hips", "rightUpperLeg", "rightLowerLeg", "rightFoot" ),
		RightLowerLeg( "hips", "rightUpperLeg", "rightLowerLeg" ),
		RightUpperLeg( "hips", "rightUpperLeg" ),
		RightHand( "hips", "stomach", "torso", "rightUpperArm", "rightLowerArm", "rightHand" ),
		RightLowerArm( "hips", "stomach", "torso", "rightUpperArm", "rightLowerArm" ),
		RightUpperArm( "hips", "stomach", "torso", "rightUpperArm" ),
		LeftHand( "hips", "stomach", "torso", "leftUpperArm", "leftLowerArm", "leftHand" ),
		LeftLowerArm( "hips", "stomach", "torso", "leftUpperArm", "leftLowerArm" ),
		LeftUpperArm( "hips", "stomach", "torso", "leftUpperArm" ),
		Hair( "hips", "stomach", "torso", "neck", "head", "hair" ),
		LeftEar( "hips", "stomach", "torso", "neck", "head", "leftEar" ),
		RightEar( "hips", "stomach", "torso", "neck", "head", "rightEar" ),
		Head( "hips", "stomach", "torso", "neck", "head" ),
		Neck( "hips", "stomach", "torso", "neck" ),
		Torso( "hips", "stomach", "torso" ),
		Stomach( "hips", "stomach" ),
		Hips( "hips" );
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
					Kristen.this.getPart(Kristen.Part.LeftUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.03) );
				}
			},
			new Runnable() {
				public void run() {
					Kristen.this.getPart(Kristen.Part.RightUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.03) );
				}
			},
			new Runnable() {
				public void run() {
					Kristen.this.getPart(Kristen.Part.RightUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1) );
				}
			},
			new Runnable() {
				public void run() {
					Kristen.this.getPart(Kristen.Part.LeftUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1) );
				}
			},
			new Runnable() {
				public void run() {
					Kristen.this.getPart(Kristen.Part.RightLowerArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.03) );
				}
			},
			new Runnable() {
				public void run() {
					Kristen.this.getPart(Kristen.Part.LeftLowerArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.03) );
				}
			},
			new Runnable() {
				public void run() {
					Kristen.this.getPart(Kristen.Part.Head).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1) );
				}
			},
			new Runnable() {
				public void run() {
					Kristen.this.moveToward( (Kristen.this.getDistanceTo(Who) + 0.38), Who, 0.7 );
				}
			}
		);

		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					Kristen.this.getPart(Kristen.Part.RightUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.02) );
				}
			},
			new Runnable() {
				public void run() {
					Kristen.this.getPart(Kristen.Part.RightLowerLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.2) );
				}
			}
		);

		this.delay(1.5);
		this.standUp( false );
		this.move( org.alice.apis.moveandturn.MoveDirection.BACKWARD, 0.75, 0.75 );
	}

	public void Cheer( final Number NumberOfTimes) {
		for (int index1 = 0; index1 < NumberOfTimes.intValue(); index1 = index1 + 1) {
			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						Kristen.this.move( org.alice.apis.moveandturn.MoveDirection.UP, 0.25, 0.3 );
					}
				},
				new Runnable() {
					public void run() {
						Kristen.this.getPart(Kristen.Part.LeftUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.3 );
					}
				},
				new Runnable() {
					public void run() {
						Kristen.this.getPart(Kristen.Part.RightUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.3 );
					}
				},
				new Runnable() {
					public void run() {
						Kristen.this.getPart(Kristen.Part.LeftLowerLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.3 );
					}
				},
				new Runnable() {
					public void run() {
						Kristen.this.getPart(Kristen.Part.RightLowerLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.3 );
					}
				},
				new Runnable() {
					public void run() {
						Kristen.this.touch( Kristen.this.getPart(Kristen.Part.RightEar), org.alice.apis.moveandturn.MoveDirection.FORWARD, edu.wustl.cse.lookingglass.apis.walkandtouch.TouchPart.RIGHT_HAND, 0.25, 0.3 );
					}
				},
				new Runnable() {
					public void run() {
						Kristen.this.touch( Kristen.this.getPart(Kristen.Part.LeftEar), org.alice.apis.moveandturn.MoveDirection.FORWARD, edu.wustl.cse.lookingglass.apis.walkandtouch.TouchPart.LEFT_HAND, 0.25, 0.3 );
					}
				}
			);

			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						Kristen.this.move( org.alice.apis.moveandturn.MoveDirection.DOWN, 0.25, 0.3 );
					}
				},
				new Runnable() {
					public void run() {
						// DoInOrder { 
					Kristen.this.delay(0.25);
					Kristen.this.standUp( true, 0.25 );
//				}
					}
				}
			);

		}

	}

	public void Slap( final edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel Who) {
		this.walkTo( Who, edu.wustl.cse.lookingglass.apis.walkandtouch.SpatialRelation.IN_FRONT_OF, 0.03 );
		this.touch( Who, org.alice.apis.moveandturn.MoveDirection.UP );
		this.delay(0.1);
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					Kristen.this.getPart(Kristen.Part.RightUpperArm).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.08), 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					Kristen.this.getPart(Kristen.Part.Torso).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					Kristen.this.getPart(Kristen.Part.Neck).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					Who.turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.25 );
				}
			}
		);

		this.delay(0.5);
		this.move( org.alice.apis.moveandturn.MoveDirection.BACKWARD, 0.5, 0.5 );
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					Who.turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.15) );
				}
			},
			new Runnable() {
				public void run() {
					Kristen.this.standUp( false );
				}
			}
		);

	}

	public void Plead( ) {
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					Kristen.this.touch( Kristen.this.getPart(Kristen.Part.Stomach) );
				}
			},
			new Runnable() {
				public void run() {
					Kristen.this.touch( Kristen.this.getPart(Kristen.Part.Stomach), org.alice.apis.moveandturn.MoveDirection.FORWARD, edu.wustl.cse.lookingglass.apis.walkandtouch.TouchPart.LEFT_HAND );
				}
			},
			new Runnable() {
				public void run() {
					Kristen.this.getPart(Kristen.Part.Head).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1) );
				}
			}
		);

		this.delay(1.0);
		this.standUp( false );
	}
}
