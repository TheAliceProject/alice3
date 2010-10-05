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

public class Philip extends edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.GalleryPerson { 
	private edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Boolean> OnBoard= new edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Boolean>( false );  
	public Philip() {
		super( "kids/Philip" );
	}
	public enum Part {
		LeftEar( "hips", "lowerTorso", "upperTorso", "neck", "head", "leftEar" ),
		RightEar( "hips", "lowerTorso", "upperTorso", "neck", "head", "rightEar" ),
		Glasses( "hips", "lowerTorso", "upperTorso", "neck", "head", "glasses" ),
		Head( "hips", "lowerTorso", "upperTorso", "neck", "head" ),
		Neck( "hips", "lowerTorso", "upperTorso", "neck" ),
		RightHand( "hips", "lowerTorso", "upperTorso", "rightUpperArm", "rightLowerArm", "rightHand" ),
		RightLowerArm( "hips", "lowerTorso", "upperTorso", "rightUpperArm", "rightLowerArm" ),
		RightUpperArm( "hips", "lowerTorso", "upperTorso", "rightUpperArm" ),
		LeftHand( "hips", "lowerTorso", "upperTorso", "leftUpperArm", "leftLowerArm", "leftHand" ),
		LeftLowerArm( "hips", "lowerTorso", "upperTorso", "leftUpperArm", "leftLowerArm" ),
		LeftUpperArm( "hips", "lowerTorso", "upperTorso", "leftUpperArm" ),
		UpperTorso( "hips", "lowerTorso", "upperTorso" ),
		LowerTorso( "hips", "lowerTorso" ),
		RightFoot( "hips", "rightUpperLeg", "rightLowerLeg", "rightFoot" ),
		RightLowerLeg( "hips", "rightUpperLeg", "rightLowerLeg" ),
		RightUpperLeg( "hips", "rightUpperLeg" ),
		LeftFoot( "hips", "leftUpperLeg", "leftLowerLeg", "leftFoot" ),
		LeftLowerLeg( "hips", "leftUpperLeg", "leftLowerLeg" ),
		LeftUpperLeg( "hips", "leftUpperLeg" ),
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

	public void Cheer( ) {
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					Philip.this.getPart(Philip.Part.RightUpperArm).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					Philip.this.getPart(Philip.Part.LeftUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					Philip.this.getPart(Philip.Part.LeftLowerArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					Philip.this.getPart(Philip.Part.LeftUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					Philip.this.getPart(Philip.Part.LeftLowerLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					Philip.this.roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.03), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					Philip.this.getPart(Philip.Part.LeftUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					Philip.this.getPart(Philip.Part.LeftUpperLeg).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.5 );
				}
			}
		);

		for (int index1 = 0; index1 < 5; index1 = index1 + 1) {
			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						Philip.this.getPart(Philip.Part.RightUpperArm).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.35 );
					}
				},
				new Runnable() {
					public void run() {
						Philip.this.getPart(Philip.Part.RightLowerArm).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.35 );
					}
				}
			);

			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						Philip.this.getPart(Philip.Part.RightUpperArm).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.35 );
					}
				},
				new Runnable() {
					public void run() {
						Philip.this.getPart(Philip.Part.RightLowerArm).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.35 );
					}
				}
			);

		}

		this.standUp( false );
	}

	public void Punch( final edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel Who) {
		this.walkTo( Who, edu.wustl.cse.lookingglass.apis.walkandtouch.SpatialRelation.IN_FRONT_OF, 0.05 );
		this.touch( Who );
		this.delay(0.1);
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					Philip.this.touch( Who, org.alice.apis.moveandturn.MoveDirection.FORWARD, edu.wustl.cse.lookingglass.apis.walkandtouch.TouchPart.RIGHT_HAND, -0.25, 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					// DoInOrder { 
				Philip.this.delay(0.1);
				Who.move( org.alice.apis.moveandturn.MoveDirection.BACKWARD, 0.5, 0.5 );
//			}
				}
			}
		);

		this.move( org.alice.apis.moveandturn.MoveDirection.BACKWARD, 0.1 );
		this.standUp( false );
	}

	public void TieShoelace( ) {
		this.kneel(  );
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					Philip.this.getPart(Philip.Part.LowerTorso).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15) );
				}
			},
			new Runnable() {
				public void run() {
					Philip.this.getPart(Philip.Part.UpperTorso).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1) );
				}
			}
		);

		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					Philip.this.touch( Philip.this.getPart(Philip.Part.LeftFoot) );
				}
			},
			new Runnable() {
				public void run() {
					Philip.this.touch( Philip.this.getPart(Philip.Part.LeftFoot), org.alice.apis.moveandturn.MoveDirection.FORWARD, edu.wustl.cse.lookingglass.apis.walkandtouch.TouchPart.LEFT_HAND );
				}
			}
		);

		for (int index1 = 0; index1 < 2; index1 = index1 + 1) {
			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						Philip.this.getPart(Philip.Part.RightUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Philip.this.getPart(Philip.Part.LeftUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.5 );
					}
				}
			);

			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						Philip.this.getPart(Philip.Part.RightUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Philip.this.getPart(Philip.Part.LeftUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.5 );
					}
				}
			);

		}

		this.standUp(  );
	}

	public void PlugEars( ) {
		this.look( edu.wustl.cse.lookingglass.apis.walkandtouch.LookDirection.DOWN );
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					Philip.this.touch( Philip.this.getPart(Philip.Part.RightEar), org.alice.apis.moveandturn.MoveDirection.FORWARD, edu.wustl.cse.lookingglass.apis.walkandtouch.TouchPart.RIGHT_HAND, 0.05 );
				}
			},
			new Runnable() {
				public void run() {
					Philip.this.touch( Philip.this.getPart(Philip.Part.LeftEar), org.alice.apis.moveandturn.MoveDirection.FORWARD, edu.wustl.cse.lookingglass.apis.walkandtouch.TouchPart.LEFT_HAND, 0.05 );
				}
			}
		);

		this.delay(1.0);
		this.standUp( false );
	}
}
