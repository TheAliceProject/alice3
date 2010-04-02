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

public class NativeAmerican extends edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.GalleryPerson { 
	public NativeAmerican() {
		super( "heroic/NativeAmerican" );
}
	public enum Part {
		LeftHand( "chest", "leftUpperArm", "leftLowerArm", "leftHand" ),
		LeftLowerArm( "chest", "leftUpperArm", "leftLowerArm" ),
		LeftUpperArm( "chest", "leftUpperArm" ),
		RightHand( "chest", "rightUpperArm", "rightLowerArm", "rightHand" ),
		RightLowerArm( "chest", "rightUpperArm", "rightLowerArm" ),
		RightUpperArm( "chest", "rightUpperArm" ),
		Feather7( "chest", "neck", "head", "headband", "Feather7" ),
		Feather6( "chest", "neck", "head", "headband", "Feather6" ),
		Feather5( "chest", "neck", "head", "headband", "Feather5" ),
		Feather4( "chest", "neck", "head", "headband", "Feather4" ),
		Feather3( "chest", "neck", "head", "headband", "Feather3" ),
		Feather2( "chest", "neck", "head", "headband", "Feather2" ),
		Feather1( "chest", "neck", "head", "headband", "Feather1" ),
		Headband( "chest", "neck", "head", "headband" ),
		Head( "chest", "neck", "head" ),
		Neck( "chest", "neck" ),
		Chest( "chest" ),
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

	public void LeapOver( final edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel Who) {
		this.walkTo( Who, edu.wustl.cse.lookingglass.apis.walkandtouch.SpatialRelation.IN_FRONT_OF, 2.0 );
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					// DoInOrder { 
				NativeAmerican.this.move( org.alice.apis.moveandturn.MoveDirection.UP, (Who.getHeight() + 0.25) );
				NativeAmerican.this.move( org.alice.apis.moveandturn.MoveDirection.DOWN, (Who.getHeight() + 0.25) );
//			}
				}
			},
			new Runnable() {
				public void run() {
					NativeAmerican.this.move( org.alice.apis.moveandturn.MoveDirection.FORWARD, 4.0, 2.0 );
				}
			},
			new Runnable() {
				public void run() {
					// DoInOrder { 
				org.alice.virtualmachine.DoTogether.invokeAndWait(
					new Runnable() {
						public void run() {
							NativeAmerican.this.getPart(NativeAmerican.Part.RightUpperLeg).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.5 );
						}
					},
					new Runnable() {
						public void run() {
							NativeAmerican.this.getPart(NativeAmerican.Part.RightLowerLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.5 );
						}
					},
					new Runnable() {
						public void run() {
							NativeAmerican.this.getPart(NativeAmerican.Part.LeftUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.35), 0.5 );
						}
					},
					new Runnable() {
						public void run() {
							NativeAmerican.this.getPart(NativeAmerican.Part.LeftLowerLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.5 );
						}
					}
				);

				NativeAmerican.this.delay(1.0);
				org.alice.virtualmachine.DoTogether.invokeAndWait(
					new Runnable() {
						public void run() {
							NativeAmerican.this.getPart(NativeAmerican.Part.RightUpperLeg).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.5 );
						}
					},
					new Runnable() {
						public void run() {
							NativeAmerican.this.getPart(NativeAmerican.Part.RightLowerLeg).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.5 );
						}
					},
					new Runnable() {
						public void run() {
							NativeAmerican.this.getPart(NativeAmerican.Part.LeftUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.35), 0.5 );
						}
					},
					new Runnable() {
						public void run() {
							NativeAmerican.this.getPart(NativeAmerican.Part.LeftLowerLeg).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.5 );
						}
					}
				);

//			}
				}
			}
		);

	}

	public void LookCross( ) {
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					NativeAmerican.this.look( edu.wustl.cse.lookingglass.apis.walkandtouch.LookDirection.DOWN, 0.05 );
				}
			},
			new Runnable() {
				public void run() {
					NativeAmerican.this.touch( NativeAmerican.this.getPart(NativeAmerican.Part.LeftUpperArm) );
				}
			},
			new Runnable() {
				public void run() {
					NativeAmerican.this.touch( NativeAmerican.this.getPart(NativeAmerican.Part.RightUpperArm), org.alice.apis.moveandturn.MoveDirection.FORWARD, edu.wustl.cse.lookingglass.apis.walkandtouch.TouchPart.LEFT_HAND );
				}
			},
			new Runnable() {
				public void run() {
					NativeAmerican.this.getPart(NativeAmerican.Part.LeftUpperArm).move( org.alice.apis.moveandturn.MoveDirection.UP, 0.05 );
				}
			},
			new Runnable() {
				public void run() {
					NativeAmerican.this.getPart(NativeAmerican.Part.RightUpperArm).move( org.alice.apis.moveandturn.MoveDirection.UP, 0.05 );
				}
			}
		);

	}

	public void HideBehind( final edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel What) {
		this.walkTo( What, edu.wustl.cse.lookingglass.apis.walkandtouch.SpatialRelation.BEHIND, 0.0, 1.5, false, edu.wustl.cse.lookingglass.apis.walkandtouch.Amount.NORMAL, edu.wustl.cse.lookingglass.apis.walkandtouch.Amount.TINY );
		this.kneel(  );
		this.delay(1.0);
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					NativeAmerican.this.getPart(NativeAmerican.Part.Chest).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.05) );
				}
			},
			new Runnable() {
				public void run() {
					NativeAmerican.this.getPart(NativeAmerican.Part.Head).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.15) );
				}
			}
		);

		this.delay(0.5);
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					NativeAmerican.this.getPart(NativeAmerican.Part.Chest).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.05) );
				}
			},
			new Runnable() {
				public void run() {
					NativeAmerican.this.getPart(NativeAmerican.Part.Head).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.15) );
				}
			}
		);

	}

	public void DoToeTouches( final Number HowMany) {
		this.sitOn( null, edu.wustl.cse.lookingglass.apis.walkandtouch.SitDirection.FORWARD );
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					NativeAmerican.this.touch( NativeAmerican.this.getPart(NativeAmerican.Part.RightFoot) );
				}
			},
			new Runnable() {
				public void run() {
					NativeAmerican.this.touch( NativeAmerican.this.getPart(NativeAmerican.Part.LeftFoot), org.alice.apis.moveandturn.MoveDirection.FORWARD, edu.wustl.cse.lookingglass.apis.walkandtouch.TouchPart.LEFT_HAND );
				}
			}
		);

		for (int index1 = 0; index1 < HowMany.intValue(); index1 = index1 + 1) {
			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						NativeAmerican.this.getPart(NativeAmerican.Part.Chest).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05) );
					}
				},
				new Runnable() {
					public void run() {
						NativeAmerican.this.look( edu.wustl.cse.lookingglass.apis.walkandtouch.LookDirection.DOWN );
					}
				}
			);

			this.delay(1.0);
			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						NativeAmerican.this.look( edu.wustl.cse.lookingglass.apis.walkandtouch.LookDirection.FORWARD );
					}
				},
				new Runnable() {
					public void run() {
						NativeAmerican.this.getPart(NativeAmerican.Part.Chest).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05) );
					}
				}
			);

			this.delay(1.0);
		}

		this.standUp(  );
	}
}
