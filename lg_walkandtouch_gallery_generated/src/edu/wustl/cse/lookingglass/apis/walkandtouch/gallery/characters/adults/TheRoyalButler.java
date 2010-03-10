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

public class TheRoyalButler extends edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.GalleryPerson { 
	public TheRoyalButler() {
		super( "adults/The Royal Butler" );
}
	public enum Part {
		RightThumb( "hips", "torso", "rightUpperArm", "rightLowerArm", "rightHand", "rightThumb" ),
		RightFinger1( "hips", "torso", "rightUpperArm", "rightLowerArm", "rightHand", "rightFinger1" ),
		RightFinger3( "hips", "torso", "rightUpperArm", "rightLowerArm", "rightHand", "rightFinger3" ),
		RightFinger2( "hips", "torso", "rightUpperArm", "rightLowerArm", "rightHand", "rightFinger2" ),
		RightHand( "hips", "torso", "rightUpperArm", "rightLowerArm", "rightHand" ),
		RightLowerArm( "hips", "torso", "rightUpperArm", "rightLowerArm" ),
		RightUpperArm( "hips", "torso", "rightUpperArm" ),
		Sceptor( "hips", "torso", "leftUpperArm", "leftLowerArm", "leftHand", "sceptor" ),
		LeftFinger3( "hips", "torso", "leftUpperArm", "leftLowerArm", "leftHand", "leftFinger3" ),
		LeftFinger2( "hips", "torso", "leftUpperArm", "leftLowerArm", "leftHand", "leftFinger2" ),
		LeftFinger1( "hips", "torso", "leftUpperArm", "leftLowerArm", "leftHand", "leftFinger1" ),
		LeftThumb( "hips", "torso", "leftUpperArm", "leftLowerArm", "leftHand", "leftThumb" ),
		LeftHand( "hips", "torso", "leftUpperArm", "leftLowerArm", "leftHand" ),
		LeftLowerArm( "hips", "torso", "leftUpperArm", "leftLowerArm" ),
		LeftUpperArm( "hips", "torso", "leftUpperArm" ),
		Crown( "hips", "torso", "neck", "head", "crown" ),
		Jaw( "hips", "torso", "neck", "head", "jaw" ),
		Nose( "hips", "torso", "neck", "head", "nose" ),
		RightEyelid( "hips", "torso", "neck", "head", "rightEye", "rightEyelid" ),
		RightEye( "hips", "torso", "neck", "head", "rightEye" ),
		LeftEyelid( "hips", "torso", "neck", "head", "leftEye", "leftEyelid" ),
		LeftEye( "hips", "torso", "neck", "head", "leftEye" ),
		Head( "hips", "torso", "neck", "head" ),
		Neck( "hips", "torso", "neck" ),
		Torso( "hips", "torso" ),
		Hips( "hips" ),
		LeftFoot( "leftUpperLeg", "leftLowerLeg", "leftFoot" ),
		LeftLowerLeg( "leftUpperLeg", "leftLowerLeg" ),
		LeftUpperLeg( "leftUpperLeg" ),
		RightFoot( "rightUpperLeg", "rightLowerLeg", "rightFoot" ),
		RightLowerLeg( "rightUpperLeg", "rightLowerLeg" ),
		RightUpperLeg( "rightUpperLeg" );
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

	public void UsherIn( ) {
		// DoInOrder { 
			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						TheRoyalButler.this.getPart(TheRoyalButler.Part.Torso).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05) );
					}
				},
				new Runnable() {
					public void run() {
						TheRoyalButler.this.getPart(TheRoyalButler.Part.RightUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1) );
					}
				},
				new Runnable() {
					public void run() {
						TheRoyalButler.this.getPart(TheRoyalButler.Part.RightUpperArm).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.05) );
					}
				},
				new Runnable() {
					public void run() {
						TheRoyalButler.this.getPart(TheRoyalButler.Part.RightLowerArm).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25) );
					}
				},
				new Runnable() {
					public void run() {
						TheRoyalButler.this.getPart(TheRoyalButler.Part.RightLowerArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.07) );
					}
				},
				new Runnable() {
					public void run() {
						TheRoyalButler.this.getPart(TheRoyalButler.Part.LeftUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1) );
					}
				},
				new Runnable() {
					public void run() {
						TheRoyalButler.this.getPart(TheRoyalButler.Part.LeftUpperArm).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.03) );
					}
				},
				new Runnable() {
					public void run() {
						TheRoyalButler.this.getPart(TheRoyalButler.Part.LeftLowerArm).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1) );
					}
				}
			);

			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						TheRoyalButler.this.getPart(TheRoyalButler.Part.Neck).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.07), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						TheRoyalButler.this.getPart(TheRoyalButler.Part.RightUpperArm).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						TheRoyalButler.this.getPart(TheRoyalButler.Part.LeftUpperArm).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.02), 0.5 );
					}
				}
			);

			this.delay(1.0);
			this.standUp( false );
//		}

	}

	public void Ogle( ) {
		this.standUp( false, 0.25 );
		// DoInOrder { 
			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						TheRoyalButler.this.getPart(TheRoyalButler.Part.Torso).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.08), 0.25 );
					}
				},
				new Runnable() {
					public void run() {
						TheRoyalButler.this.getPart(TheRoyalButler.Part.RightEye).resize( 2.0, 0.25 );
					}
				},
				new Runnable() {
					public void run() {
						TheRoyalButler.this.getPart(TheRoyalButler.Part.LeftEye).resize( 2.0, 0.25 );
					}
				},
				new Runnable() {
					public void run() {
						TheRoyalButler.this.getPart(TheRoyalButler.Part.Jaw).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.25 );
					}
				},
				new Runnable() {
					public void run() {
						TheRoyalButler.this.getPart(TheRoyalButler.Part.Neck).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.08), 0.25 );
					}
				}
			);

			this.delay(0.12);
			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						TheRoyalButler.this.getPart(TheRoyalButler.Part.RightEye).resize( 0.5, 0.25 );
					}
				},
				new Runnable() {
					public void run() {
						TheRoyalButler.this.getPart(TheRoyalButler.Part.LeftEye).resize( 0.5, 0.25 );
					}
				}
			);

//		}

		// DoInOrder { 
			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						TheRoyalButler.this.getPart(TheRoyalButler.Part.RightEye).resize( 3.0, 0.1 );
					}
				},
				new Runnable() {
					public void run() {
						TheRoyalButler.this.getPart(TheRoyalButler.Part.LeftEye).resize( 3.0, 0.1 );
					}
				}
			);

			this.delay(0.08);
			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						TheRoyalButler.this.getPart(TheRoyalButler.Part.Neck).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.08), 0.3333333333333333 );
					}
				},
				new Runnable() {
					public void run() {
						TheRoyalButler.this.getPart(TheRoyalButler.Part.Torso).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.08), 0.3333333333333333 );
					}
				},
				new Runnable() {
					public void run() {
						TheRoyalButler.this.getPart(TheRoyalButler.Part.Jaw).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.3333333333333333 );
					}
				},
				new Runnable() {
					public void run() {
						TheRoyalButler.this.getPart(TheRoyalButler.Part.RightEye).resize( 0.3333333333333333, 0.3333333333333333 );
					}
				},
				new Runnable() {
					public void run() {
						TheRoyalButler.this.getPart(TheRoyalButler.Part.LeftEye).resize( 0.3333333333333333, 0.3333333333333333 );
					}
				}
			);

//		}

		this.standUp( false, 0.25 );
	}
}
