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
package edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.characters.adults;

public class Magician extends edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.GalleryPerson { 
	public Magician() {
		super( "adults/Magician" );
}
	public enum Part {
		LeftPinkyTopJoint( "leftUpperArm", "leftLowerArm", "leftHand", "leftPinky", "leftPinkyMiddleJoint", "leftPinkyTopJoint" ),
		LeftPinkyMiddleJoint( "leftUpperArm", "leftLowerArm", "leftHand", "leftPinky", "leftPinkyMiddleJoint" ),
		LeftPinky( "leftUpperArm", "leftLowerArm", "leftHand", "leftPinky" ),
		LeftRingFingerTopJoint( "leftUpperArm", "leftLowerArm", "leftHand", "leftRingFinger", "leftRingFingerMiddleJoint", "leftRingFingerTopJoint" ),
		LeftRingFingerMiddleJoint( "leftUpperArm", "leftLowerArm", "leftHand", "leftRingFinger", "leftRingFingerMiddleJoint" ),
		LeftRingFinger( "leftUpperArm", "leftLowerArm", "leftHand", "leftRingFinger" ),
		LeftMiddleFingerTopJoint( "leftUpperArm", "leftLowerArm", "leftHand", "leftMiddleFinger", "leftMiddleFingerMiddleJoint", "leftMiddleFingerTopJoint" ),
		LeftMiddleFingerMiddleJoint( "leftUpperArm", "leftLowerArm", "leftHand", "leftMiddleFinger", "leftMiddleFingerMiddleJoint" ),
		LeftMiddleFinger( "leftUpperArm", "leftLowerArm", "leftHand", "leftMiddleFinger" ),
		LeftPointerFingerTopJoint( "leftUpperArm", "leftLowerArm", "leftHand", "leftPointerFinger", "leftPointerFingerMiddleJoint", "leftPointerFingerTopJoint" ),
		LeftPointerFingerMiddleJoint( "leftUpperArm", "leftLowerArm", "leftHand", "leftPointerFinger", "leftPointerFingerMiddleJoint" ),
		LeftPointerFinger( "leftUpperArm", "leftLowerArm", "leftHand", "leftPointerFinger" ),
		LeftThumbTopJoint( "leftUpperArm", "leftLowerArm", "leftHand", "leftThumb", "leftThumbTopJoint" ),
		LeftThumb( "leftUpperArm", "leftLowerArm", "leftHand", "leftThumb" ),
		LeftHand( "leftUpperArm", "leftLowerArm", "leftHand" ),
		LeftLowerArm( "leftUpperArm", "leftLowerArm" ),
		LeftUpperArm( "leftUpperArm" ),
		LeftFoot( "Pelvis", "leftUpperLeg", "leftLowerLeg", "leftFoot" ),
		LeftLowerLeg( "Pelvis", "leftUpperLeg", "leftLowerLeg" ),
		LeftUpperLeg( "Pelvis", "leftUpperLeg" ),
		RightFoot( "Pelvis", "rightUpperLeg", "rightLowerLeg", "rightFoot" ),
		RightLowerLeg( "Pelvis", "rightUpperLeg", "rightLowerLeg" ),
		RightUpperLeg( "Pelvis", "rightUpperLeg" ),
		Pelvis( "Pelvis" ),
		Hat( "head", "hat" ),
		RightUpperEyelid( "head", "rightUpperEyelid" ),
		RightEyeball( "head", "rightEyeball" ),
		RightLowerEyelid( "head", "rightLowerEyelid" ),
		LeftUpperEyelid( "head", "leftUpperEyelid" ),
		LeftEyeball( "head", "leftEyeball" ),
		LeftLowerEyelid( "head", "leftLowerEyelid" ),
		Head( "head" ),
		BowTie( "bowTie" ),
		RightPinkyTopJoint( "rightUpperArm", "rightLowerArm", "rightHand", "rightPinky", "rightPinkyMiddleJoint", "rightPinkyTopJoint" ),
		RightPinkyMiddleJoint( "rightUpperArm", "rightLowerArm", "rightHand", "rightPinky", "rightPinkyMiddleJoint" ),
		RightPinky( "rightUpperArm", "rightLowerArm", "rightHand", "rightPinky" ),
		RightRingFingerTopJoint( "rightUpperArm", "rightLowerArm", "rightHand", "rightRingFinger", "rightRingFingerMiddleJoint", "rightRingFingerTopJoint" ),
		RightRingFingerMiddleJoint( "rightUpperArm", "rightLowerArm", "rightHand", "rightRingFinger", "rightRingFingerMiddleJoint" ),
		RightRingFinger( "rightUpperArm", "rightLowerArm", "rightHand", "rightRingFinger" ),
		RightMiddleFingerTopJoint( "rightUpperArm", "rightLowerArm", "rightHand", "rightMiddleFinger", "rightMiddleFingerMiddleJoint", "rightMiddleFingerTopJoint" ),
		RightMiddleFingerMiddleJoint( "rightUpperArm", "rightLowerArm", "rightHand", "rightMiddleFinger", "rightMiddleFingerMiddleJoint" ),
		RightMiddleFinger( "rightUpperArm", "rightLowerArm", "rightHand", "rightMiddleFinger" ),
		RightPointerFingerTopJoint( "rightUpperArm", "rightLowerArm", "rightHand", "rightPointerFinger", "rightPointerFingerMiddleJoint", "rightPointerFingerTopJoint" ),
		RightPointerFingerMiddleJoint( "rightUpperArm", "rightLowerArm", "rightHand", "rightPointerFinger", "rightPointerFingerMiddleJoint" ),
		RightPointerFinger( "rightUpperArm", "rightLowerArm", "rightHand", "rightPointerFinger" ),
		RightThumbTopJoint( "rightUpperArm", "rightLowerArm", "rightHand", "rightThumb", "rightThumbTopJoint" ),
		RightThumb( "rightUpperArm", "rightLowerArm", "rightHand", "rightThumb" ),
		RightHand( "rightUpperArm", "rightLowerArm", "rightHand" ),
		RightLowerArm( "rightUpperArm", "rightLowerArm" ),
		RightUpperArm( "rightUpperArm" );
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

	public void Levitate( ) {
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					Magician.this.move( org.alice.apis.moveandturn.MoveDirection.UP, 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					Magician.this.getPart(Magician.Part.LeftUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1) );
				}
			},
			new Runnable() {
				public void run() {
					Magician.this.getPart(Magician.Part.RightUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1) );
				}
			},
			new Runnable() {
				public void run() {
					Magician.this.getPart(Magician.Part.LeftUpperLeg).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25) );
				}
			},
			new Runnable() {
				public void run() {
					Magician.this.getPart(Magician.Part.RightUpperLeg).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25) );
				}
			},
			new Runnable() {
				public void run() {
					Magician.this.getPart(Magician.Part.LeftLowerLeg).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.4) );
				}
			},
			new Runnable() {
				public void run() {
					Magician.this.getPart(Magician.Part.RightLowerLeg).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.4) );
				}
			},
			new Runnable() {
				public void run() {
					Magician.this.touch( Magician.this.getPart(Magician.Part.BowTie), org.alice.apis.moveandturn.MoveDirection.FORWARD, edu.wustl.cse.lookingglass.apis.walkandtouch.TouchPart.RIGHT_HAND, 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					Magician.this.touch( Magician.this.getPart(Magician.Part.BowTie), org.alice.apis.moveandturn.MoveDirection.FORWARD, edu.wustl.cse.lookingglass.apis.walkandtouch.TouchPart.LEFT_HAND, 0.25 );
				}
			}
		);

		for (int index1 = 0; index1 < 3; index1 = index1 + 1) {
			this.move( org.alice.apis.moveandturn.MoveDirection.DOWN, 0.125 );
			this.move( org.alice.apis.moveandturn.MoveDirection.UP, 0.125 );
		}

		this.move( org.alice.apis.moveandturn.MoveDirection.DOWN, 1.0 );
		this.standUp( false );
	}

	public void TaDaa( ) {
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					Magician.this.getPart(Magician.Part.LeftUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15) );
				}
			},
			new Runnable() {
				public void run() {
					Magician.this.getPart(Magician.Part.LeftUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.2) );
				}
			},
			new Runnable() {
				public void run() {
					Magician.this.getPart(Magician.Part.LeftLowerArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1) );
				}
			},
			new Runnable() {
				public void run() {
					Magician.this.getPart(Magician.Part.RightUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1) );
				}
			},
			new Runnable() {
				public void run() {
					Magician.this.getPart(Magician.Part.RightLowerArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.3) );
				}
			},
			new Runnable() {
				public void run() {
					Magician.this.getPart(Magician.Part.RightUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.2) );
				}
			}
		);

		this.delay(0.5);
		this.look( edu.wustl.cse.lookingglass.apis.walkandtouch.LookDirection.DOWN, 0.5 );
		this.standUp( false );
	}

	public void DisappearingHeadTrick( ) {
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					Magician.this.getPart(Magician.Part.Head).setOpacity(0.0 );
				}
			},
			new Runnable() {
				public void run() {
					Magician.this.getPart(Magician.Part.Hat).setOpacity(1.0 );
				}
			}
		);

		this.getPart(Magician.Part.Hat).move( org.alice.apis.moveandturn.MoveDirection.UP, 0.15 );
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					Magician.this.getPart(Magician.Part.RightEyeball).resize( 2.0, 0.0 );
				}
			},
			new Runnable() {
				public void run() {
					Magician.this.getPart(Magician.Part.LeftEyeball).resize( 2.0, 0.0 );
				}
			}
		);

		this.getPart(Magician.Part.RightEyeball).setOpacity(1.0 );
		this.delay(0.5);
		this.getPart(Magician.Part.LeftEyeball).setOpacity(1.0 );
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					Magician.this.getPart(Magician.Part.RightEyeball).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.15) );
				}
			},
			new Runnable() {
				public void run() {
					Magician.this.getPart(Magician.Part.LeftEyeball).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.15) );
				}
			}
		);

		this.delay(1.0);
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					Magician.this.getPart(Magician.Part.RightEyeball).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.3) );
				}
			},
			new Runnable() {
				public void run() {
					Magician.this.getPart(Magician.Part.LeftEyeball).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.3) );
				}
			}
		);

		this.delay(1.0);
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					Magician.this.getPart(Magician.Part.RightEyeball).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.15) );
				}
			},
			new Runnable() {
				public void run() {
					Magician.this.getPart(Magician.Part.LeftEyeball).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.15) );
				}
			}
		);

		this.delay(1.0);
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					Magician.this.getPart(Magician.Part.RightEyeball).setOpacity(0.0 );
				}
			},
			new Runnable() {
				public void run() {
					Magician.this.getPart(Magician.Part.LeftEyeball).setOpacity(0.0 );
				}
			}
		);

		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					Magician.this.getPart(Magician.Part.RightEyeball).resize( 0.5, 0.0 );
				}
			},
			new Runnable() {
				public void run() {
					Magician.this.getPart(Magician.Part.LeftEyeball).resize( 0.5, 0.0 );
				}
			}
		);

		this.delay(1.0);
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					Magician.this.getPart(Magician.Part.Head).setOpacity(1.0, 2.0 );
				}
			},
			new Runnable() {
				public void run() {
					Magician.this.getPart(Magician.Part.Hat).move( org.alice.apis.moveandturn.MoveDirection.DOWN, 0.15, 2.0 );
				}
			}
		);

	}
}
