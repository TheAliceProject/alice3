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
package edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.characters.scary;

public class Centaur extends edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.GalleryCharacter { 
	public Centaur() {
		super( "scary/Centaur" );
}
	public enum Part {
		RightWingTipFeathers( "X", "rightWingTip", "rightWingTipFeathers" ),
		RightWingTip( "X", "rightWingTip" ),
		RightWingBaseFeathers( "X", "rightWingBaseFeathers" ),
		X( "X" ),
		LeftWingTipFeathers( "Z", "leftWingTip", "leftWingTipFeathers" ),
		LeftWingTip( "Z", "leftWingTip" ),
		LeftWingBaseFeathers( "Z", "leftWingBaseFeathers" ),
		Z( "Z" ),
		Tail( "tail" ),
		RearLeftShin( "rearLeftThigh", "rearLeftShin" ),
		RearLeftThigh( "rearLeftThigh" ),
		RearRightShin( "rearRightThigh", "rearRightShin" ),
		RearRightThigh( "rearRightThigh" ),
		FrontRightFoot( "frontRightThigh", "frontRightShin", "frontRightFoot" ),
		FrontRightShin( "frontRightThigh", "frontRightShin" ),
		FrontRightThigh( "frontRightThigh" ),
		FrontLeftFoot( "frontLeftThigh", "frontLeftShin", "frontLeftFoot" ),
		FrontLeftShin( "frontLeftThigh", "frontLeftShin" ),
		FrontLeftThigh( "frontLeftThigh" ),
		RightFist( "torso", "rightUpperArm", "rightLowerArm", "rightFist" ),
		RightLowerArm( "torso", "rightUpperArm", "rightLowerArm" ),
		RightUpperArm( "torso", "rightUpperArm" ),
		LeftFist( "torso", "leftUpperArm", "leftLowerArm", "leftFist" ),
		LeftLowerArm( "torso", "leftUpperArm", "leftLowerArm" ),
		LeftUpperArm( "torso", "leftUpperArm" ),
		Head( "torso", "neck", "head" ),
		Neck( "torso", "neck" ),
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

	public void ChargeForward( ) {
		this.PawGround(  );
		this.Run( 4.0 );
	}

	public void Run( final Number Distance) {
		 final edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Double> Speed= new edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Double>( 0.3 );
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					if (false ) {				Centaur.this.move( org.alice.apis.moveandturn.MoveDirection.FORWARD, 0.5, (1.0 * Speed.value) );
				Centaur.this.move( org.alice.apis.moveandturn.MoveDirection.FORWARD, Distance.doubleValue(), ((1.25 * Distance.doubleValue()) * Speed.value) );
				Centaur.this.move( org.alice.apis.moveandturn.MoveDirection.FORWARD, 0.5, (0.5 * Speed.value) );
			} else { 
				Centaur.this.move( org.alice.apis.moveandturn.MoveDirection.FORWARD, 0.5, (1.5 * Speed.value) );
			}
				}
			},
			new Runnable() {
				public void run() {
					//move up and down
				}
			},
			new Runnable() {
				public void run() {
					// DoInOrder { 
				Centaur.this.delay(0.25);
				Centaur.this.move( org.alice.apis.moveandturn.MoveDirection.DOWN, 0.06, (Speed.value * 0.75) );
				for (int index5 = 0; index5 < Distance.intValue(); index5 = index5 + 1) {
					Centaur.this.move( org.alice.apis.moveandturn.MoveDirection.UP, 0.06, (0.75 * Speed.value) );
					Centaur.this.move( org.alice.apis.moveandturn.MoveDirection.DOWN, 0.06, (Speed.value * 0.75) );
				}

				Centaur.this.move( org.alice.apis.moveandturn.MoveDirection.UP, 0.06, (Speed.value * 0.5) );
//			}
				}
			},
			new Runnable() {
				public void run() {
					//right front leg
				}
			},
			new Runnable() {
				public void run() {
					// DoInOrder { 
				//get ready
				Centaur.this.getPart(Centaur.Part.FrontRightThigh).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), (Speed.value * 0.5) );
				Centaur.this.getPart(Centaur.Part.FrontRightShin).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), (Speed.value * 0.5) );
				//go!
				for (int index6 = 0; index6 < Distance.intValue(); index6 = index6 + 1) {
					//backward
					org.alice.virtualmachine.DoTogether.invokeAndWait(
						new Runnable() {
							public void run() {
								Centaur.this.getPart(Centaur.Part.FrontRightThigh).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), (Speed.value * 0.5) );
							}
						},
						new Runnable() {
							public void run() {
								// DoInOrder { 
							Centaur.this.getPart(Centaur.Part.FrontRightShin).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), (Speed.value * 0.25) );
							org.alice.virtualmachine.DoTogether.invokeAndWait(
								new Runnable() {
									public void run() {
										Centaur.this.getPart(Centaur.Part.FrontRightFoot).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), (Speed.value * 0.25) );
									}
								},
								new Runnable() {
									public void run() {
										Centaur.this.getPart(Centaur.Part.FrontRightShin).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), (Speed.value * 0.25) );
									}
								}
							);

//						}
							}
						}
					);

					//forward
					Centaur.this.getPart(Centaur.Part.FrontRightThigh).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), (Speed.value * 0.5) );
					Centaur.this.getPart(Centaur.Part.FrontRightFoot).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), (Speed.value * 0.25) );
				}

				//finish
				org.alice.virtualmachine.DoTogether.invokeAndWait(
					new Runnable() {
						public void run() {
							Centaur.this.getPart(Centaur.Part.FrontRightThigh).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), (Speed.value * 0.5) );
						}
					},
					new Runnable() {
						public void run() {
							Centaur.this.getPart(Centaur.Part.FrontRightShin).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), (Speed.value * 0.25) );
						}
					}
				);

//			}
				}
			},
			new Runnable() {
				public void run() {
					//left front leg
				}
			},
			new Runnable() {
				public void run() {
					// DoInOrder { 
				//get ready
				Centaur.this.delay(0.075);
				Centaur.this.getPart(Centaur.Part.FrontLeftThigh).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), (Speed.value * 0.5) );
				Centaur.this.getPart(Centaur.Part.FrontLeftShin).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), (Speed.value * 0.5) );
				//go!
				for (int index7 = 0; index7 < Distance.intValue(); index7 = index7 + 1) {
					//backward
					org.alice.virtualmachine.DoTogether.invokeAndWait(
						new Runnable() {
							public void run() {
								Centaur.this.getPart(Centaur.Part.FrontLeftThigh).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), (Speed.value * 0.5) );
							}
						},
						new Runnable() {
							public void run() {
								// DoInOrder { 
							Centaur.this.getPart(Centaur.Part.FrontLeftShin).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), (Speed.value * 0.25) );
							org.alice.virtualmachine.DoTogether.invokeAndWait(
								new Runnable() {
									public void run() {
										Centaur.this.getPart(Centaur.Part.FrontLeftFoot).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), (Speed.value * 0.25) );
									}
								},
								new Runnable() {
									public void run() {
										Centaur.this.getPart(Centaur.Part.FrontLeftShin).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), (Speed.value * 0.25) );
									}
								}
							);

//						}
							}
						}
					);

					//forward
					Centaur.this.getPart(Centaur.Part.FrontLeftThigh).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), (Speed.value * 0.5) );
					Centaur.this.getPart(Centaur.Part.FrontLeftFoot).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), (Speed.value * 0.25) );
				}

				//finish
				org.alice.virtualmachine.DoTogether.invokeAndWait(
					new Runnable() {
						public void run() {
							Centaur.this.getPart(Centaur.Part.FrontLeftThigh).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), (Speed.value * 0.5) );
						}
					},
					new Runnable() {
						public void run() {
							Centaur.this.getPart(Centaur.Part.FrontLeftShin).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), (Speed.value * 0.5) );
						}
					}
				);

//			}
				}
			},
			new Runnable() {
				public void run() {
					//right rear leg
				}
			},
			new Runnable() {
				public void run() {
					// DoInOrder { 
				Centaur.this.delay(0.22499999999999998);
				org.alice.virtualmachine.DoTogether.invokeAndWait(
					new Runnable() {
						public void run() {
							Centaur.this.getPart(Centaur.Part.RearRightThigh).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), (Speed.value * 0.25) );
						}
					},
					new Runnable() {
						public void run() {
							Centaur.this.getPart(Centaur.Part.RearRightShin).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), (Speed.value * 0.25) );
						}
					}
				);

				//go!
				for (int index8 = 0; index8 < Distance.intValue(); index8 = index8 + 1) {
					//backward
					Centaur.this.getPart(Centaur.Part.RearRightThigh).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), (Speed.value * 0.5) );
					Centaur.this.getPart(Centaur.Part.RearRightShin).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), (Speed.value * 0.5) );
					//forward
					org.alice.virtualmachine.DoTogether.invokeAndWait(
						new Runnable() {
							public void run() {
								Centaur.this.getPart(Centaur.Part.RearRightThigh).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), (Speed.value * 0.25) );
							}
						},
						new Runnable() {
							public void run() {
								Centaur.this.getPart(Centaur.Part.RearRightShin).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), (Speed.value * 0.25) );
							}
						}
					);

				}

				//finish
				Centaur.this.getPart(Centaur.Part.RearRightThigh).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), (Speed.value * 0.25) );
				Centaur.this.getPart(Centaur.Part.RearRightShin).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), (Speed.value * 0.25) );
//			}
				}
			},
			new Runnable() {
				public void run() {
					//left rear leg
				}
			},
			new Runnable() {
				public void run() {
					// DoInOrder { 
				Centaur.this.delay(0.3);
				org.alice.virtualmachine.DoTogether.invokeAndWait(
					new Runnable() {
						public void run() {
							Centaur.this.getPart(Centaur.Part.RearLeftThigh).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), (Speed.value * 0.25) );
						}
					},
					new Runnable() {
						public void run() {
							Centaur.this.getPart(Centaur.Part.RearLeftShin).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), (Speed.value * 0.25) );
						}
					}
				);

				//go!
				for (int index9 = 0; index9 < Distance.intValue(); index9 = index9 + 1) {
					//backward
					Centaur.this.getPart(Centaur.Part.RearLeftThigh).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), (Speed.value * 0.5) );
					Centaur.this.getPart(Centaur.Part.RearLeftShin).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), (Speed.value * 0.5) );
					//forward
					org.alice.virtualmachine.DoTogether.invokeAndWait(
						new Runnable() {
							public void run() {
								Centaur.this.getPart(Centaur.Part.RearLeftThigh).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), (Speed.value * 0.25) );
							}
						},
						new Runnable() {
							public void run() {
								Centaur.this.getPart(Centaur.Part.RearLeftShin).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), (Speed.value * 0.25) );
							}
						}
					);

				}

				//finish
				Centaur.this.getPart(Centaur.Part.RearLeftThigh).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), (Speed.value * 0.25) );
				Centaur.this.getPart(Centaur.Part.RearLeftShin).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), (Speed.value * 0.25) );
//			}
				}
			}
		);

	}

	public void Rear( ) {
		 final edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Double> SpeedGoingUp= new edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Double>( 1.0 );
		 final edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Double> SpeedGoingDown= new edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Double>( 1.0 );
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					//body
				}
			},
			new Runnable() {
				public void run() {
					Centaur.this.turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), SpeedGoingUp.value, Centaur.this.getPart(Centaur.Part.RearLeftThigh) );
				}
			},
			new Runnable() {
				public void run() {
					//legs
				}
			},
			new Runnable() {
				public void run() {
					Centaur.this.getPart(Centaur.Part.RearLeftThigh).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), SpeedGoingUp.value );
				}
			},
			new Runnable() {
				public void run() {
					Centaur.this.getPart(Centaur.Part.RearRightThigh).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), SpeedGoingUp.value );
				}
			},
			new Runnable() {
				public void run() {
					Centaur.this.getPart(Centaur.Part.FrontRightThigh).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), SpeedGoingUp.value );
				}
			},
			new Runnable() {
				public void run() {
					Centaur.this.getPart(Centaur.Part.FrontLeftThigh).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), SpeedGoingUp.value );
				}
			},
			new Runnable() {
				public void run() {
					Centaur.this.getPart(Centaur.Part.FrontRightShin).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), SpeedGoingUp.value );
				}
			},
			new Runnable() {
				public void run() {
					Centaur.this.getPart(Centaur.Part.FrontLeftShin).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), SpeedGoingUp.value );
				}
			}
		);

		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					Centaur.this.getPart(Centaur.Part.FrontRightShin).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.15 );
				}
			},
			new Runnable() {
				public void run() {
					Centaur.this.getPart(Centaur.Part.FrontRightThigh).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.15 );
				}
			}
		);

		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					Centaur.this.getPart(Centaur.Part.FrontRightThigh).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.15 );
				}
			},
			new Runnable() {
				public void run() {
					Centaur.this.getPart(Centaur.Part.FrontLeftThigh).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.15 );
				}
			},
			new Runnable() {
				public void run() {
					Centaur.this.getPart(Centaur.Part.FrontRightShin).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.15 );
				}
			},
			new Runnable() {
				public void run() {
					Centaur.this.getPart(Centaur.Part.FrontLeftShin).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.15 );
				}
			}
		);

		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					Centaur.this.getPart(Centaur.Part.FrontLeftThigh).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.15 );
				}
			},
			new Runnable() {
				public void run() {
					Centaur.this.getPart(Centaur.Part.FrontLeftShin).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.15 );
				}
			}
		);

		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					//body
				}
			},
			new Runnable() {
				public void run() {
					Centaur.this.turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), SpeedGoingDown.value, Centaur.this.getPart(Centaur.Part.RearLeftThigh) );
				}
			},
			new Runnable() {
				public void run() {
					//legs
				}
			},
			new Runnable() {
				public void run() {
					Centaur.this.getPart(Centaur.Part.RearLeftThigh).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), SpeedGoingDown.value );
				}
			},
			new Runnable() {
				public void run() {
					Centaur.this.getPart(Centaur.Part.RearRightThigh).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), SpeedGoingDown.value );
				}
			},
			new Runnable() {
				public void run() {
					Centaur.this.getPart(Centaur.Part.FrontRightThigh).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), SpeedGoingDown.value );
				}
			},
			new Runnable() {
				public void run() {
					Centaur.this.getPart(Centaur.Part.FrontLeftThigh).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), SpeedGoingDown.value );
				}
			},
			new Runnable() {
				public void run() {
					Centaur.this.getPart(Centaur.Part.FrontRightShin).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), SpeedGoingDown.value );
				}
			},
			new Runnable() {
				public void run() {
					Centaur.this.getPart(Centaur.Part.FrontLeftShin).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), SpeedGoingDown.value );
				}
			}
		);

	}

	public void Method( ) {
	}

	public void PawGround( ) {
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					Centaur.this.getPart(Centaur.Part.FrontRightThigh).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.08) );
				}
			},
			new Runnable() {
				public void run() {
					Centaur.this.getPart(Centaur.Part.FrontRightShin).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.08) );
				}
			},
			new Runnable() {
				public void run() {
					Centaur.this.getPart(Centaur.Part.FrontRightFoot).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.06), 0.25 );
				}
			}
		);

		for (int index1 = 0; index1 < 2; index1 = index1 + 1) {
			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						Centaur.this.getPart(Centaur.Part.FrontRightThigh).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.08), 0.25 );
					}
				},
				new Runnable() {
					public void run() {
						Centaur.this.getPart(Centaur.Part.FrontRightShin).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.08), 0.25 );
					}
				}
			);

			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						Centaur.this.getPart(Centaur.Part.FrontRightThigh).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.08), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Centaur.this.getPart(Centaur.Part.FrontRightShin).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.08), 0.5 );
					}
				}
			);

		}

		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					Centaur.this.getPart(Centaur.Part.FrontRightThigh).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.08), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					Centaur.this.getPart(Centaur.Part.FrontRightShin).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.08), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					Centaur.this.getPart(Centaur.Part.FrontRightFoot).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.06), 0.25 );
				}
			}
		);

	}

	public void Walk( final Number Distance, final Number Speed) {
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					// DoInOrder { 
				Centaur.this.move( org.alice.apis.moveandturn.MoveDirection.FORWARD, 1.0 );
				Centaur.this.move( org.alice.apis.moveandturn.MoveDirection.FORWARD, Distance.doubleValue(), (Speed.doubleValue() * (Distance.doubleValue() * 2.0)) );
//			}
				}
			},
			new Runnable() {
				public void run() {
					// DoInOrder { 
				org.alice.virtualmachine.DoTogether.invokeAndWait(
					new Runnable() {
						public void run() {
							Centaur.this.getPart(Centaur.Part.FrontLeftShin).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), (Speed.doubleValue() * 0.5) );
						}
					},
					new Runnable() {
						public void run() {
							Centaur.this.getPart(Centaur.Part.RearLeftShin).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.06), (Speed.doubleValue() * 0.5) );
						}
					},
					new Runnable() {
						public void run() {
							Centaur.this.getPart(Centaur.Part.RearLeftThigh).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.02), (Speed.doubleValue() * 0.5) );
						}
					}
				);

				for (int index1 = 0; index1 < Distance.intValue(); index1 = index1 + 1) {
					org.alice.virtualmachine.DoTogether.invokeAndWait(
						new Runnable() {
							public void run() {
								Centaur.this.getPart(Centaur.Part.FrontLeftShin).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), (Speed.doubleValue() * 0.5) );
							}
						},
						new Runnable() {
							public void run() {
								Centaur.this.getPart(Centaur.Part.RearLeftShin).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.06), (Speed.doubleValue() * 0.5) );
							}
						},
						new Runnable() {
							public void run() {
								Centaur.this.getPart(Centaur.Part.RearLeftThigh).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.02), (Speed.doubleValue() * 0.5) );
							}
						},
						new Runnable() {
							public void run() {
								Centaur.this.getPart(Centaur.Part.RearRightThigh).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.04), (Speed.doubleValue() * 0.5) );
							}
						},
						new Runnable() {
							public void run() {
								Centaur.this.getPart(Centaur.Part.FrontRightThigh).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.04), (Speed.doubleValue() * 0.5) );
							}
						}
					);

					org.alice.virtualmachine.DoTogether.invokeAndWait(
						new Runnable() {
							public void run() {
								Centaur.this.getPart(Centaur.Part.RearRightThigh).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.08), (Speed.doubleValue() * 0.5) );
							}
						},
						new Runnable() {
							public void run() {
								Centaur.this.getPart(Centaur.Part.FrontRightThigh).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.04), (Speed.doubleValue() * 0.5) );
							}
						},
						new Runnable() {
							public void run() {
								Centaur.this.getPart(Centaur.Part.FrontRightShin).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), (Speed.doubleValue() * 0.5) );
							}
						},
						new Runnable() {
							public void run() {
								Centaur.this.getPart(Centaur.Part.FrontRightFoot).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.04), (Speed.doubleValue() * 0.5) );
							}
						}
					);

					org.alice.virtualmachine.DoTogether.invokeAndWait(
						new Runnable() {
							public void run() {
								Centaur.this.getPart(Centaur.Part.RearRightThigh).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.04), (Speed.doubleValue() * 0.5) );
							}
						},
						new Runnable() {
							public void run() {
								Centaur.this.getPart(Centaur.Part.FrontRightThigh).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.04), (Speed.doubleValue() * 0.5) );
							}
						},
						new Runnable() {
							public void run() {
								Centaur.this.getPart(Centaur.Part.FrontLeftThigh).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.04), (Speed.doubleValue() * 0.5) );
							}
						},
						new Runnable() {
							public void run() {
								Centaur.this.getPart(Centaur.Part.FrontRightShin).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), (Speed.doubleValue() * 0.5) );
							}
						},
						new Runnable() {
							public void run() {
								Centaur.this.getPart(Centaur.Part.FrontRightFoot).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.04), (Speed.doubleValue() * 0.5) );
							}
						}
					);

					org.alice.virtualmachine.DoTogether.invokeAndWait(
						new Runnable() {
							public void run() {
								Centaur.this.getPart(Centaur.Part.FrontRightThigh).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.04), (Speed.doubleValue() * 0.5) );
							}
						},
						new Runnable() {
							public void run() {
								Centaur.this.getPart(Centaur.Part.FrontLeftThigh).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.04), (Speed.doubleValue() * 0.5) );
							}
						},
						new Runnable() {
							public void run() {
								Centaur.this.getPart(Centaur.Part.FrontLeftShin).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), (Speed.doubleValue() * 0.5) );
							}
						},
						new Runnable() {
							public void run() {
								Centaur.this.getPart(Centaur.Part.RearLeftThigh).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.02), (Speed.doubleValue() * 0.5) );
							}
						},
						new Runnable() {
							public void run() {
								Centaur.this.getPart(Centaur.Part.RearLeftShin).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.06), (Speed.doubleValue() * 0.5) );
							}
						}
					);

				}

				org.alice.virtualmachine.DoTogether.invokeAndWait(
					new Runnable() {
						public void run() {
							Centaur.this.getPart(Centaur.Part.FrontLeftShin).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), (Speed.doubleValue() * 0.5) );
						}
					},
					new Runnable() {
						public void run() {
							Centaur.this.getPart(Centaur.Part.RearLeftShin).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.06), (Speed.doubleValue() * 0.5) );
						}
					},
					new Runnable() {
						public void run() {
							Centaur.this.getPart(Centaur.Part.RearLeftThigh).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.02), (Speed.doubleValue() * 0.5) );
						}
					}
				);

//			}
				}
			}
		);

	}

	public void DoGuardPose( ) {
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					Centaur.this.getPart(Centaur.Part.RightUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					Centaur.this.getPart(Centaur.Part.LeftUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					Centaur.this.getPart(Centaur.Part.RightLowerArm).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.3) );
				}
			},
			new Runnable() {
				public void run() {
					Centaur.this.getPart(Centaur.Part.LeftLowerArm).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.3) );
				}
			},
			new Runnable() {
				public void run() {
					Centaur.this.getPart(Centaur.Part.Neck).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.03) );
				}
			},
			new Runnable() {
				public void run() {
					Centaur.this.PawGround(  );
				}
			}
		);

	}

	public void ThinkDeepThoughts( ) {
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					Centaur.this.getPart(Centaur.Part.Neck).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.03), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					Centaur.this.getPart(Centaur.Part.Neck).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.03), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					Centaur.this.getPart(Centaur.Part.LeftLowerArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.22), 0.3 );
				}
			},
			new Runnable() {
				public void run() {
					Centaur.this.getPart(Centaur.Part.LeftUpperArm).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					Centaur.this.getPart(Centaur.Part.LeftUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.5 );
				}
			}
		);

		for (int index1 = 0; index1 < 3; index1 = index1 + 1) {
			this.getPart(Centaur.Part.LeftUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.01), 0.3 );
			this.getPart(Centaur.Part.LeftUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.01), 0.3 );
		}

		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					Centaur.this.getPart(Centaur.Part.Neck).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.03), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					Centaur.this.getPart(Centaur.Part.Neck).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.03), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					Centaur.this.getPart(Centaur.Part.LeftLowerArm).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.22), 0.3 );
				}
			},
			new Runnable() {
				public void run() {
					Centaur.this.getPart(Centaur.Part.LeftUpperArm).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					Centaur.this.getPart(Centaur.Part.LeftUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.5 );
				}
			}
		);

	}

	public void LookLongingly( ) {
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					Centaur.this.getPart(Centaur.Part.Torso).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.06), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					Centaur.this.getPart(Centaur.Part.Neck).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.03), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					Centaur.this.getPart(Centaur.Part.Neck).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.03), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					Centaur.this.getPart(Centaur.Part.Neck).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.03), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					Centaur.this.getPart(Centaur.Part.LeftLowerArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.22), 0.3 );
				}
			},
			new Runnable() {
				public void run() {
					Centaur.this.getPart(Centaur.Part.LeftUpperArm).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					Centaur.this.getPart(Centaur.Part.LeftUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.5 );
				}
			}
		);

		this.delay(2.0);
		this.standUp( false );
	}

	public void HaveDemonEyes( ) {
		//this.getPart(Centaur.Part.Torso).setSurfaceTexture(value);
	}

	public void HaveNormalEyes( ) {
		//this.getPart(Centaur.Part.Torso).setSurfaceTexture(value);
	}
}
