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

public class ProfessorRoboto extends edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.GalleryCharacter { 
	public ProfessorRoboto() {
		super( "adults/ProfessorRoboto" );
}
	public enum Part {
		LeftFoot( "legs", "leftFoot" ),
		RightFoot( "legs", "rightFoot" ),
		Legs( "legs" ),
		Collar( "chest", "collar" ),
		LeftPinkyFinger( "chest", "leftUpperArm", "leftLowerArm", "leftCuff", "leftHand", "leftPinkyFinger" ),
		LeftMiddleFinger( "chest", "leftUpperArm", "leftLowerArm", "leftCuff", "leftHand", "leftMiddleFinger" ),
		LeftIndexFinger( "chest", "leftUpperArm", "leftLowerArm", "leftCuff", "leftHand", "leftIndexFinger" ),
		LeftThumb( "chest", "leftUpperArm", "leftLowerArm", "leftCuff", "leftHand", "leftThumb" ),
		LeftHand( "chest", "leftUpperArm", "leftLowerArm", "leftCuff", "leftHand" ),
		LeftCuff( "chest", "leftUpperArm", "leftLowerArm", "leftCuff" ),
		LeftLowerArm( "chest", "leftUpperArm", "leftLowerArm" ),
		LeftUpperArm( "chest", "leftUpperArm" ),
		Jaw( "chest", "neck", "head", "jaw" ),
		Goggles( "chest", "neck", "head", "goggles" ),
		LeftHair( "chest", "neck", "head", "leftHair" ),
		RightHair( "chest", "neck", "head", "rightHair" ),
		Head( "chest", "neck", "head" ),
		Neck( "chest", "neck" ),
		RightPinkyFinger( "chest", "rightUpperArm", "rightLowerArm", "rightCuff", "rightHand", "rightPinkyFinger" ),
		RightMiddleFinger( "chest", "rightUpperArm", "rightLowerArm", "rightCuff", "rightHand", "rightMiddleFinger" ),
		RightIndexFinger( "chest", "rightUpperArm", "rightLowerArm", "rightCuff", "rightHand", "rightIndexFinger" ),
		RightThumb( "chest", "rightUpperArm", "rightLowerArm", "rightCuff", "rightHand", "rightThumb" ),
		RightHand( "chest", "rightUpperArm", "rightLowerArm", "rightCuff", "rightHand" ),
		RightCuff( "chest", "rightUpperArm", "rightLowerArm", "rightCuff" ),
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

	public void Walk( final Number Distance) {
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					ProfessorRoboto.this.getPart(ProfessorRoboto.Part.LeftUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					ProfessorRoboto.this.getPart(ProfessorRoboto.Part.RightUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.25 );
				}
			}
		);

		for (int index1 = 0; index1 < Distance.intValue(); index1 = index1 + 1) {
			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						ProfessorRoboto.this.move( org.alice.apis.moveandturn.MoveDirection.FORWARD, 1.0 );
					}
				},
				new Runnable() {
					public void run() {
						// DoInOrder { 
					org.alice.virtualmachine.DoTogether.invokeAndWait(
						new Runnable() {
							public void run() {
								ProfessorRoboto.this.getPart(ProfessorRoboto.Part.LeftUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
							}
						},
						new Runnable() {
							public void run() {
								ProfessorRoboto.this.getPart(ProfessorRoboto.Part.RightUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
							}
						},
						new Runnable() {
							public void run() {
								// DoInOrder { 
							ProfessorRoboto.this.getPart(ProfessorRoboto.Part.RightFoot).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.25 );
							ProfessorRoboto.this.getPart(ProfessorRoboto.Part.RightFoot).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.25 );
//						}
							}
						}
					);

					org.alice.virtualmachine.DoTogether.invokeAndWait(
						new Runnable() {
							public void run() {
								ProfessorRoboto.this.getPart(ProfessorRoboto.Part.LeftUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
							}
						},
						new Runnable() {
							public void run() {
								ProfessorRoboto.this.getPart(ProfessorRoboto.Part.RightUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
							}
						},
						new Runnable() {
							public void run() {
								// DoInOrder { 
							ProfessorRoboto.this.getPart(ProfessorRoboto.Part.LeftFoot).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.25 );
							ProfessorRoboto.this.getPart(ProfessorRoboto.Part.LeftFoot).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.25 );
//						}
							}
						}
					);

//				}
					}
				}
			);

		}

		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					ProfessorRoboto.this.getPart(ProfessorRoboto.Part.LeftUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					ProfessorRoboto.this.getPart(ProfessorRoboto.Part.RightUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.25 );
				}
			}
		);

	}

	public void ReactToExplosion( ) {
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					ProfessorRoboto.this.getPart(ProfessorRoboto.Part.Head).setColor( new org.alice.apis.moveandturn.Color(0.0f, 0.0f, 0.0f), 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					ProfessorRoboto.this.getPart(ProfessorRoboto.Part.Goggles).setColor( new org.alice.apis.moveandturn.Color(1.0f, 1.0f, 1.0f), 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					ProfessorRoboto.this.getPart(ProfessorRoboto.Part.LeftHair).setColor( new org.alice.apis.moveandturn.Color(1.0f, 1.0f, 1.0f), 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					ProfessorRoboto.this.getPart(ProfessorRoboto.Part.RightHair).setColor( new org.alice.apis.moveandturn.Color(1.0f, 1.0f, 1.0f), 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					ProfessorRoboto.this.getPart(ProfessorRoboto.Part.LeftHair).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					ProfessorRoboto.this.getPart(ProfessorRoboto.Part.RightHair).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					ProfessorRoboto.this.getPart(ProfessorRoboto.Part.LeftUpperArm).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					ProfessorRoboto.this.getPart(ProfessorRoboto.Part.RightUpperArm).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					ProfessorRoboto.this.getPart(ProfessorRoboto.Part.LeftLowerArm).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					ProfessorRoboto.this.getPart(ProfessorRoboto.Part.LeftLowerArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					ProfessorRoboto.this.getPart(ProfessorRoboto.Part.RightLowerArm).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					ProfessorRoboto.this.getPart(ProfessorRoboto.Part.RightLowerArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.25 );
				}
			}
		);

		this.delay(0.5);
		this.getPart(ProfessorRoboto.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.1 );
		this.getPart(ProfessorRoboto.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.2 );
		this.getPart(ProfessorRoboto.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.1 );
		this.standUp( false );
	}

	public void Dance( final Number Length) {
		for (int index1 = 0; index1 < Length.intValue(); index1 = index1 + 1) {
			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						ProfessorRoboto.this.getPart(ProfessorRoboto.Part.Chest).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						ProfessorRoboto.this.getPart(ProfessorRoboto.Part.LeftUpperArm).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						ProfessorRoboto.this.getPart(ProfessorRoboto.Part.RightUpperArm).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.5 );
					}
				}
			);

			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						ProfessorRoboto.this.getPart(ProfessorRoboto.Part.Chest).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1) );
					}
				},
				new Runnable() {
					public void run() {
						// DoInOrder { 
					org.alice.virtualmachine.DoTogether.invokeAndWait(
						new Runnable() {
							public void run() {
								ProfessorRoboto.this.getPart(ProfessorRoboto.Part.LeftUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
							}
						},
						new Runnable() {
							public void run() {
								ProfessorRoboto.this.getPart(ProfessorRoboto.Part.RightUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
							}
						},
						new Runnable() {
							public void run() {
								ProfessorRoboto.this.getPart(ProfessorRoboto.Part.LeftHair).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.5 );
							}
						},
						new Runnable() {
							public void run() {
								ProfessorRoboto.this.getPart(ProfessorRoboto.Part.RightHair).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.5 );
							}
						}
					);

					org.alice.virtualmachine.DoTogether.invokeAndWait(
						new Runnable() {
							public void run() {
								ProfessorRoboto.this.getPart(ProfessorRoboto.Part.LeftUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
							}
						},
						new Runnable() {
							public void run() {
								ProfessorRoboto.this.getPart(ProfessorRoboto.Part.RightUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
							}
						},
						new Runnable() {
							public void run() {
								ProfessorRoboto.this.getPart(ProfessorRoboto.Part.LeftHair).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.5 );
							}
						},
						new Runnable() {
							public void run() {
								ProfessorRoboto.this.getPart(ProfessorRoboto.Part.RightHair).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.5 );
							}
						}
					);

//				}
					}
				}
			);

			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						ProfessorRoboto.this.getPart(ProfessorRoboto.Part.Chest).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						ProfessorRoboto.this.getPart(ProfessorRoboto.Part.LeftUpperArm).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						ProfessorRoboto.this.getPart(ProfessorRoboto.Part.RightUpperArm).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.5 );
					}
				}
			);

		}

	}

	public void TripAndFall( ) {
		this.Walk( 1.0 );
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					ProfessorRoboto.this.turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					ProfessorRoboto.this.move( org.alice.apis.moveandturn.MoveDirection.UP, 0.3, 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					ProfessorRoboto.this.getPart(ProfessorRoboto.Part.LeftFoot).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					ProfessorRoboto.this.getPart(ProfessorRoboto.Part.RightFoot).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					ProfessorRoboto.this.getPart(ProfessorRoboto.Part.LeftUpperArm).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					ProfessorRoboto.this.getPart(ProfessorRoboto.Part.LeftLowerArm).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					ProfessorRoboto.this.getPart(ProfessorRoboto.Part.LeftLowerArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					ProfessorRoboto.this.getPart(ProfessorRoboto.Part.RightUpperArm).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					ProfessorRoboto.this.getPart(ProfessorRoboto.Part.RightLowerArm).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					ProfessorRoboto.this.getPart(ProfessorRoboto.Part.RightLowerArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.25 );
				}
			}
		);

		this.delay(0.25);
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					ProfessorRoboto.this.turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15) );
				}
			},
			new Runnable() {
				public void run() {
					ProfessorRoboto.this.getPart(ProfessorRoboto.Part.Chest).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.2) );
				}
			},
			new Runnable() {
				public void run() {
					ProfessorRoboto.this.getPart(ProfessorRoboto.Part.LeftUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.2) );
				}
			},
			new Runnable() {
				public void run() {
					ProfessorRoboto.this.getPart(ProfessorRoboto.Part.RightUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.2) );
				}
			},
			new Runnable() {
				public void run() {
					ProfessorRoboto.this.getPart(ProfessorRoboto.Part.LeftHand).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25) );
				}
			},
			new Runnable() {
				public void run() {
					ProfessorRoboto.this.getPart(ProfessorRoboto.Part.RightHand).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25) );
				}
			}
		);

		this.standUp(  );
	}

	public void BeStressed( final Number Length) {
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					ProfessorRoboto.this.getPart(ProfessorRoboto.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05) );
				}
			},
			new Runnable() {
				public void run() {
					ProfessorRoboto.this.getPart(ProfessorRoboto.Part.LeftHair).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1) );
				}
			},
			new Runnable() {
				public void run() {
					ProfessorRoboto.this.getPart(ProfessorRoboto.Part.RightHair).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1) );
				}
			}
		);

		for (int index1 = 0; index1 < Length.intValue(); index1 = index1 + 1) {
			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						ProfessorRoboto.this.turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1) );
					}
				},
				new Runnable() {
					public void run() {
						ProfessorRoboto.this.move( org.alice.apis.moveandturn.MoveDirection.FORWARD, 3.0 );
					}
				},
				new Runnable() {
					public void run() {
						// DoInOrder { 
					org.alice.virtualmachine.DoTogether.invokeAndWait(
						new Runnable() {
							public void run() {
								ProfessorRoboto.this.getPart(ProfessorRoboto.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.25 );
							}
						},
						new Runnable() {
							public void run() {
								ProfessorRoboto.this.getPart(ProfessorRoboto.Part.LeftUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.25 );
							}
						},
						new Runnable() {
							public void run() {
								ProfessorRoboto.this.getPart(ProfessorRoboto.Part.RightUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.25 );
							}
						},
						new Runnable() {
							public void run() {
								// DoInOrder { 
							ProfessorRoboto.this.getPart(ProfessorRoboto.Part.RightFoot).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.12 );
							ProfessorRoboto.this.getPart(ProfessorRoboto.Part.RightFoot).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.12 );
//						}
							}
						}
					);

					org.alice.virtualmachine.DoTogether.invokeAndWait(
						new Runnable() {
							public void run() {
								ProfessorRoboto.this.getPart(ProfessorRoboto.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.25 );
							}
						},
						new Runnable() {
							public void run() {
								// DoInOrder { 
							ProfessorRoboto.this.getPart(ProfessorRoboto.Part.LeftFoot).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.12 );
							ProfessorRoboto.this.getPart(ProfessorRoboto.Part.LeftFoot).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.12 );
//						}
							}
						},
						new Runnable() {
							public void run() {
								ProfessorRoboto.this.getPart(ProfessorRoboto.Part.LeftUpperArm).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.25 );
							}
						},
						new Runnable() {
							public void run() {
								ProfessorRoboto.this.getPart(ProfessorRoboto.Part.RightUpperArm).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.25 );
							}
						}
					);

					org.alice.virtualmachine.DoTogether.invokeAndWait(
						new Runnable() {
							public void run() {
								ProfessorRoboto.this.getPart(ProfessorRoboto.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.25 );
							}
						},
						new Runnable() {
							public void run() {
								// DoInOrder { 
							ProfessorRoboto.this.getPart(ProfessorRoboto.Part.RightFoot).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.12 );
							ProfessorRoboto.this.getPart(ProfessorRoboto.Part.RightFoot).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.12 );
//						}
							}
						},
						new Runnable() {
							public void run() {
								ProfessorRoboto.this.getPart(ProfessorRoboto.Part.LeftUpperArm).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.25 );
							}
						},
						new Runnable() {
							public void run() {
								ProfessorRoboto.this.getPart(ProfessorRoboto.Part.RightUpperArm).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.25 );
							}
						}
					);

					org.alice.virtualmachine.DoTogether.invokeAndWait(
						new Runnable() {
							public void run() {
								ProfessorRoboto.this.getPart(ProfessorRoboto.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.25 );
							}
						},
						new Runnable() {
							public void run() {
								ProfessorRoboto.this.getPart(ProfessorRoboto.Part.LeftUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.25 );
							}
						},
						new Runnable() {
							public void run() {
								ProfessorRoboto.this.getPart(ProfessorRoboto.Part.RightUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.25 );
							}
						},
						new Runnable() {
							public void run() {
								// DoInOrder { 
							ProfessorRoboto.this.getPart(ProfessorRoboto.Part.LeftFoot).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.12 );
							ProfessorRoboto.this.getPart(ProfessorRoboto.Part.LeftFoot).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.12 );
//						}
							}
						}
					);

//				}
					}
				}
			);

		}

		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					ProfessorRoboto.this.getPart(ProfessorRoboto.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05) );
				}
			},
			new Runnable() {
				public void run() {
					ProfessorRoboto.this.getPart(ProfessorRoboto.Part.LeftHair).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1) );
				}
			},
			new Runnable() {
				public void run() {
					ProfessorRoboto.this.getPart(ProfessorRoboto.Part.RightHair).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1) );
				}
			}
		);

	}
}
