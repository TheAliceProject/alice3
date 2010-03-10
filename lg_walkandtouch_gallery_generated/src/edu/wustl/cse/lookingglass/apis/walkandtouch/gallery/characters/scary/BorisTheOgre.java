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

public class BorisTheOgre extends edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.GalleryPerson { 
	public BorisTheOgre() {
		super( "scary/Boris the Ogre" );
}
	public enum Part {
		Jaw( "Chest", "Head", "Jaw" ),
		LeftHorn( "Chest", "Head", "LeftHorn" ),
		RightHorn( "Chest", "Head", "RightHorn" ),
		LeftEar( "Chest", "Head", "LeftEar" ),
		RightEye( "Chest", "Head", "RightEye" ),
		RightEar( "Chest", "Head", "RightEar" ),
		LeftEye( "Chest", "Head", "LeftEye" ),
		Head( "Chest", "Head" ),
		RightHand( "Chest", "rightUpperArm", "rightLowerArm", "rightHand" ),
		RightLowerArm( "Chest", "rightUpperArm", "rightLowerArm" ),
		RightUpperArm( "Chest", "rightUpperArm" ),
		LeftHand( "Chest", "leftUpperArm", "leftLowerArm", "leftHand" ),
		LeftLowerArm( "Chest", "leftUpperArm", "leftLowerArm" ),
		LeftUpperArm( "Chest", "leftUpperArm" ),
		Chest( "Chest" ),
		RightFoot( "rightUpperLeg", "rightLowerLeg", "rightFoot" ),
		RightLowerLeg( "rightUpperLeg", "rightLowerLeg" ),
		RightUpperLeg( "rightUpperLeg" ),
		LeftFoot( "leftUpperLeg", "leftLowerLeg", "leftFoot" ),
		LeftLowerLeg( "leftUpperLeg", "leftLowerLeg" ),
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

	public void Roar( ) {
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					BorisTheOgre.this.getPart(BorisTheOgre.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1) );
				}
			},
			new Runnable() {
				public void run() {
					BorisTheOgre.this.getPart(BorisTheOgre.Part.Jaw).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1) );
				}
			},
			new Runnable() {
				public void run() {
					BorisTheOgre.this.getPart(BorisTheOgre.Part.RightUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1) );
				}
			},
			new Runnable() {
				public void run() {
					BorisTheOgre.this.getPart(BorisTheOgre.Part.LeftUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1) );
				}
			}
		);

		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					// DoInOrder { 
				BorisTheOgre.this.getPart(BorisTheOgre.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.25 );
				BorisTheOgre.this.getPart(BorisTheOgre.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.25 );
				BorisTheOgre.this.getPart(BorisTheOgre.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.25 );
				BorisTheOgre.this.getPart(BorisTheOgre.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.25 );
				BorisTheOgre.this.getPart(BorisTheOgre.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.25 );
//			}
				}
			},
			new Runnable() {
				public void run() {
					//BorisTheOgre.this.playSound(sound)
				}
			}
		);

		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					BorisTheOgre.this.getPart(BorisTheOgre.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1) );
				}
			},
			new Runnable() {
				public void run() {
					BorisTheOgre.this.getPart(BorisTheOgre.Part.Jaw).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1) );
				}
			},
			new Runnable() {
				public void run() {
					BorisTheOgre.this.getPart(BorisTheOgre.Part.RightUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1) );
				}
			},
			new Runnable() {
				public void run() {
					BorisTheOgre.this.getPart(BorisTheOgre.Part.LeftUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1) );
				}
			}
		);

	}

	public void Jump( ) {
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					// DoInOrder { 
				BorisTheOgre.this.delay(0.75);
				//BorisTheOgre.this.playSound(sound)
//			}
				}
			},
			new Runnable() {
				public void run() {
					// DoInOrder { 
				org.alice.virtualmachine.DoTogether.invokeAndWait(
					new Runnable() {
						public void run() {
							BorisTheOgre.this.getPart(BorisTheOgre.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1) );
						}
					},
					new Runnable() {
						public void run() {
							BorisTheOgre.this.getPart(BorisTheOgre.Part.RightUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.2) );
						}
					},
					new Runnable() {
						public void run() {
							BorisTheOgre.this.getPart(BorisTheOgre.Part.LeftUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.2) );
						}
					},
					new Runnable() {
						public void run() {
							BorisTheOgre.this.getPart(BorisTheOgre.Part.RightUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1) );
						}
					},
					new Runnable() {
						public void run() {
							BorisTheOgre.this.getPart(BorisTheOgre.Part.LeftUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1) );
						}
					},
					new Runnable() {
						public void run() {
							BorisTheOgre.this.getPart(BorisTheOgre.Part.RightLowerLeg).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.2) );
						}
					},
					new Runnable() {
						public void run() {
							BorisTheOgre.this.getPart(BorisTheOgre.Part.LeftLowerLeg).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.2) );
						}
					},
					new Runnable() {
						public void run() {
							BorisTheOgre.this.getPart(BorisTheOgre.Part.LeftFoot).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1) );
						}
					},
					new Runnable() {
						public void run() {
							BorisTheOgre.this.getPart(BorisTheOgre.Part.RightFoot).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1) );
						}
					}
				);

				org.alice.virtualmachine.DoTogether.invokeAndWait(
					new Runnable() {
						public void run() {
							BorisTheOgre.this.getPart(BorisTheOgre.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
						}
					},
					new Runnable() {
						public void run() {
							BorisTheOgre.this.getPart(BorisTheOgre.Part.RightUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.4), 0.5 );
						}
					},
					new Runnable() {
						public void run() {
							BorisTheOgre.this.getPart(BorisTheOgre.Part.LeftUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.4), 0.5 );
						}
					},
					new Runnable() {
						public void run() {
							BorisTheOgre.this.move( org.alice.apis.moveandturn.MoveDirection.UP, 20.0, 0.5 );
						}
					}
				);

//			}
				}
			}
		);

		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					//BorisTheOgre.this.playSound(sound)
				}
			},
			new Runnable() {
				public void run() {
					// DoInOrder { 
				org.alice.virtualmachine.DoTogether.invokeAndWait(
					new Runnable() {
						public void run() {
							BorisTheOgre.this.move( org.alice.apis.moveandturn.MoveDirection.DOWN, 20.0, 0.25 );
						}
					},
					new Runnable() {
						public void run() {
							BorisTheOgre.this.getPart(BorisTheOgre.Part.RightUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.25 );
						}
					},
					new Runnable() {
						public void run() {
							BorisTheOgre.this.getPart(BorisTheOgre.Part.LeftUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.25 );
						}
					}
				);

				BorisTheOgre.this.delay(0.25);
				org.alice.virtualmachine.DoTogether.invokeAndWait(
					new Runnable() {
						public void run() {
							BorisTheOgre.this.getPart(BorisTheOgre.Part.RightUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
						}
					},
					new Runnable() {
						public void run() {
							BorisTheOgre.this.getPart(BorisTheOgre.Part.LeftUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
						}
					},
					new Runnable() {
						public void run() {
							BorisTheOgre.this.getPart(BorisTheOgre.Part.RightLowerLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.5 );
						}
					},
					new Runnable() {
						public void run() {
							BorisTheOgre.this.getPart(BorisTheOgre.Part.LeftLowerLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.5 );
						}
					},
					new Runnable() {
						public void run() {
							BorisTheOgre.this.getPart(BorisTheOgre.Part.LeftFoot).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
						}
					},
					new Runnable() {
						public void run() {
							BorisTheOgre.this.getPart(BorisTheOgre.Part.RightFoot).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
						}
					}
				);

//			}
				}
			}
		);

	}

	public void ZanyHornTrick( ) {
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					BorisTheOgre.this.getPart(BorisTheOgre.Part.LeftHorn).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(5.0) );
				}
			},
			new Runnable() {
				public void run() {
					BorisTheOgre.this.getPart(BorisTheOgre.Part.RightHorn).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(5.0) );
				}
			}
		);

	}

	public void Eat( ) {
		this.touch( this.getPart(BorisTheOgre.Part.Jaw), org.alice.apis.moveandturn.MoveDirection.FORWARD, edu.wustl.cse.lookingglass.apis.walkandtouch.TouchPart.RIGHT_HAND, 0.25 );
		this.getPart(BorisTheOgre.Part.Jaw).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.08), 0.25 );
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					BorisTheOgre.this.getPart(BorisTheOgre.Part.Jaw).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.08), 0.25 );
				}
			}
		);

		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					for (int index2 = 0; index2 < 3; index2 = index2 + 1) {
				BorisTheOgre.this.delay(0.03);
				//BorisTheOgre.this.playSound(sound)
			}
				}
			},
			new Runnable() {
				public void run() {
					for (int index3 = 0; index3 < 4; index3 = index3 + 1) {
				BorisTheOgre.this.getPart(BorisTheOgre.Part.Jaw).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.01), 0.25 );
				BorisTheOgre.this.getPart(BorisTheOgre.Part.Jaw).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.01), 0.25 );
			}
				}
			}
		);

		this.standUp( false );
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					//BorisTheOgre.this.playSound(sound)
				}
			},
			new Runnable() {
				public void run() {
					// DoInOrder { 
				BorisTheOgre.this.getPart(BorisTheOgre.Part.Jaw).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.08), 0.5 );
				BorisTheOgre.this.delay(0.25);
//			}
				}
			}
		);

		this.getPart(BorisTheOgre.Part.Jaw).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.08), 0.5 );
	}

	public void Laugh( ) {
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					//BorisTheOgre.this.playSound(sound)
				}
			},
			new Runnable() {
				public void run() {
					// DoInOrder { 
				BorisTheOgre.this.getPart(BorisTheOgre.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.25 );
				for (int index1 = 0; index1 < 2; index1 = index1 + 1) {
					BorisTheOgre.this.getPart(BorisTheOgre.Part.Jaw).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.08), 0.2 );
					BorisTheOgre.this.getPart(BorisTheOgre.Part.Jaw).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.08), 0.2 );
				}

				BorisTheOgre.this.getPart(BorisTheOgre.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.25 );
//			}
				}
			},
			new Runnable() {
				public void run() {
					// DoInOrder { 
				BorisTheOgre.this.resizeDepth( 1.25, 0.25, org.alice.apis.moveandturn.ResizePolicy.PRESERVE_NOTHING  );
				BorisTheOgre.this.resizeDepth( 0.8, 0.25, org.alice.apis.moveandturn.ResizePolicy.PRESERVE_NOTHING  );
				BorisTheOgre.this.resizeDepth( 1.25, 0.25, org.alice.apis.moveandturn.ResizePolicy.PRESERVE_NOTHING  );
				BorisTheOgre.this.resizeDepth( 0.8, 0.25, org.alice.apis.moveandturn.ResizePolicy.PRESERVE_NOTHING  );
//			}
				}
			}
		);

	}
}
