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

public class HaroldTWireton extends edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.GalleryCharacter { 
	public HaroldTWireton() {
		super( "future/HaroldTWireton" );
}
	public enum Part {
		FrontRoller( "leg", "belt", "frontRoller" ),
		RearRoller( "leg", "belt", "rearRoller" ),
		Belt( "leg", "belt" ),
		Leg( "leg" ),
		Jaw( "lowerTorso", "upperTorso", "neck", "head", "jaw" ),
		LeftJawPin( "lowerTorso", "upperTorso", "neck", "head", "leftJawPin" ),
		Glasses( "lowerTorso", "upperTorso", "neck", "head", "glasses" ),
		LeftEyebrow( "lowerTorso", "upperTorso", "neck", "head", "leftEyebrow" ),
		Hair01( "lowerTorso", "upperTorso", "neck", "head", "hair01" ),
		Hair02( "lowerTorso", "upperTorso", "neck", "head", "hair02" ),
		Hair03( "lowerTorso", "upperTorso", "neck", "head", "hair03" ),
		RightJawPin( "lowerTorso", "upperTorso", "neck", "head", "rightJawPin" ),
		RightEyebrow( "lowerTorso", "upperTorso", "neck", "head", "rightEyebrow" ),
		Head( "lowerTorso", "upperTorso", "neck", "head" ),
		Neck( "lowerTorso", "upperTorso", "neck" ),
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

	public void TripAndFall( ) {
		this.move( org.alice.apis.moveandturn.MoveDirection.FORWARD, 1.0 );
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					HaroldTWireton.this.getPart(HaroldTWireton.Part.LeftUpperArm).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.07), 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					HaroldTWireton.this.getPart(HaroldTWireton.Part.RightUpperArm).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.07), 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					HaroldTWireton.this.getPart(HaroldTWireton.Part.LeftUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.07), 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					HaroldTWireton.this.getPart(HaroldTWireton.Part.RightUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.07), 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					HaroldTWireton.this.getPart(HaroldTWireton.Part.UpperTorso).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.07) );
				}
			},
			new Runnable() {
				public void run() {
					HaroldTWireton.this.getPart(HaroldTWireton.Part.LeftShoulder).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(3.0) );
				}
			},
			new Runnable() {
				public void run() {
					HaroldTWireton.this.getPart(HaroldTWireton.Part.RightShoulder).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(3.0) );
				}
			}
		);

		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					HaroldTWireton.this.getPart(HaroldTWireton.Part.UpperTorso).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.14) );
				}
			},
			new Runnable() {
				public void run() {
					HaroldTWireton.this.getPart(HaroldTWireton.Part.LeftShoulder).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(3.0) );
				}
			},
			new Runnable() {
				public void run() {
					HaroldTWireton.this.getPart(HaroldTWireton.Part.RightShoulder).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(3.0) );
				}
			}
		);

		this.getPart(HaroldTWireton.Part.Head).pointAt( this.getCamera(), 0.5 );
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					HaroldTWireton.this.getPart(HaroldTWireton.Part.Head).pointAt( HaroldTWireton.this.getCamera(), 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					HaroldTWireton.this.getPart(HaroldTWireton.Part.UpperTorso).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.07), 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					HaroldTWireton.this.turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					HaroldTWireton.this.move( org.alice.apis.moveandturn.MoveDirection.BACKWARD, 0.25, 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					HaroldTWireton.this.getPart(HaroldTWireton.Part.Belt).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.07), 0.25 );
				}
			}
		);

		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					HaroldTWireton.this.getPart(HaroldTWireton.Part.LeftShoulder).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.12), 0.1 );
				}
			},
			new Runnable() {
				public void run() {
					HaroldTWireton.this.getPart(HaroldTWireton.Part.RightShoulder).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.12), 0.1 );
				}
			}
		);

	}

	public void GoCrazy( ) {
		for (int index7 = 0; index7 < 5; index7 = index7 + 1) {
			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						HaroldTWireton.this.getPart(HaroldTWireton.Part.UpperTorso).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(4.0) );
					}
				},
				new Runnable() {
					public void run() {
						HaroldTWireton.this.getPart(HaroldTWireton.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(4.0) );
					}
				},
				new Runnable() {
					public void run() {
						// DoInOrder { 
					HaroldTWireton.this.getPart(HaroldTWireton.Part.Head).move( org.alice.apis.moveandturn.MoveDirection.UP, 0.05, 0.25 );
					HaroldTWireton.this.getPart(HaroldTWireton.Part.Head).move( org.alice.apis.moveandturn.MoveDirection.DOWN, 0.05, 0.25 );
//				}
					}
				},
				new Runnable() {
					public void run() {
						for (int index8 = 0; index8 < 2; index8 = index8 + 1) {
					// DoInOrder { 
						HaroldTWireton.this.getPart(HaroldTWireton.Part.Jaw).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.25 );
						HaroldTWireton.this.getPart(HaroldTWireton.Part.Jaw).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.25 );
//					}

				}
					}
				},
				new Runnable() {
					public void run() {
						HaroldTWireton.this.getPart(HaroldTWireton.Part.RightShoulder).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(4.0) );
					}
				},
				new Runnable() {
					public void run() {
						HaroldTWireton.this.getPart(HaroldTWireton.Part.LeftShoulder).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(4.0) );
					}
				},
				new Runnable() {
					public void run() {
						for (int index9 = 0; index9 < 2; index9 = index9 + 1) {
					// DoInOrder { 
						org.alice.virtualmachine.DoTogether.invokeAndWait(
							new Runnable() {
								public void run() {
									HaroldTWireton.this.getPart(HaroldTWireton.Part.Hair01).setColor( new org.alice.apis.moveandturn.Color(0.0f, 0.0f, 1.0f), 0.25 );
								}
							},
							new Runnable() {
								public void run() {
									HaroldTWireton.this.getPart(HaroldTWireton.Part.Hair02).setColor( new org.alice.apis.moveandturn.Color(0.0f, 0.0f, 1.0f), 0.25 );
								}
							},
							new Runnable() {
								public void run() {
									HaroldTWireton.this.getPart(HaroldTWireton.Part.Hair03).setColor( new org.alice.apis.moveandturn.Color(0.0f, 0.0f, 1.0f), 0.25 );
								}
							}
						);

						org.alice.virtualmachine.DoTogether.invokeAndWait(
							new Runnable() {
								public void run() {
									HaroldTWireton.this.getPart(HaroldTWireton.Part.Hair01).setColor( new org.alice.apis.moveandturn.Color(1.0f, 1.0f, 1.0f), 0.25 );
								}
							},
							new Runnable() {
								public void run() {
									HaroldTWireton.this.getPart(HaroldTWireton.Part.Hair02).setColor( new org.alice.apis.moveandturn.Color(1.0f, 1.0f, 1.0f), 0.25 );
								}
							},
							new Runnable() {
								public void run() {
									HaroldTWireton.this.getPart(HaroldTWireton.Part.Hair03).setColor( new org.alice.apis.moveandturn.Color(1.0f, 1.0f, 1.0f), 0.25 );
								}
							}
						);

//					}

				}
					}
				},
				new Runnable() {
					public void run() {
						HaroldTWireton.this.move( org.alice.apis.moveandturn.MoveDirection.FORWARD, 1.0, 0.25 );
					}
				}
			);

			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						HaroldTWireton.this.getPart(HaroldTWireton.Part.UpperTorso).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(4.0) );
					}
				},
				new Runnable() {
					public void run() {
						HaroldTWireton.this.getPart(HaroldTWireton.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(4.0) );
					}
				},
				new Runnable() {
					public void run() {
						// DoInOrder { 
					HaroldTWireton.this.getPart(HaroldTWireton.Part.Head).move( org.alice.apis.moveandturn.MoveDirection.UP, 0.05, 0.25 );
					HaroldTWireton.this.getPart(HaroldTWireton.Part.Head).move( org.alice.apis.moveandturn.MoveDirection.DOWN, 0.05, 0.25 );
//				}
					}
				},
				new Runnable() {
					public void run() {
						for (int index10 = 0; index10 < 2; index10 = index10 + 1) {
					// DoInOrder { 
						HaroldTWireton.this.getPart(HaroldTWireton.Part.Jaw).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.25 );
						HaroldTWireton.this.getPart(HaroldTWireton.Part.Jaw).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.25 );
//					}

				}
					}
				},
				new Runnable() {
					public void run() {
						HaroldTWireton.this.getPart(HaroldTWireton.Part.RightShoulder).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(4.0) );
					}
				},
				new Runnable() {
					public void run() {
						HaroldTWireton.this.getPart(HaroldTWireton.Part.LeftShoulder).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(4.0) );
					}
				},
				new Runnable() {
					public void run() {
						for (int index11 = 0; index11 < 2; index11 = index11 + 1) {
					// DoInOrder { 
						org.alice.virtualmachine.DoTogether.invokeAndWait(
							new Runnable() {
								public void run() {
									HaroldTWireton.this.getPart(HaroldTWireton.Part.Hair01).setColor( new org.alice.apis.moveandturn.Color(0.0f, 0.0f, 1.0f), 0.25 );
								}
							},
							new Runnable() {
								public void run() {
									HaroldTWireton.this.getPart(HaroldTWireton.Part.Hair02).setColor( new org.alice.apis.moveandturn.Color(0.0f, 0.0f, 1.0f), 0.25 );
								}
							},
							new Runnable() {
								public void run() {
									HaroldTWireton.this.getPart(HaroldTWireton.Part.Hair03).setColor( new org.alice.apis.moveandturn.Color(0.0f, 0.0f, 1.0f), 0.25 );
								}
							}
						);

						org.alice.virtualmachine.DoTogether.invokeAndWait(
							new Runnable() {
								public void run() {
									HaroldTWireton.this.getPart(HaroldTWireton.Part.Hair01).setColor( new org.alice.apis.moveandturn.Color(1.0f, 1.0f, 1.0f), 0.25 );
								}
							},
							new Runnable() {
								public void run() {
									HaroldTWireton.this.getPart(HaroldTWireton.Part.Hair02).setColor( new org.alice.apis.moveandturn.Color(1.0f, 1.0f, 1.0f), 0.25 );
								}
							},
							new Runnable() {
								public void run() {
									HaroldTWireton.this.getPart(HaroldTWireton.Part.Hair03).setColor( new org.alice.apis.moveandturn.Color(1.0f, 1.0f, 1.0f), 0.25 );
								}
							}
						);

//					}

				}
					}
				},
				new Runnable() {
					public void run() {
						HaroldTWireton.this.move( org.alice.apis.moveandturn.MoveDirection.BACKWARD, 1.0, 0.25 );
					}
				},
				new Runnable() {
					public void run() {
						HaroldTWireton.this.turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.5), 0.25 );
					}
				}
			);

			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						HaroldTWireton.this.getPart(HaroldTWireton.Part.UpperTorso).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(4.0) );
					}
				},
				new Runnable() {
					public void run() {
						HaroldTWireton.this.getPart(HaroldTWireton.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(4.0) );
					}
				},
				new Runnable() {
					public void run() {
						// DoInOrder { 
					HaroldTWireton.this.getPart(HaroldTWireton.Part.Head).move( org.alice.apis.moveandturn.MoveDirection.UP, 0.05, 0.25 );
					HaroldTWireton.this.getPart(HaroldTWireton.Part.Head).move( org.alice.apis.moveandturn.MoveDirection.DOWN, 0.05, 0.25 );
//				}
					}
				},
				new Runnable() {
					public void run() {
						for (int index12 = 0; index12 < 2; index12 = index12 + 1) {
					// DoInOrder { 
						HaroldTWireton.this.getPart(HaroldTWireton.Part.Jaw).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.25 );
						HaroldTWireton.this.getPart(HaroldTWireton.Part.Jaw).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.25 );
//					}

				}
					}
				},
				new Runnable() {
					public void run() {
						HaroldTWireton.this.getPart(HaroldTWireton.Part.RightShoulder).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(4.0) );
					}
				},
				new Runnable() {
					public void run() {
						HaroldTWireton.this.getPart(HaroldTWireton.Part.LeftShoulder).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(4.0) );
					}
				},
				new Runnable() {
					public void run() {
						for (int index13 = 0; index13 < 2; index13 = index13 + 1) {
					// DoInOrder { 
						org.alice.virtualmachine.DoTogether.invokeAndWait(
							new Runnable() {
								public void run() {
									HaroldTWireton.this.getPart(HaroldTWireton.Part.Hair01).setColor( new org.alice.apis.moveandturn.Color(0.0f, 0.0f, 1.0f), 0.25 );
								}
							},
							new Runnable() {
								public void run() {
									HaroldTWireton.this.getPart(HaroldTWireton.Part.Hair02).setColor( new org.alice.apis.moveandturn.Color(0.0f, 0.0f, 1.0f), 0.25 );
								}
							},
							new Runnable() {
								public void run() {
									HaroldTWireton.this.getPart(HaroldTWireton.Part.Hair03).setColor( new org.alice.apis.moveandturn.Color(0.0f, 0.0f, 1.0f), 0.25 );
								}
							}
						);

						org.alice.virtualmachine.DoTogether.invokeAndWait(
							new Runnable() {
								public void run() {
									HaroldTWireton.this.getPart(HaroldTWireton.Part.Hair01).setColor( new org.alice.apis.moveandturn.Color(1.0f, 1.0f, 1.0f), 0.25 );
								}
							},
							new Runnable() {
								public void run() {
									HaroldTWireton.this.getPart(HaroldTWireton.Part.Hair02).setColor( new org.alice.apis.moveandturn.Color(1.0f, 1.0f, 1.0f), 0.25 );
								}
							},
							new Runnable() {
								public void run() {
									HaroldTWireton.this.getPart(HaroldTWireton.Part.Hair03).setColor( new org.alice.apis.moveandturn.Color(1.0f, 1.0f, 1.0f), 0.25 );
								}
							}
						);

//					}

				}
					}
				},
				new Runnable() {
					public void run() {
						HaroldTWireton.this.move( org.alice.apis.moveandturn.MoveDirection.RIGHT, 1.0, 0.25 );
					}
				},
				new Runnable() {
					public void run() {
						HaroldTWireton.this.turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.25 );
					}
				}
			);

		}

	}
}
