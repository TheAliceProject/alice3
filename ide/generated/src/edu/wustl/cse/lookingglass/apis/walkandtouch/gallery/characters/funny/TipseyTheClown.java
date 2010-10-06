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

public class TipseyTheClown extends edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.GalleryPerson { 
	edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Boolean> SpotlightOn= new edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Boolean>( false );  
	edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Boolean> StillBusyCL= new edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Boolean>( false );  
	public TipseyTheClown() {
		super( "funny/Tipsey the Clown" );
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
		Torso( "torso" ),
		JuggleCenter( "juggleCenter" );
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

	public void HideInPants( ) {
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					TipseyTheClown.this.touch( TipseyTheClown.this.getPart(TipseyTheClown.Part.LeftUpperArm) );
				}
			},
			new Runnable() {
				public void run() {
					TipseyTheClown.this.touch( TipseyTheClown.this.getPart(TipseyTheClown.Part.RightUpperArm), org.alice.apis.moveandturn.MoveDirection.FORWARD, edu.wustl.cse.lookingglass.apis.walkandtouch.TouchPart.LEFT_HAND );
				}
			}
		);

		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					TipseyTheClown.this.getPart(TipseyTheClown.Part.Torso).resize( 0.8 );
				}
			},
			new Runnable() {
				public void run() {
					TipseyTheClown.this.getPart(TipseyTheClown.Part.HoopPants).resizeHeight( 3.3, 2.0, org.alice.apis.moveandturn.ResizePolicy.PRESERVE_NOTHING  );
				}
			},
			new Runnable() {
				public void run() {
					TipseyTheClown.this.getPart(TipseyTheClown.Part.HoopPants).move( org.alice.apis.moveandturn.MoveDirection.UP, 0.6, 2.0 );
				}
			}
		);

		this.getPart(TipseyTheClown.Part.Head).move( org.alice.apis.moveandturn.MoveDirection.UP, 0.2 );
		this.delay(1.0);
		this.getPart(TipseyTheClown.Part.Head).move( org.alice.apis.moveandturn.MoveDirection.DOWN, 0.2 );
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					TipseyTheClown.this.getPart(TipseyTheClown.Part.Torso).resize( 1.25 );
				}
			},
			new Runnable() {
				public void run() {
					TipseyTheClown.this.getPart(TipseyTheClown.Part.HoopPants).resizeHeight( 0.30303030303030304, 2.0, org.alice.apis.moveandturn.ResizePolicy.PRESERVE_NOTHING  );
				}
			},
			new Runnable() {
				public void run() {
					TipseyTheClown.this.getPart(TipseyTheClown.Part.HoopPants).move( org.alice.apis.moveandturn.MoveDirection.DOWN, 0.6, 2.0 );
				}
			}
		);

		this.standUp( false );
	}

	public void SpinBowTie( final Number HowLong) {
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					TipseyTheClown.this.getPart(TipseyTheClown.Part.LeftUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					TipseyTheClown.this.getPart(TipseyTheClown.Part.LeftLowerArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.35), 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					TipseyTheClown.this.getPart(TipseyTheClown.Part.RightUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					TipseyTheClown.this.getPart(TipseyTheClown.Part.RightLowerArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					TipseyTheClown.this.getPart(TipseyTheClown.Part.Bowtie).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions((HowLong.doubleValue() * 10.0)), HowLong.doubleValue() );
				}
			},
			new Runnable() {
				public void run() {
					TipseyTheClown.this.move( org.alice.apis.moveandturn.MoveDirection.BACKWARD, (0.25 * HowLong.doubleValue()), HowLong.doubleValue() );
				}
			}
		);

		this.standUp( false );
	}

	public void DoTheSplits( ) {
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					TipseyTheClown.this.getPart(TipseyTheClown.Part.LeftUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25) );
				}
			},
			new Runnable() {
				public void run() {
					TipseyTheClown.this.getPart(TipseyTheClown.Part.RightUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25) );
				}
			},
			new Runnable() {
				public void run() {
					TipseyTheClown.this.getPart(TipseyTheClown.Part.LeftUpperArm).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.42) );
				}
			},
			new Runnable() {
				public void run() {
					TipseyTheClown.this.getPart(TipseyTheClown.Part.RightUpperArm).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.35) );
				}
			}
		);

		this.move( org.alice.apis.moveandturn.MoveDirection.DOWN, this.getDistanceAbove(this.getGround()), 0.25 );
		this.delay(1.0);
		this.standUp( false );
	}

	public void TipHat( ) {
		this.touch( this.getPart(TipseyTheClown.Part.Hat), org.alice.apis.moveandturn.MoveDirection.UP );
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					TipseyTheClown.this.keepTouching( TipseyTheClown.this.getPart(TipseyTheClown.Part.Hat), org.alice.apis.moveandturn.MoveDirection.UP, edu.wustl.cse.lookingglass.apis.walkandtouch.TouchPart.RIGHT_HAND, 0.0, 2.0 );
				}
			},
			new Runnable() {
				public void run() {
					// DoInOrder { 
				TipseyTheClown.this.delay(1.0);
				TipseyTheClown.this.getPart(TipseyTheClown.Part.Hat).move( org.alice.apis.moveandturn.MoveDirection.UP, 0.1, 0.25 );
				TipseyTheClown.this.getPart(TipseyTheClown.Part.Hat).move( org.alice.apis.moveandturn.MoveDirection.DOWN, 0.1, 0.25 );
//			}
				}
			}
		);

		this.standUp( false );
	}
}
