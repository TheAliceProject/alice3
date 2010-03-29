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

public class JoeMeanie extends edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.GalleryPerson { 
	public JoeMeanie() {
		super( "adults/JoeMeanie" );
}
	public enum Part {
		RightUpperArm( "rightUpperArm" ),
		Head( "head" ),
		RightFoot( "hips", "rightUpperLeg", "rightLowerLeg", "rightFoot" ),
		RightLowerLeg( "hips", "rightUpperLeg", "rightLowerLeg" ),
		RightUpperLeg( "hips", "rightUpperLeg" ),
		LeftFoot( "hips", "leftUpperLeg", "leftLowerLeg", "leftFoot" ),
		LeftLowerLeg( "hips", "leftUpperLeg", "leftLowerLeg" ),
		LeftUpperLeg( "hips", "leftUpperLeg" ),
		Hips( "hips" ),
		LeftUpperArm( "leftUpperArm" );
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

	public void WaveArms( final Number Amount) {
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					JoeMeanie.this.getPart(JoeMeanie.Part.RightUpperArm).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25) );
				}
			},
			new Runnable() {
				public void run() {
					JoeMeanie.this.getPart(JoeMeanie.Part.LeftUpperArm).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25) );
				}
			}
		);

		for (int index1 = 0; index1 < Amount.intValue(); index1 = index1 + 1) {
			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						JoeMeanie.this.getPart(JoeMeanie.Part.RightUpperArm).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25) );
					}
				},
				new Runnable() {
					public void run() {
						JoeMeanie.this.getPart(JoeMeanie.Part.LeftUpperArm).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25) );
					}
				}
			);

			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						JoeMeanie.this.getPart(JoeMeanie.Part.RightUpperArm).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25) );
					}
				},
				new Runnable() {
					public void run() {
						JoeMeanie.this.getPart(JoeMeanie.Part.LeftUpperArm).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25) );
					}
				}
			);

		}

		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					JoeMeanie.this.getPart(JoeMeanie.Part.RightUpperArm).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25) );
				}
			},
			new Runnable() {
				public void run() {
					JoeMeanie.this.getPart(JoeMeanie.Part.LeftUpperArm).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25) );
				}
			}
		);

	}

	public void FallOver( ) {
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					JoeMeanie.this.WaveArms( 1.0 );
				}
			},
			new Runnable() {
				public void run() {
					// DoInOrder { 
				//fall
				org.alice.virtualmachine.DoTogether.invokeAndWait(
					new Runnable() {
						public void run() {
							JoeMeanie.this.move( org.alice.apis.moveandturn.MoveDirection.DOWN, 0.4 );
						}
					},
					new Runnable() {
						public void run() {
							JoeMeanie.this.turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1) );
						}
					},
					new Runnable() {
						public void run() {
							JoeMeanie.this.getPart(JoeMeanie.Part.RightUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.1 );
						}
					},
					new Runnable() {
						public void run() {
							JoeMeanie.this.getPart(JoeMeanie.Part.LeftUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.1 );
						}
					}
				);

				//kick
				for (int index1 = 0; index1 < 2; index1 = index1 + 1) {
					org.alice.virtualmachine.DoTogether.invokeAndWait(
						new Runnable() {
							public void run() {
								JoeMeanie.this.getPart(JoeMeanie.Part.RightUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.15 );
							}
						},
						new Runnable() {
							public void run() {
								JoeMeanie.this.getPart(JoeMeanie.Part.LeftUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.15 );
							}
						}
					);

					org.alice.virtualmachine.DoTogether.invokeAndWait(
						new Runnable() {
							public void run() {
								JoeMeanie.this.getPart(JoeMeanie.Part.RightUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.15 );
							}
						},
						new Runnable() {
							public void run() {
								JoeMeanie.this.getPart(JoeMeanie.Part.LeftUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.15 );
							}
						}
					);

				}

//			}
				}
			}
		);

	}

	public void LookAngry( ) {
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					JoeMeanie.this.getPart(JoeMeanie.Part.Head).setColor( new org.alice.apis.moveandturn.Color(0.6f, 0.0f, 0.2f) );
				}
			},
			new Runnable() {
				public void run() {
					JoeMeanie.this.getPart(JoeMeanie.Part.RightUpperArm).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.15) );
				}
			},
			new Runnable() {
				public void run() {
					JoeMeanie.this.getPart(JoeMeanie.Part.LeftUpperArm).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.15) );
				}
			}
		);

		this.WaveArms( 2.0 );
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					JoeMeanie.this.getPart(JoeMeanie.Part.Head).setColor( new org.alice.apis.moveandturn.Color(1.0f, 1.0f, 1.0f) );
				}
			},
			new Runnable() {
				public void run() {
					JoeMeanie.this.standUp( false );
				}
			}
		);

	}

	public void Pant( ) {
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					JoeMeanie.this.turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.2) );
				}
			},
			new Runnable() {
				public void run() {
					JoeMeanie.this.getPart(JoeMeanie.Part.Hips).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.2) );
				}
			},
			new Runnable() {
				public void run() {
					JoeMeanie.this.getPart(JoeMeanie.Part.RightUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.2) );
				}
			},
			new Runnable() {
				public void run() {
					JoeMeanie.this.getPart(JoeMeanie.Part.LeftUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.2) );
				}
			},
			new Runnable() {
				public void run() {
					JoeMeanie.this.move( org.alice.apis.moveandturn.MoveDirection.UP, 0.5, 1.0, org.alice.apis.moveandturn.AsSeenBy.SCENE );
				}
			}
		);

		for (int index1 = 0; index1 < 3; index1 = index1 + 1) {
			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						JoeMeanie.this.resize( 1.1, 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						JoeMeanie.this.getPart(JoeMeanie.Part.Hips).resize( 0.9090909090909091, 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						JoeMeanie.this.move( org.alice.apis.moveandturn.MoveDirection.DOWN, 0.05, 0.5, org.alice.apis.moveandturn.AsSeenBy.SCENE );
					}
				}
			);

			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						JoeMeanie.this.resize( 0.9090909090909091, 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						JoeMeanie.this.getPart(JoeMeanie.Part.Hips).resize( 1.1, 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						JoeMeanie.this.move( org.alice.apis.moveandturn.MoveDirection.UP, 0.05, 0.5, org.alice.apis.moveandturn.AsSeenBy.SCENE );
					}
				}
			);

		}

		this.standUp( false );
	}
}
