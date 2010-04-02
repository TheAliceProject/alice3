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

import edu.cmu.cs.dennisc.print.PrintUtilities;

public class Sammy extends edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.GalleryCharacter { 
	edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Boolean> InsideShell= new edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Boolean>(Boolean.FALSE);  
	public Sammy() {
		super( "undersea/Sammy" );
		edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Boolean> InsideShell= new edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Boolean>(Boolean.FALSE);  
}
	public enum Part {
		Eyelidright( "body2", "body1", "head", "eyeright", "eyelidright" ),
		Eyeright( "body2", "body1", "head", "eyeright" ),
		Eyelidleft( "body2", "body1", "head", "eyeleft", "eyelidleft" ),
		Eyeleft( "body2", "body1", "head", "eyeleft" ),
		Glasses( "body2", "body1", "head", "glasses" ),
		Jaw( "body2", "body1", "head", "jaw" ),
		Antenneatopright( "body2", "body1", "head", "antennae1right", "antennae2right", "antennea3right", "antennae4right", "antenneatopright" ),
		Antennae4right( "body2", "body1", "head", "antennae1right", "antennae2right", "antennea3right", "antennae4right" ),
		Antennea3right( "body2", "body1", "head", "antennae1right", "antennae2right", "antennea3right" ),
		Antennae2right( "body2", "body1", "head", "antennae1right", "antennae2right" ),
		Antennae1right( "body2", "body1", "head", "antennae1right" ),
		Antenneatopleft( "body2", "body1", "head", "antennae1left", "antennae2left", "antennea3left", "antennae4left", "antenneatopleft" ),
		Antennae4left( "body2", "body1", "head", "antennae1left", "antennae2left", "antennea3left", "antennae4left" ),
		Antennea3left( "body2", "body1", "head", "antennae1left", "antennae2left", "antennea3left" ),
		Antennae2left( "body2", "body1", "head", "antennae1left", "antennae2left" ),
		Antennae1left( "body2", "body1", "head", "antennae1left" ),
		Head( "body2", "body1", "head" ),
		Body1( "body2", "body1" ),
		Body2( "body2" ),
		Shelltop2( "shelltop1", "shelltop2" ),
		Shelltop1( "shelltop1" ),
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

	public void Crawl( final Number Amount) {
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					for (int index3 = 0; index3 < (Amount.doubleValue() / 0.15); index3 = index3 + 1) {
				Sammy.this.getPart(Sammy.Part.Body2).move( org.alice.apis.moveandturn.MoveDirection.FORWARD, 0.15 );
				org.alice.virtualmachine.DoTogether.invokeAndWait(
					new Runnable() {
						public void run() {
							Sammy.this.move( org.alice.apis.moveandturn.MoveDirection.FORWARD, 0.15 );
						}
					},
					new Runnable() {
						public void run() {
							Sammy.this.getPart(Sammy.Part.Body2).move( org.alice.apis.moveandturn.MoveDirection.BACKWARD, 0.15 );
						}
					},
					new Runnable() {
						public void run() {
							Sammy.this.getPart(Sammy.Part.Tail).move( org.alice.apis.moveandturn.MoveDirection.BACKWARD, 0.15 );
						}
					}
				);

				Sammy.this.getPart(Sammy.Part.Tail).move( org.alice.apis.moveandturn.MoveDirection.FORWARD, 0.15 );
			}
				}
			},
			new Runnable() {
				public void run() {
					for (int index4 = 0; index4 < (Amount.doubleValue() / 0.3); index4 = index4 + 1) {
				org.alice.virtualmachine.DoTogether.invokeAndWait(
					new Runnable() {
						public void run() {
							Sammy.this.getPart(Sammy.Part.Body1).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.01), 3.0 );
						}
					},
					new Runnable() {
						public void run() {
							Sammy.this.getPart(Sammy.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.03), 3.0 );
						}
					}
				);

				org.alice.virtualmachine.DoTogether.invokeAndWait(
					new Runnable() {
						public void run() {
							Sammy.this.getPart(Sammy.Part.Body1).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.01), 3.0 );
						}
					},
					new Runnable() {
						public void run() {
							Sammy.this.getPart(Sammy.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.03), 3.0 );
						}
					}
				);

			}
				}
			},
			new Runnable() {
				public void run() {
					for (int index5 = 0; index5 < (Amount.doubleValue() / 0.1); index5 = index5 + 1) {
				Sammy.this.WiggleAntennae( 1.0 );
			}
				}
			}
		);

	}

	public void WiggleAntennae( final Number Speed) {
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					Sammy.this.getPart(Sammy.Part.Antennae1right).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(1.0), Speed.doubleValue() );
				}
			},
			new Runnable() {
				public void run() {
					Sammy.this.getPart(Sammy.Part.Antennae2right).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.11), Speed.doubleValue() );
				}
			},
			new Runnable() {
				public void run() {
					Sammy.this.getPart(Sammy.Part.Antennea3right).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), Speed.doubleValue() );
				}
			},
			new Runnable() {
				public void run() {
					Sammy.this.getPart(Sammy.Part.Antennae4right).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.11), Speed.doubleValue() );
				}
			},
			new Runnable() {
				public void run() {
					Sammy.this.getPart(Sammy.Part.Antennae1left).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(1.0), Speed.doubleValue() );
				}
			},
			new Runnable() {
				public void run() {
					Sammy.this.getPart(Sammy.Part.Antennae2left).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), Speed.doubleValue() );
				}
			},
			new Runnable() {
				public void run() {
					Sammy.this.getPart(Sammy.Part.Antennea3left).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.11), Speed.doubleValue() );
				}
			},
			new Runnable() {
				public void run() {
					Sammy.this.getPart(Sammy.Part.Antennae4left).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), Speed.doubleValue() );
				}
			}
		);

		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					Sammy.this.getPart(Sammy.Part.Antennae1right).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(1.0), Speed.doubleValue() );
				}
			},
			new Runnable() {
				public void run() {
					Sammy.this.getPart(Sammy.Part.Antennae2right).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.11), Speed.doubleValue() );
				}
			},
			new Runnable() {
				public void run() {
					Sammy.this.getPart(Sammy.Part.Antennea3right).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), Speed.doubleValue() );
				}
			},
			new Runnable() {
				public void run() {
					Sammy.this.getPart(Sammy.Part.Antennae4right).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.11), Speed.doubleValue() );
				}
			},
			new Runnable() {
				public void run() {
					Sammy.this.getPart(Sammy.Part.Antennae1left).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(1.0), Speed.doubleValue() );
				}
			},
			new Runnable() {
				public void run() {
					Sammy.this.getPart(Sammy.Part.Antennae2left).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), Speed.doubleValue() );
				}
			},
			new Runnable() {
				public void run() {
					Sammy.this.getPart(Sammy.Part.Antennea3left).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.11), Speed.doubleValue() );
				}
			},
			new Runnable() {
				public void run() {
					Sammy.this.getPart(Sammy.Part.Antennae4left).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), Speed.doubleValue() );
				}
			}
		);

	}

	public void Hide( ) {
		if (InsideShell.value ) {		} else { 
			InsideShell.value = true;
			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						//pull tail in
					}
				},
				new Runnable() {
					public void run() {
						Sammy.this.getPart(Sammy.Part.Tail).move( org.alice.apis.moveandturn.MoveDirection.FORWARD, 0.8 );
					}
				},
				new Runnable() {
					public void run() {
						//pull head in
					}
				},
				new Runnable() {
					public void run() {
						Sammy.this.getPart(Sammy.Part.Body2).resize( 0.8 );
					}
				},
				new Runnable() {
					public void run() {
						Sammy.this.getPart(Sammy.Part.Body2).move( org.alice.apis.moveandturn.MoveDirection.BACKWARD, 1.0 );
					}
				},
				new Runnable() {
					public void run() {
						Sammy.this.getPart(Sammy.Part.Body1).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1) );
					}
				},
				new Runnable() {
					public void run() {
						//fold antenna down
					}
				},
				new Runnable() {
					public void run() {
						Sammy.this.getPart(Sammy.Part.Antennae1right).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.25 );
					}
				},
				new Runnable() {
					public void run() {
						Sammy.this.getPart(Sammy.Part.Antennae2right).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.11), 0.25 );
					}
				},
				new Runnable() {
					public void run() {
						Sammy.this.getPart(Sammy.Part.Antennea3right).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.11), 0.25 );
					}
				},
				new Runnable() {
					public void run() {
						Sammy.this.getPart(Sammy.Part.Antennae4right).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.11), 0.25 );
					}
				},
				new Runnable() {
					public void run() {
						Sammy.this.getPart(Sammy.Part.Antennae1left).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.25 );
					}
				},
				new Runnable() {
					public void run() {
						Sammy.this.getPart(Sammy.Part.Antennae2left).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.11), 0.25 );
					}
				},
				new Runnable() {
					public void run() {
						Sammy.this.getPart(Sammy.Part.Antennea3left).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.11), 0.25 );
					}
				},
				new Runnable() {
					public void run() {
						Sammy.this.getPart(Sammy.Part.Antennae4left).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.11), 0.25 );
					}
				}
			);

		}

	}

	public void ComeOutOfShell( ) {
		if (InsideShell.value ) {			InsideShell.value = false;
			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						//push tail out
					}
				},
				new Runnable() {
					public void run() {
						Sammy.this.getPart(Sammy.Part.Tail).move( org.alice.apis.moveandturn.MoveDirection.BACKWARD, 0.8 );
					}
				},
				new Runnable() {
					public void run() {
						//push head out
					}
				},
				new Runnable() {
					public void run() {
						Sammy.this.getPart(Sammy.Part.Body2).resize( 1.25 );
					}
				},
				new Runnable() {
					public void run() {
						Sammy.this.getPart(Sammy.Part.Body2).move( org.alice.apis.moveandturn.MoveDirection.FORWARD, 1.0 );
					}
				},
				new Runnable() {
					public void run() {
						Sammy.this.getPart(Sammy.Part.Body1).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1) );
					}
				},
				new Runnable() {
					public void run() {
						//fold antenna down
					}
				},
				new Runnable() {
					public void run() {
						Sammy.this.getPart(Sammy.Part.Antennae1right).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.25 );
					}
				},
				new Runnable() {
					public void run() {
						Sammy.this.getPart(Sammy.Part.Antennae2right).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.11), 0.25 );
					}
				},
				new Runnable() {
					public void run() {
						Sammy.this.getPart(Sammy.Part.Antennea3right).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.11), 0.25 );
					}
				},
				new Runnable() {
					public void run() {
						Sammy.this.getPart(Sammy.Part.Antennae4right).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.11), 0.25 );
					}
				},
				new Runnable() {
					public void run() {
						Sammy.this.getPart(Sammy.Part.Antennae1left).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.25 );
					}
				},
				new Runnable() {
					public void run() {
						Sammy.this.getPart(Sammy.Part.Antennae2left).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.11), 0.25 );
					}
				},
				new Runnable() {
					public void run() {
						Sammy.this.getPart(Sammy.Part.Antennea3left).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.11), 0.25 );
					}
				},
				new Runnable() {
					public void run() {
						Sammy.this.getPart(Sammy.Part.Antennae4left).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.11), 0.25 );
					}
				}
			);

		} else { 
		}

	}

	public void LookEmbarassed( ) {
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					Sammy.this.getPart(Sammy.Part.Head).setColor( new org.alice.apis.moveandturn.Color(1.0f, 0.0f, 0.0f) );
				}
			},
			new Runnable() {
				public void run() {
					//fold antenna down
				}
			},
			new Runnable() {
				public void run() {
					Sammy.this.getPart(Sammy.Part.Antennae1right).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					Sammy.this.getPart(Sammy.Part.Antennae2right).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.11), 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					Sammy.this.getPart(Sammy.Part.Antennea3right).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.11), 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					Sammy.this.getPart(Sammy.Part.Antennae4right).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.11), 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					Sammy.this.getPart(Sammy.Part.Antennae1left).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					Sammy.this.getPart(Sammy.Part.Antennae2left).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.11), 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					Sammy.this.getPart(Sammy.Part.Antennea3left).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.11), 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					Sammy.this.getPart(Sammy.Part.Antennae4left).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.11), 0.25 );
				}
			}
		);

		this.delay(1.0);
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					Sammy.this.getPart(Sammy.Part.Head).setColor( new org.alice.apis.moveandturn.Color(1.0f, 1.0f, 1.0f) );
				}
			},
			new Runnable() {
				public void run() {
					//fold antenna down
				}
			},
			new Runnable() {
				public void run() {
					Sammy.this.getPart(Sammy.Part.Antennae1right).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					Sammy.this.getPart(Sammy.Part.Antennae2right).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.11), 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					Sammy.this.getPart(Sammy.Part.Antennea3right).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.11), 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					Sammy.this.getPart(Sammy.Part.Antennae4right).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.11), 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					Sammy.this.getPart(Sammy.Part.Antennae1left).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					Sammy.this.getPart(Sammy.Part.Antennae2left).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.11), 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					Sammy.this.getPart(Sammy.Part.Antennea3left).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.11), 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					Sammy.this.getPart(Sammy.Part.Antennae4left).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.11), 0.25 );
				}
			}
		);

	}
}
