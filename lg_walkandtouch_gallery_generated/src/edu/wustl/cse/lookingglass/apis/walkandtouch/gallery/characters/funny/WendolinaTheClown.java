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
package edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.characters.funny;

public class WendolinaTheClown extends edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.GalleryPerson { 
	edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Boolean> SpotlightOn= new edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Boolean>();  
	edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Boolean> StillBusyCL= new edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Boolean>();  
	public WendolinaTheClown() {
		super( "funny/Wendolina the Clown" );
		edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Boolean> SpotlightOn= new edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Boolean>();  
		edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Boolean> StillBusyCL= new edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Boolean>();  
}
	public enum Part {
		LeftSuspenders( "hoopPants", "leftSuspenders" ),
		RightSuspenders( "hoopPants", "rightSuspenders" ),
		HoopPants( "hoopPants" ),
		LeftFoot( "leftUpperLeg", "leftLowerLeg", "leftFoot" ),
		LeftLowerLeg( "leftUpperLeg", "leftLowerLeg" ),
		LeftUpperLeg( "leftUpperLeg" ),
		RightFoot( "rightUpperLeg", "rightLowerLeg", "rightFoot" ),
		RightLowerLeg( "rightUpperLeg", "rightLowerLeg" ),
		RightUpperLeg( "rightUpperLeg" ),
		Skirt( "skirt" ),
		LeftFinger2( "torso", "leftUpperArm", "leftLowerArm", "leftHand", "leftFinger2" ),
		LeftFinger3( "torso", "leftUpperArm", "leftLowerArm", "leftHand", "leftFinger3" ),
		LeftFinger1( "torso", "leftUpperArm", "leftLowerArm", "leftHand", "leftFinger1" ),
		LeftThumb( "torso", "leftUpperArm", "leftLowerArm", "leftHand", "leftThumb" ),
		Petal04( "torso", "leftUpperArm", "leftLowerArm", "leftHand", "stembottom01", "stemtop01", "petal04" ),
		Petal05( "torso", "leftUpperArm", "leftLowerArm", "leftHand", "stembottom01", "stemtop01", "petal05" ),
		Petal07( "torso", "leftUpperArm", "leftLowerArm", "leftHand", "stembottom01", "stemtop01", "petal07" ),
		Petal06( "torso", "leftUpperArm", "leftLowerArm", "leftHand", "stembottom01", "stemtop01", "petal06" ),
		Stemtop01( "torso", "leftUpperArm", "leftLowerArm", "leftHand", "stembottom01", "stemtop01" ),
		Stembottom01( "torso", "leftUpperArm", "leftLowerArm", "leftHand", "stembottom01" ),
		Petal08( "torso", "leftUpperArm", "leftLowerArm", "leftHand", "stembottom02", "stemtop02", "petal08" ),
		Petal09( "torso", "leftUpperArm", "leftLowerArm", "leftHand", "stembottom02", "stemtop02", "petal09" ),
		Petal11( "torso", "leftUpperArm", "leftLowerArm", "leftHand", "stembottom02", "stemtop02", "petal11" ),
		Petal10( "torso", "leftUpperArm", "leftLowerArm", "leftHand", "stembottom02", "stemtop02", "petal10" ),
		Stemtop02( "torso", "leftUpperArm", "leftLowerArm", "leftHand", "stembottom02", "stemtop02" ),
		Stembottom02( "torso", "leftUpperArm", "leftLowerArm", "leftHand", "stembottom02" ),
		Petal02( "torso", "leftUpperArm", "leftLowerArm", "leftHand", "stembottom", "stemtop", "petal02" ),
		Petal03( "torso", "leftUpperArm", "leftLowerArm", "leftHand", "stembottom", "stemtop", "petal03" ),
		Petal( "torso", "leftUpperArm", "leftLowerArm", "leftHand", "stembottom", "stemtop", "petal" ),
		Petal01( "torso", "leftUpperArm", "leftLowerArm", "leftHand", "stembottom", "stemtop", "petal01" ),
		Stemtop( "torso", "leftUpperArm", "leftLowerArm", "leftHand", "stembottom", "stemtop" ),
		Stembottom( "torso", "leftUpperArm", "leftLowerArm", "leftHand", "stembottom" ),
		LeftHand( "torso", "leftUpperArm", "leftLowerArm", "leftHand" ),
		LeftLowerArm( "torso", "leftUpperArm", "leftLowerArm" ),
		LeftUpperArm( "torso", "leftUpperArm" ),
		RightFinger2( "torso", "rightUpperArm", "rightLowerArm", "rightHand", "rightFinger2" ),
		RightFinger3( "torso", "rightUpperArm", "rightLowerArm", "rightHand", "rightFinger3" ),
		RightFinger1( "torso", "rightUpperArm", "rightLowerArm", "rightHand", "rightFinger1" ),
		RightThumb( "torso", "rightUpperArm", "rightLowerArm", "rightHand", "rightThumb" ),
		Jugglingball02( "torso", "rightUpperArm", "rightLowerArm", "rightHand", "jugglingball02" ),
		Jugglingball03( "torso", "rightUpperArm", "rightLowerArm", "rightHand", "jugglingball03" ),
		Jugglingball1( "torso", "rightUpperArm", "rightLowerArm", "rightHand", "jugglingball1" ),
		RightHand( "torso", "rightUpperArm", "rightLowerArm", "rightHand" ),
		RightLowerArm( "torso", "rightUpperArm", "rightLowerArm" ),
		RightUpperArm( "torso", "rightUpperArm" ),
		Hair_male( "torso", "neck", "head", "hair-male" ),
		Hair_female( "torso", "neck", "head", "hair-female" ),
		Nose( "torso", "neck", "head", "nose" ),
		LeftEyelid( "torso", "neck", "head", "leftEye", "leftEyelid" ),
		LeftEye( "torso", "neck", "head", "leftEye" ),
		RightEyelid( "torso", "neck", "head", "rightEye", "rightEyelid" ),
		RightEye( "torso", "neck", "head", "rightEye" ),
		Jaw( "torso", "neck", "head", "jaw" ),
		Hat( "torso", "neck", "head", "hat" ),
		Spinningstars03( "torso", "neck", "head", "spinningstars03" ),
		Spinningstars04( "torso", "neck", "head", "spinningstars04" ),
		Heart( "torso", "neck", "head", "heart" ),
		Spinningstars02( "torso", "neck", "head", "spinningstars02" ),
		Spinningstars1( "torso", "neck", "head", "spinningstars1" ),
		Head( "torso", "neck", "head" ),
		Neck( "torso", "neck" ),
		Bowtie( "torso", "bowtie" ),
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

	public void HandStand( ) {
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					WendolinaTheClown.this.getPart(WendolinaTheClown.Part.LeftUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.5) );
				}
			},
			new Runnable() {
				public void run() {
					WendolinaTheClown.this.getPart(WendolinaTheClown.Part.LeftHand).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25) );
				}
			},
			new Runnable() {
				public void run() {
					WendolinaTheClown.this.getPart(WendolinaTheClown.Part.LeftHand).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25) );
				}
			},
			new Runnable() {
				public void run() {
					WendolinaTheClown.this.getPart(WendolinaTheClown.Part.RightUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.5) );
				}
			},
			new Runnable() {
				public void run() {
					WendolinaTheClown.this.getPart(WendolinaTheClown.Part.RightHand).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25) );
				}
			},
			new Runnable() {
				public void run() {
					WendolinaTheClown.this.getPart(WendolinaTheClown.Part.RightHand).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25) );
				}
			}
		);

		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					WendolinaTheClown.this.turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.5), 1.0, WendolinaTheClown.this.getPart(WendolinaTheClown.Part.Torso) );
				}
			},
			new Runnable() {
				public void run() {
					WendolinaTheClown.this.move( org.alice.apis.moveandturn.MoveDirection.UP, 3.0 );
				}
			},
			new Runnable() {
				public void run() {
					WendolinaTheClown.this.move( org.alice.apis.moveandturn.MoveDirection.UP, 0.08, 1.0, org.alice.apis.moveandturn.AsSeenBy.SCENE );
				}
			}
		);

		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					WendolinaTheClown.this.turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.5), 1.0, WendolinaTheClown.this.getPart(WendolinaTheClown.Part.Torso) );
				}
			},
			new Runnable() {
				public void run() {
					WendolinaTheClown.this.move( org.alice.apis.moveandturn.MoveDirection.DOWN, 3.0 );
				}
			},
			new Runnable() {
				public void run() {
					WendolinaTheClown.this.move( org.alice.apis.moveandturn.MoveDirection.UP, 0.08, 1.0, org.alice.apis.moveandturn.AsSeenBy.SCENE );
				}
			}
		);

		this.standUp( false );
	}

	public void SpinningHandStand( ) {
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					WendolinaTheClown.this.getPart(WendolinaTheClown.Part.LeftUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.5) );
				}
			},
			new Runnable() {
				public void run() {
					WendolinaTheClown.this.getPart(WendolinaTheClown.Part.LeftHand).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25) );
				}
			},
			new Runnable() {
				public void run() {
					WendolinaTheClown.this.getPart(WendolinaTheClown.Part.LeftHand).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25) );
				}
			},
			new Runnable() {
				public void run() {
					WendolinaTheClown.this.getPart(WendolinaTheClown.Part.RightUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.5) );
				}
			},
			new Runnable() {
				public void run() {
					WendolinaTheClown.this.getPart(WendolinaTheClown.Part.RightHand).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25) );
				}
			},
			new Runnable() {
				public void run() {
					WendolinaTheClown.this.getPart(WendolinaTheClown.Part.RightHand).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25) );
				}
			}
		);

		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					WendolinaTheClown.this.turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.5), 1.0, WendolinaTheClown.this.getPart(WendolinaTheClown.Part.Torso) );
				}
			},
			new Runnable() {
				public void run() {
					WendolinaTheClown.this.move( org.alice.apis.moveandturn.MoveDirection.UP, 3.0 );
				}
			},
			new Runnable() {
				public void run() {
					WendolinaTheClown.this.move( org.alice.apis.moveandturn.MoveDirection.UP, 0.08, 1.0, org.alice.apis.moveandturn.AsSeenBy.SCENE );
				}
			}
		);

		for (int index1 = 0; index1 < 3; index1 = index1 + 1) {
			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						// DoInOrder { 
					WendolinaTheClown.this.move( org.alice.apis.moveandturn.MoveDirection.UP, 1.0, 0.5, org.alice.apis.moveandturn.AsSeenBy.SCENE );
					WendolinaTheClown.this.move( org.alice.apis.moveandturn.MoveDirection.DOWN, 1.0, 0.5, org.alice.apis.moveandturn.AsSeenBy.SCENE );
//				}
					}
				},
				new Runnable() {
					public void run() {
						WendolinaTheClown.this.turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(2.0) );
					}
				}
			);

		}

		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					WendolinaTheClown.this.turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.5), 1.0, WendolinaTheClown.this.getPart(WendolinaTheClown.Part.Torso) );
				}
			},
			new Runnable() {
				public void run() {
					WendolinaTheClown.this.move( org.alice.apis.moveandturn.MoveDirection.DOWN, 3.0 );
				}
			},
			new Runnable() {
				public void run() {
					WendolinaTheClown.this.move( org.alice.apis.moveandturn.MoveDirection.UP, 0.08, 1.0, org.alice.apis.moveandturn.AsSeenBy.SCENE );
				}
			}
		);

		this.standUp( false );
	}

	public void TakeABow( ) {
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					WendolinaTheClown.this.getPart(WendolinaTheClown.Part.Torso).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25) );
				}
			},
			new Runnable() {
				public void run() {
					WendolinaTheClown.this.getPart(WendolinaTheClown.Part.Skirt).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05) );
				}
			}
		);

		this.delay(1.0);
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					WendolinaTheClown.this.getPart(WendolinaTheClown.Part.Torso).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25) );
				}
			},
			new Runnable() {
				public void run() {
					WendolinaTheClown.this.getPart(WendolinaTheClown.Part.Skirt).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05) );
				}
			}
		);

	}

	public void LookShocked( ) {
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					WendolinaTheClown.this.getPart(WendolinaTheClown.Part.Torso).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.07) );
				}
			},
			new Runnable() {
				public void run() {
					WendolinaTheClown.this.getPart(WendolinaTheClown.Part.Skirt).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05) );
				}
			},
			new Runnable() {
				public void run() {
					WendolinaTheClown.this.getPart(WendolinaTheClown.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.07) );
				}
			}
		);

		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					WendolinaTheClown.this.getPart(WendolinaTheClown.Part.Jaw).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.2) );
				}
			},
			new Runnable() {
				public void run() {
					WendolinaTheClown.this.getPart(WendolinaTheClown.Part.LeftEye).resize( 1.5, 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					WendolinaTheClown.this.getPart(WendolinaTheClown.Part.RightEye).resize( 1.5, 0.5 );
				}
			}
		);

		this.delay(0.5);
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					WendolinaTheClown.this.getPart(WendolinaTheClown.Part.LeftEye).resize( 0.6666666666666666, 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					WendolinaTheClown.this.getPart(WendolinaTheClown.Part.RightEye).resize( 0.6666666666666666, 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					WendolinaTheClown.this.standUp( false );
				}
			}
		);

	}
}
