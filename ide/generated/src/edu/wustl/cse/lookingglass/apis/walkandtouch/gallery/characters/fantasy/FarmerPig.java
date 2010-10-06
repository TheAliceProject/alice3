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
package edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.characters.fantasy;

public class FarmerPig extends edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.GalleryPerson { 
	public FarmerPig() {
		super( "fantasy/FarmerPig" );
}
	public enum Part {
		LeftEye( "torso", "head", "leftEye" ),
		LeftEyelid( "torso", "head", "leftEyelid" ),
		RightEye( "torso", "head", "rightEye" ),
		RightEyelid( "torso", "head", "rightEyelid" ),
		RightEyeBrow( "torso", "head", "rightEyeBrow" ),
		RightEar( "torso", "head", "rightEar" ),
		LeftEar( "torso", "head", "leftEar" ),
		LeftEyebrow( "torso", "head", "strawHat", "leftEyebrow" ),
		StrawHat( "torso", "head", "strawHat" ),
		Head( "torso", "head" ),
		LeftLowerArm( "torso", "leftUpperArm", "leftLowerArm" ),
		LeftUpperArm( "torso", "leftUpperArm" ),
		RightLowerArm( "torso", "rightUpperArm", "rightLowerArm" ),
		RightUpperArm( "torso", "rightUpperArm" ),
		LeftLowerLeg( "torso", "leftUpperLeg", "leftLowerLeg" ),
		LeftUpperLeg( "torso", "leftUpperLeg" ),
		RightLowerLeg( "torso", "rightUpperLeg", "rightLowerLeg" ),
		RightUpperLeg( "torso", "rightUpperLeg" ),
		Tail( "torso", "tail" ),
		Torso( "torso" );
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

	public void LookHappy( ) {
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					FarmerPig.this.standUp( false );
				}
			},
			new Runnable() {
				public void run() {
					// DoInOrder { 
				//FarmerPig.this.setSurfaceTexture(value);
				//FarmerPig.this.getPart(FarmerPig.Part.StrawHat).setSurfaceTexture(value);
//			}
				}
			}
		);

	}

	public void WorkOnSomething( final edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel OnWhat) {
		this.walkTo( OnWhat );
		for (int index1 = 0; index1 < 3; index1 = index1 + 1) {
			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						// DoInOrder { 
					FarmerPig.this.getPart(FarmerPig.Part.LeftUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.04), 0.4 );
					FarmerPig.this.delay(0.1);
					FarmerPig.this.getPart(FarmerPig.Part.LeftUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.08), 0.4 );
					FarmerPig.this.delay(0.2);
					FarmerPig.this.getPart(FarmerPig.Part.LeftUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.04), 0.4 );
//				}
					}
				},
				new Runnable() {
					public void run() {
						// DoInOrder { 
					FarmerPig.this.getPart(FarmerPig.Part.RightUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.04), 0.4 );
					FarmerPig.this.delay(0.2);
					FarmerPig.this.getPart(FarmerPig.Part.RightUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.08), 0.4 );
					FarmerPig.this.delay(0.1);
					FarmerPig.this.getPart(FarmerPig.Part.RightUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.04), 0.4 );
//				}
					}
				}
			);

		}

		this.LookHappy(  );
	}

	public void LookHorrified( ) {
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					FarmerPig.this.getPart(FarmerPig.Part.LeftUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					FarmerPig.this.getPart(FarmerPig.Part.RightUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					FarmerPig.this.getPart(FarmerPig.Part.RightUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					FarmerPig.this.touch( FarmerPig.this.getPart(FarmerPig.Part.LeftEar), org.alice.apis.moveandturn.MoveDirection.FORWARD, edu.wustl.cse.lookingglass.apis.walkandtouch.TouchPart.LEFT_HAND, 0.0, 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					FarmerPig.this.getPart(FarmerPig.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.06), 0.1 );
				}
			}
		);

		// DoInOrder { 
			//this.setSurfaceTexture(value);
			//this.getPart(FarmerPig.Part.StrawHat).setSurfaceTexture(value);
//		}

		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					for (int index1 = 0; index1 < 2; index1 = index1 + 1) {
				FarmerPig.this.move( org.alice.apis.moveandturn.MoveDirection.UP, 0.25, 0.25 );
				FarmerPig.this.move( org.alice.apis.moveandturn.MoveDirection.DOWN, 0.25, 0.25 );
			}
				}
			},
			new Runnable() {
				public void run() {
					FarmerPig.this.getPart(FarmerPig.Part.LeftEye).resize( 3.0, 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					FarmerPig.this.getPart(FarmerPig.Part.RightEye).resize( 3.0, 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					FarmerPig.this.getPart(FarmerPig.Part.RightEye).move( org.alice.apis.moveandturn.MoveDirection.FORWARD, 0.3, 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					FarmerPig.this.getPart(FarmerPig.Part.LeftEye).move( org.alice.apis.moveandturn.MoveDirection.FORWARD, 0.3, 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					FarmerPig.this.getPart(FarmerPig.Part.StrawHat).move( org.alice.apis.moveandturn.MoveDirection.UP, 0.3, 0.25 );
				}
			}
		);

		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					FarmerPig.this.getPart(FarmerPig.Part.RightEye).move( org.alice.apis.moveandturn.MoveDirection.BACKWARD, 0.3, 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					FarmerPig.this.getPart(FarmerPig.Part.LeftEye).move( org.alice.apis.moveandturn.MoveDirection.BACKWARD, 0.3, 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					FarmerPig.this.getPart(FarmerPig.Part.StrawHat).move( org.alice.apis.moveandturn.MoveDirection.DOWN, 0.3, 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					FarmerPig.this.getPart(FarmerPig.Part.RightEye).resize( 0.3333333333333333 );
				}
			},
			new Runnable() {
				public void run() {
					FarmerPig.this.getPart(FarmerPig.Part.LeftEye).resize( 0.3333333333333333 );
				}
			},
			new Runnable() {
				public void run() {
					FarmerPig.this.LookHappy(  );
				}
			}
		);

	}

	public void LookSad( ) {
		// DoInOrder { 
			//this.setSurfaceTexture(value);
			//this.getPart(FarmerPig.Part.StrawHat).setSurfaceTexture(value);
			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						FarmerPig.this.look( edu.wustl.cse.lookingglass.apis.walkandtouch.LookDirection.DOWN, 0.15, 2.0 );
					}
				},
				new Runnable() {
					public void run() {
						FarmerPig.this.getPart(FarmerPig.Part.Torso).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.03), 2.0 );
					}
				},
				new Runnable() {
					public void run() {
						FarmerPig.this.getPart(FarmerPig.Part.LeftUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.03), 2.0 );
					}
				},
				new Runnable() {
					public void run() {
						FarmerPig.this.getPart(FarmerPig.Part.RightUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.03), 2.0 );
					}
				},
				new Runnable() {
					public void run() {
						FarmerPig.this.getPart(FarmerPig.Part.LeftUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.03), 2.0 );
					}
				},
				new Runnable() {
					public void run() {
						FarmerPig.this.getPart(FarmerPig.Part.RightUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.03), 2.0 );
					}
				}
			);

//		}

	}

	public void LookBoastful( ) {
		this.LookHappy(  );
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					FarmerPig.this.touch( FarmerPig.this.getPart(FarmerPig.Part.Torso), org.alice.apis.moveandturn.MoveDirection.RIGHT, edu.wustl.cse.lookingglass.apis.walkandtouch.TouchPart.RIGHT_HAND, 0.0, 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					FarmerPig.this.touch( FarmerPig.this.getPart(FarmerPig.Part.Torso), org.alice.apis.moveandturn.MoveDirection.LEFT, edu.wustl.cse.lookingglass.apis.walkandtouch.TouchPart.LEFT_HAND, 0.0, 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					FarmerPig.this.look( edu.wustl.cse.lookingglass.apis.walkandtouch.LookDirection.UP, 0.07 );
				}
			}
		);

	}
}
