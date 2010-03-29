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
package edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.scenes.factory;

public class FactoryMachine extends edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.GalleryModel { 
	edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Boolean> On= new edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Boolean>();  
	public FactoryMachine() {
		super( "factory/FactoryMachine" );
		edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Boolean> On= new edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Boolean>();  
}
	public enum Part {
		Conveyorbelt( "conveyorbelt" ),
		Beamsupport_Beam_Poundingarm_Poundingplate( "beamsupport", "beam", "poundingarm", "poundingplate" ),
		Beamsupport_Beam_Poundingarm( "beamsupport", "beam", "poundingarm" ),
		Beamsupport_Beam_Poundingarm01_Poundingplate01( "beamsupport", "beam", "poundingarm01", "poundingplate01" ),
		Beamsupport_Beam_Poundingarm01( "beamsupport", "beam", "poundingarm01" ),
		Beamsupport_Beam_Poundingarm02_Poundingplate02( "beamsupport", "beam", "poundingarm02", "poundingplate02" ),
		Beamsupport_Beam_Poundingarm02( "beamsupport", "beam", "poundingarm02" ),
		Beamsupport_Beam( "beamsupport", "beam" ),
		Beamsupport( "beamsupport" ),
		Machineblock_LaserORdrill( "machineblock", "laserORdrill" ),
		Machineblock( "machineblock" );
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

	public void Break( ) {
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					// DoInOrder { 
				org.alice.virtualmachine.DoTogether.invokeAndWait(
					new Runnable() {
						public void run() {
							FactoryMachine.this.getPart(FactoryMachine.Part.Beamsupport_Beam_Poundingarm_Poundingplate).move( org.alice.apis.moveandturn.MoveDirection.RIGHT, 3.0, 1.0, FactoryMachine.this );
						}
					},
					new Runnable() {
						public void run() {
							FactoryMachine.this.getPart(FactoryMachine.Part.Beamsupport_Beam_Poundingarm_Poundingplate).move( org.alice.apis.moveandturn.MoveDirection.DOWN, 2.0, 1.0, FactoryMachine.this );
						}
					},
					new Runnable() {
						public void run() {
							FactoryMachine.this.getPart(FactoryMachine.Part.Beamsupport_Beam_Poundingarm_Poundingplate).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(2.0) );
						}
					}
				);

				org.alice.virtualmachine.DoTogether.invokeAndWait(
					new Runnable() {
						public void run() {
							FactoryMachine.this.getPart(FactoryMachine.Part.Beamsupport_Beam_Poundingarm01_Poundingplate01).move( org.alice.apis.moveandturn.MoveDirection.LEFT, 3.0, 1.0, FactoryMachine.this );
						}
					},
					new Runnable() {
						public void run() {
							FactoryMachine.this.getPart(FactoryMachine.Part.Beamsupport_Beam_Poundingarm01_Poundingplate01).move( org.alice.apis.moveandturn.MoveDirection.DOWN, 2.0, 1.0, FactoryMachine.this );
						}
					},
					new Runnable() {
						public void run() {
							FactoryMachine.this.getPart(FactoryMachine.Part.Beamsupport_Beam_Poundingarm01_Poundingplate01).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(2.0) );
						}
					}
				);

				org.alice.virtualmachine.DoTogether.invokeAndWait(
					new Runnable() {
						public void run() {
							FactoryMachine.this.getPart(FactoryMachine.Part.Beamsupport_Beam_Poundingarm02_Poundingplate02).move( org.alice.apis.moveandturn.MoveDirection.RIGHT, 3.0, 1.0, FactoryMachine.this );
						}
					},
					new Runnable() {
						public void run() {
							FactoryMachine.this.getPart(FactoryMachine.Part.Beamsupport_Beam_Poundingarm02_Poundingplate02).move( org.alice.apis.moveandturn.MoveDirection.DOWN, 2.0, 1.0, FactoryMachine.this );
						}
					},
					new Runnable() {
						public void run() {
							FactoryMachine.this.getPart(FactoryMachine.Part.Beamsupport_Beam_Poundingarm02_Poundingplate02).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(2.0) );
						}
					}
				);

//			}
				}
			},
			new Runnable() {
				public void run() {
					// DoInOrder { 
				org.alice.virtualmachine.DoTogether.invokeAndWait(
					new Runnable() {
						public void run() {
							FactoryMachine.this.move( org.alice.apis.moveandturn.MoveDirection.LEFT, 0.01, 0.25 );
						}
					},
					new Runnable() {
						public void run() {
							FactoryMachine.this.move( org.alice.apis.moveandturn.MoveDirection.UP, 0.01, 0.25 );
						}
					}
				);

				for (int index1 = 0; index1 < 11; index1 = index1 + 1) {
					org.alice.virtualmachine.DoTogether.invokeAndWait(
						new Runnable() {
							public void run() {
								FactoryMachine.this.move( org.alice.apis.moveandturn.MoveDirection.RIGHT, 0.02, 0.15 );
							}
						},
						new Runnable() {
							public void run() {
								FactoryMachine.this.move( org.alice.apis.moveandturn.MoveDirection.DOWN, 0.01, 0.15 );
							}
						}
					);

					org.alice.virtualmachine.DoTogether.invokeAndWait(
						new Runnable() {
							public void run() {
								FactoryMachine.this.move( org.alice.apis.moveandturn.MoveDirection.LEFT, 0.01, 0.15 );
							}
						},
						new Runnable() {
							public void run() {
								FactoryMachine.this.move( org.alice.apis.moveandturn.MoveDirection.UP, 0.01, 0.15 );
							}
						}
					);

				}

//			}
				}
			}
		);

	}

	public void Run( final Number HowManySeconds) {
		for (int index2 = 0; index2 < HowManySeconds.intValue(); index2 = index2 + 1) {
			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						// DoInOrder { 
					FactoryMachine.this.getPart(FactoryMachine.Part.Beamsupport_Beam_Poundingarm).move( org.alice.apis.moveandturn.MoveDirection.DOWN, 1.1, 0.25 );
					FactoryMachine.this.getPart(FactoryMachine.Part.Beamsupport_Beam_Poundingarm).move( org.alice.apis.moveandturn.MoveDirection.UP, 1.1 );
//				}
					}
				},
				new Runnable() {
					public void run() {
						// DoInOrder { 
					FactoryMachine.this.delay(1.0);
					FactoryMachine.this.getPart(FactoryMachine.Part.Beamsupport_Beam_Poundingarm01).move( org.alice.apis.moveandturn.MoveDirection.DOWN, 1.1, 0.25 );
					FactoryMachine.this.getPart(FactoryMachine.Part.Beamsupport_Beam_Poundingarm01).move( org.alice.apis.moveandturn.MoveDirection.UP, 1.1 );
//				}
					}
				},
				new Runnable() {
					public void run() {
						// DoInOrder { 
					FactoryMachine.this.delay(2.0);
					FactoryMachine.this.getPart(FactoryMachine.Part.Beamsupport_Beam_Poundingarm02).move( org.alice.apis.moveandturn.MoveDirection.DOWN, 1.1, 0.25 );
					FactoryMachine.this.getPart(FactoryMachine.Part.Beamsupport_Beam_Poundingarm02).move( org.alice.apis.moveandturn.MoveDirection.UP, 1.1 );
//				}
					}
				},
				new Runnable() {
					public void run() {
						// DoInOrder { 
					org.alice.virtualmachine.DoTogether.invokeAndWait(
						new Runnable() {
							public void run() {
								FactoryMachine.this.getPart(FactoryMachine.Part.Machineblock_LaserORdrill).move( org.alice.apis.moveandturn.MoveDirection.DOWN, 0.5 );
							}
						},
						new Runnable() {
							public void run() {
								FactoryMachine.this.getPart(FactoryMachine.Part.Machineblock_LaserORdrill).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(1.0) );
							}
						}
					);

					org.alice.virtualmachine.DoTogether.invokeAndWait(
						new Runnable() {
							public void run() {
								FactoryMachine.this.getPart(FactoryMachine.Part.Machineblock_LaserORdrill).move( org.alice.apis.moveandturn.MoveDirection.UP, 0.5 );
							}
						},
						new Runnable() {
							public void run() {
								FactoryMachine.this.getPart(FactoryMachine.Part.Machineblock_LaserORdrill).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(1.0) );
							}
						}
					);

//				}
					}
				},
				new Runnable() {
					public void run() {
						for (int index3 = 0; index3 < 4; index3 = index3 + 1) {
					// DoInOrder { 
						//FactoryMachine.this.getPart(FactoryMachine.Part.Conveyorbelt).setSurfaceTexture(value);
						//FactoryMachine.this.getPart(FactoryMachine.Part.Conveyorbelt).setSurfaceTexture(value);
//					}

				}
					}
				}
			);

		}

	}

	public void Method( ) {
	}
}
