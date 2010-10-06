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
package edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.characters.future;

public class Biff extends edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.GalleryCharacter { 
	public Biff() {
		super( "future/Biff" );
}
	public enum Part {
		Jaw( "waist", "torso", "head", "jaw" ),
		LeftEye( "waist", "torso", "head", "leftEye" ),
		LeftEyebrow( "waist", "torso", "head", "leftEyebrow" ),
		LeftJawPin( "waist", "torso", "head", "leftJawPin" ),
		RightJawPin( "waist", "torso", "head", "rightJawPin" ),
		RightEyebrow( "waist", "torso", "head", "rightEyebrow" ),
		RightEye( "waist", "torso", "head", "rightEye" ),
		Head( "waist", "torso", "head" ),
		LeftIndexFinger( "waist", "torso", "leftShoulder", "leftUpperArm", "leftElbow", "leftLowerArm", "leftHand", "leftIndexFinger" ),
		LeftPinkyFinger( "waist", "torso", "leftShoulder", "leftUpperArm", "leftElbow", "leftLowerArm", "leftHand", "leftPinkyFinger" ),
		LeftMIddleFinger( "waist", "torso", "leftShoulder", "leftUpperArm", "leftElbow", "leftLowerArm", "leftHand", "leftMIddleFinger" ),
		LeftThumb( "waist", "torso", "leftShoulder", "leftUpperArm", "leftElbow", "leftLowerArm", "leftHand", "leftThumb" ),
		LeftHand( "waist", "torso", "leftShoulder", "leftUpperArm", "leftElbow", "leftLowerArm", "leftHand" ),
		LeftLowerArm( "waist", "torso", "leftShoulder", "leftUpperArm", "leftElbow", "leftLowerArm" ),
		LeftElbow( "waist", "torso", "leftShoulder", "leftUpperArm", "leftElbow" ),
		LeftUpperArm( "waist", "torso", "leftShoulder", "leftUpperArm" ),
		LeftShoulder( "waist", "torso", "leftShoulder" ),
		RightPinkyFinger( "waist", "torso", "rightShoulder", "rightUpperArm", "rightElbow", "rightLowerArm", "rightHand", "rightPinkyFinger" ),
		RightMiddleFinger( "waist", "torso", "rightShoulder", "rightUpperArm", "rightElbow", "rightLowerArm", "rightHand", "rightMiddleFinger" ),
		RightIndexFinger( "waist", "torso", "rightShoulder", "rightUpperArm", "rightElbow", "rightLowerArm", "rightHand", "rightIndexFinger" ),
		RightThumb( "waist", "torso", "rightShoulder", "rightUpperArm", "rightElbow", "rightLowerArm", "rightHand", "rightThumb" ),
		RightHand( "waist", "torso", "rightShoulder", "rightUpperArm", "rightElbow", "rightLowerArm", "rightHand" ),
		RightLowerArm( "waist", "torso", "rightShoulder", "rightUpperArm", "rightElbow", "rightLowerArm" ),
		RightElbow( "waist", "torso", "rightShoulder", "rightUpperArm", "rightElbow" ),
		RightUpperArm( "waist", "torso", "rightShoulder", "rightUpperArm" ),
		RightShoulder( "waist", "torso", "rightShoulder" ),
		Torso( "waist", "torso" ),
		Waist( "waist" ),
		FootWheel( "leftLegStrut", "footWheel" ),
		LeftLegStrut( "leftLegStrut" ),
		RightLegStrut( "rightLegStrut" );
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

	public void Flex( ) {
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					Biff.this.getPart(Biff.Part.Torso).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1) );
				}
			},
			new Runnable() {
				public void run() {
					Biff.this.getPart(Biff.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1) );
				}
			},
			new Runnable() {
				public void run() {
					Biff.this.getPart(Biff.Part.LeftShoulder).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15) );
				}
			},
			new Runnable() {
				public void run() {
					Biff.this.getPart(Biff.Part.RightShoulder).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15) );
				}
			},
			new Runnable() {
				public void run() {
					Biff.this.getPart(Biff.Part.LeftShoulder).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.05) );
				}
			},
			new Runnable() {
				public void run() {
					Biff.this.getPart(Biff.Part.RightShoulder).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.05) );
				}
			},
			new Runnable() {
				public void run() {
					Biff.this.getPart(Biff.Part.LeftLowerArm).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25) );
				}
			},
			new Runnable() {
				public void run() {
					Biff.this.getPart(Biff.Part.RightLowerArm).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25) );
				}
			},
			new Runnable() {
				public void run() {
					org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						Biff.this.getPart(Biff.Part.RightPinkyFinger).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25) );
					}
				},
				new Runnable() {
					public void run() {
						Biff.this.getPart(Biff.Part.RightIndexFinger).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25) );
					}
				},
				new Runnable() {
					public void run() {
						Biff.this.getPart(Biff.Part.RightMiddleFinger).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25) );
					}
				}
			);
				}
			},
			new Runnable() {
				public void run() {
					org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						Biff.this.getPart(Biff.Part.LeftIndexFinger).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25) );
					}
				},
				new Runnable() {
					public void run() {
						Biff.this.getPart(Biff.Part.LeftMIddleFinger).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25) );
					}
				},
				new Runnable() {
					public void run() {
						Biff.this.getPart(Biff.Part.LeftPinkyFinger).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25) );
					}
				},
				new Runnable() {
					public void run() {
						Biff.this.getPart(Biff.Part.LeftThumb).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25) );
					}
				}
			);
				}
			}
		);

		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					for (int index2 = 0; index2 < 2; index2 = index2 + 1) {
				org.alice.virtualmachine.DoTogether.invokeAndWait(
					new Runnable() {
						public void run() {
							Biff.this.getPart(Biff.Part.LeftLowerArm).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.5 );
						}
					},
					new Runnable() {
						public void run() {
							Biff.this.getPart(Biff.Part.RightLowerArm).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.5 );
						}
					}
				);

				org.alice.virtualmachine.DoTogether.invokeAndWait(
					new Runnable() {
						public void run() {
							Biff.this.getPart(Biff.Part.LeftLowerArm).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.5 );
						}
					},
					new Runnable() {
						public void run() {
							Biff.this.getPart(Biff.Part.RightLowerArm).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.5 );
						}
					}
				);

			}
				}
			},
			new Runnable() {
				public void run() {
					// DoInOrder { 
				Biff.this.getPart(Biff.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.01), 0.1 );
				for (int index3 = 0; index3 < 8; index3 = index3 + 1) {
					Biff.this.getPart(Biff.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.02), 0.1 );
					Biff.this.getPart(Biff.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.02), 0.1 );
				}

				Biff.this.getPart(Biff.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.01), 0.1 );
//			}
				}
			}
		);

		this.standUp( false );
	}

	public void MoonWalk( ) {
		this.standUp( false );
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					Biff.this.move( org.alice.apis.moveandturn.MoveDirection.BACKWARD, 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					Biff.this.getPart(Biff.Part.FootWheel).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(2.0) );
				}
			},
			new Runnable() {
				public void run() {
					Biff.this.getPart(Biff.Part.LeftShoulder).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15) );
				}
			},
			new Runnable() {
				public void run() {
					Biff.this.getPart(Biff.Part.RightShoulder).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15) );
				}
			},
			new Runnable() {
				public void run() {
					Biff.this.getPart(Biff.Part.RightHand).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25) );
				}
			},
			new Runnable() {
				public void run() {
					Biff.this.getPart(Biff.Part.LeftHand).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25) );
				}
			},
			new Runnable() {
				public void run() {
					// DoInOrder { 
				Biff.this.getPart(Biff.Part.Head).move( org.alice.apis.moveandturn.MoveDirection.LEFT, 0.05, 0.5 );
				Biff.this.getPart(Biff.Part.Head).move( org.alice.apis.moveandturn.MoveDirection.RIGHT, 0.1, 0.5 );
//			}
				}
			}
		);

		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					Biff.this.move( org.alice.apis.moveandturn.MoveDirection.BACKWARD, 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					Biff.this.getPart(Biff.Part.FootWheel).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(2.0) );
				}
			},
			new Runnable() {
				public void run() {
					Biff.this.getPart(Biff.Part.LeftShoulder).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15) );
				}
			},
			new Runnable() {
				public void run() {
					Biff.this.getPart(Biff.Part.LeftLowerArm).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.15) );
				}
			},
			new Runnable() {
				public void run() {
					Biff.this.getPart(Biff.Part.RightShoulder).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15) );
				}
			},
			new Runnable() {
				public void run() {
					Biff.this.getPart(Biff.Part.RightLowerArm).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.15) );
				}
			},
			new Runnable() {
				public void run() {
					// DoInOrder { 
				Biff.this.getPart(Biff.Part.Head).move( org.alice.apis.moveandturn.MoveDirection.LEFT, 0.1, 0.5 );
				Biff.this.getPart(Biff.Part.Head).move( org.alice.apis.moveandturn.MoveDirection.RIGHT, 0.1, 0.5 );
//			}
				}
			}
		);

		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					Biff.this.move( org.alice.apis.moveandturn.MoveDirection.BACKWARD, 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					Biff.this.getPart(Biff.Part.FootWheel).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(2.0) );
				}
			},
			new Runnable() {
				public void run() {
					Biff.this.getPart(Biff.Part.LeftShoulder).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15) );
				}
			},
			new Runnable() {
				public void run() {
					Biff.this.getPart(Biff.Part.LeftLowerArm).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.15) );
				}
			},
			new Runnable() {
				public void run() {
					Biff.this.getPart(Biff.Part.RightShoulder).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15) );
				}
			},
			new Runnable() {
				public void run() {
					Biff.this.getPart(Biff.Part.RightLowerArm).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.15) );
				}
			},
			new Runnable() {
				public void run() {
					// DoInOrder { 
				Biff.this.getPart(Biff.Part.Head).move( org.alice.apis.moveandturn.MoveDirection.LEFT, 0.1, 0.5 );
				Biff.this.getPart(Biff.Part.Head).move( org.alice.apis.moveandturn.MoveDirection.RIGHT, 0.1, 0.5 );
//			}
				}
			}
		);

		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					Biff.this.move( org.alice.apis.moveandturn.MoveDirection.BACKWARD, 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					Biff.this.getPart(Biff.Part.FootWheel).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(2.0) );
				}
			},
			new Runnable() {
				public void run() {
					Biff.this.getPart(Biff.Part.LeftShoulder).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15) );
				}
			},
			new Runnable() {
				public void run() {
					Biff.this.getPart(Biff.Part.LeftLowerArm).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.15) );
				}
			},
			new Runnable() {
				public void run() {
					Biff.this.getPart(Biff.Part.RightShoulder).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15) );
				}
			},
			new Runnable() {
				public void run() {
					Biff.this.getPart(Biff.Part.RightLowerArm).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.15) );
				}
			},
			new Runnable() {
				public void run() {
					Biff.this.getPart(Biff.Part.RightHand).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25) );
				}
			},
			new Runnable() {
				public void run() {
					Biff.this.getPart(Biff.Part.LeftHand).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25) );
				}
			},
			new Runnable() {
				public void run() {
					// DoInOrder { 
				Biff.this.getPart(Biff.Part.Head).move( org.alice.apis.moveandturn.MoveDirection.LEFT, 0.05, 0.5 );
				Biff.this.getPart(Biff.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05) );
//			}
				}
			}
		);

		this.turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(2.0) );
		this.getPart(Biff.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.5 );
		this.standUp( false );
	}

	public void Kiss( final edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel Who) {
		this.moveTo( Who, edu.wustl.cse.lookingglass.apis.walkandtouch.SpatialRelation.IN_FRONT_OF );
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					Biff.this.getPart(Biff.Part.RightShoulder).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25) );
				}
			},
			new Runnable() {
				public void run() {
					Biff.this.getPart(Biff.Part.RightShoulder).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25) );
				}
			},
			new Runnable() {
				public void run() {
					Biff.this.getPart(Biff.Part.RightHand).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25) );
				}
			},
			new Runnable() {
				public void run() {
					Biff.this.getPart(Biff.Part.RightLowerArm).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25) );
				}
			},
			new Runnable() {
				public void run() {
					Biff.this.getPart(Biff.Part.RightLowerArm).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.15) );
				}
			},
			new Runnable() {
				public void run() {
					Biff.this.getPart(Biff.Part.LeftEyebrow).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.05) );
				}
			},
			new Runnable() {
				public void run() {
					Biff.this.getPart(Biff.Part.RightEyebrow).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.05) );
				}
			}
		);

		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					Biff.this.getPart(Biff.Part.RightLowerArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25) );
				}
			},
			new Runnable() {
				public void run() {
					Biff.this.getPart(Biff.Part.RightLowerArm).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25) );
				}
			}
		);

		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					Biff.this.getPart(Biff.Part.Head).move( org.alice.apis.moveandturn.MoveDirection.FORWARD, 0.1 );
				}
			},
			new Runnable() {
				public void run() {
					// DoInOrder { 
				Biff.this.getPart(Biff.Part.Head).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.05) );
				Biff.this.getPart(Biff.Part.Head).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1) );
				Biff.this.getPart(Biff.Part.Head).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1) );
//			}
				}
			}
		);

		this.standUp( false );
	}

	public void HaveACrushOn( final edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel Who) {
		this.turnToFace( Who );
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					Biff.this.getPart(Biff.Part.Torso).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1) );
				}
			},
			new Runnable() {
				public void run() {
					Biff.this.getPart(Biff.Part.LeftShoulder).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1) );
				}
			},
			new Runnable() {
				public void run() {
					Biff.this.getPart(Biff.Part.RightShoulder).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1) );
				}
			},
			new Runnable() {
				public void run() {
					Biff.this.getPart(Biff.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1) );
				}
			},
			new Runnable() {
				public void run() {
					Biff.this.getPart(Biff.Part.Jaw).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1) );
				}
			},
			new Runnable() {
				public void run() {
					// DoInOrder { 
				org.alice.virtualmachine.DoTogether.invokeAndWait(
					new Runnable() {
						public void run() {
							Biff.this.getPart(Biff.Part.RightEye).setColor( new org.alice.apis.moveandturn.Color(1.0f, 0.0f, 1.0f), 0.5 );
						}
					},
					new Runnable() {
						public void run() {
							Biff.this.getPart(Biff.Part.LeftEye).setColor( new org.alice.apis.moveandturn.Color(1.0f, 0.0f, 1.0f), 0.5 );
						}
					}
				);

				org.alice.virtualmachine.DoTogether.invokeAndWait(
					new Runnable() {
						public void run() {
							Biff.this.getPart(Biff.Part.RightEye).setColor( new org.alice.apis.moveandturn.Color(1.0f, 1.0f, 1.0f), 0.25 );
						}
					},
					new Runnable() {
						public void run() {
							Biff.this.getPart(Biff.Part.LeftEye).setColor( new org.alice.apis.moveandturn.Color(1.0f, 1.0f, 1.0f), 0.25 );
						}
					}
				);

				org.alice.virtualmachine.DoTogether.invokeAndWait(
					new Runnable() {
						public void run() {
							Biff.this.getPart(Biff.Part.RightEye).setColor( new org.alice.apis.moveandturn.Color(1.0f, 0.0f, 1.0f), 0.5 );
						}
					},
					new Runnable() {
						public void run() {
							Biff.this.getPart(Biff.Part.LeftEye).setColor( new org.alice.apis.moveandturn.Color(1.0f, 0.0f, 1.0f), 0.5 );
						}
					}
				);

				org.alice.virtualmachine.DoTogether.invokeAndWait(
					new Runnable() {
						public void run() {
							Biff.this.getPart(Biff.Part.RightEye).setColor( new org.alice.apis.moveandturn.Color(1.0f, 1.0f, 1.0f), 0.25 );
						}
					},
					new Runnable() {
						public void run() {
							Biff.this.getPart(Biff.Part.LeftEye).setColor( new org.alice.apis.moveandturn.Color(1.0f, 1.0f, 1.0f), 0.25 );
						}
					}
				);

				org.alice.virtualmachine.DoTogether.invokeAndWait(
					new Runnable() {
						public void run() {
							Biff.this.getPart(Biff.Part.RightEye).setColor( new org.alice.apis.moveandturn.Color(1.0f, 0.0f, 1.0f), 0.5 );
						}
					},
					new Runnable() {
						public void run() {
							Biff.this.getPart(Biff.Part.LeftEye).setColor( new org.alice.apis.moveandturn.Color(1.0f, 0.0f, 1.0f), 0.5 );
						}
					}
				);

				org.alice.virtualmachine.DoTogether.invokeAndWait(
					new Runnable() {
						public void run() {
							Biff.this.getPart(Biff.Part.RightEye).setColor( new org.alice.apis.moveandturn.Color(1.0f, 1.0f, 1.0f), 0.25 );
						}
					},
					new Runnable() {
						public void run() {
							Biff.this.getPart(Biff.Part.LeftEye).setColor( new org.alice.apis.moveandturn.Color(1.0f, 1.0f, 1.0f), 0.25 );
						}
					}
				);

				org.alice.virtualmachine.DoTogether.invokeAndWait(
					new Runnable() {
						public void run() {
							Biff.this.getPart(Biff.Part.RightEye).setColor( new org.alice.apis.moveandturn.Color(1.0f, 0.0f, 1.0f), 0.5 );
						}
					},
					new Runnable() {
						public void run() {
							Biff.this.getPart(Biff.Part.LeftEye).setColor( new org.alice.apis.moveandturn.Color(1.0f, 0.0f, 1.0f), 0.5 );
						}
					}
				);

				org.alice.virtualmachine.DoTogether.invokeAndWait(
					new Runnable() {
						public void run() {
							Biff.this.getPart(Biff.Part.RightEye).setColor( new org.alice.apis.moveandturn.Color(1.0f, 1.0f, 1.0f), 0.25 );
						}
					},
					new Runnable() {
						public void run() {
							Biff.this.getPart(Biff.Part.LeftEye).setColor( new org.alice.apis.moveandturn.Color(1.0f, 1.0f, 1.0f), 0.25 );
						}
					}
				);

//			}
				}
			}
		);

	}
}
