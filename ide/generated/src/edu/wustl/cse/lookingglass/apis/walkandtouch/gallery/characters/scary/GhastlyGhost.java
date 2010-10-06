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

public class GhastlyGhost extends edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.GalleryCharacter { 
	public GhastlyGhost() {
		super( "scary/GhastlyGhost" );
}
	public enum Part {
		RightHand( "rightUpperArm", "rightLowerArm", "rightHand" ),
		RightLowerArm( "rightUpperArm", "rightLowerArm" ),
		RightUpperArm( "rightUpperArm" ),
		LeftHand( "leftUpperArm", "leftLowerArm", "leftHand" ),
		LeftLowerArm( "leftUpperArm", "leftLowerArm" ),
		LeftUpperArm( "leftUpperArm" ),
		Torso( "torso" ),
		Head( "head" );
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

	public void Whoooooooo( ) {
		 final edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Double> UpperArmRotation= new edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Double>( 0.1 );
		 final edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Double> Duration= new edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Double>( 0.5 );
		 final edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Double> ForearmRotation= new edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Double>( 0.2 );
		 final edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Double> HandRotation= new edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Double>( 0.2 );
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					//GhastlyGhost.this.playSound(sound)
				}
			},
			new Runnable() {
				public void run() {
					// DoInOrder { 
				org.alice.virtualmachine.DoTogether.invokeAndWait(
					new Runnable() {
						public void run() {
							GhastlyGhost.this.getPart(GhastlyGhost.Part.RightUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions((UpperArmRotation.value / 2.0)), Duration.value );
						}
					},
					new Runnable() {
						public void run() {
							GhastlyGhost.this.getPart(GhastlyGhost.Part.RightLowerArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions((ForearmRotation.value / 2.0)), Duration.value );
						}
					},
					new Runnable() {
						public void run() {
							GhastlyGhost.this.getPart(GhastlyGhost.Part.RightHand).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions((HandRotation.value / 2.0)), Duration.value );
						}
					},
					new Runnable() {
						public void run() {
							GhastlyGhost.this.getPart(GhastlyGhost.Part.LeftUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions((UpperArmRotation.value / 2.0)), Duration.value );
						}
					},
					new Runnable() {
						public void run() {
							GhastlyGhost.this.getPart(GhastlyGhost.Part.LeftLowerArm).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions((ForearmRotation.value / 2.0)), Duration.value );
						}
					},
					new Runnable() {
						public void run() {
							GhastlyGhost.this.getPart(GhastlyGhost.Part.LeftHand).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions((HandRotation.value / 2.0)), Duration.value );
						}
					}
				);

				org.alice.virtualmachine.DoTogether.invokeAndWait(
					new Runnable() {
						public void run() {
							GhastlyGhost.this.getPart(GhastlyGhost.Part.RightUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(UpperArmRotation.value), Duration.value );
						}
					},
					new Runnable() {
						public void run() {
							GhastlyGhost.this.getPart(GhastlyGhost.Part.RightLowerArm).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(ForearmRotation.value), Duration.value );
						}
					},
					new Runnable() {
						public void run() {
							GhastlyGhost.this.getPart(GhastlyGhost.Part.RightHand).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(HandRotation.value), Duration.value );
						}
					},
					new Runnable() {
						public void run() {
							GhastlyGhost.this.getPart(GhastlyGhost.Part.LeftUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(UpperArmRotation.value), Duration.value );
						}
					},
					new Runnable() {
						public void run() {
							GhastlyGhost.this.getPart(GhastlyGhost.Part.LeftLowerArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(ForearmRotation.value), Duration.value );
						}
					},
					new Runnable() {
						public void run() {
							GhastlyGhost.this.getPart(GhastlyGhost.Part.LeftHand).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(HandRotation.value), Duration.value );
						}
					}
				);

				org.alice.virtualmachine.DoTogether.invokeAndWait(
					new Runnable() {
						public void run() {
							GhastlyGhost.this.getPart(GhastlyGhost.Part.RightUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(UpperArmRotation.value), Duration.value );
						}
					},
					new Runnable() {
						public void run() {
							GhastlyGhost.this.getPart(GhastlyGhost.Part.RightLowerArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(ForearmRotation.value), Duration.value );
						}
					},
					new Runnable() {
						public void run() {
							GhastlyGhost.this.getPart(GhastlyGhost.Part.RightHand).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(HandRotation.value), Duration.value );
						}
					},
					new Runnable() {
						public void run() {
							GhastlyGhost.this.getPart(GhastlyGhost.Part.LeftUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(UpperArmRotation.value), Duration.value );
						}
					},
					new Runnable() {
						public void run() {
							GhastlyGhost.this.getPart(GhastlyGhost.Part.LeftLowerArm).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(ForearmRotation.value), Duration.value );
						}
					},
					new Runnable() {
						public void run() {
							GhastlyGhost.this.getPart(GhastlyGhost.Part.LeftHand).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(HandRotation.value), Duration.value );
						}
					}
				);

				org.alice.virtualmachine.DoTogether.invokeAndWait(
					new Runnable() {
						public void run() {
							GhastlyGhost.this.getPart(GhastlyGhost.Part.RightUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(UpperArmRotation.value), Duration.value );
						}
					},
					new Runnable() {
						public void run() {
							GhastlyGhost.this.getPart(GhastlyGhost.Part.RightLowerArm).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(ForearmRotation.value), Duration.value );
						}
					},
					new Runnable() {
						public void run() {
							GhastlyGhost.this.getPart(GhastlyGhost.Part.RightHand).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(HandRotation.value), Duration.value );
						}
					},
					new Runnable() {
						public void run() {
							GhastlyGhost.this.getPart(GhastlyGhost.Part.LeftUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(UpperArmRotation.value), Duration.value );
						}
					},
					new Runnable() {
						public void run() {
							GhastlyGhost.this.getPart(GhastlyGhost.Part.LeftLowerArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(ForearmRotation.value), Duration.value );
						}
					},
					new Runnable() {
						public void run() {
							GhastlyGhost.this.getPart(GhastlyGhost.Part.LeftHand).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(HandRotation.value), Duration.value );
						}
					}
				);

				org.alice.virtualmachine.DoTogether.invokeAndWait(
					new Runnable() {
						public void run() {
							GhastlyGhost.this.getPart(GhastlyGhost.Part.RightUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions((UpperArmRotation.value / 2.0)), Duration.value );
						}
					},
					new Runnable() {
						public void run() {
							GhastlyGhost.this.getPart(GhastlyGhost.Part.RightLowerArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions((ForearmRotation.value / 2.0)), Duration.value );
						}
					},
					new Runnable() {
						public void run() {
							GhastlyGhost.this.getPart(GhastlyGhost.Part.RightHand).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions((HandRotation.value / 2.0)), Duration.value );
						}
					},
					new Runnable() {
						public void run() {
							GhastlyGhost.this.getPart(GhastlyGhost.Part.LeftUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions((UpperArmRotation.value / 2.0)), Duration.value );
						}
					},
					new Runnable() {
						public void run() {
							GhastlyGhost.this.getPart(GhastlyGhost.Part.LeftLowerArm).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions((ForearmRotation.value / 2.0)), Duration.value );
						}
					},
					new Runnable() {
						public void run() {
							GhastlyGhost.this.getPart(GhastlyGhost.Part.LeftHand).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions((HandRotation.value / 2.0)), Duration.value );
						}
					}
				);

//			}
				}
			}
		);

	}

	public void Disappear( ) {
		this.setOpacity(0.0, 3.0 );
	}

	public void Appear( ) {
		this.setOpacity(1.0, 3.0 );
	}

	public void Boo( ) {
		 final edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Double> HeadRotation= new edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Double>( 0.1 );
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					// DoInOrder { 
				GhastlyGhost.this.delay(0.1);
				//GhastlyGhost.this.playSound(sound)
//			}
				}
			},
			new Runnable() {
				public void run() {
					// DoInOrder { 
				org.alice.virtualmachine.DoTogether.invokeAndWait(
					new Runnable() {
						public void run() {
							GhastlyGhost.this.getPart(GhastlyGhost.Part.Torso).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.25 );
						}
					},
					new Runnable() {
						public void run() {
							GhastlyGhost.this.getPart(GhastlyGhost.Part.RightUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.25 );
						}
					},
					new Runnable() {
						public void run() {
							GhastlyGhost.this.getPart(GhastlyGhost.Part.RightUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.25 );
						}
					},
					new Runnable() {
						public void run() {
							GhastlyGhost.this.getPart(GhastlyGhost.Part.LeftUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.25 );
						}
					},
					new Runnable() {
						public void run() {
							GhastlyGhost.this.getPart(GhastlyGhost.Part.LeftUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.25 );
						}
					}
				);

				org.alice.virtualmachine.DoTogether.invokeAndWait(
					new Runnable() {
						public void run() {
							GhastlyGhost.this.getPart(GhastlyGhost.Part.Torso).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
						}
					},
					new Runnable() {
						public void run() {
							GhastlyGhost.this.getPart(GhastlyGhost.Part.RightUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.75 );
						}
					},
					new Runnable() {
						public void run() {
							GhastlyGhost.this.getPart(GhastlyGhost.Part.RightUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.75 );
						}
					},
					new Runnable() {
						public void run() {
							GhastlyGhost.this.getPart(GhastlyGhost.Part.LeftUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.75 );
						}
					},
					new Runnable() {
						public void run() {
							GhastlyGhost.this.getPart(GhastlyGhost.Part.LeftUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.75 );
						}
					}
				);

				GhastlyGhost.this.getPart(GhastlyGhost.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(HeadRotation.value), 0.1 );
				// DoInOrder { 
					GhastlyGhost.this.getPart(GhastlyGhost.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions((HeadRotation.value / 2.0)), 0.05 );
					GhastlyGhost.this.getPart(GhastlyGhost.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions((HeadRotation.value / 2.0)), 0.05 );
//				}

				// DoInOrder { 
					GhastlyGhost.this.getPart(GhastlyGhost.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions((HeadRotation.value / 2.0)), 0.05 );
					GhastlyGhost.this.getPart(GhastlyGhost.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions((HeadRotation.value / 2.0)), 0.05 );
//				}

				GhastlyGhost.this.getPart(GhastlyGhost.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(HeadRotation.value), 0.1 );
//			}
				}
			}
		);

	}

	public void BoredBoo( ) {
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					// DoInOrder { 
				GhastlyGhost.this.delay(0.1);
				//GhastlyGhost.this.playSound(sound)
//			}
				}
			},
			new Runnable() {
				public void run() {
					// DoInOrder { 
				org.alice.virtualmachine.DoTogether.invokeAndWait(
					new Runnable() {
						public void run() {
							GhastlyGhost.this.getPart(GhastlyGhost.Part.Torso).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
						}
					},
					new Runnable() {
						public void run() {
							GhastlyGhost.this.getPart(GhastlyGhost.Part.RightUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
						}
					},
					new Runnable() {
						public void run() {
							GhastlyGhost.this.getPart(GhastlyGhost.Part.RightUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
						}
					},
					new Runnable() {
						public void run() {
							GhastlyGhost.this.getPart(GhastlyGhost.Part.LeftUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
						}
					},
					new Runnable() {
						public void run() {
							GhastlyGhost.this.getPart(GhastlyGhost.Part.LeftUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1) );
						}
					}
				);

				org.alice.virtualmachine.DoTogether.invokeAndWait(
					new Runnable() {
						public void run() {
							GhastlyGhost.this.getPart(GhastlyGhost.Part.Torso).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.75 );
						}
					},
					new Runnable() {
						public void run() {
							GhastlyGhost.this.getPart(GhastlyGhost.Part.RightUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.75 );
						}
					},
					new Runnable() {
						public void run() {
							GhastlyGhost.this.getPart(GhastlyGhost.Part.RightUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.75 );
						}
					},
					new Runnable() {
						public void run() {
							GhastlyGhost.this.getPart(GhastlyGhost.Part.LeftUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.75 );
						}
					},
					new Runnable() {
						public void run() {
							GhastlyGhost.this.getPart(GhastlyGhost.Part.LeftUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.75 );
						}
					}
				);

//			}
				}
			}
		);

	}
}
