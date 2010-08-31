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

public class DopeyDon extends edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.GalleryCharacter { 
	private final edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Boolean> Inflated= new edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Boolean>( false );  
	public DopeyDon() {
		super( "undersea/Dopey Don" );
	}
	public enum Part {
		Upperlip( "body", "upperlip" ),
		Lowerlip( "body", "lowerlip" ),
		Cheekleft( "body", "cheekleft" ),
		Cheekright( "body", "cheekright" ),
		Tail( "body", "tail" ),
		Finhandright( "body", "finarmright", "finhandright" ),
		Finarmright( "body", "finarmright" ),
		Finhandleft( "body", "finarmleft", "finhandleft" ),
		Finarmleft( "body", "finarmleft" ),
		Eyelidleft( "body", "eyeleft", "eyelidleft" ),
		Eyeleft( "body", "eyeleft" ),
		Eyelidright( "body", "eyeright", "eyelidright" ),
		Eyeright( "body", "eyeright" ),
		Spike1( "body", "spike1" ),
		Spike02( "body", "spike02" ),
		Spike03( "body", "spike03" ),
		Spike04( "body", "spike04" ),
		Spike05( "body", "spike05" ),
		Spike06( "body", "spike06" ),
		Spike07( "body", "spike07" ),
		Spike08( "body", "spike08" ),
		Spike09( "body", "spike09" ),
		Spike10( "body", "spike10" ),
		Spike11( "body", "spike11" ),
		Spike12( "body", "spike12" ),
		Spike13( "body", "spike13" ),
		Spike14( "body", "spike14" ),
		Spike15( "body", "spike15" ),
		Spike16( "body", "spike16" ),
		Spike17( "body", "spike17" ),
		Spike18( "body", "spike18" ),
		Spike19( "body", "spike19" ),
		Spike20( "body", "spike20" ),
		Spike21( "body", "spike21" ),
		Spike22( "body", "spike22" ),
		Spike23( "body", "spike23" ),
		Spike24( "body", "spike24" ),
		Body( "body" );
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

	public void BlowUp( ) {
		 final edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Double> Speed= new edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Double>( 1.0 );
		if (Inflated.value ) {		} else { 
			Inflated.value = true;
			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						// DoInOrder { 
					org.alice.virtualmachine.DoTogether.invokeAndWait(
						new Runnable() {
							public void run() {
								DopeyDon.this.getPart(DopeyDon.Part.Upperlip).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), (Speed.value * 0.25) );
							}
						},
						new Runnable() {
							public void run() {
								DopeyDon.this.getPart(DopeyDon.Part.Lowerlip).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), (Speed.value * 0.25) );
							}
						}
					);

					org.alice.virtualmachine.DoTogether.invokeAndWait(
						new Runnable() {
							public void run() {
								DopeyDon.this.getPart(DopeyDon.Part.Upperlip).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), (Speed.value * 0.25) );
							}
						},
						new Runnable() {
							public void run() {
								DopeyDon.this.getPart(DopeyDon.Part.Lowerlip).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), (Speed.value * 0.25) );
							}
						}
					);

//				}
					}
				},
				new Runnable() {
					public void run() {
						DopeyDon.this.getPart(DopeyDon.Part.Body).resize( 2.0, Speed.value );
					}
				},
				new Runnable() {
					public void run() {
						DopeyDon.this.getPart(DopeyDon.Part.Eyeleft).resize( 0.5, Speed.value );
					}
				},
				new Runnable() {
					public void run() {
						DopeyDon.this.getPart(DopeyDon.Part.Eyeright).resize( 0.5, Speed.value );
					}
				},
				new Runnable() {
					public void run() {
						DopeyDon.this.getPart(DopeyDon.Part.Cheekleft).resize( 2.0, Speed.value );
					}
				},
				new Runnable() {
					public void run() {
						DopeyDon.this.getPart(DopeyDon.Part.Cheekright).resize( 2.0, Speed.value );
					}
				},
				new Runnable() {
					public void run() {
						DopeyDon.this.getPart(DopeyDon.Part.Finarmright).move( org.alice.apis.moveandturn.MoveDirection.LEFT, 0.5, Speed.value );
					}
				},
				new Runnable() {
					public void run() {
						DopeyDon.this.getPart(DopeyDon.Part.Finarmleft).move( org.alice.apis.moveandturn.MoveDirection.RIGHT, 0.5, Speed.value );
					}
				}
			);

		}

	}

	public void Deflate( ) {
		 final edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Double> Speed= new edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Double>( 1.0 );
		if (Inflated.value ) {			Inflated.value = false;
			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						// DoInOrder { 
					org.alice.virtualmachine.DoTogether.invokeAndWait(
						new Runnable() {
							public void run() {
								DopeyDon.this.getPart(DopeyDon.Part.Upperlip).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), (Speed.value * 0.5) );
							}
						},
						new Runnable() {
							public void run() {
								DopeyDon.this.getPart(DopeyDon.Part.Lowerlip).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), (Speed.value * 0.5) );
							}
						}
					);

					org.alice.virtualmachine.DoTogether.invokeAndWait(
						new Runnable() {
							public void run() {
								DopeyDon.this.getPart(DopeyDon.Part.Upperlip).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), (Speed.value * 0.5) );
							}
						},
						new Runnable() {
							public void run() {
								DopeyDon.this.getPart(DopeyDon.Part.Lowerlip).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), (Speed.value * 0.5) );
							}
						}
					);

//				}
					}
				},
				new Runnable() {
					public void run() {
						DopeyDon.this.getPart(DopeyDon.Part.Body).resize( 0.5, Speed.value );
					}
				},
				new Runnable() {
					public void run() {
						DopeyDon.this.getPart(DopeyDon.Part.Eyeleft).resize( 2.0, Speed.value );
					}
				},
				new Runnable() {
					public void run() {
						DopeyDon.this.getPart(DopeyDon.Part.Eyeright).resize( 2.0, Speed.value );
					}
				},
				new Runnable() {
					public void run() {
						DopeyDon.this.getPart(DopeyDon.Part.Cheekleft).resize( 0.5, Speed.value );
					}
				},
				new Runnable() {
					public void run() {
						DopeyDon.this.getPart(DopeyDon.Part.Cheekright).resize( 0.5, Speed.value );
					}
				},
				new Runnable() {
					public void run() {
						DopeyDon.this.getPart(DopeyDon.Part.Finarmright).move( org.alice.apis.moveandturn.MoveDirection.RIGHT, 0.5, Speed.value );
					}
				},
				new Runnable() {
					public void run() {
						DopeyDon.this.getPart(DopeyDon.Part.Finarmleft).move( org.alice.apis.moveandturn.MoveDirection.LEFT, 0.5, Speed.value );
					}
				}
			);

		} else { 
		}

	}

	public void Collide( final edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel Who) {
		 final edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Double> Speed= new edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Double>( 1.0 );
		this.moveTo( Who, edu.wustl.cse.lookingglass.apis.walkandtouch.SpatialRelation.IN_FRONT_OF );
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					DopeyDon.this.move( org.alice.apis.moveandturn.MoveDirection.FORWARD, 1.0, Speed.value );
				}
			},
			new Runnable() {
				public void run() {
					DopeyDon.this.resizeDepth( 0.25, Speed.value, org.alice.apis.moveandturn.ResizePolicy.PRESERVE_NOTHING  );
				}
			},
			new Runnable() {
				public void run() {
					DopeyDon.this.getPart(DopeyDon.Part.Cheekleft).resize( 2.0, Speed.value );
				}
			},
			new Runnable() {
				public void run() {
					DopeyDon.this.getPart(DopeyDon.Part.Cheekright).resize( 2.0, Speed.value );
				}
			},
			new Runnable() {
				public void run() {
					DopeyDon.this.getPart(DopeyDon.Part.Finarmright).move( org.alice.apis.moveandturn.MoveDirection.LEFT, 0.5, Speed.value );
				}
			},
			new Runnable() {
				public void run() {
					DopeyDon.this.getPart(DopeyDon.Part.Finarmleft).move( org.alice.apis.moveandturn.MoveDirection.RIGHT, 0.5, Speed.value );
				}
			}
		);

		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					DopeyDon.this.move( org.alice.apis.moveandturn.MoveDirection.BACKWARD, 1.0 );
				}
			},
			new Runnable() {
				public void run() {
					DopeyDon.this.resizeDepth( 4.0, Speed.value, org.alice.apis.moveandturn.ResizePolicy.PRESERVE_NOTHING  );
				}
			},
			new Runnable() {
				public void run() {
					DopeyDon.this.getPart(DopeyDon.Part.Body).resize( 0.6666666666666666, Speed.value );
				}
			},
			new Runnable() {
				public void run() {
					DopeyDon.this.getPart(DopeyDon.Part.Cheekleft).resize( 0.5, Speed.value );
				}
			},
			new Runnable() {
				public void run() {
					DopeyDon.this.getPart(DopeyDon.Part.Cheekright).resize( 0.5, Speed.value );
				}
			},
			new Runnable() {
				public void run() {
					DopeyDon.this.getPart(DopeyDon.Part.Finarmright).move( org.alice.apis.moveandturn.MoveDirection.RIGHT, 0.5, Speed.value );
				}
			},
			new Runnable() {
				public void run() {
					DopeyDon.this.getPart(DopeyDon.Part.Finarmleft).move( org.alice.apis.moveandturn.MoveDirection.LEFT, 0.5, Speed.value );
				}
			},
			new Runnable() {
				public void run() {
					DopeyDon.this.move( org.alice.apis.moveandturn.MoveDirection.BACKWARD, 3.0 );
				}
			}
		);

	}

	public void Whoa( ) {
		for (int index1 = 0; index1 < 3; index1 = index1 + 1) {
			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						DopeyDon.this.getPart(DopeyDon.Part.Eyeleft).resize( 3.0, 0.25 );
					}
				},
				new Runnable() {
					public void run() {
						DopeyDon.this.getPart(DopeyDon.Part.Eyeright).resize( 3.0, 0.25 );
					}
				}
			);

			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						DopeyDon.this.getPart(DopeyDon.Part.Eyeleft).resize( 0.3333333333333333, 0.25 );
					}
				},
				new Runnable() {
					public void run() {
						DopeyDon.this.getPart(DopeyDon.Part.Eyeright).resize( 0.3333333333333333, 0.25 );
					}
				}
			);

		}

	}

	public void Hug( final edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel Who) {
		this.moveTo( Who, edu.wustl.cse.lookingglass.apis.walkandtouch.SpatialRelation.IN_FRONT_OF );
		if (Inflated.value ) {		} else { 
			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						DopeyDon.this.getPart(DopeyDon.Part.Finarmright).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.25 );
					}
				},
				new Runnable() {
					public void run() {
						DopeyDon.this.getPart(DopeyDon.Part.Finarmleft).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.25 );
					}
				},
				new Runnable() {
					public void run() {
						DopeyDon.this.getPart(DopeyDon.Part.Finarmleft).resize( 2.0 );
					}
				},
				new Runnable() {
					public void run() {
						DopeyDon.this.getPart(DopeyDon.Part.Finarmright).resize( 2.0 );
					}
				}
			);

			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						DopeyDon.this.getPart(DopeyDon.Part.Finarmright).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.25 );
					}
				},
				new Runnable() {
					public void run() {
						DopeyDon.this.getPart(DopeyDon.Part.Finarmleft).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.25 );
					}
				}
			);

			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						DopeyDon.this.getPart(DopeyDon.Part.Finarmright).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.25 );
					}
				},
				new Runnable() {
					public void run() {
						DopeyDon.this.getPart(DopeyDon.Part.Finarmleft).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.25 );
					}
				}
			);

			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						DopeyDon.this.getPart(DopeyDon.Part.Finarmright).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.25 );
					}
				},
				new Runnable() {
					public void run() {
						DopeyDon.this.getPart(DopeyDon.Part.Finarmleft).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.25 );
					}
				},
				new Runnable() {
					public void run() {
						DopeyDon.this.getPart(DopeyDon.Part.Finarmleft).resize( 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						DopeyDon.this.getPart(DopeyDon.Part.Finarmright).resize( 0.5 );
					}
				}
			);

		}

	}

	public void Bounce( final Number NumberOfTimes) {
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					for (int index2 = 0; index2 < NumberOfTimes.intValue(); index2 = index2 + 1) {
				DopeyDon.this.move( org.alice.apis.moveandturn.MoveDirection.UP, 1.0 );
				DopeyDon.this.move( org.alice.apis.moveandturn.MoveDirection.DOWN, 1.0 );
			}
				}
			},
			new Runnable() {
				public void run() {
					// DoInOrder { 
				for (int index3 = 0; index3 < NumberOfTimes.intValue(); index3 = index3 + 1) {
					org.alice.virtualmachine.DoTogether.invokeAndWait(
						new Runnable() {
							public void run() {
								DopeyDon.this.getPart(DopeyDon.Part.Tail).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25) );
							}
						},
						new Runnable() {
							public void run() {
								DopeyDon.this.getPart(DopeyDon.Part.Finarmright).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25) );
							}
						},
						new Runnable() {
							public void run() {
								DopeyDon.this.getPart(DopeyDon.Part.Finarmleft).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25) );
							}
						}
					);

					org.alice.virtualmachine.DoTogether.invokeAndWait(
						new Runnable() {
							public void run() {
								DopeyDon.this.getPart(DopeyDon.Part.Tail).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25) );
							}
						},
						new Runnable() {
							public void run() {
								DopeyDon.this.getPart(DopeyDon.Part.Finarmright).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25) );
							}
						},
						new Runnable() {
							public void run() {
								DopeyDon.this.getPart(DopeyDon.Part.Finarmleft).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25) );
							}
						}
					);

				}

//			}
				}
			}
		);

	}
}
