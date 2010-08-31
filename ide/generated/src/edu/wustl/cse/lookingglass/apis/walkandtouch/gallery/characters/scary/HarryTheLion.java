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
package edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.characters.scary;

public class HarryTheLion extends edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.GalleryCharacter { 
	private final edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Boolean> SpotlightOn= new edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Boolean>( false );  
	private final edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Boolean> StillBusy= new edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Boolean>( false );  
	private final edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Double> NumEaten= new edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Double>( 0.0 );
	public HarryTheLion() {
		super( "scary/Harry the Lion" );
}
	public enum Part {
		Frontpawright( "body", "frontlegright", "frontshinright", "frontpawright" ),
		Frontshinright( "body", "frontlegright", "frontshinright" ),
		Frontlegright( "body", "frontlegright" ),
		Tailfur( "body", "tail", "tailfur" ),
		Tail( "body", "tail" ),
		Mane( "body", "head", "mane" ),
		Jaw( "body", "head", "jaw" ),
		Eyelidright( "body", "head", "eyeright", "eyelidright" ),
		Eyeright( "body", "head", "eyeright" ),
		Eyelidleft( "body", "head", "eyeleft", "eyelidleft" ),
		Eyeleft( "body", "head", "eyeleft" ),
		Spinningstars02( "body", "head", "spinningstars02" ),
		Spinningstars03( "body", "head", "spinningstars03" ),
		Spinningstars04( "body", "head", "spinningstars04" ),
		Spinningstars1( "body", "head", "spinningstars1" ),
		Head( "body", "head" ),
		Frontpawleft( "body", "frontlegleft", "frontshinleft", "frontpawleft" ),
		Frontshinleft( "body", "frontlegleft", "frontshinleft" ),
		Frontlegleft( "body", "frontlegleft" ),
		Backpawleft( "body", "backlegleft", "backshinleft", "backpawleft" ),
		Backshinleft( "body", "backlegleft", "backshinleft" ),
		Backlegleft( "body", "backlegleft" ),
		Backpawright( "body", "backlegright", "backshinright", "backpawright" ),
		Backshinright( "body", "backlegright", "backshinright" ),
		Backlegright( "body", "backlegright" ),
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

	public void Attack( ) {
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					// DoInOrder { 
				HarryTheLion.this.delay(1.8);
				HarryTheLion.this.move( org.alice.apis.moveandturn.MoveDirection.FORWARD, 2.0 );
//			}
				}
			},
			new Runnable() {
				public void run() {
					HarryTheLion.this.getPart(HarryTheLion.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1) );
				}
			},
			new Runnable() {
				public void run() {
					// DoInOrder { 
				org.alice.virtualmachine.DoTogether.invokeAndWait(
					new Runnable() {
						public void run() {
							HarryTheLion.this.getPart(HarryTheLion.Part.Tail).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25) );
						}
					},
					new Runnable() {
						public void run() {
							HarryTheLion.this.getPart(HarryTheLion.Part.Tail).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25) );
						}
					}
				);

				HarryTheLion.this.getPart(HarryTheLion.Part.Tail).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25) );
//			}
				}
			},
			new Runnable() {
				public void run() {
					// DoInOrder { 
				HarryTheLion.this.delay(1.8);
				org.alice.virtualmachine.DoTogether.invokeAndWait(
					new Runnable() {
						public void run() {
							// DoInOrder { 
						HarryTheLion.this.turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.25 );
						HarryTheLion.this.turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.25 );
//					}
						}
					},
					new Runnable() {
						public void run() {
							HarryTheLion.this.getPart(HarryTheLion.Part.Backlegleft).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
						}
					},
					new Runnable() {
						public void run() {
							HarryTheLion.this.getPart(HarryTheLion.Part.Frontlegleft).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
						}
					},
					new Runnable() {
						public void run() {
							HarryTheLion.this.getPart(HarryTheLion.Part.Backlegright).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
						}
					},
					new Runnable() {
						public void run() {
							HarryTheLion.this.getPart(HarryTheLion.Part.Frontlegright).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
						}
					}
				);

				org.alice.virtualmachine.DoTogether.invokeAndWait(
					new Runnable() {
						public void run() {
							// DoInOrder { 
						HarryTheLion.this.delay(0.25);
						HarryTheLion.this.standUp( false );
//					}
						}
					}
				);

//			}
				}
			},
			new Runnable() {
				public void run() {
					//HarryTheLion.this.playSound(sound)
				}
			}
		);

	}

	public void Cower( ) {
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					HarryTheLion.this.getPart(HarryTheLion.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					HarryTheLion.this.getPart(HarryTheLion.Part.Body).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.02), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					HarryTheLion.this.getPart(HarryTheLion.Part.Frontlegright).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					HarryTheLion.this.getPart(HarryTheLion.Part.Backlegright).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					HarryTheLion.this.getPart(HarryTheLion.Part.Frontlegleft).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					HarryTheLion.this.getPart(HarryTheLion.Part.Backlegleft).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					for (int index1 = 0; index1 < 10; index1 = index1 + 1) {
				HarryTheLion.this.move( org.alice.apis.moveandturn.MoveDirection.LEFT, 0.02, 0.1 );
				HarryTheLion.this.move( org.alice.apis.moveandturn.MoveDirection.RIGHT, 0.02, 0.1 );
			}
				}
			}
		);

		this.standUp( false );
	}
}
