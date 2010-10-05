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
package edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.characters.fantasy;

public class FarraFaerimin extends edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.GalleryPerson { 
	public FarraFaerimin() {
		super( "fantasy/FarraFaerimin" );
}
	public enum Part {
		Hair( "neck", "head", "hair" ),
		LeftEar( "neck", "head", "leftEar" ),
		RightEar( "neck", "head", "rightEar" ),
		RightEyelid( "neck", "head", "rightEyelid" ),
		RightEye( "neck", "head", "rightEye" ),
		LeftEyelid( "neck", "head", "leftEyelid" ),
		LeftEye( "neck", "head", "leftEye" ),
		Head( "neck", "head" ),
		Neck( "neck" ),
		LeftHand( "leftUpperArm", "leftLowerArm", "leftHand" ),
		LeftLowerArm( "leftUpperArm", "leftLowerArm" ),
		LeftUpperArm( "leftUpperArm" ),
		LeftFoot( "leftUpperLeg", "leftFoot" ),
		LeftUpperLeg( "leftUpperLeg" ),
		LeftWing( "leftWing" ),
		RightWing( "rightWing" ),
		RightFoot( "rightUpperLeg", "rightFoot" ),
		RightUpperLeg( "rightUpperLeg" ),
		RightHand( "rightUpperArm", "rightLowerArm", "rightHand" ),
		RightLowerArm( "rightUpperArm", "rightLowerArm" ),
		RightUpperArm( "rightUpperArm" );
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

	public void FlapWings( final Number Duration) {
		 final edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Boolean> StillFlapping= new edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Boolean>( true );
		 final edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Double> NumberOfFlapsPerSecond= new edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Double>( 1.0 );
		//Faerie will flap wings "FlapsPerSecond" times for "HowLong" seconds
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					// DoInOrder { 
				for (int index1 = 0; index1 < (Duration.doubleValue() * NumberOfFlapsPerSecond.value); index1 = index1 + 1) {
					org.alice.virtualmachine.DoTogether.invokeAndWait(
						new Runnable() {
							public void run() {
								FarraFaerimin.this.getPart(FarraFaerimin.Part.LeftWing).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), (1.0 / (NumberOfFlapsPerSecond.value * 2.0)) );
							}
						},
						new Runnable() {
							public void run() {
								FarraFaerimin.this.getPart(FarraFaerimin.Part.RightWing).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), (1.0 / (NumberOfFlapsPerSecond.value * 2.0)) );
							}
						}
					);

					org.alice.virtualmachine.DoTogether.invokeAndWait(
						new Runnable() {
							public void run() {
								FarraFaerimin.this.getPart(FarraFaerimin.Part.LeftWing).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), (1.0 / (NumberOfFlapsPerSecond.value * 2.0)) );
							}
						},
						new Runnable() {
							public void run() {
								FarraFaerimin.this.getPart(FarraFaerimin.Part.RightWing).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), (1.0 / (NumberOfFlapsPerSecond.value * 2.0)) );
							}
						}
					);

				}

				StillFlapping.value = false;
//			}
				}
			}
		);

	}

	public void PoseToFly( ) {
		//pose arms and legs
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					FarraFaerimin.this.getPart(FarraFaerimin.Part.RightUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1) );
				}
			},
			new Runnable() {
				public void run() {
					FarraFaerimin.this.getPart(FarraFaerimin.Part.RightFoot).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15) );
				}
			},
			new Runnable() {
				public void run() {
					FarraFaerimin.this.getPart(FarraFaerimin.Part.LeftUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.07) );
				}
			},
			new Runnable() {
				public void run() {
					FarraFaerimin.this.getPart(FarraFaerimin.Part.LeftFoot).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15) );
				}
			},
			new Runnable() {
				public void run() {
					FarraFaerimin.this.getPart(FarraFaerimin.Part.LeftUpperArm).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.07) );
				}
			},
			new Runnable() {
				public void run() {
					FarraFaerimin.this.getPart(FarraFaerimin.Part.RightUpperArm).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.07) );
				}
			},
			new Runnable() {
				public void run() {
					FarraFaerimin.this.getPart(FarraFaerimin.Part.LeftLowerArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15) );
				}
			},
			new Runnable() {
				public void run() {
					FarraFaerimin.this.getPart(FarraFaerimin.Part.RightLowerArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.11) );
				}
			}
		);

	}

	public void FlyForward( final Number Distance, final Number Duration) {
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					FarraFaerimin.this.move( org.alice.apis.moveandturn.MoveDirection.UP, FarraFaerimin.this.getHeight() );
				}
			},
			new Runnable() {
				public void run() {
					FarraFaerimin.this.PoseToFly(  );
				}
			},
			new Runnable() {
				public void run() {
					FarraFaerimin.this.FlapWings( 1.0 );
				}
			}
		);

		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					FarraFaerimin.this.FlapWings( Duration );
				}
			},
			new Runnable() {
				public void run() {
					FarraFaerimin.this.move( org.alice.apis.moveandturn.MoveDirection.FORWARD, Distance.doubleValue(), Duration.doubleValue() );
				}
			}
		);

		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					FarraFaerimin.this.move( org.alice.apis.moveandturn.MoveDirection.DOWN, FarraFaerimin.this.getHeight() );
				}
			},
			new Runnable() {
				public void run() {
					FarraFaerimin.this.FlapWings( 1.0 );
				}
			}
		);

		this.standUp(  );
	}

	public void Blink( ) {
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					FarraFaerimin.this.getPart(FarraFaerimin.Part.RightEyelid).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.2 );
				}
			},
			new Runnable() {
				public void run() {
					FarraFaerimin.this.getPart(FarraFaerimin.Part.LeftEyelid).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.2 );
				}
			}
		);

		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					FarraFaerimin.this.getPart(FarraFaerimin.Part.RightEyelid).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.2 );
				}
			},
			new Runnable() {
				public void run() {
					FarraFaerimin.this.getPart(FarraFaerimin.Part.LeftEyelid).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.2 );
				}
			}
		);

	}

	public void LookAfraid( ) {
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					FarraFaerimin.this.look( edu.wustl.cse.lookingglass.apis.walkandtouch.LookDirection.DOWN, 0.1, 0.2 );
				}
			},
			new Runnable() {
				public void run() {
					FarraFaerimin.this.touch( FarraFaerimin.this.getPart(FarraFaerimin.Part.RightEye), org.alice.apis.moveandturn.MoveDirection.FORWARD, edu.wustl.cse.lookingglass.apis.walkandtouch.TouchPart.RIGHT_HAND, 0.0, 0.2 );
				}
			},
			new Runnable() {
				public void run() {
					FarraFaerimin.this.touch( FarraFaerimin.this.getPart(FarraFaerimin.Part.LeftEye), org.alice.apis.moveandturn.MoveDirection.FORWARD, edu.wustl.cse.lookingglass.apis.walkandtouch.TouchPart.LEFT_HAND, 0.0, 0.2 );
				}
			},
			new Runnable() {
				public void run() {
					FarraFaerimin.this.getPart(FarraFaerimin.Part.RightEyelid).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.2 );
				}
			},
			new Runnable() {
				public void run() {
					FarraFaerimin.this.getPart(FarraFaerimin.Part.LeftEyelid).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.2 );
				}
			}
		);

		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					FarraFaerimin.this.getPart(FarraFaerimin.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.2 );
				}
			},
			new Runnable() {
				public void run() {
					FarraFaerimin.this.getPart(FarraFaerimin.Part.LeftUpperArm).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.2 );
				}
			},
			new Runnable() {
				public void run() {
					FarraFaerimin.this.getPart(FarraFaerimin.Part.RightUpperArm).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.2 );
				}
			}
		);

		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					FarraFaerimin.this.getPart(FarraFaerimin.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.2 );
				}
			},
			new Runnable() {
				public void run() {
					FarraFaerimin.this.getPart(FarraFaerimin.Part.LeftUpperArm).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.2 );
				}
			},
			new Runnable() {
				public void run() {
					FarraFaerimin.this.getPart(FarraFaerimin.Part.RightUpperArm).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.2 );
				}
			}
		);

		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					FarraFaerimin.this.getPart(FarraFaerimin.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.2 );
				}
			},
			new Runnable() {
				public void run() {
					FarraFaerimin.this.getPart(FarraFaerimin.Part.LeftUpperArm).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.2 );
				}
			},
			new Runnable() {
				public void run() {
					FarraFaerimin.this.getPart(FarraFaerimin.Part.RightUpperArm).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.2 );
				}
			}
		);

		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					FarraFaerimin.this.getPart(FarraFaerimin.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.2 );
				}
			},
			new Runnable() {
				public void run() {
					FarraFaerimin.this.getPart(FarraFaerimin.Part.LeftUpperArm).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.2 );
				}
			},
			new Runnable() {
				public void run() {
					FarraFaerimin.this.getPart(FarraFaerimin.Part.RightUpperArm).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.2 );
				}
			}
		);

		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					FarraFaerimin.this.getPart(FarraFaerimin.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.2 );
				}
			},
			new Runnable() {
				public void run() {
					FarraFaerimin.this.getPart(FarraFaerimin.Part.LeftUpperArm).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.2 );
				}
			},
			new Runnable() {
				public void run() {
					FarraFaerimin.this.getPart(FarraFaerimin.Part.RightUpperArm).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.2 );
				}
			}
		);

		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					FarraFaerimin.this.getPart(FarraFaerimin.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.2 );
				}
			},
			new Runnable() {
				public void run() {
					FarraFaerimin.this.standUp( false );
				}
			}
		);

	}

	public void DoVictoryDance( ) {
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					// DoInOrder { 
				org.alice.virtualmachine.DoTogether.invokeAndWait(
					new Runnable() {
						public void run() {
							FarraFaerimin.this.move( org.alice.apis.moveandturn.MoveDirection.UP, 0.05, 0.25 );
						}
					},
					new Runnable() {
						public void run() {
							FarraFaerimin.this.getPart(FarraFaerimin.Part.RightFoot).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.25 );
						}
					},
					new Runnable() {
						public void run() {
							FarraFaerimin.this.getPart(FarraFaerimin.Part.LeftFoot).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.25 );
						}
					}
				);

				for (int index2 = 0; index2 < 5; index2 = index2 + 1) {
					org.alice.virtualmachine.DoTogether.invokeAndWait(
						new Runnable() {
							public void run() {
								FarraFaerimin.this.getPart(FarraFaerimin.Part.RightUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.35 );
							}
						},
						new Runnable() {
							public void run() {
								FarraFaerimin.this.getPart(FarraFaerimin.Part.LeftUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.35 );
							}
						}
					);

					org.alice.virtualmachine.DoTogether.invokeAndWait(
						new Runnable() {
							public void run() {
								FarraFaerimin.this.getPart(FarraFaerimin.Part.RightUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.5 );
							}
						},
						new Runnable() {
							public void run() {
								FarraFaerimin.this.getPart(FarraFaerimin.Part.LeftUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.5 );
							}
						}
					);

					org.alice.virtualmachine.DoTogether.invokeAndWait(
						new Runnable() {
							public void run() {
								FarraFaerimin.this.getPart(FarraFaerimin.Part.RightUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.35 );
							}
						},
						new Runnable() {
							public void run() {
								FarraFaerimin.this.getPart(FarraFaerimin.Part.LeftUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.35 );
							}
						}
					);

				}

//			}
				}
			},
			new Runnable() {
				public void run() {
					FarraFaerimin.this.turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(2.0), 7.0 );
				}
			},
			new Runnable() {
				public void run() {
					FarraFaerimin.this.FlapWings( 7.0 );
				}
			},
			new Runnable() {
				public void run() {
					// DoInOrder { 
				org.alice.virtualmachine.DoTogether.invokeAndWait(
					new Runnable() {
						public void run() {
							FarraFaerimin.this.Blink(  );
						}
					},
					new Runnable() {
						public void run() {
							FarraFaerimin.this.getPart(FarraFaerimin.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
						}
					},
					new Runnable() {
						public void run() {
							FarraFaerimin.this.getPart(FarraFaerimin.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
						}
					}
				);

				for (int index3 = 0; index3 < 3; index3 = index3 + 1) {
					org.alice.virtualmachine.DoTogether.invokeAndWait(
						new Runnable() {
							public void run() {
								FarraFaerimin.this.Blink(  );
							}
						},
						new Runnable() {
							public void run() {
								FarraFaerimin.this.getPart(FarraFaerimin.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.2) );
							}
						},
						new Runnable() {
							public void run() {
								// DoInOrder { 
							FarraFaerimin.this.getPart(FarraFaerimin.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
							FarraFaerimin.this.getPart(FarraFaerimin.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
//						}
							}
						}
					);

					org.alice.virtualmachine.DoTogether.invokeAndWait(
						new Runnable() {
							public void run() {
								FarraFaerimin.this.Blink(  );
							}
						},
						new Runnable() {
							public void run() {
								FarraFaerimin.this.getPart(FarraFaerimin.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.2) );
							}
						},
						new Runnable() {
							public void run() {
								// DoInOrder { 
							FarraFaerimin.this.getPart(FarraFaerimin.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
							FarraFaerimin.this.getPart(FarraFaerimin.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
//						}
							}
						}
					);

				}

				org.alice.virtualmachine.DoTogether.invokeAndWait(
					new Runnable() {
						public void run() {
							FarraFaerimin.this.Blink(  );
						}
					},
					new Runnable() {
						public void run() {
							FarraFaerimin.this.getPart(FarraFaerimin.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
						}
					},
					new Runnable() {
						public void run() {
							FarraFaerimin.this.getPart(FarraFaerimin.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
						}
					}
				);

//			}
				}
			},
			new Runnable() {
				public void run() {
					// DoInOrder { 
				org.alice.virtualmachine.DoTogether.invokeAndWait(
					new Runnable() {
						public void run() {
							FarraFaerimin.this.getPart(FarraFaerimin.Part.LeftUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.35), 0.5 );
						}
					},
					new Runnable() {
						public void run() {
							FarraFaerimin.this.getPart(FarraFaerimin.Part.LeftLowerArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.35), 0.5 );
						}
					}
				);

				FarraFaerimin.this.getPart(FarraFaerimin.Part.LeftLowerArm).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.35), 0.5 );
				FarraFaerimin.this.getPart(FarraFaerimin.Part.LeftUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.35), 0.5 );
//			}
				}
			},
			new Runnable() {
				public void run() {
					// DoInOrder { 
				FarraFaerimin.this.delay(1.0);
				org.alice.virtualmachine.DoTogether.invokeAndWait(
					new Runnable() {
						public void run() {
							FarraFaerimin.this.getPart(FarraFaerimin.Part.RightUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.35), 0.5 );
						}
					},
					new Runnable() {
						public void run() {
							FarraFaerimin.this.getPart(FarraFaerimin.Part.RightLowerArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.35), 0.5 );
						}
					}
				);

				org.alice.virtualmachine.DoTogether.invokeAndWait(
					new Runnable() {
						public void run() {
							FarraFaerimin.this.getPart(FarraFaerimin.Part.RightLowerArm).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.35), 0.5 );
						}
					},
					new Runnable() {
						public void run() {
							FarraFaerimin.this.getPart(FarraFaerimin.Part.RightUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.35), 0.5 );
						}
					}
				);

//			}
				}
			},
			new Runnable() {
				public void run() {
					// DoInOrder { 
				FarraFaerimin.this.delay(6.0);
				org.alice.virtualmachine.DoTogether.invokeAndWait(
					new Runnable() {
						public void run() {
							FarraFaerimin.this.FlapWings( 1.0 );
						}
					},
					new Runnable() {
						public void run() {
							FarraFaerimin.this.move( org.alice.apis.moveandturn.MoveDirection.DOWN, 0.05, 0.25 );
						}
					},
					new Runnable() {
						public void run() {
							FarraFaerimin.this.getPart(FarraFaerimin.Part.RightFoot).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.25 );
						}
					},
					new Runnable() {
						public void run() {
							FarraFaerimin.this.getPart(FarraFaerimin.Part.LeftFoot).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.25 );
						}
					}
				);

//			}
				}
			}
		);

	}
}
