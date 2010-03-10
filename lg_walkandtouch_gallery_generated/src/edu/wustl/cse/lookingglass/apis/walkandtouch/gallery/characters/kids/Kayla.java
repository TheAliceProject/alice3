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

public class Kayla extends edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.GalleryPerson { 
	public Kayla() {
		super( "kids/Kayla" );
}
	public enum Part {
		RightHand( "torso", "rightUpperArm", "rightLowerArm", "rightHand" ),
		Corsage( "torso", "rightUpperArm", "rightLowerArm", "corsage" ),
		RightLowerArm( "torso", "rightUpperArm", "rightLowerArm" ),
		RightUpperArm( "torso", "rightUpperArm" ),
		LeftHand( "torso", "leftUpperArm", "leftLowerArm", "leftHand" ),
		LeftLowerArm( "torso", "leftUpperArm", "leftLowerArm" ),
		LeftUpperArm( "torso", "leftUpperArm" ),
		Head( "torso", "neck", "head" ),
		Neck( "torso", "neck" ),
		Torso( "torso" ),
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

	public void Dance( final Number Duration) {
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					Kayla.this.getPart(Kayla.Part.RightLowerArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.35) );
				}
			},
			new Runnable() {
				public void run() {
					Kayla.this.getPart(Kayla.Part.LeftLowerArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.35) );
				}
			},
			new Runnable() {
				public void run() {
					Kayla.this.getPart(Kayla.Part.RightUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1) );
				}
			},
			new Runnable() {
				public void run() {
					Kayla.this.getPart(Kayla.Part.LeftUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1) );
				}
			},
			new Runnable() {
				public void run() {
					Kayla.this.roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.01) );
				}
			},
			new Runnable() {
				public void run() {
					Kayla.this.getPart(Kayla.Part.Torso).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.01) );
				}
			},
			new Runnable() {
				public void run() {
					Kayla.this.getPart(Kayla.Part.RightLowerArm).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.075) );
				}
			},
			new Runnable() {
				public void run() {
					Kayla.this.getPart(Kayla.Part.LeftLowerArm).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.075) );
				}
			}
		);

		for (int index1 = 0; index1 < Duration.intValue(); index1 = index1 + 1) {
			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						Kayla.this.getPart(Kayla.Part.RightLowerArm).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Kayla.this.getPart(Kayla.Part.LeftLowerArm).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Kayla.this.roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.02), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Kayla.this.getPart(Kayla.Part.Torso).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.02), 0.5 );
					}
				}
			);

			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						Kayla.this.getPart(Kayla.Part.RightLowerArm).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Kayla.this.getPart(Kayla.Part.LeftLowerArm).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Kayla.this.roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.02), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Kayla.this.getPart(Kayla.Part.Torso).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.02), 0.5 );
					}
				}
			);

		}

		this.standUp( false );
	}

	public void FixHair( ) {
		for (int index1 = 0; index1 < 2; index1 = index1 + 1) {
			this.touch( this.getPart(Kayla.Part.Head), org.alice.apis.moveandturn.MoveDirection.RIGHT, edu.wustl.cse.lookingglass.apis.walkandtouch.TouchPart.RIGHT_HAND, 0.1 );
			this.touch( this.getPart(Kayla.Part.Head), org.alice.apis.moveandturn.MoveDirection.RIGHT, edu.wustl.cse.lookingglass.apis.walkandtouch.TouchPart.RIGHT_HAND, 0.08 );
		}

		this.standUp( false );
	}

	public void LookHonored( final Boolean NodYes) {
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					Kayla.this.touch( Kayla.this.getPart(Kayla.Part.Torso) );
				}
			},
			new Runnable() {
				public void run() {
					Kayla.this.getPart(Kayla.Part.RightHand).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.2) );
				}
			},
			new Runnable() {
				public void run() {
					Kayla.this.getPart(Kayla.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.02) );
				}
			},
			new Runnable() {
				public void run() {
					Kayla.this.getPart(Kayla.Part.Head).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.05) );
				}
			}
		);

		this.delay(0.5);
		this.standUp( false );
		if (NodYes.booleanValue() ) {			this.look( edu.wustl.cse.lookingglass.apis.walkandtouch.LookDirection.DOWN, 0.05 );
			this.look( edu.wustl.cse.lookingglass.apis.walkandtouch.LookDirection.FORWARD );
		} else { 
			this.look( edu.wustl.cse.lookingglass.apis.walkandtouch.LookDirection.RIGHT, 0.02, 0.25 );
			for (int index1 = 0; index1 < 2; index1 = index1 + 1) {
				this.look( edu.wustl.cse.lookingglass.apis.walkandtouch.LookDirection.LEFT, 0.04, 0.5 );
				this.look( edu.wustl.cse.lookingglass.apis.walkandtouch.LookDirection.RIGHT, 0.04, 0.5 );
			}

			this.look( edu.wustl.cse.lookingglass.apis.walkandtouch.LookDirection.FORWARD, 0.25 );
		}

	}

	public void Kiss( final edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel Who) {
		this.walkTo( Who, edu.wustl.cse.lookingglass.apis.walkandtouch.SpatialRelation.IN_FRONT_OF, -0.05 );
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					Kayla.this.touch( Who, org.alice.apis.moveandturn.MoveDirection.LEFT );
				}
			},
			new Runnable() {
				public void run() {
					Kayla.this.getPart(Kayla.Part.Head).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1) );
				}
			},
			new Runnable() {
				public void run() {
					Kayla.this.getPart(Kayla.Part.RightUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15) );
				}
			}
		);

		this.standUp( false );
	}
}
