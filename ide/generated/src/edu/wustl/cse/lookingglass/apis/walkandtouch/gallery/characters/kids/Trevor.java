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
package edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.characters.kids;

public class Trevor extends edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.GalleryPerson { 
	edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Boolean> OnBoard= new edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Boolean>( false );  
	public Trevor() {
		super( "kids/Trevor" );
}
	public enum Part {
		RightFoot( "upperTorso", "lowerTorso", "hips", "rightUpperLeg", "rightLowerLeg", "rightFoot" ),
		RightLowerLeg( "upperTorso", "lowerTorso", "hips", "rightUpperLeg", "rightLowerLeg" ),
		RightUpperLeg( "upperTorso", "lowerTorso", "hips", "rightUpperLeg" ),
		LeftFoot( "upperTorso", "lowerTorso", "hips", "leftUpperLeg", "leftLowerLeg", "leftFoot" ),
		LeftLowerLeg( "upperTorso", "lowerTorso", "hips", "leftUpperLeg", "leftLowerLeg" ),
		LeftUpperLeg( "upperTorso", "lowerTorso", "hips", "leftUpperLeg" ),
		Hips( "upperTorso", "lowerTorso", "hips" ),
		LowerTorso( "upperTorso", "lowerTorso" ),
		RightHand( "upperTorso", "rightUpperArm", "rightLowerArm", "rightHand" ),
		RightLowerArm( "upperTorso", "rightUpperArm", "rightLowerArm" ),
		RightUpperArm( "upperTorso", "rightUpperArm" ),
		LeftHand( "upperTorso", "leftUpperArm", "leftLowerArm", "leftHand" ),
		LeftLowerArm( "upperTorso", "leftUpperArm", "leftLowerArm" ),
		LeftUpperArm( "upperTorso", "leftUpperArm" ),
		Hat( "upperTorso", "neck", "head", "hat" ),
		RightEar( "upperTorso", "neck", "head", "rightEar" ),
		LeftEar( "upperTorso", "neck", "head", "leftEar" ),
		Head( "upperTorso", "neck", "head" ),
		Neck( "upperTorso", "neck" ),
		UpperTorso( "upperTorso" );
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

	public void Hug( final edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel Who) {
		this.walkTo( Who );
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					Trevor.this.moveToward( (Trevor.this.getDistanceTo(Who) + 0.5), Who );
				}
			},
			new Runnable() {
				public void run() {
					Trevor.this.getPart(Trevor.Part.RightUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.03) );
				}
			},
			new Runnable() {
				public void run() {
					Trevor.this.getPart(Trevor.Part.RightLowerLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.03) );
				}
			},
			new Runnable() {
				public void run() {
					Trevor.this.getPart(Trevor.Part.RightUpperArm).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.15) );
				}
			},
			new Runnable() {
				public void run() {
					Trevor.this.getPart(Trevor.Part.RightLowerArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25) );
				}
			},
			new Runnable() {
				public void run() {
					Trevor.this.getPart(Trevor.Part.LeftUpperArm).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.15) );
				}
			},
			new Runnable() {
				public void run() {
					Trevor.this.getPart(Trevor.Part.LeftLowerArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25) );
				}
			}
		);

		this.delay(3.0);
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					Trevor.this.move( org.alice.apis.moveandturn.MoveDirection.BACKWARD, 0.5, 0.5 );
				}
			}
		);

		this.standUp( false );
	}

	public void Shove( final edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel Who) {
		this.walkTo( Who );
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					Trevor.this.touch( Trevor.this.getPart(Trevor.Part.RightUpperArm), org.alice.apis.moveandturn.MoveDirection.FORWARD, edu.wustl.cse.lookingglass.apis.walkandtouch.TouchPart.RIGHT_HAND, 0.1, 0.7 );
				}
			},
			new Runnable() {
				public void run() {
					Trevor.this.touch( Trevor.this.getPart(Trevor.Part.LeftUpperArm), org.alice.apis.moveandturn.MoveDirection.FORWARD, edu.wustl.cse.lookingglass.apis.walkandtouch.TouchPart.LEFT_HAND, 0.1, 0.7 );
				}
			}
		);

		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					Trevor.this.touch( Who, org.alice.apis.moveandturn.MoveDirection.FORWARD, edu.wustl.cse.lookingglass.apis.walkandtouch.TouchPart.RIGHT_HAND, 0.1, 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					Trevor.this.touch( Who, org.alice.apis.moveandturn.MoveDirection.FORWARD, edu.wustl.cse.lookingglass.apis.walkandtouch.TouchPart.LEFT_HAND, 0.1, 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					Trevor.this.moveToward( (Trevor.this.getDistanceTo(Who) + 0.5), Who, 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					// DoInOrder { 
				Trevor.this.delay(0.1);
				Who.move( org.alice.apis.moveandturn.MoveDirection.BACKWARD, 0.5, 0.25 );
//			}
				}
			}
		);

		this.standUp( false );
	}

	public void Cheer( final Number HowManyTimes) {
		for (int index1 = 0; index1 < HowManyTimes.intValue(); index1 = index1 + 1) {
			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						Trevor.this.getPart(Trevor.Part.RightUpperArm).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Trevor.this.getPart(Trevor.Part.LeftUpperArm).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Trevor.this.getPart(Trevor.Part.RightUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Trevor.this.getPart(Trevor.Part.RightLowerLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Trevor.this.getPart(Trevor.Part.LeftUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Trevor.this.getPart(Trevor.Part.LeftLowerLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Trevor.this.move( org.alice.apis.moveandturn.MoveDirection.DOWN, 0.15, 0.5 );
					}
				}
			);

			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						Trevor.this.touch( Trevor.this.getPart(Trevor.Part.Hat), org.alice.apis.moveandturn.MoveDirection.LEFT, edu.wustl.cse.lookingglass.apis.walkandtouch.TouchPart.LEFT_HAND, 0.0, 0.25 );
					}
				},
				new Runnable() {
					public void run() {
						Trevor.this.getPart(Trevor.Part.RightUpperArm).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.25 );
					}
				},
				new Runnable() {
					public void run() {
						Trevor.this.getPart(Trevor.Part.RightLowerArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.25 );
					}
				},
				new Runnable() {
					public void run() {
						Trevor.this.getPart(Trevor.Part.RightUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.25 );
					}
				},
				new Runnable() {
					public void run() {
						Trevor.this.getPart(Trevor.Part.RightLowerLeg).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.25 );
					}
				},
				new Runnable() {
					public void run() {
						Trevor.this.getPart(Trevor.Part.LeftUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.25 );
					}
				},
				new Runnable() {
					public void run() {
						Trevor.this.getPart(Trevor.Part.LeftLowerLeg).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.25 );
					}
				},
				new Runnable() {
					public void run() {
						Trevor.this.move( org.alice.apis.moveandturn.MoveDirection.UP, 0.75, 0.25 );
					}
				}
			);

			this.move( org.alice.apis.moveandturn.MoveDirection.DOWN, 0.5, 0.25 );
			this.standUp( false, 0.25 );
		}

	}

	public void Dance( final Number HowManyTimes) {
		for (int index1 = 0; index1 < HowManyTimes.intValue(); index1 = index1 + 1) {
			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						Trevor.this.getPart(Trevor.Part.UpperTorso).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Trevor.this.getPart(Trevor.Part.UpperTorso).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Trevor.this.getPart(Trevor.Part.LowerTorso).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Trevor.this.getPart(Trevor.Part.LowerTorso).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Trevor.this.getPart(Trevor.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Trevor.this.getPart(Trevor.Part.RightUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Trevor.this.getPart(Trevor.Part.RightUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Trevor.this.getPart(Trevor.Part.LeftUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Trevor.this.getPart(Trevor.Part.LeftUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.5 );
					}
				}
			);

			this.delay(0.25);
			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						Trevor.this.getPart(Trevor.Part.UpperTorso).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Trevor.this.getPart(Trevor.Part.UpperTorso).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Trevor.this.getPart(Trevor.Part.LowerTorso).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Trevor.this.getPart(Trevor.Part.LowerTorso).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Trevor.this.getPart(Trevor.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Trevor.this.getPart(Trevor.Part.RightUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Trevor.this.getPart(Trevor.Part.RightUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Trevor.this.getPart(Trevor.Part.RightLowerArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Trevor.this.getPart(Trevor.Part.LeftUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Trevor.this.getPart(Trevor.Part.LeftUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Trevor.this.getPart(Trevor.Part.LeftLowerArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.5 );
					}
				}
			);

			this.standUp( false, 0.5 );
			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						Trevor.this.getPart(Trevor.Part.UpperTorso).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Trevor.this.getPart(Trevor.Part.UpperTorso).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Trevor.this.getPart(Trevor.Part.LowerTorso).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Trevor.this.getPart(Trevor.Part.LowerTorso).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Trevor.this.getPart(Trevor.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Trevor.this.getPart(Trevor.Part.RightUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Trevor.this.getPart(Trevor.Part.RightUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Trevor.this.getPart(Trevor.Part.LeftUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Trevor.this.getPart(Trevor.Part.LeftUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.5 );
					}
				}
			);

			this.delay(0.25);
			org.alice.virtualmachine.DoTogether.invokeAndWait(
				new Runnable() {
					public void run() {
						Trevor.this.getPart(Trevor.Part.UpperTorso).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Trevor.this.getPart(Trevor.Part.UpperTorso).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Trevor.this.getPart(Trevor.Part.LowerTorso).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Trevor.this.getPart(Trevor.Part.LowerTorso).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Trevor.this.getPart(Trevor.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Trevor.this.getPart(Trevor.Part.RightUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Trevor.this.getPart(Trevor.Part.RightUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Trevor.this.getPart(Trevor.Part.RightLowerArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Trevor.this.getPart(Trevor.Part.LeftUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Trevor.this.getPart(Trevor.Part.LeftUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.5 );
					}
				},
				new Runnable() {
					public void run() {
						Trevor.this.getPart(Trevor.Part.LeftLowerArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.5 );
					}
				}
			);

			this.standUp( false, 0.5 );
		}

	}
}
