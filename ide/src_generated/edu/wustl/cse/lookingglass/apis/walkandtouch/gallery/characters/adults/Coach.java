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

public class Coach extends edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.GalleryPerson { 
	public Coach() {
		super( "adults/Coach" );
}
	public enum Part {
		RightFoot( "rightUpperLeg", "rightFoot" ),
		RightUpperLeg( "rightUpperLeg" ),
		LeftFoot( "leftUpperLeg", "leftFoot" ),
		LeftUpperLeg( "leftUpperLeg" ),
		Whistle( "torso", "head", "Whistle" ),
		Head( "torso", "head" ),
		ChestPlate( "torso", "chestPlate" ),
		LeftHand( "torso", "leftUpperArm", "leftHand" ),
		LeftUpperArm( "torso", "leftUpperArm" ),
		RightHand( "torso", "rightUpperArm", "rightHand" ),
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

	public void Pant( ) {
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					Coach.this.getPart(Coach.Part.Torso).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15) );
				}
			},
			new Runnable() {
				public void run() {
					Coach.this.getPart(Coach.Part.LeftUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15) );
				}
			},
			new Runnable() {
				public void run() {
					Coach.this.getPart(Coach.Part.RightUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15) );
				}
			},
			new Runnable() {
				public void run() {
					Coach.this.getPart(Coach.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05) );
				}
			}
		);

		for (int index1 = 0; index1 < 5; index1 = index1 + 1) {
			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						// DoInOrder { 
					Coach.this.getPart(Coach.Part.Torso).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.02), 0.5 );
					Coach.this.getPart(Coach.Part.Torso).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.02), 0.5 );
//				}
					}
				},
				new Runnable() {
					public void run() {
						// DoInOrder { 
					Coach.this.getPart(Coach.Part.Torso).resize( 1.05, 0.5 );
					Coach.this.getPart(Coach.Part.Torso).resize( 0.9523809523809523, 0.5 );
//				}
					}
				}
			);

		}

		this.standUp( false );
	}

	public void DoJumpingJacks( final Integer HowMany) {
		for (int index1 = 0; index1 < HowMany.intValue(); index1 = index1 + 1) {
			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						// DoInOrder { 
					Coach.this.move( org.alice.apis.moveandturn.MoveDirection.UP, 0.2, 0.25 );
					Coach.this.move( org.alice.apis.moveandturn.MoveDirection.DOWN, Coach.this.getDistanceAbove(Coach.this.getGround()), 0.25 );
//				}
					}
				},
				new Runnable() {
					public void run() {
						Coach.this.getPart(Coach.Part.RightUpperLeg).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Coach.this.getPart(Coach.Part.RightFoot).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Coach.this.getPart(Coach.Part.LeftUpperLeg).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Coach.this.getPart(Coach.Part.LeftFoot).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Coach.this.getPart(Coach.Part.LeftUpperArm).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.4), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Coach.this.getPart(Coach.Part.RightUpperArm).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.4), 0.5 );
					}
				}
			);

			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						// DoInOrder { 
					Coach.this.move( org.alice.apis.moveandturn.MoveDirection.UP, 0.2, 0.25 );
					Coach.this.move( org.alice.apis.moveandturn.MoveDirection.DOWN, Coach.this.getDistanceAbove(Coach.this.getGround()), 0.25 );
//				}
					}
				},
				new Runnable() {
					public void run() {
						Coach.this.getPart(Coach.Part.RightUpperLeg).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Coach.this.getPart(Coach.Part.RightFoot).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Coach.this.getPart(Coach.Part.LeftUpperLeg).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Coach.this.getPart(Coach.Part.LeftFoot).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Coach.this.getPart(Coach.Part.LeftUpperArm).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.4), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Coach.this.getPart(Coach.Part.RightUpperArm).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.4), 0.5 );
					}
				}
			);

		}

	}

	public void DoToeTouches( final Number HowMany) {
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					// DoInOrder { 
				Coach.this.move( org.alice.apis.moveandturn.MoveDirection.UP, 0.2, 0.25 );
				Coach.this.move( org.alice.apis.moveandturn.MoveDirection.DOWN, Coach.this.getDistanceAbove(Coach.this.getGround()), 0.25 );
//			}
				}
			},
			new Runnable() {
				public void run() {
					Coach.this.getPart(Coach.Part.RightUpperLeg).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					Coach.this.getPart(Coach.Part.RightFoot).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					Coach.this.getPart(Coach.Part.LeftUpperLeg).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					Coach.this.getPart(Coach.Part.LeftFoot).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					Coach.this.getPart(Coach.Part.LeftUpperArm).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					Coach.this.getPart(Coach.Part.RightUpperArm).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.5 );
				}
			}
		);

		for (int index1 = 0; index1 < HowMany.intValue(); index1 = index1 + 1) {
			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						Coach.this.getPart(Coach.Part.Torso).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Coach.this.getPart(Coach.Part.Torso).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
					}
				}
			);

			this.touch( this.getPart(Coach.Part.LeftFoot), org.alice.apis.moveandturn.MoveDirection.FORWARD, edu.wustl.cse.lookingglass.apis.walkandtouch.TouchPart.RIGHT_HAND, 0.0, 0.5 );
			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						Coach.this.getPart(Coach.Part.Torso).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Coach.this.getPart(Coach.Part.Torso).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						// DoInOrder { 
					Coach.this.getPart(Coach.Part.RightUpperArm).straightenUp(0.25 );
					Coach.this.getPart(Coach.Part.RightUpperArm).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.25 );
//				}
					}
				}
			);

			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						Coach.this.getPart(Coach.Part.Torso).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Coach.this.getPart(Coach.Part.Torso).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
					}
				}
			);

			this.touch( this.getPart(Coach.Part.RightFoot), org.alice.apis.moveandturn.MoveDirection.FORWARD, edu.wustl.cse.lookingglass.apis.walkandtouch.TouchPart.LEFT_HAND, 0.0, 0.5 );
			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						Coach.this.getPart(Coach.Part.Torso).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Coach.this.getPart(Coach.Part.Torso).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						// DoInOrder { 
					Coach.this.getPart(Coach.Part.LeftUpperArm).straightenUp(0.25 );
					Coach.this.getPart(Coach.Part.LeftUpperArm).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.25 );
//				}
					}
				}
			);

		}

		this.standUp( false );
	}

	public void Cheer( ) {
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					Coach.this.look( edu.wustl.cse.lookingglass.apis.walkandtouch.LookDirection.UP, 0.3, 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					Coach.this.getPart(Coach.Part.LeftUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.48), 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					Coach.this.getPart(Coach.Part.RightUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.48), 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					Coach.this.move( org.alice.apis.moveandturn.MoveDirection.UP, 0.2, 0.25 );
				}
			}
		);

		this.move( org.alice.apis.moveandturn.MoveDirection.DOWN, 0.2, 0.25 );
		this.delay(1.0);
		this.standUp( false );
	}
}
