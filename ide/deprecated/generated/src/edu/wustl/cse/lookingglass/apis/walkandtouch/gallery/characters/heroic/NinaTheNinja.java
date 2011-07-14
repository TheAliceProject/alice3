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
package edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.characters.heroic;

public class NinaTheNinja extends edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.GalleryPerson { 
	public NinaTheNinja() {
		super( "heroic/NinaTheNinja" );
}
	public enum Part {
		RightFoot( "hips", "rightUpperLeg", "rightLowerLeg", "rightFoot" ),
		RightLowerLeg( "hips", "rightUpperLeg", "rightLowerLeg" ),
		RightUpperLeg( "hips", "rightUpperLeg" ),
		LeftFoot( "hips", "leftUpperLeg", "leftLowerLeg", "leftFoot" ),
		LeftLowerLeg( "hips", "leftUpperLeg", "leftLowerLeg" ),
		LeftUpperLeg( "hips", "leftUpperLeg" ),
		Hips( "hips" ),
		RightFingers( "torso", "rightUpperArm", "rightLowerArm", "rightHand", "rightFingers" ),
		RightThumb( "torso", "rightUpperArm", "rightLowerArm", "rightHand", "rightThumb" ),
		RightHand( "torso", "rightUpperArm", "rightLowerArm", "rightHand" ),
		RightLowerArm( "torso", "rightUpperArm", "rightLowerArm" ),
		RightUpperArm( "torso", "rightUpperArm" ),
		LeftFingers( "torso", "leftUpperArm", "leftLowerArm", "leftHand", "leftFingers" ),
		LeftThumb( "torso", "leftUpperArm", "leftLowerArm", "leftHand", "leftThumb" ),
		DistractingStick( "torso", "leftUpperArm", "leftLowerArm", "leftHand", "distractingStick" ),
		LeftHand( "torso", "leftUpperArm", "leftLowerArm", "leftHand" ),
		LeftLowerArm( "torso", "leftUpperArm", "leftLowerArm" ),
		LeftUpperArm( "torso", "leftUpperArm" ),
		Facemask( "torso", "neck", "head", "facemask" ),
		RightEyebrow( "torso", "neck", "head", "rightEyebrow" ),
		LeftEyebrow( "torso", "neck", "head", "leftEyebrow" ),
		RightEye( "torso", "neck", "head", "rightEye" ),
		LeftEye( "torso", "neck", "head", "leftEye" ),
		Ponytail( "torso", "neck", "head", "ponytail" ),
		Spinningstars03( "torso", "neck", "head", "spinningstars03" ),
		Spinningstars01( "torso", "neck", "head", "spinningstars01" ),
		Spinningstars04( "torso", "neck", "head", "spinningstars04" ),
		Spinningstars02( "torso", "neck", "head", "spinningstars02" ),
		Head( "torso", "neck", "head" ),
		Neck( "torso", "neck" ),
		Collar( "torso", "collar" ),
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

	public void DoNinjaMove( ) {
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					// DoInOrder { 
				org.alice.virtualmachine.DoTogether.invokeAndWait(
					new Runnable() {
						public void run() {
							NinaTheNinja.this.getPart(NinaTheNinja.Part.LeftUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.15 );
						}
					},
					new Runnable() {
						public void run() {
							NinaTheNinja.this.getPart(NinaTheNinja.Part.RightLowerLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.15 );
						}
					}
				);

				org.alice.virtualmachine.DoTogether.invokeAndWait(
					new Runnable() {
						public void run() {
							// DoInOrder { 
						org.alice.virtualmachine.DoTogether.invokeAndWait(
							new Runnable() {
								public void run() {
									NinaTheNinja.this.turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(6.0) );
								}
							},
							new Runnable() {
								public void run() {
									NinaTheNinja.this.getPart(NinaTheNinja.Part.RightUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.12) );
								}
							},
							new Runnable() {
								public void run() {
									NinaTheNinja.this.getPart(NinaTheNinja.Part.LeftUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.12) );
								}
							}
						);

						org.alice.virtualmachine.DoTogether.invokeAndWait(
							new Runnable() {
								public void run() {
									NinaTheNinja.this.turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(6.0) );
								}
							},
							new Runnable() {
								public void run() {
									NinaTheNinja.this.getPart(NinaTheNinja.Part.RightUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.12) );
								}
							},
							new Runnable() {
								public void run() {
									NinaTheNinja.this.getPart(NinaTheNinja.Part.LeftUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.12) );
								}
							}
						);

//					}
						}
					}
				);

				NinaTheNinja.this.standUp( false );
//			}
				}
			},
			new Runnable() {
				public void run() {
					//NinaTheNinja.this.playSound(sound)
				}
			}
		);

	}

	public void Surrender( ) {
		this.standUp( false );
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					NinaTheNinja.this.touch( NinaTheNinja.this.getPart(NinaTheNinja.Part.RightEye), org.alice.apis.moveandturn.MoveDirection.RIGHT, edu.wustl.cse.lookingglass.apis.walkandtouch.TouchPart.RIGHT_HAND, -0.25 );
				}
			},
			new Runnable() {
				public void run() {
					NinaTheNinja.this.touch( NinaTheNinja.this.getPart(NinaTheNinja.Part.LeftEye), org.alice.apis.moveandturn.MoveDirection.LEFT, edu.wustl.cse.lookingglass.apis.walkandtouch.TouchPart.LEFT_HAND, -0.25 );
				}
			}
		);

		this.getPart(NinaTheNinja.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.06) );
		this.getPart(NinaTheNinja.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.12) );
		this.getPart(NinaTheNinja.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.06) );
		this.standUp( false );
	}

	public void JumpOver( ) {
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					// DoInOrder { 
				NinaTheNinja.this.delay(0.25);
				//NinaTheNinja.this.playSound(sound)
//			}
				}
			},
			new Runnable() {
				public void run() {
					NinaTheNinja.this.move( org.alice.apis.moveandturn.MoveDirection.FORWARD, 1.0, 1.0, org.alice.apis.moveandturn.AsSeenBy.SELF );
				}
			},
			new Runnable() {
				public void run() {
					// DoInOrder { 
				NinaTheNinja.this.move( org.alice.apis.moveandturn.MoveDirection.UP, 0.6, 0.5, org.alice.apis.moveandturn.AsSeenBy.SCENE );
				NinaTheNinja.this.delay(0.1);
				NinaTheNinja.this.move( org.alice.apis.moveandturn.MoveDirection.DOWN, 0.6, 0.5, org.alice.apis.moveandturn.AsSeenBy.SCENE );
//			}
				}
			},
			new Runnable() {
				public void run() {
					// DoInOrder { 
				NinaTheNinja.this.delay(0.4);
				NinaTheNinja.this.turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(1.0), 0.25, NinaTheNinja.this.getPart(NinaTheNinja.Part.Torso) );
				NinaTheNinja.this.delay(0.5);
//			}
				}
			},
			new Runnable() {
				public void run() {
					// DoInOrder { 
				org.alice.virtualmachine.DoTogether.invokeAndWait(
					new Runnable() {
						public void run() {
							NinaTheNinja.this.getPart(NinaTheNinja.Part.RightUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.5 );
						}
					},
					new Runnable() {
						public void run() {
							NinaTheNinja.this.getPart(NinaTheNinja.Part.LeftUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.5 );
						}
					},
					new Runnable() {
						public void run() {
							NinaTheNinja.this.getPart(NinaTheNinja.Part.RightLowerLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.5 );
						}
					},
					new Runnable() {
						public void run() {
							NinaTheNinja.this.getPart(NinaTheNinja.Part.LeftLowerLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.5 );
						}
					}
				);

				NinaTheNinja.this.delay(0.25);
				NinaTheNinja.this.standUp( false, 0.25 );
//			}
				}
			}
		);

	}
}
