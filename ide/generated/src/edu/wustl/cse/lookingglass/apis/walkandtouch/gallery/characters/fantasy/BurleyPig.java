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

public class BurleyPig extends edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.GalleryPerson { 
	public BurleyPig() {
		super( "fantasy/BurleyPig" );
}
	public enum Part {
		RightLowerLeg( "torso", "rightUpperLeg", "rightLowerLeg" ),
		RightUpperLeg( "torso", "rightUpperLeg" ),
		LeftLowerLeg( "torso", "leftUpperLeg", "leftLowerLeg" ),
		LeftUpperLeg( "torso", "leftUpperLeg" ),
		Tail( "torso", "tail" ),
		RightLowerArm( "torso", "rightUpperArm", "rightLowerArm" ),
		RightUpperArm( "torso", "rightUpperArm" ),
		LeftLowerArm( "torso", "leftUpperArm", "leftLowerArm" ),
		LeftUpperArm( "torso", "leftUpperArm" ),
		LeftEar( "torso", "head", "leftEar" ),
		RightEar( "torso", "head", "rightEar" ),
		LeftEye( "torso", "head", "leftEye" ),
		LeftEyelid( "torso", "head", "leftEyelid" ),
		RightEyelid( "torso", "head", "rightEyelid" ),
		RightEye( "torso", "head", "rightEye" ),
		RightEyebrow( "torso", "head", "rightEyebrow" ),
		LeftEyebrow( "torso", "head", "leftEyebrow" ),
		Head( "torso", "head" ),
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
					//BurleyPig.this.setSurfaceTexture(value);
				}
			},
			new Runnable() {
				public void run() {
					BurleyPig.this.standUp( false );
				}
			}
		);

	}

	public void WorkOnSomething( final edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel OnWhat) {
		this.walkTo( OnWhat, edu.wustl.cse.lookingglass.apis.walkandtouch.SpatialRelation.IN_FRONT_OF, 0.1 );
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					BurleyPig.this.touch( OnWhat, org.alice.apis.moveandturn.MoveDirection.FORWARD, edu.wustl.cse.lookingglass.apis.walkandtouch.TouchPart.RIGHT_HAND, 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					BurleyPig.this.touch( OnWhat, org.alice.apis.moveandturn.MoveDirection.FORWARD, edu.wustl.cse.lookingglass.apis.walkandtouch.TouchPart.LEFT_HAND, -0.25 );
				}
			}
		);

		for (int index1 = 0; index1 < 3; index1 = index1 + 1) {
			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						// DoInOrder { 
					BurleyPig.this.getPart(BurleyPig.Part.RightUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.04), 0.4 );
					BurleyPig.this.delay(0.1);
					BurleyPig.this.getPart(BurleyPig.Part.RightUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.08), 0.4 );
					BurleyPig.this.delay(0.2);
					BurleyPig.this.getPart(BurleyPig.Part.RightUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.04), 0.4 );
//				}
					}
				},
				new Runnable() {
					public void run() {
						// DoInOrder { 
					BurleyPig.this.getPart(BurleyPig.Part.LeftUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.04), 0.4 );
					BurleyPig.this.delay(0.2);
					BurleyPig.this.getPart(BurleyPig.Part.LeftUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.08), 0.4 );
					BurleyPig.this.delay(0.1);
					BurleyPig.this.getPart(BurleyPig.Part.LeftUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.04), 0.4 );
//				}
					}
				}
			);

		}

		this.LookHappy(  );
	}

	public void FlexAndShowOff( ) {
		this.LookHappy(  );
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					BurleyPig.this.getPart(BurleyPig.Part.LeftUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1) );
				}
			},
			new Runnable() {
				public void run() {
					BurleyPig.this.getPart(BurleyPig.Part.LeftLowerLeg).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.08) );
				}
			},
			new Runnable() {
				public void run() {
					BurleyPig.this.getPart(BurleyPig.Part.Torso).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1) );
				}
			},
			new Runnable() {
				public void run() {
					BurleyPig.this.getPart(BurleyPig.Part.RightUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1) );
				}
			},
			new Runnable() {
				public void run() {
					BurleyPig.this.getPart(BurleyPig.Part.RightUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.35) );
				}
			},
			new Runnable() {
				public void run() {
					BurleyPig.this.getPart(BurleyPig.Part.LeftUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.35) );
				}
			},
			new Runnable() {
				public void run() {
					BurleyPig.this.getPart(BurleyPig.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1) );
				}
			}
		);

		for (int index1 = 0; index1 < 2; index1 = index1 + 1) {
			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						// DoInOrder { 
					BurleyPig.this.getPart(BurleyPig.Part.LeftUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.5 );
					BurleyPig.this.getPart(BurleyPig.Part.LeftUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.5 );
//				}
					}
				},
				new Runnable() {
					public void run() {
						// DoInOrder { 
					BurleyPig.this.getPart(BurleyPig.Part.RightUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.5 );
					BurleyPig.this.getPart(BurleyPig.Part.RightUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.5 );
//				}
					}
				}
			);

		}

	}

	public void LookSad( ) {
		this.LookHappy(  );
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					//BurleyPig.this.setSurfaceTexture(value);
				}
			},
			new Runnable() {
				public void run() {
					BurleyPig.this.getPart(BurleyPig.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.07), 2.0 );
				}
			},
			new Runnable() {
				public void run() {
					BurleyPig.this.getPart(BurleyPig.Part.Torso).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.03), 2.0 );
				}
			},
			new Runnable() {
				public void run() {
					BurleyPig.this.getPart(BurleyPig.Part.RightUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.03), 2.0 );
				}
			},
			new Runnable() {
				public void run() {
					BurleyPig.this.getPart(BurleyPig.Part.LeftUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.03), 2.0 );
				}
			},
			new Runnable() {
				public void run() {
					BurleyPig.this.getPart(BurleyPig.Part.RightUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.03), 2.0 );
				}
			},
			new Runnable() {
				public void run() {
					BurleyPig.this.getPart(BurleyPig.Part.LeftUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.03), 2.0 );
				}
			}
		);

	}
}
