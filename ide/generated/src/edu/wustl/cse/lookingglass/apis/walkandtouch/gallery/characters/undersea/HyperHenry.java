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

public class HyperHenry extends edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.GalleryCharacter { 
	public HyperHenry() {
		super( "undersea/Hyper Henry" );
}
	public enum Part {
		Fintop( "body", "fintop" ),
		Finhandright( "body", "finarmright", "finhandright" ),
		Finarmright( "body", "finarmright" ),
		Finhandleft( "body", "finarmleft", "finhandleft" ),
		Finarmleft( "body", "finarmleft" ),
		Jaw( "body", "head", "jaw" ),
		Eyeleft( "body", "head", "eyeleft" ),
		Eyeright( "body", "head", "eyeright" ),
		Head( "body", "head" ),
		Body( "body" ),
		Tail( "tailmid", "tail" ),
		Tailmid( "tailmid" );
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

	public void MoveTail( final Number Amount) {
		 final edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Double> Speed= new edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Double>();  Speed.value = new Double(0.25);;
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					// DoInOrder { 
				HyperHenry.this.getPart(HyperHenry.Part.Tailmid).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.12), (1.0 * Speed.value) );
				for (int index2 = 0; index2 < ((Amount.doubleValue() + 1.0) * 2.0); index2 = index2 + 1) {
					HyperHenry.this.getPart(HyperHenry.Part.Tailmid).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), (0.5 * Speed.value) );
					HyperHenry.this.getPart(HyperHenry.Part.Tailmid).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), (0.5 * Speed.value) );
				}

				HyperHenry.this.getPart(HyperHenry.Part.Tailmid).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.12), (1.0 * Speed.value) );
//			}
				}
			},
			new Runnable() {
				public void run() {
					// DoInOrder { 
				HyperHenry.this.getPart(HyperHenry.Part.Tail).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.12), (1.0 * Speed.value) );
				for (int index3 = 0; index3 < ((Amount.doubleValue() + 1.0) * 2.0); index3 = index3 + 1) {
					HyperHenry.this.getPart(HyperHenry.Part.Tail).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), (0.5 * Speed.value) );
					HyperHenry.this.getPart(HyperHenry.Part.Tail).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), (0.5 * Speed.value) );
				}

				HyperHenry.this.getPart(HyperHenry.Part.Tail).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.12), (1.0 * Speed.value) );
//			}
				}
			}
		);

	}

	public void ChaseOwnTail( final Number Circles) {
		this.getPart(HyperHenry.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.25 );
		for (int index1 = 0; index1 < Circles.intValue(); index1 = index1 + 1) {
			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						HyperHenry.this.turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(1.0) );
					}
				},
				new Runnable() {
					public void run() {
						HyperHenry.this.MoveTail( 2.0 );
					}
				},
				new Runnable() {
					public void run() {
						HyperHenry.this.MoveArms( 2.0 );
					}
				}
			);

		}

		this.getPart(HyperHenry.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.25 );
	}

	public void MoveArms( final Number Amount) {
		 final edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Double> Speed= new edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Double>();  Speed.value = new Double(0.125);;
		for (int index1 = 0; index1 < (Amount.doubleValue() * 2.0); index1 = index1 + 1) {
			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						HyperHenry.this.getPart(HyperHenry.Part.Finarmright).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.12), Speed.value );
					}
				},
				new Runnable() {
					public void run() {
						HyperHenry.this.getPart(HyperHenry.Part.Finarmleft).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.12), Speed.value );
					}
				}
			);

			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						HyperHenry.this.getPart(HyperHenry.Part.Finarmright).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.12), Speed.value );
					}
				},
				new Runnable() {
					public void run() {
						HyperHenry.this.getPart(HyperHenry.Part.Finarmleft).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.12), Speed.value );
					}
				}
			);

		}

	}

	public void Swim( final Number Distance) {
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					HyperHenry.this.BobInWater( (Distance.doubleValue() * 0.5) );
				}
			},
			new Runnable() {
				public void run() {
					HyperHenry.this.move( org.alice.apis.moveandturn.MoveDirection.FORWARD, Distance.doubleValue(), (Distance.doubleValue() * 0.25) );
				}
			},
			new Runnable() {
				public void run() {
					HyperHenry.this.MoveArms( (Distance.doubleValue() * 0.5) );
				}
			},
			new Runnable() {
				public void run() {
					HyperHenry.this.MoveTail( (Distance.doubleValue() * 0.5) );
				}
			}
		);

	}

	public void BobInWater( final Number Amount) {
		for (int index1 = 0; index1 < Amount.intValue(); index1 = index1 + 1) {
			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						HyperHenry.this.getPart(HyperHenry.Part.Fintop).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.02), 0.25 );
					}
				},
				new Runnable() {
					public void run() {
						HyperHenry.this.move( org.alice.apis.moveandturn.MoveDirection.RIGHT, 0.125, 0.25 );
					}
				},
				new Runnable() {
					public void run() {
						HyperHenry.this.turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.0020), 0.25 );
					}
				}
			);

			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						HyperHenry.this.getPart(HyperHenry.Part.Fintop).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.02), 0.25 );
					}
				},
				new Runnable() {
					public void run() {
						HyperHenry.this.move( org.alice.apis.moveandturn.MoveDirection.LEFT, 0.125, 0.25 );
					}
				},
				new Runnable() {
					public void run() {
						HyperHenry.this.turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.0020), 0.25 );
					}
				}
			);

		}

	}
}
