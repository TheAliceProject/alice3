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
package edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.characters.undersea;

public class MsAngelaTSnobbington extends edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.GalleryCharacter { 
	public MsAngelaTSnobbington() {
		super( "undersea/Ms Angela T Snobbington" );
}
	public enum Part {
		Finhandleft( "body", "finarmleft", "finhandleft" ),
		Finarmleft( "body", "finarmleft" ),
		Finhandright( "body", "finarmright", "finhandright" ),
		Finarmright( "body", "finarmright" ),
		Topfin( "body", "topfin" ),
		Bottomfin( "body", "bottomfin" ),
		Chinfin( "body", "head", "chinfin" ),
		Upperlip( "body", "head", "upperlip" ),
		Lowerlip( "body", "head", "lowerlip" ),
		Eyelidright( "body", "head", "eyeright", "eyelidright" ),
		Eyeright( "body", "head", "eyeright" ),
		Eyelidleft( "body", "head", "eyeleft", "eyelidleft" ),
		Eyeleft( "body", "head", "eyeleft" ),
		Head( "body", "head" ),
		Body( "body" ),
		Tailfin( "tail", "tailfin" ),
		Tail( "tail" );
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

	public void Swim( final Number Amount) {
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					MsAngelaTSnobbington.this.WiggleTail( (Amount.doubleValue() * 2.0) );
				}
			},
			new Runnable() {
				public void run() {
					for (int index2 = 0; index2 < (Amount.doubleValue() * 0.5); index2 = index2 + 1) {
				MsAngelaTSnobbington.this.turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.025) );
				MsAngelaTSnobbington.this.turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.025) );
			}
				}
			},
			new Runnable() {
				public void run() {
					MsAngelaTSnobbington.this.move( org.alice.apis.moveandturn.MoveDirection.FORWARD, Amount.doubleValue(), Amount.doubleValue() );
				}
			},
			new Runnable() {
				public void run() {
					for (int index3 = 0; index3 < Amount.intValue(); index3 = index3 + 1) {
				org.alice.virtualmachine.DoTogether.invokeAndWait(
					new Runnable() {
						public void run() {
							MsAngelaTSnobbington.this.getPart(MsAngelaTSnobbington.Part.Finarmleft).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.125), 0.5 );
						}
					},
					new Runnable() {
						public void run() {
							MsAngelaTSnobbington.this.getPart(MsAngelaTSnobbington.Part.Finarmright).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.125), 0.5 );
						}
					}
				);

				org.alice.virtualmachine.DoTogether.invokeAndWait(
					new Runnable() {
						public void run() {
							MsAngelaTSnobbington.this.getPart(MsAngelaTSnobbington.Part.Finarmleft).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.125), 0.5 );
						}
					},
					new Runnable() {
						public void run() {
							MsAngelaTSnobbington.this.getPart(MsAngelaTSnobbington.Part.Finarmright).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.125), 0.5 );
						}
					}
				);

			}
				}
			}
		);

	}

	public void WiggleTail( final Number Amount) {
		this.getPart(MsAngelaTSnobbington.Part.Tail).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.025), 0.125 );
		for (int index1 = 0; index1 < (Amount.doubleValue() + 1.0); index1 = index1 + 1) {
			this.getPart(MsAngelaTSnobbington.Part.Tail).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.25 );
			this.getPart(MsAngelaTSnobbington.Part.Tail).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.25 );
		}

		this.getPart(MsAngelaTSnobbington.Part.Tail).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.025), 0.125 );
	}

	public void Primp( ) {
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					//move arm
				}
			},
			new Runnable() {
				public void run() {
					MsAngelaTSnobbington.this.getPart(MsAngelaTSnobbington.Part.Finhandleft).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.5) );
				}
			},
			new Runnable() {
				public void run() {
					MsAngelaTSnobbington.this.getPart(MsAngelaTSnobbington.Part.Finarmleft).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25) );
				}
			},
			new Runnable() {
				public void run() {
					MsAngelaTSnobbington.this.getPart(MsAngelaTSnobbington.Part.Finarmleft).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.05) );
				}
			},
			new Runnable() {
				public void run() {
					MsAngelaTSnobbington.this.getPart(MsAngelaTSnobbington.Part.Finarmleft).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.03) );
				}
			}
		);

		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					MsAngelaTSnobbington.this.getPart(MsAngelaTSnobbington.Part.Head).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.02) );
				}
			},
			new Runnable() {
				public void run() {
					for (int index3 = 0; index3 < 4; index3 = index3 + 1) {
				//wiggle top fin
				org.alice.virtualmachine.DoTogether.invokeAndWait(
					new Runnable() {
						public void run() {
							MsAngelaTSnobbington.this.getPart(MsAngelaTSnobbington.Part.Topfin).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.01), 0.5 );
						}
					}
				);

				org.alice.virtualmachine.DoTogether.invokeAndWait(
					new Runnable() {
						public void run() {
							MsAngelaTSnobbington.this.getPart(MsAngelaTSnobbington.Part.Topfin).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.01), 0.5 );
						}
					}
				);

			}
				}
			},
			new Runnable() {
				public void run() {
					for (int index4 = 0; index4 < 2; index4 = index4 + 1) {
				//pat head
				MsAngelaTSnobbington.this.getPart(MsAngelaTSnobbington.Part.Finarmleft).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.02) );
				MsAngelaTSnobbington.this.getPart(MsAngelaTSnobbington.Part.Finarmleft).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.02) );
			}
				}
			},
			new Runnable() {
				public void run() {
					for (int index5 = 0; index5 < 2; index5 = index5 + 1) {
				//move lips
				// DoInOrder { 
					org.alice.virtualmachine.DoTogether.invokeAndWait(
						new Runnable() {
							public void run() {
								MsAngelaTSnobbington.this.getPart(MsAngelaTSnobbington.Part.Upperlip).resize( 1.1 );
							}
						},
						new Runnable() {
							public void run() {
								MsAngelaTSnobbington.this.getPart(MsAngelaTSnobbington.Part.Lowerlip).resize( 1.1 );
							}
						}
					);

					org.alice.virtualmachine.DoTogether.invokeAndWait(
						new Runnable() {
							public void run() {
								MsAngelaTSnobbington.this.getPart(MsAngelaTSnobbington.Part.Upperlip).resize( 0.9090909090909091 );
							}
						},
						new Runnable() {
							public void run() {
								MsAngelaTSnobbington.this.getPart(MsAngelaTSnobbington.Part.Lowerlip).resize( 0.9090909090909091 );
							}
						}
					);

//				}

			}
				}
			},
			new Runnable() {
				public void run() {
					// DoInOrder { 
				//bat eyes
				MsAngelaTSnobbington.this.Blink( 2.0, 0.15 );
				MsAngelaTSnobbington.this.delay(1.0);
				MsAngelaTSnobbington.this.Blink( 2.0, 0.15 );
//			}
				}
			}
		);

		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					//move arm back
				}
			},
			new Runnable() {
				public void run() {
					MsAngelaTSnobbington.this.getPart(MsAngelaTSnobbington.Part.Head).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.02) );
				}
			},
			new Runnable() {
				public void run() {
					MsAngelaTSnobbington.this.getPart(MsAngelaTSnobbington.Part.Finarmleft).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.03) );
				}
			},
			new Runnable() {
				public void run() {
					MsAngelaTSnobbington.this.getPart(MsAngelaTSnobbington.Part.Finarmleft).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.05) );
				}
			},
			new Runnable() {
				public void run() {
					MsAngelaTSnobbington.this.getPart(MsAngelaTSnobbington.Part.Finarmleft).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25) );
				}
			},
			new Runnable() {
				public void run() {
					MsAngelaTSnobbington.this.getPart(MsAngelaTSnobbington.Part.Finhandleft).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.5) );
				}
			}
		);

	}

	public void Blink( final Number Amount, final Number Speed) {
		for (int index1 = 0; index1 < Amount.intValue(); index1 = index1 + 1) {
			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						MsAngelaTSnobbington.this.getPart(MsAngelaTSnobbington.Part.Eyelidright).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.3), Speed.doubleValue() );
					}
				},
				new Runnable() {
					public void run() {
						MsAngelaTSnobbington.this.getPart(MsAngelaTSnobbington.Part.Eyelidright).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), Speed.doubleValue() );
					}
				},
				new Runnable() {
					public void run() {
						MsAngelaTSnobbington.this.getPart(MsAngelaTSnobbington.Part.Eyelidleft).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.3), Speed.doubleValue() );
					}
				},
				new Runnable() {
					public void run() {
						MsAngelaTSnobbington.this.getPart(MsAngelaTSnobbington.Part.Eyelidleft).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), Speed.doubleValue() );
					}
				}
			);

			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						MsAngelaTSnobbington.this.getPart(MsAngelaTSnobbington.Part.Eyelidright).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), Speed.doubleValue() );
					}
				},
				new Runnable() {
					public void run() {
						MsAngelaTSnobbington.this.getPart(MsAngelaTSnobbington.Part.Eyelidright).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.3), Speed.doubleValue() );
					}
				},
				new Runnable() {
					public void run() {
						MsAngelaTSnobbington.this.getPart(MsAngelaTSnobbington.Part.Eyelidleft).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), Speed.doubleValue() );
					}
				},
				new Runnable() {
					public void run() {
						MsAngelaTSnobbington.this.getPart(MsAngelaTSnobbington.Part.Eyelidleft).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.3), Speed.doubleValue() );
					}
				}
			);

		}

	}

	public void TalkToTheFin( ) {
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					for (int index1 = 0; index1 < 2; index1 = index1 + 1) {
				org.alice.virtualmachine.DoTogether.invokeAndWait(
					new Runnable() {
						public void run() {
							MsAngelaTSnobbington.this.getPart(MsAngelaTSnobbington.Part.Finarmleft).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.125), 0.5 );
						}
					},
					new Runnable() {
						public void run() {
							MsAngelaTSnobbington.this.getPart(MsAngelaTSnobbington.Part.Finarmright).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.125), 0.5 );
						}
					}
				);

				org.alice.virtualmachine.DoTogether.invokeAndWait(
					new Runnable() {
						public void run() {
							MsAngelaTSnobbington.this.getPart(MsAngelaTSnobbington.Part.Finarmleft).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.125), 0.5 );
						}
					},
					new Runnable() {
						public void run() {
							MsAngelaTSnobbington.this.getPart(MsAngelaTSnobbington.Part.Finarmright).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.125), 0.5 );
						}
					}
				);

			}
				}
			},
			new Runnable() {
				public void run() {
					// DoInOrder { 
				MsAngelaTSnobbington.this.turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.5) );
				org.alice.virtualmachine.DoTogether.invokeAndWait(
					new Runnable() {
						public void run() {
							MsAngelaTSnobbington.this.move( org.alice.apis.moveandturn.MoveDirection.FORWARD, 2.0, 1.5 );
						}
					},
					new Runnable() {
						public void run() {
							MsAngelaTSnobbington.this.turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.03) );
						}
					},
					new Runnable() {
						public void run() {
							MsAngelaTSnobbington.this.WiggleTail( 4.0 );
						}
					}
				);

//			}
				}
			}
		);

	}

	public void Wave( ) {
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					MsAngelaTSnobbington.this.getPart(MsAngelaTSnobbington.Part.Finhandleft).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.5) );
				}
			},
			new Runnable() {
				public void run() {
					MsAngelaTSnobbington.this.getPart(MsAngelaTSnobbington.Part.Finarmleft).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25) );
				}
			},
			new Runnable() {
				public void run() {
					MsAngelaTSnobbington.this.getPart(MsAngelaTSnobbington.Part.Finarmleft).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.03) );
				}
			}
		);

		for (int index1 = 0; index1 < 2; index1 = index1 + 1) {
			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						MsAngelaTSnobbington.this.getPart(MsAngelaTSnobbington.Part.Finarmleft).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.05) );
					}
				},
				new Runnable() {
					public void run() {
						MsAngelaTSnobbington.this.getPart(MsAngelaTSnobbington.Part.Finhandleft).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15) );
					}
				}
			);

			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						MsAngelaTSnobbington.this.getPart(MsAngelaTSnobbington.Part.Finarmleft).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.05) );
					}
				},
				new Runnable() {
					public void run() {
						MsAngelaTSnobbington.this.getPart(MsAngelaTSnobbington.Part.Finhandleft).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15) );
					}
				}
			);

		}

		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					MsAngelaTSnobbington.this.getPart(MsAngelaTSnobbington.Part.Finarmleft).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.05) );
				}
			},
			new Runnable() {
				public void run() {
					MsAngelaTSnobbington.this.getPart(MsAngelaTSnobbington.Part.Finhandleft).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.5) );
				}
			},
			new Runnable() {
				public void run() {
					MsAngelaTSnobbington.this.getPart(MsAngelaTSnobbington.Part.Finarmleft).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25) );
				}
			},
			new Runnable() {
				public void run() {
					MsAngelaTSnobbington.this.getPart(MsAngelaTSnobbington.Part.Finarmleft).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.03) );
				}
			}
		);

	}
}
