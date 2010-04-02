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
package edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.characters.pets;

public class PriscillaPoodle extends edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.GalleryCharacter { 
	public PriscillaPoodle() {
		super( "pets/PriscillaPoodle" );
}
	public enum Part {
		LeftEar( "head", "leftEar" ),
		LeftEyelid( "head", "leftEyelid" ),
		RightEar( "head", "rightEar" ),
		Hair( "head", "hair" ),
		Mouth( "head", "mouth" ),
		RightEyelid( "head", "rightEyelid" ),
		Head( "head" ),
		Tail( "tail" ),
		RightRearUpperLegPoof( "rightRearUpperLeg", "rightRearLowerLeg", "rightRearUpperLegPoof" ),
		RightRearLowerLeg( "rightRearUpperLeg", "rightRearLowerLeg" ),
		RightRearLowerLegPoof( "rightRearUpperLeg", "rightRearLowerLegPoof" ),
		RightRearUpperLeg( "rightRearUpperLeg" ),
		RightFrontUpperLegPoof( "rightFrontUpperLeg", "rightFrontUpperLegPoof" ),
		RightFrontLowerLegPoof( "rightFrontUpperLeg", "rightFrontLowerLeg", "rightFrontLowerLegPoof" ),
		RightFrontLowerLeg( "rightFrontUpperLeg", "rightFrontLowerLeg" ),
		RightFrontUpperLeg( "rightFrontUpperLeg" ),
		LeftFrontUpperLegPoof( "leftFrontUpperLeg", "leftFrontUpperLegPoof" ),
		LeftFrontLowerLegPoof( "leftFrontUpperLeg", "leftFrontLowerLeg", "leftFrontLowerLegPoof" ),
		Mirror( "leftFrontUpperLeg", "leftFrontLowerLeg", "mirror" ),
		LeftFrontLowerLeg( "leftFrontUpperLeg", "leftFrontLowerLeg" ),
		LeftFrontUpperLeg( "leftFrontUpperLeg" ),
		LeftRearUpperLegPoof( "leftRearUpperLeg", "leftRearUpperLegPoof" ),
		LeftRearLowerLegPoof( "leftRearUpperLeg", "leftRearLowerLeg", "leftRearLowerLegPoof" ),
		LeftRearLowerLeg( "leftRearUpperLeg", "leftRearLowerLeg" ),
		LeftRearUpperLeg( "leftRearUpperLeg" );
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

	public void Bark( ) {
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					for (int index1 = 0; index1 < 3; index1 = index1 + 1) {
				PriscillaPoodle.this.getPart(PriscillaPoodle.Part.Mouth).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.03), 0.20483333333333334 );
				PriscillaPoodle.this.getPart(PriscillaPoodle.Part.Mouth).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.03), 0.20483333333333334 );
			}
				}
			},
			new Runnable() {
				public void run() {
					//PriscillaPoodle.this.playSound(sound)
				}
			}
		);

	}

	public void BeASnob( ) {
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					//head
				}
			},
			new Runnable() {
				public void run() {
					PriscillaPoodle.this.getPart(PriscillaPoodle.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					PriscillaPoodle.this.getPart(PriscillaPoodle.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.01), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					//tail
				}
			},
			new Runnable() {
				public void run() {
					PriscillaPoodle.this.getPart(PriscillaPoodle.Part.Tail).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.5 );
				}
			}
		);

		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					//turn
				}
			},
			new Runnable() {
				public void run() {
					PriscillaPoodle.this.turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.5) );
				}
			},
			new Runnable() {
				public void run() {
					PriscillaPoodle.this.WagTail( 5.0 );
				}
			}
		);

	}

	public void BatEyelashes( ) {
		for (int index1 = 0; index1 < 2; index1 = index1 + 1) {
			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						PriscillaPoodle.this.getPart(PriscillaPoodle.Part.RightEyelid).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.1 );
					}
				},
				new Runnable() {
					public void run() {
						PriscillaPoodle.this.getPart(PriscillaPoodle.Part.LeftEyelid).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.1 );
					}
				}
			);

			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						PriscillaPoodle.this.getPart(PriscillaPoodle.Part.RightEyelid).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.1 );
					}
				},
				new Runnable() {
					public void run() {
						PriscillaPoodle.this.getPart(PriscillaPoodle.Part.LeftEyelid).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.1 );
					}
				}
			);

		}

	}

	public void Jump( ) {
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					// DoInOrder { 
				//move legs
				org.alice.virtualmachine.DoTogether.invokeAndWait(
					new Runnable() {
						public void run() {
							PriscillaPoodle.this.getPart(PriscillaPoodle.Part.RightFrontUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.15 );
						}
					},
					new Runnable() {
						public void run() {
							PriscillaPoodle.this.getPart(PriscillaPoodle.Part.LeftFrontUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.15 );
						}
					},
					new Runnable() {
						public void run() {
							PriscillaPoodle.this.getPart(PriscillaPoodle.Part.RightRearUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.15 );
						}
					},
					new Runnable() {
						public void run() {
							PriscillaPoodle.this.getPart(PriscillaPoodle.Part.LeftRearUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.15 );
						}
					}
				);

				org.alice.virtualmachine.DoTogether.invokeAndWait(
					new Runnable() {
						public void run() {
							PriscillaPoodle.this.getPart(PriscillaPoodle.Part.RightFrontUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.15 );
						}
					},
					new Runnable() {
						public void run() {
							PriscillaPoodle.this.getPart(PriscillaPoodle.Part.LeftFrontUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.15 );
						}
					},
					new Runnable() {
						public void run() {
							PriscillaPoodle.this.getPart(PriscillaPoodle.Part.RightRearUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.15 );
						}
					},
					new Runnable() {
						public void run() {
							PriscillaPoodle.this.getPart(PriscillaPoodle.Part.LeftRearUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.15 );
						}
					}
				);

//			}
				}
			},
			new Runnable() {
				public void run() {
					// DoInOrder { 
				//jump up
				PriscillaPoodle.this.move( org.alice.apis.moveandturn.MoveDirection.UP, 0.5, 0.15 );
				PriscillaPoodle.this.delay(0.0010);
				PriscillaPoodle.this.move( org.alice.apis.moveandturn.MoveDirection.DOWN, 0.5, 0.15 );
//			}
				}
			},
			new Runnable() {
				public void run() {
					PriscillaPoodle.this.move( org.alice.apis.moveandturn.MoveDirection.FORWARD, 1.0, 0.3 );
				}
			}
		);

	}

	public void WagTail( final Number NumberOfTimes) {
		this.getPart(PriscillaPoodle.Part.Tail).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.075), 0.05 );
		for (int index1 = 0; index1 < NumberOfTimes.intValue(); index1 = index1 + 1) {
			this.getPart(PriscillaPoodle.Part.Tail).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.15 );
			this.getPart(PriscillaPoodle.Part.Tail).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.15 );
		}

		this.getPart(PriscillaPoodle.Part.Tail).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.075), 0.05 );
	}

	public void ShakeHeadNo( ) {
		this.getPart(PriscillaPoodle.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.1 );
		for (int index1 = 0; index1 < 2; index1 = index1 + 1) {
			this.getPart(PriscillaPoodle.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.1 );
			this.getPart(PriscillaPoodle.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.1 );
		}

		this.getPart(PriscillaPoodle.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.1 );
	}

	public void BlowAKiss( ) {
		this.getPart(PriscillaPoodle.Part.RightFrontUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25) );
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					PriscillaPoodle.this.getPart(PriscillaPoodle.Part.RightFrontLowerLeg).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.75 );
				}
			},
			new Runnable() {
				public void run() {
					PriscillaPoodle.this.getPart(PriscillaPoodle.Part.RightFrontLowerLeg).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.75 );
				}
			}
		);

		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					//PriscillaPoodle.this.playSound(sound)
				}
			},
			new Runnable() {
				public void run() {
					// DoInOrder { 
				PriscillaPoodle.this.delay(0.1);
				PriscillaPoodle.this.getPart(PriscillaPoodle.Part.RightFrontLowerLeg).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.8 );
//			}
				}
			}
		);

		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					PriscillaPoodle.this.getPart(PriscillaPoodle.Part.RightFrontLowerLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					PriscillaPoodle.this.getPart(PriscillaPoodle.Part.RightFrontUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.5 );
				}
			}
		);

	}

	public void Fight( ) {
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					//PriscillaPoodle.this.playSound(sound)
				}
			},
			new Runnable() {
				public void run() {
					// DoInOrder { 
				PriscillaPoodle.this.getPart(PriscillaPoodle.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.05 );
				for (int index2 = 0; index2 < 3; index2 = index2 + 1) {
					PriscillaPoodle.this.ShakeHeadNo(  );
				}

				PriscillaPoodle.this.getPart(PriscillaPoodle.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.05 );
//			}
				}
			},
			new Runnable() {
				public void run() {
					// DoInOrder { 
				org.alice.virtualmachine.DoTogether.invokeAndWait(
					new Runnable() {
						public void run() {
							PriscillaPoodle.this.getPart(PriscillaPoodle.Part.LeftFrontUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.25 );
						}
					},
					new Runnable() {
						public void run() {
							PriscillaPoodle.this.getPart(PriscillaPoodle.Part.LeftFrontLowerLeg).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.25 );
						}
					}
				);

				for (int index3 = 0; index3 < 3; index3 = index3 + 1) {
					org.alice.virtualmachine.DoTogether.invokeAndWait(
						new Runnable() {
							public void run() {
								PriscillaPoodle.this.getPart(PriscillaPoodle.Part.RightFrontUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.25 );
							}
						},
						new Runnable() {
							public void run() {
								PriscillaPoodle.this.getPart(PriscillaPoodle.Part.RightFrontLowerLeg).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.25 );
							}
						},
						new Runnable() {
							public void run() {
								PriscillaPoodle.this.getPart(PriscillaPoodle.Part.LeftFrontUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.25 );
							}
						},
						new Runnable() {
							public void run() {
								PriscillaPoodle.this.getPart(PriscillaPoodle.Part.LeftFrontLowerLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.25 );
							}
						}
					);

					org.alice.virtualmachine.DoTogether.invokeAndWait(
						new Runnable() {
							public void run() {
								PriscillaPoodle.this.getPart(PriscillaPoodle.Part.RightFrontUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.25 );
							}
						},
						new Runnable() {
							public void run() {
								PriscillaPoodle.this.getPart(PriscillaPoodle.Part.RightFrontLowerLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.25 );
							}
						},
						new Runnable() {
							public void run() {
								PriscillaPoodle.this.getPart(PriscillaPoodle.Part.LeftFrontUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.25 );
							}
						},
						new Runnable() {
							public void run() {
								PriscillaPoodle.this.getPart(PriscillaPoodle.Part.LeftFrontLowerLeg).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.25 );
							}
						}
					);

				}

				org.alice.virtualmachine.DoTogether.invokeAndWait(
					new Runnable() {
						public void run() {
							PriscillaPoodle.this.getPart(PriscillaPoodle.Part.LeftFrontUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.25 );
						}
					},
					new Runnable() {
						public void run() {
							PriscillaPoodle.this.getPart(PriscillaPoodle.Part.LeftFrontLowerLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.25 );
						}
					}
				);

//			}
				}
			}
		);

	}
}
