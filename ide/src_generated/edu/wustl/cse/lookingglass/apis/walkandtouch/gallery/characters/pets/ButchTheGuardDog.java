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
package edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.characters.pets;

public class ButchTheGuardDog extends edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.GalleryCharacter { 
	public ButchTheGuardDog() {
		super( "pets/ButchTheGuardDog" );
}
	public enum Part {
		Cane( "body", "frontLeftUpperLeg", "frontLeftLowerLeg", "frontLeftFoot", "cane" ),
		FrontLeftFoot( "body", "frontLeftUpperLeg", "frontLeftLowerLeg", "frontLeftFoot" ),
		FrontLeftLowerLeg( "body", "frontLeftUpperLeg", "frontLeftLowerLeg" ),
		FrontLeftUpperLeg( "body", "frontLeftUpperLeg" ),
		FrontRightFoot( "body", "frontRightUpperLeg", "frontRightLowerLeg", "frontRightFoot" ),
		FrontRightLowerLeg( "body", "frontRightUpperLeg", "frontRightLowerLeg" ),
		FrontRightUpperLeg( "body", "frontRightUpperLeg" ),
		Jaw( "body", "head", "jaw" ),
		RightEar( "body", "head", "rightEar" ),
		LeftEar( "body", "head", "leftEar" ),
		TopHat( "body", "head", "topHat" ),
		LeftEyelid( "body", "head", "leftEye", "leftEyelid" ),
		LeftEye( "body", "head", "leftEye" ),
		RightEyelid( "body", "head", "rightEye", "rightEyelid" ),
		RightEye( "body", "head", "rightEye" ),
		Thought( "body", "head", "bubble1", "bubble2", "bubble3", "thought" ),
		Bubble3( "body", "head", "bubble1", "bubble2", "bubble3" ),
		Bubble2( "body", "head", "bubble1", "bubble2" ),
		Bubble1( "body", "head", "bubble1" ),
		Head( "body", "head" ),
		Tail( "body", "tail" ),
		Body( "body" ),
		RearRightFoot( "rearRightUpperLeg", "rearRightLowerLeg", "rearRightFoot" ),
		RearRightLowerLeg( "rearRightUpperLeg", "rearRightLowerLeg" ),
		RearRightUpperLeg( "rearRightUpperLeg" ),
		RearLeftFoot( "rearLeftUpperLeg", "rearLeftLowerLeg", "rearLeftFoot" ),
		RearLeftLowerLeg( "rearLeftUpperLeg", "rearLeftLowerLeg" ),
		RearLeftUpperLeg( "rearLeftUpperLeg" );
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

	public void Bark( ) {
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					//ButchTheGuardDog.this.playSound(sound)
				}
			},
			new Runnable() {
				public void run() {
					// DoInOrder { 
				// DoInOrder { 
					org.alice.virtualmachine.DoTogether.invokeAndWait(
						new Runnable() {
							public void run() {
								ButchTheGuardDog.this.getPart(ButchTheGuardDog.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.25 );
							}
						},
						new Runnable() {
							public void run() {
								ButchTheGuardDog.this.getPart(ButchTheGuardDog.Part.Jaw).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.25 );
							}
						}
					);

					org.alice.virtualmachine.DoTogether.invokeAndWait(
						new Runnable() {
							public void run() {
								ButchTheGuardDog.this.getPart(ButchTheGuardDog.Part.Jaw).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.25 );
							}
						},
						new Runnable() {
							public void run() {
								ButchTheGuardDog.this.getPart(ButchTheGuardDog.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.25 );
							}
						}
					);

//				}

				ButchTheGuardDog.this.delay(0.65);
				// DoInOrder { 
					org.alice.virtualmachine.DoTogether.invokeAndWait(
						new Runnable() {
							public void run() {
								ButchTheGuardDog.this.getPart(ButchTheGuardDog.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.25 );
							}
						},
						new Runnable() {
							public void run() {
								ButchTheGuardDog.this.getPart(ButchTheGuardDog.Part.Jaw).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.25 );
							}
						}
					);

					org.alice.virtualmachine.DoTogether.invokeAndWait(
						new Runnable() {
							public void run() {
								ButchTheGuardDog.this.getPart(ButchTheGuardDog.Part.Jaw).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.25 );
							}
						},
						new Runnable() {
							public void run() {
								ButchTheGuardDog.this.getPart(ButchTheGuardDog.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.25 );
							}
						}
					);

//				}

				// DoInOrder { 
					org.alice.virtualmachine.DoTogether.invokeAndWait(
						new Runnable() {
							public void run() {
								ButchTheGuardDog.this.getPart(ButchTheGuardDog.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.25 );
							}
						},
						new Runnable() {
							public void run() {
								ButchTheGuardDog.this.getPart(ButchTheGuardDog.Part.Jaw).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.25 );
							}
						}
					);

					org.alice.virtualmachine.DoTogether.invokeAndWait(
						new Runnable() {
							public void run() {
								ButchTheGuardDog.this.getPart(ButchTheGuardDog.Part.Jaw).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.25 );
							}
						},
						new Runnable() {
							public void run() {
								ButchTheGuardDog.this.getPart(ButchTheGuardDog.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.25 );
							}
						}
					);

//				}

//			}
				}
			}
		);

	}

	public void WagTail( ) {
		this.getPart(ButchTheGuardDog.Part.Tail).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.25 );
		for (int index1 = 0; index1 < 2; index1 = index1 + 1) {
			this.getPart(ButchTheGuardDog.Part.Tail).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.5 );
			this.getPart(ButchTheGuardDog.Part.Tail).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.5 );
		}

		this.getPart(ButchTheGuardDog.Part.Tail).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.25 );
	}

	public void Eat( ) {
		this.getPart(ButchTheGuardDog.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.5 );
		for (int index1 = 0; index1 < 2; index1 = index1 + 1) {
			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						//ButchTheGuardDog.this.playSound(sound)
					}
				},
				new Runnable() {
					public void run() {
						// DoInOrder { 
					ButchTheGuardDog.this.getPart(ButchTheGuardDog.Part.Jaw).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.06), 0.25 );
					ButchTheGuardDog.this.getPart(ButchTheGuardDog.Part.Jaw).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.06), 0.25 );
//				}
					}
				}
			);

		}

		this.getPart(ButchTheGuardDog.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.5 );
	}
}
