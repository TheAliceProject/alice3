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
package edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.scenes.forest;

public class Scenery extends edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.GalleryModel { 
	public Scenery() {
		super( "forest/Scenery" );
}
	public enum Part {
		Bog( "Bog" ),
		Kettle_Firewood1( "Kettle", "Firewood1" ),
		Kettle_Firewood3( "Kettle", "Firewood3" ),
		Kettle_Firewood2( "Kettle", "Firewood2" ),
		Kettle( "Kettle" ),
		Post_SignBottom( "Post", "signBottom" ),
		Post_SignTop( "Post", "signTop" ),
		Post( "Post" ),
		Trunk_Leaves( "Trunk", "Leaves" ),
		Trunk_Wire_Cage_Rings( "Trunk", "wire", "Cage", "Rings" ),
		Trunk_Wire_Cage_Floor( "Trunk", "wire", "Cage", "Floor" ),
		Trunk_Wire_Cage( "Trunk", "wire", "Cage" ),
		Trunk_Wire( "Trunk", "wire" ),
		Trunk( "Trunk" ),
		Trunk_Fat_Leaves_fat( "Trunk-Fat", "Leaves-fat" ),
		Trunk_Fat( "Trunk-Fat" ),
		Trunk_tall_Leaves_tall( "Trunk-tall", "Leaves-tall" ),
		Trunk_tall( "Trunk-tall" ),
		Chair( "Chair" ),
		Board( "Board" );
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

	public void BoilKettle( final Number HowLong) {
		for (int index1 = 0; index1 < HowLong.intValue(); index1 = index1 + 1) {
			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						//Scenery.this.playSound(sound)
					}
				},
				new Runnable() {
					public void run() {
						// DoInOrder { 
					org.alice.virtualmachine.DoTogether.invokeAndWait(
						new Runnable() {
							public void run() {
								Scenery.this.getPart(Scenery.Part.Kettle).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.01), 0.3 );
							}
						},
						new Runnable() {
							public void run() {
								Scenery.this.getPart(Scenery.Part.Kettle).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.01), 0.3 );
							}
						}
					);

					org.alice.virtualmachine.DoTogether.invokeAndWait(
						new Runnable() {
							public void run() {
								Scenery.this.getPart(Scenery.Part.Kettle).move( org.alice.apis.moveandturn.MoveDirection.UP, 0.01, 0.3 );
							}
						},
						new Runnable() {
							public void run() {
								Scenery.this.getPart(Scenery.Part.Kettle).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.02), 0.3 );
							}
						},
						new Runnable() {
							public void run() {
								Scenery.this.getPart(Scenery.Part.Kettle).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.02), 0.3 );
							}
						},
						new Runnable() {
							public void run() {
								Scenery.this.getPart(Scenery.Part.Kettle).resizeHeight( 1.2, 0.3, org.alice.apis.moveandturn.ResizePolicy.PRESERVE_NOTHING  );
							}
						}
					);

					org.alice.virtualmachine.DoTogether.invokeAndWait(
						new Runnable() {
							public void run() {
								Scenery.this.getPart(Scenery.Part.Kettle).resizeHeight( 0.8333333333333334, 0.3, org.alice.apis.moveandturn.ResizePolicy.PRESERVE_NOTHING  );
							}
						},
						new Runnable() {
							public void run() {
								Scenery.this.getPart(Scenery.Part.Kettle).move( org.alice.apis.moveandturn.MoveDirection.DOWN, 0.01, 0.3 );
							}
						},
						new Runnable() {
							public void run() {
								Scenery.this.getPart(Scenery.Part.Kettle).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.01), 0.3 );
							}
						},
						new Runnable() {
							public void run() {
								Scenery.this.getPart(Scenery.Part.Kettle).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.01), 0.3 );
							}
						}
					);

					org.alice.virtualmachine.DoTogether.invokeAndWait(
						new Runnable() {
							public void run() {
								Scenery.this.getPart(Scenery.Part.Kettle).resizeHeight( 1.2, 0.3, org.alice.apis.moveandturn.ResizePolicy.PRESERVE_NOTHING  );
							}
						},
						new Runnable() {
							public void run() {
								Scenery.this.getPart(Scenery.Part.Kettle).move( org.alice.apis.moveandturn.MoveDirection.UP, 0.01, 0.3 );
							}
						},
						new Runnable() {
							public void run() {
								Scenery.this.getPart(Scenery.Part.Kettle).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.01), 0.3 );
							}
						},
						new Runnable() {
							public void run() {
								Scenery.this.getPart(Scenery.Part.Kettle).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.01), 0.3 );
							}
						}
					);

					org.alice.virtualmachine.DoTogether.invokeAndWait(
						new Runnable() {
							public void run() {
								Scenery.this.getPart(Scenery.Part.Kettle).resizeHeight( 0.8333333333333334, 0.3, org.alice.apis.moveandturn.ResizePolicy.PRESERVE_NOTHING  );
							}
						},
						new Runnable() {
							public void run() {
								Scenery.this.getPart(Scenery.Part.Kettle).move( org.alice.apis.moveandturn.MoveDirection.DOWN, 0.01, 0.3 );
							}
						},
						new Runnable() {
							public void run() {
								Scenery.this.getPart(Scenery.Part.Kettle).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.02), 0.3 );
							}
						},
						new Runnable() {
							public void run() {
								Scenery.this.getPart(Scenery.Part.Kettle).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.02), 0.3 );
							}
						}
					);

					org.alice.virtualmachine.DoTogether.invokeAndWait(
						new Runnable() {
							public void run() {
								Scenery.this.getPart(Scenery.Part.Kettle).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.01), 0.3 );
							}
						},
						new Runnable() {
							public void run() {
								Scenery.this.getPart(Scenery.Part.Kettle).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.01), 0.3 );
							}
						}
					);

//				}
					}
				}
			);

		}

	}

	public void SwingCage( ) {
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					for (int index1 = 0; index1 < 3; index1 = index1 + 1) {
				//play squeaky cage sound
				//Scenery.this.playSound(sound)
			}
				}
			},
			new Runnable() {
				public void run() {
					// DoInOrder { 
				Scenery.this.getPart(Scenery.Part.Trunk_Wire).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.02), 0.25 );
				Scenery.this.getPart(Scenery.Part.Trunk_Wire).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.04), 0.5 );
				Scenery.this.getPart(Scenery.Part.Trunk_Wire).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.04), 0.5 );
				Scenery.this.getPart(Scenery.Part.Trunk_Wire).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.04), 0.5 );
				Scenery.this.getPart(Scenery.Part.Trunk_Wire).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.04), 0.5 );
				Scenery.this.getPart(Scenery.Part.Trunk_Wire).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.02), 0.25 );
//			}
				}
			}
		);

	}
}
