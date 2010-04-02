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

public class ActiveAli extends edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.GalleryCharacter { 
	public ActiveAli() {
		super( "undersea/Active Ali" );
}
	public enum Part {
		Finhandleft( "body", "finarmleft", "finhandleft" ),
		Finarmleft( "body", "finarmleft" ),
		Finhandlright( "body", "finarmright", "finhandlright" ),
		Finarmright( "body", "finarmright" ),
		Eyeright( "body", "neck3", "neck2", "neck1", "head", "eyeright" ),
		Eyeleft( "body", "neck3", "neck2", "neck1", "head", "eyeleft" ),
		Jaw( "body", "neck3", "neck2", "neck1", "head", "jaw" ),
		Visor( "body", "neck3", "neck2", "neck1", "head", "visor" ),
		Head( "body", "neck3", "neck2", "neck1", "head" ),
		Neck1( "body", "neck3", "neck2", "neck1" ),
		Neck2( "body", "neck3", "neck2" ),
		Neck3( "body", "neck3" ),
		Buttfin( "body", "buttfin" ),
		Body( "body" ),
		Soccerball( "soccerball" ),
		Tail9( "tail1", "tail2", "tail3", "tail4", "tail5", "tail6", "tail7", "tail8", "tail9" ),
		Tail8( "tail1", "tail2", "tail3", "tail4", "tail5", "tail6", "tail7", "tail8" ),
		Tail7( "tail1", "tail2", "tail3", "tail4", "tail5", "tail6", "tail7" ),
		Tail6( "tail1", "tail2", "tail3", "tail4", "tail5", "tail6" ),
		Tail5( "tail1", "tail2", "tail3", "tail4", "tail5" ),
		Tail4( "tail1", "tail2", "tail3", "tail4" ),
		Tail3( "tail1", "tail2", "tail3" ),
		Tail2( "tail1", "tail2" ),
		Tail1( "tail1" );
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

	public void Swim( final Number Distance) {
		 final edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Double> Speed= new edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Double>();  Speed.value = new Double(0.5);;
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					ActiveAli.this.getPart(ActiveAli.Part.Tail1).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15) );
				}
			},
			new Runnable() {
				public void run() {
					ActiveAli.this.getPart(ActiveAli.Part.Tail2).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1) );
				}
			},
			new Runnable() {
				public void run() {
					ActiveAli.this.getPart(ActiveAli.Part.Tail8).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1) );
				}
			}
		);

		this.getPart(ActiveAli.Part.Finarmleft).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), Speed.value );
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					ActiveAli.this.move( org.alice.apis.moveandturn.MoveDirection.FORWARD, Distance.doubleValue(), Distance.doubleValue() );
				}
			},
			new Runnable() {
				public void run() {
					for (int index1 = 0; index1 < Distance.intValue(); index1 = index1 + 1) {
				org.alice.virtualmachine.DoTogether.invokeAndWait(
					new Runnable() {
						public void run() {
							ActiveAli.this.getPart(ActiveAli.Part.Finarmleft).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), Speed.value );
						}
					},
					new Runnable() {
						public void run() {
							ActiveAli.this.getPart(ActiveAli.Part.Finarmright).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), Speed.value );
						}
					},
					new Runnable() {
						public void run() {
							ActiveAli.this.getPart(ActiveAli.Part.Tail2).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(2.0), Speed.value );
						}
					}
				);

				org.alice.virtualmachine.DoTogether.invokeAndWait(
					new Runnable() {
						public void run() {
							ActiveAli.this.getPart(ActiveAli.Part.Finarmleft).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), Speed.value );
						}
					},
					new Runnable() {
						public void run() {
							ActiveAli.this.getPart(ActiveAli.Part.Finarmright).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), Speed.value );
						}
					},
					new Runnable() {
						public void run() {
							ActiveAli.this.getPart(ActiveAli.Part.Tail2).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(2.0), Speed.value );
						}
					}
				);

			}
				}
			}
		);

		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					ActiveAli.this.getPart(ActiveAli.Part.Finarmleft).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), Speed.value );
				}
			},
			new Runnable() {
				public void run() {
					ActiveAli.this.getPart(ActiveAli.Part.Tail1).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15) );
				}
			},
			new Runnable() {
				public void run() {
					ActiveAli.this.getPart(ActiveAli.Part.Tail2).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1) );
				}
			},
			new Runnable() {
				public void run() {
					ActiveAli.this.getPart(ActiveAli.Part.Tail8).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1) );
				}
			}
		);

	}

	public void BumpInto( final edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel Who) {
		this.moveTo( Who, edu.wustl.cse.lookingglass.apis.walkandtouch.SpatialRelation.IN_FRONT_OF, -0.25 );
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					ActiveAli.this.getPart(ActiveAli.Part.Finarmleft).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					ActiveAli.this.getPart(ActiveAli.Part.Finarmright).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					ActiveAli.this.getPart(ActiveAli.Part.Neck3).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					ActiveAli.this.getPart(ActiveAli.Part.Tail1).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					ActiveAli.this.getPart(ActiveAli.Part.Tail2).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					ActiveAli.this.getPart(ActiveAli.Part.Tail2).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					ActiveAli.this.move( org.alice.apis.moveandturn.MoveDirection.BACKWARD, 0.5 );
				}
			}
		);

		this.delay(1.0);
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					ActiveAli.this.getPart(ActiveAli.Part.Finarmleft).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					ActiveAli.this.getPart(ActiveAli.Part.Finarmright).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					ActiveAli.this.getPart(ActiveAli.Part.Neck3).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					ActiveAli.this.getPart(ActiveAli.Part.Tail1).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					ActiveAli.this.getPart(ActiveAli.Part.Tail2).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					ActiveAli.this.getPart(ActiveAli.Part.Tail2).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.5 );
				}
			}
		);

	}

	public void Hug( final edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel Who) {
		this.moveTo( Who, edu.wustl.cse.lookingglass.apis.walkandtouch.SpatialRelation.IN_FRONT_OF );
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					ActiveAli.this.getPart(ActiveAli.Part.Finarmleft).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					ActiveAli.this.getPart(ActiveAli.Part.Finarmleft).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					ActiveAli.this.getPart(ActiveAli.Part.Finarmright).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					ActiveAli.this.getPart(ActiveAli.Part.Finarmright).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
				}
			}
		);

		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					ActiveAli.this.getPart(ActiveAli.Part.Finhandleft).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1) );
				}
			},
			new Runnable() {
				public void run() {
					ActiveAli.this.getPart(ActiveAli.Part.Finhandlright).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1) );
				}
			}
		);

		this.delay(1.0);
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					ActiveAli.this.getPart(ActiveAli.Part.Finhandleft).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1) );
				}
			},
			new Runnable() {
				public void run() {
					ActiveAli.this.getPart(ActiveAli.Part.Finhandlright).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1) );
				}
			}
		);

		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					ActiveAli.this.getPart(ActiveAli.Part.Finarmleft).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					ActiveAli.this.getPart(ActiveAli.Part.Finarmleft).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					ActiveAli.this.getPart(ActiveAli.Part.Finarmright).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					ActiveAli.this.getPart(ActiveAli.Part.Finarmright).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
				}
			}
		);

	}

	public void Shove( final edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel Who) {
		this.moveTo( Who, edu.wustl.cse.lookingglass.apis.walkandtouch.SpatialRelation.IN_FRONT_OF );
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					ActiveAli.this.getPart(ActiveAli.Part.Finarmleft).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					ActiveAli.this.getPart(ActiveAli.Part.Finarmright).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					ActiveAli.this.getPart(ActiveAli.Part.Finhandleft).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					ActiveAli.this.getPart(ActiveAli.Part.Finhandlright).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					ActiveAli.this.getPart(ActiveAli.Part.Tail2).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.5 );
				}
			}
		);

		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					ActiveAli.this.getPart(ActiveAli.Part.Finhandleft).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.15 );
				}
			},
			new Runnable() {
				public void run() {
					ActiveAli.this.getPart(ActiveAli.Part.Finhandlright).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.15 );
				}
			},
			new Runnable() {
				public void run() {
					ActiveAli.this.getPart(ActiveAli.Part.Tail6).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.15 );
				}
			},
			new Runnable() {
				public void run() {
					ActiveAli.this.getPart(ActiveAli.Part.Tail7).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.15 );
				}
			},
			new Runnable() {
				public void run() {
					ActiveAli.this.getPart(ActiveAli.Part.Tail8).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.15 );
				}
			},
			new Runnable() {
				public void run() {
					Who.move( org.alice.apis.moveandturn.MoveDirection.BACKWARD, 1.0, 0.4 );
				}
			}
		);

		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					ActiveAli.this.getPart(ActiveAli.Part.Finarmleft).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					ActiveAli.this.getPart(ActiveAli.Part.Finarmright).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					ActiveAli.this.getPart(ActiveAli.Part.Finhandleft).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					ActiveAli.this.getPart(ActiveAli.Part.Finhandlright).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					ActiveAli.this.getPart(ActiveAli.Part.Tail2).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					ActiveAli.this.getPart(ActiveAli.Part.Tail6).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					ActiveAli.this.getPart(ActiveAli.Part.Tail7).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					ActiveAli.this.getPart(ActiveAli.Part.Tail8).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.25 );
				}
			}
		);

	}
}
