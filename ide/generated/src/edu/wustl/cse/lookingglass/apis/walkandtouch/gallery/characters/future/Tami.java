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

public class Tami extends edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.GalleryCharacter { 
	public Tami() {
		super( "future/Tami" );
}
	public enum Part {
		FootWheel( "legs", "footWheel" ),
		Legs( "legs" ),
		Hips( "hips" ),
		LeftPinkyFinger( "lowerTorso", "upperTorso", "leftShoulder", "leftUpperArm", "leftElbow", "leftLowerArm", "leftHand", "leftPinkyFinger" ),
		LeftMiddleFinger( "lowerTorso", "upperTorso", "leftShoulder", "leftUpperArm", "leftElbow", "leftLowerArm", "leftHand", "leftMiddleFinger" ),
		LeftIndexFinger( "lowerTorso", "upperTorso", "leftShoulder", "leftUpperArm", "leftElbow", "leftLowerArm", "leftHand", "leftIndexFinger" ),
		LeftThumb( "lowerTorso", "upperTorso", "leftShoulder", "leftUpperArm", "leftElbow", "leftLowerArm", "leftHand", "leftThumb" ),
		LeftHand( "lowerTorso", "upperTorso", "leftShoulder", "leftUpperArm", "leftElbow", "leftLowerArm", "leftHand" ),
		LeftLowerArm( "lowerTorso", "upperTorso", "leftShoulder", "leftUpperArm", "leftElbow", "leftLowerArm" ),
		LeftElbow( "lowerTorso", "upperTorso", "leftShoulder", "leftUpperArm", "leftElbow" ),
		LeftUpperArm( "lowerTorso", "upperTorso", "leftShoulder", "leftUpperArm" ),
		LeftShoulder( "lowerTorso", "upperTorso", "leftShoulder" ),
		RightPinkyFinger( "lowerTorso", "upperTorso", "rightShoulder", "rightUpperArm", "rightElbow", "rightLowerArm", "rightHand", "rightPinkyFinger" ),
		RightMiddleFinger( "lowerTorso", "upperTorso", "rightShoulder", "rightUpperArm", "rightElbow", "rightLowerArm", "rightHand", "rightMiddleFinger" ),
		RightIndexFinger( "lowerTorso", "upperTorso", "rightShoulder", "rightUpperArm", "rightElbow", "rightLowerArm", "rightHand", "rightIndexFinger" ),
		RightThumb( "lowerTorso", "upperTorso", "rightShoulder", "rightUpperArm", "rightElbow", "rightLowerArm", "rightHand", "rightThumb" ),
		RightHand( "lowerTorso", "upperTorso", "rightShoulder", "rightUpperArm", "rightElbow", "rightLowerArm", "rightHand" ),
		RightLowerArm( "lowerTorso", "upperTorso", "rightShoulder", "rightUpperArm", "rightElbow", "rightLowerArm" ),
		RightElbow( "lowerTorso", "upperTorso", "rightShoulder", "rightUpperArm", "rightElbow" ),
		RightUpperArm( "lowerTorso", "upperTorso", "rightShoulder", "rightUpperArm" ),
		RightShoulder( "lowerTorso", "upperTorso", "rightShoulder" ),
		LipRing( "lowerTorso", "upperTorso", "head", "jaw", "lipRing" ),
		Jaw( "lowerTorso", "upperTorso", "head", "jaw" ),
		RightEarring( "lowerTorso", "upperTorso", "head", "rightEarring" ),
		LeftEarring( "lowerTorso", "upperTorso", "head", "leftEarring" ),
		LeftEye( "lowerTorso", "upperTorso", "head", "leftEye" ),
		RightEye( "lowerTorso", "upperTorso", "head", "rightEye" ),
		RightEyebrow( "lowerTorso", "upperTorso", "head", "rightEyebrow" ),
		EyebrowRing( "lowerTorso", "upperTorso", "head", "leftEyebrow", "eyebrowRing" ),
		LeftEyebrow( "lowerTorso", "upperTorso", "head", "leftEyebrow" ),
		Hair01( "lowerTorso", "upperTorso", "head", "hair01" ),
		Hair02( "lowerTorso", "upperTorso", "head", "hair02" ),
		Hair03( "lowerTorso", "upperTorso", "head", "hair03" ),
		Hair04( "lowerTorso", "upperTorso", "head", "hair04" ),
		Hair05( "lowerTorso", "upperTorso", "head", "hair05" ),
		Hair06( "lowerTorso", "upperTorso", "head", "hair06" ),
		Hair07( "lowerTorso", "upperTorso", "head", "hair07" ),
		Hair08( "lowerTorso", "upperTorso", "head", "hair08" ),
		Hair09( "lowerTorso", "upperTorso", "head", "hair09" ),
		Hair10( "lowerTorso", "upperTorso", "head", "hair10" ),
		Head( "lowerTorso", "upperTorso", "head" ),
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
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					Tami.this.getPart(Tami.Part.LeftShoulder).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					Tami.this.getPart(Tami.Part.RightShoulder).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					Tami.this.getPart(Tami.Part.LowerTorso).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.01), 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					Tami.this.getPart(Tami.Part.UpperTorso).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.01), 0.25 );
				}
			}
		);

		for (int index1 = 0; index1 < Distance.intValue(); index1 = index1 + 1) {
			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						Tami.this.move( org.alice.apis.moveandturn.MoveDirection.FORWARD, 1.0 );
					}
				},
				new Runnable() {
					public void run() {
						Tami.this.getPart(Tami.Part.FootWheel).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(1.0) );
					}
				},
				new Runnable() {
					public void run() {
						// DoInOrder { 
					org.alice.virtualmachine.DoTogether.invokeAndWait(
						new Runnable() {
							public void run() {
								Tami.this.getPart(Tami.Part.LeftShoulder).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
							}
						},
						new Runnable() {
							public void run() {
								Tami.this.getPart(Tami.Part.RightShoulder).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
							}
						},
						new Runnable() {
							public void run() {
								Tami.this.getPart(Tami.Part.Hips).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.02), 0.5 );
							}
						}
					);

					org.alice.virtualmachine.DoTogether.invokeAndWait(
						new Runnable() {
							public void run() {
								Tami.this.getPart(Tami.Part.LeftShoulder).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
							}
						},
						new Runnable() {
							public void run() {
								Tami.this.getPart(Tami.Part.RightShoulder).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
							}
						},
						new Runnable() {
							public void run() {
								Tami.this.getPart(Tami.Part.Hips).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.02), 0.5 );
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
					Tami.this.getPart(Tami.Part.LeftShoulder).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					Tami.this.getPart(Tami.Part.RightShoulder).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					Tami.this.getPart(Tami.Part.LowerTorso).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.01), 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					Tami.this.getPart(Tami.Part.UpperTorso).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.01), 0.25 );
				}
			}
		);

	}

	public void Kiss( final edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel Who) {
		this.moveTo( Who, edu.wustl.cse.lookingglass.apis.walkandtouch.SpatialRelation.IN_FRONT_OF );
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					Tami.this.getPart(Tami.Part.LeftShoulder).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					Tami.this.getPart(Tami.Part.LeftUpperArm).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					Tami.this.getPart(Tami.Part.RightShoulder).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					Tami.this.getPart(Tami.Part.RightUpperArm).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					Tami.this.getPart(Tami.Part.LowerTorso).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.01), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					Tami.this.getPart(Tami.Part.UpperTorso).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.01), 0.5 );
				}
			}
		);

		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					Tami.this.getPart(Tami.Part.LeftShoulder).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					Tami.this.getPart(Tami.Part.LeftUpperArm).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					Tami.this.getPart(Tami.Part.RightShoulder).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					Tami.this.getPart(Tami.Part.RightUpperArm).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					Tami.this.getPart(Tami.Part.LowerTorso).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					Tami.this.getPart(Tami.Part.UpperTorso).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					Tami.this.getPart(Tami.Part.Jaw).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.02), 0.5 );
				}
			}
		);

		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					Tami.this.getPart(Tami.Part.Head).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					Tami.this.getPart(Tami.Part.LeftShoulder).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.5 );
				}
			}
		);

		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					Tami.this.getPart(Tami.Part.Head).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1) );
				}
			},
			new Runnable() {
				public void run() {
					Tami.this.getPart(Tami.Part.LeftShoulder).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05) );
				}
			},
			new Runnable() {
				public void run() {
					Tami.this.getPart(Tami.Part.RightShoulder).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05) );
				}
			}
		);

		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					Tami.this.getPart(Tami.Part.Head).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					Tami.this.getPart(Tami.Part.RightShoulder).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.5 );
				}
			}
		);

		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					Tami.this.getPart(Tami.Part.Jaw).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.02), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					Tami.this.getPart(Tami.Part.LeftShoulder).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					Tami.this.getPart(Tami.Part.RightShoulder).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					Tami.this.getPart(Tami.Part.LowerTorso).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					Tami.this.getPart(Tami.Part.LowerTorso).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.01), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					Tami.this.getPart(Tami.Part.UpperTorso).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					Tami.this.getPart(Tami.Part.UpperTorso).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.01), 0.5 );
				}
			}
		);

	}

	public void Dance( final Number Length) {
		for (int index1 = 0; index1 < Length.intValue(); index1 = index1 + 1) {
			this.turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.11 );
			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						Tami.this.move( org.alice.apis.moveandturn.MoveDirection.FORWARD, 0.5, 0.4 );
					}
				},
				new Runnable() {
					public void run() {
						Tami.this.getPart(Tami.Part.FootWheel).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.5), 0.4 );
					}
				},
				new Runnable() {
					public void run() {
						Tami.this.getPart(Tami.Part.RightShoulder).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.4 );
					}
				},
				new Runnable() {
					public void run() {
						Tami.this.getPart(Tami.Part.RightElbow).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.4 );
					}
				},
				new Runnable() {
					public void run() {
						Tami.this.getPart(Tami.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.4 );
					}
				}
			);

			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						Tami.this.move( org.alice.apis.moveandturn.MoveDirection.BACKWARD, 0.5, 0.4 );
					}
				},
				new Runnable() {
					public void run() {
						Tami.this.getPart(Tami.Part.FootWheel).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.5), 0.4 );
					}
				},
				new Runnable() {
					public void run() {
						Tami.this.getPart(Tami.Part.RightShoulder).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.4 );
					}
				},
				new Runnable() {
					public void run() {
						Tami.this.getPart(Tami.Part.RightElbow).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.4 );
					}
				},
				new Runnable() {
					public void run() {
						Tami.this.getPart(Tami.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.4 );
					}
				}
			);

			this.turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.22 );
			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						Tami.this.move( org.alice.apis.moveandturn.MoveDirection.FORWARD, 0.5, 0.4 );
					}
				},
				new Runnable() {
					public void run() {
						Tami.this.getPart(Tami.Part.FootWheel).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.5), 0.4 );
					}
				},
				new Runnable() {
					public void run() {
						Tami.this.getPart(Tami.Part.LeftShoulder).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.4 );
					}
				},
				new Runnable() {
					public void run() {
						Tami.this.getPart(Tami.Part.LeftElbow).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.4 );
					}
				},
				new Runnable() {
					public void run() {
						Tami.this.getPart(Tami.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.4 );
					}
				}
			);

			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						Tami.this.move( org.alice.apis.moveandturn.MoveDirection.BACKWARD, 0.5, 0.4 );
					}
				},
				new Runnable() {
					public void run() {
						Tami.this.getPart(Tami.Part.FootWheel).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.5), 0.4 );
					}
				},
				new Runnable() {
					public void run() {
						Tami.this.getPart(Tami.Part.LeftShoulder).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.4 );
					}
				},
				new Runnable() {
					public void run() {
						Tami.this.getPart(Tami.Part.LeftElbow).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.4 );
					}
				},
				new Runnable() {
					public void run() {
						Tami.this.getPart(Tami.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.4 );
					}
				}
			);

			this.turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.11 );
		}

	}
}
