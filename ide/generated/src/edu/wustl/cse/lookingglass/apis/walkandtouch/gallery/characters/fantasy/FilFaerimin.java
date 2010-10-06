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

public class FilFaerimin extends edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.GalleryPerson { 
	public FilFaerimin() {
		super( "fantasy/FilFaerimin" );
}
	public enum Part {
		RightWing( "rightWing" ),
		LeftWing( "leftWing" ),
		RightHand( "rightUpperArm", "rightLowerArm", "rightHand" ),
		RightLowerArm( "rightUpperArm", "rightLowerArm" ),
		RightUpperArm( "rightUpperArm" ),
		LeftHand( "leftUpperArm", "leftLowerArm", "leftHand" ),
		LeftLowerArm( "leftUpperArm", "leftLowerArm" ),
		LeftUpperArm( "leftUpperArm" ),
		Hair( "neck", "head", "hair" ),
		RightEar( "neck", "head", "rightEar" ),
		LeftEar( "neck", "head", "leftEar" ),
		LeftEyelid( "neck", "head", "leftEyelid" ),
		RightEyelid( "neck", "head", "rightEyelid" ),
		RightEye( "neck", "head", "rightEye" ),
		LeftEye( "neck", "head", "leftEye" ),
		Head( "neck", "head" ),
		Neck( "neck" ),
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

	public void FlapWings( final Number Duration) {
		 final edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Boolean> StillFlapping= new edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Boolean>( true );
		 final edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Double> NumberOfFlapsPerSecond= new edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Double>( 10.0 );
		//Faerie will flap wings "FlapsPerSecond" times for "HowLong" seconds
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					// DoInOrder { 
				for (int index1 = 0; index1 < (Duration.doubleValue() * NumberOfFlapsPerSecond.value); index1 = index1 + 1) {
					org.alice.virtualmachine.DoTogether.invokeAndWait(
						new Runnable() {
							public void run() {
								FilFaerimin.this.getPart(FilFaerimin.Part.LeftWing).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), (1.0 / (NumberOfFlapsPerSecond.value * 2.0)) );
							}
						},
						new Runnable() {
							public void run() {
								FilFaerimin.this.getPart(FilFaerimin.Part.RightWing).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), (1.0 / (NumberOfFlapsPerSecond.value * 2.0)) );
							}
						}
					);

					org.alice.virtualmachine.DoTogether.invokeAndWait(
						new Runnable() {
							public void run() {
								FilFaerimin.this.getPart(FilFaerimin.Part.LeftWing).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), (1.0 / (NumberOfFlapsPerSecond.value * 2.0)) );
							}
						},
						new Runnable() {
							public void run() {
								FilFaerimin.this.getPart(FilFaerimin.Part.RightWing).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), (1.0 / (NumberOfFlapsPerSecond.value * 2.0)) );
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

	public void FunkyChicken( final Number HowManyTimes) {
		for (int index3 = 0; index3 < 2; index3 = index3 + 1) {
			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						FilFaerimin.this.getPart(FilFaerimin.Part.RightWing).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						FilFaerimin.this.getPart(FilFaerimin.Part.LeftWing).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						FilFaerimin.this.getPart(FilFaerimin.Part.RightLowerArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						FilFaerimin.this.getPart(FilFaerimin.Part.RightUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						FilFaerimin.this.getPart(FilFaerimin.Part.RightHand).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						FilFaerimin.this.getPart(FilFaerimin.Part.LeftUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						FilFaerimin.this.getPart(FilFaerimin.Part.LeftHand).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.5 );
					}
				}
			);

			for (int index4 = 0; index4 < 2; index4 = index4 + 1) {
				org.alice.virtualmachine.DoTogether.invokeAndWait(
					new Runnable() {
						public void run() {
							FilFaerimin.this.getPart(FilFaerimin.Part.LeftUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.01), 0.25 );
						}
					},
					new Runnable() {
						public void run() {
							FilFaerimin.this.getPart(FilFaerimin.Part.RightLowerArm).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.02), 0.25 );
						}
					},
					new Runnable() {
						public void run() {
							FilFaerimin.this.getPart(FilFaerimin.Part.Head).move( org.alice.apis.moveandturn.MoveDirection.FORWARD, 0.04, 0.25 );
						}
					}
				);

				this.delay(0.25);
				org.alice.virtualmachine.DoTogether.invokeAndWait(
					new Runnable() {
						public void run() {
							FilFaerimin.this.getPart(FilFaerimin.Part.LeftUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.01), 0.25 );
						}
					},
					new Runnable() {
						public void run() {
							FilFaerimin.this.getPart(FilFaerimin.Part.RightLowerArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.02), 0.25 );
						}
					},
					new Runnable() {
						public void run() {
							FilFaerimin.this.getPart(FilFaerimin.Part.Head).move( org.alice.apis.moveandturn.MoveDirection.BACKWARD, 0.04, 0.25 );
						}
					}
				);

			}

			this.standUp( false, 0.5 );
			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						FilFaerimin.this.getPart(FilFaerimin.Part.RightWing).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						FilFaerimin.this.getPart(FilFaerimin.Part.LeftWing).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						FilFaerimin.this.getPart(FilFaerimin.Part.LeftLowerArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						FilFaerimin.this.getPart(FilFaerimin.Part.LeftUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						FilFaerimin.this.getPart(FilFaerimin.Part.LeftHand).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						FilFaerimin.this.getPart(FilFaerimin.Part.RightUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						FilFaerimin.this.getPart(FilFaerimin.Part.RightHand).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.5 );
					}
				}
			);

			this.delay(0.5);
			for (int index5 = 0; index5 < 2; index5 = index5 + 1) {
				org.alice.virtualmachine.DoTogether.invokeAndWait(
					new Runnable() {
						public void run() {
							FilFaerimin.this.getPart(FilFaerimin.Part.RightUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.01), 0.25 );
						}
					},
					new Runnable() {
						public void run() {
							FilFaerimin.this.getPart(FilFaerimin.Part.LeftLowerArm).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.02), 0.25 );
						}
					},
					new Runnable() {
						public void run() {
							FilFaerimin.this.getPart(FilFaerimin.Part.Head).move( org.alice.apis.moveandturn.MoveDirection.FORWARD, 0.04, 0.25 );
						}
					}
				);

				this.delay(0.25);
				org.alice.virtualmachine.DoTogether.invokeAndWait(
					new Runnable() {
						public void run() {
							FilFaerimin.this.getPart(FilFaerimin.Part.RightUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.01), 0.25 );
						}
					},
					new Runnable() {
						public void run() {
							FilFaerimin.this.getPart(FilFaerimin.Part.LeftLowerArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.02), 0.25 );
						}
					},
					new Runnable() {
						public void run() {
							FilFaerimin.this.getPart(FilFaerimin.Part.Head).move( org.alice.apis.moveandturn.MoveDirection.BACKWARD, 0.04, 0.25 );
						}
					}
				);

			}

			this.standUp( false, 0.5 );
		}

	}
}
