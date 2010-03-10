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

public class DouglasTheTree extends edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.GalleryCharacter { 
	public DouglasTheTree() {
		super( "fantasy/DouglasTheTree" );
}
	public enum Part {
		Glasses( "glasses" ),
		Eyes( "eyes" ),
		LeftEyebrow( "leftEyebrow" ),
		RightEyebrow( "rightEyebrow" ),
		RightUpperArm( "rightUpperArm" ),
		LeftUpperArm( "leftUpperArm" ),
		LargeLeaves( "largeLeaves" ),
		SmallLeaves( "smallLeaves" );
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

	public void LookPuzzled( ) {
		this.getPart(DouglasTheTree.Part.RightEyebrow).move( org.alice.apis.moveandturn.MoveDirection.UP, 0.4, 0.5 );
		this.delay(1.0);
		this.getPart(DouglasTheTree.Part.RightEyebrow).move( org.alice.apis.moveandturn.MoveDirection.DOWN, 0.4, 0.5 );
	}

	public void ActSurprised( ) {
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					DouglasTheTree.this.getPart(DouglasTheTree.Part.LeftEyebrow).move( org.alice.apis.moveandturn.MoveDirection.UP, 0.6 );
				}
			},
			new Runnable() {
				public void run() {
					DouglasTheTree.this.getPart(DouglasTheTree.Part.RightEyebrow).move( org.alice.apis.moveandturn.MoveDirection.UP, 0.6 );
				}
			},
			new Runnable() {
				public void run() {
					DouglasTheTree.this.getPart(DouglasTheTree.Part.Eyes).resize( 2.0, 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					// DoInOrder { 
				org.alice.virtualmachine.DoTogether.invokeAndWait(
					new Runnable() {
						public void run() {
							DouglasTheTree.this.getPart(DouglasTheTree.Part.RightUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.3 );
						}
					},
					new Runnable() {
						public void run() {
							DouglasTheTree.this.getPart(DouglasTheTree.Part.RightUpperArm).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.3 );
						}
					},
					new Runnable() {
						public void run() {
							DouglasTheTree.this.getPart(DouglasTheTree.Part.LeftUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.3 );
						}
					},
					new Runnable() {
						public void run() {
							DouglasTheTree.this.getPart(DouglasTheTree.Part.LeftUpperArm).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.3 );
						}
					}
				);

				org.alice.virtualmachine.DoTogether.invokeAndWait(
					new Runnable() {
						public void run() {
							DouglasTheTree.this.getPart(DouglasTheTree.Part.RightUpperArm).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.3 );
						}
					},
					new Runnable() {
						public void run() {
							DouglasTheTree.this.getPart(DouglasTheTree.Part.RightUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.3 );
						}
					},
					new Runnable() {
						public void run() {
							DouglasTheTree.this.getPart(DouglasTheTree.Part.LeftUpperArm).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.3 );
						}
					},
					new Runnable() {
						public void run() {
							DouglasTheTree.this.getPart(DouglasTheTree.Part.LeftUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.3 );
						}
					}
				);

//			}
				}
			},
			new Runnable() {
				public void run() {
					// DoInOrder { 
				DouglasTheTree.this.getPart(DouglasTheTree.Part.Glasses).move( org.alice.apis.moveandturn.MoveDirection.UP, 0.4, 0.25 );
				DouglasTheTree.this.getPart(DouglasTheTree.Part.Glasses).move( org.alice.apis.moveandturn.MoveDirection.DOWN, 0.4, 0.25 );
//			}
				}
			},
			new Runnable() {
				public void run() {
					// DoInOrder { 
				org.alice.virtualmachine.DoTogether.invokeAndWait(
					new Runnable() {
						public void run() {
							DouglasTheTree.this.move( org.alice.apis.moveandturn.MoveDirection.UP, 0.08, 0.5 );
						}
					},
					new Runnable() {
						public void run() {
							DouglasTheTree.this.move( org.alice.apis.moveandturn.MoveDirection.BACKWARD, 0.08, 0.5 );
						}
					}
				);

				DouglasTheTree.this.move( org.alice.apis.moveandturn.MoveDirection.DOWN, 0.08, 0.25 );
//			}
				}
			},
			new Runnable() {
				public void run() {
					// DoInOrder { 
				DouglasTheTree.this.delay(0.6);
				org.alice.virtualmachine.DoTogether.invokeAndWait(
					new Runnable() {
						public void run() {
							DouglasTheTree.this.getPart(DouglasTheTree.Part.RightEyebrow).move( org.alice.apis.moveandturn.MoveDirection.DOWN, 0.6, 0.25 );
						}
					},
					new Runnable() {
						public void run() {
							DouglasTheTree.this.getPart(DouglasTheTree.Part.LeftEyebrow).move( org.alice.apis.moveandturn.MoveDirection.DOWN, 0.6, 0.25 );
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
					DouglasTheTree.this.move( org.alice.apis.moveandturn.MoveDirection.FORWARD, 0.08 );
				}
			},
			new Runnable() {
				public void run() {
					DouglasTheTree.this.getPart(DouglasTheTree.Part.Eyes).resize( 0.5 );
				}
			}
		);

	}

	public void ActScary( ) {
		 final edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Boolean> StillScaring= new edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Boolean>();  StillScaring.value = new Boolean(true);;
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					DouglasTheTree.this.Scowl(  );
				}
			},
			new Runnable() {
				public void run() {
					// DoInOrder { 
				org.alice.virtualmachine.DoTogether.invokeAndWait(
					new Runnable() {
						public void run() {
							DouglasTheTree.this.getPart(DouglasTheTree.Part.RightUpperArm).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.5 );
						}
					},
					new Runnable() {
						public void run() {
							DouglasTheTree.this.getPart(DouglasTheTree.Part.LeftUpperArm).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.5 );
						}
					}
				);

				for (int index1 = 0; index1 < 2; index1 = index1 + 1) {
					DouglasTheTree.this.WaveArms( 0.5 );
				}

				StillScaring.value = false;
				org.alice.virtualmachine.DoTogether.invokeAndWait(
					new Runnable() {
						public void run() {
							DouglasTheTree.this.getPart(DouglasTheTree.Part.RightUpperArm).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.5 );
						}
					},
					new Runnable() {
						public void run() {
							DouglasTheTree.this.getPart(DouglasTheTree.Part.LeftUpperArm).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.5 );
						}
					}
				);

//			}
				}
			},
			new Runnable() {
				public void run() {
					while ( StillScaring.value) {
				// DoInOrder { 
					org.alice.virtualmachine.DoTogether.invokeAndWait(
						new Runnable() {
							public void run() {
								// DoInOrder { 
							DouglasTheTree.this.delay(0.0020);
							DouglasTheTree.this.getPart(DouglasTheTree.Part.LargeLeaves).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.18 );
//						}
							}
						},
						new Runnable() {
							public void run() {
								DouglasTheTree.this.getPart(DouglasTheTree.Part.SmallLeaves).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.18 );
							}
						}
					);

					org.alice.virtualmachine.DoTogether.invokeAndWait(
						new Runnable() {
							public void run() {
								// DoInOrder { 
							DouglasTheTree.this.delay(0.0020);
							DouglasTheTree.this.getPart(DouglasTheTree.Part.LargeLeaves).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.18 );
//						}
							}
						},
						new Runnable() {
							public void run() {
								DouglasTheTree.this.getPart(DouglasTheTree.Part.SmallLeaves).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.18 );
							}
						}
					);

//				}

			}
				}
			}
		);

	}

	public void Scowl( ) {
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					DouglasTheTree.this.getPart(DouglasTheTree.Part.LeftEyebrow).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					DouglasTheTree.this.getPart(DouglasTheTree.Part.RightEyebrow).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
				}
			}
		);

		this.delay(2.0);
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					DouglasTheTree.this.getPart(DouglasTheTree.Part.LeftEyebrow).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					DouglasTheTree.this.getPart(DouglasTheTree.Part.RightEyebrow).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
				}
			}
		);

	}

	public void WaveArms( final Number Duration) {
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					// DoInOrder { 
				org.alice.virtualmachine.DoTogether.invokeAndWait(
					new Runnable() {
						public void run() {
							DouglasTheTree.this.getPart(DouglasTheTree.Part.RightUpperArm).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), (Duration.doubleValue() / 2.0) );
						}
					},
					new Runnable() {
						public void run() {
							DouglasTheTree.this.getPart(DouglasTheTree.Part.RightUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), (Duration.doubleValue() / 2.0) );
						}
					}
				);

				org.alice.virtualmachine.DoTogether.invokeAndWait(
					new Runnable() {
						public void run() {
							DouglasTheTree.this.getPart(DouglasTheTree.Part.RightUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), (Duration.doubleValue() / 2.0) );
						}
					},
					new Runnable() {
						public void run() {
							DouglasTheTree.this.getPart(DouglasTheTree.Part.RightUpperArm).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), (Duration.doubleValue() / 2.0) );
						}
					}
				);

//			}
				}
			},
			new Runnable() {
				public void run() {
					// DoInOrder { 
				DouglasTheTree.this.delay(0.05);
				org.alice.virtualmachine.DoTogether.invokeAndWait(
					new Runnable() {
						public void run() {
							DouglasTheTree.this.getPart(DouglasTheTree.Part.LeftUpperArm).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), (Duration.doubleValue() / 2.0) );
						}
					},
					new Runnable() {
						public void run() {
							DouglasTheTree.this.getPart(DouglasTheTree.Part.LeftUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), (Duration.doubleValue() / 2.0) );
						}
					}
				);

				org.alice.virtualmachine.DoTogether.invokeAndWait(
					new Runnable() {
						public void run() {
							DouglasTheTree.this.getPart(DouglasTheTree.Part.LeftUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), (Duration.doubleValue() / 2.0) );
						}
					},
					new Runnable() {
						public void run() {
							DouglasTheTree.this.getPart(DouglasTheTree.Part.LeftUpperArm).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), (Duration.doubleValue() / 2.0) );
						}
					}
				);

//			}
				}
			}
		);

	}

	public void CreepForward( final Number Distance) {
		 final edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Boolean> Moving= new edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Boolean>();  Moving.value = new Boolean(true);;
		//makes the tree creep forwards. you can enter a negative number to make the tree creep backwards
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					while ( Moving.value) {
				DouglasTheTree.this.WaveArms( 2.0 );
			}
				}
			},
			new Runnable() {
				public void run() {
					while ( Moving.value) {
				// DoInOrder { 
					DouglasTheTree.this.turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.0020), 0.33 );
					DouglasTheTree.this.turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.0040), 0.33 );
					DouglasTheTree.this.turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.0020), 0.33 );
//				}

			}
				}
			},
			new Runnable() {
				public void run() {
					// DoInOrder { 
				DouglasTheTree.this.move( org.alice.apis.moveandturn.MoveDirection.FORWARD, Distance.doubleValue(), Distance.doubleValue() );
				Moving.value = false;
//			}
				}
			}
		);

	}

	public void WaveHello( ) {
		// DoInOrder { 
			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						DouglasTheTree.this.getPart(DouglasTheTree.Part.LeftEyebrow).move( org.alice.apis.moveandturn.MoveDirection.UP, 0.2, 0.2 );
					}
				},
				new Runnable() {
					public void run() {
						DouglasTheTree.this.getPart(DouglasTheTree.Part.RightEyebrow).move( org.alice.apis.moveandturn.MoveDirection.UP, 0.2, 0.2 );
					}
				}
			);

			this.getPart(DouglasTheTree.Part.LeftUpperArm).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.3 );
			this.getPart(DouglasTheTree.Part.LeftUpperArm).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.02), 0.2 );
			this.getPart(DouglasTheTree.Part.LeftUpperArm).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.02), 0.2 );
			this.getPart(DouglasTheTree.Part.LeftUpperArm).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.02), 0.2 );
			this.getPart(DouglasTheTree.Part.LeftUpperArm).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.02), 0.2 );
			this.getPart(DouglasTheTree.Part.LeftUpperArm).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.2 );
			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						DouglasTheTree.this.getPart(DouglasTheTree.Part.LeftEyebrow).move( org.alice.apis.moveandturn.MoveDirection.DOWN, 0.2, 0.2 );
					}
				},
				new Runnable() {
					public void run() {
						DouglasTheTree.this.getPart(DouglasTheTree.Part.RightEyebrow).move( org.alice.apis.moveandturn.MoveDirection.DOWN, 0.2, 0.2 );
					}
				}
			);

//		}

	}
}
