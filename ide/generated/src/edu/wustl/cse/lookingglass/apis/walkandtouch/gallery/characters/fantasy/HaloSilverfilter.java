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

public class HaloSilverfilter extends edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.GalleryPerson { 
	public HaloSilverfilter() {
		super( "fantasy/HaloSilverfilter" );
}
	public enum Part {
		LeftFoot( "leftUpperLeg", "leftLowerLeg", "leftFoot" ),
		LeftLowerLeg( "leftUpperLeg", "leftLowerLeg" ),
		LeftUpperLeg( "leftUpperLeg" ),
		RightFoot( "rightUpperLeg", "rightLowerLeg", "rightFoot" ),
		RightLowerLeg( "rightUpperLeg", "rightLowerLeg" ),
		RightUpperLeg( "rightUpperLeg" ),
		RightTopWing( "torso", "chest", "rightTopWing" ),
		RightBottomWing( "torso", "chest", "rightBottomWing" ),
		LeftTopWing( "torso", "chest", "leftTopWing" ),
		LeftBottomWing( "torso", "chest", "leftBottomWing" ),
		LeftEye( "torso", "chest", "neck", "head", "leftEye" ),
		RightEye( "torso", "chest", "neck", "head", "rightEye" ),
		LeftLowerEyelids( "torso", "chest", "neck", "head", "leftEyelids", "leftLowerEyelids" ),
		LeftUpperEyelids( "torso", "chest", "neck", "head", "leftEyelids", "leftUpperEyelids" ),
		LeftEyelids( "torso", "chest", "neck", "head", "leftEyelids" ),
		RightUpperEyelids( "torso", "chest", "neck", "head", "rightEyelids", "rightUpperEyelids" ),
		RightLowerEyelids( "torso", "chest", "neck", "head", "rightEyelids", "rightLowerEyelids" ),
		RightEyelids( "torso", "chest", "neck", "head", "rightEyelids" ),
		LeftBrow( "torso", "chest", "neck", "head", "leftBrow" ),
		RightBrow( "torso", "chest", "neck", "head", "rightBrow" ),
		LeftEar( "torso", "chest", "neck", "head", "leftEar" ),
		RightEar( "torso", "chest", "neck", "head", "rightEar" ),
		Head( "torso", "chest", "neck", "head" ),
		Neck( "torso", "chest", "neck" ),
		RightHand( "torso", "chest", "rightSleeve", "rightUpperArm", "rightLowerArm", "rightHand" ),
		RightLowerArm( "torso", "chest", "rightSleeve", "rightUpperArm", "rightLowerArm" ),
		RightUpperArm( "torso", "chest", "rightSleeve", "rightUpperArm" ),
		RightSleeve( "torso", "chest", "rightSleeve" ),
		LeftHand( "torso", "chest", "leftSleeve", "leftUpperArm", "leftLowerArm", "leftHand" ),
		LeftLowerArm( "torso", "chest", "leftSleeve", "leftUpperArm", "leftLowerArm" ),
		LeftUpperArm( "torso", "chest", "leftSleeve", "leftUpperArm" ),
		LeftSleeve( "torso", "chest", "leftSleeve" ),
		Chest( "torso", "chest" ),
		Clothes( "torso", "clothes" ),
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

	public void Wink( ) {
		//Close eyelids
		this.getPart(HaloSilverfilter.Part.RightUpperEyelids).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.125), 0.25 );
		//Open eyelids
		this.getPart(HaloSilverfilter.Part.RightUpperEyelids).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(-0.125), 0.25 );
	}

	public void LookEmbarrassed( ) {
		this.getPart(HaloSilverfilter.Part.Head).setColor( new org.alice.apis.moveandturn.Color(1.0f, 0.4f, 0.4f) );
	}

	public void LookAngry( ) {
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					//lower the eyebrows
				}
			},
			new Runnable() {
				public void run() {
					HaloSilverfilter.this.getPart(HaloSilverfilter.Part.LeftBrow).move( org.alice.apis.moveandturn.MoveDirection.DOWN, (HaloSilverfilter.this.getPart(HaloSilverfilter.Part.LeftBrow).getHeight() * 0.5) );
				}
			},
			new Runnable() {
				public void run() {
					HaloSilverfilter.this.getPart(HaloSilverfilter.Part.LeftBrow).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.02) );
				}
			},
			new Runnable() {
				public void run() {
					HaloSilverfilter.this.getPart(HaloSilverfilter.Part.RightBrow).move( org.alice.apis.moveandturn.MoveDirection.DOWN, (HaloSilverfilter.this.getPart(HaloSilverfilter.Part.RightBrow).getHeight() * 0.5) );
				}
			},
			new Runnable() {
				public void run() {
					HaloSilverfilter.this.getPart(HaloSilverfilter.Part.RightBrow).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.02) );
				}
			},
			new Runnable() {
				public void run() {
					//narrow the eyelids
				}
			},
			new Runnable() {
				public void run() {
					HaloSilverfilter.this.getPart(HaloSilverfilter.Part.LeftUpperEyelids).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.02) );
				}
			},
			new Runnable() {
				public void run() {
					HaloSilverfilter.this.getPart(HaloSilverfilter.Part.RightUpperEyelids).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.02) );
				}
			},
			new Runnable() {
				public void run() {
					HaloSilverfilter.this.getPart(HaloSilverfilter.Part.LeftEyelids).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.01) );
				}
			},
			new Runnable() {
				public void run() {
					HaloSilverfilter.this.getPart(HaloSilverfilter.Part.RightEyelids).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.01) );
				}
			}
		);

	}

	public void Blink( ) {
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					//close eyelids
				}
			},
			new Runnable() {
				public void run() {
					HaloSilverfilter.this.getPart(HaloSilverfilter.Part.RightUpperEyelids).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.125), 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					HaloSilverfilter.this.getPart(HaloSilverfilter.Part.LeftUpperEyelids).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.125), 0.25 );
				}
			}
		);

		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					//open eyelids
				}
			},
			new Runnable() {
				public void run() {
					HaloSilverfilter.this.getPart(HaloSilverfilter.Part.RightUpperEyelids).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(-0.125), 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					HaloSilverfilter.this.getPart(HaloSilverfilter.Part.LeftUpperEyelids).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(-0.125), 0.25 );
				}
			}
		);

	}

	public void Shrug( ) {
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					//raise arms to shoulder height, and cock head slightly
				}
			},
			new Runnable() {
				public void run() {
					HaloSilverfilter.this.getPart(HaloSilverfilter.Part.Head).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.03), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					HaloSilverfilter.this.getPart(HaloSilverfilter.Part.RightUpperArm).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					HaloSilverfilter.this.getPart(HaloSilverfilter.Part.RightLowerArm).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					HaloSilverfilter.this.getPart(HaloSilverfilter.Part.RightHand).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.4), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					HaloSilverfilter.this.getPart(HaloSilverfilter.Part.LeftUpperArm).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					HaloSilverfilter.this.getPart(HaloSilverfilter.Part.LeftLowerArm).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					HaloSilverfilter.this.getPart(HaloSilverfilter.Part.LeftHand).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.4), 0.5 );
				}
			}
		);

		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					//shrug shoulders up
				}
			},
			new Runnable() {
				public void run() {
					HaloSilverfilter.this.getPart(HaloSilverfilter.Part.RightSleeve).move( org.alice.apis.moveandturn.MoveDirection.UP, HaloSilverfilter.this.getPart(HaloSilverfilter.Part.LeftBrow).getHeight(), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					HaloSilverfilter.this.getPart(HaloSilverfilter.Part.LeftSleeve).move( org.alice.apis.moveandturn.MoveDirection.UP, HaloSilverfilter.this.getPart(HaloSilverfilter.Part.LeftBrow).getHeight(), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					HaloSilverfilter.this.getPart(HaloSilverfilter.Part.Head).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.01), 0.5 );
				}
			}
		);

		this.delay(0.25);
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					//shrug shoulders down
				}
			},
			new Runnable() {
				public void run() {
					HaloSilverfilter.this.getPart(HaloSilverfilter.Part.RightSleeve).move( org.alice.apis.moveandturn.MoveDirection.DOWN, HaloSilverfilter.this.getPart(HaloSilverfilter.Part.LeftBrow).getHeight(), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					HaloSilverfilter.this.getPart(HaloSilverfilter.Part.LeftSleeve).move( org.alice.apis.moveandturn.MoveDirection.DOWN, HaloSilverfilter.this.getPart(HaloSilverfilter.Part.LeftBrow).getHeight(), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					HaloSilverfilter.this.getPart(HaloSilverfilter.Part.Head).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.01), 0.5 );
				}
			}
		);

		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					//lower arms and straighten head
				}
			},
			new Runnable() {
				public void run() {
					HaloSilverfilter.this.getPart(HaloSilverfilter.Part.Head).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.03), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					HaloSilverfilter.this.getPart(HaloSilverfilter.Part.RightUpperArm).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					HaloSilverfilter.this.getPart(HaloSilverfilter.Part.RightLowerArm).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					HaloSilverfilter.this.getPart(HaloSilverfilter.Part.RightHand).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.4), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					HaloSilverfilter.this.getPart(HaloSilverfilter.Part.LeftUpperArm).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					HaloSilverfilter.this.getPart(HaloSilverfilter.Part.LeftLowerArm).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					HaloSilverfilter.this.getPart(HaloSilverfilter.Part.LeftHand).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.4), 0.5 );
				}
			}
		);

	}

	public void LookAsleep( ) {
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					//close eyelids
				}
			},
			new Runnable() {
				public void run() {
					HaloSilverfilter.this.getPart(HaloSilverfilter.Part.RightUpperEyelids).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.125), 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					HaloSilverfilter.this.getPart(HaloSilverfilter.Part.LeftUpperEyelids).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.125), 0.25 );
				}
			}
		);

	}

	public void Slouch( ) {
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					//bend over
				}
			},
			new Runnable() {
				public void run() {
					HaloSilverfilter.this.getPart(HaloSilverfilter.Part.Neck).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 1.5 );
				}
			},
			new Runnable() {
				public void run() {
					HaloSilverfilter.this.getPart(HaloSilverfilter.Part.Torso).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 1.5 );
				}
			},
			new Runnable() {
				public void run() {
					HaloSilverfilter.this.getPart(HaloSilverfilter.Part.RightUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 1.5 );
				}
			},
			new Runnable() {
				public void run() {
					HaloSilverfilter.this.getPart(HaloSilverfilter.Part.LeftUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 1.5 );
				}
			},
			new Runnable() {
				public void run() {
					HaloSilverfilter.this.getPart(HaloSilverfilter.Part.RightTopWing).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 1.5 );
				}
			},
			new Runnable() {
				public void run() {
					HaloSilverfilter.this.getPart(HaloSilverfilter.Part.RightBottomWing).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 1.5 );
				}
			},
			new Runnable() {
				public void run() {
					HaloSilverfilter.this.getPart(HaloSilverfilter.Part.LeftTopWing).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 1.5 );
				}
			},
			new Runnable() {
				public void run() {
					HaloSilverfilter.this.getPart(HaloSilverfilter.Part.LeftBottomWing).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 1.5 );
				}
			},
			new Runnable() {
				public void run() {
					// DoInOrder { 
				//shake head in shame
				HaloSilverfilter.this.getPart(HaloSilverfilter.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.25 );
				HaloSilverfilter.this.getPart(HaloSilverfilter.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.5 );
				HaloSilverfilter.this.getPart(HaloSilverfilter.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.5 );
				HaloSilverfilter.this.getPart(HaloSilverfilter.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.5 );
				HaloSilverfilter.this.getPart(HaloSilverfilter.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.5 );
				HaloSilverfilter.this.getPart(HaloSilverfilter.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.5 );
				HaloSilverfilter.this.getPart(HaloSilverfilter.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.25 );
//			}
				}
			}
		);

		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					//return to original pose
				}
			},
			new Runnable() {
				public void run() {
					HaloSilverfilter.this.getPart(HaloSilverfilter.Part.Neck).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					HaloSilverfilter.this.getPart(HaloSilverfilter.Part.Torso).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					HaloSilverfilter.this.getPart(HaloSilverfilter.Part.RightUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					HaloSilverfilter.this.getPart(HaloSilverfilter.Part.LeftUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					HaloSilverfilter.this.getPart(HaloSilverfilter.Part.RightTopWing).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					HaloSilverfilter.this.getPart(HaloSilverfilter.Part.RightBottomWing).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					HaloSilverfilter.this.getPart(HaloSilverfilter.Part.LeftTopWing).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					HaloSilverfilter.this.getPart(HaloSilverfilter.Part.LeftBottomWing).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
				}
			}
		);

	}

	public void FlapWings( final Number Duration) {
		 final edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Double> TurnAmount= new edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Double>( 0.1 );
		for (int index1 = 0; index1 < (Duration.doubleValue() * 5.0); index1 = index1 + 1) {
			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						HaloSilverfilter.this.getPart(HaloSilverfilter.Part.RightTopWing).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(TurnAmount.value), 0.1 );
					}
				},
				new Runnable() {
					public void run() {
						HaloSilverfilter.this.getPart(HaloSilverfilter.Part.RightBottomWing).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(TurnAmount.value), 0.1 );
					}
				},
				new Runnable() {
					public void run() {
						HaloSilverfilter.this.getPart(HaloSilverfilter.Part.LeftTopWing).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(TurnAmount.value), 0.1 );
					}
				},
				new Runnable() {
					public void run() {
						HaloSilverfilter.this.getPart(HaloSilverfilter.Part.LeftBottomWing).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(TurnAmount.value), 0.1 );
					}
				}
			);

			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						HaloSilverfilter.this.getPart(HaloSilverfilter.Part.RightTopWing).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(TurnAmount.value), 0.1 );
					}
				},
				new Runnable() {
					public void run() {
						HaloSilverfilter.this.getPart(HaloSilverfilter.Part.RightBottomWing).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(TurnAmount.value), 0.1 );
					}
				},
				new Runnable() {
					public void run() {
						HaloSilverfilter.this.getPart(HaloSilverfilter.Part.LeftTopWing).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(TurnAmount.value), 0.1 );
					}
				},
				new Runnable() {
					public void run() {
						HaloSilverfilter.this.getPart(HaloSilverfilter.Part.LeftBottomWing).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(TurnAmount.value), 0.1 );
					}
				}
			);

		}

	}
}
