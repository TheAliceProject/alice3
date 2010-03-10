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

public class LittleRed extends edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.GalleryPerson { 
	public LittleRed() {
		super( "adults/Little red" );
}
	public enum Part {
		LeftEyeLid( "torso", "head", "leftEyeLid" ),
		RightEyeLid( "torso", "head", "rightEyeLid" ),
		LeftEye( "torso", "head", "leftEye" ),
		RightEye( "torso", "head", "rightEye" ),
		Head( "torso", "head" ),
		LeftFinger( "torso", "leftUpperArm", "leftLowerArm", "leftHand", "leftFinger" ),
		LeftThumb( "torso", "leftUpperArm", "leftLowerArm", "leftHand", "leftThumb" ),
		LeftHand( "torso", "leftUpperArm", "leftLowerArm", "leftHand" ),
		LeftLowerArm( "torso", "leftUpperArm", "leftLowerArm" ),
		LeftUpperArm( "torso", "leftUpperArm" ),
		LeftFoot( "torso", "dress", "leftUpperLeg", "leftFoot" ),
		LeftUpperLeg( "torso", "dress", "leftUpperLeg" ),
		RightFoot( "torso", "dress", "rightUpperLeg", "rightFoot" ),
		RightUpperLeg( "torso", "dress", "rightUpperLeg" ),
		Dress( "torso", "dress" ),
		RightThumb( "torso", "rightUpperArm", "rightLowerArm", "rightHand", "rightThumb" ),
		RightFinger( "torso", "rightUpperArm", "rightLowerArm", "rightHand", "rightFinger" ),
		RightHand( "torso", "rightUpperArm", "rightLowerArm", "rightHand" ),
		RightLowerArm( "torso", "rightUpperArm", "rightLowerArm" ),
		RightUpperArm( "torso", "rightUpperArm" ),
		Hood( "torso", "hood" ),
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

	public void LookHappy( ) {
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					LittleRed.this.standUp( false );
				}
			},
			new Runnable() {
				public void run() {
					//LittleRed.this.setSurfaceTexture(value);
				}
			}
		);

	}

	public void MatrixKick( final edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel KickWho, final Boolean BulletTimeCamera) {
		this.walkTo( KickWho, edu.wustl.cse.lookingglass.apis.walkandtouch.SpatialRelation.IN_FRONT_OF, 0.25 );
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					LittleRed.this.getPart(LittleRed.Part.LeftUpperArm).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					LittleRed.this.getPart(LittleRed.Part.LeftLowerArm).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					LittleRed.this.getPart(LittleRed.Part.RightUpperArm).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					LittleRed.this.getPart(LittleRed.Part.RightLowerArm).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					LittleRed.this.getPart(LittleRed.Part.Dress).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					LittleRed.this.move( org.alice.apis.moveandturn.MoveDirection.UP, 0.25, 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					if (BulletTimeCamera.booleanValue() ) {				LittleRed.this.getCamera().getTwoShotOf( LittleRed.this, KickWho, edu.wustl.cse.lookingglass.apis.walkandtouch.SpatialRelation.RIGHT_OF, 1.5, 0.5 );
			} else { 
			}
				}
			}
		);

		if (BulletTimeCamera.booleanValue() ) {			//set camera vehicle to world
			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						LittleRed.this.getCamera().turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(1.0), 2.5, KickWho );
					}
				},
				new Runnable() {
					public void run() {
						org.alice.virtualmachine.DoTogether.invokeAndWait(
					new Runnable() {
						public void run() {
							LittleRed.this.getPart(LittleRed.Part.Dress).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.5 );
						}
					},
					new Runnable() {
						public void run() {
							LittleRed.this.getPart(LittleRed.Part.Dress).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
						}
					}
				);
					}
				},
				new Runnable() {
					public void run() {
						// DoInOrder { 
					org.alice.virtualmachine.DoTogether.invokeAndWait(
						new Runnable() {
							public void run() {
								LittleRed.this.getPart(LittleRed.Part.Dress).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.5 );
							}
						},
						new Runnable() {
							public void run() {
								LittleRed.this.getPart(LittleRed.Part.Dress).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
							}
						}
					);

					LittleRed.this.move( org.alice.apis.moveandturn.MoveDirection.DOWN, 0.25 );
					LittleRed.this.standUp( false, 0.5 );
//				}
					}
				},
				new Runnable() {
					public void run() {
						KickWho.move( org.alice.apis.moveandturn.MoveDirection.FORWARD, 2.0, 2.5, KickWho );
					}
				}
			);

		} else { 
			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						org.alice.virtualmachine.DoTogether.invokeAndWait(
					new Runnable() {
						public void run() {
							LittleRed.this.getPart(LittleRed.Part.Dress).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.1 );
						}
					},
					new Runnable() {
						public void run() {
							LittleRed.this.getPart(LittleRed.Part.Dress).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.1 );
						}
					}
				);
					}
				},
				new Runnable() {
					public void run() {
						// DoInOrder { 
					org.alice.virtualmachine.DoTogether.invokeAndWait(
						new Runnable() {
							public void run() {
								LittleRed.this.getPart(LittleRed.Part.Dress).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.1 );
							}
						},
						new Runnable() {
							public void run() {
								LittleRed.this.getPart(LittleRed.Part.Dress).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.1 );
							}
						}
					);

					LittleRed.this.move( org.alice.apis.moveandturn.MoveDirection.DOWN, 0.25, 0.2 );
					LittleRed.this.standUp( false, 0.1 );
//				}
					}
				},
				new Runnable() {
					public void run() {
						KickWho.move( org.alice.apis.moveandturn.MoveDirection.FORWARD, 2.0, 0.4, KickWho );
					}
				}
			);

		}

	}

	public void Cheer( ) {
		this.standUp( false, 0.25 );
		for (int index1 = 0; index1 < 3; index1 = index1 + 1) {
			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						LittleRed.this.getPart(LittleRed.Part.LeftUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.35), 0.35 );
					}
				},
				new Runnable() {
					public void run() {
						LittleRed.this.getPart(LittleRed.Part.LeftLowerArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.35 );
					}
				},
				new Runnable() {
					public void run() {
						LittleRed.this.getPart(LittleRed.Part.LeftHand).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.35 );
					}
				},
				new Runnable() {
					public void run() {
						LittleRed.this.getPart(LittleRed.Part.RightUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.35), 0.35 );
					}
				},
				new Runnable() {
					public void run() {
						LittleRed.this.getPart(LittleRed.Part.RightLowerArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.35 );
					}
				},
				new Runnable() {
					public void run() {
						LittleRed.this.getPart(LittleRed.Part.RightHand).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.35 );
					}
				},
				new Runnable() {
					public void run() {
						LittleRed.this.move( org.alice.apis.moveandturn.MoveDirection.UP, 0.1, 0.35 );
					}
				}
			);

			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						LittleRed.this.getPart(LittleRed.Part.LeftUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.35), 0.35 );
					}
				},
				new Runnable() {
					public void run() {
						LittleRed.this.getPart(LittleRed.Part.LeftLowerArm).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.35 );
					}
				},
				new Runnable() {
					public void run() {
						LittleRed.this.getPart(LittleRed.Part.LeftHand).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.35 );
					}
				},
				new Runnable() {
					public void run() {
						LittleRed.this.getPart(LittleRed.Part.RightUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.35), 0.35 );
					}
				},
				new Runnable() {
					public void run() {
						LittleRed.this.getPart(LittleRed.Part.RightLowerArm).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.35 );
					}
				},
				new Runnable() {
					public void run() {
						LittleRed.this.getPart(LittleRed.Part.RightHand).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.35 );
					}
				},
				new Runnable() {
					public void run() {
						LittleRed.this.move( org.alice.apis.moveandturn.MoveDirection.DOWN, 0.1, 0.35 );
					}
				}
			);

		}

		this.LookHappy(  );
	}

	public void LookSurprised( ) {
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					LittleRed.this.getPart(LittleRed.Part.Torso).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					LittleRed.this.getPart(LittleRed.Part.Dress).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					LittleRed.this.getPart(LittleRed.Part.LeftUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					LittleRed.this.getPart(LittleRed.Part.LeftLowerArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.35), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					LittleRed.this.getPart(LittleRed.Part.RightUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					LittleRed.this.getPart(LittleRed.Part.RightLowerArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.35), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					//LittleRed.this.setSurfaceTexture(value);
				}
			}
		);

		this.delay(1.0);
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					LittleRed.this.LookHappy(  );
				}
			},
			new Runnable() {
				public void run() {
					LittleRed.this.standUp( false );
				}
			}
		);

	}

	public void LookSad( ) {
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					//LittleRed.this.setSurfaceTexture(value);
				}
			},
			new Runnable() {
				public void run() {
					LittleRed.this.touch( LittleRed.this.getPart(LittleRed.Part.Dress), org.alice.apis.moveandturn.MoveDirection.FORWARD, edu.wustl.cse.lookingglass.apis.walkandtouch.TouchPart.RIGHT_HAND, 0.1 );
				}
			},
			new Runnable() {
				public void run() {
					LittleRed.this.touch( LittleRed.this.getPart(LittleRed.Part.Dress), org.alice.apis.moveandturn.MoveDirection.FORWARD, edu.wustl.cse.lookingglass.apis.walkandtouch.TouchPart.LEFT_HAND, 0.1 );
				}
			},
			new Runnable() {
				public void run() {
					LittleRed.this.look( edu.wustl.cse.lookingglass.apis.walkandtouch.LookDirection.DOWN );
				}
			}
		);

	}
}
