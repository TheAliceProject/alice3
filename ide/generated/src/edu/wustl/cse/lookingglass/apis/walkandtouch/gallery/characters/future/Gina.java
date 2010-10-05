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

public class Gina extends edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.GalleryCharacter { 
	public Gina() {
		super( "future/Gina" );
}
	public enum Part {
		Wheel01( "legs", "footBase", "wheel01" ),
		Wheel02( "legs", "footBase", "wheel02" ),
		Wheel03( "legs", "footBase", "wheel03" ),
		Wheel04( "legs", "footBase", "wheel04" ),
		FootBase( "legs", "footBase" ),
		Legs( "legs" ),
		LeftPinkyFinger( "lowerTorso", "upperTorso", "leftShoulder", "leftUpperArm", "leftElbow", "leftLowerArm", "leftHand", "leftPinkyFinger" ),
		LeftMiddleFinger( "lowerTorso", "upperTorso", "leftShoulder", "leftUpperArm", "leftElbow", "leftLowerArm", "leftHand", "leftMiddleFinger" ),
		LeftIndexFinger( "lowerTorso", "upperTorso", "leftShoulder", "leftUpperArm", "leftElbow", "leftLowerArm", "leftHand", "leftIndexFinger" ),
		LeftThumb( "lowerTorso", "upperTorso", "leftShoulder", "leftUpperArm", "leftElbow", "leftLowerArm", "leftHand", "leftThumb" ),
		LeftHand( "lowerTorso", "upperTorso", "leftShoulder", "leftUpperArm", "leftElbow", "leftLowerArm", "leftHand" ),
		LeftLowerArm( "lowerTorso", "upperTorso", "leftShoulder", "leftUpperArm", "leftElbow", "leftLowerArm" ),
		LeftElbow( "lowerTorso", "upperTorso", "leftShoulder", "leftUpperArm", "leftElbow" ),
		LeftUpperArm( "lowerTorso", "upperTorso", "leftShoulder", "leftUpperArm" ),
		LeftShoulder( "lowerTorso", "upperTorso", "leftShoulder" ),
		LeftEye( "lowerTorso", "upperTorso", "head", "leftEye" ),
		LeftPigtail( "lowerTorso", "upperTorso", "head", "leftPigtailHolder", "leftPigtail" ),
		LeftPigtailHolder( "lowerTorso", "upperTorso", "head", "leftPigtailHolder" ),
		LeftEyebrow( "lowerTorso", "upperTorso", "head", "leftEyebrow" ),
		LeftJawPin( "lowerTorso", "upperTorso", "head", "leftJawPin" ),
		Jaw( "lowerTorso", "upperTorso", "head", "jaw" ),
		RightPigtail( "lowerTorso", "upperTorso", "head", "rightPigtailHolder", "rightPigtail" ),
		RightPigtailHolder( "lowerTorso", "upperTorso", "head", "rightPigtailHolder" ),
		RightJawPin( "lowerTorso", "upperTorso", "head", "rightJawPin" ),
		RightEye( "lowerTorso", "upperTorso", "head", "rightEye" ),
		RightEyebrow( "lowerTorso", "upperTorso", "head", "rightEyebrow" ),
		Head( "lowerTorso", "upperTorso", "head" ),
		RightPinkyFinger( "lowerTorso", "upperTorso", "rightShoulder", "rightUpperArm", "rightElbow", "rightLowerArm", "rightHand", "rightPinkyFinger" ),
		RightMiddleFinger( "lowerTorso", "upperTorso", "rightShoulder", "rightUpperArm", "rightElbow", "rightLowerArm", "rightHand", "rightMiddleFinger" ),
		RightIndexFinger( "lowerTorso", "upperTorso", "rightShoulder", "rightUpperArm", "rightElbow", "rightLowerArm", "rightHand", "rightIndexFinger" ),
		RightThumb( "lowerTorso", "upperTorso", "rightShoulder", "rightUpperArm", "rightElbow", "rightLowerArm", "rightHand", "rightThumb" ),
		RightHand( "lowerTorso", "upperTorso", "rightShoulder", "rightUpperArm", "rightElbow", "rightLowerArm", "rightHand" ),
		RightLowerArm( "lowerTorso", "upperTorso", "rightShoulder", "rightUpperArm", "rightElbow", "rightLowerArm" ),
		RightElbow( "lowerTorso", "upperTorso", "rightShoulder", "rightUpperArm", "rightElbow" ),
		RightUpperArm( "lowerTorso", "upperTorso", "rightShoulder", "rightUpperArm" ),
		RightShoulder( "lowerTorso", "upperTorso", "rightShoulder" ),
		UpperTorso( "lowerTorso", "upperTorso" ),
		LowerTorso( "lowerTorso" );
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

	public void RollForward( final Number Distance) {
		for (int index1 = 0; index1 < 1.0; index1 = index1 + 1) {
			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						Gina.this.move( org.alice.apis.moveandturn.MoveDirection.FORWARD, 1.0 );
					}
				},
				new Runnable() {
					public void run() {
						Gina.this.getPart(Gina.Part.Wheel04).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(1.0) );
					}
				},
				new Runnable() {
					public void run() {
						Gina.this.getPart(Gina.Part.Wheel03).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(1.0) );
					}
				},
				new Runnable() {
					public void run() {
						Gina.this.getPart(Gina.Part.Wheel01).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(1.0) );
					}
				},
				new Runnable() {
					public void run() {
						Gina.this.getPart(Gina.Part.Wheel02).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(1.0) );
					}
				},
				new Runnable() {
					public void run() {
						// DoInOrder { 
					org.alice.virtualmachine.DoTogether.invokeAndWait(
						new Runnable() {
							public void run() {
								Gina.this.roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.02), 0.5 );
							}
						},
						new Runnable() {
							public void run() {
								Gina.this.getPart(Gina.Part.Legs).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.02), 0.5 );
							}
						},
						new Runnable() {
							public void run() {
								Gina.this.getPart(Gina.Part.LowerTorso).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.02), 0.5 );
							}
						},
						new Runnable() {
							public void run() {
								Gina.this.getPart(Gina.Part.Head).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.04), 0.5 );
							}
						},
						new Runnable() {
							public void run() {
								Gina.this.getPart(Gina.Part.LeftShoulder).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
							}
						},
						new Runnable() {
							public void run() {
								Gina.this.getPart(Gina.Part.RightShoulder).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
							}
						}
					);

					org.alice.virtualmachine.DoTogether.invokeAndWait(
						new Runnable() {
							public void run() {
								Gina.this.roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.02), 0.5 );
							}
						},
						new Runnable() {
							public void run() {
								Gina.this.getPart(Gina.Part.Legs).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.02), 0.5 );
							}
						},
						new Runnable() {
							public void run() {
								Gina.this.getPart(Gina.Part.LowerTorso).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.02), 0.5 );
							}
						},
						new Runnable() {
							public void run() {
								Gina.this.getPart(Gina.Part.Head).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.04), 0.5 );
							}
						},
						new Runnable() {
							public void run() {
								Gina.this.getPart(Gina.Part.LeftShoulder).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
							}
						},
						new Runnable() {
							public void run() {
								Gina.this.getPart(Gina.Part.RightShoulder).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
							}
						}
					);

//				}
					}
				}
			);

		}

	}

	public void FlirtWith( final edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel Who) {
		this.turnToFace( Who );
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					Gina.this.roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.02), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					Gina.this.getPart(Gina.Part.Legs).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.02), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					Gina.this.getPart(Gina.Part.LowerTorso).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.02), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					Gina.this.getPart(Gina.Part.Head).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.04), 0.5 );
				}
			}
		);

		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					Gina.this.getPart(Gina.Part.LeftShoulder).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					Gina.this.getPart(Gina.Part.LeftUpperArm).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					Gina.this.getPart(Gina.Part.LeftElbow).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.35), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					Gina.this.getPart(Gina.Part.LeftLowerArm).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.5 );
				}
			}
		);

		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					Gina.this.getPart(Gina.Part.LeftShoulder).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					Gina.this.getPart(Gina.Part.LeftPigtail).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					Gina.this.getPart(Gina.Part.LeftPigtail).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.5 );
				}
			}
		);

		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					Gina.this.getPart(Gina.Part.LeftShoulder).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					Gina.this.getPart(Gina.Part.LeftPigtail).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					Gina.this.getPart(Gina.Part.LeftPigtail).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.5 );
				}
			}
		);

		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					Gina.this.getPart(Gina.Part.LeftShoulder).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					Gina.this.getPart(Gina.Part.LeftUpperArm).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					Gina.this.getPart(Gina.Part.LeftElbow).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.35), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					Gina.this.getPart(Gina.Part.LeftLowerArm).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					Gina.this.roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.02), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					Gina.this.getPart(Gina.Part.Legs).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.02), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					Gina.this.getPart(Gina.Part.LowerTorso).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.02), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					Gina.this.getPart(Gina.Part.Head).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.04), 0.5 );
				}
			}
		);

	}

	public void TalkOnThePhone( final Number Length) {
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					Gina.this.getPart(Gina.Part.RightElbow).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.2) );
				}
			},
			new Runnable() {
				public void run() {
					Gina.this.getPart(Gina.Part.RightElbow).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25) );
				}
			},
			new Runnable() {
				public void run() {
					Gina.this.getPart(Gina.Part.RightUpperArm).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.15) );
				}
			},
			new Runnable() {
				public void run() {
					Gina.this.getPart(Gina.Part.RightShoulder).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25) );
				}
			},
			new Runnable() {
				public void run() {
					Gina.this.getPart(Gina.Part.RightShoulder).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.15) );
				}
			},
			new Runnable() {
				public void run() {
					Gina.this.getPart(Gina.Part.RightHand).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.05) );
				}
			},
			new Runnable() {
				public void run() {
					Gina.this.getPart(Gina.Part.RightHand).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1) );
				}
			},
			new Runnable() {
				public void run() {
					Gina.this.getPart(Gina.Part.RightLowerArm).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15) );
				}
			}
		);

		for (int index1 = 0; index1 < 1.0; index1 = index1 + 1) {
			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						Gina.this.getPart(Gina.Part.Head).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.04), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Gina.this.getPart(Gina.Part.Jaw).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Gina.this.getPart(Gina.Part.LeftElbow).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
					}
				}
			);

			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						Gina.this.getPart(Gina.Part.Head).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.04), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Gina.this.getPart(Gina.Part.Jaw).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Gina.this.getPart(Gina.Part.LeftElbow).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
					}
				}
			);

		}

		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					Gina.this.getPart(Gina.Part.RightElbow).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.2) );
				}
			},
			new Runnable() {
				public void run() {
					Gina.this.getPart(Gina.Part.RightElbow).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25) );
				}
			},
			new Runnable() {
				public void run() {
					Gina.this.getPart(Gina.Part.RightUpperArm).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.15) );
				}
			},
			new Runnable() {
				public void run() {
					Gina.this.getPart(Gina.Part.RightShoulder).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25) );
				}
			},
			new Runnable() {
				public void run() {
					Gina.this.getPart(Gina.Part.RightShoulder).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.15) );
				}
			},
			new Runnable() {
				public void run() {
					Gina.this.getPart(Gina.Part.RightHand).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.05) );
				}
			},
			new Runnable() {
				public void run() {
					Gina.this.getPart(Gina.Part.RightHand).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1) );
				}
			},
			new Runnable() {
				public void run() {
					Gina.this.getPart(Gina.Part.RightLowerArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15) );
				}
			}
		);

	}

	public void Kiss( final edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel Who) {
		this.moveTo( Who, edu.wustl.cse.lookingglass.apis.walkandtouch.SpatialRelation.IN_FRONT_OF );
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					Gina.this.getPart(Gina.Part.LeftEyebrow).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.02) );
				}
			},
			new Runnable() {
				public void run() {
					Gina.this.getPart(Gina.Part.RightEyebrow).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.02) );
				}
			}
		);

		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					Gina.this.getPart(Gina.Part.LowerTorso).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05) );
				}
			},
			new Runnable() {
				public void run() {
					Gina.this.getPart(Gina.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05) );
				}
			}
		);

		this.getPart(Gina.Part.Head).setColor( new org.alice.apis.moveandturn.Color(1.0f, 0.6862745f, 0.6862745f) );
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					Gina.this.getPart(Gina.Part.LowerTorso).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05) );
				}
			},
			new Runnable() {
				public void run() {
					Gina.this.getPart(Gina.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05) );
				}
			},
			new Runnable() {
				public void run() {
					Gina.this.getPart(Gina.Part.LeftEyebrow).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.02) );
				}
			},
			new Runnable() {
				public void run() {
					Gina.this.getPart(Gina.Part.RightEyebrow).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.02) );
				}
			}
		);

		this.getPart(Gina.Part.Head).setColor( new org.alice.apis.moveandturn.Color(1.0f, 1.0f, 1.0f) );
	}

	public void Slap( final edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel Who) {
		this.moveTo( Who, edu.wustl.cse.lookingglass.apis.walkandtouch.SpatialRelation.IN_FRONT_OF );
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					Gina.this.getPart(Gina.Part.RightShoulder).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.3) );
				}
			},
			new Runnable() {
				public void run() {
					Gina.this.getPart(Gina.Part.RightUpperArm).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.05) );
				}
			},
			new Runnable() {
				public void run() {
					Gina.this.getPart(Gina.Part.RightHand).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.05) );
				}
			},
			new Runnable() {
				public void run() {
					Gina.this.getPart(Gina.Part.RightHand).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1) );
				}
			},
			new Runnable() {
				public void run() {
					Gina.this.getPart(Gina.Part.RightElbow).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25) );
				}
			}
		);

		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					Gina.this.getPart(Gina.Part.RightUpperArm).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					Gina.this.getPart(Gina.Part.RightElbow).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					Gina.this.getPart(Gina.Part.RightElbow).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.3), 0.25 );
				}
			}
		);

		this.delay(0.25);
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					Gina.this.getPart(Gina.Part.RightUpperArm).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					Gina.this.getPart(Gina.Part.RightElbow).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					Gina.this.getPart(Gina.Part.RightElbow).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.3), 0.5 );
				}
			}
		);

		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					Gina.this.getPart(Gina.Part.RightShoulder).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.3), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					Gina.this.getPart(Gina.Part.RightUpperArm).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					Gina.this.getPart(Gina.Part.RightHand).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					Gina.this.getPart(Gina.Part.RightHand).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					Gina.this.getPart(Gina.Part.RightElbow).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.5 );
				}
			}
		);

	}

	public void Dance( final Number Length) {
		for (int index1 = 0; index1 < 1.0; index1 = index1 + 1) {
			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						Gina.this.getPart(Gina.Part.LeftShoulder).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Gina.this.getPart(Gina.Part.LeftUpperArm).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Gina.this.getPart(Gina.Part.LeftElbow).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.35), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Gina.this.getPart(Gina.Part.LeftLowerArm).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Gina.this.roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.02), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Gina.this.getPart(Gina.Part.Legs).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.02), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Gina.this.getPart(Gina.Part.LowerTorso).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.02), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Gina.this.getPart(Gina.Part.Head).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.04), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Gina.this.getPart(Gina.Part.Wheel04).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Gina.this.getPart(Gina.Part.Wheel03).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Gina.this.getPart(Gina.Part.Wheel01).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Gina.this.getPart(Gina.Part.Wheel02).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.5 );
					}
				}
			);

			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						Gina.this.getPart(Gina.Part.LeftShoulder).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Gina.this.getPart(Gina.Part.LeftUpperArm).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Gina.this.getPart(Gina.Part.LeftElbow).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.35), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Gina.this.getPart(Gina.Part.LeftLowerArm).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Gina.this.roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.02), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Gina.this.getPart(Gina.Part.Legs).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.02), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Gina.this.getPart(Gina.Part.LowerTorso).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.02), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Gina.this.getPart(Gina.Part.Head).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.04), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Gina.this.getPart(Gina.Part.Wheel04).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Gina.this.getPart(Gina.Part.Wheel03).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Gina.this.getPart(Gina.Part.Wheel01).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Gina.this.getPart(Gina.Part.Wheel02).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.5 );
					}
				}
			);

			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						Gina.this.getPart(Gina.Part.RightShoulder).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Gina.this.getPart(Gina.Part.RightUpperArm).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Gina.this.getPart(Gina.Part.RightElbow).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.35), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Gina.this.getPart(Gina.Part.RightLowerArm).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Gina.this.roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.02), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Gina.this.getPart(Gina.Part.Legs).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.02), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Gina.this.getPart(Gina.Part.LowerTorso).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.02), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Gina.this.getPart(Gina.Part.Head).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.04), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Gina.this.getPart(Gina.Part.Wheel04).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Gina.this.getPart(Gina.Part.Wheel03).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Gina.this.getPart(Gina.Part.Wheel01).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Gina.this.getPart(Gina.Part.Wheel02).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.5 );
					}
				}
			);

			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						Gina.this.getPart(Gina.Part.RightShoulder).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Gina.this.getPart(Gina.Part.RightUpperArm).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Gina.this.getPart(Gina.Part.RightElbow).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.35), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Gina.this.getPart(Gina.Part.RightLowerArm).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Gina.this.roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.02), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Gina.this.getPart(Gina.Part.Legs).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.02), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Gina.this.getPart(Gina.Part.LowerTorso).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.02), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Gina.this.getPart(Gina.Part.Head).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.04), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Gina.this.getPart(Gina.Part.Wheel04).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Gina.this.getPart(Gina.Part.Wheel03).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Gina.this.getPart(Gina.Part.Wheel01).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Gina.this.getPart(Gina.Part.Wheel02).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.5 );
					}
				}
			);

		}

	}
}
