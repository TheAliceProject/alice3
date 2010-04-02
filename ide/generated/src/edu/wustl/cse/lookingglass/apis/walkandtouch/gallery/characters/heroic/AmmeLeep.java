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

public class AmmeLeep extends edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.GalleryPerson { 
	public AmmeLeep() {
		super( "heroic/AmmeLeep" );
}
	public enum Part {
		LeftFoot( "hips", "leftUpperLeg", "leftLowerLeg", "leftFoot" ),
		LeftLowerLeg( "hips", "leftUpperLeg", "leftLowerLeg" ),
		LeftUpperLeg( "hips", "leftUpperLeg" ),
		RightFoot( "hips", "rightUpperLeg", "rightLowerLeg", "rightFoot" ),
		RightLowerLeg( "hips", "rightUpperLeg", "rightLowerLeg" ),
		RightUpperLeg( "hips", "rightUpperLeg" ),
		Belt( "hips", "belt" ),
		RightFingers( "hips", "torso", "rightUpperArm", "rightLowerArm", "rightHand", "rightFingers" ),
		RightHand( "hips", "torso", "rightUpperArm", "rightLowerArm", "rightHand" ),
		RightLowerArm( "hips", "torso", "rightUpperArm", "rightLowerArm" ),
		RightUpperArm( "hips", "torso", "rightUpperArm" ),
		LeftFingers( "hips", "torso", "leftUpperArm", "leftLowerArm", "leftHand", "leftFingers" ),
		LeftHand( "hips", "torso", "leftUpperArm", "leftLowerArm", "leftHand" ),
		LeftLowerArm( "hips", "torso", "leftUpperArm", "leftLowerArm" ),
		LeftUpperArm( "hips", "torso", "leftUpperArm" ),
		Hair( "hips", "torso", "head", "hair" ),
		Head( "hips", "torso", "head" ),
		Torso( "hips", "torso" ),
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

	public void SuperJumpKick( final edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel What, final Boolean SlowMotion) {
		 final edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Double> Delay= new edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Double>();  Delay.value = new Double(1.0);;
		this.walkTo( What, edu.wustl.cse.lookingglass.apis.walkandtouch.SpatialRelation.IN_FRONT_OF, 1.2 );
		if (SlowMotion.booleanValue() ) {			Delay.value = 1.0;
		} else { 
			Delay.value = 0.25;
		}

		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					AmmeLeep.this.getPart(AmmeLeep.Part.Torso).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), (Delay.value * 1.5) );
				}
			},
			new Runnable() {
				public void run() {
					AmmeLeep.this.getPart(AmmeLeep.Part.LeftUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), (Delay.value * 1.5) );
				}
			},
			new Runnable() {
				public void run() {
					AmmeLeep.this.getPart(AmmeLeep.Part.LeftLowerLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), (Delay.value * 1.5) );
				}
			},
			new Runnable() {
				public void run() {
					AmmeLeep.this.getPart(AmmeLeep.Part.RightUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.22), (Delay.value * 1.5) );
				}
			},
			new Runnable() {
				public void run() {
					AmmeLeep.this.getPart(AmmeLeep.Part.RightLowerLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), (Delay.value * 1.5) );
				}
			},
			new Runnable() {
				public void run() {
					AmmeLeep.this.move( org.alice.apis.moveandturn.MoveDirection.UP, (AmmeLeep.this.getPart(AmmeLeep.Part.LeftUpperLeg).getHeight() + AmmeLeep.this.getPart(AmmeLeep.Part.LeftLowerLeg).getHeight()), (Delay.value * 1.5) );
				}
			}
		);

		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					// DoInOrder { 
				org.alice.virtualmachine.DoTogether.invokeAndWait(
					new Runnable() {
						public void run() {
							AmmeLeep.this.move( org.alice.apis.moveandturn.MoveDirection.UP, (What.getHeight() / 3.0), (Delay.value * 1.5) );
						}
					},
					new Runnable() {
						public void run() {
							AmmeLeep.this.move( org.alice.apis.moveandturn.MoveDirection.FORWARD, (AmmeLeep.this.getDistanceTo(What) + 1.0), (Delay.value * 1.5) );
						}
					},
					new Runnable() {
						public void run() {
							AmmeLeep.this.getPart(AmmeLeep.Part.Hips).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), (Delay.value * 1.5) );
						}
					},
					new Runnable() {
						public void run() {
							AmmeLeep.this.getPart(AmmeLeep.Part.LeftLowerLeg).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), (Delay.value * 1.5) );
						}
					},
					new Runnable() {
						public void run() {
							AmmeLeep.this.getPart(AmmeLeep.Part.LeftUpperLeg).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), (Delay.value * 1.5) );
						}
					},
					new Runnable() {
						public void run() {
							AmmeLeep.this.getPart(AmmeLeep.Part.RightUpperLeg).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), (Delay.value * 1.5) );
						}
					}
				);

				org.alice.virtualmachine.DoTogether.invokeAndWait(
					new Runnable() {
						public void run() {
							// DoInOrder { 
						AmmeLeep.this.delay(0.5);
						AmmeLeep.this.standUp( false, (Delay.value * 1.5) );
//					}
						}
					},
					new Runnable() {
						public void run() {
							AmmeLeep.this.move( org.alice.apis.moveandturn.MoveDirection.DOWN, (AmmeLeep.this.getPart(AmmeLeep.Part.LeftUpperLeg).getHeight() + AmmeLeep.this.getPart(AmmeLeep.Part.LeftLowerLeg).getHeight()), (Delay.value * 1.5) );
						}
					},
					new Runnable() {
						public void run() {
							AmmeLeep.this.move( org.alice.apis.moveandturn.MoveDirection.FORWARD, 1.0, (Delay.value * 1.5) );
						}
					}
				);

//			}
				}
			},
			new Runnable() {
				public void run() {
					// DoInOrder { 
				AmmeLeep.this.delay(1.0);
				What.move( org.alice.apis.moveandturn.MoveDirection.FORWARD, 3.0, (Delay.value * 1.5), What );
//			}
				}
			}
		);

	}

	public void HoldAndKick( final edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel What) {
		this.walkTo( What, edu.wustl.cse.lookingglass.apis.walkandtouch.SpatialRelation.IN_FRONT_OF, 0.0 );
		this.touch( What );
		for (int index1 = 0; index1 < 3; index1 = index1 + 1) {
			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						AmmeLeep.this.getPart(AmmeLeep.Part.LeftUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.25 );
					}
				},
				new Runnable() {
					public void run() {
						AmmeLeep.this.getPart(AmmeLeep.Part.LeftLowerLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.25 );
					}
				},
				new Runnable() {
					public void run() {
						What.turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.25, What );
					}
				}
			);

			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						AmmeLeep.this.getPart(AmmeLeep.Part.LeftUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.25 );
					}
				},
				new Runnable() {
					public void run() {
						AmmeLeep.this.getPart(AmmeLeep.Part.LeftLowerLeg).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.25 );
					}
				},
				new Runnable() {
					public void run() {
						What.turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.25, What );
					}
				}
			);

		}

		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					AmmeLeep.this.standUp( false, 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					What.move( org.alice.apis.moveandturn.MoveDirection.FORWARD, 2.0, 0.5, What );
				}
			}
		);

		this.PrepareToFight(  );
	}

	public void BackFlip( final Number HowMany) {
		for (int index1 = 0; index1 < HowMany.intValue(); index1 = index1 + 1) {
			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						// DoInOrder { 
					org.alice.virtualmachine.DoTogether.invokeAndWait(
						new Runnable() {
							public void run() {
								AmmeLeep.this.move( org.alice.apis.moveandturn.MoveDirection.UP, 0.5, 0.15 );
							}
						},
						new Runnable() {
							public void run() {
								AmmeLeep.this.getPart(AmmeLeep.Part.RightUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.5), 0.15 );
							}
						},
						new Runnable() {
							public void run() {
								AmmeLeep.this.getPart(AmmeLeep.Part.LeftUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.5), 0.15 );
							}
						},
						new Runnable() {
							public void run() {
								AmmeLeep.this.getPart(AmmeLeep.Part.RightHand).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.3 );
							}
						},
						new Runnable() {
							public void run() {
								AmmeLeep.this.getPart(AmmeLeep.Part.LeftHand).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.3 );
							}
						},
						new Runnable() {
							public void run() {
								AmmeLeep.this.getPart(AmmeLeep.Part.LeftUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.3 );
							}
						},
						new Runnable() {
							public void run() {
								AmmeLeep.this.getPart(AmmeLeep.Part.RightUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.3 );
							}
						}
					);

					AmmeLeep.this.delay(0.25);
					AmmeLeep.this.standUp( false, 0.15 );
//				}
					}
				},
				new Runnable() {
					public void run() {
						AmmeLeep.this.move( org.alice.apis.moveandturn.MoveDirection.BACKWARD, 1.2, 0.7 );
					}
				},
				new Runnable() {
					public void run() {
						AmmeLeep.this.turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(1.0), 0.7, AmmeLeep.this.getPart(AmmeLeep.Part.Hips) );
					}
				}
			);

		}

	}

	public void ShakeHead( final Number HowManyTimes) {
		this.getPart(AmmeLeep.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1) );
		this.getPart(AmmeLeep.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.075), 0.25 );
		for (int index1 = 0; index1 < HowManyTimes.intValue(); index1 = index1 + 1) {
			this.getPart(AmmeLeep.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.25 );
			this.getPart(AmmeLeep.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.25 );
		}

		this.getPart(AmmeLeep.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.075), 0.25 );
		this.getPart(AmmeLeep.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1) );
	}

	public void PrepareToFight( ) {
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					AmmeLeep.this.getPart(AmmeLeep.Part.RightUpperArm).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					AmmeLeep.this.getPart(AmmeLeep.Part.LeftUpperArm).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					AmmeLeep.this.getPart(AmmeLeep.Part.RightUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					AmmeLeep.this.getPart(AmmeLeep.Part.LeftUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					AmmeLeep.this.getPart(AmmeLeep.Part.RightLowerArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					AmmeLeep.this.getPart(AmmeLeep.Part.LeftLowerArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.35), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					AmmeLeep.this.getPart(AmmeLeep.Part.LeftLowerArm).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					AmmeLeep.this.getPart(AmmeLeep.Part.RightLowerArm).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					AmmeLeep.this.getPart(AmmeLeep.Part.Torso).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					AmmeLeep.this.getPart(AmmeLeep.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					AmmeLeep.this.getPart(AmmeLeep.Part.RightUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.3), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					AmmeLeep.this.getPart(AmmeLeep.Part.RightLowerLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.3), 0.5 );
				}
			}
		);

	}
}
