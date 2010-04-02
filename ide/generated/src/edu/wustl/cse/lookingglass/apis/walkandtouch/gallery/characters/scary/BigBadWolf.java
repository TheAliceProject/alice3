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

public class BigBadWolf extends edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.GalleryPerson { 
	public BigBadWolf() {
		super( "scary/BigBadWolf" );
}
	public enum Part {
		RightFoot( "torso", "rightUpperLeg", "rightLowerLeg", "rightFoot" ),
		RightLowerLeg( "torso", "rightUpperLeg", "rightLowerLeg" ),
		RightUpperLeg( "torso", "rightUpperLeg" ),
		LeftFoot( "torso", "leftUpperLeg", "leftLowerLeg", "leftFoot" ),
		LeftLowerLeg( "torso", "leftUpperLeg", "leftLowerLeg" ),
		LeftUpperLeg( "torso", "leftUpperLeg" ),
		Tail( "torso", "tail" ),
		LeftPinkyFinger( "torso", "leftUpperArm", "leftLowerArm", "leftHand", "leftPinkyFinger" ),
		LeftMiddleFinger( "torso", "leftUpperArm", "leftLowerArm", "leftHand", "leftMiddleFinger" ),
		LeftIndexFinger( "torso", "leftUpperArm", "leftLowerArm", "leftHand", "leftIndexFinger" ),
		LeftThumb( "torso", "leftUpperArm", "leftLowerArm", "leftHand", "leftThumb" ),
		LeftHand( "torso", "leftUpperArm", "leftLowerArm", "leftHand" ),
		LeftLowerArm( "torso", "leftUpperArm", "leftLowerArm" ),
		LeftUpperArm( "torso", "leftUpperArm" ),
		RightIndexFinger( "torso", "rightUpperArm", "rightLowerArm", "rightHand", "rightIndexFinger" ),
		RightThumb( "torso", "rightUpperArm", "rightLowerArm", "rightHand", "rightThumb" ),
		RightMiddleFinger( "torso", "rightUpperArm", "rightLowerArm", "rightHand", "rightMiddleFinger" ),
		RightPinkyFinger( "torso", "rightUpperArm", "rightLowerArm", "rightHand", "rightPinkyFinger" ),
		RightHand( "torso", "rightUpperArm", "rightLowerArm", "rightHand" ),
		RightLowerArm( "torso", "rightUpperArm", "rightLowerArm" ),
		RightUpperArm( "torso", "rightUpperArm" ),
		Jaw( "torso", "head", "jaw" ),
		Specs( "torso", "head", "specs" ),
		GrandmaHat( "torso", "head", "grandmaHat" ),
		Tophat( "torso", "head", "tophat" ),
		LeftEye( "torso", "head", "leftEye" ),
		RightEye( "torso", "head", "rightEye" ),
		RightEyeLi( "torso", "head", "rightEyeLi" ),
		LeftEyeLid( "torso", "head", "leftEyeLid" ),
		Head( "torso", "head" ),
		Skirt( "torso", "skirt" ),
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

	public void HuffAndPuffAndBlow( ) {
		this.standUp( false );
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					BigBadWolf.this.getPart(BigBadWolf.Part.Torso).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.12) );
				}
			},
			new Runnable() {
				public void run() {
					BigBadWolf.this.getPart(BigBadWolf.Part.RightUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.12) );
				}
			},
			new Runnable() {
				public void run() {
					BigBadWolf.this.getPart(BigBadWolf.Part.LeftUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.12) );
				}
			},
			new Runnable() {
				public void run() {
					BigBadWolf.this.move( org.alice.apis.moveandturn.MoveDirection.UP, 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					BigBadWolf.this.getPart(BigBadWolf.Part.LeftUpperArm).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.2) );
				}
			},
			new Runnable() {
				public void run() {
					BigBadWolf.this.getPart(BigBadWolf.Part.LeftLowerArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15) );
				}
			},
			new Runnable() {
				public void run() {
					BigBadWolf.this.getPart(BigBadWolf.Part.RightUpperArm).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.2) );
				}
			},
			new Runnable() {
				public void run() {
					BigBadWolf.this.getPart(BigBadWolf.Part.RightLowerArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15) );
				}
			}
		);

		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					BigBadWolf.this.getPart(BigBadWolf.Part.Torso).resize( 1.1, 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					BigBadWolf.this.getPart(BigBadWolf.Part.RightUpperLeg).resize( 0.9090909090909091, 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					BigBadWolf.this.getPart(BigBadWolf.Part.LeftUpperLeg).resize( 0.9090909090909091, 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					BigBadWolf.this.getPart(BigBadWolf.Part.Tail).resize( 0.9090909090909091, 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					BigBadWolf.this.getPart(BigBadWolf.Part.LeftUpperArm).resize( 0.9090909090909091, 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					BigBadWolf.this.getPart(BigBadWolf.Part.RightUpperArm).resize( 0.9090909090909091, 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					BigBadWolf.this.getPart(BigBadWolf.Part.Head).resize( 0.9090909090909091, 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					BigBadWolf.this.getPart(BigBadWolf.Part.Skirt).resize( 0.9090909090909091, 0.25 );
				}
			}
		);

		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					BigBadWolf.this.getPart(BigBadWolf.Part.Torso).resize( 0.98, 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					BigBadWolf.this.getPart(BigBadWolf.Part.RightUpperLeg).resize( 1.0204081632653061, 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					BigBadWolf.this.getPart(BigBadWolf.Part.LeftUpperLeg).resize( 1.0204081632653061, 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					BigBadWolf.this.getPart(BigBadWolf.Part.Tail).resize( 1.0204081632653061, 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					BigBadWolf.this.getPart(BigBadWolf.Part.LeftUpperArm).resize( 1.0204081632653061, 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					BigBadWolf.this.getPart(BigBadWolf.Part.RightUpperArm).resize( 1.0204081632653061, 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					BigBadWolf.this.getPart(BigBadWolf.Part.Head).resize( 1.0204081632653061, 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					BigBadWolf.this.getPart(BigBadWolf.Part.Skirt).resize( 1.0204081632653061, 0.5 );
				}
			}
		);

		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					BigBadWolf.this.getPart(BigBadWolf.Part.Torso).resize( 1.1 );
				}
			},
			new Runnable() {
				public void run() {
					BigBadWolf.this.getPart(BigBadWolf.Part.RightUpperLeg).resize( 0.9090909090909091 );
				}
			},
			new Runnable() {
				public void run() {
					BigBadWolf.this.getPart(BigBadWolf.Part.LeftUpperLeg).resize( 0.9090909090909091 );
				}
			},
			new Runnable() {
				public void run() {
					BigBadWolf.this.getPart(BigBadWolf.Part.Tail).resize( 0.9090909090909091 );
				}
			},
			new Runnable() {
				public void run() {
					BigBadWolf.this.getPart(BigBadWolf.Part.LeftUpperArm).resize( 0.9090909090909091 );
				}
			},
			new Runnable() {
				public void run() {
					BigBadWolf.this.getPart(BigBadWolf.Part.RightUpperArm).resize( 0.9090909090909091 );
				}
			},
			new Runnable() {
				public void run() {
					BigBadWolf.this.getPart(BigBadWolf.Part.Head).resize( 0.9090909090909091 );
				}
			},
			new Runnable() {
				public void run() {
					BigBadWolf.this.getPart(BigBadWolf.Part.Skirt).resize( 0.9090909090909091 );
				}
			}
		);

		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					BigBadWolf.this.getPart(BigBadWolf.Part.Torso).resize( 0.8433125316242199, 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					BigBadWolf.this.getPart(BigBadWolf.Part.RightUpperLeg).resize( 1.1858, 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					BigBadWolf.this.getPart(BigBadWolf.Part.LeftUpperLeg).resize( 1.1858, 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					BigBadWolf.this.getPart(BigBadWolf.Part.Tail).resize( 1.1858, 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					BigBadWolf.this.getPart(BigBadWolf.Part.LeftUpperArm).resize( 1.1858, 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					BigBadWolf.this.getPart(BigBadWolf.Part.RightUpperArm).resize( 1.1858, 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					BigBadWolf.this.getPart(BigBadWolf.Part.Head).resize( 1.1858, 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					BigBadWolf.this.getPart(BigBadWolf.Part.Skirt).resize( 1.1858, 0.25 );
				}
			}
		);

		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					BigBadWolf.this.look( edu.wustl.cse.lookingglass.apis.walkandtouch.LookDirection.UP );
				}
			},
			new Runnable() {
				public void run() {
					BigBadWolf.this.getPart(BigBadWolf.Part.Torso).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25) );
				}
			},
			new Runnable() {
				public void run() {
					BigBadWolf.this.getPart(BigBadWolf.Part.RightUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25) );
				}
			},
			new Runnable() {
				public void run() {
					BigBadWolf.this.getPart(BigBadWolf.Part.LeftUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25) );
				}
			},
			new Runnable() {
				public void run() {
					BigBadWolf.this.move( org.alice.apis.moveandturn.MoveDirection.UP, 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					BigBadWolf.this.getPart(BigBadWolf.Part.LeftUpperArm).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.2) );
				}
			},
			new Runnable() {
				public void run() {
					BigBadWolf.this.getPart(BigBadWolf.Part.RightUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15) );
				}
			},
			new Runnable() {
				public void run() {
					BigBadWolf.this.getPart(BigBadWolf.Part.LeftUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15) );
				}
			},
			new Runnable() {
				public void run() {
					BigBadWolf.this.getPart(BigBadWolf.Part.LeftLowerArm).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15) );
				}
			},
			new Runnable() {
				public void run() {
					BigBadWolf.this.getPart(BigBadWolf.Part.RightUpperArm).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.2) );
				}
			},
			new Runnable() {
				public void run() {
					BigBadWolf.this.getPart(BigBadWolf.Part.RightLowerArm).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15) );
				}
			}
		);

		this.delay(0.5);
		this.standUp(  );
	}

	public void Scare( ) {
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					// DoInOrder { 
				BigBadWolf.this.move( org.alice.apis.moveandturn.MoveDirection.UP, 0.25, 0.25 );
				BigBadWolf.this.move( org.alice.apis.moveandturn.MoveDirection.DOWN, 0.25, 0.25 );
//			}
				}
			},
			new Runnable() {
				public void run() {
					BigBadWolf.this.getPart(BigBadWolf.Part.Torso).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					BigBadWolf.this.getPart(BigBadWolf.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					BigBadWolf.this.getPart(BigBadWolf.Part.Tail).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					BigBadWolf.this.getPart(BigBadWolf.Part.RightUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					BigBadWolf.this.getPart(BigBadWolf.Part.LeftUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					BigBadWolf.this.getPart(BigBadWolf.Part.RightLowerLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					BigBadWolf.this.getPart(BigBadWolf.Part.LeftLowerLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					BigBadWolf.this.getPart(BigBadWolf.Part.LeftUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					BigBadWolf.this.getPart(BigBadWolf.Part.LeftLowerArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					BigBadWolf.this.getPart(BigBadWolf.Part.RightUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					BigBadWolf.this.getPart(BigBadWolf.Part.RightLowerArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					BigBadWolf.this.getPart(BigBadWolf.Part.RightHand).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					BigBadWolf.this.getPart(BigBadWolf.Part.LeftHand).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.5 );
				}
			}
		);

		this.getPart(BigBadWolf.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.05 );
		for (int index1 = 0; index1 < 7; index1 = index1 + 1) {
			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						// DoInOrder { 
					BigBadWolf.this.getPart(BigBadWolf.Part.Jaw).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.02), 0.05 );
					BigBadWolf.this.getPart(BigBadWolf.Part.Jaw).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.02), 0.05 );
//				}
					}
				},
				new Runnable() {
					public void run() {
						// DoInOrder { 
					BigBadWolf.this.getPart(BigBadWolf.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.05 );
					BigBadWolf.this.getPart(BigBadWolf.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.05 );
//				}
					}
				}
			);

		}

		this.getPart(BigBadWolf.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.05 );
		this.delay(0.5);
		this.standUp( false );
	}

	public void Howl( ) {
		this.standUp( false );
		this.look( edu.wustl.cse.lookingglass.apis.walkandtouch.LookDirection.UP );
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					// DoInOrder { 
				for (int index2 = 0; index2 < 2; index2 = index2 + 1) {
					BigBadWolf.this.getPart(BigBadWolf.Part.Jaw).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.3 );
					BigBadWolf.this.getPart(BigBadWolf.Part.Jaw).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.1 );
				}

//			}
				}
			},
			new Runnable() {
				public void run() {
					// DoInOrder { 
				for (int index3 = 0; index3 < 2; index3 = index3 + 1) {
					BigBadWolf.this.move( org.alice.apis.moveandturn.MoveDirection.UP, 0.1, 0.3 );
					BigBadWolf.this.move( org.alice.apis.moveandturn.MoveDirection.DOWN, 0.1, 0.3 );
				}

//			}
				}
			},
			new Runnable() {
				public void run() {
					// DoInOrder { 
				BigBadWolf.this.getPart(BigBadWolf.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.15 );
				BigBadWolf.this.getPart(BigBadWolf.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.3 );
				BigBadWolf.this.getPart(BigBadWolf.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.15 );
//			}
				}
			}
		);

		this.delay(0.5);
		this.standUp( false );
	}

	public void LookUpset( ) {
		this.look( edu.wustl.cse.lookingglass.apis.walkandtouch.LookDirection.DOWN );
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					BigBadWolf.this.touch( BigBadWolf.this.getPart(BigBadWolf.Part.Jaw), org.alice.apis.moveandturn.MoveDirection.UP, edu.wustl.cse.lookingglass.apis.walkandtouch.TouchPart.LEFT_HAND, 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					BigBadWolf.this.touch( BigBadWolf.this.getPart(BigBadWolf.Part.Jaw), org.alice.apis.moveandturn.MoveDirection.UP, edu.wustl.cse.lookingglass.apis.walkandtouch.TouchPart.RIGHT_HAND, 0.25 );
				}
			}
		);

		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					BigBadWolf.this.keepTouching( BigBadWolf.this.getPart(BigBadWolf.Part.Jaw), org.alice.apis.moveandturn.MoveDirection.UP, edu.wustl.cse.lookingglass.apis.walkandtouch.TouchPart.RIGHT_HAND, 0.0, 2.5 );
				}
			},
			new Runnable() {
				public void run() {
					BigBadWolf.this.keepTouching( BigBadWolf.this.getPart(BigBadWolf.Part.Jaw), org.alice.apis.moveandturn.MoveDirection.UP, edu.wustl.cse.lookingglass.apis.walkandtouch.TouchPart.LEFT_HAND, 0.0, 2.5 );
				}
			},
			new Runnable() {
				public void run() {
					// DoInOrder { 
				BigBadWolf.this.getPart(BigBadWolf.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.06), 0.25 );
				for (int index1 = 0; index1 < 2; index1 = index1 + 1) {
					BigBadWolf.this.getPart(BigBadWolf.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.12), 0.5 );
					BigBadWolf.this.getPart(BigBadWolf.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.12), 0.5 );
				}

				BigBadWolf.this.getPart(BigBadWolf.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.06), 0.25 );
//			}
				}
			}
		);

		this.standUp( false );
	}
}
