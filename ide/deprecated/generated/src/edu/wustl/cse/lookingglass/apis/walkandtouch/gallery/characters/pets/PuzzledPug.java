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

public class PuzzledPug extends edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.GalleryCharacter { 
	private final edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Boolean> Flopped= new edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Boolean>( false );  
	private final edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Boolean> Sitting= new edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Boolean>( false );  
	public PuzzledPug() {
		super( "pets/Puzzled Pug" );
}
	public enum Part {
		Front_LeftCalf( "front_LeftThigh", "front_LeftCalf" ),
		Front_LeftThigh( "front_LeftThigh" ),
		Back_LeftCalf( "back_LeftThigh", "back_LeftCalf" ),
		Back_LeftThigh( "back_LeftThigh" ),
		Tail( "tail" ),
		Leftear( "head", "leftear" ),
		Rightear( "head", "rightear" ),
		Head( "head" ),
		Back_RightCalf( "back_RightThigh", "back_RightCalf" ),
		Back_RightThigh( "back_RightThigh" ),
		Front_RightCalf( "front_RightThigh", "front_RightCalf" ),
		Front_RightThigh( "front_RightThigh" );
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
					//PuzzledPug.this.playSound(sound)
				}
			},
			new Runnable() {
				public void run() {
					for (int index1 = 0; index1 < 2; index1 = index1 + 1) {
				PuzzledPug.this.getPart(PuzzledPug.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.0975 );
				PuzzledPug.this.getPart(PuzzledPug.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.0975 );
			}
				}
			}
		);

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
							PuzzledPug.this.getPart(PuzzledPug.Part.Front_LeftThigh).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.15 );
						}
					},
					new Runnable() {
						public void run() {
							PuzzledPug.this.getPart(PuzzledPug.Part.Front_RightThigh).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.15 );
						}
					},
					new Runnable() {
						public void run() {
							PuzzledPug.this.getPart(PuzzledPug.Part.Back_LeftThigh).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.15 );
						}
					},
					new Runnable() {
						public void run() {
							PuzzledPug.this.getPart(PuzzledPug.Part.Back_RightThigh).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.15 );
						}
					}
				);

				org.alice.virtualmachine.DoTogether.invokeAndWait(
					new Runnable() {
						public void run() {
							PuzzledPug.this.getPart(PuzzledPug.Part.Front_LeftThigh).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.15 );
						}
					},
					new Runnable() {
						public void run() {
							PuzzledPug.this.getPart(PuzzledPug.Part.Front_RightThigh).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.15 );
						}
					},
					new Runnable() {
						public void run() {
							PuzzledPug.this.getPart(PuzzledPug.Part.Back_LeftThigh).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.15 );
						}
					},
					new Runnable() {
						public void run() {
							PuzzledPug.this.getPart(PuzzledPug.Part.Back_RightThigh).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.15 );
						}
					}
				);

//			}
				}
			},
			new Runnable() {
				public void run() {
					// DoInOrder { 
				PuzzledPug.this.move( org.alice.apis.moveandturn.MoveDirection.UP, 0.5, 0.15 );
				PuzzledPug.this.delay(0.0010);
				PuzzledPug.this.move( org.alice.apis.moveandturn.MoveDirection.DOWN, 0.5, 0.15 );
//			}
				}
			},
			new Runnable() {
				public void run() {
					PuzzledPug.this.move( org.alice.apis.moveandturn.MoveDirection.FORWARD, 1.0, 0.3 );
				}
			}
		);

	}

	public void ShakeHeadNo( ) {
		this.getPart(PuzzledPug.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.1 );
		for (int index1 = 0; index1 < 2; index1 = index1 + 1) {
			this.getPart(PuzzledPug.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.1 );
			this.getPart(PuzzledPug.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.1 );
		}

		this.getPart(PuzzledPug.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.1 );
	}

	public void WagTail( final Number NumberOfTimes) {
		this.getPart(PuzzledPug.Part.Tail).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.1 );
		for (int index1 = 0; index1 < NumberOfTimes.intValue(); index1 = index1 + 1) {
			this.getPart(PuzzledPug.Part.Tail).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.1 );
			this.getPart(PuzzledPug.Part.Tail).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.1 );
		}

		this.getPart(PuzzledPug.Part.Tail).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.1 );
	}

	public void DoBellyflop( ) {
		if (Flopped.value ) {		} else { 
			Flopped.value = true;
			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						PuzzledPug.this.getPart(PuzzledPug.Part.Front_LeftThigh).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.15 );
					}
				},
				new Runnable() {
					public void run() {
						PuzzledPug.this.getPart(PuzzledPug.Part.Front_RightThigh).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.15 );
					}
				},
				new Runnable() {
					public void run() {
						PuzzledPug.this.getPart(PuzzledPug.Part.Back_LeftThigh).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.15 );
					}
				},
				new Runnable() {
					public void run() {
						PuzzledPug.this.getPart(PuzzledPug.Part.Back_RightThigh).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.15 );
					}
				},
				new Runnable() {
					public void run() {
						PuzzledPug.this.move( org.alice.apis.moveandturn.MoveDirection.DOWN, 0.12, 0.15 );
					}
				}
			);

			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						PuzzledPug.this.ShakeHeadNo(  );
					}
				},
				new Runnable() {
					public void run() {
						// DoInOrder { 
					PuzzledPug.this.turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.03) );
					PuzzledPug.this.turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.03) );
//				}
					}
				}
			);

		}

	}

	public void StandUp( ) {
		if (Flopped.value ) {			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						Flopped.value = false;
					}
				},
				new Runnable() {
					public void run() {
						PuzzledPug.this.getPart(PuzzledPug.Part.Front_LeftThigh).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.25 );
					}
				},
				new Runnable() {
					public void run() {
						PuzzledPug.this.getPart(PuzzledPug.Part.Front_RightThigh).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.25 );
					}
				},
				new Runnable() {
					public void run() {
						PuzzledPug.this.getPart(PuzzledPug.Part.Back_LeftThigh).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.25 );
					}
				},
				new Runnable() {
					public void run() {
						PuzzledPug.this.getPart(PuzzledPug.Part.Back_RightThigh).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.25 );
					}
				},
				new Runnable() {
					public void run() {
						PuzzledPug.this.move( org.alice.apis.moveandturn.MoveDirection.UP, 0.12, 0.25 );
					}
				}
			);

		} else { 
		}

		if (Sitting.value ) {			Sitting.value = false;
			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						PuzzledPug.this.getPart(PuzzledPug.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1) );
					}
				},
				new Runnable() {
					public void run() {
						PuzzledPug.this.turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1) );
					}
				},
				new Runnable() {
					public void run() {
						PuzzledPug.this.getPart(PuzzledPug.Part.Back_RightThigh).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15) );
					}
				},
				new Runnable() {
					public void run() {
						PuzzledPug.this.getPart(PuzzledPug.Part.Back_LeftThigh).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15) );
					}
				},
				new Runnable() {
					public void run() {
						PuzzledPug.this.move( org.alice.apis.moveandturn.MoveDirection.UP, 0.17 );
					}
				}
			);

		} else { 
		}

	}

	public void Summersault( ) {
		this.Sit(  );
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					PuzzledPug.this.turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(1.0) );
				}
			}
		);

		this.standUp(  );
	}

	public void Sit( ) {
		if (Sitting.value ) {		} else { 
			Sitting.value = true;
			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						PuzzledPug.this.getPart(PuzzledPug.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1) );
					}
				},
				new Runnable() {
					public void run() {
						PuzzledPug.this.turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1) );
					}
				},
				new Runnable() {
					public void run() {
						PuzzledPug.this.getPart(PuzzledPug.Part.Back_RightThigh).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15) );
					}
				},
				new Runnable() {
					public void run() {
						PuzzledPug.this.getPart(PuzzledPug.Part.Back_LeftThigh).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15) );
					}
				},
				new Runnable() {
					public void run() {
						PuzzledPug.this.move( org.alice.apis.moveandturn.MoveDirection.DOWN, 0.17 );
					}
				}
			);

		}

	}

	public void ChaseTail( ) {
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					//PuzzledPug.this.playSound(sound)
				}
			},
			new Runnable() {
				public void run() {
					PuzzledPug.this.WagTail( 5.0 );
				}
			},
			new Runnable() {
				public void run() {
					// DoInOrder { 
				PuzzledPug.this.getPart(PuzzledPug.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.25 );
				PuzzledPug.this.turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(2.0), 2.0 );
				PuzzledPug.this.getPart(PuzzledPug.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.25 );
//			}
				}
			}
		);

	}

	public void Fight( ) {
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					// DoInOrder { 
				org.alice.virtualmachine.DoTogether.invokeAndWait(
					new Runnable() {
						public void run() {
							PuzzledPug.this.ShakeHeadNo(  );
						}
					},
					new Runnable() {
						public void run() {
							PuzzledPug.this.move( org.alice.apis.moveandturn.MoveDirection.FORWARD, 0.5, 0.25 );
						}
					}
				);

				org.alice.virtualmachine.DoTogether.invokeAndWait(
					new Runnable() {
						public void run() {
							PuzzledPug.this.move( org.alice.apis.moveandturn.MoveDirection.BACKWARD, 0.5, 0.25 );
						}
					},
					new Runnable() {
						public void run() {
							PuzzledPug.this.ShakeHeadNo(  );
						}
					}
				);

//			}
				}
			},
			new Runnable() {
				public void run() {
					//PuzzledPug.this.playSound(sound)
				}
			}
		);

	}
}
